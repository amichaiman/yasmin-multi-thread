package hw6;

import java.util.Set;

/**
 * SampleQueues - periodically wake up and sample the Bank's queues for the
 * purpose of gathering statistics.
 * 
 */
public class SamplerQueues extends Thread {
	private Set<Teller> tellers;
	private double rate;

	public SamplerQueues(double samplingRate, Set<Teller> tellers) {
		rate = samplingRate;
		this.tellers = tellers;
	}

	public void run() {

	}

} /* class SampleQueues */
