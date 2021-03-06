package org.aursir.aursir4j;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.aursir.aursir4j.agent.IncomingAgent;
import org.aursir.aursir4j.agent.OutgoingAgent;
import org.aursir.aursir4j.messages.AddExportMessage;
import org.aursir.aursir4j.messages.AddImportMessage;
import org.aursir.aursir4j.messages.DockMessage;
import org.zeromq.ZMQ;
import scala.util.parsing.combinator.testing.Str;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
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

    private Map<String,ImportedAppkey> imports;
    private Map<String,ExportedAppkey> exports;

    public Interface(final String name){
        this.setupChannels();
        try {
            this.launchActor(name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.imports= new HashMap<String, ImportedAppkey>();
        this.exports= new HashMap<String, ExportedAppkey>();

        this.dock();
        String docked = this.dockskt.recvStr();

    }

    public ExportedAppkey AddExport(AppKey key){
        String[] tags = new String[]{};
        return this.AddExport(key,tags);
    }
    public ExportedAppkey AddExport(AppKey key, String[] tags){
        AddExportMessage m = new AddExportMessage(key,tags);
        this.outChan.tell(m, ActorRef.noSender());
        String eid = this.addexportskt.recvStr();
        ExportedAppkey ea = new ExportedAppkey(eid,this.ctx,this.outChan);
        this.exports.put(eid,ea);
        return ea;
    }
    public ImportedAppkey AddImport(AppKey key){
        String[] tags = new String[]{};
        return this.AddImport(key, tags);
    }

    public ImportedAppkey AddImport(AppKey key, String[] tags){
        AddImportMessage m = new AddImportMessage(key,tags);
        this.outChan.tell(m, ActorRef.noSender());
        String iid = this.addimportskt.recvStr();
        ImportedAppkey ia = new ImportedAppkey(iid,key,tags,this.ctx,this.outChan);
        this.imports.put(iid,ia);
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
    private void closeChannels(){
        this.dockskt.close();
        this.addexportskt.close();
        this.addimportskt.close();
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
        this.system.awaitTermination();

        this.closeChannels();

        for (Map.Entry<String, ImportedAppkey> entry : this.imports.entrySet())
        {
            entry.getValue().Remove();
        }
        for (Map.Entry<String, ExportedAppkey> entry : this.exports.entrySet())
        {
            entry.getValue().Remove();
        }
        this.ctx.term();

    }

    private int getFreePort() throws IOException {
        ServerSocket s = new ServerSocket(0);
         int port = s.getLocalPort();
        s.close();
        return port;
    }
}

