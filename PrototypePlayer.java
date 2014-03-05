package aPackage.MoveableObjects;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;

import startUp.GamePanel;

import aPackage.Animation;
import aPackage.TileMap;

public class PrototypePlayer
{
	private boolean infiniteJumps = true; // Currently this is a CHEAT CODE

	private double x;
	private double y;
	private double dx;
	private double dy;

	private int width;
	private int height;
	private int currentSpriteHeight = 24;

	private boolean goingLeft;
	private boolean goingRight;
	private boolean usingWeapon;

	private boolean jumping;
	private boolean falling = true;

	private double moveSpeed = 3.0;
	private double maxFallingSpeed = 12;
	private double jumpPower = -6.0;
	private double gravity = 0.25;

	private boolean topLeft;
	private boolean topRight;
	private boolean bottomLeft;
	private boolean bottomRight;

	private Animation animation = new Animation();
	private String graphicLocation = "graphics/player/";
	private String megaManType = "n_MegaMan";
	private BufferedImage[] idleSprites;
	private BufferedImage[] inAirSprites;
	private BufferedImage[] shootingSprites;
	private BufferedImage[] walkingSprites;
	// private BufferedImage[] walkingShootingSprites;
	// private BufferedImage[] inAirShootingSprites;
	private boolean facingLeft;

	private TileMap tileMap;

	public PrototypePlayer(TileMap tm)
	{
		tileMap = tm;

		width = 15;
		height = 22;

		try
		{
			// Player's Sprites!
			idleSprites = new BufferedImage[1];
			inAirSprites = new BufferedImage[1];
			shootingSprites = new BufferedImage[1];
			walkingSprites = new BufferedImage[3];
			// walkingShootingSprites = new BufferedImage[3];
			// inAirShootingSprites = new BufferedImage[1];

			// Full sprite page of sprites is 50 pixels width, 50 pixels height.

			// Actual image locations in use!
			idleSprites[0] = ImageIO.read(new File(graphicLocation + megaManType
					+ "-idle.png"));
			inAirSprites[0] = ImageIO.read(new File(graphicLocation + megaManType
					+ "-inAir.png"));
			shootingSprites[0] = ImageIO.read(new File(graphicLocation + megaManType
					+ "-shooting.png"));
			// inAirShootingSprites[0] = ImageIO.read(new File(graphicLocation +
			// megaManType + "-inAirShooting.png"));

			BufferedImage walkingImage = ImageIO.read(new File(graphicLocation
					+ megaManType + "-walking.png"));
			for(int i = 0; i < walkingSprites.length; i++)
			{
				walkingSprites[i] = walkingImage.getSubimage(i * 24, 0, 24, 24);
			}
			// walkingShootingSprites[0] = ImageIO.read(new File(megaManType +
			// "-walkingShooting.png"));

		} catch (Exception e)
		{
			System.out.println("You done messed up the animation boy. Check the graphic's location again.");
		}
	}

	public void setX(int number)
	{
		x = number;
	}

	public void setY(int number)
	{
		y = number;
	}

	public void setLeft(boolean b)
	{
		goingLeft = b;
	}

	public void setRight(boolean b)
	{
		goingRight = b;
	}

	// Called when player tries to jump
	public void setJumping(boolean b)
	{
		if(!falling || infiniteJumps)
		{
			jumping = true;
		}
	}

	public void setWeaponUse(boolean b)
	{
		usingWeapon = b;
	}

	private void calculateCorners(double x, double y)
	{
		int leftTile = tileMap.getColumnTile((int) (x - width / 2));
		int rightTile = tileMap.getColumnTile((int) (x + width / 2) - 1);
		int topTile = tileMap.getRowTile((int) (y - height / 2));
		int bottomTile = tileMap.getRowTile((int) (y + height / 2) - 1);

		/*
		 * topLeft = (tileMap.getTile(topTile, leftTile) == 0); topRight =
		 * (tileMap.getTile(topTile, rightTile) == 0); bottomLeft =
		 * (tileMap.getTile(bottomTile, leftTile) == 0); bottomRight =
		 * (tileMap.getTile(bottomTile, rightTile) == 0);
		 */

		// Replaced getTile with isBlocked for tileset usage fun
		topLeft = tileMap.isBlocked(topTile, leftTile);
		topRight = tileMap.isBlocked(topTile, rightTile);
		bottomLeft = tileMap.isBlocked(bottomTile, leftTile);
		bottomRight = tileMap.isBlocked(bottomTile, rightTile);
	}

	// ////////////////////////////////////////////////////////////

	public void update()
	{
		// Move the player
		howMuchMovement();

		// Check for collisions
		checkCollisions();

		// Move the map
		mapScroll();

		// Do the animations
		animateSprite();
	}

