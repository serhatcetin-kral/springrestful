package restfulwebservice03;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final PasswordEncoder passwordEncoder;
	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.
			csrf().disable().//This opens POST, PUT, PATCH, DELETE methods, they are closed as default
			authorizeRequests().
			antMatchers("/", "index", "/css/*", "/js/*").permitAll().
			antMatchers("/api/**").hasRole(ApplicationUserRoles.ADMIN.name()).// ==> "/api/**" can be accessed just by ADMIN
			anyRequest().
			authenticated().
			and().
			httpBasic();		
	}
	
	@Override
	@Bean      //when you want to ad user use that method put below
	protected UserDetailsService userDetailsService() {

		UserDetails student = User.
								builder().
								username("student").
								password(passwordEncoder.encode("12345")).
								authorities(ApplicationUserRoles.STUDENT.getGrantedAuthorities()).
								//roles(ApplicationUserRoles.STUDENT.name()).
								build();
		
		UserDetails admin = User.
								builder().
								username("admin").
								password(passwordEncoder.encode("12345")).
								authorities(ApplicationUserRoles.ADMIN.getGrantedAuthorities()).
							//roles(ApplicationUserRoles.ADMIN.name()).
								build();
		
		return new InMemoryUserDetailsManager(student, admin);
		
	}
}