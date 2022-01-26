import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class WordleGame {
    private String secretWord;
    private Stack<String> guesses;
    private Stack<Status[]> results;
    private Set<String> dictionary;

    public WordleGame(Set<String> dictionary, String word) {
        this.secretWord = word;
        this.guesses = new Stack<String>();
        this.results = new Stack<Status[]>();
        this.dictionary = dictionary;
    }

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

    public int getTurn() {
        return guesses.size()+1;
    }

    public boolean gameOver() {
        return guesses.size() >= 6 || (guesses.size() > 0 && guesses.peek().equals(secretWord));
    }

    public Status[] getResult() {
        return results.peek();
    }

    public String getResultString() {
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
