import java.awt.Color;
import java.util.ArrayList;

public class ColorShift {
	public int steps;
	public ArrayList<Color> rainbow;

	public ColorShift() {
		rainbow = makeColorGradient(0, 2, 4);
	}

	// based on http://krazydad.com/tutorials/makecolors.php
	public ArrayList<Color> makeColorGradient(double phase1, double phase2,
			double phase3) {
		int center = Settings.rand.nextInt(50) + 100;
		int width = Settings.rand.nextInt(50) + 100;
		int len = Settings.rand.nextInt(50) + 500;
		
		double frequency = 2 * Math.PI / len;
		double frequency1 = frequency;
		double frequency2 = frequency; 
		double frequency3 = frequency;
		
		ArrayList<Color> res = new ArrayList<Color>();
		if (len == 0)
			return res;
		for (int i = 0; i < len; ++i) {
			int red = Math.max(
					0,
					Math.min(255, (int) (Math.sin(frequency1 * i + phase1)
							* width + center)));
			int grn = Math.max(
					0,
					Math.min(255, (int) (Math.sin(frequency2 * i + phase2)
							* width + center)));
			int blu = Math.max(
					0,
					Math.min(255, (int) (Math.sin(frequency3 * i + phase3)
							* width + center)));
			res.add(new Color(red, grn, blu));
		}
		return res;
	}
}
