package phzzk.aisolutionmanagement.api.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import phzzk.aisolutionmanagement.api.auth.dto.LoginRequest;
import phzzk.aisolutionmanagement.api.auth.dto.LoginResponse;
import phzzk.aisolutionmanagement.api.auth.dto.SignupRequest;
import phzzk.aisolutionmanagement.api.auth.dto.SignupResponse;
import phzzk.aisolutionmanagement.common.exception.CustomException;
import phzzk.aisolutionmanagement.common.exception.ErrorCode;
import phzzk.aisolutionmanagement.config.security.JwtTokenProvider;
import phzzk.aisolutionmanagement.api.member.entity.Member;
import phzzk.aisolutionmanagement.api.member.repository.MemberRepository;
import phzzk.aisolutionmanagement.api.member.service.MemberService;

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
        return modelMapper.map(member, SignupResponse.class);
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }
        return new LoginResponse(member.getUsername(), member.getRole());
    }
}