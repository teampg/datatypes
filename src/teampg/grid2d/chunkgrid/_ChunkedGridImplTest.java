package teampg.grid2d.chunkgrid;

import static org.junit.Assert.*;


import java.awt.Dimension;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import teampg.grid2d.GridInterface;
import teampg.grid2d.GridInterface.Entry;
import teampg.grid2d.point.BoundedPos;

import com.google.common.collect.Iterators;


public class _ChunkedGridImplTest {
	private static final Dimension CHUNK_SIZE = new Dimension(3, 3);

	Chunk<String> middle;
	Chunk<String> below;
	Chunk<String> left;
	Chunk<String> leftOfLeft;

	ChunkedGrid<String> grid;

	@Before
	public void setUp() throws Exception {
		// @formatter:off
		/**
		 * !!grid!!
		 * #: loaded chunks
		 * .: isOnOutsideBorder returns true
		 *   |-3 |-2 |-1 | 0 | 1 |
		 * --+---+---+---+---+---+
		 * -1|   | . | . | . |   |
		 * --+---+---+---+---+---+
		 *  0| . | # | # | # | . |
		 * --+---+---+---+---+---+
		 *  1|   | . | . | # | . |
		 * --+---+---+---+---+---+
		 *  2|   |   |   | . |   |
		 * --+---+---+---+---+---+
		 */
		// @formatter:on
		grid = new ChunkedGridImpl<>(CHUNK_SIZE);

		{
			ChunkPos middlePos = ChunkPos.of(0, 0);
			middle = buildChunk(middlePos, CHUNK_SIZE);
			grid.putChunk(middlePos, middle);
		}

		{
			// note down is +y, up is -y
			ChunkPos downOfMiddlePos = ChunkPos.of(0, 1);
			below = buildChunk(downOfMiddlePos, CHUNK_SIZE);
			grid.putChunk(downOfMiddlePos, below);
		}

		{
			ChunkPos leftOfMiddlePos = ChunkPos.of(-1, 0);
			left = buildChunk(leftOfMiddlePos, CHUNK_SIZE);
			grid.putChunk(leftOfMiddlePos, left);
		}

		{
			ChunkPos leftOfLeftPos = ChunkPos.of(-2, 0);
			leftOfLeft = buildChunk(leftOfLeftPos, CHUNK_SIZE);
			grid.putChunk(leftOfLeftPos, leftOfLeft);
		}
	}

	@Test
	public void testPointSet() {
		Set<ChunkPos> pointsInGrid = grid.pointSet();

		assertEquals(4, pointsInGrid.size());
		assertTrue(pointsInGrid.contains(ChunkPos.of( 0, 0)));
		assertTrue(pointsInGrid.contains(ChunkPos.of(-1, 0)));
		assertTrue(pointsInGrid.contains(ChunkPos.of(-2, 0)));
		assertTrue(pointsInGrid.contains(ChunkPos.of( 0, 1)));
	}

