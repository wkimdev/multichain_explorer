package kr.doublechain.basic.demo.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by 전성국 on 2018-05-23.
 */
public class CommonUtils {
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
}
