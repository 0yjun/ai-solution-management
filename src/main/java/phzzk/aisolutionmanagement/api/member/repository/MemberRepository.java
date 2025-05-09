package phzzk.aisolutionmanagement.api.member.repository;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phzzk.aisolutionmanagement.api.member.entity.Member;
import phzzk.aisolutionmanagement.common.constants.Role;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    /**
     * username 부분 일치(Containing) + 대소문자 무시(IgnoreCase)
     * AND role 동일(Equals)
     * → Page<Member> 형태로 반환
     */
    Page<Member> findByUsernameContainingIgnoreCaseAndRole(
            String usernamePart,
            Role role,
            Pageable pageable
    );

    Page<Member> findByUsernameContainingIgnoreCase(String search, Pageable pageable);
}