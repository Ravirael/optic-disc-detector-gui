package pl.polsl.detector;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javafx.scene.image.Image;
import org.apache.commons.lang3.SystemUtils;
import org.junit.*;


public class OpticDiscDetectorProcessTest {

    private OpticDiscDetector createDetector(ProgramArgument... programArguments) throws IOException {
        return new OpticDiscDetectorProcess(getClass().getResource("bin/optic-disc-detector" + (SystemUtils.IS_OS_WINDOWS ? ".exe" : "")).getFile(),
                Arrays.asList(programArguments));
    }

    @Test(timeout = 10000)
    public void shouldProcessImageCorrectly() throws IOException, ExecutionException, InterruptedException, FailedDetectionException {
        final OpticDiscDetector detector = createDetector();

        final Image image = detector.detect(getClass().getResource("test_image.jpg").getFile()).getImage();

        Assert.assertFalse(image.isError());
    }

    @Test(timeout = 10000, expected = FailedDetectionException.class)
    public void shouldThrowWithInvalidImageParameter() throws IOException, ExecutionException, InterruptedException, FailedDetectionException {
        final OpticDiscDetector detector = createDetector();
        detector.detect("invalid_image_path.jpg");
    }

    @Test(timeout = 10000, expected = FailedDetectionException.class)
    public void shouldThrowWithInvalidParameter() throws IOException, ExecutionException, InterruptedException, FailedDetectionException {
        final OpticDiscDetector detector = createDetector(new NumberProgramArgument<>("--invalidParameter", 0));
        detector.detect("test_image.jpg");
    }
}
