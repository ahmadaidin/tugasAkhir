package processor;

import java.awt.*;
import java.awt.geom.Point2D;

public class ColorSpaceFinder {
    public static boolean isBorderMatch(double value, double bottom, double top){
        return ( value>=bottom && value<=top);
    }

    public static MyImage maskByYCbCr(MyImage image, double lowYValue, double highYValue, double lowCbValue, double highCbValue, double lowCrValue, double highCrValue){
        MyImage result = new MyImage(image.getData());
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                int rgb=image.getRGB(x,y);
                double Y = MyImage.RGBtoY(rgb);
                int newRGB = rgb;
                if (isBorderMatch(Y,lowYValue,highYValue)){
                    double Cb = MyImage.RGBtoCb(rgb);
                    if(isBorderMatch(Cb,lowCbValue,highCbValue)){
                        double Cr = MyImage.RGBtoCr(rgb);
                        if(isBorderMatch(Cr,lowCrValue,highCrValue)){
                            newRGB = MyImage.makeRGB(255,255,255);
                        }
                    }
                }
                image.setRGB(x,y,newRGB);
            }
        }
        return result;
    }

    public static MyImage maskByHSV(MyImage image, double lowHValue, double highHValue, double lowSValue, double highSValue, double lowVValue, double highVValue){
        MyImage result = new MyImage(image.getData());
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                int rgb=image.getRGB(x,y);
                double H = MyImage.RGBtoH(rgb);
                int newRGB = rgb;
                if (isBorderMatch(H,lowHValue,highHValue)){
                    double S = MyImage.RGBtoS(rgb);
                    if(isBorderMatch(S,lowSValue,highSValue)){
                        double V = MyImage.RGBtoV(rgb);
                        if(isBorderMatch(V,lowVValue,highVValue)){
                            newRGB = MyImage.makeRGB(255,255,255);
                        }
                    }
                }
                image.setRGB(x,y,newRGB);
            }
        }
        return result;
    }

    public static MyImage maskByRGB(MyImage image, int lowRValue, int highRValue, int lowGValue, int highGValue, int lowBValue, int highBValue){
        MyImage result = new MyImage(image.getData());
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                int rgb=image.getRGB(x,y);
                int R = MyImage.getR(rgb);
                int newRGB = rgb;
                if (isBorderMatch(R,lowRValue,highRValue)){
                    int G = MyImage.getG(rgb);
                    if(isBorderMatch(G,lowGValue,highGValue)){
                        int B = MyImage.getB(rgb);
                        if(isBorderMatch(B,lowBValue,highBValue)){
                            newRGB = MyImage.makeRGB(255,255,255);
                        }
                    }
                }
                image.setRGB(x,y,newRGB);
            }
        }
        return result;
    }

    public static Point getRBorder(MyImage image){
        int rgb = image.getRGB(0,0);
        int min = MyImage.getR(rgb);
        int max = MyImage.getR(rgb);
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                rgb = image.getRGB(x,y);
                int R = MyImage.getR(rgb);
                if(R<min) min = R; else
                if(R>max) max = R;
            }
        }
        return new Point(min,max);
    }

    public static Point getGBorder(MyImage image){
        int rgb = image.getRGB(0,0);
        int min = MyImage.getG(rgb);
        int max = MyImage.getG(rgb);
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                rgb = image.getRGB(x,y);
                int G = MyImage.getG(rgb);
                if(G<min) min = G; else
                if(G>max) max = G;
            }
        }
        return new Point(min,max);
    }

    public static Point getBBorder(MyImage image){
        int rgb = image.getRGB(0,0);
        int min = MyImage.getB(rgb);
        int max = MyImage.getB(rgb);
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                rgb = image.getRGB(x,y);
                int B = MyImage.getB(rgb);
                if(B<min) min = B; else
                if(B>max) max = B;
            }
        }
        return new Point(min,max);
    }

    public static Point2D.Double getYBorder(MyImage image){
        int rgb = image.getRGB(0,0);
        double min = MyImage.RGBtoY(rgb);
        double max = MyImage.RGBtoY(rgb);
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                rgb = image.getRGB(x,y);
                double Y = MyImage.RGBtoY(rgb);
                if(Y<min) min = Y; else
                if(Y>max) max = Y;
            }
        }
        return new Point2D.Double(min,max);
    }

    public static Point2D.Double getCbBorder(MyImage image){
        int rgb = image.getRGB(0,0);
        double min = MyImage.RGBtoCb(rgb);
        double max = MyImage.RGBtoCb(rgb);
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                rgb = image.getRGB(x,y);
                double Cb = MyImage.RGBtoCb(rgb);
                if(Cb<min) min = Cb; else
                if(Cb>max) max = Cb;
            }
        }
        return new Point2D.Double(min,max);
    }

    public static Point2D.Double getCrBorder(MyImage image){
        int rgb = image.getRGB(0,0);
        double min = MyImage.RGBtoCr(rgb);
        double max = MyImage.RGBtoCr(rgb);
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                rgb = image.getRGB(x,y);
                double Cr = MyImage.RGBtoY(rgb);
                if(Cr<min) min = Cr; else
                if(Cr>max) max = Cr;
            }
        }
        return new Point2D.Double(min,max);
    }


    public static Point2D.Double getHBorder(MyImage image){
        int rgb = image.getRGB(0,0);
        double min = MyImage.RGBtoH(rgb);
        double max = MyImage.RGBtoH(rgb);
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                rgb = image.getRGB(x,y);
                double H = MyImage.RGBtoH(rgb);
                if(H<min) min = H; else
                if(H>max) max = H;
            }
        }
        return new Point2D.Double(min,max);
    }

    public static Point2D.Double getSBorder(MyImage image){
        int rgb = image.getRGB(0,0);
        double min = MyImage.RGBtoS(rgb);
        double max = MyImage.RGBtoS(rgb);
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                rgb = image.getRGB(x,y);
                double S = MyImage.RGBtoS(rgb);
                if(S<min) min = S; else
                if(S>max) max = S;
            }
        }
        return new Point2D.Double(min,max);
    }

    public static Point2D.Double getVBorder(MyImage image){
        int rgb = image.getRGB(0,0);
        double min = MyImage.RGBtoV(rgb);
        double max = MyImage.RGBtoV(rgb);
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                rgb = image.getRGB(x,y);
                double V = MyImage.RGBtoV(rgb);
                if(V<min) min = V; else
                if(V>max) max = V;
            }
        }
        return new Point2D.Double(min,max);
    }
}
