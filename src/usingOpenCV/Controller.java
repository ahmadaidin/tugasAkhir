package usingOpenCV;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import myGraphic.MyLine;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import processor.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    @FXML private SplitPane masterPane;
    @FXML private ScrollPane imagePane;
    @FXML private ImageView imageView;

    @FXML private CheckBox detectEdgeBox;

    @FXML
    private Slider threshold;
    // checkbox for enabling/disabling background removal

    @FXML
    private TextField numCluster;

    String format = "%1$.3f";

    ArrayList<MyImage> images;
    int currentIdx;
    int clusterIdx;
    ArrayList<Mat> clusters;

    public void initialize() {
        images = new ArrayList<>();
        currentIdx = 0;
        clusterIdx = 0;
        clusters = new ArrayList<>();

        threshold.valueChangingProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging, Boolean changing) {
                if(changing);
                else {
                    undo();
                    doCanny();
                }
            }
        });
    }

    public void addImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(masterPane.getScene().getWindow());
        if(file != null){
            BufferedImage newImg;
            Image image;

            try{
                newImg = ImageIO.read(file);
                MyImage newMyImage = new MyImage(newImg);
                MyImage oldMyImage;
                if(imageView.getImage()!=null){
                    oldMyImage = new MyImage(images.get(currentIdx));
                    newMyImage= MyImage.mergeImage(oldMyImage,newMyImage);
                    newImg = newMyImage.getData();
                }
                images.clear();
                currentIdx=-1;
                setImageData(newMyImage);
                image = SwingFXUtils.toFXImage(newImg, null );
                imageView.setImage(image);
                imagePane.setContent(imageView);
            } catch (IOException e){

            }
        }
    }

    public void changeImage(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(masterPane.getScene().getWindow());
        if(file != null){
            BufferedImage newImg;
            Image image;

            try{
                newImg = ImageIO.read(file);
                MyImage newMyImage = new MyImage(newImg);
                if(imageView.getImage()!=null){
                    newImg = newMyImage.getData();
                }
                images.clear();
                currentIdx=-1;
                setImageData(newMyImage);
                image = SwingFXUtils.toFXImage(newImg, null );
                imageView.setImage(image);
                imagePane.setContent(imageView);
            } catch (IOException e){

            }
        }
    }

    public void resetImage() {
        MyImage image = images.get(0);
        setImageView(image);
        setImageData(image);
    }

    public void undo(){
        if(currentIdx > 0) {
            currentIdx--;
            setImageView(images.get(currentIdx));
        }
    }

    public void redo(){
        if (currentIdx < images.size()-1) {
            currentIdx++;
            setImageView(images.get(currentIdx));
        }
    }

    public void setImageData(MyImage myImage) {
        for(int i = currentIdx+1; i < images.size(); i++){
            images.remove(i);
        }
        images.add(myImage);
        currentIdx++;
    }


    public MyImage getImage(){
        return new MyImage(SwingFXUtils.fromFXImage(imageView.getImage(),null));
    }

    public void setImageView(MyImage image){
        Image imageFX = SwingFXUtils.toFXImage(image.getData(),null);
        imageView.setImage(imageFX);
        imagePane.setContent(imageView);
    }

    private void doCanny(){
        MyImage src = getImage();
        Mat srcFrame = Converter.myImage2Mat(src);
        Mat frame = cannyEdgeDetect(srcFrame);
        MyImage image = Converter.mat2MyImage(frame);
        BinaryConverter.convertImage(image,250);
        image= ZhangSuenSkeletonizer.skeletonize(image);
        setImageView(image);
        setImageData(image);
    }

    public void edgeDetectSelected(){
        if(this.detectEdgeBox.isSelected()){
            threshold.setDisable(false);
            MyImage image = getImage();
            setImageData(image);
        } else {
            threshold.setDisable(true);
        }
    }

    public void sobel(){
        MyImage myImage = new MyImage(images.get(currentIdx));
        myImage = EdgeDetector.sobelOperatorDetection(myImage);
        int size = myImage.getWidth() * myImage.getHeight();
        int[] histogram = HistogramProcessor.getGreylevelHistogram(myImage);
        int threshold = OtsuBinaryConverter.countThreshold(size, histogram);
        myImage = OtsuBinaryConverter.convertImage(myImage, threshold);
        myImage = ZhangSuenSkeletonizer.skeletonize(myImage);
        setImageView(myImage);
        setImageData(myImage);
    }

    public void robert(){
        MyImage myImage = new MyImage(images.get(currentIdx));
        myImage = EdgeDetector.robertOperatorDetection(myImage);
        int size = myImage.getWidth() * myImage.getHeight();
        int[] histogram = HistogramProcessor.getGreylevelHistogram(myImage);
        int threshold = OtsuBinaryConverter.countThreshold(size, histogram);
        myImage = OtsuBinaryConverter.convertImage(myImage, threshold);
        myImage = ZhangSuenSkeletonizer.skeletonize(myImage);
        setImageView(myImage);
        setImageData(myImage);
    }

    public void prewitt(){
        MyImage myImage = new MyImage(images.get(currentIdx));
        Mat mat = Converter.myImage2Mat(myImage);
        Mat blur = new Mat();
        Imgproc.blur(mat, blur, new Size(3,3));
        myImage = Converter.mat2MyImage(blur);
        myImage = EdgeDetector.prewittOperatorDetection(myImage);
        int size = myImage.getWidth() * myImage.getHeight();
        int[] histogram = HistogramProcessor.getGreylevelHistogram(myImage);
        int threshold = OtsuBinaryConverter.countThreshold(size, histogram);
        myImage = OtsuBinaryConverter.convertImage(myImage, threshold);
        myImage = ZhangSuenSkeletonizer.skeletonize(myImage);
        setImageView(myImage);
        setImageData(myImage);
    }

    public void freiChen(){
        MyImage myImage = new MyImage(images.get(currentIdx));
        Mat mat = Converter.myImage2Mat(myImage);
        Mat blur = new Mat();
        Imgproc.blur(mat, blur, new Size(3,3));
        myImage = Converter.mat2MyImage(blur);
        myImage = EdgeDetector.freiChenOperatorDetection(myImage);
        int size = myImage.getWidth() * myImage.getHeight();
        int[] histogram = HistogramProcessor.getGreylevelHistogram(myImage);
        int threshold = OtsuBinaryConverter.countThreshold(size, histogram);
        myImage = OtsuBinaryConverter.convertImage(myImage, threshold);
        myImage = ZhangSuenSkeletonizer.skeletonize(myImage);
        setImageView(myImage);
        setImageData(myImage);
    }

    public void robinson3(){
        MyImage myImage = new MyImage(images.get(currentIdx));
        Mat mat = Converter.myImage2Mat(myImage);
        Mat blur = new Mat();
        Imgproc.blur(mat, blur, new Size(3,3));
        myImage = Converter.mat2MyImage(blur);
        myImage = EdgeDetector.robinson3LvCompassDetection(myImage);
        int size = myImage.getWidth() * myImage.getHeight();
        int[] histogram = HistogramProcessor.getGreylevelHistogram(myImage);
        int threshold = OtsuBinaryConverter.countThreshold(size, histogram);
        myImage = OtsuBinaryConverter.convertImage(myImage, threshold);
        myImage = ZhangSuenSkeletonizer.skeletonize(myImage);
        setImageView(myImage);
        setImageData(myImage);
    }

    public void robinson5(){
        MyImage myImage = new MyImage(images.get(currentIdx));
        Mat mat = Converter.myImage2Mat(myImage);
        Mat blur = new Mat();
        Imgproc.blur(mat, blur, new Size(3,3));
        myImage = Converter.mat2MyImage(blur);
        myImage = EdgeDetector.robinson5LvCompassDetection(myImage);
        int size = myImage.getWidth() * myImage.getHeight();
        int[] histogram = HistogramProcessor.getGreylevelHistogram(myImage);
        int threshold = OtsuBinaryConverter.countThreshold(size, histogram);
        myImage = OtsuBinaryConverter.convertImage(myImage, threshold);
        myImage = ZhangSuenSkeletonizer.skeletonize(myImage);
        setImageView(myImage);
        setImageData(myImage);
    }

    public void krisch(){
        MyImage myImage = new MyImage(images.get(currentIdx));
        Mat mat = Converter.myImage2Mat(myImage);
        Mat blur = new Mat();
        Imgproc.blur(mat, blur, new Size(3,3));
        myImage = Converter.mat2MyImage(blur);
        myImage = EdgeDetector.krischCompassDetection(myImage);
        int size = myImage.getWidth() * myImage.getHeight();
        int[] histogram = HistogramProcessor.getGreylevelHistogram(myImage);
        int threshold = OtsuBinaryConverter.countThreshold(size, histogram);
        myImage = OtsuBinaryConverter.convertImage(myImage, threshold);
        myImage = ZhangSuenSkeletonizer.skeletonize(myImage);
        setImageView(myImage);
        setImageData(myImage);
    }

    public void prewittCompass(){
        MyImage myImage = new MyImage(images.get(currentIdx));
        Mat mat = Converter.myImage2Mat(myImage);
        Mat blur = new Mat();
        Imgproc.blur(mat, blur, new Size(3,3));
        myImage = Converter.mat2MyImage(blur);
        myImage = EdgeDetector.prewittCompassDetection(myImage);
        int size = myImage.getWidth() * myImage.getHeight();
        int[] histogram = HistogramProcessor.getGreylevelHistogram(myImage);
        int threshold = OtsuBinaryConverter.countThreshold(size, histogram);
        myImage = OtsuBinaryConverter.convertImage(myImage, threshold);
        myImage = ZhangSuenSkeletonizer.skeletonize(myImage);
        setImageView(myImage);
        setImageData(myImage);
    }



    /**
     * Apply Canny
     *
     * @param frame
     *            the current frame
     * @return an image elaborated with Canny
     */
    private Mat cannyEdgeDetect(Mat frame){
        // init
        Mat grayImage = new Mat();
        Mat detectedEdges = new Mat();

        // convert to grayscale
        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);

        // reduce noise with a 3x3 kernel
        Imgproc.blur(grayImage, detectedEdges, new Size(5,5));

        // canny detector, with ratio of lower:upper threshold of 3:1
        Imgproc.Canny(detectedEdges, detectedEdges, this.threshold.getValue(), this.threshold.getValue() * 3);

        // using Canny's output as a mask, display the result
        Mat dest = new Mat();
        frame.copyTo(dest, detectedEdges);

        return dest;
    }

    private Mat laplacian(Mat frame){
        // init
        Mat grayImage = new Mat();
        Mat detectedEdges = new Mat();
        int kernel_size = 3;
        int scale = 1;
        int delta = 0;
        int ddepth = CvType.CV_16S;

        // reduce noise with a 3x3 kernel
        Imgproc.GaussianBlur(frame, frame, new Size(3,3),0,0,Core.BORDER_DEFAULT);

        // convert to grayscale
        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);

        Imgproc.Laplacian(grayImage, detectedEdges, ddepth, kernel_size,scale,delta,Core.BORDER_DEFAULT);

        // using Canny's output as a mask, display the result
        Mat dest = new Mat();
        // converting back to CV_8U
        Core.convertScaleAbs( detectedEdges, dest);

        Mat result = new Mat();

        frame.copyTo(result, dest);

        return result;
    }


    private Mat HoughCircleDetection(Mat frame){
        // init
        Mat grayImage = new Mat();
        Mat detectedEdges = new Mat();

        // convert to grayscale
        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);

        // reduce noise with a 3x3 kernel
        Imgproc.medianBlur(grayImage, detectedEdges, 5);

        Imgproc.HoughCircles(grayImage,detectedEdges,Imgproc.CV_HOUGH_GRADIENT,1,grayImage.rows()/16,100,30,1,30);
        // using Canny's output as a mask, display the result
        for (int x = 0; x < detectedEdges.cols(); x++) {
            double[] c = detectedEdges.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            // circle center
            Imgproc.circle(frame, center, 1, new Scalar(0,100,100), 1, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(frame, center, radius, new Scalar(255,0,255), 1, 8, 0 );
        }

        return frame;
    }

    public void dilate(){
        MyImage imgSrc = getImage();
        Mat src = Converter.myImage2Mat(imgSrc);
        Mat dst = new Mat();

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((2* 2) + 1, (2 * 2) + 1));

        Imgproc.dilate(src, dst, kernel);

        MyImage myImage = Converter.mat2MyImage(dst);

        setImageView(myImage);
        setImageData(myImage);
    }

    public void erode(){
        MyImage imgSrc = getImage();
        Mat src = Converter.myImage2Mat(imgSrc);
        Mat dst = new Mat();

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((3 * 3) + 1, (3 * 3) + 1));

        Imgproc.erode(src, dst, kernel);

        MyImage myImage = Converter.mat2MyImage(dst);

        setImageView(myImage);
        setImageData(myImage);
    }

    public void getTeethCandidate(){
        MyImage image = getImage();
        MyImage oriImg = new MyImage(images.get(0));
        int threshold = OtsuBinaryConverter.countThreshold(image.getWidth()*image.getHeight(), HistogramProcessor.getGreylevelHistogram(image));
        for(int y = 0; y<image.getHeight();y++) {
            for(int x = 0; x<image.getWidth();x++) {
                int rgb = image.getRGB(x,y);
                if(MyImage.getGraylevel(rgb)<threshold) {
                    int color = MyImage.makeRGB(0,0,0);
                    oriImg.setRGB(x,y,color);
                }
            }
        }
        setImageData(oriImg);
        setImageView(oriImg);
    }

    public void binarize(){
        MyImage image = getImage();
        int threshold = OtsuBinaryConverter.countThreshold(image.getWidth()*image.getHeight(), HistogramProcessor.getGreylevelHistogram(image));
        image = OtsuBinaryConverter.convertImage(image,threshold);
        setImageData(image);
        setImageView(image);
    }


    /**
     * Apply Image Clustering
     */

    public void clusterImage(){
        MyImage image = getImage();
        Mat img = Converter.myImage2Mat(image);
        int num = Integer.parseInt(numCluster.getText());
        clusters = cluster(img, num);
        image = Converter.mat2MyImage(clusters.get(0));
        clusterIdx = 0;
        setImageView(image);
        setImageData(image);
    }

    public void nextCluster(){
        if(clusterIdx<clusters.size()-1){
            clusterIdx++;
            MyImage image = Converter.mat2MyImage(clusters.get(clusterIdx));
            setImageView(image);
            setImageData(image);
        }
    }

    public void prevCluster(){
        if(clusterIdx>0){
            clusterIdx--;
            MyImage image = Converter.mat2MyImage(clusters.get(clusterIdx));
            setImageView(image);
        }
    }


    public static ArrayList<Mat> cluster(Mat cutout, int k) {
        Mat samples = cutout.reshape(1, cutout.cols() * cutout.rows());
        Mat samples32f = new Mat();
        samples.convertTo(samples32f, CvType.CV_32F, 1.0 / 255.0);

        Mat labels = new Mat();
        TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 100, 1);
        Mat centers = new Mat();
        Core.kmeans(samples32f, k, labels, criteria, 1, Core.KMEANS_PP_CENTERS, centers);
        return showClusters(cutout, labels, centers);
    }

    private static ArrayList<Mat> showClusters (Mat cutout, Mat labels, Mat centers) {
        centers.convertTo(centers, CvType.CV_8UC1, 255.0);
        centers.reshape(3);

        ArrayList<Mat> clusters = new ArrayList<>();
        for(int i = 0; i < centers.rows(); i++) {
            clusters.add(Mat.zeros(cutout.size(), cutout.type()));
        }

        Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
        for(int i = 0; i < centers.rows(); i++) counts.put(i, 0);

        int rows = 0;
        for(int y = 0; y < cutout.rows(); y++) {
            for(int x = 0; x < cutout.cols(); x++) {
                int label = (int)labels.get(rows, 0)[0];
                int r = (int)centers.get(label, 2)[0];
                int g = (int)centers.get(label, 1)[0];
                int b = (int)centers.get(label, 0)[0];
                counts.put(label, counts.get(label) + 1);
                clusters.get(label).put(y, x, b, g, r);
                rows++;
            }
        }
        System.out.println(counts);
        return clusters;
    }

    private int midOrdinat = 1;

    public void getHorizontalLine(){
        MyImage edgesImage = new MyImage(images.get(currentIdx));

        int[] sumPixel = new int[edgesImage.getHeight()-1];
        int max = 0;
        for (int y = 1; y < edgesImage.getHeight()-1; y++) {
            sumPixel[y] = 0;
            for (int x = 0; x < edgesImage.getWidth(); x++) {
                int rgb = edgesImage.getRGB(x,y);
                int grayVal = MyImage.getGraylevel(rgb);
                if (grayVal != 0) {
                    sumPixel[y]++;
                }
            }
            if (sumPixel[y] >= max) {
                max = sumPixel[y];
                midOrdinat = y;
            }
        }

        int rgb = MyImage.makeRGB(255,0,0);

        for (int x = 0; x < edgesImage.getWidth(); x++) {
            edgesImage.setRGB(x,midOrdinat,rgb);
        }

        setImageData(edgesImage);
        setImageView(edgesImage);
    }

    public void matchTooth(){
        MyImage myImage = getImage();
        int x = myImage.getWidth()/2;

        java.awt.Point origin = new java.awt.Point(x, midOrdinat);
        myImage = drawTemplate(myImage,origin,true,true);
        setImageData(myImage);
        setImageView(myImage);
    }

    public MyImage drawTemplate(MyImage src, java.awt.Point origin, boolean top, boolean right){
        int height = src.getHeight();
        int width = 10;
        int rgb = MyImage.makeRGB(0,0,255);
        MyImage myImage = new MyImage(src);

        return myImage;
    }







}
