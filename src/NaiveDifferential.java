
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class NaiveDifferential implements Database {
	@Override
	public String retrieveRecord(String key) {
		return retrieveRecordFrom(key, DIFF_FILE)
				.orElseGet(() -> retrieveRecordFrom(key, DATABASE_FILE).orElseGet(() -> null));
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
}