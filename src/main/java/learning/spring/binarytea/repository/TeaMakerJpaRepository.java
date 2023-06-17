package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.Order;
import learning.spring.binarytea.model.TeaMaker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeaMakerJpaRepository extends JpaRepository<TeaMaker, Long> {
}
