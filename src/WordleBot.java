import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class WordleBot {
    private Set<String> dictionary;
    private char[] correct;
    private String[] incorrect;
    private Set<Character> contains;
    private Set<Character> incorrectOverall;
    private PriorityQueue<String> guesses;

    public WordleBot(Set<String> dictionary) {
        this.dictionary = dictionary;
        this.correct = new char[]{'0', '0', '0', '0', '0'};
        this.incorrect = new String[]{"", "", "", "", ""};
        this.contains = new HashSet<>();
        this.incorrectOverall = new HashSet<>();
        populateGuesses();
    }

    private void populateGuesses() {
        this.guesses = new PriorityQueue<>(new WordComparator(dictionary, correct, contains));
        for(String s : dictionary) {
            if (isValid(s)) {
                guesses.add(s);
            }
            
        }
    }

    private boolean isValid(String word) {
        for(int i = 0; i < 5; i++) {
            char c = word.charAt(i);
            if ((correct[i] != '0' && correct[i] != c) || incorrect[i].contains(Character.toString(c)) || incorrectOverall.contains(c)) {
                return false;
            }
        }
        for(char c : contains) {
            if (!word.contains(Character.toString(c))) {
                return false;
            }
        }
        return true;
    }

    public void updateModel(String word, Status[] result) {
        for(int i = 0; i < 5; i++) {
            char c = word.charAt(i);
            switch (result[i]) {
                case CORRECT:
                    correct[i] = c;
                    break;
                case PARTIAL:
                    contains.add(c);
                    incorrect[i] = incorrect[i] + c;
                    break;
                case INCORRECT:
                    incorrectOverall.add(c);
                    break;
            }
        }
        populateGuesses();
    }

    public String getNextGuess() {
        while(true) {
            String word = guesses.poll();
            if (isValid(word)) {
                return word;
            }
        }
    }

    public static void main(String[] args) {
        Set<String> dictionary = new HashSet<>();
        try {
            Scanner scanner = new Scanner(new File(args[0]));
            while(scanner.hasNextLine()) {
                dictionary.add(scanner.nextLine().strip().toLowerCase());
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        WordleBot bot = new WordleBot(dictionary);
    }
}