package teampg.datatypes;

public interface ReadBitStr extends Cloneable {
	/**
	 * Retrieves bit at index
	 *
	 * @param index
	 *            Position to read. Right to left.
	 * @return Value at position. true:1, false:0
	 */
	public boolean getBit(int index);

	/**
	 * Retrieves integer value from bits in specified range
	 *
	 * @param startBit
	 *            Position to start reading. Rightmost bit of range.
	 * @param bitCount
	 *            Number of bits to read; includes startBit (bitCount 1 would
	 *            read the bit at startBit).
	 * @return Integer value of bits in range. Reading 011 would return 3.
	 */
	public int getBits(int startBit, int bitCount);

	/**
	 * Gets allocated size of BitStr.
	 *
	 * @return Size of bitstring
	 */
	public int size();

	/**
	 * A BitStrInterface is equal to another if size and value of each bit are
	 * the same.
	 *
	 * @param what
	 *            An object that implements BitStrInterface.
	 */
	@Override
	public boolean equals(Object what);

	public int getAll();

	public Object clone();
}
