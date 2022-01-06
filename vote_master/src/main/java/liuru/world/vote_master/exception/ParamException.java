package liuru.world.vote_master.exception;

public class ParamException extends RuntimeException {
    //错误码，一般的HTTP返回码对应
    private Integer errorCode;
    //错误信息
    private String errorMsg;
    //构造函数
    public ParamException(Integer errorCode,String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
