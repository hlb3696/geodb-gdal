package com.tq.geodb.tool;

public class JsonResult<T> {
	public static final int SUCCESS_CODE = 200;
	public static final int FAIL_CODE = 500;
	public static final JsonResult<String> SUCCESS = new JsonResult<String>(SUCCESS_CODE,"","成功");
	public static final JsonResult<String> FAIL = new JsonResult<String>(FAIL_CODE,"","失败");
	
    private boolean success;
    private int code;
    private String msg;
    private T data;
    
    public JsonResult(){}
	public JsonResult(int code, String msg) {
		this(code,null,msg);
	}
	public JsonResult(int code, T data, String msg) {
		this.code = code;
		this.data = data;
		this.msg = msg;
		this.success = SUCCESS_CODE == code;
	}

    public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
	public void success(T data) {
		this.success(data,"");
	}
	public void success(T data,String msg) {
		this.code=SUCCESS_CODE;
		this.success=true;
		this.data=data;
		this.msg=msg;
	}
}
