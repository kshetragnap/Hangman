public class HangmanPlayer extends Player {
    private int gamesWon;
    private int currentStreak;

    public HangmanPlayer(String name, double balance, double cost) {
        super(name, balance, cost);
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int wins) {
        gamesWon = wins;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int streak) {
        currentStreak = streak;
    }

    public void recordWin() {
        gamesWon++;
        currentStreak++;
    }

    public void recordLoss() {
        currentStreak = 0;
    }

    @Override
    public String getPlayerType() {
        return "HangmanPlayer";
    }
}
