package pl.polsl.gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.IOException;

public class ResizableImageView extends HBox {
    @SuppressWarnings("unused")
    @FXML
    private Slider slider;

    @SuppressWarnings("unused")
    @FXML
    private ImageView imageView;

    @SuppressWarnings("unused")
    @FXML
    private ScrollPane imageScrollPane;

    public ResizableImageView() {
        super();
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ResizableImageView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        slider.valueProperty().addListener(x -> calibrate());
        this.widthProperty().addListener(c -> calibrate());
        this.heightProperty().addListener(c -> calibrate());
        this.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    calibrate();
                }
            }
        });
    }

    public Image getImage() {
        return imageView.getImage();
    }

    public void setImage(Image image) {
        imageView.setImage(image);
        calibrate();
    }

    private void calibrate() {
        if (imageView.getImage() == null) {
            return;
        }

        final double paneAspectRatio = imageScrollPane.getViewportBounds().getWidth()
                / imageScrollPane.getViewportBounds().getHeight();

        final double imageAspectRatio = imageView.getImage().getWidth()
                / imageView.getImage().getHeight();

        final double scaleFactor = slider.getValue() / 100.0;

        if (imageAspectRatio > paneAspectRatio) {
            System.out.println("Scaling to width");
            imageView.setFitHeight(0);
            imageView.setFitWidth(imageScrollPane.getViewportBounds().getWidth() * scaleFactor);
        } else {
            System.out.println("Scaling to height");
            imageView.setFitWidth(0);
            imageView.setFitHeight(imageScrollPane.getViewportBounds().getHeight() * scaleFactor);
        }
    }
}
