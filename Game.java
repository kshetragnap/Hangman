/*
parent class for the game which stores information for the games

@author Student
*/

public class Game {
    private int maxIncorrectGuesses;
    private double baseWinnings; 
    /*
    created the game object
    @param maxIncorrectGuesses maximum incorrect guesses allowed
    @param baseWinnings shws the starting amount of winnings
    */
   public Game(int maxIncorrectGuesses, double baseWinnings) {
    this.maxIncorrectGuesses = maxIncorrectGuesses;
    this.baseWinnings = baseWinnings;
   }

    /*
    sthis sets the max incorrect guesses
    @para, maxIncorrectGuesses sets the mew max incorrect guesses
    */
   public void setMaxIncorrectGuesses(int maxIncorrectGuesses) {
    this.maxIncorrectGuesses = maxIncorrectGuesses;
   }
   /*
   gets the base winnings
   @return base winnings
   */
  public void setBaseWinnings(double baseWinnings) {
    this.baseWinnings = baseWinnings;
  }
  //gets the base winnings
  public double getBaseWinnings() {
    return baseWinnings;
  }
  //gets the max incorrect guesses
  public int getMaxIncorrectGuesses() {
    return maxIncorrectGuesses;
  }
  //returns the type of game
  public String getGameType() {
    return "General Game";
  }
  
    
}