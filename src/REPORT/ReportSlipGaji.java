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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;

public class ReportSlipGaji extends JFrame{
    private JPanel JPReportProdi;
    private JButton btnCetak;
    private JPanel JPTanggalAkhir;
    private JPanel JPTanggalAwal;
    private JButton btnFilter;
    private JComboBox cbIdDosen;
    private JLabel labelNamaDosen;
    private JLabel labelMataKuliah;
    private JLabel labelProgramStudi;
    private JLabel labelJumlahSks;
    private JLabel labelInsentifKehadiranPerSks;
    private JLabel labelKompensasiMengajar;
    private JLabel labelTransportMengajar;
    private JLabel labelInsentifKehadiran;
    private JLabel labelTunjanganTax;
    private JLabel labelPph21;
    private JLabel labelTotalPph21;
    private JLabel labelTotalPendapatan;
    private JPanel JPReportSlipGaji;
    private JLabel labelTotalDibayarkan;
    private DBConnect connection = new DBConnect();

    public ReportSlipGaji() {
        setSize(500, 500);
        setTitle("Form Laporan Prodi");
        setContentPane(JPReportSlipGaji);
        setLocationRelativeTo(null);

        JDateChooser tanggalAwal = new JDateChooser();
        JPTanggalAwal.add(tanggalAwal);

        JDateChooser tanggalAkhir = new JDateChooser();
        JPTanggalAkhir.add(tanggalAkhir);

        showDosen(null);

        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Format formatTanggalAwal = new SimpleDateFormat("yyyy-MM-dd");
                String tanggal_awal = formatTanggalAwal.format(tanggalAwal.getDate());

                Format formatTanggalAkhir = new SimpleDateFormat("yyyy-MM-dd");
                String tanggal_akhir = formatTanggalAkhir.format(tanggalAkhir.getDate());

                ComboboxOption selectedJenisDosen = (ComboboxOption) cbIdDosen.getSelectedItem();
                String id_dosen = selectedJenisDosen.getValue();

                System.out.println(tanggal_awal+"-"+tanggal_akhir+"-"+id_dosen);

                showReportSlipGaji(id_dosen, tanggal_awal, tanggal_akhir);
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

                    parameter.put("PERIODE", tanggal_awal+" - "+tanggal_akhir);
                    parameter.put("NAMA", labelNamaDosen.getText());
                    parameter.put("MATA KULIAH", labelMataKuliah.getText());
                    parameter.put("PRODI", labelProgramStudi.getText());
                    parameter.put("JUMLAHSKS", labelJumlahSks.getText());
                    parameter.put("INSENTIFKEHADIRANPERSKS", labelInsentifKehadiranPerSks.getText());

                    parameter.put("kompensasi_mengajar", labelKompensasiMengajar.getText());
                    parameter.put("transport_mengajar", labelTransportMengajar.getText());
                    parameter.put("insentif_kehadiran", labelInsentifKehadiran.getText());
                    parameter.put("tunjangan_tax", labelPph21.getText());
                    parameter.put("pph21", labelPph21.getText());
                    parameter.put("total_pendapatan", labelTotalPendapatan.getText());
                    parameter.put("dibayarkan", labelTotalDibayarkan.getText());

                    JasperDesign jd = JRXmlLoader.load(getClass().getResourceAsStream("reportSlipGaji.jrxml"));
                    JasperReport jr = JasperCompileManager.compileReport(jd);
                    JasperPrint jp = JasperFillManager.fillReport(jr, parameter, new JREmptyDataSource());
                    JasperViewer viewer = new JasperViewer(jp, false);
                    viewer.setVisible(true);
                }catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        });
    }

    public void showReportSlipGaji(String id_dosen, String tanggal_awal, String tanggal_akhir) {
        try {
            String functionCall = "SELECT * FROM dbo.getReportSlipGaji(?,?,?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, id_dosen);
            connection.pstat.setString(2, tanggal_awal);
            connection.pstat.setString(3, tanggal_akhir);

            connection.result = connection.pstat.executeQuery();

            resetLabel();

            while (connection.result.next()) {
                labelNamaDosen.setText(connection.result.getString("nama_dosen"));
                labelMataKuliah.setText(connection.result.getString("list_matkul"));
                labelProgramStudi.setText(connection.result.getString("list_prodi"));
                labelJumlahSks.setText(connection.result.getString("jumlah_sks"));
                labelInsentifKehadiranPerSks.setText(formatRupiah(Double.parseDouble(connection.result.getString("insentif_kehadiran_per_sks"))));
                labelKompensasiMengajar.setText(formatRupiah(Double.parseDouble(connection.result.getString("kompensasi_mengajar"))));
                labelTransportMengajar.setText(formatRupiah(Double.parseDouble(connection.result.getString("transport_mengajar"))));
                labelInsentifKehadiran.setText(formatRupiah(Double.parseDouble(connection.result.getString("insentif_kehadiran"))));
                labelTunjanganTax.setText(formatRupiah(Double.parseDouble(connection.result.getString("tunjangan_pph21"))));
                labelPph21.setText(formatRupiah(Double.parseDouble(connection.result.getString("potongan_pph21"))));
                labelTotalPendapatan.setText(formatRupiah(Double.parseDouble(connection.result.getString("total_pendapatan"))));
                labelTotalPph21.setText(formatRupiah(Double.parseDouble(connection.result.getString("total_potongan"))));
                labelTotalDibayarkan.setText(formatRupiah(Double.parseDouble(connection.result.getString("total_dibayar"))));
            }

            connection.pstat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }

    public void resetLabel() {
        labelNamaDosen.setText("");
        labelMataKuliah.setText("");
        labelProgramStudi.setText("");
        labelJumlahSks.setText("");
        labelInsentifKehadiranPerSks.setText("");
        labelKompensasiMengajar.setText("");
        labelTransportMengajar.setText("");
        labelInsentifKehadiran.setText("");
        labelTunjanganTax.setText("");
        labelPph21.setText("");
        labelTotalPendapatan.setText("");
        labelTotalPph21.setText("");
        labelTotalDibayarkan.setText("");
    }

    public void showDosen(String filter_nama_dosen) {
        try {
            String functionCall = "SELECT * FROM dbo.getListDosen(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, filter_nama_dosen);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                String id_dosen = connection.result.getString("id_dosen");
                String nama_dosen = connection.result.getString("nama_dosen");
                cbIdDosen.addItem(new ComboboxOption(id_dosen, nama_dosen));
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

    public static void main(String[]args){
        new ReportSlipGaji().setVisible(true);
    }
}
