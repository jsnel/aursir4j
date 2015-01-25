package org.aursir.aursir4j.messages;

import com.google.gson.Gson;

/**
 * Created by joern on 1/25/15.
 */
public class DockedMessage implements Message {
    public Boolean Ok;

    public DockedMessage(boolean ok){
        this.Ok = ok;
    }


    @Override
    public int GetMessageType() {
        return Calltypes.types.DOCKED.ordinal();
    }

    @Override
    public String GetEncoded() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    @Override
    public String GetCodec() {
        return "JSON";
    }
}
