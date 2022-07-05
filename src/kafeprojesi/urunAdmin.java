package kafeprojesi;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class urunAdmin extends JFrame {

	private JPanel contentPane;
	private Connection con;
	private ResultSet res;
	private JTextField textisim;
	private JTextField textfiyat;
	private DefaultListModel listModel;
	private JList urunlist;
	private JButton btnSil;
	
	
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
	private void insert()
	  {
		  
	    try
	    {
	    	
	      String query = "SELECT * FROM `urunler`";
	      Statement st = (Statement) con.createStatement();
	      res = (ResultSet) st.executeQuery(query);

	      while (res.next())
	      {
	        String urunAdi = res.getString("urunisim");
	        listModel.addElement(urunAdi);
	      }
	      st.close();
	    }
	    catch (Exception e)
	    {
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
					urunAdmin frame = new urunAdmin();
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
	public urunAdmin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 583, 373);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textisim = new JTextField();
		textisim.setBounds(10, 105, 138, 28);
		contentPane.add(textisim);
		textisim.setColumns(10);
		
		textfiyat = new JTextField();
		textfiyat.setColumns(10);
		textfiyat.setBounds(10, 156, 138, 28);
		contentPane.add(textfiyat);
		
		JButton btnNewButton = new JButton("Ekle");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String urunisim=textisim.getText();
			    int urunfiyat=Integer.valueOf(textfiyat.getText());
			    listModel.addElement(urunisim);
				try 
				{
					openConnection();
					Statement st = (Statement) con.createStatement();
					String query = ("insert into urunler (urunisim,urunfiyat) values (?,?)");
					PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
					preparedStmt.setString(1, urunisim);
					preparedStmt.setInt(2, urunfiyat);
					preparedStmt.executeUpdate();
					closeConnection();
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(59, 208, 89, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Urun isim");
		lblNewLabel.setBounds(10, 80, 138, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Urun fiyat");
		lblNewLabel_1.setBounds(10, 142, 138, 14);
		contentPane.add(lblNewLabel_1);
		
        listModel = new DefaultListModel();
		
		urunlist = new JList(listModel);
		urunlist.setBounds(310, 53, 181, 202);
		contentPane.add(urunlist);
		
		btnSil = new JButton("Sil");
		btnSil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//listedeki elemaný seçip deðiþkene aktardým
				int tmp = urunlist.getSelectedIndex();
				String str = listModel.get(tmp).toString();
				//fiziksel olarak seçtiðim þeyi sildim
				listModel.remove(tmp);
				//buradaki olay ilkinde seçtiðim stringi databasede seçtim
				try {
					openConnection();
					Statement st = (Statement) con.createStatement();
					res = (ResultSet) st.executeQuery("SELECT * FROM urunler WHERE urunisim='" + str + "'");
					res.next();
					int id = res.getInt("urunid");
					st.close();
					closeConnection();
					
					//burada databaseden seçtiðim idyi tablodan sildim
					openConnection();
					Statement st1 = (Statement) con.createStatement();
					String query = " DELETE FROM urunler WHERE urunid =" +id;
					PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
					preparedStmt.executeUpdate();
					closeConnection();
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
			}
		});
		btnSil.setBounds(402, 276, 89, 23);
		contentPane.add(btnSil);
		openConnection();
		insert();
		closeConnection();
	}
}
