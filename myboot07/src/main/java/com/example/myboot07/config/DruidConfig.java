package com.example.myboot07.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhenXinMa
 * @date 2019/12/16 0:24
 */
@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid(){
        //  耦合度高
        return new DruidDataSource();
    }

    /**
     *  servlet注册器
     * @return
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean(){

        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

        Map<String,String> initParam = new HashMap<>(10);

        // 允许登录的账号
        initParam.put("loginUsername","mzx123");
        // 允许登录的账号的密码
        initParam.put("loginPassword","mzx123");
        //  allow  默认是允许的
        initParam.put("allow","");
        // 拒绝登录的ip的地址
        initParam.put("deny","192.168.0.107");

        bean.setInitParameters(initParam);

        return bean;

    }

    /**
     *   配置过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistration(){

        FilterRegistrationBean bean = new FilterRegistrationBean();

        //  设置参数
        bean.setFilter(new WebStatFilter());

        Map<String,String> initParam = new HashMap<>(10);

        //  设置默认 对静态资源不拦截
        initParam.put("exclusions","*.js,*.css,*.js,/druid/*");

        bean.setUrlPatterns(Arrays.asList("/*"));
        bean.setInitParameters(initParam);

        return bean;

    }





}
