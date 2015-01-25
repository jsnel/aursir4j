package org.aursir.aursir4j.messages;

import com.google.gson.Gson;
import org.aursir.aursir4j.AppKey;

/**
 * Created by joern on 1/25/15.
 */
public class AddImportMessage implements Message {
    public AppKey AppKey ;
    public String[] Tags;
    public String ImportId="";

    public AddImportMessage(AppKey key, String[] tags){
        this.AppKey = key;
        this.Tags=tags;
    }


    @Override
    public int GetMessageType() {
        return Calltypes.types.ADD_IMPORT.ordinal();
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
