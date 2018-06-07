package hw6;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Teller - represents a bank clerk
 * 
 */
public class Teller extends Thread {
	private long idleTime;
	private long activeTime;
	private Queue<Customer> queue;

	public Teller(long activeTime, long idleTime) {
		this.activeTime = activeTime;
		this.idleTime = idleTime;

		queue = new LinkedBlockingQueue<>();
	}

	/**
	 * run - main thread action
	 */
	public void run() {

	}

	public int getQueueLength() {
		return queue.size();
	}

	public void addCustomer(Customer customer) {
		queue.add(customer);
	}
}