	@Test
	public void testRemoveChunk() {
		Chunk<String> popped = grid.removeChunk(ChunkPos.of(0, 0));
		assertEquals(middle, popped);

		assertFalse(grid.contains(ChunkPos.of(0, 0)));

		//fail
		try {
			grid.removeChunk(ChunkPos.of(8, 8));
			fail("Removing not-contained chunks should throw error");
		} catch (IllegalArgumentException e) {
		}

		try {
			grid.removeChunk(ChunkPos.of(0, 0));
			fail("Removing not-contained chunks should throw error");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testIterator() {
		assertEquals(1, Iterators.frequency(grid.iterator(), new ChunkEntry<>(ChunkPos.of(0,0), middle)));
		assertEquals(1, Iterators.frequency(grid.iterator(), new ChunkEntry<>(ChunkPos.of(0,0), below)));
		assertEquals(1, Iterators.frequency(grid.iterator(), new ChunkEntry<>(ChunkPos.of(0,0), left)));
		assertEquals(1, Iterators.frequency(grid.iterator(), new ChunkEntry<>(ChunkPos.of(0,0), leftOfLeft)));

		assertEquals(4, Iterators.size(grid.iterator()));
	}

	@Test
	public void testGlobalPositionIsSameAsLocalPlusChunk() {
		// inside leftOfLeft, at 3,2. leftOfLeft is at -2,0.
		BoundedPos localPosition = BoundedPos.of(2, 2, CHUNK_SIZE);
		ChunkPos leftOfLeftPos = ChunkPos.of(-2, 0);

		GlobalPos globalPosition = GlobalPos.of(leftOfLeftPos, localPosition);

		// getting by global position returns exactly the same object as getting
		// by local position
		assertTrue(leftOfLeft.get(localPosition) == grid.get(globalPosition));
	}

	@Test
	public void testManyGetInnerPos() {
		for (ChunkEntry<String> chunkEntry : grid) {
			ChunkPos chunkPos = chunkEntry.getPosition();

			for (Entry<String> entry : grid.get(chunkPos).getEntries()) {
				BoundedPos innerPos = entry.getPosition();

				GlobalPos globalPosition = GlobalPos.of(chunkPos, innerPos);

				assertTrue(entry.getContents() == grid.get(globalPosition));
			}
		}
	}

	@Test
	public void testSet() {
		GlobalPos globalPos = GlobalPos.of(ChunkPos.of(-1, 0), BoundedPos.of(0, 1, CHUNK_SIZE));
		assertEquals("c[-1, 0], i[0, 1]", grid.get(globalPos));

		grid.set(globalPos, "Bob");
		assertEquals("Bob", grid.get(globalPos));
	}

	@Test
	public void testGet(){
		GlobalPos globalPos = GlobalPos.of(ChunkPos.of(-1, 0), BoundedPos.of(0, 1, CHUNK_SIZE));
		assertEquals("c[-1, 0], i[0, 1]", grid.get(globalPos));

	}

	@Test
	public void testSetOutOfBounds() {
		GlobalPos globalPos = GlobalPos.of(ChunkPos.of(-1, -1), BoundedPos.of(0, 1, CHUNK_SIZE));

		try {
			grid.set(globalPos, "Bob");
			fail("should throw error");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testIsInBoundsGlobalPos() {
		GlobalPos inBounds = GlobalPos.of(ChunkPos.of(0, 1), BoundedPos.of(0, 1, CHUNK_SIZE));
		System.out.println(inBounds);
		assertEquals(true, grid.contains(inBounds));

		assertEquals(true, grid.contains(GlobalPos.of(0, 0, CHUNK_SIZE)));


		GlobalPos reallyOutOfBounds = GlobalPos.of(ChunkPos.of(-100, -100), BoundedPos.of(0, 1, CHUNK_SIZE));
		assertEquals(false, grid.contains(reallyOutOfBounds));

		GlobalPos outOfBounds = GlobalPos.of(ChunkPos.of(-1, -1), BoundedPos.of(0, 1, CHUNK_SIZE));
		assertEquals(false, grid.contains(outOfBounds));

		ChunkedGrid<String> emptyGrid = new ChunkedGridImpl<>(new Dimension(3,3));
		assertEquals(false, emptyGrid.contains(GlobalPos.of(0, 0, CHUNK_SIZE)));
	}

	@Test
	public void testManyIsInBounds() {
		for (ChunkEntry<String> chunkEntry : grid) {
			ChunkPos chunkPos = chunkEntry.getPosition();

			// chunk is in grid
			assertEquals(true, grid.contains(chunkPos));

			for (Entry<String> entry : grid.get(chunkPos).getEntries()) {
				BoundedPos innerPos = entry.getPosition();

				// cell is in grid
				GlobalPos globalPosition = GlobalPos.of(chunkPos, innerPos);
				assertEquals(true, grid.contains(globalPosition));
			}
		}

	}

	@Test
	public void testGetChunkSize() {
		assertEquals(CHUNK_SIZE, grid.getChunkSize());
	}

	@Test
	public void testMadeNewInstanceOfAddedChunks() {
		// ChunkedGrid should make new instances of added chunks
		assertFalse(middle == grid.get(ChunkPos.of(0, 0)));
		assertFalse(below == grid.get(ChunkPos.of(0, 1)));
		assertFalse(left == grid.get(ChunkPos.of(-1, 0)));
		assertFalse(leftOfLeft == grid.get(ChunkPos.of(-2, 0)));
	}

	@Test
	public void testGetChunkPos() {
		assertEquals(middle, grid.get(ChunkPos.of(0, 0)));
		assertEquals(below, grid.get(ChunkPos.of(0, 1)));
		assertEquals(left, grid.get(ChunkPos.of(-1, 0)));
		assertEquals(leftOfLeft, grid.get(ChunkPos.of(-2, 0)));

		ChunkPos notInGrid = ChunkPos.of(-1, -1);
		try {
			grid.get(notInGrid);
			fail("should throw error");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testAddChunk() {
		ChunkPos newChunkPos = ChunkPos.of(-1, -1);
		ChunkPos adjacentToNewChunk = ChunkPos.of(-1, -2);

		assertFalse(grid.contains(newChunkPos));
		assertTrue(grid.isOnBorder(newChunkPos));
		assertFalse(grid.isOnBorder(adjacentToNewChunk));

		Chunk<String> newChunk = buildChunk(newChunkPos, CHUNK_SIZE);
		grid.putChunk(newChunkPos, newChunk);

		assertEquals(CHUNK_SIZE.width * CHUNK_SIZE.height, Iterators.size(grid.get(newChunkPos).iterator()));

		assertTrue(grid.contains(newChunkPos));
		assertFalse(grid.isOnBorder(newChunkPos));
		assertTrue(grid.isOnBorder(adjacentToNewChunk));
	}

	@Test
	public void testAddChunkInIllegalPosition() {
		ChunkPos alreadyFullPos = ChunkPos.of(0,0);
		Chunk<String> newChunk = buildChunk(alreadyFullPos, CHUNK_SIZE);

		try {
			grid.putChunk(alreadyFullPos, newChunk);
			fail("Trying to add a chunk where there is already a chunk should be illegal");
		} catch (IllegalArgumentException e) {
		}

		ChunkPos legalPosToPutChunk = ChunkPos.of(1, 0);
		Chunk<String> wrongSizeChunk = buildChunk(legalPosToPutChunk, new Dimension(3,4));
		try {
			grid.putChunk(legalPosToPutChunk, wrongSizeChunk);
			fail("Adding a chunk with different size from grid should fail");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testIsInBoundsChunkPos() {
		ChunkPos inBounds = ChunkPos.of(-1, 0);
		assertTrue(grid.contains(inBounds));

		ChunkPos middlePos = ChunkPos.of(0, 0);
		assertTrue(grid.contains(middlePos));

		ChunkPos outOfBounds = ChunkPos.of(-1, -1);
		assertFalse(grid.contains(outOfBounds));

		ChunkedGrid<String> emptyGrid = new ChunkedGridImpl<>(new Dimension(3,3));
		assertFalse(emptyGrid.contains(ChunkPos.of(0, 0)));
	}

	@Test
	public void testIsOnOutsideBorder() {
		assertTrue(grid.isOnBorder(ChunkPos.of(-2, -1)));
		assertTrue(grid.isOnBorder(ChunkPos.of(-1, -1)));
		assertTrue(grid.isOnBorder(ChunkPos.of( 0, -1)));

		assertTrue(grid.isOnBorder(ChunkPos.of(-3,  0)));
		assertTrue(grid.isOnBorder(ChunkPos.of( 1,  0)));

		assertTrue(grid.isOnBorder(ChunkPos.of( 1,  1)));
		assertTrue(grid.isOnBorder(ChunkPos.of(-1,  1)));
		assertTrue(grid.isOnBorder(ChunkPos.of(-2,  1)));

		assertTrue(grid.isOnBorder(ChunkPos.of( 0,  2)));



		// negative tests
		assertFalse(grid.isOnBorder(ChunkPos.of(-3, -1)));

		assertFalse(grid.isOnBorder(ChunkPos.of(-2,  0)));
		assertFalse(grid.isOnBorder(ChunkPos.of(-1,  0)));
		assertFalse(grid.isOnBorder(ChunkPos.of( 0,  0)));

		assertFalse(grid.isOnBorder(ChunkPos.of( 0,  1)));

		assertFalse(grid.isOnBorder(ChunkPos.of( 1,  2)));


		// empty grid
		// in empty grid, none should be on outside border
		ChunkedGrid<String> emptyGrid = new ChunkedGridImpl<>(new Dimension(3,3));
		assertFalse(emptyGrid.isOnBorder(ChunkPos.of(0, 0)));
	}

	@Test
	// @formatter:off
	/**
	 * !!hollowGrid!!
	 * #: loaded chunks
	 * .: isOnOutsideBorder returns true
	 *   |-3 |-2 |-1 | 0 | 1 | 2 |
	 * --+---+---+---+---+---+---+
	 * -2|   |   |   | . |   |   |
	 * --+---+---+---+---+---+---+
	 * -1|   |   | . | # | . |   |
	 * --+---+---+---+---+---+---+
	 *  0|   | . | # | . | # | . |
	 * --+---+---+---+---+---+---+
	 *  1| . | # | . | # | . |   |
	 * --+---+---+---+---+---+---+
	 *  2|   | . |   | . |   |   |
	 * --+---+---+---+---+---+---+
	 */
	// @formatter:on
	public void testIsOnBorderHollow() {
		ChunkedGrid<String> hollowGrid = new ChunkedGridImpl<>(CHUNK_SIZE);

		ChunkPos[] allChunkPosToPut = {
				ChunkPos.of( 0, -1),
				ChunkPos.of(-1,  0),
				ChunkPos.of( 1,  0),
				ChunkPos.of(-2,  1),
				ChunkPos.of( 0,  1),
		};
		for (ChunkPos toPut : allChunkPosToPut) {
			hollowGrid.putChunk(toPut, buildChunk(toPut, CHUNK_SIZE));
		}


		assertTrue(hollowGrid.isOnBorder(ChunkPos.of( 0, -2)));

		assertTrue(hollowGrid.isOnBorder(ChunkPos.of(-1, -1)));
		assertTrue(hollowGrid.isOnBorder(ChunkPos.of( 1, -1)));

		assertTrue(hollowGrid.isOnBorder(ChunkPos.of(-2, 0)));
		assertTrue(hollowGrid.isOnBorder(ChunkPos.of( 0, 0)));
		assertTrue(hollowGrid.isOnBorder(ChunkPos.of( 2, 0)));

		assertTrue(hollowGrid.isOnBorder(ChunkPos.of(-3, 1)));
		assertTrue(hollowGrid.isOnBorder(ChunkPos.of(-1, 1)));
		assertTrue(hollowGrid.isOnBorder(ChunkPos.of( 1, 1)));

		assertTrue(hollowGrid.isOnBorder(ChunkPos.of(-2, 2)));
		assertTrue(hollowGrid.isOnBorder(ChunkPos.of( 0, 2)));

		// negative tests
		for (ChunkPos loadedChunkPos : allChunkPosToPut) {
			assertFalse(hollowGrid.isOnBorder(loadedChunkPos));
		}
	}

	static SimpleChunk<String> buildChunk(ChunkPos chunkPos, Dimension chunkSize) {
		SimpleChunk<String> ret = new SimpleChunk<>(chunkSize);

		for (GridInterface.Entry<String> e : ret.getEntries()) {
			BoundedPos innerPos = e.getPosition();

			ret.set(innerPos,
					"c[" + chunkPos.x + ", " + chunkPos.y + "], " + "i[" + innerPos.x + ", "
							+ innerPos.y + "]");
		}

		return ret;
	}
}
