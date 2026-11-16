# Hangman – Practical Exam 2

AI assistance: ChatGPT helped resolve merge conflicts, update code, and prepare these notes after instructor approval. Students should review the changes, add their own edit notes, and record each partner's actual contribution.

## Algorithm

1. Ask for player and game settings. Re-prompt for invalid numbers or blank text.
2. Keep the secret word and revealed characters in arrays of equal length. Show punctuation and spaces immediately.
3. Before each round, check the wallet and deduct the entry fee only if it is affordable.
4. Read one letter. Reject invalid or repeated guesses. Compare the guess to each secret array position, reveal all matching positions, or count a wrong guess.
5. End when all letters are revealed or the wrong-guess limit is reached. Count revealed letter positions and apply the matching prize multiplier.
6. Add a RoundRecord, credit winnings, update player totals, and show the result.
7. The menu displays stats/history, saves or reloads state, and writes a statistics report. File errors are shown without terminating the menu.

Multiplier tiers: 0-1 matches = 1.0; 2 = 1.2; 3 = 1.5; 4 = 2.0; 5 = 2.5; 6+ = 3.0. Matches are revealed character positions, so repeated letters count once for each position.

## UML

```mermaid
classDiagram
  class Game {
    -int maxIncorrectGuesses
    -double baseWinnings
    +Game(int, double)
    +getMaxIncorrectGuesses() int
    +setMaxIncorrectGuesses(int) void
    +getBaseWinnings() double
    +setBaseWinnings(double) void
    +getGameType() String
  }
  class HangmanGame {
    -Path SAVE_PATH
    -Path STATS_PATH
    -String secretWord
    -char[] secretLetters
    -char[] revealedLetters
    -char[] guessedLetters
    -int incorrectGuesses
    -ArrayList~RoundRecord~ history
    -boolean roundRecorded
    +HangmanGame(String, int)
    +getSecretWord() String
    +setSecretWord(String) void
    +getSecretLetters() char[]
    +setSecretLetters(char[]) void
    +getRevealedLetters() char[]
    +setRevealedLetters(char[]) void
    +getGuessedLetters() char[]
    +setGuessedLetters(char[]) void
    +getIncorrectGuesses() int
    +setIncorrectGuesses(int) void
    +isRoundRecorded() boolean
    +startGame() void
    +beginRound(Player) void
    +makeGuess(char) boolean
    +countMatches() int
    +isWordComplete() boolean
    +isGameOver() boolean
    +getMultiplier(int) double
    +calculateReward() double
    +recordRound(Player) RoundRecord
    +getMaskedWord() String
    +getHistory() ArrayList
    +setHistory(ArrayList) void
    +getGameType() String
    +saveGame(Player) boolean
    +loadGame(Player) boolean
    +generateStatistics(Player) boolean
  }
  class Player {
    -String name
    -double balance
    -double costPerPlay
    -double amountSpent
    -double totalWinnings
    -int gamesPlayed
    +Player(String, double, double)
    +getName() String
    +setName(String) void
    +getBalance() double
    +setBalance(double) void
    +getCostPerPlay() double
    +setCostPerPlay(double) void
    +getAmountSpent() double
    +setAmountSpent(double) void
    +getTotalWinnings() double
    +setTotalWinnings(double) void
    +getGamesPlayed() int
    +setGamesPlayed(int) void
    +payForGame() boolean
    +addWinnings(double) void
    +getPlayerType() String
    -nonNegative(double, String) double
  }
  class HangmanPlayer {
    -int gamesWon
    -int currentStreak
    +HangmanPlayer(String, double, double)
    +getGamesWon() int
    +setGamesWon(int) void
    +getCurrentStreak() int
    +setCurrentStreak(int) void
    +recordWin() void
    +recordLoss() void
    +getPlayerType() String
  }
  class RoundRecord {
    -String word
    -int incorrectGuesses
    -int matches
    -double winnings
    -boolean won
    +RoundRecord(String, int, int, double, boolean)
    +getWord() String
    +setWord(String) void
    +getIncorrectGuesses() int
    +setIncorrectGuesses(int) void
    +getMatches() int
    +setMatches(int) void
    +getWinnings() double
    +setWinnings(double) void
    +isWon() boolean
    +setWon(boolean) void
  }
  class Main { +main(String[]) void }
  class HangmanConsole {
    -HangmanConsole()
    +run() void
    -runMenu(Scanner, HangmanPlayer, HangmanGame) void
    -playRound(Scanner, HangmanPlayer, HangmanGame) void
    -showStats(HangmanPlayer) void
    -showHistory(ArrayList) void
    -readGuess(Scanner) char
    -readText(Scanner, String) String
    -readNonNegativeDouble(Scanner, String) double
    -readPositiveInt(Scanner, String) int
  }
  Game <|-- HangmanGame
  Player <|-- HangmanPlayer
  HangmanGame "1" o-- "*" RoundRecord
```

## Tests run

Compile command: `javac -Xdiags:verbose -d <temporary-folder> HangmanGame.java Player.java HangmanPlayer.java RoundRecord.java main.java`. Launch: `java -cp <temporary-folder> Main`. Compile and launch both exited with code 0.

| Case | Observed result |
|---|---|
| Win: solve cat with base prize 10 and entry fee 5 | 3 matches selected ×1.5; winnings 15; starting balance 100 became 110. |
| Loss: one wrong guess at limit 1 | No winnings; entry fee deducted; history recorded loss. |
| Numeric guess | Rejected with a clear message; next guess accepted. |
| Repeated guess | Rejected without changing the revealed positions or wrong-guess count. |
| Insufficient funds | Balance 2 could not pay cost 5; spending and games played stayed at 0. |
| Invalid startup number | Invalid text was rejected and the program prompted again. |
| Save then load in a new process | Balance, player totals, game word, and history restored. |
| Missing save file | Friendly error shown; menu remained usable. |
| Statistics after one win and one loss | File reported 2 games, 1 win, 10 spent, 15 won, net 5, and 50% win rate. |

These were console checks; no screenshots were captured. Capture 8-10 screenshots for submission and attach them to the test cases.

## Complete before submission

- Complete and sign the pledge in Main's file header.
- Add both partners' names, actual work split, and signatures to the collaboration agreement; do not infer contributions.
- Document student changes made after AI assistance.
- Capture and submit 8-10 screenshots. Prepare the Day 6 presentation with both partners explaining their work.
