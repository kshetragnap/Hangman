import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("___Hangman___");
            String name = readText(scanner, "Player name: ");
            double balance = readNonNegativeDouble(scanner, "Starting balance: $");
            double costPerPlay = readNonNegativeDouble(scanner, "Cost per play: $");
            String word = readText(scanner, "Secret word or phrase: ");
            int maxGuesses = readPositiveInt(scanner, "Maximum incorrect guesses: ");
            double prize = readNonNegativeDouble(scanner, "Prize for a win: $");

            HangmanPlayer player = new HangmanPlayer(name, balance, costPerPlay);
            HangmanGame game = new HangmanGame(word, maxGuesses);
            game.setBaseWinnings(prize);
            runMenu(scanner, player, game);
        } catch (IllegalArgumentException ex) {
            System.out.println("Could not start Hangman: " + ex.getMessage());
        }
    }

    private static void runMenu(Scanner scanner, HangmanPlayer player, HangmanGame game) {
        boolean running = true;
        while (running) {
            System.out.println("\n1. Play a round");
            System.out.println("2. Show player stats");
            System.out.println("3. Show round history");
            System.out.println("4. Exit");
            String choice = readText(scanner, "Choose an option: ");

            switch (choice) {
                case "1":
                    playRound(scanner, player, game);
                    break;
                case "2":
                    showStats(player);
                    break;
                case "3":
                    showHistory(game.getHistory());
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Please choose 1, 2, 3, or 4.");
            }
        }
        System.out.println("Thanks for playing, " + player.getName() + "!");
    }

    private static void playRound(Scanner scanner, HangmanPlayer player, HangmanGame game) {
        try {
            game.beginRound(player);
        } catch (IllegalStateException ex) {
            System.out.println(ex.getMessage());
            return;
        }

        System.out.println("Round started. Entry fee: $" + player.getCostPerPlay());
        while (!game.isGameOver()) {
            System.out.println("\nWord: " + game.getMaskedWord());
            System.out.println("Incorrect guesses: " + game.getIncorrectGuesses()
                    + "/" + game.getMaxIncorrectGuesses());
            try {
                char guess = readGuess(scanner);
                boolean matched = game.makeGuess(guess);
                System.out.println(matched ? "That letter is in the word." : "No match.");
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage() + " Try again.");
            }
        }

        RoundRecord record = game.recordRound(player);
        if (record.isWon()) {
            System.out.println("You solved it! The word was: " + record.getWord());
            System.out.println("Winnings: $" + record.getWinnings());
        } else {
            System.out.println("Out of guesses. The word was: " + record.getWord());
        }
        System.out.println("Balance: $" + player.getBalance());
    }

    private static void showStats(HangmanPlayer player) {
        System.out.println("\nPlayer: " + player.getName());
        System.out.println("Type: " + player.getPlayerType());
        System.out.println("Balance: $" + player.getBalance());
        System.out.println("Total spent: $" + player.getAmountSpent());
        System.out.println("Total won: $" + player.getTotalWinnings());
        System.out.println("Games played: " + player.getGamesPlayed());
        System.out.println("Games won: " + player.getGamesWon());
        System.out.println("Current win streak: " + player.getCurrentStreak());
    }

    private static void showHistory(ArrayList<RoundRecord> history) {
        if (history.isEmpty()) {
            System.out.println("No completed rounds yet.");
            return;
        }
        System.out.println("\nRound history:");
        for (int i = 0; i < history.size(); i++) {
            RoundRecord record = history.get(i);
            System.out.println((i + 1) + ". " + (record.isWon() ? "Won" : "Lost")
                    + " | word: " + record.getWord()
                    + " | matches: " + record.getMatches()
                    + " | incorrect guesses: " + record.getIncorrectGuesses()
                    + " | winnings: $" + record.getWinnings());
        }
    }

    private static char readGuess(Scanner scanner) {
        String input = readText(scanner, "Guess one letter: ");
        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
            throw new IllegalArgumentException("Enter exactly one letter.");
        }
        return input.charAt(0);
    }

    private static String readText(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (scanner.hasNextLine()) {
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.print("Please enter a value: ");
        }
        throw new IllegalArgumentException("Input ended unexpectedly.");
    }

    private static double readNonNegativeDouble(Scanner scanner, String prompt) {
        while (true) {
            String input = readText(scanner, prompt);
            try {
                double value = Double.parseDouble(input);
                if (Double.isFinite(value) && value >= 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Re-prompt below with a clear message.
            }
            System.out.println("Enter a non-negative number.");
        }
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            String input = readText(scanner, prompt);
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Re-prompt below with a clear message.
            }
            System.out.println("Enter a whole number greater than zero.");
        }
    }
}
