package teampg.datatypes.valuerange;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
public class ValueRangeMapper<D extends Comparable<D>, T> {
	public enum Side {
		RIGHT, LEFT;
	};

	private final Range<D> totalBounds;

	private final List<RangePartition<D>> partitions;
	private final List<T> values;

	public ValueRangeMapper(Range<D> bounds, T initialValue) {
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

		RangePartition<D> toReplace = partitions.get(index);
		if (boundToSetClosed == toReplace.getClosedSide()) {
			return;
		}

		RangePartition<D> changedBound = RangePartition.of(toReplace.getPosition(), boundToSetClosed);
		partitions.add(index, changedBound);
		partitions.remove(index + 1);
	}

	// ==================

	public void addPartition(RangePartition<D> toAdd, T fillForNewRange, Side toOverwrite) {
		checkArgument(totalBounds.contains(toAdd.getPosition()), "Split point out of bounds " + totalBounds);
		checkArgument(!partitions.contains(toAdd));

		/*
		 * FIXME throws error when you try to add partition at same pos as
		 * another. Would be untested edge case in GUI.... be sure to remember
		 * this when implementing gui component
		 */

		int valueRangeToShrink = indexOfRangeOwningPosition(toAdd.getPosition());
		int valuesInsertionPoint = valueRangeToShrink + (toOverwrite == Side.RIGHT ? 1 : 0);

		// add the new value and partition that marks its end or start
		forceListInsert(values, valuesInsertionPoint, fillForNewRange);
		forceListInsert(partitions, valueRangeToShrink, toAdd);
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

	public void removePartition(int partitionIndex, Side valueToRemove) {
		// TODO
	}

	// ===================

	@Override
	public boolean equals(Object obj) {
		@SuppressWarnings("unchecked")
		ValueRangeMapper<D, T> other = (ValueRangeMapper<D, T>) obj;

		if (!totalBounds.equals(other.totalBounds)) {
			return false;
		}

		if (!partitions.equals(other.partitions)) {
			return false;
		}

		return values.equals(other.values);
	}

	@Override
	public int hashCode() {
		return Objects.hash(totalBounds, partitions, values);
	}

	// ===================

	/**
	 * Exposes fluent syntax for building an instance of the containing class for convenience.
	 * <br />
	 * Safe to reuse to build multiple instances.
	 */
	public static class Builder<D extends Comparable<D>, T> {
		private final Range<D> bounds;
		private final T lowestValue;

		private final List<T> values;
		private final List<RangePartition<D>> partitions;

		public Builder(Range<D> bounds, T lowestValue) {
			this.bounds = bounds;
			this.lowestValue = lowestValue;

			values = new ArrayList<>();
			partitions = new ArrayList<>();
		}

		public Builder<D, T> add(RangePartition<D> floor, T value) {
			partitions.add(floor);
			values.add(value);

			return this;
		}

		public ValueRangeMapper<D, T> build() {
			ValueRangeMapper<D, T> inst = new ValueRangeMapper<>(bounds, lowestValue);

			for(int i = 0; i < partitions.size(); i++) {
				inst.addPartition(partitions.get(i), values.get(i), Side.RIGHT);
			}

			return inst;
		}
	}

}
