package processor;

import java.awt.*;
import java.util.*;

public class RegionSegmenter {
    public static final int VISITED = 200;
    public static final int FG = 255;
    private int[][] grayscale;

    private final int numPart=16;
    private int partSize;



    public RegionSegmenter(MyImage image){
        grayscale = ImgFilterer.getGrayscale(image);
        partSize = grayscale[0].length/numPart;
    }


    public void setVisited(Point p) {
        grayscale[p.y][p.x]=VISITED;
    }

    public ArrayList<ArrayList<ArrayList<Point>>> getAllPartRegion(){
        ArrayList<ArrayList<ArrayList<Point>>> regionParts = new ArrayList<>();
        for(int part=1;part<=numPart;part++){
             ArrayList<ArrayList<Point>> partRegions= findRegionsEachPart(part);
             regionParts.add(partRegions);
        }
        return regionParts;
    }

//    public ArrayList<ArrayList<Point>> mergePart(ArrayList<ArrayList<Point>> part1, ArrayList<ArrayList<Point>> part2,int divider){
//        ArrayList<ArrayList<Point>> result = new ArrayList<>();
//        ArrayList<ArrayList<Point>> mergeCandidate1 = new ArrayList<>();
//        ArrayList<ArrayList<Point>> mergeCandidate2 = new ArrayList<>();
//        ArrayList<Boolean> isVisitedCandidate1 = new ArrayList<>();
//        ArrayList<Boolean> isVisitedCandidate2 = new ArrayList<>();
//
//        for(int i=0;i<part1.size();i++){
//            ArrayList<Point> partOfRegion = getAllPointsWithX(part1.get(i),divider);
//            if(partOfRegion.size()==0){
//                result.add(part1.get(i));
//            } else {
//                mergeCandidate1.add(part1.get(i));
//                isVisitedCandidate1.add(false);
//            }
//        }
//
//        for(int i=0;i<part2.size();i++){
//            ArrayList<Point> partOfRegion = getAllPointsWithX(part2.get(i),divider);
//            if(partOfRegion.size()==0){
//                result.add(part2.get(i));
//            } else {
//                mergeCandidate2.add(part2.get(i));
//                isVisitedCandidate2.add(false);
//            }
//        }
//        DFSMerge(result,mergeCandidate1,mergeCandidate2,isVisitedCandidate1,isVisitedCandidate2,0,0);
//        return result;
//    }
//
//    public void DFSMerge(ArrayList<ArrayList<Point>> result, ArrayList<ArrayList<Point>> candidate1,
//                         ArrayList<ArrayList<Point>> candidate2, ArrayList<Boolean> visitedCandidate1,ArrayList<Boolean> visitedCandidate2, int idx, int part)
//    {
//        if(part==0) {
//            ArrayList<Integer> adj = getAdj(candidate1.get(idx), candidate2,visitedCandidate2);
//            visitedCandidate1.set(idx,true);
//            while (adj.size()>0){
//                adj.remove(adj.size()-1);
//            }
//        }else{
//
//            }
//    }
//
//    ArrayList<Integer> getAdj(ArrayList<Point> region, ArrayList<ArrayList<Point>> mergeCandidate,ArrayList<Boolean> visitedCandidate){
//        ArrayList<Integer> result = new ArrayList<>();
//        for(int i = 0; i<mergeCandidate.size();i++){
//            if(!visitedCandidate.get(i))
//            if(isAdj(region,mergeCandidate.get(i))){
//                result.add(i);
//            }
//        }
//        return result;
//    }
//
//    boolean isAnyPointWithX(ArrayList<Point> points,int x){
//        boolean found = false;
//        int i = 0;
//        while (!found && i<points.size()){
//            if(points.get(i).x==x) {
//                found = true;
//            }
//            i++;
//        }
//        return found;
//    }
//
//    ArrayList<Point> getAllPointsWithX(ArrayList<Point> points,int x){
//        ArrayList<Point> result = new ArrayList<>();
//        for(int i = 0; i < points.size(); i++){
//            if(points.get(i).x==x){
//                result.add((points.get(i)));
//            }
//        }
//        return result;
//    }
//
//    boolean isAdj(ArrayList<Point> partOfRegion1, ArrayList<Point> partOfRegion2){
//        //prekondisi: semua poin di kedua array memiliki titik x yang sama
//        boolean found = false;
//        int i = 0;
//        int j = 0;
//        while(!found && i<partOfRegion1.size()){
//            while(!found && j < partOfRegion2.size()){
//                if(partOfRegion1.get(i).y==partOfRegion2.get(j).y){
//                    found = true;
//                }
//                j++;
//            }
//            i++;
//        }
//        return found;
//    }

