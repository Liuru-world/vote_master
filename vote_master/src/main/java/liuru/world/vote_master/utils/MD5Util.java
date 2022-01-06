package liuru.world.vote_master.utils;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

    /**
     * MD5加密工具
     *
     */
    public class MD5Util {

        private MD5Util(){

        }

        public static String stringToMD5(String plainText) {
            byte[] mdBytes = null;
            try {
                mdBytes = MessageDigest.getInstance("MD5").digest(
                        plainText.getBytes());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("MD5算法不存在！");
            }
            String mdCode = new BigInteger(1, mdBytes).toString(16);
            if(mdCode.length() < 32) {
                int a = 32 - mdCode.length();
                for (int i = 0; i < a; i++) {
                    mdCode = "0"+mdCode;
                }
            }
            return mdCode.toUpperCase(); //返回32位大写
//        return mdCode;            // 默认返回32位小写
        }


    }

