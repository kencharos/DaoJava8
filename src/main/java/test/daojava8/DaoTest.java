package test.daojava8;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import test.daojava8.model.Dept;
import test.daojava8.model.User;

/**
 * 
 */
public class DaoTest {
   
    
    private static final String URL ="jdbc:h2:sample";
    private static final String USER ="sa";
    private static final String PASS ="";
    
    public static void main(String[] args) throws Exception{
        Class.forName("org.h2.Driver");
        
        try(Connection con = DriverManager.getConnection(URL, USER, PASS)){
        
            // 準備
            ddl(con, "drop table dept;"); // 初回はコメントアウト
            ddl(con, "drop table user;"); // 初回はコメントアウト
            ddl(con, "create table dept(deptId char(1), deptName varchar(10));");
            ddl(con, "create table user(id char(2), name varchar(10), deptId char(1));");
            ddl(con, "insert into dept values('1', '○部');");
            ddl(con, "insert into dept values('2', '△部');");
            ddl(con, "insert into dept values('3', '×部');");
            ddl(con, "insert into user values('01', 'tanaka', '1');");
            ddl(con, "insert into user values('02', 'tanaka2', '1');");
            ddl(con, "insert into user values('03', 'tanaka3', '3');");
            ddl(con, "insert into user values('04', 'tanaka4', '3');");
            ddl(con, "insert into user values('05', 'tanaka5', '3');");

            // stream実験
            DaoTest dao = new DaoTest();
            Stream<Dept> depts = dao.select(con, "select * from dept", Dept.MAPPER);
            System.out.println("select実行時点でSQLは実行されない");
            // forEach等、終端操作でSQL実行。
            depts.forEach(System.out::println);

            
            String userSql = "select * from user where deptId='%s'";
            Function<Dept, Dept> relation = d -> {
                d.users = dao.select(con, String.format(userSql, d.deptId), User.MAPPER)
                          .collect(Collectors.toList());
                return d;
            };
            Function<Dept, Stream<User>> oneToMany = d -> {
                return dao.select(con, String.format(userSql, d.deptId), User.MAPPER)
                            .peek(_u -> _u.dept = d);
             };

            // map,flatmapで展開
            System.out.println("部署に所属ユーザー一覧を足してみる。");
            dao.select(con, "select * from dept", Dept.MAPPER)
                    .map(d -> relation.apply(d))
                    .forEach(System.out::println);
            System.out.println("部署から所属ユーザーの一覧に変換して件数制限");
            dao.select(con, "select * from dept" , Dept.MAPPER)
                    .flatMap(d -> oneToMany.apply(d))
                    .skip(2)
                    .limit(3)
                    .forEach(System.out::println);
        }
        
    }
    
    public <R> Stream<R> select(Connection con, String sql, RSMapFunction<R> mapper){
        Statement st;
        ResultSet rs;
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
        } catch (SQLException e) {
             throw new RuntimeException(e);
        }
        WrapResult<R> itr = new WrapResult<>(st, rs, mapper);
        // StreamSupportで、イテレータからStreamを作れる。
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(itr, 0), false);
                
    }

    public static void ddl(Connection con, String sql) throws SQLException{
        Statement st = con.createStatement();
        st.executeUpdate(sql);
        st.close();
    }
}
