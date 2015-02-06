package org.aursir.aursir4j.messages;

import com.google.gson.Gson;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by joern on 1/25/15.
 */
public class Result implements Message {
    public String AppKeyName;
    public String FunctionName;
    public int CallType;
    public String[] Tags;
    public String Uuid;
    public String ImportId;
    public String ExportId;
    //public String Timestamp = "";
    public String Codec="JSON";
    public String Result;
    public boolean Stream=false;
    public boolean StreamFinished=true;

    public Result(){}

    public Result(Request request, Object Result, String ExportId){
          this.AppKeyName = request.AppKeyName;
        this.FunctionName = request.FunctionName;
        this.CallType = request.CallType;
        this.Tags = request.Tags;
        this.Uuid = request.Uuid;
        this.ImportId = request.ImportId;
        this.ExportId = ExportId;
        Gson gson = new Gson();
        String json = gson.toJson(Result);

        this.Result = DatatypeConverter.printBase64Binary(json.getBytes());

    }

    @Override
    public int GetMessageType() {
        return MsgTypes.types.RESULT.ordinal();
    }

    @Override
    public String GetEncoded() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        System.out.println(json);

        return json;
    }

    @Override
    public String GetCodec() {
        return "JSON";
    }

    public <T> T Decode(Class<T> type) {
        Gson gson = new Gson();
        byte[] req = DatatypeConverter.parseBase64Binary(this.Result);
        return gson.fromJson(new String(req),type);
    }
}
