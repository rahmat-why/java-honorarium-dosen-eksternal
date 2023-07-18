package DAAA;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;
import LOGIN.ADTUser;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    Format formatter = new SimpleDateFormat("yyyy-MM-dd");
    DBConnect connection = new DBConnect();
    String id_absensi, id_dosen, id_matkul, id_prodi, tanggal_mengajar, tanggal_awal, tanggal_akhir, kelas;
    Date currentDate = new Date();
    int sks;
    JDateChooser tanggalMengajar = new JDateChooser();
    JDateChooser tanggalAwal = new JDateChooser();
    JDateChooser tanggalAkhir = new JDateChooser();

    public void showDefaultAbsensi() {
        LocalDate currentDate = LocalDate.now();
        LocalDate start = currentDate.minusMonths(2).withDayOfMonth(16);
        Calendar calendar = Calendar.getInstance();
        calendar.set(start.getYear(), start.getMonthValue() - 1, start.getDayOfMonth());
        tanggalAwal.setDate(calendar.getTime());

        LocalDate end = start.plusMonths(1).withDayOfMonth(15);
        calendar.set(end.getYear(), end.getMonthValue() - 1, end.getDayOfMonth());
        tanggalAkhir.setDate(calendar.getTime());
    }

    public Absen(ADTUser verifyUser) {
        JPTanggalMengajar.add(tanggalMengajar);
        jpTanggalAwal.add(tanggalAwal);
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
        showDefaultAbsensi();

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ComboboxOption selectedOptionidDosen = (ComboboxOption) cmbIDDosen.getSelectedItem();
                    id_dosen = selectedOptionidDosen.getValue().toString();

                    ComboboxOption selectedOption1 = (ComboboxOption) cmbMatkul.getSelectedItem();
                    id_matkul = selectedOption1.getValue().toString();

                    ComboboxOption selectedOption2 = (ComboboxOption) cmbProdi.getSelectedItem();
                    id_prodi = selectedOption2.getValue().toString();

                    Date tanggal_mengajar_selected = tanggalMengajar.getDate();

                    if (tanggal_mengajar_selected.after(currentDate)) {
                        JOptionPane.showMessageDialog(null, "Tanggal mengajar tidak boleh lebih dari hari ini!");
                        return;
                    }

                    tanggal_mengajar = formatter.format(tanggal_mengajar_selected);

                    kelas = txtKelas.getText();
                    sks = Integer.parseInt(txtSks.getText());

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data jenis dosen?",
                            "Konfirmasi Simpan Data", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL sp_CreateAbsensi(?, ?, ?, ?, ?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_dosen);
                        connection.pstat.setString(2, id_matkul);
                        connection.pstat.setString(3, id_prodi);
                        connection.pstat.setString(4, verifyUser.getId_user());
                        connection.pstat.setString(5, kelas);
                        connection.pstat.setString(6, tanggal_mengajar);
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

            tanggal_awal = formatter.format(tanggalAwal.getDate()).toString();
            tanggal_akhir = formatter.format(tanggalAkhir.getDate()).toString();

            ComboboxOption selectedOption = (ComboboxOption) cmbJenisDosen.getSelectedItem();
            id_dosen = selectedOption.getValue().toString();

            showByNama(tanggal_awal, tanggal_akhir, id_dosen, txtSearch.getText());
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
                tanggal_awal = formatter.format(tanggalAwal.getDate()).toString();
                tanggal_akhir = formatter.format(tanggalAkhir.getDate()).toString();

                ComboboxOption selectedOption = (ComboboxOption) cmbJenisDosen.getSelectedItem();
                String id_jenis_dosen = selectedOption.getValue().toString();

                showByNama(tanggal_awal, tanggal_akhir, id_jenis_dosen, null);
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    id_absensi = txtIdAbsen.getText();
                    Date tanggal_mengajar_selected = tanggalMengajar.getDate();

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);
                    calendar.add(Calendar.DAY_OF_MONTH, -30);
                    Date last_month = calendar.getTime();

                    // Memeriksa apakah tanggal transaksi dikurangi 30 hari >= tanggal 16
                    if (!(tanggal_mengajar_selected.after(last_month) || tanggal_mengajar_selected.equals(last_month))) {
                        JOptionPane.showMessageDialog(null, "Transaksi ini tidak dapat dihapus karena sudah dibukukan");
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_DeleteAbsensi(?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_absensi);
                        connection.pstat.execute();
                        connection.pstat.close();

                        btnDelete.setEnabled(true);
                        btnSave.setEnabled(true);

                        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
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

                String nama_dosen = (String) tableModel.getValueAt(selectedRow, 1);
                for (int x = 0; x < cmbIDDosen.getItemCount(); x++) {
                    Object item = cmbIDDosen.getItemAt(x);
                    String cb_nama_dosen = ((ComboboxOption) item).getDisplay();
                    if (cb_nama_dosen.equals(nama_dosen)) {
                        cmbIDDosen.setSelectedItem(item);
                        break;
                    }
                }

                String nama_matkul = (String) tableModel.getValueAt(selectedRow, 2);
                for (int x = 0; x < cmbMatkul.getItemCount(); x++) {
                    Object item = cmbMatkul.getItemAt(x);
                    String cb_nama_matkul = ((ComboboxOption) item).getDisplay();
                    if (cb_nama_matkul.equals(nama_matkul)) {
                        cmbMatkul.setSelectedItem(item);
                        break;
                    }
                }

                String nama_prodi = (String) tableModel.getValueAt(selectedRow, 3);
                for (int x = 0; x < cmbProdi.getItemCount(); x++) {
                    Object item = cmbProdi.getItemAt(x);
                    String cb_nama_prodi = ((ComboboxOption) item).getDisplay();
                    if (cb_nama_prodi.equals(nama_prodi)) {
                        cmbProdi.setSelectedItem(item);
                        break;
                    }
                }

                txtKelas.setText((String) tableModel.getValueAt(selectedRow, 4));
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
                tanggalMengajar.setDate(null);
            }
        });
    }

    public void showProdi() {
        try {
            String functionCall = "SELECT * FROM dbo.getListProdi(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, null);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                String id_prodi = connection.result.getString("id_prodi");
                String nama_prodi = connection.result.getString("nama_prodi");
                cmbProdi.addItem(new ComboboxOption(id_prodi, nama_prodi));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
    }

    public void Clear(){
        txtIdAbsen.setText("Otomatis");
        txtKelas.setText("");
        txtSks.setText("");
    }

    public void showMatkul() {
        try {
            String functionCall = "SELECT * FROM dbo.getListMatkul(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, null);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                String id_matkul = connection.result.getString("id_matkul");
                String nama_matkul = connection.result.getString("nama_matkul");
                cmbMatkul.addItem(new ComboboxOption(id_matkul, nama_matkul));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
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
                cmbJenisDosen.addItem(new ComboboxOption(id_jenis_dosen, nama_jenis));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
    }

    public void showDosen() {
        try {
            String functionCall = "SELECT * FROM dbo.getListDosen(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, null);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                String id_dosen = connection.result.getString("id_dosen");
                String nama_dosen = connection.result.getString("nama_dosen");
                cmbIDDosen.addItem(new ComboboxOption(id_dosen, nama_dosen));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
    }

    public void addColumn() {
        tableModel.addColumn("ID Absen");
        tableModel.addColumn("Nama Dosen");
        tableModel.addColumn("Mata Kuliah");
        tableModel.addColumn("Prodi");
        tableModel.addColumn("Kelas");
        tableModel.addColumn("Tanggal Mengajar");
        tableModel.addColumn("SKS");
        tableModel.addColumn("Kompensansi Mengajar");
        tableModel.addColumn("Transport Mengajar");
        tableModel.addColumn("Insentif Kehadiran");
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
                Object[] obj = new Object[12];
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
        }catch (Exception exc){
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
    }
}
