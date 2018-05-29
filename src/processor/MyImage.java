package processor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author toshiba
 * @improved_by Aidin
 */
public class MyImage {
    BufferedImage img;
    
    public MyImage(BufferedImage image) {
        img = image;
    }
            
    public MyImage(MyImage image) {
        img = new BufferedImage(image.getWidth(), image.getHeight(), TYPE_INT_RGB);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                img.setRGB(i, j, image.getRGB(i, j));
            }
        }
    }

    public boolean isHasColor(int x, int y, int createdRGB){
        int imgR= getRed(x,y);
        int crtR= getR(createdRGB);

        int imgG = getGreen(x,y);
        int crtG = getG(createdRGB);

        int imgB = getBlue(x,y);
        int crtB = getB(createdRGB);

        return (imgR==crtR && imgG == crtG && imgB == crtB);
    }
    
//    public Image(Image image, Square s) {
//        img = new BufferedImage(s.xmax - s.xmin + 1, s.ymax - s.ymin + 1, TYPE_INT_RGB);
//        for (int i = 0; i < this.getWidth(); i++) {
//            for (int j = 0; j < this.getHeight(); j++) {
//                img.setRGB(i, j, image.getRGB(i + s.xmin, j + s.ymin));
//            }
//        }
//    }


    public MyImage(String filepath){
        int width = 963;    //width of the image
        int height = 640;   //height of the image

        img = null;
        File f;
        try{
            f = new File(filepath); //image file path
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            img = ImageIO.read(f);
            System.out.println("Reading complete.");
        }catch(IOException e){
            System.out.println("Error: "+e);
        }
    }
    
    public MyImage(int height, int width) {
        img = new BufferedImage(width, height, TYPE_INT_RGB);
    }
    
    public BufferedImage getData() {
        return img;
    }
    
    public int getHeight() {
        return img.getHeight();
    }
    
    public int getWidth() {
        return img.getWidth();
    }
    
    public int getRGB(int i, int j) {
        return img.getRGB(i, j);
    }

    public int getRed(int x, int y){
        return new Color(img.getRGB(x, y)).getRed();
    }

    public int getGreen(int x, int y){
        return new Color(img.getRGB(x, y)).getGreen();
    }

    public int getBlue(int x, int y){
        return new Color(img.getRGB(x, y)).getBlue();
    }
    
    public void setRGB(int i, int j, int c) {
        img.setRGB(i, j, c);
    }
    
    public static int getR(int rgb) {
        return (rgb >> 16) & 0xFF;                
    }
    
    public static int getG(int rgb) {
        return (rgb >> 8) & 0xFF;
    }
    
    public static int getB(int rgb) {
        return rgb & 0xFF;
    }
    
    public static int makeRGB(int r, int g, int b) {
        int rgb = r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;
        return rgb;
    }
    
    public static int convertGrayLevel(int rgb) {
        int gray = convertRGB(rgb, 0.2126,0.7152,0.0722);
        return makeRGB(gray,gray,gray);
    }

    public static int getGraylevel(int rgb){
        return (int) Math.round(0.3*getB(rgb)+0.59*getG(rgb)+0.11*getR(rgb));
    }
    
    public static int convertRGB(int rgb, double a, double b, double c) {
        return (int) (a*getR(rgb) + b*getG(rgb) + c*getB(rgb));
    }

    public static int manipulateRGB(int rgb, int addition) {
        int result;

        int r = MyImage.getR(rgb) + addition;
        int g = MyImage.getG(rgb) + addition;
        int b = MyImage.getB(rgb) + addition;

        r = Math.min(Math.max(0, r), 255);
        g = Math.min(Math.max(0, g), 255);
        b = Math.min(Math.max(0, b), 255);

        result = MyImage.makeRGB(r, g, b);
        return result;
    }
    
    public static double RGBtoY(int rgb) {
        double R = MyImage.getR(rgb);
        double G = MyImage.getG(rgb);
        double B = MyImage.getB(rgb);

        double r = R/(R+G+B);
        double g = G/(R+G+B);
        double b = B/(R+G+B);

        return r*0.299f+g*0.587f+b*0.114f;
    }

    public static double RGBtoCb(int rgb) {
        double R = MyImage.getR(rgb);
        double G = MyImage.getG(rgb);
        double B = MyImage.getB(rgb);

        double r = R/(R+G+B);
        double g = G/(R+G+B);
        double b = B/(R+G+B);

        return r*-0.168736f-g*0.331264f+b*0.5f;
    }
    
    public static double RGBtoCr(int rgb) {
        double R = MyImage.getR(rgb);
        double G = MyImage.getG(rgb);
        double B = MyImage.getB(rgb);

        double r = R/(R+G+B);
        double g = G/(R+G+B);
        double b = B/(R+G+B);

        return r*0.5f-g*0.418688f-b*0.081312f;
    }

    public static double RGBtoH(int rgb) {
        double R = MyImage.getR(rgb);
        double G = MyImage.getG(rgb);
        double B = MyImage.getB(rgb);

        double r = R/255;
        double g = G/255;
        double b = B/255;

        double CMax = Math.max(Math.max(r,g),b);
        double CMin = Math.min(Math.min(r,g),b);
        double delta = CMax-CMin;

        if(delta==0) return 0;
        else if (CMax==r) return 60*(Math.floorMod((int)((g-b)/delta),6));
        else if (CMax==g) return 60*((b-r)/delta+2);
        else return 60*((r-g)/delta+4);
    }

    public static double RGBtoS(int rgb) {
        double R = MyImage.getR(rgb);
        double G = MyImage.getG(rgb);
        double B = MyImage.getB(rgb);

        double r = R/255;
        double g = G/255;
        double b = B/255;

        double CMax = Math.max(Math.max(r,g),b);
        double CMin = Math.min(Math.min(r,g),b);
        double delta = CMax-CMin;

        if(CMax==0) return 0;
        else return 100*delta/CMax;
    }

    public static double RGBtoV(int rgb) {
        double R = MyImage.getR(rgb);
        double G = MyImage.getG(rgb);
        double B = MyImage.getB(rgb);

        double r = R/(R+G+B);
        double g = G/(R+G+B);
        double b = B/(R+G+B);

        return 100*Math.max(r,Math.max(g,b));
    }

    public static int HSVtoRGB(double H, double S, double V){
        double C = V*S;
        double X = C*(1-Math.abs((H/60)%2-1));
        double m = V-C;
        double R1,G1,B1;

        if(0<=H && H<60){
            R1=C; G1=X; B1=0;
        } else if(60<=H && H<120){
            R1=X; G1=C; B1=0;
        } else if(120<=H && H<180) {
            R1=0; G1=C; B1=X;
        } else if(180<=H && H<240) {
            R1=0; G1=X; B1=C;
        } else if(240<=H && H<300) {
            R1=X; G1=0; B1=C;
        } else {
            R1=C; G1=0; B1=X;
        }

        int R = (int) (R1+m)*255;
        int G = (int) (G1+m)*255;
        int B = (int) (B1+m)*255;

        return makeRGB(R,G,B);
    }

    public static int YCbCrToRGBJPEG(double Y, double Cb, double Cr){
        int R = (int) (Y + 1.4*(Cr-128));
        int G = (int) (Y - 0.343*(Cb-128) - 0.711*(Cr-128));
        int B = (int) (Y + 1.765*(Cb-128));

        return makeRGB(R,G,B);
    }



