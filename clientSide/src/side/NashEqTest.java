package side;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NashEqTest {

    @Test
    void action() {
        NashEq nashEq=new NashEq(0.5,0.1,0.5,4,0);
        assertEquals(1.4,nashEq.action());
        NashEq nashEq1=new NashEq(0.5,0.1,0.9,4,0);
        assertEquals(3,nashEq1.action());
    }
}