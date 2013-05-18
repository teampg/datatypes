package teampg.datatypes.valuerange;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import teampg.datatypes.valuerange.ValueRangeMapper.Side;

import com.google.common.collect.BoundType;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;

public class _ValueRangeMapperTest {

	static final String BOTTOM = "Bottom";
	static final String INIT = "Init";
	static final String MIDDLE = "Middle";
	static final String TOP = "Top";

	ValueRangeMapper<Double, String> empty;
	ValueRangeMapper<Double, String> filled;

	RangePartition<Double> leftPartition = RangePartition.of(-0.5D, Side.RIGHT);
	RangePartition<Double> middlePartition = RangePartition.of(0.0D, Side.LEFT);
	RangePartition<Double> rightPartition = RangePartition.of(0.5D, Side.RIGHT);

	@Before
	public void setUp() throws Exception {
		empty = new ValueRangeMapper<>(Range.closed(-1D, 1D), INIT);

		filled = new ValueRangeMapper<>(Range.closed(-1D, 1D), INIT);
		filled.addPartition(middlePartition, TOP, Side.RIGHT);
		filled.addPartition(rightPartition, MIDDLE, Side.LEFT);
		filled.addPartition(leftPartition, BOTTOM, Side.LEFT);
	}

	@Test
	public void testInit() {
		ValueRangeMapper<Double, String> expectedEmpty = new ValueRangeMapper.Builder<>(
				Range.closed(-1D, 1D), INIT)
				.build();
		assertEquals(expectedEmpty, empty);
		assertValid(empty);


		ValueRangeMapper<Double, String> expectedFilled = new ValueRangeMapper.Builder<>(
				Range.closed(-1D, 1D), BOTTOM)
				.add(RangePartition.of(-0.5D, Side.RIGHT), INIT)
				.add(RangePartition.of(0D, Side.LEFT), MIDDLE)
				.add(RangePartition.of(0.5D, Side.RIGHT), TOP)
		 		.build();
		assertEquals(expectedFilled, filled);
		assertValid(filled);
	}

	@Test
	public void testGetBounds() {
		assertEquals(Range.closed(-1D, 1D), empty.getBounds());
	}

	@Test
	public void testMovePartitionSingle() {
		// assumes testBuilder passes

		filled.movePartition(0, -0.4D);

		ValueRangeMapper<Double, String> expectedValRange = new ValueRangeMapper.Builder<>(
				Range.closed(-1D, 1D), BOTTOM)
				.add(RangePartition.of(-0.4D, Side.RIGHT), INIT)
				.add(RangePartition.of(0D, Side.LEFT), MIDDLE)
				.add(RangePartition.of(0.5D, Side.RIGHT), TOP).build();

		assertEquals(expectedValRange, filled);
	}

	@Test
	public void testMovePartitionMultiple() {
		// assumes testBuilder passes

		filled.movePartition(0, -0.4D);
		filled.movePartition(1, 0.1D);
		filled.movePartition(2, 0.2D);

		ValueRangeMapper<Double, String> expectedValRange = new ValueRangeMapper.Builder<>(
				Range.closed(-1D, 1D), BOTTOM)
				.add(RangePartition.of(-0.4D, Side.RIGHT), INIT)
				.add(RangePartition.of(0.1D, Side.LEFT), MIDDLE)
				.add(RangePartition.of(0.2D, Side.RIGHT), TOP).build();

		assertEquals(expectedValRange, filled);
	}

	@Test
	public void testMovePartitionExceptions() {
		// should throw exceptions:
		// moving a partition out of bounds
		// moving a partition such that it changes partition order

		// TODO
		fail("Not yet implemented");
	}

	@Test
	public void testSetPartitionType() {
		int partToTest = 0;
		Double partPos = filled.getPartitions().get(partToTest).getPosition();

		// closed side is right, so pos of partition is owned by range to its right (INIT)
		assertEquals(INIT, filled.getValue(partPos));
		assertEquals(Side.RIGHT, filled.getPartitions().get(partToTest).getClosedSide());

		// changed closed side
		filled.setPartitionType(partToTest, Side.LEFT);

		// partPos should now belong to range on left (BOTTOM)
		assertEquals(BOTTOM, filled.getValue(partPos));
		assertEquals(Side.LEFT, filled.getPartitions().get(partToTest).getClosedSide());
	}

