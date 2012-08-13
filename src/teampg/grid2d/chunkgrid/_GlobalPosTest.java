package teampg.grid2d.chunkgrid;

import static org.junit.Assert.*;


import java.awt.Dimension;

import org.junit.Test;

import teampg.grid2d.point.AbsPos;


public class _GlobalPosTest {

	private static final Dimension CHUNK = new Dimension(3, 3);


	@Test
	public final void testInZeroChunk() {
		GlobalPos zero = GlobalPos.of(0, 0, CHUNK);
		assertEquals(ChunkPos.of(0, 0), zero.getChunkComponent());
		assertEquals(AbsPos.of(0, 0), zero.getInnerComponent());
		assertEquals(zero.getChunkSize(), CHUNK);


		GlobalPos mid = GlobalPos.of(1, 1, CHUNK);
		assertEquals(ChunkPos.of(0, 0), mid.getChunkComponent());
		assertEquals(AbsPos.of(1, 1), mid.getInnerComponent());
		assertEquals(mid.getChunkSize(), CHUNK);
	}

	@Test
	public final void testMoveOverZero() {
		GlobalPos leftOfZero = GlobalPos.of(-1, -1, CHUNK);

		assertEquals(ChunkPos.of(-1, -1), leftOfZero.getChunkComponent());
		assertEquals(AbsPos.of(2, 2), leftOfZero.getInnerComponent());
		assertEquals(leftOfZero.getChunkSize(), CHUNK);


		GlobalPos movingIntoNextNegChunk = GlobalPos.of(-3, 0, CHUNK);

		assertEquals(ChunkPos.of(-1, 0), movingIntoNextNegChunk.getChunkComponent());
		assertEquals(AbsPos.of(0, 0), movingIntoNextNegChunk.getInnerComponent());
		assertEquals(movingIntoNextNegChunk.getChunkSize(), CHUNK);
	}

}
