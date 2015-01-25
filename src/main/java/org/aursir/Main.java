package org.aursir;


import com.google.gson.Gson;
import org.aursir.aursir4j.Interface;
import org.aursir.aursir4j.messages.DockMessage;
import org.aursir.aursir4j.messages.Message;

public class Main {

    public static void main(String[] args) {
        Message msg = new DockMessage("testapp",false);
        System.out.println(msg.GetCodec());
        System.out.println(msg.GetMessageType());
        System.out.println(msg.GetEncoded());
        Gson gson = new Gson();
        DockMessage msg2 = gson.fromJson(msg.GetEncoded(), DockMessage.class);
        System.out.println(msg2.GetEncoded());
        Interface iface = new Interface("Testapp");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping");

       // iface.stop();
    }


}


