import java.awt.*;
import java.util.ArrayList;

import foy.util.JINI;

public class CrazyPixelsCanvas extends DoubleBufferCanvas {

	private static final long serialVersionUID = 8647759938885297720L;

	public Settings settings;
	
	private Frame parentF;

	private int[][] grid;
	private int[][] shadowGrid;
	private int canvasWidth;
	private int canvasHeight;
	private int cellWidth;
	private int cellHeight;
	private int iteration = 1;
	private int scanInversion = 0;

	private int x0;
	private int dx;
	private int xbound;
	private int y0;
	private int dy;
	private int ybound;

	private long systemNanoTime;

	private int[] surroundings;

	public CrazyPixelsCanvas(JINI jini, Frame f) {
		super();
		parentF = f;
		settings = new Settings(jini, parentF);
		//settings.choosePreset(3);
		for (Color c : settings.colorArray) {
			System.out.println(c.getRGB());
		}

		grid = new int[settings.WIDTH][settings.HEIGHT];
		shadowGrid = new int[settings.WIDTH][settings.HEIGHT];

		for (int x = 0; x < settings.WIDTH; x++) {
			for (int y = 0; y < settings.HEIGHT; y++) {
				grid[x][y] = Settings.rand.nextInt(3);
			}
		}

		if (settings.COLOR_MORPH) {
			if (settings.MORPH_METHOD == 1) {
				settings.colorShiftArray = new ColorShift[settings.colorArray.length];
				for (int i = 0; i < settings.colorArray.length; i++) {
					settings.colorShiftArray[i] = new ColorShift();
				}
			}
		}
	}

	public void paintBuffer(Graphics g) {
		Color [] paintBufferColorArray = settings.colorArray.clone();
		// / g is the offscreen graphics
		if (iteration > 6) {
			for (int x = 0; x < settings.WIDTH; x++) {
				for (int y = 0; y < settings.HEIGHT; y++) {
					// System.out.println("Pos ("+x+","+y+"): " + grid[x][y]);
					Color c;
					if (settings.COLOR_SMOOTHING) {
						c = averageColor(paintBufferColorArray, x, y);
						if (settings.OUTLINES_ONLY) {
							boolean colArrContains = false;
							for (Color col : paintBufferColorArray) {
								if (col.getRGB() == c.getRGB()) {
									colArrContains = true;
								}
							}
							if (!colArrContains) {
								c = Color.BLACK;
							} else {
								c = Color.WHITE;
							}
						}
					} else {
						int gridVal = grid[x][y];
						c = paintBufferColorArray[gridVal];
					}
					g.setColor(c);

					// System.out.println("set colour just fine.");
					g.fillRect(x * cellWidth, y * cellHeight, cellWidth,
							cellHeight);
				}
			}
			
			// This creates a border as thick as specified by borderThickness in the settings.
			if (settings.fuzzEdges) {
				this.canvasHeight = this.getHeight();
				this.canvasWidth = this.getWidth();
				g.setColor(Color.BLACK);
				for (int i = 0; i <= settings.borderThickness; i++) {
					g.drawRect(i, i, this.canvasWidth - (2*i+1), this.canvasHeight - (2*i+1));
				}
			}
		}
	}

