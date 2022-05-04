/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Query;

/**
 *
 * @author ASUS
 */
public class UserQuery {
    public String login = "SELECT * FROM users WHERE username = ? "
                        + "AND password = ?";
    
    public String register = "INSERT INTO users (username,password) "
                           + "VALUES " + "(?, ?)";
    
    public String updateSaldo = "UPDATE users SET saldo = ? WHERE id = ? ";
    public String getSaldo = "SELECT saldo FROM users WHERE id = ? ";
}
