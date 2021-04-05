package side;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NashEqTest {

    @Test
    void action() {
        NashEq nashEq=new NashEq(0.5,0.1,0.4,4,0.3,0.4);
        assertFalse(nashEq.action());
        NashEq nashEq1=new NashEq(0.5,0.1,0.9,4,0,0);
        assertTrue(nashEq1.action());
    }
}