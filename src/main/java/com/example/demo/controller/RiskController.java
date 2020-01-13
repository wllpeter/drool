package com.example.demo.controller;

import com.example.demo.modal.DataInfo;
import com.example.demo.modal.Product;
import com.example.demo.service.EmailService;
import com.example.demo.service.RiskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by 86131 on 2020/1/13.
 */

@RestController
@RequestMapping("/risk")
public class RiskController {

    @Autowired
    private RiskService riskService;
    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/control", method = RequestMethod.POST)
    public DataInfo<String> control(@RequestBody Product product) {
        riskService.riskJudge(product);
        return DataInfo.success("");
    }

    @ApiOperation(value = "发送邮件(含附件)")
    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public DataInfo<String> sendEmailHaveExcel(HttpServletResponse response) {
        Boolean result = emailService.sendEmailHaveAttachment();
        if (!result) {
            return DataInfo.success("发送失败");
        }
        return DataInfo.success("发送成功");
    }

    @ApiOperation(value = "发送邮件")
    @RequestMapping(value = "/sendSimpleMail", method = RequestMethod.POST)
    public DataInfo<String> sendSimpleMail() {
        emailService.sendSimpleMail();
        return DataInfo.success("发送成功");
    }
}
