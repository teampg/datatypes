package teampg.grid2d.chunkgrid;


import java.awt.Dimension;

import teampg.grid2d.RectGrid;

public class SimpleChunk<T> extends RectGrid<T> implements Chunk<T> {
	public SimpleChunk(Dimension size) {
		super(size);
	}

	@Override
	public SimpleChunk<T> copy() {
		SimpleChunk<T> myCopy = new SimpleChunk<>(size);
		System.arraycopy(contents, 0, myCopy.contents, 0, contents.length);

		return myCopy;
	}
}
