package processor;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 17/10/2016.
 */

public class BinaryConverter {

    public static MyImage convertImage(MyImage image, int threshold){
        MyImage result = new MyImage(image.getHeight(),image.getWidth());
        for(int y = 0; y<image.getHeight();y++) {
            for(int x = 0; x<image.getWidth();x++) {
                int val;
                int rgb = image.getRGB(x,y);
                if(MyImage.getGraylevel(rgb)<threshold) {
                    val = 0;
                } else {
                    val = 255;
                }
                int color = MyImage.makeRGB(val,val,val);
                result.setRGB(x,y,color);
            }
        }
        return result;
    }

    public static MyImage convertImageY(MyImage image, int threshold){
        MyImage result = new MyImage(image.getHeight(),image.getWidth());
        for(int y = 0; y<image.getHeight();y++) {
            for(int x = 0; x<image.getWidth();x++) {
                int val;
                int rgb = image.getRGB(x,y);
                int Y = (int) (MyImage.RGBtoY(rgb)*100);
                if(Y<threshold) {
                    val = 0;
                } else {
                    val = 255;
                }
                int color = MyImage.makeRGB(val,val,val);
                result.setRGB(x,y,color);
            }
        }
        return result;
    }

    public static MyImage convertImageRed(MyImage image, int threshold){
        MyImage result = new MyImage(image.getHeight(),image.getWidth());
        for(int y = 0; y<image.getHeight();y++) {
            for(int x = 0; x<image.getWidth();x++) {
                int val;
                int rgb = image.getRGB(x,y);
                if(MyImage.getR(rgb)<threshold) {
                    val = 0;
                } else {
                    val = 255;
                }
                int color = MyImage.makeRGB(val,val,val);
                result.setRGB(x,y,color);
            }
        }
        return result;
    }

    public static MyImage convertImageInvers(MyImage grayscaleImg, int threshold){
        MyImage result = new MyImage(grayscaleImg.getHeight(),grayscaleImg.getWidth());
        for(int y = 0; y<grayscaleImg.getHeight();y++) {
            for(int x = 0; x<grayscaleImg.getWidth();x++) {
                int val;
                int rgb = grayscaleImg.getRGB(x,y);
                if(MyImage.getGraylevel(rgb)>=threshold) {
                    val = 0;
                } else {
                    val = 255;
                }

                int color = MyImage.makeRGB(val,val,val);
                result.setRGB(x,y,color);
            }
        }
        return result;
    }


}
