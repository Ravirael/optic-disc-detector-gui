package detector;

import org.apache.commons.collections4.ListUtils;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class OpticDiscDetectorProcess implements OpticDiscDetector {
    private final List<ProgramArgument> programArguments;
    private final File outputFile;

    public OpticDiscDetectorProcess(List<ProgramArgument> programArguments) throws IOException {
        this.programArguments = programArguments;
        this.outputFile = File.createTempFile("optic_disc_detection",".png");
    }

    private List<String> processParameters(String filePath) {
        final String programName = getClass().getResource("bin" + File.separator + "optic-disc-detector.exe").getFile();
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
                Arrays.asList(programName),
                arguments
        );
    }

    @Override
    public DetectionResult detect(String filePath) {
        try {
            final List<String> arguments = processParameters(filePath);
            final Process process = new ProcessBuilder().command(arguments).start();
            process.waitFor();

            if (process.exitValue() == 0) {
                final File outputImageFile = new File(outputFile.getAbsolutePath());
                return new CorrectDetectionResult(ImageIO.read(outputImageFile));
            } else {
                final BufferedReader errorStreamReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream())
                );

                final String errorMessage = errorStreamReader
                        .lines()
                        .reduce("", (prev, current) -> prev + current);

                return new InvalidDetectionResult(errorMessage);
            }
        } catch (IOException | InterruptedException e) {
            return new InvalidDetectionResult(e.getMessage());
        }
    }
}
