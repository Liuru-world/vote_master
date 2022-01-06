package liuru.world.vote_master.utils;

import java.io.Serializable;

public class HttpReturn<T> implements Serializable {
    //HTTP返回码
    private Integer httpCode;
    //HTTP返回码对应的信息
    private String httpMsg;
    //返回的数据，用泛型定义
    private T data;

    public HttpReturn(HttpCodeEnum httpCodeEnum,T data){
        this.httpCode = httpCodeEnum.getHttpCode();
        this.httpMsg = httpCodeEnum.getHttpMsg();
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpReturn{" +
                "httpCode=" + httpCode +
                ", httpMsg='" + httpMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public String getHttpMsg() {
        return httpMsg;
    }

    public T getData() {
        return data;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public void setHttpMsg(String httpMsg) {
        this.httpMsg = httpMsg;
    }

    public void setData(T data) {
        this.data = data;
    }
}
