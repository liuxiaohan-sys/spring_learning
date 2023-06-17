package learning.spring.binarytea;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import learning.spring.binarytea.actuator.SalesMetrics;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.Random;

@SpringBootApplication
@EnableScheduling
@ComponentScan(nameGenerator = UniqueNameGenerator.class)
public class BinaryTeaApplication {

	private static Logger logger = LoggerFactory.getLogger(BinaryTeaApplication.class);
	private Random random = new Random();
	@Autowired
	private SalesMetrics salesMetrics;

//	public static void main(String[] args) {
//		SpringApplication.run(BinaryTeaApplication.class, args);
//	}

	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(BinaryTeaApplication.class)
				.main(BinaryTeaApplication.class)
				.bannerMode(Banner.Mode.OFF)
				.web(WebApplicationType.SERVLET)
				.run(args);
	}

	@Bean
	public MeterRegistry customMeterRegistry(){
		CompositeMeterRegistry meterRegistry = new CompositeMeterRegistry();
		meterRegistry.add(new SimpleMeterRegistry());
		meterRegistry.add(new LoggingMeterRegistry());
		return meterRegistry;
	}

	@Scheduled(fixedDelay = 5000, initialDelay = 1000)
	public void periodicallyMakeAnOrder() {
		int amount = random.nextInt(100);
		salesMetrics.makeNewOrder(amount);
		logger.info("Make an order of RMB {} yuan.", amount);
	}

	/**
	 * 使用JPA repository接口(Spring Data)需要注释此处Hibernate的配置，否则报如下错误
	 * A component required a bean named 'entityManagerFactory' that could not be found.
	 */
//	@Bean
//	public LocalSessionFactoryBean sessionFactoryBean(DataSource dataSource) {
//		Properties properties = new Properties();
//		properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
//		properties.setProperty("hibernate.show_sql", "true");
//		properties.setProperty("hibernate.format_sql", "true");
//
//		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
//		factoryBean.setDataSource(dataSource);
//		factoryBean.setHibernateProperties(properties);
//		factoryBean.setPackagesToScan("learning.spring.binarytea.model");
//		return factoryBean;
//	}
//
//	@Bean
//	public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
//		return new HibernateTransactionManager(sessionFactory);
//	}



}
