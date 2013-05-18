package teampg.grid2d;

import java.awt.Dimension;

import teampg.grid2d.point.AbsPos;

public interface ReadGrid<T> extends Iterable<T> {
	T get(AbsPos at);
	Dimension getSize();
	boolean isInBounds(AbsPos pos);
}
