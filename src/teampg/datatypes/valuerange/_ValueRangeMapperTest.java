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
import com.google.common.collect.Ranges;

public class _ValueRangeMapperTest {
	ValueRangeMapper<Double, String> valRange;

	RangePartition<Double> leftPartition = RangePartition.of(-0.5D, Side.RIGHT);
	RangePartition<Double> middlePartition = RangePartition.of(0.0D, Side.LEFT);
	RangePartition<Double> rightPartition = RangePartition.of(0.5D, Side.RIGHT);

	@Before
	public void setUp() throws Exception {
		valRange = new ValueRangeMapper<>(Ranges.closed(-1D, 1D), "Initial Fill");
	}

	@Test
	public void testInit() {
		assertEquals(1, valRange.getValues().size());
		assertEquals("Initial Fill", valRange.getValues().get(0));

		assertEquals(0, valRange.getPartitions().size());

		validateValueRangeMapper(valRange);
		//		assertValues(valRange, Ranges.closed(-1D, 1D), "Initial Fill");
	}

	@Test
	public void testGetBounds() {
		assertEquals(Ranges.closed(-1D, 1D), valRange.getBounds());
	}

	@Test
	public void testMovePartitionSingle() {
		testAddPartition();
		// assumes testBuilder passes

		valRange.movePartition(0, -0.4D);

		ValueRangeMapper<Double, String> expectedValRange = new ValueRangeMapper.Builder<Double, String>(
				Ranges.closed(-1D, 1D), "Bottom")
				.add(RangePartition.of(-0.4D, Side.RIGHT), "Initial Fill")
				.add(RangePartition.of(0D, Side.LEFT), "Middle")
				.add(RangePartition.of(0.5D, Side.RIGHT), "Top").build();

		assertEquals(expectedValRange, valRange);
	}

	@Test
	public void testMovePartitionMultiple() {
		testAddPartition();
		// assumes testBuilder passes

		valRange.movePartition(0, -0.4D);
		valRange.movePartition(1, 0.1D);
		valRange.movePartition(2, 0.2D);

		ValueRangeMapper<Double, String> expectedValRange = new ValueRangeMapper.Builder<Double, String>(
				Ranges.closed(-1D, 1D), "Bottom")
				.add(RangePartition.of(-0.4D, Side.RIGHT), "Initial Fill")
				.add(RangePartition.of(0.1D, Side.LEFT), "Middle")
				.add(RangePartition.of(0.2D, Side.RIGHT), "Top").build();

		assertEquals(expectedValRange, valRange);
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
		testAddPartition();

		int partToTest = 0;
		Double partPos = valRange.getPartitions().get(partToTest).getPosition();

		// closed side is right, so pos of partition is owned by range to its right ("Initial Fill")
		assertEquals("Initial Fill", valRange.getValue(partPos));
		assertEquals(Side.RIGHT, valRange.getPartitions().get(partToTest).getClosedSide());

		// changed closed side
		valRange.setPartitionType(partToTest, Side.LEFT);

		// partPos should now belong to range on left ("Bottom")
		assertEquals("Bottom", valRange.getValue(partPos));
		assertEquals(Side.LEFT, valRange.getPartitions().get(partToTest).getClosedSide());
	}

	@Test
	public void testAddPartition() {
		valRange.addPartition(middlePartition, "Top", Side.RIGHT);
		validateValueRangeMapper(valRange);

		valRange.addPartition(rightPartition, "Middle", Side.LEFT);
		validateValueRangeMapper(valRange);

		valRange.addPartition(leftPartition, "Bottom", Side.LEFT);
		validateValueRangeMapper(valRange);
	}

	@Test
	public void testGetEmptyPartitionsValues() {
		assertEquals(Lists.newArrayList("Initial Fill"), valRange.getValues());
		assertEquals(Lists.newArrayList(), valRange.getPartitions());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetManyPartitionsValues() {
		testAddPartition();

		assertEquals(Lists.newArrayList("Bottom", "Initial Fill", "Middle", "Top"),
				valRange.getValues());
		assertEquals(Lists.newArrayList(leftPartition, middlePartition, rightPartition),
				valRange.getPartitions());
	}

	@Test
	public void testRemoveValueRangeRight() {
		testAddPartition();

		//remove Initial Fill value section, filling with "Bottom"
		valRange.removePartition(0, Side.RIGHT);

		ValueRangeMapper<Double, String> expected = new ValueRangeMapper.Builder<Double, String>(
				Ranges.closed(-1D, 1D), "Bottom").add(RangePartition.of(0D, Side.LEFT), "Middle")
				.add(RangePartition.of(0.5D, Side.RIGHT), "Top").build();
		assertEquals(expected, valRange);
	}

	@Test
	public void testRemoveValueRangeLeft() {
		testAddPartition();

		//remove Bottom value section, filling with "Initial Fill"
		valRange.removePartition(0, Side.LEFT);

		ValueRangeMapper<Double, String> expected = new ValueRangeMapper.Builder<Double, String>(
				Ranges.closed(-1D, 1D), "Initial Fill")
				.add(RangePartition.of(0D, Side.LEFT), "Middle")
				.add(RangePartition.of(0.5D, Side.RIGHT), "Top").build();
		assertEquals(expected, valRange);
	}

	@Test
	public void testRemoveValueRangeTop() {
		testAddPartition();

		//remove Bottom value section, filling with "Initial Fill"
		valRange.removePartition(2, Side.RIGHT);

		ValueRangeMapper<Double, String> expected = new ValueRangeMapper.Builder<Double, String>(
				Ranges.closed(-1D, 1D), "Bottom")
				.add(RangePartition.of(-0.5D, Side.RIGHT), "Initial Fill")
				.add(RangePartition.of(0D, Side.LEFT), "Middle").build();
		assertEquals(expected, valRange);
	}

	@Test
	public void testBuilder() {
		testAddPartition();

		ValueRangeMapper.Builder<Double, String> myBuilder = new ValueRangeMapper.Builder<Double, String>(
				Ranges.closed(-1D, 1D), "Bottom")
				.add(RangePartition.of(-0.5D, Side.RIGHT), "Initial Fill")
				.add(RangePartition.of(0D, Side.LEFT), "Middle")
				.add(RangePartition.of(0.5D, Side.RIGHT), "Top");
		ValueRangeMapper<Double, String> sameAsValRange = myBuilder.build();

		assertEquals(valRange, sameAsValRange);

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

	static <T> void validateValueRangeMapper(ValueRangeMapper<Double, T> valRange) {
		validateValueRangeMapper(valRange, 0.1D);
	}

	static <T> void validateValueRangeMapper(ValueRangeMapper<Double, T> valRange,
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

			ret.add(Ranges.range(floor, floorType, ceiling, ceilingType));
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
