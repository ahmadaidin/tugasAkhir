package usingOpenCV;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import processor.MyImage;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Converter {
    public static Mat myImage2Mat(MyImage myImage){
        BufferedImage image = myImage.getData();

        Mat out = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        byte[] data = new byte[image.getWidth() * image.getHeight() * (int) out.elemSize()];
        int[] dataBuff = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        for (int i = 0; i < dataBuff.length; i++) {
            data[i * 3] = (byte) ((dataBuff[i]));
            data[i * 3 + 1] = (byte) ((dataBuff[i]));
            data[i * 3 + 2] = (byte) ((dataBuff[i]));
        }
        out.put(0, 0, data);
        return out;
    }

    public static MyImage mat2MyImage(Mat original){
        BufferedImage image = null;
        int width = original.width(), height = original.height(), channels = original.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        original.get(0, 0, sourcePixels);

        if (original.channels() > 1)
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        }
        else
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return new MyImage(image);
    }
}
