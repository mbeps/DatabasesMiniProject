
import java.io.*;
import java.util.*;

public class Parsing {
	/**
	 * Reads string and splits every time there is a comma `,`.
	 * 
	 * @param str (String): string that needs to be split
	 */
	public static String[] split(String str) {
		/**
		 * Every time there is a comma, the string is split.
		 * There are 4 splits.
		 * Each section after the comma is stored in the array
		 */
		return str.split(",");
	}

	/**
	 * Reads a file line.
	 * Each line is added to an array which is then returned.
	 * 
	 * @param filename (String): name of the to be read
	 * @return (ArrayList): array where all the lines in the file are stored
	 */
	public static ArrayList<String> readFileStore(String filename) {
		try {
			File file = new File(filename);
			Scanner myReader = new Scanner(file);
			ArrayList<String> line = new ArrayList<String>();

			while (myReader.hasNextLine()) {
				// Add the next line into the array
				line.add(myReader.nextLine());
			}
			myReader.close();
			return line;
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
			return null;
		}
	}

	public static void splitFile(String filename) {
		for (String line : readFileStore(filename)) {
			System.out.println(line);
		}
	}

	public static void main(String args[]) {
		// String str = "ABR,Aberdeen Regional Airport,Aberdeen,SD";

		// for (String word : split(str))
		// 	System.out.println(word);

		System.out.println(readFileStore("src/airport").get(5));

		String line = readFileStore("src/airport").get(5);
		for (int i = 0; i < readFileStore("src/airport").size(); i++) {
			String sections[] = split(readFileStore("src/airport").get(i));
			for (String section : sections) {
				System.out.println(section);
			}
		}
		// for (String word : split(line)) {
		// 	System.out.println(word);
		// }
	}
}
