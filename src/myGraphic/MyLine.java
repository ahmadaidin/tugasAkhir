package myGraphic;

import java.awt.*;
import java.util.ArrayList;

public class MyLine {
    private Point start;
    private Point end;

    public MyLine() {
        start = new Point();
        end = new Point();
    }

    public MyLine(Point start, Point end) {
        this.start= start;
        this.end = end;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public ArrayList<Point> getAllPoints(){
        ArrayList<Point> points = new ArrayList<>();

        int dx = Math.abs(end.x-start.x), sx = start.x<end.x ? 1 : -1;
        int dy = Math.abs(end.y-start.y), sy = start.y<end.y ? 1 : -1;
        int err = (dx>dy ? dx : -dy)/2, e2;
        int x = start.x;
        int y = start.y;

        for(;;){
            Point P = new Point(x,y);
            points.add(P);
            if (P.x==end.x && P.y==end.y) break;
            e2 = err;
            if (e2 >-dx) { err -= dy; x += sx; }
            if (e2 < dy) { err += dx; y += sy; }
        }
        return points;
    }


}
