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
    private boolean roundRecorded;

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
        roundRecorded = false;

        for (int i = 0; i < secretLetters.length; i++) {
            if (!Character.isLetter(secretLetters[i])) {
                revealedLetters[i] = secretLetters[i];
            }
        }
    }

    public boolean makeGuess(char guess) {
        if (!Character.isLetter(guess)) {
            throw new IllegalArgumentException("Guess must be a letter.");
        }
        if (isGameOver()) {
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
        if (matches < 0) {
            throw new IllegalArgumentException("matches cannot be negative");
        }
        return 1.0;
    }

    public double calculateReward() {
        return isWordComplete() ? baseWinnings * getMultiplier(countMatches()) : 0.0;
    }

    /** Charges the entry fee and starts a fresh round. */
    public void beginRound(Player player) {
        Objects.requireNonNull(player, "player cannot be null");
        if (!player.payForGame()) {
            throw new IllegalStateException("The player does not have enough money to play.");
        }
        startGame();
    }

    /** Records a completed round once and credits any prize to the player. */
    public RoundRecord recordRound(Player player) {
        Objects.requireNonNull(player, "player cannot be null");
        if (!isGameOver()) {
            throw new IllegalStateException("The round is not over yet.");
        }
        if (roundRecorded) {
            throw new IllegalStateException("This round has already been recorded.");
        }

        boolean won = isWordComplete();
        double winnings = won ? calculateReward() : 0.0;
        if (winnings > 0) {
            player.addWinnings(winnings);
        }
        if (player instanceof HangmanPlayer) {
            HangmanPlayer hangmanPlayer = (HangmanPlayer) player;
            if (won) {
                hangmanPlayer.recordWin();
            } else {
                hangmanPlayer.recordLoss();
            }
        }

        RoundRecord record = new RoundRecord(secretWord, incorrectGuesses, countMatches(), winnings, won);
        history.add(record);
        roundRecorded = true;
        return record;
    }

    public String getMaskedWord() {
        StringBuilder masked = new StringBuilder();
        for (char letter : revealedLetters) {
            masked.append(letter == '\0' ? '_' : letter).append(' ');
        }
        return masked.toString().trim();
    }

    public int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    public int getMaxIncorrectGuesses() {
        return maxIncorrectGuesses;
    }

    public double getBaseWinnings() {
        return baseWinnings;
    }

    public void setBaseWinnings(double baseWinnings) {
        if (baseWinnings < 0) {
            throw new IllegalArgumentException("baseWinnings cannot be negative");
        }
        this.baseWinnings = baseWinnings;
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
        return new ArrayList<>(history);
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
