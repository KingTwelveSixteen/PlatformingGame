package aPackage;

import aPackage.Tile;
import startUp.GamePanel;

import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class TileMap
{
	private int x;
	private int y;

	private int tileSize;
	private int[][] tileIDMap;
	private int mapWidth;
	private int mapHeight;

	private BufferedReader reader;
	// private BufferedReader backgroundReader;

	private String delimeter = "\\s+|\\n"; // Need to do this for numbers greater than 10 otherwise
										   // it would read it as 1, then 0 and screw everything up.
										   // \\s+ means any white space.

	private BufferedImage fullTilesetImage;
	private String tileSetLocation = "graphics/tilesets/";

	private ArrayList<Tile> tilesByID;

	// These values are for the edge of the map: the screen will not scroll past these points.
	private int minX;
	private int minY;
	private int maxX = 0;
	private int maxY = 0;

	public TileMap(String tileMapName, int tileSize)
	{
		this.tileSize = tileSize;

		try
		{
			reader = new BufferedReader(new FileReader(tileMapName + ".txt"));

			// The first two lines of the file MUST be the WIDTH, then HEIGHT of the map. The actual
			// map comes afterwards.
			mapWidth = Integer.parseInt(reader.readLine());
			mapHeight = Integer.parseInt(reader.readLine());

			minX = GamePanel.width - (mapWidth * tileSize);
			minY = GamePanel.height - (mapHeight * tileSize);

			tileIDMap = new int[mapHeight][mapWidth]; // Y, then X

			for(int currentRow = 0; currentRow < mapHeight; currentRow++)
			{
				int currentColumn = 0;
				while(currentColumn < mapWidth)
				{
					// Read one line from file...
					String currentLine = reader.readLine();

					// Split it by the delimeter to get the individual tile IDs as string...
					String[] aLineOfTileIDs = currentLine.split(delimeter);

					int currentIDFromCurrentLine = 0;
					while(aLineOfTileIDs.length > currentIDFromCurrentLine)
					{
						// Put those numbers in the tileIDMap as ints
						tileIDMap[currentRow][currentColumn] = Integer
								.parseInt(aLineOfTileIDs[currentIDFromCurrentLine]);

						// Increment what column the current tile ID is on.
						currentColumn++;

						// Increment what tile ID from the current line is being used.
						currentIDFromCurrentLine++;
					}
					// When done with all things from current line, check to see if that line
					// actually contained everything from the row. If not, repeat everything after
					// currentColumn = 0.
				}
			}

			// While not read as many tiles as the total number of tiles...
			/*
			 * while(tilesRead < mapWidth * mapHeight) { // Read one more of the lines... String
			 * currentLine = reader.readLine();
			 * 
			 * // Split it by the delimeter to get the tile numbers... String[] singleTiles =
			 * currentLine.split(delimeter);
			 * 
			 * // Then add every number on the line to the array of tiles. for(int i = 0; i <
			 * singleTiles.length; i++) { stringTilesArray[tilesRead] = singleTiles[i];
			 * 
			 * tilesRead++; // Remember to increase the counter of how many tiles have been // read
			 * so far, or this goes on forever! } // Repeat as necessary. Thus, the while loop. }
			 * 
			 * for(int row = 0; row < mapHeight; row++) { for(int column = 0; column < mapWidth;
			 * column++) { tileIDMap[row][column] = Integer.parseInt(stringTilesArray[column]); } }
			 */

		} catch (FileNotFoundException e)
		{
			System.out.println("Tilemap not found exception in TileMap.");
		} catch (NumberFormatException e)
		{
			System.out.println("Number format exception in TileMap constructor");
		} catch (IOException e)
		{
			System.out.println("IO Exceeption in TileMap constructor");
		}
	}

	public void loadTiles(String tileSetName, int pixelOffset)
	{
		int actualPixelOffset = pixelOffset * 2;
		try
		{
			fullTilesetImage = ImageIO.read(new File(tileSetLocation + tileSetName));

			int numTilesAcross = fullTilesetImage.getWidth() / (tileSize + actualPixelOffset);
			int numTilesDown = fullTilesetImage.getHeight() / (tileSize + actualPixelOffset);

			// Woo, initialize some stuff for the following loops
			tilesByID = new ArrayList<Tile>();
			BufferedImage currentTileImage;

			for(int row = 0; row < numTilesDown; row++)
			{
				for(int column = 0; column < numTilesAcross; column++)
				{
					// Grabbing a single tile's image from the main image. Pixel offset is used to
					// ignore the extra pixels.
					currentTileImage = fullTilesetImage.getSubimage((column * tileSize)
							+ (actualPixelOffset * column), (row * tileSize)
							+ (actualPixelOffset * row), tileSize, tileSize);

					// The prototype tileset stuff is set up so that all the blocked things are on a
					// row other than the first. The top row contains all background elements
					// (currently).
					if(row == 0)
					{
						Tile currentTile = new Tile(currentTileImage, false);
						tilesByID.add(currentTile);
					}
					else
					{
						Tile currentTile = new Tile(currentTileImage, true);
						tilesByID.add(currentTile);
					}
				}
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getColumnTileLocation(int x)
	{
		return x / tileSize;
	}

	public int getRowTileLocation(int y)
	{
		return y / tileSize;
	}

	public int getTileIDAt(int row, int column)
	{
		return tileIDMap[row][column];
	}

	public int getTileSize()
	{
		return tileSize;
	}

	// The commented code is old stuff, it is kept because I don't remember why I did it that
	// way, so there might be an actual reason to switch back.
	public boolean isBlocked(int yLocation, int xLocation)
	{
		// return tiles[row][column].isBlocked();

		int checkingThisTileID = tileIDMap[yLocation][xLocation];

		return tilesByID.get(checkingThisTileID).isBlocked();
	}

	public void setX(int num)
	{
		if(num > maxX)
		{
			x = maxX;
		}
		else if(num < minX)
		{
			x = minX;
		}
		else
		{
			x = num;
		}
	}

	public void setY(int num)
	{
		if(num > maxY)
		{
			y = maxY;
		}
		else if(num < minY)
		{
			y = minY;
		}
		else
		{
			y = num;
		}
	}

	// //////////////////////////////////////////////////////////

	public void update()
	{

	}

	public void draw(Graphics2D graphics)
	{
		// Go through all the tiles and draw their colors in at their specific locations
		/*
		 * for(int row = 0; row < mapHeight; row++) { for(int column = 0; column < mapWidth;
		 * column++) { int currentTile = map[row][column];
		 * 
		 * if(currentTile == 0) { graphics.setColor(Color.BLACK); } if(currentTile == 1) {
		 * graphics.setColor(Color.WHITE); }
		 * 
		 * graphics.fillRect(x + column * tileSize, y + row * tileSize, tileSize, tileSize); } }
		 */

		// Same as above, but with tileset included.
		for(int row = 0; row < mapHeight; row++)
		{
			for(int column = 0; column < mapWidth; column++)
			{
				int currentTileID = tileIDMap[row][column];

				graphics.drawImage(tilesByID.get(currentTileID).getImage(), x + column * tileSize,
						y + row * tileSize, null);
			}
		}
	}
}
