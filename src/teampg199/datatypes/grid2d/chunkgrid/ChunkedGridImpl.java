package teampg199.datatypes.grid2d.chunkgrid;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableSet;

import teampg199.datatypes.grid2d.point.BoundedPos;
import teampg199.datatypes.grid2d.point.Pos2D;

public class ChunkedGridImpl<T> implements ChunkedGrid<T> {
	private final Dimension chunkSize;
	private final Map<ChunkPos, Chunk<T>> chunks;

	public ChunkedGridImpl(Dimension chunkSize) {
		checkArgument(chunkSize.height > 0);
		checkArgument(chunkSize.width > 0);

		this.chunkSize = (Dimension) chunkSize.clone();

		chunks = new HashMap<>();
	}

	@Override
	public Dimension getChunkSize() {
		return chunkSize;
	}

	@Override
	public Iterator<ChunkEntry<T>> iterator() {

		List<ChunkEntry<T>> chunkEntries = new ArrayList<>();
		for (Map.Entry<ChunkPos, Chunk<T>> entry : chunks.entrySet()) {
			ChunkEntry<T> chunkEntry = new ChunkEntry<>(entry.getKey(),
					entry.getValue());
			chunkEntries.add(chunkEntry);
		}

		return chunkEntries.iterator();
	}

	@Override
	public T get(GlobalPos tileCoordsOnMap) {
		checkArgument(contains(tileCoordsOnMap));

		Chunk<T> chunkToGetFrom = getChunk(tileCoordsOnMap);

		BoundedPos tileCoordsInChunk = tileCoordsOnMap.getInnerComponent();

		T tileFound = chunkToGetFrom.get(tileCoordsInChunk);
		return tileFound;
	}

	@Override
	public void set(GlobalPos at, T newValue) {
		checkArgument(contains(at));

		Chunk<T> chunkToSet = getChunk(at);
		chunkToSet.set(at.getInnerComponent(), newValue);
	}

	private Chunk<T> getChunk(GlobalPos tileCoordsOnMap) {
		ChunkPos chunkPosOnMap = tileCoordsOnMap.getChunkComponent();
		return chunks.get(chunkPosOnMap);
	}

	// TODO shouldn't be able to directly get, because you could mess with ChunkGrid's innards
	@Override
	public Chunk<T> get(ChunkPos at) {
		checkArgument(contains(at));
		return chunks.get(at);
	}

	@Override
	public void putChunk(ChunkPos posToPut, Chunk<T> inChunk) {
		checkArgument(contains(posToPut) == false, "Cannot overwrite chunks");
		checkArgument(inChunk.getSize().equals(chunkSize), "Chunks must be uniform size");

		Chunk<T> newChunk = inChunk.copy();

		chunks.put(posToPut, newChunk);
	}

	@Override
	public boolean contains(GlobalPos toTest) {
		ChunkPos chunkPosToTest = toTest.getChunkComponent();

		return chunks.keySet().contains(chunkPosToTest);
	}

	/**
	 * Checks if the chunk exists on the board
	 */
	@Override
	public boolean contains(ChunkPos toTest) {
		return chunks.keySet().contains(toTest);
	}

	@Override
	public boolean isOnBorder(ChunkPos toTest) {
		if (chunks.keySet().contains(toTest)) {
			return false;
		}

		for (ChunkPos chunk : Pos2D.getDiamondNear(toTest, 1)) {
			if (chunks.keySet().contains(chunk)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Chunk<T> removeChunk(ChunkPos removeFrom) {
		checkArgument(chunks.containsKey(removeFrom));

		Chunk<T> toPop = chunks.get(removeFrom);
		chunks.remove(removeFrom);

		return toPop;
	}

	@Override
	public ImmutableSet<ChunkPos> pointSet() {
		return ImmutableSet.copyOf(chunks.keySet());
	}
}
