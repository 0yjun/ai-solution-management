package phzzk.aisolutionmanagement.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import phzzk.aisolutionmanagement.auth.dto.LoginRequest;
import phzzk.aisolutionmanagement.auth.dto.LoginResponse;
import phzzk.aisolutionmanagement.auth.dto.SignupRequest;
import phzzk.aisolutionmanagement.auth.dto.SignupResponse;
import phzzk.aisolutionmanagement.common.exception.CustomException;
import phzzk.aisolutionmanagement.common.exception.ErrorCode;
import phzzk.aisolutionmanagement.config.security.JwtTokenProvider;
import phzzk.aisolutionmanagement.member.entity.Member;
import phzzk.aisolutionmanagement.member.repository.MemberRepository;
import phzzk.aisolutionmanagement.member.service.MemberService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public SignupResponse signup(SignupRequest request) {
        Member member = memberService.register(request);
        log.info(member.toString());
        return modelMapper.map(member, SignupResponse.class);
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.username())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }
        return new LoginResponse(member.getUsername(), member.getRole());
    }
}