package pl.polsl.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.SystemUtils;
import pl.polsl.detector.FailedDetectionException;
import pl.polsl.detector.OpticDiscDetector;
import pl.polsl.detector.OpticDiscDetectorProcess;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
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
    private final FileChooser imageFileChooser;


    public MainWindowController() {
        try {
            parameterControl = new ParametersControlFactory(
                    getClass().getResourceAsStream("parameters.json")
            )
                    .create();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.imageFileChooser = new ImageFileChooserFactory().create();
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
    private void showOpenDialog() {
        final File file = imageFileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                setImage(new CorrectImageFileReader().load(file));
                filePath = file.getAbsolutePath();
                imageFileChooser.setInitialDirectory(file.toPath().getParent().toFile());
            } catch (ImageLoadingException e) {
                showError("Cannot open image!", e.getMessage());
            }
        }
    }

    @FXML
    private void saveCurrentImage() {
        final String extension = "png";
        final Image image = imageView.getImage();

        if (image != null) {
            final File file = new File(
                    imageFileChooser.showSaveDialog(null).getAbsolutePath()
                            + "."
                            + extension
            );

            try {
                ImageIO.write(
                        SwingFXUtils.fromFXImage(image, null),
                        extension,
                        file
                );
            } catch (IOException e) {
                showError("Cannot save image!", e.getMessage());
            }
        }
    }

    @FXML
    private void processImage() {
        try {
            final OpticDiscDetector detector = createDetector();
            progressIndicator.setVisible(true);

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
