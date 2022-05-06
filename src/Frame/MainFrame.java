/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Connection.DBConnection;
import Helper.Helper;
import Model.TransactionModel;
import Model.UserModel;
import Query.CategoryQuery;
import Query.TransactionQuery;
import Query.UserQuery;
import Validation.MainFrameValidation;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author ASUS
 */
public class MainFrame extends javax.swing.JFrame {
    
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    
    UserModel userModel;
    TransactionModel transactionModel;
    CategoryQuery categoryQuery;
    TransactionQuery transactionQuery;
    UserQuery userQuery;
    
    private int id = 0;
    private int saldo = 0;
    private int tempAmount = 0;
    
    Helper helper = new Helper();
    MainFrameValidation mainFrameValidation = new MainFrameValidation();
     
    public MainFrame(UserModel userModel) {
        initComponents();
        
        this.userModel = userModel;
        this.transactionModel = new TransactionModel();
        this.categoryQuery = new CategoryQuery();
        this.transactionQuery = new TransactionQuery();
        this.userQuery = new UserQuery();
        this.conn = DBConnection.getConnection();
        this.saldo += userModel.getSaldo();

        setCategoryName();
        setLabel();
        
        btnReset.setEnabled(false);
        btnDelete.setEnabled(false);
    }
    
    private MainFrame() {
        
    }
    
    public void clear(){
        txtDate.setDate(null);
        txtAmount.setText("");
        txtDesc.setText("");
        cbType.setSelectedIndex(0);
        cbCategory.setSelectedIndex(0);
        cbFilter.setSelectedIndex(0);
        
        this.id = 0;
        this.tempAmount = 0;
        cbType.setEnabled(true);
    }
    
    public void setLabel(){
        labelUsername.setText(userModel.getUsername());
        labelSaldo.setText(String.valueOf(this.saldo));
    }
    
