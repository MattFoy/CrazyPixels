import java.util.Random;


public class RandomCounter extends Random {

	private static final long serialVersionUID = 5592690872012592508L;
	private int count;
	private int lastReport;
	
	public RandomCounter() {
		super();
		count = 0;
	}
	
	public int nextInt(int bound) {
		count++;
		return super.nextInt(bound);
	}
	
	public int getCount() {
		lastReport = count;
		return count;
	}
	
	public int getLastReport() {
		return lastReport;
	}
}
