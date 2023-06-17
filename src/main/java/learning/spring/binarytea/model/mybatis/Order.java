package learning.spring.binarytea.model.mybatis;


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
