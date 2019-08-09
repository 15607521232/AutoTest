package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.InterfaceName;
import com.course.model.LoginCase;
import com.course.utils.ConfigFile;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTest {


    @BeforeTest(groups="loginTrue",description = "测试准备工作")
    public void beforeTest(){

        TestConfig.defaultHttpClient =  new DefaultHttpClient();

        TestConfig.getUserInfoUrl = ConfigFile.getUrl(InterfaceName.GETUSERINFO);

        TestConfig.loginUrl = ConfigFile.getUrl(InterfaceName.LOGIN);

        TestConfig.addUserUrl = ConfigFile.getUrl(InterfaceName.ADDUSERINFO);

        TestConfig.getUserListUrl = ConfigFile.getUrl(InterfaceName.GETUSERLIST);

        TestConfig.updateUserInfoUrl = ConfigFile.getUrl(InterfaceName.UPDATEUSERINFO);






    }


   @Test(groups = "loginTrue",description = "用户成功登陆接口")
    public void loginTrue() throws IOException {

        SqlSession session = DatabaseUtil.getSqlSession();

         LoginCase loginCase =session.selectOne("loginCase",1);

        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //发送请求
       String result = getResult(loginCase);



       //验证结果
       Assert.assertEquals(loginCase.getExpected(),result);


    }



    @Test(groups = "loginFalse",description = "用户登陆失败接口测试")
    public void loginFalse() throws IOException {

        SqlSession session = DatabaseUtil.getSqlSession();

        LoginCase loginCase =session.selectOne("loginCase",2);

        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //发送请求
        String result = getResult(loginCase);



        //验证结果
        Assert.assertEquals(loginCase.getExpected(),result);

    }


    private String getResult(LoginCase loginCase) throws IOException {

        HttpPost post = new HttpPost(TestConfig.loginUrl);
        JSONObject param = new JSONObject();
        param.put("userName",loginCase.getUserName());
        param.put("password",loginCase.getPassWord());
        //设置请求头信息 设置header
        post.setHeader("content-type","application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //声明一个对象来进行响应结果的存储
        String result;
        //执行post方法
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        TestConfig.store = TestConfig.defaultHttpClient.getCookieStore();
        return result;

    }


}
