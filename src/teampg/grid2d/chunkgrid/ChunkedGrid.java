package teampg.grid2d.chunkgrid;

import java.awt.Dimension;

import com.google.common.collect.ImmutableSet;

/**
 * A sparse 2D board of equal-size sub-boards (chunks). <br />
 * <br />
 * Each (x,y) ChunkPos may have a Chunk, or be empty. Each chunk can be
 * retrieved (by ChunkPos), or individual cells in a chunk (by GlobalPos).
 * GlobalPos.x is equivalent to (ChunkPos.x * chunkWidth + PosInThatChunk.x)
 * [[plus or minus one -- see GlobalPos for details]] <br />
 * <br />
 * All contained chunks must be the same size. getChunkSize() must not change.
 * Negative positions are legal. Any given ChunkPos may be empty.
 *
 * @author Jackson Williams
 */
public interface ChunkedGrid<T> extends Iterable<ChunkEntry<T>> {
	public T get(GlobalPos at);

	public void set(GlobalPos at, T newValue);

	public boolean contains(GlobalPos toTest);

	// ======================

	public Dimension getChunkSize();

	public Chunk<T> get(ChunkPos at);

	/**
	 * Adds chunk into grid.
	 *
	 * @param atEdge
	 *            {@link isOnOutsideBorder}(atEdge) must be true.
	 * @param newChunk
	 *            Chunk to put into grid.
	 */
	public void putChunk(ChunkPos atEdge, Chunk<T> newChunk);

	public Chunk<T> removeChunk(ChunkPos removeFrom);

	// ======================

	public boolean contains(ChunkPos toTest);

	/**
	 * @return True if ChunkedGrid doesn't have a chunk at toTest, and it is
	 *         strictly adjacent to a ChunkPos that does have a chunk. Diagonal
	 *         doesn't count. False otherwise.
	 */
	public boolean isOnBorder(ChunkPos toTest);

	/**
	 * @return Positions of all Chunks that are in this ChunkedGrid at the
	 *         moment.
	 */
	public ImmutableSet<ChunkPos> pointSet();
}
