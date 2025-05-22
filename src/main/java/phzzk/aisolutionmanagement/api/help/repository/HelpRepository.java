package phzzk.aisolutionmanagement.api.help.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import phzzk.aisolutionmanagement.api.help.entity.Help;

import java.util.List;
import java.util.Optional;

public interface HelpRepository extends JpaRepository<Help, Long> {
    @EntityGraph(attributePaths = "images")
    Optional<Help> findByMenu_Id(Long menuId);
}
