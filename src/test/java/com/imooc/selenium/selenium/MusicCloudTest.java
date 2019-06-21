package com.imooc.selenium.selenium;

import static org.testng.Assert.assertTrue;

import java.util.concurrent.*;

import org.testng.annotations.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class MusicCloudTest {

    private WebDriver driver;

    @DataProvider
    public Object[][] data1() {
        Object[][] arr = { { "非登录状态，发表评论失败", "https://music.163.com/#/song?id=29099236", By.className("area"), By.className("n-log2"), "手机号登录" } };
        return arr;
    }

    @Test(dataProvider = "data1")
    public void f1(String desc, String url, By by1, By by2, String expected) throws Exception {
        driver.get(url);
        // Thread.sleep(2000);
        driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='g_iframe']")));
        // 点击评论选项
        driver.findElement(by1).sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        // driver.findElement(by1).click();
        driver.switchTo().defaultContent();
        assertTrue(driver.findElement(by2).getText().contains(expected), desc);
    }

    @BeforeTest
    public void beforeTest() {
        // 设置IE浏览器驱动的路径
        System.setProperty("webdriver.chrome.driver", "D://driver//chromedriver.exe");

        // InternetExplorerOptions option = new InternetExplorerOptions();
        // option.requireWindowFocus();
        // driver = new InternetExplorerDriver(option);
        driver = new ChromeDriver();
        // System.setProperty("webdriver.gecko.driver", "d:\\drivers\\geckodriver.exe");
        // driver = new FirefoxDriver();
        // 设置默认的等待时长
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // 最大化浏览器窗口
        // driver.manage().window().maximize();
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }

}
