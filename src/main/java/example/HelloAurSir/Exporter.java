package example.HelloAurSir;


import org.aursir.aursir4j.AppKey;
import org.aursir.aursir4j.ExportedAppkey;
import org.aursir.aursir4j.Interface;
import org.aursir.aursir4j.messages.Request;

public class Exporter {



    public static void main(String[] args) {

        Interface iface = new Interface("Testapp");

        AppKey key = new AppKey();

        ExportedAppkey exp = iface.AddExport(HelloAurSirAppkey.Get());


        Request request = exp.WaitForRequest();
        System.out.println("REQUEST!!!");

        HelloAurSirAppkey.SayHelloRequest sayHelloRequest = request.Decode(HelloAurSirAppkey.SayHelloRequest.class);
        System.out.println(sayHelloRequest.Greeting);

        exp.Reply(request, new HelloAurSirAppkey.SayHelloReply("Grettings back from AurSir4j"));

        System.out.println("Stopping");

       iface.stop();
    }


}


