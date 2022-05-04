/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ASUS
 */
public class DBConnection {
    private static Connection connection = null;
     
    public static Connection getConnection(){
        if(connection != null) return connection;
        else{
            String dbUrl = "jdbc:mysql://localhost:3306/db_pfr?user=root&password=";
            try{
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(dbUrl);
                System.out.println("koneksi sukses");
            }catch(ClassNotFoundException | SQLException e){
                System.out.println("koneksi gagal : " + e);
            }
            return connection;
        }
    }
}
