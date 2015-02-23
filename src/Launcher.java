import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import foy.util.JINI;

public class Launcher {

	private Frame mainFrame;
	private static JINI jini = null;

	public static void main(String[] args) {
		try {
			jini = new JINI("CrazyPixelSettings.ini", true);
		} catch (IOException e) {
			System.out.println("Failed to instantiate JINI");
			e.printStackTrace();
		}

		if (args.length > 0) {
			for (String s : args) { System.out.println(s); }
			if (args[0].equals("/c")) {
				configure();
				// launch a configuration form?
			} else if (args[0].equals("/p")) {
				preview();
				// Maybe there's some windows api hook for this, probably not
				// important
			} else if (args[0].equals("/s")) {
				show();
				// http://stackoverflow.com/questions/1936566/how-do-you-get-the-screen-width-in-java
			}
		} else {
			show();
		}

	}

	public static void configure() {
		Settings settings = new Settings(jini);
		Frame configFrame = new Frame("Configuration");
		configFrame.setSize(500, 500);
		Panel panel = new Panel();
		configFrame.add(panel);
		
		configFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		
		Button preset1Btn = new Button();
		preset1Btn.setLabel("Fifth Dimension");
		preset1Btn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				settings.choosePreset(0);
				try {
					settings.iniSave();
				} catch (NumberFormatException | IOException e) {
					System.out.println("Failed to save settings.");
					e.printStackTrace();
				}
			}
		});		
		panel.add(preset1Btn);
		
		Button preset2Btn = new Button();
		preset2Btn.setLabel("Crazy Triangles");
		preset2Btn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				settings.choosePreset(1);
				try {
					settings.iniSave();
				} catch (NumberFormatException | IOException e) {
					System.out.println("Failed to save settings.");
					e.printStackTrace();
				}
			}
		});		
		panel.add(preset2Btn);
		
		Button preset3Btn = new Button();
		preset3Btn.setLabel("Evolution Complete");
		preset3Btn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				settings.choosePreset(2);
				try {
					settings.iniSave();
				} catch (NumberFormatException | IOException e) {
					System.out.println("Failed to save settings.");
					e.printStackTrace();
				}
			}
		});		
		panel.add(preset3Btn);
		
		Button preset4Btn = new Button();
		preset4Btn.setLabel("Ripples");
		preset4Btn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				settings.choosePreset(3);
				try {
					settings.iniSave();
				} catch (NumberFormatException | IOException e) {
					System.out.println("Failed to save settings.");
					e.printStackTrace();
				}
			}
		});		
		panel.add(preset4Btn);
		
		configFrame.setVisible(true);		
	}

	public static void preview() {

	}

	public static void show() {
		GraphicsDevice[] gs = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getScreenDevices();
		for (int j = 0; j < gs.length; j++) {
			GraphicsConfiguration[] gc = gs[j].getConfigurations();
			for (int i = 0; i < gc.length; i++) {
				boolean splitScreen;
				try {
					splitScreen = jini.getBoolean("GLOBAL", "SPLIT_SCREEN");
				} catch (IOException e) {
					splitScreen = false;
					e.printStackTrace();
					System.out.println("Don't worry, defaulting to false.");
				} // this splits each screen into quarters for mini-displays
					// instead of using the whole screen
				if (splitScreen) {
					// Launch a canvas for each screen
					Rectangle r = gc[i].getBounds();
					Rectangle[] bounds = new Rectangle[4];
					bounds[0] = new Rectangle(r.x, r.y, r.width / 2,
							r.height / 2);
					bounds[1] = new Rectangle(r.x + (r.width / 2), r.y,
							r.width / 2, r.height / 2);
					bounds[2] = new Rectangle(r.x, r.y + (r.height / 2),
							r.width / 2, r.height / 2);
					bounds[3] = new Rectangle(r.x + (r.width / 2), r.y
							+ (r.height / 2), r.width / 2, r.height / 2);

					int preset = 0;
					for (Rectangle rect : bounds) {
						new Launcher().buildUI(rect, preset++, 4);
					}
				} else {
					new Launcher().buildUI(gc[i].getBounds(), 0, 2);
				}
			}
		}
	}

	public void buildUI(Rectangle r, int preset, int canvasRatio) {
		mainFrame = new Frame("");
		mainFrame.setBounds(r);
		mainFrame.setAlwaysOnTop(true);
		// mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);

		mainFrame.setLayout(new GridLayout(1, 1));

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		mainFrame.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
					mainFrame.dispose();
					System.exit(0);
					return;
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {

			}

			@Override
			public void keyTyped(KeyEvent arg0) {

			}
		});
		mainFrame.setUndecorated(true);

		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16,
				BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		mainFrame.setCursor(blankCursor);

		CrazyPixelsCanvas canv = new CrazyPixelsCanvas(jini);
		if (canv.settings.BREAK_ON_MOUSE_MOVEMENT) {
			//TODO make the program stop if the mouse moves.
		}
		
		canv.setBackground(Color.BLACK);
		mainFrame.add(canv);
		mainFrame.setVisible(true);

		CrazyPixelsThread cpt = new CrazyPixelsThread(canv);
		cpt.start();
	}
}