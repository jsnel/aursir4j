package org.aursir.aursir4j.messages;

import com.google.gson.Gson;
import org.aursir.aursir4j.AppKey;

/**
 * Created by joern on 1/25/15.
 */
public class ExportAddedMessage implements Message {
    public AppKey AppKey ;
    public String[] Tags;
    public String ExportId;

    public ExportAddedMessage(AppKey key, String[] tags, String ExportId){
        this.AppKey = key;
        this.Tags=tags;
        this.ExportId=ExportId;
    }


    @Override
    public int GetMessageType() {
        return MsgTypes.types.EXPORT_ADDED.ordinal();
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
