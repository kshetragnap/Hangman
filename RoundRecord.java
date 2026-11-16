/* AI assistance: ChatGPT helped update validation and documentation*/
public class RoundRecord {
    private String word;
    private int incorrectGuesses;
    private int matches;
    private double winnings;
    private boolean won;

    /** Creates a round result.
     * @param word round word
     * @param incorrect wrong guesses
     * @param matches revealed positions
     * @param winnings prize
     * @param won whether the word was solved
     */
    public RoundRecord(String word, int incorrect, int matches, double winnings, boolean won) {
        setWord(word); setIncorrectGuesses(incorrect); setMatches(matches); setWinnings(winnings); this.won = won;
    }
    /** Returns the round word.
     * @return round word
     */
    public String getWord() { return word; }
    /** Sets the round word.
     * @param word non-blank round word
     */
    public void setWord(String word) {
        if (word == null || word.trim().isEmpty()) throw new IllegalArgumentException("Word cannot be blank.");
        this.word = word;
    }
    /** Returns the wrong-guess count.
     * @return incorrect guesses
     */
    public int getIncorrectGuesses() { return incorrectGuesses; }
    /** Sets the wrong-guess count.
     * @param value non-negative guess count
     */
    public void setIncorrectGuesses(int value) {
        if (value < 0) throw new IllegalArgumentException("Count cannot be negative.");
        incorrectGuesses = value;
    }
    /** Returns the match count.
     * @return revealed positions
     */
    public int getMatches() { return matches; }
    /** Sets the match count.
     * @param value non-negative match count
     */
    public void setMatches(int value) {
        if (value < 0) throw new IllegalArgumentException("Count cannot be negative.");
        matches = value;
    }
    /** Returns the round winnings.
     * @return prize earned
     */
    public double getWinnings() { return winnings; }
    /** Sets the round winnings.
     * @param value non-negative prize
     */
    public void setWinnings(double value) {
        if (!Double.isFinite(value) || value < 0) throw new IllegalArgumentException("Winnings must be non-negative.");
        winnings = value;
    }
    /** Returns the round outcome.
     * @return true if the player won
     */
    public boolean isWon() { return won; }
    /** Sets the round outcome.
     * @param won whether the round was won
     */
    public void setWon(boolean won) { this.won = won; }
}