	// This method attempts to "blur" the colour of a specific cell in the grid
	// by taking the average of each of the
	// R, G, and B channels. This makes the colours less stark and somewhat
	// smoother. It softens the image but
	// unfortunately makes it look more "out of focus" than smooth.
	public Color averageColor(Color[] paintBufferColorArray, int x, int y) {
		int[] rgbTotals = new int[] { 0, 0, 0 };
		Color c;
		int avgCount = 0;
		// Top left
		try {
			c = paintBufferColorArray[grid[x - 1][y - 1]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Top
		try {
			c = paintBufferColorArray[grid[x][y - 1]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Top right
		try {
			c = paintBufferColorArray[grid[x + 1][y - 1]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Left
		try {
			c = paintBufferColorArray[grid[x - 1][y]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Right
		try {
			c = paintBufferColorArray[grid[x + 1][y]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Bottom left
		try {
			c = paintBufferColorArray[grid[x - 1][y + 1]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Bottom
		try {
			c = paintBufferColorArray[grid[x][y + 1]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Bottom Right
		try {
			c = paintBufferColorArray[grid[x + 1][y + 1]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Self
		c = paintBufferColorArray[grid[x][y]];
		int selfWeight = 3;
		avgCount += selfWeight;
		rgbTotals[0] += c.getRed() * selfWeight;
		rgbTotals[1] += c.getGreen() * selfWeight;
		rgbTotals[2] += c.getBlue() * selfWeight;

		return new Color((int) (rgbTotals[0] / avgCount),
				(int) (rgbTotals[1] / avgCount),
				(int) (rgbTotals[2] / avgCount));
	}

	// This method goes through the grid, updating the cells based on variable
	// weighted factor.
	// Each call to evolve advances the current iteration.
	public void evolve() {
		systemNanoTime = System.nanoTime();
		this.canvasHeight = this.getHeight();
		this.canvasWidth = this.getWidth();
		this.cellHeight = (int) (canvasHeight / settings.HEIGHT);
		this.cellWidth = (int) (canvasWidth / settings.WIDTH);

		// configure the parameters for looping through the grid based on which
		// way we're looping.
		// Essentially you can look top to bottom, or bottom to top, as well as
		// right to left, or left to right.
		// This means there are 4 combinations.
		// If "scrandomize" is set to false then evolve will always process
		// left-right, top-down
		// Furthermore, if randScrand is true then it will pick one of the four
		// directions at random each time,
		// otherwise it will alternate sequentially between scan directions.
		if (!settings.scrandomize) {
			x0 = 0;
			dx = 1;
			xbound = settings.WIDTH;
			y0 = 0;
			dy = 1;
			ybound = settings.HEIGHT;
		} else {
			if (scanInversion == 0) {
				x0 = 0;
				dx = 1;
				xbound = settings.WIDTH;
				y0 = 0;
				dy = 1;
				ybound = settings.HEIGHT;
			} else if (scanInversion == 1) {
				x0 = 0;
				dx = 1;
				xbound = settings.WIDTH;
				y0 = settings.HEIGHT - 1;
				dy = -1;
				ybound = -1;
			} else if (scanInversion == 3) {
				x0 = settings.WIDTH - 1;
				dx = -1;
				xbound = -1;
				y0 = settings.HEIGHT - 1;
				dy = -1;
				ybound = -1;
			} else if (scanInversion == 3) {
				x0 = settings.WIDTH - 1;
				dx = -1;
				xbound = -1;
				y0 = 0;
				dy = 1;
				ybound = settings.HEIGHT;
			}
			if (settings.randScrand) {
				scanInversion = Settings.rand.nextInt(4);
			} else {
				scanInversion++;
				if (scanInversion == 4) {
					scanInversion = 0;
				}
			}
		}

		// the shadowGrid is a new array used to keep track of changes.
		// Using this prevents certain cell changes from cascading as cell
		// updates would be performed in place
		// mid calculation without this.
		if (settings.useShadowGrid) {
			shadowGrid = new int[settings.WIDTH][settings.HEIGHT];
		}

		for (int x = x0; x != xbound; x += dx) {
			for (int y = y0; y != ybound; y += dy) {
				// ============= BEGIN BUSINESS LOGIC =================

				// This array tracks the tallies of each grid value.
				surroundings = new int[] { 0, 0, 0 };

				// In my defense, the "pokemon" try/catch block is because if a
				// pixel is on the edge then
				// there will certainly be an array out of bounds exception for
				// things beyond that boundary.

				// This value tracks how many adjacent cells are offgrid
				// This may be used later to add to a random grid value
				int exceptionCount = 0;

				// Top left
				try {
					surroundings[grid[x - 1][y - 1]] += settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Top
				try {
					surroundings[grid[x][y - 1]] += settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Top right
				try {
					surroundings[grid[x + 1][y - 1]] += settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Left
				try {
					surroundings[grid[x - 1][y]] += settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Right
				try {
					surroundings[grid[x + 1][y]] += settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Bottom left
				try {
					surroundings[grid[x - 1][y + 1]] += settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Bottom
				try {
					surroundings[grid[x][y + 1]] += settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Bottom Right
				try {
					surroundings[grid[x + 1][y + 1]] += settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Self
				surroundings[grid[x][y]] += settings.selfCount;

				// Count missing edge pieces as a random colour?
				// This way edge cells are strongly encouraged to flip to a
				// random value.
				if (settings.fuzzEdges && exceptionCount != 0) {
					// for(int i = 0; i < durdleCount; i++) {
					// surroundings[rand.nextInt(3)]++;
					// }
					surroundings[Settings.rand.nextInt(surroundings.length)] += exceptionCount
							* settings.othersCount;
				}

				// This value adds itself to a random grid value, encoruaging
				// change.
				if (settings.chaosFactor != 0) {
					surroundings[Settings.rand.nextInt(surroundings.length)] += settings.chaosFactor;
				}

				// Figure out the highest value in the tally
				int maxRecord = 0;
				for (int i = 0; i < surroundings.length; i++) {
					if (surroundings[i] >= maxRecord) {
						maxRecord = surroundings[i];
					}
				}

				// Normalize based on that result.
				// Any value equal to the highest is reduced and any value not
				// the highest is increased.
				for (int i = 0; i < surroundings.length; i++) {
					if (surroundings[i] < maxRecord) {
						surroundings[i] += settings.normalization;
					} else if (surroundings[i] >= maxRecord) {
						surroundings[i] -= settings.normalization;
					}
				}

				// Recalculate the highest value, as it may have changed.
				maxRecord = 0;
				for (int i = 0; i < surroundings.length; i++) {
					if (surroundings[i] >= maxRecord) {
						maxRecord = surroundings[i];
					}
				}

				// Create a list of each index containing a highest value, to
				// determine tie breaks
				ArrayList<Integer> indexes = new ArrayList<Integer>();
				for (int i = 0; i < surroundings.length; i++) {
					if (surroundings[i] == maxRecord) {
						indexes.add(i);
					}
				}

				// Now we determine the "winning" value
				int maxIndex = 0;
				if (indexes.size() == 0) { // If somehow there are no tallies
											// that qualified
					// (perhaps due to strange normalization / chaos values) it
					// picks a value at random
					maxIndex = Settings.rand.nextInt(3);
				} else if (indexes.size() == 1) {
					maxIndex = indexes.get(0);
				} else {
					// In the even of a 2 or 3 way tie, it uses a
					// rock-paper-scissors style tie breaking system
					if (indexes.contains(0) && indexes.contains(1)
							&& indexes.contains(2)) {
						maxIndex = Settings.rand.nextInt(3);
						// System.out.println("3-way tie!");
					}
					if (indexes.contains(0) && indexes.contains(1)) {
						maxIndex = 1;
					} else if (indexes.contains(1) && indexes.contains(2)) {
						maxIndex = 2;
					} else if (indexes.contains(0) && indexes.contains(2)) {
						maxIndex = 0;
					} else {
						maxIndex = Settings.rand.nextInt(3);
						System.err.println("OOPS?!?!");
					}
				}

				// then, set that cell to the new winner in the appropriate
				// array
				if (settings.useShadowGrid) {
					shadowGrid[x][y] = maxIndex;
				} else {
					grid[x][y] = maxIndex;
				}

				// ============= END BUSINESS LOGIC =================
			}
		}

		// finally, if using a shadowGrid, swap it in.
		if (settings.useShadowGrid) {
			grid = shadowGrid.clone();
		}

		// This is just to track which iteration we're on
		// and how many calls we've made to Settings.rand.nextInt
		int lastCount = Settings.rand.getLastReport();
		int randCount = Settings.rand.getCount();
		long newTime = System.nanoTime();
		int msDuration = (int) ((newTime - systemNanoTime) / 1000000);
		systemNanoTime = newTime;
		System.out.println("[" + msDuration + "ms] Iteration:"
				+ this.iteration++ + " RandomCounter:" + randCount + "("
				+ (randCount - lastCount) + ")");

		// Colour shifting is still a WIP, it's hard to 
		// get a good gradient going
		int sineFactor = Math.max(1, Math.abs((int)(10 * Math.sin(iteration / (Math.PI * 10 * settings.colourShiftInterval))+1)));
		//System.out.println(sineFactor);
		if (settings.COLOR_MORPH && this.iteration % sineFactor == 0) {
			System.out.println(iteration + " colour shifting!");
			for (int i = 0; i < settings.colorArray.length; i++) {
				if (settings.MORPH_METHOD == 0) {
					int newR = Math
							.max(0,
									Math.min(
											255,
											settings.colorArray[i].getRed()
													+ (((int) Math
															.pow(-1,
																	Settings.rand
																			.nextInt(2)) * Settings.rand
															.nextInt(7)))));
					int newG = Math
							.max(0,
									Math.min(
											255,
											settings.colorArray[i].getGreen()
													+ (((int) Math
															.pow(-1,
																	Settings.rand
																			.nextInt(2)) * Settings.rand
															.nextInt(7)))));
					int newB = Math
							.max(0,
									Math.min(
											255,
											settings.colorArray[i].getBlue()
													+ (((int) Math
															.pow(-1,
																	Settings.rand
																			.nextInt(2)) * Settings.rand
															.nextInt(7)))));
					settings.colorArray[i] = new Color(newR, newG, newB);
				} else if (settings.MORPH_METHOD == 1) {
					if (settings.colorShiftArray[i].rainbowIdx >= settings.colorShiftArray[i].rainbow.size() - 1) {
						settings.colorShiftArray[i].rainbowIdx = 0;
					}
//					Color oldC = settings.colorArray[i];
					settings.colorArray[i] = settings.colorShiftArray[i].rainbow
							.get(++settings.colorShiftArray[i].rainbowIdx);
					
//					int dR = Math.abs(oldC.getRed() - settings.colorArray[i].getRed());
//					int dG = Math.abs(oldC.getGreen() - settings.colorArray[i].getGreen());
//					int dB = Math.abs(oldC.getBlue() - settings.colorArray[i].getBlue());
//					System.out.println("dR:" + dR + ", dG:" + dG + ", dB:" + dB);
//					if ((dR + dG + dB) > 20) {
//						System.out.println("JARRED??");
//					}
				}
			}
		}

		// It's possible to switch from one preset to another at any point
		if (settings.ALTERNATE_PRESETS && this.iteration % 200 == 0) {
			settings.randomizeLogic(true);
		}

		repaint();
	}

}