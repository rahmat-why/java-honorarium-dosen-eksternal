package ADMIN;

import LOGIN.ADTUser;
import LOGIN.Login;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPage extends JFrame{
    private JButton logoutButton;
    private JButton pembuatanAkunButton;
    private JButton dosenButton;
    private JButton programStudiButton;
    private JButton mataKuliahButton;
    private JButton kategoriDosenButton;
    private JPanel panelContent;
    private JPanel panelUtama;
    private JLabel labelNama;
    private JLabel labelRole;
    private JButton golonganButton;
    private JButton perusahaanButton;

    public AdminPage(ADTUser verifyUser) {
        add(this.panelUtama);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        labelNama.setText(verifyUser.getNama());
        labelRole.setText(verifyUser.getRole());

        dosenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                viewDosen view = new viewDosen();
                view.panelViewDosen.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(view.panelViewDosen);
            }
        });
        kategoriDosenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                JenisDosen jenisDosen = new JenisDosen();
                jenisDosen.panelJenisDosen.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(jenisDosen.panelJenisDosen);
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LOGIN.Login login = new Login();
                login.setVisible(true);
            }
        });
        programStudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                Prodi prodi = new Prodi();
                prodi.panelProdi.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(prodi.panelProdi);
            }
        });
        mataKuliahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                Matkul matkul = new Matkul();
                matkul.panelMatkul.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(matkul.panelMatkul);
            }
        });
        pembuatanAkunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                User user = new User();
                user.panelUser.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(user.panelUser);
            }
        });
        golonganButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                GolonganDosen golonganDosen = new GolonganDosen();
                golonganDosen.panelGolongan.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(golonganDosen.panelGolongan);
            }
        });
        perusahaanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                PerusahaanAstra perusahaanAstra = new PerusahaanAstra();
                perusahaanAstra.panelPerusahaanAstra.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(perusahaanAstra.panelPerusahaanAstra);
            }
        });
    }
}
