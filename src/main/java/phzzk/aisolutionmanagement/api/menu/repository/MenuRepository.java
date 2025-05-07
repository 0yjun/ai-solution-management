package phzzk.aisolutionmanagement.api.menu.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import phzzk.aisolutionmanagement.api.menu.entity.Menu;
import phzzk.aisolutionmanagement.common.constants.Role;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    /**
     * 1) 최상위 메뉴 + 자식 메뉴(fetch join children)
     */
//    @EntityGraph(attributePaths = {"children"})
//    List<Menu> findRootMenusOrderBySeq();

    /**
     * 2) 최상위 메뉴 + 자식 + 이전 + 다음(fetch join children, prevMenu, nextMenu)
     */
    @EntityGraph(attributePaths = {"children","prevMenu","nextMenu"})
    List<Menu> findByParentIsNullOrderBySeq();

    /**
     * 3) isActive = true인 최상위 메뉴 + 자식 + 이전 + 다음(fetch join children, prevMenu, nextMenu)
     */
    @EntityGraph(attributePaths = {"children","prevMenu","nextMenu"})
    List<Menu> findByParentIsNullAndIsActiveTrueOrderBySeq();

    /**
     * 4) 주어진 role에 속하는 최상위 메뉴 + 그 자식(fetch join children)
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
    List<Menu> findDistinctByParentIsNullAndRolesContainingOrderBySeq(String roleName);


}