package kr.doublechain.basic.explorer.node;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import kr.doublechain.basic.explorer.common.utils.CommonUtil;

/**
 * 
 * Dcc Node model
 *
 */
@Service("dcc")
public class Dcc {
	
	@Autowired
    private HttpClient httpClient;

    @Autowired
    private HttpPost httpPost;
    
    /**
     * RPC Call
     */
    public Object RPCCall(String rpcCommand, List<Object> param) throws Exception {
        
        JSONObject json = new JSONObject();
        json.put("method", rpcCommand);
        json.put("params", new JSONArray(param));
        httpPost.setEntity(new StringEntity(json.toString()));

        HttpResponse response = httpClient.execute(httpPost);

        String contents = EntityUtils.toString(response.getEntity());
        JsonObject jsonObject = CommonUtil.convertGsonFromString(contents);

        if (jsonObject.get("error").isJsonNull()) {
            return jsonObject.get("result");
        } else {
            return null;
        }
        
    }
}