	@Test
	public void testAddPartition() {
		empty.addPartition(middlePartition, TOP, Side.RIGHT);
		assertValid(empty);

		empty.addPartition(rightPartition, MIDDLE, Side.LEFT);
		assertValid(empty);

		empty.addPartition(leftPartition, BOTTOM, Side.LEFT);
		assertValid(empty);

		fail("TODO make test more complete by checking with builder"); //TODO
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetPartitionsValues() {
		assertEquals(Lists.newArrayList(INIT), empty.getValues());
		assertEquals(Lists.newArrayList(), empty.getPartitions());

		assertEquals(Lists.newArrayList(BOTTOM, INIT, MIDDLE, TOP),
				filled.getValues());
		assertEquals(Lists.newArrayList(leftPartition, middlePartition, rightPartition),
				filled.getPartitions());
	}

	@Test
	public void testRemovePartitionRangeRight() {
		//remove Initial Fill value section, filling with BOTTOM
		filled.removePartition(0, Side.RIGHT);

		ValueRangeMapper<Double, String> expected = new ValueRangeMapper.Builder<Double, String>(
				Range.closed(-1D, 1D), BOTTOM).add(RangePartition.of(0D, Side.LEFT), MIDDLE)
				.add(RangePartition.of(0.5D, Side.RIGHT), TOP).build();
		assertEquals(expected, filled);
	}

	@Test
	public void testRemovePartitionRangeLeft() {
		//remove Bottom value section, filling with INIT
		filled.removePartition(0, Side.LEFT);

		ValueRangeMapper<Double, String> expected = new ValueRangeMapper.Builder<Double, String>(
				Range.closed(-1D, 1D), INIT)
				.add(RangePartition.of(0D, Side.LEFT), MIDDLE)
				.add(RangePartition.of(0.5D, Side.RIGHT), TOP).build();
		assertEquals(expected, filled);
	}

	@Test
	public void testRemovePartitionRangeTop() {
		//remove Bottom value section, filling with INIT
		filled.removePartition(2, Side.RIGHT);

		ValueRangeMapper<Double, String> expected = new ValueRangeMapper.Builder<Double, String>(
				Range.closed(-1D, 1D), BOTTOM)
				.add(RangePartition.of(-0.5D, Side.RIGHT), INIT)
				.add(RangePartition.of(0D, Side.LEFT), MIDDLE).build();
		assertEquals(expected, filled);
	}

	@Test
	public void testBuilder() {
		ValueRangeMapper.Builder<Double, String> myBuilder = new ValueRangeMapper.Builder<>(
				Range.closed(-1D, 1D), BOTTOM)
				.add(RangePartition.of(-0.5D, Side.RIGHT), INIT)
				.add(RangePartition.of(0D, Side.LEFT), MIDDLE)
				.add(RangePartition.of(0.5D, Side.RIGHT), TOP);
		ValueRangeMapper<Double, String> sameAsValRange = myBuilder.build();

		assertEquals(filled, sameAsValRange);

		//building multiple times returns different instances
		assertEquals(sameAsValRange, myBuilder.build());
		assertFalse(sameAsValRange == myBuilder.build());
	}

	@Test
	public void testEquals() {
		//should test by bounds, partitions, and values.
		//		assertEquals(valRange.getBounds(), 		sameAsValRange.getBounds());
		//		assertEquals(valRange.getPartitions(), 	sameAsValRange.getPartitions());
		//		assertEquals(valRange.getValues(), 		sameAsValRange.getValues());

		// above is equivalent to below
		//		assertEquals(valRange, sameAsValRange);
		//TODO
		fail("Not yet implemented");
	}

	static <T> void assertValid(ValueRangeMapper<Double, T> valRange) {
		assertValid(valRange, 0.1D);
	}

	static <T> void assertValid(ValueRangeMapper<Double, T> valRange,
			Double checkInterval) {
		List<Range<Double>> mapSegments = convertValueRangeMapperToRangeList(valRange);

		for (int i = 0; i < mapSegments.size(); i++) {
			Range<Double> segment = mapSegments.get(i);
			T segmentValue = valRange.getValues().get(i);

			assertValues(valRange, segment, segmentValue, checkInterval);
		}
	}

	// TODO consider if this might be used elsewhere, put it into ValueRangeMapper class if so.
	public static List<Range<Double>> convertValueRangeMapperToRangeList(
			ValueRangeMapper<Double, ?> valRange) {
		List<Range<Double>> ret = new ArrayList<>();

		for (int i = 0; i < valRange.getValues().size(); i++) {
			Double floor;
			BoundType floorType;
			if (i == 0) {
				floor = valRange.getBounds().lowerEndpoint();
				floorType = valRange.getBounds().lowerBoundType();
			} else {
				floor = valRange.getPartitions().get(i - 1).getPosition();
				floorType = (valRange.getPartitions().get(i - 1).getClosedSide() == Side.RIGHT) ? BoundType.CLOSED
						: BoundType.OPEN;
			}

			Double ceiling;
			BoundType ceilingType;
			if (i == valRange.getValues().size() - 1) {
				ceiling = valRange.getBounds().upperEndpoint();
				ceilingType = valRange.getBounds().upperBoundType();
			} else {
				ceiling = valRange.getPartitions().get(i).getPosition();
				ceilingType = (valRange.getPartitions().get(i).getClosedSide() == Side.LEFT) ? BoundType.CLOSED
						: BoundType.OPEN;
			}

			ret.add(Range.range(floor, floorType, ceiling, ceilingType));
		}

		return ret;
	}

	private static <T> void assertValues(ValueRangeMapper<Double, T> valRange, Range<Double> in,
			T expected, Double checkInterval) {
		// check leftmost point
		Double cursor = in.lowerEndpoint();
		if (in.lowerBoundType().equals(BoundType.OPEN)) {
			cursor += 0.0000000001D;
		}

		// check fill points
		while (in.contains(cursor)) {
			assertEquals(expected, valRange.getValue(cursor));
			cursor += checkInterval;
		}

		// check rightmost point
		cursor = in.upperEndpoint();
		if (in.upperBoundType().equals(BoundType.OPEN)) {
			cursor -= 0.0000000001D;
		}
		assertEquals(expected, valRange.getValue(cursor));
	}
}
