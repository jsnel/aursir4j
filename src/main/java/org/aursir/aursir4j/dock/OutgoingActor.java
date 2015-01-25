package org.aursir.aursir4j.dock;

import akka.actor.UntypedActor;
import org.zeromq.ZMQ;

public class OutgoingActor extends UntypedActor {


    public class Msg {
        public ZMQ.Socket chan;
    };


    @Override
    public void onReceive(Object msg) {
        if (msg instanceof Msg){
            ZMQ.Socket tmp = (ZMQ.Socket) msg;
            byte[] inmsg = tmp.recv();

        } else
            unhandled(msg);
    }


}
