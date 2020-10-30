package miu.edu.usermanagement;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UserManagementConfigurer implements WebMvcConfigurer {

//    @Bean
//    public PasswordEncoder encoder() {
    //TODO error: "BCryptPasswordEncoder" "illegal character: '\u200b', WHY???
//        return new BCryptPasswordEncoder();
//    }​​​​​

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder(16);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:errorMessages", "classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    //To use custom name messages in a properties file like we need to define a LocalValidatorFactoryBean and register the messageSource
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
