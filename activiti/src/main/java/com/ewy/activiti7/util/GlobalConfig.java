package com.ewy.activiti7.util;

public class GlobalConfig {

    public static final Boolean Test = false;
    // 注意区分win和linux发布路径
//    public static final String BPMN_PathMapping = "file:D:\\Users\\liantao\\Downloads\\activiti\\src\\main\\resources\\resources\\bpmn\\";
    public static final String BPMN_PathMapping = "file:/Users/liantao/Downloads/fileRepository/";

    public enum ResponseCode{
        SUCCESS(0,"成功"),
        ERROR(1,"失败");
        private final int code;
        private final String desc;
        ResponseCode(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

}
