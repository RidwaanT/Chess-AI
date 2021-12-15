package chessnotcheckers;

public class Move {
    int srcx, srcy, destx, desty;
    boolean capture;  /* true if this was a capture move */
    boolean castle = false;

    Move() {}
	// moves from one position to the next
    Move(int xPos, int yPos, int xDes, int yDes, boolean c) {
	srcx = xPos;
	srcy = yPos;
	destx = xDes;
	desty = yDes;
	capture = c;
    }
    // moves but considers castling
    Move(int xPos, int yPos, int xDes, int yDes, boolean c, boolean castle) {
	srcx = xPos;
	srcy = yPos;
	destx = xDes;
	desty = yDes;
	capture = c;
        this.castle = castle;
    }

    Move(String s) {
	srcx = s.charAt(0) - 'a';
	srcy = s.charAt(1) - '1';
	destx = s.charAt(2) - 'a';
	desty = s.charAt(3) - '1';
    }

    public void copyMove(Move m) {
	srcx = m.srcx;
	srcy = m.srcy;
	destx = m.destx;
	desty = m.desty;
	capture = m.capture;
    }

    public boolean equals(Move m) {
	return (m.srcx == srcx && m.srcy == srcy && m.destx == destx && m.desty == desty);
    }
    
    public String toString() {
	return new String (new byte[] {
	    (byte) ('a'+srcx), (byte)('1'+srcy), (byte)('a'+destx), (byte)('1'+desty)});
    }
}
