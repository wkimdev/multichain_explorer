package kr.doublechain.basic.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.doublechain.basic.demo.common.CommonUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 전성국 on 2018-05-23.
 */
@Service("multichainRpc")
public class MultichainRpcImpl implements MultichainRpc {
    public Object MultichainRpc(String Api, ArrayList params) throws Exception {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("multichainrpc", "88888888");
        provider.setCredentials(AuthScope.ANY, credentials);


        HttpClient client = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(provider)
                .build();

        HttpPost request = new HttpPost( "http://192.168.0.143:8872/" );
        //http://multichainrpc:88888888@192.168.0.143:8872

//        String[] params = new String[1];
//        ArrayList params = new ArrayList();
//        params.add("3360");
//        params.add(4);
//        params[0] = "0000006192671dfe63af742338b8a0f28ceec15f38d28ded897527c2f1127675 4";

        JSONObject json = new JSONObject();
        json.put("method", Api);
        json.put("params", new JSONArray(params));
//        json.put("params",  new JSONArray(new String[0]));


//        System.out.println( json.toString());

        request.setEntity( new StringEntity( json.toString() ) );


        HttpResponse response = client.execute( request );

        int statusCode = response.getStatusLine().getStatusCode();

       // Assert.assertEquals(statusCode, HttpStatus.SC_OK);

        //System.out.println(EntityUtils.toString(response.getEntity()));
        String contents = EntityUtils.toString(response.getEntity());
//        System.out.println(contents);
        //System.out.println(contents);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        // map = mapper.readValue(js, new TypeReference<Map<String, String>>(){});
        //map = mapper.readValue(contents, new TypeReference<HashMap<String, Object>>(){});
        map = CommonUtils.convertObjectFromJsonString(contents, HashMap.class);
        if( map.get("error") == null ) {
            return map.get("result");
        }
        else {
            return null;
        }
//        System.out.println(map);
//        ObjectMapper mapper = new ObjectMapper();
//        Map map = mapper.readValue(contents, Map.class);
//        System.out.println(map.toString());
//        return map;
    }

    @Override
    public String getNextHash(String hash) throws Exception {
        ArrayList arrayList = new ArrayList();
        arrayList.add(hash);
        arrayList.add(4);
        Map map = (Map)MultichainRpc("getblock", arrayList);

        if (map.containsKey("nextblockhash")) {
            return ( map.get("nextblockhash") ).toString();
        } else {
            return null;
        }
    }

    @Override
    public String getNextHash(long height) throws Exception {
        ArrayList arrayList = new ArrayList();
        arrayList.add(String.valueOf(height));
        arrayList.add(4);
        Map map = (Map)MultichainRpc("getblock", arrayList);

        if (map.containsKey("nextblockhash")) {
            return ( map.get("nextblockhash") ).toString();
        } else {
            return null;
        }
    }

    @Override
    public String getHash(long height) throws Exception {
        ArrayList arrayList = new ArrayList();
        arrayList.add(String.valueOf(height));
        arrayList.add(4);
        Map map = (Map)MultichainRpc("getblock", arrayList);
        return ( map.get("hash") ).toString();
    }

    @Override
    public Map getBlock(long height) throws Exception {
        ArrayList arrayList = new ArrayList();
        arrayList.add(String.valueOf(height));
        arrayList.add(4);
        return (Map)MultichainRpc("getblock", arrayList);
    }

    @Override
    public Map getBlock(String hash) throws Exception {
        ArrayList arrayList = new ArrayList();
        arrayList.add(hash);
        arrayList.add(4);
        return (Map)MultichainRpc("getblock", arrayList);
    }
}
