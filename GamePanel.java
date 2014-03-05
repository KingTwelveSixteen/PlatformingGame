package startUp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import aPackage.TileMap;
import aPackage.MoveableObjects.PrototypePlayer;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener
{
	public static final int width = 400;
	public static final int height = 400;

	private Thread thread;
	private boolean running;

	private BufferedImage image;
	private Graphics2D graphics;

	private int fps = 30; // Frames per second
	private int targetTime = 1000 / fps; // How fast the game will preferably run

	private TileMap tileMap;
	private PrototypePlayer player;

	public GamePanel()
	{
		super();
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
	}

	// This starts the thread.
	public void addNotify()
	{
		super.addNotify();
		if(thread == null)
		{
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}

	public void run()
	{
		init();

		long startTime;
		long urdTime;
		long waitTime;

		while(running)
		{
			startTime = System.nanoTime();

			update();
			render();
			draw();

			urdTime = (System.nanoTime() - startTime) / 1000000; // One million
			waitTime = targetTime - urdTime;

			try
			{
				thread.sleep(waitTime);
			} catch (Exception e)
			{

			}
		}
	}

	private void init()
	{
		running = true;

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D) image.getGraphics();

		tileMap = new TileMap("testmap.txt", 32); // 32 is the tile-size
		tileMap.loadTiles("tileset.gif");

		player = new PrototypePlayer(tileMap);
		player.setX(50);
		player.setY(50);
	}

	// SEPERATOR LINE -----------------------------------------------------------------

	private void update()
	{
		tileMap.update();
		player.update();
	}

	private void render()
	{
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, width, height);

		tileMap.draw(graphics);
		player.draw(graphics);
	}

	private void draw()
	{
		Graphics drawing = getGraphics();
		drawing.drawImage(image, 0, 0, null);
		drawing.dispose();
	}

	public void keyTyped(KeyEvent key)
	{}

	// Every in-game action that happens when a key is pressed
	public void keyPressed(KeyEvent key)
	{
		int code = key.getKeyCode();

		if(code == KeyEvent.VK_LEFT)
		{
			player.setLeft(true);
		}
		if(code == KeyEvent.VK_RIGHT)
		{
			player.setRight(true);
		}
		if(code == KeyEvent.VK_SHIFT)
		{
			player.setJumping(true);
		}
		if(code == KeyEvent.VK_X)
		{
			player.setWeaponUse(true);
		}
	}

	// Every in-game action that happens when a key is released
	public void keyReleased(KeyEvent key)
	{
		int code = key.getKeyCode();

		if(code == KeyEvent.VK_LEFT)
		{
			player.setLeft(false);
		}
		if(code == KeyEvent.VK_RIGHT)
		{
			player.setRight(false);
		}
		if(code == KeyEvent.VK_X)
		{
			player.setWeaponUse(false);
		}
	}
}
