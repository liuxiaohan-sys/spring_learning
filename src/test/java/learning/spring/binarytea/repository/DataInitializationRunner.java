package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.*;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataInitializationRunner implements ApplicationRunner {

    @Autowired
    private TeaMakerJpaRepository teaMakerJpaRepository;
    @Autowired
    private MenuJpaRepository menuJpaRepository;
    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        List<MenuItemEntity> menuItemEntityList = Stream.of("Go橙汁", "Python气泡水", "JavaScript苏打水")
                .map(n -> MenuItemEntity.builder().name(n)
                        .size(Size.MEDIUM)
                        .price(Money.ofMinor(CurrencyUnit.of("CNY"), 1200))
                        .build())
                .peek(m -> menuJpaRepository.save(m)).collect(Collectors.toList());

        List<TeaMaker> teaMakerList = Stream.of("LiLei", "HanMeimei")
                .map(n -> TeaMaker.builder().name(n).build())
                .peek(m -> teaMakerJpaRepository.save(m)).collect(Collectors.toList());
        Order order = Order.builder()
                .maker(teaMakerList.get(0))
                .amount(Amount.builder()
                        .discount(90)
                        .totalAmount(Money.ofMinor(CurrencyUnit.of("CNY"), 1200))
                        .payAmount(Money.ofMinor(CurrencyUnit.of("CNY"), 1080))
                        .build())
                .items(List.of(menuItemEntityList.get(0)))
                .status(OrderStatus.ORDERED)
                .build();
        orderJpaRepository.save(order);

        try{
            Thread.sleep(200);
        } catch (InterruptedException ignored) {

        }

        order = Order.builder()
                .maker(teaMakerList.get(0))
                .amount(Amount.builder()
                        .discount(100)
                        .totalAmount(Money.ofMinor(CurrencyUnit.of("CNY"), 1200))
                        .payAmount(Money.ofMinor(CurrencyUnit.of("CNY"), 1200))
                        .build())
                .items(List.of(menuItemEntityList.get(1)))
                .status(OrderStatus.ORDERED)
                .build();
        orderJpaRepository.save(order);
    }
}
