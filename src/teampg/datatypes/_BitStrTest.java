package teampg.datatypes;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;




public class _BitStrTest {
	final int LEN = 3;
	BitStrInterface aBS;

	@Before
	public void setUp() throws Exception {
		aBS = new BitStr(LEN);
	}

	/*-**************
	 * CONSTRUCTORS *
	 ****************/
	@Test
	public void testConstruct() {
		assertTrue(bitStrHasValue(aBS, 0b000, LEN));
	}

	@Test
	public void testValueConstructor() {
		final int len = 4;
		final int val = 0b1101;
		BitStrInterface bitStr1101 = new BitStr(len, val);
		assertTrue(bitStrHasValue(bitStr1101, val, len));
	}

	@Test
	public void testGet() {
		BitStrInterface bitStr01101 = new BitStr(5, 0b01101);
		assertEquals(false, bitStr01101.getBit(4));
		assertEquals(true, bitStr01101.getBit(3));
		assertEquals(true, bitStr01101.getBit(2));
		assertEquals(false, bitStr01101.getBit(1));
		assertEquals(true, bitStr01101.getBit(0));
	}
	@Test
	public void testGetBits() {
		BitStrInterface bitStr01101 = new BitStr(5, 0b01101);
		assertEquals(0b11, bitStr01101.getBits(2, 2));
		assertEquals(0b1101, bitStr01101.getBits(0, 4));
		assertEquals(0b011, bitStr01101.getBits(2, 3));
		assertEquals(0b01, bitStr01101.getBits(0, 2));
		assertEquals(0b1, bitStr01101.getBits(0, 1));
	}

	/*-*********
	 * METHODS *
	 ***********/
	@Test
	public void testStoreBitOnce() {
		aBS.setBit(0, true);
		assertTrue(bitStrHasValue(aBS, 0b001, LEN));

		aBS.setBit(1, true);
		assertTrue(bitStrHasValue(aBS, 0b011, LEN));

		aBS.setBit(2, true);
		assertTrue(bitStrHasValue(aBS, 0b111, LEN));
	}

	@Test
	public void testStoreBitMany() {
		aBS.setBit(0, true);
		assertTrue(bitStrHasValue(aBS, 0b001, LEN));
		aBS.setBit(0, false);
		assertTrue(bitStrHasValue(aBS, 0b000, LEN));
		aBS.setBit(0, true);
		assertTrue(bitStrHasValue(aBS, 0b001, LEN));

		aBS.setBit(1, true);
		assertTrue(bitStrHasValue(aBS, 0b011, LEN));
		aBS.setBit(1, false);
		assertTrue(bitStrHasValue(aBS, 0b001, LEN));
		aBS.setBit(1, true);
		assertTrue(bitStrHasValue(aBS, 0b011, LEN));

		aBS.setBit(2, true);
		assertTrue(bitStrHasValue(aBS, 0b111, LEN));
		aBS.setBit(2, false);
		assertTrue(bitStrHasValue(aBS, 0b011, LEN));
		aBS.setBit(2, true);
		assertTrue(bitStrHasValue(aBS, 0b111, LEN));
	}

	@Test
	public void testStoreAllBits() {
		final int bits101 = 0b101;
		final int bits010 = 0b010;
		final int bits111 = 0b111;
		final int bits000 = 0b000;

		aBS.setBits(0, LEN, bits101);
		assertTrue(bitStrHasValue(aBS, bits101, LEN));

		aBS.setBits(0, LEN, bits010);
		assertTrue(bitStrHasValue(aBS, bits010, LEN));

		aBS.setBits(0, LEN, bits111);
		assertTrue(bitStrHasValue(aBS, bits111, LEN));

		aBS.setBits(0, LEN, bits000);
		assertTrue(bitStrHasValue(aBS, bits000, LEN));
	}

	@Test
	public void testStoreSomeBits() {
		final int bits01 = 0b01;
		final int bits11 = 0b11;
		final int bits00 = 0b00;
		final int inLen = 2;

		assertTrue(bitStrHasValue(aBS, 0b000, LEN));

		aBS.setBits(1, inLen, bits11);
		assertTrue(bitStrHasValue(aBS, 0b110, LEN));

		aBS.setBits(0, inLen, bits00);
		assertTrue(bitStrHasValue(aBS, 0b100, LEN));

		aBS.setBits(1, inLen, bits11);
		assertTrue(bitStrHasValue(aBS, 0b110, LEN));

		aBS.setBits(0, inLen, bits01);
		assertTrue(bitStrHasValue(aBS, 0b101, LEN));

		aBS.setBits(0, 3, 3);
		assertTrue(bitStrHasValue(aBS, 3, LEN));
	}

	@Test
	public void testEqualsBitString() {
		BitStrInterface longBS = new BitStr(5);
		assertTrue(!longBS.equals(aBS));

		BitStrInterface diffValueBS = new BitStr(3);
		diffValueBS.setBit(1, true);
		assertTrue(!diffValueBS.equals(aBS));

		BitStrInterface sameDiffValueBS = new BitStr(3);
		sameDiffValueBS.setBit(1, true);
		assertTrue(diffValueBS.equals(sameDiffValueBS));
	}

