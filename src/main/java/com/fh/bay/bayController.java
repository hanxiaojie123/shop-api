package com.fh.bay;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fh.common.ServerResponse;
import com.fh.sdk.MyConfig;
import com.fh.sdk.WXPay;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("bay")
public class bayController {

    @RequestMapping("createNative")
    public ServerResponse createNative(String orderNo, BigDecimal totalPrice){

     try {
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "飞狐购物中心-1908B");
        data.put("out_trade_no", orderNo);
        data.put("device_info", "WHE");
        data.put("fee_type", "CNY");
        data.put("total_fee", "1");
        data.put("spbill_create_ip", "123.12.12.123");
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        data.put("product_id", "12");
        //设置失效时间
         SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
         String format = sim.format(DateUtils.addMinutes(new Date(), 2));
         data.put("time_expire",format);

         Map<String, String> resp = wxpay.unifiedOrder(data);
         System.out.println(resp);

        //判断通讯是否成功
         if (!resp.get("return_code").equalsIgnoreCase("SUCCESS")){
                return ServerResponse.error("微信支付平台报错:"+resp.get("return_msg"));
         }
        //判断业务是否正确
         if (!resp.get("result_code").equalsIgnoreCase("SUCCESS")){
                return ServerResponse.error("微信支付平台报错:"+resp.get("err_code_des"));
         }
         //result_code 与return_code 都为success
         String code_url = resp.get("code_url");
         return ServerResponse.success(code_url);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error(e.getMessage());
        }

    }

    @RequestMapping("queryCertStatus")
    public ServerResponse queryCertStatus(String orderNo){
        try {
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", orderNo);
            int count = 0;
            for (;;){
                Map<String, String> resp = wxpay.orderQuery(data);
                System.out.println(resp);
                //判断通讯是否成功
                if (!resp.get("return_code").equalsIgnoreCase("SUCCESS")) {
                    return ServerResponse.error("微信支付平台报错:" + resp.get("return_msg"));
                }
                //判断业务是否正确
                if (!resp.get("result_code").equalsIgnoreCase("SUCCESS")) {
                    return ServerResponse.error("微信支付平台报错:" + resp.get("err_code_des"));
                }
                //交易状态
                if (resp.get("trade_state").equalsIgnoreCase("SUCCESS")) {
                    return ServerResponse.success();
                }
                count++;
                Thread.sleep(3000);
                if (count > 10){
                    return ServerResponse.error("支付超时！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error(e.getMessage());
        }


    }
}
