package teampg.grid2d;

import teampg.grid2d.point.AbsPos;

public interface ReadGrid<T> {
	T get(AbsPos at);
}
