package side;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NashEqTest {

    @Test
    void action() {

        assertFalse(NashEq.action(0.5,0.1,0.4,4,0.3,0.4));

        assertTrue(NashEq.action(0.5,0.1,0.9,4,0,0));
    }
}