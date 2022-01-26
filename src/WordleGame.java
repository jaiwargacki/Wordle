import java.util.Random;
import java.util.Set;
import java.util.Stack;

/**
 * Object for playing a game of Wordle
 * @author Jai Wargacki
 */
public class WordleGame {
    /**
     * The secret word to be guessed
     */
    private String secretWord;
    /**
     * Collection of guesses
     */
    private Stack<String> guesses;
    /**
     * Collection of status results for the made guesses
     */
    private Stack<Status[]> results;
    /**
     * Set of possible words
     */
    private Set<String> dictionary;

    /**
     * Constructor for a game of Wordle
     * @param dictionary set of possible words
     * @param word the secret word to be guessed
     */
    public WordleGame(Set<String> dictionary, String word) {
        this.secretWord = word;
        this.guesses = new Stack<String>();
        this.results = new Stack<Status[]>();
        this.dictionary = dictionary;
    }

    /**
     * Constructor for a game of Wordle
     * The secret word will be set randomly
     * @param dictionary set of possible words
     */
    public WordleGame(Set<String> dictionary) {
        int item = new Random().nextInt(dictionary.size());
        int i = 0;
        for(String s : dictionary)
        {
            if (i++ == item)
                this.secretWord = s;
        }
        this.guesses = new Stack<String>();
        this.results = new Stack<Status[]>();
        this.dictionary = dictionary;
    }

    /**
     * Gets a string displaying the final score of the game
     * @return a string displaying the final score of the game
     */
    public String finalScore() {
        String top = "\nWordle ";
        String rows = "";
        if (guesses.peek().equals(secretWord) || guesses.size() < 6) {
            top += guesses.size() + "/6\n";
        } else {
            top += "X/6\n";
        }
        while(!results.empty()) {
            Status[] result = results.pop();
            rows = "" + result[0] + result[1] + result[2] + result[3] + result[4] + "\n" + rows;
        }

        return top + "\n" + rows;
    }

    /**
     * gets the number of the turn (1-6)
     * @return the number of the turn
     */
    public int getTurn() {
        return guesses.size()+1;
    }

    /**
     * Determine if the game is over
     * @return true if game is over, false otherwise
     */
    public boolean gameOver() {
        return guesses.size() >= 6 || (guesses.size() > 0 && guesses.peek().equals(secretWord));
    }

    /**
     * Gets the results of the last guess as a Status array
     * @return the results of the last guess as a Status array
     */
    public Status[] getResult() {
        return results.peek();
    }

    /**
     * Gets the result of the last guess as a printable string
     * @return the result of the last guess as a printable string
     */
    public String getResultString() {
        Status[] result = results.peek();
        return results.size() + ":" + result[0] + result[1] + result[2] + result[3] + result[4];
    }

    /**
     * Guess a word in the game
     * @param guess the string guess
     * @throws GuessException thrown if guess is invalid for whatever reason
     */
    public void guess(String guess) throws GuessException {
        if (guesses.size() == 6) {
            throw new GuessException("No more valid guesses.");
        } else if (!dictionary.contains(guess)) {
            throw new GuessException("Guess must be a valid 5 letter word.");
        } else if (guesses.contains(guess)) {
            throw new GuessException("That word was already guessed.");
        }
        guesses.add(guess);
        Status[] result = new Status[5];
        for(int i = 0; i < 5; i++) {
            char guessChar = guess.charAt(i);
            if(secretWord.charAt(i) == guessChar) {
                result[i] = Status.CORRECT;
            } else if (secretWord.contains(Character.toString(guessChar))) {
                result[i] = Status.PARTIAL;
            } else {
                result[i] = Status.INCORRECT;
            }
        }
        results.add(result);
    }
}
