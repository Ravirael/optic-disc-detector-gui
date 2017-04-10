package gui;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ParametersControlFactory {

    private final File file;

    public ParametersControlFactory(File file) {
        this.file = file;
    }

    List<ParameterControl> create() throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<ParameterControlBuilder> builders =
                Arrays.asList(
                    objectMapper.readValue(file, ParameterControlBuilder[].class)
                );

        return builders.stream().map(b -> b.build()).collect(Collectors.toList());
    }
}
