package teampg199.datatypes.grid2d.chunkgrid;

public class ChunkEntry<E> {
	private final ChunkPos pos;
	private final Chunk<E> chunk;

	@Override
	public String toString() {
		return "ChunkEntry [pos=" + pos + ", chunk=" + chunk + "]";
	}

	public ChunkEntry(ChunkPos pos, Chunk<E> chunk) {
		this.pos = pos;
		this.chunk = chunk;
	}

	public ChunkPos getPosition() {
		return pos;
	}

	public Chunk<E> getChunk() {
		return chunk;
	}

	@Override
	public int hashCode() {
		return pos.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		@SuppressWarnings("unchecked")
		ChunkEntry<E> other = (ChunkEntry<E>) obj;

		return pos.equals(other.pos);
	}
}
