package ADMIN;

import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class GolonganDosen extends JFrame {
    private JTextField txtNamaGolongan;
    private JTextField txtInsentifKehadiran;
    private JTextField txtTahunBatasBawah;
    private JTextField txtID;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTextField txtSearch;
    JPanel panelGolongan;
    private JTextField txtTahunBatasAtas;
    private JTable tblGolongan;
    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();

    public GolonganDosen(){

        connection = new DBConnect();

        tableModel = new DefaultTableModel();
        tblGolongan.setModel(tableModel);

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        addColumn();
        loadData(null);
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String namaGolongan = txtNamaGolongan.getText();
                    String insentifKehadiran = txtInsentifKehadiran.getText();
                    String tahunBatasBawah = txtTahunBatasBawah.getText();
                    String tahunBatasAtas = txtTahunBatasAtas.getText();

                    if (namaGolongan.isEmpty() || insentifKehadiran.isEmpty() || tahunBatasBawah.isEmpty() || tahunBatasAtas.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return; // Menghentikan proses penyimpanan data jika validasi tidak terpenuhi
                    }
                    int tahunBawah = Integer.parseInt(tahunBatasBawah);
                    int tahunAtas = Integer.parseInt(tahunBatasAtas);

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_CreateGolongan(?, ?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, namaGolongan);
                        connection.pstat.setString(2, insentifKehadiran);
                        connection.pstat.setInt(3, tahunBawah);
                        connection.pstat.setInt(4, tahunAtas);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(false);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data Golongan dosen berhasil disimpan!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Penyimpanan data dibatalkan.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam penyimpanan data Mata Kuliah!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "SKS harus berupa angka!");
                }
            }
        });
        tblGolongan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblGolongan.getSelectedRow();
                if (i == -1) {
                    return;
                }
                txtID.setText((String) tableModel.getValueAt(i, 0));
                txtNamaGolongan.setText((String) tableModel.getValueAt(i, 1));
                txtInsentifKehadiran.setText((String) tableModel.getValueAt(i, 2));
                txtTahunBatasBawah.setText(String.valueOf((int) tableModel.getValueAt(i, 3)));
                txtTahunBatasAtas.setText(String.valueOf((int) tableModel.getValueAt(i, 4)));

                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
            }
        });
        txtInsentifKehadiran.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtInsentifKehadiran.getText().length() >= 25 ) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Insentif Kehadiran hanya boleh diisi dengan angka!");
                }
            }
        });

        txtTahunBatasBawah.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtTahunBatasBawah.getText().length() >= 25 ) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Tahun batas bawah hanya boleh diisi dengan angka!");
                }
            }
        });
        txtTahunBatasAtas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtTahunBatasAtas.getText().length() >= 25 ) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Tahun batas atas hanya boleh diisi dengan angka!");
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
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id;
                    id = txtID.getText();

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Prepare the stored procedure call
                        String procedureCall = "{CALL dbo.sp_DeleteGolongan(?)}";
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
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = txtID.getText();
                    String nama = txtNamaGolongan.getText();
                    String insentifKehadiran = txtInsentifKehadiran.getText();
                    String tahunBatasBawah = txtTahunBatasBawah.getText();
                    String tahunBatasAtas = txtTahunBatasAtas.getText();

                    if (nama.isEmpty() || insentifKehadiran.isEmpty() || tahunBatasBawah.isEmpty() || tahunBatasAtas.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return; // Menghentikan proses penyimpanan data jika validasi tidak terpenuhi
                    }

                    int tahunBawah = Integer.parseInt(tahunBatasBawah);
                    int tahunAtas = Integer.parseInt(tahunBatasAtas);

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin memperbarui data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {

                        String procedureCall = "{CALL sp_UpdateGolongan(?, ?, ?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, nama);
                        connection.pstat.setString(3, insentifKehadiran);
                        connection.pstat.setInt(4, tahunBawah);
                        connection.pstat.setInt(5, tahunAtas);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data Golongan berhasil diperbarui!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Pembaruan data dibatalkan.");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam penyimpanan data Mata Kuliah!");
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
    public void loadData(String nama){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try{
            String functionCall = "SELECT * FROM dbo.getListGolongan(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, nama);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[5]; // Menyesuaikan jumlah kolom dengan tabel tblMatkul
                obj[0] = connection.result.getString("id_golongan");
                obj[1] = connection.result.getString("nama");
                obj[2] = connection.result.getInt("insentif_kehadiran");
                obj[3] = connection.result.getInt("tahun_batas_bawah");
                obj[4] = connection.result.getInt("tahun_batas_atas");

                tableModel.addRow(obj);
            }

            connection.pstat.close();
            connection.result.close();
        }catch (Exception e){
            System.out.println("Terjadi error saat load data Golongan :" + e);
        }
    }
    public void addColumn(){
        tableModel.addColumn("ID Golongan");
        tableModel.addColumn("Nama Golongan");
        tableModel.addColumn("Insentif Kehadiran");
        tableModel.addColumn("Tahun Batas Bawah");
        tableModel.addColumn("Tahun Batas Atas");
    }
    public void clear() {
        txtID.setText("Otomatis");
        txtNamaGolongan.setText("");
        txtInsentifKehadiran.setText("");
        txtTahunBatasBawah.setText("");
        txtTahunBatasAtas.setText("");
    }
}
