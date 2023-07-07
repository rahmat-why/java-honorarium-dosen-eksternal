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
    String id_golongan, nama_golongan;
    float insentif_kehadiran;
    int tahun_batas_bawah, tahun_batas_atas;

    public GolonganDosen(){
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
                    nama_golongan = txtNamaGolongan.getText();
                    insentif_kehadiran = Float.parseFloat(txtInsentifKehadiran.getText());
                    tahun_batas_bawah = Integer.parseInt(txtTahunBatasBawah.getText());
                    tahun_batas_atas = Integer.parseInt(txtTahunBatasAtas.getText());

                    if (nama_golongan.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_CreateGolongan(?, ?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, nama_golongan);
                        connection.pstat.setFloat(2, insentif_kehadiran);
                        connection.pstat.setInt(3, tahun_batas_bawah);
                        connection.pstat.setInt(4, tahun_batas_atas);

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
                } catch (SQLException exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Form harus lengkap dan sesuai format!");
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
                txtInsentifKehadiran.setText(String.valueOf((int) tableModel.getValueAt(i, 2)));
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
                    id_golongan = txtID.getText();

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_DeleteGolongan(?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_golongan);

                        connection.pstat.execute();
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
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    id_golongan = txtID.getText();
                    nama_golongan = txtNamaGolongan.getText();
                    insentif_kehadiran = Float.parseFloat(txtInsentifKehadiran.getText());
                    tahun_batas_bawah = Integer.parseInt(txtTahunBatasBawah.getText());
                    tahun_batas_atas = Integer.parseInt(txtTahunBatasAtas.getText());

                    if (nama_golongan.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin memperbarui data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {

                        String procedureCall = "{CALL sp_UpdateGolongan(?, ?, ?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_golongan);
                        connection.pstat.setString(2, nama_golongan);
                        connection.pstat.setFloat(3, insentif_kehadiran);
                        connection.pstat.setInt(4, tahun_batas_bawah);
                        connection.pstat.setInt(5, tahun_batas_atas);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data Golongan berhasil diperbarui!");
                    }
                } catch (SQLException exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Form harus lengkap dan sesuai format!");
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
        }catch (Exception exc){
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
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
