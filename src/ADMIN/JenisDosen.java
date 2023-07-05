package ADMIN;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

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
    //private JComboBox cbIDGolongan;

    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();


    public JenisDosen() {
        connection = new DBConnect();

        tampilReferensiDosen();
        tableModel = new DefaultTableModel();
        tblJenisDosen.setModel(tableModel);

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        addColumn();
        loadData(null);
//        showGolongan();
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
                    String namaJenis = txtNamaJenis.getText();
                    String kompensasiText = txtKompensasi.getText();
                    String transportText = txtTransport.getText();
                    String persentaseNPWPText = txtPersentaseNPWP.getText();
                    String persentaseNonNPWPText = txtPersentaseNonNPWP.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cbReferensiDosen.getSelectedItem();
                    String Referensi = selectedOption.getValue().toString();

                    // Check if any field is empty
                    if (namaJenis.isEmpty() || kompensasiText.isEmpty() || transportText.isEmpty()
                            || persentaseNPWPText.isEmpty() || persentaseNonNPWPText.isEmpty() || Referensi.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return; // Stop the data saving process if any field is empty
                    }

                    // Parse the numeric fields to their respective data types
                    int Kompensasi = Integer.parseInt(kompensasiText);
                    int Transport = Integer.parseInt(transportText);
                    float NPWP = Float.parseFloat(persentaseNPWPText);
                    float NonNPWP = Float.parseFloat(persentaseNonNPWPText);

                    // Display confirmation dialog
                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data jenis dosen?",
                            "Konfirmasi Simpan Data", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_CreateJenisDosen(?, ?, ?, ?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, namaJenis);
                        connection.pstat.setInt(2, Kompensasi);
                        connection.pstat.setInt(3, Transport);
                        connection.pstat.setFloat(4, NPWP);
                        connection.pstat.setFloat(5, NonNPWP);
                        connection.pstat.setString(6, Referensi);

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
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam penyimpanan data jenis dosen!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Pastikan input angka pada field yang memerlukan nilai numerik!");
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

                String Referensi = (String) tableModel.getValueAt(i, 6);
                for (int x = 0; x < cbReferensiDosen.getItemCount(); x++) {
                    Object item = cbReferensiDosen.getItemAt(x);
                    String jenisCb = ((ComboboxOption) item).getValue();
                    System.out.println("referensi = "+Referensi+" jenisCb = "+jenisCb);
                    if (jenisCb.equals(Referensi)) {
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
                    String id = txtID.getText();
                    String namaJenis = txtNamaJenis.getText();
                    String kompensasiText = txtKompensasi.getText();
                    String transportText = txtTransport.getText();
                    String persentaseNPWPText = txtPersentaseNPWP.getText();
                    String persentaseNonNPWPText = txtPersentaseNonNPWP.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cbReferensiDosen.getSelectedItem();
                    String Referensi = selectedOption.getValue().toString();

                    // Check if any field is empty
                    if (id.isEmpty() || namaJenis.isEmpty() || kompensasiText.isEmpty() || transportText.isEmpty()
                            || persentaseNPWPText.isEmpty() || persentaseNonNPWPText.isEmpty() || Referensi.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return; // Stop the data updating process if any field is empty
                    }

                    // Parse the numeric fields to their respective data types
                    int Kompensasi = Integer.parseInt(kompensasiText);
                    int Transport = Integer.parseInt(transportText);
                    float NPWP = Float.parseFloat(persentaseNPWPText);
                    float NonNPWP = Float.parseFloat(persentaseNonNPWPText);

                    // Display confirmation dialog
                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin memperbarui data jenis dosen?",
                            "Konfirmasi Perbarui Data", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_UpdateJenisDosen(?, ?, ?, ?, ?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, namaJenis);
                        connection.pstat.setInt(3, Kompensasi);
                        connection.pstat.setInt(4, Transport);
                        connection.pstat.setFloat(5, NPWP);
                        connection.pstat.setFloat(6, NonNPWP);
                        connection.pstat.setString(7, Referensi);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        JOptionPane.showMessageDialog(null, "Data Jenis Dosen berhasil diperbarui!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam perbarui data jenis dosen!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Pastikan input angka pada field yang memerlukan nilai numerik!");
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
                    String id;
                    id = txtID.getText();

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Prepare the stored procedure call
                        String procedureCall = "{CALL dbo.sp_DeleteJenisDosen(?)}";
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
                    }else{
                        JOptionPane.showMessageDialog(null, "Penghapusan data dibatalkan.");
                    }
                } catch (Exception exc) {
                    System.out.println("Error: " + exc.toString());

                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
                }
            }
        });
        txtPersentaseNPWP.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Input harus menggunakan desimal!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (c == '.' && txtPersentaseNPWP.getText().contains(".")) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Input harus menggunakan desimal!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        txtPersentaseNonNPWP.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Input harus menggunakan desimal!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (c == '.' && txtPersentaseNonNPWP.getText().contains(".")) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Input harus menggunakan desimal!", "Error", JOptionPane.ERROR_MESSAGE);
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
    }
    public void generateId() {
        try {
            String query = "SELECT dbo.GenerateJenisDosenID() AS newId";
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
    public void loadData(String nama_jenis) {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            String functionCall = "SELECT * FROM dbo.getListJenisDosen(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, nama_jenis);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[7]; // Menyesuaikan jumlah kolom dengan tabel tblPenyewa
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
            System.out.println("Error: " + exc.toString());
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
