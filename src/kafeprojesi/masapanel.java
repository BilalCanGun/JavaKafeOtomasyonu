package kafeprojesi;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.ListModel;
import javax.swing.JLabel;
import javax.sql.ConnectionEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.SwingConstants;

public class masapanel extends JFrame {

	private JPanel contentPane;
	private DefaultListModel listModel;
	private JList masalist;
	private Connection con;
	private int selectedItemId=-1;
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
					masapanel frame = new masapanel();
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
	public masapanel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 472, 357);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		listModel = new DefaultListModel();
		
		masalist = new JList(listModel);
		masalist.setBounds(62, 58, 157, 236);
		contentPane.add(masalist);
		
		JScrollBar masascrollBar = new JScrollBar();
		masascrollBar.setBounds(217, 58, 27, 236);
		contentPane.add(masascrollBar);
		
		JLabel masalbl = new JLabel("Masalar");
		masalbl.setHorizontalAlignment(SwingConstants.CENTER);
		masalbl.setBounds(76, 29, 130, 22);
		contentPane.add(masalbl);
		
		JButton btnMasaOnayla = new JButton("Onayla");
		btnMasaOnayla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openConnection();
				searchId();
				closeConnection();
				islemler islem=new islemler();
				islem.setVisible(true);
				dispose();
			}
		});
		btnMasaOnayla.setBounds(289, 55, 120, 42);
		contentPane.add(btnMasaOnayla);
		
		JButton btngerike = new JButton("\u00C7\u0131k\u0131\u015F");
		btngerike.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login l1=new login();
				l1.setVisible(true);
				dispose();
			}
		});
		btngerike.setBounds(289, 108, 120, 49);
		contentPane.add(btngerike);
		
		openConnection();
		insert();
		closeConnection();
	}
}
