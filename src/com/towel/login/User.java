package com.towel.login;

import com.towel.role.RoleMember;

public interface User extends RoleMember {
	public String getPassword();

	public String getUsername();
}