	/*
	 * howMuchMovement * in charge of the acceleration of the player character. MAKES NO ACTUAL
	 * MOVEMENT. Call checkCollisions afterwards for the real movement.
	 */
	private void howMuchMovement()
	{
		// find next position
		if(goingLeft)
		{
			// dx -= moveSpeed;
			dx = -moveSpeed;
		}
		else if(goingRight)
		{
			// dx += moveSpeed;
			dx = moveSpeed;
		}
		else
		{
			dx = 0;
		}
		// Friction could be added here

		if(jumping)
		{
			dy += jumpPower;
			falling = true;
			jumping = false;
			currentSpriteHeight = 30;
		}

		if(falling)
		{
			dy += gravity;
			if(dy > maxFallingSpeed)
			{
				dy = maxFallingSpeed;
			}
		}
		else
		// You must be on the ground then
		{
			dy = 0;
			currentSpriteHeight = 24;
		}
	}

	/*
	 * checkCollisions * takes the current x and y coordinates and the current x acceleration
	 * and y acceleration and figures out the collisions, then finally moves the player
	 * appropriately. It takes all its stuff directly from the 'private x' area at the top.
	 */
	private void checkCollisions()
	{
		int currentColumn = tileMap.getColumnTile((int) x);
		int currentRow = tileMap.getRowTile((int) y);

		double goingToX = x + dx;
		double goingToY = y + dy;

		// Don't want to actually change the x and y before we know the collisions
		double tempX = x;
		double tempY = y;

		calculateCorners(x, goingToY);
		if(dy < 0)
		{
			if(topLeft || topRight)
			{
				dy = 0;
				tempY = currentRow * tileMap.getTileSize() + (height / 2); // Prevents
											   // getting
											   // stuck
											   // inside
											   // walls
			}
			else
			{
				tempY += dy;
			}
		}
		if(dy > 0)
		{
			if(bottomLeft || bottomRight)
			{
				dy = 0;
				falling = false;
				tempY = (currentRow + 1) * tileMap.getTileSize() - (height / 2); // Prevents
												 // getting
												 // stuck
												 // inside
												 // walls
			}
			else
			{
				tempY += dy;
			}
		}

		calculateCorners(goingToX, y);
		if(dx < 0)
		{
			if(topLeft || bottomLeft)
			{
				dx = 0;
				tempX = currentColumn * tileMap.getTileSize() + (width / 2); // Prevents
											     // getting
											     // stuck
											     // inside
											     // walls
			}
			else
			{
				tempX += dx;
			}
		}
		if(dx > 0)
		{
			if(topRight || bottomRight)
			{
				dx = 0;
				tempX = (currentColumn + 1) * tileMap.getTileSize() - (width / 2); // Prevents
												   // getting
												   // stuck
												   // inside
												   // walls
			}
			else
			{
				tempX += dx;
			}
		}

		if(!falling)
		{
			calculateCorners(x, y + 1);
			// If there are no solid tiles to the bottom left or right
			if(!bottomLeft && !bottomRight)
			{
				// Then the player is falling
				falling = true;
				currentSpriteHeight = 30;
			}
		}

		// X and Y are finally actually changed
		x = tempX;
		y = tempY;
	}

	/*
	 * mapScroll * scrolls the map based on the player's movements. Multiple scrolling types are
	 * available; just use comments to get what you want to use.
	 */
	private void mapScroll()
	{
		// Keeps the player directly in the center of the screen
		tileMap.setX((int) (GamePanel.width / 2 - x));
		tileMap.setY((int) (GamePanel.height / 2 - y));
	}

	private void animateSprite()
	{
		if(goingLeft || goingRight)
		{
			animation.setFrames(walkingSprites);
			animation.setDelay(200);
		}
		else
		{
			if(usingWeapon)
			{
				// CHECK FOR WEAPON TYPE HERE? ~~~~~~~~~~~~~~~~~~~~
				animation.setFrames(shootingSprites);
				animation.setDelay(-1);
			}
			animation.setFrames(idleSprites);
			animation.setDelay(-1);
		}
		if(falling)
		{
			animation.setFrames(inAirSprites);
			animation.setDelay(-1);
		}
		animation.update();

		if(dx < 0)
		{
			facingLeft = true;
		}
		if(dx > 0)
		{
			facingLeft = false;
		}
	}

	public void draw(Graphics2D graphic)
	{
		int tx = tileMap.getX();
		int ty = tileMap.getY();

		// Sprite graphics
		if(!facingLeft)
		{
			// Right facing
			graphic.drawImage(animation.getImage(), (int) (tx + x - width / 2),
					(int) (ty + y - height / 2), null);
		}
		else
		{
			// This one is the same as above, but flipped horizontally.
			// Left facing
			graphic.drawImage(animation.getImage(), (int) (tx + x - (width / 2) + 24),
					(int) (ty + y - height / 2), -24, currentSpriteHeight, null);
		}

		// Hitbox graphic
		//graphic.setColor(Color.RED);
		// Use offset from drawing the player, since x and y are the middle of the player
		// and draw draws from the top-left.
		//graphic.drawRect((int) (tx + x - width / 2), (int) (ty + y - height / 2), width,
				//height);
	}
}
