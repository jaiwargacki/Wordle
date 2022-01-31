import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Object for a bot to play a game of Wordle
 * @author Jai Wargacki
 */
public class Wordle {
    /**
     * Usage message for program
     */
    private static final String USAGE_MSG = "Usage: Wordle [-d <filename>] [-b] [-w <word>] [-h]\n" +
                                            "\t-d\tprovide word list file\n" +
                                            "\t-b\tturn on bot\n" +
                                            "\t-w\tset secret word\n" +
                                            "\t-h\tfor this message";

    /**
     * Main method of Wordle program
     * @param args contains optional flags and parameters for program
     */
    public static void main(String[] args) {
        String filename = "../words.txt";
        boolean botFlag = false;
        String assignedWord = null;

        for(int i = 0; i < args.length; i++) {
            String current = args[i];
            switch(current) {
                case "-d":
                    filename = args[++i];
                    break;
                case "-b":
                    botFlag = true;
                    break;
                case "-w":
                    assignedWord = args[++i];
                    break;
                default:
                    System.out.println(USAGE_MSG);
                    return;
            }
        }

        Set<String> dictionary = new HashSet<>();
        try {
            Scanner scanner = new Scanner(new File(filename));
            while(scanner.hasNextLine()) {
                dictionary.add(scanner.nextLine().strip().toLowerCase());
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        WordleGame game;
        if (assignedWord == null) {
            game = new WordleGame(dictionary);
        } else {
            game = new WordleGame(dictionary, assignedWord);
        }
        
        if (botFlag) {
            WordleBot bot = new WordleBot(dictionary);
                while(!game.gameOver()) {
                    String word;
                    try {
                        word = bot.getNextGuess();
                        System.out.println(game.getTurn() + ": " + word);
                        game.guess(word);
                    } catch (GuessException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                    bot.updateModel(word, game.getResult());
                    System.out.println(game.getResultString());
                }
        } else {
            Scanner scanner = new Scanner(System.in);
            while(!game.gameOver()) {
                try {
                    System.out.print(game.getTurn() + ": ");
                    game.guess(scanner.nextLine().strip());
                } catch (GuessException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                System.out.println(game.getResultString());
            }
            scanner.close();
        } 
        System.out.println(game.finalScore()); 
    }
}
