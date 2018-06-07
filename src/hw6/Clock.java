package hw6;

/**
 * Clock - a main clock regulating the bank's work day
 * 
 */
public class Clock extends Thread {

	private boolean isWorking;
	private long numberOfMinutes;

	public Clock(long numberOfMinutes) {
		this.numberOfMinutes = numberOfMinutes;
		isWorking = false;
	}

	public void run() {
		isWorking = true;

		try {
			Thread.sleep(numberOfMinutes);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		isWorking = false;
	}

	public boolean isWorking(){
		return isWorking;
	}

}
