/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author NicR
 */
public class SQLManage {

//Instance Variables
    private static String dbType = "mysql";
    private static String url = "52.206.157.109/U04H1H";
    private static String userName = "U04H1H";
    private static String password = "53688238168";
    
//Open Connection to DB
    public static Connection getConn() throws SQLException{
        System.out.println("Connections to SQL DB Opened");
        Connection conn = DriverManager.getConnection("jdbc:mysql://52.206.157.109/U04H1H", "U04H1H","53688238168");
        return conn;
    }
    
//Close connection 
    public void stopConn(Connection conn) throws SQLException{
        try{
            conn.close();
            System.out.println("Connection to SQL DB closed");
        }
        catch(SQLException err){
        }
    }
    
//Select Statement method for managing calendar
    public static PreparedStatement prepare(Connection conn, String sql, PreparedStatementSetter setter) throws SQLException{
        PreparedStatement ps = conn.prepareStatement(sql);
        setter.setValues(ps);
        return ps;
    }

    @FunctionalInterface
    public interface PreparedStatementSetter {
        void setValues(PreparedStatement ps) throws SQLException;
    }
    //"jdbc:mysql://52.206.157.109/U04H1H", "U04H1H","53688238168"
}