package detector;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberProgramArgument<T extends Number> implements ProgramArgument {
    private final static NumberFormat format = new DecimalFormat("0.######");
    private final String name;
    private final T value;

    public NumberProgramArgument(String name, T value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String value() {
        return format.format(value);
    }
}
