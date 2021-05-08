package side;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class NashEqTest {

    private double size, RTT, priority, award, queueOccupancy, queue2;

    @BeforeEach
    void setUp() {
        size = 0.5;
        RTT = 0.1;
        priority = 0.4;
        award = 4;
        queueOccupancy = 0.3;
        queue2 = 0.4;


    }


    @Test
    void action() {
        boolean result = NashEq.action(size, RTT, priority, award, queueOccupancy, queue2);
        assertFalse(result);

    }

    @AfterEach
    void tearDown() {


    }
}