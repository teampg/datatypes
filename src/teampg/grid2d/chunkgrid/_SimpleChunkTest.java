package teampg.grid2d.chunkgrid;

import static org.junit.Assert.*;


import java.awt.Dimension;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import teampg.grid2d.point.AbsPos;


public class _SimpleChunkTest {
	private static final Dimension CHUNK_SIZE = new Dimension(3, 3);

	SimpleChunk<Integer> c;
	SimpleChunk<Integer> sameAsC;
	SimpleChunk<Integer> q;

	@Before
	public void setUp() throws Exception {
		c = new SimpleChunk<>(CHUNK_SIZE);
		for (int y = 0; y < CHUNK_SIZE.width; y++) {
			for (int x = 0; x < CHUNK_SIZE.height; x++) {
				c.set(AbsPos.of(x, y), y * CHUNK_SIZE.height + x);
			}
		}

		sameAsC = new SimpleChunk<>(CHUNK_SIZE);
		for (int y = 0; y < CHUNK_SIZE.width; y++) {
			for (int x = 0; x < CHUNK_SIZE.height; x++) {
				sameAsC.set(AbsPos.of(x, y), y * CHUNK_SIZE.height + x);
			}
		}

		q = new SimpleChunk<>(CHUNK_SIZE);
		for (int y = 0; y < CHUNK_SIZE.width; y++) {
			for (int x = 0; x < CHUNK_SIZE.height; x++) {
				q.set(AbsPos.of(x, y), 6413);
			}
		}
	}

	@Test
	public final void testToArray() {
		Integer[] expected = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };

		Integer[] actual = new Integer[9];
		c.toArray(actual);

		assertTrue(Arrays.equals(expected, actual));
	}

	@Test
	public final void testEqualsObject() {
		assertEquals(c, sameAsC);
		assertFalse(c.equals(q));
	}

}
