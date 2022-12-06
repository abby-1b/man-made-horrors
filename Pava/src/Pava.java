import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

class Pava {
	public static String errString = "\033[91m";

	public static void main(String[] args) {
		// Get file name
		String fileName = Pava.getFileFromArgs(args);

		// Get code from file and compile
		String code = Pava.getFileContents(fileName);
		String finalCode = Converter.convert(code);

		if (Pava.argIdx(args, "-r") >= 0) {
			// If "-r" flag is set, run the file directly.

		} else if (Pava.argIdx(args, "-p") >= 0) {
			// If "-p" flag is set, output to console and exit
			System.out.println(finalCode);
		} else if (Pava.argIdx(args, "-o") >= 0) {
			// If output file is set, output to the set file
			int idx = Pava.argIdx(args, "-o") + 1;
			if (idx >= args.length) {
				System.out.println(Pava.errString + "Output file name not found, but -o flag is set!");
				System.exit(1);
			}

			Pava.putFileContents(args[Pava.argIdx(args, "-o") + 1], finalCode);
		} else {
			// If no flag is set, use filename with ".java" extension.
			Pava.putFileContents(fileName.substring(0, fileName.lastIndexOf('.')) + ".java", finalCode);
		}
	}

	private static int argIdx(String[] args, String arg) {
		for (int a = 0; a < args.length; a++) {
			if (args[a].equals(arg)) return a;
		}
		return -1;
	}

	private static String getFileFromArgs(String[] args) {
		for (int a = 0; a < args.length; a++) {
			if (args[a].charAt(0) != '-') return args[a];
		}
		System.out.println(Pava.errString + "No input file");
		System.exit(1);

		return "";
	}

	private static String getFileContents(String path) {
		try {
			String ret = "";

			File fileObj = new File(path);
			Scanner fileReader = new Scanner(fileObj);

			while (fileReader.hasNextLine()) {
				ret += fileReader.nextLine() + "\n";
			}

			fileReader.close();

			return ret;
		} catch (FileNotFoundException e) {
			System.out.println(Pava.errString + "Couldn't read file " + path);
			e.printStackTrace();
			System.exit(1);
		}
		return "";
	}

	private static boolean putFileContents(String path, String data) {
		try {
			FileWriter writer = new FileWriter(path);
			writer.write(data);
			writer.close();
			return true;
		} catch (IOException e) {
			System.out.println(Pava.errString + "Couldn't write to file " + path);
			System.exit(1);
		}
		return false;
	}
}

class Converter {
	public static String convert(String code) {
		code += "\n ";
		ArrayList<Integer> bracketIndexes = new ArrayList<Integer>();
		ArrayList<String> brackets = new ArrayList<String>();

		boolean inSingleComment = false;
		boolean inStr = false;

		boolean stillTab = true;
		int lastTabCount = 0;
		int tabCount = 0;

		int bracketCount = 0;

		for (int i = 0; i < code.length(); i++) {
			// Skip single-line comments
			if (code.charAt(i) == '/' && code.charAt(i + 1) == '/') inSingleComment = true;
			if (inSingleComment && code.charAt(i) == '\n') inSingleComment = false;
			if (inSingleComment) continue;

			// Skip strings
			if (code.charAt(i) == '"' && (i == 0 || (code.charAt(i - 1) != '\\'))) inStr = !inStr;
			if (inStr) continue;

			// Skip single characters
			if (code.charAt(i) == '\'') i += code.charAt(i + 1) == '\\' ? 3 : 2;

			// TODO: use bracketcount to avoid putting extra symbols inside multi-line containers.

			// Count tabs at the start of a line
			if (code.charAt(i) != '\t' && stillTab && bracketCount == 0) {
				stillTab = false;
				int diff = tabCount - lastTabCount;
				lastTabCount = tabCount;

				// When the tabs are counted, check if they need to be changed.
				if (diff <= 0) {
					bracketIndexes.add(i);
					brackets.add(";");
				}
				if (diff != 0) {
					bracketIndexes.add(i);
					brackets.add((diff > 0 ? "{" : "}").repeat(Math.abs(diff)));
				}
			}
			if (code.charAt(i) == '\t' && stillTab) tabCount++;
			if (code.charAt(i) == '\n') { stillTab = true; tabCount = 0; }


			if ("([{".contains(Character.toString(code.charAt(i)))) bracketCount++;
			if ("}])".contains(Character.toString(code.charAt(i)))) bracketCount--;
		}

		// Add brackets to their proper positions in the code.
		String outCode = "";
		int bracketIdx = 0;
		for (int i = 0; i < code.length(); i++) {
			while (bracketIdx < bracketIndexes.size() && bracketIndexes.get(bracketIdx) == i) {
				outCode += brackets.get(bracketIdx++);
			}
			outCode += code.charAt(i);
		}

		return outCode;
	}
}
