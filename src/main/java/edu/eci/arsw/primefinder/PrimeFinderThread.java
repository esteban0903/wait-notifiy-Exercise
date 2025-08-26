package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	
	private List<Integer> primes;
	private  final Control control;

	public PrimeFinderThread(int a, int b, Control control) {
		super();
                this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
		this.control = control;
	}


    /**
     * Executes the thread's main logic to find and print prime numbers within the range [a, b).
     * The thread checks for a pause condition using a shared control object. If paused, it waits
     * on the monitor until notified to resume. For each number in the range, it checks if the number
     * is prime, adds it to the list of found primes, and prints it to the console.
     * Handles thread interruption during the wait state.
     */
    @Override
	public void run(){
        for (int i= a;i < b;i++){
            synchronized (control.getMonitor()) {
                while(control.isPaused()) {
                    try {
                        control.getMonitor().wait();
                    } catch (InterruptedException ex) {
                        System.out.println("Thread interrupted");
                    }
                }
            }	

            if (isPrime(i)){
                primes.add(i);
                System.out.println(i);
            }
        }
	}
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}

}
