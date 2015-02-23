import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import foy.util.JINI;

public class Settings {

	public static RandomCounter rand = new RandomCounter();

	public JINI jini;

	public int fpsCap = 30;
	public boolean BREAK_ON_MOUSE_MOVEMENT = false;
	public boolean COLOR_SMOOTHING = true;
	public int MORPH_METHOD = 0;
	public boolean ALTERNATE_PRESETS = true;
	public boolean OUTLINES_ONLY = false;
	public boolean SPLIT_SCREEN = false;

	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	private int screenRatio = 2;
	private int screenWidth = screen.width;
	private int screenHeight = screen.height;
	public int WIDTH = (int) (screenWidth / screenRatio);
	public int HEIGHT = (int) (screenHeight / screenRatio);
	public int borderThickness = 1;
	public int colourShiftInterval = 2;
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

	public Settings(JINI jini) {
		this.jini = jini;

		String[] assertSections = new String[] { "GLOBAL", "COLOR",
				"CALCULATION" };
		String[][] assertKeys = new String[][] {
				new String[] { "fpsCap", "resToCellRatio",
						"BREAK_ON_MOUSE_MOVEMENT", "SPLIT_SCREEN" },
				new String[] { "rgb1", "rgb2", "rgb3", "CRUDE_OUTLINES_ONLY",
						"colourShiftInterval", "COLOR_MORPH", "MORPH_METHOD",
						"COLOR_SMOOTHING", "borderThickness" },
				new String[] { "selfCount", "othersCount", "normalization",
						"useShadowGrid", "scrandomize", "randScrand",
						"fuzzEdges", "chaosFactor" } };
		String verificationResult = jini.verify(assertSections, assertKeys,
				true);
		if (verificationResult.length() == 0) {
			// try to load values from file
			try {
				iniLoad();
				System.out.println("Loaded from settings file!");
			} catch (NumberFormatException | IOException e) {
				System.out.println("Failed to iniLoad()");
				e.printStackTrace();
				choosePreset(0);
				try {
					jini.saveFile();
				} catch (IOException e1) {
					System.out.println("Failed to iniSave()");
					e1.printStackTrace();
				}
			}
		} else {
			System.out.println(verificationResult);
			choosePreset(0);

			try {
				jini.saveFile();
			} catch (IOException e1) {
				System.out.println("Failed to iniSave()");
				e1.printStackTrace();
			}
		}
	}

	public void iniLoad() throws NumberFormatException, IOException {

		// Load GLOBAL values
		fpsCap = jini.getInt("GLOBAL", "fpsCap");
		BREAK_ON_MOUSE_MOVEMENT = jini.getBoolean("GLOBAL",
				"BREAK_ON_MOUSE_MOVEMENT");
		SPLIT_SCREEN = jini.getBoolean("GLOBAL", "SPLIT_SCREEN");
		screenRatio = jini.getInt("GLOBAL", "resToCellRatio");
		if (SPLIT_SCREEN) {
			screenRatio *= 2;
		}

		// Load COLOR values
		colorArray = new Color[] { new Color(jini.getInt("COLOR", "rgb1")),
				new Color(jini.getInt("COLOR", "rgb2")),
				new Color(jini.getInt("COLOR", "rgb3")) };
		OUTLINES_ONLY = jini.getBoolean("COLOR", "CRUDE_OUTLINES_ONLY");
		COLOR_MORPH = jini.getBoolean("COLOR", "COLOR_MORPH");
		MORPH_METHOD = jini.getInt("COLOR", "MORPH_METHOD");
		COLOR_SMOOTHING = jini.getBoolean("COLOR", "COLOR_SMOOTHING");
		borderThickness = jini.getInt("COLOR", "borderThickness");
		colourShiftInterval = jini.getInt("COLOR", "colourShiftInterval");

		// Load CALCULATION values
		selfCount = jini.getInt("CALCULATION", "selfCount");
		othersCount = jini.getInt("CALCULATION", "othersCount");
		normalization = jini.getInt("CALCULATION", "normalization");
		useShadowGrid = jini.getBoolean("CALCULATION", "useShadowGrid");
		scrandomize = jini.getBoolean("CALCULATION", "scrandomize");
		randScrand = jini.getBoolean("CALCULATION", "randScrand");
		fuzzEdges = jini.getBoolean("CALCULATION", "fuzzEdges");
		chaosFactor = jini.getInt("CALCULATION", "chaosFactor");

		this.setScale();
	}

