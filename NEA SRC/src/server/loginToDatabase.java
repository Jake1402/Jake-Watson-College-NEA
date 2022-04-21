package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.swing.JOptionPane;

import java.sql.*;

@SuppressWarnings("unused")
public class loginToDatabase {
	
	//jdbc:ucanaccess://C:\\\\Users\\\\jakey\\\\eclipse-workspace\\\\NEA\\\\NEA_DB.accdb
	//URL/path of the .accdb folder, static so its the same across all references
	private static String URL = "";
	
	private String username;	//keeping them private
	private String password;	//so only this and subclasses have access
	private static boolean loginState;	//private so it can be accessed

	
	//~~~~~~~~Getters and Setters~~~~~~~~\\
	
	public void setURL(String URL) {
		loginToDatabase.URL = URL;
	}
	public String getURL() {
		return loginToDatabase.URL;
	}
	
	private void setLoginState(boolean state) {
		loginToDatabase.loginState = state;
	}
	public boolean getLoginState() {
		return loginToDatabase.loginState;
	}
	
	
	//~~~~~~~~Constructor~~~~~~~~\\
	public loginToDatabase(String URL){		
		//this.setURL(URL);
	}
	
	public void loginDB(String user, String Password) {
		
		//get the .accdb location
		String databaseURL = this.getURL();
	    try (Connection connection = DriverManager.getConnection(loginToDatabase.URL)) {
	             
	    	//selecting all columns from database
	           String sql = "SELECT * FROM NEA";
	             
	           Statement statement = connection.createStatement();
	           ResultSet result = statement.executeQuery(sql);
	            
	           while (result.next()) {
	               int id = result.getInt("ID");
	               String username = result.getString("UserName");
	               String password = result.getString("Password");
	               String salt = result.getString("salt");
	               
	               //check if the user details match
	               if((username.strip()).equals(user.strip()) && (password.strip()).equals(this.encrypyWithSHA256salt(Password, Integer.parseInt(salt)))) {
	               	this.setLoginState(true);
	               }
	               
	           }
	             
	       } catch (SQLException ex) {
	            ex.printStackTrace();
	       }
		
	}
	
	
	public void createAcc(String userName, String PassWord) throws SQLException{
		
		//load db path
		String databaseURL = loginToDatabase.URL;
		//randomly generate the salt for added encrpytion
        int salt = ((int) Math.floor(Math.random()*64000));
		
        //attempt connection
        Connection connection = DriverManager.getConnection(databaseURL);
        
        //selecting all not good for security
        String sql = "SELECT * FROM NEA";
        
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
         
        while (result.next()) {
        	//checking if username already exists
            String username = result.getString("UserName");    
            if((username.strip()).equals(userName.strip())) {
            	//throw an error is username is taken, this is then caught and error is relayed back
            	throw new java.sql.SQLException("User already exists");
            }
        }
        
        //inserting into db if user doesn't exist
        sql = "INSERT INTO NEA (UserName, Password, salt) VALUES (?, ?, ?)";
	           
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, this.encrypyWithSHA256salt(PassWord, salt));
        preparedStatement.setString(3, Integer.toString(salt));
	            
        int row = preparedStatement.executeUpdate();
	            
        if (row > 0) {
        	//sucsess
        	JOptionPane.showMessageDialog(null, "User created!");
        }                   
	}
	
	//encryption of passwords
	private String encrypyWithSHA256salt(String text, int salt) {
		
		MessageDigest digest;
		try {
			//setting up SHA256
			digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest((text.concat(Integer.toString(salt))).getBytes(StandardCharsets.UTF_8));
		    StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
		    for (int i = 0; i < encodedhash.length; i++) {
		        String hex = Integer.toHexString(0xff & encodedhash[i]);
		        if(hex.length() == 1) {
		            hexString.append('0');
		        }
		        hexString.append(hex);
		    }
		    //returning hashed string in hex form
		    return hexString.toString();
		} catch (NoSuchAlgorithmException e) {}
		return "";
	}
}
