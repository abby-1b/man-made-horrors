import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Pava {
	public static String errString = "\033[91m";

	public static void main(String[] args) {
		// Pava.Converter.convert(
		// 	  "class TestClass\n"
		// 	+ "\tpublic static void main(String[] args)\n"
		// 	+ "\t\tSystem.out.println(\"Oh no. This is terrible. Kill me.\")\n"
		// 	+ "\tpublic static void other()\n"
		// 	+ "\t\tSystem.out.println(\"I beg.\")"
		// );

		String code = Pava.getFileContents(Pava.getFile(args));
		String finalCode = Pava.Converter.convert(code);
		// if (Pava.isCompiled(args)) {
			
		// }
	}

	private static boolean isCompiled(String[] args) {
		for (int a = 0; a < args.length; a++) {
			if (args[a] == "-c") return true;
		}
		return false;
	}

	private static String getFile(String[] args) {
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
			System.out.println(Pava.errString + "An error occurred.");
			e.printStackTrace();
		}
		return "";
	}


	static class Converter {
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

				if ("([{".contains(Character.toString(code.charAt(i)))) bracketCount++;
				if ("}])".contains(Character.toString(code.charAt(i)))) bracketCount--;

				// TODO: use bracketcount

				// Count tabs at the start of a line
				if (code.charAt(i) != '\t' && stillTab) {
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

			// Add ending brackets (that weren't closed)
			System.out.println(outCode);
			return outCode;
		}
	}

}
