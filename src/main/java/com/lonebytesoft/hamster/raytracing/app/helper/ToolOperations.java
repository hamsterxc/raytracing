package com.lonebytesoft.hamster.raytracing.app.helper;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ToolOperations {

    private static final char[] RANDOM_ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    private static final String OS = System.getProperty("os.name");
    private static final boolean IS_WINDOWS = OS.startsWith("Windows");
    private static final boolean IS_LINUX = OS.startsWith("Linux");

    private static final String MAVEN_OUTPUT_PATH = "target";
    private static final String MAVEN_ARTIFACT_NAME = "raytracing-1.0-SNAPSHOT.jar";

    // git log -n 1 --pretty=format:"%H"
    public static String gitGetCommitHash(final String project, final OutputStream log) throws IOException {
        final OutputStream outputStream = new StringBuilderWrapper();
        final int exitCodeClone = FileOperations.runProcess(Arrays.asList(
                "git", "log",
                "-n", "1",
                "--pretty=format:%H"
        ), project, outputStream);
        return FileOperations.checkExitCode(exitCodeClone, "latest commit hash (git log) failed",
                outputStream.toString(), log);
    }

    // git log --pretty=format:"%H %P"
    public static String gitGetLog(final String project, final OutputStream log) throws IOException {
        final OutputStream outputStream = new StringBuilderWrapper();
        final int exitCodeClone = FileOperations.runProcess(Arrays.asList(
                "git", "log",
                "--pretty=format:%H %P"
        ), project, outputStream);
        return FileOperations.checkExitCode(exitCodeClone, "git log failed", outputStream.toString(), log);
    }

    // git clone . ${name}"."${rand} && cd ${name}"."${rand} && git reset --hard ${qualifier}
    public static String gitClone(final String qualifier, final String name, final OutputStream log) throws IOException {
        final String path = name + "." + obtainRandomString(8);
        FileOperations.delete(path);

        final int exitCodeClone = FileOperations.runProcess(Arrays.asList(
                "git", "clone",
                FileOperations.CURRENT_DIRECTORY,
                path
        ), null, log);
        FileOperations.checkExitCode(exitCodeClone, "git clone failed");

        final int exitCodeReset = FileOperations.runProcess(Arrays.asList(
                "git", "reset",
                "--hard",
                qualifier
        ), path, log);
        FileOperations.checkExitCode(exitCodeReset, "git reset failed");

        return path;
    }

    // mvn -f ${project}/pom.xml clean package dependency:build-classpath -Dmdep.outputFile="mdep"${rand}
    public static String mavenBuild(final String project, final OutputStream log) throws IOException {
        final String dependenciesOutputFileName = "mdep" + obtainRandomString(8);
        final String dependenciesOutputFile = Paths.get(project, dependenciesOutputFileName).toString();
        FileOperations.delete(dependenciesOutputFile);

        maven(Arrays.asList(
                "-f", mavenGetPom(project),
                "clean",
                "package",
                "dependency:build-classpath", "-Dmdep.outputFile=" + dependenciesOutputFileName
        ), null, log);

        final StringBuilder dependenciesBuilder = new StringBuilder();
        try(final InputStream inputStream = new BufferedInputStream(new FileInputStream(dependenciesOutputFile))) {
            int read;
            while((read = inputStream.read()) != -1) {
                dependenciesBuilder.append((char) (read & 0xFF));
            }
        }
        final String dependencies = dependenciesBuilder.toString();
        FileOperations.delete(dependenciesOutputFile);

        return
                Paths.get(project, MAVEN_OUTPUT_PATH, MAVEN_ARTIFACT_NAME).toString()
                + System.getProperty("path.separator")
                + dependencies;
    }

    private static void maven(final List<String> command, final String workingPath, final OutputStream log) throws IOException {
        final List<String> commandSpecific = new ArrayList<>(command);
        if(IS_WINDOWS) {
            commandSpecific.addAll(0, Arrays.asList("cmd.exe", "/c", "mvn"));
        } else if(IS_LINUX) {
            commandSpecific.add(0, "mvn");
        } else {
            throw new RuntimeException("Unknown OS: " + OS);
        }

        final int exitCode = FileOperations.runProcess(commandSpecific, workingPath, log);
        FileOperations.checkExitCode(exitCode, "maven build failed");
    }

    private static String mavenGetPom(final String project) {
        return Paths.get(project, "pom.xml").toString();
    }

    public static void java(final String classPath, final String className, final Map<String, String> parameters,
                            final String workingPath, final OutputStream log) throws IOException {
        final List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(classPath);
        parameters.forEach((key, value) -> command.add("-D" + key + "=" + value));
        command.add(className);

        final int exitCode = FileOperations.runProcess(command, workingPath, log);
        FileOperations.checkExitCode(exitCode, "java run '" + className + "' failed");
    }

    private static String obtainRandomString(final int length) {
        final StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < length; i++) {
            final int index = (int) Math.floor(Math.random() * RANDOM_ALPHABET.length);
            stringBuilder.append(RANDOM_ALPHABET[index]);
        }
        return stringBuilder.toString();
    }

    private static class StringBuilderWrapper extends OutputStream {

        private final StringBuilder stringBuilder = new StringBuilder();

        @Override
        public void write(int b) {
            stringBuilder.append((char) (b & 0xFF));
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }

    }

}
