package org.event.pojo.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.event.pojo.Permission;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionShowDao {
    private Integer id;
    private List<Permission> permissions;
}
