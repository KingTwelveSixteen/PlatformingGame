package startUp;

import java.awt.*;

import javax.swing.*;

/**
 * Class: Screen
 * 
 * @author James Timothy McCravy
 * 
 * @version 1.0 Written: March 5 2014
 * 
 *          This Class - Creates a window.
 * 
 *          Purpose - The window the player will use to play the game.
 */
public class Screen
{
	/*
	 * // Menu bar initializations
	 * 
	 * private JMenuBar menuBar = new JMenuBar(); private JMenu levelSelect = new
	 * JMenu("Level Select"); private JMenuItem testLevel = new JMenuItem();
	 */
	public Screen()
	{
		JFrame window = new JFrame("MegaMan Game");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new GamePanel());
		window.setLayout(new BorderLayout());
		/*
		 * // Dropdown menu creation menuBar.add(levelSelect); levelSelect.add(testLevel);
		 * window.add(menuBar, BorderLayout.NORTH);
		 * 
		 * 
		 * // This is the stuff that happens when this menu option is chosen
		 * testLevel.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent event) { Level level = new Level(); } });
		 */
		
		//JPanel mainScreen = new JPanel();

		window.pack();
		window.setVisible(true);
	}
}
