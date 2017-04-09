package gui;

import detector.DetectionResult;
import detector.OpticDiscDetector;
import detector.OpticDiscDetectorProcess;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final List<ParameterControl> parameterControl;

    @FXML
    private Slider slider;
    @FXML
    private ImageView imageView;

    @FXML
    private VBox parametersView;

    private String filePath;

    public MainWindowController() {
        parameterControl = new ParametersControlFactory().create();
    }

    @FXML
    private void showOpenDialog(final ActionEvent event) {
        final FileChooser fileChooser = new ImageFileChooserFactory().create();
        final File file = fileChooser.showOpenDialog(null);

        filePath = file.getAbsolutePath();

        try {
            final BufferedImage bufferedImage = ImageIO.read(file);
            final Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
        } catch (IOException ex) {
        }
    }

    @FXML
    private void processImage(final ActionEvent event) {
        try {
            final OpticDiscDetector detector = new OpticDiscDetectorProcess(
                    parameterControl
                            .stream()
                            .map(c -> c.toProgramArgument())
                            .collect(Collectors.toList()),
                    executorService
            );
            final Future<DetectionResult> result = detector.detect(filePath);
            result.get()
                    .image()
                    .map(i -> SwingFXUtils.toFXImage((BufferedImage)i, null))
                    .ifPresent(i -> imageView.setImage(i));
            result.get().error().ifPresent(e -> System.out.println(e));
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void zoomChanged() {
        final double scale = slider.getValue() / 100.0;
        System.out.println(scale);

        imageView.setScaleX(scale);
        imageView.setScaleY(scale);
    }

    public void initialize(URL location, ResourceBundle resources) {
        parametersView.getChildren().addAll(parameterControl);
        slider.valueProperty().addListener(x -> zoomChanged());
    }


}
