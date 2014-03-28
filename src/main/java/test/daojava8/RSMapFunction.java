/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.daojava8;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
@FunctionalInterface
public interface RSMapFunction<T> {
    T apply(ResultSet rs) throws SQLException;
}
