package syntax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamInterceptor {
    private final ByteArrayOutputStream outputInterception;
    public StreamInterceptor() {
        outputInterception = new ByteArrayOutputStream();
    }
    public void prepare() {
        outputInterception.reset();
        System.setOut(new PrintStream(outputInterception));
    }
    public void prepare(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        prepare();
    }
    public void test(String expected) {
        assertEquals(expected.trim(), outputInterception.toString().trim());
    }
}
