package aPackage;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import startUp.GamePanel;

public class TileMap
{
	private int x;
	private int y;

	private int tileSize;
	private int[][] map;
	private int mapWidth;
	private int mapHeight;

	private BufferedReader reader;
	private BufferedReader backgroundReader;
	private String delimeter = "\\s+"; // Need to do this for numbers greater than 10 otherwise
					   // it would read it as 1, then 0 and screw everything up.
					   // \\s+ means any white space.

	private BufferedImage tileSet;
	private String tileSetLocation = "graphics/tilesets/";

	private Tile tiles[][];

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

			// The first two lines of the file MUST be the WIDTH, then HEIGHT of the
			// map. The actual map comes afterwards.
			mapWidth = Integer.parseInt(reader.readLine());
			mapHeight = Integer.parseInt(reader.readLine());

			minX = GamePanel.width - mapWidth * tileSize;
			minY = GamePanel.height - mapHeight * tileSize;

			map = new int[mapHeight][mapWidth]; // Y, then X

			for(int row = 0; row < mapHeight; row++)
			{
				String currentLine = reader.readLine();
				String[] tokens = currentLine.split(delimeter);

				for(int column = 0; column < mapWidth; column++)
				{
					map[row][column] = Integer.parseInt(tokens[column]);
				}
			}

		} catch (Exception e)
		{
			System.out.println("The tile-map file was not found.");
		}

	}

	public void loadTiles(String tileSetName, boolean onePixelOffset)
	{
		try
		{
			tileSet = ImageIO.read(new File(tileSetLocation + tileSetName));

			int numTilesAcross;
			if(onePixelOffset)
			{
				// If the tileset being used has 1 pixel of white-space squares
				// surrounding all the tiles the +1's are gonna get rid of that.
				numTilesAcross = (tileSet.getWidth() + 1) / (tileSize + 1);
			}
			else
			{
				numTilesAcross = (tileSet.getWidth() / tileSize);
			}

			tiles = new Tile[2][numTilesAcross];

			BufferedImage subImage;
			for(int column = 0; column < numTilesAcross; column++)
			{
				subImage = tileSet.getSubimage(column * tileSize + column, 0,
						tileSize, tileSize);
				tiles[0][column] = new Tile(subImage, false);

				// The prototype tileset stuff is set up so that all the blocked
				// things are on the second row. Also only two available rows.
				subImage = tileSet.getSubimage(column * tileSize + column,
						tileSize + 1, tileSize, tileSize);
				tiles[1][column] = new Tile(subImage, true);
			}

		} catch (Exception e)
		{
			System.out.println("tile load error");
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

	public int getColumnTile(int x)
	{
		return x / tileSize;
	}

	public int getRowTile(int y)
	{
		return y / tileSize;
	}

	public int getTile(int row, int column)
	{
		return map[row][column];
	}

	public int getTileSize()
	{
		return tileSize;
	}

	public boolean isBlocked(int row, int column)
	{
		int currentTile = map[row][column];

		int r = currentTile / tiles[0].length;
		int c = currentTile % tiles[0].length;

		return tiles[r][c].isBlocked();
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
		 * graphics.fillRect(x + column * tileSize, y + row * tileSize, tileSize, tileSize);
		 * } }
		 */

		// Same as above, but with tileset included.
		for(int row = 0; row < mapHeight; row++)
		{
			for(int column = 0; column < mapWidth; column++)
			{
				int currentTile = map[row][column];

				int r = currentTile / tiles[0].length;
				int c = currentTile % tiles[0].length;

				graphics.drawImage(tiles[r][c].getImage(), x + column * tileSize, y
						+ row * tileSize, null);
			}
		}
	}
}
