package org.aursir.aursir4j.agent;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import org.zeromq.ZMQ;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.UUID;

/**
 * Created by joern on 1/25/15.
 */
public class UdpActor extends UntypedActor {

    private String uuid;
    private DatagramSocket clientSocket;
    private InetAddress IPAddress;

    public static Props props(final String uuid) {
        return Props.create(new Creator<UdpActor>() {
            private static final long serialVersionUID = 1L;

            @Override
            public UdpActor create() throws Exception {
                return new UdpActor(uuid);
            }
        });
    }

    public UdpActor(final String uuid){
         this.uuid = uuid;
    }

    @Override
    public void preStart() throws Exception {
        this.clientSocket = new DatagramSocket();
        this.IPAddress = InetAddress.getByName("127.0.0.1");
    }

    @Override
    public void onReceive(Object message) throws Exception {

        DatagramPacket sendPacket = new DatagramPacket(
                this.uuid.getBytes(),
                this.uuid.getBytes().length,
                this.IPAddress,
                5557);
        this.clientSocket.send(sendPacket);

        Thread.sleep(1000);
        getSelf().tell("",getSelf());

    }


}
