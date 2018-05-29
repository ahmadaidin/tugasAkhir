package processor;

import java.awt.*;

public class HistogramProcessor {

    public static int[] getGreylevelHistogram(MyImage img) {
        int [] grayHistogram = new int[256];
        for(int i=0; i<grayHistogram.length; i++) grayHistogram[i]=0;

        for(int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x<img.getWidth(); x++) {
                int rgb = img.getRGB(x,y);
                int gray = MyImage.getGraylevel(rgb);
                grayHistogram[gray]++;
            }
        }
        return grayHistogram;
    }

    public static int[] getRedChannelHistogram(MyImage img) {
        int [] grayHistogram = new int[256];
        for(int i=0; i<grayHistogram.length; i++) grayHistogram[i]=0;

        for(int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x<img.getWidth(); x++) {
                int rgb = img.getRGB(x,y);
                int red = MyImage.getR(rgb);
                grayHistogram[red]++;
            }
        }
        return grayHistogram;
    }

    public static int[] getYChannelHistogram(MyImage image) {
        int [] YHistogram = new int[101];
        for(int i=0; i<YHistogram.length; i++) YHistogram[i]=0;

        for(int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x<image.getWidth(); x++) {
                int rgb = image.getRGB(x,y);
                int Y = (int) (MyImage.RGBtoY(rgb)*100);
                YHistogram[Y]++;
            }
        }
        return YHistogram;
    }


    public static int[] getGraylevelCDF(int[] grayHistogram){
        int[] grayCDF = new int[grayHistogram.length];
        grayCDF[0] = grayHistogram[0];
        for (int i = 1; i<grayHistogram.length;i++) {
            grayCDF[i] = grayCDF[i-1]+grayHistogram[i];
        }
        return grayCDF;
    }

    public static int[] getGrayHistogramEqualizationLUT(int height, int width, int[] grayHistogram) {
        int [] imageLUT = new int[grayHistogram.length];

        int[] graylevelCDF = getGraylevelCDF(grayHistogram);

        int cdfMin = graylevelCDF[0];

        long numPixel = width * height;

        float denumerator = numPixel-cdfMin;
        for(int i=0; i<graylevelCDF.length;i++){
            float numerator = (graylevelCDF[i] - cdfMin);
            int val = Math.round((numerator/denumerator)*(256-1));
            imageLUT[i]=val;
        }
        return imageLUT;
    }

    public static MyImage histogramEqualization(MyImage grayImg){
        int[] grayhisto = getGreylevelHistogram(grayImg);

        MyImage result = new MyImage(grayImg.getHeight(),grayImg.getWidth());

        int[] histLUT = getGrayHistogramEqualizationLUT(grayImg.getHeight(),grayImg.getWidth(),grayhisto);

        for(int y=0; y<grayImg.getHeight(); y++){
            for(int x=0; x<grayImg.getWidth(); x++){
                int oldGray = MyImage.getGraylevel(grayImg.getRGB(x,y));
                int newGray = histLUT[oldGray];
                Color c = new Color(newGray, newGray, newGray);
                int rgb = c.getRGB();
                result.setRGB(x, y, rgb);
            }
        }
        return result;
    }
}
