package test;

import core.ApplicationContext;

public class Main {
    public static void main(String[] args) {
        // Version Annotations
        ApplicationContext context = new ApplicationContext(ServiceA.class, ServiceB.class);
        ServiceA serviceA = context.getBean(ServiceA.class);
        serviceA.doSomething();

        // Version XML
        ApplicationContext contextXml = new ApplicationContext("src/resources/beans.xml");
        ServiceA serviceAXml = contextXml.getBean(ServiceA.class);
        serviceAXml.doSomething();
    }
}
