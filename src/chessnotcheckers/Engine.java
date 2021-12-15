package chessnotcheckers;

/** For the chess engine I also learned the steps on how by reviewing online resources for measures to complete
 *  it*/

import java.util.*;

// This is the algorithm that runs our program, it pieces the sections together.
public class Engine {

    private ChessBoard board;  //   The current board state
    private Evaluator scoreCheck; // USed to access the evaluator and values.
    

    // The time is adjusted by MS
    private long startTime;  // the time in milliseconds to start think of move.
    private long allocated;  // Allowed time to Think
    private int  increment;  // The time control (initialTime)

    
    

    /* The name of your program */
    private static final String studentName = "Rt18yd - 6556138";

    /* Here are the public methods for Player.  You may change these
       methods, but do not alter their signatures.  There's probably
       no reason to change these unless you rewrite ChessBoard.
    */
    
    // Get Name of the program for the Main method
    public String getName() {
	return studentName;
    }

    public void newGame() {
	board = new ChessBoard(); // We create a new board with all pieces.
    }

    // This Method actually makes the move on the board
    public void applyMove(Move m) {
	board.makeMove(m);
    }

    /* Return the board of the current player */
    public ChessBoard getBoard() {
	return board;
    }

    /**
     * Just as written the function that automates the computer moves.
    */
    public Move computeMove(int timeleft, int optime, int height) {
        
	
        
	startTime = System.currentTimeMillis();
	allocated = allocateTime(timeleft);
	System.out.println("ALLOCATED: " + allocated + "ms");

	return alphaBetaSearch(height); // We use this method to start our search
    }



    /**
     * This lets us work how long or how much time we want to assert for each move
    */
    private int allocateTime(int timeleft) {
	double t = increment + timeleft/30.0;
	if (t > timeleft) t = .9*timeleft;
	return (int) t;
    }


    /**
     * Checks the timer to see if we surpassed the time given
    */
    private boolean timeup() {
        // Once your timer passes the set time from main, we return all
	if ((System.currentTimeMillis()-startTime) > allocated) {
	    return true; // true if time is done.
	}
	return false;
    }


    /*
        When checking for time it will interrupt alpha beta search
        but if you have a really deep depth than you only get a search for one move
    */
    

    // Available moves,
    private Move alphaBetaSearch(int height){
        Move bestMove = null; // Object to hold best Move
        List allMoves = board.generateMoves(); // we get all possible moves for 
                                                // the current piece.
        Move[] moves = (Move[]) allMoves.toArray(new Move[0]); 
                                                // cast list to array
        int bestScore = Integer.MIN_VALUE; // set best to min so we accept all
        for(int i=0; i<moves.length; i++){ //we loop through all poss, moves.
            if(timeup())break; // if we run out of time 
            ChessBoard sBoard = new ChessBoard(board); // we create a scanboard.
            sBoard.makeMove(moves[i]); // the scan board will hold tests moves.
                            // can go deeper than usual.
            int alpha = Integer.MIN_VALUE; // alpha beta requires low alpha
            int beta = Integer.MAX_VALUE; // high beta so we weed out extras.
            int testScore = alpha_beta(sBoard, height,alpha, beta, false); 
                                // Need to implement the alpha beta section
            if(testScore>bestScore){ // if we get a higher test score, we set
                bestScore = testScore; // a new variable on the throne.
                bestMove = moves[i]; // if test is bigger new best Move
            }
        }
        return bestMove; // give the Move to be played back.
    }
    // Deprecated Method that can be used to randomly move Pieces.
    private Move randomMove() {
	/* If there is at least one capture move, choose one of them at random,
	   otherwise just move randomly. */
	Random rand = new Random(); 
	List moveList = board.generateMoves();
	if (moveList.size() == 0) return null;  // if the game is over, 
                                                //just return null

	Move [] moveArray = (Move[]) moveList.toArray(new Move[0]);
	Move [] captureArray = new Move[moveArray.length];
	int capCount = 0;
	for (int i=0; i<moveArray.length; i++) if (moveArray[i].capture) 
            captureArray[capCount++] = moveArray[i];
	if (capCount > 0) return captureArray[rand.nextInt(capCount)];
	return moveArray[rand.nextInt(moveArray.length)];
    }


    /*The Alpha Beta A.k.a alternate minmax method, this method will take the
    its parameters and return a score for the best moves that it searched.
    */ 
    private int alpha_beta(ChessBoard board, int height,int alpha, 
            int beta, boolean maxi) {
        height--; // every move we lower the height, once 0 we return
        
        
        // we use the evaluator to give us our initial score
        int score = scoreCheck.eval(board);
        
        if(maxi)score*=-1; // on the maxi turns we want a negative number
        
        if(timeup()) return score;
        
        if(0 == height){ // Check height if we reached limit return score
        return score;
        }
        // We return at a win or StaleMate, win = 50,000 stalemate = 100
        // The negative and positive value is to return for max or mini
        if(board.turn == 0){ // This checks for checkmate for opposing
                board.turn = 1;
                if(board.generateMoves().isEmpty()){
                    board.turn =0;
                    return score;
                }
        }
        // we create a scan board to make moves on.
        ChessBoard sBoard = new ChessBoard(board);
        // we want our best to take any higher value
        int best = Integer.MIN_VALUE; //we will take any score
        // The array of Position Moves will hold all possible Moves
            Move[] posMoves = (Move[])sBoard.generateMoves().
                    toArray(new Move[0]);
           
            if(maxi) { // if were the MAxi player
            for(int i=0; i<posMoves.length; i++){ // look at All possible moves
                if(timeup()){ // if Time is up we either give the best score
                    if(best>Integer.MIN_VALUE) return best;
                    return score; // or we give the current score w/o checks.
                } 
                sBoard.makeMove(posMoves[i]); // we make our test move
                
                best = Math.max(best, alpha_beta(sBoard, // rerun search tree.
                        height, alpha, beta, !maxi)); // one end it's found.
                alpha = Math.max(alpha, best); // compare to get rid of sliding.
                
                // Pruning
                if(beta <= alpha) {
                    break;
                }
                    
            }
            return best; // we return the score which gives us the best.

        } else{ // Same exant as for Max
                best = Integer.MAX_VALUE;
                for(int i=0; i<posMoves.length; i++){
                    if(timeup()){
                        if(best<Integer.MAX_VALUE) return best;
                        return score;
                    } 
                    sBoard.makeMove(posMoves[i]);
                    //if Alpha >max go to max else Alpha = max
                    
                    best = Math.min(best, alpha_beta(sBoard,
                            height, alpha, beta, !maxi));
                    beta = Math.min(beta, best);

                    if(beta <= alpha){ //prune the search
                        //System.out.println("Pruned");
                        break;
                    }
                }
            return best; // wer return the score which gives us the best.
            }
    }
    
   
}
