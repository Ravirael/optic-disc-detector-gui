package pl.polsl.gui;

import javafx.scene.control.SpinnerValueFactory;

class ParameterControlBuilder {
    private double min;
    private double max;
    private double value;
    private double step;
    private String label;
    private String name;

    ParameterControlBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    ParameterControlBuilder setName(String name) {
        this.name = name;
        return this;
    }

    ParameterControlBuilder setMin(double min) {
        this.min = min;
        return this;
    }

    ParameterControlBuilder setMax(double max) {
        this.max = max;
        return this;
    }

    ParameterControlBuilder setValue(double def) {
        this.value = def;
        return this;
    }

    ParameterControlBuilder setStep(double step) {
        this.step = step;
        return this;
    }

    ParameterControl build() {
        return new ParameterControl(label, name,
                    new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, value, step)
                );
    }
}
