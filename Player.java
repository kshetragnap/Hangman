public class Player {
    private String name;
    private double balance;
    private double costPerPlay;
    private double amountSpent;
    private double totalWinnings;
    private int gamesPlayed;

    public Player(String name, double balance, double costPerPlay) {
        this.name = name;
        this.balance = balance;
        this.costPerPlay = costPerPlay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getCostPerPlay() {
        return costPerPlay;
    }

    public void setCostPerPlay(double cost) {
        costPerPlay = cost;
    }

    public double getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(double amount) {
        amountSpent = amount;
    }

    public double getTotalWinnings() {
        return totalWinnings;
    }

    public void setTotalWinnings(double winnings) {
        totalWinnings = winnings;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int games) {
        gamesPlayed = games;
    }

    public boolean payForGame() {
        if (balance < costPerPlay) {
            return false;
        }

        balance -= costPerPlay;
        amountSpent += costPerPlay;
        gamesPlayed++;
        return true;
    }

    public void addWinnings(double amount) {
        balance += amount;
        totalWinnings += amount;
    }

    public String getPlayerType() {
        return "Player";
    }
}
