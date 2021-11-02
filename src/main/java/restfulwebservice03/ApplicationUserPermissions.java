package restfulwebservice03;

public enum ApplicationUserPermissions {
	
	STUDENT_READ("student:read"),
	STUDENT_WRITE("student:write");
	
	private final String permission;

	public String getPermission() {
		return permission;
	}
	
	private ApplicationUserPermissions(String permission) {
		this.permission = permission;
	}
}