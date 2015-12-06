//Garret Stevens
import javax.swing.*;
import java.util.*;
import java.io.*;

public class Graph1 {
    
    public static HashMap<String, ArrayList> amazingGraph = new HashMap<>();
    
    public static boolean intermediateHop(String start, String end){
        for(int i=0;i<amazingGraph.get(start).size();i++){
            String location = (String) amazingGraph.get(start).get(i);
            if(amazingGraph.containsKey(location)){
                if(amazingGraph.get(location).contains(end)) return true;
            }
        }
        return false;
    }
    
    public static void main(String[] args){

        FileInputStream fin;
        try{
            fin = new FileInputStream("atc.txt");
        }
        catch(IOException e){
            System.out.println("Too bad.");
            return;
        }

        Scanner in = new Scanner(fin);
        TreeSet<String> T = new TreeSet<>();

        while(in.hasNext()){
            String line = in.nextLine();
            int idx = line.indexOf("->");
            String key = line.substring(0,idx).trim();
            String value = line.substring(idx+2).trim();
            ArrayList<String> myList = new ArrayList<>();myList.add(value);
            if(amazingGraph.containsKey(key)){
                amazingGraph.get(key).add(myList.get(0));
                myList = amazingGraph.get(key);
            }
            amazingGraph.put(key, myList);
            T.add(key);
            T.add(value);
        }
        String[] AS = T.toArray(new String[0]);
        
        JPanel panny = new JPanel();
        JList<String> lst1 = new JList<>(AS);
        JList<String> lst2 = new JList<>(AS);
        JScrollPane jsp1 = new JScrollPane(lst1);
        JScrollPane jsp2 = new JScrollPane(lst2);
        panny.add(jsp1);
        panny.add(new JLabel("<HTML>&rarr;</HTML>"));
        panny.add(jsp2);
        JOptionPane.showOptionDialog(
            null,
            panny,
            "Select...",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,   //icon
            null,   //options: array
            null    //initial option
        );
        
        List<String> tmp = lst1.getSelectedValuesList();
        if( tmp.size() == 0 )
            System.exit(0);
        String start = tmp.get(0);
        
        tmp = lst2.getSelectedValuesList();
        if( tmp.size() == 0 )
            System.exit(0);
        String end = tmp.get(0);
        boolean skip = (!amazingGraph.containsKey(start) || amazingGraph.get(start).isEmpty());
        if(!skip && amazingGraph.get(start).contains(end)){
            JOptionPane.showOptionDialog(null,
                "Yes, there is a path directly from "+start+" to\n"+end+".",
                "Message",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"OK"},
                null    );
        }else if(!skip && intermediateHop(start, end)){
            JOptionPane.showOptionDialog(null,
                "Yes, there is an intermediate hop from "+start+" to\n"+end+".",
                "Message",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"OK"},
                null    );
        }else{JOptionPane.showOptionDialog(null,
            "No, there is no direct path or intermediate hop from\n"+start+" to "+end+".",
            "Message",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"OK"},
            null    );
        }
        System.exit(0);
    }
}
