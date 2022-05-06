/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Validation;

import javax.swing.JOptionPane;

/**
 *
 * @author ASUS
 */
public class LoginFrameValidation extends Validation{
    public void isInvalid(String msg){
        JOptionPane.showMessageDialog(null, msg + " invalid!");
    }
}
