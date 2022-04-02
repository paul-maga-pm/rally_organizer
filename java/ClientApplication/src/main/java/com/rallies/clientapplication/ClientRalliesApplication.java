package com.rallies.clientapplication;

import com.rallies.gui.RalliesApplicationFxGui;
import com.rallies.networking.client.proxy.RalliesServicesProxy;
import com.rallies.networking.client.proxy.RalliesServicesRpcProxy;
import javafx.application.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static com.rallies.gui.RalliesApplicationFxGui.setServices;


public class ClientRalliesApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("ClientRalliesApplicationConfig.xml");
        RalliesServicesRpcProxy services = context.getBean(RalliesServicesRpcProxy.class);
        setServices(services);
        Application.launch(RalliesApplicationFxGui.class, args);
    }
}
