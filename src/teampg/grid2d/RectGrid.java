package teampg.grid2d;

import static com.google.common.base.Preconditions.checkArgument;


import java.awt.Dimension;
import java.util.Arrays;
import java.util.Iterator;

import teampg.grid2d.point.BoundedPos;



import com.google.common.collect.Iterators;

public class RectGrid<T> implements GridInterface<T> {
	protected final T[] contents;
	protected final Dimension size;

	@SuppressWarnings("unchecked")
	public RectGrid(Dimension size) {
		this.size = (Dimension) size.clone();

		contents = (T[]) new Object[size.width * size.height];
	}

	@Override
	public T get(BoundedPos at) {
		checkArgument(at.getChunkSize().equals(size));
		if (!isInBounds(at)) {
			throw new IndexOutOfBoundsException();
		}

		return contents[(at.y * size.width) + at.x];
	}

	@Override
	public void set(BoundedPos at, T val) {
		checkArgument(at.getChunkSize().equals(size));
		if (!isInBounds(at)) {
			throw new IndexOutOfBoundsException();
		}

		contents[(at.y * size.width) + at.x] = val;
	}

	@Override
	public Iterator<T> iterator() {
		return Iterators.forArray(contents);
	}

	@Override
	public Iterable<Entry<T>> getEntries() {
		final Iterator<T> iter = Iterators.forArray(contents);

		return new Iterable<Entry<T>>() {

			@Override
			public Iterator<Entry<T>> iterator() {
				return new Iterator<Entry<T>>() {
					int x = -1;
					int y = 0;

					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public Entry<T> next() {
						x++;
						if (x == size.width) {
							x = 0;
							y++;
						}

						return new Entry<T>(BoundedPos.of(x, y, size), iter.next());
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException("Remove not implemented for this iterator");
					}
				};
			}
		};
	}

	@Override
	public boolean isInBounds(BoundedPos pos) {
		checkArgument(pos.getChunkSize().equals(size));

		int x = pos.x;
		int y = pos.y;

		if (x < 0) {
			return false;
		}

		if (x >= size.width) {
			return false;
		}

		if (y < 0) {
			return false;
		}

		if (y >= size.height) {
			return false;
		}

		return true;
	}

	@Override
	public Entry<T> get(T toFind) {
		for (int i = 0; i < contents.length; i++) {
			T anElement = contents[i];
			if (toFind.equals(anElement)) {
				int x = i % size.width;
				int y = i / size.width;

				return new Entry<T>(BoundedPos.of(x, y, size), anElement);
			}
		}

		return null;
	}

	@Override
	public boolean contains(T toFind) {
		for (T anElement : contents) {
			if (toFind.equals(anElement)) {
				return true;
			}
		}

		return false;
	}


	@Override
	public void toArray(T[] toFill) {
		checkArgument(toFill.length == contents.length);
		System.arraycopy(contents, 0, toFill, 0, contents.length);
	}

	@Override
	public boolean equals(Object what) {
		@SuppressWarnings("unchecked")
		RectGrid<T> other = (RectGrid<T>) what;

		return Arrays.equals(contents, other.contents);
	}

	@Override
	public RectGrid<T> copy() {
		RectGrid<T> myCopy = new RectGrid<>(size);

		System.arraycopy(contents, 0, myCopy.contents, 0, contents.length);

		return myCopy;

	}

	@Override
	public Dimension getSize() {
		return (Dimension) size.clone();
	}
}
