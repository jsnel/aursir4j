package org.aursir.aursir4j.messages;

import com.google.gson.Gson;

/**
 * Created by joern on 1/25/15.
 */
public class DockMessage implements Message {
    private String AppName;
    private String[] Codecs = {"JSON"};
    private Boolean Node = false;

    public DockMessage(String AppName, boolean isNode){
        this.AppName = AppName;
        this.Node = isNode;
    }


    @Override
    public int GetMessageType() {
        return MsgTypes.types.DOCK.ordinal();
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
