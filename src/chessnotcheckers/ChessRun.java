package chessnotcheckers;

/*
    I used many resources to learn on how to make the chess piece run, my AI isn't the fastest
    so if you run at large depths it becomes unproductive
*/

/*
 *
 *    Text based java program because I couldn't implement the GUI

 */



import java.util.*;
import java.io.*;

public class ChessRun {

    private static boolean logfile = false; // true if we're using a logfile
    
public static void main(String[] args) throws IOException {
    InputStreamReader stdin;
    stdin = new InputStreamReader(System.in);

	Move[] moveArray;
	ChessBoard pBoard;
	String command, prompt;
	Move m;
	int height =25;
	System.out.println("Depth of Search? (If doing a deep search increase time to allow for more moves checked)");
	height = new Scanner(System.in).nextInt();
	System.out.println("max time for each move in Seconds? (Remember the higher the depth the more time needed)");
	int t = new Scanner(System.in).nextInt();
	Engine player = new Engine();
	System.out.println("Computer player: "+player.getName());
	
	while(true) {
	    player.newGame();  // Check to see if it works
	    while (true) {
		pBoard = player.getBoard();
		if (pBoard.getTurn() == ChessBoard.WHITE) {
			prompt = "Human"; // we check the turn for the player etc.
		} else prompt = "Computer";
		System.out.println("\n\nPosition ("+prompt+" to move):\n"+pBoard);
		moveArray = (Move[]) pBoard.generateMoves().toArray(new Move[0]);
		if (moveArray.length == 0) {
		    if (pBoard.inCheck()) System.out.println("Checkmate");
		    else System.out.println("Stalemate");
		    break;
		}
		

		System.out.println("Moves:");
		System.out.print("   ");
		for (int i=0; i<moveArray.length; i++) {
			if ((i % 10) == 0 && i>0) System.out.print("\n   ");
			System.out.print(moveArray[i]+" ");
		}

		System.out.println();
		while(true) { 
                    if(player.getBoard().turn == 0){ // if you comment this
                        // you can debug and play just with 2 players
                        m = player.computeMove(t*60*1000, 0, height);  /*
                                                    * simulate t minute on the
                                                    * clock
                                                    */
			System.out.println("Computer Moves: " + m);
			break;
                    }
		    System.out.print(prompt+" move (or \"go\" or \"quit\")> ");
		    command = readCommand(stdin);
		    if (command.equals("go")) {
			m = player.computeMove(t*60*1000, 0, height);  /*
                                                    * simulate t minute on the
                                                    * clock
                                                    */
			System.out.println("Computer Moves: " + m);
			break;
		    } else if (command.equals("quit")) {
		        System.out.println("QUIT.\n");
			System.exit(1);
		    } else {
			m = null;
			for (int i=0; i<moveArray.length; i++) {
			    if (command.equals(moveArray[i].toString())) {
				m = moveArray[i];
				break;
			    }
			}
			if (m != null) break;
			System.out.println("\""+command+"\" not a legal move");
		    }
		}
		player.applyMove(m);
		System.out.println(prompt + " made move "+m);
	    }

	    while(true) {
		System.out.print("Play again? (y/n):");
		command = readCommand(stdin);
		if (command.equals("n")) System.exit(1);
		if (command.equals("y")) break;
	    }
	}
}
    static String readCommand(InputStreamReader stdin) throws IOException {
        final int MAX = 100;
        int len = 0;
        char[] cbuf = new char[MAX];
        //len = stdin.read(cbuf, 0, MAX);
        for(int i=0; i<cbuf.length; i++){
            if(logfile && !stdin.ready()) return "quit"; // file is done.

            cbuf[i] = (char)stdin.read();
            len++;
            if(cbuf[i] == '\n')
                break;
            if(cbuf[i] == -1){
                System.out.println("An error occurred reading input");
                System.exit(1);
            }
        }
        
        /*if (len == -1){
            System.out.println("An error occurred reading input");
            System.exit(1);
        }*/
        return new String(cbuf, 0, len).trim();  /* trim() removes \n in unix 
        and \r\n in windows */
    }
}
