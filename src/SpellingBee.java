// Spelling Bee by Oliver Faris
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // Helper function to makeWords()
    public void generate() {
        makeWords("", letters);
    }

    // Recursively generates every possible mutation of a string
    public void makeWords(String newStr, String letters) {
        if(letters.isEmpty()) {
            return;
        }
        for (int i = 0; i < letters.length(); i++) {
            words.add(newStr + letters.charAt(i));
            makeWords(newStr +letters.charAt(i), letters.substring(0, i) + letters.substring(i+1));
        }
    }

    // Helper function to mergeSort()
    public void sort() {
        words = mergeSort(0, words.size() -1);
    }

    // Implements merge sort to sort all the words
    public ArrayList<String> mergeSort(int low, int high) {
        if (high - low == 0) {
            ArrayList<String> newArr = new ArrayList<>();
            newArr.add(words.get(low));
            return newArr;
        }
        int med = (high +low) /2;
        ArrayList<String> arr1 = mergeSort(low, med);
        ArrayList<String> arr2 = mergeSort(med +1, high);
        return merge(arr1, arr2);
    }

    // Actually merges the sub-arrays after splitting them up
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> sol = new ArrayList<>();
        int index1 = 0;
        int index2 = 0;

        while (index1 < arr1.size() && index2 < arr2.size()) {
            if(arr1.get(index1).compareTo(arr2.get(index2)) < 0) {
                sol.add(arr1.get(index1++));
            } else {
                sol.add(arr2.get(index2++));
            }
        }

        // Copy over any remaining elements
        while (index1 < arr1.size()) {
            sol.add(arr1.get(index1++));
        }

        while (index2 < arr2.size()) {
            sol.add(arr2.get(index2++));
        }

        return sol;
    }


    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // Removes words that aren't in the dictionary
    public void checkWords() {
        int size = words.size();
        for (int i = 0; i < size; i++) {
            if (!found(words.get(i), 0, DICTIONARY_SIZE-1)) {
                words.remove(i);
                size--;
                i--;
            }
        }
    }

    // Binary search function to find if a word is part of the dictionary
    public boolean found(String str, int low, int high) {
        if (low > high) {
            return false;
        }
        int med = (high + low) /2;
        int compareVal = str.compareTo(DICTIONARY[med]);
        if(compareVal > 0)
            return found(str, med +1, high);
        else if (compareVal < 0)
            return found(str, low, med -1);
        return true;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
