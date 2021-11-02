package restfulwebservice03;

import java.util.Set;
import java.util.stream.Collectors;
import static restfulwebservice03.ApplicationUserPermissions.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

public enum ApplicationUserRoles {
	
	STUDENT(Sets.newHashSet(STUDENT_READ)), // ROLE_STUDENT
	ADMIN(Sets.newHashSet(STUDENT_READ, STUDENT_WRITE)); //ROLE_ADMIN
	
	private final Set<ApplicationUserPermissions> permissions;

	public Set<ApplicationUserPermissions> getPermissions() {
		return permissions;
	}
	
	private ApplicationUserRoles(Set<ApplicationUserPermissions> permissions) {
		this.permissions = permissions;
	}
	
	///////
	public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
		
		Set<SimpleGrantedAuthority> permissions = getPermissions().
															stream().
															map(permission -> new SimpleGrantedAuthority(permission.getPermission())).
															collect(Collectors.toSet());
		
		permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		
		return permissions;
		}

	

}