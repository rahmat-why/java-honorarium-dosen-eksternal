package DAAA;

import ADMIN.Dosen;
import LOGIN.Login;
import REPORT.ReportDosen;
import REPORT.ReportProdi;
import REPORT.ReportSlipGaji;
import REPORT.ReportTransfer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DAAAPage extends JFrame{
    private JButton logoutButton;
    private JButton absensiDosenButton;
    private JButton dosenButton;
    private JButton prodiButton;
    private JButton transferButton;
    private JPanel panelContent;
    private JPanel panelUtama;
    private JLabel labelNama;
    private JButton riwayatAbsensiDosenButton;
    private JLabel labelRole;
    private JButton slipGajiButton;
    String nama, role;
    public DAAAPage(String[] value) {
        add(this.panelUtama);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        labelNama.setText(value[2]);
        labelRole.setText(value[5]);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login();
                login.setVisible(true);
            }
        });
        absensiDosenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                Absensi absensi = new Absensi();
                absensi.JPAbsensi.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(absensi.JPAbsensi);
            }
        });
        dosenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                REPORT.ReportDosen reportDosen = new ReportDosen();
                reportDosen.panelReportDosen.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(reportDosen.panelReportDosen);
            }
        });
        prodiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                REPORT.ReportProdi reportProdi = new ReportProdi();
                reportProdi.panelReportProdi.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(reportProdi.panelReportProdi);
            }
        });
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                REPORT.ReportTransfer reportTransfer = new ReportTransfer();
                reportTransfer.panelReportTransfer.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(reportTransfer.panelReportTransfer);
            }
        });
        slipGajiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                REPORT.ReportSlipGaji reportSlipGaji = new ReportSlipGaji();
                reportSlipGaji.panelReportSlipGaji.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(reportSlipGaji.panelReportSlipGaji);
            }
        });
    }

}
