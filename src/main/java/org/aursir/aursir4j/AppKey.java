package org.aursir.aursir4j;


import com.google.gson.Gson;

public class AppKey {

    public String ApplicationKeyName;
    public Function[] Functions;

    public class Function{
        public String Name;

        public Data[] Input;
        public Data[] Output;

        public class Data {
            public String Name;
            public int Type;
        }
    }

    public AppKey fromJson(String json) {
        Gson gson = new Gson();
        AppKey key = gson.fromJson(json, AppKey.class);
        return key;
    }

}

