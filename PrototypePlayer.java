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

	// X and Y are the middle of the sprite
	private double x;
	private double y;

	private double xMovement;
	private double yMovement;

	private int width;
	private int height;

	private int slideDelay;
	private int usingWeaponDelay;

	private boolean goingLeft;
	private boolean goingRight;
	private boolean usingWeapon;
	private boolean holdingDown;
	private boolean sliding;

	private boolean jumping;
	private boolean falling = true;

	private double moveSpeed = 1.5;
	private double maxFallingSpeed = 12;
	private double jumpPower = -4.875;
	private double gravity = 0.20;

	private boolean topLeftBlocked;
	private boolean topRightBlocked;
	private boolean bottomLeftBlocked;
	private boolean bottomRightBlocked;

	private Animation animation = new Animation();
	private String graphicLocation = "graphics/player/";
	private String megaManType = "normal";
	private BufferedImage playerSprites;
	private BufferedImage[] idleSprites;
	private BufferedImage[] inAirSprites;
	private BufferedImage[] shootingSprites;
	private BufferedImage[] walkingSprites;
	private BufferedImage[] walkingShootingSprites;
	// private BufferedImage[] startWalkingSprites;
	// private BufferedImage[] startWalkingShootingSprites;
	private BufferedImage[] inAirShootingSprites;
	private BufferedImage[] slidingSprites;
	private boolean facingLeft;

	private TileMap tileMap;

	public PrototypePlayer(TileMap currentLevelsTileMap)
	{
		tileMap = currentLevelsTileMap;

		width = 12;
		height = 24;

		// Load the graphics once per pallette swap.
		loadGraphics(0);
		loadGraphics(1);
	}

	private void loadGraphics(int index)
	{
		/*
		 * Pro tip: index 0 = normal index 1 = twilightsparkle
		 */
		try
		{
			// Player's Sprites!

			// This one is the full sprite list page.
			playerSprites = ImageIO.read(new File(graphicLocation + megaManType
					+ "_MegaMan_SpriteSheet.png"));

			// Number at the end is how many frames in the animation. 1 is no animation.
			idleSprites = new BufferedImage[6];
			inAirSprites = new BufferedImage[1];
			shootingSprites = new BufferedImage[1];
			walkingSprites = new BufferedImage[4];
			walkingShootingSprites = new BufferedImage[4];
			// startWalkingSprites = new BufferedImage[1];
			// startWalkingShootingSprites = new BufferedImage[1];
			inAirShootingSprites = new BufferedImage[1];
			slidingSprites = new BufferedImage[1];

			// Used for easy locating specific sprites.
			// Starts at top-left and moves right and down.
			// ZERO COUNTS. First row and column are at 0.
			int numColumnsRight = 0;
			int numRowsDown = 0;

			// inAirShootingSprites[0] = ImageIO.read(new File(graphicLocation +
			// megaManType + "-inAirShooting.png"));

			numColumnsRight = 3;
			numRowsDown = 5;

			idleSprites[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			idleSprites[1] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			idleSprites[2] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			idleSprites[3] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			idleSprites[4] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			numColumnsRight++;
			idleSprites[5] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);

			numColumnsRight = 0;
			numRowsDown = 1;
			shootingSprites[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);

			numColumnsRight = 1;
			numRowsDown = 0;
			walkingSprites[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			numColumnsRight++;
			walkingSprites[1] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			numColumnsRight++;
			walkingSprites[2] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			numColumnsRight--;
			walkingSprites[3] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);

			numColumnsRight = 1;
			numRowsDown = 1;
			walkingShootingSprites[0] = playerSprites.getSubimage(
					(numColumnsRight * 50), (numRowsDown * 50), 50, 50);
			numColumnsRight++;
			walkingShootingSprites[1] = playerSprites.getSubimage(
					(numColumnsRight * 50), (numRowsDown * 50), 50, 50);
			numColumnsRight++;
			walkingShootingSprites[2] = playerSprites.getSubimage(
					(numColumnsRight * 50), (numRowsDown * 50), 50, 50);
			numColumnsRight--;
			walkingShootingSprites[3] = playerSprites.getSubimage(
					(numColumnsRight * 50), (numRowsDown * 50), 50, 50);

			numColumnsRight = 4;
			numRowsDown = 0;
			inAirSprites[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);

			numColumnsRight = 4;
			numRowsDown = 1;
			inAirShootingSprites[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);

			numColumnsRight = 0;
			numRowsDown = 3;
			slidingSprites[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);

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

	public void setDown(boolean b)
	{
		holdingDown = b;
	}

	// Called when player tries to jump
	// Doesn't work if the player is in mid-air (unless infinite jump cheat is active)
	// Also doesn't work while sliding
	public void setJumping(boolean b)
	{
		if((!falling || infiniteJumps))
		{
			jumping = true;
		}
	}

	public void setWeaponUse(boolean b)
	{
		usingWeapon = b;
	}

	public void setMegaManType(String type)
	{
		megaManType = type;
	}

	private void calculateCorners(double x, double y)
	{
		// X and Y are the middle of the sprite, thats what the weirdness is about. Negative
		// one is for avoiding going out of boundaries.
		int leftTileLocation = tileMap.getColumnTileLocation((int) (x - width / 2));
		int rightTileLocation = tileMap.getColumnTileLocation((int) (x + width / 2) - 1);
		int topTileLocation = tileMap.getRowTileLocation((int) (y - height / 2));
		int bottomTileLocation = tileMap.getRowTileLocation((int) (y + height / 2) - 1);

		/*
		 * topLeft = (tileMap.getTile(topTile, leftTile) == 0); topRight =
		 * (tileMap.getTile(topTile, rightTile) == 0); bottomLeft =
		 * (tileMap.getTile(bottomTile, leftTile) == 0); bottomRight =
		 * (tileMap.getTile(bottomTile, rightTile) == 0);
		 */

		topLeftBlocked = tileMap.isBlocked(topTileLocation, leftTileLocation);
		topRightBlocked = tileMap.isBlocked(topTileLocation, rightTileLocation);
		bottomLeftBlocked = tileMap.isBlocked(bottomTileLocation, leftTileLocation);
		bottomRightBlocked = tileMap.isBlocked(bottomTileLocation, rightTileLocation);
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
	 * 
	 * IF SLIDING THE METHOD ENDS EARLY BECAUSE SLIDING OVERRIDES ALL OTHER MOVEMENT BUT FALLING
	 */
	private void howMuchMovement()
	{
		// find next position

		// Y position is found first due to jumping/sliding using same button

		// Are you pressing jump? (doesn't work while sliding)
		if(jumping && !sliding)
		{
			// Jump + Down makes player slide
			if(holdingDown)
			{
				sliding = true;

				// Begin slideDelay. This decides how long until slide ends - if
				// the slide doesn't ram a wall or fall off a cliff or something
				// first.
				slideDelay = 30; // Slide counter is in frames. 60 frames a
						 // second.
			}
			// Jumping
			else
			{

				// dy += jumpPower;
				yMovement = jumpPower;// The difference between these two is that
						      // the first
						      // one allows for ridiculously high
						      // double-jumps if
						      // infinite jumps is turned on. That's why the
						      // second
						      // one is the one that isn't commented out.

				falling = true;
			}
			jumping = false; // Can't jump when not on the ground!
		}
		if(falling) // Falling overrides jumping
		{
			// Can't slide while in the air.
			// REMOVE THIS FOR COOL JUMP-KICKS
			sliding = false;

			// Apply gravity - set player's fall speed to max fall speed if over max
			yMovement += gravity;
			if(yMovement > maxFallingSpeed)
			{
				yMovement = maxFallingSpeed;
			}
		}
		else
		// You must be on the ground then
		{
			yMovement = 0;
		}

		// X position next

		// Are you sliding?
		// Falling stops sliding (as does ramming walls, but that's in collision detection).
		// Cannot slide while in mid-air.
		if(sliding)
		{
			// Deincrement slide delay. When reaches 0 stops sliding. Sliding continues
			// for 1 additional frame.
			slideDelay--;

			// When delay reaches 0 megaMan stops sliding
			if(slideDelay == 0)
			{
				sliding = false;
			}

			// Which way are you sliding? (uses direction player is facing)
			if(facingLeft)
			{
				// Sliding makes you twice as fast!
				xMovement = -moveSpeed * 2;
			}
			else
			{
				// Sliding makes you twice as fast!
				xMovement = moveSpeed * 2;
			}
			return; // THIS IS BAD CODING
		}

		// Are you moving left or right or not at all?
		if(goingLeft)
		{
			// dx -= moveSpeed;
			xMovement = -moveSpeed;
		}
		else if(goingRight)
		{
			// dx += moveSpeed;
			xMovement = moveSpeed;
		}
		else
		{
			// How boring. No horizontal movement at all.
			xMovement = 0;
		}
		// Friction could be added here if necessary

	}

	/*
	 * checkCollisions * takes the current x and y coordinates and the current x acceleration
	 * and y acceleration and figures out the collisions, then finally moves the player
	 * appropriately. It takes all its stuff directly from the 'private x' area at the top.
	 */
	private void checkCollisions()
	{
		int currentColumn = tileMap.getColumnTileLocation((int) x);
		int currentRow = tileMap.getRowTileLocation((int) y);

		double goingToX = x + xMovement;
		double goingToY = y + yMovement;

		// Don't want to actually change the x and y before we know the collisions
		double tempX = x;
		double tempY = y;

		calculateCorners(x, goingToY);
		if(yMovement < 0)
		{
			if(topLeftBlocked || topRightBlocked)
			{
				yMovement = 0;
				tempY = currentRow * tileMap.getTileSize() + (height / 2); // Prevents
											   // getting
											   // stuck
											   // inside
											   // walls
			}
			else
			{
				tempY += yMovement;
			}
		}
		if(yMovement > 0)
		{
			if(bottomLeftBlocked || bottomRightBlocked)
			{
				yMovement = 0;
				falling = false;
				tempY = (currentRow + 1) * tileMap.getTileSize() - (height / 2); // Prevents
												 // getting
												 // stuck
												 // inside
												 // walls
			}
			else
			{
				tempY += yMovement;
			}
		}

		calculateCorners(goingToX, y);
		if(xMovement < 0)
		{
			if(topLeftBlocked || bottomLeftBlocked)
			{
				xMovement = 0;

				// Ramming walls stops your slide
				sliding = false;

				tempX = currentColumn * tileMap.getTileSize() + (width / 2); // Prevents
											     // getting
											     // stuck
											     // inside
											     // walls
			}
			else
			{
				tempX += xMovement;
			}
		}
		if(xMovement > 0)
		{
			if(topRightBlocked || bottomRightBlocked)
			{
				xMovement = 0;

				// Ramming walls stops your slide
				sliding = false;

				tempX = (currentColumn + 1) * tileMap.getTileSize() - (width / 2); // Prevents
												   // getting
												   // stuck
												   // inside
												   // walls
			}
			else
			{
				tempX += xMovement;
			}
		}

		if(!falling)
		{
			calculateCorners(x, y + 1);
			// If there are no solid tiles to the bottom left or right
			if(!bottomLeftBlocked && !bottomRightBlocked)
			{
				// Then the player is falling
				falling = true;
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
		// Cascade effect is important here.
		// Pay attention to the order and the else/if.

		if(usingWeapon && !sliding)
		{
			// Using weapon delay because megaman needs to hold out weapon continuously
			// if used recently
			usingWeaponDelay = 5; // Delay is in frames.
		}

		if(sliding)
		{
			animation.setFrames(slidingSprites);
			animation.setDelay(-1);

			// You stop holding out your weapon if you slide
			usingWeaponDelay = 0;
		}
		else if(falling)
		{
			if(usingWeaponDelay > 0)
			{
				animation.setFrames(inAirShootingSprites);
				animation.setDelay(-1);
				usingWeaponDelay--;
			}
			else
			{
				animation.setFrames(inAirSprites);
				animation.setDelay(-1);
			}
		}
		else if(goingLeft || goingRight)
		{
			if(usingWeaponDelay > 0)
			{
				// CHECK FOR WEAPON TYPE HERE? ~~~~~~~~~~~~~~~~~~~~
				animation.setFrames(walkingShootingSprites);
				animation.setDelay(200);
				usingWeaponDelay--;
			}
			else
			{
				animation.setFrames(walkingSprites);
				animation.setDelay(200);
			}
		}
		else if(usingWeaponDelay > 0)
		{
			// CHECK FOR WEAPON TYPE HERE? ~~~~~~~~~~~~~~~~~~~~
			animation.setFrames(shootingSprites);
			animation.setDelay(-1);
			usingWeaponDelay--;
		}
		else
		{
			animation.setFrames(idleSprites);
			animation.setDelay(350);
		}
		animation.update();

		if(xMovement < 0)
		{
			facingLeft = true;
		}
		if(xMovement > 0)
		{
			facingLeft = false;
		}

	}

	public void draw(Graphics2D graphic)
	{
		// For scrolling
		int tileOffsetX = tileMap.getX();
		int tileOffsetY = tileMap.getY();

		// Sprite graphics
		if(!facingLeft) // Right facing
		{
			graphic.drawImage(animation.getImage(), (int) (tileOffsetX + x) - 26,
					(int) (tileOffsetY + y) - 25, 50, 50, null);
		}
		else
		{
			// This one is the same as above, but flipped horizontally.
			// Left facing
			graphic.drawImage(animation.getImage(), (int) (tileOffsetX + x) - 23 + 50,
					(int) (tileOffsetY + y) - 25, -50, 50, null);
			// The additional 50 in X is to offset the -50 in width that was used to
			// flip the sprite
		}

		// Hitbox graphic
		graphic.setColor(Color.RED);

		// Use offset from drawing the player, since x and y are the middle of the player
		// and draw draws from the top-left.
		graphic.drawRect((int) (tileOffsetX + x - width / 2),
				(int) (tileOffsetY + y - height / 2), width, height);
	}
}
