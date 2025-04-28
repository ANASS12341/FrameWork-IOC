package test;

import annotations.Component;
import annotations.Inject;

@Component
public class Service1 {
    @Inject
    private ServiceB serviceB;

    public void doSomething() {
        serviceB.help();
    }
}
