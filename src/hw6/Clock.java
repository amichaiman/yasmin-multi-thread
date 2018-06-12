package hw6;

/**
 * Clock - a main clock regulating the bank's work day
 * 
 */
public class Clock extends Thread {

	private boolean isWorking;
	private int  numberOfMinutes;

	public Clock(int numberOfMinutes) {
		this.numberOfMinutes = numberOfMinutes;
		isWorking = false;
	}

	public void run() {
		isWorking = true;

		try {
			Thread.sleep(numberOfMinutes*Bank.TIME_SIMULATION_FACTOR);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		isWorking = false;
	}

	public boolean isWorking(){
		return isWorking;
	}

}
