package com;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import server.loginToDatabase;
import startup.drawLogo;
import agentPortal.agentPortal;



public class Multi_AI_Agents_Main{
	
	@SuppressWarnings("serial")
	public static class login extends JFrame implements Runnable{
		
		//default URL for testing
		String URL = "jdbc:ucanaccess://C:\\Users\\jakey\\eclipse-workspace\\NEA\\NEA_DB.accdb";
		
		// all global varaibles go here
		drawLogo logo;
		private int width, height;
		private boolean status;
		loginToDatabase loginToDB = new loginToDatabase(URL);

		//~~~~~~~~so they're available for events handlers~~~~~~~~\\
		
		private JTextField usernameBox = new JTextField();
		private JPasswordField passwordBox = new JPasswordField();
		
		private JButton login = new JButton("Login");
		private JButton createAccount = new JButton("Create Account");
		
		private JLabel settings = new JLabel("settings");
		
		
		//~~~~~~~~Getters and setters~~~~~~~~\\
		
		private void setWidth(int screenWidth) {
			this.width = screenWidth;
		}
		public int getWidth() {
			return this.width;
		}
		
		
		private void setHeight(int screenHeight) {
			this.height = screenHeight;
		}
		public int getHeight() {
			return this.height;
		}
		
		@SuppressWarnings("unused")
		private void setLoginstatus(boolean loginStatus) {
			this.status = loginStatus;
		}
		public boolean getLoginStatus() {
			return this.status;
		}
		
		//~~~~~~~~Login method and constructor~~~~~~~~\\
		
		protected void login() {
			
			//~~~~~~~~grabbing monitor dimensions for scaling~~~~~~~~\\
			
			GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			setWidth(graphicsDevice.getDisplayMode().getWidth());
			setHeight(graphicsDevice.getDisplayMode().getHeight());
			
			
			//~~~~~~~~creating main page elements~~~~~~~~\\
	
			this.setTitle("MultiAgent Login - Jake Watson");
			
			//for rendering icon
			ImageIcon ico = new ImageIcon(this.getClass().getResource("/images/padlock.jpg"));	//creating an icon
			this.setIconImage(ico.getImage());
			
			//To render logo to the screen
			logo = new drawLogo();
			
			
			//~~~~~~~~Setting position and size of elements using scalars to make it as robust as possible~~~~~~~~\\
			
			logo.setBounds((int) Math.round((this.width*0.25)*0.05),
							(int) Math.round((this.height*0.7)*0.03),
							(int) Math.round((this.width*0.25)*1),
							(int) Math.round((this.height*0.7)*0.06)
							);
			
			usernameBox.setBounds((int) Math.round((this.width*0.25)*0.05),
								  (int) Math.round((this.height*0.7)*0.55),
								  (int) Math.round((this.width*0.25)*0.85),
								  (int) Math.round((this.height*0.7)*0.06)
								  );
			
			passwordBox.setBounds((int) Math.round((this.width*0.25)*0.05),
								  (int) Math.round((this.height*0.7)*0.65),
								  (int) Math.round((this.width*0.25)*0.85),
								  (int) Math.round((this.height*0.7)*0.06)
								  );
			
			login.setBounds((int) Math.round((this.width*0.25)*0.05),
							(int) Math.round((this.height*0.7)*0.75),
							(int) Math.round((this.width*0.25)*0.85),
							(int) Math.round((this.height*0.7)*0.06)
							);
			
			createAccount.setBounds((int) Math.round((this.width*0.25)*0.05),
								  	(int) Math.round((this.height*0.7)*0.825),
									(int) Math.round((this.width*0.25)*0.85),
									(int) Math.round((this.height*0.7)*0.06)
									);
			
			settings.setBounds((int) Math.round((this.width*0.25)*0.4),
							   (int) Math.round((this.height*0.7)*0.89),
						       (int) Math.round((this.width*0.25)*0.83),
							   (int) Math.round((this.height*0.7)*0.06)
							   );
			
			
			//~~~~~~~~Setting fonts~~~~~~~~\\
			
			usernameBox.setFont(new Font(Font.SERIF, Font.BOLD, 16));
			usernameBox.setHorizontalAlignment(JTextField.CENTER);
			
			passwordBox.setFont(new Font(Font.SERIF, Font.BOLD, 16));
			passwordBox.setHorizontalAlignment(JTextField.CENTER);
			
			login.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			createAccount.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			
			
			//~~~~~~~~Adding details~~~~~~~~\\
			
			usernameBox.setBackground(Color.decode("#94BF8A"));
			passwordBox.setBackground(Color.decode("#94BF8A"));
			login.setBackground(Color.decode("#508244"));
			createAccount.setBackground(Color.decode("#508244"));
			
			
			//~~~~~~~~adding event listeners & handlers~~~~~~~~\\
			
			handler react = new handler();
			login.addActionListener(react);
			createAccount.addActionListener(react);
			
			
			//~~~~~~~~Mouse click event on settings~~~~~~~~\\
			
			settings.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent mouseClick){
					URL = JOptionPane.showInputDialog("New URL/File path for Access DB. INCLUDE THE .accdb FILE");
					System.out.println("jdbc:ucanaccess://"+URL);
					loginToDB.setURL("jdbc:ucanaccess://"+URL);
				}
			});
			
			
			//~~~~~~~~adding elements~~~~~~~~\\
			
			this.add(settings);
			this.add(logo);
			this.add(usernameBox);
			this.add(passwordBox);
			this.add(login);
			this.add(createAccount);
			
			
			//~~~~~~~~finishing the GUI settings~~~~~~~~\\
			
			this.setLayout(null);
			//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize((int)Math.round(width*0.25), (int)Math.round(height*0.7));
			this.getContentPane().setBackground(Color.decode("#28A660"));
			this.setResizable(false);
			this.setVisible(true);
			
		}
		
		
		//~~~~~~~~Action listener class for event handling~~~~~~~~\\
		@SuppressWarnings({ "deprecation" })
		private class handler implements ActionListener{
			
			private String userName, password;
			
			public void actionPerformed(ActionEvent event) {
				
				// If login button has been pressed
				if(event.getSource() == login) {
					userName = usernameBox.getText();
					password = passwordBox.getText();

					if(userName.isBlank() || userName.isEmpty() || userName.contains(" ")) {
						JOptionPane.showMessageDialog(null, "Username must not be blank");
					}else {
						loginToDatabase loginToDB = new loginToDatabase(URL);
						loginToDB.loginDB(userName, password);
					}
					//atleast i can catch the errors
					try {
						if((loginToDB.getLoginState())) {
							Thread appThread = new Thread(new agentPortal(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()));
							appThread.run();
							dispose();
						}else {
							JOptionPane.showMessageDialog(null, "Login credentials are incorrect");
						}
					} catch (HeadlessException e) {} catch (Exception e) {}
				}
				
				if(event.getSource() == createAccount) {
					try {
						userName = usernameBox.getText();
						password = passwordBox.getText();
						loginToDatabase loginToDB = new loginToDatabase(URL);
						loginToDB.createAcc(userName, password);
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, "Failed to create account, username may be taken.");
					}
				}		
			}
		}

		@Override
		public void run() {
			this.login();
		}
	}
	
	//java main file
	public static void main(String[] args) throws Exception {
		
		//Threading to improve performance
		Thread thread = new Thread(new login());
		thread.start();
		JOptionPane.showMessageDialog(null, "<html>Recommended use with a 1920x1080<br/>monitor with appropriate scaling of 125%</html>");
	}
}