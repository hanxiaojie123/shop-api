package com.fh.member.biz;

import com.fh.common.ServerResponse;
import com.fh.member.po.Member;

public interface ILoginService {

    ServerResponse login(Member member);
}
