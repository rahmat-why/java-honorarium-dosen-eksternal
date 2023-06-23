package ADMIN;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class Prodi extends JFrame {
    private JPanel JPProdi;
    private JTextField txtNamaProdi;
    private JTextField txtSingkatan;
    private JComboBox cmbTransport;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTextField txtSearch;
    private JTable tblProdi;
    private JTextField txtID;

    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();

    public Prodi() {

        connection = new DBConnect();
        setSize(500, 500);
        setTitle("Form Prodi");
        setContentPane(JPProdi);
        setLocationRelativeTo(null);

        tampilTransport();
        tableModel = new DefaultTableModel();
        tblProdi.setModel(tableModel);

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        addColumn();
        loadData();

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nmProdi = txtNamaProdi.getText();
                    String singkatan = txtSingkatan.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cmbTransport.getSelectedItem();
                    String transport = selectedOption.getValue().toString();

                    String procedureCall = "{CALL dbo.sp_CreateProdi(?, ?, ?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, nmProdi);
                    connection.pstat.setString(2, singkatan);
                    connection.pstat.setString(3, transport);

                    if (nmProdi.isEmpty() || singkatan.isEmpty() || transport.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return; // Menghentikan proses penyimpanan data jika validasi tidak terpenuhi
                    }

                    connection.pstat.execute();
                    connection.pstat.close();

                    loadData();
                    clear();

                    btnSave.setEnabled(true);
                    btnUpdate.setEnabled(false);
                    btnDelete.setEnabled(false);

                    JOptionPane.showMessageDialog(null, "Data Prodi berhasil disimpan!");

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam penyimpanan data prodi!");
                }
            }
        });

        tblProdi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblProdi.getSelectedRow();
                if (i == -1) {
                    return;
                }
                txtID.setText((String) tableModel.getValueAt(i, 0));
                txtNamaProdi.setText((String) tableModel.getValueAt(i, 1));
                txtSingkatan.setText((String) tableModel.getValueAt(i, 2));

                String transport = (String) tableModel.getValueAt(i, 3);
                for (int x = 0; x < cmbTransport.getItemCount(); x++) {
                    Object item = cmbTransport.getItemAt(x);
                    String jenisCb = ((ComboboxOption) item).getValue();
                    System.out.println("transport = "+transport+" jenisCb = "+jenisCb);
                    if (jenisCb.equals(transport)) {
                        cmbTransport.setSelectedItem(item);
                        break;
                    }
                }
                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);

            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = txtID.getText();
                    String nmProdi = txtNamaProdi.getText();
                    String singkatan = txtSingkatan.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cmbTransport.getSelectedItem();
                    String transport = selectedOption.getValue().toString();

                    if (nmProdi.isEmpty() || singkatan.isEmpty() || transport.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return; // Stop the data updating process if any field is empty
                    }

                    String procedureCall = "{CALL sp_UpdateProdi(?, ?, ?, ?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, id);
                    connection.pstat.setString(2, nmProdi);
                    connection.pstat.setString(3, singkatan);
                    connection.pstat.setString(4, transport);

                    connection.pstat.execute();
                    connection.pstat.close();

                    loadData();
                    clear();

                    btnSave.setEnabled(true);
                    btnDelete.setEnabled(false);
                    btnUpdate.setEnabled(false);

                    JOptionPane.showMessageDialog(null, "Data Prodi berhasil diUpdate!");

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam penyimpanan data prodi!");
                }
            }
        });

        txtNamaProdi.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == KeyEvent.VK_SPACE)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Nama hanya boleh diisi dengan huruf dan spasi!");
                }
            }
        });
        txtSingkatan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                int is_error = 0;
                if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && (c != KeyEvent.VK_BACK_SPACE)
                        && (c != KeyEvent.VK_PERIOD)) {
                    e.consume();
                    is_error = 1;
                }

                if (c == KeyEvent.VK_SPACE) {
                    e.consume();
                    is_error = 1;
                }

                if (is_error == 1) {
                    JOptionPane.showMessageDialog(null, "Singkatan hanya boleh diisi dengan huruf tanpa spasi!");
                }
            }
        });


        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                clear();
                btnSave.setEnabled(true);
                btnUpdate.setEnabled(false);
                btnDelete.setEnabled(false);
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id;
                    id = txtID.getText();
                    // Prepare the stored procedure call
                    String procedureCall = "{CALL dbo.sp_DeleteProdi(?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, id);

                    // Execute the stored procedure
                    connection.pstat.execute();

                    // Close the statement and connection
                    connection.pstat.close();

                    loadData();
                    clear();

                    btnDelete.setEnabled(true);
                    btnSave.setEnabled(false);
                    btnUpdate.setEnabled(false);


                    JOptionPane.showMessageDialog(null, "Success delete!");
                } catch (Exception exc) {
                    System.out.println("Error: "+exc.toString());

                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
                }
            }
        });
    }

    public void generateId() {
        try {
            String query = "SELECT dbo.GenerateProdiID() AS newId";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            // perbarui data
            while (connection.result.next()) {
                txtID.setText(connection.result.getString("newId"));
            }

            // Close the statement and connection
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }
    public void loadData() {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String query = "select * from prodi";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                Object[] obj = new Object[4]; // Menyesuaikan jumlah kolom dengan tabel tblPenyewa
                obj[0] = connection.result.getString("id_prodi");
                obj[1] = connection.result.getString("nama_prodi");
                obj[2] = connection.result.getString("singkatan");
                obj[3] = connection.result.getString("transport");

                tableModel.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }
    public void addColumn(){
        tableModel.addColumn("ID Prodi");
        tableModel.addColumn("Nama Prodi");
        tableModel.addColumn("Singkatan");
        tableModel.addColumn("Transport");
    }

    public void clear() {
        txtID.setText("Otomatis");
        txtNamaProdi.setText("");
        txtSingkatan.setText("");
    }

    public void tampilTransport() {
        cmbTransport.addItem(new ComboboxOption("PER HARI MENGAJAR", "PER HARI MENGAJAR"));
        cmbTransport.addItem(new ComboboxOption("PER SEKALI MENGAJAR", "PER SEKALI MENGAJAR"));
    }
    public static void main(String[]args){
        new Prodi().setVisible(true);
    }
}