import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class CrazyPixelsThread extends Thread {
	private CrazyPixelsCanvas cpc;	

	public CrazyPixelsThread(CrazyPixelsCanvas cpc) {
		this.cpc = cpc;
	}

	public void run() {
		try {
			while (true) {
				cpc.evolve();
				sleep(1);
			}
		} catch (InterruptedException e) {
			System.out.println("interrupted:" + e.getMessage());
		}
	}

}
