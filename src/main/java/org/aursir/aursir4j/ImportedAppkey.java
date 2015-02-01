package org.aursir.aursir4j;

import akka.actor.ActorRef;
import com.google.gson.Gson;
import org.aursir.aursir4j.messages.Request;
import org.aursir.aursir4j.messages.Result;
import org.zeromq.ZMQ;

/**
 * Created by joern on 1/25/15.
 */
public class ImportedAppkey {

    private String exportid;

    private ZMQ.Socket requestskt;

    private ActorRef outChan;


    public ImportedAppkey(String exportid, ZMQ.Context ctx, ActorRef outChan){
        this.exportid = exportid;
        this.outChan = outChan;
        this.requestskt = ctx.socket(ZMQ.PAIR);
        this.requestskt.connect("inproc://exportreq_" + exportid);
    }

    public Request WaitForRequest(){
        String reqm = this.requestskt.recvStr();
        Gson gson = new Gson();
        return gson.fromJson(reqm, Request.class);
    }

    public void Reply(Request request, Object reply) {
        this.outChan.tell(new Result(request, reply), ActorRef.noSender());
    }
}
