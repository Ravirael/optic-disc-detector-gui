package pl.polsl.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.SystemUtils;
import pl.polsl.detector.FailedDetectionException;
import pl.polsl.detector.OpticDiscDetector;
import pl.polsl.detector.OpticDiscDetectorProcess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable {
    private String detectorExecutablePath;
    private List<ParameterControl> parameterControl;

    @FXML
    private ResizableImageView imageView;

    @FXML
    private VBox parametersView;

    @FXML
    private ProgressIndicator progressIndicator;

    private String filePath;

    public MainWindowController() {
        try {
            parameterControl = new ParametersControlFactory(
                    getClass().getResourceAsStream("parameters.json")
            )
                    .create();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert
                .getDialogPane()
                .getChildren()
                .stream()
                .filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void showOpenDialog(final ActionEvent event) {
        final FileChooser fileChooser = new ImageFileChooserFactory().create();
        final File file = fileChooser.showOpenDialog(null);

        if (file == null) {
            return;
        }

        filePath = file.getAbsolutePath();

        try {
            setImage(new CorrectImageFileReader().load(file));
        } catch (ImageLoadingException e) {
            showError("Cannot open image!", e.getMessage());
        }
    }

    @FXML
    private void processImage(final ActionEvent event) {
        try {
            progressIndicator.setVisible(true);
            final OpticDiscDetector detector = createDetector();
            
            final CompletableFuture<Image> result = CompletableFuture
                    .supplyAsync(() -> {
                        try {
                            return detector.detect(filePath);
                        } catch (FailedDetectionException e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    });

            result
                .thenAccept(this::setImage)
                .exceptionally((ex) -> {
                    Platform.runLater(() -> {
                        showError("Cannot process image!", ex.getCause().getMessage());
                        progressIndicator.setVisible(false);
                    });
                    return null;
                });

        } catch (IOException | RuntimeException e) {
            progressIndicator.setVisible(false);
            e.printStackTrace();
            showError("Cannot process image!", e.getMessage());
        }
    }

    private void setImage(Image image) {
        imageView.setImage(image);
        progressIndicator.setVisible(false);
    }

    private OpticDiscDetectorProcess createDetector() throws IOException {
        return new OpticDiscDetectorProcess(
                detectorExecutablePath,
                parameterControl
                        .stream()
                        .map(ParameterControl::toProgramArgument)
                        .collect(Collectors.toList())
        );
    }

    public void initialize(URL location, ResourceBundle resources) {
        final Application.Parameters parameters = (Application.Parameters) resources.getObject("programArguments");
        final List<String> parametersList = parameters.getRaw();

        detectorExecutablePath =
                parametersList.size() > 0 ?
                parametersList.get(0) :
                "optic-disc-detector" + (SystemUtils.IS_OS_WINDOWS ? ".exe" : "");

        parametersView.getChildren().addAll(parameterControl);
    }
   
}
