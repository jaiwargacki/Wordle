import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Comparator for ranking possible guesses
 * @author Jai Wargacki
 */
public class WordComparator implements Comparator<String> {
    /**
     * set of possible words
     */
    private Map<Character, Integer[]> scoring;

    /**
     * Constructor for this WordComparator
     * @param dictionary set of possible words
     */
    public WordComparator(Set<String> dictionary) {
        scoring = new HashMap<>();
        for(String s : dictionary) {
            for(int i = 0; i < 5; i++) {
                char c = s.charAt(i);
                if (!scoring.containsKey(c)) {
                    scoring.put(c, new Integer[]{0, 0, 0, 0, 0});
                }
                scoring.get(c)[i] += 1;
            }
        }
    }

    /**
     * Override of compare method to properly order two given words
     */
    @Override
    public int compare(String word1, String word2) {
        int word1Score = 0;
        int word2Score = 0;
        Set<Character> word1Unique = new HashSet<>();
        Set<Character> word2Unique = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            char c1 = word1.charAt(i);
            if (!word1Unique.contains(c1)) {
                word1Score += scoring.get(word1.charAt(i))[i];
                word1Unique.add(c1);
            }
            
            char c2 = word2.charAt(i);
            if (!word2Unique.contains(c2)) {
                word2Score += scoring.get(word2.charAt(i))[i];
                word2Unique.add(c2);
            }

        }
        return word2Score - word1Score;
    }
}
