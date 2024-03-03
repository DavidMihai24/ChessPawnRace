package pawnRace;

public class Square {
    private Color color;
    public int x;
    public int y;


    public Square(int x, int y) {
        assert(x >= 0 && x < 8
                && y >= 0 && y < 8):
                "Invalid square: " + x + " " + y;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color OccupiedBy() {
        return color;
    }

    public void setOccupier(Color color) {
        this.color = color;
    }
}

