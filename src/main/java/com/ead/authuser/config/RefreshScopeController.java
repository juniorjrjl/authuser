package com.ead.authuser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class RefreshScopeController {

    private final String name;

    public RefreshScopeController(@Value("${authuser.refreshscope.name}") final String name) {
        this.name = name;
    }

    @RequestMapping("/refreshscope")
    public String refreshScope(){
        return this.name;
    }

}
