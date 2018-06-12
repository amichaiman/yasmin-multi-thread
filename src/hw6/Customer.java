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

	public Customer(int serveTime, Set<Teller> tellers) {
		this.serviceTime = serveTime;
		this.tellers = tellers;
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
		shortestTellerQueue.addCustomer(this);
	}


	public void serve() {
		status = Status.INSERVICE;
		try {
			Thread.sleep(serviceTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		status = Status.DONE;

	}

} /* class Customer */
