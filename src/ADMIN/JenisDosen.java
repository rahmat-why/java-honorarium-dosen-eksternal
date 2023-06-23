package ADMIN;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class JenisDosen extends JFrame{
    public JPanel panelJenisDosen;
    private JPanel JPJenis;
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


    public JenisDosen() {
        connection = new DBConnect();
        setSize(500, 500);
        setTitle("Form Jenis Dosen");
        setContentPane(JPJenis);
        setLocationRelativeTo(null);

        tampilReferensiDosen();
        tableModel = new DefaultTableModel();
        tblJenisDosen.setModel(tableModel);

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        addColumn();
        loadData();

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
        txtKehadiranGol1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtKehadiranGol1.getText().length() >= 25 ) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Intensif Kehadiran hanya boleh diisi dengan angka!");
                }
            }
        });
            txtKehadiranGol2.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    char c = e.getKeyChar();
                    if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtKehadiranGol2.getText().length() >= 25 ) {
                        e.consume();
                        JOptionPane.showMessageDialog(null, "Intensif Kehadiran hanya boleh diisi dengan angka!");
                    }
                }
            });
            txtKehadiranGol3.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    char c = e.getKeyChar();
                    if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtKehadiranGol3.getText().length() >= 25 ) {
                        e.consume();
                        JOptionPane.showMessageDialog(null, "Intensif Kehadiran hanya boleh diisi dengan angka!");
                    }
                }
            });
            txtKompensasi.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    char c = e.getKeyChar();
                    if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtKehadiranGol3.getText().length() >= 25 ) {
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
                    if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtKehadiranGol3.getText().length() >= 25 ) {
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
                    String kehadiranGol1Text = txtKehadiranGol1.getText();
                    String kehadiranGol2Text = txtKehadiranGol2.getText();
                    String kehadiranGol3Text = txtKehadiranGol3.getText();
                    String kompensasiText = txtKompensasi.getText();
                    String transportText = txtTransport.getText();
                    String persentaseNPWPText = txtPersentaseNPWP.getText();
                    String persentaseNonNPWPText = txtPersentaseNonNPWP.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cbReferensiDosen.getSelectedItem();
                    String Referensi = selectedOption.getValue().toString();

                    // Check if any field is empty
                    if (namaJenis.isEmpty() || kehadiranGol1Text.isEmpty() || kehadiranGol2Text.isEmpty()
                            || kehadiranGol3Text.isEmpty() || kompensasiText.isEmpty() || transportText.isEmpty()
                            || persentaseNPWPText.isEmpty() || persentaseNonNPWPText.isEmpty() || Referensi.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return; // Stop the data saving process if any field is empty
                    }

                    // Parse the numeric fields to their respective data types
                    int Golongan1 = Integer.parseInt(kehadiranGol1Text);
                    int Golongan2 = Integer.parseInt(kehadiranGol2Text);
                    int Golongan3 = Integer.parseInt(kehadiranGol3Text);
                    int Kompensasi = Integer.parseInt(kompensasiText);
                    int Transport = Integer.parseInt(transportText);
                    float NPWP = Float.parseFloat(persentaseNPWPText);
                    float NonNPWP = Float.parseFloat(persentaseNonNPWPText);

                    String procedureCall = "{CALL dbo.sp_CreateJenisDosen(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, namaJenis);
                    connection.pstat.setInt(2, Golongan1);
                    connection.pstat.setInt(3, Golongan2);
                    connection.pstat.setInt(4, Golongan3);
                    connection.pstat.setInt(5, Kompensasi);
                    connection.pstat.setInt(6, Transport);
                    connection.pstat.setFloat(7, NPWP);
                    connection.pstat.setFloat(8, NonNPWP);
                    connection.pstat.setString(9, Referensi);

                    connection.pstat.execute();
                    connection.pstat.close();

                    loadData();
                    clear();

                    btnSave.setEnabled(true);
                    btnUpdate.setEnabled(false);
                    btnDelete.setEnabled(false);

                    JOptionPane.showMessageDialog(null, "Data Jenis Dosen berhasil disimpan!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam penyimpanan data jenis dosen!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Pastikan input angka pada field yang memerlukan nilai numerik!");
                }
            }
        });
        tblJenisDosen.addMouseListener(new MouseAdapter() {
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
                txtKehadiranGol1.setText(String.valueOf((int) tableModel.getValueAt(i, 2)));
                txtKehadiranGol2.setText(String.valueOf((int) tableModel.getValueAt(i, 3)));
                txtKehadiranGol3.setText(String.valueOf((int) tableModel.getValueAt(i, 4)));
                txtKompensasi.setText(String.valueOf((int) tableModel.getValueAt(i, 5)));
                txtTransport.setText(String.valueOf((int) tableModel.getValueAt(i, 6)));
                txtPersentaseNPWP.setText(String.valueOf((float) tableModel.getValueAt(i, 7)));
                txtPersentaseNonNPWP.setText(String.valueOf((float) tableModel.getValueAt(i, 8)));

                String Referensi = (String) tableModel.getValueAt(i, 9);
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
                    String kehadiranGol1Text = txtKehadiranGol1.getText();
                    String kehadiranGol2Text = txtKehadiranGol2.getText();
                    String kehadiranGol3Text = txtKehadiranGol3.getText();
                    String kompensasiText = txtKompensasi.getText();
                    String transportText = txtTransport.getText();
                    String persentaseNPWPText = txtPersentaseNPWP.getText();
                    String persentaseNonNPWPText = txtPersentaseNonNPWP.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cbReferensiDosen.getSelectedItem();
                    String Referensi = selectedOption.getValue().toString();

                    // Check if any field is empty
                    if (id.isEmpty() || namaJenis.isEmpty() || kehadiranGol1Text.isEmpty() || kehadiranGol2Text.isEmpty()
                            || kehadiranGol3Text.isEmpty() || kompensasiText.isEmpty() || transportText.isEmpty()
                            || persentaseNPWPText.isEmpty() || persentaseNonNPWPText.isEmpty() || Referensi.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return; // Stop the data updating process if any field is empty
                    }

                    // Parse the numeric fields to their respective data types
                    int Golongan1 = Integer.parseInt(kehadiranGol1Text);
                    int Golongan2 = Integer.parseInt(kehadiranGol2Text);
                    int Golongan3 = Integer.parseInt(kehadiranGol3Text);
                    int Kompensasi = Integer.parseInt(kompensasiText);
                    int Transport = Integer.parseInt(transportText);
                    float NPWP = Float.parseFloat(persentaseNPWPText);
                    float NonNPWP = Float.parseFloat(persentaseNonNPWPText);

                    String procedureCall = "{CALL dbo.sp_UpdateJenisDosen(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, id);
                    connection.pstat.setString(2, namaJenis);
                    connection.pstat.setInt(3, Golongan1);
                    connection.pstat.setInt(4, Golongan2);
                    connection.pstat.setInt(5, Golongan3);
                    connection.pstat.setInt(6, Kompensasi);
                    connection.pstat.setInt(7, Transport);
                    connection.pstat.setFloat(8, NPWP);
                    connection.pstat.setFloat(9, NonNPWP);
                    connection.pstat.setString(10, Referensi);

                    connection.pstat.execute();
                    connection.pstat.close();

                    loadData();

                    JOptionPane.showMessageDialog(null, "Data Jenis Dosen berhasil diupdate!");

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam perbarui data jenis dosen!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Pastikan input angka pada field yang memerlukan nilai numerik!");
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
                    String procedureCall = "{CALL dbo.sp_DeleteJenisDosen(?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, id);

                    // Execute the stored procedure
                    connection.pstat.execute();

                    // Close the statement and connection
                    connection.pstat.close();

                    loadData();
                    clear();

                    btnDelete.setEnabled(true);
                    btnSave.setEnabled(false);
                    btnUpdate.setEnabled(false);

                    JOptionPane.showMessageDialog(null, "Success delete!");
                } catch (Exception exc) {
                    System.out.println("Error: "+exc.toString());

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
    public void loadData() {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String query = "select * from jenis_dosen";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                Object[] obj = new Object[10]; // Menyesuaikan jumlah kolom dengan tabel tblPenyewa
                obj[0] = connection.result.getString("id_jenis_dosen");
                obj[1] = connection.result.getString("nama_jenis");
                obj[2] = connection.result.getInt("insentif_kehadiran_golongan1");
                obj[3] = connection.result.getInt("insentif_kehadiran_golongan2");
                obj[4] = connection.result.getInt("insentif_kehadiran_golongan3");
                obj[5] = connection.result.getInt("kompensasi_mengajar");
                obj[6] = connection.result.getInt("transport_mengajar");
                obj[7] = connection.result.getFloat("persentase_pph21_npwp");
                obj[8] = connection.result.getFloat("persentase_pph21_nonnpwp");
                obj[9] = connection.result.getString("referensi_dosen");

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
        txtKehadiranGol1.setText("");
        txtKehadiranGol2.setText("");
        txtKehadiranGol3.setText("");
        txtKompensasi.setText("");
        txtTransport.setText("");
        txtPersentaseNPWP.setText("");
        txtPersentaseNonNPWP.setText("");
    }
    public void addColumn(){
        tableModel.addColumn("ID Jenis");
        tableModel.addColumn("Nama Jenis");
        tableModel.addColumn("Intensif Kehadiran Gol 1");
        tableModel.addColumn("Intensif Kehadiran Gol 2");
        tableModel.addColumn("Intensif Kehadiran Gol 3");
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
    public static void main(String[]args){
        new JenisDosen().setVisible(true);
    }
}
