package teampg.grid2d.chunkgrid;


import java.awt.Dimension;

import com.google.common.base.Objects;

import teampg.grid2d.point.AbsPos;
import teampg.grid2d.point.Pos2D;


/**
 * The position of a specific Cell in a specific Chunk on some {@link ChunkedGrid}.
 * <br /><br />
 * Note that 0,0 is in the positive x and y quadrant (which is bottom right).
 * @author Jackson Williams
 */
public class GlobalPos extends Pos2D {
	private final ChunkPos chunkPos;
	private final AbsPos innerPos;
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

		innerPos = AbsPos.of(innerX, innerY);
	}

	private GlobalPos(ChunkPos chunkPos, AbsPos innerPos, Dimension chunkSize) {
		super((chunkPos.x * chunkSize.width) + innerPos.x,
				(chunkPos.y * chunkSize.height) + innerPos.y);

		this.chunkPos = chunkPos;
		this.innerPos = innerPos;
		this.chunkSize = chunkSize;
	}

	public ChunkPos getChunkComponent() {
		return chunkPos;
	}

	public AbsPos getInnerComponent() {
		return innerPos;
	}

	public Dimension getChunkSize() {
		return (Dimension) chunkSize.clone();
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

	public static GlobalPos of(ChunkPos chunkPos, AbsPos innerPos, Dimension chunkSize) {
		return new GlobalPos(chunkPos, innerPos, chunkSize);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("Chunk", chunkPos)
				.add("Inner", innerPos)
				.add("ChunkSize", chunkSize)
				.toString();
	}
}
