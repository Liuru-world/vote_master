package liuru.world.vote_master.utils;


public enum HttpCodeEnum {
    // 用枚举方式定义HTTP返回码和返回信息
    OK(200,"OK"),
    CREATEOK(201,"CREATEOK"),
    REDIRECT(301,"REDIRECT"),
    PARAM_ERROR(400,"PARAM_ERROR"),
    NOT_FOUND(404,"NOT_FOUND"),
    SERVER_ERROR(500,"SERVER_ERROR");

    //HTTP返回码和返回信息的变量
    private final Integer httpCode;
    private final String httpMsg;

    //通过构造函数传入HTTP返回码和返回信息
    HttpCodeEnum(Integer code,String msg) {
        this.httpCode = code;
        this.httpMsg = msg;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public String getHttpMsg() {
        return httpMsg;
    }

}
