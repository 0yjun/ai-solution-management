package phzzk.aisolutionmanagement.api.member.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import phzzk.aisolutionmanagement.api.auth.dto.SignupRequestDto;
import phzzk.aisolutionmanagement.api.member.dto.MemberAdminDto;
import phzzk.aisolutionmanagement.common.constants.Role;
import phzzk.aisolutionmanagement.common.exception.CustomException;
import phzzk.aisolutionmanagement.common.exception.ErrorCode;
import phzzk.aisolutionmanagement.api.member.repository.MemberRepository;
import phzzk.aisolutionmanagement.api.member.entity.Member;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public Member register(SignupRequestDto request) {
        validateDuplicateUsername(request.getUsername());
        Member member = Member.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .description(request.getDescription())
                .build();

        return memberRepository.save(member);
    }

    public Page<MemberAdminDto> getMemberPage(String search, Role role, Pageable pageable) {
        if (role == null) {
            // role 필터 없이 유저명 검색만
            return memberRepository
                    .findByUsernameContainingIgnoreCase(search, pageable)
                    .map(entity -> modelMapper.map(entity, MemberAdminDto.class));
        }
        // role이 지정된 경우에만 role 필터 추가
        return memberRepository
                .findByUsernameContainingIgnoreCaseAndRole(search, role, pageable)
                .map(entity -> modelMapper.map(entity, MemberAdminDto.class));
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public void validateDuplicateUsername(String username) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }
    }
}