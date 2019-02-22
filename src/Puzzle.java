import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;



public class Puzzle extends JPanel{
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
    //Log4J setup
    private static final Logger log = LogManager.getLogger(Puzzle.class);

    private JPanel puzzlePannel = new JPanel();
    private JFrame puzzleFrame = new JFrame();

   public Puzzle(){
       size = 4;
       dimensions =550;
       margin = 30;

       //Pieces
       numTiles = (size * size -1); //left space for blank tile
       pieces = new int[size*size];

       //grid size
       gridSize = (dimensions-2* margin);
       pieceSize = gridSize/size;

       //Set Up JPanel
       puzzlePannel.setMinimumSize(new Dimension(100,100));
       puzzlePannel.setMaximumSize(new Dimension(250, 250));


       puzzlePannel.setBackground(Color.WHITE);
       puzzlePannel.setForeground(foreGround);
       puzzlePannel.setFont(new Font("SansSerif",Font.BOLD,100));


       puzzleFrame.addMouseListener(new MouseAdapter() {

           @Override
           public void mousePressed(MouseEvent e){
               log.info("Mouse has been pressed!");
               if(gameOver){
                   newGame();
               }
               else{
                   log.debug("You clicked A portion of the frame");
                   int ex = e.getX() -margin;
                   int ey = e.getY() -margin;
                    //grid clicking
                   if (ex<0||ex >gridSize || ey<0 || ey >gridSize){
                       return;
                   }
                   log.info("You have pressed a Square in the Grid");
                   int c1 = ex/ pieceSize;
                   int r1 = ey/ pieceSize;

                   //position of blank piece
                   int c2 = blankPiece % size;
                   int r2 = blankPiece / size;

                   //convert to coordinate
                   int clickPos = r1*size+c1;
                   int dir = 0;
                   //search direction for movement
                   if (c1 ==c2&& Math.abs(r1-r2)>0) {
                        dir =(r1 -r2) > 0 ? size: -size;
                        log.info("searching for movement options and changing size");

                   } else if(r1 ==r2 && Math.abs(c1-c2)>0){
                       dir =(c1 -c2) > 0 ? 1: -1;
                       log.debug("2nd attempt to make a move");

                   }
                   if (dir != 0){
                       //move tile
                       do{
                           int newBlankPiece = blankPiece+dir;
                           pieces[blankPiece] = pieces[newBlankPiece];
                           blankPiece= newBlankPiece;
                       } while(blankPiece != clickPos);
                             log.info("Got in while loop");
                       pieces[blankPiece] = 0;
                       }
                   log.info("Block has been moved");

                       //check if solved
                   gameOver = isSolved();
                   }
                   //repaint panel
               repaint();
               }

       });





      newGame();

   }

   private void newGame(){
       log.info("New Game Has been started");
       log.debug("Debugging the New Game");
       do{
           reset();
           shuffle();
       } while(!isSolvable());
       gameOver = false;
   }

   private void reset(){
       log.info("The game has been reset");
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
       log.info("The Puzzle Has been solved");
       return true;

   }

   private void drawGrid(Graphics2D g){
       for(int i =0; i< pieces.length; i++){
           int row = i/size;
           int col= i%size;
           //create coordinates
           int x = margin + col*pieceSize;
           int y = margin + row*pieceSize;
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
           g.drawString(s,(puzzlePannel.getWidth()-g.getFontMetrics().stringWidth(s))/2,(puzzlePannel.getHeight()- margin));
       }

   }

   private void drawCentString(Graphics2D g,String s, int x, int y){
     FontMetrics fM = g.getFontMetrics();
     int ascending = fM.getAscent();
     int descending = fM.getDescent();
       g.drawString(s,(x+ (pieceSize - fM.stringWidth(s))/2),(y+(ascending +(pieceSize - (ascending+descending))/2)));
   }

   @Override
    protected void paintComponent(Graphics g){
       super.paintComponent(g);
       Graphics2D g2d = (Graphics2D) g;
       g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
       drawGrid(g2d);
       drawStart(g2d);
   }


   public void run(){
       SwingUtilities.invokeLater(() -> {
           puzzleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           puzzleFrame.setTitle("Row 2's Puzzle Game");
           puzzleFrame.add(new Puzzle(),BorderLayout.CENTER);
           puzzleFrame.pack();
           puzzleFrame.setLocationRelativeTo(null);
           puzzleFrame.setVisible(true);
           puzzleFrame.setSize(600,600);

       });



   }


}
