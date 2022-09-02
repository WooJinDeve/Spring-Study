package hello.advanced.trace;

public class TraceStatus {
    private TraceId traceId;
    public Long startTimeMs;
    private String message;

    public TraceStatus(TraceId traceId, Long startTimems, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimems;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }
}
