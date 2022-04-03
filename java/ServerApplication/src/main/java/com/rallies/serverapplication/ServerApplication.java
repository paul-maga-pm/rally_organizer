package com.rallies.serverapplication;

import com.rallies.business.impl.RalliesApplicationServicesImpl;
import com.rallies.networking.server.impl.RpcServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("ServerApplicationConfig.xml");
        var services = context.getBean(RalliesApplicationServicesImpl.class);

        RpcServer server = context.getBean(RpcServer.class);
        server.start();
    }
}
