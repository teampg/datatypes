package teampg.datatypes;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.Range;

/**
 * Maps values to different segments of one contiguous range. Similar to a
 * gradient, but without the fading. <br>
 *
 * <pre>
 * <code>
 * Example<Integer, String>: <b>[</b> means left is inclusive, <b>)</b> means right is exclusive
 * [0..1) => "None"
 * [1..2) => "One"
 * [2..3) => "Several"
 * [3..10) => "Lots"
 * [10..infinity] => "Many"
 * </code>
 * </pre>
 *
 * @author Jackson Williams
 *
 * @param <D>
 *            Domain this range uses. For example ints or doubles.
 * @param <T>
 *            Value associated with each different section of the range
 */
public class ContiguousRangeSet<D extends Comparable<D>, T> {
	public enum Side {
		RIGHT(1), LEFT(-1);

		private final int index;

		Side(int index) {
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
		int rangeIndex = indexOfRangeOwningPosition(atPos);
		T value = values.get(rangeIndex);

		return value;
	}

	private int indexOfRangeOwningPosition(D atPos) {
		checkArgument(totalBounds.contains(atPos));

		int positionOfRangeOwningAtPos = -(Collections.binarySearch(partitions, atPos) + 1);
		return positionOfRangeOwningAtPos;
	}

	// ==================

	public void movePartition(int index, D newValue) {
		checkElementIndex(index, partitions.size(), "Partition index out of bounds");
		checkArgument(totalBounds.contains(newValue), "New value out of bounds " + totalBounds);

		// TODO
	}

	public void moveValueRange(int index, int newIndex) {
		// TODO
		// NOTE should also swap dimensions (meaning partitions...)
	}

	// ==================

	public void setPartitionType(int index, Side boundToSetClosed) {
		checkElementIndex(index, partitions.size(), "Partition index out of bounds");
		partitions.get(index).closedSide = boundToSetClosed;
	}

	// ==================

	public void addPartition(D splitPoint, Side toClose, T fillForNewRange, Side toOverwrite) {
		checkArgument(totalBounds.contains(splitPoint), "Split point out of bounds " + totalBounds);
		checkArgument(!partitions.contains(new RangePartition<D>(splitPoint, null)));

		/*
		 * FIXME throws error when you try to add partition at same pos as
		 * another. Would be untested edge case in GUI.... be sure to remember
		 * this when implementing gui component
		 */

		int valueRangeToShrink = indexOfRangeOwningPosition(splitPoint);
		int valuesInsertionPoint = valueRangeToShrink + (toOverwrite == Side.RIGHT ? 1 : 0);

		// add the new value and partition that marks its end or start
		forceListInsert(values, valuesInsertionPoint, fillForNewRange);
		forceListInsert(partitions, valueRangeToShrink, new RangePartition<D>(splitPoint, toClose));
	}

	/**
	 * Inserts some value into a list, or adds it if insertion index is at end
	 * of list
	 */
	private <Q> void forceListInsert(List<Q> list, int index, Q toAdd) {
		checkArgument(index < list.size() + 1);

		if (index == list.size()) {
			list.add(toAdd);
			return;
		}

		list.add(index, toAdd);
	}

	// ===================

	public void removeValueRange(int partitionIndex, Side rangeToDelete) {
		// TODO
	}

	// ===================

	private static class RangePartition<D extends Comparable<D>> implements Comparable<D> {
		private final D position;
		private Side closedSide;

		public RangePartition(D position, Side closedSide) {
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
	}
}
