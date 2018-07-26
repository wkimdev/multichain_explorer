package kr.doublechain.basic.explorer.code;

public enum RPCCommandCode {
	
	/**
	 * DCC Command
	 */
	DCC_GETINFO("getinfo"),
	
	DCC_GETBLOCK("getblock"),
	
	DCC_GETTRANSACTION("getrawtransaction");

	/** int type constructor */
	RPCCommandCode(String code, int Id) {
		this.CODE = code;
		this.ID = Id;
	}
	
	/** int type constructor */
	RPCCommandCode(int Id) {
		this.ID = Id;
	}

	public int ID;
		int getId() { return this.ID; }	

	/** String type constructor */
	RPCCommandCode(String code) {
		this.CODE = code;
	}
		
	public String CODE;
		String getCode() { return this.CODE; }
		
}