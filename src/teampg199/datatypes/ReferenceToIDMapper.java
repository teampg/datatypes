package teampg199.datatypes;

import java.util.Hashtable;

//TODO TEST
public class ReferenceToIDMapper {
	private Hashtable<Object, Long> map;
	private long idCount;

	private ReferenceToIDMapper() {
		map = new Hashtable<>(100);
		idCount = 0;
	}

	private long add(Object e) {
		idCount++;
		map.put(e, idCount);

		return idCount;
	}

	public long getID(Object e) {
		Long found = map.get(e);
		
		if (found == null) {
			return add(e);
		}
		
		return found;
	}
}
