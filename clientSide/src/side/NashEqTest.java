package side;

import org.junit.jupiter.api.*;

import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;

class NashEqTest {

    private double size, RTT, priority, award, queueOccupancy, queue2;
    NashEq nashEq;

    @BeforeEach
    void setUp() {
        size = 0.5;
        RTT = 0;
        priority = 0.1;
        award = 3;
        queueOccupancy = 0;
        queue2 = 0;

        nashEq = new NashEq();


    }


    @Test
    void action() {
        //boolean result = nashEq.action(size, RTT, priority, award, queueOccupancy, queue2);
        //assertTrue(result);

    }

    @AfterEach
    void tearDown() {


    }
}