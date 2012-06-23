package teampg.datatypes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Ranges;

public class ContiguousRangeSetTest {
	ContiguousRangeSet<Double, String> noPartitions;

	@Before
	public void setUp() throws Exception {
		noPartitions = new ContiguousRangeSet<>(Ranges.closed(-1D, 1D), "Only");


	}

	@Test
	public void testInit() {
		System.out.println();

		assertEquals(1, noPartitions.getValues().size());
		assertEquals("Only", noPartitions.getValues().get(0));

		assertEquals(0, noPartitions.getPartitions().size());

		for (Double d = -1D; d.compareTo(1D) < 0; d+=0.001D) {
			assertEquals("Only", noPartitions.getValue(d));
		}
	}

	@Test
	public void testGetBounds() {
		assertEquals(Ranges.closed(-1D, 1D), noPartitions.getBounds());
	}

	@Test
	public void testInvalidParameters() {
		fail("Not yet implemented");
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
	public void testPartitionAddAbove() {
		fail("Not yet implemented");
	}

	@Test
	public void testPartitionAddBelow() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemovePartitionDeleteRangeBelow() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemovePartitionDeleteRangeAbove() {
		fail("Not yet implemented");
	}

}
