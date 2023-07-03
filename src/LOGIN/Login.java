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
    String id, nama, username, password, role;

    DBConnect connection = new DBConnect();

    public void FrameConfig() {
        add(this.PanelUtama);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public String[] validasi() {

        if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            JOptionPane.showMessageDialog(PanelUtama, "Username / Password Kosong", "Peringatan", JOptionPane.WARNING_MESSAGE);
        } else {
                try {
                    String functionCall = "SELECT * FROM VerifyUser(?, ?)";
                    connection.pstat = connection.conn.prepareStatement(functionCall);
                    connection.pstat.setString(1, txtUsername.getText());
                    connection.pstat.setString(2, txtPassword.getText());

                    connection.result = connection.pstat.executeQuery();


                    // Mengecheck isi query , apabila isi query kosong maka akan dilakukan perintah dibawahnya
                    if (!connection.result.next()) {
                        //Membuat kesalahan yang baru pada struktur try-catch
                        throw new Exception("User Not Found!");
                    }

                    // Mengambil isi query pada index ke - n
                    id = connection.result.getString(1);
                    nama = connection.result.getString(2);
                    username = connection.result.getString(3);
                    password = connection.result.getString(4);
                    role = connection.result.getString(5);

                    // Mengembalikan nilai validasi
                    return new String[]{"true", id, nama, username, password, role};

                } catch (Exception ex) {
                    //Menampilkan dialog pesan
                    System.out.println(".." + ex.getMessage());
                    JOptionPane.showMessageDialog(PanelUtama, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
            // Mengembalikan nilai validasi
            return new String[]{"false"};
        }

    public Login() {
        FrameConfig();
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mengisi array value dengan nilai yang dikembalikan dari metode validasi()
                String[] value = validasi();
                // Mengisi boolean valid dengan nilai dari index ke 0 value
                Boolean valid = Boolean.parseBoolean(value[0]);
                // Melakukan pengecheckan
                if (valid) {
                    // Menampilkan dialog pesan
                    JOptionPane.showMessageDialog(PanelUtama, "Welcome "+role+" to Honorer Dosen External", "Information", JOptionPane.INFORMATION_MESSAGE);

                    System.out.println("value 5: "+value[5]);
                    if (value[5].equals("ADMIN")) {
                        // menutup form yang sekarang
                        dispose();

                        // instantiasi form baru
                        AdminPage form = new AdminPage(value);

                        // menampilkan form
                        form.setVisible(true);
                    } else if (value[5].equals("DAAA")) {
                        // menutup form yang sekarang
                        dispose();

                        // instantiasi form baru
                        DAAAPage form = new DAAAPage(value);

                        // menampilkan form
                        form.setVisible(true);
                    }
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
