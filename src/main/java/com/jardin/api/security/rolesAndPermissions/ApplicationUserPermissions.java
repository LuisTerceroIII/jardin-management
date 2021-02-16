package com.jardin.api.security.rolesAndPermissions;

public enum ApplicationUserPermissions {
  VISITOR_READ("visitor:read"),
  ADMIN_READ("admin:read"),
  ADMIN_WRITE("admin:write");

  private final String permission;

  ApplicationUserPermissions(String permission) {
    this.permission = permission;
  }

  public String getPermission() {
    return permission;
  }
}
