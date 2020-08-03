package com.fh.member.biz;


import com.fh.common.ServerResponse;
import com.fh.member.po.Member;


public interface MemberService {
    ServerResponse addMember(Member member);

    ServerResponse valiDatamemberName(String memberName);

    ServerResponse valiDataphone(String phone);

    ServerResponse register(Member member);
}
