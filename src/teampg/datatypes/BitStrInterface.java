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

public interface BitStrInterface extends ReadBitStr {

	/**
	 * Stores a single bit at given position
	 *
	 * @param index
	 *            Position to store bit.
	 * @param inBit
	 *            Value to store. True represents 1; false 0.
	 */
	public void setBit(int index, boolean inBit);

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
	public void setBits(int startBit, int bitCount, int value);

	public void fill(int value);
}