package learning.spring.binarytea;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import learning.spring.binarytea.actuator.SalesMetrics;
import learning.spring.binarytea.model.MenuItemEntity;
import learning.spring.binarytea.support.BytesToMoneyConverter;
import learning.spring.binarytea.support.MoneyToBytesConverter;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

@SpringBootApplication
@EnableScheduling
@EnableCaching
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

	@Bean
	public RedisTemplate<String, MenuItemEntity> redisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper){
		Jackson2JsonRedisSerializer<MenuItemEntity> serializer = new Jackson2JsonRedisSerializer<>(MenuItemEntity.class);
		serializer.setObjectMapper(objectMapper);

		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(RedisSerializer.string());
		redisTemplate.setValueSerializer(serializer);
		return redisTemplate;
	}

	@Bean
	public RedisTemplate redisRepositoryTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper){
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(RedisSerializer.string());
		redisTemplate.setValueSerializer(serializer);
		return redisTemplate;
	}

	@Bean
	public RedisCustomConversions redisCustomConversions(ObjectMapper objectMapper){
		return new RedisCustomConversions(Arrays.asList(new MoneyToBytesConverter(objectMapper), new BytesToMoneyConverter(objectMapper)));
	}


	@Scheduled(fixedDelay = 5000, initialDelay = 1000)
	public void periodicallyMakeAnOrder() {
		int amount = random.nextInt(100);
		salesMetrics.makeNewOrder(amount);
		logger.info("Make an order of RMB {} yuan.", amount);
	}

	@Bean
	public JodaMoneyModule jodaMoneyModule(){
		return new JodaMoneyModule();
	}
	@Bean
	public Hibernate5Module hibernate5Module(){
		return new Hibernate5Module();
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
