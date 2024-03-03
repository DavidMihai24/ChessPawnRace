package pawnRace;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Player {
    public Game game;
    public Board board;
    public Color color;
    public boolean isComputerPlayer;
    public Square[] pawnList;

    public Player(Game game, Board board, Color color, boolean isComputerPlayer) {
        this.game = game;
        this.board = board;
        this.color = color;
        this.isComputerPlayer = isComputerPlayer;
    }

    public Color getColor() {
        return color;
    }

    public Square[] getAllPawns() {
        List<Square> pawnList = new ArrayList<>();

        for(int x=0;x<8;x++)
            for(int y=0;y<8;y++)
                if(board.getSquare(x, y).OccupiedBy()==color)
                {
                    pawnList.add(board.getSquare(x, y));
                }
        return pawnList.toArray(new Square[pawnList.size()]);
    }

    public Move[] getAllValidMoves(){
        Color opponent = GameUtil.oppositePlayer(color);

        pawnList = getAllPawns();
        List<Move> moveList = new ArrayList<>();
        for(int i = 0; i < pawnList.length; i++) {
            Square from = pawnList[i];
            int file = from.getX();
            int rank = from.getY();
            Square to;
            Move move;
            int direction = GameUtil.getDirectionOfPawn(color);

            //enPassant
            if(!GameUtil.movedPawn(pawnList[i]) &&
                    (board.getSquare(file, rank + direction).OccupiedBy() == Color.NONE) &&
                    (board.getSquare(file, rank + 2 * direction).OccupiedBy() == Color.NONE)) {
                to = board.getSquare(file, rank + 2 * direction);
                move = new Move(from, to, false, false);
                moveList.add(move);
            }

            // normal move: if the square in front of the pawn is empty, add the move to the list. If the square is on
            // the second or seventh rank, it can move two squares, otherwise it can move only one.
            if(board.getSquare(file, rank + direction).OccupiedBy() == Color.NONE) {
                to = board.getSquare(file, rank + direction);
                move = new Move(from, to, false, false);
                moveList.add(move);
                if((rank == 1 && color == Color.WHITE) || (rank == 6 && color == Color.BLACK)) {
                    to = board.getSquare(file, rank + 2 * direction);
                    move = new Move(from, to, false, false);
                    moveList.add(move);
                }
            }

            //capture
            if(file > 0) {
                if(board.getSquare(file - 1, rank + direction).OccupiedBy() == opponent) {
                    to = board.getSquare(file - 1, rank + direction);
                    move = new Move(from, to, true, false);
                    moveList.add(move);
                }
            }

            //capture
            if(file < 7) {
                if(board.getSquare(file + 1, rank + direction).OccupiedBy() == opponent) {
                    to = board.getSquare(file + 1, rank + direction);
                    move = new Move(from, to, true, false);
                    moveList.add(move);
                }
            }

            //enPassantCapture
            Move lastMove = game.getLastMove();
            if(lastMove != null) {
                if(file > 0) {
                    if(board.getSquare(file - 1, rank).OccupiedBy() == opponent &&
                            (board.getSquare(file - 1, rank + direction).OccupiedBy() == Color.NONE) &&
                            GameUtil.isEnPassant(lastMove) &&
                            (lastMove.getTo(). getX() == file - 1) &&
                            (lastMove.getTo().getY() == rank)) {
                        to = board.getSquare(file - 1, rank + direction);
                        move = new Move(from, to, true, true);
                        moveList.add(move);
                    }

                    if(file < 7) {
                        if (board.getSquare(file + 1, rank).OccupiedBy() == opponent &&
                                (board.getSquare(file + 1, rank + direction).OccupiedBy() == Color.NONE) &&
                                GameUtil.isEnPassant(lastMove) &&
                                (lastMove.getTo().getX() == file + 1) &&
                                (lastMove.getTo().getY() == rank)) {
                            to = board.getSquare(file + 1, rank + direction);
                            move = new Move(from, to, true, true);
                            moveList.add(move);
                        }
                    }
                }
            }
        }
        return moveList.toArray(new Move[moveList.size()]);
    }

    public void makeMove() {
        Move[] moves = getAllValidMoves();
        int numMoves = moves.length;
        int moveIndex = new Random().nextInt(numMoves);
        Move nextMove = moves[moveIndex];
        game.applyMove(nextMove);
    }
}