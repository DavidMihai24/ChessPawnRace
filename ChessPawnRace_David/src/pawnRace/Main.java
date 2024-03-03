package pawnRace;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        char whiteGap;
        char blackGap;
        boolean isHumanPlayer1;
        boolean isHumanPlayer2;

        Scanner input = new Scanner(System.in);
        if (args.length == 4) {
            isHumanPlayer1 = args[0].equals("P");
            isHumanPlayer2 = args[1].equals("P");
            whiteGap = args[2].charAt(0);
            blackGap = args[3].charAt(0);
        }
        else {
            System.out.println("Enter P - human or C - computer for player 1");
            isHumanPlayer1 = input.next().equals("P");
            System.out.println("Enter P - human or C - computer for player 2");
            isHumanPlayer2 = input.next().equals("P");
            System.out.println("Enter the file for the white gap (A - H)");
            whiteGap = input.next().charAt(0);
            System.out.println("Enter the file for the black gap (A - H)");
            blackGap = input.next().charAt(0);
        }

        Board board = new Board(whiteGap, blackGap);
        Game game = new Game(board);
        int playerTurn = 1;

        Player player1 = new Player(game, board, Color.WHITE, isHumanPlayer1);
        Player player2 = new Player(game, board, Color.BLACK, isHumanPlayer2);
        String san;
        Move move;
        board.display();

        while (!game.isFinished()) {
            if (playerTurn == 1) {
                boolean validMove = false;
                if (isHumanPlayer1) {
                    do {
                        System.out.println("Player 1, enter your move");
                        san = input.next().toLowerCase();
                    }
                    while (!GameUtil.isValidMove(san));
                    san = GameUtil.toStandardNotation(san, player1.getColor(), board);
                    move = game.parseMove(san);
                    if (move != null) {
                        validMove = true;
                    }
                    game.applyMove(move);
                }
                else {
                    player1.makeMove();
                }
                board.display();
                System.out.println();
                if (validMove || !isHumanPlayer1) {
                    playerTurn = 2;
                }
            }

            if (playerTurn == 2) {
                boolean validMove = false;
                if (isHumanPlayer2) {
                    do {
                        System.out.println("Player 2, enter your move");
                        san = input.next().toLowerCase();
                    }
                    while (!GameUtil.isValidMove(san));
                    san = GameUtil.toStandardNotation(san, player2.getColor(), board);
                    move = game.parseMove(san);
                    if (move != null) {
                        validMove = true;
                    }
                    game.applyMove(move);
                }
                else {
                    player2.makeMove();
                }
                board.display();
                System.out.println();
                if (validMove || !isHumanPlayer2) {
                    playerTurn = 1;
                }
            }
        }
        Color color = game.getGameResult();
        System.out.println(GameUtil.gameResults(color));
    }
}