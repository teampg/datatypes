package teampg.datatypes.ValueRangeMapper;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import teampg.datatypes.ValueRangeMapper.ValueRangeMapper.Side;

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

		assertValues(valRange, Ranges.closed(-1D, 1D), "Initial Fill");
	}

	@Test
	public void testGetBounds() {
		assertEquals(Ranges.closed(-1D, 1D), valRange.getBounds());
	}

	@Test
	public void testMovePartition() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetPartitionType() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddPartition() {
		valRange.addPartition(middlePartition, "Top", Side.RIGHT);
		assertValues(valRange, Ranges.closed(-1D, 0D), "Initial Fill");
		assertValues(valRange, Ranges.openClosed(0.0D, 1.0D), "Top");

		valRange.addPartition(rightPartition, "Middle", Side.LEFT);
		assertValues(valRange, Ranges.closed(-1D, 0D), "Initial Fill");
		assertValues(valRange, Ranges.open(0D, 0.5D), "Middle");
		assertValues(valRange, Ranges.closed(0.5D, 1.0D), "Top");

		valRange.addPartition(leftPartition, "Bottom", Side.LEFT);
		assertValues(valRange, Ranges.closedOpen(-1D, -0.5D), "Bottom");
		assertValues(valRange, Ranges.closed(-0.5D, 0D), "Initial Fill");
		assertValues(valRange, Ranges.open(0D, 0.5D), "Middle");
		assertValues(valRange, Ranges.closed(0.5D, 1.0D), "Top");
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
	public void testRemovePartition() {
		fail("Not yet implemented");
	}

	@Test
	public void testBuilder() {
		testAddPartition();

		ValueRangeMapper.Builder<Double, String> myBuilder =
				new ValueRangeMapper.Builder<Double, String>(Ranges.closed(-1D, 1D), "Bottom")
				.add(RangePartition.of(-0.5D, Side.RIGHT), "Initial Fill")
				.add(RangePartition.of(0D, Side.LEFT), "Middle")
				.add(RangePartition.of(0.5D, Side.RIGHT), "Top");
		ValueRangeMapper<Double, String> sameAsValRange = myBuilder.build();

		assertEquals(valRange.getBounds(), 		sameAsValRange.getBounds());
		assertEquals(valRange.getPartitions(), 	sameAsValRange.getPartitions());
		assertEquals(valRange.getValues(), 		sameAsValRange.getValues());

		assertEquals(valRange, sameAsValRange);

		//building multiple times returns different instances
		assertEquals(sameAsValRange, myBuilder.build());
		assertFalse(sameAsValRange == myBuilder.build());
	}

	static <T> void assertValues(ValueRangeMapper<Double, T> valRange, Range<Double> in,
			T expected) {
		assertValues(valRange, in, expected, 0.1D);
	}

	static <T> void assertValues(ValueRangeMapper<Double, T> valRange, Range<Double> in,
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
