package com.example.demo.enhance.annotation;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@MyCustom
@Setter
@Data
public class TestAnnotation {

    @MyCustom
    private boolean test;

    @MyCustom
    public static void main(@MyCustom  String[] args) {
        @MyCustom int demo;
        TestAnnotation testAnnotation = new TestAnnotation();
        testAnnotation.isTest();
    }

    @MyCustom
    TestAnnotation() {

    }
}
