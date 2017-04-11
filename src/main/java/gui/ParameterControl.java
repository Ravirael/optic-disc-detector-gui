package gui;

import detector.NumberProgramArgument;
import detector.ProgramArgument;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ParameterControl extends HBox {
    private final String parameterName;
    @FXML
    private Spinner<Double> spinner;
    @FXML
    private Label label;

    public ParameterControl(String labelText, String parameterName, SpinnerValueFactory valueFactory) {
        super();
        this.parameterName = parameterName;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ParameterControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        label.setText(labelText);
        spinner.setValueFactory(valueFactory);

        spinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                spinner.increment(0); // won't change value, but will commit editor
            }
        });
    }

    public ProgramArgument toProgramArgument() {
        return new NumberProgramArgument<>(parameterName, spinner.getValueFactory().getValue());
    }
}
