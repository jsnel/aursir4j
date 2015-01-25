package org.aursir.aursir4j.messages;

/**
 * Created by joern on 1/25/15.
 */
public interface Message {
    public int GetMessageType();
    public String GetEncoded();
    public String GetCodec();
}
