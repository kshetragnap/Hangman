public class RoundRecord {
    private String word;
    private int incorrectGuesses;
    private int matches;
    private double winnings;
    private boolean won;

    public RoundRecord(String word, int incorrect, int matches, double winnings, boolean won) {
        this.word = word;
        incorrectGuesses = incorrect;
        this.matches = matches;
        this.winnings = winnings;
        this.won = won;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    public void setIncorrectGuesses(int incorrect) {
        incorrectGuesses = incorrect;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public double getWinnings() {
        return winnings;
    }

    public void setWinnings(double winnings) {
        this.winnings = winnings;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }
}
