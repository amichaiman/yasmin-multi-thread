package hw6;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Bank - launches the simulation and keeps feeding it with new customers
 * 
 */
public class Bank extends Thread {
	public enum Action {
		STARTED, SERVING, SERVED, FINISHED
	};

	private Random random;

	/*
	 * Determines the ratio between the simulated time and running time.
	 * Specifically, this number states how many milliseconds should the program
	 * wait to simulate one minute, if periods in the constructor to Bank are
	 * specified in minutes. Thus, if 'TIME_SIMULATION_FACTOR' is set to 1, a
	 * service time of 1 minutes will be simulated as 1 milliseconds, and a service
	 * time of 10 minutes will be simulated as 10 milliseconds.
	 * 
	 * A good value is 60, making the simulation clock run 1000 faster than the
	 * processes it simulates. Then a service time of 1 minutes(=60 seconds) will be
	 * simulated as 60 milliseconds. Simulating an 8-hour day should take about 30
	 * seconds, since 8 hours/1000 = 30 seconds.
	 */
	public static final int TIME_SIMULATION_FACTOR = 60;

	/*
	 * custCount - number of active customers. It is incremented each time Bank
	 * launches one, and is decremented whenever a customer terminates. It is used
	 * to determine if there is still any activity in the bank.
	 */
	private int custCount;

	/*
	 * statistical parameters relating to customers:
	 */
	private double custArrivalMean;
	private double custArrivalVar;
	private double custServeTimeMean;
	private double custServeTimeVar;

	/*
	 * floor - set of tellers in the bank
	 */
	private Set<Teller> tellers;

	/*
	 * statistical parameters relating to tellers:
	 */
	private int tellerCount;
	private double tellerActiveMean;
	private double tellerActiveVar;
	private double tellerIdleMean;
	private double tellerIdleVar;

	/*
	 * variables related to the sampling task
	 */
	private SamplerQueues sampler;
	private double samplingRate;

	/*
	 * parameters relating to the clock and working hours
	 */
	private long dayLength; 
	private Clock clock;

	/**
	 * 
	 * Constructor - for bank: sets up all time distributions for various simulation
	 * aspects, such as customer arrival rate and teller work cycle. All periods are
	 * stated in minutes unless otherwise specified.
	 *
	 * @param dayLength
	 *            - length of work day (hours)
	 * 
	 * @param tellerActiveMean
	 *            - mean and spread of teller's active period
	 * @param tellerActiveVar
	 * 
	 * @param tellerIdleMean
	 *            - mean and spread of teller's idle time
	 * @param tellerIdleVar
	 * 
	 * @param tellerCount
	 *            - number of tellers to simulate
	 * 
	 * @param custArrivalMean
	 *            - mean and spread of customer inter-arrival periods
	 * @param custArrivalVar
	 * 
	 * @param custServeTimeMean
	 *            - mean and spread of duration of customer's required service
	 * @param custServeTimeVar
	 * 
	 * @param samplingRate
	 *            - delay between samples taken by the observer
	 */
	public Bank(long dayLength,
				double tellerActiveMean, 
				double tellerActiveVar, 
				double tellerIdleMean,
				double tellerIdleVar, 
				int tellerCount, double custArrivalMean, double custArrivalVar,
			double custServeTimeMean, double custServeTimeVar, double samplingRate) {

		this.dayLength = dayLength;
		this.tellerActiveMean = tellerActiveMean;
		this.tellerActiveVar = tellerActiveVar;
		this.tellerIdleMean = tellerIdleMean;
		this.tellerIdleVar = tellerIdleVar;
		this.tellerCount = tellerCount;
		this.custArrivalMean = custArrivalMean;
		this.custArrivalVar = custArrivalVar;
		this.custServeTimeMean = custServeTimeMean;
		this.custServeTimeVar = custServeTimeVar;
		this.samplingRate = samplingRate;

		random = new Random();

		tellers = new HashSet<>();

		for (int i=0 ; i<tellerCount; i++){
			long activeTime = gaussian(tellerActiveMean,tellerActiveVar);
			long idleTime = gaussian(tellerIdleMean,tellerIdleVar);

			tellers.add(new Teller(activeTime,idleTime));
		}


		sampler = new SamplerQueues(samplingRate,  tellers);
		clock = new Clock(dayLength*TIME_SIMULATION_FACTOR);
	}

	/**
	 * run - main thread action
	 */
	public void run() {
		clock.start();
		for (Teller t : tellers){
			t.start();	//TODO : implement run()
		}
		sampler.start();


		while (clock.isWorking()){
			try {
				Thread.sleep(gaussian(custArrivalMean,custArrivalVar));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long serveTime = gaussian(custServeTimeMean,custServeTimeVar);
			Customer customer = new Customer(serveTime,tellers);

			customer.start();
		}
	}
	
	
	/**
	 * gaussian - compute a random number drawn from a normal (Gaussian)
	 * distribution
	 *
	 * @param periodMean
	 *            - the mean of the distribution
	 * @param periodVar
	 *            - the variance of the distribution
	 * @return
	 */
	public long gaussian(double periodMean, double periodVar) {
		double period = 0;
		while (period < 1)
			period = periodMean + Math.sqrt(periodVar) * random.nextGaussian();
		return ((long) (period * Bank.TIME_SIMULATION_FACTOR));
	}

	/**
	 * main -
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Bank bank = new Bank( 	8,
								90,
								20,
								15,
								5,
								5,
								1,
								6,
								15,
								30,
								30);

		bank.start();
	}

} /* class Bank */
