
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ACER
 */
public class FrameCekCuaca extends javax.swing.JFrame {

    private void loadFromCSV() {
        // Pilih file CSV menggunakan JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih File CSV");

        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return; // Jika pengguna membatalkan pemilihan file
        }

        File fileToOpen = fileChooser.getSelectedFile();
        String filePath = fileToOpen.getAbsolutePath();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            DefaultTableModel model = (DefaultTableModel) jTableSemua.getModel();
            model.setRowCount(0); // Hapus baris yang ada sebelum memuat data baru

            String line;
            boolean isFirstLine = true;

            // Membaca setiap baris dari file CSV
            while ((line = reader.readLine()) != null) {
                // Skip header
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Pisahkan kolom berdasarkan koma (csv)
                String[] data = line.split(",");

                // Menambahkan data ke dalam model tabel
                model.addRow(data);
            }

            // Menampilkan konfirmasi bahwa data telah dimuat
            JOptionPane.showMessageDialog(this, "Data berhasil dimuat dari " + filePath, "Sukses", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            // Menampilkan dialog error jika terjadi kesalahan saat memuat file CSV
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveToCSV() {
        // Pilih lokasi dan nama file CSV menggunakan JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Data Cuaca");
        fileChooser.setSelectedFile(new File("data_cuaca.csv")); // Nama file default

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return; // Jika pengguna membatalkan penyimpanan
        }

        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();

        // Pastikan file berakhiran .csv
        if (!filePath.endsWith(".csv")) {
            filePath += ".csv";
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            DefaultTableModel model = (DefaultTableModel) jTableSemua.getModel();
            int rowCount = model.getRowCount();
            int columnCount = model.getColumnCount();

            // Menulis header (kolom) ke file CSV
            for (int i = 0; i < columnCount; i++) {
                writer.write(model.getColumnName(i));
                if (i < columnCount - 1) {
                    writer.write(",");
                }
            }
            writer.newLine();

            // Menulis data setiap baris ke file CSV
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    writer.write(String.valueOf(model.getValueAt(row, col)));
                    if (col < columnCount - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }

            // Menampilkan konfirmasi bahwa data telah disimpan
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan ke " + filePath, "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            // Menampilkan dialog error jika terjadi kesalahan saat menyimpan
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cekCuaca() {
        String apiKey = "e57367f6bf9f06154278b83d65a4a557"; // Ganti dengan API Key Anda
        String kota = jKota.getText().trim(); // Ambil teks dari jKota
        if (kota.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Masukkan nama kota terlebih dahulu!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + kota + "&units=metric&appid=" + apiKey;

        try {
            // Membuat URL dan membuka koneksi
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Periksa status respons
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Baca respons dari API
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parsing JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                String kondisi = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("main");
                String detail = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
                String iconCode = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("icon");
                double suhu = jsonResponse.getJSONObject("main").getDouble("temp");

                // Update UI
                jLabelKondisi.setText("Kondisi: " + kondisi);
                jLabelKet.setText("Detail: " + detail);
                jLabelSuhu.setText("Suhu: " + suhu + "°C");

                // Tampilkan ikon cuaca
                String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                ImageIcon icon = new ImageIcon(new URL(iconUrl));
                jLabelIcon.setIcon(icon);

                // Tambahkan kota ke jKotaFavorit jika belum ada
                if (!isKotaInComboBox(kota)) {
                    jKotaFavorit.addItem(kota);
                }

                // Tambahkan data ke tabel
                DefaultTableModel model = (DefaultTableModel) jTableSemua.getModel();
                model.addRow(new Object[]{kota, kondisi, detail, suhu + "°C"});

            } else {
                // Tampilkan pesan kesalahan dalam dialog
                JOptionPane.showMessageDialog(this,
                        "Gagal mengambil data cuaca. Periksa koneksi atau nama kota.\nKode respons: " + responseCode,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            // Tampilkan pesan kesalahan exception dalam dialog
            JOptionPane.showMessageDialog(this,
                    "Terjadi kesalahan:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

// Helper method to check if kota is already in the ComboBox
    private boolean isKotaInComboBox(String kota) {
        for (int i = 0; i < jKotaFavorit.getItemCount(); i++) {
            if (jKotaFavorit.getItemAt(i).equalsIgnoreCase(kota)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cek apakah nama kota sudah ada di jComboBox
     *
     * @param kota Nama kota yang ingin dicek
     * @return true jika kota sudah ada, false jika belum
     */
    /**
     * Creates new form FrameCekCuaca
     */
    public FrameCekCuaca() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelIcon = new javax.swing.JLabel();
        jLabelKondisi = new javax.swing.JLabel();
        jLabelKet = new javax.swing.JLabel();
        jLabelSuhu = new javax.swing.JLabel();
        jKota = new javax.swing.JTextField();
        jCek = new javax.swing.JButton();
        jKotaFavorit = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableSemua = new javax.swing.JTable();
        jLoad = new javax.swing.JButton();
        jSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Kota");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(6, 13, 6, 13);
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Pilih Kota Favorit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(6, 13, 6, 13);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabelIcon.setText("icon");
        jLabelIcon.setPreferredSize(new java.awt.Dimension(64, 64));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(6, 13, 6, 13);
        jPanel1.add(jLabelIcon, gridBagConstraints);

        jLabelKondisi.setText("Kondisi...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(6, 13, 6, 13);
        jPanel1.add(jLabelKondisi, gridBagConstraints);

        jLabelKet.setText("Keterangan Cuaca");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(6, 13, 6, 13);
        jPanel1.add(jLabelKet, gridBagConstraints);

        jLabelSuhu.setText("Suhu....");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(6, 13, 6, 13);
        jPanel1.add(jLabelSuhu, gridBagConstraints);

        jKota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jKotaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 13, 6, 13);
        jPanel1.add(jKota, gridBagConstraints);

        jCek.setText("Cek");
        jCek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCekActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 17, 6, 17);
        jPanel1.add(jCek, gridBagConstraints);

        jKotaFavorit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jakarta" }));
        jKotaFavorit.setSelectedIndex(-1);
        jKotaFavorit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jKotaFavoritItemStateChanged(evt);
            }
        });
        jKotaFavorit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jKotaFavoritActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 13, 6, 13);
        jPanel1.add(jKotaFavorit, gridBagConstraints);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(452, 200));

        jTableSemua.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kota", "Kondisi Cuaca", "Detail Cuaca", "Suhu "
            }
        ));
        jScrollPane1.setViewportView(jTableSemua);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(6, 13, 6, 13);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jLoad.setText("Load");
        jLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLoadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(6, 13, 6, 13);
        jPanel1.add(jLoad, gridBagConstraints);

        jSave.setText("Save");
        jSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(6, 13, 6, 13);
        jPanel1.add(jSave, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jKotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jKotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jKotaActionPerformed

    private void jCekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCekActionPerformed
        cekCuaca();        // TODO add your handling code here:
    }//GEN-LAST:event_jCekActionPerformed

    private void jKotaFavoritItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jKotaFavoritItemStateChanged
        jKota.setText(jKotaFavorit.getSelectedItem().toString());
        cekCuaca();// TODO add your handling code here:
    }//GEN-LAST:event_jKotaFavoritItemStateChanged

    private void jKotaFavoritActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jKotaFavoritActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jKotaFavoritActionPerformed

    private void jSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveActionPerformed
        saveToCSV();        // TODO add your handling code here:
    }//GEN-LAST:event_jSaveActionPerformed

    private void jLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLoadActionPerformed
        loadFromCSV();        // TODO add your handling code here:
    }//GEN-LAST:event_jLoadActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameCekCuaca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameCekCuaca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameCekCuaca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameCekCuaca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameCekCuaca().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jCek;
    private javax.swing.JTextField jKota;
    private javax.swing.JComboBox<String> jKotaFavorit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelIcon;
    private javax.swing.JLabel jLabelKet;
    private javax.swing.JLabel jLabelKondisi;
    private javax.swing.JLabel jLabelSuhu;
    private javax.swing.JButton jLoad;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jSave;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableSemua;
    // End of variables declaration//GEN-END:variables
}
