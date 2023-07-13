package REPORT;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;
import LOGIN.ADTUser;
import com.toedter.calendar.JDateChooser;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class transferReport extends JFrame{
    public JPanel panelTransfer;
    private JTable tblTransfer;
    private JButton btnCetak;
    private JPanel JPTanggalAkhir;
    private JPanel JPTanggalAwal;
    private JButton btnFilter;
    private JComboBox cbJenisDosen;
    private JLabel TOTALTRANSFER;
    public JPanel panelReportTransfer;

    DefaultTableModel tableModel;

    DBConnect connection = new DBConnect();;
    JDateChooser tanggalAwal = new JDateChooser();
    JDateChooser tanggalAkhir = new JDateChooser();

    double total_transfer = 0;

    public transferReport(ADTUser verifyUser) {
        JPTanggalAwal.add(tanggalAwal);
        JPTanggalAkhir.add(tanggalAkhir);

        showJenisDosen(null);

        tableModel = new DefaultTableModel();
        tblTransfer.setModel(tableModel);
        addColumn();

        showDefaultAbsensi();

        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Format formatTanggalAwal = new SimpleDateFormat("yyyy-MM-dd");
                String tanggal_awal = formatTanggalAwal.format(tanggalAwal.getDate());

                Format formatTanggalAkhir = new SimpleDateFormat("yyyy-MM-dd");
                String tanggal_akhir = formatTanggalAkhir.format(tanggalAkhir.getDate());

                ComboboxOption selectedJenisDosen = (ComboboxOption) cbJenisDosen.getSelectedItem();
                String id_jenis_dosen = selectedJenisDosen.getValue();

                loadData(tanggal_awal, tanggal_akhir, id_jenis_dosen);
            }
        });

        btnCetak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    HashMap parameter = new HashMap();

                    Format formatTanggalAwal = new SimpleDateFormat("dd MMMM yyyy");
                    String tanggal_awal = formatTanggalAwal.format(tanggalAwal.getDate());

                    Format formatTanggalAkhir = new SimpleDateFormat("dd MMMM yyyy");
                    String tanggal_akhir = formatTanggalAkhir.format(tanggalAkhir.getDate());

                    Format formatTanggalAkhirSemester = new SimpleDateFormat("yyyy-MM-dd");
                    String tanggal_awal_semester = formatTanggalAkhirSemester.format(tanggalAwal.getDate());
                    parameter.put("SEMESTER", getSemester(LocalDate.parse(tanggal_awal_semester)));
                    parameter.put("THNAKADEMIK", getTahunAkademik(LocalDate.parse(tanggal_awal_semester)));

                    ComboboxOption selectedJenisDosen = (ComboboxOption) cbJenisDosen.getSelectedItem();
                    String nama_jenis = selectedJenisDosen.getDisplay();
                    parameter.put("JENISDOSEN", nama_jenis);

                    parameter.put("PERIODE", tanggal_awal+" - "+tanggal_akhir);
                    parameter.put("TOTALTRANSFER", formatRupiah(total_transfer));
                    parameter.put("TERBILANG", formatTerbilang((int) total_transfer));
                    parameter.put("MEMBUAT", verifyUser.getNama());
                    parameter.put("MENYETUJUI", "Kepala DAAA");
                    parameter.put("MENGETAHUI", "Staff Keuangan");

                    JRDataSource dataSource = new JRTableModelDataSource(tblTransfer.getModel());
                    JasperDesign jd = JRXmlLoader.load(getClass().getResourceAsStream("reportTransfer.jrxml"));
                    JasperReport jr = JasperCompileManager.compileReport(jd);
                    JasperPrint jp = JasperFillManager.fillReport(jr, parameter, dataSource);
                    JasperViewer viewer = new JasperViewer(jp, false);
                    viewer.setVisible(true);
                }catch (Exception exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                }
            }
        });
    }
    public void showJenisDosen(String nama_jenis) {
        try {
            String functionCall = "SELECT * FROM dbo.getListJenisDosen(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, nama_jenis);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                String jnId = connection.result.getString("id_jenis_dosen");
                String jnNama = connection.result.getString("nama_jenis");
                cbJenisDosen.addItem(new ComboboxOption(jnId, jnNama));
            }

            connection.pstat.close();
            connection.result.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
    }
    public void addColumn() {
        tableModel.addColumn("id_dosen");
        tableModel.addColumn("nama_dosen");
        tableModel.addColumn("nama_bank");
        tableModel.addColumn("cabang_bank");
        tableModel.addColumn("kota");
        tableModel.addColumn("no_rekening");
        tableModel.addColumn("atasnama");
        tableModel.addColumn("total");

    }

    public static String formatTerbilang(int angka) {
        String[] ANGKA = {
                "", "satu ", "dua ", "tiga ", "empat ", "lima ", "enam ", "tujuh ", "delapan ", "sembilan ", "sepuluh ", "sebelas "
        };

        if (angka < 12) {
            return ANGKA[angka];
        }
        if (angka < 20) {
            return ANGKA[angka % 10] + "belas";
        }
        if (angka < 100) {
            return ANGKA[angka / 10] + "puluh " + ANGKA[angka % 10];
        }
        if (angka < 200) {
            return "seratus " + formatTerbilang(angka % 100);
        }
        if (angka < 1000) {
            return ANGKA[angka / 100] + "ratus " + formatTerbilang(angka % 100);
        }
        if (angka < 2000) {
            return "seribu " + formatTerbilang(angka % 1000);
        }
        if (angka < 1000000) {
            return formatTerbilang(angka / 1000) + "ribu " + formatTerbilang(angka % 1000);
        }
        if (angka < 1000000000) {
            return formatTerbilang(angka / 1000000) + "juta " + formatTerbilang(angka % 1000000);
        }

        return "Angka terlalu besar";
    }

    public void loadData(String tanggal_awal, String tanggal_akhir, String id_jenis_dosen) {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String functionCall = "SELECT * FROM dbo.getReportTransfer(?,?,?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, tanggal_awal);
            connection.pstat.setString(2, tanggal_akhir);
            connection.pstat.setString(3, id_jenis_dosen);

            connection.result = connection.pstat.executeQuery();
            System.out.println(tanggal_awal+"-"+tanggal_akhir+"-"+id_jenis_dosen);

            while (connection.result.next()) {
                Object[] obj = new Object[11];

                obj[0] = connection.result.getString("id_dosen");
                obj[1] = connection.result.getString("nama_dosen");
                obj[2] = connection.result.getString("nama_bank");
                obj[3] = connection.result.getString("cabang_bank");
                obj[4] = connection.result.getString("kota");
                obj[5] = connection.result.getString("no_rekening");
                obj[6] = connection.result.getString("atasnama");
                obj[7] = formatRupiah(connection.result.getDouble("total"));
                total_transfer += connection.result.getDouble("total");
                tableModel.addRow(obj);
            }

            connection.pstat.close();
            connection.result.close();

            TOTALTRANSFER.setText(String.valueOf(formatRupiah(total_transfer)));
        } catch (SQLException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
    }

    public static String getSemester(LocalDate endDate) {
        int month = endDate.getMonthValue();

        if (month > 3 && month < 8) {
            return "2"; // Semester 2: Maret - Juli
        } else {
            return "1"; // Semester 1: Agustus - Februari
        }
    }

    public static String getTahunAkademik(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();

        if (month >= 7) {
            return year + "/" + (year + 1); // Contoh: 2023/2024
        } else {
            return (year - 1) + "/" + year; // Contoh: 2022/2023
        }
    }

    public static String formatRupiah(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
        symbols.setCurrencySymbol("Rp. ");
        symbols.setMonetaryDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat formatter = new DecimalFormat("#,##0", symbols);
        return "Rp. "+formatter.format(amount);
    }

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
}
