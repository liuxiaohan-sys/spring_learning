package learning.spring.binarytea.repository.plus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import learning.spring.binarytea.model.plus.MenuItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.BaseTypeHandler;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository("menuItemMapper")
public interface MenuItemMapper extends BaseMapper<MenuItem> {
    @Select("select m.* from t_menu m, t_order_item i where m.id = i.item_id and i.order_id = #{orderId}")
    List<MenuItem> findByOrderId(Long orderId);
}
