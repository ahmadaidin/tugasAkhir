package processor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ExMain2 {

    public static void main(String[] args) {
        findColorspaceRange();
    }

    public static void showGUI(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }

    public static void eliminateColorSpace(){

    }

    public static void findColorspaceRange(){
        double HMin=0, HMax=0, SMin=0, SMax=0, VMin=0, VMax = 0;
        double YMin= 0, YMax= 0, CbMin= 0, CbMax= 0, CrMin= 0, CrMax = 0;
        int RMin= 0, RMax= 0, GMin= 0, GMax= 0, BMin= 0, BMax = 0;

        for(int i = 1; i< 7; i++) {

            MyImage myImage = new MyImage("D:\\Image\\bijigigi\\gigi"+i+".png");
            Point2D.Double H = ColorSpaceFinder.getHBorder(myImage);
            Point2D.Double S = ColorSpaceFinder.getSBorder(myImage);
            Point2D.Double V = ColorSpaceFinder.getVBorder(myImage);

            Point2D.Double Y = ColorSpaceFinder.getYBorder(myImage);
            Point2D.Double Cb = ColorSpaceFinder.getCbBorder(myImage);
            Point2D.Double Cr = ColorSpaceFinder.getCrBorder(myImage);

            Point R = ColorSpaceFinder.getRBorder(myImage);
            Point G = ColorSpaceFinder.getGBorder(myImage);
            Point B = ColorSpaceFinder.getBBorder(myImage);

            if(H.x<HMin) HMin = H.x;
            if(H.y>HMax) HMax = H.y;

            if(S.x<SMin) SMin = S.x;
            if(S.y>SMax) SMax = S.y;

            if(V.x<VMin) VMin = V.x;
            if(V.y>VMax) VMax = V.y;

            if(Y.x<YMin) YMin = Y.x;
            if(Y.y>YMax) YMax = Y.y;

            if(Cb.x<CbMin) CbMin = Cb.x;
            if(Cb.y>CbMax) CbMax = Cb.y;

            if(Cr.x<CrMin) CrMin = Cr.x;
            if(Cr.y>CrMax) CrMax = Cr.y;


            if(R.x<RMin) RMin = R.x;
            if(R.y>RMax) RMax = R.y;

            if(G.x<GMin) GMin = G.x;
            if(G.y>GMax) GMax = G.y;

            if(B.x<BMin) BMin = B.x;
            if(B.y>BMax) BMax = B.y;

        }

        NumberFormat formater = new DecimalFormat("#0.00");

        System.out.println("H = " + formater.format(HMin) + " " + formater.format(HMax));
        System.out.println("S = " + formater.format(SMin) + " " + formater.format(SMax));
        System.out.println("V = " + formater.format(VMin) + " " + formater.format(VMax));

        System.out.println("");
        System.out.println("Y = " + YMin + " " + YMax);
        System.out.println("Cb = " + CbMin + " " + CbMax);
        System.out.println("Cr = " + CrMin + " " + CrMax);

        System.out.println("");
        System.out.println("R = " + RMin + " " + RMax);
        System.out.println("G = " + GMin + " " + GMax);
        System.out.println("B = " + BMin + " " + BMax);
    }

    private static void createAndShowGui() {
        MyImage myImage = new MyImage("D:\\Image\\gigi1.png");
//        Pair p = ColorSpaceFinder.getYBorder(myImage);
        createAndShowImage(myImage,"gigi");

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

    private static void createAndShowLineChart(int[] data, String title, String categoryAxisLabel,String valueAxisLabel) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < data.length; i++) {
            dataset.setValue(data[i], "", "" + i);
        }
        JPanel panel = new JPanel();
        JFreeChart chart = ChartFactory.createLineChart(title, categoryAxisLabel, valueAxisLabel, dataset, PlotOrientation.VERTICAL, false, false, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.removeAll();
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.validate();

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
