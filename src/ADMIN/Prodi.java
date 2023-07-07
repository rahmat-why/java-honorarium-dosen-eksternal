package ADMIN;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class Prodi extends JFrame {
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
    JPanel panelProdi;
    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();
    String id_prodi, nama_prodi, singkatan, transport;

    public Prodi() {
        tampilTransport();
        tableModel = new DefaultTableModel();
        tblProdi.setModel(tableModel);

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        addColumn();
        loadData(null);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    nama_prodi = txtNamaProdi.getText();
                    singkatan = txtSingkatan.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cmbTransport.getSelectedItem();
                    transport = selectedOption.getValue().toString();

                    if (nama_prodi.isEmpty() || singkatan.isEmpty() || transport.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_CreateProdi(?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, nama_prodi);
                        connection.pstat.setString(2, singkatan);
                        connection.pstat.setString(3, transport);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data Prodi berhasil disimpan!");
                    }

                } catch (SQLException exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
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
                String cb_transport = ((ComboboxOption) item).getValue();
                if (cb_transport.equals(transport)) {
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
                    id_prodi = txtID.getText();
                    nama_prodi = txtNamaProdi.getText();
                    singkatan = txtSingkatan.getText();

                    ComboboxOption selectedOption = (ComboboxOption) cmbTransport.getSelectedItem();
                    transport = selectedOption.getValue().toString();

                    if (nama_prodi.isEmpty() || singkatan.isEmpty() || transport.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin mengupdate data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL sp_UpdateProdi(?, ?, ?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_prodi);
                        connection.pstat.setString(2, nama_prodi);
                        connection.pstat.setString(3, singkatan);
                        connection.pstat.setString(4, transport);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnDelete.setEnabled(false);
                        btnUpdate.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data Prodi berhasil diupdate!");
                    }
                } catch (SQLException exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                }
            }
        });

        txtNamaProdi.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            char c = e.getKeyChar();
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == KeyEvent.VK_SPACE || c == KeyEvent.VK_BACK_SPACE)) {
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
                    id_prodi = txtID.getText();

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_DeleteProdi(?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_prodi);

                        connection.pstat.execute();

                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                }
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            loadData(txtSearch.getText());
            }
        });
    }

    public void loadData(String nama_prodi) {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String functionCall = "SELECT * FROM dbo.getListProdi(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, nama_prodi);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[4];
                obj[0] = connection.result.getString("id_prodi");
                obj[1] = connection.result.getString("nama_prodi");
                obj[2] = connection.result.getString("singkatan");
                obj[3] = connection.result.getString("transport");

                tableModel.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
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
}