	public void iniSave() throws NumberFormatException, IOException {
		// Save GLOBAL section
		jini.setKVP("GLOBAL", "resToCellRatio", (SPLIT_SCREEN ? screenRatio / 2
				: screenRatio) + "");
		jini.setKVP("GLOBAL", "fpsCap", fpsCap + "");
		jini.setKVP("GLOBAL", "BREAK_ON_MOUSE_MOVEMENT",
				BREAK_ON_MOUSE_MOVEMENT + "");
		jini.setKVP("GLOBAL", "SPLIT_SCREEN", SPLIT_SCREEN + "");

		// Save COLOR section
		jini.setKVP("COLOR", "rgb1", colorArray[0].getRGB() + "");
		jini.setKVP("COLOR", "rgb2", colorArray[1].getRGB() + "");
		jini.setKVP("COLOR", "rgb3", colorArray[2].getRGB() + "");
		jini.setKVP("COLOR", "CRUDE_OUTLINES_ONLY", OUTLINES_ONLY + "");
		jini.setKVP("COLOR", "COLOR_MORPH", COLOR_MORPH + "");
		jini.setKVP("COLOR", "MORPH_METHOD", MORPH_METHOD + "");
		jini.setKVP("COLOR", "colourShiftInterval", colourShiftInterval + "");
		jini.setKVP("COLOR", "COLOR_SMOOTHING", COLOR_SMOOTHING + "");
		jini.setKVP("COLOR", "borderThickness", borderThickness + "");

		// Save CALCULATION section
		jini.setKVP("CALCULATION", "selfCount", selfCount + "");
		jini.setKVP("CALCULATION", "othersCount", othersCount + "");
		jini.setKVP("CALCULATION", "normalization", normalization + "");
		jini.setKVP("CALCULATION", "useShadowGrid", useShadowGrid + "");
		jini.setKVP("CALCULATION", "scrandomize", scrandomize + "");
		jini.setKVP("CALCULATION", "randScrand", randScrand + "");
		jini.setKVP("CALCULATION", "fuzzEdges", fuzzEdges + "");
		jini.setKVP("CALCULATION", "chaosFactor", chaosFactor + "");

		jini.saveFile();
	}

	public void setScale() {
		// screenRatio = scale;
		WIDTH = (int) (screenWidth / screenRatio);
		HEIGHT = (int) (screenHeight / screenRatio);
	}

	public void choosePreset(int choice) {
		switch (choice) {
		case 0:
			// THE FIFTH DIMENSION
			colorArray = new Color[] { new Color(150, 0, 150),
					new Color(240, 240, 255), new Color(0, 0, 200) };
			selfCount = -0;
			othersCount = 2;
			normalization = 2;
			useShadowGrid = true;
			scrandomize = true;
			randScrand = true;
			fuzzEdges = true;
			chaosFactor = 0;
			//COLOR_MORPH = false;
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
			//COLOR_MORPH = true;
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
			//COLOR_MORPH = false;
			break;
		case 3:
			// Making ripples Black/Blue
			colorArray = new Color[] { Color.WHITE, Color.BLUE, Color.BLACK };
			selfCount = -1;
			othersCount = 1;
			normalization = 1;
			useShadowGrid = true;
			scrandomize = true;
			randScrand = false;
			fuzzEdges = true;
			chaosFactor = 1;
			//COLOR_MORPH = false;
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
			//COLOR_MORPH = false;
			break;
		case 5:
			// EVOLUTION COMPLETE
			colorArray = new Color[] { new Color(140, 114, 62),
					new Color(70, 60, 42), new Color(199, 154, 82) };
			selfCount = -1;
			othersCount = 4;
			normalization = 5;
			useShadowGrid = true;
			scrandomize = true;
			randScrand = false;
			fuzzEdges = true;
			chaosFactor = 1;
			//COLOR_MORPH = false;
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
			//COLOR_MORPH = false;
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