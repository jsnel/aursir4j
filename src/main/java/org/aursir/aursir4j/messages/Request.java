package org.aursir.aursir4j.messages;

import com.google.gson.Gson;
import org.aursir.aursir4j.calltypes;
import org.zeromq.ZMQ;

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
    public boolean StreamFinished=true;

    private static ZMQ.Socket resultsock;

    public Request(){}

    public Request(String appKeyName,String functionName, String uuid, String importId, int callType,
                   String[] tags, Object Request, ZMQ.Socket resultsock){
          this.AppKeyName = appKeyName;
        this.FunctionName = functionName;
        this.Uuid=uuid;
        this.ImportId = importId;
        this.CallType = callType;
        this.Tags = tags;
        this.resultsock = resultsock;

        Gson gson = new Gson();
        String json = gson.toJson(Request);

        this.Request = DatatypeConverter.printBase64Binary(json.getBytes());

    }

    @Override
    public int GetMessageType() {
        return MsgTypes.types.REQUEST.ordinal();
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

    public Result WaitForResult(){
        String reqm = this.resultsock.recvStr();
        Gson gson = new Gson();
        Result r = gson.fromJson(reqm, Result.class);
        if (r.CallType == calltypes.ONE2ONE.ordinal()) {
            this.resultsock.close();
        }
        return r;
    }
}
