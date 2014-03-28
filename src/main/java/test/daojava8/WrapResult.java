/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.daojava8;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

/**
 *
 * @author kentaro.maeda
 */
public class WrapResult<T> implements Iterator<T> {

    private Statement st;
    private ResultSet rs;
    private boolean hasNext;
    private T cur;
    private RSMapFunction<T> mapper;

    public WrapResult(Statement st, ResultSet rs, RSMapFunction<T> mapper) {
        this.st = st;
        this.rs = rs;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        try {
            hasNext = rs.next();
            if (!hasNext) {
                st.close();
                return hasNext;
            }
            cur = mapper.apply(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hasNext;
    }

    @Override
    public T next() {
        return cur;
    }

}
