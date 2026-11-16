import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

<<<<<<< HEAD
public class HangmanGame {
=======
public class HangmanGame extends Game {
>>>>>>> 448c1b1 (kshetra code)
    private String secretWord;
    private char[] secretLetters;
    private char[] revealedLetters;
    private char[] guessedLetters;
    private int incorrectGuesses;
<<<<<<< HEAD
    private int maxIncorrectGuesses;
    private double baseWinnings;
    private ArrayList<RoundRecord> history;

    public HangmanGame(String word, int maxGuesses) {
=======
    private ArrayList<RoundRecord> history;

    public HangmanGame(String word, int maxGuesses) {
        super(maxGuesses, 10.0);
>>>>>>> 448c1b1 (kshetra code)
        secretWord = Objects.requireNonNull(word, "word cannot be null");
        if (word.isEmpty()) {
            throw new IllegalArgumentException("word cannot be empty");
        }
        if (maxGuesses < 0) {
            throw new IllegalArgumentException("maxGuesses cannot be negative");
        }

        secretLetters = word.toCharArray();
        revealedLetters = new char[secretLetters.length];
        guessedLetters = new char[0];
<<<<<<< HEAD
        maxIncorrectGuesses = maxGuesses;
=======
>>>>>>> 448c1b1 (kshetra code)
        history = new ArrayList<>();
        startGame();
    }

    public void startGame() {
        incorrectGuesses = 0;
        guessedLetters = new char[0];
        revealedLetters = new char[secretLetters.length];

        for (int i = 0; i < secretLetters.length; i++) {
            if (!Character.isLetter(secretLetters[i])) {
                revealedLetters[i] = secretLetters[i];
            }
        }
    }

    public boolean makeGuess(char guess) {
        if (isGameOver() || !Character.isLetter(guess)) {
            return false;
        }

        char normalizedGuess = Character.toLowerCase(guess);
        for (char guessedLetter : guessedLetters) {
            if (Character.toLowerCase(guessedLetter) == normalizedGuess) {
                return containsSecretLetter(normalizedGuess);
            }
        }

        guessedLetters = Arrays.copyOf(guessedLetters, guessedLetters.length + 1);
        guessedLetters[guessedLetters.length - 1] = guess;

        boolean matched = false;
        for (int i = 0; i < secretLetters.length; i++) {
            if (Character.toLowerCase(secretLetters[i]) == normalizedGuess) {
                revealedLetters[i] = secretLetters[i];
                matched = true;
            }
        }

        if (!matched) {
            incorrectGuesses++;
        }
        return matched;
    }

    public int countMatches() {
        int matches = 0;
        for (int i = 0; i < secretLetters.length; i++) {
            if (Character.isLetter(secretLetters[i]) && revealedLetters[i] != '\0') {
                matches++;
            }
        }
        return matches;
    }

    public boolean isWordComplete() {
        for (int i = 0; i < secretLetters.length; i++) {
            if (Character.isLetter(secretLetters[i]) && revealedLetters[i] == '\0') {
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
<<<<<<< HEAD
        return isWordComplete() || incorrectGuesses >= maxIncorrectGuesses;
=======
        return isWordComplete() || incorrectGuesses >= maxIncorrectGuesses();
>>>>>>> 448c1b1 (kshetra code)
    }

    public double getMultiplier(int matches) {
        throw new UnsupportedOperationException("The reward multiplier has not been specified.");
    }

    public double calculateReward() {
        throw new UnsupportedOperationException("The reward calculation has not been specified.");
    }

    public void saveGame(Player player) {
        throw new UnsupportedOperationException("Game persistence has not been implemented.");
    }

    public void loadGame(Player player) {
        throw new UnsupportedOperationException("Game persistence has not been implemented.");
    }

    public void generateStatistics() {
        throw new UnsupportedOperationException("Statistics generation has not been implemented.");
    }

    public ArrayList<RoundRecord> getHistory() {
        return history;
    }

    private boolean containsSecretLetter(char letter) {
        for (char secretLetter : secretLetters) {
            if (Character.toLowerCase(secretLetter) == letter) {
                return true;
            }
        }
        return false;
    }
<<<<<<< HEAD
=======
    @Override
    public String getGameType() {
        return "Hangman";
    }
>>>>>>> 448c1b1 (kshetra code)
}
