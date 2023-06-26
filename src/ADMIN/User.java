package ADMIN;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class User extends JFrame {
    private JPanel JPUser;
    private JTextField txtEmpName;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTextField txtSearch;
    private JTable tableEmployee;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox cmbRole;
    private JTextField txtIDUser;
    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();
    public User() {

        connection = new DBConnect();
        setSize(500, 500);
        setTitle("Form User");
        setContentPane(JPUser);
        setLocationRelativeTo(null);

        tampilRole();

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        tableModel = new DefaultTableModel();
        tableEmployee.setModel(tableModel);

        addColumn();
        loadData(null);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String Nama = txtEmpName.getText();
                    String username = txtUsername.getText();
                    String password = txtPassword.getText();
                    ComboboxOption selectedOption = (ComboboxOption) cmbRole.getSelectedItem();
                    String role = selectedOption.getValue().toString();

                    if (Nama.isEmpty() || username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap isi semua data!");
                        return; // Stop the data saving process if any field is empty
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {

                        String procedureCall = "{CALL dbo.sp_CreateUser(?, ?, ?,?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, Nama);
                        connection.pstat.setString(2, username);
                        connection.pstat.setString(3, password);
                        connection.pstat.setString(4, role);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data User berhasil disimpan!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Penyimpanan data dibatalkan.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam penyimpanan data User!");
                }
            }
        });

        tableEmployee.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tableEmployee.getSelectedRow();
                if (i == -1) {
                    return;
                }
                txtIDUser.setText((String) tableModel.getValueAt(i, 0));
                txtEmpName.setText((String) tableModel.getValueAt(i, 1));
                txtUsername.setText((String) tableModel.getValueAt(i, 2));
                txtPassword.setText((String) tableModel.getValueAt(i, 3));

                String ROLE = (String) tableModel.getValueAt(i, 3);
                for (int x = 0; x < cmbRole.getItemCount(); x++) {
                    Object item = cmbRole.getItemAt(x);
                    String jenisCb = ((ComboboxOption) item).getValue();
                    System.out.println("ROLE = " + ROLE + " jenisCb = " + jenisCb);
                    if (jenisCb.equals(ROLE)) {
                        cmbRole.setSelectedItem(item);
                        break;
                    }
                    btnSave.setEnabled(false);
                    btnUpdate.setEnabled(true);
                    btnDelete.setEnabled(true);
                }
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();

                btnSave.setEnabled(true);
                btnUpdate.setEnabled(false);
                btnDelete.setEnabled(false);
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = txtIDUser.getText();
                    String nama = txtEmpName.getText();
                    String usrName = txtUsername.getText();
                    String password = txtPassword.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cmbRole.getSelectedItem();
                    String Role = selectedOption.getValue().toString();

                    if (nama.isEmpty() || usrName.isEmpty() || password.isEmpty() || Role.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return; // Stop the data updating process if any field is empty
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin mengupdate data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL sp_UpdateUser(?, ?, ?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, nama);
                        connection.pstat.setString(3, usrName);
                        connection.pstat.setString(4, password);
                        connection.pstat.setString(5, Role);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnDelete.setEnabled(false);
                        btnUpdate.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data User berhasil diupdate!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Pembaruan data dibatalkan.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam pembaruan data User!");
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = txtIDUser.getText();

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Prepare the stored procedure call
                        String procedureCall = "{CALL dbo.sp_DeleteUser(?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id);

                        // Execute the stored procedure
                        connection.pstat.execute();

                        // Close the statement and connection
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnDelete.setEnabled(false);
                        btnUpdate.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Penghapusan data dibatalkan.");
                    }
                } catch (Exception exc) {
                    System.out.println("Error: " + exc.toString());

                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
                }
            }
        });
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                // Get the entered text
                String currentUsername = txtUsername.getText();

                // Check if the entered text contains a space character
                char c = e.getKeyChar();
                if (c == ' ') {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Username tidak boleh menggunakan spasi!");
                }
            }
        });

        txtEmpName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                // Get the entered text
                String currentName = txtEmpName.getText();

                // Check if the entered text contains any non-letter characters except space
                char c = e.getKeyChar();
                if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == KeyEvent.VK_SPACE)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Nama hanya boleh diisi dengan huruf dan spasi!");
                }
            }
        });

        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                // Get the entered text
                String currentUsername = txtUsername.getText();

                // Check if the entered text contains a space character
                char c = e.getKeyChar();
                if (c == ' ') {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Username tidak boleh menggunakan spasi!");
                }
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                loadData(txtSearch.getText());
            }
        });
    }


    public void addColumn(){
        tableModel.addColumn("ID User");
        tableModel.addColumn("Nama");
        tableModel.addColumn("Username");
        tableModel.addColumn("Password");
        tableModel.addColumn("Role");
    }
    public void clear() {
        txtIDUser.setText("Otomatis");
        txtEmpName.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
    }
    public void loadData(String nama) {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String functionCall = "SELECT * FROM dbo.getListUsers(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, nama);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[5]; // Menyesuaikan jumlah kolom dengan tabel tblPenyewa
                obj[0] = connection.result.getString("id_user");
                obj[1] = connection.result.getString("nama");
                obj[2] = connection.result.getString("username");
                obj[3] = "*****";
                obj[4] = connection.result.getString("role");

                tableModel.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }
    public void tampilRole() {
        cmbRole.addItem(new ComboboxOption("DAAA", "DAAA"));
    }

    public static void main(String[]args){
        new User().setVisible(true);
    }

}
