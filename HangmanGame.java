/* AI assistance: ChatGPT helped resolve conflicts and update this file */
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * Stores settings shared by games.
 */
class Game {
    private int maxIncorrectGuesses;
    private double baseWinnings;

    /** Creates shared game settings.
     * @param maxIncorrectGuesses positive guess limit
     * @param baseWinnings finite, non-negative prize
     */
    public Game(int maxIncorrectGuesses, double baseWinnings) {
        setMaxIncorrectGuesses(maxIncorrectGuesses);
        setBaseWinnings(baseWinnings);
    }
    /** Returns the guess limit.
     * @return maximum incorrect guesses
     */
    public int getMaxIncorrectGuesses() { return maxIncorrectGuesses; }
    /** Sets the guess limit.
     * @param value positive guess limit
     */
    public void setMaxIncorrectGuesses(int value) {
        if (value <= 0) throw new IllegalArgumentException("Guess limit must be positive.");
        maxIncorrectGuesses = value;
    }
    /** Returns the base prize.
     * @return prize before the multiplier
     */
    public double getBaseWinnings() { return baseWinnings; }
    /** Sets the base prize.
     * @param value finite, non-negative base prize
     */
    public void setBaseWinnings(double value) {
        if (!Double.isFinite(value) || value < 0) throw new IllegalArgumentException("Prize must be non-negative.");
        baseWinnings = value;
    }
    /** Returns the game type.
     * @return game type name
     */
    public String getGameType() { return "General Game"; }
}

/**
 * Runs Hangman rounds, tracks completed-round history, and handles save files
 * and statistics reports.
 *
 * @implNote AI assistance was used while developing the guess-processing,
 *           persistence, and statistics logic.
 */
public class HangmanGame extends Game {
    private static final Path SAVE_PATH = Paths.get("hangman_save.properties");
    private static final Path STATS_PATH = Paths.get("hangman_statistics.txt");
    private String secretWord;
    private char[] secretLetters;
    private char[] revealedLetters;
    private char[] guessedLetters;
    private int incorrectGuesses;
    private ArrayList<RoundRecord> history = new ArrayList<>();
    private boolean roundRecorded;

