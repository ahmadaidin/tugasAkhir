package processor;

import java.util.Arrays;

import static processor.ImgFilterer.*;

public class EdgeDetector {

    public static MyImage robertOperatorDetection(MyImage img){
        double[][] operator = {
                {0,-1},
                {1,0}
        };
        return doubleConvolutionFilter(operator,img);
    }

    public static MyImage prewittOperatorDetection(MyImage img) {
        double[][] operator = {
                {1,1,1},
                {0,0,0},
                {-1,-1,-1}
        };
        return doubleConvolutionFilter(operator,img);
    }

    public static MyImage sobelOperatorDetection(MyImage img) {
        double[][] operator = {
                {1,2,1},
                {0,0,0},
                {-1,-2,-1}
        };
        return doubleConvolutionFilter(operator,img);
    }

    public static MyImage freiChenOperatorDetection(MyImage img) {
        double[][] operator = {
                {-1,-1.41421356237,-1},
                {0,0,0},
                {1,1.41421356237,1}
        };
        return doubleConvolutionFilter(operator,img);
    }

    public static MyImage robinson3LvCompassDetection(MyImage img) {
        double[][] operator = {
                {-1,0,1},
                {-1,0,1},
                {-1,0,1}
        };
        return compassConvolutionFilter(operator,img);
    }

    public static MyImage robinson5LvCompassDetection(MyImage img) {
        double[][] operator = {
                {-1,0,1},
                {-2,0,2},
                {-1,0,1}
        };
        return compassConvolutionFilter(operator,img);
    }


    public static MyImage krischCompassDetection(MyImage img) {
        double[][] operator = {
                {-3,-3,5},
                {-3,0,5},
                {-3,-3,5}
        };
        return compassConvolutionFilter(operator,img);
    }

    public static MyImage prewittCompassDetection(MyImage img) {
        double[][] operator = {
                {1,0,-1},
                {1,0,-1},
                {1,0,-1}
        };
        return compassConvolutionFilter(operator,img);
    }

}
