package com.fh.member.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ServerEnum;
import com.fh.common.ServerResponse;
import com.fh.member.biz.MemberService;
import com.fh.member.mapper.MemberMapper;
import com.fh.member.po.Member;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public ServerResponse addMember(Member member) {
        String memberName = member.getMemberName();
        String password = member.getPassword();
        String phone = member.getPhone();
        //判断信息不能为空
        if (StringUtils.isEmpty(memberName)||StringUtils.isEmpty(password)
                                            ||StringUtils.isEmpty(phone)

        ){
        return ServerResponse.error(ServerEnum.REG_MEMBER_IS_NULL);
        }
        //判断会员名不能为重复
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("memberName",memberName);
        Member member1 = memberMapper.selectOne(queryWrapper);
        if (member1!=null){
            return ServerResponse.error(ServerEnum.REG_MEMBER_IS_NXIST);
        }
        //判断手机号是否存在
        QueryWrapper<Member> phoneQueryWrapper = new QueryWrapper<>();
        phoneQueryWrapper.eq("phone",phone);
        Member member3 = memberMapper.selectOne(phoneQueryWrapper);
        if (member3 !=null){
            return ServerResponse.error(ServerEnum.REG_MEMBER_IS_NPONE);
        }


        //注册会员
        memberMapper.addMember(member);


        return ServerResponse.success();
    }
    //会员名是否存在判断
    @Override
    public ServerResponse valiDatamemberName(String memberName) {
        if(StringUtils.isEmpty(memberName)){
        return ServerResponse.error(ServerEnum.REG_MEMBER_IS_NULL);
        }
        //进行唯一判断
        QueryWrapper<Member> mailQueryWrapper = new QueryWrapper<>();
        mailQueryWrapper.eq("memberName",memberName);
        Member member2 = memberMapper.selectOne(mailQueryWrapper);
        if (member2 !=null){
            return ServerResponse.error(ServerEnum.REG_MEMBER_IS_NXIST);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse valiDataphone(String phone) {
        if(StringUtils.isEmpty(phone)){
            return ServerResponse.error(ServerEnum.REG_MEMBER_IS_NULL);
        }
        //进行唯一判断
        QueryWrapper<Member> mailQueryWrapper = new QueryWrapper<>();
        mailQueryWrapper.eq("phone",phone);
        Member member3 = memberMapper.selectOne(mailQueryWrapper);
        if (member3 !=null){
            return ServerResponse.error(ServerEnum.REG_MEMBER_IS_NPONE);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse register(Member member) {
        String redisCode = RedisUtil.get(member.getPhone());
        if (redisCode==null){
            return ServerResponse.error("验证码已失效");
        }
        if (!redisCode.equals(member.getCode())){
            return ServerResponse.error("验证码错误");
        }
        memberMapper.register(member);

        return ServerResponse.success();
    }


}
