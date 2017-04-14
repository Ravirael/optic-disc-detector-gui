package pl.polsl.detector;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by rames on 09.04.17.
 */
public class NumberProgramArgumentTest {

    @Test
    public void shouldCreateValidFloatingPointString() {
        final ProgramArgument programArgument = new NumberProgramArgument<Double>("--arg", 0.045);

        Assert.assertEquals("0.045", programArgument.value());
    }

    @Test
    public void shouldNotAddTrailingZeros() {
        final ProgramArgument programArgument = new NumberProgramArgument<Double>("--arg", 10.000);

        Assert.assertEquals("10", programArgument.value());
    }
}
