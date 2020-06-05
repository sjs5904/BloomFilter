
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class BloomJoin {
	String f1;
	DynamicFilter f;
	HashMap<String, String> relation2;

	/**
	 * A constructor that takes names of two files that hold relations R1 and R2.
	 */
	public BloomJoin(String f1, String f2) throws IOException {
		f = new DynamicFilter(8);
		this.f1 = f1;
		relation2 = new HashMap<String, String>();

		FileInputStream fstream = new FileInputStream(f2);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String line;

		while ((line = br.readLine()) != null) {
			String[] tokens = line.split("\\s+");
			f.add(tokens[0]);
			relation2.put(tokens[0], tokens[1]);
		}

		br.close();
	}

	/**
	 * Writes the join of R1 and R2 to a file named f3.
	 */
	public void join(String f3) throws IOException {
		FileInputStream fstream = new FileInputStream(f1);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String output = "";

		String line;
		while ((line = br.readLine()) != null) {
			String[] tokens = line.split("\\s+");
			if (f.appears(tokens[0])) {
				output += tokens[1] + " " + tokens[0] + " " + relation2.get(tokens[0]) + "\n";
			}
		}
		br.close();

		BufferedWriter writer = new BufferedWriter(new FileWriter(f3));
		writer.write(output);
		writer.close();
	}

}