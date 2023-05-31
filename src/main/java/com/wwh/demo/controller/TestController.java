package com.wwh.demo.controller;

import com.wwh.demo.annotation.Limit;
import com.wwh.demo.domain.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("test")
public class TestController {
    private BigDecimal money=new BigDecimal(1000000);

    @Limit(dqs = 10)
    @RequestMapping(value = "get",method = RequestMethod.GET)
    public String getMoney(){
        return "成功";
    }
    @RequestMapping(value = "save",method = RequestMethod.POST)
    public String saveMoney(){
        return "成功";
    }
}
