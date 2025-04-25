package phzzk.aisolutionmanagement.member.entity;

import jakarta.persistence.*;
import lombok.*;
import phzzk.aisolutionmanagement.common.constants.Role;

import java.time.LocalDateTime;



@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 로그인 ID 및 식별자

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String description;

    private LocalDateTime lastLoginAt;

    @Column(nullable = false)
    private int loginFailCount;
}
