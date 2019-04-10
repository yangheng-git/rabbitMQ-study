package com.yanghx.rabbitmq.api;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yangHX
 * createTime  2019/4/10 10:58
 */
public class ForeachTest {

    public static void main(String[] args) {

        List<HashMap<String, String>> collect = Stream.generate(() -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("a", "b");
            return hashMap;
        }).limit(100000).collect(Collectors.toList());

        long startTime = System.currentTimeMillis();
        System.out.println(startTime);
        String list = collect.stream().map(item -> String.join(",", item.keySet())).collect(Collectors.joining(","));
        System.out.println(list);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime);
        System.out.println("时间差:" + (endTime - startTime));


        long startTime2 = System.currentTimeMillis();
        System.out.println(startTime2);
        StringBuilder builder = new StringBuilder();
        for (HashMap<String, String> hashMap : collect) {
            String join = String.join(",", hashMap.keySet());
            builder.append(join).append(",");
        }
        System.out.println(builder);
        long endTime2 = System.currentTimeMillis();
        System.out.println(endTime2);
        System.out.println("时间差:" + (endTime2 - startTime2));


        long startTime3 = System.currentTimeMillis();
        System.out.println(startTime3);
        StringBuilder builder3 = new StringBuilder();
        collect.stream().parallel().forEach(item -> {
            String join = String.join(",", item.keySet());
            builder3.append(join).append(",");
        });
        System.out.println(builder3);
        long endTime3 = System.currentTimeMillis();
        System.out.println(endTime3);
        System.out.println("时间差:" + (endTime3 - startTime3));
    }


}

class test2 {
    public static void main(String[] args) {
        List<HashMap<String, String>> collect = Stream.generate(() -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("a", "b");
            return hashMap;
        }).limit(100000).collect(Collectors.toList());





        long startTime = System.currentTimeMillis();
        System.out.println(startTime);
        StringBuilder string = collect.stream().parallel().collect(StringBuilder::new
                , (stringBuilder, map) -> {
                    String join = String.join(",", map.keySet());
                    stringBuilder.append(join).append(",");
                }, StringBuilder::append);

        System.out.println(string);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime);
        System.out.println("时间差:" + (endTime - startTime));
    }

}
