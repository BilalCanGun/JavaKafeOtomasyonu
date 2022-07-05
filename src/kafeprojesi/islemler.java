package kafeprojesi;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.management.Query;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.security.PublicKey;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;

public class islemler extends JFrame {

	private JPanel contentPane;
	private DefaultListModel listModel, listModel2;
	private JLabel lblToplam;
	private Connection con;
	private ResultSet rs;

	private void openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/proje";
			con = (Connection) DriverManager.getConnection(url, "root", "");
			System.out.println("baglandi!");
		} catch (SQLException e) {
			System.out.println("sql hata!" + e.toString());
		} catch (ClassNotFoundException e) {
			System.out.println("hata!" + e.toString());
		}
	}

	private void closeConnection() {
		try {
			con.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void fillToSepet() {
		ArrayList<Integer> arrList = new ArrayList<Integer>();
		ArrayList<String> strArrList = new ArrayList<String>();
		try {

			String query = "SELECT urunid FROM `sepet` WHERE masaid = " + helper.currentMasaId;
			Statement st = (Statement) con.createStatement();
			rs = (ResultSet) st.executeQuery(query);

			while (rs.next()) {
				int gecici = rs.getInt("urunid");
				arrList.add(gecici);
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		try {
			Statement st = (Statement) con.createStatement();
			for (int i = 0; i < arrList.size(); i++) {
				rs = (ResultSet) st.executeQuery("SELECT urunisim FROM urunler WHERE urunid = " + arrList.get(i));
				rs.next();
				String isim = rs.getString("urunisim");
				listModel2.addElement(isim);

			}
			st.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	private void toplamFiyat() {
		try {
			int top = 0;
			openConnection();
			Statement st = (Statement) con.createStatement();
			rs = (ResultSet) st
					.executeQuery("SELECT * FROM adisyonlar where masaid='"
							+ helper.currentMasaId + "'");
			while (rs.next()) {
				top += rs.getInt("topfiyat");
			}
			st.close();
			closeConnection();
			lblToplam.setText(String.valueOf(top));

		} catch (Exception er) {
			er.printStackTrace();
		}
	}
	
	private void sepetTemizle() {
		// burasý databaseden masa idsi ile veriyi siliyor
		try {
			openConnection();
			String query = "Delete from sepet where masaid='" + helper.currentMasaId + "'";
			Statement st = (Statement) con.createStatement();
			PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
			preparedStmt.executeUpdate();
			st.close();
			closeConnection();
		} catch (Exception er) {
			er.printStackTrace();
		}
		// listeden siliyor
		listModel2.removeAllElements();
	}

	private void inserturunler() {

		try {

			String query = "SELECT * FROM `urunler`";
			Statement st = (Statement) con.createStatement();
			rs = (ResultSet) st.executeQuery(query);

			while (rs.next()) {
				String urunadi = rs.getString("urunisim");
				listModel.addElement(urunadi);
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					islemler frame = new islemler();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public islemler() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 515, 359);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		listModel2 = new DefaultListModel();
		JList masalist = new JList(listModel2);
		masalist.setBounds(29, 60, 156, 251);
		contentPane.add(masalist);

		listModel = new DefaultListModel();
		JList urunlerlist = new JList(listModel);
		urunlerlist.setBounds(305, 60, 161, 251);
		contentPane.add(urunlerlist);

		JLabel masalarlabel = new JLabel("Masa sepeti");
		masalarlabel.setBounds(29, 35, 156, 14);
		contentPane.add(masalarlabel);

		JLabel urunlerlabel = new JLabel("\u00DCr\u00FCnler");
		urunlerlabel.setBounds(305, 35, 161, 14);
		contentPane.add(urunlerlabel);

		JButton btnEkle = new JButton("Ekle");
		btnEkle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int tmp = urunlerlist.getSelectedIndex();
				String str = listModel.get(tmp).toString();
				// listModel2.addElement(str);
			
				try {
					openConnection();
					// String query = "SELECT * FROM items WHERE item='"+str+'"
					Statement st = (Statement) con.createStatement();
					rs = (ResultSet) st.executeQuery("SELECT * FROM urunler WHERE urunisim='" + str + "'");
					rs.next();
					String urunadi = rs.getString("urunisim");
					int urunid = rs.getInt("urunid");
					int urunfiyat=rs.getInt("urunfiyat");
					listModel2.addElement(urunadi);
					st.close();
					closeConnection();
					openConnection();
					String query = " insert into sepet (masaid, urunid)" + " values (?, ?)";
					PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
					preparedStmt.setInt(1, helper.currentMasaId);
					preparedStmt.setInt(2, urunid);
					
					String query2 = " insert into adisyonlar (masaid, topfiyat,urunid)" + " values (?, ?, ?)";
					PreparedStatement preparedStmt2 = (PreparedStatement) con.prepareStatement(query2);
					preparedStmt2.setInt(1, helper.currentMasaId);
					preparedStmt2.setInt(2,urunfiyat);
					preparedStmt2.setInt(3,urunid);
					
					
						
					// execute the preparedstatement
					preparedStmt2.execute();
					preparedStmt.execute();

					con.close();

				} catch (Exception er) {
					er.printStackTrace();
				}
				toplamFiyat();

			}
		});
		btnEkle.setBounds(195, 98, 97, 23);
		contentPane.add(btnEkle);

		JButton btnCikar = new JButton("Sil");
		btnCikar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int tmp = masalist.getSelectedIndex();
				String str = listModel2.get(tmp).toString();
				try {
					openConnection();
					Statement st = (Statement) con.createStatement();
					rs = (ResultSet) st.executeQuery(
							"SELECT * FROM sepet WHERE(urunid=(SELECT (urunid) FROM urunler WHERE urunisim='" + str
									+ "') AND masaid=" + helper.currentMasaId + ")");
					rs.next();
					int sepetId = rs.getInt("sepetid");
					rs = (ResultSet) st.executeQuery(
							"SELECT * FROM adisyonlar WHERE(urunid=(SELECT (urunid) FROM urunler WHERE urunisim='" + str
									+ "') AND masaid=" + helper.currentMasaId + ")");
					rs.next();
					int adisyonId=rs.getInt("adisyonid");
					st.close();
					closeConnection();
					openConnection();
					String query = " DELETE FROM sepet WHERE sepetid =" + sepetId;
					PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
					preparedStmt.executeUpdate();

					// execute the preparedstatement
					System.out.print(preparedStmt.executeUpdate());
					query = " DELETE FROM adisyonlar WHERE adisyonid =" + adisyonId;
					preparedStmt = (PreparedStatement) con.prepareStatement(query);
					preparedStmt.executeUpdate();
					
					closeConnection();
					toplamFiyat();

					listModel2.remove(tmp);

				} catch (Exception er) {
					er.printStackTrace();
				}

			}
		});
		btnCikar.setBounds(195, 147, 97, 23);
		contentPane.add(btnCikar);

		JButton btnHesap = new JButton("Hesap");
		btnHesap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				//databasedeki sepet tablosunu temizledi
				
			    sepetTemizle();
			
			
			//databasedeki adisyonu temizledi
			
			try 
			{
				openConnection();
				Statement st = (Statement) con.createStatement();
				String query = " DELETE FROM adisyonlar WHERE masaid =" + helper.currentMasaId;
				PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
				preparedStmt.executeUpdate();
				
			} catch (Exception e2) {
				
			}
			
			//fizikseli temizledi
			listModel2.removeAllElements();
			//messagebox
			JOptionPane.showMessageDialog(null,"Hesabýnýz:"+lblToplam.getText()+" TL");

			}
		});
		btnHesap.setBounds(195, 233, 97, 78);
		contentPane.add(btnHesap);

		JButton btngeri = new JButton("Geri");
		btngeri.setForeground(Color.DARK_GRAY);
		btngeri.setFont(new Font("Tahoma", Font.BOLD, 15));
		btngeri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				masapanel m1 = new masapanel();
				m1.setVisible(true);
				dispose();
			}
		});
		btngeri.setBounds(203, 11, 89, 23);
		contentPane.add(btngeri);

		JLabel lblMasa = new JLabel("New label");
		lblMasa.setForeground(Color.RED);
		lblMasa.setHorizontalAlignment(SwingConstants.CENTER);
		lblMasa.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMasa.setBounds(29, 0, 156, 23);
		contentPane.add(lblMasa);
		String masaId = Integer.toString(helper.currentMasaId);
		
		lblMasa.setText(masaIsim());

		lblToplam = new JLabel("Fiyat");
		lblToplam.setBounds(231, 210, 47, 14);
		toplamFiyat();
		contentPane.add(lblToplam);
		
		JLabel lblNewLabel = new JLabel("\u20BA ");
		lblNewLabel.setBounds(221, 210, 14, 14);
		contentPane.add(lblNewLabel);
		openConnection();
		inserturunler();
		fillToSepet();
		closeConnection();

	}

	private String masaIsim() {
		String str="";
		try {
			openConnection();
			String query = "SELECT * FROM masalar where masaid="+helper.currentMasaId;
			Statement st = (Statement) con.createStatement();
			rs = (ResultSet) st.executeQuery(query);
			rs.next();
			str=rs.getString("masaisim");
			st.close();
			closeConnection();
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return str;
	}
	
}
