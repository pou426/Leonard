package vision.detection;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import vision.ImageManipulatorWithOptions;
import vision.TitledComponent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Properties;

import static vision.utils.Converter.binaryImageToMat;
import static vision.utils.Converter.matToBinaryImage;

/**
 * Created by Ivan Georgiev (s1410984) on 02/02/17.
 * An image dilation manipulation
 */
public class DilateImage extends ImageManipulatorWithOptions implements ChangeListener {

    private static final String DILATION_PROP = "Dilation";

    private JSlider dilationSlider = new JSlider(1, 20, 1);
    private Component gui = new TitledComponent("Dilation: ", dilationSlider);

    private static int DILATION_SIZE = 1;

    public DilateImage() {
        dilationSlider.addChangeListener(this);
    }


    @Override
    protected Component getModificationGUI() {
        return gui;
    }

    @Override
    protected void saveModificationSettings(Properties prop) {
        prop.setProperty(DILATION_PROP, String.valueOf(DILATION_SIZE));
    }

    @Override
    protected void loadModificationSettings(Properties prop) {
        DILATION_SIZE = Integer.valueOf(prop.getProperty(DILATION_PROP, String.valueOf(DILATION_SIZE)));
    }

    @Override
    protected BufferedImage run(BufferedImage input) {
        Mat image = binaryImageToMat(input);
        Mat dilatedImage = new Mat();
        Mat dilationElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new Size(DILATION_SIZE, DILATION_SIZE));
        Imgproc.dilate(image, dilatedImage, dilationElement);
        return matToBinaryImage(dilatedImage);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider slider = (JSlider) e.getSource();
        DILATION_SIZE = slider.getValue();
    }
}