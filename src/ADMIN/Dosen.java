package ADMIN;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.sql.Date;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Dosen extends JFrame{
    public JPanel panelDosen;
    private JPanel JPDosen;
    private JTextField txtNamaDosen;
    private JTextField txtEmail;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTextField txtSearch;
    private JTable tblDosen;
    private JComboBox cbIDJenis;
    private JTextField txtNamaBank;
    private JTextField txtCabangBank;
    private JTextField txtNoRekening;
    private JTextField txtNPWP;
    private JTextField txtAsalPerusahaan;
    private JTextField txtAtasNama;
    private JTextField txtKota;
    private JComboBox cbStatus;
    private JTextField txtIDDosen;
    private JPanel JPTanggalKampus;
    private JPanel JPTanggalIndustri;
    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();
public Dosen() {

    connection = new DBConnect();
    JDateChooser datechooser = new JDateChooser();
    JDateChooser dateChooser = new JDateChooser();
    setSize(500, 500);
    setTitle("Form Dosen");
    setContentPane(JPDosen);
    setLocationRelativeTo(null);

    JPTanggalKampus.add(datechooser);
    JPTanggalIndustri.add(dateChooser);

    btnSave.setEnabled(true);
    btnUpdate.setEnabled(false);
    btnDelete.setEnabled(false);

    //tampilDosen();
    tableModel = new DefaultTableModel();
    tblDosen.setModel(tableModel);

    cbStatus.addItem(new ComboboxOption("AKTIF", "AKTIF"));
    cbStatus.addItem(new ComboboxOption("TIDAK AKTIF", "TIDAK AKTIF"));


    addColumn();
    loadData(null);
    showJenisDosen();


    txtNamaDosen.addKeyListener(new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            char c = e.getKeyChar();
            if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && (c != KeyEvent.VK_BACK_SPACE)
                    && (c != KeyEvent.VK_SPACE) && (c != KeyEvent.VK_PERIOD)) {
                e.consume();
                JOptionPane.showMessageDialog(null, "Kolom ini hanya boleh diisi dengan huruf!","Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    txtNamaBank.addKeyListener(new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            char c = e.getKeyChar();
            if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && (c != KeyEvent.VK_BACK_SPACE)
                    && (c != KeyEvent.VK_SPACE) && (c != KeyEvent.VK_PERIOD)) {
                e.consume();
                JOptionPane.showMessageDialog(null, "Kolom ini hanya boleh diisi dengan huruf!","Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    txtCabangBank.addKeyListener(new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            char c = e.getKeyChar();
            if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && (c != KeyEvent.VK_BACK_SPACE)
                    && (c != KeyEvent.VK_SPACE) && (c != KeyEvent.VK_PERIOD)) {
                e.consume();
                JOptionPane.showMessageDialog(null, "Kolom ini hanya boleh diisi dengan huruf!","Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    txtNoRekening.addKeyListener(new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            char inputChar = e.getKeyChar();

            // Cek apakah karakter input adalah digit atau tombol backspace
            if (!Character.isDigit(inputChar) && inputChar != '\b') {
                e.consume();
                JOptionPane.showMessageDialog(null, "Kolom ini hanya boleh diisi dengan angka!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String currentInput = txtNoRekening.getText();

                // Validasi panjang input tidak melebihi 16 digit
                if (currentInput.length() >= 16 && inputChar != '\b') {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Input harus berupa 16 digit angka!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    });
    txtNPWP.addKeyListener(new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            char inputChar = e.getKeyChar();

            // Cek apakah karakter input adalah digit atau tombol backspace
            if (!Character.isDigit(inputChar) && inputChar != '\b') {
                e.consume();
                JOptionPane.showMessageDialog(null, "Kolom ini hanya boleh berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String currentInput = txtNPWP.getText();

                // Validasi panjang input tidak melebihi 16 digit
                if (currentInput.length() >= 16 && inputChar != '\b') {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Input harus berupa 16 digit angka!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    });
    txtAsalPerusahaan.addKeyListener(new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            char c = e.getKeyChar();
            if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && (c != KeyEvent.VK_BACK_SPACE)
                    && (c != KeyEvent.VK_SPACE) && (c != KeyEvent.VK_PERIOD)) {
                e.consume();
                JOptionPane.showMessageDialog(null, "Kolom ini hanya boleh diisi dengan huruf!","Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    txtAtasNama.addKeyListener(new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            char inputChar = e.getKeyChar();

            // Cek apakah karakter input adalah angka
            if (Character.isDigit(inputChar)) {
                e.consume();
                JOptionPane.showMessageDialog(null, "Kolom ini harus diisi dengan huruf!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String currentInput = txtAtasNama.getText();

                // Validasi hanya huruf dan maksimal 30 karakter
                if (!isValidLettersOnly(currentInput) || currentInput.length() > 30) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Input harus berupa huruf dan maksimal 30 karakter!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    });

    txtKota.addKeyListener(new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            char c = e.getKeyChar();
            if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && (c != KeyEvent.VK_BACK_SPACE)
                    && (c != KeyEvent.VK_SPACE) && (c != KeyEvent.VK_PERIOD)) {
                e.consume();
                JOptionPane.showMessageDialog(null, "Kolom ini hanya boleh diisi dengan huruf!","Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    btnSave.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String tanggalKampus = formatter.format(datechooser.getDate());
                Format formatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String tanggalIndustri = formatter1.format(dateChooser.getDate());

                String namaDosen = txtNamaDosen.getText();
                String idDosen = txtIDDosen.getText();
                String email = txtEmail.getText();

                ComboboxOption selectedOptionJenis = (ComboboxOption) cbIDJenis.getSelectedItem();
                String jnID = selectedOptionJenis.getValue();

                String namaBank = txtNamaBank.getText();
                String cabangBank = txtCabangBank.getText();
                String noRekening = txtNoRekening.getText();
                String npwp = txtNPWP.getText();
                String asalPerusahaan = txtAsalPerusahaan.getText();

                ComboboxOption selectedOption = (ComboboxOption) cbStatus.getSelectedItem();
                String status = selectedOption.getValue();

                String atasNama = txtAtasNama.getText();
                String kota = txtKota.getText();

                // Validasi data
                if (namaDosen.isEmpty() || idDosen.isEmpty() || email.isEmpty() || jnID.isEmpty() ||
                        namaBank.isEmpty() || cabangBank.isEmpty() || noRekening.isEmpty() ||
                        npwp.isEmpty() || asalPerusahaan.isEmpty() || status.isEmpty() ||
                        atasNama.isEmpty() || kota.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Harap lengkapi semua data sebelum menyimpan!", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Stop further execution
                }

                // Validasi format email
                if (!isValidEmailFormat(email)) {
                    JOptionPane.showMessageDialog(null, "Format email tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Stop further execution
                }

                int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String procedureCall = "{CALL sp_CreateDosen(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, namaDosen);
                    connection.pstat.setString(2, email);
                    connection.pstat.setString(3, jnID);
                    connection.pstat.setString(4, namaBank);
                    connection.pstat.setString(5, cabangBank);
                    connection.pstat.setString(6, noRekening);
                    connection.pstat.setString(7, npwp);
                    connection.pstat.setString(8, tanggalKampus);
                    connection.pstat.setString(9, tanggalIndustri);
                    connection.pstat.setString(10, asalPerusahaan);
                    connection.pstat.setString(11, status);
                    connection.pstat.setString(12, atasNama);
                    connection.pstat.setString(13, kota);
                    connection.pstat.execute();

                    connection.pstat.close();

                    btnSave.setEnabled(true);
                    btnUpdate.setEnabled(false);
                    btnDelete.setEnabled(false);

                    JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
                    loadData(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Penyimpanan data dibatalkan.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam penyimpanan data Dosen!");
            }
        }
    });
    tblDosen.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            int i = tblDosen.getSelectedRow();
            if (i == -1) {
                return;
            }
            txtIDDosen.setText((String) tableModel.getValueAt(i, 0));
            txtNamaDosen.setText((String) tableModel.getValueAt(i,1));
            txtEmail.setText((String) tableModel.getValueAt(i,2));
            String jnID = (String) tableModel.getValueAt(i, 3);
            for (int x = 0; x < cbIDJenis.getItemCount(); x++) {
                Object item =cbIDJenis.getItemAt(x);
                String jenisCb = ((ComboboxOption) item).getValue();
                System.out.println("jnID = "+jnID+" jenisCb = "+jenisCb);
                if (jenisCb.equals(jnID)) {
                    cbIDJenis.setSelectedItem(item);
                    break;
                }
            }
            txtNamaBank.setText((String) tableModel.getValueAt(i,4));
            txtCabangBank.setText((String) tableModel.getValueAt(i,5));
            txtNoRekening.setText((String) tableModel.getValueAt(i,6));
            txtNPWP.setText((String) tableModel.getValueAt(i,7));
            datechooser.setDate((Date) tableModel.getValueAt(i, 8));
            dateChooser.setDate((Date) tableModel.getValueAt(i, 9));
            txtAsalPerusahaan.setText((String) tableModel.getValueAt(i, 10));
            String status = (String) tableModel.getValueAt(i, 11);
            for (int x = 0; x < cbStatus.getItemCount(); x++) {
                Object item =cbStatus.getItemAt(x);
                String Status= ((ComboboxOption) item).getValue();
                System.out.println("status = "+status+" Status = "+Status);
                if (Status.equals(jnID)) {
                    cbStatus.setSelectedItem(item);
                    break;
                }
            }
            txtAtasNama.setText((String) tableModel.getValueAt(i, 12));
            txtKota.setText((String) tableModel.getValueAt(i, 13));


            btnSave.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
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
                Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String tanggalKampus = formatter.format(datechooser.getDate());
                Format formatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String tanggalIndustri = formatter1.format(dateChooser.getDate());

                String namaDosen = txtNamaDosen.getText();
                String idDosen = txtIDDosen.getText();
                String email = txtEmail.getText();

                if (!isValidEmailFormat(email)) {
                    JOptionPane.showMessageDialog(null, "Input harus menggunakan format email yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ComboboxOption selectedOptionJenis = (ComboboxOption) cbIDJenis.getSelectedItem();
                String jnID =selectedOptionJenis.getValue();

                String namaBank = txtNamaBank.getText();
                String cabangBank = txtCabangBank.getText();
                String noRekening = txtNoRekening.getText();
                String npwp = txtNPWP.getText();
                String asalPerusahaan = txtAsalPerusahaan.getText();

                ComboboxOption selectedOption = (ComboboxOption) cbStatus.getSelectedItem();
                String status = selectedOption.getValue();

                String atasNama = txtAtasNama.getText();
                String kota = txtKota.getText();

                if (namaDosen.isEmpty() || idDosen.isEmpty() || email.isEmpty() || jnID.isEmpty() ||
                        namaBank.isEmpty() || cabangBank.isEmpty() || noRekening.isEmpty() ||
                        npwp.isEmpty() || asalPerusahaan.isEmpty() || status.isEmpty() ||
                        atasNama.isEmpty() || kota.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Stop further execution
                }

                int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin memperbarui data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String procedureCall = "{CALL sp_UpdateDosen(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, idDosen);
                    connection.pstat.setString(2, namaDosen);
                    connection.pstat.setString(3, email);
                    connection.pstat.setString(4, jnID);
                    connection.pstat.setString(5, namaBank); // Atur nilai untuk parameter nomor 4 (pny_telp)
                    connection.pstat.setString(6, cabangBank);
                    connection.pstat.setString(7, noRekening);
                    connection.pstat.setString(8, npwp);
                    connection.pstat.setString(9, tanggalKampus);
                    connection.pstat.setString(10, tanggalIndustri);
                    connection.pstat.setString(11, asalPerusahaan);
                    connection.pstat.setString(12, status);
                    connection.pstat.setString(13, atasNama);
                    connection.pstat.setString(14, kota);

                    connection.pstat.executeUpdate();
                    connection.pstat.close();

                    loadData(null);
                    clear();

                    btnSave.setEnabled(true);
                    btnDelete.setEnabled(false);
                    btnUpdate.setEnabled(false);

                    JOptionPane.showMessageDialog(null, "Data dosen berhasil diperbarui!");
                } else {
                    JOptionPane.showMessageDialog(null, "Pembaruan data dibatalkan.");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam penyimpanan data dosen!");
            }
        }
    });
    btnDelete.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String id;
                id = txtIDDosen.getText();
                // Prepare the stored procedure call
                int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Prepare the stored procedure call
                    String procedureCall = "{CALL dbo.sp_DeleteDosen(?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, id);

                    // Execute the stored procedure
                    connection.pstat.execute();

                    // Close the statement and connection
                    connection.pstat.close();

                    loadData(null);
                    clear();

                    btnDelete.setEnabled(true);
                    btnSave.setEnabled(false);
                    btnUpdate.setEnabled(false);

                    JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                } else {
                    JOptionPane.showMessageDialog(null, "Penghapusan data dibatalkan.");
                }
            } catch (Exception exc) {
                System.out.println("Error: "+exc.toString());

                JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
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
    private boolean isValidEmailFormat(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
    private boolean isValidNumber(String input){
        return input.matches("\\d*");
    }
    private boolean isValidLettersOnly(String input) {
        return input.matches("[a-zA-Z\\s]*");
    }
    public void loadData(String nama_dosen) {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            String functionCall = "SELECT * FROM dbo.getListDosen(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, nama_dosen);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[14]; // Menyesuaikan jumlah kolom dengan tabel tblPenyewa
                obj[0] = connection.result.getString("id_dosen");
                obj[1] = connection.result.getString("nama_dosen");
                obj[2] = connection.result.getString("email");
                obj[3] = connection.result.getString("id_jenis_dosen");
                obj[4] = connection.result.getString("nama_bank");
                obj[5] = connection.result.getString("cabang_bank");
                obj[6] = connection.result.getString("no_rekening");
                obj[7] = connection.result.getString("npwp");
                obj[8] = connection.result.getDate("tanggal_gabung_kampus");
                obj[9] = connection.result.getDate("tanggal_gabung_industri");
                obj[10] = connection.result.getString("asal_perusahaan");
                obj[11] = connection.result.getString("status");
                obj[12] = connection.result.getString("atasnama");
                obj[13] = connection.result.getString("kota");

                tableModel.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }
    public void generateId() {
        try {
            String query = "SELECT dbo.GenerateDosenID() AS newId";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            // perbarui data
            while (connection.result.next()) {
                txtIDDosen.setText(connection.result.getString("newId"));
            }

            // Close the statement and connection
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }
    public void addColumn(){
        tableModel.addColumn("ID Dosen");
        tableModel.addColumn("Nama Dosen");
        tableModel.addColumn("Email");
        tableModel.addColumn("ID Jenis Dosen");
        tableModel.addColumn("Nama Bank");
        tableModel.addColumn("Cabang Bank");
        tableModel.addColumn("No Rekening");
        tableModel.addColumn("NPWP");
        tableModel.addColumn("Tanggal Gabung Kampus");
        tableModel.addColumn("Tanggal Gabung Industri");
        tableModel.addColumn("Asal Perusahaan");
        tableModel.addColumn("Status");
        tableModel.addColumn("Atas Nama");
        tableModel.addColumn("Kota");
    }
    public void clear() {
        txtIDDosen.setText("Otomatis");
        txtNamaDosen.setText("");
        txtEmail.setText("");
        txtNamaBank.setText("");
        txtNoRekening.setText("");
        txtCabangBank.setText("");
        txtNPWP.setText("");
        txtAsalPerusahaan.setText("");
        txtAtasNama.setText("");
        txtKota.setText("");
    }
    public void showJenisDosen() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT * FROM jenis_dosen";
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                String jnID = connection.result.getString("id_jenis_dosen");
                String namaJenis = connection.result.getString("nama_jenis");
                cbIDJenis.addItem(new ComboboxOption( jnID, namaJenis));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }
    public static void main(String[]args){
        new Dosen().setVisible(true);
    }
}
