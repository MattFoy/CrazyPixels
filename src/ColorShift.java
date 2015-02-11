import java.awt.Color;
import java.util.ArrayList;

public class ColorShift {
	public int steps;
	public ArrayList<Color> rainbow;

	public ColorShift() {
		rainbow = makeColorGradient(.1,.1,.1,0,2,4);
	}

	public static Color[] goodColors = new Color[] { Color.RED, Color.GREEN,
			Color.BLUE, Color.BLACK, Color.WHITE };

	public ArrayList<Color> makeColorGradient(double frequency1, double frequency2,
			double frequency3, double phase1, double phase2, double phase3) {
		int center = Settings.rand.nextInt(50)+100;
		int width = Settings.rand.nextInt(50)+100;
		int len = Settings.rand.nextInt(50) + 50;
		ArrayList<Color> res = new ArrayList<Color>();
		if (len == 0)
			return res;
		for (int i = 0; i < len; ++i) {
			int red = Math.max(0, Math.min(255,(int) (Math.sin(frequency1 * i + phase1) * width + center)));
			int grn = Math.max(0, Math.min(255,(int) (Math.sin(frequency2 * i + phase2) * width + center)));
			int blu = Math.max(0, Math.min(255,(int) (Math.sin(frequency3 * i + phase3) * width + center)));
			res.add(new Color(red, grn, blu));
		}
		return res;
	}
}
