package com.jesper.seckill.bean;

import java.io.IOException;

/**
 * @author hongxingyi
 * @description TODO 测试FastJson 反序列化安全
 * @date 2022/5/27 11:32
 */
public class FastJsonUser {

    private String name;
    private int age;

    public FastJsonUser() throws IOException {
        Runtime.getRuntime().exec("notepad.exe");//启动记事本
    }

    public FastJsonUser(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    /**
     * @param name
     * @throws IOException
     */
    public void setName(String name) throws IOException {
        this.name = name;
        //Runtime.getRuntime().exec("notepad.exe");//启动记事本
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
