package pl.polsl.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.SystemUtils;
import pl.polsl.detector.DetectionResult;
import pl.polsl.detector.FailedDetectionException;
import pl.polsl.detector.OpticDiscDetector;
import pl.polsl.detector.OpticDiscDetectorProcess;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    public void calibrateImage() {
        imageView.calibrate();
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
            final Image image = new Image(new FileInputStream(file));
            imageView.setImage(image);
            if (image.isError()) {
                showError("Cannot open image!", image.getException().getMessage());
            }
        } catch (FileNotFoundException e) {
            showError("Cannot open image!", e.getMessage());
        }
    }

    @FXML
    private void processImage(final ActionEvent event) {
        try {
            progressIndicator.setVisible(true);
            final OpticDiscDetector detector = new OpticDiscDetectorProcess(
                    detectorExecutablePath,
                    parameterControl
                            .stream()
                            .map(c -> c.toProgramArgument())
                            .collect(Collectors.toList())
            );
            
            final CompletableFuture<Image> result = CompletableFuture
                    .supplyAsync(() -> {
                        try {
                            return detector.detect(filePath);
                        } catch (FailedDetectionException e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    });

            result
                .thenAccept(
                        detectionResult -> {
                            imageView.setImage(detectionResult);
                            progressIndicator.setVisible(false);
                        }
                )
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


    public void initialize(URL location, ResourceBundle resources) {
        parametersView.getChildren().addAll(parameterControl);

        final Application.Parameters parameters = (Application.Parameters) resources.getObject("programArguments");
        final List<String> parametersList = parameters.getRaw();
        detectorExecutablePath =
                parametersList.size() > 0 ?
                parametersList.get(0) :
                "optic-disc-detector" + (SystemUtils.IS_OS_WINDOWS ? ".exe" : "");
    }
   
}
