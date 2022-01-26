import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Object for a bot to play a game of Wordle
 * @author Jai Wargacki
 */
public class WordleBot {
    /**
     * Set of possible words
     */
    private Set<String> dictionary;
    /**
     * Character array of correct letters
     */
    private char[] correct;
    /**
     * String array keeping track of incorrect positions
     */
    private String[] incorrect;
    /**
     * Set of letters in the secret word
     */
    private Set<Character> contains;
    /**
     * Set of letters not in the secret word
     */
    private Set<Character> incorrectOverall;
    /**
     * Collection of words to be considered guessing
     */
    private PriorityQueue<String> guesses;

    /**
     * Constructor for a Wordle Bot
     * @param dictionary set of possible words
     */
    public WordleBot(Set<String> dictionary) {
        this.dictionary = dictionary;
        this.correct = new char[]{'0', '0', '0', '0', '0'};
        this.incorrect = new String[]{"", "", "", "", ""};
        this.contains = new HashSet<>();
        this.incorrectOverall = new HashSet<>();
        populateGuesses();
    }

    /**
     * Populate the guesses fields with words to consider
     */
    private void populateGuesses() {
        this.guesses = new PriorityQueue<>(new WordComparator(dictionary, correct, contains));
        for(String s : dictionary) {
            if (isValid(s)) {
                guesses.add(s);
            }
            
        }
    }

    /**
     * Determines if a word is a valid possible guess
     * @param word the word to consider
     * @return true if valid, false otherwise
     */
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

    /**
     * Update this bot based on the word guessed and the result
     * @param word the word guessed
     * @param result the result of the guessed word
     */
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
}