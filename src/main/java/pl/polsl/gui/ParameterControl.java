package pl.polsl.gui;

import pl.polsl.detector.NumberProgramArgument;
import pl.polsl.detector.ProgramArgument;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;

class ParameterControl extends HBox {
    private final String parameterName;
    @SuppressWarnings("unused")
    @FXML
    private Spinner<Double> spinner;
    @SuppressWarnings("unused")
    @FXML
    private Label label;

    public ParameterControl(String labelText, String parameterName, SpinnerValueFactory<Double> valueFactory) {
        super();
        new FXMLLoaderFacade().load(this);
        this.parameterName = parameterName;

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
