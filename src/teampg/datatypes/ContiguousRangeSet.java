package teampg.datatypes;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.common.collect.Range;

/**
 * Maps values to different segments of one contiguous range. Similar to a
 * gradient, but without the fading.
 * <br><pre><code>
 * Example<Integer, String>: <b>[</b> means left is inclusive, <b>)</b> means right is exclusive
 * [0..1) => "None"
 * [1..2) => "One"
 * [2..3) => "Several"
 * [3..10) => "Lots"
 * [10..infinity] => "Many"
 * </code></pre>
 * @author Jackson Williams
 *
 * @param <D>
 *            Domain this range uses. For example ints or doubles.
 * @param <T>
 *            Value associated with each different section of the range
 */
public class ContiguousRangeSet<D extends Comparable<D>, T> {
	private enum Direction {
		UP(1), DOWN(-1);

		private final int index;

		Direction(int index) {
			this.index = index;
		}
	};

	private final Range<D> totalBounds;

	private final List<RangePartition<D>> partitions;
	private final List<T> values;

	public ContiguousRangeSet(Range<D> bounds, T initialValue) {
		totalBounds = bounds;

		partitions = new ArrayList<>();

		values = new ArrayList<>();
		values.add(initialValue); // at index 0
	}

	public Range<D> getBounds() {
		return totalBounds;
	}

	public List<RangePartition<D>> getPartitions() {
		return new ArrayList<RangePartition<D>>(partitions);
	}

	public List<T> getValues() {
		return new ArrayList<T>(values);
	}

	// ==================

	public T getValue(D atPos) {
		int rangeIndex = getRangeIndex(atPos);
		T value = values.get(rangeIndex);

		return value;
	}

	private int getRangeIndex(D atPos) {
		checkArgument(totalBounds.contains(atPos));

		int correctInsertionPoint = Collections.binarySearch(partitions, atPos);
		assert (correctInsertionPoint < 0); // partition compareTo never returns
											// "equals" (0)

		return -(correctInsertionPoint + 1);
	}

	// ==================

	public void movePartition(int index, D newValue) {
		checkElementIndex(index, partitions.size(), "Partition index out of bounds");
		checkArgument(totalBounds.contains(newValue), "New value out of bounds " + totalBounds);

		// TODO
	}

	// ==================

	public void setPartitionType(int index, Direction boundToSetClosed) {
		checkElementIndex(index, partitions.size(), "Partition index out of bounds");
		partitions.get(index).closedSide = boundToSetClosed;
	}

	// ==================

	public void partitionAddAbove(D splitPoint, Direction boundToSetClosed, T fillForNewRange) {
		partition(splitPoint, boundToSetClosed, fillForNewRange, Direction.UP);
	}

	public void partitionAddBelow(D splitPoint, Direction boundToSetClosed, T fillForNewRange) {
		partition(splitPoint, boundToSetClosed, fillForNewRange, Direction.DOWN);
	}

	private void partition(D splitPoint, Direction boundToSetClosed, T fillForNewRange,
			Direction positionForNewRange) {
		checkArgument(totalBounds.contains(splitPoint), "Split point out of bounds " + totalBounds);

		partitions.add(new RangePartition<D>(splitPoint, boundToSetClosed));

		int rangeToShrink = getRangeIndex(splitPoint);
		int insertionPoint = rangeToShrink + (positionForNewRange == Direction.UP ? 1 : 0);
		values.add(insertionPoint, fillForNewRange);
	}

	// ===================

	public void removePartitionDeleteRangeBelow(int partitionIndex) {
		removeRange(partitionIndex, Direction.DOWN);
	}

	public void removePartitionDeleteRangeAbove(int partitionIndex) {
		removeRange(partitionIndex, Direction.UP);
	}

	private void removeRange(int partitionIndex, Direction rangeToDelete) {
		// TODO
	}

	// ===================

	private static class RangePartition<D extends Comparable<D>> implements Comparable<D> {
		private final D position;
		private Direction closedSide;

		public RangePartition(D position, Direction closedSide) {
			this.position = position;
			this.closedSide = closedSide;
		}

		@Override
		public int compareTo(D other) {
			int positionDifference = position.compareTo(other);
			if (positionDifference != 0) {
				return positionDifference;
			}

			// if range above partition owns this pos, other belongs in it.
			// otherwise belongs in range below this partition.
			return -closedSide.index;
		}

		@SuppressWarnings("unused")
		public D getPosition() {
			return position;
		}

		@SuppressWarnings("unused")
		public Direction getClosedSide() {
			return closedSide;
		}
	}
}
