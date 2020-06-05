
import java.nio.file.Path;
import java.nio.file.Paths;

public interface Database {

	Path DATABASE_FILE = Paths.get("data", "database.txt");
	Path DIFF_FILE = Paths.get("data", "DiffFile.txt");
	Path GRAMS_FILE = Paths.get("data", "grams.txt");

	/**
	 * Returns the value associated with this key, or else the null if this
	 * key was not found.
	 */
	String retrieveRecord(String key);

}
