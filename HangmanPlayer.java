/* AI assistance: ChatGPT helped update validation and documentation*/
public class HangmanPlayer extends Player {
    private int gamesWon;
    private int currentStreak;

    /** Creates a Hangman player.
     * @param name display name
     * @param balance starting balance
     * @param cost entry fee
     */
    public HangmanPlayer(String name, double balance, double cost) { super(name, balance, cost); }
    /** Returns the win total.
     * @return total wins
     */
    public int getGamesWon() { return gamesWon; }
    /** Sets the win total.
     * @param gamesWon wins from zero through games played
     */
    public void setGamesWon(int gamesWon) {
        if (gamesWon < 0 || gamesWon > getGamesPlayed()) throw new IllegalArgumentException("Invalid win count.");
        this.gamesWon = gamesWon;
    }
    /** Returns the current streak.
     * @return consecutive wins
     */
    public int getCurrentStreak() { return currentStreak; }
    /** Sets the current streak.
     * @param currentStreak streak from zero through total wins
     */
    public void setCurrentStreak(int currentStreak) {
        if (currentStreak < 0 || currentStreak > gamesWon) throw new IllegalArgumentException("Invalid streak.");
        this.currentStreak = currentStreak;
    }
    /** Adds a win and extends the streak. */
    public void recordWin() { gamesWon++; currentStreak++; }
    /** Resets the streak after a loss. */
    public void recordLoss() { currentStreak = 0; }
    /** Returns the player subtype.
     * @return player subtype name
     */
    @Override public String getPlayerType() { return "HangmanPlayer"; }
}
