package phzzk.aisolutionmanagement.api.help.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import phzzk.aisolutionmanagement.api.menu.entity.Menu;
import phzzk.aisolutionmanagement.common.constants.Role;
import phzzk.aisolutionmanagement.common.converter.RoleConverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "help")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"helpId", "helpDescription, menuId"})
@Builder
public class Help {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "help_id")
    private Long helpId;

    @Column(name = "help_description", length = 2000)
    private String helpDescription;

    @OneToOne(optional = false /* cascade 없음 */, fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", unique = true, nullable = false)
    private Menu menu;

    @Column(name = "menu_id", insertable = false, updatable = false)
    private Integer menuId;

    @OneToMany(
            mappedBy      = "help",
            cascade       = CascadeType.ALL,    // PERSIST/MERGE/REMOVE 전파
            orphanRemoval = true                // Help에서 이미지 참조 제거 시 삭제
    )
    @OrderBy("seq ASC")
    private List<HelpImage> images = new ArrayList<>();

    // == 연관관계 편의 메서드 ==
    public void assignMenu(Menu menu) {
        this.menu = menu;
    }

    public void addImage(HelpImage image) {
        if (images.size() >= 3) {
            throw new IllegalStateException("이미지는 최대 3개까지 저장 가능합니다.");
        }
        image.setHelp(this);
        image.setSeq(images.size() + 1);
        images.add(image);
    }

    public void removeImage(int index) {
        if (index < 0 || index >= images.size()) {
            throw new IndexOutOfBoundsException("유효하지 않은 이미지 인덱스입니다: " + index);
        }
        images.remove(index);
        // 순서 재정렬
        for (int i = 0; i < images.size(); i++) {
            images.get(i).setSeq(i + 1);
        }
    }

    public void clearImages() {
        images.clear();
    }
}