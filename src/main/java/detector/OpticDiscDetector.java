package detector;

import java.util.concurrent.Future;

/**
 * Created by rames on 05.04.17.
 */
public interface OpticDiscDetector {
    Future<DetectionResult> detect(String filePath);
}
