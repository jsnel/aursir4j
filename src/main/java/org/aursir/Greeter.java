package org.aursir;

import akka.actor.UntypedActor;

public class Greeter extends UntypedActor {

    public boolean Test = false;

    public static enum Msg {
        GREET, DONE;
    }

    @Override
    public void onReceive(Object msg) {
        if (msg == Msg.GREET) {
            System.out.println("Hello World!");
            getSender().tell(Msg.DONE, getSelf());
        } else
            unhandled(msg);
    }

}
