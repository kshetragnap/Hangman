/*
 * AI assistance disclosure: ChatGPT helped resolve the Main filename issue and update the menu.
 * Review and document any further student edits before submission, as required by the assignment.
 * The work submitted is my/our own work.
 * Elnatan Teaghes, 2026-11-15
 * Kshetragna Pitchuka, 2026-11-15
 */
import java.util.ArrayList;
import java.util.Scanner;

/** Program entry point for the Hangman project. */
class Main {
    /** Starts the console application.
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        HangmanConsole.run();
    }
}
/** Handles prompts, menus, and user-friendly recovery from invalid input. */
final class HangmanConsole {
    private HangmanConsole() { }

    /** Creates the player and game, then runs the menu. */
    public static void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Hangman ===");
            HangmanPlayer player = new HangmanPlayer(
                    readText(scanner, "Player name: "),
                    readNonNegativeDouble(scanner, "Starting balance: $"),
                    readNonNegativeDouble(scanner, "Cost per play: $"));
            HangmanGame game = new HangmanGame(
                    readText(scanner, "Secret word or phrase: "),
                    readPositiveInt(scanner, "Maximum incorrect guesses: "));
            game.setBaseWinnings(readNonNegativeDouble(scanner, "Base prize for a win: $"));
            runMenu(scanner, player, game);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            System.out.println("Program ended: " + ex.getMessage());
        }
    }

    private static void runMenu(Scanner scanner, HangmanPlayer player, HangmanGame game) {
        boolean running = true;
        while (running) {
            System.out.println("\n1. Play a round");
            System.out.println("2. Show player statistics");
            System.out.println("3. Show round history");
            System.out.println("4. Save game");
            System.out.println("5. Load game");
            System.out.println("6. Generate statistics file");
            System.out.println("7. Exit");
            String choice = readText(scanner, "Choose an option: ");
            try {
                switch (choice) {
                    case "1": playRound(scanner, player, game); break;
                    case "2": showStats(player); break;
                    case "3": showHistory(game.getHistory()); break;
                    case "4": game.saveGame(player); break;
                    case "5": game.loadGame(player); break;
                    case "6": game.generateStatistics(player); break;
                    case "7": running = false; break;
                    default: System.out.println("Choose a number from 1 to 7.");
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("That action could not be completed: " + ex.getMessage());
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

        System.out.println("Entry fee paid: $" + player.getCostPerPlay());
        while (!game.isGameOver()) {
            System.out.println("\nWord: " + game.getMaskedWord());
            System.out.println("Incorrect guesses: " + game.getIncorrectGuesses()
                    + "/" + game.getMaxIncorrectGuesses());
            try {
                boolean matched = game.makeGuess(readGuess(scanner));
                System.out.println(matched ? "That letter is in the word." : "No match.");
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage() + " Try again.");
            }
        }

        RoundRecord record = game.recordRound(player);
        if (record.isWon()) {
            System.out.println("You solved it! Winnings: $" + record.getWinnings());
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
        System.out.println("Net profit/loss: $" + (player.getTotalWinnings() - player.getAmountSpent()));
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
            if (!value.isEmpty()) return value;
            System.out.print("Please enter a value: ");
        }
        throw new IllegalStateException("Input ended unexpectedly.");
    }

    private static double readNonNegativeDouble(Scanner scanner, String prompt) {
        while (true) {
            String input = readText(scanner, prompt);
            try {
                double value = Double.parseDouble(input);
                if (Double.isFinite(value) && value >= 0) return value;
            } catch (NumberFormatException ignored) {
                // Recover by displaying the validation message and prompting again.
            }
            System.out.println("Enter a finite, non-negative number.");
        }
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            String input = readText(scanner, prompt);
            try {
                int value = Integer.parseInt(input);
                if (value > 0) return value;
            } catch (NumberFormatException ignored) {
                // Recover by displaying the validation message and prompting again.
            }
            System.out.println("Enter a whole number greater than zero.");
        }
    }
}
