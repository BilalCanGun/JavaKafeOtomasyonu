package kafeprojesi;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class adminpanel extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					adminpanel frame = new adminpanel();
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
	public adminpanel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 606, 364);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblAdmin = new JLabel("Hosgeldin Patron");
		lblAdmin.setForeground(Color.RED);
		lblAdmin.setFont(new Font("MV Boli", Font.ITALIC, 25));
		lblAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdmin.setBounds(183, 11, 200, 35);
		contentPane.add(lblAdmin);
		
		JButton btnUrunislemleri = new JButton("\u00DCr\u00FCn \u0130\u015Flemleri");
		btnUrunislemleri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				urunAdmin u1=new urunAdmin();
				u1.setVisible(true);
				dispose();
			}
		});
		btnUrunislemleri.setBounds(208, 108, 152, 58);
		contentPane.add(btnUrunislemleri);
		
		JButton btnElemanislem = new JButton("Eleman i\u015Flemleri");
		btnElemanislem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				elemanAdmin admin1=new elemanAdmin();
				admin1.setVisible(true);
				dispose();
			}
		});
		btnElemanislem.setBounds(415, 108, 152, 58);
		contentPane.add(btnElemanislem);
		
		JButton btnMasaislemleri = new JButton("Masa \u0130\u015Flemleri");
		btnMasaislemleri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				masaAdmin admin1=new masaAdmin();
				admin1.setVisible(true);
				dispose();
			
			}
		});
		btnMasaislemleri.setBounds(10, 108, 152, 58);
		contentPane.add(btnMasaislemleri);
		
		JButton btnNewButton = new JButton("Geri");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login l1=new login();
				l1.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setBounds(10, 11, 89, 23);
		contentPane.add(btnNewButton);
	}
}
