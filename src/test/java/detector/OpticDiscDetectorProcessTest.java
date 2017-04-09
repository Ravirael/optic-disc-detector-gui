package detector;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.*;

/**
 * Created by rames on 08.04.17.
 */
public class OpticDiscDetectorProcessTest {

    private ExecutorService executorService;


    private OpticDiscDetector createDetector(ProgramArgument... programArguments) throws IOException {
        return new OpticDiscDetectorProcess(Arrays.asList(programArguments), executorService);
    }


    @Before
    public void setup() {
        executorService = Executors.newSingleThreadExecutor();
    }

    @Test(timeout = 10000)
    public void shouldProcessImageCorrectly() throws IOException, ExecutionException, InterruptedException {
        OpticDiscDetector detector = createDetector();

        DetectionResult result = detector.detect(getClass().getResource("detector/test_image.jpg").getFile()).get();

        Assert.assertFalse(result.error().isPresent());
        Assert.assertTrue(result.image().isPresent());
    }

    @Test(timeout = 10000)
    public void shouldReturnErrorWithInvalidImageParameter() throws IOException, ExecutionException, InterruptedException {
        OpticDiscDetector detector = createDetector();

        DetectionResult result = detector.detect("invalid_image_path.jpg").get();

        Assert.assertTrue(result.error().isPresent());
        Assert.assertFalse(result.image().isPresent());
    }

    @Test(timeout = 10000)
    public void shouldReturnErrorWithInvalidParameter() throws IOException, ExecutionException, InterruptedException {
        OpticDiscDetector detector = createDetector(new NumberProgramArgument<Integer>("--invalidParameter", 0));

        DetectionResult result = detector.detect("invalid_image_path.jpg").get();

        Assert.assertTrue(result.error().isPresent());
        Assert.assertFalse(result.image().isPresent());
    }
}
