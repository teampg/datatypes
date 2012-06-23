package teampg.grid2d.chunkgrid;

import static org.junit.Assert.*;


import java.awt.Dimension;

import org.junit.Test;

import teampg.grid2d.point.BoundedPos;


public class _GlobalPosTest {

	private static final Dimension CHUNK = new Dimension(3, 3);


	@Test
	public final void testInZeroChunk() {
		GlobalPos zero = GlobalPos.of(0, 0, CHUNK);
		assertEquals(ChunkPos.of(0, 0), zero.getChunkComponent());
		assertEquals(BoundedPos.of(0, 0, CHUNK), zero.getInnerComponent());


		GlobalPos mid = GlobalPos.of(1, 1, CHUNK);
		assertEquals(ChunkPos.of(0, 0), mid.getChunkComponent());
		assertEquals(BoundedPos.of(1, 1, CHUNK), mid.getInnerComponent());
	}

	@Test
	public final void testMoveOverZero() {
		GlobalPos leftOfZero = GlobalPos.of(-1, -1, CHUNK);

		assertEquals(ChunkPos.of(-1, -1), leftOfZero.getChunkComponent());
		assertEquals(BoundedPos.of(2, 2, CHUNK), leftOfZero.getInnerComponent());


		GlobalPos movingIntoNextNegChunk = GlobalPos.of(-3, 0, CHUNK);

		assertEquals(ChunkPos.of(-1, 0), movingIntoNextNegChunk.getChunkComponent());
		assertEquals(BoundedPos.of(0, 0, CHUNK), movingIntoNextNegChunk.getInnerComponent());
	}

}
