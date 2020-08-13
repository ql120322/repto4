package com.lianfu.lf_scancode_accounts.mapper;

import com.lianfu.lf_scancode_accounts.model.Lf_gl;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Lf_glMapper {


    //20206-23 齐亮创建 往后台管理表 增加数据
    int insetoLf_gl(Lf_gl lf);




}
