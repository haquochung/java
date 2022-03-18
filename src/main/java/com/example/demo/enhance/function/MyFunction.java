package com.example.demo.enhance.function;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class MyFunction {
    private static final Map<String, Function<MyBean, String>> functions = new HashMap<>();

    public static void main(String[] args) {
        functions.put("print_it", MyBean::toString);
        functions.put("print_id", bean -> Integer.toString(bean.getId()));
        functions.put("id_to_hex", bean -> {
            int i = bean.getId();
            return String.format("0x%x%n", i);
        });

        String s = doFunctionWork(new MyBean(16), "id_to_hex");
        System.out.println(s); // 0x10

        Consumer<String> simpleInterface = System.out::println;
        simpleInterface.accept("abc");
    }

    public static String doFunctionWork(MyBean myBean, String functionKey) {
        return functions.get(functionKey).apply(myBean);
    }

    public static class MyBean {

        private final int id;

        public MyBean(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

    }
}
