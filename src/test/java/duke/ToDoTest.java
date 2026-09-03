package duke;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToDoTest {
    @Test
    public void testFormat() {
        ToDo newDeadline = new ToDo(new String[] {"Todo task1",});
        assertEquals("[T][ ] task1", newDeadline.toString());
    }
}
