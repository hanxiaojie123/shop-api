package com.fh.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.member.po.Member;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberMapper extends BaseMapper<Member>{

    void addMember(Member member);

    void register(Member member);
}
