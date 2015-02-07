package example.HelloAurSir;


import example.HelloAurSir.HelloAurSirAppkey;
import org.aursir.aursir4j.ImportedAppkey;
import org.aursir.aursir4j.Interface;
import org.aursir.aursir4j.calltypes;
import org.aursir.aursir4j.messages.Result;

public class ImporterMANY2ONE {



    public static void main(String[] args) {

        Interface iface = new Interface("TestImporter");

        ImportedAppkey imp = iface.AddImport(HelloAurSirAppkey.Get());

        imp.listenToFunction("SayHello");

        HelloAurSirAppkey.SayHelloRequest sayHelloRequest = new HelloAurSirAppkey.SayHelloRequest("Hi from AurSir4j");
        imp.CallFunction("SayHello", sayHelloRequest, calltypes.MANY2ONE.ordinal());
        Result res = imp.Listen();
        HelloAurSirAppkey.SayHelloReply rep = res.Decode(HelloAurSirAppkey.SayHelloReply.class);


        System.out.println("Result");
        System.out.println(rep.Answer);


        iface.stop();


    }



}


