import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Launcher {

	private Frame mainFrame;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length > 0) {
			if (args[0] == "/c") {
				configure();
				
				//launch a configuration form?
			} else if (args[0] == "/p") {
				preview();
				
				//Maybe there's some windows api hook for this, probably not important
			} else if (args[0] == "/s") {
				show();

				// http://stackoverflow.com/questions/1936566/how-do-you-get-the-screen-width-in-java
				
			}
		} else {
			show();
		}

	}
	
	public static void configure() {
		
	}
	
	public static void preview() {
		
	}
	
	public static void show() {
		GraphicsDevice[] gs = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getScreenDevices();
		for (int j = 0; j < gs.length; j++) {
			GraphicsConfiguration[] gc = gs[j].getConfigurations();
			for (int i = 0; i < gc.length; i++) {
				boolean splitScreen = false; // this splits each screen
												// into quarters for
												// mini-displays instead
												// of using the whole
												// screen
				if (false) {
					// Launch a canvas for each screen
					Rectangle r = gc[i].getBounds();
					Rectangle[] bounds = new Rectangle[4];
					bounds[0] = new Rectangle(r.x, r.y, r.width / 2,
							r.height / 2);
					bounds[1] = new Rectangle(r.x + (r.width / 2), r.y,
							r.width / 2, r.height / 2);
					bounds[2] = new Rectangle(r.x,
							r.y + (r.height / 2), r.width / 2,
							r.height / 2);
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
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

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

		CrazyPixelsCanvas canv = new CrazyPixelsCanvas(preset, canvasRatio);
		canv.setBackground(Color.BLACK);
		mainFrame.add(canv);
		mainFrame.setVisible(true);

		CrazyPixelsThread cpt = new CrazyPixelsThread(canv);
		cpt.start();

	}

}
