package aPackage;

import java.awt.image.*;

/**
 * Class: Tile
 * 
 * @author James Timothy McCravy
 * 
 * 
 *         This class - has an image and a boolean. Both are set when constructed and can be
 *         obtained through get methods.
 * 
 *         Purpose: - represents a single tile out of a given tileset, with a specific image and if
 *         it is a background tile or a foreground tile.
 */
public class Tile
{
	private BufferedImage image;
	private boolean blocked;

	public Tile(BufferedImage image, boolean blocked)
	{
		this.image = image;
		this.blocked = blocked;
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public boolean isBlocked()
	{
		return blocked;
	}
}
