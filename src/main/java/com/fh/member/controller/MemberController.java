package com.fh.member.controller;

import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.member.biz.MemberService;
import com.fh.member.po.Member;
import com.fh.util.RedisUtil;
import com.fh.util.annotation.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
//会员注册
    @RequestMapping("member")
    public ServerResponse addMember(Member member){
        return memberService.addMember(member);
    }
    //验证账号唯一
    @RequestMapping("valiDatamemberName")
    @Ignore
    public ServerResponse valiDatamemberName(String memberName){
        return memberService.valiDatamemberName(memberName);
    }
    //验证手机号唯一
    @RequestMapping("/valiDataphone")
    @Ignore
    public ServerResponse valiDataphone(String phone){
        return memberService.valiDataphone(phone);
    }
//注册会员
  @RequestMapping("register")
  @Ignore
    public ServerResponse register(Member member){
        return memberService.register(member);

  }
//验证用户是否已经登陆
  @RequestMapping("checkLogin")
    public ServerResponse checkLogin(HttpServletRequest request){
      Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
    if(member==null){
    return  ServerResponse.error();
    }
      return ServerResponse.success();

    }
    //退出
    @RequestMapping("out")
    @Ignore
    public ServerResponse out(HttpServletRequest request){
        //让token失效
        String token = (String) request.getSession().getAttribute(SystemConstant.TOKEN_KEY);
        RedisUtil.del(SystemConstant.TOKEN_KEY+token);
        request.getSession().getAttribute(SystemConstant.TOKEN_KEY);
        // 清除session中用户信息
        request.getSession().removeAttribute(SystemConstant.SESSION_KEY);
        return ServerResponse.success();
    }


}
