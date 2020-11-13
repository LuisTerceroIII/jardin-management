package com.jardin.api.security.rolesAndPermissions;
import com.google.common.collect.Sets;
import java.util.Set;

import static com.jardin.api.security.rolesAndPermissions.ApplicationUserPermissions.*;

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
