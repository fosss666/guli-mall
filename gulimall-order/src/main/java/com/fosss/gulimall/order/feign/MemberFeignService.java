package com.fosss.gulimall.order.feign;

import com.fosss.gulimall.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author: fosss
 * Date: 2023/6/3
 * Time: 21:47
 * Description:
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {
    /**
     * 获取用户的所有收货地址
     */
    @GetMapping("/member/memberreceiveaddress/{memberId}/addresses")
    List<MemberAddressVo> getMemberReceiveAddressList(@PathVariable("memberId") Long memberId) ;
}
