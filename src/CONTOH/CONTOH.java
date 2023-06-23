package CONTOH;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CONTOH extends JFrame{
    private JPanel panelAdmin;
    private JButton CRUD1Button;
    private JButton CRUD2Button;
    private JPanel panelContent;
    private JButton LOGOUTButton;

    public CONTOH() {
        add(this.panelAdmin);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        CRUD1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                CRUD1 crud1 = new CRUD1();
                crud1.panelCrud1.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(crud1.panelCrud1);
            }
        });

        CRUD2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelContent.removeAll();
                panelContent.revalidate();
                panelContent.repaint();
                CRUD2 crud2 = new CRUD2();
                crud2.panelCrud2.setVisible(true);
                panelContent.revalidate();
                panelContent.setLayout(new java.awt.BorderLayout());
                panelContent.add(crud2.panelCrud2);
            }
        });
    }
}
