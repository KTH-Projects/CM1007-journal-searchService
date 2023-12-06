package com.example.core.entity;

import com.example.persistance.entity.StaffDB;
import com.example.util.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    private String id;
    private Role role;

    private String name;
    private int age;
    private String sex;

    public static Staff convert(StaffDB sDB){
        Staff s = new Staff();
        s.setRole(sDB.getRole());
        s.setSex(sDB.getSex());
        s.setId(sDB.getId());
        s.setAge(sDB.getAge());
        s.setName(sDB.getName());

        return s;
    }

}
