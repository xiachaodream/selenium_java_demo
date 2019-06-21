package com.imooc.selenium.selenium;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.internal.WrapsDriver;

public class Test {

    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver", "D://driver//chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        /*
         * driver.manage().window().maximize(); driver.get("C://Users//51testing//Desktop//123.html"); WebElement
         * searchBox = driver.findElement(By.id("kw")); searchBox.sendKeys("51Testing"); WebElement searchButton =
         * driver.findElement(By.id("su")); searchButton.submit(); // 模拟鼠标操作 Actions action = new Actions(driver);
         * action.contextClick(driver.findElement(By.id("kw"))).perform();
         */

        // driver.close();
        driver.manage().window().maximize();
        driver.get("https://www.cnfuzheng.com/server");
        WebElement nameInput = driver.findElement(By.id("yhMc"));
        nameInput.sendKeys("admin");
        WebElement password = driver.findElement(By.id("yhMm"));
        password.sendKeys("FZ888888");
        WebElement ValidCode = driver.findElement(By.id("yzm"));
        WebElement webElement = driver.findElement(By.id("num"));
        File file = captureElement(webElement);
        FileUtils.copyFile(file, new File(new TestOrc().testResourcesDataPath, file.getName()));
        String str = new TestOrc().testDoOCR_SkewedImage(file.getName());
        System.out.println(str);// 得到验证码
        ValidCode.sendKeys(str);
        WebElement submitBtn = driver.findElement(By.id("loginBtn"));
        submitBtn.click();
        if (str.length() != 4) {

        }
        // Alert alert = driver.switchTo().alert();

    }

    public static File captureElement(WebElement element) throws Exception {
        // TODO Auto-generated method stub
        WrapsDriver wrapsDriver = (WrapsDriver) element;
        // 截图整个页面
        File screen = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
        BufferedImage img = ImageIO.read(screen);
        // 获得元素的高度和宽度
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();
        // 创建一个矩形使用上面的高度，和宽度
        Rectangle rect = new Rectangle(width, height);
        // 得到元素的坐标
        Point p = element.getLocation();
        BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
        // 存为png格式
        ImageIO.write(dest, "png", screen);
        return screen;
    }

}
