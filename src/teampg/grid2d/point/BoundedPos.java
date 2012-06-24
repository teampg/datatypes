package teampg.grid2d.point;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.Dimension;


public class BoundedPos extends Pos2D {
	private final Dimension bounds;

	private BoundedPos(int x, int y, Dimension bounds) {
		super(x, y);

		this.bounds = bounds;
	}

	public Dimension getChunkSize() {
		return (Dimension) bounds.clone();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends Pos2D> T of(int x, int y, T ofType) {
		checkArgument(ofType instanceof BoundedPos, "Of type must be BoundedPos");

		BoundedPos ret = new BoundedPos(x, y, ((BoundedPos)ofType).bounds);
		if (ret.isValid() == false) {
			return null;
		}

		return (T) ret;
	}

	public static BoundedPos of(int x, int y, Dimension chunkSize) throws PosOutOfBoundsException {
		BoundedPos ret = new BoundedPos(x, y, chunkSize);
		if (!ret.isValid()) {
			throw new PosOutOfBoundsException(ret.bounds, ret);
		}

		return ret;
	}

	private boolean isValid() {
		return Pos2D.isWithinDimensions(this, bounds);
	}

	public static class PosOutOfBoundsException extends Error {
		private static final long serialVersionUID = 1L;

		private final Dimension bounds;
		private final Pos2D pos;

		public PosOutOfBoundsException(Dimension bounds, Pos2D pos) {
			this.bounds = bounds;
			this.pos = pos;
		}

		public Dimension getBounds() {
			return bounds;
		}

		public Pos2D getPos() {
			return pos;
		}
	}
}
