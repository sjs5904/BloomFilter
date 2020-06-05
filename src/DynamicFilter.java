
import java.util.ArrayList;

public class DynamicFilter implements BloomFilter {
	static int setSize = 1000;
	int dataSize;
	int bitsPerElement;

	// Array list of Random Bloom Filters
	ArrayList<BloomFilterRan> filters = new ArrayList<BloomFilterRan>();

	public DynamicFilter(int bitsPerElement) {
		this.bitsPerElement = bitsPerElement;
		filters.add(new BloomFilterRan(setSize, bitsPerElement));
	}

	@Override
	public void add(String s) {
		s = s.toLowerCase();

		if (!appears(s)) {
			dataSize++;
		}

		if (dataSize >= setSize) {
			setSize *= 2;
			filters.add(new BloomFilterRan(setSize, bitsPerElement));
		}

		for (int i = 0; i < filters.size(); i++) {
			filters.get(i).add(s);
		}
	}

	@Override
	public boolean appears(String s) {
		s = s.toLowerCase();

		for (int i = 0; i < filters.size(); i++) {
			if (!filters.get(i).appears(s)) {
				return false;
			}

		}

		return true;
	}

	@Override
	public int filterSize() {
		int size = 0;
		for (int i = 0; i < filters.size(); i++) {
			size += filters.get(i).filterSize();
		}
		return size;
	}

	@Override
	public int dataSize() {
		return dataSize;
	}

	@Override
	public int numHashes() {
		int hashes = 0;
		for (int i = 0; i < filters.size(); i++) {
			hashes += filters.get(i).numHashes();
		}
		return hashes;
	}

	@Override
	public boolean getBit(int j) {
		for (int i = 0; i < filters.size(); i++) {
			if (filters.get(i).getBit(j)) {
				return true;
			}
		}
		return false;
	}

}