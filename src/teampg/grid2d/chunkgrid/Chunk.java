package teampg.grid2d.chunkgrid;

import teampg.grid2d.GridInterface;

public interface Chunk<T> extends GridInterface<T> {
	@Override
	public void toArray(T[] toFill);
	@Override
	public Chunk<T> copy();
}
