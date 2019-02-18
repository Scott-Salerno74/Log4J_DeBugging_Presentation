import java.awt.*;
import java.util.Random;

public class Puzzle {
    // size of the puzzle
    private int size;
    // number of tiles in puzzle
    private int numTiles;
    // Dimensions of UI
    private int dimensions;
    //Foreground Color
    private static final Color foreGround = new Color(0,0,204);
    //Random to Shuffle our Pieces
    private static final Random rand = new Random();
    //Array to store our 1 dimensional objects
    private int[] pieces;
    //size of pieces
    private int pieceSize;
    //blank tile
    private int blankPiece;
    //margin size
    private int margin;
    //Grid size
    private int gridSize;
    //Game Over [true = game over, false !=]
    private boolean gameOver;

   public Puzzle(int size, int dim, int marg){
       this.size = size;
       dimensions = dim;
       margin = marg;

       //Pieces
       numTiles = (size * size -1); //left space for blank tile
       pieces = new int[size*size];

       //grid size
       gridSize = (dim-2* margin);
       pieceSize = gridSize/size;


   }

}
