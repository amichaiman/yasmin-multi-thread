package hw6;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Teller - represents a bank clerk
 * 
 */
public class Teller extends Thread {
    private Clock bankClock;
    private int idleTime;
	private int activeTime;
	private Queue<Customer> queue;

	public Teller(int activeTime, int idleTime, Clock bankClock) {
		this.activeTime = activeTime;
		this.idleTime = idleTime;
        this.bankClock = bankClock;
		queue = new LinkedBlockingQueue<>();
	}

	/**
	 * run - main thread action
	 */
	public void run() {

	    while (bankClock.isWorking() || !queue.isEmpty()){
	        TellerClock activeTimeClock = new TellerClock(activeTime,this);
	    	activeTimeClock.start();


	        while (activeTimeClock.isWorking()) {
				if (queue.isEmpty()) {
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if (!queue.isEmpty()){
					Customer currentCustomer = queue.remove();
					currentCustomer.serve();
				}
				if (!bankClock.isWorking() && queue.isEmpty()){
					return;
				}
			}



			TellerClock idleTimeClock = new TellerClock(idleTime,this);
	        idleTimeClock.start();

	        //going to rest
            try {
            	synchronized (this) {
					this.wait();
				}
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}

	public int getQueueLength() {
		return queue.size();
	}

	public void addCustomer(Customer customer) {
		queue.add(customer);

		synchronized (this){
			this.notify();
		}
	}
}
