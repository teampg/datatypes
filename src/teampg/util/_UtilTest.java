package teampg.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

public class _UtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testBounded() {
		Range<Integer> r = Range.closedOpen(0, 11);
		DiscreteDomain<Integer> d = DiscreteDomain.integers();

		// values already in range are unchanged
		int[] inRange = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		for (int i : inRange) {
			assertEquals(new Integer(i), Util.bounded(r, d, i));
		}

		// values below range are set to lowest val in range
		int[] belowRange = {-10000, -10, -1};
		for (int i : belowRange) {
			assertEquals(new Integer(0), Util.bounded(r, d, i));
		}

		// values above range are set to highest val in range
		int[] aboveRange = {11, 100, 10000};
		for (int i : aboveRange) {
			assertEquals(new Integer(10), Util.bounded(r, d, i));
		}


	}

}
