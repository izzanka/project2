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
public class TransactionQuery {
    public String getAll = "SELECT t.id , t.amount, t.description, t.date, "
            + "c.type, c.name AS 'category name' FROM transactions t "
            + "INNER JOIN categories c ON t.category_id = c.id WHERE user_id = ?";
    
    public String getByType = "SELECT t.id, t.amount, t.description, t.date,"
            + "c.type, c.name AS 'category name' FROM transactions t "
            + "INNER JOIN categories c ON t.category_id = c.id "
            + "WHERE c.type = ? AND user_id = ?";
    
    public String create = "INSERT INTO transactions (user_id,category_id,"
            + "amount,description,date) VALUES (?,?,?,?,?)";
    
    public String update = "UPDATE transactions SET user_id = ?, category_id = ?,"
            + "amount = ?, description = ?, date = ? WHERE id = ?";
    
    public String delete = "DELETE FROM transactions WHERE id = ?";
}
