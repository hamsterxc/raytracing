#!/bin/sh

#set -xeu

function usage() {
    printf "DEPRECATED: use TestRunner class instead"

    echo
    printf "usage:\n"
    printf "\t$0 [-b|--base <base>] [-t|--target <target>] [-r|--runs <runs-count>] [-c|--collect-files <path>] [-v|--verbose] [-lg|--log-git <path>] [-lm|--log-maven <path>] <paths-to-process>\n"
    printf "\t$0 [-h|--help]\n"

    argument_format="%-3s %-18s %s\n"
    echo
    printf "${argument_format}" "-b" "--base" "Base codebase to compare against, just statistics will be generated if absent"
    printf "${argument_format}" "-t" "--target" "Codebase to calculate statistics for, current working copy if absent"
    printf "${argument_format}" "-r" "--runs" "Number of times for each test to run, 1 by default. Average time will be taken"
    printf "${argument_format}" "-c" "--collect-files" "Collect generated files to specified path, files will be removed if absent"
    printf "${argument_format}" "-v" "--verbose" "Verbose output"
    printf "${argument_format}" "-lg" "--log-git" "Path to write Git logs to, stdout if absent"
    printf "${argument_format}" "-lm" "--log-maven" "Path to write Maven logs to, stdout if absent"
    printf "${argument_format}" "-h" "--help" "Display this help message"
}

base=""
target=""
runs=1
collect_path=""
verbose="0"
log_git=""
log_maven=""
paths=()

help="0"

while [[ $# -gt 0 ]]; do
    case $1 in
        -b|--base)
            base=$2
            shift
            shift
            ;;

        -t|--target)
            target=$2
            shift
            shift
            ;;

        -r|--runs)
            runs=$2
            shift
            shift
            ;;

        -c|--collect-files)
            collect_path=$2
            shift
            shift
            ;;

        -v|--verbose)
            verbose="1"
            shift
            ;;
            
        -lg|--log-git)
            log_git=$2
            shift
            shift
            ;;
            
        -lm|--log-maven)
            log_maven=$2
            shift
            shift
            ;;

        -h|--help)
            help="1"
            shift
            ;;

        *)
            paths+=($1)
            shift
            ;;
    esac
done

if [[ "$help" = "1" ]]; then
    usage
    exit 0
fi

