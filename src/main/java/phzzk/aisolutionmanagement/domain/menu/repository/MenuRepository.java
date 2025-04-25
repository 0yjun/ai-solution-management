package phzzk.aisolutionmanagement.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import phzzk.aisolutionmanagement.domain.menu.entity.Menu;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT DISTINCT m FROM Menu m LEFT JOIN FETCH m.children WHERE m.parent is null ")
    List<Menu> findAllWithChildren();
}