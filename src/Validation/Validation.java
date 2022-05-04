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
public class Validation {
    public void isNull(String msg){
        JOptionPane.showMessageDialog(null, msg + " cannot null!");
    }
    
    public void isSuccess(String msg){
        JOptionPane.showMessageDialog(null, msg + " success!");
    }
    
    public void isFailed(String msg){
        JOptionPane.showMessageDialog(null, msg + " failed!");
    }
    
    public void isError(String msg){
        System.out.println(msg);
    }
}
