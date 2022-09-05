package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderServiceConcreteProxy extends OrderServiceV2 {

    private final OrderServiceV2 target;
    private final LogTrace logTrace;


    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void oderItem(String itemId) {
        TraceStatus statue = null;
        try {
            statue = logTrace.begin("OrderService.oderItem()");
            // target 호출
            target.oderItem(itemId);
            logTrace.end(statue);
        } catch (Exception e) {
            logTrace.exception(statue,e);
        }
    }
}