paths_count=${#paths[@]}
if [[ $paths_count -eq 0 ]]; then
    echo "No paths specified" >&2
    usage
    exit 1
fi

function get_rand() {
    echo $(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | head -c 8)
}

function clone() {
    qualifier=$1
    name=$2

    if [[ -n $qualifier ]]; then
        rand=$(get_rand)
        path=${name}"."${rand}"/"

        rm -rf $path
        git clone . $path

        cd $path
        git reset --hard $qualifier
        cd ..

        echo $path
    else
        echo ""
    fi
}

function cleanup() {
    path=$1

    if [[ -n $path ]]; then
        rm -rf $path
    fi
}

function cleanup_all() {
    cleanup "$base_path"
    cleanup "$target_path"
    rm -f $tempfile
    rm -f $jar
}

function create_file() {
    name=$1
    if [[ -n $name ]]; then
        if [[ ! -f $name ]]; then
            mkdir -p $(dirname $name)
            touch $name
        fi
    fi
}

function extract_name() {
    name=$1
    basename=$(basename "$name")
    echo ${basename%.*}
}

function check_fail() {
    code=$1
    message=$2

    if [[ $1 -ne 0 ]]; then
        echo $message >&2
        cleanup_all
        exit 1
    fi
}

function run() {
    name=$1
    type_name=$2
    path=$3
    class_path=$4
    package=$5
    picture=$6
    suffix=$7

    cp ${name}".java" ${path}${class_path}${name}".java"
    mvn -f ${path}"pom.xml" clean package
    check_fail $? "Maven build failure"
    time mvn -f ${path}"pom.xml" exec:java -Dexec.mainClass=${package}"."${name}
    rm -f ${path}${class_path}${name}".java"

    if [[ -n $collect_path ]]; then
        prefix=${collect_path}${type_name}"_"${name}
        mv -f ${picture} ${prefix}${suffix}".bmp"
        mv -f raytracing.log ${prefix}${suffix}".log"
    else
        rm ${picture}
        rm raytracing.log
    fi
}

function get_run_suffix() {
    runs=$1
    run=$2

    if [[ $runs -gt 1 ]]; then
        echo "_"${run}
    fi
    # i raaawwwrrr you! <3
}

function run_command_logging() {
    command=$1
    command_log=$2
    command_tempfile=$3

    if [[ -z $command_tempfile ]]; then
        command_tempfile="/dev/null"
    fi

    if [[ -z $command_log ]]; then
        eval "${command} | tee ${command_tempfile}"
    else
        eval "${command} | tee -a ${command_log} > ${command_tempfile}"
    fi
}

function log_verbose() {
    message=$1
    if [[ "$verbose" = "1" ]]; then
        echo -e $message
    fi
}

if [[ -n $log_git ]]; then
    create_file "${log_git}"
fi
if [[ -n $log_maven ]]; then
    create_file "${log_maven}"
fi

if [[ -n $collect_path ]]; then
    mkdir -p $collect_path
fi

tempfile="temp."$(get_rand)

base_path=""
if [[ -n $base ]]; then
    log_verbose "Cloning base @ ${base} ..."
    run_command_logging "clone ${base} base 2>&1" "${log_git}" $tempfile
    base_path=$(tail -n 1 $tempfile)
fi

target_path=""
if [[ -n $target ]]; then
    log_verbose "Cloning target @ ${target} ..."
    run_command_logging "clone ${target} target 2>&1" "${log_git}" $tempfile
    target_path=$(tail -n 1 $tempfile)
fi

log_verbose "Building jar to generate code..."
run_command_logging "mvn clean package -P fat-jar" "${log_maven}" ""
check_fail $? "Maven build failure"
jar="raytracing.jar"
mv "target/raytracing-1.0-SNAPSHOT-jar-with-dependencies.jar" $jar

code_builder_class="com.lonebytesoft.hamster.raytracing.app.builder.code.XmlCodeBuilder"
generated_class_package="com.lonebytesoft.hamster.raytracing.app"
generated_class_path="src/main/java/com/lonebytesoft/hamster/raytracing/app/"

# todo: no floating-point arithmetic?
TIMEFORMAT='%0R'
path_number=0
times_base=()
times_target=()
comparison=()
for path in "${paths[@]}"; do
    path_number=$(( $path_number + 1 ))
    log_verbose "Running test ${path_number}/${paths_count}... ${path}"

    name=$(extract_name $path)
    java -cp $jar \
        "-Draytracing.xmlcodebuilder.input=$path" \
        "-Draytracing.xmlcodebuilder.package=$generated_class_package" \
        "-Draytracing.xmlcodebuilder.class=$name" \
        "-Draytracing.xmlcodebuilder.picture=${name}.bmp" \
        $code_builder_class > ${name}".java"
    check_fail $? "Java class text build failure"

    time_base=0
    base_diffs=0
    if [[ -n $base ]]; then
        run=1
        while [[ $run -le $runs ]]; do
            suffix=$(get_run_suffix $runs $run)
            command_run_base="run ${name} base \"${base_path}\" ${generated_class_path} ${generated_class_package} ${name}.bmp "${suffix}" 2>&1"
            run_command_logging "$command_run_base" "$log_maven" $tempfile
            time_base_run=$(tail -n 1 $tempfile)
            time_base=$(( $time_base + $time_base_run ))
            run=$(( $run + 1 ))
        done
    fi
    time_base=$(( $time_base / $runs ))
    times_base+=($time_base)

    time_target=0
    run=1
    while [[ $run -le $runs ]]; do
        suffix=$(get_run_suffix $runs $run)
        command_run_target="run ${name} target \"${target_path}\" ${generated_class_path} ${generated_class_package} ${name}.bmp "${suffix}" 2>&1"
        run_command_logging "$command_run_target" "$log_maven" $tempfile
        time_target_run=$(tail -n 1 $tempfile)
        time_target=$(( $time_target + $time_target_run ))
        run=$(( $run + 1 ))
    done
    time_target=$(( $time_target / $runs ))
    times_target+=($time_target)

    if [[ -n $collect_path ]]; then
        mv -f ${name}".java" ${collect_path}${name}".java"
    else
        rm ${name}".java"
    fi
done
unset TIMEFORMAT

log_verbose "Cleaning up..."
cleanup_all

if [[ -n $base ]]; then
    log_verbose "Name\tBase, s\tTarget, s"
    index=0
    while [[ $index -lt ${#paths[@]} ]]; do
        printf "${paths[$index]}\t${times_base[$index]}\t${times_target[$index]}\n"
        index=$(( $index + 1 ))
    done
else
    log_verbose "Name\tTime, s"
    index=0
    while [[ $index -lt ${#paths[@]} ]]; do
        printf "${paths[$index]}\t${times_target[$index]}\n"
        index=$(( $index + 1 ))
    done
fi