    /** Creates a Hangman game.
     * @param word non-blank hidden word or phrase
     * @param maxGuesses positive incorrect-guess limit
     */
    public HangmanGame(String word, int maxGuesses) {
        super(maxGuesses, 10.0);
        setSecretWord(word);
    }
    /** Returns the hidden word.
     * @return hidden word or phrase
     */
    public String getSecretWord() { return secretWord; }
    /** Sets the hidden word and resets the round.
     * @param word non-blank secret word or phrase
     */
    public void setSecretWord(String word) {
        Objects.requireNonNull(word, "Word cannot be null.");
        if (word.trim().isEmpty()) throw new IllegalArgumentException("Word cannot be blank.");
        secretWord = word;
        secretLetters = word.toCharArray();
        startGame();
    }
    /** Returns a copy of the secret-letter array.
     * @return copied secret letters
     */
    public char[] getSecretLetters() { return secretLetters.clone(); }
    /** Sets the secret letters and resets the round.
     * @param letters replacement secret letters
     */
    public void setSecretLetters(char[] letters) {
        Objects.requireNonNull(letters, "Letters cannot be null.");
        setSecretWord(new String(letters));
    }
    /** Returns a copy of currently revealed positions.
     * @return copied revealed letters
     */
    public char[] getRevealedLetters() { return revealedLetters.clone(); }
    /** Sets revealed positions while preserving word alignment.
     * @param letters array aligned with the secret word
     */
    public void setRevealedLetters(char[] letters) {
        Objects.requireNonNull(letters, "Letters cannot be null.");
        if (letters.length != secretLetters.length) throw new IllegalArgumentException("Array length must match word.");
        for (int i = 0; i < letters.length; i++) {
            if (Character.isLetter(secretLetters[i]) && letters[i] != '\0'
                    && Character.toLowerCase(letters[i]) != Character.toLowerCase(secretLetters[i]))
                throw new IllegalArgumentException("Revealed letters must match their positions.");
            if (!Character.isLetter(secretLetters[i]) && letters[i] != secretLetters[i])
                throw new IllegalArgumentException("Punctuation and spaces must stay revealed.");
        }
        revealedLetters = letters.clone();
    }
    /** Returns a copy of the guesses made this round.
     * @return copied guesses
     */
    public char[] getGuessedLetters() { return guessedLetters.clone(); }
    /** Sets guesses after validating them.
     * @param letters unique alphabetic guesses
     */
    public void setGuessedLetters(char[] letters) {
        Objects.requireNonNull(letters, "Letters cannot be null.");
        char[] copy = letters.clone();
        for (int i = 0; i < copy.length; i++) {
            copy[i] = Character.toLowerCase(copy[i]);
            if (!Character.isLetter(copy[i])) throw new IllegalArgumentException("Guesses must be letters.");
            for (int j = 0; j < i; j++)
                if (copy[i] == copy[j]) throw new IllegalArgumentException("Guesses must be unique.");
        }
        guessedLetters = copy;
    }
    /** Returns the wrong-guess count.
     * @return number of incorrect guesses this round
     */
    public int getIncorrectGuesses() { return incorrectGuesses; }
    /** Sets the wrong-guess count.
     * @param count count from zero through the guess limit
     */
    public void setIncorrectGuesses(int count) {
        if (count < 0 || count > getMaxIncorrectGuesses()) throw new IllegalArgumentException("Invalid guess count.");
        incorrectGuesses = count;
    }
    /** Checks whether the round was recorded.
     * @return true when this round is already in history
     */
    public boolean isRoundRecorded() { return roundRecorded; }
    /** Starts a fresh round using the current word. */
    public void startGame() {
        incorrectGuesses = 0;
        guessedLetters = new char[0];
        revealedLetters = new char[secretLetters.length];
        roundRecorded = false;
        for (int i = 0; i < secretLetters.length; i++)
            if (!Character.isLetter(secretLetters[i])) revealedLetters[i] = secretLetters[i];
    }
    /** Charges the player and starts a round.
     * @param player player paying the entry fee
     * @throws IllegalStateException if funds are insufficient
     */
    public void beginRound(Player player) {
        Objects.requireNonNull(player, "Player cannot be null.");
        if (!player.payForGame()) throw new IllegalStateException("You do not have enough money to play.");
        startGame();
    }
    /** Checks a guess and reveals matching positions.
     * @param guess one letter
     * @return true if the letter occurs
     * @throws IllegalArgumentException for invalid or repeated guesses
     */
    public boolean makeGuess(char guess) {
        if (!Character.isLetter(guess)) throw new IllegalArgumentException("Guess must be a letter.");
        if (isGameOver()) return false;
        char letter = Character.toLowerCase(guess);
        for (char prior : guessedLetters)
            if (prior == letter) throw new IllegalArgumentException("You already guessed that letter.");
        guessedLetters = Arrays.copyOf(guessedLetters, guessedLetters.length + 1);
        guessedLetters[guessedLetters.length - 1] = letter;
        boolean matched = false;
        for (int i = 0; i < secretLetters.length; i++) {
            if (Character.toLowerCase(secretLetters[i]) == letter) {
                revealedLetters[i] = secretLetters[i];
                matched = true;
            }
        }
        if (!matched) incorrectGuesses++;
        return matched;
    }
    /** Counts revealed letter positions.
     * @return count of revealed letter positions
     */
    public int countMatches() {
        int count = 0;
        for (int i = 0; i < secretLetters.length; i++)
            if (Character.isLetter(secretLetters[i]) && revealedLetters[i] != '\0') count++;
        return count;
    }
    /** Checks whether the word is solved.
     * @return true when all letter positions are revealed
     */
    public boolean isWordComplete() {
        for (int i = 0; i < secretLetters.length; i++)
            if (Character.isLetter(secretLetters[i]) && revealedLetters[i] == '\0') return false;
        return true;
    }
    /** Checks whether the round has ended.
     * @return true when solved or the guess limit is reached
     */
    public boolean isGameOver() { return isWordComplete() || incorrectGuesses >= getMaxIncorrectGuesses(); }
    /** Applies the documented multiplier tier for revealed positions.
     * @param matches number of revealed positions
     * @return reward multiplier (1.0, 1.2, 1.5, 2.0, 2.5, or 3.0)
     */
    public double getMultiplier(int matches) {
        if (matches < 0) throw new IllegalArgumentException("Matches cannot be negative.");
        switch (matches) {
            case 0: case 1: return 1.0;
            case 2: return 1.2;
            case 3: return 1.5;
            case 4: return 2.0;
            case 5: return 2.5;
            default: return 3.0;
        }
    }
    /** Calculates the final prize.
     * @return winnings, or zero for a loss
     */
    public double calculateReward() {
        return isWordComplete() ? getBaseWinnings() * getMultiplier(countMatches()) : 0.0;
    }
    /** Saves a completed round and updates player totals.
     * @param player player whose totals are updated
     * @return saved round record
     */
    public RoundRecord recordRound(Player player) {
        Objects.requireNonNull(player, "Player cannot be null.");
        if (!isGameOver() || roundRecorded) throw new IllegalStateException("Round is unfinished or already recorded.");
        boolean won = isWordComplete();
        double winnings = won ? calculateReward() : 0.0;
        if (winnings > 0) player.addWinnings(winnings);
        if (player instanceof HangmanPlayer) {
            HangmanPlayer hp = (HangmanPlayer) player;
            if (won) hp.recordWin(); else hp.recordLoss();
        }
        RoundRecord record = new RoundRecord(secretWord, incorrectGuesses, countMatches(), winnings, won);
        history.add(record);
        roundRecorded = true;
        return record;
    }
    /** Returns the masked word.
     * @return word with underscores for unrevealed positions
     */
    public String getMaskedWord() {
        StringBuilder result = new StringBuilder();
        for (char c : revealedLetters) {
            if (result.length() > 0) result.append(' ');
            result.append(c == '\0' ? '_' : c);
        }
        return result.toString();
    }
    /** Returns a copy of the history.
     * @return completed round records
     */
    public ArrayList<RoundRecord> getHistory() { return new ArrayList<>(history); }
    /** Replaces round history.
     * @param records completed round records
     */
    public void setHistory(ArrayList<RoundRecord> records) {
        Objects.requireNonNull(records, "History cannot be null.");
        if (records.contains(null)) throw new IllegalArgumentException("History cannot contain null records.");
        history = new ArrayList<>(records);
    }
    /** Returns the game type.
     * @return Hangman game type
     */
    @Override public String getGameType() { return "Hangman"; }

