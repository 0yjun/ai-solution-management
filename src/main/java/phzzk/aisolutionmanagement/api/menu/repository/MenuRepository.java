package phzzk.aisolutionmanagement.api.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import phzzk.aisolutionmanagement.api.menu.entity.Menu;
import phzzk.aisolutionmanagement.common.constants.Role;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    /**
     * 주어진 role 에 속하는 최상위 메뉴와,
     * 그 자식 메뉴 중에도 role 에 속한 항목만 함께 페치
     */
    @Query("""
        SELECT DISTINCT m
          FROM Menu m
        LEFT JOIN FETCH m.children c
        WHERE m.parent IS NULL
    """)
    List<Menu> findAllWithChildren();

    /**
     * 주어진 role 에 속하는 최상위 메뉴와,
     * 그 자식 메뉴 중에도 role 에 속한 항목만 함께 페치
     */
    @Query("""
        SELECT DISTINCT m
        FROM Menu m
        LEFT JOIN FETCH m.children c
        WHERE m.parent IS NULL
          AND CONCAT(',', m.roles, ',') LIKE CONCAT('%,', :roleName, ',%')
          AND (c IS NULL
               OR CONCAT(',', c.roles, ',') LIKE CONCAT('%,', :roleName, ',%')
              )
    """)
    List<Menu> findAllByRoleWithChildren(@Param("roleName") String roleName);

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