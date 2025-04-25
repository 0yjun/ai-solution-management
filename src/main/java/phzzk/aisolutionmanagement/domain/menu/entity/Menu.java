package phzzk.aisolutionmanagement.domain.menu.entity;

import jakarta.persistence.*;
import lombok.*;
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
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 200, unique = true)
    private String url;

    @Column(length = 100)
    private String icon;

    @Convert(converter = RoleConverter.class)
    @Column(name = "roles", nullable = false, length = 100)
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> children = new ArrayList<>();
}
