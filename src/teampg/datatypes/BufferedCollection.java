package teampg.datatypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterables;

public class BufferedCollection<E> implements Collection<E>{
	private final List<E> data;
	private final List<E> addBuffer;
	private final List<E> removeBuffer;

	public BufferedCollection() {
		data = new ArrayList<>();
		addBuffer = new ArrayList<>();
		removeBuffer = new ArrayList<>();
	}

	public BufferedCollection(Iterable<E> c) {
		this();
		Iterables.addAll(data, c);
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return data.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return data.iterator();
	}

	@Override
	public Object[] toArray() {
		return data.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return data.toArray(a);
	}

	@Override
	public boolean add(E e) {
		return addBuffer.add(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		return removeBuffer.add((E)o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return data.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException(); //TODO ... not using atm, not bothering to implement
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		data.clear();
		addBuffer.clear();
		removeBuffer.clear();
	}

	public void mergeAddBuffer() {
		data.addAll(addBuffer);
		addBuffer.clear();
	}

	public void mergeRemoveBuffer() {
		data.removeAll(removeBuffer);
		removeBuffer.clear();
	}

	@Override
	public String toString() {
		return "BufferedCollection [data=" + data + ", addBuffer=" + addBuffer + ", removeBuffer="
				+ removeBuffer + "]";
	}
}
