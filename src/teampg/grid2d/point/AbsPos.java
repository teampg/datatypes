package teampg.grid2d.point;

public final class AbsPos extends Pos2D {

	private AbsPos(int inX, int inY) {
		super(inX, inY);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends Pos2D> T of(int x, int y, T ofType) {
		if (! (ofType instanceof AbsPos)) {
			throw new IllegalArgumentException("Of type must be AbsPos");
		}

		return (T) new AbsPos(x, y);
	}

	public static AbsPos of(int x, int y) {
		return new AbsPos(x, y);
	}
}