    public void setSaldo(){
        try {
            pst = conn.prepareStatement(userQuery.updateSaldo);
            pst.setInt(1, this.saldo);
            pst.setInt(2, userModel.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            mainFrameValidation.isError(e.getMessage());
        }
    }
    
    public void setCategoryName(){
        try {
            String type = cbType.getSelectedItem().toString();
            pst = conn.prepareStatement(categoryQuery.getByType);
            pst.setString(1, type);
            rs = pst.executeQuery();

            while(rs.next()){
                cbCategory.addItem(rs.getString("name"));
            }
  
        } catch (SQLException e) {
            mainFrameValidation.isError(e.getMessage());
        }
    }
    
    public void setCategoryId(String name,String type){
        try {
            pst = conn.prepareStatement(categoryQuery.getByName);
            pst.setString(1, name);
            pst.setString(2, type);
            rs = pst.executeQuery(); 
            if(rs.next()){
               transactionModel.setCategory_id(rs.getInt("id"));
            }            
        } catch (SQLException e) {
            mainFrameValidation.isError(e.getMessage());
        }
    }
    
    public void setTable(ResultSet rs){
        tblTr.setModel(DbUtils.resultSetToTableModel(rs));
    }
    
    public void getAll(){
        try {
            pst = conn.prepareStatement(transactionQuery.getAll);
            pst.setInt(1, userModel.getId());
            rs = pst.executeQuery();
            setTable(rs);
        } catch (SQLException e) {
            mainFrameValidation.isError(e.getMessage());
        }
    }
    
    public void filterTable(){
        try {
            String filter = cbFilter.getSelectedItem().toString();
            if (filter.equals("All")) {
                getAll();
            }else{
                pst = conn.prepareStatement(transactionQuery.getByType);
                pst.setString(1, filter);
                pst.setInt(2, userModel.getId());
                rs = pst.executeQuery();
                setTable(rs);
            }
        } catch (SQLException e) {
            mainFrameValidation.isError(e.getMessage());
        }
    }
    
    public void create(){
         try {
             
            Date date = txtDate.getDate();
            int amount = new Integer(txtAmount.getText());
            String desc = txtDesc.getText();
            String type = cbType.getSelectedItem().toString();
            String category = cbCategory.getSelectedItem().toString();
            
            if(!type.equals("income")){
                if (amount > this.saldo) {
                    mainFrameValidation.isMoreThanCurrentSaldo();
                    return;
                }
            }
            
            setCategoryId(category,type);

            transactionModel.setDate(date);
            transactionModel.setAmount(amount);
            transactionModel.setDescription(desc);
            transactionModel.setUser_id(userModel.getId());
           
            String stringDate = helper.dateToString(transactionModel.getDate());
            
            if(this.id > 0){
                if(type.equals("income")){
                    this.saldo -= this.tempAmount;
                    this.saldo += amount;
                }else{
                    this.saldo += this.tempAmount;
                    this.saldo -= amount;
                }
            }else{
                if (type.equals("income")) {
                    this.saldo += amount;
                }else{
                    this.saldo -= amount;
                }
            }
           
            setSaldo();
            setLabel();
            
            String query;
            
            if(this.id > 0){
                query = transactionQuery.update;
            }else{
                query = transactionQuery.create;
            }

            pst = conn.prepareStatement(query);
            pst.setInt(1, userModel.getId());
            pst.setInt(2, transactionModel.getCategory_id());
            pst.setInt(3, transactionModel.getAmount());
            pst.setString(4, transactionModel.getDescription());
            pst.setString(5, stringDate);
            if(this.id > 0){
                pst.setInt(6, this.id);
            }

            int result = pst.executeUpdate();
            
            String msg;
            
            if(this.id > 0){
                msg = "update data";
            }else{
                msg = "create data";
            }
          
            if(result == 1){
                mainFrameValidation.isSuccess(msg);
                if (this.id > 0) {
                    btnDelete.setEnabled(false);
                }
            }else{
                mainFrameValidation.isFailed(msg);
            }      
            
        } catch (SQLException | ParseException e) {
            mainFrameValidation.isError(e.getMessage());
        }
    }
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelUsername = new javax.swing.JLabel();
        labelSaldo = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDate = new com.toedter.calendar.JDateChooser();
        txtAmount = new javax.swing.JTextField();
        txtDesc = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbType = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cbCategory = new javax.swing.JComboBox<>();
        btnSave = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTr = new javax.swing.JTable();
        cbFilter = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        labelUsername.setText("username");
        labelUsername.setToolTipText("");

        labelSaldo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelSaldo.setText("0");

        btnLogout.setText("LOGOUT");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        jLabel1.setText("WELCOME, ");

        jLabel2.setText("TOTAL SALDO (Rp) : ");

        jLabel3.setText("Date");

        jLabel4.setText("Amount (Rp)");

        jLabel5.setText("Description");

        txtAmount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAmountMouseClicked(evt);
            }
        });
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAmountKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAmountKeyTyped(evt);
            }
        });

        txtDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDescKeyReleased(evt);
            }
        });

        jLabel6.setText("Type");

        cbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "income", "spending" }));
        cbType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbTypeItemStateChanged(evt);
            }
        });
        cbType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbTypeKeyReleased(evt);
            }
        });

        jLabel7.setText("Category");

        cbCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCategoryActionPerformed(evt);
            }
        });

        btnSave.setText("SAVE");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnReset.setText("RESET");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        tblTr.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTrMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTr);

        cbFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Income", "Spending" }));
        cbFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbFilterItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelUsername)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLogout))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelSaldo))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbCategory, 0, 175, Short.MAX_VALUE)
                                    .addComponent(cbType, 0, 175, Short.MAX_VALUE)
                                    .addComponent(txtDate, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                    .addComponent(txtAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                    .addComponent(txtDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogout)
                    .addComponent(labelUsername)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSaldo)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDate, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cbType, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(cbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSave)
                            .addComponent(btnReset))
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete))
                    .addComponent(jSeparator3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        
        int option = JOptionPane.showConfirmDialog(null, "Are you sure?", 
                     "Logout", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            mainFrameValidation.isSuccess("logout");
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        
        String amount = txtAmount.getText();
        
        if( txtDate.getDate() == null || txtDesc.getText().isEmpty() || 
            amount.isEmpty()){
            mainFrameValidation.isNull("date, amount and description");
            return;
        }else if(Integer.valueOf(amount) == 0){
            mainFrameValidation.isZero("amount");
            return;
        }

        create();
        
        clear();
        getAll();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void cbCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCategoryActionPerformed

    private void cbTypeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbTypeKeyReleased
        
    }//GEN-LAST:event_cbTypeKeyReleased

    private void cbTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTypeItemStateChanged
        cbCategory.removeAllItems();
        setCategoryName();
    }//GEN-LAST:event_cbTypeItemStateChanged

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        getAll();
    }//GEN-LAST:event_formWindowOpened

    private void cbFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbFilterItemStateChanged
        filterTable();
    }//GEN-LAST:event_cbFilterItemStateChanged

    private void tblTrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTrMouseClicked
        try {
            int selectedId = new Integer(helper.getValueRows(tblTr, 0));
            String amount = helper.getValueRows(tblTr, 1);
            String desc = helper.getValueRows(tblTr, 2);
            String stringDate = helper.getValueRows(tblTr, 3);
            String type = helper.getValueRows(tblTr, 4);
            String category_name = helper.getValueRows(tblTr, 5);
            
            btnDelete.setEnabled(true);
            btnReset.setEnabled(true);
            cbType.setEnabled(false);
            
            Date date = new Date(helper.stringToDate(stringDate));
            
            this.id = selectedId;
            this.tempAmount = new Integer(amount);
            
            txtDate.setDate(date);
            txtAmount.setText(amount);
            txtDesc.setText(desc);
            
            if(type.equals("income")){
                cbType.setSelectedIndex(0);
            }else{
                cbType.setSelectedIndex(1);
            }
            
            cbCategory.setSelectedItem(category_name);
            
        } catch (NumberFormatException | ParseException e) {
            mainFrameValidation.isError(e.getMessage());
        }
    }//GEN-LAST:event_tblTrMouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        try {
            int option = JOptionPane.showConfirmDialog(null, " Data id : " +
            this.id + " will be deleted! Are you sure?", "Delete", 
            JOptionPane.YES_NO_OPTION);
            
            if(option == JOptionPane.YES_OPTION){
                
                String type = cbType.getSelectedItem().toString();
                
                if(type.equals("income")){
                    this.saldo -= this.tempAmount;
                }else{
                    this.saldo += this.tempAmount;
                } 
                
                setSaldo();
                setLabel();
                
                pst = conn.prepareStatement(transactionQuery.delete);
                pst.setInt(1, this.id);
                int result =  pst.executeUpdate();

                String msg = "delete data";

                if(result == 1){
                    mainFrameValidation.isSuccess(msg);
                    clear();
                    getAll();
                }else{
                    mainFrameValidation.isFailed(msg);
                }      
            }
             
        } catch (HeadlessException | SQLException e) {
            mainFrameValidation.isError(e.getMessage());
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAmountMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmountMouseClicked

    private void txtAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyTyped
       
    }//GEN-LAST:event_txtAmountKeyTyped

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        clear();
    }//GEN-LAST:event_btnResetActionPerformed

    private void txtAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyReleased
        int x;
        
        try {
        x = Integer.parseInt(txtAmount.getText());
        } catch (NumberFormatException nfe) {
            txtAmount.setText("");
            mainFrameValidation.isString("amount");
        }
        
    }//GEN-LAST:event_txtAmountKeyReleased

    private void txtDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescKeyReleased
        String desc = txtDesc.getText();
        
        if(desc.length() > 20){
            txtDesc.setText("");
            mainFrameValidation.isTooLong("description");
        }
    }//GEN-LAST:event_txtDescKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbCategory;
    private javax.swing.JComboBox<String> cbFilter;
    private javax.swing.JComboBox<String> cbType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel labelSaldo;
    private javax.swing.JLabel labelUsername;
    private javax.swing.JTable tblTr;
    private javax.swing.JTextField txtAmount;
    private com.toedter.calendar.JDateChooser txtDate;
    private javax.swing.JTextField txtDesc;
    // End of variables declaration//GEN-END:variables
}
