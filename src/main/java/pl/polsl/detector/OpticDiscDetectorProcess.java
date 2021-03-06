package pl.polsl.detector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.image.Image;
import org.apache.commons.collections4.ListUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class OpticDiscDetectorProcess implements OpticDiscDetector {
    private final String executablePath;
    private final List<ProgramArgument> programArguments;
    private final File outputFile;

    public OpticDiscDetectorProcess(String executablePath, List<ProgramArgument> programArguments) throws IOException {
        this.executablePath = executablePath;
        this.programArguments = programArguments;
        this.outputFile = File.createTempFile("optic_disc_detection",".png");
    }

    private List<String> processParameters(String filePath) {
        final List<String> arguments =
                new ProgramArguments(
                        ListUtils.sum(
                                programArguments,
                                Arrays.asList(
                                    new StringProgramArgument("-o", outputFile.getAbsolutePath()),
                                    new StringProgramArgument("-i", filePath)
                                )
                        )
                );
        return ListUtils.sum(
                Arrays.asList(executablePath),
                arguments
        );
    }

    @Override
    public DetectionResult detect(String filePath) throws FailedDetectionException {
        try {
            final List<String> arguments = processParameters(filePath);
            final Process process = new ProcessBuilder().command(arguments).start();
            process.waitFor();

            final BufferedReader outputStreamReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            if (process.exitValue() == 0) {
                return DetectionResult.create(
                        new Image(new FileInputStream(outputFile)),
                        readCircle(outputStreamReader)
                );
            } else {
                final BufferedReader errorStreamReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream())
                );

                final String errorMessage = errorStreamReader
                        .lines()
                        .reduce("", (prev, current) -> prev + current);

                throw new FailedDetectionException(errorMessage);
            }
        } catch (IOException | InterruptedException e) {
            throw new FailedDetectionException(e);
        } finally {
            //noinspection ResultOfMethodCallIgnored
            outputFile.delete();
        }
    }

    private Circle readCircle(BufferedReader streamReader) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode node = mapper.readTree(streamReader).get("circle");

        return mapper.treeToValue(node, Circle.class);
    }
}
