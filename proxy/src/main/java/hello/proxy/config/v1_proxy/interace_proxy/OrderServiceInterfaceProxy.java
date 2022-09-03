package hello.proxy.config.v1_proxy.interace_proxy;

import hello.proxy.app.v1.OrderServiceV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderServiceInterfaceProxy implements OrderServiceV1 {

    private final OrderServiceV1 target;
    private final LogTrace logTrace;


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
