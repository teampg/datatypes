package teampg199.datatypes.grid2d.chunkgrid;

import java.awt.Dimension;

import teampg199.datatypes.grid2d.point.BoundedPos;
import teampg199.datatypes.grid2d.point.Pos2D;

/**
 * The position of a specific Cell in a specific Chunk on some {@link ChunkedGrid}.
 * <br /><br />
 * Note that 0,0 is in the positive x and y quadrant (which is bottom right).
 * @author Jackson Williams
 */
public class GlobalPos extends Pos2D {
	private final ChunkPos chunkPos;
	private final BoundedPos innerPos;
	private final Dimension chunkSize;

	private GlobalPos(int x, int y, Dimension inChunkSize) {
		super(x, y);
		chunkSize = (Dimension) inChunkSize.clone();

		int chunkX;
		if (x >= 0) {
			chunkX = (x / chunkSize.width);
		} else {
			chunkX = ((x + 1) / chunkSize.width) - 1;
		}

		int chunkY;
		if (y >= 0) {
			chunkY = (y / chunkSize.height);
		} else {
			chunkY = ((y + 1) / chunkSize.height) - 1;
		}
		chunkPos = ChunkPos.of(chunkX, chunkY);

		int innerX;
		if (x >= 0) {
			innerX = x % chunkSize.width;
		} else {
			innerX = x - (chunkX * chunkSize.width);
		}

		int innerY;
		if (y >= 0) {
			innerY = y % chunkSize.height;
		} else {
			innerY = y - (chunkY * chunkSize.height);
		}

		innerPos = BoundedPos.of(innerX, innerY, chunkSize);
	}

	private GlobalPos(ChunkPos chunkPos, BoundedPos innerPos) {
		super((chunkPos.x * innerPos.getChunkSize().width) + innerPos.x,
				(chunkPos.y * innerPos.getChunkSize().height) + innerPos.y);

		this.chunkPos = chunkPos;
		this.innerPos = innerPos;
		chunkSize = (Dimension)innerPos.getChunkSize().clone();
	}

	public ChunkPos getChunkComponent() {
		return chunkPos;
	}

	public BoundedPos getInnerComponent() {
		return innerPos;
	}


	@SuppressWarnings("unchecked")
	@Override
	protected <T extends Pos2D> T of(int x, int y, T ofType) {
		if (! (ofType instanceof GlobalPos)) {
			throw new IllegalArgumentException("Of type must be GlobalPos");
		}

		return (T) new GlobalPos(x, y, ((GlobalPos)ofType).chunkSize);
	}

	public static GlobalPos of(int x, int y, Dimension chunkSize) {
		return new GlobalPos(x, y, chunkSize);
	}

	public static GlobalPos of(ChunkPos chunkPos, BoundedPos innerPos) {
		return new GlobalPos(chunkPos, innerPos);
	}

	@Override
	public String toString() {
		String ret = "global:" + super.toString();

		ret += "\tchunk:";
		ret += chunkPos;
		ret += ", \tinner:";
		ret += innerPos;

		return ret;
	}
}
