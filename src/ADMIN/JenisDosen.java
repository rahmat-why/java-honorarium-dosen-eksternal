package ADMIN;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.NumberFormat;

public class JenisDosen extends JFrame{
    public JPanel panelJenisDosen;
    private JTextField txtNamaJenis;
    private JTextField txtKehadiranGol1;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTextField txtSearch;
    private JTable tblJenisDosen;
    private JTextField txtKehadiranGol2;
    private JTextField txtKehadiranGol3;
    private JTextField txtKompensasi;
    private JTextField txtTransport;
    private JTextField txtPersentaseNPWP;
    private JTextField txtPersentaseNonNPWP;
    private JTextField txtID;
    private JComboBox cbReferensiDosen;
    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();
    String id_jenis, nama_jenis, referensi_dosen;
    float kompensasi_mengajar, transport_mengajar, persentase_pph21_npwp, persentasi_pph21_non_npwpw;

    public JenisDosen() {
        tampilReferensiDosen();
        tableModel = new DefaultTableModel();
        tblJenisDosen.setModel(tableModel);

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        addColumn();
        loadData(null);

        txtNamaJenis.addKeyListener(new KeyAdapter() {
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

        txtKompensasi.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            char c = e.getKeyChar();
            if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtKompensasi.getText().length() >= 25 ) {
                e.consume();
                JOptionPane.showMessageDialog(null, "Kompensasi mengajar hanya boleh diisi dengan angka!");
            }
            }
        });

        txtTransport.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            char c = e.getKeyChar();
            if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtTransport.getText().length() >= 25 ) {
                e.consume();
                JOptionPane.showMessageDialog(null, "Transport Mengajar hanya boleh diisi dengan angka!");
            }
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    nama_jenis = txtNamaJenis.getText();
                    kompensasi_mengajar = Float.parseFloat(txtKompensasi.getText());
                    transport_mengajar = Float.parseFloat(txtTransport.getText());
                    persentase_pph21_npwp = Float.parseFloat(txtPersentaseNPWP.getText());
                    persentasi_pph21_non_npwpw = Float.parseFloat(txtPersentaseNonNPWP.getText());

                    ComboboxOption selectedOption = (ComboboxOption) cbReferensiDosen.getSelectedItem();
                    referensi_dosen = selectedOption.getValue().toString();

                    if (nama_jenis.isEmpty() || referensi_dosen.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data jenis dosen?",
                            "Konfirmasi Simpan Data", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_CreateJenisDosen(?, ?, ?, ?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, nama_jenis);
                        connection.pstat.setFloat(2, kompensasi_mengajar);
                        connection.pstat.setFloat(3, transport_mengajar);
                        connection.pstat.setFloat(4, persentase_pph21_npwp);
                        connection.pstat.setFloat(5, persentasi_pph21_non_npwpw);
                        connection.pstat.setString(6, referensi_dosen);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data Jenis Dosen berhasil disimpan!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                }
            }
        });
        tblJenisDosen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblJenisDosen.getSelectedRow();
                if (i == -1) {
                    return;
                }
                txtID.setText((String) tableModel.getValueAt(i, 0));
                txtNamaJenis.setText((String) tableModel.getValueAt(i, 1));
                txtKompensasi.setText(String.valueOf((int) tableModel.getValueAt(i, 2)));
                txtTransport.setText(String.valueOf((int) tableModel.getValueAt(i, 3)));
                txtPersentaseNPWP.setText(String.valueOf((float) tableModel.getValueAt(i, 4)));
                txtPersentaseNonNPWP.setText(String.valueOf((float) tableModel.getValueAt(i, 5)));

                referensi_dosen = (String) tableModel.getValueAt(i, 6);
                for (int x = 0; x < cbReferensiDosen.getItemCount(); x++) {
                    Object item = cbReferensiDosen.getItemAt(x);
                    String cb_referensi_dosen = ((ComboboxOption) item).getValue();

                    if (cb_referensi_dosen.equals(referensi_dosen)) {
                        cbReferensiDosen.setSelectedItem(item);
                        break;
                    }
                }

                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    id_jenis = txtID.getText();
                    nama_jenis = txtNamaJenis.getText();
                    kompensasi_mengajar = Float.parseFloat(txtKompensasi.getText());
                    transport_mengajar = Float.parseFloat(txtTransport.getText());
                    persentase_pph21_npwp = Float.parseFloat(txtPersentaseNPWP.getText());
                    persentasi_pph21_non_npwpw = Float.parseFloat(txtPersentaseNonNPWP.getText());

                    ComboboxOption selectedOption = (ComboboxOption) cbReferensiDosen.getSelectedItem();
                    referensi_dosen = selectedOption.getValue().toString();

                    if (id_jenis.isEmpty() || nama_jenis.isEmpty() || referensi_dosen.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin memperbarui data jenis dosen?",
                            "Konfirmasi Perbarui Data", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_UpdateJenisDosen(?, ?, ?, ?, ?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_jenis);
                        connection.pstat.setString(2, nama_jenis);
                        connection.pstat.setFloat(3, kompensasi_mengajar);
                        connection.pstat.setFloat(4, transport_mengajar);
                        connection.pstat.setFloat(5, persentase_pph21_npwp);
                        connection.pstat.setFloat(6, persentasi_pph21_non_npwpw);
                        connection.pstat.setString(7, referensi_dosen);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        JOptionPane.showMessageDialog(null, "Data Jenis Dosen berhasil diperbarui!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Form harus lengkap dan sesuai format!");
                }

                btnSave.setEnabled(true);
                btnDelete.setEnabled(false);
                btnUpdate.setEnabled(false);
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    id_jenis = txtID.getText();

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_DeleteJenisDosen(?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_jenis);

                        connection.pstat.execute();

                        connection.pstat.close();

                        loadData(null);
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


        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
                btnSave.setEnabled(true);
                btnUpdate.setEnabled(false);
                btnDelete.setEnabled(false);
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                loadData(txtSearch.getText());
            }
        });
        txtPersentaseNPWP.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField textField = (JTextField) input;
                String text = textField.getText().trim();

                try {
                    double value = Double.parseDouble(text);
                    if (value >= 0 && value <= 100) {
                        String formattedValue = NumberFormat.getInstance().format(value);
                        textField.setText(formattedValue);
                        return true;
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Input harus berupa angka dengan desimal");
                    return false;
                }
            }
        });
        txtPersentaseNPWP.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Input harus berupa angka dengan format desimal");
                }
            }
        });
        txtPersentaseNonNPWP.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                char character = e.getKeyChar();

                // Memeriksa apakah karakter yang dimasukkan adalah angka atau desimal
                if (!Character.isDigit(character) && character != '.' && character != KeyEvent.VK_BACK_SPACE) {
                    e.consume(); // Mencegah karakter yang tidak valid dimasukkan
                    JOptionPane.showMessageDialog(null, "Input harus berupa angka dengan format desimal");
                }
            }
        });

    }

    public void loadData(String nama_jenis) {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            String functionCall = "SELECT * FROM dbo.getListJenisDosen(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, nama_jenis);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[7];
                obj[0] = connection.result.getString("id_jenis_dosen");
                obj[1] = connection.result.getString("nama_jenis");
                obj[2] = connection.result.getInt("kompensasi_mengajar");
                obj[3] = connection.result.getInt("transport_mengajar");
                obj[4] = connection.result.getFloat("persentase_pph21_npwp");
                obj[5] = connection.result.getFloat("persentase_pph21_nonnpwp");
                obj[6] = connection.result.getString("referensi_dosen");

                tableModel.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
    }

    public void clear() {
        txtID.setText("Otomatis");
        txtNamaJenis.setText("");
        txtKompensasi.setText("");
        txtTransport.setText("");
        txtPersentaseNPWP.setText("");
        txtPersentaseNonNPWP.setText("");
    }

    public void addColumn(){
        tableModel.addColumn("ID Jenis");
        tableModel.addColumn("Nama Jenis");
        tableModel.addColumn("Kompensasi Mengajar");
        tableModel.addColumn("Transport Mengajar");
        tableModel.addColumn("Persentase PPH21 NPWP");
        tableModel.addColumn("Persentase PPH21 NonNPWP");
        tableModel.addColumn("Referensi Dosen");
    }

    public void tampilReferensiDosen(){
        cbReferensiDosen.addItem(new ComboboxOption("UMUM", "UMUM"));
        cbReferensiDosen.addItem(new ComboboxOption("INDUSTRI", "INDUSTRI"));
    }
}
