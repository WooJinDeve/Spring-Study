package hello.aop.exam;

import hello.aop.exam.annotation.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.annotation.Target;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;

    @Trace
    public void request(String itemId) {
        examRepository.save(itemId);
    }
}
