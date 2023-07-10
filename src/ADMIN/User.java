package ADMIN;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class User extends JFrame {
    private JTextField txtEmpName;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTextField txtSearch;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox cmbRole;
    JPanel panelUser;
    private JTextField txtID;
    private JTable tblEmployee;
    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();
    String id_user, nama, username, password, role;
    public User() {
        tampilRole();
        tableModel = new DefaultTableModel();
        tblEmployee.setModel(tableModel);

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        addColumn();
        loadData(null);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    nama = txtEmpName.getText();
                    username = txtUsername.getText();
                    password = txtPassword.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cmbRole.getSelectedItem();
                    role = selectedOption.getValue().toString();

                    if (nama.isEmpty() || username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap isi semua data!");
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_CreateUser(?, ?, ?,?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, nama);
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
                    }
                } catch (SQLException exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                }
            }
        });

        tblEmployee.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            int i = tblEmployee.getSelectedRow();
            if (i == -1) {
                return;
            }

            txtID.setText((String) tableModel.getValueAt(i, 0));
            txtEmpName.setText((String) tableModel.getValueAt(i, 1));
            txtUsername.setText((String) tableModel.getValueAt(i, 2));
            txtPassword.setText("");

            String id_role = (String) tableModel.getValueAt(i, 0);
            for (int x = 0; x < cmbRole.getItemCount(); x++) {
                Object item = cmbRole.getItemAt(x);
                String cb_id_role = ((ComboboxOption) item).getValue();

                if (cb_id_role.equals(id_role)) {
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
                    id_user = txtID.getText();
                    nama = txtEmpName.getText();
                    username = txtUsername.getText();
                    password = txtPassword.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cmbRole.getSelectedItem();
                    role = selectedOption.getValue().toString();

                    if (nama.isEmpty() || username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin mengupdate data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL sp_UpdateUser(?, ?, ?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_user);
                        connection.pstat.setString(2, nama);
                        connection.pstat.setString(3, username);
                        connection.pstat.setString(4, password);
                        connection.pstat.setString(5, role);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnDelete.setEnabled(false);
                        btnUpdate.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data User berhasil diupdate!");
                    }
                } catch (SQLException exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    id_user = txtID.getText();

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_DeleteUser(?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_user);

                        connection.pstat.execute();

                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnDelete.setEnabled(false);
                        btnUpdate.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                }
            }
        });

        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
            super.keyTyped(e);

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
            // Check if the entered text contains any non-letter characters except space
            char c = e.getKeyChar();
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == KeyEvent.VK_SPACE || c == KeyEvent.VK_BACK_SPACE)) {
                e.consume();
                JOptionPane.showMessageDialog(null, "Nama hanya boleh diisi dengan huruf dan spasi!");
            }
            }
        });

        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
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
        txtID.setText("Otomatis");
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
                Object[] obj = new Object[5];
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
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
    }
    public void tampilRole() {
        cmbRole.addItem(new ComboboxOption("DAAA", "DAAA"));
    }
}
