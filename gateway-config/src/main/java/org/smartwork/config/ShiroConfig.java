package org.smartwork.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.smartwork.config.filter.JwtFilter;
import org.smartwork.config.realm.ExtRealm;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/***
 * ShiroConfig概要说明：shiro 配置类
 * @author Huanghy
 */
@Configuration
@Order(value = Integer.MIN_VALUE)
public class ShiroConfig implements Ordered {
	
	/**
	 * Filter Chain定义说明 
	 * 
	 * 1、一个URL可以配置多个Filter，使用逗号分隔
	 * 2、当设置多个过滤器时，全部验证通过，才视为通过
	 * 3、部分过滤器可指定参数，如perms，roles
	 */
	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 拦截器
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置不会被拦截的链接 顺序判断
		//登录接口排除
		filterChainDefinitionMap.put("/**/sys/login", "anon");
		//检验接口排除
		filterChainDefinitionMap.put("/**/sys/common/preview", "anon");
		//检验接口排除
		filterChainDefinitionMap.put("/**/sys/common/download", "anon");
		//新闻接口排除
		filterChainDefinitionMap.put("/**/api-news/*", "anon");
		//新闻类型接口排除
		filterChainDefinitionMap.put("/**/api-news-type/*", "anon");
		//登录验证码
		filterChainDefinitionMap.put("/**/auth/2step-code", "anon");
		//导出接口
		filterChainDefinitionMap.put("/**/exportXls", "anon");
		//导入接口
		filterChainDefinitionMap.put("/**/importExcel", "anon");
		//图片预览不限制token
		filterChainDefinitionMap.put("/**/sys/common/view/**", "anon");
		//智工任务大厅
		//filterChainDefinitionMap.put("/**/api/v1.0/task/list/**", "anon");
		//智工任务大厅查看最新成交动态
		//filterChainDefinitionMap.put("/**/api/v1.0/task/order/**", "anon");
		//任务总数
		//filterChainDefinitionMap.put("/**/api/v1.0/task/all-count/**", "anon");
		//行业类型查询
		//filterChainDefinitionMap.put("/**/api/v1.0/zgtindtype/lists/**", "anon");
		//任务类型查询
		//filterChainDefinitionMap.put("/**/api/v1.0/zgtasktype/lists/**", "anon");
		//参与任务竞标人员查询
		//filterChainDefinitionMap.put("/**/api/v1.0/taskmembers/list/**", "anon");
		//任务
		//filterChainDefinitionMap.put("/**/api/v1.0/taskdetail/detail/**", "anon");
		//任务推荐
		//filterChainDefinitionMap.put("/**/api/v1.0/recommend/list/**", "anon");
		//暂时取消token认证,方便调接口
		filterChainDefinitionMap.put("/**/api/v1.0/*", "anon");

		filterChainDefinitionMap.put("/**/*.js", "anon");
		filterChainDefinitionMap.put("/**/*.css", "anon");
		filterChainDefinitionMap.put("/**/*.html", "anon");
		filterChainDefinitionMap.put("/**/*.svg", "anon");
		filterChainDefinitionMap.put("/**/*.jpg", "anon");
		filterChainDefinitionMap.put("/**/*.png", "anon");
		filterChainDefinitionMap.put("/**/*.ico", "anon");
		filterChainDefinitionMap.put("/druid/**", "anon");
		filterChainDefinitionMap.put("/docs.html", "anon");
		filterChainDefinitionMap.put("/swagger**/**", "anon");
		filterChainDefinitionMap.put("/webjars/**", "anon");
		filterChainDefinitionMap.put("/v2/**", "anon");
		//性能监控
		filterChainDefinitionMap.put("/actuator/metrics/**", "anon");
		filterChainDefinitionMap.put("/actuator/httptrace/**", "anon");
		filterChainDefinitionMap.put("/redis/**", "anon");
		// 添加推单人的过滤器并且取名为jwt
		Map<String, Filter> filterMap = new HashMap<String, Filter>(2);
		filterMap.put("jwt", new JwtFilter());
		shiroFilterFactoryBean.setFilters(filterMap);
		// <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
		filterChainDefinitionMap.put("/**", "jwt");
		// 未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	/***
	 * securityManager方法慨述:
	 * @param extRealm
	 * @return DefaultWebSecurityManager
	 * @创建人 huanghy
	 * @创建时间 2019年11月15日 下午9:22:23
	 * @修改人 (修改了该文件，请填上修改人的名字)
	 * @修改日期 (请填上修改该文件时的日期)
	 */
	@Bean("securityManager")
	public DefaultWebSecurityManager securityManager(ExtRealm extRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(extRealm);
		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		securityManager.setSubjectDAO(subjectDAO);
		return securityManager;
	}

	/**
	 * 下面的代码是添加注解支持
	 * @return
	 */
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
