//Garret Stevens
package map.lab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MapLab {
    
    static TreeMap<Integer, Integer> colorMap = new TreeMap<>();
    public static int[] colorAmounts = new int[10];
    
    public static void getColors(BufferedImage img){
        for(int i=0;i<img.getHeight();i++){
            for(int j=0;j<img.getWidth();j++){
                int color = img.getRGB(j,i);
                if(colorMap.containsKey(color)){
                    int plusOne = colorMap.get(color);
                    colorMap.put(color, plusOne+1);
                }else{
                    colorMap.put(color, 1);
                }
            }
        }
    }
    
    public static boolean contains(int[] list, int number){
        for(int i=0;i<list.length;i++){
            if(list[i] == number){
                return true;
            }
        }
        return false;
    }
    
    public static void drawHistogram(int[] tenColors){
        JFrame fr = new JFrame("10 Most Common Colors");
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setLayout(new BorderLayout());
        
        JPanel p = new JPanel();
        fr.add(p, BorderLayout.CENTER);
        p.setLayout(null);
        fr.setVisible(true);
        
        JPanel bar;
        int height;
        double relativeHeight = 480.0 / colorAmounts[0];
        
        for(int i=0;i<10;i++){
            bar = new JPanel();
            height = (int) (colorAmounts[i] * relativeHeight);
            bar.setLocation(63*i,481-height);
            bar.setSize(63,height); 
            bar.setBackground(new Color(tenColors[i]));
            p.add(bar);
        }
        fr.setSize(646, 520);
    }
  
    public static void main(String[] args) {
        JFileChooser jf = new JFileChooser(".");
        jf.showOpenDialog(null);
        File f = jf.getSelectedFile();
        BufferedImage img;
        
        try{
            img = ImageIO.read(f);
        }
        catch(IOException e){
            System.out.println("No.");
            return;
        }
        getColors(img);
        int[] mostCommonColors = new int[10];
        
        colorMap.keySet().stream().forEach((color) -> {
            for(int i=0;i<10;i++){
                if(colorMap.get(color) > colorAmounts[i] && !contains(mostCommonColors, color)){
                    colorAmounts[i] = colorMap.get(color);
                    mostCommonColors[i] = color;
                }
            }
        });
        
        System.out.println("The 10 most common colors in the image are:");
        System.out.println(Arrays.toString(mostCommonColors));
        drawHistogram(mostCommonColors);
    }
}
