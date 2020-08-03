package com.fh.member.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.member.mapper.LoginMapper;
import com.fh.member.biz.ILoginService;
import com.fh.member.po.Member;
import com.fh.util.JwtUtil;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service("loginService")
public class LoginServiceImpl implements ILoginService {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public ServerResponse login(Member member) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("memberName",member.getMemberName());
        queryWrapper.or();
        queryWrapper.eq("phone",member.getMemberName());
        Member memberDB = loginMapper.selectOne(queryWrapper);
        if (memberDB == null){
            return ServerResponse.error("用户名或手机号不存在！！！");
        }
        //判断密码是否正确
        if (!member.getPassword().equals(memberDB.getPassword())){
            return ServerResponse.error("密码错误");
        }
    /*String md5Password = Md5Util.md5(Md5Util.md5(memberDB.getPassword()));
    if(!md5Password.equals(memberDB.getPassword())){
        //密码错误
        return ServerResponse.error("密码错误");
    }*/
        //密码正确
        String token ="";
        try {
            String jsonString = JSONObject.toJSONString(memberDB);
            String encodeJson = URLEncoder.encode(jsonString, "utf-8");
            token = JwtUtil.sign(encodeJson);
            //给token设置过期时间
            RedisUtil.setEx(SystemConstant.TOKEN_KEY+token,token, SystemConstant.TOKEN_EXPIRE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ServerResponse.success(token);
    }


}
