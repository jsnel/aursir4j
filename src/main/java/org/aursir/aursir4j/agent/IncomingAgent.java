package org.aursir.aursir4j.agent;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import com.google.gson.Gson;
import org.aursir.aursir4j.messages.Calltypes;
import org.aursir.aursir4j.messages.DockedMessage;
import org.zeromq.ZMQ;

import java.net.ServerSocket;
import java.util.Map;


/**
 * Created by joern on 1/25/15.
 */
public class IncomingAgent extends UntypedActor {

    private ZMQ.Context ctx;
    private ZMQ.Socket skt;
    private int port ;

    private ZMQ.Socket dockskt;
    private ZMQ.Socket addexportskt;
    private ZMQ.Socket addimportskt;
    private Map exportedskts;
    private Map resultskts;
    private Map requestskts;


    public static Props props(final ZMQ.Context ctx, final int port) {
        return Props.create(new Creator<IncomingAgent>() {
            private static final long serialVersionUID = 1L;

            @Override
            public IncomingAgent create() throws Exception {
                return new IncomingAgent(ctx,port);
            }
        });
    }

    public IncomingAgent(final ZMQ.Context ctx, final  int port){
        this.ctx = ctx;
        this.port = port;
    }
    private void setupChannels(){
        this.dockskt = this.ctx.socket(ZMQ.PAIR);
        this.dockskt.connect("inproc://dock");
        this.addexportskt = this.ctx.socket(ZMQ.PAIR);
        this.addexportskt.connect("inproc://addexport");
        this.addimportskt = this.ctx.socket(ZMQ.PAIR);
        this.addimportskt.connect("inproc://addimport");
    }
    @Override
    public void preStart() throws Exception {
        this.skt = this.ctx.socket(ZMQ.ROUTER);
        String connection = "tcp://127.0.0.1:"+String.format("%d",this.port);
        System.out.println(connection);
        this.skt.bind(connection);
        this.setupChannels();
    }

    @Override
    public void onReceive(Object message) throws Exception {

        String senderid = this.receiveMsgPart();
        String msgtypestring = this.receiveMsgPart();
        String codec = this.receiveMsgPart();
        String encmsg = this.receiveMsgPart();
        this.receiveMsgPart();
        this.receiveMsgPart();
        this.receiveMsgPart();
        System.out.println(encmsg);
        this.processMsg(msgtypestring,codec,encmsg);
        getSelf().tell("",getSelf());
    }

    private String receiveMsgPart(){
        byte[] encmsg = this.skt.recv();
        return new String(encmsg);

    }

    private void processMsg(String msgtyp,String codec, String msg){
        Gson gson = new Gson();
        int type = Integer.parseInt(msgtyp);
        switch (Calltypes.types.values()[type]){
            case DOCKED:
                DockedMessage m = gson.fromJson(msg,DockedMessage.class);
                String s = "0";
                if (m.Ok)  {
                s= "1";
            }
            this.dockskt.send(s.getBytes());
        }

    }
}
