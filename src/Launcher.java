import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Launcher {

	private Frame mainFrame;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Launcher().buildUI();
	}

	public void buildUI() {
		mainFrame = new Frame("");
		mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);

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
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		mainFrame.setCursor(blankCursor);
		
		CrazyPixelsCanvas canv = new CrazyPixelsCanvas();
		canv.setBackground(Color.BLACK);
		mainFrame.add(canv);
		mainFrame.setVisible(true);
		
		CrazyPixelsThread cpt = new CrazyPixelsThread(canv);
		cpt.start();

	}
	
}


