package org.aursir.aursir4j;

import akka.actor.ActorRef;
import com.google.gson.Gson;
import org.aursir.aursir4j.messages.Request;
import org.aursir.aursir4j.messages.Result;
import org.zeromq.ZMQ;

import java.util.UUID;

/**
 * Created by joern on 1/25/15.
 */
public class ImportedAppkey {

    public String importid;

    private ZMQ.Context ctx;
    private ZMQ.Socket listenskt;

    private ActorRef outChan;

    private AppKey appKey;

    private String[] tags;

    public ImportedAppkey(String importid, AppKey appKey, String[] tags,ZMQ.Context ctx, ActorRef outChan){
        this.importid = importid;
        this.outChan = outChan;
        this.appKey = appKey;
        this.tags = tags;
        this.ctx = ctx;
        this.listenskt = ctx.socket(ZMQ.PAIR);
        this.listenskt.connect("inproc://listen" + importid);
    }

    public Result Listen(){
        String reqm = this.listenskt.recvStr();
        Gson gson = new Gson();
        return gson.fromJson(reqm, Result.class);
    }

    public Request CallFunction(String functionName,Object request, int callType) {

        String uuid = UUID.randomUUID().toString();

        ZMQ.Socket resultskt = ctx.socket(ZMQ.PAIR);
        resultskt.bind("inproc://result" + uuid);

        Request req = new Request(this.appKey.ApplicationKeyName,functionName,uuid,this.importid,
                callType,this.tags, request,resultskt);

        this.outChan.tell(req, ActorRef.noSender());
        return req;
    }

    public void Remove() {
        this.listenskt.close();
        System.out.println("imp closed");

    }
}

