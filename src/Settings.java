import java.awt.Color;


public class Settings {
	
	public static class Presets {
		public static int DEFAULT = -1;
		public static int FIFTH_DIMENSION = 0;
		public static int CRAZY_TRIANGLES = 1;
		public static int EVOLUTION_COMPLETE = 2;
		public static int SHADOW_RIPPLES = 3;
		public static int BLUE_RIPPLES = 4;
	}
	
	public static RandomCounter rand = new RandomCounter();
	
	public static boolean COLOR_SMOOTHING = true;
	public static boolean CRUDE_COLOUR_SHIFT = true;
	public static boolean ALTERNATE_PRESETS = false;
	
	private static double screenRatio = 2;
	private static int screenWidth = 1600;
	private static int screenHeight = 900;
	
	public static int WIDTH = (int)(screenWidth/screenRatio);
	public static int HEIGHT = (int)(screenHeight/screenRatio);
	
	public static boolean COLOR_MORPH;
	public static Color[] colorArray;
	public static ColorShift[] colorShiftArray;
	public static int selfCount;
	public static int othersCount;
	public static int normalization;
	public static boolean useShadowGrid;
	public static boolean scrandomize;
	public static boolean randScrand;
	public static boolean fuzzEdges;
	public static int chaosFactor;
	
	public static void choosePreset(int choice) {
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
	
	public static void randomizeLogic(boolean preserveColors) {
		Color[] oldColorArray = new Color[3];
		if (preserveColors) {
			oldColorArray = colorArray.clone();
		}
		
		Settings.choosePreset(rand.nextInt(5));
		
		if (preserveColors) {
			colorArray = oldColorArray;
		}
	}
}
