package DAAA;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Absen extends JFrame{
    JPanel panelAbsen;
    private JComboBox cmbIDDosen;
    private JComboBox cmbMatkul;
    private JComboBox cmbProdi;
    private JTextField txtKelas;
    private JPanel JPTanggalMengajar;
    private JTextField txtSks;
    private JComboBox cmbJenisDosen;
    private JPanel jpTanggalAkhir;
    private JPanel jpTanggalAwal;
    private JButton btnFilter;
    private JButton btnSave;
    private JTable tblAbsensi;
    private JTextField txtSearch;
    private JButton btnDelete;
    private JTextField txtIdAbsen;
    private JButton btnClear;
    private JButton txtClear;

    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();
    public Absen() {
        JDateChooser tanggalMengajar = new JDateChooser();
        JPTanggalMengajar.add(tanggalMengajar);
        JDateChooser tanggalAwal = new JDateChooser();
        jpTanggalAwal.add(tanggalAwal);
        JDateChooser tanggalAkhir = new JDateChooser();
        jpTanggalAkhir.add(tanggalAkhir);

        tableModel = new DefaultTableModel();
        tblAbsensi.setModel(tableModel);

        showProdi();
        showMatkul();
        showDosen();
        showJenisDosen();
        addColumn();
        btnSave.setEnabled(true);
        btnDelete.setEnabled(false);


        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Mendapatkan tanggal saat ini
                    Date currentDate = new Date();

                    ComboboxOption selectedOptionidDosen = (ComboboxOption) cmbIDDosen.getSelectedItem();
                    String idDosen = selectedOptionidDosen.getValue().toString();

                    ComboboxOption selectedOption1 = (ComboboxOption) cmbMatkul.getSelectedItem();
                    String matkul = selectedOption1.getValue().toString();

                    ComboboxOption selectedOption2 = (ComboboxOption) cmbProdi.getSelectedItem();
                    String prodi = selectedOption2.getValue().toString();

                    Date selectedDate = tanggalMengajar.getDate();

                    // Memeriksa apakah tanggal yang dipilih lebih besar dari tanggal saat ini
                    if (selectedDate.after(currentDate)) {
                        JOptionPane.showMessageDialog(null, "Tanggal mengajar tidak boleh lebih dari hari ini!");
                        return;
                    }

                    Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String tanggal = formatter.format(selectedDate);

                    String kelas = txtKelas.getText();
                    int sks = Integer.parseInt(txtSks.getText());

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data jenis dosen?",
                            "Konfirmasi Simpan Data", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL sp_CreateAbsensi(?, ?, ?, ?, ?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, idDosen);
                        connection.pstat.setString(2, matkul);
                        connection.pstat.setString(3, prodi);
                        connection.pstat.setString(4, "USR003");
                        connection.pstat.setString(5, kelas);
                        connection.pstat.setString(6, tanggal);
                        connection.pstat.setInt(7, sks);
                        connection.pstat.execute();

                        connection.pstat.close();

                        JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
                        btnDelete.setEnabled(false);
                    }
                } catch (Exception exc) {
                    System.out.println("Error: " + exc.toString());
                    JOptionPane.showMessageDialog(null, "data tidak boleh kosong!");
                }
            }
        });



        txtSks.addKeyListener(new KeyAdapter() {
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
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                Format formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                String firstDate = formatter1.format(tanggalAwal.getDate()).toString();

                Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                String lastDate = formatter2.format(tanggalAkhir.getDate()).toString();

                ComboboxOption selectedOption = (ComboboxOption) cmbJenisDosen.getSelectedItem();
                String jnDosen = selectedOption.getValue().toString();

                showByNama(firstDate, lastDate, jnDosen, txtSearch.getText());
            }
        });
        cmbIDDosen.addKeyListener(new KeyAdapter() {
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
        cmbMatkul.addKeyListener(new KeyAdapter() {
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
        cmbProdi.addKeyListener(new KeyAdapter() {
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
        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Format formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                String firstDate = formatter1.format(tanggalAwal.getDate()).toString();

                Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                String lastDate = formatter2.format(tanggalAkhir.getDate()).toString();

                ComboboxOption selectedOption = (ComboboxOption) cmbJenisDosen.getSelectedItem();
                String jnDosen = selectedOption.getValue().toString();

                showByNama(firstDate, lastDate, jnDosen, null);
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = txtIdAbsen.getText();
                    Date tanggal = tanggalMengajar.getDate();

                    // Mendapatkan tanggal saat ini
                    Date currentDate = new Date();

                    // Mendapatkan tanggal setelah dikurangi 30 hari
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);
                    calendar.add(Calendar.DAY_OF_MONTH, -30);
                    Date minDate = calendar.getTime();

                    // Memeriksa apakah tanggal transaksi dikurangi 30 hari >= tanggal 16
                    if (tanggal.after(minDate) || tanggal.equals(minDate)) {
                        int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            // Prepare the stored procedure call
                            String procedureCall = "{CALL dbo.sp_DeleteAbsensi(?)}";
                            connection.pstat = connection.conn.prepareCall(procedureCall);
                            connection.pstat.setString(1, id);
                            // Execute the stored procedure
                            connection.pstat.execute();

                            // Close the statement and connection
                            connection.pstat.close();

                            btnDelete.setEnabled(true);
                            btnSave.setEnabled(true);

                            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Penghapusan data dibatalkan.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Transaksi ini tidak dapat dihapus karena sudah dibukukan");
                    }
                } catch (Exception exc) {
                    System.out.println("Error: " + exc.toString());
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
                }
            }
        });

        tblAbsensi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectedRow = tblAbsensi.getSelectedRow();
                if (selectedRow == -1) {
                    return;
                }
                txtIdAbsen.setText((String) tableModel.getValueAt(selectedRow, 0));
                String jnID = (String) tableModel.getValueAt(selectedRow, 1);
                for (int x = 0; x < cmbIDDosen.getItemCount(); x++) {
                    Object item = cmbIDDosen.getItemAt(x);
                    if (item instanceof ComboboxOption) {
                        String jenisCb = ((ComboboxOption) item).getDisplay();
                        System.out.println("jnID = " + jnID + " jenisCb = " + jenisCb);
                        if (jenisCb.equals(jnID)) {
                            cmbIDDosen.setSelectedItem(item);
                            break;
                        }
                    }
                }

                String matkul = (String) tableModel.getValueAt(selectedRow, 2);
                for (int x = 0; x < cmbMatkul.getItemCount(); x++) {
                    Object item = cmbMatkul.getItemAt(x);
                    if (item instanceof ComboboxOption) {
                        String jenisCb = ((ComboboxOption) item).getDisplay();
                        System.out.println("matkul = " + matkul + " jenisCb = " + jenisCb);
                        if (jenisCb.equals(matkul)) {
                            cmbMatkul.setSelectedItem(item);
                            break;
                        }
                    }
                }

                String prodi = (String) tableModel.getValueAt(selectedRow, 3);
                for (int x = 0; x < cmbProdi.getItemCount(); x++) {
                    Object item = cmbProdi.getItemAt(x);
                    if (item instanceof ComboboxOption) {
                        String jenisCb = ((ComboboxOption) item).getDisplay();
                        System.out.println("prodi = " + prodi + " jenisCb = " + jenisCb);
                        if (jenisCb.equals(prodi)) {
                            cmbProdi.setSelectedItem(item);
                            break;
                        }
                    }
                }

                txtKelas.setText((String) tableModel.getValueAt(selectedRow, 4));

                //System.out.println((Date) tblAbsensi.getValueAt(selectedRow, 5));
                tanggalMengajar.setDate((Date) tblAbsensi.getValueAt(selectedRow, 5));

                txtSks.setText(String.valueOf((int) tableModel.getValueAt(selectedRow, 6)));

                btnSave.setEnabled(false);
                btnDelete.setEnabled(true);
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clear();
                btnSave.setEnabled(true);
                btnDelete.setEnabled(false);
            }
        });
    }


    public void showProdi() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT * FROM prodi"; // Corrected table name
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                String idProdi = connection.result.getString("id_prodi"); // Corrected column name
                String namaProdi = connection.result.getString("nama_prodi"); // Corrected column name
                cmbProdi.addItem(new ComboboxOption(idProdi, namaProdi));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }
    public void Clear(){
        txtIdAbsen.setText("Otomatis");
        txtKelas.setText("");
        txtSks.setText("");
    }

    public void showMatkul() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT * FROM matkul"; // Corrected table name
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                String idMatkul = connection.result.getString("id_matkul"); // Corrected column name
                String namaMatkul = connection.result.getString("nama_matkul"); // Corrected column name
                cmbMatkul.addItem(new ComboboxOption(idMatkul, namaMatkul));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }
    public void showJenisDosen() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT * FROM jenis_dosen";
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                String jnID = connection.result.getString("id_jenis_dosen");
                String namaJenis = connection.result.getString("nama_jenis");
                cmbJenisDosen.addItem(new ComboboxOption(jnID, namaJenis));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }
    public void showDosen() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT * FROM dosen"; // Corrected table name
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                String idDosen = connection.result.getString("id_dosen"); // Corrected column name
                String namaDosen = connection.result.getString("nama_dosen"); // Corrected column name
                cmbIDDosen.addItem(new ComboboxOption(idDosen, namaDosen));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }

    public void addColumn() {
        tableModel.addColumn("Nama Dosen");
        tableModel.addColumn("Mata Kuliah");
        tableModel.addColumn("Prodi");
        tableModel.addColumn("Kelas");
        tableModel.addColumn("Tanggal Mengajar");
        tableModel.addColumn("SKS");
        tableModel.addColumn("Kompensansi Mengajar");
        tableModel.addColumn("Transport Mengajar");
        tableModel.addColumn("Kehadiran");
        tableModel.addColumn("Total Pendapatan");
        tableModel.addColumn("Gross up");
    }

    private void showByNama(String tanggal_awal, String tanggal_akhir, String id_jenis_dosen, String filter){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try{
            String functionCall = "SELECT * FROM dbo.getListAbsensi(?,?,?,?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, tanggal_awal);
            connection.pstat.setString(2, tanggal_akhir);
            connection.pstat.setString(3, id_jenis_dosen);
            connection.pstat.setString(4, filter);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[12]; // Menyesuaikan jumlah kolom dengan tabel tblPenyewa
                obj[0] = connection.result.getString("id_absensi");
                obj[1] = connection.result.getString("nama_dosen");
                obj[2] = connection.result.getString("nama_matkul");
                obj[3] = connection.result.getString("nama_prodi");
                obj[4] = connection.result.getString("kelas");
                obj[5] = connection.result.getDate("tanggal_mengajar");
                obj[6] = connection.result.getInt("sks");

                float kompensasi_mengajar = connection.result.getFloat("kompensasi_mengajar");
                obj[7] = kompensasi_mengajar;

                float insentif_kehadiran = connection.result.getFloat("insentif_kehadiran");
                obj[8] = insentif_kehadiran;

                float transport_mengajar = connection.result.getFloat("transport_mengajar");
                obj[9] = transport_mengajar;

                float total_pendapatan = kompensasi_mengajar+insentif_kehadiran+transport_mengajar;
                obj[10] = total_pendapatan;

                float pph21 = connection.result.getInt("pph21");
                obj[11] = total_pendapatan+pph21;

                tableModel.addRow(obj);
            }

            connection.pstat.close();
            connection.result.close();
        }catch (Exception e){
            System.out.println("Terjadi error saat load data Absensi :" + e);
        }
    }

    public static void main(String[]args){
        new Absen().setVisible(true);
    }
}
