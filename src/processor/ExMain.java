package processor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

public class ExMain  {

    public static void showImage(MyImage img, String title){
        ImageIcon icon = new ImageIcon(img.getData());
        JLabel label = new JLabel(icon);
        JOptionPane.showMessageDialog(null, label,title,1);
    }

    public static void createAndShowImage(MyImage myImage, String title){
        JPanel panel = new JPanel();
        ImageIcon imageIcon = new ImageIcon(myImage.getData());
        JLabel label = new JLabel(title,imageIcon,JLabel.HORIZONTAL);
        panel.add(label);

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void createAndShowMultiImages(MyImage[] myImages, String[] titles){
        JFrame frame = new JFrame("Tugas Akhir");
        JPanel panel = new JPanel();

        for(int i = 0; i < myImages.length; i++) {
            ImageIcon imageIcon = new ImageIcon(myImages[i].getData());
            JLabel label = new JLabel(titles[i], imageIcon,JLabel.HORIZONTAL);
            panel.add(label);
        }

//        frame.getContentPane().add(panel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setLocationByPlatform(true);
//        frame.setVisible(true);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(50, 30, 1100, 640);
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(1200, 700));
        contentPane.add(scrollPane);
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }


//        MyImage result = ImgFilterer.smoothen(myImage);

//    result = MyImage.mergeImage(myImage,result);
//    showImage(result,"Test");

    private static void createAndShowBarChart(int[] data, String title, String categoryAxisLabel,String valueAxisLabel){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(int i = 0; i < data.length; i++) {
            dataset.setValue(data[i],"",""+i);
        }
        JPanel panel = new JPanel();
        JFreeChart chart = ChartFactory.createBarChart(title, categoryAxisLabel,valueAxisLabel,dataset, PlotOrientation.VERTICAL,false,false,false);
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.removeAll();
        panel.add(chartPanel,BorderLayout.CENTER);
        panel.validate();

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private static void createAndShowLineChart(int[] data, String title, String categoryAxisLabel,String valueAxisLabel){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(int i = 0; i < data.length; i++) {
            dataset.setValue(data[i],"",""+i);
        }
        JPanel panel = new JPanel();
        JFreeChart chart = ChartFactory.createLineChart(title, categoryAxisLabel,valueAxisLabel,dataset, PlotOrientation.VERTICAL,false,false,false);
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.removeAll();
        panel.add(chartPanel,BorderLayout.CENTER);
        panel.validate();

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
    private static void createAndShowGui() {
//        MyImage myImage = new MyImage("D:\\Image\\alpa.png");
//        MyImage myImage = new MyImage("D:\\Image\\bunga.png");
//        MyImage myImage = new MyImage("D:\\Image\\cup.png");
//        MyImage myImage = new MyImage("D:\\Image\\einstein.png");
        MyImage myImage = new MyImage("D:\\Image\\gigi1.png");
//        MyImage myImage = new MyImage("D:\\Image\\ikan.png");
//        MyImage myImage = new MyImage("D:\\Image\\jeli.png");
//        MyImage myImage = new MyImage("D:\\Image\\mobil.png");


//        System.out.println("size=" + myImage.getWidth()*myImage.getHeight());
//        int[] oldHistogram = HistogramProcessor.getGreylevelHistogram(myImage);
//        int[] LUT = HistogramProcessor.getGrayHistogramEqualizationLUT(myImage.getHeight(),myImage.getWidth(),oldHistogram);
//        myImage = HistogramProcessor.histogramEqualization(myImage);

        MyImage[] results = new MyImage[9];
        String[] titles = new String[9];

//        myImage = ImgFilterer.getGrayImage(myImage);
        myImage = ImgFilterer.blur(myImage);
        results[0] = myImage;
        results[1] = EdgeDetector.robertOperatorDetection(myImage);
        results[2] = EdgeDetector.sobelOperatorDetection(myImage);
        results[3] = EdgeDetector.prewittOperatorDetection(myImage);
        results[4] = EdgeDetector.freiChenOperatorDetection(myImage);

        results[5] = EdgeDetector.robinson3LvCompassDetection(myImage);
        results[6] = EdgeDetector.robinson5LvCompassDetection(myImage);
        results[7] = EdgeDetector.prewittCompassDetection(myImage);
        results[8] = EdgeDetector.krischCompassDetection(myImage);

        titles[0] = "Original";
        titles[1] = "robert";
        titles[2] = "sobel";
        titles[3] = "prewitt";
        titles[4] = "freiChen";

        titles[5] = "robinson3LvCompass";
        titles[6] = "robinson5LvCompass";
        titles[7] = "prewittCompass";
        titles[8] = "krischCompass";

        for(int i = 1; i<9;i++) {
            int[] histogram = HistogramProcessor.getGreylevelHistogram(results[i]);
            int size = results[i].getWidth() * results[i].getHeight();
            int threshold = OtsuBinaryConverter.countThreshold(size, histogram);
            results[i] = OtsuBinaryConverter.convertImage(results[i], threshold);
            results[i] = ZhangSuenSkeletonizer.skeletonize(results[i]);
//            ChainCodeExtractor chainCodeExtractor = new ChainCodeExtractor(results[i]);
//            ArrayList<ChainCode> chainCodes = chainCodeExtractor.findSegments();
////            chainCodes = chainCodeExtractor.cleareNoise(chainCodes);
//            for(int j = 0; j< chainCodes.size(); j++ ){
//                for(int k = 0; k< chainCodes.get(j).pixelPosSize(); k++){
//                    ChainCode chainCode = chainCodes.get(j);
//                    int rgb = MyImage.makeRGB(200,0,0);
//                    results[i].setRGB(chainCode.getPixelPos().get(k).x, chainCode.getPixelPos().get(k).y,rgb);
//                    System.out.print(chainCode.getDirection()+", ");
//                }
//                System.out.println(" ");
//            }
        }

//        for(int i=0; i<results.length;i++){
//            createAndShowImage(results[i],titles[i]);
//        }

        createAndShowMultiImages(results,titles);

//        int[] newHistogram = HistogramProcessor.getGreylevelHistogram(newImage);
//
//        System.out.print("oldPx   ");
//        System.out.print("newPx   ");
//        System.out.print("oldFreq   ");
//        System.out.print("newFreq  ");
//        System.out.println("");
//
//        for(int i=0; i<oldHistogram.length;i++){
//            System.out.print(i+"       ");
//            System.out.print(LUT[i]+"        ");
//            System.out.print(oldHistogram[i]+"         ");
//            System.out.print(newHistogram[i]);
//            System.out.println(" ");
//        }
//
//        createAndShowBarChart(oldHistogram,"old histogram","pixel intensity","frequency");
//        createAndShowBarChart(newHistogram,"new histogram","pixel intensity","frequency");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }
}
