

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class BloomDifferential implements Database {

	private static final int bitsPerElement = 8;

	private BloomFilter filter = null;

	/**
	 * Returns a Bloom Filter corresponding to the records in the differential file,
	 * `DiffFile.txt`.
	 */
	public BloomFilter createFilter() {
		if (filter != null) {
			return filter;
		}
		BloomFilter filter = new BloomFilterFNV(countLines(DIFF_FILE), bitsPerElement);
		try (Stream<String> stream = Files.lines(DIFF_FILE)) {
			stream.forEach((l) -> filter.add(splitLine(l).getKey()));
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		this.filter = filter;
		return filter;
	}

	@Override
	public String retrieveRecord(String key) {
		if (filter == null) {
			createFilter();
		}
		if (filter.appears(key)) {
			Optional<String> value = retrieveRecordFrom(key, DIFF_FILE);
			if (value.isPresent()) {
				return value.get();
			}
		}
		return retrieveRecordFrom(key, DATABASE_FILE).orElseGet(() -> null);
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
	 * A helper method to scan DIFF_FILE to see if it contains the
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