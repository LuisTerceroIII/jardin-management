package com.jardin.api.security.rolesAndPermissions;

import static com.jardin.api.security.rolesAndPermissions.ApplicationUserPermissions.*;

import com.google.common.collect.Sets;
import java.util.Set;

public enum ApplicationUserRoles {
  ADMIN(Sets.newHashSet(ADMIN_READ, ADMIN_WRITE)),
  VISITOR(Sets.newHashSet(VISITOR_READ));

  private final Set<ApplicationUserPermissions> permissions;

  ApplicationUserRoles(Set<ApplicationUserPermissions> permissions) {
    this.permissions = permissions;
  }

  public Set<ApplicationUserPermissions> getPermissions() {
    return permissions;
  }
}
