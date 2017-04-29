package pl.polsl.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

class FXMLLoaderFacade {
    public <T extends Node> void load(T node) {
        final FXMLLoader fxmlLoader = new FXMLLoader(
                node.getClass().getResource(node.getClass().getSimpleName() + ".fxml")
        );
        fxmlLoader.setRoot(node);
        fxmlLoader.setController(node);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
