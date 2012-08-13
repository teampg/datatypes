package teampg.grid2d;


import java.awt.Dimension;

import teampg.grid2d.point.AbsPos;
import teampg.grid2d.point.Pos2D;


public interface GridInterface<T> extends ReadGrid<T>, Iterable<T> {
	void set(AbsPos at, T val);

	Dimension getSize();

	Iterable<Entry<T>> getEntries();

	Entry<T> get(T toFind);
	boolean contains(T element);

	boolean isInBounds(AbsPos pos);

	void fill(T filler);

	public void toArray(T[] toFill);
	public GridInterface<T> copy();

	public static class Entry<E> {
		private final AbsPos pos;
		private final E contents;

		@Override
		public String toString() {
			return "Entry [in=" + pos + ", contents=" + contents + "]";
		}

		public Entry(AbsPos pos, E contents) {
			this.pos = pos;
			this.contents = contents;
		}

		public AbsPos getPosition() {
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

		public int distance(AbsPos to) {
			return Pos2D.squareDistance(pos, to);
		}
	}
}
