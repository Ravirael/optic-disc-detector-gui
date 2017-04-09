package gui;

import detector.DetectionResult;
import detector.OpticDiscDetector;
import detector.OpticDiscDetectorProcess;
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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainWindowController implements Initializable {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    @FXML
    private ImageView imageView;

    @FXML
    private VBox parametersList;

    String filePath;

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
            final OpticDiscDetector detector = new OpticDiscDetectorProcess(new ArrayList<>(), executorService);
            final Future<DetectionResult> result = detector.detect(filePath);
            result.get()
                    .image()
                    .map(i -> SwingFXUtils.toFXImage((BufferedImage)i, null))
                    .ifPresent(i -> imageView.setImage(i));
            result.get().error().ifPresent(e -> System.out.println(e));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        //parametersList.getChildren().add(new ParameterControl(valueFactory));
        parametersList.getChildren().addAll(new ParametersControlFactory().create());
    }


}
