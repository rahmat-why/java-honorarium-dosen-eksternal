package ADMIN;

import COMPONENT.ComboboxOption;
import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class viewDosen extends JFrame {
    JPanel panelViewDosen;
    private JTextField txtSearch;
    private JButton btnAdd;
    private JTable tblDosen;
    DefaultTableModel tableModel;
    DBConnect connection = new DBConnect();
    public viewDosen() {
        tableModel = new DefaultTableModel();
        tblDosen.setModel(tableModel);

        addColumn();
        loadData(null);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popUp pop = new popUp(null, null,null,null,null,null,null,null,null);
                pop.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                pop.pack();
                pop.setLocationRelativeTo(null); // Center the form on the screen
                pop.setVisible(true);
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                loadData(txtSearch.getText());
            }
        });
        tblDosen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int i = tblDosen.getSelectedRow();
                if (i == -1) {
                    return;
                }
                String id_dosen = (String) tableModel.getValueAt(i, 0);
                String nama_dosen = (String) tableModel.getValueAt(i, 1);
                String email = (String) tableModel.getValueAt(i,2);
                String nama_bank = (String) tableModel.getValueAt(i, 4);
                String cabang_bank = (String) tableModel.getValueAt(i, 5);
                String no_rekening = (String) tableModel.getValueAt(i, 6);
                String npwp = (String) tableModel.getValueAt(i, 7);
                String atas_nama = (String) tableModel.getValueAt(i, 12);
                String kota = (String) tableModel.getValueAt(i, 13);
//                for (int x = 0; x < cbIDJenis.getItemCount(); x++) {
//                    Object item = cbIDJenis.getItemAt(x);
//                    String jenisCb = ((ComboboxOption) item).getDisplay();
//                    if (jenisCb.equals(jnID)) {
//                        cbIDJenis.setSelectedItem(item);
//                        break;
//                    }
//                }
                loadData(null);

                popUp pop = new popUp(id_dosen,nama_dosen,email,nama_bank,cabang_bank,no_rekening,npwp,atas_nama,kota);
                pop.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                pop.pack();
                pop.setLocationRelativeTo(null); // Center the form on the screen
                pop.setVisible(true);
            }
        });
    }
    public void addColumn(){
        tableModel.addColumn("ID Dosen");
        tableModel.addColumn("Nama Dosen");
        tableModel.addColumn("Email");
        tableModel.addColumn("Jenis Dosen");
        tableModel.addColumn("Nama Bank");
        tableModel.addColumn("Cabang Bank");
        tableModel.addColumn("No Rekening");
        tableModel.addColumn("NPWP");
        tableModel.addColumn("Tanggal Gabung Kampus");
        tableModel.addColumn("Tanggal Gabung Industri");
        tableModel.addColumn("Asal Perusahaan");
        tableModel.addColumn("Status");
        tableModel.addColumn("Atas Nama");
        tableModel.addColumn("Kota");
    }
    public void loadData(String nama_dosen) {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String functionCall = "SELECT * FROM dbo.getListDosen(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, nama_dosen);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[14]; // Menyesuaikan jumlah kolom dengan tabel tblPenyewa
                obj[0] = connection.result.getString("id_dosen");
                obj[1] = connection.result.getString("nama_dosen");
                obj[2] = connection.result.getString("email");
                obj[3] = connection.result.getString("nama_jenis");
                obj[4] = connection.result.getString("nama_bank");
                obj[5] = connection.result.getString("cabang_bank");
                obj[6] = connection.result.getString("no_rekening");
                obj[7] = connection.result.getString("npwp");
                obj[8] = connection.result.getDate("tanggal_gabung_kampus");
                obj[9] = connection.result.getDate("tanggal_gabung_industri");
                obj[10] = connection.result.getString("asal_perusahaan");
                obj[11] = connection.result.getString("status");
                obj[12] = connection.result.getString("atasnama");
                obj[13] = connection.result.getString("kota");

                tableModel.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan! Hubungi tim IT!");
        }
    }
    public static  void  main (String[] args){
        viewDosen view = new viewDosen();
    }
}

