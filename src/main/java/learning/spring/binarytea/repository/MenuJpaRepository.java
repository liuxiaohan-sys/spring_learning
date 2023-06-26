package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.MenuItemEntity;
import learning.spring.binarytea.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuJpaRepository extends JpaRepository<MenuItemEntity, Long> {
    Optional<MenuItemEntity> findByNameAndSize(String name, Size size);
}
