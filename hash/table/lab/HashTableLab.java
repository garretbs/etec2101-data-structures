//Garret Stevens
package hash.table.lab;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class HashTableLab {
    static int x;
    static int y;
    
    public static void doShuffle(HashSet[][] R){
        ArrayList<String> shufflar = new ArrayList<>(R[x][y]);
        Collections.shuffle(shufflar);
        if(shufflar.isEmpty()){
            x = 1;
            y = 2;
        }else{
            x = Integer.parseInt(shufflar.get(0).split("[^\\d]+")[1]);
            y = Integer.parseInt(shufflar.get(0).split("[^\\d]+")[2]);
        }
    }
    
    public static void getRandomDir(boolean[][] M, int size, HashSet[][] R){
        int oldX = x;int oldY = y;
        int randomizer;
        Random r = new Random();
        randomizer = r.nextInt(4);
        if(randomizer == 0 && y < size-2){ //down
          y++;
        }else if(randomizer == 1 && x > 1){ //left
            x--;
        }else if(randomizer == 2 && y > 1){ //up
            y--;
        }else if(randomizer == 3 && x < size-2){ //right
            x++;
        }
        if(x == 0)x=1;
        if(y == 0)y=1;
        if(x == oldX && y == oldY){
            doShuffle(R);
            getRandomDir(M, size, R);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Enter the desired maze size:");
        Scanner inputScan = new Scanner(System.in);
        String input = inputScan.nextLine();
        final int size = Integer.parseInt(input);
        int[] startPoint = {1, 0};
        int[] endPoint = {size*3/4, size-2};
        
        boolean[][] M = new boolean[size][size];
        M[startPoint[0]][startPoint[1]] = true;
        M[startPoint[0]][startPoint[1]+1] = true;
        M[endPoint[0]][endPoint[1]] = true;
        M[endPoint[0]][endPoint[1]+1] = true;
        
        boolean a;boolean b;boolean c;boolean d;boolean e;boolean f;
        boolean g;boolean h;boolean i;
        HashSet[][] R = new HashSet[size][size];
        HashSet tempSet;
        for(x=0;x<R.length;x++){
            for(y=0;y<R[x].length;y++){
                R[x][y] = new HashSet<>();
            }
        }
        
        x=1;y=2;
        int[] coord = new int[2];
        for(int j=0;j<size*size;j++){
            a = (x > 0 && y > 0) ? M[x-1][y-1] : false;
            b = (y > 0) ? M[x][y-1] : false;
            c = (y > 0) ? M[x+1][y-1] : false;
            d = (x > 0) ? M[x-1][y] : false;
            e = M[x][y];
            f = M[x+1][y];
            g = (x > 0 && y < size-2) ? M[x-1][y+1] : false;
            h = (y < size-2) ? M[x][y+1] : false;
            i = M[x+1][y+1];
            
            if(b){
                coord[0] = x;
                coord[1] = y-1;
                R[x][y].add(Arrays.toString(coord));
                R[x][y].addAll(R[x][y-1]);
            }
            if(d){
                coord[0] = x-1;
                coord[1] = y;
                R[x][y].add(Arrays.toString(coord));
                R[x][y].addAll(R[x-1][y]);
            }
            if(f){
                coord[0] = x+1;
                coord[1] = y;
                R[x][y].add(Arrays.toString(coord));
                R[x][y].addAll(R[x+1][y]);
            }
            if(h){
                coord[0] = x;
                coord[1] = y+1;
                R[x][y].add(Arrays.toString(coord));
                R[x][y].addAll(R[x][y+1]);
            }
            tempSet = R[x][y-1]; //cell above
            tempSet.retainAll(R[x-1][y]); //cell left
            tempSet.retainAll(R[x+1][y]); //cell right
            tempSet.retainAll(R[x][y+1]); //cell below
            coord[0] = x;coord[1] = y;
            if(!((a&&b&&d) || (b&&c&&f) || (d&&g&&h) || (f&&h&&i) || tempSet.contains(Arrays.toString(coord)))){
                M[x][y] = true;
            }else{
                doShuffle(R);
            }
            getRandomDir(M, size, R);
        }
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for(x=0;x<size;x++){
                for(y=0;y<size;y++){
                    img.setRGB(x, y, M[x][y]? 0xffffffff : 0xff000000);
                }
            }
        try{
            ImageIO.write(img,"png",new File("maze.png") );
        }catch(IOException ex){
        }
    }
}
