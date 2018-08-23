package kr.doublechain.basic.explorer.common.code;


/**
 * 
 * Enum Constants Definition
 *
 */
public enum Constants {
	
	/* HTTPSTATUS */
	/**
	 * 400
	 */
	HTTPSTATUS_BAD(400)
	
	/**
	 * 200
	 */
	,HTTPSTATUS_OK(200)
	
	/**
	 * 201
	 */
	,HTTPSTATUS_CREATED(201)
	
	/**
	 * 204
	 */
	,HTTPSTATUS_NO_CONTENT(204)
	
	/**
	 * 301
	 */
	,HTTPSTATUS_MOVED_PERMANENTLY(301)
	
	/**
	 * 401
	 */
	,HTTPSTATUS_UNAUTHOR(401)
	
	/**
	 * 500
	 */
	,HTTPSTATUS_SEVERERROR(500)
	
	/**
	 * 404
	 */
	,HTTPSTATUS_NOTFOUND(404);
	/* END HTTPSTATUS */
	
	public int ITYPE;
	
	public String STYPE;
	
	Constants(int ITYPE) {
		this.ITYPE = ITYPE;
	}
	
	int getITYPE() {
		return this.ITYPE;
	}
	
	Constants(String STYPE) {
		this.STYPE = STYPE;
	}
	
	String getSTYPE() {
		return this.STYPE;
	}

}
