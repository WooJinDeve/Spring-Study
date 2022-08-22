package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
    * */
    public Long join(Member member){
        //같은 이름이 있는 중복 회원X
        validateDulicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDulicateMember(Member member) {
        //ifPresent null 아니라 값이 있으면
        memberRepository.findByName(member.getName())
                .ifPresent(m ->{
                     throw new IllegalStateException("이미 존재하는 회원입니다.");
                 });
    }

    /**
     * 전체 회원 조희
     * */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
