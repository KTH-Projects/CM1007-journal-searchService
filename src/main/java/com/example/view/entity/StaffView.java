package com.example.view.entity;

import com.example.core.entity.Staff;
import com.example.util.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffView {
    private String id;
    private String name;
    private int age;
    private String sex;
    private Role role;

    public static StaffView convert(Staff staff){
        StaffView s = new StaffView();
        s.setId(staff.getId());
        s.setAge(staff.getAge());
        s.setRole(staff.getRole());
        s.setSex(staff.getSex());
        s.setName(staff.getName());
        return s;
    }

}
