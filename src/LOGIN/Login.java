package LOGIN;

import Connection.DBConnect;

import javax.swing.*;
import java.awt.event.*;

import ADMIN.AdminPage;
import DAAA.DAAAPage;

public class Login extends JFrame {
    private JPanel PanelUtama;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton loginButton;
    private JCheckBox showPasswordCheckBox;
    String id_user, nama, username, password, role;

    DBConnect connection = new DBConnect();

    public void FrameConfig() {
        add(this.PanelUtama);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public ADTUser verifyUser() {
        try {
            String functionCall = "SELECT * FROM VerifyUser(?, ?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, txtUsername.getText());
            connection.pstat.setString(2, txtPassword.getText());

            connection.result = connection.pstat.executeQuery();

            if (!connection.result.next()) {
                return null;
            }

            id_user = connection.result.getString("id_user");
            nama = connection.result.getString("nama");
            username = connection.result.getString("username");
            password = connection.result.getString("password");
            role = connection.result.getString("role");

            ADTUser adtUser = new ADTUser(id_user, nama, username, password, role);

            return adtUser;
        } catch (Exception exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim TI!");
        }

        return null;
    }

    public Login() {
        FrameConfig();
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(PanelUtama, "Akun tidak ditemukan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                ADTUser verifyUser = verifyUser();
                if (verifyUser == null){
                    JOptionPane.showMessageDialog(PanelUtama, "Akun tidak ditemukan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(PanelUtama, "Selamat datang "+verifyUser.getNama()+"-"+verifyUser.getRole(), "Peringatan", JOptionPane.INFORMATION_MESSAGE);
                if(verifyUser.getRole().equals("ADMIN")){
                    dispose();
                    AdminPage form = new AdminPage(verifyUser);
                    form.setVisible(true);
                }else{
                    dispose();
                    DAAAPage form = new DAAAPage(verifyUser);
                    form.setVisible(true);
                }
            }
        });
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected() == true) {
                    txtPassword.setEchoChar('\0');
                } else {
                    txtPassword.setEchoChar('*');
                }
            }
        });
    }
}
