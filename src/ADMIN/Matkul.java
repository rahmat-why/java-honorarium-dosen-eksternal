package ADMIN;

import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class Matkul  extends JFrame {
    private JPanel JPMatkul;
    private JTextField txtMatkul;
    private JTextField txtSKS;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTextField txtSearch;
    private JTable tblMatkul;
    private JTextField txtID;

    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();

    public Matkul() {

        connection = new DBConnect();
        setSize(500, 500);
        setTitle("Form Mata Kuliah");
        setContentPane(JPMatkul);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel();
        tblMatkul.setModel(tableModel);

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        addColumn();
        loadData();
        //generateId();
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String Nama_Matkul = txtMatkul.getText();
                    String SKS_text = txtSKS.getText();

                    // Validasi data tidak boleh kosong
                    if (Nama_Matkul.isEmpty() || SKS_text.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return; // Menghentikan proses penyimpanan data jika validasi tidak terpenuhi
                    }

                    int SKS = Integer.parseInt(SKS_text);

                    String procedureCall = "{CALL dbo.sp_CreateMatkul(?, ?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, Nama_Matkul);
                    connection.pstat.setInt(2, SKS);

                    connection.pstat.execute();
                    connection.pstat.close();

                    loadData();
                    clear();

                    btnSave.setEnabled(false);
                    btnUpdate.setEnabled(false);
                    btnDelete.setEnabled(false);

                    JOptionPane.showMessageDialog(null, "Data Mata Kuliah berhasil disimpan!");

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam penyimpanan data Mata Kuliah!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "SKS harus berupa angka!");
                }
            }
        });

        tblMatkul.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblMatkul.getSelectedRow();
                if (i == -1) {
                    return;
                }
                txtID.setText((String) tableModel.getValueAt(i, 0));
                txtMatkul.setText((String) tableModel.getValueAt(i, 1));
                txtSKS.setText(String.valueOf((int) tableModel.getValueAt(i, 2)));

                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
            }
        });
        txtMatkul.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && (c != KeyEvent.VK_BACK_SPACE)
                        && (c != KeyEvent.VK_SPACE) && (c != KeyEvent.VK_PERIOD)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Nama hanya boleh diisi dengan huruf!");
                }
            }
        });
        txtSKS.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "SKS hanya boleh diisi dengan angka!");
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id;
                    id = txtID.getText();
                    // Prepare the stored procedure call
                    String procedureCall = "{CALL dbo.sp_DeleteMatkul(?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, id);

                    // Execute the stored procedure
                    connection.pstat.execute();
                    // Close the statement and connection
                    connection.pstat.close();

                    loadData();
                    clear();

                    btnSave.setEnabled(false);
                    btnUpdate.setEnabled(false);
                    btnDelete.setEnabled(false);

                    JOptionPane.showMessageDialog(null, "Success delete!");
                } catch (Exception exc) {
                    System.out.println("Error: " + exc.toString());
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = txtID.getText();
                    String nama_matkul = txtMatkul.getText();
                    String sks = txtSKS.getText();

                    if (nama_matkul.isEmpty() || sks.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return; // Stop the data updating process if any field is empty
                    }

                    String procedureCall = "{CALL sp_UpdateMatkul(?, ?, ?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, id);
                    connection.pstat.setString(2, nama_matkul);
                    connection.pstat.setString(3, sks);

                    connection.pstat.execute();
                    connection.pstat.close();

                    loadData();
                    clear();

                    btnSave.setEnabled(true);
                    btnUpdate.setEnabled(false);
                    btnDelete.setEnabled(false);

                    JOptionPane.showMessageDialog(null, "Data Mata Kuliah berhasil diUpdate!");

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam penyimpanan data Mata Kuliah!");
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
    }
    public void clear() {
        txtID.setText("Otomatis");
        txtMatkul.setText("");
        txtSKS.setText("");
    }

    public void loadData() {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String query = "select * from matkul";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                Object[] obj = new Object[3]; // Menyesuaikan jumlah kolom dengan tabel tblPenyewa
                obj[0] = connection.result.getString("id_matkul");
                obj[1] = connection.result.getString("nama_matkul");
                obj[2] = connection.result.getInt("sks");

                tableModel.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }
    public void addColumn(){
        tableModel.addColumn("ID Matkul");
        tableModel.addColumn("Nama Matkul");
        tableModel.addColumn("sks");
    }
    public void generateId() {
        try {
            String query = "SELECT dbo. GenerateMatkulID() AS newId";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            // perbarui data
            while (connection.result.next()) {
                txtID.setText(connection.result.getString("newId"));
            }

            // Close the statement and connection
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }
    public static void main(String[]args){
        new Matkul().setVisible(true);
    }
}
