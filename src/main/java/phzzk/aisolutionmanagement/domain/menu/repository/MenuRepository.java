package phzzk.aisolutionmanagement.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import phzzk.aisolutionmanagement.domain.menu.entity.Menu;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    /**
     * 부모가 없는(isRoot) 활성화된 메뉴와,
     * 자식 중 isActive = true 인 것만 Fetch Join
     */
    @Query("""
      SELECT DISTINCT m
      FROM Menu m
      LEFT JOIN FETCH m.children c
      WHERE m.parent IS NULL
        AND m.isActive = true
    """)
    List<Menu> findAllActiveWithChildren();
}