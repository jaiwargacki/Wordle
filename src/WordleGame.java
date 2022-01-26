import java.util.Set;
import java.util.Stack;

public class WordleGame {
    private String secretWord;
    private Stack<String> guesses;
    private Stack<Status[]> results;
    private Set<String> dictionary;


    public WordleGame(String word, Set<String> dictionary) {
        this.secretWord = word;
        this.guesses = new Stack<String>();
        this.results = new Stack<Status[]>();
        this.dictionary = dictionary;
    }

    public int getTurn() {
        return guesses.size()+1;
    }

    public boolean gameOver() {
        return guesses.size() >= 6 || (guesses.size() > 0 && guesses.peek().equals(secretWord));
    }

    public String getResult() {
        Status[] result = results.peek();
        return results.size() + ":" + result[0] + result[1] + result[2] + result[3] + result[4];
    }

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
