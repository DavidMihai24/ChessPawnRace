package pawnRace;

public class Move {
    public Square from;
    public Square to;
    public boolean isCapture;
    public boolean isEnPassantCapture;

    public Move(Square from, Square to, boolean isCapture, boolean isEnPassantCapture) {
        this.from = from;
        this.to = to;
        this.isCapture = isCapture;
        this.isEnPassantCapture = isEnPassantCapture;
    }

    public Square getFrom(){
        return from;
    }


    public Square getTo() {
        return to;
    }

    public boolean isCapture() {
        return from.OccupiedBy() != to.OccupiedBy() && from.OccupiedBy() != Color.NONE && to.OccupiedBy() != Color.NONE;
    }

    public boolean isEnPassantCapture() {
        Square square = new Square(from.getX()+1,from.getY()+1);
        return from.OccupiedBy() != square.OccupiedBy() && to.OccupiedBy() == Color.NONE && from.OccupiedBy() != Color.NONE;
    }
}
