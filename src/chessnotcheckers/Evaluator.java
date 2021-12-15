package chessnotcheckers;


import chessnotcheckers.ChessBoard;
import java.util.LinkedList;
import java.util.List;

public class Evaluator {

    private static final int WHITE = ChessBoard.WHITE; // these are the values 
    private static final int BLACK = ChessBoard.BLACK; // for the colors
    
/* This function will use multiple algorithims to calculate the value of a move.
    */
    public static int eval(ChessBoard board) {
        
        board.turn = 1-board.turn; // we use this to set the board to look at us
        
        ChessBoard.SquareDesc[] pieceArray = board.getPieces(board.getTurn());
        int value = 0; // we initialize our value
        
        // This for Loop will check if the pieces match the map below
        // and what points it should get for it
        for(int i=0; i<pieceArray.length; i++){ //we look at all pieces
            value += checkValue(pieceArray[i].type); //we give the value for
                                                    // appropriate pieces
            if(checkPiece(pieceArray[i].type)){ // if the piece is P,N,B we
                                                // give it points based on map
                int[] typeArray = checkType(pieceArray[i].type);
                int x = pieceArray[i].x;
                int y = pieceArray[i].y;
                if(pieceArray[i].color == BLACK){ // black needs a reverse map
                    value += typeArray[(63-(y/8*8*7 + y%8*8 +x))]; // this formula gives us the tile location
                } else{
                    value +=typeArray[(y/8*8*7 + y%8*8 +x)]; // this will be white
                }
            }
        }
            // we check whose turn it is and switch it to look for checks
            if(board.turn==BLACK){ 
                board.turn= 1-board.turn;
                if(board.inCheck()){ // if we have a check we give some points
                    value += 1500; // technically we could count checks and give
                } // more points for more checks
                board.turn= 1-board.turn;
            } else{ //if the turn is white we set it to black
                board.turn= 1-board.turn;
                if(board.inCheck()){
                    value+= 1500;
                }
                board.turn= 1-board.turn;
            }
            
            List tMoves = board.generateMoves(); // we need a List of our moves
            Move[] cMoves = (Move[]) tMoves.toArray(new Move[0]); 
            int castlingCount =0; // we count the number of castling moves, and
                                     // to keep availability we give points for
                                     // it
            for(int i=0; i<cMoves.length;i++){ // we check all moves for castles
                if(cMoves[i].castle ==true){
                    castlingCount++;
                }
                
            }
            
            value+= castlingCount * 10; //we provide the value with a small
                                        // point value
            if(board.turn == BLACK){ // This checks for checkmate for opposing
                board.turn = 1-board.turn; // we change the boards turn
                if(board.generateMoves().isEmpty()){ // if it has no moves
                    if(board.inCheck()){
                    board.turn = 1-board.turn; // we know that
                    value += 300000; // we give a huge payout for checkmaye
                    } else{
                        board.turn = 1-board.turn;
                        value+= 100; // if it's a tie 
                    }
                } 
            } else { // do the same thing but for white
                board.turn = 1-board.turn;
                if(board.generateMoves().isEmpty()){ // if it has no moves
                    if(board.inCheck()){
                    board.turn = 1-board.turn; // we know that
                    value += 300000;
                } else{
                    board.turn = 1-board.turn;
                    value+= 100;
                    }
                }
            }
            board.turn = 1-board.turn;
	return value;
    }

    /* Material value of a piece */
    private static int[] pieceValue = new int[7];
    static {
	pieceValue[ChessBoard.PAWN]   = 300;
	pieceValue[ChessBoard.KNIGHT] = 900;
	pieceValue[ChessBoard.BISHOP] = 900;
	pieceValue[ChessBoard.ROOK]   = 1500;
	pieceValue[ChessBoard.QUEEN]  = 2700;
	pieceValue[ChessBoard.KING]   = 106000;
	pieceValue[ChessBoard.EMPTY]  = 0;
    }
    
    // Used to give the correct points for the piece
    private static int checkValue(int type){
        return pieceValue[type];
    }
    
    // used to check if the piece is a pawn, night or bishop
    private static boolean checkPiece(int type){
        if ( type == 0 || type == 1 || type == 5){
            return true;
        } else return false;
    }
    
    // since we know it's one of these 3 we return the right piece
    private static int[] checkType(int type){
        if(type ==0){
            return bishoppos;
        } else if(type ==1){
            return knightpos;
        } else{
            return pawnpos;
        }
    }


    /* Piece value tables modify the value of each
       piece according to where it is on the board.

       To orient these tables, each row of 8 represents one
       row (rank) of the chessboard.  The first row is
       where white's pieces start.  So, for example
       having a pawn at d2 for is worth -5 for white.
       Having it at d7 is worth 20.  Note that these
       have to be flipped over to evaluate black's pawns.
    */
    
    // these tables Are taking from my source but it's based off hte idea that
    //certain pieces are more powerful in some locations, I've updated and 
    // adjusted the Pawn to strive towards getting promoted
    private static int bishoppos[] = {-5,-5,-5,-5,-5,-5,-5,-5,
				      -5,10,5,8,8,5,10,-5,
				      -5,5,3,8,8,3,5,-5,
				      -5,3,10,3,3,10,3,-5,
				      -5,3,10,3,3,10,3,-5,
				      -5,5,3,8,8,3,5,-5,
				      -5,10,5,8,8,5,10,-5,
				      -5,-5,-5,-5,-5,-5,-5,-5};
    
    private static int knightpos[] = {-10,-5,-5,-5,-5,-5,-5,-10,
				      -8,0,0,3,3,0,0,-8,
				      -8,0,10,8,8,10,0,-8,
				      -8,0,8,10,10,8,0,-8,
				      -8,0,8,10,10,8,0,-8,
				      -8,0,10,8,8,10,0,-8,
				      -8,0,0,3,3,0,0,-8,
				      -10,-5,-5,-5,-5,-5,-5,-10};
    
    private static int pawnpos[] =   {0,0,0,0,0,0,0,0,
				      0,0,0,-5,-5,0,0,0,	
				      0,2,3,4,4,3,2,0,
				      0,4,6,10,10,6,4,0,
				      0,6,9,10,10,9,6,0,
				      4,8,12,16,16,12,8,4,
				      5,10,15,20,20,15,10,5,
				      25,30,35,35,35,35,30,25};
}
