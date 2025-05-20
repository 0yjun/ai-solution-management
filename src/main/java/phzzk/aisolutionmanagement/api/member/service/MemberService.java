package phzzk.aisolutionmanagement.api.member.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phzzk.aisolutionmanagement.api.auth.dto.SignupRequestDto;
import phzzk.aisolutionmanagement.api.member.dto.*;
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

    public MemberClientDto register(SignupRequestDto request) {
        Member createMember =  createNewMember(
                request.getUsername(),
                request.getPassword(),
                request.getRole(),
                null
        );
        return modelMapper.map(createMember, MemberClientDto.class);
    }

    public MemberAdminDto createMemberByAdmin(MemberCreateRequestDto request) {
        Member createMember =  createNewMember(
                request.getUsername(),
                request.getPassword(),
                request.getRole(),
                request.getDescription()
        );
        return modelMapper.map(createMember, MemberAdminDto.class);
    }

    @Transactional
    public Member createNewMember(String username, String rawPassword, Role role, String description){
        validateDuplicateUsername(username);
        String encoded = passwordEncoder.encode(rawPassword);
        Member member = Member.builder()
                .username(username)
                .password(encoded)
                .role(role)
                .description(description)
                .build();
        return memberRepository.save(member);
    }

    public void validateDuplicateUsername(String username) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }
    }

    @Transactional
    public MemberAdminDto updateMember(Long memberId, MemberUpdateRequestDto memberUpdateRequestDto){
        if(!memberUpdateRequestDto.getId().equals(memberId)){
            throw new CustomException(ErrorCode.ID_MISMATCH);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        member.setRole(memberUpdateRequestDto.getRole());
        member.setDescription(memberUpdateRequestDto.getDescription());
        memberRepository.save(member);

        return modelMapper.map(member, MemberAdminDto.class);
    }

    @Transactional
    public MemberAdminDto resetPassword(
            Long memberId, MemberResetPasswordRequestDto memberResetPasswordRequestDto
    ){
        if(!memberResetPasswordRequestDto.getId().equals(memberId)){
            throw new CustomException(ErrorCode.ID_MISMATCH);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        String encoded = passwordEncoder.encode(memberResetPasswordRequestDto.getPassword());

        member.setPassword(encoded);
        memberRepository.save(member);

        return modelMapper.map(member, MemberAdminDto.class);
    }

    @Transactional
    public void deleteMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        memberRepository.delete(member);
    }
}