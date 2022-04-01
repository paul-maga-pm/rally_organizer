package com.rallies.clientapplication;

import com.rallies.business.impl.RalliesApplicationServicesImpl;
import com.rallies.gui.RalliesApplicationFxGui;
import javafx.application.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static com.rallies.gui.RalliesApplicationFxGui.setServices;


public class ClientRalliesApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("ClientRalliesApplicationConfig.xml");
        RalliesApplicationServicesImpl services = context.getBean(RalliesApplicationServicesImpl.class);
        setServices(services);
        Application.launch(RalliesApplicationFxGui.class, args);
    }
}