    public ArrayList<ArrayList<Point>> findRegionsEachPart(int part) {
        ArrayList<ArrayList<Point>> regions= new ArrayList<>();
        int leftBorder = (part-1)*partSize;
        Point start = new Point(leftBorder,0);
        Point lastStart = findRegionStartPoint(start,part);
        while (!start.equals(lastStart)){
            lastStart.setLocation(start);
            ArrayList<Point> region = new ArrayList<>();
            setVisited(start);
            region.add(start);
            getRegion(start, region, part);
            regions.add(region);
            start = findRegionStartPoint(lastStart,part);
        }
        return regions;
    }

    public void getRegion(Point start, ArrayList<Point> region, int part){
        ArrayList<Point> adj = find4Adj(start, part);
        for(int i=0; i<adj.size();i++){
            setVisited(adj.get(i));
            region.add(adj.get(i));
        }
        while(adj.size()>0){
            Point newStart = adj.remove(adj.size()-1);
            getRegion(newStart,region,part);
        }
    }

    public Point findRegionStartPoint(Point point,int part){
        Point start = new Point(point);
        int leftBorder = (part-1)*partSize;
        int rightBorder = partSize*part;
        while(start.y<grayscale.length){
            start.x++;
            while(start.x<rightBorder){
                if(grayscale[start.y][start.x]==FG) {
                    return start;
                }
                else start.x++;
            }
            start.x = leftBorder-1;
            start.y++;
        }
        return point;
    }

    public ArrayList<Point> find8Adj(Point start,int part){
        ArrayList<Point> adj = new ArrayList<>();
        int leftBorder= (part-1)*partSize;
        int rightBorder = partSize*part-1;

        Point searcher;
        if(start.y>0 && start.x>leftBorder)
            if(grayscale[start.y-1][start.x-1]==FG){
                searcher = new Point(start.x-1,start.y-1);
                adj.add(searcher);
            }

        if(start.y>0)
            if (grayscale[start.y-1][start.x]==FG ) {
                searcher = new Point(start.x,start.y-1);
                adj.add(searcher);
            }

        if(start.y>0 && start.x<rightBorder)
            if (grayscale[start.y-1][start.x+1]==FG ) {
                searcher = new Point(start.x+1,start.y-1);
                adj.add(searcher);
            }

        if(start.x<rightBorder)
            if (grayscale[start.y][start.x+1]==FG ) {
                searcher = new Point(start.x+1,start.y);
                adj.add(searcher);
            }

        if(start.y<grayscale.length-1 && start.x < rightBorder)
            if (grayscale[start.y+1][start.x+1]==FG) {
                searcher = new Point(start.x+1,start.y+1);
                adj.add(searcher);
            }

        if(start.y<grayscale.length-1)
            if (grayscale[start.y+1][start.x]==FG) {
                searcher = new Point(start.x,start.y+1);
                adj.add(searcher);
            }

        if(start.y<grayscale.length-1 && start.x>leftBorder)
            if (grayscale[start.y+1][start.x-1]==FG ) {
                searcher = new Point(start.x-1,start.y+1);
                adj.add(searcher);
            }

        if(start.x>leftBorder)
            if (grayscale[start.y][start.x-1]==FG) {
                searcher = new Point(start.x-1,start.y);
                adj.add(searcher);
            }
        return adj;
    }

    public ArrayList<Point> find4Adj(Point start, int part){
        ArrayList<Point> adj = new ArrayList<>();
        int leftBorder= (part-1)*partSize;
        int rightBorder = partSize*part-1;

        Point searcher;

        if(start.y>0)
            if (grayscale[start.y-1][start.x]==FG ) {
                searcher = new Point(start.x,start.y-1);
                adj.add(searcher);
            }

        if(start.x<rightBorder)
            if (grayscale[start.y][start.x+1]==FG ) {
                searcher = new Point(start.x+1,start.y);
                adj.add(searcher);
            }


        if(start.y<grayscale.length-1)
            if (grayscale[start.y+1][start.x]==FG) {
                searcher = new Point(start.x,start.y+1);
                adj.add(searcher);
            }


        if(start.x>leftBorder)
            if (grayscale[start.y][start.x-1]==FG) {
                searcher = new Point(start.x-1,start.y);
                adj.add(searcher);
            }
        return adj;
    }
}
