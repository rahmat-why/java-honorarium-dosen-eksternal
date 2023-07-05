package DAAA;

import ADMIN.Dosen;
import LOGIN.Login;
import REPORT.*;

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
                Absen absen = new Absen();
                absen.panelAbsen.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(absen.panelAbsen);
            }
        });
        dosenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                REPORT.dosenReport dosen = new dosenReport();
                dosen.panelDosenReport.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(dosen.panelDosenReport);
            }
        });
        prodiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                REPORT.prodiReport prodi = new prodiReport();
                prodi.panelProdiReport.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(prodi.panelProdiReport);
            }
        });
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                REPORT.transferReport transfer = new transferReport();
                transfer.panelTransfer.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(transfer.panelTransfer);
            }
        });
        slipGajiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                REPORT.gajiReport gaji = new gajiReport();
                gaji.panelGaji.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(gaji.panelGaji);
            }
        });
    }

}
