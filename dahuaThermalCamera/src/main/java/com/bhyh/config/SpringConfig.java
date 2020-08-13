package com.bhyh.config;

//import java.util.Properties;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
//import org.hibernate.SessionFactory;
//import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.orm.hibernate5.HibernateTemplate;
//import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

/**
 * IOC容器相关配置（Datasource Hibernate Mybatis Commondao ）
 * @author david_lin
 *
 */
@Configuration
public class SpringConfig {
	@Value("${spring.datasource.username}")
    private String username;
	
	@Value("${spring.datasource.password}")
    private String password;
	
	@Value("${spring.datasource.url}")
    private String url;
	
	@Value("${spring.datasource.driver-class-name}")
    private String driver;
	
//	@Value("${spring.jpa.properties.hibernate.dialect}")
//    private String DIALECT;
//
//    @Value("${spring.jpa.show_sql}")
//    private String SHOW_SQL;
//
//    @Value("${spring.jpa.hbm2ddl.auto}")
//    private String HBM2DDL_AUTO;
//
//    @Value("${spring.jpa.entitymanager.packagesToScan}")
//    private String PACKAGES_TO_SCAN;
	
	
	
	@Bean
	public DataSource dataSource() {
		DataSourceBuilder<?> ds = DataSourceBuilder.create();
		ds.username(username);
		ds.password(password);
		ds.url(url);
		ds.driverClassName(driver);
		return ds.build();
	}
	/**
	 * Mybatis的SqlSessionFactory
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	@Bean
    public SqlSessionFactory SqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        return sessionFactory.getObject();
    }
    /**
     * Mybatis的SqlSessionTemplate
     * @param sqlSessionFactory
     * @return
     */
    @Bean
    public SqlSessionTemplate SqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    	System.out.println("=======================hello,sqlSessionTemplate^^^^^^^^^^");
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    /**
     * Hibernate 
     */
 
//	@Bean
//	public LocalSessionFactoryBean sessionFactory() {
//    	System.out.println("Hibernate SessionFactory初始化=========");
//    	LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//		sessionFactory.setDataSource(this.dataSource());
//        sessionFactory.setPackagesToScan(PACKAGES_TO_SCAN);		
//        Properties hibernateProperties = new Properties();
//        hibernateProperties.put("hibernate.dialect", DIALECT);
//        hibernateProperties.put("hibernate.show_sql", SHOW_SQL);
//        hibernateProperties.put("hibernate.hbm2ddl.auto", HBM2DDL_AUTO);
//        sessionFactory.setHibernateProperties(hibernateProperties);
//     	return sessionFactory;		
//	}
//	@Bean
//	public HibernateTemplate hibernateTemplate() {
//		HibernateTemplate hibernateTemplate = new HibernateTemplate((SessionFactory) sessionFactory());
//		return hibernateTemplate;
//	}
//	@Bean(value = "commonDao")
//	public ICommonDao commonDao() {
//		return new CommonDao(this.hibernateTemplate());	
//	}
}


















