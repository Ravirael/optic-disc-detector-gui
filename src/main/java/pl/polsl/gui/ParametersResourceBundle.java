package pl.polsl.gui;

import javafx.application.Application;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class ParametersResourceBundle extends ResourceBundle {
    private final Application.Parameters parameters;

    public ParametersResourceBundle(Application.Parameters parameters) {
        this.parameters = parameters;
    }

    @Override
    protected Object handleGetObject(String key) {
        return parameters;
    }

    @Override
    public Enumeration<String> getKeys() {
        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return false;
            }

            @Override
            public String nextElement() {
                return "programArguments";
            }
        };
    }
}
