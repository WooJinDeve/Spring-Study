package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV2Test {
    @Test
    void strategyV1(){
        ContextV2 contextV2 = new ContextV2();
        contextV2.execute(new StrategyLogic1());
        contextV2.execute(new StrategyLogic2());
    }

    @Test
    void strategyV2(){
        ContextV2 contextV2 = new ContextV2();
        contextV2.execute(() -> log.info("비즈니스 로직 1실행"));
        contextV2.execute(() -> log.info("비즈니스 로직 2실행"));
    }
}
