package com.wuxp.study.proto;

public class Test {


    public static void main(String[] args) throws Exception {
        PersonEntity.Person.Builder builder=PersonEntity.Person.newBuilder();
        PersonEntity.Person person = builder.setEmail("110818213@qq.com")
                .setId(1)
                .setName("张三")
                .build();
        byte[] bytes = person.toByteArray();

        PersonEntity.Person parseFrom = PersonEntity.Person.parseFrom(bytes);
        System.out.print(parseFrom.getName());
    }
}
