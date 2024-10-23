package org.event.pojo.dao;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.event.pojo.Permission;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDao extends Permission {
    private String createUserName;
    private List<PermissionDao> childrenList=new ArrayList<>();
}
