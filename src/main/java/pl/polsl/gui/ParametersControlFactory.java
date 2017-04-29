package pl.polsl.gui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;


class ParametersControlFactory {

    private final InputStream stream;

    public ParametersControlFactory(InputStream file) {
        this.stream = file;
    }

    List<ParameterControl> create() throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<ParameterControlBuilder> builders =
                Arrays.asList(
                    objectMapper.readValue(stream, ParameterControlBuilder[].class)
                );

        return builders.stream().map(ParameterControlBuilder::build).collect(Collectors.toList());
    }
}
