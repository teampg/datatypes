package teampg.grid2d.point;

import java.util.Comparator;

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

	/**
	 * Smaller magnitude is better (smaller)
	 */
	public static Comparator<RelPos> byMagnitude() {
		return new Comparator<RelPos>() {
			@Override
			public int compare(RelPos a, RelPos b) {
				double magA = Pos2D.magnitude(a);
				double magB = Pos2D.magnitude(b);
				return Double.compare(magA, magB);
			}
		};
	}

	public int squareMagnitude() {
		return Math.abs(x) + Math.abs(y);
	}

	//TODO test
	public RelPos unitVector() {
		float hypot = (float) Math.hypot(x, y);

		float unitX = x/hypot;
		float unitY = y/hypot;

		return new RelPos(Math.round(unitX), Math.round(unitY));
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

	public static <T extends Pos2D> RelPos offsetVector(T origin, T offset) {
		return Pos2D.shiftCenterPosition(new RelPos(origin.x, origin.y), new RelPos(offset.x, offset.y));
	}
}
