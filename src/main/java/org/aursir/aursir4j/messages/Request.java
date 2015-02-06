package org.aursir.aursir4j.messages;

import com.google.gson.Gson;
import org.aursir.aursir4j.AppKey;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by joern on 1/25/15.
 */
public class Request implements Message {
    public String AppKeyName;
    public String FunctionName;
    public int CallType;
    public String[] Tags;
    public String Uuid;
    public String ImportId;
    public String ExportId;
   // public String Timestamp;
    public String Codec="JSON";
    public String Request;
    public boolean Stream=false;
    public boolean StreamFinished=false;

    public Request(){}

    public Request(String appKeyName,String functionName, int callType,
                   String[] tags, Object Request){
          this.AppKeyName = appKeyName;
        this.FunctionName = functionName;
        this.CallType = callType;
        this.Tags = tags;

    }

    @Override
    public int GetMessageType() {
        return Calltypes.types.REQUEST.ordinal();
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

    public <T> T Decode(Class<T> type) {
        Gson gson = new Gson();
        byte[] req = DatatypeConverter.parseBase64Binary(this.Request);
        return gson.fromJson(new String(req),type);
    }
}
