package processor;


import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 05/10/2016.
 */

public class ObjectExtractor {

    private MyImage grayscale;
    public int fg;
    public int bg;

    public ObjectExtractor(MyImage grayImage) {
        grayscale = new MyImage(grayImage);
        fg = 255;
        bg = 0;
    }

    public static final int VISITED_PIXEL = 200;

    public void iterate(ArrayList<ChainCode> chainCodes, Point iterator){
        setVisited(iterator);
        ChainCode paths = findPath(iterator);
        ChainCode chainCode = new ChainCode();
        for(int i = 0; i<paths.pixelPosSize(); i++) {
            chainCode.addPixelPos(iterator);
            chainCode.addDirection(paths.getDirectionElmt(i));
            Point start = new Point(paths.getPixelPosElmt(i));
            addPixelToFeature(chainCode, start);
            if(chainCode.pixelPosSize()!=0){
                chainCodes.add(chainCode);
            }
        }
    }

    public void addPixelToFeature(ChainCode chainCode, Point iterator) {
        chainCode.addPixelPos(iterator);
        ChainCode paths = findPath(iterator);
        while(paths.pixelPosSize()>=1) {
            setVisited(iterator);
            iterator = new Point(paths.getPixelPosElmt(0));
            chainCode.addDirection(paths.getDirectionElmt(0));
            chainCode.addPixelPos(paths.getPixelPosElmt(0));
            paths = findPath(iterator);
        }
    }

    public void setVisited(Point p) {
        grayscale.setRGB(p.x,p.y,VISITED_PIXEL);
    }

    public Point getNextIterator(Point lastIterator){
        Point iterator = new Point(lastIterator);
        while(iterator.y<grayscale.getHeight()){
            iterator.x++;
            while(iterator.x<grayscale.getWidth()){
                if(grayscale.getRGB(iterator.x,iterator.y)==fg) {
                    return iterator;
                }
                else iterator.x++;
            }
            iterator.x = 0;
            iterator.y++;
        }
        return lastIterator;
    }

    public ArrayList<ChainCode> findFeatures() {
        ArrayList<ChainCode> chainCodes = new ArrayList<>();
        Point lastIterator = new Point(0,0);
        Point iterator = new Point(0,0);
        do{
            setVisited(iterator);
            iterate(chainCodes,iterator);
            iterator = getNextIterator(lastIterator);
            lastIterator.setLocation(iterator);
        } while(!iterator.equals(lastIterator));


        return chainCodes;
    }

    public ChainCode findPath(Point iterator) {
        ChainCode paths = new ChainCode();

        Point searcher;
        if(iterator.y>0 && iterator.x>0)
        if(grayscale.getRGB(iterator.x-1,iterator.y-1)==fg){
            searcher = new Point(iterator.x-1,iterator.y-1);
            paths.addPixelPos(searcher);
            paths.addDirection(1);
        }

        if(iterator.y>0)
        if (grayscale.getRGB(iterator.x,iterator.y-1)==fg ) {
            searcher = new Point(iterator.x,iterator.y-1);
            paths.addPixelPos(searcher);
            paths.addDirection(2);
        }

        if(iterator.y>0 && iterator.x<grayscale.getWidth())
        if (grayscale.getRGB(iterator.x+1,iterator.y-1)==fg ) {
            searcher = new Point(iterator.x+1,iterator.y-1);
            paths.addPixelPos(searcher);
            paths.addDirection(3);
        }

        if(iterator.x<grayscale.getWidth())
        if (grayscale.getRGB(iterator.x+1,iterator.y)==fg ) {
            searcher = new Point(iterator.x+1,iterator.y);
            paths.addPixelPos(searcher);
            paths.addDirection(4);
        }

        if(iterator.y<grayscale.getHeight() && iterator.x < grayscale.getWidth())
        if (grayscale.getRGB(iterator.x+1,iterator.y+1)==fg) {
            searcher = new Point(iterator.x+1,iterator.y+1);
            paths.addPixelPos(searcher);
            paths.addDirection(5);
        }

        if(iterator.y<grayscale.getHeight())
        if (grayscale.getRGB(iterator.x,iterator.y+1)==fg) {
            searcher = new Point(iterator.x,iterator.y+1);
            paths.addPixelPos(searcher);
            paths.addDirection(6);
        }

        if(iterator.y<grayscale.getHeight() && iterator.x>0)
        if (grayscale.getRGB(iterator.x-1,iterator.y+1)==fg ) {
            searcher = new Point(iterator.x-1,iterator.y+1);
            paths.addPixelPos(searcher);
            paths.addDirection(7);
        }

        if(iterator.x>0)
        if (grayscale.getRGB(iterator.x-1,iterator.y)==fg) {
            searcher = new Point(iterator.x-1,iterator.y);
            paths.addPixelPos(searcher);
            paths.addDirection(8);
        }
        return paths;
    }

    public ArrayList<ChainCode> cleareNoise(ArrayList<ChainCode> chainCodes){
        ArrayList<ChainCode> result = new ArrayList<>();
        for(int i = 0; i< chainCodes.size(); i++){
            if(chainCodes.get(i).directionSize()>0){
                result.add(chainCodes.get(i));
            }
        }
        return result;
    }

}
