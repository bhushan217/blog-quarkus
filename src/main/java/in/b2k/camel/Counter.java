package in.b2k.camel;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Counter {
    private final AtomicInteger value = new AtomicInteger(0);

    public int increment() {
        return value.incrementAndGet();
    }

    public int getValue() {
        return value.get();
    }
}
