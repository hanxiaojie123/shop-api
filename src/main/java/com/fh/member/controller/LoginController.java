package com.fh.member.controller;

import com.fh.common.ServerResponse;
import com.fh.member.biz.ILoginService;
import com.fh.member.po.Member;
import com.fh.util.annotation.Ignore;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private ILoginService loginService;


    //登录
    @RequestMapping("doLogin")
    @Ignore
    public ServerResponse login(Member member){
        return loginService.login(member);
    }
}
