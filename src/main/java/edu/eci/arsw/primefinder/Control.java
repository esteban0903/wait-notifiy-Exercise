/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;
/**
 * The {@code Control} class manages the execution and coordination of multiple {@link PrimeFinderThread}
 * instances to find prime numbers in a specified range. It divides the workload among a fixed number of threads,
 * periodically pauses their execution to report progress, and resumes them upon user input.
 * <p>
 * The class uses a monitor object to synchronize pause and resume operations across all threads.
 * After a configurable interval, the main thread pauses all worker threads, displays the total number
 * of primes found so far, and waits for the user to press Enter before resuming the computation.
 * </p>
 * <p>
 * <b>Thread Safety:</b> The pause and resume mechanisms are synchronized using a monitor object to ensure
 * safe communication between the control thread and worker threads.
 * </p>
 *
 * <ul>
 *   <li>Number of threads: {@code NTHREADS}</li>
 *   <li>Maximum value to check for primes: {@code MAXVALUE}</li>
 *   <li>Pause interval in milliseconds: {@code TMILISECONDS}</li>
 * </ul>
 *
 * <b>Usage:</b>
 * <pre>
 *     Control control = Control.newControl();
 *     control.start();
 * </pre>
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final Object monitor = new Object();
    private boolean paused = false;  

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    

    /**
     * Constructor for the Control class.
     * Initializes the PrimeFinderThread array and creates the threads.
     */
    Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, this);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1, this);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        //start all
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }

        //Implementation of the TMILISECONDS
        try {
            while (true){
                //sleep the principal thread
                Thread.sleep(TMILISECONDS);
                //sleep the list thhe PrimeFinderThreads
                pauseAll();

                //Count Primes 
                int totalPrimes=0;
                for (PrimeFinderThread thread : pft) {
                    totalPrimes += thread.getPrimes().size();
                }
                System.out.println("Total primes found: " + totalPrimes);

                System.out.println("Press Enter to continue");
                String input = "";

                //only pass if the user press Enter. Readline returns "" if is enter
                while (!( input = System.console().readLine()).isEmpty()){
                    System.out.println("Only Enter is valid. Try again");
                }

                resumeAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Pauses all threads by setting the paused flag to true.
     * This method is synchronized on the monitor object to ensure thread safety
     * when modifying the paused state.
     */
    public void pauseAll() {
        synchronized (monitor){
            paused = true;
        }
    }


    /**
     * Resumes all threads that are waiting on the monitor object.
     * <p>
     * This method sets the paused flag to false and notifies all threads
     * that are waiting on the monitor, allowing them to continue execution.
     * It should be called when you want to resume the operation of threads
     * that were previously paused and waiting.
     * </p>
     * 
     * <b>Thread Safety:</b> This method is synchronized on the monitor object
     * to ensure that changes to the paused flag and notification of waiting
     * threads are atomic.
     */
    public void resumeAll() {
        synchronized (monitor){
            paused = false;
            monitor.notifyAll();
        }
    }

    /**
     * Checks if the current process is paused.
     * This method is thread-safe and returns the value of the paused flag
     * within a synchronized block to ensure visibility across threads.
     *
     * @return true if the process is paused; false otherwise.
     */
    public boolean isPaused() {
        synchronized (monitor) {
            return paused;
        }
    }
    
    /**
     * Returns the monitor object used for synchronization.
     * This object can be used to coordinate thread communication,
     * such as with wait() and notify() methods.
     *
     * @return the monitor object for synchronization
     */
    public Object getMonitor() {
        return monitor;
    }
}
