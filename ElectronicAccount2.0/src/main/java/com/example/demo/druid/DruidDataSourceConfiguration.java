package com.example.demo.druid;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

@Component
@ConfigurationProperties(prefix="spring.datasource")
public class DruidDataSourceConfiguration {
	
	private static final Logger log = LoggerFactory.getLogger(DruidDataSourceConfiguration.class);
	
	private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int initialSize;
    private int minIdle;
    private int maxActive;
    private long maxWait;
    private long timeBetweenEvictionRunsMillis;
    private long minEvictableIdleTimeMillis;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean poolPreparedStatements;
    private int maxPoolPreparedStatementPerConnectionSize;
    private long removeAbandonedTimeoutMillis;
    private boolean removeAbandoned;
    private boolean logDifferentThread;
    private String filters;
    private String connectionProperties;
    private boolean useGlobalDataSourceStat;
    
    public String getdriverClassName() {return driverClassName;}
	public void setdriverClassName(String driverClassName) {this.driverClassName = driverClassName;}
	
	public String geturl() {return url;}
	public void seturl(String url) {this.url = url;}
	
	public String getusername() {return username;}
	public void setusername(String username) {this.username = username;}
	
	public String getpassword() {return password;}
	public void setpassword(String password) {this.password = password;}
    
	public int getinitialSize() {return initialSize;}
	public void setinitialSize(int initialSize) {this.initialSize = initialSize;}
	
	public int getminIdle() {return minIdle;}
	public void setminIdle(int minIdle) {this.minIdle = minIdle;}
    
	public int getmaxActive() {return maxActive;}
	public void setmaxActive(int maxActive) {this.maxActive = maxActive;}
	
	public long getmaxWait() {return maxWait;}
	public void setmaxWait(long maxWait) {this.maxWait = maxWait;}
	
	public long gettimeBetweenEvictionRunsMillis() {return timeBetweenEvictionRunsMillis;}
	public void settimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;}
    
	public long getminEvictableIdleTimeMillis() {return minEvictableIdleTimeMillis;}
	public void setminEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;}
	
	public String getvalidationQuery() {return validationQuery;}
	public void setvalidationQuery(String validationQuery) {this.validationQuery = validationQuery;}
	
	public boolean gettestWhileIdle() {return testWhileIdle;}
	public void settestWhileIdle(boolean testWhileIdle) {this.testWhileIdle = testWhileIdle;}
	
	public boolean gettestOnBorrow() {return testOnBorrow;}
	public void settestOnBorrow(boolean testOnBorrow) {this.testOnBorrow = testOnBorrow;}
	
	public boolean gettestOnReturn() {return testOnReturn;}
	public void settestOnReturn(boolean testOnReturn) {this.testOnReturn = testOnReturn;}
	
	public boolean getpoolPreparedStatements() {return poolPreparedStatements;}
	public void setpoolPreparedStatements(boolean poolPreparedStatements) {this.poolPreparedStatements = poolPreparedStatements;}
	
	public int getmaxPoolPreparedStatementPerConnectionSize() {return maxPoolPreparedStatementPerConnectionSize;}
	public void setmaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;}
	
	public long getremoveAbandonedTimeoutMillis() {return removeAbandonedTimeoutMillis;}
	public void setremoveAbandonedTimeoutMillis(long removeAbandonedTimeoutMillis) {this.removeAbandonedTimeoutMillis = removeAbandonedTimeoutMillis;}
	
	public boolean getremoveAbandoned() {return removeAbandoned;}
	public void setremoveAbandoned(boolean removeAbandoned) {this.removeAbandoned = removeAbandoned;}
	
	public boolean getlogDifferentThread() {return logDifferentThread;}
	public void setlogDifferentThread(boolean logDifferentThread) {this.logDifferentThread = logDifferentThread;}
	
	public String getfilters() {return filters;}
	public void setfilters(String filters) {this.filters = filters;}
	
	public String getconnectionProperties() {return connectionProperties;}
	public void setconnectionProperties(String connectionProperties) {this.connectionProperties = connectionProperties;}
	
	public boolean getuseGlobalDataSourceStat() {return useGlobalDataSourceStat;}
	public void setuseGlobalDataSourceStat(boolean useGlobalDataSourceStat) {this.useGlobalDataSourceStat = useGlobalDataSourceStat;}

	
	//告诉框架，如果开发者没有指定其他数据源，那么就默认调用这个方法来提供数据源
	@Bean(initMethod = "init", destroyMethod = "close")
    @Primary 
    public DataSource druidDataSource() {        
    	DruidDataSource dataSource = new DruidDataSource();
    	dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
    	dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setPoolPreparedStatements(poolPreparedStatements);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        dataSource.setRemoveAbandonedTimeoutMillis(removeAbandonedTimeoutMillis);
        dataSource.setRemoveAbandoned(removeAbandoned);
        dataSource.setLogDifferentThread(logDifferentThread);

        dataSource.setConnectionProperties(connectionProperties);
        dataSource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);

        return dataSource;
    }  
	
	@Bean
	public WallFilter wallFilter(){
	    WallFilter wallFilter=new WallFilter();
	    wallFilter.setConfig(wallConfig());
	    return  wallFilter;
	}
	@Bean
	public WallConfig wallConfig(){
	    WallConfig config =new WallConfig();
	    config.setMultiStatementAllow(true);//允许一次执行多条语句
	    config.setNoneBaseStatementAllow(true);//允许非基本语句的其他语句
	    return config;
	}

	
	@Bean
	public ServletRegistrationBean<StatViewServlet> druidServlet() {
		log.info("init Druid Servlet Configuration ");
		ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<StatViewServlet>(new StatViewServlet(), "/druid/*"); // 进行druid监控的配置处理操作
		servletRegistrationBean.addUrlMappings("/druid/*");
		
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("loginUsername", "admin");// 用户名
		initParameters.put("loginPassword", "QAZqaz123");// 密码
		initParameters.put("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能
		initParameters.put("allow", ""); // IP白名单 (没有配置或者为空，则允许所有访问)
		
		servletRegistrationBean.setInitParameters(initParameters);
		return servletRegistrationBean;
	}
 
	@Bean
	public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
		FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<WebStatFilter>();
		filterRegistrationBean.setFilter(new WebStatFilter());
 
		// 添加过滤规则
		filterRegistrationBean.addUrlPatterns("/*");
 
		// 添加不需要忽略的格式信息.
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}
	
}
