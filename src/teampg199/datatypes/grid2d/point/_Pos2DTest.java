package teampg199.datatypes.grid2d.point;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;




public class _Pos2DTest {
	RelPos relA;
	RelPos relB;
	BoundedPos absA;
	BoundedPos absB;

	static final Dimension BOUNDS_DONT_MATTER = new Dimension(100, 1000);

	@Before
	public void setUp() throws Exception {
		relA = RelPos.of(-2, 2);
		relB = RelPos.of(4, 2);
		absA = BoundedPos.of(4, 2, BOUNDS_DONT_MATTER);
		absB = BoundedPos.of(3, 0, BOUNDS_DONT_MATTER);
	}

	@Test
	public void testProxComp() {
		BoundedPos goal = BoundedPos.of(0, 0, BOUNDS_DONT_MATTER);
		BoundedPos a = BoundedPos.of(1, 0, BOUNDS_DONT_MATTER);
		BoundedPos b = BoundedPos.of(0, 2, BOUNDS_DONT_MATTER);
		BoundedPos c = BoundedPos.of(3, 0, BOUNDS_DONT_MATTER);
		BoundedPos d = BoundedPos.of(2, 2, BOUNDS_DONT_MATTER);

		List<BoundedPos> positions = new ArrayList<>();
		positions.add(b);
		positions.add(a);
		positions.add(c);
		positions.add(d);

		Collections.sort(positions, new Pos2D.DistanceComparator(goal));

		assertTrue(positions.get(0) == a);
		assertTrue(positions.get(1) == b);
		assertTrue(positions.get(2) == c);
		assertTrue(positions.get(3) == d);
	}

	@Test
	public void testEqualsObject() {
		assertTrue(relA.equals(relB) == false);
		assertTrue(relA.equals(relA) == true);

		RelPos sameA = RelPos.of(-2, 2);
		assertTrue(relA.equals(sameA) == true);

		assertTrue(relB.equals(absA) == false);
	}

}
