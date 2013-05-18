package teampg.datatypes.valuerange;

import teampg.datatypes.valuerange.ValueRangeMapper.Side;

import com.google.common.base.Objects;
import com.google.common.collect.Range;

public class RangePartition<D extends Comparable<D>> implements Comparable<D> {
	private final D position;
	private final Side closedSide;

	private RangePartition(D position, Side closedSide) {
		this.position = position;
		this.closedSide = closedSide;
	}

	@Override
	public int compareTo(D somePosition) {
		int positionDifference = position.compareTo(somePosition);
		if (positionDifference != 0) {
			return positionDifference;
		}

		// if range above partition owns this pos, other belongs in it.
		// otherwise belongs in range below this partition.
		switch (closedSide) {
		case LEFT:
			return 1;
		case RIGHT:
			return -1;
		default:
			throw new IllegalStateException();
		}
	}

	public D getPosition() {
		return position;
	}

	public Side getClosedSide() {
		return closedSide;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("pos", position).add("closed", closedSide)
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		@SuppressWarnings("unchecked")
		RangePartition<D> other = (RangePartition<D>) obj;
		return position.equals(other.position);
	}

	public static <D extends Comparable<D>> RangePartition<D> of(D position, Side closedSide) {
		// check D is valid range type
		Range.singleton(position);

		return new RangePartition<D>(position, closedSide);
	}
}
