package pawnRace;

public class GameUtil {

    public static Color oppositePlayer(Color color) {
        return color==Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    public static int getDirectionOfPawn(Color pawn){
        return switch (pawn) {
            case WHITE -> 1;
            case BLACK -> -1;
            default -> {
                assert (false) : "Pawn should be white or black";
                yield 0;
            }
        };
    }

    public static boolean lastRank(Board board, Color playercolor){
        for(int x = 0; x < 8; x++) {
            if(board.getSquare(x, 7).OccupiedBy()==playercolor)
                return true;
            if(board.getSquare(x,0).OccupiedBy()==playercolor)
                return true;
        }
        return false;
    }


    public static boolean canMove(Board board, int x, int y, Move lastMove){
        Color pawncolor = board.getSquare(x, y).OccupiedBy();
        if(lastRank(board,pawncolor))
            return false;
        if(canCapture(board,x,y))
            return true;
        if(canEnPassantCapture(board,x,y,lastMove))
            return true;
        return canForward(board, x, y);

    }

    public static boolean canForward(Board board,int x, int y){
        Color pawn = board.getSquare(x, y).OccupiedBy();
        int direction = getDirectionOfPawn(pawn);

        return board.getSquare(x, y + direction).OccupiedBy() == Color.NONE;

    }

    public static boolean canCapture(Board board,int x, int y){
        Color pawn = board.getSquare(x, y).OccupiedBy();
        int direction = getDirectionOfPawn(pawn);
        if(x>0 && (board.getSquare(x-direction, y+direction).OccupiedBy()==oppositePlayer(pawn))){
            return true;
        }
        return x < 7 && (board.getSquare(x + 1, y + direction).OccupiedBy() == oppositePlayer(pawn));
    }

    public static boolean canEnPassantCapture(Board board,int x,int y, Move lastMove ){

        if(lastMove==null)
            return false;

        int toPrevX = lastMove.getTo().getX();
        int toPrevY = lastMove.getTo().getY();

        int toMoveX = board.getSquare(x, y).getX();
        int toMoveY = board.getSquare(x, y).getY();

        if(!lastMove.isEnPassantCapture())
            return false;

        return toPrevY == toMoveY && (toMoveX + 1 == toPrevX || toMoveX - 1 == toPrevX);

    }

    public static boolean draw(Board board, Color currentplayer, Move lastmove){
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                if(board.getSquare(x, y).OccupiedBy() == currentplayer)
                    if(canMove(board, x, y, lastmove))
                        return false;
            }
        }
        return true;
    }

    public static boolean allWhiteCaptured(Board board){
        int ok = 1;
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++)
            {if(board.getSquare(x, y).OccupiedBy()==Color.WHITE)
                ok = 0;
            }
        }
        return ok == 1;
    }

    public static boolean allBlackCaptured(Board board){
        int ok = 0;
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++)
            {if(board.getSquare(x, y).OccupiedBy() == Color.BLACK)
                ok = 1;
            }
        }
        return ok == 0;
    }

    public static Square stringToSquare(String str,Board board){
        int x = str.charAt(0) - 'a';
        int y = str.charAt(1) - '1';

        Square square = board.getSquare(x, y);
        square.setOccupier(board.getSquare(x, y).OccupiedBy());

        return square;
    }

    public static boolean validMove(Square from, Square to, Board board,
                                    Color player, Move lastMove,
                                    boolean isCapture, boolean isEnPassant) {
        int direction = getDirectionOfPawn(player);
        if(isEnPassant){

            Square lastFrom = lastMove.getFrom();
            Square lastTo = lastMove.getTo();

            Square enPassantPawn = board.getSquare(to.getX(), to.getY() - direction );

            //direction e -1
            int ydifference = to.getY() - from.getY();

            if(to.getY() != 2 && to.getY() != 5 ) return false;

            if( (to.getY() == from.getY() + direction) &&
                    lastFrom.getY() + 2 * direction == lastTo.getY() && to.OccupiedBy()==Color.NONE
                    && (enPassantPawn.OccupiedBy() == oppositePlayer(from.OccupiedBy())
                    && (ydifference == 1 || ydifference == -1) && (lastTo.getX() == enPassantPawn.getX())
                    && (lastTo.getY() == enPassantPawn.getY())))
                return true;

        }
        if(isCapture){

            if(to.OccupiedBy() == oppositePlayer(from.OccupiedBy()) &&
                    to.OccupiedBy() != Color.NONE && from.OccupiedBy() != Color.NONE
                    && (from.getX() + 1 == to.getX() || from.getX() -1 == to.getX()) // might be a problem here
                    && (from.getY() == to.getY() - direction))
                return true;
        }
        return from.getX() == to.getX() && (to.getY() == from.getY() + direction ||
                to.getY() == from.getY() + direction * 2) &&
                to.OccupiedBy() == Color.NONE &&
                board.getSquare(from.getX(), to.getY()).OccupiedBy() == Color.NONE;
    }

    public static Move stringToMove(String san, Board board, int index, Move lastMove, Color player){
        if(san.length() != 5)
            return null; //san has to be 5 characters long

        String squarestring1 = san.substring(0, 2);
        Square fromSquare = stringToSquare(squarestring1, board);

        int direction = getDirectionOfPawn(player);

        String squarestring2 = san.substring(3, 5);
        Square toSquare = stringToSquare(squarestring2, board);

        boolean isCapture = san.contains("x");
        boolean isEnPassantCapture = san.contains("x") && (toSquare.OccupiedBy()==Color.NONE);

        if(index==0)
        {
            if(fromSquare.OccupiedBy() == player && toSquare.getX()==fromSquare.getX()
                    && (toSquare.getY()==fromSquare.getY() + direction ||
                    toSquare.getY() == fromSquare.getX() + 2*direction)) {
                return new Move(fromSquare, toSquare, isCapture, isEnPassantCapture);
            }
            System.out.println("Invalid first move");
            return null;
        }

        if(validMove(fromSquare, toSquare,board,player,lastMove, isCapture, isEnPassantCapture)){
            return new Move(fromSquare, toSquare, isCapture, isEnPassantCapture);
        }

        System.out.println("Invalid move");
        return null;
    }

    public static boolean movedPawn(Square pawn){
        int y = pawn.getX();
        Color color = pawn.OccupiedBy();

        return switch (color) {
            case WHITE -> !(y == 1);
            case BLACK -> !(y == 7);
            default -> false;
        };
    }
    public static boolean isEnPassant (Move move){
        return Math.abs(move.getTo().getY() - move.getFrom().getY()) == 2;
    }

    public static boolean isValidMove(String san){
        return san.length() == 5 || san.length() == 2 || san.length() == 4;
    }

    public static String toStandardNotation(String san, Color color, Board board) {
        //already in standard notation
        if(san.length() == 5)
            return san;

        //normal move
        if(san.length() == 2) {
            int file = Character.toLowerCase(san.charAt(0)) - 'a';
            int rank = san.charAt(1) - '1';
            int direction = GameUtil.getDirectionOfPawn(color);
            if(board.getSquare(file, rank - direction).OccupiedBy() == color) {
                return "" + (char)(file + 'a') + (char)(rank - direction + '1') + '-' + san;
            }
            else if(board.getSquare(file, rank - 2 * direction).OccupiedBy() == color) {
                return "" + (char)(file + 'a') + (char)(rank - 2 * direction + '1') + '-' + san;
            }
            assert(false) : "Invalid move";
            return null;
        }

        //capture
        if(san.length() == 4) {
            int direction = GameUtil.getDirectionOfPawn(color);
            int rank = san.charAt(3) - '1';
            return "" +san.charAt(0) + (char)(rank - direction + '1') + "x"
                    + san.charAt(2)
                    + san.charAt(3);
        }
        //invalid move
        assert(false):
                "move is not valid";
        return null;
    }

    public static String gameResults(Color winner){
        return switch (winner) {
            case WHITE -> "WHITE HAS WON!!";
            case BLACK -> "BLACK HAS WON!!";
            default -> "STALEMATE!!";
        };
    }
}