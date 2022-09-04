package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceFilterHanler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;
    private final String[] pattern;

    public LogTraceFilterHanler(Object target, LogTrace logTrace, String[] pattern) {
        this.target = target;
        this.logTrace = logTrace;
        this.pattern = pattern;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //메서드 이름 필터
        String methodName = method.getName();
        //save, request, reque*, *est;
        if(!PatternMatchUtils.simpleMatch(pattern, methodName)){
            return method.invoke(target, args);
        }

        TraceStatus statue = null;
        try {
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            statue = logTrace.begin(message);

            // Logic 호출
            Object result = method.invoke(target, args);

            logTrace.end(statue);
            return result;
        } catch (Exception e) {
            logTrace.exception(statue,e);
            throw e;
        }
    }
}
