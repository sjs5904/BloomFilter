
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Arrays.sort;

public class EmpiricalComparison {
	static Path DATABASE_FILE = Paths.get("data", "database.txt");
	static Path DIFF_FILE = Paths.get("data", "DiffFile.txt");
	static Path GRAMS_FILE = Paths.get("data", "grams.txt");

	private static Random random = new Random();

	public static void main(String args[]) {
		out.println("Initializing...");
		NaiveDifferential naiveDatabase = new NaiveDifferential();
		BloomDifferential bloomDatabase = new BloomDifferential();
		BloomFilter bloomFilter = bloomDatabase.createFilter();
		int numKeys = countLines(GRAMS_FILE); // Count the total number of keys
		int numQueries = numKeys / 1000000;
		if (numQueries <= 0) {
			throw new RuntimeException("Not enough queries.");
		}

		out.println(format("Randomly selecting %d query keys...", numQueries));
		int[] indices = random.ints(numQueries, 0, numKeys).toArray();
		sort(indices);
		List<String> queryKeys = getSelectedKeys(indices);
		out.println("random example query key: " + queryKeys.get(random.nextInt(queryKeys.size())));

		out.println("time in micro second");
		out.println("Naive,\tBloom,\tInDiff,\tInFilter");
		for (String key : queryKeys) {
			out.println(format("%d,\t%d,\t%b,\t%b", timeQueryDuration(naiveDatabase, key).toMillis(),
					timeQueryDuration(bloomDatabase, key).toMillis(), isInDiffFile(key), bloomFilter.appears(key)));
		}

		out.println();
		out.println("-- Finished --");
	}

	/**
	 * return time(ms) taken finding records
	 * 
	 * @param db
	 * @param key
	 */
	private static Duration timeQueryDuration(Database db, String key) {
		Instant start = Instant.now();
		String value = db.retrieveRecord(key);
		Instant end = Instant.now();
		if (value == null) {
			throw new RuntimeException("Cannot find records.");
		}
		return Duration.between(start, end);
	}

	/**
	 * Gets the selected indices of keys from GRAMS_FILE. Return values identified
	 * in the argument indicate line numbers of the file.
	 *
	 * @param selections
	 */
	private static List<String> getSelectedKeys(int[] selections) {
		try (Stream<String> stream = Files.lines(GRAMS_FILE)) {
			List<String> list = new ArrayList<>(selections.length);

			Iterator<String> iter = stream.iterator();
			int iterIdx = 0;
			String cur = iter.next();

			for (int selection : selections) {
				while (selection != iterIdx) {
					if (!iter.hasNext()) {
						String msg = "end of the stream";
						throw new IllegalStateException(msg);
					} else {
						iterIdx++;
						cur = iter.next();
					}
				}
				list.add(cur);
			}
			return list;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Splits the given line in the database file into a key value pair.
	 */
	static Map.Entry<String, String> splitLine(String line) {
		int count = 0;
		int cur = 0;
		do {
			cur = line.indexOf(' ', cur + 1);
			count++;
		} while (0 <= cur && count < 4);

		String key, value;
		if (0 <= cur && cur < line.length()) {
			key = line.substring(0, cur); //
			value = line.substring(cur + 1, line.length());
		} else {
			key = line;
			value = "";
		}

		return new Map.Entry<String, String>() {
			@Override
			public String getKey() {
				return key;
			}

			@Override
			public String getValue() {
				return value;
			}

			@Override
			public String setValue(String value) {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * Searches the file indicated by the given path
	 */
	static Optional<String> retrieveRecordFrom(String key, Path path) {
		try (Stream<String> stream = Files.lines(path)) {
			return stream.filter((l) -> key.equals(splitLine(l).getKey())).findFirst();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * count how many lines given file contains or the number of key-value pairs which it contains
	 */
	static int countLines(Path path) {
		try (LineNumberReader reader = new LineNumberReader(new FileReader(path.toFile()))) {
			while (reader.readLine() != null) {
			}
			return reader.getLineNumber();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * A helper method to scans DIFF_FILE to see if it contains the
	 * given key.
	 */
	static boolean isInDiffFile(String key) {
		try (Stream<String> stream = Files.lines(DIFF_FILE)) {
			return stream.anyMatch((l) -> key.equals(splitLine(l).getKey()));
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}
