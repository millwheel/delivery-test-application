package msa.customer.repository;

import msa.customer.DAO.Member;

import java.util.Optional;

public interface MemberRepository {
    public void make(Member member);
    Optional<Member> findByEmail(String email);
}
