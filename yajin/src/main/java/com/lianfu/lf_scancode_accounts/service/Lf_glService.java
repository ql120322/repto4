package com.lianfu.lf_scancode_accounts.service;


import com.lianfu.lf_scancode_accounts.mapper.Lf_glMapper;
import com.lianfu.lf_scancode_accounts.model.Lf_gl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class Lf_glService {


 @Autowired
   private Lf_glMapper lfglmapper;

    //2020-6-23 齐亮创建   添加 数据
    public  int setLf_gl(Lf_gl lf){
        return lfglmapper.insetoLf_gl(lf);
    }




}
