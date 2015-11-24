//Garret Stevens
package mazesolver;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class MazeSolver {

    //access as maze[row][col]
    char[][] maze = new char[][]{{'*'}};
    
    
    //unmodified copy of maze
    char[][] omaze = new char[][]{{'*'}};
    
    
    //where we start from
    int startrow=-1,startcolumn=-1;                      
    
    //our goal
    int endrow=-1, endcolumn=-1;
    
    
    //copy omaze to maze
    private void copy_omaze_to_maze(){
        maze = new char[omaze.length][omaze[0].length];
        for(int i=0;i<omaze.length;++i){
            for(int j=0;j<omaze[0].length;++j){
                maze[i][j] = omaze[i][j];
            }
        }
    }
    
    public MazeSolver(){
        JFrame win = new JFrame("A-Mazing!");
        win.setLayout(new BorderLayout());
        
        JPanel mazepanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g2){
                Graphics2D g = (Graphics2D) g2;
                int columns = maze[0].length;
                int rows = maze.length;
                double boxw = getWidth()*1.0/columns;
                double boxh = getHeight()*1.0/rows;
                for(int x=0;x<columns;++x){
                    for(int y=0;y<rows;++y){
                        if( maze[y][x] == ' ' )
                            g.setPaint(Color.WHITE);
                        else if( maze[y][x] == '*' )
                            g.setPaint(Color.BLACK);
                        else if( maze[y][x] == 'S' )
                            g.setPaint(Color.RED);
                        else if( maze[y][x] == 'E' )
                            g.setPaint(Color.GREEN);
                        else if( maze[y][x] == 'X' )
                            g.setPaint(Color.GRAY);
                        else if( maze[y][x] == '.' )
                            g.setPaint(Color.PINK);
                        g.fillRect((int)(x*boxw),(int)(y*boxh),(int)boxw+1,(int)boxh+1);
                    }
                }
            }
        };

        
        mazepanel.addMouseListener( new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent me) {
                float pctx = me.getX() * 1.0f / mazepanel.getWidth();
                float pcty = me.getY() * 1.0f / mazepanel.getHeight();
                
                int col = (int)( pctx * maze[0].length);
                int row = (int)( pcty * maze.length );
                if(col == maze[0].length )
                    col--;
                if( row == maze.length )
                    row--;
                
                if( endrow != -1 ){
                    //restore maze
                    startrow=endrow=startcolumn=endcolumn=-1;
                    copy_omaze_to_maze();
                }
                
                if( startrow == -1 && maze[row][col] == ' ' ){
                    startrow=row;
                    startcolumn=col;
                    maze[row][col] = 'S';
                    win.repaint();
                }
                else if( endrow == -1 && maze[row][col] == ' ' ){
                    endrow = row;
                    endcolumn=col;
                    maze[row][col] = 'E';
                    try{
                        solveMaze(maze,startrow,startcolumn);
                    }catch(StopRecursion e){
                        win.repaint();
                    }
                    win.repaint();
                }
                else{
                }
            }
        });
        
        
        win.add(mazepanel,BorderLayout.CENTER);    
        JMenuBar mbar = new JMenuBar();
        win.setJMenuBar(mbar);
        
        JMenu file = new JMenu("File");
        mbar.add(file);
        JMenuItem open = new JMenuItem("Open");
        file.add(open);
        open.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_O,
                ActionEvent.CTRL_MASK));
        open.addActionListener( (ActionEvent ev) -> {
            JFileChooser ch = new JFileChooser(".");
            if( ch.showOpenDialog(win) == JFileChooser.APPROVE_OPTION ){
                startrow=startcolumn=endrow=endcolumn=-1;
                
                File f = ch.getSelectedFile();
                Scanner in;
                try {
                    in = new Scanner(f);
                    ArrayList<String>A = new ArrayList<>();
                    while(in.hasNextLine()){
                        String line = in.nextLine();
                        A.add(line);
                    }
                    omaze = new char[A.size()][A.get(0).length()];
                    for(int i=0;i<A.size();++i){
                        if( A.get(i).length() != omaze[i].length )
                            throw new RuntimeException("Bad maze");
                        for(int j=0;j<A.get(i).length();++j){
                            omaze[i][j] = A.get(i).charAt(j);
                        }
                    }
                    copy_omaze_to_maze();
                    win.repaint();
                } catch (FileNotFoundException ex) {
                    //should never happen
                    JOptionPane.showMessageDialog(win, "Could not load. Sorry.");
                    System.exit(1);
                }
            }    
        });
        
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(512,512);
        win.setVisible(true);
    }
    
    public static void main(String[] args) {
        new MazeSolver();
    }
    
    void solveMaze(char[][] maze, int startrow, int startcolumn ){
        if(maze[startrow][startcolumn] == 'E') throw new StopRecursion();//base case
        if(maze[startrow][startcolumn] != 'S') maze[startrow][startcolumn] = '.';//path from start to finish
        
        if(startrow>0){
            if(maze[startrow-1][startcolumn] == ' ' || maze[startrow-1][startcolumn] == 'E') solveMaze(maze, startrow-1, startcolumn);//north
        }
        if(startrow<maze.length-1){
            if(maze[startrow+1][startcolumn] == ' ' || maze[startrow+1][startcolumn] == 'E') solveMaze(maze, startrow+1, startcolumn);//south
        }
        if(startcolumn>0){
            if(maze[startrow][startcolumn-1] == ' ' || maze[startrow][startcolumn-1] == 'E') solveMaze(maze, startrow, startcolumn-1);//west
        }
        if(startcolumn<maze[startrow].length-1){
            if(maze[startrow][startcolumn+1] == ' ' || maze[startrow][startcolumn+1] == 'E') solveMaze(maze, startrow, startcolumn+1);//east
        }
        if(maze[startrow][startcolumn] != 'S') maze[startrow][startcolumn] = 'X';//taken path, not solution
    }
}
