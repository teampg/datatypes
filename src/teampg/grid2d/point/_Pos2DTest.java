package teampg.grid2d.point;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;




public class _Pos2DTest {
	RelPos relA;
	RelPos relB;
	AbsPos absA;
	AbsPos absB;

	@Before
	public void setUp() throws Exception {
		relA = RelPos.of(-2, 2);
		relB = RelPos.of(4, 2);
		absA = AbsPos.of(4, 2);
		absB = AbsPos.of(3, 0);
	}

	@Test
	public void testProxComp() {
		AbsPos goal = AbsPos.of(0, 0);
		AbsPos a = AbsPos.of(1, 0);
		AbsPos b = AbsPos.of(0, 2);
		AbsPos c = AbsPos.of(3, 0);
		AbsPos d = AbsPos.of(2, 2);

		List<AbsPos> positions = new ArrayList<>();
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
