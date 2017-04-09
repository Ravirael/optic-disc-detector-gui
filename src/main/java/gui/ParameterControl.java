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

    public ParameterControl(SpinnerValueFactory valueFactory, String labelText, String parameterName) {
        super();
        this.parameterName = parameterName;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gui/ParameterControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        label.setText(labelText);
        spinner.setValueFactory(valueFactory);
    }

    public ProgramArgument toProgramArgument() {
        return new NumberProgramArgument<Double>(parameterName, spinner.getValue());
    }
}
