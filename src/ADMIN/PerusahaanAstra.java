package ADMIN;

import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class PerusahaanAstra extends JFrame {
    JPanel panelPerusahaan;
    private JPanel JPPerusahaan;
    private JTextField txtNamaPerusahaan;
    private JTextField txtID;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTextField txtSearch;
    private JTable tblPerusahaanAstra;

    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();

    public PerusahaanAstra(){

        connection = new DBConnect();

        tableModel = new DefaultTableModel();
        tblPerusahaanAstra.setModel(tableModel);

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        addColumn();
        loadData(null);
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String namaPerusahaan = txtNamaPerusahaan.getText();

                    if (namaPerusahaan.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return; // Menghentikan proses penyimpanan data jika validasi tidak terpenuhi
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_CreatePerusahaan(?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, namaPerusahaan);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(false);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data perusahaan dosen berhasil disimpan!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Penyimpanan data dibatalkan.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam penyimpanan data perusahaan!");
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = txtID.getText();
                    String namaPerusahaan = txtNamaPerusahaan.getText();

                    if (namaPerusahaan.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return; // Menghentikan proses penyimpanan data jika validasi tidak terpenuhi
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin memperbarui data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {

                        String procedureCall = "{CALL sp_UpdatePerusahaan(?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, namaPerusahaan);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data perusahaan berhasil diperbarui!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Pembaruan data dibatalkan.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam penyimpanan data perusahaan!");
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id;
                    id = txtID.getText();

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Prepare the stored procedure call
                        String procedureCall = "{CALL dbo.sp_DeletePerusahaan(?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id);

                        // Execute the stored procedure
                        connection.pstat.execute();
                        // Close the statement and connection
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(false);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

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
        txtNamaPerusahaan.addKeyListener(new KeyAdapter() {
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
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                loadData(txtSearch.getText());
            }
        });
    }
    public void loadData(String nama_perusahaan){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try{
            String functionCall = "SELECT * FROM dbo.getListPerusahaan(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, nama_perusahaan);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[2]; // Menyesuaikan jumlah kolom dengan tabel tblMatkul
                obj[0] = connection.result.getString("id_perusahaan");
                obj[1] = connection.result.getString("nama_perusahaan");

                tableModel.addRow(obj);
            }

            connection.pstat.close();
            connection.result.close();
        }catch (Exception e){
            System.out.println("Terjadi error saat load data Perusahaan :" + e);
        }
    }
    public void clear() {
        txtID.setText("Otomatis");
        txtNamaPerusahaan.setText("");
    }
    public void addColumn(){
        tableModel.addColumn("ID Perusahaan");
        tableModel.addColumn("Nama Perusahaan");
    }
}
