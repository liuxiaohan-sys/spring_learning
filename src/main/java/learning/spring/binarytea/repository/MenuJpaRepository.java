package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuJpaRepository extends JpaRepository<MenuItemEntity, Long> {
}
