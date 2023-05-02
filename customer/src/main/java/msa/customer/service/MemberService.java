package msa.customer.service;

import lombok.extern.slf4j.Slf4j;
import msa.customer.DAO.Member;
import msa.customer.DTO.JoinForm;
import msa.customer.repository.MemberRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Boolean join(JoinForm joinData){
        Member member = new Member();
        if(memberRepository.findByEmail(joinData.getEmail()).isPresent()){
            log.info("this email is already used");
            return false;
        }
        member.setName(joinData.getName());
        member.setEmail(joinData.getEmail());
        if(!joinData.getPassword().equals(joinData.getPasswordConfirm())){
            log.info("Password doesn't match");
            return false;
        }
        member.setPassword(joinData.getPassword());
        memberRepository.make(member);
        return true;
    }

    public Member login(String email, String password){
        Optional<Member> user = memberRepository.findByEmail(email);
        if(user.isEmpty()){
            log.info("email doesn't exist");
            return null;
        }
        Member member = user.get();
        if(!password.equals(member.getPassword())){
            log.info("wrong password");
            return null;
        }
        return member;
    }

    public String getUserEmail(String token){
        String decodedPayload = parseJwtPayload(token);
        return getEmailFromPayload(decodedPayload);
    }

    public String parseJwtPayload(String token){
        String base64Payload = token.split("\\.")[1];
        byte[] decodedBytes = Base64.getDecoder().decode(base64Payload);
        return new String(decodedBytes);
    }

    public String getEmailFromPayload(String payloadString){
        JSONObject payloadJson = new JSONObject(payloadString);
        return payloadJson.getString("email");
    }

}
