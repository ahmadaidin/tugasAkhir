package processor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PartsMerger{
    HashMap<Integer, ArrayList<Point>> mergerCandidate1;
    HashMap<Integer, ArrayList<Point>> mergerCandidate2;
    HashMap<Integer, Boolean> visitedCandidate1;
    HashMap<Integer, Boolean> visitedCandidate2;

    public PartsMerger(HashMap<Integer, ArrayList<Point>> mergerCandidate1, HashMap<Integer, ArrayList<Point>> mergerCandidate2){
        this.mergerCandidate1 = mergerCandidate1;
        this.mergerCandidate2 = mergerCandidate2;
        visitedCandidate1 = new HashMap<>();
        visitedCandidate2 = new HashMap<>();

        for(Integer key: mergerCandidate1.keySet()){
            visitedCandidate1.put(key,false);
        }

        for(Integer key: mergerCandidate2.keySet()){
            visitedCandidate2.put(key,false);
        }
    }



}
