package teampg199.datatypes.grid2d;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import teampg199.datatypes.grid2d.point.BoundedPos;


public class _RectGridTest {
	GridInterface<Integer> grid;
	Dimension size = new Dimension(4, 3);

	@Before
	public void setUp() throws Exception {
		grid = new RectGrid<Integer>(size);

		grid.set(BoundedPos.of(0, 0, size), 00);
		grid.set(BoundedPos.of(1, 0, size), 10);
		grid.set(BoundedPos.of(2, 0, size), 20);
		grid.set(BoundedPos.of(3, 0, size), 30);

		grid.set(BoundedPos.of(0, 1, size), 01);
		grid.set(BoundedPos.of(1, 1, size), 11);
		grid.set(BoundedPos.of(2, 1, size), 21);
		grid.set(BoundedPos.of(3, 1, size), 31);

		grid.set(BoundedPos.of(0, 2, size), 02);
		grid.set(BoundedPos.of(1, 2, size), 12);
		grid.set(BoundedPos.of(2, 2, size), 22);
		grid.set(BoundedPos.of(3, 2, size), 32);

		/**
		 * <pre>
		 *      +----+----+----+----+
		 *      | 00 | 10 | 20 | 30 |
		 *      +----+----+----+----+
		 *      | 01 | 11 | 21 | 31 |
		 *      +----+----+----+----+
		 *      | 02 | 12 | 22 | 32 |
		 *      +----+----+----+----+
		 * </pre>
		 */
	}

	private void assertGetThrowsOutOfBounds(GridInterface<Integer> in,
			BoundedPos from) {
		boolean threw = false;

		try {
			in.get(from);
		} catch (IndexOutOfBoundsException e) {
			threw = true;
		}

		assertTrue(threw);
	}

	@Test
	public void testRectGrid() {
		assertTrue(grid.get(BoundedPos.of(0, 0, size)) == 00);
		assertTrue(grid.get(BoundedPos.of(1, 0, size)) == 10);
		assertTrue(grid.get(BoundedPos.of(2, 0, size)) == 20);
		assertTrue(grid.get(BoundedPos.of(3, 0, size)) == 30);

		assertTrue(grid.get(BoundedPos.of(0, 1, size)) == 01);
		assertTrue(grid.get(BoundedPos.of(1, 1, size)) == 11);
		assertTrue(grid.get(BoundedPos.of(2, 1, size)) == 21);
		assertTrue(grid.get(BoundedPos.of(3, 1, size)) == 31);

		assertTrue(grid.get(BoundedPos.of(0, 2, size)) == 02);
		assertTrue(grid.get(BoundedPos.of(1, 2, size)) == 12);
		assertTrue(grid.get(BoundedPos.of(2, 2, size)) == 22);
		assertTrue(grid.get(BoundedPos.of(3, 2, size)) == 32);
	}

	@Test
	public void testGridIterator() {
		Iterator<Integer> iter = grid.iterator();

		assertTrue(iter.next() == 00);
		assertTrue(iter.next() == 10);
		assertTrue(iter.next() == 20);
		assertTrue(iter.next() == 30);

		assertTrue(iter.next() == 01);
		assertTrue(iter.next() == 11);
		assertTrue(iter.next() == 21);
		assertTrue(iter.next() == 31);

		assertTrue(iter.next() == 02);
		assertTrue(iter.next() == 12);
		assertTrue(iter.next() == 22);
		assertTrue(iter.next() == 32);

		assertTrue(iter.hasNext() == false);
	}

	@Test
	public void testOutOfBounds() {
		assertGetThrowsOutOfBounds(grid, BoundedPos.of(-1, 0, size));
		assertGetThrowsOutOfBounds(grid, BoundedPos.of(0, -1, size));
		assertGetThrowsOutOfBounds(grid, BoundedPos.of(4, 0, size));
		assertGetThrowsOutOfBounds(grid, BoundedPos.of(0, 3, size));
	}

	@Test
	public void testGetT() {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 4; x++) {
				Integer toFind = new Integer(x * 10 + y);
				BoundedPos found = grid.get(toFind).getPosition();
				BoundedPos expected = BoundedPos.of(x, y, size);

				System.out.println(toFind);
				assertEquals(expected, found);
			}
		}

	}

	@Test
	public void testGetEntries() {
		Iterator<GridInterface.Entry<Integer>> iter = grid.getEntries()
				.iterator();

		GridInterface.Entry<Integer> actualEntry;

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 4; x++) {
				actualEntry = iter.next();

				BoundedPos expectedPosition = BoundedPos.of(x, y, size);
				Integer expectedValue = x * 10 + y;

				assertEquals(expectedPosition, actualEntry.getPosition());
				assertEquals(expectedValue, actualEntry.getContents());
			}
		}

		assertTrue(iter.hasNext() == false);
	}

	@Test
	public void testContains() {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 4; x++) {
				Integer expectedValue = x * 10 + y;

				assertTrue(grid.contains(expectedValue));
			}
		}

		assertFalse(grid.contains(Integer.valueOf(-1)));
	}
}
