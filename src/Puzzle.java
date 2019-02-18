import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    private JPanel puzzlePannel = new JPanel();

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

       //Set Up JPanel
       puzzlePannel.setPreferredSize(new Dimension(dimensions, (dimensions+margin)));
       puzzlePannel.setBackground(Color.WHITE);
       puzzlePannel.setForeground(foreGround);
       puzzlePannel.setFont(new Font("SansSerif",Font.BOLD,60));

       gameOver = true;

       puzzlePannel.addMouseListener(new MouseAdapter() {

       });







   }

   private void newGame(){
       do{
           reset();
           shuffle();
       } while(!isSolvable());
       gameOver = false;
   }

   private void reset(){
       for(int i =0; i<pieces.length;i++){
           pieces[i]= (i+1) % pieces.length;
       }
       //set blank tile
       blankPiece = pieces.length-1;
   }

   private void shuffle(){
       int num = numTiles;

       while(num >1){
           int randomInt = rand.nextInt(num--);
           int temp = pieces[randomInt];
           pieces[randomInt] = pieces[num];
           pieces[num]= temp;
       }
   }

   private boolean isSolvable(){
       int count = 0;

       for(int i = 0; i < numTiles; i++){
           for(int j = 0; j<i; j++){
               if(pieces[j]>pieces[i])
                   count++;
           }
       }
       return count % 2 == 0;

   }

   private boolean isSolved(){
       if(pieces[pieces.length -1] !=0)
           return false;

       for(int i = numTiles-1; i>=0;i--){
           if(pieces[i] != i+1)
               return false;
       }
       return true;
   }

}
