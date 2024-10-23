package org.event.pojo.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.event.pojo.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDao extends Role {
    private String updateUserName;
    private String createUserName;
}
