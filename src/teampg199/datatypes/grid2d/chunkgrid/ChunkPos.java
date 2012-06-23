package teampg199.datatypes.grid2d.chunkgrid;

import teampg199.datatypes.grid2d.point.Pos2D;

public class ChunkPos extends Pos2D {
	private ChunkPos(int x, int y) {
		super(x, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends Pos2D> T of(int x, int y, T ofType) {
		if (! (ofType instanceof ChunkPos)) {
			throw new IllegalArgumentException("Of type must be ChunkPos");
		}

		return (T) new ChunkPos(x, y);
	}

	public static ChunkPos of(int x, int y) {
		return new ChunkPos(x, y);
	}
}