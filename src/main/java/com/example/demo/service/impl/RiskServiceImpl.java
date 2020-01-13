package com.example.demo.service.impl;

import com.example.demo.modal.Product;
import com.example.demo.service.RiskService;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

/**
 * Created by 86131 on 2020/1/13.
 */
@Service
public class RiskServiceImpl implements RiskService {

    @Override
    public Boolean riskJudge(Product product) {
        // 构建 KieServices
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();
        // 获取 kmodule.xml 中配置中名称为 ksession-rule 的 session，默认为有状态的。
        KieSession kSession = kieContainer.newKieSession("session-money");
        kSession.insert(product);
        int count = kSession.fireAllRules();
        kSession.dispose();
        System.out.println("命中了" + count + "条规则！ ");
        System.out.println(" 商 品 " + product.getType() + " 的 商 品 折 扣 为 " +
                product.getDiscount() + "%。 ");

        return count > 0 ? false : true;
    }
}
