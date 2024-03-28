/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package telas;

import java.sql.*;
import dal.ModuloConexao;
import javax.swing.JOptionPane;

public class telaCadastro extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public telaCadastro() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    private String formatarCPF(String cpf) {
    // Remove caracteres não numéricos
    cpf = cpf.replaceAll("[^0-9]", "");
    // Insere os pontos e o traço
    return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }
 
    public class CPFValidator {
        public static boolean validarCPF(String cpf_str) {
            // Verificar se o CPF tem 11 dígitos
            if (cpf_str.length() != 11) {
                JOptionPane.showMessageDialog(null, "CPF deve ter 11 dígitos. Tente novamente.");
                return false;
            }
 
            char[] cpf_char = cpf_str.toCharArray();
            int[] cpf_int = new int[cpf_char.length];
 
            for (int i = 0; i < cpf_char.length; i++) {
                cpf_int[i] = Character.getNumericValue(cpf_char[i]);
            }
 
            // Calcular o primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += cpf_int[i] * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito > 9) {
                primeiroDigito = 0;
            }
 
            // Calcular o segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += cpf_int[i] * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito > 9) {
                segundoDigito = 0;
            }
 
            // Verificar se os dígitos verificadores estão corretos
            if (cpf_int[9] == primeiroDigito && cpf_int[10] == segundoDigito) {
                JOptionPane.showMessageDialog(null, "CPF válido.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "CPF inválido. Tente novamente.");
                return false;
            }
        }
    }
    
    private void adicionar(){
        
    // Validação do CPF
    String cpf = inputCpf.getText();
    boolean cpfValido = CPFValidator.validarCPF(cpf);
    if (!cpfValido) {
        JOptionPane.showMessageDialog(null, "CPF inválido. Por favor, insira um CPF válido.");
        return;
    }
    
        String sql= "INSERT INTO usuarios(nome, ddd, telefone, email, mes_nascimento, cpf, endereco, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
               pst.setString(1, inputNome.getText());
               pst.setString(2, inputDDD.getText());
               pst.setString(3, inputTelefone.getText());
               pst.setString(4, inputEmail.getText());
               pst.setString(5, inputMes.getText());
               pst.setString(6, inputCpf.getText());
               pst.setString(7, inputEndereco.getText());
               pst.setString(8, inputCidade.getText());
               pst.setString(9, inputEstado.getText());
               
               int adicionado = pst.executeUpdate();
               
               if (adicionado > 0){
               JOptionPane.showMessageDialog(null,"USUARIO CADASTRADO!");
               inputNome.setText(null);
               inputDDD.setText(null);
               inputTelefone.setText(null);
               inputEmail.setText(null);
               inputMes.setText(null);
               inputCpf.setText(null);
               inputEndereco.setText(null);
               inputCidade.setText(null);
               inputEstado.setText(null);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void consultar(){
        String sql = "SELECT * FROM usuarios WHERE id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, inputId.getText());
            rs = pst.executeQuery();
            if (rs.next()){
                inputNome.setText(rs.getString(2));
                inputDDD.setText(rs.getString(3));
                inputTelefone.setText(rs.getString(4));
                inputEmail.setText(rs.getString(5));
                inputMes.setText(rs.getString(6));
                inputCpf.setText(rs.getString(7));
                inputEndereco.setText(rs.getString(8));
                inputCidade.setText(rs.getString(9));
                inputEstado.setText(rs.getString(10));
            }else {
                JOptionPane.showMessageDialog(null,"Esse usuario não existe.");
                inputNome.setText(null);
                inputDDD.setText(null);
                inputTelefone.setText(null);
                inputEmail.setText(null);
                inputMes.setText(null);
                inputCpf.setText(null);
                inputEndereco.setText(null);
                inputCidade.setText(null);
                inputEstado.setText(null);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void apagar(){
        //a estrutura abaixo confirma a exclusão do usuario da tabela
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir este usuario?", "ATENÇÃO", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
            String sql = "DELETE FROM usuarios WHERE id=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, inputId.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0){
                    JOptionPane.showMessageDialog(null, "Usuário Apagado!");
                     inputNome.setText(null);
                    inputDDD.setText(null);
                    inputTelefone.setText(null);
                    inputEmail.setText(null);
                    inputMes.setText(null);
                    inputCpf.setText(null);
                    inputEndereco.setText(null);
                    inputCidade.setText(null);
                    inputEstado.setText(null);
                    }
                }   
            catch (Exception e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
        private void editar() {
        String sql = "UPDATE usuarios SET nome=?, ddd=?, telefone=?, email=?, mes_nascimento=?, cpf=?, endereco=?, cidade=?, estado=? WHERE id=?";
        
        // Validação do CPF
        String cpf = inputCpf.getText();
        boolean cpfValido = CPFValidator.validarCPF(cpf);
        if (!cpfValido) {
            JOptionPane.showMessageDialog(null, "CPF inválido. Por favor, insira um CPF válido.");
            return;
        }
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, inputNome.getText());
            pst.setString(2, inputDDD.getText());
            pst.setString(3, inputTelefone.getText());
            pst.setString(4, inputEmail.getText());
            pst.setString(5, inputMes.getText());
            pst.setString(6, inputCpf.getText());
            pst.setString(7, inputEndereco.getText());
            pst.setString(8, inputCidade.getText());
            pst.setString(9, inputEstado.getText());
            pst.setString(10, inputId.getText());
            
            int adicionado = pst.executeUpdate();
            //retorna 1 se ok
            if(adicionado > 0){
                JOptionPane.showMessageDialog(null, "USUARIO ALTERADO COM SUCESSO");
                //Limpar os campos de formulário
                inputNome.setText(null);
                inputDDD.setText(null);
                inputTelefone.setText(null);
                inputEmail.setText(null);
                inputMes.setText(null);
                inputCpf.setText(null);
                inputEndereco.setText(null);
                inputCidade.setText(null);
                inputEstado.setText(null);
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
        
    public void limpar(){
        try {
            inputNome.setText("");
            inputDDD.setText("");
            inputTelefone.setText("");
            inputEmail.setText("");
            inputMes.setText("");
            inputCpf.setText("");
            inputEndereco.setText("");
            inputCidade.setText("");
            inputEstado.setText("");
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }    
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        inputId = new javax.swing.JTextField();
        inputNome = new javax.swing.JTextField();
        inputDDD = new javax.swing.JTextField();
        inputTelefone = new javax.swing.JTextField();
        inputEmail = new javax.swing.JTextField();
        inputMes = new javax.swing.JTextField();
        inputCpf = new javax.swing.JTextField();
        inputEndereco = new javax.swing.JTextField();
        inputCidade = new javax.swing.JTextField();
        inputEstado = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnAtualizar = new javax.swing.JButton();
        btnDeletar = new javax.swing.JButton();
        btnAdicionar = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        limpar = new javax.swing.JButton();

        jLabel11.setText("jLabel11");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Tela de Cadastro");
        setAutoscrolls(true);
        setFocusable(false);
        setMaximumSize(new java.awt.Dimension(500, 650));
        setMinimumSize(new java.awt.Dimension(500, 300));
        setNormalBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(500, 650));
        setRequestFocusEnabled(false);
        setVisible(true);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 800));

        jDesktopPane1.setFocusable(false);
        jDesktopPane1.setMaximumSize(new java.awt.Dimension(490, 640));
        jDesktopPane1.setMinimumSize(new java.awt.Dimension(490, 300));
        jDesktopPane1.setName(""); // NOI18N
        jDesktopPane1.setOpaque(false);
        jDesktopPane1.setPreferredSize(new java.awt.Dimension(490, 640));
        jDesktopPane1.setSelectedFrame(this);

        jLabel1.setText("ID");

        jLabel2.setText("Nome");

        jLabel3.setText("DDD");

        jLabel4.setText("Telefone");

        jLabel5.setText("Email");

        jLabel6.setText("Mês Nasc.");

        jLabel7.setText("CPF");

        jLabel8.setText("Endereço");

        jLabel9.setText("Cidade");

        jLabel10.setText("Estado");

        btnAtualizar.setText("Atualizar");
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

        btnDeletar.setText("Deletar");
        btnDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletarActionPerformed(evt);
            }
        });

        btnAdicionar.setText("Adicionar");
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnConsultar.setText("Consultar");
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        limpar.setText("Limpar");
        limpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparActionPerformed(evt);
            }
        });

        jDesktopPane1.setLayer(inputId, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(inputNome, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(inputDDD, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(inputTelefone, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(inputEmail, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(inputMes, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(inputCpf, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(inputEndereco, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(inputCidade, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(inputEstado, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel10, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(btnAtualizar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(btnDeletar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(btnAdicionar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(btnConsultar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(limpar, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputMes, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(inputCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inputNome, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                            .addComponent(inputEndereco)
                            .addComponent(inputEstado)
                            .addComponent(inputEmail)
                            .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9)
                                .addComponent(jLabel3)
                                .addComponent(inputId, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2)
                                .addComponent(jLabel10)
                                .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                    .addComponent(inputDDD, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(inputTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)))
                                .addComponent(jLabel6)
                                .addComponent(jLabel5)
                                .addComponent(inputCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                    .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnAdicionar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnAtualizar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                            .addComponent(btnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(limpar))))))
                        .addGap(0, 119, Short.MAX_VALUE))))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputDDD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdicionar)
                    .addComponent(btnConsultar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAtualizar)
                    .addComponent(btnDeletar)
                    .addComponent(limpar))
                .addContainerGap(84, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jDesktopPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        adicionar();
        
        inputId.setVisible(false);
        jLabel1.setVisible(false);
        
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarActionPerformed
        apagar();
        
        inputId.setVisible(true);
        jLabel1.setVisible(true);    
    }//GEN-LAST:event_btnDeletarActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        consultar();
        
        inputId.setVisible(true);
        jLabel1.setVisible(true);
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        editar();
        
        inputId.setVisible(true);
        jLabel1.setVisible(true);
    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void limparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparActionPerformed
       limpar();
    }//GEN-LAST:event_limparActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnDeletar;
    private javax.swing.JTextField inputCidade;
    private javax.swing.JTextField inputCpf;
    private javax.swing.JTextField inputDDD;
    private javax.swing.JTextField inputEmail;
    private javax.swing.JTextField inputEndereco;
    private javax.swing.JTextField inputEstado;
    private javax.swing.JTextField inputId;
    private javax.swing.JTextField inputMes;
    private javax.swing.JTextField inputNome;
    private javax.swing.JTextField inputTelefone;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton limpar;
    // End of variables declaration//GEN-END:variables
}
