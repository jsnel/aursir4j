package org.aursir.aursir4j.messages;

import com.google.gson.Gson;
import org.aursir.aursir4j.AppKey;

/**
 * Created by joern on 1/25/15.
 */
public class ImportAddedMessage implements Message {
    public AppKey AppKey ;
    public String[] Tags;
    public String ImportId;
    public boolean Exported;

    public ImportAddedMessage(AppKey key, String[] tags, String ImportId){
        this.AppKey = key;
        this.Tags=tags;
        this.ImportId=ImportId;
    }


    @Override
    public int GetMessageType() {
        return MsgTypes.types.IMPORT_ADDED.ordinal();
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
