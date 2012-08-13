package teampg.grid2d;

import static org.junit.Assert.*;


import java.awt.Dimension;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import teampg.grid2d.point.AbsPos;



public class _RectGridTest {
	GridInterface<Integer> grid;
	Dimension size = new Dimension(4, 3);

	@Before
	public void setUp() throws Exception {
		grid = new RectGrid<Integer>(size);

		grid.set(AbsPos.of(0, 0), 00);
		grid.set(AbsPos.of(1, 0), 10);
		grid.set(AbsPos.of(2, 0), 20);
		grid.set(AbsPos.of(3, 0), 30);

		grid.set(AbsPos.of(0, 1), 01);
		grid.set(AbsPos.of(1, 1), 11);
		grid.set(AbsPos.of(2, 1), 21);
		grid.set(AbsPos.of(3, 1), 31);

		grid.set(AbsPos.of(0, 2), 02);
		grid.set(AbsPos.of(1, 2), 12);
		grid.set(AbsPos.of(2, 2), 22);
		grid.set(AbsPos.of(3, 2), 32);

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
			AbsPos from) {
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
		assertTrue(grid.get(AbsPos.of(0, 0)) == 00);
		assertTrue(grid.get(AbsPos.of(1, 0)) == 10);
		assertTrue(grid.get(AbsPos.of(2, 0)) == 20);
		assertTrue(grid.get(AbsPos.of(3, 0)) == 30);

		assertTrue(grid.get(AbsPos.of(0, 1)) == 01);
		assertTrue(grid.get(AbsPos.of(1, 1)) == 11);
		assertTrue(grid.get(AbsPos.of(2, 1)) == 21);
		assertTrue(grid.get(AbsPos.of(3, 1)) == 31);

		assertTrue(grid.get(AbsPos.of(0, 2)) == 02);
		assertTrue(grid.get(AbsPos.of(1, 2)) == 12);
		assertTrue(grid.get(AbsPos.of(2, 2)) == 22);
		assertTrue(grid.get(AbsPos.of(3, 2)) == 32);
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
		assertGetThrowsOutOfBounds(grid, AbsPos.of(-1, 0));
		assertGetThrowsOutOfBounds(grid, AbsPos.of(0, -1));
		assertGetThrowsOutOfBounds(grid, AbsPos.of(4, 0));
		assertGetThrowsOutOfBounds(grid, AbsPos.of(0, 3));
	}

	@Test
	public void testGetT() {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 4; x++) {
				Integer toFind = new Integer(x * 10 + y);
				AbsPos found = grid.get(toFind).getPosition();
				AbsPos expected = AbsPos.of(x, y);

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

				AbsPos expectedPosition = AbsPos.of(x, y);
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
