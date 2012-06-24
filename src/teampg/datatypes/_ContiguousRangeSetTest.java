package teampg.datatypes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import teampg.datatypes.ContiguousRangeSet.Side;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public class _ContiguousRangeSetTest {
	ContiguousRangeSet<Double, String> valRange;

	@Before
	public void setUp() throws Exception {
		valRange = new ContiguousRangeSet<>(Ranges.closed(-1D, 1D), "Initial Fill");
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
		valRange.addPartition(0.0D, Side.LEFT, "Top", Side.RIGHT);
		assertValues(valRange, Ranges.closed(-1D, 0D), "Initial Fill");
		assertValues(valRange, Ranges.openClosed(0.0D, 1.0D), "Top");

		valRange.addPartition(0.5D, Side.RIGHT, "Middle", Side.LEFT);
		assertValues(valRange, Ranges.closed(-1D, 0D), "Initial Fill");
		assertValues(valRange, Ranges.open(0D, 0.5D), "Middle");
		assertValues(valRange, Ranges.closed(0.5D, 1.0D), "Top");

		valRange.addPartition(-0.5D, Side.RIGHT, "Bottom", Side.LEFT);
		assertValues(valRange, Ranges.closedOpen(-1D, -0.5D), "Bottom");
		assertValues(valRange, Ranges.closed(-0.5D, 0D), "Initial Fill");
		assertValues(valRange, Ranges.open(0D, 0.5D), "Middle");
		assertValues(valRange, Ranges.closed(0.5D, 1.0D), "Top");
	}

	@Test
	public void testRemovePartitionDeleteRangeBelow() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemovePartitionDeleteRangeAbove() {
		fail("Not yet implemented");
	}


	static <T> void assertValues(ContiguousRangeSet<Double, T> valRange, Range<Double> in, T expected) {
		assertValues(valRange, in, expected, 0.1D);
	}

	static <T> void assertValues(ContiguousRangeSet<Double, T> valRange, Range<Double> in, T expected, Double checkInterval) {
		//check leftmost point
		Double cursor = in.lowerEndpoint();
		if (in.lowerBoundType().equals(BoundType.OPEN)) {
			cursor += 0.0000000001D;
		}

		//check fill points
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
