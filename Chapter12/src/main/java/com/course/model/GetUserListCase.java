package com.course.model;


import lombok.Data;

@Data
public class GetUserListCase {
    private String userName;
    private String passWord;
    private String sex;
    private String age;
    private String expected;
}
