package teampg.datatypes;
/**
 * Stores and manipulates bits.
 * 
 * <p>
 * Stores a string of bits. Bits can be retrieved and altered individually or in
 * ranges.
 * <p>
 * Note that indexes start at rightmost bits (low order). Values smaller than
 * their specified length will be padded with zeroes on the left:
 * <p>
 * <code>storeBits(startIndex:0, bitCount:3, value:0b11)</code>
 * 
 * <pre>
 *   2   1   0
 * +---+---+---+
 * | 0 | 1 | 1 |
 * +---+---+---+
 * </pre>
 * 
 * 
 * @author Jackson Williams
 */

public interface BitStrInterface {

	/**
	 * Stores a single bit at given position
	 * 
	 * @param index
	 *            Position to store bit.
	 * @param inBit
	 *            Value to store. True represents 1; false 0.
	 */
	public void storeBit(int index, boolean inBit);

	/**
	 * Stores an array of bits into BitStr, starting at startBit, ending at
	 * startBit+bitCount, counting from right to left.
	 * 
	 * @param startBit
	 *            Starting index -- lowest order bit (rightmost).
	 * @param bitCount
	 *            Number of bits. Includes bit at starting index.
	 * @param value
	 *            Value to store into specified range. If smaller than range,
	 *            zeros are added to left (high order bits).
	 */
	public void storeBits(int startBit, int bitCount, int value);

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
	 * @return True for equal, false otherwise.
	 */
	public boolean equals(Object what);

}