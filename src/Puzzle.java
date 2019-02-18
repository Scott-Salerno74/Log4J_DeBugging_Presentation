import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.Graphics;
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

   private void drawGrid(Graphics2D g){
       for(int i =0; i< pieces.length; i++){
           int row = i/size;
           int col= i%size;
           //create coordinates
           int x = (margin + col*pieceSize);
           int y = (margin + row*pieceSize);
           //special case
           if(pieces[i] == 0){
               if(gameOver){
                   g.setColor(foreGround);
                   drawCentString(g,"v",x,y);
               }
               continue;
           }
           //other pieces
           g.setColor(puzzlePannel.getForeground());
           g.fillRoundRect(x,y,pieceSize,pieceSize,25,25);
           g.setColor(Color.BLACK);
           g.drawRoundRect(x,y,pieceSize,pieceSize,25,25);
           g.setColor(Color.WHITE);

           drawCentString(g,String.valueOf(pieces[i]),x,y);

       }



   }

   private void drawStart(Graphics2D g){
       if(gameOver){
           g.setFont(puzzlePannel.getFont().deriveFont(Font.BOLD,18));
           g.setColor(Color.BLACK);
           String s = "Click To Start!";
           g.drawString(s,(puzzlePannel.getWidth()-g.getFontMetrics().stringWidth(s))/2,puzzlePannel.getHeight()-margin);
       }

   }

   private void drawCentString(Graphics2D g,String s, int x, int y){
     FontMetrics fM = g.getFontMetrics();
     int ascending = fM.getAscent();
     int descending = fM.getDescent();
       g.drawString(s,x+ (pieceSize - fM.stringWidth(s))/2,y+(ascending +(pieceSize - (ascending-descending))/2));
   }

   @Override
    protected void paintComponent(Graphics g){
       super.paintComponent(g);
       Graphics2D g2d = (Graphics2D) g;
       g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
       drawGrid(g2d);
       drawStart(g2d);
   }

}
