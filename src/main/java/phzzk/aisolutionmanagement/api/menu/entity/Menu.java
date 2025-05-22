package phzzk.aisolutionmanagement.api.menu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import phzzk.aisolutionmanagement.api.help.entity.Help;
import phzzk.aisolutionmanagement.common.constants.Role;
import phzzk.aisolutionmanagement.common.converter.RoleConverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"parent","children","prevMenu","nextMenu"})
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 200)
    private String url;

    @Column(nullable = false)
    private Integer seq;

    @Column(length = 100)
    private String icon;

    @Convert(converter = RoleConverter.class)
    @Column(name = "roles", nullable = false, length = 100)
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private boolean isActive;

    // — prev/next id 저장용 컬럼
    @Column(name = "prev_menu_id")
    private Integer prevMenuId;
    @Column(name = "next_menu_id")
    private Integer nextMenuId;

    // — prev/next 객체 매핑 (조회 전용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_menu_id", insertable = false, updatable = false)
    private Menu prevMenu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_menu_id", insertable = false, updatable = false)
    private Menu nextMenu;

    @Column(name = "parent_id", insertable = false, updatable = false)
    private Integer parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> children = new ArrayList<>();

    @OneToOne(
            mappedBy = "menu",
            cascade   = CascadeType.REMOVE,   // DELETE Menu → DELETE Help
            orphanRemoval = true,             // Menu에서 Help 참조 끊기면 Help 삭제
            fetch     = FetchType.LAZY
    )
    private Help help;
}
