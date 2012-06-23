package teampg.grid2d.point;

public final class RelPos extends Pos2D {
	public static final RelPos ZERO       = new RelPos( 0,  0);
	public static final RelPos UP         = new RelPos( 0, -1);
	public static final RelPos UP_RIGHT   = new RelPos( 1, -1);
	public static final RelPos RIGHT      = new RelPos( 1,  0);
	public static final RelPos DOWN_RIGHT = new RelPos( 1,  1);
	public static final RelPos DOWN       = new RelPos( 0,  1);
	public static final RelPos DOWN_LEFT  = new RelPos(-1,  1);
	public static final RelPos LEFT       = new RelPos(-1,  0);
	public static final RelPos UP_LEFT    = new RelPos(-1, -1);

	private RelPos(int inX, int inY) {
		super(inX, inY);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends Pos2D> T of(int x, int y, T ofType) {
		if (! (ofType instanceof RelPos)) {
			throw new IllegalArgumentException("Of type must be RelPos");
		}

		return (T) new RelPos(x, y);
	}

	public static RelPos of(int x, int y) {
		return new RelPos(x, y);
	}
}
