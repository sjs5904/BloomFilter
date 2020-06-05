
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;

public class BloomFilterFNV implements BloomFilter {
	// Filter size
	public int size;
	// Number of elements added to the Bloom Filter
	public int dataSize;
	// Number of hash functions
	public int numHashes;
	// Bloom Filter
	public BitSet filter;
	// Set of hash functions
	public HashFunction[] hashFunctions;
	// bits to be taken placed by hash values of one element
	public int bitsPerElement;

	public BloomFilterFNV(int setSize, int bitsPerElement) {
		this.bitsPerElement = bitsPerElement;
		size = setSize * bitsPerElement;
		numHashes = (int) Math.round(Math.log(2) * (size / setSize));
		filter = new BitSet(size);
		hashFunctions = new HashFunction[numHashes];
		dataSize = 0;
		for (int i = 0; i < numHashes; i++) {
			hashFunctions[i] = new HashFunctionFNV();
		}
	}

	@Override
	public void add(String s) {
		if (s == null)
			throw new IllegalArgumentException("String s null error.");

		int key;
		s = s.toLowerCase();

		if (!appears(s)) {
			dataSize++;
		}

		for (int i = 0; i < numHashes; i++) {
			key = hashFunctions[i].hash(s);
			filter.set(key);
		}
	}

	@Override
	public boolean appears(String s) {
		int key;
		s = s.toLowerCase();

		for (int i = 0; i < numHashes; i++) {
			key = hashFunctions[i].hash(s);
			if (!filter.get(key)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int filterSize() {
		return size;
	}

	@Override
	public int dataSize() {
		return dataSize;
	}

	@Override
	public int numHashes() {
		return numHashes;
	}

	@Override
	public boolean getBit(int j) {
		return filter.get(j);
	}
	
	/**
	 * number of zeros in filter
	 */
	public int count0() {
		return size - filter.cardinality();
	}

	private class HashFunctionFNV extends HashFunction {
		private final BigInteger FNVInit = new BigInteger("109951168211"); // 64
		private final BigInteger FNVPrime = new BigInteger("14695981039346656037");
		private final BigInteger FNVBigInt = new BigInteger("9223372036854775807");
		Random rand = new Random();
		int n = rand.nextInt();

		public int hash(String s) {
			s = s + n;
			return hash(s.getBytes());
		}

		private int hash(byte[] k) {
			long h = FNVInit.longValue();
			int len = k.length;
			for (int i = 0; i < len; i++) {
				h ^= k[i];
				h *= FNVPrime.longValue();
				h = mod(h, FNVBigInt.longValue());
			}
			return mod(h, size);
		}

	}

}