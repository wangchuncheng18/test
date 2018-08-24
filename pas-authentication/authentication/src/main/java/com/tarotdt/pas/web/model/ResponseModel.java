package com.tarotdt.pas.web.model;


/**
 * 返回值类
 * @author lir
 *
 * @param <T>
 */
public class ResponseModel<T> {
	//状态
    private Integer code = HttpCode.OK.value();
    //返回的值
    private T data;
    //返回状态说明
    private String msg;

    public ResponseModel() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ResponseModel(Integer code, T data, String msg) {
        super();
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setInfo(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public void setSuccessInfo(T data,String msg){
        this.code = 200;
        this.data = data;
        this.msg = msg;
    }

}