//    public Complex[][] toComplex() {
//        Complex[][] ret = new Complex[getWidth()][getHeight()];
//        for (int i = 0; i <getWidth(); i++) {
//            for (int j = 0; j < getHeight(); j++) {
//                ret[i][j] = new Complex(getR(getRGB(i,j)),0);
//            }
//        }
//        return ret;
//    }
//
//    public Complex[] getColumn(int j) {
//        Complex[] temp = new Complex[getHeight()];
//        for (int i = 0; i < getHeight(); i++) {
//            //System.out.println(getHeight() + " : " + j + " | " + getWidth() + " : " + i);
//            temp[i]= new Complex(getR(getRGB(j,i)),0);
//        }
//        return temp;
//    }
//
//    public Complex[] getRow(int i) {
//        Complex[] temp = new Complex[getWidth()];
//        for (int j = 0; j < getWidth(); j++) {
//            temp[j]= new Complex(getR(getRGB(j,i)),0);
//        }
//        return temp;
//    }
    
    public MyImage to2PowerSize() {
        int width = this.getWidth();
        int d = 99999;
        int two = 1;
        //System.out.println(n);
        while ( width - two*2 > 0) {
            //System.out.println("two : " + two + " | d : " + d);
            two *= 2;
            d = width - two;
        }
        width = two*2;
        int height = this.getHeight();
        d = 99999;
        two = 1;
        //System.out.println(n);
        while ( height - two*2 > 0) {
            //System.out.println("two : " + two + " | d : " + d);
            two *= 2;
            d = height - two;
        }
        height = two*2;
        
        MyImage temp = new MyImage(height, width);
        
        int dwidth = (width - this.getWidth())/2;
        int dheight = (height - this.getHeight())/2;
        
        for (int i = dwidth; i < width - dwidth; i++) {                        
            for (int j = dheight; j < height - dheight; j++) {
                temp.setRGB(i, j, this.getRGB(i - dwidth, j - dheight));
            }                         
        }
        
        return temp;
    }
    
    public MyImage cut(int width, int height) {
        MyImage temp = new MyImage(height, width);
        int dw = (this.getWidth() - width) / 2;
        int dh = (this.getHeight() - height) / 2;
        
        for (int i = 0; i < width; i++) {                        
            for (int j = 0; j < height; j++) {
                temp.setRGB(i, j, this.getRGB(i + dw, j + dh));
            }                         
        }
        
        return temp;
    }
    
    public double[][] toDoubleArray() {
        double[][] arr = new double[this.getHeight()][this.getWidth()*2];
        
        int iter = 0;
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                //arr[i*2*this.getHeight()+j*2] = Image.convertGrayLevel(this.getRGB(i, j));
                //arr[i*2*this.getHeight()+j*2+1] = 0;
                arr[i][j*2] = MyImage.convertGrayLevel(this.getRGB(j, i));
                arr[i][j*2+1] = 0;
            }
        }        
        
        return arr;
    }
    public static MyImage fromDoubleArray(double[][] d){
        MyImage image = new MyImage(d.length,d[0].length/2);
        
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                double real = d[i][2*j];
                double imaj = d[i][2*j+1];
                int val = (int) Math.hypot(real, imaj);
                image.setRGB(j, i, MyImage.makeRGB(val, val, val));
            }
        }
        return image;
    }

    public static MyImage mergeImage(MyImage img1, MyImage img2){
        MyImage result = new MyImage(Math.min(img1.getHeight(),img2.getHeight()), img1.getWidth()+img2.getWidth());
        for(int y=0; y<result.getHeight();y++){
            for(int x=0; x<img1.getWidth(); x++){
                result.setRGB(x,y,img1.getRGB(x,y));
            }
            for(int x=0; x<img2.getWidth();x++){
                result.setRGB(img1.getWidth()+x,y,img2.getRGB(x,y));
            }
        }
        return result;
    }

    public void draw(ArrayList<Point> points, int rgb){
        for (Point point : points) {
            setRGB(point.x,point.y,rgb);
        }
    }


}
