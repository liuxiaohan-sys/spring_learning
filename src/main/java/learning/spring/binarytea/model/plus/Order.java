package learning.spring.binarytea.model.plus;

import learning.spring.binarytea.model.Amount;
import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.OrderStatus;
import learning.spring.binarytea.model.TeaMaker;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private TeaMaker maker;
    private List<MenuItem> items;
    private Amount amount;
    private OrderStatus status;
    private Date createTime;
    private Date updateTime;
}
