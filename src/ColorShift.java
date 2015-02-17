import java.awt.Color;
import java.util.ArrayList;

public class ColorShift {
	public int rainbowIdx = 0;
	public ArrayList<Color> rainbow;

	public ColorShift() {
		rainbow = makeColorGradient();
	}

	// based on http://krazydad.com/tutorials/makecolors.php
	public ArrayList<Color> makeColorGradient() {		
		double phase1 = (0.1 * Settings.rand.nextInt(40));
		double phase2 = (0.1 * Settings.rand.nextInt(40));
		double phase3 = (0.1 * Settings.rand.nextInt(40));
		
		int center = Settings.rand.nextInt(100) + 100;
		int width = Settings.rand.nextInt(100) + 50;
		int len = Settings.rand.nextInt(200) + 200;
		
		double frequency = 2 * Math.PI / len;
		//System.out.println("Frequency: " + frequency);
		double frequency1 = frequency * ((Settings.rand.nextInt(50) / 10) + 1);
		double frequency2 = frequency * ((Settings.rand.nextInt(50) / 10) + 1); 
		double frequency3 = frequency * ((Settings.rand.nextInt(50) / 10) + 1);

		//System.out.println("Frequency 1: " + frequency1);
		//System.out.println("Frequency 2: " + frequency2);
		//System.out.println("Frequency 3: " + frequency3);
		
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
