package ADMIN;

import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class PerusahaanAstra extends JFrame {
    JPanel panelPerusahaanAstra;
    private JTextField txtNamaPerusahaan;
    private JTextField txtID;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JTextField txtSearch;
    private JTable tblPerusahaan;
    private JButton btnClear;

    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();

    String id_perusahaan, nama_perusahaan;

    public PerusahaanAstra(){
        tableModel = new DefaultTableModel();
        tblPerusahaan.setModel(tableModel);

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        addColumn();
        loadData(null);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    nama_perusahaan = txtNamaPerusahaan.getText();

                    if (nama_perusahaan.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_CreatePerusahaan(?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, nama_perusahaan);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(false);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data perusahaan dosen berhasil disimpan!");
                    }
                } catch (SQLException exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                }
            }
        });

        txtNamaPerusahaan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            char c = e.getKeyChar();
            if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && (c != KeyEvent.VK_BACK_SPACE)
                    && (c != KeyEvent.VK_SPACE) && (c != KeyEvent.VK_PERIOD)) {
                e.consume();
                JOptionPane.showMessageDialog(null, "Nama hanya boleh diisi dengan huruf!");
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

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();

                btnSave.setEnabled(true);
                btnUpdate.setEnabled(false);
                btnDelete.setEnabled(false);
            }
        });
        tblPerusahaan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblPerusahaan.getSelectedRow();
                if (i == -1) {
                    return;
                }

                txtID.setText((String) tableModel.getValueAt(i, 0));
                txtNamaPerusahaan.setText((String) tableModel.getValueAt(i, 1));

                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    id_perusahaan = txtID.getText();
                    nama_perusahaan = txtNamaPerusahaan.getText();

                    if (nama_perusahaan.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!");
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin memperbarui data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL sp_UpdatePerusahaan(?, ?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_perusahaan);
                        connection.pstat.setString(2, nama_perusahaan);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(true);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

                        JOptionPane.showMessageDialog(null, "Data Perusahaan berhasil diperbarui!");
                    }
                } catch (SQLException exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    id_perusahaan = txtID.getText();

                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String procedureCall = "{CALL dbo.sp_DeletePerusahaan(?)}";
                        connection.pstat = connection.conn.prepareCall(procedureCall);
                        connection.pstat.setString(1, id_perusahaan);

                        connection.pstat.execute();
                        connection.pstat.close();

                        loadData(null);
                        clear();

                        btnSave.setEnabled(false);
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

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();

                btnSave.setEnabled(true);
                btnUpdate.setEnabled(false);
                btnDelete.setEnabled(false);
            }
        });
    }

    public void loadData(String nama_perusahaan){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try{
            String functionCall = "SELECT * FROM dbo.getListPerusahaan(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, nama_perusahaan);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[2];
                obj[0] = connection.result.getString("id_perusahaan");
                obj[1] = connection.result.getString("nama_perusahaan");

                tableModel.addRow(obj);
            }

            connection.pstat.close();
            connection.result.close();
        }catch (Exception exc){
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
    }
    public void clear() {
        txtID.setText("Otomatis");
        txtNamaPerusahaan.setText("");
    }

    public void addColumn(){
        tableModel.addColumn("ID Perusahaan");
        tableModel.addColumn("Nama Perusahaan");
    }
}
