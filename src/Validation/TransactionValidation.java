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
public class TransactionValidation extends Validation{
    public void isMoreThanCurrentSaldo(){
        JOptionPane.showMessageDialog(null, "Amount cannot more than saldo!");
    }
    
    public void isZero(String msg){
        JOptionPane.showMessageDialog(null, msg + " cannot null(zero)!");
    }
    
    public void isString(String msg){
        JOptionPane.showMessageDialog(null, msg + " can only number!");
    }
}
