package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;
import org.controlsfx.control.RangeSlider;
import processor.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SampleController {
    @FXML private SplitPane masterPane;
    @FXML private ScrollPane imagePane;
    @FXML private ImageView imageView;
    @FXML private ComboBox colorComboBox;
    @FXML private ComboBox dentComboBox;


    @FXML private Label colorSpace1;
    @FXML private Label colorSpace2;
    @FXML private Label colorSpace3;

    @FXML private RangeSlider colorSlider1;
    @FXML private RangeSlider colorSlider2;
    @FXML private RangeSlider colorSlider3;

    @FXML private TextField colorHighVal1;
    @FXML private TextField colorHighVal2;
    @FXML private TextField colorHighVal3;

    @FXML private TextField colorLowVal1;
    @FXML private TextField colorLowVal2;
    @FXML private TextField colorLowVal3;

    @FXML private ComboBox RGBChannel;
    @FXML private ComboBox YCbCrChannel;
    @FXML private ComboBox HSVChannel;


    String format = "%1$.3f";

    MyImage originalImage;
    MyImage tempImage;

    public void initialize(){

        colorSlider1.setLowValue(0);
        colorSlider2.setLowValue(-0.5);
        colorSlider3.setLowValue(-0.5);

        colorHighVal1.textProperty().bind(colorSlider1.highValueProperty().asString(format));
        colorHighVal2.textProperty().bind(colorSlider2.highValueProperty().asString(format));
        colorHighVal3.textProperty().bind(colorSlider3.highValueProperty().asString(format));

        colorLowVal1.textProperty().bind(colorSlider1.lowValueProperty().asString(format));
        colorLowVal2.textProperty().bind(colorSlider2.lowValueProperty().asString(format));
        colorLowVal3.textProperty().bind(colorSlider3.lowValueProperty().asString(format));


        colorSlider1.lowValueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(
                    ObservableValue<? extends Boolean> observableValue,
                    Boolean wasChanging,
                    Boolean changing) {

                if (changing);
                else findColorRange();
            }
        });

        colorSlider1.highValueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(
                    ObservableValue<? extends Boolean> observableValue,
                    Boolean wasChanging,
                    Boolean changing) {

                if (changing);
                else findColorRange();
            }
        });

        colorSlider2.lowValueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(
                    ObservableValue<? extends Boolean> observableValue,
                    Boolean wasChanging,
                    Boolean changing) {

                if (changing);
                else findColorRange();
            }
        });

        colorSlider2.highValueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(
                    ObservableValue<? extends Boolean> observableValue,
                    Boolean wasChanging,
                    Boolean changing) {

                if (changing);
                else findColorRange();
            }
        });

        colorSlider3.lowValueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(
                    ObservableValue<? extends Boolean> observableValue,
                    Boolean wasChanging,
                    Boolean changing) {

                if (changing);
                else findColorRange();
            }
        });

        colorSlider3.highValueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(
                    ObservableValue<? extends Boolean> observableValue,
                    Boolean wasChanging,
                    Boolean changing) {

                if (changing);
                else findColorRange();
            }
        });
    }

    public MyImage getImage(){
        return new MyImage(SwingFXUtils.fromFXImage(imageView.getImage(),null));
    }

    public void setImage(MyImage image){
        Image imageFX = SwingFXUtils.toFXImage(image.getData(),null);
        imageView.setImage(imageFX);
        imagePane.setContent(imageView);
    }

    public void findColorRange(){
        MyImage myImage = new MyImage(originalImage);
        if (colorComboBox.getValue().equals("YCbCr"))
            myImage = findYCbCrRange(myImage);
        else if (colorComboBox.getValue().equals("HSV"))
            myImage = findHSVRange(myImage);
        else myImage = findRGBRange(myImage);
        BufferedImage bufferedImage = myImage.getData();
        Image newImage = SwingFXUtils.toFXImage(bufferedImage,null);
        imageView.setImage(newImage);
    }

    public MyImage findYCbCrRange(MyImage myImage){
        double lowYValue = colorSlider1.getLowValue();
        double highYValue = colorSlider1.getHighValue();
        double lowCbValue = colorSlider2.getLowValue();
        double highCbValue = colorSlider2.getHighValue();
        double lowCrValue = colorSlider3.getLowValue();
        double highCrValue = colorSlider3.getHighValue();
        return ColorSpaceFinder.maskByYCbCr(myImage, lowYValue,highYValue,lowCbValue,highCbValue,lowCrValue,highCrValue);
    }

    public MyImage findHSVRange(MyImage myImage){
        double lowHValue = colorSlider1.getLowValue();
        double highHValue = colorSlider1.getHighValue();
        double lowSValue = colorSlider2.getLowValue();
        double highSValue = colorSlider2.getHighValue();
        double lowVValue = colorSlider3.getLowValue();
        double highVValue = colorSlider3.getHighValue();
        return ColorSpaceFinder.maskByHSV(myImage, lowHValue,highHValue,lowSValue,highSValue,lowVValue,highVValue);
    }

    public MyImage findRGBRange(MyImage myImage){
        int lowRValue = (int) colorSlider1.getLowValue();
        int highRValue = (int)colorSlider1.getHighValue();
        int lowGValue = (int) colorSlider2.getLowValue();
        int highGValue = (int) colorSlider2.getHighValue();
        int lowBValue = (int) colorSlider3.getLowValue();
        int highBValue = (int) colorSlider3.getHighValue();
        return ColorSpaceFinder.maskByRGB(myImage, lowRValue,highRValue,lowGValue,highGValue,lowBValue,highBValue);
    }

    public void addImage(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(masterPane.getScene().getWindow());
        if(file != null){
            BufferedImage newImg = null;
            Image image = null;

            try{
                newImg = ImageIO.read(file);
                MyImage newMyImage = new MyImage(newImg);
                MyImage oldMyImage;
                if(imageView.getImage()!=null){
                    oldMyImage = new MyImage(originalImage);
                    newMyImage= MyImage.mergeImage(oldMyImage,newMyImage);
                    newImg = newMyImage.getData();
                }
                originalImage = newMyImage;
                tempImage = new MyImage(originalImage);
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
            BufferedImage newImg = null;
            Image image = null;

            try{
                newImg = ImageIO.read(file);
                MyImage newMyImage = new MyImage(newImg);
                if(imageView.getImage()!=null){
                    newImg = newMyImage.getData();
                }
                originalImage = newMyImage;
                tempImage = new MyImage(originalImage);
                image = SwingFXUtils.toFXImage(newImg, null );
                imageView.setImage(image);
                imagePane.setContent(imageView);
            } catch (IOException e){

            }
        }
    }

    public void setColorSpace(ActionEvent event){
        String value = colorComboBox.getValue().toString();
        String dentPict = dentComboBox.getValue().toString();

        if(value.equals("YCbCr")){
            colorSpace1.setText("Y");
            colorSpace2.setText("Cb");
            colorSpace3.setText("Cr");

            colorSlider1.setMin(0);
            colorSlider2.setMin(-0.5);
            colorSlider3.setMin(-0.5);

            colorSlider1.setMax(1);
            colorSlider2.setMax(0.5);
            colorSlider3.setMax(0.5);

            colorSlider1.setBlockIncrement(0.01);
            colorSlider2.setBlockIncrement(0.01);
            colorSlider3.setBlockIncrement(0.01);

            if(dentPict.equals("Gigi Depan") || dentPict.equals("Gigi Samping")) {
                colorSlider1.setLowValue(0.310);
                colorSlider2.setLowValue(-0.058);
                colorSlider3.setLowValue(-0.014);

                colorSlider1.setHighValue(0.357);
                colorSlider2.setHighValue(0.017);
                colorSlider3.setHighValue(0.059);
            } else if(dentPict.equals("Gigi Atas")) {
                colorSlider1.setLowValue(0.330);
                colorSlider2.setLowValue(-0.085);
                colorSlider3.setLowValue(0.000);

                colorSlider1.setHighValue(0.382);
                colorSlider2.setHighValue(0.003);
                colorSlider3.setHighValue(0.082);
            } else{
                colorSlider1.setLowValue(0.330);
                colorSlider2.setLowValue(-0.105);
                colorSlider3.setLowValue(-0.018);

                colorSlider1.setHighValue(0.410);
                colorSlider2.setHighValue(0.003);
                colorSlider3.setHighValue(0.098);
            }

            format = "%1$.3f";

            colorHighVal1.textProperty().bind(colorSlider1.highValueProperty().asString(format));
            colorHighVal2.textProperty().bind(colorSlider2.highValueProperty().asString(format));
            colorHighVal3.textProperty().bind(colorSlider3.highValueProperty().asString(format));

            colorLowVal1.textProperty().bind(colorSlider1.lowValueProperty().asString(format));
            colorLowVal2.textProperty().bind(colorSlider2.lowValueProperty().asString(format));
            colorLowVal3.textProperty().bind(colorSlider3.lowValueProperty().asString(format));
        } else if(value.equals("HSV")){
            format = "%1$.0f";

            colorSpace1.setText("H");
            colorSpace2.setText("S");
            colorSpace3.setText("V");

            colorSlider1.setMin(0);
            colorSlider2.setMin(0);
            colorSlider3.setMin(0);

            colorSlider1.setMax(360);
            colorSlider2.setMax(100);
            colorSlider3.setMax(100);

            colorSlider1.setBlockIncrement(1);
            colorSlider2.setBlockIncrement(1);
            colorSlider3.setBlockIncrement(1);

            if(dentPict.equals("Gigi Depan") || dentPict.equals("Gigi Samping")) {
                colorSlider1.setLowValue(0);
                colorSlider2.setLowValue(0);
                colorSlider3.setLowValue(33);

                colorSlider1.setHighValue(186);
                colorSlider2.setHighValue(27);
                colorSlider3.setHighValue(50);
            } else if(dentPict.equals("Gigi Atas")) {
                colorSlider1.setLowValue(0);
                colorSlider2.setLowValue(0);
                colorSlider3.setLowValue(0);

                colorSlider1.setHighValue(0);
                colorSlider2.setHighValue(100);
                colorSlider3.setHighValue(44);
            } else{
                colorSlider1.setLowValue(0);
                colorSlider2.setLowValue(2);
                colorSlider3.setLowValue(31);

                colorSlider1.setHighValue(360);
                colorSlider2.setHighValue(49);
                colorSlider3.setHighValue(45);
            }

            colorHighVal1.textProperty().bind(colorSlider1.highValueProperty().asString(format));
            colorHighVal2.textProperty().bind(colorSlider2.highValueProperty().asString(format));
            colorHighVal3.textProperty().bind(colorSlider3.highValueProperty().asString(format));

            colorLowVal1.textProperty().bind(colorSlider1.lowValueProperty().asString(format));
            colorLowVal2.textProperty().bind(colorSlider2.lowValueProperty().asString(format));
            colorLowVal3.textProperty().bind(colorSlider3.lowValueProperty().asString(format));
        } else if(value.equals("RGB")){
            format = "%1$.0f";

            colorSpace1.setText("R");
            colorSpace2.setText("G");
            colorSpace3.setText("B");

            colorSlider1.setMin(0);
            colorSlider2.setMin(0);
            colorSlider3.setMin(0);

            colorSlider1.setMax(255);
            colorSlider2.setMax(255);
            colorSlider3.setMax(255);

            colorSlider1.setBlockIncrement(1);
            colorSlider2.setBlockIncrement(1);
            colorSlider3.setBlockIncrement(1);

            if(dentPict.equals("Gigi Depan") || dentPict.equals("Gigi Samping")) {
                colorSlider1.setLowValue(123);
                colorSlider2.setLowValue(138);
                colorSlider3.setLowValue(114);

                colorSlider1.setHighValue(255);
                colorSlider2.setHighValue(254);
                colorSlider3.setHighValue(255);
            } else if(dentPict.equals("Gigi Atas")) {
                colorSlider1.setLowValue(93);
                colorSlider2.setLowValue(113);
                colorSlider3.setLowValue(75);

                colorSlider1.setHighValue(255);
                colorSlider2.setHighValue(255);
                colorSlider3.setHighValue(255);
            } else{
                colorSlider1.setLowValue(165);
                colorSlider2.setLowValue(114);
                colorSlider3.setLowValue(101);

                colorSlider1.setHighValue(255);
                colorSlider2.setHighValue(255);
                colorSlider3.setHighValue(255);
            }


            colorHighVal1.textProperty().bind(colorSlider1.highValueProperty().asString(format));
            colorHighVal2.textProperty().bind(colorSlider2.highValueProperty().asString(format));
            colorHighVal3.textProperty().bind(colorSlider3.highValueProperty().asString(format));

            colorLowVal1.textProperty().bind(colorSlider1.lowValueProperty().asString(format));
            colorLowVal2.textProperty().bind(colorSlider2.lowValueProperty().asString(format));
            colorLowVal3.textProperty().bind(colorSlider3.lowValueProperty().asString(format));
        }
    }

    public void resetImage(ActionEvent event) {
        Image image = SwingFXUtils.toFXImage(originalImage.getData(),null);
        tempImage = new MyImage(originalImage);
        imageView.setImage(image);
    }

    public void clearNoise(){
        MyImage image = new MyImage(SwingFXUtils.fromFXImage(imageView.getImage(),null));
        NoiseClearer noiseClearer = new NoiseClearer(image,originalImage);
        image = noiseClearer.clear();
        tempImage = new MyImage(image);
        imageView.setImage(SwingFXUtils.toFXImage(image.getData(),null));
        imagePane.setContent(imageView);
    }

    public void setRGBChannel(){
        String channel = RGBChannel.getValue().toString();
        MyImage image = new MyImage(tempImage);
        image = ImgFilterer.getRGBChannel(image,channel);
        imageView.setImage((SwingFXUtils.toFXImage(image.getData(),null)));
    }

    public void setYCbCrChannel(){
        String channel = YCbCrChannel.getValue().toString();
        MyImage image = new MyImage(tempImage);
        image = ImgFilterer.getYCbCrChannel(image,channel);
        imageView.setImage((SwingFXUtils.toFXImage(image.getData(),null)));
    }

    public void setHSVChannel(){
        String channel = HSVChannel.getValue().toString();
        MyImage image = new MyImage(tempImage);
        image = ImgFilterer.getHSVChannel(image,channel);
        imageView.setImage((SwingFXUtils.toFXImage(image.getData(),null)));
    }

    public void binarize(){
        MyImage image = getImage();
        int threshold = OtsuBinaryConverter.countThreshold(image.getWidth()*image.getHeight(),HistogramProcessor.getRedChannelHistogram(image));
        image = OtsuBinaryConverter.convertImageRed(image,threshold);
        setImage(image);
    }


}
