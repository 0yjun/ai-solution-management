package phzzk.aisolutionmanagement.api.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import phzzk.aisolutionmanagement.api.auth.dto.LoginRequestDto;
import phzzk.aisolutionmanagement.api.auth.dto.LoginResponseDto;
import phzzk.aisolutionmanagement.api.auth.dto.SignupRequestDto;
import phzzk.aisolutionmanagement.api.auth.dto.SignupResponseDto;
import phzzk.aisolutionmanagement.common.exception.CustomException;
import phzzk.aisolutionmanagement.common.exception.ErrorCode;
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

    public SignupResponseDto signup(SignupRequestDto request) {
        Member member = memberService.register(request);
        return modelMapper.map(member, SignupResponseDto.class);
    }

    public LoginResponseDto login(LoginRequestDto request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }
        return new LoginResponseDto(member.getUsername(), member.getRole());
    }
}