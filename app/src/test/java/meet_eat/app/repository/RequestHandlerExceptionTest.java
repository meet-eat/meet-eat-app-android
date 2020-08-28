package meet_eat.app.repository;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class RequestHandlerExceptionTest {

    @Test
    public void testConstructor() {
        // Execution
        RequestHandlerException exceptionOne = new RequestHandlerException();
        RequestHandlerException exceptionTwo = new RequestHandlerException();

        // Assertions
        assertNotNull(exceptionOne);
        assertNotNull(exceptionTwo);
        assertNotEquals(exceptionOne, exceptionTwo);
    }

    @Test
    public void testConstructorMessage() {
        // Execution
        String message = "Evil message";
        RequestHandlerException exception = new RequestHandlerException(message);

        // Assertions
        assertEquals(message, exception.getMessage());
    }
}
