package com.imooc.selenium.selenium;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recognition.software.jdeskew.ImageDeskew;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.ImageHelper;
import net.sourceforge.tess4j.util.LoggHelper;

public class TestOrc {

    private static final Logger logger                    = LoggerFactory.getLogger(new LoggHelper().toString());

    static final double         MINIMUM_DESKEW_THRESHOLD  = 0.05d;

    private final String        datapath                  = "src/test/resources";
    final String                testResourcesDataPath     = "src/test/resources/test-data";
    private final String        testResourcesLanguagePath = "src/test/resources/tessdata";

    public String testDoOCR_SkewedImage(String file_name) throws Exception {
        Tesseract instance = new Tesseract();
        // 设置语言库
        instance.setDatapath(testResourcesLanguagePath);
        instance.setLanguage("chi_sim");

        logger.info("doOCR on a skewed PNG image");
        File imageFile = new File(testResourcesDataPath, file_name);
        BufferedImage bi = ImageIO.read(imageFile);
        ImageDeskew id = new ImageDeskew(bi);
        double imageSkewAngle = id.getSkewAngle(); // determine skew angle
        if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
            bi = ImageHelper.rotateImage(bi, -imageSkewAngle); // deskew image
        }

        String result = instance.doOCR(bi);
        logger.info(result);
        return result;
    }

    public static void main(String[] args) throws Exception {
        new TestOrc().testDoOCR_SkewedImage("screenshot7783418803801329344.png");

    }

}