	/**
	 * newInstance should copy by value, not reference.
	 */
	@Test
	public void testNewInstance() {
		aBS.setBit(1, true);
		BitStrInterface newABS = (BitStrInterface) aBS.clone();

		assertTrue(aBS.equals(newABS));
		assertTrue(!(aBS == newABS));

		aBS.setBit(2, true);

		assertTrue(!aBS.equals(newABS));
	}


	/*-************
	 * EXCEPTIONS *
	 **************/

	//used by testStoreBitsExceptions
	private void assertStoreBitsRaisesException(BitStrInterface b, int toStoreStart, int toStoreLen, int toStoreVal) {
		boolean threwRightException = false;
		final int initialLength = b.size();
		final int initialValue = b.getBits(0, initialLength);

		try {
			b.setBits(toStoreStart, toStoreLen, toStoreVal);
		} catch (IndexOutOfBoundsException e) {
			threwRightException = true;
		}

		assertTrue(bitStrHasValue(b, initialValue, initialLength));
		assertTrue(threwRightException);
	}

	@Test
	public void testStoreBitsExceptions() {
		int startPos;
		int length;
		int value;

		//past end
		startPos = LEN;
		length = 1;
		value = 0b1;
		assertStoreBitsRaisesException(aBS, startPos, length, value);

		//before beginning
		startPos = -1;
		length = 1;
		value = 0b1;
		assertStoreBitsRaisesException(aBS, startPos, length, value);

		//length longer than available space
		startPos = 0;
		length = 4;
		value = 0b1;
		assertStoreBitsRaisesException(aBS, startPos, length, value);

		//goes past end
		startPos = 2;
		length = 2;
		value = 0b11;
		assertStoreBitsRaisesException(aBS, startPos, length, value);

		//value bigger than given size
		startPos = 0;
		length = 1;
		value = 0b11;
		try {
			aBS.setBits(startPos, length, value);
			fail("Didn't throw correct exception");
		} catch (IllegalArgumentException e) {
		}
	}

	//used by testStoreBitExceptions
	private void assertStoreBitRaisesException(BitStrInterface b, int toStoreStart, boolean toStoreVal) {
		boolean threwRightException = false;
		final int initialLength = b.size();
		final int initialValue = b.getBits(0, initialLength);

		try {
			b.setBit(toStoreStart, toStoreVal);
		} catch (IndexOutOfBoundsException e) {
			threwRightException = true;
		}

		assertTrue(bitStrHasValue(b, initialValue, initialLength));
		assertTrue(threwRightException);
	}

	@Test
	public void testStoreBitExceptions() {
		int startPos;
		boolean value;

		//past end
		startPos = LEN;
		value = true;
		assertStoreBitRaisesException(aBS, startPos, value);

		//before beginning
		startPos = -1;
		value = true;
		assertStoreBitRaisesException(aBS, startPos, value);
	}

	@Test
	public void testGetBitExceptions() {
		boolean threwRightException;

		//before start
		threwRightException = false;
		try {
			aBS.getBit(-1);
		} catch (IndexOutOfBoundsException e) {
			threwRightException = true;
		}

		assertTrue(threwRightException);

		//after end
		threwRightException = false;
		try {
			aBS.getBit(LEN);
		} catch (IndexOutOfBoundsException e) {
			threwRightException = true;
		}

		assertTrue(threwRightException);
	}



	//used by testGetBitsExceptions
	private void assertGetBitsRaisesException(BitStrInterface b, int toGetStart, int toGetLen) {
		boolean threwRightException = false;

		try {
			b.getBits(toGetStart, toGetLen);
		} catch (IndexOutOfBoundsException e) {
			threwRightException = true;
		}

		assertTrue(threwRightException);
	}

	@Test
	public void testGetBitsExceptions() {
		int startPos;
		int length;

		//past end
		startPos = LEN;
		length = 1;
		assertGetBitsRaisesException(aBS, startPos, length);

		//before beginning
		startPos = -1;
		length = 1;
		assertGetBitsRaisesException(aBS, startPos, length);

		//goes past end
		startPos = 2;
		length = 2;
		assertGetBitsRaisesException(aBS, startPos, length);
	}

	@Test
	public void testBigBitStr() {
		int bigVal = 0b10101010101010101010;
		int bigLen = 20;
		BitStrInterface bigBS = new BitStr(bigLen, bigVal);

		assertTrue(bitStrHasValue(bigBS, bigVal, bigLen));

		bigBS.setBits(9, 10, 0b1011111111);
		assertTrue(bitStrHasValue(bigBS, 0b11011111111010101010, bigLen));

	}


	/*-**************
	 * UTIL METHODS *
	 ****************/
	private boolean bitStrHasValue(BitStrInterface b, int expectedValue, int expectedLength) {
		final int len = b.size();

		if ( ! (len == expectedLength)) {
			return false;
		}

		int val = b.getBits(0, len);
		return val == expectedValue;
	}

}
