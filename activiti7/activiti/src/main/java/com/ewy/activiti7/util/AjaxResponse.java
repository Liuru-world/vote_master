package com.ewy.activiti7.util;

import lombok.Data;

@Data
public class AjaxResponse {
    private Integer status;
    private String msg;
    private Object obj;

    private AjaxResponse(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public static AjaxResponse ajaxData(Integer status, String msg, Object obj){
        return new AjaxResponse(status,msg,obj);
    }
}
