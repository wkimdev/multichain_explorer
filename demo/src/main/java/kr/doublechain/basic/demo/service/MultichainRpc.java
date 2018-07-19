package kr.doublechain.basic.demo.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 전성국 on 2018-05-23.
 */
public interface MultichainRpc {
    public Object MultichainRpc(String Api, ArrayList params) throws Exception;

    public String getNextHash(String hash) throws Exception;

    public String getNextHash(long height) throws Exception;

    public String getHash(long height) throws Exception;

    public Map getBlock(long height) throws Exception;

    public Map getBlock(String hash) throws Exception;
}
