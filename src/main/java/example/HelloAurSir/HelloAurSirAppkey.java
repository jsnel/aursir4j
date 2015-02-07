package example.HelloAurSir;

import org.aursir.aursir4j.AppKey;

/**
 * Created by joern on 2/7/15.
 */
public class HelloAurSirAppkey {
    public static AppKey Get() {
        String testkey = "{\n" +
                "    \"ApplicationKeyName\":\"org.aursir.helloaursir\",\n" +
                "    \"Functions\":[\n" +
                "        {\"Name\":\"SayHello\",\n" +
                "        \"Input\":[\n" +
                "            {\"Name\":\"Greeting\",\"Type\":1}\n" +
                "            ],\n" +
                "        \"Output\":[\n" +
                "            {\"Name\":\"Answer\",\"Type\":1}\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        AppKey key = AppKey.fromJson(testkey);
        return key;
    }

    public static class SayHelloRequest {
        public String Greeting;

        public SayHelloRequest(String greeting){
            this.Greeting = greeting;
        }
    }

    public static class SayHelloReply {
        public String Answer;

        public SayHelloReply(String answer){
            this.Answer = answer;
        }
    }


}
