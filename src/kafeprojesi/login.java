package kafeprojesi;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.Connection;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.sql.*;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;

public class login extends JFrame {

	private JPanel contentPane;
	private JTextField ad;
	private JTextField sifre;

	private Connection con;
	private ResultSet res;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login frame = new login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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

	private void search() {
		try {
			Statement sta=con.createStatement();
			String cmd="SELECT * FROM `personeller` WHERE isim='"+ad.getText()+"'";
			res=sta.executeQuery(cmd);
			res.next();
			if(res.getString("sifre").equals(sifre.getText())) {
				if(res.getInt("tipi")==1) { 
					adminpanel adminpanel=new adminpanel();
					adminpanel.setVisible(true);
					dispose();
				}
				else {
					masapanel masapanel=new masapanel();
					masapanel.setVisible(true);
					dispose();
				}
			}
			else {
				new JOptionPane();
				JOptionPane.showMessageDialog(null, "Parola yanlýþ!"); 
			}
			sta.close();
		} catch (SQLException e) {
			new JOptionPane();
			JOptionPane.showMessageDialog(null, "Kullanýcý bulunamadý!"); 
			System.out.println("hata!"+e.toString());
		}
	}

	/**
	 * Create the frame.
	 */
	public login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 511, 357);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		ad = new JTextField();
		ad.setBounds(111, 119, 268, 38);
		contentPane.add(ad);
		ad.setColumns(10);

		JButton btnNewButton = new JButton("Giri\u015F Yap");
		btnNewButton.setForeground(Color.RED);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				openConnection();
				search();
				closeConnection();

			}
		});
		btnNewButton.setBounds(265, 252, 114, 31);
		contentPane.add(btnNewButton);

		JLabel lblNewLabel = new JLabel("Ad\u0131n\u0131z:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(121, 94, 80, 14);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Sifreniz:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(121, 168, 114, 14);
		contentPane.add(lblNewLabel_1);

		sifre = new JTextField();
		sifre.setColumns(10);
		sifre.setBounds(111, 193, 268, 38);
		contentPane.add(sifre);
		
		JLabel lblNewLabel_2 = new JLabel("BUCKS Kafeye Hosgeldiniz");
		lblNewLabel_2.setFont(new Font("Ink Free", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(10, 11, 475, 57);
		contentPane.add(lblNewLabel_2);
	}
}
