package org.aursir.aursir4j.agent;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import org.aursir.aursir4j.messages.LeaveMessage;
import org.aursir.aursir4j.messages.Message;
import org.zeromq.ZMQ;

import java.util.UUID;

/**
 * Created by joern on 1/25/15.
 */
public class OutgoingAgent extends UntypedActor {

    private ZMQ.Context ctx;
    private ZMQ.Socket skt;
    private int port;
    private String ip;
    private String uuid;

    public static Props props(final UUID uuid,final ZMQ.Context ctx,final int port, final String ip) {
        return Props.create(new Creator<OutgoingAgent>() {
            private static final long serialVersionUID = 1L;

            @Override
            public OutgoingAgent create() throws Exception {
                return new OutgoingAgent(uuid,ctx,port,ip);
            }
        });
    }

    public OutgoingAgent(final UUID uuid, final ZMQ.Context ctx,final int port, final String ip){
        this.ctx = ctx;
        this.port= port;
        this.ip= ip;
        this.uuid = uuid.toString();
    }

    @Override
    public void preStart() throws Exception {
        this.skt = this.ctx.socket(ZMQ.DEALER);
        this.skt.setIdentity(this.uuid.getBytes());
        this.skt.connect("tcp://127.0.0.1:5555");
        final ActorRef udp = getContext().actorOf(UdpActor.props(this.uuid));

        udp.tell("", getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        Message m = (Message) message;
        this.send(m);
    }

    @Override
    public void postStop() throws Exception {
        LeaveMessage m = new LeaveMessage();
        this.send(m);
        this.skt.close();
        System.out.println("Closed outgoing actor");

        super.postStop();
    }

    private void send(Message m) {
        this.skt.sendMore(String.format("%d",m.GetMessageType()));
        this.skt.sendMore(m.GetCodec());
        this.skt.sendMore(m.GetEncoded());
        this.skt.sendMore(String.format("%d", this.port));
        this.skt.send(this.ip);
    }
}
