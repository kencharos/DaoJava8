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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
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
    
    /** limit等で最後までフェッチされない場合にもステートメントを閉じたいのだが、、 */
    public void close() {
            try {
                if(!st.isClosed()) {
                    System.out.println("close..");
                    st.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
