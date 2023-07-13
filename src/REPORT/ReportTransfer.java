package REPORT;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;
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
import java.util.HashMap;
import java.util.Locale;

public class ReportTransfer extends JFrame{
    private JPanel JPReportTransfer;
    private JTable tableReportTransfer;
    private JButton btnCetak;
    private JPanel JPTanggalAkhir;
    private JPanel JPTanggalAwal;
    private JButton btnFilter;
    private JComboBox cbJenisDosen;
    private JLabel TOTALTRANSFER;
    public JPanel panelReportTransfer;

    DefaultTableModel tableModel;

    private DBConnect connection;

    private double total_transfer = 0;

    public ReportTransfer() {

        connection = new DBConnect();

        JDateChooser tanggalAwal = new JDateChooser();
        JPTanggalAwal.add(tanggalAwal);

        JDateChooser tanggalAkhir = new JDateChooser();
        JPTanggalAkhir.add(tanggalAkhir);

        showJenisDosen(null);

        tableModel = new DefaultTableModel();
        tableReportTransfer.setModel(tableModel);
        addColumn();
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

                    parameter.put("PERIODE", tanggal_awal+" - "+tanggal_akhir);
                    parameter.put("TOTALTRANSFER", formatRupiah(total_transfer));
                    parameter.put("TERBILANG", formatTerbilang((int) total_transfer));
                    parameter.put("MEMBUAT", "Rahmat");
                    parameter.put("MENYETUJUI", "Agung Kaswadi, S.T., M.T.");
                    parameter.put("MENGETAHUI", "Agung Kurniawan");

                    JRDataSource dataSource = new JRTableModelDataSource(tableReportTransfer.getModel());
                    JasperDesign jd = JRXmlLoader.load(getClass().getResourceAsStream("reportTransfer.jrxml"));
                    JasperReport jr = JasperCompileManager.compileReport(jd);
                    JasperPrint jp = JasperFillManager.fillReport(jr, parameter, dataSource);
                    JasperViewer viewer = new JasperViewer(jp, false);
                    viewer.setVisible(true);
                }catch (Exception ex) {
                    System.out.println(ex.toString());
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
            System.out.println("Error: " + exc.toString());
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

    public String formatTerbilang(int number) {
        String[] ones = {"", "satu", "dua", "tiga", "empat", "lima", "enam", "tujuh", "delapan", "sembilan"};
        String[] tens = {"", "sepuluh", "dua puluh", "tiga puluh", "empat puluh", "lima puluh", "enam puluh", "tujuh puluh", "delapan puluh", "sembilan puluh"};
        String[] thousands = {"ribu"};

        String[] millions = {"juta"};

        String[] billions = {"milyar"};

        if (number < 0) {
            return "minus " + formatTerbilang(-number);
        }

        if (number < 10) {
            return ones[number];
        }

        if (number < 20) {
            return tens[number - 10];
        }

        if (number < 100) {
            return tens[number / 10] + (number % 10 != 0 ? " " : "") + ones[number % 10];
        }

        if (number < 1000) {
            return ones[number / 100] + " ratus" + (number % 100 != 0 ? " " : "") + formatTerbilang(number % 100);
        }

        if (number < 1000000) {
            return formatTerbilang(number / 1000) + " ribu" + (number % 1000 != 0 ? " " : "") + formatTerbilang(number % 1000);
        }

        if (number < 1000000000) {
            return formatTerbilang(number / 1000000) + " juta" + (number % 1000000 != 0 ? " " : "") + formatTerbilang(number % 1000000);
        }

        if (number < Integer.MAX_VALUE) {
            return formatTerbilang(number / 1000000000) + " milyar" + (number % 1000000000 != 0 ? " " : "") + formatTerbilang(number % 1000000000);
        }

        return "";
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
            System.out.println("Error: " + exc.toString());
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

    public static void main(String[]args){
        new ReportTransfer().setVisible(true);
    }
}
