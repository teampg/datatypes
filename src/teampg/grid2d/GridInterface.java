package teampg.grid2d;


import java.awt.Dimension;

import teampg.grid2d.point.BoundedPos;
import teampg.grid2d.point.Pos2D;


public interface GridInterface<T> extends Iterable<T> {
	T get(BoundedPos at);
	void set(BoundedPos at, T val);

	Dimension getSize();

	Iterable<Entry<T>> getEntries();

	Entry<T> get(T toFind);
	boolean contains(T element);

	boolean isInBounds(BoundedPos pos);

	public void toArray(T[] toFill);
	public GridInterface<T> copy();

	public static class Entry<E> {
		private final BoundedPos pos;
		private final E contents;

		@Override
		public String toString() {
			return "Entry [in=" + pos + ", contents=" + contents + "]";
		}

		public Entry(BoundedPos pos, E contents) {
			this.pos = pos;
			this.contents = contents;
		}

		public BoundedPos getPosition() {
			return pos;
		}

		public E getContents() {
			return contents;
		}

		@Override
		public int hashCode() {
			return pos.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			@SuppressWarnings("unchecked")
			Entry<E> other = (Entry<E>) obj;

			return pos.equals(other.pos);
		}

		public int distance(Entry<E> other) {
			return Pos2D.squareDistance(pos, other.pos);
		}

		public int distance(BoundedPos to) {
			return Pos2D.squareDistance(pos, to);
		}
	}
}
