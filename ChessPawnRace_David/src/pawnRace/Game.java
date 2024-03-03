package pawnRace;

public class Game {
    public Board board;
    public Move[] moves  = new Move[10000];
    public int index = 0;
    public Color currentPlayer = Color.WHITE;


    public Game(Board board) {
        this.board = board;
    }

    public Move getLastMove(){
        if(index==0) return null;
        else
            return moves[index-1];
    }

    public void applyMove(Move move){
        //assert(move!=null) : "error empty move";

        if (move != null) {
            moves[index] = move;
            index += 1;
            board.applyMove(move);
            currentPlayer = GameUtil.oppositePlayer(currentPlayer);
        }
    }

    public boolean isFinished(){
        if(index<1)
            return false;

        if(GameUtil.lastRank(board,Color.WHITE) || GameUtil.lastRank(board,Color.BLACK))
            return true;

        if(GameUtil.allWhiteCaptured(board) || GameUtil.allBlackCaptured(board))
            return true;

        if(GameUtil.draw(board, currentPlayer, getLastMove()))
            return true;
        return false;
    }

    public Move parseMove(String san) {
        return GameUtil.stringToMove(san, board, index , getLastMove(), currentPlayer);
    }

    public Color getGameResult() {

        if(isFinished()){
            assert(index > 0) : "No moves have been played";
            if(GameUtil.lastRank(board, Color.WHITE))
                return Color.WHITE;
            if(GameUtil.lastRank(board, Color.BLACK))
                return Color.BLACK;
            if(GameUtil.draw(board, currentPlayer, moves[index-1]))
                return Color.NONE;
        }

        return null;
    }
}