package kr.doublechain.basic.explorer.node;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.doublechain.basic.explorer.common.CommonUtil;
import kr.doublechain.basic.explorer.service.dcc.DccService;

/**
 * 
 * Dcc Node model
 *
 */
@Service("dcc")
public class Dcc {
	
	@Autowired
	DccService dccSercvice;
	
	@Value("${dcc.host}")
	private String host;
	
	@Value("${dcc.port}")
	private String port;
	
	@Value("${dcc.user}")
	private String user;
	
	@Value("${dcc.password}")
	private String password;
	
	/**
	 * RPC Call
	 */
	public Object RPCCall(String rpcCommand, List<Object> param) throws Exception {
		return CommonUtil.RemoteCall(user, password, host, port, rpcCommand, param);
	}
}
