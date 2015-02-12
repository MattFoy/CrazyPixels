import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Settings {
	
	public class Presets {
		public int DEFAULT = -1;
		public int FIFTH_DIMENSION = 0;
		public int CRAZY_TRIANGLES = 1;
		public int EVOLUTION_COMPLETE = 2;
		public int SHADOW_RIPPLES = 3;
		public int BLUE_RIPPLES = 4;
	}
	
	public static RandomCounter rand = new RandomCounter();
	
	public boolean COLOR_SMOOTHING = true;
	public boolean CRUDE_COLOUR_SHIFT = true;
	public boolean ALTERNATE_PRESETS = false;
	public boolean OUTLINES_ONLY = false;
	
	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	private double screenRatio = 2;
	private int screenWidth = screen.width;
	private int screenHeight = screen.height;
	
	public int WIDTH = (int)(screenWidth/screenRatio);
	public int HEIGHT = (int)(screenHeight/screenRatio);
	
	public boolean COLOR_MORPH;
	public Color[] colorArray;
	public ColorShift[] colorShiftArray;
	public int selfCount;
	public int othersCount;
	public int normalization;
	public boolean useShadowGrid;
	public boolean scrandomize;
	public boolean randScrand;
	public boolean fuzzEdges;
	public int chaosFactor;
	
	public void setScale(int scale) {
		screenRatio = scale;
		
		WIDTH = (int)(screenWidth/screenRatio);
		HEIGHT = (int)(screenHeight/screenRatio);
	}
	
	public void choosePreset(int choice) {
		switch (choice) {
		case 0:
			// THE FIFTH DIMENSION
			colorArray = new Color[] { new Color(150, 0, 150), new Color(240, 240, 255), new Color(0, 0, 200) };
			selfCount = -0;
			othersCount = 2;
			normalization = 2;
			useShadowGrid = true;
			scrandomize = true;
			randScrand = true;
			fuzzEdges = true;
			chaosFactor = 0;
			COLOR_MORPH = false;
			break;
		case 1:
			// CRAZY TRIANGLES
			colorArray = new Color[] { Color.GREEN, Color.BLACK, Color.RED };
			selfCount = -0;
			othersCount = 2;
			normalization = 2;
			useShadowGrid = false;
			scrandomize = true;
			randScrand = true;
			fuzzEdges = false;
			chaosFactor = 0;
			COLOR_MORPH = true;
			break;
		case 2:
			// EVOLUTION COMPLETE
			colorArray = new Color[] { Color.GREEN, Color.BLACK, Color.RED };
			selfCount = -1;
			othersCount = 4;
			normalization = 5;
			useShadowGrid = true;
			scrandomize = true;
			randScrand = false;
			fuzzEdges = true;
			chaosFactor = 1;
			COLOR_MORPH = false;
			break;
		case 3:
			// Making ripples Black/Blue
			colorArray = new Color[] { Color.BLUE, Color.BLACK, Color.WHITE };
			selfCount = -1;
			othersCount = 1;
			normalization = 1;
			useShadowGrid = true;
			scrandomize = true;
			randScrand = false;
			fuzzEdges = true;
			chaosFactor = 1;
			COLOR_MORPH = false;
			break;
		case 4:
			// Making ripples Blue/Blue
			colorArray = new Color[] { Color.BLUE, Color.WHITE, Color.BLACK };
			selfCount = -1;
			othersCount = 1;
			normalization = 1;
			useShadowGrid = true;
			scrandomize = true;
			randScrand = false;
			fuzzEdges = true;
			chaosFactor = 1;
			COLOR_MORPH = false;
			break;
		case 5:
			// EVOLUTION COMPLETE
			colorArray = new Color[] { new Color(140, 114, 62), new Color(70, 60, 42), new Color(199, 154, 82) };
			selfCount = -1;
			othersCount = 4;
			normalization = 5;
			useShadowGrid = true;
			scrandomize = true;
			randScrand = false;
			fuzzEdges = true;
			chaosFactor = 1;
			COLOR_MORPH = false;
			break;
		default:
			colorArray = new Color[] { Color.BLUE, Color.BLACK, Color.WHITE };
			selfCount = -1;
			othersCount = 3;
			normalization = 2;
			useShadowGrid = true;
			scrandomize = true;
			randScrand = false;
			fuzzEdges = true;
			chaosFactor = 1;
			COLOR_MORPH = false;
			break;
		}
	}
	
	public void randomizeLogic(boolean preserveColors) {
		Color[] oldColorArray = new Color[3];
		if (preserveColors) {
			oldColorArray = colorArray.clone();
		}
		
		choosePreset(rand.nextInt(5));
		
		if (preserveColors) {
			colorArray = oldColorArray;
		}
	}
}
