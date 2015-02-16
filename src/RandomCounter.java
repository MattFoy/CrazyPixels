import java.util.Random;


public class RandomCounter extends Random {

	private static final long serialVersionUID = 5592690872012592508L;
	private int count = 0;
	private int lastReport = 0;
	
	// simply a wrapper for Random.nextInt but with a counter
	public int nextInt(int bound) {
		count++;
		return super.nextInt(bound);
	}
	
	// returns the total count of nextInt calls
	public int getCount() {
		lastReport = count;
		return count;
	}
	
	// return what the total count was the last time it was called
	public int getLastReport() {
		return lastReport;
	}
}
