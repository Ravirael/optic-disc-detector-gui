package pl.polsl.gui;

import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.ParseException;

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
        SpinnerValueFactory<Double> factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, value, step);

        factory.setConverter(new StringConverter<Double>() {
            private final DecimalFormat df = new DecimalFormat("#.###");

            @Override public String toString(Double value) {
                // If the specified value is null, return a zero-length String
                if (value == null) {
                    return "";
                }

                return df.format(value);
            }

            @Override public Double fromString(String value) {
                try {
                    // If the specified value is null or zero-length, return null
                    if (value == null) {
                        return null;
                    }

                    value = value.trim();

                    if (value.length() < 1) {
                        return null;
                    }

                    // Perform the requested parsing
                    return df.parse(value).doubleValue();
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        return new ParameterControl(label, name,
                    factory
                );
    }
}
