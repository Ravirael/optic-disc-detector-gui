package detector;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by rames on 05.04.17.
 */
public class OpticDiscDetectorProcess implements OpticDiscDetector {
    private final List<ProgramArgument> programArguments;
    private final File outputFile;
    private final ExecutorService executorService;

    public OpticDiscDetectorProcess(List<ProgramArgument> programArguments, ExecutorService executorService) throws IOException {
        this.programArguments = programArguments;
        this.executorService = executorService;
        this.outputFile = File.createTempFile("optic_disc_detection",".png");
    }

    @Override
    public Future<DetectionResult> detect(String filePath) {
        return executorService.submit(() -> detectImpl(filePath));
    }

    private List<String> detectorParameters() {
        List<String> params = programArguments.stream().flatMap(programArgument -> {
            List<String> output = new ArrayList<>();
            output.add(programArgument.name());
            output.add(programArgument.value());
            return output.stream();
        }).collect(Collectors.toList());

        params.add("-o");
        params.add(outputFile.getAbsolutePath());
        return params;
    }

    private List<String> processParameters(String filePath) {
        List<String> process = new ArrayList<>();
        process.add(getClass().getResource("detector/bin" + File.separator + "optic-disc-detector.exe").getFile());
        process.add("-i");
        process.add(filePath);
        return process;
    }

    private DetectionResult detectImpl(String filePath) {
        try {
            List<String> params = processParameters(filePath);
            params.addAll(detectorParameters());

            Process process = new ProcessBuilder().command(params).start();
            process.waitFor();

            if (process.exitValue() == 0) {
                File input = new File(outputFile.getAbsolutePath());
                return new CorrectDetectionResult(ImageIO.read(input));
            } else {
                String s = null;
                final StringBuilder message = new StringBuilder();
                final BufferedReader in = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((s = in.readLine()) != null) {
                    message.append(s);
                }

                return new InvalidDetectionResult(message.toString());
            }
        } catch (IOException | InterruptedException e) {
            return new InvalidDetectionResult(e.getMessage());
        }
    }
}
