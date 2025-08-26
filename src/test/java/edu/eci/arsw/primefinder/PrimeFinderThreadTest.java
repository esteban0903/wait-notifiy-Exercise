package edu.eci.arsw.primefinder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PrimeFinderThreadTest {

    @Test
    public void testRunAndGetPrimes() throws InterruptedException {
        // Hilo para buscar primos entre 2 y 10
        PrimeFinderThread pft = new PrimeFinderThread(2, 10, new Control());
        pft.start();
        pft.join();  // Espera a que termine

        List<Integer> primes = pft.getPrimes();
        assertEquals(Arrays.asList(2, 3, 5, 7), primes);
    }

    @Test
    public void testNoPrimesInRange() throws InterruptedException {
        PrimeFinderThread pft = new PrimeFinderThread(14, 16, new Control());
        pft.start();
        pft.join();
        assertTrue(pft.getPrimes().isEmpty());
    }
}