import java.util.*;
import java.io.*;

public class Arpabet {
    private HashMap<String, String> vowels;
    private HashMap<String, String> consonants;
    private ArrayList<String> phonemes;

    /* Compile the scores for phoneme similarity */
    
    private void loadData () {
        try {
            File f = new File("arpabet.txt");
            BufferedReader br = new BufferedReader(new FileReader(f));
            vowels = new HashMap<String, String>();
            consonants = new HashMap<String, String>();
            phonemes = new ArrayList<String>();
            String[] data = new String[3];
            for (String line; (line = br.readLine()) != null; ) {
                if (line.startsWith(";")) {
                    continue;
                }
                data = line.split(",");
                phonemes.add(data[0]);
                if (data[1].compareTo("v") == 0) {
                    vowels.put(data[0], data[2]);
                } else {
                    consonants.put(data[0], data[2]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void compileScores() {
        this.loadData();
    }

    public double compareSounds(String phoneme1, String phoneme2) {
        Boolean p1_vowel = vowels.containsKey(phoneme1);
        Boolean p1_consonant = consonants.containsKey(phoneme1);
        Boolean p2_vowel = vowels.containsKey(phoneme2);
        Boolean p2_consonant = consonants.containsKey(phoneme2);
        if (!(p1_vowel || p1_consonant) || !(p2_consonant || p2_vowel)) {
            return -1; // not a valid phoneme
        } 
        if ((p1_vowel ^ p2_vowel) && (p1_consonant ^ p2_consonant)) {
            return 0; // comparing a consonant with vowel
        }
        HashMap<String, String> dictionary = (p1_vowel && p2_vowel) ? vowels : consonants;
        String data1 = dictionary.get(phoneme1);
        String data2 = dictionary.get(phoneme2);
        return computeScore(data1, data2);
    }

    private double computeScore(String data1, String data2) {
        int length = data1.length();
        int comparison = 0;
        for (int i = 0; i < length; i++) {
            Character data1_byte = (Character) data1.charAt(i);
            if (data1_byte.compareTo(data2.charAt(i)) == 0) {
                comparison++;
            }
        }
        return ((double) comparison) / length;
    }

    /* Compare the words in the dictionary. Writes result to a txt file*/
    public void checkDictionary (String[] target) {
        try {
            File f = new File("cmudict.0.7a");
            BufferedReader br = new BufferedReader(new FileReader(f));
            String[][] data;
            Double result;
            for (String line; (line = br.readLine()) != null; ) {
                if (!line.matches("^[A-Za-z].*")) {
                    continue;
                }
                data = readDictionary(line);
                result = compareWords(target, data[1]);
                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("results.txt", true)));
                    out.print(result);
                    out.println(" " + data[0][0]); // prints the word
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double compareWords (String[] target, String[] word) {
        if (target.length > word.length) {
            return 0;
        }
        double max = 0;
        String[] sub;
        double result;
        for (int i=0; i <= (word.length - target.length); i++) {
            sub = Arrays.copyOfRange(word, i, i+target.length);
            result = compareSubstrings(sub, target);
            max = result > max ? result : max;
        }
        return max;
    }

    private double compareSubstrings(String[] sub1, String[] sub2) {
        double result = 0.0;
        for (int i=0; i < sub1.length; i++) {
            result += compareSounds(sub1[i], sub2[i]);
        }
        return result;
    }

    private String[][] readDictionary(String line) {
        String[] line_parts = line.split("  ");
        String[] word = {line_parts[0]};
        String[] phonemes = line_parts[1].split(" ");
        for (int i = 0; i < phonemes.length; i++) {
            String phoneme = phonemes[i];
            if (phoneme.endsWith("0") || phoneme.endsWith("1") || phoneme.endsWith("2")) {
                phonemes[i] = phoneme.substring(0, phoneme.length() - 1);
            }
        }
        String[][] result = {word, phonemes};
        return result;
    }

    public static void main(String[] args) {
        Arpabet a = new Arpabet();
        a.compileScores();
        a.checkDictionary(args);
    }
}
