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

public class ReportProdi extends JFrame{
    public JPanel panelReportProdi;
    private JTable tableReportProdi;
    private JButton btnCetak;
    private JButton btnFilter;
    private JComboBox cbJenisDosen;
    private JPanel JPTanggalAwal;
    private JPanel JPTanggalAkhir;

    DefaultTableModel tableModel;

    private DBConnect connection;

    public ReportProdi() {
        setSize(500, 500);
        setTitle("Form Laporan Prodi");
        setContentPane(panelReportProdi);
        setLocationRelativeTo(null);

        connection = new DBConnect();

        JDateChooser tanggalAwal = new JDateChooser();
        JPTanggalAwal.add(tanggalAwal);

        JDateChooser tanggalAkhir = new JDateChooser();
        JPTanggalAkhir.add(tanggalAkhir);

        showJenisDosen(null);

        tableModel = new DefaultTableModel();
        tableReportProdi.setModel(tableModel);
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
                    parameter.put("TAHUNAKADEMIK", getTahunAkademik(LocalDate.parse(tanggal_awal_semester)));

                    parameter.put("PERIODE", tanggal_awal+" - "+tanggal_akhir);
                    parameter.put("PREPARED_BY", "Rahmat");
                    parameter.put("CHECKED_BY", "Kepala DAAA");

                    JRDataSource dataSource = new JRTableModelDataSource(tableReportProdi.getModel());
                    JasperDesign jd = JRXmlLoader.load(getClass().getResourceAsStream("reportProdi.jrxml"));
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

    public static void main(String[]args){
        new ReportProdi().setVisible(true);
    }

    public void addColumn() {
        tableModel.addColumn("singkatan");
        tableModel.addColumn("gross_up");
        tableModel.addColumn("pph21");
        tableModel.addColumn("net_income");
    }

    public void loadData(String tanggal_awal, String tanggal_akhir, String id_jenis_dosen) {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String functionCall = "SELECT * FROM dbo.getReportProdi(?,?,?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, tanggal_awal);
            connection.pstat.setString(2, tanggal_akhir);
            connection.pstat.setString(3, id_jenis_dosen);

            connection.result = connection.pstat.executeQuery();
            System.out.println(tanggal_awal+"-"+tanggal_akhir+"-"+id_jenis_dosen);

            while (connection.result.next()) {
                Object[] obj = new Object[4];

                obj[0] = connection.result.getString("singkatan");
                obj[1] = formatRupiah(connection.result.getDouble("gross_up"));
                obj[2] = formatRupiah(connection.result.getDouble("pph21"));
                obj[3] = formatRupiah(connection.result.getDouble("net_income"));
                tableModel.addRow(obj);
            }

            connection.pstat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
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
}
