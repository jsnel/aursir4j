package example.exampleExporter;


import com.google.gson.Gson;
import org.aursir.aursir4j.AppKey;
import org.aursir.aursir4j.ExportedAppkey;
import org.aursir.aursir4j.Interface;
import org.aursir.aursir4j.messages.DockMessage;
import org.aursir.aursir4j.messages.Message;
import org.aursir.aursir4j.messages.Request;
import org.aursir.aursir4j.hellorrep;

public class Main {



    public static void main(String[] args) {

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

        Message msg = new DockMessage("testapp",false);
        System.out.println(msg.GetCodec());
        System.out.println(msg.GetMessageType());
        System.out.println(msg.GetEncoded());
        Gson gson = new Gson();
        DockMessage msg2 = gson.fromJson(msg.GetEncoded(), DockMessage.class);
        System.out.println(msg2.GetEncoded());
        Interface iface = new Interface("Testapp");

        AppKey key = new AppKey();

        AppKey HelloWorldKey = key.fromJson(testkey);
        ExportedAppkey exp = iface.AddExport(HelloWorldKey);
        Request req = exp.WaitForRequest();
        System.out.println("REQUEST!!!");
        System.out.println(req.Request);
        hellorreq hellomsg = req.Decode(hellorreq.class);
        System.out.println(hellomsg.Greeting);

        hellorrep rep = new hellorrep();
        rep.Answer = "Hello from Aursir4J";
        exp.Reply(req,rep);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping");

       iface.stop();
    }

    class hellorreq {
        public String Greeting;
    }


}


