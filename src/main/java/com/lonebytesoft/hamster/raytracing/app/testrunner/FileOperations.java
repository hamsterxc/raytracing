package com.lonebytesoft.hamster.raytracing.app.testrunner;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

final class FileOperations {

    public static void delete(final String name) throws IOException {
        final Path path = Paths.get(name);
        delete(path);
    }

    public static void delete(final Path path) throws IOException {
        if(Files.isDirectory(path)) {
            Files.walk(path, 1)
                    .forEach(child -> {
                        try {
                            if(!Files.isSameFile(child, path)) {
                                delete(child);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
            Files.delete(path);
        } else {
            Files.deleteIfExists(path);
        }
    }

    public static OutputStream obtainOutputStream(final String fileName) throws IOException {
        if((fileName == null) || (fileName.length() == 0)) {
            return new SystemOutWrapper();
        } else {
            createFileDirectories(fileName);
            return new BufferedOutputStream(new FileOutputStream(fileName, true));
        }
    }

    private static void createFileDirectories(final String path) throws IOException {
        final Path filePath = Paths.get(path);
        filePath.normalize();

        final Path parentPath = filePath.getParent();
        if(parentPath != null) {
            Files.createDirectories(parentPath);
        }
    }

    public static void overwrite(final Path target, final String content) throws IOException {
        delete(target);
        try(final OutputStream outputStream = obtainOutputStream(target.toString())) {
            outputStream.write(content.getBytes());
        }
    }

    public static int runProcess(final List<String> command, final String workingPath,
                                 final OutputStream outputStream) throws IOException {
        final ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(workingPath == null ? null : new File(workingPath));
        processBuilder.redirectErrorStream(true);
        final Process process = processBuilder.start();

        try(final InputStream inputStream = new BufferedInputStream(process.getInputStream())) {
            int read;
            while((read = inputStream.read()) != -1) {
                outputStream.write(read);
            }
        }

        final int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }

        return exitCode;
    }

    public static void checkExitCode(final int exitCode, final String error) {
        if(exitCode != 0) {
            throw new RuntimeException(error);
        }
    }

    public static String checkExitCode(final int exitCode, final String error, final String output,
                                       final OutputStream log) throws IOException {
        if(exitCode == 0) {
            return output;
        } else {
            log.write(output.getBytes());
            throw new RuntimeException(error);
        }
    }

    private static class SystemOutWrapper extends OutputStream {

        @Override
        public void write(int b) {
            System.out.write(b);
        }

    }

}
