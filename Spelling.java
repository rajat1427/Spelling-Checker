import java.io.*;
import java.util.*;
import java.util.regex.*;

class Spelling {

	private final HashMap<String, Integer> nWords = new HashMap<String, Integer>();

	// This constructor takes input from the file, and stores the words(tokens)
	// into HashMap i.e nWords
	public Spelling(String file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		Pattern p = Pattern.compile("\\w+");
		for (String temp = ""; temp != null; temp = in.readLine()) {
			Matcher m = p.matcher(temp.toLowerCase());
			while (m.find())
				nWords.put((temp = m.group()), nWords.containsKey(temp) ? nWords.get(temp) + 1 : 1);
		}
		in.close();
	}

	//This function returns a list of all the edited strings (whether words or not) that can be made with one simple edit
	private final ArrayList<String> edits(String word) {
		ArrayList<String> result = new ArrayList<String>();
		
		//deletion (remove one letter)
		for (int i = 0; i < word.length(); ++i) {
			result.add(word.substring(0, i) + word.substring(i + 1));
		}
		
		//transposition (swap two adjacent letters)
		for (int i = 0; i < word.length() - 1; ++i) {
			result.add(word.substring(0, i) + word.substring(i + 1, i + 2) + word.substring(i, i + 1)
					+ word.substring(i + 2));
		}
		
		//replacement (change one letter to another)
		for (int i = 0; i < word.length(); ++i) {
			for (char c = 'a'; c <= 'z'; ++c) {
				result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i + 1));
			}
		}
		
		//insertion (add a letter)
		for (int i = 0; i <= word.length(); ++i) {
			for (char c = 'a'; c <= 'z'; ++c) {
				result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
			}
		}
		return result;
	}
   
	//This method will give the most probable spelling correction for the given word
	public final String correct(String word) {
		if (nWords.containsKey(word))
			return word;
		ArrayList<String> list = edits(word);
		HashMap<Integer, String> candidates = new HashMap<Integer, String>();
		for (String s : list)
			if (nWords.containsKey(s))
				candidates.put(nWords.get(s), s);
		if (candidates.size() > 0)
			return candidates.get(Collections.max(candidates.keySet()));
		for (String s : list)
			for (String w : edits(s))
				if (nWords.containsKey(w))
					candidates.put(nWords.get(w), w);
		return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
	}

	public static void main(String args[]) throws IOException {
		if (args.length > 0)
			System.out.println((new Spelling("big.txt")).correct(args[0]));
	}

}
