package com.lonebytesoft.hamster.raytracing.app.testrunner;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.FeatureNotImplementedException;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ClassFileDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.factory.CodeBuilderFactory;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.factory.XmlParserFactory;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.picture.builder.PictureBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.picture.factory.PictureBuilderFactory;
import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.color.ColorCalculator;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates2d;
import com.lonebytesoft.hamster.raytracing.format.reader.BmpReader;
import com.lonebytesoft.hamster.raytracing.format.writer.BmpWriter;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import com.lonebytesoft.hamster.raytracing.picture.PictureMutable;
import com.lonebytesoft.hamster.raytracing.picture.PictureMutableFactory;
import com.lonebytesoft.hamster.raytracing.util.Utils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunner {

    private static final int RUNS_DEFAULT = 1;

    private static final int PICTURE_DIMENSIONS = 2;

    private static final String CODE_PACKAGE = "com.lonebytesoft.hamster.raytracing.app";
    private static final String CODE_PATH = "src/main/java/" + CODE_PACKAGE.replaceAll("\\.", "/");

    private static final String LOG_FILE_NAME = "raytracing.log";

    private String base;
    private String target;

    private int runs = RUNS_DEFAULT;
    private boolean isNoRebuild = false;
    private String artifacts;
    private boolean isVerbose = false;

    private String logOwn;
    private String logGit;
    private String logMaven;
    private String logJava;

    public void run(final List<String> definitions) throws IOException {
        String basePath = null;
        String targetPath = null;
        final boolean isRegressionFailed;
        try (
                final OutputStream logStreamOwn = FileOperations.obtainOutputStream(logOwn);
                final OutputStream logStreamGit = FileOperations.obtainOutputStream(logGit);
                final OutputStream logStreamMaven = FileOperations.obtainOutputStream(logMaven);
                final OutputStream logStreamJava = FileOperations.obtainOutputStream(logJava);
        ) {
            basePath = isEmpty(base) ? null : gitClone(base, "base", logStreamGit, logStreamOwn);
            targetPath = isEmpty(target) ? "." : gitClone(target, "target", logStreamGit, logStreamOwn);

            final String baseCommitHash = isEmpty(base) ? null : ToolOperations.gitGetCommitHash(basePath, logStreamGit);
            final String targetCommitHash = isEmpty(target) ? null : ToolOperations.gitGetCommitHash(targetPath, logStreamGit);

            final boolean isTargetInPlace = isEmpty(target) && isNoRebuild;

            final XmlParser xmlParser = new XmlParserFactory().obtainXmlParser();
            final Map<String, SceneDefinition> sceneDefinitions = new HashMap<>();
            final Map<String, String> codeBase = new HashMap<>();
            final Map<String, String> codeTarget = new HashMap<>();
            for(final String definition : definitions) {
                final SceneDefinition sceneDefinition = buildSceneDefinition(definition, xmlParser);
                final int pictureDimensions = sceneDefinition.getPictureDimensions();
                if(pictureDimensions != PICTURE_DIMENSIONS) {
                    throw new RuntimeException("Definition '" + definition + "': unsupported picture dimensions " + pictureDimensions);
                }
                sceneDefinitions.put(definition, sceneDefinition);

                final String sceneName = getSceneName(definition);
                final String pictureName = getPictureName(sceneName);

                if(!isEmpty(base)) {
                    final String code = buildCode("base", definition,
                            baseCommitHash, sceneDefinition, sceneName, pictureName);
                    codeBase.put(definition, code);
                }
                if(!isTargetInPlace) {
                    final String code = buildCode("target", definition,
                            targetCommitHash, sceneDefinition, sceneName, pictureName);
                    codeTarget.put(definition, code);
                }
            }

            if(!isEmpty(artifacts)) {
                Files.createDirectories(Paths.get(artifacts));
            }

            final List<DefinitionRunResult> definitionRunResults = new ArrayList<>();
            final int count = definitions.size();
            int index = 0;
            for(final String definition : definitions) {
                index++;
                log("Running test " + index + "/" + count + "... " + definition, logStreamOwn);

                final String sceneName = getSceneName(definition);
                final String pictureName = getPictureName(sceneName);

                Picture<Coordinates2d> reference = null;
                TestPictureStatus testPictureStatus = TestPictureStatus.IDENTICAL;

                long timeBase = 0;
                if(!isEmpty(base)) {
                    final String code = codeBase.get(definition);
                    saveCodeArtifact("base", sceneName, code);

                    final TestRunResult<Coordinates2d> testRunResult = runTest(
                            "base", basePath, sceneName, pictureName,
                            code, reference, logStreamMaven, logStreamJava);
                    reference = testRunResult.getPicture();
                    testPictureStatus = testRunResult.getPictureStatus();
                    timeBase = testRunResult.getTime();
                }

                final String type = isEmpty(base) ? "" : "target";
                final TestRunResult<Coordinates2d> testRunResult;
                if(isTargetInPlace) {
                    testRunResult = runTest(sceneDefinitions.get(definition), type, sceneName, reference);
                } else {
                    final String code = codeTarget.get(definition);
                    saveCodeArtifact(type, sceneName, code);
                    testRunResult = runTest(
                            type, targetPath, sceneName, pictureName,
                            code, reference, logStreamMaven, logStreamJava);
                }
                switch(testPictureStatus) {
                    case DIFFER:
                        testPictureStatus = testRunResult.getPictureStatus() == TestPictureStatus.INVALID
                                ? TestPictureStatus.INVALID : TestPictureStatus.DIFFER;
                        break;

                    case IDENTICAL:
                        testPictureStatus = testRunResult.getPictureStatus();
                        break;
                }

                definitionRunResults.add(new DefinitionRunResult(
                        sceneName, timeBase, testRunResult.getTime(), testPictureStatus));
            }

            if(!isEmpty(artifacts) && isTargetInPlace) {
                Files.copy(
                        Paths.get(LOG_FILE_NAME),
                        Paths.get(artifacts, (isEmpty(base) ? "" : "target_") + LOG_FILE_NAME),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            isRegressionFailed = definitionRunResults.stream()
                    .anyMatch(result -> result.getPictureStatus() != TestPictureStatus.IDENTICAL);
            logResults(definitionRunResults, logStreamOwn);
        } finally {
            if(!isEmpty(base) && !isEmpty(basePath)) {
                FileOperations.delete(basePath);
            }
            if(!isEmpty(target) && !isEmpty(targetPath)) {
                FileOperations.delete(targetPath);
            }
        }

        System.exit(isRegressionFailed ? 1 : 0);
    }
    
    private String gitClone(final String qualifier, final String name,
                            final OutputStream logStreamGit, final OutputStream logStreamOwn) throws IOException {
        log("Cloning " + name + " @ " + qualifier + " ...", logStreamOwn);
        return ToolOperations.gitClone(qualifier, name, logStreamGit);
    }

    private String getSceneName(final String definition) {
        final String fileName = Paths.get(definition).getFileName().toString();
        final int periodPosition = fileName.indexOf(".");
        return periodPosition == -1 ? fileName : fileName.substring(0, periodPosition);
    }

    private String getPictureName(final String sceneName) {
        return sceneName + ".bmp";
    }

    private String getRunSpecificNamePart(final String type, final String sceneName, final int run) {
        return (isEmpty(type) ? "" : type + "_") + sceneName + (runs > 1 ? "_" + run : "");
    }

    private SceneDefinition buildSceneDefinition(final String definitionFileName,
                                                 final XmlParser xmlParser) throws IOException {
        final Document document;
        try(final InputStream inputStream = new BufferedInputStream(new FileInputStream(definitionFileName))) {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
        } catch (SAXException | ParserConfigurationException e) {
            throw new IOException(e);
        }

        return xmlParser.parse(document);
    }

    private String buildCode(final Commit commit, final SceneDefinition sceneDefinition,
                             final String name, final String picture) {
        final ExpressionBuilder<ClassFileDefinition> classFileBuilder = new CodeBuilderFactory().build(commit);

        final ClassFileDefinition classFileDefinition = new ClassFileDefinition(sceneDefinition, CODE_PACKAGE, name, picture);
        return classFileBuilder.build(classFileDefinition);
    }

    private String buildCode(final String type, final String definition,
                             final String commitHash, final SceneDefinition sceneDefinition,
                             final String name, final String picture) {
        final Commit commit = Commit.find(commitHash);
        try {
            return buildCode(commit, sceneDefinition, name, picture);
        } catch (FeatureNotImplementedException e) {
            throw new RuntimeException("Feature not implemented in commit " + commit + " for " + type
                    + ", definition '" + definition + "': " + e.getFeature(), e);
        }
    }

    private void saveCodeArtifact(final String type, final String sceneName, final String code) throws IOException {
        if(!isEmpty(artifacts)) {
            final Path classArtifact = Paths.get(artifacts, (isEmpty(type) ? "" : type + "_") + sceneName + ".java");
            FileOperations.overwrite(classArtifact, code);
        }
    }

    private TestRunResult<Coordinates2d> runTest(
            final SceneDefinition sceneDefinition, final String type, final String sceneName,
            final Picture<Coordinates2d> previous) throws IOException {
        Picture<Coordinates2d> reference = previous;
        TestPictureStatus pictureStatus = TestPictureStatus.IDENTICAL;
        long timeTotal = 0;
        for(int run = 0; run < runs; run++) {
            final PictureBuilder<?, Coordinates2d> pictureBuilder =
                    new PictureBuilderFactory().obtainPictureBuilder(sceneDefinition.getPictureDimensions());

            final long time = System.nanoTime();
            final Picture<Coordinates2d> picture = pictureBuilder.buildPicture(sceneDefinition);
            timeTotal += System.nanoTime() - time;
            final Picture<Coordinates2d> pictureEmulated = emulateWriteRead(picture);

            if(reference == null) {
                reference = pictureEmulated;
            } else if(pictureStatus != TestPictureStatus.DIFFER) {
                if(!reference.isEqual(pictureEmulated)) {
                    pictureStatus = TestPictureStatus.DIFFER;
                }
            }

            if(!isEmpty(artifacts)) {
                final String name = getRunSpecificNamePart(type, sceneName, run);
                final Path pictureFilePath = Paths.get(artifacts, getPictureName(name));
                FileOperations.delete(pictureFilePath);
                try(final OutputStream outputStream = FileOperations.obtainOutputStream(pictureFilePath.toString())) {
                    new BmpWriter().write(pictureEmulated, outputStream);
                }
            }
        }

        return new TestRunResult<>(timeTotal / runs, pictureStatus, reference);
    }

    private <T extends Coordinates<T>> Picture<T> emulateWriteRead(final Picture<T> picture) {
        PictureMutable<T> transformed = null;
        for(final T coordinate : Utils.obtainIterable(picture.getAllPixelCoordinates())) {
            if(transformed == null) {
                transformed = PictureMutableFactory.obtainPictureMutable(coordinate);
            }

            final Color color = picture.getPixelColor(coordinate);
            transformed.setPixelColor(coordinate, new Color(
                    ColorCalculator.colorByteToComponent(ColorCalculator.colorComponentToByte(color.getRed())),
                    ColorCalculator.colorByteToComponent(ColorCalculator.colorComponentToByte(color.getGreen())),
                    ColorCalculator.colorByteToComponent(ColorCalculator.colorComponentToByte(color.getBlue()))
            ));
        }

        return transformed;
    }

    private TestRunResult<Coordinates2d> runTest(
            final String type, final String project, final String sceneName, final String pictureName, final String code,
            final Picture<Coordinates2d> previous, final OutputStream logStreamMaven, final OutputStream logStreamJava)
            throws IOException {
        final Path classFile = Paths.get(project, CODE_PATH, sceneName + ".java");
        FileOperations.overwrite(classFile, code);

        final String classPath = ToolOperations.mavenBuild(project, logStreamMaven);
        FileOperations.delete(classFile);
        final String className = CODE_PACKAGE + "." + sceneName;
        Picture<Coordinates2d> reference = previous;
        TestPictureStatus pictureStatus = TestPictureStatus.IDENTICAL;
        long timeTotal = 0;
        for(int run = 0; run < runs; run++) {
            final long time = System.nanoTime();
            ToolOperations.java(classPath, className, Collections.emptyMap(), null, logStreamJava);
            timeTotal += System.nanoTime() - time;

            if(pictureStatus != TestPictureStatus.INVALID) {
                final Picture<Coordinates2d> picture = readPicture(pictureName);
                if(picture == null) {
                    pictureStatus = TestPictureStatus.INVALID;
                } else {
                    if(reference == null) {
                        reference = picture;
                    } else if(pictureStatus != TestPictureStatus.DIFFER) {
                        if(!reference.isEqual(picture)) {
                            pictureStatus = TestPictureStatus.DIFFER;
                        }
                    }
                }
            }

            if(isEmpty(artifacts)) {
                FileOperations.delete(LOG_FILE_NAME);
                FileOperations.delete(pictureName);
            } else {
                final String name = getRunSpecificNamePart(type, sceneName, run);

                final Path logFilePath = Paths.get(artifacts, name + ".log");
                Files.move(Paths.get(LOG_FILE_NAME), logFilePath, StandardCopyOption.REPLACE_EXISTING);

                final Path pictureFilePath = Paths.get(artifacts, getPictureName(name));
                Files.move(Paths.get(pictureName), pictureFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        return new TestRunResult<>(timeTotal / runs, pictureStatus, reference);
    }

    private Picture<Coordinates2d> readPicture(final String fileName) {
        try(final InputStream inputStream = new BufferedInputStream(new FileInputStream(fileName))) {
            return new BmpReader().read(inputStream);
        } catch (IOException e) {
            return null;
        }
    }

    private void logResults(final List<DefinitionRunResult> results, final OutputStream logStreamOwn) {
        final RunResultsLogger runResultsLogger = new RunResultsLogger(message -> {
            try {
                output(message, logStreamOwn);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        runResultsLogger.setVerbose(isVerbose);
        runResultsLogger.setComparing(!isEmpty(base));
        runResultsLogger.setPictureRegression(!isEmpty(base) || (runs > 1));

        runResultsLogger.log(results);
    }

    private void log(final String message, final OutputStream logStreamOwn) throws IOException {
        if(isVerbose) {
            output(message, logStreamOwn);
        }
    }

    private void output(final String message, final OutputStream logStreamOwn) throws IOException {
        if(logStreamOwn != null) {
            logStreamOwn.write((message + "\n").getBytes());
        }
    }

    private boolean isEmpty(final String s) {
        return (s == null) || (s.length() == 0);
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public void setNoRebuild(boolean noRebuild) {
        isNoRebuild = noRebuild;
    }

    public void setArtifacts(String artifacts) {
        this.artifacts = artifacts;
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }

    public void setLogOwn(String logOwn) {
        this.logOwn = logOwn;
    }

    public void setLogGit(String logGit) {
        this.logGit = logGit;
    }

    public void setLogMaven(String logMaven) {
        this.logMaven = logMaven;
    }

    public void setLogJava(String logJava) {
        this.logJava = logJava;
    }

    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.err.println("No paths specified");
            System.exit(1);
            return;
        }

        final TestRunner testRunner = new TestRunner();

        testRunner.setBase(System.getProperty("raytracing.testrunner.base"));
        testRunner.setTarget(System.getProperty("raytracing.testrunner.target"));

        final String runs = System.getProperty("raytracing.testrunner.runs", null);
        if(runs != null) {
            try {
                final int runsValue = Integer.parseInt(runs);
                if(runsValue >= 1) {
                    testRunner.setRuns(runsValue);
                } else {
                    errorRunsDefault(testRunner, runs);
                }
            } catch (NumberFormatException e) {
                errorRunsDefault(testRunner, runs);
            }
        }

        testRunner.setNoRebuild(Boolean.parseBoolean(System.getProperty("raytracing.testrunner.norebuild")));
        testRunner.setArtifacts(System.getProperty("raytracing.testrunner.artifacts"));
        testRunner.setVerbose(Boolean.parseBoolean(System.getProperty("raytracing.testrunner.verbose")));

        testRunner.setLogGit(System.getProperty("raytracing.testrunner.loggit"));
        testRunner.setLogMaven(System.getProperty("raytracing.testrunner.logmaven"));
        testRunner.setLogJava(System.getProperty("raytracing.testrunner.logjava"));

        testRunner.run(Arrays.asList(args));
    }

    private static void errorRunsDefault(final TestRunner testRunner, final String runs) {
        System.err.println("Invalid runs count: " + runs + ", defaulting to " + RUNS_DEFAULT);
        testRunner.setRuns(RUNS_DEFAULT);
    }

}
