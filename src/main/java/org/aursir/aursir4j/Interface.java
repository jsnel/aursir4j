package org.aursir.aursir4j;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.aursir.HelloWorld;
import org.aursir.aursir4j.agent.IncomingAgent;
import org.aursir.aursir4j.agent.OutgoingAgent;
import org.aursir.aursir4j.messages.AddExportMessage;
import org.aursir.aursir4j.messages.AddImportMessage;
import org.aursir.aursir4j.messages.DockMessage;
import org.zeromq.ZMQ;

import javax.lang.model.element.Name;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.UUID;

/**
 * Created by joern on 1/24/15.
 */
public class Interface {

    private final UUID uuid = UUID.randomUUID();
    ;
    private String name;
    private int port;
    private ActorSystem system;
    private final ZMQ.Context ctx = ZMQ.context(1);


    private ActorRef outChan;
    private ActorRef inChan;

    private ZMQ.Socket dockskt;
    private ZMQ.Socket addexportskt;
    private ZMQ.Socket addimportskt;
    private Map exportedskts;
    private Map resultskts;
    private Map requestskts;

    public Interface(final String name){
      //  this.inChan = ctx.socket(ZMQ.PAIR);
       // this.outChan = ctx.socket(ZMQ.PAIR);
        this.setupChannels();
        try {
            this.launchActor(name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.dock();
        String docked = this.dockskt.recvStr();
        System.out.println(docked);

    }

    public ExportedAppkey AddExport(AppKey key, String[] tags){
        AddExportMessage m = new AddExportMessage(key,tags);
        this.outChan.tell(m, ActorRef.noSender());
        String eid = this.addexportskt.recvStr();
        ExportedAppkey ea = new ExportedAppkey(eid);
        return ea;
    }

    public ImportedAppkey AddImport(AppKey key, String[] tags){
        AddImportMessage m = new AddImportMessage(key,tags);
        this.outChan.tell(m, ActorRef.noSender());
        String iid = this.addimportskt.recvStr();
        ImportedAppkey ia = new ImportedAppkey(iid);
        return ia;
    }

    private void setupChannels(){
        this.dockskt = this.ctx.socket(ZMQ.PAIR);
        this.dockskt.bind("inproc://dock");
        this.addexportskt = this.ctx.socket(ZMQ.PAIR);
        this.addexportskt.bind("inproc://addexport");
        this.addimportskt = this.ctx.socket(ZMQ.PAIR);
        this.addimportskt.bind("inproc://addimport");
    }

    private void dock() {
        DockMessage msg = new DockMessage(this.name,false);
        this.outChan.tell(msg, ActorRef.noSender());
    }


    private void launchActor(final String Name) throws IOException {
        this.system = ActorSystem.create("aursir4jcore");
        final int port = this.getFreePort();
        this.port = port;
        this.inChan = system.actorOf(IncomingAgent.props( this.ctx, port));
        this.outChan = system.actorOf(OutgoingAgent.props(this.uuid,this.ctx,port,"127.0.0.1"));
        this.inChan.tell("", ActorRef.noSender());

    }

    public void stop(){
        this.system.shutdown();
    }

    private int getFreePort() throws IOException {
        ServerSocket s = new ServerSocket(0);
         int port = s.getLocalPort();
        s.close();
        return port;
    }

}

