package teampg.datatypes;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import java.math.RoundingMode;

import com.google.common.math.IntMath;

/**
 * Implementation of BitStrInterface backed by byte(s)
 * <p>
 * Stores bits in an array of bytes; 1 byte where length <= 8; 2 bytes where
 * length <= 16, etc.
 *
 * @author JWill
 *
 */
public class BitStr implements BitStrInterface {
	private final int length;
	private int bits;

	/**
	 * Creates a BitString of some length; all bits are set to 0.
	 *
	 * @param length
	 *            Number of bits to allowed
	 */
	public BitStr(int length) {
		checkArgument(length > 0);
		this.length = length;
		bits = 0;
	}

	/**
	 * Creates a BitString of some length, and sets contents to given value.
	 *
	 * @param length
	 *            Number of bits allowed
	 * @param value
	 *            Initial value of bits. If value doesn't fill all bits, higher
	 *            order bits are set to 0.
	 */
	public BitStr(int length, int value) {
		this(length);
		set(value);
	}

	@Override
	public void setBit(int index, boolean inBit) {
		checkElementIndex(index, length);

		if (getBit(index) == inBit) {
			return; //already in desired state
		}

		//else flip bit
		int newBits = 0;
		if (getBit(index) == true) {
			newBits = bits - (1 << index);
		} else {
			newBits = bits + (1 << index);
		}

		set(newBits);
	}

	@Override
	public void setBits(int startBit, int bitCount, int value) {
		checkArgument(bitCount >= 0);
		checkArgument(value >= 0);
		checkElementIndex(startBit, length);
		checkElementIndex(startBit + (bitCount - 1), length);
		checkArgument(countBits(value) <= bitCount, "Value is larger than given bitCount"); //fill left with zeroes

		// clear current bits
		int newBits =  bits - (getBits(startBit, bitCount) << startBit);

		// set to desired bits
		set(newBits + (value << startBit));
	}

	@Override
	public boolean getBit(int index) {
		checkElementIndex(index, length);

		return (bits & (1 << index)) != 0;
	}

	@Override
	public int getBits(int startBit, int bitCount) {
		checkArgument(bitCount >= 0);
		checkElementIndex(startBit, length);
		checkElementIndex(startBit + (bitCount - 1), length);

		int rightTrimmed = bits >> startBit;
		int fullTrimmed = rightTrimmed % (1 << bitCount);

//		System.out.println("bits: 0b" + Integer.toBinaryString(bits) + " start: " + startBit
//				+ " count: " + bitCount + " rightTrimmed: 0b" + Integer.toBinaryString(rightTrimmed)
//				+ " final: 0b" + Integer.toBinaryString(fullTrimmed));

		return fullTrimmed;
	}

	@Override
	public int size() {
		return length;
	}

	@Override
	public boolean equals(Object what) {
		BitStrInterface other = (BitStrInterface) what;
		if (size() != other.size()) {
			return false;
		}

		return getBits(0, length) == other.getBits(0, length);
	}

	@Override
	public String toString() {
		return "0b" + Integer.toBinaryString(bits);
	}

	@Override
	public Object clone() {
		return new BitStr(length, bits);
	}

	private void set(int value) {
		assert(value >= 0);
		if (countBits(value) > length) {
			throw new IndexOutOfBoundsException();
		}

		bits = value;
	}

	public static int countBits(int value) {
		if (value == 0) {
			return 0;
		}

		return IntMath.log2(value, RoundingMode.CEILING);
	}

	@Override
	public void fill(int value) {
		setBits(0, length, value);
	}

	@Override
	public int getAll() {
		return getBits(0, length);
	}
}
