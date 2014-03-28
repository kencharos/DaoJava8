/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.daojava8.model;

import test.daojava8.RSMapFunction;

/**
 *
 */
public class User {

    public String id;
    public String name;
    public Dept dept;

    public User(String id, String name, String deptId) {
        this.id = id;
        this.name = name;
        this.dept = new Dept(deptId, null);
    }

    public String toString() {
        return id + ":" + name + ":" + dept;
    }
    
    
    public static final RSMapFunction<User> MAPPER = rs ->
         new User(rs.getString("id"), rs.getString("name"), rs.getString("deptId"));
}
