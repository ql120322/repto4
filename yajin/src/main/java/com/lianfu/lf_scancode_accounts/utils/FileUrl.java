package com.lianfu.lf_scancode_accounts.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUrl {
    /*
        存放模板文档  /usr/origin/origin.docx
        存放预览word文档 /usr/preview/date/openid.docx
        存放签名 /usr/signpic/date/openid.png
        存放签名后word文档 /usr/signedword/date/openid.docx
        存放转换后的pdf /usr/finalpdf/date/openid.docx
     */

    private static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        return format + "/";
    }




    public static final String origin = "/usr/origin/origin.docx";
    public static final String preview ="/usr/preview/";
    public static final String signpic ="/usr/signpic/";/*"/usr/signpic/";*/
    public static final String signedword = "/usr/signedword/";/*"/usr/signedword/"*/;


}
