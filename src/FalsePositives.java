
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

// Estimate the false positive probability of filters
public class FalsePositives {
	private static BloomFilter[] filters;
	private static ArrayList<String> list;

	private static final double numTest = 10000;
	private static final int setSize = 100000;
	private static final int bitsPerElement = 16;

	/**
	 * test experiment for False Positives
	 */
	public static void main(String[] args) {
		filters = new BloomFilter[3];
		list = new ArrayList<String>();
		filters[0] = new BloomFilterFNV(setSize, bitsPerElement);
		filters[1] = new BloomFilterRan(setSize, bitsPerElement);
		filters[2] = new DynamicFilter(bitsPerElement);
		int length = ThreadLocalRandom.current().nextInt(4, bitsPerElement + 1);

		String random;
		for (int i = 0; i < setSize; i++) {
			random = generateRandomString(length);

			for (int j = 0; j < filters.length; j++) {
				filters[j].add(random);
			}
			list.add(random.toLowerCase());
		}

		int[] falsePositives = new int[3];

		double i = numTest;
		do {
			random = generateRandomString(length);

			if (list.contains(random.toLowerCase())) {
				continue;
			}

			for (int j = 0; j < filters.length; j++) {
				if (filters[j].appears(random)) {
					falsePositives[j]++;
				}
			}

			i--;
		} while (i > 0);

		double percent;
		for (int j = 0; j < filters.length; j++) {
			percent = ((double) falsePositives[j] / numTest) * 100;
			System.out.printf(filters[j].getClass().getName() + ": " + "%.2f%%\n", percent);
		}
	}

	/**
	 * Generates a random string of length len
	 */
	public static String generateRandomString(int len) {
		if (len < 1)
			throw new IllegalArgumentException();

		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder sb = new StringBuilder(len);
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < len; i++) {
			int index = random.nextInt(chars.length());
			char ranChar = chars.charAt(index);

			sb.append(ranChar);

		}

		return sb.toString();

	}

}