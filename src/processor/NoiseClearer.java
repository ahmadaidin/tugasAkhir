package processor;

import java.awt.*;
import java.util.ArrayList;

public class NoiseClearer {
    public int bgColor;
    public int fgColor;
    public int threshold;
    public MyImage image;
    public MyImage originalImage;


    public NoiseClearer(MyImage image, MyImage originalImage) {
        this.bgColor = MyImage.makeRGB(0,255,0);
        this.fgColor = MyImage.makeRGB(0,0,255);
        this.image = new MyImage(image);
        this.originalImage = new MyImage(originalImage);
    }

    public MyImage clear(){
        MyImage result= ImgFilterer.getGrayImage(image);

        result = BinaryConverter.convertImage(result, 255);
        result = EdgeDetector.robertOperatorDetection(result);
//        int[] histogram = HistogramProcessor.getGreylevelHistogram(result);
//        int size = result.getWidth() * result.getHeight();
//        int threshold = OtsuBinaryConverter.countThreshold(size, histogram);
//        result = OtsuBinaryConverter.convertImage(result, threshold);
//        result = ZhangSuenSkeletonizer.skeletonize(result);


//        RegionSegmenter regionSegmenter = new RegionSegmenter(result);
//        ArrayList<ArrayList<ArrayList<Point>>> parts = regionSegmenter.getAllPartRegion();

        result = ZhangSuenSkeletonizer.skeletonize(result);
        ChainCodeExtractor chainCodeExtractor = new ChainCodeExtractor(result);
        ArrayList<ChainCode> chainCodes = chainCodeExtractor.findSegments();
        System.out.println("objek= "+ chainCodes.size());
        int max = -1;
        int idxMax = -1;
        for(int i = 0; i < chainCodes.size(); i++) {
            if(chainCodes.get(i).pixelPosSize()>max){
                max = chainCodes.get(i).pixelPosSize();
                idxMax = i;
            }
        }
        if(max != -1){
            ChainCode chainCode = chainCodes.get(idxMax);
            ArrayList<Point> points = chainCode.getPixelPos();

//            ArrayList<ArrayList<Point>> teethRegions = getRegionBorderIntersection(parts,points);

            for(int y = 0; y < result.getHeight(); y++) {
                for(int x = 0; x < result.getWidth(); x++) {
                    result.setRGB(x,y,bgColor);
                }
            }

            for(int i = 0; i < points.size(); i++) {
                result.setRGB(points.get(i).x, points.get(i).y,fgColor);
            }

            floodFill(result);
            for(int y = 0; y < result.getHeight(); y++) {
                for(int x = 0; x < result.getWidth(); x++) {
                    if(!result.isHasColor(x,y,fgColor)) {
                        int rgb = originalImage.getRGB(x,y);
                        result.setRGB(x, y, rgb);
                    }
                }
            }


//            for(int i=0; i<teethRegions.size();i++){
//                for(int j=0; j<teethRegions.get(i).size();j++){
//                    Point point = teethRegions.get(i).get(j);
//                    int color = originalImage.getRGB(point.x, point.y);
//                    result.setRGB(point.x, point.y,color);
//                }
//            }
        }

        return result;
    }


    public ArrayList<ArrayList<Point>> getRegionBorderIntersection(ArrayList<ArrayList<ArrayList<Point>>> parts, ArrayList<Point> border){
        ArrayList<ArrayList<Point>> result = new ArrayList<>();
        for(int p = 0; p<parts.size(); p++){
            ArrayList<ArrayList<Point>> part = parts.get(p);
            for(int r = 0; r<part.size(); r++){
                ArrayList<Point> region = part.get(r);
                boolean isHasIntersection = isHasIntersection(region,border);
                if(isHasIntersection){
                    result.add(region);
                }
            }
        }


        return result;
    }

    public boolean isHasIntersection(ArrayList<Point> region, ArrayList<Point> border){
        boolean found = false;
        int rIdx=0;
        int bIdx=0;

        while(!found && rIdx<region.size()){
            while(!found && bIdx<border.size()){
                int xDiv = Math.abs(border.get(bIdx).x - region.get(rIdx).x);
                int yDiv = Math.abs(border.get(bIdx).y - region.get(rIdx).y);
                if(xDiv<=1 && yDiv<=1)
                    found=true;
                bIdx++;
            }
            rIdx++;
        }
        return found;
    }

    public void floodFill(MyImage image){
        ArrayList<Point> activePoint = new ArrayList<>();
        activePoint.add(new Point(0,0));
        while(activePoint.size()>0) {
            Point p = activePoint.remove(activePoint.size()-1);
            ArrayList<Point> adj = findAdj(image,p);
            for(int i=0; i<adj.size(); i++){
                Point pxl = adj.get(i);
                image.setRGB(pxl.x, pxl.y, fgColor);
                activePoint.add(pxl);
            }
        }
    }

    public ArrayList<Point> findAdj(MyImage image, Point start){
        ArrayList<Point> adj = new ArrayList<>();
        int leftBorder= 0;
        int rightBorder = image.getWidth()-1;

        Point searcher;
//        if(start.y>0 && start.x>leftBorder)
//            if(image.isHasColor(start.x-1,start.y-1,bgColor)){
//                searcher = new Point(start.x-1,start.y-1);
//                adj.add(searcher);
//            }

        if(start.y>0)
            if (image.isHasColor(start.x,start.y-1,bgColor)) {
                searcher = new Point(start.x,start.y-1);
                adj.add(searcher);
            }

//        if(start.y>0 && start.x<rightBorder)
//            if (image.isHasColor(start.x+1,start.y-1,bgColor)) {
//                searcher = new Point(start.x+1,start.y-1);
//                adj.add(searcher);
//            }

        if(start.x<rightBorder)
            if (image.isHasColor(start.x+1,start.y,bgColor)) {
                searcher = new Point(start.x+1,start.y);
                adj.add(searcher);
            }

//        if(start.y<image.getHeight()-1 && start.x < rightBorder)
//            if (image.isHasColor(start.x+1,start.y+1,bgColor)) {
//                searcher = new Point(start.x+1,start.y+1);
//                adj.add(searcher);
//            }

        if(start.y<image.getHeight()-1)
            if (image.isHasColor(start.x,start.y+1,bgColor)) {
                searcher = new Point(start.x,start.y+1);
                adj.add(searcher);
            }

//        if(start.y<image.getHeight()-1 && start.x>leftBorder)
//            if (image.isHasColor(start.x-1,start.y+1,bgColor)) {
//                searcher = new Point(start.x-1,start.y+1);
//                adj.add(searcher);
//            }

        if(start.x>leftBorder)
            if (image.isHasColor(start.x-1,start.y,bgColor)) {
                searcher = new Point(start.x-1,start.y);
                adj.add(searcher);
            }
        return adj;
    }


}
