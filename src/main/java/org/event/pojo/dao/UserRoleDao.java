package org.event.pojo.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.event.pojo.UserRole;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDao extends UserRole {
    private String nickname;
    private String name;
    private String updateUserName;
}
