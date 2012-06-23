package teampg199.datatypes.grid2d.point;

import java.awt.Dimension;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public abstract class Pos2D {
	public static enum Axis {X, Y};

	public final int x;
	public final int y;

	protected Pos2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public final int get(Axis axis) {
		switch (axis) {
		case X:
			return x;
		case Y:
			return y;
		default:
			throw new IllegalStateException("Unimplemented axis");
		}
	}

	@Override
	public boolean equals(Object what) {
		if (this == what) {
			return true;
		}

		if (this.getClass() != what.getClass()) {
			return false;
		}

		Pos2D other = (Pos2D) what;

		if (x != other.x) {
			return false;
		}

		if (y != other.y) {
			return false;
		}

		return true;
	}

	protected abstract <T extends Pos2D> T of(int x, int y, T ofType);

	@Override
	public String toString() {
		return "Pos2D [x=" + x + ", y=" + y + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	//============================//
	//  U T I L    M E T H O D S  //
	//============================//

	/**
	 * A is bigger than B if it's closer to goal.
	 */
	public static class DistanceComparator implements Comparator<Pos2D> {
		private final Pos2D goal;

		public DistanceComparator(Pos2D goal) {
			this.goal = goal;
		}

		@Override
		public int compare(Pos2D a, Pos2D b) {
			int aDist = Pos2D.squareDistance(a, goal);
			int bDist = Pos2D.squareDistance(b, goal);

			return aDist - bDist;
		}

	}

	public static class AxisSizeComparator implements Comparator<Pos2D> {
		private final Axis axis;

		public AxisSizeComparator(Axis axis) {
			this.axis = axis;
		}

		@Override
		public int compare(Pos2D a, Pos2D b) {
			return a.get(axis) - b.get(axis);
		}

	}

	public static double magnitude(RelPos vector) {
		int dx = Math.abs(vector.x);
		int dy = Math.abs(vector.y);

		return Math.hypot(dx, dy);
	}

	public static <T extends Pos2D> T offset(T absPos, RelPos offset) {
		int x = absPos.x + offset.x;
		int y = absPos.y + offset.y;

		return absPos.of(x, y, absPos);
	}

	public static <T extends Pos2D> T shiftCenterPosition(T newCenterPosition, T toShift) {
		int x = toShift.x - newCenterPosition.x;
		int y = toShift.y - newCenterPosition.y;

		return toShift.of(x, y, toShift);
	}

	public static <T extends Pos2D> int squareDistance(T a, T b) {
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
	}

	public static boolean inSquareRange(Pos2D toTest, int range) {
		if (Math.abs(toTest.x) > range) {
			return false;
		}

		if (Math.abs(toTest.y) > range) {
			return false;
		}

		return true;
	}

	/**
	 * Gets ring of points around center. Gap in middle is of radius radius. So
	 * a value of 1 would return:
	 *
	 * <pre>
	 *  OOO
	 *  O_O
	 *  OOO
	 * </pre>
	 */
	public static <T extends Pos2D> Collection<T> getRing(T near, int radius) {
		Set<T> points = new HashSet<>();

		int leftBoundary = near.x - radius;
		int rightBoundary = near.x + radius;

		int topBoundary = near.y - radius;
		int bottomBoundary = near.y + radius;

		// top and bottom
		for (int x = leftBoundary; x <= rightBoundary; x++) {
			addIfNotNull(near.of(x, topBoundary, near), points);
			addIfNotNull(near.of(x, bottomBoundary, near), points);
		}

		// left and right
		for (int y = topBoundary; y <= bottomBoundary; y++) {
			addIfNotNull(near.of(leftBoundary, y, near), points);
			addIfNotNull(near.of(rightBoundary, y, near), points);
		}

		return points;
	}

	private static <T extends Pos2D> void addIfNotNull(T toAdd, Collection<T> collect) {
		if (toAdd == null) {
			return;
		}

		collect.add(toAdd);
	}

	/**
	 * topLeft and bottomRight are in bounds (inclusive)
	 * @param toFilter Iterator must implement remove
	 * @return toFilter.  Changes collection in place.
	 */
	public static <T extends Pos2D> Collection<T> removeOutOfRectBounds(Collection<T> toFilter, int top, int right, int bottom, int left) {
		for (Iterator<T> iter = toFilter.iterator(); iter.hasNext();) {
			T p = iter.next();

			if ( !isInRectBounds(top, right, bottom, left, p)) {
				iter.remove();
			}
		}

		return toFilter;
	}

	/**
	 * topLeft and bottomRight are in bounds (inclusive)
	 */
	public static boolean isInRectBounds(int top, int right, int bottom, int left, Pos2D toTest) {
		if (toTest.x < left) {
			return false;
		}

		if (toTest.x > right) {
			return false;
		}

		if (toTest.y < top) {
			return false;
		}

		if (toTest.y > bottom) {
			return false;
		}

		return true;
	}

	/**
	 * Excludes near.
	 */
	public static <T extends Pos2D> Collection<T> getDiamondNear(T near, int radius) {
		Collection<T> pointsNear = new HashSet<>();

		int xi = near.x;
		int yi = near.y;

		int minXToTest = (xi - radius);
		int maxXToTest = (xi + radius);

		int minYToTest = (yi - radius);
		int maxYToTest = (yi + radius);

		for (int x = minXToTest; x <= maxXToTest; x++) {
			for (int y = minYToTest; y <= maxYToTest; y++) {
				T pos = near.of(x, y, near);

				// if this pos class doesn't want to make this point for some reason, don't add it
				// eg if it's a BoundedPos that's out of bounds
				if (pos == null) {
					continue;
				}

				// skip centre
				if (pos.equals(near)) {
					continue;
				}

				// inside radius?
				if (Pos2D.squareDistance(near, pos) > radius) {
					continue;
				}

				pointsNear.add(pos);
			}
		}

		return pointsNear;
	}

	/**
	 * From -1,-1, exclusive
	 */
	public static boolean isWithinDimensions(Pos2D toTest, Dimension size) {
		return isInRectBounds(0, size.width - 1, size.height - 1, 0, toTest);
	}
}