package pl.polsl.detector;

public class StringProgramArgument implements ProgramArgument {
    private final String name;
    private final String value;

    public StringProgramArgument(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String value() {
        return value;
    }
}
