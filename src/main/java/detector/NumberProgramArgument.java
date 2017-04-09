package detector;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class NumberProgramArgument<T extends Number> implements ProgramArgument {
    private final static NumberFormat format;

    static {
        final DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
        decimalSymbols.setDecimalSeparator('.');
        format = new DecimalFormat("0.######", decimalSymbols);
    }

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
