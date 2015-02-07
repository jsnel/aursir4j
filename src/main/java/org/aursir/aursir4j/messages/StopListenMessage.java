package org.aursir.aursir4j.messages;

import com.google.gson.Gson;

/**
 * Created by joern on 1/25/15.
 */
public class StopListenMessage implements Message {
    private String ImportId;
    private String FunctionName;

    public StopListenMessage(String importId, String functionName){
        this.ImportId = importId;
        this.FunctionName = functionName;
    }


    @Override
    public int GetMessageType() {
        return MsgTypes.types.STOP_LISTEN.ordinal();
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
