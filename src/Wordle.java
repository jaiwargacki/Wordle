import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Wordle {
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
        WordleGame game = new WordleGame("hello", dictionary);
        Scanner scanner = new Scanner(System.in);
        while(!game.gameOver()) {
            try {
                System.out.print(game.getTurn() + ": ");
                game.guess(scanner.nextLine().strip());
            } catch (GuessException e) {
                System.out.println(e.getMessage());
                continue;
            }
            System.out.println(game.getResult());
        }
        scanner.close();
    }
}
