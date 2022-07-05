package kafeprojesi;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JList;

public class elemanAdmin extends JFrame {

	private JPanel contentPane;
	private JTextField textisim;
	private JTextField textpass;
	private Connection con;
	private ResultSet res;
	private DefaultListModel listModel;
	private JList masalist;

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
	    	
	      String query = "SELECT * FROM `personeller`";
	      Statement st = (Statement) con.createStatement();
	      res = (ResultSet) st.executeQuery(query);

	      while (res.next())
	      {
	        String isim = res.getString("isim");
	        listModel.addElement(isim);
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
					elemanAdmin frame = new elemanAdmin();
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
	public elemanAdmin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 587, 369);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Geri");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adminpanel a1=new adminpanel();
				a1.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setBounds(10, 37, 89, 23);
		contentPane.add(btnNewButton);
		
		textisim = new JTextField();
		textisim.setBounds(10, 96, 164, 29);
		contentPane.add(textisim);
		textisim.setColumns(10);
		
		textpass = new JTextField();
		textpass.setColumns(10);
		textpass.setBounds(10, 147, 164, 29);
		contentPane.add(textpass);
		
		JButton btnEkle = new JButton("Ekle");
		btnEkle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String isim=textisim.getText();
				String pass=textpass.getText();
				try 
				{
					openConnection();
					Statement st = (Statement) con.createStatement();
					String query = ("insert into personeller (isim,sifre) values (?,?)");
					PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
					preparedStmt.setString(1, isim);
					preparedStmt.setString(2, pass);
					preparedStmt.executeUpdate();
					closeConnection();
					listModel.addElement(isim);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
			}
		});
		btnEkle.setBounds(85, 187, 89, 23);
		contentPane.add(btnEkle);
		
		JLabel lblisim = new JLabel("\u0130sim");
		lblisim.setBounds(10, 71, 47, 14);
		contentPane.add(lblisim);
		
		JLabel lblNewLabel = new JLabel("\u015Eifre");
		lblNewLabel.setBounds(10, 136, 47, 14);
		contentPane.add(lblNewLabel);
		
		listModel = new DefaultListModel();
		
		masalist = new JList(listModel);
		masalist.setBounds(369, 40, 157, 236);
		contentPane.add(masalist);
		
		JButton btnSil = new JButton("Sil");
		btnSil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//listedeki elemaný seçip deðiþkene aktardým
				int tmp = masalist.getSelectedIndex();
				String str = listModel.get(tmp).toString();
				//fiziksel olarak seçtiðim þeyi sildim
				listModel.remove(tmp);
				//buradaki olay ilkinde seçtiðim stringi databasede seçtim
				try {
					openConnection();
					Statement st = (Statement) con.createStatement();
					res = (ResultSet) st.executeQuery("SELECT * FROM personeller WHERE isim='" + str + "'");
					res.next();
					int id = res.getInt("idpersoneller");
					st.close();
					closeConnection();
					
					//burada databaseden seçtiðim idyi tablodan sildim
					openConnection();
					Statement st1 = (Statement) con.createStatement();
					String query = " DELETE FROM personeller WHERE idpersoneller =" +id;
					PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
					preparedStmt.executeUpdate();
					closeConnection();
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
			}
		});
		btnSil.setBounds(436, 287, 89, 23);
		contentPane.add(btnSil);
		openConnection();
		insert();
		closeConnection();
	}
}
