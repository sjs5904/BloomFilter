
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BloomFilterRan implements BloomFilter {
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

	public BloomFilterRan(int setSize, int bitsPerElement) {
		size = setSize * bitsPerElement;
		numHashes = (int) Math.round(Math.log(2) * (size / setSize));
		filter = new BitSet(size);
		hashFunctions = new HashFunction[numHashes];
		dataSize = 0;
		hashFunctions = new HashFunctionRan[numHashes];
		for (int i = 0; i < numHashes; i++) {
			hashFunctions[i] = new HashFunctionRan();
		}

	}

	@Override
	public void add(String s) {
		int key;
		s = s.toLowerCase();

		if (!appears(s)) {
			dataSize++;
		}

		for (int i = 0; i < numHashes; i++) {
			key = hashFunctions[i].hash(s);
			filter.set(HashFunction.mod(key, size));
		}
	}

	@Override
	public boolean appears(String s) {
		int key;
		s = s.toLowerCase();
		for (int i = 0; i < numHashes; i++) {
			key = hashFunctions[i].hash(s);
			if (!filter.get(HashFunction.mod(key, size))) {
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

	private class HashFunctionRan extends HashFunction {
		int prime = getPrime(size);
		int a = ThreadLocalRandom.current().nextInt(0, prime);
		int b = ThreadLocalRandom.current().nextInt(0, prime);

		public int hash(String s) {
			return hash(s.hashCode());
		}

		private int hash(int x) {
			return mod((a * x) + b, prime);
		}

		/**
		 * get prime number bigger than n
		 * 
		 * @param n
		 * @return boolean
		 */
		private int getPrime(int n) {
			boolean found = false;

			while (!found) {
				if (isPrime(n)) {
					found = true;
				} else {
					if (n == 1 || n % 2 == 0) {
						n = n + 1;
					} else {
						n = n + 2;
					}
				}
			}
			return n;
		}

		/**
		 * return true if inputNum is prime
		 * 
		 * @param inputNum
		 * @return boolean
		 */
		private boolean isPrime(int inputNum) {
			if (inputNum <= 3 || inputNum % 2 == 0)
				return inputNum == 2 || inputNum == 3;
			int divisor = 3;
			while ((divisor <= Math.sqrt(inputNum)) && (inputNum % divisor != 0))
				divisor += 2;
			return inputNum % divisor != 0;
		}
	}
}