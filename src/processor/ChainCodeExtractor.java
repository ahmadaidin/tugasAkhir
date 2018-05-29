package processor;


import java.awt.Point;

import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 05/10/2016.
 */

public class ChainCodeExtractor {

    private int[][] grayscale;

    public ChainCodeExtractor(MyImage grayImage) {
        grayscale = ImgFilterer.getGrayscale(grayImage);
    }

    public static final int VISITED_PIXEL = 200;

    public void setVisited(Point p) {
        grayscale[p.y][p.x]=VISITED_PIXEL;
    }

    public ArrayList<ChainCode> findSegments() {
        ArrayList<ChainCode> chainCodes = new ArrayList<>();
        Point lastStart = new Point(0,0);
        Point start = getNextStart(lastStart);
        while (!start.equals(lastStart)){
            lastStart.setLocation(start);
            ChainCode chainCode = new ChainCode();
            DFSSegment(start, chainCode,0);
            chainCodes.add(chainCode);
            start = getNextStart(lastStart);
        }
        return chainCodes;
    }

    public void DFSSegment(Point pixel, ChainCode result, int dir){
        setVisited(pixel);
        result.addPixelPos(pixel);
        result.addDirection(dir);
        ChainCode adj = findAdj(pixel);

        while(adj.pixelPosSize()>0){
            Point newPixel = adj.popPixelPos();
            int newDir = adj.popPixelDir();
            DFSSegment(newPixel,result,newDir);
        }
    }


    public Point getNextStart(Point lastStart){
        Point start = new Point(lastStart);
        while(start.y<grayscale.length){
            start.x++;
            while(start.x<grayscale[0].length){
                if(grayscale[start.y][start.x]==255) {
                    return start;
                }
                else start.x++;
            }
            start.x = -1;
            start.y++;
        }
        return lastStart;
    }


    public ChainCode findAdj(Point start) {
        ChainCode paths = new ChainCode();

        Point searcher;
        if(start.y>0 && start.x>0)
        if(grayscale[start.y-1][start.x-1]==255){
            searcher = new Point(start.x-1,start.y-1);
            paths.addPixelPos(searcher);
            paths.addDirection(1);
        }

        if(start.y>0)
        if (grayscale[start.y-1][start.x]==255 ) {
            searcher = new Point(start.x,start.y-1);
            paths.addPixelPos(searcher);
            paths.addDirection(2);
        }

        if(start.y>0 && start.x<grayscale[0].length-1)
        if (grayscale[start.y-1][start.x+1]==255 ) {
            searcher = new Point(start.x+1,start.y-1);
            paths.addPixelPos(searcher);
            paths.addDirection(3);
        }

        if(start.x<grayscale[0].length-1)
        if (grayscale[start.y][start.x+1]==255 ) {
            searcher = new Point(start.x+1,start.y);
            paths.addPixelPos(searcher);
            paths.addDirection(4);
        }

        if(start.y<grayscale.length-1 && start.x < grayscale[0].length-1)
        if (grayscale[start.y+1][start.x+1]==255) {
            searcher = new Point(start.x+1,start.y+1);
            paths.addPixelPos(searcher);
            paths.addDirection(5);
        }

        if(start.y<grayscale.length-1)
        if (grayscale[start.y+1][start.x]==255) {
            searcher = new Point(start.x,start.y+1);
            paths.addPixelPos(searcher);
            paths.addDirection(6);
        }

        if(start.y<grayscale.length-1 && start.x>0)
        if (grayscale[start.y+1][start.x-1]==255 ) {
            searcher = new Point(start.x-1,start.y+1);
            paths.addPixelPos(searcher);
            paths.addDirection(7);
        }

        if(start.x>0)
        if (grayscale[start.y][start.x-1]==255) {
            searcher = new Point(start.x-1,start.y);
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
