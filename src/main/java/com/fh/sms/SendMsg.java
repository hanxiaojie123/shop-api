package com.fh.sms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.util.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("sms")
@RestController
public class SendMsg {

    @RequestMapping("sendMsg")
    public ServerResponse senMsg(String phone){
        String newcode = AliyunSmsUtils.getCode();
        try {
            SendSmsResponse sendSmsResponse = AliyunSmsUtils.sendSms(phone, newcode);
        if (sendSmsResponse != null && "OK".equals(sendSmsResponse.getCode())){
            //把code 放到redis中
            RedisUtil.setEx(phone,newcode, SystemConstant.REDIS_KEY_EXPIRE);
            return  ServerResponse.success();
        }
        } catch (ClientException e) {
            e.printStackTrace();
            return  ServerResponse.error(e.getErrMsg());
        }
        return ServerResponse.success();
    }

}
