package ADMIN;

import ADMIN.Dosen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPage extends JFrame{
    private JButton logoutButton;
    private JButton dashboardButton;
    private JButton pembuatanAkunButton;
    private JButton tableReferensiButton;
    private JButton dosenButton;
    private JButton programStudiButton;
    private JButton mataKuliahButton;
    private JButton kategoriDosenButton;
    private JPanel panelContent;
    private JPanel panelUtama;

    public AdminPage() {
        add(this.panelUtama);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        dashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        dosenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                Dosen dosen = new Dosen();
                dosen.panelDosen.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(dosen.panelDosen);
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
                LOGIN.Login form = new LOGIN.Login();
                form.setVisible(true);
            }
        });
    }
}
