/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.daojava8.model;

import java.util.ArrayList;
import java.util.List;
import test.daojava8.RSMapFunction;

/**
 *
 * @author kentaro.maeda
 */
public class Dept {

    public String deptId;
    public String deptName;
    public List<User> users = new ArrayList<>();

    public Dept(String id, String name) {
        this.deptId = id;
        this.deptName = name;
    }

    public String toString() {
        return deptId + ":" + deptName + ";" + users;
    }
    
    public static final RSMapFunction<Dept> MAPPER = rs -> 
          new Dept(rs.getString("deptId"), rs.getString("deptName"));
   
}
