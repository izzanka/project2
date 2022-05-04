
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfr;

import Connection.DBConnection;
import Frame.LoginFrame;
/**
 *
 * @author ASUS
 */
public class Pfr {
    
    public static void main(String[] args) {
        System.out.println(DBConnection.getConnection());
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }
    
}
