package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.UpdateUserInfoCase;
import com.course.model.User;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class UpdateUserInfoTest {

    @Test(dependsOnGroups = "loginTrue",description = "更改用户信息")
    public void updateUserInfoTest() throws IOException {

        SqlSession sqlSession = DatabaseUtil.getSqlSession();

        UpdateUserInfoCase updateUserInfocase = sqlSession.selectOne("updateUserInfoCase", 1);

        System.out.println(updateUserInfocase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);


        int result = getResult(updateUserInfocase);
        User user = sqlSession.selectOne(updateUserInfocase.getExpected(),updateUserInfocase);
        Assert.assertNotNull(user);
        Assert.assertNotNull(result);

    }




    @Test(dependsOnGroups = "loginTrue",description = "更改用户信息")
    public void deleteUser() throws IOException {

        SqlSession sqlSession = DatabaseUtil.getSqlSession();

        UpdateUserInfoCase updateUserInfocase = sqlSession.selectOne("updateUserInfoCase", 1);

        System.out.println(updateUserInfocase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);

        int result = getResult(updateUserInfocase);
        User user = sqlSession.selectOne(updateUserInfocase.getExpected(),updateUserInfocase);
        Assert.assertNotNull(user);
        Assert.assertNotNull(result);




    }
    private int getResult(UpdateUserInfoCase updateUserInfocase) throws IOException {

        HttpPost post = new HttpPost(TestConfig.updateUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id",updateUserInfocase.getId());
        param.put("name",updateUserInfocase.getUserName());
        param.put("age",updateUserInfocase.getAge());
        param.put("sex",updateUserInfocase.getSex());
        param.put("permissoin",updateUserInfocase.getPermission());
        param.put("isDelete",updateUserInfocase.getIsDelete());

        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setHeader("content-type","application/json");
        post.setEntity(entity);
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.store);

        String result;
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        result = EntityUtils.toString(response.getEntity(),"utf-8");



        return Integer.parseInt(result);
    }

}
