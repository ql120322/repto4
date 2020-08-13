package com.lianfu.lf_scancode_accounts.model;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @全局API接口
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ApiModel(description = "全局API接口")
public class ResponseModel<T> implements Serializable {

    //    @ApiModelProperty(value = "状态码,成功为200", example = "200")
    private int code;

    //    @ApiModelProperty(value = "成功标识", example = "true")
    private boolean success;

    //    @ApiModelProperty(value = "状态码为200时，message为OK， 否则为错误信息", example = "OK")
    private String message;

    //    @ApiModelProperty(value = "返回数据")
    private T data;

    //    @ApiModelProperty(value = "时间戳", example = "1853558969352")
    private long timestamp;


    public ResponseModel(long i, boolean b, String ok, T data, long currentTimeMillis) {
    }


}
