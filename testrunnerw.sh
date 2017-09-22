#!/bin/sh

#set -xeu

function usage() {
    printf "Test Runner wrapper"

    echo
    printf "usage:\n"
    printf "\t$0 [-b|--base <base>] [-t|--target <target>] [-r|--runs <runs-count>] [--no-rebuild] [-a|--artifacts <path>] [-v|--verbose] [-lg|--log-git <path>] [-lm|--log-maven <path>] [-lj|--log-java <path>] <paths-to-process>\n"
    printf "\t$0 [-h|--help]\n"

    argument_format="%-5s%-14s%s\n"
    echo
    printf "${argument_format}" "-b" "--base" "Base codebase to compare against, just statistics will be generated if absent"
    printf "${argument_format}" "-t" "--target" "Codebase to calculate statistics for, current working copy if absent"
    printf "${argument_format}" "-r" "--runs" "Number of times for each test to run, 1 by default. Average time will be taken"
    printf "${argument_format}" "" "--no-rebuild" "Do not rebuild the target-version project for each path. Ignored if non-working-copy target specified"
    printf "${argument_format}" "-a" "--artifacts" "Collect generated artifacts to specified path, files will be removed if absent"
    printf "${argument_format}" "-v" "--verbose" "Verbose output"
    printf "${argument_format}" "-lg" "--log-git" "Path to write Git logs to, stdout if absent"
    printf "${argument_format}" "-lm" "--log-maven" "Path to write Maven logs to, stdout if absent"
    printf "${argument_format}" "-lj" "--log-java" "Path to write Java logs to, stdout if absent"
    printf "${argument_format}" "-h" "--help" "Display this help message"
}

base=""
target=""
runs=1
no_rebuild="false"
artifacts=""
verbose="false"
log_git=""
log_maven=""
log_java=""
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

        --no-rebuild)
            no_rebuild="true"
            shift
            ;;

        -a|--artifacts)
            artifacts=$2
            shift
            shift
            ;;

        -v|--verbose)
            verbose="true"
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
            
        -lj|--log-java)
            log_java=$2
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

if [[ "${help}" = "1" ]]; then
    usage
    exit 0
fi

if [[ "${no_rebuild}" == "true" && -n ${target} ]]; then
    echo "--no-rebuild flag will be ignored for non-working-copy target" 1>&2
fi

if [[ "${verbose}" == "true" ]]; then
    echo "Building main project..."
fi
if [[ -z ${log_maven} ]]; then
    mvn clean package dependency:copy-dependencies -DoutputDirectory=target
else
    if [[ ! -f ${log_maven} ]]; then
        mkdir -p $(dirname ${log_maven})
        touch ${log_maven}
    fi
    mvn clean package dependency:copy-dependencies -DoutputDirectory=target 2>&1 >${log_maven}
fi

classpath_copy="false"
if [[ "${no_rebuild}" == "false" && -z ${target} ]]; then
    classpath_copy="true"
fi

if [[ "${classpath_copy}" == "true" ]]; then
    cp="cp."$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | head -c 8)
    rm -rf ${cp}
    mkdir ${cp}
    cp target/*.jar ${cp}
    classpath=${cp}"/*"
else
    classpath="target/*"
fi

java -cp "${classpath}" \
    -Draytracing.testrunner.base=${base} \
    -Draytracing.testrunner.target=${target} \
    -Draytracing.testrunner.runs=${runs} \
    -Draytracing.testrunner.norebuild=${no_rebuild} \
    -Draytracing.testrunner.artifacts=${artifacts} \
    -Draytracing.testrunner.verbose=${verbose} \
    -Draytracing.testrunner.loggit=${log_git} \
    -Draytracing.testrunner.logmaven=${log_maven} \
    -Draytracing.testrunner.logjava=${log_java} \
    com.lonebytesoft.hamster.raytracing.app.testrunner.TestRunner \
    ${paths[@]}

if [[ "${classpath_copy}" == "true" ]]; then
    rm -rf ${cp}
fi
