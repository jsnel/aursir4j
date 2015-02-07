package example.HelloAurSir;


import example.HelloAurSir.HelloAurSirAppkey;
import org.aursir.aursir4j.ImportedAppkey;
import org.aursir.aursir4j.Interface;
import org.aursir.aursir4j.calltypes;
import org.aursir.aursir4j.messages.Request;
import org.aursir.aursir4j.messages.Result;

public class ImporterONE2ONE {



    public static void main(String[] args) {

        Interface iface = new Interface("TestImporter");

        ImportedAppkey imp = iface.AddImport(HelloAurSirAppkey.Get());

        HelloAurSirAppkey.SayHelloRequest sayHelloRequest = new HelloAurSirAppkey.SayHelloRequest("Hi from AurSir4j");
        Request req = imp.CallFunction("SayHello", sayHelloRequest, calltypes.ONE2ONE.ordinal());
        Result res = req.WaitForResult();
        HelloAurSirAppkey.SayHelloReply rep = res.Decode(HelloAurSirAppkey.SayHelloReply.class);


        System.out.println("Result");
        System.out.println(rep.Answer);


        iface.stop();


    }



}


