package com.example.demo.rule;

import com.example.demo.modal.Product;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * Created by 86131 on 2020/1/13.
 */
public class RuleTest {

    @Test
    public void testRules() {
        // 构建 KieServices
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();
        // 获取 kmodule.xml 中配置中名称为 ksession-rule 的 session，默认为有状态的。
        KieSession kSession = kieContainer.newKieSession("session-rule");
        Product product = new Product();
        product.setType(Product.GOLD);
        kSession.insert(product);
        int count = kSession.fireAllRules();
        System.out.println("命中了" + count + "条规则！ ");
        System.out.println(" 商 品 " +product.getType() + " 的 商 品 折 扣 为 " +
                product.getDiscount() + "%。 ");
    }

    @Test
    public void email() {
        // 构建 KieServices
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();
        // 获取 kmodule.xml 中配置中名称为 ksession-rule 的 session，默认为有状态的。
        KieSession kSession = kieContainer.newKieSession("session-money");
        Product product = new Product();
        product.setMoney(9999);
        kSession.insert(product);
        int count = kSession.fireAllRules();
        kSession.dispose();
        System.out.println("命中了" + count + "条规则！ ");
        System.out.println(" 商 品 " + product.getType() + " 的 商 品 折 扣 为 " +
                product.getDiscount() + "%。 ");
    }
}

