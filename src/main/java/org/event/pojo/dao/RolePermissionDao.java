package org.event.pojo.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.event.pojo.RolePermission;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionDao extends RolePermission {
    private String roleName;
    private String permissionName;
    private String updateUserName;
}
