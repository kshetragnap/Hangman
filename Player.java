/* AI assistance: ChatGPT helped update validation and documentation; students should review and document edits. */
/** Player wallet and cumulative totals. */
public class Player {
    private String name;
    private double balance;
    private double costPerPlay;
    private double amountSpent;
    private double totalWinnings;
    private int gamesPlayed;

    /** Creates a player.
     * @param name display name
     * @param balance starting balance
     * @param costPerPlay entry fee
     */
    public Player(String name, double balance, double costPerPlay) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be blank.");
        this.name = name.trim();
        this.balance = nonNegative(balance, "Balance");
        this.costPerPlay = nonNegative(costPerPlay, "Cost");
    }
    /** Returns the player's name.
     * @return player name
     */
    public String getName() { return name; }
    /** Sets the player's name.
     * @param name non-blank display name
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be blank.");
        this.name = name.trim();
    }
    /** Returns the wallet balance.
     * @return wallet balance
     */
    public double getBalance() { return balance; }
    /** Sets the wallet balance.
     * @param balance non-negative wallet balance
     */
    public void setBalance(double balance) { this.balance = nonNegative(balance, "Balance"); }
    /** Returns the entry fee.
     * @return cost per game
     */
    public double getCostPerPlay() { return costPerPlay; }
    /** Sets the entry fee.
     * @param costPerPlay non-negative entry fee
     */
    public void setCostPerPlay(double costPerPlay) { this.costPerPlay = nonNegative(costPerPlay, "Cost"); }
    /** Returns cumulative spending.
     * @return cumulative amount spent
     */
    public double getAmountSpent() { return amountSpent; }
    /** Sets cumulative spending.
     * @param amountSpent non-negative total
     */
    public void setAmountSpent(double amountSpent) { this.amountSpent = nonNegative(amountSpent, "Spent"); }
    /** Returns cumulative winnings.
     * @return total winnings
     */
    public double getTotalWinnings() { return totalWinnings; }
    /** Sets cumulative winnings.
     * @param totalWinnings non-negative total
     */
    public void setTotalWinnings(double totalWinnings) { this.totalWinnings = nonNegative(totalWinnings, "Winnings"); }
    /** Returns the number of rounds started.
     * @return games played
     */
    public int getGamesPlayed() { return gamesPlayed; }
    /** Sets the number of rounds played.
     * @param gamesPlayed non-negative round count
     */
    public void setGamesPlayed(int gamesPlayed) {
        if (gamesPlayed < 0) throw new IllegalArgumentException("Games played cannot be negative.");
        this.gamesPlayed = gamesPlayed;
    }
    /** Attempts to pay the entry fee.
     * @return true if entry fee was paid
     */
    public boolean payForGame() {
        if (balance < costPerPlay) return false;
        balance -= costPerPlay; amountSpent += costPerPlay; gamesPlayed++;
        return true;
    }
    /** Credits a prize to the wallet and totals.
     * @param amount non-negative prize
     */
    public void addWinnings(double amount) {
        amount = nonNegative(amount, "Winnings"); balance += amount; totalWinnings += amount;
    }
    /** Returns the player subtype.
     * @return player subtype name
     */
    public String getPlayerType() { return "Player"; }
    private static double nonNegative(double value, String label) {
        if (!Double.isFinite(value) || value < 0) throw new IllegalArgumentException(label + " must be non-negative.");
        return value;
    }
}
