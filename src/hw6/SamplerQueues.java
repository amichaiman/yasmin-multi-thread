package hw6;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * SampleQueues - periodically wake up and sample the Bank's queues for the
 * purpose of gathering statistics.
 * 
 */
public class SamplerQueues extends Thread {
	private Set<Teller> tellers;
	private int rate;
	private List<int[]> samples;
	private Clock bankClock;

	public SamplerQueues(int samplingRate, Set<Teller> tellers, Clock clock) {
		rate = samplingRate;
		this.tellers = tellers;
		this.bankClock = clock;
		samples = new ArrayList<>();
	}

	public void run() {
		while (bankClock.isWorking()) {
			int[] sample = new int[tellers.size()];
			int i = 0;

			for (Teller t : tellers) {
				sample[i++] = t.getQueueLength();
			}

			samples.add(sample);
			try {
				Thread.sleep(rate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void printStatistics(){
		int[] averages = new int[tellers.size()];

		for (int[] row : samples){
			for (int i=0; i<row.length; i++){
				System.out.print(row[i] + "\t");
				averages[i] += row[i];
			}
			System.out.println();
		}
		System.out.println("AVERAGE:");
		for (int i=0; i<averages.length; i++){
			System.out.print(averages[i]/samples.size() + "\t");
		}
		System.out.println();
	}

} /* class SampleQueues */
