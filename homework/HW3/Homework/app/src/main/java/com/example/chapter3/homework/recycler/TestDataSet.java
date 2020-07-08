package com.example.chapter3.homework.recycler;

import java.util.ArrayList;
import java.util.List;

public class TestDataSet {

    public static List<TestData> getData() {
        List<TestData> result = new ArrayList();
        result.add(new TestData("我", "文件","11:15"));
        result.add(new TestData("林丹", "hhh","昨天"));
        result.add(new TestData("汛哥？", "打扰了","昨天"));
        result.add(new TestData("阿伦", "常波","昨天"));
        result.add(new TestData("老师", "vivado又出错了","昨天"));
        result.add(new TestData("爸", "吃饭没","昨天"));
        result.add(new TestData("老虎", "啊呜","昨天"));
        result.add(new TestData("妈", "在干嘛","前天"));
        result.add(new TestData("大哥", "Gun","前天"));
        result.add(new TestData("姐姐", "周末看电影吗","前天"));
        result.add(new TestData("好看", "gun","前天"));
        return result;
    }

}
