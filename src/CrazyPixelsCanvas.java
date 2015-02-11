import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class CrazyPixelsCanvas extends DoubleBufferCanvas {

	private int[][] grid = new int[Settings.WIDTH][Settings.HEIGHT];
	private int[][] shadowGrid = new int[Settings.WIDTH][Settings.HEIGHT];
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

	private int[] surroundings;

	public CrazyPixelsCanvas() {
		super();

		Settings.choosePreset(Settings.Presets.BLUE_RIPPLES);
		
		for (int x = 0; x < Settings.WIDTH; x++) {
			for (int y = 0; y < Settings.HEIGHT; y++) {
				grid[x][y] = Settings.rand.nextInt(3);
			}
		}
		
		if (Settings.COLOR_MORPH) {
			if (!Settings.CRUDE_COLOUR_SHIFT) {
				Settings.colorShiftArray = new ColorShift[Settings.colorArray.length];
				for (int i = 0; i < Settings.colorArray.length; i++) {
					Settings.colorShiftArray[i] = new ColorShift();
				}
			}
		}
	}

	public void paintBuffer(Graphics g) {
		// / g is the offscreen graphics
		if (iteration > 6) {
			for (int x = 0; x < Settings.WIDTH; x++) {
				for (int y = 0; y < Settings.HEIGHT; y++) {
					// System.out.println("Pos ("+x+","+y+"): " + grid[x][y]);
					Color c;
					if (Settings.COLOR_SMOOTHING) {
						c = averageColor(x, y);
					} else {
						int gridVal = grid[x][y];
						c = Settings.colorArray[gridVal];
					}
					g.setColor(c);

					// System.out.println("set colour just fine.");
					g.fillRect(x * cellWidth, y * cellHeight, cellWidth,
							cellHeight);
				}
			}
			if (Settings.fuzzEdges) {
				this.canvasHeight = this.getHeight();
				this.canvasWidth = this.getWidth();
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, this.canvasWidth - 1, this.canvasHeight - 1);
				g.drawRect(1, 1, this.canvasWidth - 3, this.canvasHeight - 3);
				g.drawRect(2, 2, this.canvasWidth - 5, this.canvasHeight - 5);
				g.drawRect(3, 3, this.canvasWidth - 7, this.canvasHeight - 7);
			}
		}
	}

	//This method attempts to "blur" the colour of a specific cell in the grid by taking the average of each of the
	//R, G, and B channels. This makes the colours less stark and somewhat smoother. It softens the image but
	//unfortunately makes it look more "out of focus" than smooth.
	public Color averageColor(int x, int y) {
		int[] rgbTotals = new int[] { 0, 0, 0 };
		Color c;
		int avgCount = 0;
		// Top left
		try {
			c = Settings.colorArray[grid[x - 1][y - 1]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Top
		try {
			c = Settings.colorArray[grid[x][y - 1]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Top right
		try {
			c = Settings.colorArray[grid[x + 1][y - 1]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Left
		try {
			c = Settings.colorArray[grid[x - 1][y]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Right
		try {
			c = Settings.colorArray[grid[x + 1][y]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Bottom left
		try {
			c = Settings.colorArray[grid[x - 1][y + 1]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Bottom
		try {
			c = Settings.colorArray[grid[x][y + 1]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Bottom Right
		try {
			c = Settings.colorArray[grid[x + 1][y + 1]];
			avgCount++;
			rgbTotals[0] += c.getRed();
			rgbTotals[1] += c.getGreen();
			rgbTotals[2] += c.getBlue();
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Self
		c = Settings.colorArray[grid[x][y]];
		int selfWeight = 3;
		avgCount += selfWeight;
		rgbTotals[0] += c.getRed() * selfWeight;
		rgbTotals[1] += c.getGreen() * selfWeight;
		rgbTotals[2] += c.getBlue() * selfWeight;

		return new Color((int) (rgbTotals[0] / avgCount),
				(int) (rgbTotals[1] / avgCount),
				(int) (rgbTotals[2] / avgCount));
	}

	//This method goes through the grid, updating the cells based on variable weighted factor.
	//Each call to evolve advances the current iteration.
	public void evolve() {
		this.canvasHeight = this.getHeight();
		this.canvasWidth = this.getWidth();
		this.cellHeight = (int) (canvasHeight / Settings.HEIGHT);
		this.cellWidth = (int) (canvasWidth / Settings.WIDTH);

		// configure the parameters for looping through the grid based on which
		// way we're looping.
		// Essentially you can look top to bottom, or bottom to top, as well as right to left, or left to right.
		// This means there are 4 combinations.
		// If "scrandomize" is set to false then evolve will always process left-right, top-down
		// Furthermore, if randScrand is true then it will pick one of the four directions at random each time,
		// otherwise it will alternate sequentially between scan directions.
		if (!Settings.scrandomize) {
			x0 = 0;
			dx = 1;
			xbound = Settings.WIDTH;
			y0 = 0;
			dy = 1;
			ybound = Settings.HEIGHT;
		} else {
			if (scanInversion == 0) {
				x0 = 0;
				dx = 1;
				xbound = Settings.WIDTH;
				y0 = 0;
				dy = 1;
				ybound = Settings.HEIGHT;
			} else if (scanInversion == 1) {
				x0 = 0;
				dx = 1;
				xbound = Settings.WIDTH;
				y0 = Settings.HEIGHT - 1;
				dy = -1;
				ybound = -1;
			} else if (scanInversion == 3) {
				x0 = Settings.WIDTH - 1;
				dx = -1;
				xbound = -1;
				y0 = Settings.HEIGHT - 1;
				dy = -1;
				ybound = -1;
			} else if (scanInversion == 3) {
				x0 = Settings.WIDTH - 1;
				dx = -1;
				xbound = -1;
				y0 = 0;
				dy = 1;
				ybound = Settings.HEIGHT;
			}
			if (Settings.randScrand) {
				scanInversion = Settings.rand.nextInt(4);
			} else {
				scanInversion++;
				if (scanInversion == 4) {
					scanInversion = 0;
				}
			}
		}
		
		// the shadowGrid is a new array used to keep track of changes.
		// Using this prevents certain cell changes from cascading as cell updates would be performed in place
		// mid calculation without this.
		if (Settings.useShadowGrid) {
			shadowGrid = new int[Settings.WIDTH][Settings.HEIGHT];
		}

		for (int x = x0; x != xbound; x += dx) {
			for (int y = y0; y != ybound; y += dy) {
				// ============= BEGIN BUSINESS LOGIC =================

				//This array tracks the tallies of each grid value.
				surroundings = new int[] { 0, 0, 0 };

				// In my defense, the "pokemon" try/catch block is because if a
				// pixel is on the edge then
				// there will certainly be an array out of bounds exception for
				// things beyond that boundary.

				//This value tracks how many adjacent cells are offgrid
				//This may be used later to add to a random grid value
				int exceptionCount = 0;

				// Top left
				try {
					surroundings[grid[x - 1][y - 1]] += Settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Top
				try {
					surroundings[grid[x][y - 1]] += Settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Top right
				try {
					surroundings[grid[x + 1][y - 1]] += Settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Left
				try {
					surroundings[grid[x - 1][y]] += Settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Right
				try {
					surroundings[grid[x + 1][y]] += Settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Bottom left
				try {
					surroundings[grid[x - 1][y + 1]] += Settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Bottom
				try {
					surroundings[grid[x][y + 1]] += Settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Bottom Right
				try {
					surroundings[grid[x + 1][y + 1]] += Settings.othersCount;
				} catch (ArrayIndexOutOfBoundsException e) {
					exceptionCount++;
				}

				// Self
				surroundings[grid[x][y]] += Settings.selfCount;

				// Count missing edge pieces as a random colour?
				// This way edge cells are strongly encouraged to flip to a random value.
				if (Settings.fuzzEdges && exceptionCount != 0) {
					// for(int i = 0; i < durdleCount; i++) {
					// surroundings[rand.nextInt(3)]++;
					// }
					surroundings[Settings.rand.nextInt(surroundings.length)] += exceptionCount
							* Settings.othersCount;
				}
				
				// This value adds itself to a random grid value, encoruaging change.
				if (Settings.chaosFactor != 0) {
					surroundings[Settings.rand.nextInt(surroundings.length)] += Settings.chaosFactor;
				}

				// Figure out the highest value in the tally
				int maxRecord = 0;
				for (int i = 0; i < surroundings.length; i++) {
					if (surroundings[i] >= maxRecord) {
						maxRecord = surroundings[i];
					}
				}

				// Normalize based on that result. 
				// Any value equal to the highest is reduced and any value not the highest is increased.
				for (int i = 0; i < surroundings.length; i++) {
					if (surroundings[i] < maxRecord) {
						surroundings[i] += Settings.normalization;
					} else if (surroundings[i] >= maxRecord) {
						surroundings[i] -= Settings.normalization;
					}
				}

				// Recalculate the highest value, as it may have changed.
				maxRecord = 0;
				for (int i = 0; i < surroundings.length; i++) {
					if (surroundings[i] >= maxRecord) {
						maxRecord = surroundings[i];
					}
				}
				
				// Create a list of each index containing a highest value, to determine tie breaks
				ArrayList<Integer> indexes = new ArrayList<Integer>();
				for (int i = 0; i < surroundings.length; i++) {
					if (surroundings[i] == maxRecord) {
						indexes.add(i);
					}
				}
				
				// Now we determine the "winning" value
				int maxIndex = 0;
				if (indexes.size() == 0) { // If somehow there are no tallies that qualified
					// (perhaps due to strange normalization / chaos values) it picks a value at random
					maxIndex = Settings.rand.nextInt(3);
				} else if (indexes.size() == 1) {
					maxIndex = indexes.get(0);
				} else {
					// In the even of a 2 or 3 way tie, it uses a rock-paper-scissors style tie breaking system
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
				
				// then, set that cell to the new winner in the appropriate array
				if (Settings.useShadowGrid) {
					shadowGrid[x][y] = maxIndex;
				} else {
					grid[x][y] = maxIndex;
				}

				// ============= END BUSINESS LOGIC =================
			}
		}
		
		// finally, if using a shadowGrid, swap it in.
		if (Settings.useShadowGrid) {
			grid = shadowGrid.clone();
		}
		
		// This is just to track which iteration we're on
		// and how many calls we've made to Settings.rand.nextInt
		int lastCount = Settings.rand.getLastReport();
		int randCount = Settings.rand.getCount();
		System.out.println("Iteration:" + this.iteration++ + " RandomCounter:"
				+ randCount + "(" + (randCount - lastCount) + ")");
		
		// Colour shifting is still a WIP, it's hard to get a good gradient going
		if (Settings.COLOR_MORPH && this.iteration % 5 == 0) {
			for (int i = 0; i < Settings.colorArray.length; i++) {
				if (Settings.CRUDE_COLOUR_SHIFT) {
					int newR = Math
							.max(0,
									Math.min(
											255,
											Settings.colorArray[i].getRed()
													+ (((int) Math
															.pow(-1,
																	Settings.rand
																			.nextInt(2)) * Settings.rand
															.nextInt(7)))));
					int newG = Math
							.max(0,
									Math.min(
											255,
											Settings.colorArray[i].getGreen()
													+ (((int) Math
															.pow(-1,
																	Settings.rand
																			.nextInt(2)) * Settings.rand
															.nextInt(7)))));
					int newB = Math
							.max(0,
									Math.min(
											255,
											Settings.colorArray[i].getBlue()
													+ (((int) Math
															.pow(-1,
																	Settings.rand
																			.nextInt(2)) * Settings.rand
															.nextInt(7)))));
					Settings.colorArray[i] = new Color(newR, newG, newB);
				} else {
					int rainbowIdx = --Settings.colorShiftArray[i].steps;
					if (rainbowIdx < 0) {
						rainbowIdx = Settings.colorShiftArray[i].rainbow.size()-1;
						Settings.colorShiftArray[i].steps = rainbowIdx;
					}
					Settings.colorArray[i] = Settings.colorShiftArray[i].rainbow.get(rainbowIdx);
				}
			}
		}
		
		// It's possible to switch from one preset to another at any point
		if (Settings.ALTERNATE_PRESETS && this.iteration % 200 == 0) {
			Settings.randomizeLogic(true);
		}

		repaint();
	}

}