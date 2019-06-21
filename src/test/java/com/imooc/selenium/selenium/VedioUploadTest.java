package com.imooc.selenium.selenium;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.util.ajax.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

/**
 * 视频上传接口的测试点如下： 1）writetoken（上传钥匙）正确，JSONRPC（视频信息）正确，Filedata（视频文件）正确，上传成功
 * 2）上传各种正确格式的视频（flv,avi,mpg,mp4,wmv,mov,3gp,asf等），上传成功，可正常播放 3）format=xml，上传成功，接口返回的数据为xml格式，视频可正常播放
 * 4）format=json，上传成功，接口返回的数据为json格式，视频可正常播放 5）format参数值不正确的情况 6）format参数值为空的情况 7）fileMd5（文件校验码）正确，上传成功，可正常播放
 * 8）fileMd5（文件校验码）不正确的情况，上传失败 9）fileMd5（文件校验码）为空的情况，上传失败 10）jsonp（函数名）的值正确,上传成功，可正常播放 11）jsonp（函数名）不正确的情况
 * 12）jsonp（函数名）为空的情况 13）cataid（视频分类编号）正确，上传成功，可正常播放 14）cataid（视频分类编号）不正确的情况 15）cataid（视频分类编号）为空的情况
 * 16）watermark（水印）正确，上传成功，可正常播放，水印正常显示 17）watermark（水印）不正确的情况 18）watermark（水印）为空的情况 19）JSONRPC里面没有视频标题，视频文件名包含中文的情况
 * 20）JSONRPC里面没有视频标题，视频文件名过长的情况 21）JSONRPC里面没有视频标签的情况 22）JSONRPC里面没有视频描述的情况 23）JSONRPC里面没有视频标题，标签，描述的情况（即JSONRPC={}）
 * 24）JSONRPC里面视频标题为空的情况： {"title": "", "tag":"标签yzc","desc":"描述yzc"} 25）JSONRPC里面视频标签为空的情况： {"title": "标题yzc",
 * "tag":"","desc":"描述yzc"} 26）JSONRPC里面视频描述为空的情况： {"title": "标题yzc", "tag":"标签yzc","desc":""} 27）JSONRPC参数值的格式错误，上传失败
 * 28）JSONRPC没有或者为空，上传失败 29）Filedata没有或者为空，上传失败 30）视频文件格式不符合要求（非视频文件），上传失败 31）视频文件过大，上传失败 32）视频标题过长，上传失败 33）视频标签过长，上传失败
 * 34）视频描述过长，上传失败 35）writetoken错误，上传失败 36）writetoken过期，上传失败 37）writetoken没有或者为空，上传失败
 * 38）fcharset为ISO-8859-1，JSONRPC里面没有视频标题，视频文件名包含中文，上传成功 39）fcharset参数值不正确的情况 40）fcharset参数值为空的情况
 * 41）luping（是否压缩）的值等于1的情况 42）luping（是否压缩）的值等于0的情况 43）luping（是否压缩）的值不正确的情况 44）luping（是否压缩）的值为空的情况
 * 
 * @author yangzc
 */
public class VedioUploadTest {

    private CloseableHttpClient client;
    private HttpPost            request;

