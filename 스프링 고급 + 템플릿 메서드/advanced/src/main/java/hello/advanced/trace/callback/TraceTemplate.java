package hello.advanced.trace.callback;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;

public class TraceTemplate {
    private final LogTrace trace;

    public TraceTemplate(LogTrace trace) {
        this.trace = trace;
    }

    public <T> T excute(String message, TraceCallBack<T> callBack) {
        TraceStatus status = null;
        try{
            status = trace.begin(message);

            //로직 호출
            T result = callBack.call();

            trace.end(status);
            return result;
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }
    }
}
