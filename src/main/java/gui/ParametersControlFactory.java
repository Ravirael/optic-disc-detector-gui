package gui;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rames on 09.04.17.
 */
public class ParametersControlFactory {
    List<ParameterControl> create() {
        return Arrays.asList(
                builder()
                .setLabel("Min. radius")
                .setName("--minRadius")
                .setMin(0)
                .setStep(0.005)
                .setMax(1.0)
                .setDefault(0.045)
                .build(),

                builder()
                .setLabel("Max. radius")
                .setName("--maxRadius")
                .setMin(0.0)
                .setMax(1.0)
                .setStep(0.005)
                .setDefault(0.065)
                .build(),

                builder()
                .setLabel("Min. distance")
                .setName("--minDistance")
                .setMin(0.0)
                .setMax(1.0)
                .setStep(0.01)
                .setDefault(0.14)
                .build()
        );
    }

    private ParameterControlBuilder builder() {
        return new ParameterControlBuilder();
    }
}
