import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordComparator implements Comparator<String> {
    private Map<Character, Integer[]> scoring;
    private char[] correct;
    private Set<Character> contains;

    public WordComparator(Set<String> dictionary, char[] correct, Set<Character> contains) {
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
        this.correct = correct;
        this.contains = contains;
    }

    @Override
    public int compare(String word1, String word2) {
        int word1Score = 0;
        int word2Score = 0;
        int word1Correct = 0;
        int word2Correct = 0;
        int word1Contains = 0;
        int word2Contains = 0;
        Set<Character> word1Unique = new HashSet<>();
        Set<Character> word2Unique = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            char c1 = word1.charAt(i);
            if (!word1Unique.contains(c1)) {
                word1Score += scoring.get(word1.charAt(i))[i];
                word1Unique.add(c1);
            }
            if (correct[i] == c1) {
                word1Correct += 1;
            }
            if (contains.contains(c1)) {
                word1Contains += 1;
            }
            
            char c2 = word2.charAt(i);
            if (!word2Unique.contains(c2)) {
                word2Score += scoring.get(word2.charAt(i))[i];
                word2Unique.add(c2);
            }
            if (correct[i] == c2) {
                word2Correct += 1;
            }
            if (contains.contains(c2)) {
                word2Contains += 1;
            }

        }
        if (word2Correct - word1Correct != 0) {
            return word2Correct - word1Correct;
        } else if (word2Contains - word1Contains != 0) {
            return word2Contains - word1Contains;
        } else {
            return word2Score - word1Score;
        }
    }
}
