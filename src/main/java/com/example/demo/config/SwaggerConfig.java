package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;

/**
 * Created by 79987 on 2018/1/15.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller")).paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        InetAddress ia = null;
        String localname = null;
        String localip = null;
        try {
            ia = InetAddress.getLocalHost();
            localname = ia.getHostName();
            localip = ia.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ApiInfoBuilder().title("wllpeter").description("测试@" + localname)
                .termsOfServiceUrl("http://" + localip + "/swagger-ui.html").version("1.0.0").build();
    }
}
