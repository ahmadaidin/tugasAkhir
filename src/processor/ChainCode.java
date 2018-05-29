package processor;


import java.awt.Point;

import java.util.ArrayList;

public class ChainCode {
    private ArrayList<Integer> direction;
    private ArrayList<Point> pixelPos;

    public ChainCode(){
        direction = new ArrayList<>();
        pixelPos = new ArrayList<>();
    }

    public ChainCode(ArrayList<Integer> direction, ArrayList<Point> pixelPos) {
        this.direction = direction;
        this.pixelPos = pixelPos;
    }

    public ArrayList<Integer> getDirection() {
        return direction;
    }

    public ArrayList<Point> getPixelPos() {
        return pixelPos;
    }

    public void addDirection(Integer elmt) {
        direction.add(elmt);
    }

    public void addPixelPos(Point elmt) {
        Point p = new Point(elmt);
        pixelPos.add(p);
    }

    public Integer getDirectionElmt(int idx) {
        return direction.get(idx);
    }

    public Point getPixelPosElmt(int idx) {
        return pixelPos.get(idx);
    }

    public void setDirectionElmt(int idx, Integer elmt) {
        direction.set(idx, elmt);
    }

    public void setPixelPosElmt(int idx, Point elmt) {
        Point p = new Point(elmt);
        pixelPos.add(p);pixelPos.set(idx, p);
    }

    public int directionSize(){
        return direction.size();
    }

    public int pixelPosSize() {
        return pixelPos.size();
    }

    public void clear() {
        direction.clear();
        pixelPos.clear();
    }

    public Point popPixelPos(){
        return pixelPos.remove(pixelPos.size()-1);
    }

    public int popPixelDir(){
        return direction.remove(direction.size()-1);
    }


    public void pushPixelPos(Point point){
        pixelPos.add(point);
    }

}
