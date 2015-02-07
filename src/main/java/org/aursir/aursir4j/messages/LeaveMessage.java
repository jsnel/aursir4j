package org.aursir.aursir4j.messages;

/**
 * Created by joern on 1/25/15.
 */
public class LeaveMessage implements Message {


    public LeaveMessage(){}


    @Override
    public int GetMessageType() {
        return MsgTypes.types.LEAVE.ordinal();
    }

    @Override
    public String GetEncoded() {
        return "";
    }

    @Override
    public String GetCodec() {
        return "JSON";
    }
}
