package myGraphic;

import java.awt.*;
import java.util.ArrayList;

public class MyPolygon {
    public ArrayList<Point> points;

    public MyPolygon(){
        points = new ArrayList<>();
    }

    public MyPolygon(ArrayList<Point> points) {
        this.points = points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public ArrayList<MyLine> getAllLines() {
        ArrayList<MyLine> myLines = new ArrayList<>();

        for(int i = 0; i < points.size(); i++) {
            Point start = points.get(i);
            Point end = points.get((i+1)% points.size());
            MyLine myLine = new MyLine(start,end);
            myLines.add(myLine);
        }

        return myLines;
    }

    public ArrayList<Point> getAllPoints(){
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<MyLine> allLines = getAllLines();
        for (MyLine myLine : allLines) {
            ArrayList<Point> lineAllPoints = myLine.getAllPoints();
            for (int i = 1; i < lineAllPoints.size(); i++) {
                points.add(lineAllPoints.get(i));
            }
        }
        return points;
    }

}
