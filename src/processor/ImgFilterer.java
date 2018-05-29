package processor;

import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;

public class ImgFilterer {

    public static int[][] getGrayscale(MyImage img){
        int[][] result = new int[img.getHeight()][img.getWidth()];
        for (int y = 0; y<img.getHeight(); y++){
            for(int x= 0 ; x<img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                int gray = MyImage.getGraylevel(rgb);
                result[y][x]=gray;
            }
        }
        return  result;
    }

    public static MyImage getGrayImage(MyImage img) {
        MyImage result = new MyImage(img.getHeight(), img.getWidth());
        for (int y = 0; y<img.getHeight(); y++){
            for(int x= 0 ; x<img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                int gray = MyImage.getGraylevel(rgb);
                Color c = new Color(gray, gray, gray);
                rgb = c.getRGB();
                result.setRGB(x, y, rgb);
            }
        }
        return result;
    }

    public static void swap(int[] array, int a, int b) {
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    // selects the k-th largest element from the data between start and end (end exclusive)
    public static int selectKth(int[] data, int s, int e, int k){
        // 5 or less elements: do a small insertion sort
        if(e - s <= 5){
            for(int i = s + 1; i < e; i++)
                for(int j = i; j > 0 && data[j - 1] > data[j]; j--)
                    swap(data, j,j - 1);
            return s + k;
        }

        int p = (s + e) / 2; // choose simply center element as pivot

        // partition around pivot into smaller and larger elements
        swap(data, p, e - 1); // temporarily move pivot to the end
        int j = s;  // new pivot location to be calculated
        for(int i = s; i + 1 < e; i++)
            if(data[i] < data[e - 1])
                swap(data, i, j++);
        swap(data,j, e - 1);

        // recurse into the applicable partition
        if(k == j - s) return s + k;
        else if(k < j - s) return selectKth(data, s, j, k);
        else return selectKth(data, j + 1, e, k - j + s - 1); // subtract amount of smaller elements from k
    }

    public static MyImage medianFilter(int filterWidth,int filterHeight ,MyImage img){
        int filterSize = filterHeight * filterWidth;
        int[] red = new int [filterSize];
        int[] green = new int [filterSize];
        int[] blue = new int [filterSize];

        MyImage result= new MyImage(img.getHeight(),img.getWidth());

        for(int y = 1; y<img.getHeight()-1; y++){
            for(int x = 1; x<img.getWidth()-1; x++){
                int n = 0;
                //set the color values in the arrays
                for(int filterY = 0; filterY < filterHeight; filterY++)
                    for(int filterX = 0; filterX < filterWidth; filterX++)
                    {
                        int imageX = (x - filterWidth/ 2 + filterX + img.getWidth()) % img.getWidth();
                        int imageY = (y - filterHeight/ 2 + filterY + img.getHeight()) % img.getHeight();
                        red[n] = MyImage.getR(img.getRGB(imageX,imageY));
                        green[n] = MyImage.getG(img.getRGB(imageX,imageY));
                        blue[n] = MyImage.getB(img.getRGB(imageX,imageY));
                        n++;
                    }

                int r = red[selectKth(red, 0, filterSize, filterSize / 2)];
                int g = green[selectKth(green, 0, filterSize, filterSize / 2)];
                int b = blue[selectKth(blue, 0, filterSize, filterSize / 2)];

                int rgb = MyImage.makeRGB(r,g,b);
                result.setRGB(x,y,rgb);
            }
        }
        return result;
    }

    public static MyImage filter(double [][] filter, MyImage img, double factor, double bias) {
        int w = 0, h = 0;
        MyImage result = new MyImage(img.getHeight(), img.getWidth());
        //set up the screen

        //apply the filter
        for (int y = 0; y < img.getHeight(); y++)
            for (int x = 0; x < img.getWidth(); x++) {
                double red = 0.0, green = 0.0, blue = 0.0;

                //multiply every value of the filter with corresponding image pixel
                for (int filterY = 0; filterY < filter.length; filterY++)
                    for (int filterX = 0; filterX < filter.length; filterX++) {
                        int imageX = (x - filter.length / 2 + filterX + img.getWidth()) % img.getWidth();
                        int imageY = (y - filter.length / 2 + filterY + img.getHeight()) % img.getHeight();
                        red += MyImage.getR(img.getRGB(imageX, imageY)) * filter[filterY][filterX];
                        green += MyImage.getG(img.getRGB(imageX, imageY)) * filter[filterY][filterX];
                        blue += MyImage.getB(img.getRGB(imageX, imageY)) * filter[filterY][filterX];
                    }

                //truncate values smaller than zero and larger than 255
                int r = Math.min(Math.max((int) (red / factor+ bias), 0), 255);
                int g = Math.min(Math.max((int) (green / factor+ bias), 0), 255);
                int b = Math.min(Math.max((int) (blue / factor + bias), 0), 255);

                int rgb = MyImage.makeRGB(r, g, b);
                result.setRGB(x, y, rgb);
            }
        return result;
    }

    public static MyImage grayFilter(double [][] filter, MyImage img, double factor, double bias) {
        int w = 0, h = 0;
        MyImage result = new MyImage(img.getHeight(), img.getWidth());
        //set up the screen

        //apply the filter
        for (int y = 0; y < img.getHeight(); y++)
            for (int x = 0; x < img.getWidth(); x++) {
                double gray = 0.0;
                //multiply every value of the filter with corresponding image pixel
                for (int filterY = 0; filterY < filter.length; filterY++)
                    for (int filterX = 0; filterX < filter.length; filterX++) {
                        int imageX = (x - filter.length / 2 + filterX + img.getWidth()) % img.getWidth();
                        int imageY = (y - filter.length / 2 + filterY + img.getHeight()) % img.getHeight();
                        int rgb = img.getRGB(imageX,imageY);
                        gray += MyImage.getGraylevel(rgb)*filter[filterY][filterX];
                    }

                //truncate values smaller than zero and larger than 255
                int newGray = Math.min(Math.max((int) (gray / factor+ bias), 0), 255);
                int rgb = MyImage.makeRGB(newGray, newGray, newGray);
                result.setRGB(x, y, rgb);
            }
        return result;
    }

    public static MyImage doubleConvolutionFilter(double [][] filter, MyImage img) {
        double[][] filter2 = MatrixOperator.rotateRight(filter);
        MyImage result = new MyImage(img.getHeight(), img.getWidth());
        //set up the screen

        //apply the filter
        for (int y = 0; y < img.getHeight(); y++)
            for (int x = 0; x < img.getWidth(); x++) {
                double grayV = 0.0;
                double grayH = 0.0;
                //multiply every value of the filter with corresponding image pixel
                for (int filterY = 0; filterY < filter.length; filterY++)
                    for (int filterX = 0; filterX < filter.length; filterX++) {
                        int imageX = (x - filter.length / 2 + filterX + img.getWidth()) % img.getWidth();
                        int imageY = (y - filter.length / 2 + filterY + img.getHeight()) % img.getHeight();
                        int rgb = img.getRGB(imageX,imageY);
                        grayV += MyImage.getGraylevel(rgb)*filter[filterY][filterX];
                        grayH += MyImage.getGraylevel(rgb)*filter2[filterY][filterX];
                    }
                double gray = Math.sqrt(Math.pow(grayH,2)+Math.pow(grayV,2));
                //truncate values smaller than zero and larger than 255
                int newGray = Math.min(Math.max((int) gray, 0), 255);
                int rgb = MyImage.makeRGB(newGray, newGray, newGray);
                result.setRGB(x, y, rgb);
            }
        return result;
    }


    public static MyImage compassConvolutionFilter(double[][] filterMatrix, MyImage image)
    {
        MyImage result = new MyImage(image.getHeight(),image.getWidth());

        double grayCompass;
        int filterWidth = filterMatrix.length;


        int filterOffset = (filterWidth-1) / 2;

        double gray;

        double[][][] allFilters = new double[8][filterMatrix.length][filterMatrix.length];
        allFilters[0] = filterMatrix;

        for(int i = 1; i < allFilters.length; i++) {
            allFilters[i] = MatrixOperator.rotateHalfRight(allFilters[i-1]);
        }

        for (int offsetY = filterOffset; offsetY < image.getHeight() - filterOffset; offsetY++) {
            for (int offsetX = filterOffset; offsetX < image.getWidth() - filterOffset; offsetX++) {
                gray =0;

                for (int compass = 0; compass < allFilters.length; compass++) {
                    grayCompass = 0.0;

                    for (int filterY = -filterOffset; filterY <= filterOffset; filterY++) {
                        for (int filterX = -filterOffset; filterX <= filterOffset; filterX++) {
                            int imageX = (offsetX + filterX + image.getWidth()) % image.getWidth();
                            int imageY = (offsetY + filterY + image.getHeight()) % image.getHeight();

                            double grayLevel = MyImage.getGraylevel(image.getRGB(imageX,imageY));
                            grayCompass+= grayLevel * allFilters[compass][filterY + filterOffset][filterX + filterOffset];
                        }
                    }
                    gray = (grayCompass> gray? grayCompass: gray);
                }

                if(gray > 255)
                { gray = 255; }
                else if(gray< 0)
                { gray = 0; }

                int rgb = MyImage.makeRGB((int)gray,(int)gray,(int)gray);
                result.setRGB(offsetX,offsetY,rgb);
            }
        }
        return result;
    }

    public static MyImage smoothen(MyImage img){
        double[][] mask = {
            {0.11111111,0.11111111,0.11111111},
            {0.11111111,0.11111111,0.11111111},
            {0.11111111,0.11111111,0.11111111}
        };
        return filter(mask,img, 1,0);
    }

    public static MyImage sharpen(MyImage img){
        double[][] mask = {
            {-1,-1,-1},
            {-1,9,-1},
            {-1,-1,-1}
        };
        return filter(mask,img,1,0);
    }

    public static MyImage blur(MyImage img){
//        double[][] mask = {
//                {0, 0, 1, 0, 0},
//                {0, 1, 1, 1, 0},
//                {1, 1, 1, 1, 1},
//                {0, 1, 1, 1, 0},
//                {0, 0, 1, 0, 0},
//        };

        double[][] mask = {
                {2, 4, 5, 4, 2},
                {4, 9, 12, 9, 4},
                {5, 12, 15, 12, 5},
                {4, 9, 12, 9, 4},
                {2, 4, 5, 4, 2},
        };
        return filter(mask,img,159,0);
    }

    public static MyImage getRGBChannel(MyImage image, String channel){
        MyImage result = new MyImage(image);
        if(channel.equals("R")) {
            for (int y = 0; y < result.getHeight(); y++) {
                for (int x = 0; x < result.getWidth(); x++) {
                    int R = MyImage.getR(result.getRGB(x, y));
                    result.setRGB(x, y, MyImage.makeRGB(R, 0, 0));
                }
            }
        } else if(channel.equals("G")) {
            for (int y = 0; y < result.getHeight(); y++) {
                for (int x = 0; x < result.getWidth(); x++) {
                    int G = MyImage.getG(result.getRGB(x, y));
                    result.setRGB(x, y, MyImage.makeRGB(0, G, 0));
                }
            }
        } else if(channel.equals("B")) {
            for (int y = 0; y < result.getHeight(); y++) {
                for (int x = 0; x < result.getWidth(); x++) {
                    int B = MyImage.getB(result.getRGB(x, y));
                    result.setRGB(x, y, MyImage.makeRGB(0, 0, B));
                }
            }
        }
        return result;
    }

    public static MyImage getYCbCrChannel(MyImage image, String channel){
        MyImage result = new MyImage(image);
        if(channel.equals("Y")) {
            for (int y = 0; y < result.getHeight(); y++) {
                for (int x = 0; x < result.getWidth(); x++) {
                    double Y = MyImage.RGBtoY(result.getRGB(x, y));
                    result.setRGB(x, y, MyImage.YCbCrToRGBJPEG(Y, 0, 0));
                }
            }
        } else if(channel.equals("Cb")) {
            for (int y = 0; y < result.getHeight(); y++) {
                for (int x = 0; x < result.getWidth(); x++) {
                    double Cb = MyImage.RGBtoCb(result.getRGB(x, y));
                    result.setRGB(x, y, MyImage.YCbCrToRGBJPEG(0, Cb, 0));
                }
            }
        } else if(channel.equals("Cr")) {
            for (int y = 0; y < result.getHeight(); y++) {
                for (int x = 0; x < result.getWidth(); x++) {
                    double Cr = MyImage.RGBtoCr(result.getRGB(x, y));
                    result.setRGB(x, y, MyImage.YCbCrToRGBJPEG(0, 0, Cr));
                }
            }
        }
        return result;
    }

    public static MyImage getHSVChannel(MyImage image, String channel){
        MyImage result = new MyImage(image);
        if(channel.equals("H")) {
            for (int y = 0; y < result.getHeight(); y++) {
                for (int x = 0; x < result.getWidth(); x++) {
                    double H = MyImage.RGBtoH(result.getRGB(x, y));
                    result.setRGB(x, y, MyImage.HSVtoRGB(H, 0, 0));
                }
            }
        } else if(channel.equals("S")) {
            for (int y = 0; y < result.getHeight(); y++) {
                for (int x = 0; x < result.getWidth(); x++) {
                    double S = MyImage.RGBtoS(result.getRGB(x, y));
                    result.setRGB(x, y, MyImage.HSVtoRGB(0, S, 0));
                }
            }
        } else if(channel.equals("V")) {
            for (int y = 0; y < result.getHeight(); y++) {
                for (int x = 0; x < result.getWidth(); x++) {
                    double V = MyImage.RGBtoV(result.getRGB(x, y));
                    result.setRGB(x, y, MyImage.HSVtoRGB(0, 0, V));
                }
            }
        }
        return result;
    }


}
