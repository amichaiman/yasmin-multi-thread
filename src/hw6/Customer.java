package hw6;

import java.util.Set;

/**
 * Customer - represents a client of the bank.
 * 
 */
public class Customer extends Thread {


	private Set<Teller> tellers;

	public enum Status {
		WAITING, INSERVICE, DONE;

	};
	private int serviceTime;
	private Status status;
	private long startWaitTime;
	private long totalWaitTime;
	private Bank bank;

	public Customer(int serveTime, Set<Teller> tellers, Bank bank) {
		this.serviceTime = serveTime;
		this.tellers = tellers;
		this.bank = bank;
	}

	/**
	 * run - main thread action
	 */
	public void run() {
		Teller shortestTellerQueue = null;
		int shortestLengthQueue = Integer.MAX_VALUE;

		for (Teller t : tellers){
			if (t.getQueueLength() < shortestLengthQueue){
				shortestLengthQueue = t.getQueueLength();
				shortestTellerQueue = t;
			}
		}
		status = Status.WAITING;
		startWaitTime = System.nanoTime();
		shortestTellerQueue.addCustomer(this);
	}


	public void serve() {
		totalWaitTime = System.nanoTime()-startWaitTime;
		status = Status.INSERVICE;
		try {
			Thread.sleep(serviceTime*Bank.TIME_SIMULATION_FACTOR);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		status = Status.DONE;
		bank.decrementCustCount();
	}
	public long getTotalWaitTime(){
		return totalWaitTime;
	}
} /* class Customer */
