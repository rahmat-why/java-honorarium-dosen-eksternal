package ADMIN;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;
import LOGIN.Login;
import com.toedter.calendar.JDateChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;

public class popUp extends JFrame{
    private JPanel panelPop;
    private JButton btnBrowser;
    private JPanel Panel_Konten_Gambar;
    private JLabel Label_Gambar;
    private JTextField txtIDDosen;
    private JTextField txtNamaDosen;
    private JTextField txtEmail;
    private JComboBox cbIDJenis;
    private JTextField txtNamaBank;
    private JTextField txtCabangBank;
    private JTextField txtNoRekening;
    private JTextField txtNPWP;
    private JPanel JPTanggalKampus;
    private JPanel JPTanggalIndustri;
    private JComboBox cbIdPerusahaan;
    private JComboBox cbStatus;
    private JTextField txtAtasNama;
    private JTextField txtKota;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnBack;
    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();
    String selectedImagePath = "";
    private File selectedImageFile;
    byte[] imageBytes;
    Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    JDateChooser tanggalKampus = new JDateChooser();
    JDateChooser tanggalIndustri = new JDateChooser();
    String id_dosen, nama_dosen, email, id_jenis_dosen, nama_bank, cabang_bank, no_rekening, npwp, tanggal_gabung_kampus, tanggal_gabung_industri, status, atas_nama, kota, id_perusahaan;

//    String param_nama_bank, String param_cabang_bank, String param_no_rek, String param_npwp, String param_atas_nama,String param_kota
    public popUp(String param_id_dosen, String param_nama_dosen,String param_email,String param_nama_bank,String param_cabang_bank,String param_no_rek, String param_npwp, String param_atas_nama, String param_kota){
        setSize(200, 300);
        setTitle("FORM Dosen");
        setContentPane(panelPop);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        txtIDDosen.setText(param_id_dosen);
        txtNamaDosen.setText(param_nama_dosen);
        txtEmail.setText(param_email);
        txtNamaBank.setText(param_nama_bank);
        txtCabangBank.setText(param_cabang_bank);
        txtNoRekening.setText(param_no_rek);
        txtNPWP.setText(param_npwp);
        txtAtasNama.setText(param_atas_nama);
        txtKota.setText(param_kota);


        JPTanggalKampus.add(tanggalKampus);
        JPTanggalIndustri.add(tanggalIndustri);

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        //tampilDosen();

        cbStatus.addItem(new ComboboxOption("AKTIF", "AKTIF"));
        cbStatus.addItem(new ComboboxOption("TIDAK AKTIF", "TIDAK AKTIF"));


        showJenisDosen();
        showPerusahaan();
        txtNamaDosen.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && (c != KeyEvent.VK_BACK_SPACE)
                        && (c != KeyEvent.VK_SPACE) && (c != KeyEvent.VK_PERIOD)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Kolom ini hanya boleh diisi dengan huruf!", "Error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Kolom ini hanya boleh diisi dengan huruf!", "Error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Kolom ini hanya boleh diisi dengan huruf!", "Error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Kolom ini hanya boleh diisi dengan huruf!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tanggal_gabung_kampus = formatter.format(tanggalKampus.getDate());
                    tanggal_gabung_industri = formatter.format(tanggalIndustri.getDate());

                    nama_dosen = txtNamaDosen.getText();
                    id_dosen = txtIDDosen.getText();
                    email = txtEmail.getText();

                    ComboboxOption selectedOptionJenis = (ComboboxOption) cbIDJenis.getSelectedItem();
                    id_jenis_dosen = selectedOptionJenis.getValue();

                    nama_bank = txtNamaBank.getText();
                    cabang_bank = txtCabangBank.getText();
                    no_rekening = txtNoRekening.getText();
                    npwp = txtNPWP.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cbStatus.getSelectedItem();
                    status = selectedOption.getValue();

                    atas_nama = txtAtasNama.getText();
                    kota = txtKota.getText();

                    // Validasi data
                    if (nama_dosen.isEmpty() || email.isEmpty() || id_jenis_dosen.isEmpty() ||
                            nama_bank.isEmpty() || cabang_bank.isEmpty() || no_rekening.isEmpty() ||
                            status.isEmpty() ||
                            atas_nama.isEmpty() || kota.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data sebelum menyimpan!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!isValidEmailFormat(email)) {
                        JOptionPane.showMessageDialog(null, "Format email tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL sp_CreateDosen(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, nama_dosen);
                        connection.pstat.setString(2, email);
                        connection.pstat.setString(3, id_jenis_dosen);
                        connection.pstat.setString(4, nama_bank);
                        connection.pstat.setString(5, cabang_bank);
                        connection.pstat.setString(6, no_rekening);
                        connection.pstat.setString(7, npwp);
                        connection.pstat.setString(8, tanggal_gabung_kampus);
                        connection.pstat.setString(9, tanggal_gabung_industri);
                        connection.pstat.setString(10, status);
                        connection.pstat.setString(11, atas_nama);
                        connection.pstat.setString(12, kota);
                        connection.pstat.setString(13, id_perusahaan);
                        connection.pstat.setBytes(14, imageBytes);
                        connection.pstat.execute();

                        connection.pstat.close();

                        btnSave.setEnabled(true);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
                    }
                } catch (SQLException exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tanggal_gabung_kampus = formatter.format(tanggalKampus.getDate());
                    tanggal_gabung_industri = null;
                    if(tanggalIndustri.getDate() != null) {
                        tanggal_gabung_industri = formatter.format(tanggalIndustri.getDate());
                    }

                    nama_dosen = txtNamaDosen.getText();
                    id_dosen = txtIDDosen.getText();
                    email = txtEmail.getText();

                    if (!isValidEmailFormat(email)) {
                        JOptionPane.showMessageDialog(null, "Input harus menggunakan format email yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    ComboboxOption selectedOptionJenis = (ComboboxOption) cbIDJenis.getSelectedItem();
                    id_jenis_dosen = selectedOptionJenis.getValue();

                    nama_bank = txtNamaBank.getText();
                    cabang_bank = txtCabangBank.getText();
                    no_rekening = txtNoRekening.getText();
                    npwp = txtNPWP.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cbStatus.getSelectedItem();
                    status = selectedOption.getValue();

                    atas_nama = txtAtasNama.getText();
                    kota = txtKota.getText();

                    ComboboxOption selectedOptionPerusahaan = (ComboboxOption) cbIdPerusahaan.getSelectedItem();
                    id_perusahaan = selectedOptionPerusahaan.getValue();

                    if (nama_dosen.isEmpty() || email.isEmpty() || id_jenis_dosen.isEmpty() ||
                            nama_bank.isEmpty() || cabang_bank.isEmpty() || no_rekening.isEmpty() ||
                            status.isEmpty() ||
                            atas_nama.isEmpty() || kota.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin memperbarui data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL sp_UpdateDosen(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_dosen);
                        connection.pstat.setString(2, nama_dosen);
                        connection.pstat.setString(3, email);
                        connection.pstat.setString(4, id_jenis_dosen);
                        connection.pstat.setString(5, nama_bank);
                        connection.pstat.setString(6, cabang_bank);
                        connection.pstat.setString(7, no_rekening);
                        connection.pstat.setString(8, npwp);
                        connection.pstat.setString(9, tanggal_gabung_kampus);
                        connection.pstat.setString(10, tanggal_gabung_industri);
                        connection.pstat.setString(11, status);
                        connection.pstat.setString(12, atas_nama);
                        connection.pstat.setString(13, kota);
                        connection.pstat.setString(14, id_perusahaan);
                        connection.pstat.setBytes(15, imageBytes);

                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        //loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnDelete.setEnabled(false);
                        btnUpdate.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data dosen berhasil diperbarui!");
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
                    id_dosen = txtIDDosen.getText();
                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_DeleteDosen(?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_dosen);

                        connection.pstat.execute();

                        connection.pstat.close();

                        //loadData(null);
                        clear();

                        btnDelete.setEnabled(true);
                        btnSave.setEnabled(false);
                        btnUpdate.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                }
            }
        });
        btnBrowser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser browseImageFile = new JFileChooser();
                //Filter extensions
                FileNameExtensionFilter fnef = new FileNameExtensionFilter("IMAGES", "png", "jpg", "jpeg");
                browseImageFile.addChoosableFileFilter(fnef);
                int showOpenDialogue = browseImageFile.showOpenDialog(null);

                if (showOpenDialogue == JFileChooser.APPROVE_OPTION) {
                    selectedImageFile = browseImageFile.getSelectedFile();
                    selectedImagePath = selectedImageFile.getAbsolutePath();

                    try {
                        // Mengubah gambar menjadi byte array
                        imageBytes = convertImageToByteArray(selectedImageFile);

                        //Display image on jlable
                        ImageIcon ii = new ImageIcon(selectedImagePath);
                        //Resize image to fit jlabel
                        Image image = ii.getImage().getScaledInstance(Label_Gambar.getWidth(), Label_Gambar.getHeight(), Image.SCALE_SMOOTH);
                        Label_Gambar.setIcon(new ImageIcon(image));

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        cbIDJenis.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ComboboxOption selectedOptionJenis = (ComboboxOption) cbIDJenis.getSelectedItem();
                String referensi_dosen = selectedOptionJenis.getHelper();
                for (Component component : JPTanggalIndustri.getComponents()) {
                    component.setEnabled(true);
                }
                cbIdPerusahaan.setEnabled(true);

                if (referensi_dosen.equals("UMUM")) {
                    for (Component component : JPTanggalIndustri.getComponents()) {
                        component.setEnabled(false);
                    }

                    for (int x = 0; x < cbIdPerusahaan.getItemCount(); x++) {
                        Object item = cbIdPerusahaan.getItemAt(x);
                        String Perusahaan = ((ComboboxOption) item).getDisplay();
                        if (Perusahaan.equals("UMUM")) {
                            cbStatus.setSelectedItem(item);
                            break;
                        }
                    }

                    cbIdPerusahaan.setEnabled(false);
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tanggal_gabung_kampus = formatter.format(tanggalKampus.getDate());
                    tanggal_gabung_industri = null;
                    if(tanggalIndustri.getDate() != null) {
                        tanggal_gabung_industri = formatter.format(tanggalIndustri.getDate());
                    }

                    nama_dosen = txtNamaDosen.getText();
                    id_dosen = txtIDDosen.getText();
                    email = txtEmail.getText();

                    if (!isValidEmailFormat(email)) {
                        JOptionPane.showMessageDialog(null, "Input harus menggunakan format email yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    ComboboxOption selectedOptionJenis = (ComboboxOption) cbIDJenis.getSelectedItem();
                    id_jenis_dosen = selectedOptionJenis.getValue();

                    nama_bank = txtNamaBank.getText();
                    cabang_bank = txtCabangBank.getText();
                    no_rekening = txtNoRekening.getText();
                    npwp = txtNPWP.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cbStatus.getSelectedItem();
                    status = selectedOption.getValue();

                    atas_nama = txtAtasNama.getText();
                    kota = txtKota.getText();

                    ComboboxOption selectedOptionPerusahaan = (ComboboxOption) cbIdPerusahaan.getSelectedItem();
                    id_perusahaan = selectedOptionPerusahaan.getValue();

                    if (nama_dosen.isEmpty() || email.isEmpty() || id_jenis_dosen.isEmpty() ||
                            nama_bank.isEmpty() || cabang_bank.isEmpty() || no_rekening.isEmpty() ||
                            status.isEmpty() ||
                            atas_nama.isEmpty() || kota.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin memperbarui data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL sp_UpdateDosen(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_dosen);
                        connection.pstat.setString(2, nama_dosen);
                        connection.pstat.setString(3, email);
                        connection.pstat.setString(4, id_jenis_dosen);
                        connection.pstat.setString(5, nama_bank);
                        connection.pstat.setString(6, cabang_bank);
                        connection.pstat.setString(7, no_rekening);
                        connection.pstat.setString(8, npwp);
                        connection.pstat.setString(9, tanggal_gabung_kampus);
                        connection.pstat.setString(10, tanggal_gabung_industri);
                        connection.pstat.setString(11, status);
                        connection.pstat.setString(12, atas_nama);
                        connection.pstat.setString(13, kota);
                        connection.pstat.setString(14, id_perusahaan);
                        connection.pstat.setBytes(15, imageBytes);

                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        //loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnDelete.setEnabled(false);
                        btnUpdate.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data dosen berhasil diperbarui!");
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
                    id_dosen = txtIDDosen.getText();
                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_DeleteDosen(?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_dosen);

                        connection.pstat.execute();

                        connection.pstat.close();

                        //loadData(null);
                        clear();

                        btnDelete.setEnabled(true);
                        btnSave.setEnabled(false);
                        btnUpdate.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                }
            }
        });
        btnBrowser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser browseImageFile = new JFileChooser();
                //Filter extensions
                FileNameExtensionFilter fnef = new FileNameExtensionFilter("IMAGES", "png", "jpg", "jpeg");
                browseImageFile.addChoosableFileFilter(fnef);
                int showOpenDialogue = browseImageFile.showOpenDialog(null);

                if (showOpenDialogue == JFileChooser.APPROVE_OPTION) {
                    selectedImageFile = browseImageFile.getSelectedFile();
                    selectedImagePath = selectedImageFile.getAbsolutePath();

                    try {
                        // Mengubah gambar menjadi byte array
                        imageBytes = convertImageToByteArray(selectedImageFile);

                        //Display image on jlable
                        ImageIcon ii = new ImageIcon(selectedImagePath);
                        //Resize image to fit jlabel
                        Image image = ii.getImage().getScaledInstance(Label_Gambar.getWidth(), Label_Gambar.getHeight(), Image.SCALE_SMOOTH);
                        Label_Gambar.setIcon(new ImageIcon(image));

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        cbIDJenis.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ComboboxOption selectedOptionJenis = (ComboboxOption) cbIDJenis.getSelectedItem();
                String referensi_dosen = selectedOptionJenis.getHelper();
                for (Component component : JPTanggalIndustri.getComponents()) {
                    component.setEnabled(true);
                }
                cbIdPerusahaan.setEnabled(true);

                if (referensi_dosen.equals("UMUM")) {
                    for (Component component : JPTanggalIndustri.getComponents()) {
                        component.setEnabled(false);
                    }

                    for (int x = 0; x < cbIdPerusahaan.getItemCount(); x++) {
                        Object item = cbIdPerusahaan.getItemAt(x);
                        String Perusahaan = ((ComboboxOption) item).getDisplay();
                        if (Perusahaan.equals("UMUM")) {
                            cbStatus.setSelectedItem(item);
                            break;
                        }
                    }

                    cbIdPerusahaan.setEnabled(false);
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
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                viewDosen view = new viewDosen();
                view.setVisible(true);
            }
        });
    }


    public void clear() {
        txtIDDosen.setText("Otomatis");
        txtNamaDosen.setText("");
        txtEmail.setText("");
        txtNamaBank.setText("");
        txtNoRekening.setText("");
        txtCabangBank.setText("");
        txtNPWP.setText("");
        txtAtasNama.setText("");
        txtKota.setText("");
    }
    private File convertImageToFile(Image image) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        File tempFile = File.createTempFile("temp", ".jpg");
        ImageIO.write(bufferedImage, "jpg", tempFile);
        return tempFile;
    }

    private boolean isValidEmailFormat(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private boolean isValidNumber(String input) {
        return input.matches("\\d*");
    }

    private boolean isValidLettersOnly(String input) {
        return input.matches("[a-zA-Z\\s]*");
    }
    private static byte[] convertImageToByteArray(File imageFile) throws IOException {
        FileInputStream fis = new FileInputStream(imageFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        fis.close();
        baos.close();

        return baos.toByteArray();
    }
    public void showJenisDosen() {
        try {
            String functionCall = "SELECT * FROM dbo.getListJenisDosen(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, null);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                String id_jenis_dosen = connection.result.getString("id_jenis_dosen");
                String nama_jenis = connection.result.getString("nama_jenis");
                String referensi_dosen = connection.result.getString("referensi_dosen");
                cbIDJenis.addItem(new ComboboxOption(id_jenis_dosen, nama_jenis, referensi_dosen));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
    }
    public void addColumn(){
        tableModel.addColumn("ID Dosen");
        tableModel.addColumn("Nama Dosen");
        tableModel.addColumn("Email");
        tableModel.addColumn("Jenis Dosen");
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
                obj[3] = connection.result.getString("nama_jenis");
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
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
    }
    public void showPerusahaan() {
        try {
            String functionCall = "SELECT * FROM dbo.getListPerusahaan(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, null);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                String id_perusahaan = connection.result.getString("id_perusahaan");
                String nama_perusahaan = connection.result.getString("nama_perusahaan");
                cbIdPerusahaan.addItem(new ComboboxOption( id_perusahaan, nama_perusahaan));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
    }
}
