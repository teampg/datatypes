package teampg.datatypes;


/**
 * Implementation of BitStrInterface backed by byte(s)
 * <p>
 * Stores bits in an array of bytes; 1 byte where length <= 8; 2 bytes where length <= 16, etc.
 * 
 * @author YOU!
 *
 */
public class BitStr implements BitStrInterface {

	/**
	 * Creates a BitString of some length; all bits are set to 0.
	 * @param length Number of bits to allowed
	 */
	public BitStr(int length) {
		// TODO Auto-generated method stub		
		
	}
	
	/**
	 * Creates a BitString of some length, and sets contents to given value.
	 * @param length Number of bits allowed
	 * @param value Initial value of bits.  If value doesn't fill all bits, highest order bits are set to 0.
	 */
	public BitStr(int length, int value) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void storeBit(int index, boolean inBit) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeBits(int startBit, int bitCount, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getBit(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getBits(int startBit, int bitCount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static BitStrInterface newInstance(BitStrInterface aBS) {
		// TODO Auto-generated method stub
		return null;
	}

}