    @DataProvider(parallel = false)
    public Object[][] data() {
        File file = new File("C:\\Users\\yangzc\\Desktop\\我的视频yzc.avi");
        StringBuilder title = new StringBuilder();
        StringBuilder tag = new StringBuilder();
        StringBuilder desc = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            title.append("标题");
            tag.append("标签");
            desc.append("描述");
        }
        Object[][] arr = {
                { "writetoken,JSONRPC,Filedata都正确的情况",
                        "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "{'title':'接口yzc','tag':'接口yzc','desc':'接口yzc'}", file,
                        "", "", "", "", "", "ISO-8859-1", "", "0" },
                { "format=xml的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "{'title':'接口yzc','tag':'接口yzc','desc':'接口yzc'}", file,
                        "", "", "", "xml", "", "ISO-8859-1", "", "0" },
                { "format=json的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "{}", file, "", "", "", "json", "", "", "", "0" },
                { "fileMd5值正确的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "{}", file, "", "", "", "json", "", "ISO-8859-1",
                        "A99F357D1F52EB8437E0572E98EBB012", "0" },
                { "fileMd5值错误的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "{}", file, "", "", "", "json", "", "",
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "14" },
                { "jsonp=callback的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "{}", file, "", "", "", "json", "callback", "", "", "1" },
                { "cataid值正确的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a", "{}",
                        file, "1557730808709", "", "", "json", "",
                        "ISO-8859-1", "", "0" },
                { "cataid值错误的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a", "{}",
                        file, "0000000000000", "", "", "json", "",
                        "ISO-8859-1", "", "0" },
                {
                        "watermark值正确的情况",
                        "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "{}",
                        file,
                        "",
                        "http://www.w3school.com.cn/i/eg_dragdrop_w3school.gif",
                        "", "json", "", "", "", "0" },
                { "watermark值错误的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "{}", file, "", "w3school.gif", "", "json", "",
                        "ISO-8859-1", "", "0" },
                { "JSONRPC参数值的格式错误", "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "titile=接口yzc,tag=接口yzc,desc=接口yzc", file, "", "", "",
                        "json", "", "", "", "15" },
                { "JSONRPC为空的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a", "",
                        file, "", "", "", "json", "", "", "", "15" },
                { "Filedata为空的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "{}", null, "", "", "", "json", "", "", "", "1" },
                { "视频文件格式不符合要求（非视频文件）的情况",
                        "032bb8e3-c7ef-43af-9b01-813a002e0e4a", "{}",
                        new File("C:\\Users\\yangzc\\Desktop\\笔记0514.txt"), "",
                        "", "", "json", "", "", "", "4" },
                { "视频标题过长的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "{'title':'" + title + "'}", file, "", "", "", "json",
                        "", "", "", "15" },
                { "视频标签过长的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "{'tag':'" + tag + "'}", file, "", "", "", "json", "",
                        "", "", "15" },
                { "视频描述过长的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "{'desc':'" + desc + "'}", file, "", "", "", "json",
                        "", "", "", "15" },
                { "writetoken过期的情况", "f56c086c-a208-4a21-badf-bf06d70fe356",
                        "{}", file, "", "", "", "json", "", "", "", "17" },
                { "writetoken错误的情况", "00000000-0000-0000-0000-000000000000",
                        "{}", file, "", "", "", "json", "", "", "", "1" },
                { "fcharset为ISO-8859-1",
                        "032bb8e3-c7ef-43af-9b01-813a002e0e4a", "{}", file, "",
                        "", "", "json", "", "ISO-8859-1", "", "0" },
                { "fcharset为UTF-8", "032bb8e3-c7ef-43af-9b01-813a002e0e4a",
                        "{}", file, "", "", "", "json", "", "UTF-8", "", "0" },
                { "luping等于1的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a", "{}",
                        file, "", "", "1", "json", "", "ISO-8859-1", "", "0" },
                { "luping等于0的情况", "032bb8e3-c7ef-43af-9b01-813a002e0e4a", "{}",
                        file, "", "", "0", "json", "", "ISO-8859-1", "", "0" } };
        return arr;
    }

    @BeforeTest
    public void begin() {
        // 创建一个可关闭的客户端工具
        client = HttpClients.createDefault();
        // 创建一个Post请求
        request = new HttpPost(
                               "http://{接口服务器域名}/uc/services/rest?method=uploadfile");
    }

    @AfterTest
    public void end() throws Exception {
        // 关闭客户端
        client.close();
    }

    @Test(dataProvider = "data")
    public void f(String desc, String writetoken, String jsonrpc, File file,
                  String cataid, String watermark, String luping,
                  String format, String jsonp, String fcharset, String fileMd5,
                  String expected) throws Exception {
        // 构造发送的数据
        HttpEntity send_data = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532).addTextBody("writetoken",
                                                                                                              writetoken).addTextBody("JSONRPC",
                                                                                                                                      jsonrpc,
                                                                                                                                      ContentType.create("application/x-www-form-urlencoded",
                                                                                                                                                         "utf-8")).addBinaryBody("Filedata",
                                                                                                                                                                                 file,
                                                                                                                                                                                 ContentType.DEFAULT_BINARY,
                                                                                                                                                                                 file.getName()).addTextBody("cataid",
                                                                                                                                                                                                             cataid).addTextBody("watermark",
                                                                                                                                                                                                                                 watermark).addTextBody("luping",
                                                                                                                                                                                                                                                        luping).addTextBody("format",
                                                                                                                                                                                                                                                                            format).addTextBody("jsonp",
                                                                                                                                                                                                                                                                                                jsonp).addTextBody("fcharset",
                                                                                                                                                                                                                                                                                                                   fcharset).addTextBody("fileMd5",
                                                                                                                                                                                                                                                                                                                                         fileMd5).build();
        request.setEntity(send_data); // 将send_data设置为请求的正文
        HttpResponse response = client.execute(request); // execute方法会返回接口发送回来的数据

        /*
         * for(Header h:response.getAllHeaders()){ //打印响应头 System.out.println(h.toString()); }
         */

        // 打印响应正文
        // System.out.println(EntityUtils.toString(receive_data.getEntity()));

        if (format.equals("xml")) {
            Document doc = Jsoup.parse(EntityUtils.toString(response.getEntity()));
            Element element = doc.getElementsByTag("error").get(0);
            Assert.assertEquals(element.text(), expected, desc);
        } else {
            JSONObject obj = (JSONObject) JSON.parse(EntityUtils.toString(response.getEntity()));
            Assert.assertEquals(obj.get("error"), expected, desc);
        }

    }
}