    /** Saves the game and player state.
     * @param player player state to save
     * @return true if saving succeeded
     */
    public boolean saveGame(Player player) {
        Properties p = new Properties();
        p.setProperty("word", secretWord);
        p.setProperty("maxGuesses", Integer.toString(getMaxIncorrectGuesses()));
        p.setProperty("baseWinnings", Double.toString(getBaseWinnings()));
        p.setProperty("incorrect", Integer.toString(incorrectGuesses));
        p.setProperty("guessed", new String(guessedLetters));
        p.setProperty("revealed", new String(revealedLetters));
        p.setProperty("roundRecorded", Boolean.toString(roundRecorded));
        p.setProperty("name", player.getName());
        p.setProperty("balance", Double.toString(player.getBalance()));
        p.setProperty("cost", Double.toString(player.getCostPerPlay()));
        p.setProperty("spent", Double.toString(player.getAmountSpent()));
        p.setProperty("winnings", Double.toString(player.getTotalWinnings()));
        p.setProperty("played", Integer.toString(player.getGamesPlayed()));
        if (player instanceof HangmanPlayer) {
            HangmanPlayer hp = (HangmanPlayer) player;
            p.setProperty("won", Integer.toString(hp.getGamesWon()));
            p.setProperty("streak", Integer.toString(hp.getCurrentStreak()));
        }
        p.setProperty("historyCount", Integer.toString(history.size()));
        for (int i = 0; i < history.size(); i++) {
            RoundRecord r = history.get(i);
            String key = "history." + i + ".";
            p.setProperty(key + "word", r.getWord());
            p.setProperty(key + "incorrect", Integer.toString(r.getIncorrectGuesses()));
            p.setProperty(key + "matches", Integer.toString(r.getMatches()));
            p.setProperty(key + "winnings", Double.toString(r.getWinnings()));
            p.setProperty(key + "won", Boolean.toString(r.isWon()));
        }
        try (var writer = Files.newBufferedWriter(SAVE_PATH, StandardCharsets.UTF_8)) {
            p.store(writer, "Hangman saved state");
            System.out.println("Game saved to " + SAVE_PATH.toAbsolutePath());
            return true;
        } catch (IOException | SecurityException ex) {
            System.out.println("Could not save game: " + ex.getMessage());
            return false;
        }
    }
    /** Restores saved game and player state.
     * @param player player object to update
     * @return true if loading succeeded
     */
    public boolean loadGame(Player player) {
        Properties p = new Properties();
        try (var reader = Files.newBufferedReader(SAVE_PATH, StandardCharsets.UTF_8)) {
            p.load(reader);
            String word = p.getProperty("word");
            int max = Integer.parseInt(p.getProperty("maxGuesses"));
            double base = Double.parseDouble(p.getProperty("baseWinnings"));
            int incorrect = Integer.parseInt(p.getProperty("incorrect"));
            char[] revealed = p.getProperty("revealed").toCharArray();
            if (word == null || word.trim().isEmpty() || revealed.length != word.length() || incorrect < 0 || incorrect > max)
                throw new IllegalArgumentException("Save file contains invalid round state.");
            ArrayList<RoundRecord> loadedHistory = new ArrayList<>();
            int count = Integer.parseInt(p.getProperty("historyCount"));
            if (count < 0 || count > 100000) throw new IllegalArgumentException("Invalid history size.");
            for (int i = 0; i < count; i++) {
                String key = "history." + i + ".";
                loadedHistory.add(new RoundRecord(p.getProperty(key + "word"),
                        Integer.parseInt(p.getProperty(key + "incorrect")),
                        Integer.parseInt(p.getProperty(key + "matches")),
                        Double.parseDouble(p.getProperty(key + "winnings")),
                        Boolean.parseBoolean(p.getProperty(key + "won"))));
            }
            setMaxIncorrectGuesses(max);
            setBaseWinnings(base);
            secretWord = word;
            secretLetters = word.toCharArray();
            revealedLetters = revealed;
            guessedLetters = p.getProperty("guessed", "").toCharArray();
            incorrectGuesses = incorrect;
            roundRecorded = Boolean.parseBoolean(p.getProperty("roundRecorded", "false"));
            history = loadedHistory;
            player.setName(p.getProperty("name"));
            player.setBalance(Double.parseDouble(p.getProperty("balance")));
            player.setCostPerPlay(Double.parseDouble(p.getProperty("cost")));
            player.setAmountSpent(Double.parseDouble(p.getProperty("spent")));
            player.setTotalWinnings(Double.parseDouble(p.getProperty("winnings")));
            player.setGamesPlayed(Integer.parseInt(p.getProperty("played")));
            if (player instanceof HangmanPlayer && p.containsKey("won")) {
                HangmanPlayer hp = (HangmanPlayer) player;
                hp.setGamesWon(Integer.parseInt(p.getProperty("won")));
                hp.setCurrentStreak(Integer.parseInt(p.getProperty("streak")));
            }
            System.out.println("Game loaded from " + SAVE_PATH.toAbsolutePath());
            return true;
        } catch (IOException | IllegalArgumentException | SecurityException | NullPointerException ex) {
            System.out.println("Could not load game: " + ex.getMessage());
            return false;
        }
    }
    /** Writes totals, net result, and win rate.
     * @param player player totals to report
     * @return true if the report was written
     */
    public boolean generateStatistics(Player player) {
        int wins = 0;
        for (RoundRecord r : history) if (r.isWon()) wins++;
        double rate = player.getGamesPlayed() == 0 ? 0 : 100.0 * wins / player.getGamesPlayed();
        List<String> lines = Arrays.asList("Hangman Statistics", "Player: " + player.getName(),
                "Games played: " + player.getGamesPlayed(), "Wins: " + wins,
                "Total spent: " + player.getAmountSpent(), "Total won: " + player.getTotalWinnings(),
                "Net profit/loss: " + (player.getTotalWinnings() - player.getAmountSpent()),
                "Win rate (%): " + rate);
        try {
            Files.write(STATS_PATH, lines, StandardCharsets.UTF_8);
            System.out.println("Statistics saved to " + STATS_PATH.toAbsolutePath());
            return true;
        } catch (IOException | SecurityException ex) {
            System.out.println("Could not write statistics: " + ex.getMessage());
            return false;
        }
    }
}
