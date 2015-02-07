package example.exampleImporter;


import example.hellorrep;
import org.aursir.aursir4j.*;
import org.aursir.aursir4j.messages.*;
import example.hellorreq;
import org.zeromq.ZMQ;
import zmq.Pair;

public class Importertest {



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


        Interface iface = new Interface("Testapp");

        AppKey key = new AppKey();

        AppKey HelloWorldKey = key.fromJson(testkey);
        ImportedAppkey imp = iface.AddImport(HelloWorldKey);
        System.out.println(imp.importid);
        hellorreq hreq = new hellorreq("Hi from AurSir4j");
        Request req = imp.CallFunction("SayHello", hreq, calltypes.ONE2ONE.ordinal());
        Result res = req.WaitForResult();
        hellorrep rep = res.Decode(hellorrep.class);
        System.out.println("Result");
        System.out.println(rep.Answer);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping");

        iface.stop();

        System.out.println("Stopped");

    }



}


