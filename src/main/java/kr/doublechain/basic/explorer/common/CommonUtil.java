package kr.doublechain.basic.explorer.common;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


@Component
public class CommonUtil {

	/**
	 * Object convert to json String
	 * 
	 * @param object
	 * @return String
	 * @throws JsonProcessingException
	 */
	public static String convertJsonStringFromObject(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

	/**
	 * json String convert Object
	 * 
	 * @param content
	 * @param clazz
	 * @return T
	 * @throws Exception
	 */
	public static <T> T convertObjectFromJsonString(String content, Class<T> clazz) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		T object = (T) mapper.readValue(content, clazz);
		return object;
	}

	/**
	 * Generic Collection Type covert method
	 * 
	 * @param content
	 * @param clazz
	 * @return T
	 * @throws Exception
	 */
	public static <T> T convertObjectFromJsonStringByTypeRef(String content, TypeReference<T> clazz) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		T object = mapper.readValue(content, clazz);
		return object;
	}

	/**
	 * Gson을 사용한 타입 캐스트 변환
	 * 
	 * @param jsonObject
	 * @param typeToken
	 * @return T
	 * @throws Exception
	 */
	public static <T> T convertTypeTokenFromGson(JsonElement jsonObject, TypeToken<T> typeToken) throws Exception {
		T object = new Gson().fromJson(jsonObject, typeToken.getType());
		return object;
	}

	/**
	 * Gson 타입의 json객체를 string으로 변환한다.
	 * 
	 * @param <T>
	 * @param jsonObject
	 * @return String
	 * @throws JsonProcessingException
	 */
	public static <T> String convertJsonStringFromGson(JsonObject jsonObject) throws JsonProcessingException {
		return new Gson().toJson(jsonObject);
	}

	/**
	 * json형식의 스트링을 Gson json객체로 변환한다.
	 * 
	 * @param jsonString
	 * @return JsonObject
	 * @throws JsonProcessingException
	 */
	public static JsonObject convertGsonFromString(String jsonString) throws JsonProcessingException {
		return new JsonParser().parse(jsonString).getAsJsonObject();
	}

	/**
	 * block chain node connect url
	 * 
	 * @param host
	 * @param port
	 * @return String
	 */
	public static String makeUrl(String host, String port) {
		return "http://" + host + ":" + port + "/";
	}

	/**
	 * Remote Call Bitcoin based BlockChain
	 * 
	 * @param user
	 * @param password
	 * @param host
	 * @param port
	 * @param rpcCommand
	 * @param param
	 * @return Object
	 * @throws Exception
	 * @desc bitcoin 기반
	 */
	public static Object RemoteCall(String user, String password, String host, String port, String rpcCommand,
			List<Object> param) throws Exception {

		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

		HttpPost request = new HttpPost(CommonUtil.makeUrl(host, port));

		JSONObject json = new JSONObject();
		json.put("method", rpcCommand);
		json.put("params", new JSONArray(param));
		request.setEntity(new StringEntity(json.toString()));

		HttpResponse response = client.execute(request);

		String contents = EntityUtils.toString(response.getEntity());
		JsonObject jsonObject = CommonUtil.convertGsonFromString(contents);

		if (jsonObject.get("error").isJsonNull()) {
			return jsonObject.get("result");
		} else {
			return null;
		}

	}

	
	/**
	 * Json RPC로 받은 tx list를 List형태로 반환한다.
	 * 
	 * @param JsonObject
	 * @return List<String>
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getTxList(JsonObject block) {
		Gson gson = new Gson();
		HashMap<String, Object> blockMap = gson.fromJson(block, HashMap.class);
		return ((ArrayList<String>) blockMap.get("tx"));
	}
	
	/**
	 * JsonObject를 Object로 반환한다.
	 * 
	 * @param JsonObject
	 * @return Object
	 */
	public static Object convertObjectFromGson(JsonObject json) throws Exception {
		JSONParser parser = new JSONParser();
		return parser.parse(json.toString());
	}

}
