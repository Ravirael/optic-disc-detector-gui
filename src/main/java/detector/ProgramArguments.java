package detector;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ProgramArguments extends AbstractList<String> {
    private final List<String> arguments;

    public ProgramArguments(Collection<ProgramArgument> arguments) {
        this.arguments = arguments.stream().flatMap(programArgument -> {
            List<String> output = new ArrayList<>();
            output.add(programArgument.name());
            output.add(programArgument.value());
            return output.stream();
        }).collect(Collectors.toList());
    }

    @Override
    public String get(int index) {
        return arguments.get(index);
    }

    @Override
    public int size() {
        return arguments.size();
    }
}
