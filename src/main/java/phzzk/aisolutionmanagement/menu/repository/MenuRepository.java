package phzzk.aisolutionmanagement.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import phzzk.aisolutionmanagement.common.constants.Role;
import phzzk.aisolutionmanagement.menu.entity.Menu;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT DISTINCT m FROM Menu m LEFT JOIN FETCH m.children WHERE m.parent is null ")
    List<Menu> findAllWithChildren();
}