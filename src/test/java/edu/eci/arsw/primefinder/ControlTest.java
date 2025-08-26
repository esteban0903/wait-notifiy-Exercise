package edu.eci.arsw.primefinder;



import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ControlTest {

    @Test
    public void testPauseAndResume() throws InterruptedException {
        Control control = Control.newControl();
        PrimeFinderThread pft = new PrimeFinderThread(2, 10, control);
        pft.start();

        control.pauseAll();
        assertTrue(control.isPaused());

        control.resumeAll();
        assertFalse(control.isPaused());

        pft.join();
    }
}
