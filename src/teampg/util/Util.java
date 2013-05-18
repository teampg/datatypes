package teampg.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Util {
	private static Random rand = new Random();

	public static void setSeed(long seed) {
		rand = new Random(seed);
	}

	public static <T> T choice(T... options) {
		return options[rand.nextInt(options.length)];
	}

	public static <T> T choice(List<T> options) {
		return options.get(rand.nextInt(options.size()));
	}

	@SuppressWarnings("rawtypes")
	private static Map<Class<Enum>, Enum[]> enumValues = new HashMap<>();
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <E extends Enum> E choice(Class<E> token) {
		if (!enumValues.containsKey(token)) {
			enumValues.put((Class<Enum>) token, token.getEnumConstants());
		}

		E[] tokenTypeEnumValues = (E[]) enumValues.get(token);
		return tokenTypeEnumValues[rand.nextInt(tokenTypeEnumValues.length)];
	}

	public static <T extends Iterable<T>> void addEachBranchAndLeaf(List<T> ret, T branch) {
		ret.add(branch);

		Iterator<T> iter = branch.iterator();
		if (iter.hasNext() == false) {
			return;
		}

		while (iter.hasNext()) {
			T toAdd = iter.next();
			addEachBranchAndLeaf(ret, toAdd);
		}
	}
}
