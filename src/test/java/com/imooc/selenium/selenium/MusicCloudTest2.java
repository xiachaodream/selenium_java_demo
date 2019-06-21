package com.imooc.selenium.selenium;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MusicCloudTest2 {

    private WebDriver driver;

    @DataProvider(parallel = false)
    public Object[][] data2() {
        Object[][] arr = {
                { "输入的内容是HTML代码", "https://music.163.com/#/song?id=22006167",
                        By.className("area"), By.className("u-btn-1"),
                        By.className("sysmsg"), By.id("g_iframe"),
                        "<h1>好听，不錯哦</h1>", "评论成功" },
                { "输入的内容是JS代码", "https://music.163.com/#/song?id=22006168",
                        By.className("area"), By.className("u-btn-1"),
                        By.className("sysmsg"), By.id("g_iframe"),
                        "<script>alert('好听，不錯哦')</script>", "评论成功" },
                { "输入的内容是合法的", "https://music.163.com/#/song?id=22006169",
                        By.className("area"), By.className("u-btn-1"),
                        By.className("sysmsg"), By.id("g_iframe"), "好听，不錯哦",
                        "评论成功" },
                { "输入的内容包含换行符", "https://music.163.com/#/song?id=22006170",
                        By.className("area"), By.className("u-btn-1"),
                        By.className("sysmsg"), By.id("g_iframe"),
                        "好听，不錯哦！\n" + "好听，不錯哦！\n", "评论成功" },
                { "输入的内容包含表情", "https://music.163.com/#/song?id=22006171",
                        By.className("area"), By.className("u-btn-1"),
                        By.className("sysmsg"), By.id("g_iframe"),
                        "[强][强][强]\n" + "好听，不錯哦！\n", "评论成功" },
                { "输入的内容包含@{账号}", "https://music.163.com/#/song?id=22006172",
                        By.className("area"), By.className("u-btn-1"),
                        By.className("sysmsg"), By.id("g_iframe"),
                        "@云音乐小秘书 厉害了\n" + "好听，不錯哦！\n", "评论成功" } };
        return arr;
    }

    @DataProvider(parallel = false)
    public Object[][] data3() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 300; i++) {
            sb.append("1");
        }
        Object[][] arr = {
                { "输入的内容是空，发表评论失败", "https://music.163.com/#/song?id=17673505",
                        By.className("area"), By.className("u-btn-1"),
                        By.className("sysmsg"), By.xpath("//iframe[1]"), "",
                        "输入点内容再提交" },
                { "输入的内容是空格，发表评论失败",
                        "https://music.163.com/#/song?id=17673505",
                        By.className("area"), By.className("u-btn-1"),
                        By.className("sysmsg"), By.xpath("//iframe[1]"), "   ",
                        "输入点内容再提交" },
                { "输入的内容过长，发表评论失败", "https://music.163.com/#/song?id=17673505",
                        By.className("area"), By.className("u-btn-1"),
                        By.className("sysmsg"), By.xpath("//iframe[1]"),
                        sb.toString(), "输入不能超过140个字符" },
                { "输入的内容包含特殊字符", "https://music.163.com/#/song?id=17673505",
                        By.className("area"), By.className("u-btn-1"),
                        By.className("sysmsg"), By.xpath("//iframe[1]"),
                        "@好听，不錯哦！@#￥%&*<>\n';,{}[]=+" + "好听，不錯哦！\n", "评论成功" },
                { "输入的内容包含qq号码，发表评论失败",
                        "https://music.163.com/#/song?id=17673505",
                        By.className("area"), By.className("u-btn-1"),
                        By.className("sysmsg"), By.xpath("//iframe[1]"),
                        "发票，QQ876295854", "评论成功" },
                { "输入的内容包含手机号码，发表评论失败",
                        "https://music.163.com/#/song?id=17673505",
                        By.className("area"), By.className("u-btn-1"),
                        By.className("sysmsg"), By.xpath("//iframe[1]"),
                        "发票，17721038951", "评论成功" } };
        return arr;
    }

    @Test(dataProvider = "data2", enabled = true)
    public void f2(String desc, String url, By by6, By by7, By by8, By by10,
                   String content, String expected) throws Exception {
        driver.get(url);
        Thread.sleep(2000);
        // driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(by10));
        List<WebElement> elements = driver.findElements(By.className("u-hd4"));
        int index = elements.size() - 1;
        String comments_count = elements.get(index).getText();// 最新评论数
        driver.findElement(By.className("u-btni-cmmt")).sendKeys(Keys.ENTER);// 点击评论选项
        Thread.sleep(2000);
        driver.findElement(by6).sendKeys(content);// 输入评论的内容
        driver.findElement(by7).sendKeys(Keys.ENTER);// 点击发布评论
        System.out.println(driver.findElement(by8).getText());
        assertTrue(driver.findElement(by8).getText().contains(expected), desc);
        Thread.sleep(2000);
        JavascriptExecutor exec = (JavascriptExecutor) driver;
        for (int i = 0; i < 2; i++) {
            exec.executeScript("scrollBy(0, 600)");
            Thread.sleep(1000);
        }
        driver.navigate().refresh();
        Thread.sleep(3000);
        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(by10));
        elements = driver.findElements(By.className("u-hd4"));
        index = elements.size() - 1;
        String comments_count2 = elements.get(index).getText();// 最新评论数
        System.out.println(comments_count2);
        assertFalse(comments_count.equals(comments_count2));// 验证最新评论数不同
    }

    @Test(dataProvider = "data3", enabled = true)
    public void f3(String desc, String url, By by6, By by7, By by8, By by10,
                   String content, String expected) throws Exception {
        driver.get(url);
        Thread.sleep(2000);
        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(by10));
        List<WebElement> elements = driver.findElements(By.className("u-hd4"));
        int index = elements.size() - 1;
        String comments_count = elements.get(index).getText();// 最新评论数
        System.out.println(comments_count);
        driver.findElement(By.className("u-btni-cmmt")).sendKeys(Keys.ENTER);// 点击评论选项
        Thread.sleep(2000);
        driver.findElement(by6).sendKeys(content);// 输入评论的内容
        driver.findElement(by7).sendKeys(Keys.ENTER);// 点击发布评论
        System.out.println(driver.findElement(by8).getText());
        assertTrue(driver.findElement(by8).getText().contains(expected), desc);
        Thread.sleep(2000);
        driver.navigate().refresh();
        // String html = driver.getPageSource();
        // System.out.println(html);
        Thread.sleep(3000);
        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(by10));
        elements = driver.findElements(By.className("u-hd4"));
        index = elements.size() - 1;
        String comments_count2 = elements.get(index).getText();// 最新评论数
        System.out.println(comments_count2);
        assertEquals(comments_count, comments_count2);// 验证最新评论数相同
    }

    @BeforeTest
    public void beforeTest() throws Exception {
        // 设置IE浏览器驱动的路径
        System.setProperty("webdriver.chrome.driver",
                           "D://driver//chromedriver.exe");

        // InternetExplorerOptions option = new InternetExplorerOptions();
        // option.requireWindowFocus();
        // driver = new InternetExplorerDriver(option);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // System.setProperty("webdriver.gecko.driver", "d:\\drivers\\geckodriver.exe");
        // driver = new FirefoxDriver();
        // 设置默认的等待时长
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // 最大化浏览器窗口
        // driver.manage().window().maximize();
        //
        driver.get("https://music.163.com/#/song?id=17673505");
        Thread.sleep(2000);
        Cookie cookie1 = new Cookie("__csrf",
                                    "34ed4ebefedea32864da98eee22bd58e", "/",
                                    null);
        Cookie cookie2 = new Cookie("_iuqxldmzr_", "32", "/", null);
        Cookie cookie3 = new Cookie(
                                    "_ntes_nnid",
                                    "564a08b10bd96b3c1e0ecdaae38e91d4,1560934220899",
                                    "/", null);
        Cookie cookie4 = new Cookie("_ntes_nuid",
                                    "564a08b10bd96b3c1e0ecdaae38e91d4", "/",
                                    null);
        Cookie cookie5 = new Cookie(
                                    "JSESSIONID-WYYY",
                                    "UysXh8o150I4JrAs3lt%2FwrCTmeCpwcisQjCQusgYlpCeFo8s%2FhY6BGe%2BEoxYb8iwmcx2jDh1jhXYXhvcKpQAh4nv8kgjH5r7SoNiqqWkkupPjVJgdUEqBsHgOwBK%2BsHt%2FCD%2BKMi8QHAcMIZQ2EI0SFguHeN%2Bvia0b818SeppNf%2Fl2xGq%3A1561012002852",
                                    "/", null);
        Cookie cookie6 = new Cookie(
                                    "MUSIC_U",
                                    "5c74e3573cca17ac69037d4c772c3057c97596dfabf6c42c6b4f203ce6bafcf8805e280db6c48d8fc2f5d6778cb813634dd3177448df7f618da74aebd92589768a0ebbcec2b3aa46f2f513a9c38b5dc7",
                                    "/", null);
        Cookie cookie7 = new Cookie("WM_TID",
                                    "nEYMUZVa6ShABAVERAI8myn9%2BL0IbX7E", "/",
                                    null);
        // Cookie cookie8 = new
        // Cookie("WM_NI","9mKBslD3uMhXh39HIyhUbLjHnnQheFOBtrZr3GMdU%2FjlaH5KncNzqnZCoDHMv1lEw5cmBUTkhyZlloiRJYBO74uSTcooi8zdluJRd9ur8NshcJlrFYNvKGBjDDJrzuFWQ1A%3D","/",null);
        // Cookie cookie9 = new
        // Cookie("WM_NIKE","9ca17ae2e6ffcda170e2e6eeaeb359a2eaac8af772ede78ab6c15b838b9aafb8638ba9a7d8d05d878ca089f72af0fea7c3b92afbb385a3f26888edbbaeeb54acedf7a9cc73ac8baeade944aeb29f89e84e8d9ebc93d97af1998385d825aab4afa9b841aebd829bf4669a9da1a5cb47b6efa4a6f63fa1b38eb6d761aca7968def65ab8e86bac145f5ecbbb4d769adb1a7d9f970b0ab86b3e733b7aef8aff264a196a493c5538eaa85a8c162889c8693f97f86899fd2c437e2a3","/",null);
        driver.manage().addCookie(cookie1);
        driver.manage().addCookie(cookie2);
        driver.manage().addCookie(cookie3);
        driver.manage().addCookie(cookie4);
        driver.manage().addCookie(cookie5);
        driver.manage().addCookie(cookie6);
        driver.manage().addCookie(cookie7);
        // driver.manage().addCookie(cookie8);
        // driver.manage().addCookie(cookie9);
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }

}
