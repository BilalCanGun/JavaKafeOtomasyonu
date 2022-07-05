package kafeprojesi;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.awt.event.ActionEvent;

public class masaAdmin extends JFrame {

	private JPanel contentPane;
	private JTextField textisim;
	private DefaultListModel listModel;
	private JList masalist;
	private Connection con;
	private ResultSet res;
	private ResultSet rs;
	private int selectedItemId=-1;
	
	
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
	
	private void searchId()
	{
		try
	    {
	    	
	      String query = "SELECT * FROM `masalar`";
	      Statement st = (Statement) con.createStatement();
	      rs = (ResultSet) st.executeQuery(query);

	      while (rs.next())
	      {
	        String masaAdi = rs.getString("masaisim");
	        if (masaAdi.equals(masalist.getSelectedValue())) {
				selectedItemId=rs.getInt("masaid");
				System.out.print(selectedItemId);
				helper.currentMasaId = selectedItemId;
				break;
			}
	      }
	      st.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println(e.getMessage());
	    }
	}
	private void insert()
	  {
		  
	    try
	    {
	    	
	      String query = "SELECT * FROM `masalar`";
	      Statement st = (Statement) con.createStatement();
	      rs = (ResultSet) st.executeQuery(query);

	      while (rs.next())
	      {
	        String masaAdi = rs.getString("masaisim");
	        listModel.addElement(masaAdi);
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
					masaAdmin frame = new masaAdmin();
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
	public masaAdmin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 513, 369);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textisim = new JTextField();
		textisim.setBounds(30, 139, 149, 31);
		contentPane.add(textisim);
		textisim.setColumns(10);
		
		JButton btnNewButton = new JButton("Ekle");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String masaismi =textisim.getText();
				listModel.addElement(masaismi);
				try 
				{
					openConnection();
					Statement st = (Statement) con.createStatement();
					String query = ("insert into masalar (masaisim) values (?)");
					PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
					preparedStmt.setString(1, masaismi);
					preparedStmt.executeUpdate();
					closeConnection();
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(89, 194, 89, 23);
		contentPane.add(btnNewButton);
		
        listModel = new DefaultListModel();
		
		masalist = new JList(listModel);
		masalist.setBounds(274, 60, 181, 202);
		contentPane.add(masalist);
		
		JButton btnNewButton_1 = new JButton("Sil");
		btnNewButton_1.addActionListener(new ActionListener() {
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
					res = (ResultSet) st.executeQuery("SELECT * FROM masalar WHERE masaisim='" + str + "'");
					res.next();
					int id = res.getInt("masaid");
					st.close();
					closeConnection();
					
					//burada databaseden seçtiðim idyi tablodan sildim
					openConnection();
					Statement st1 = (Statement) con.createStatement();
					String query = " DELETE FROM masalar WHERE masaid =" +id;
					PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
					preparedStmt.executeUpdate();
					closeConnection();
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
				
			}
		});
		btnNewButton_1.setBounds(349, 286, 89, 23);
		contentPane.add(btnNewButton_1);
		
		JLabel lblNewLabel = new JLabel("Masa ismi giriniz:");
		lblNewLabel.setBounds(30, 114, 123, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Masa Ekleme B\u00F6l\u00FCm\u00FC");
		lblNewLabel_1.setBounds(30, 11, 149, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Masa Silme B\u00F6l\u00FCm\u00FC");
		lblNewLabel_2.setBounds(274, 11, 181, 14);
		contentPane.add(lblNewLabel_2);
		
		JButton btnNewButton_2 = new JButton("Geri");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adminpanel admin1=new adminpanel();
				admin1.setVisible(true);
				dispose();
			}
		});
		btnNewButton_2.setBounds(175, 7, 89, 23);
		contentPane.add(btnNewButton_2);
		openConnection();
		insert();
		closeConnection();
	}
}
