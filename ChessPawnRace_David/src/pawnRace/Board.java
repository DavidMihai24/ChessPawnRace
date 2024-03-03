package pawnRace;

public class Board {
    public int whiteGap;
    public int blackGap;
    public Square[][] board = new Square[8][8];

    public Board(char whiteGap, char blackGap) {
        whiteGap = Character.toUpperCase(whiteGap);
        blackGap = Character.toUpperCase(blackGap);

        assert(whiteGap >= 'A' && blackGap >= 'A' &&
                whiteGap <= 'H' && blackGap <= 'H'):
                "invalid pawn gaps";

        this.whiteGap = whiteGap - 'A';
        this.blackGap = blackGap - 'A';

        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                if((y == 1 && x + 'A' != whiteGap) || (y == 6 && x + 'A' != blackGap)) {
                    if(y == 1) {
                        board[x][y] = new Square(x,y);
                        board[x][y].setOccupier(Color.WHITE);
                }
                    if(y == 6) {
                        board[x][y] = new Square(x,y);
                        board[x][y].setOccupier(Color.BLACK);
                }
            }
                else {
                    board[x][y] = new Square(x,y);
                    board[x][y].setOccupier(Color.NONE);
                }
            }
        }
    }

    public Square getSquare(int x, int y) {
        return board[x][y];
    }

    public void applyMove(Move move) {
        int fromX = move.getFrom().getX();
        int fromY = move.getFrom().getY();
        int toX = move.getTo().getX();
        int toY = move.getTo().getY();

        Color pawnColor = board[fromX][fromY].OccupiedBy();
        board[fromX][fromY].setOccupier(Color.NONE);
        board[toX][toY].setOccupier(pawnColor);

        int direction = GameUtil.getDirectionOfPawn(pawnColor);

        if(move.isEnPassantCapture()){
            board[toX][toY-direction].setOccupier(Color.NONE);
        }

        if(move.isCapture()) {
            Square fromSquare = getSquare(move.getFrom().getX(), move.getFrom().getY());
            fromSquare.setOccupier(Color.NONE);
            Square toSquare = getSquare(move.getTo().getX(), move.getTo().getY());
            toSquare.setOccupier(fromSquare.OccupiedBy());
        }
    }

    public void display(){
        System.out.println();
        System.out.println(" ABCDEFGH ");
        for(int y = 7; y >= 0; y--){
            System.out.print((y + 1) + "");
            for(int x = 0; x <= 7; x++){
                if(board[x][y].OccupiedBy()==Color.BLACK) {
                    System.out.print('B');
                }
                else if(board[x][y].OccupiedBy()==Color.WHITE) {
                    System.out.print('W');
                }
                else {
                    System.out.print('.');
                }
            }
            System.out.print("" + (y + 1));
            System.out.println();
        }
        System.out.println(" ABCDEFGH ");
    }
}
