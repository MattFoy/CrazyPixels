//This class is simply a Thread class which contains a CrazyPixelCanvas element in addition to overloading the run method.
public class CrazyPixelsThread extends Thread {
	private CrazyPixelsCanvas cpc;

	public CrazyPixelsThread(CrazyPixelsCanvas cpc) {
		this.cpc = cpc;
	}

	public void run() {
		try {
			int fpsCap = cpc.settings.fpsCap;
			int iterationCap = (1000 / fpsCap);
			while (true) {
				long strt = System.nanoTime();
				cpc.evolve();
				long end = System.nanoTime();
				long elapse = (end - strt) / 1000000; // divide by 1000000 to
														// get milliseconds.
				if (elapse < iterationCap) {
					// System.out.println("At FPS cap... waiting for: "+(iterationCap-elapse)+"ms");
					sleep(iterationCap - elapse);
				}
			}
		} catch (InterruptedException e) {
			System.out.println("interrupted:" + e.getMessage());
		}
	}
}