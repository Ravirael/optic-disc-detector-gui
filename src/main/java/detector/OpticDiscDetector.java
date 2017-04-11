package detector;

import java.util.concurrent.Future;

/**
 * Created by rames on 05.04.17.
 */
public interface OpticDiscDetector {
    DetectionResult detect(String filePath);
}
