package startup;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;

//
//	This class draws the logo of the chess piece
//	that you see on the login page of the app,
//	For some reason i couldn't get this too work
//	properly in the "loginClass" class however this solution
//	worked fine and doesn't seem to add any bugs
//	other that a TTP bug.
//

@SuppressWarnings("serial")
public class drawLogo extends JPanel{
	
	//globals 
	BufferedImage img;
	private int width, height;
	
	//~~~~~~~~Getters and setters~~~~~~~~\\
	public void setWidth(int screenWidth) {
		this.width = screenWidth;
	}
	public int getWidth() {
		return this.width;
	}
	
	public void setHeight(int screenHeight) {
		this.height = screenHeight;
	}
	public int getHeight() {
		return this.height;
	}
	
	
	//~~~~~~~~Constructor to act as a panel for logo~~~~~~~~\\
	public drawLogo(){
		
		//getting screen dimensions important for rendering
		GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		setWidth(graphicsDevice.getDisplayMode().getWidth());
		setHeight(graphicsDevice.getDisplayMode().getHeight());
		
		this.setBackground(Color.decode("#28A660"));
	}
	
	//~~~~~~~~To draw a scalable logo to the screen~~~~~~~~\\
	public void paint(Graphics g) {
	
		Graphics2D g2d = (Graphics2D)g;

		//load image from file
		try {
			img = ImageIO.read(this.getClass().getResource("/images/logo.jpg"));
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to load logo: \nFILE \"drawLogo.java\",LINE 65");
		}
		
		//using java graphics to draw
		g2d.setBackground(Color.decode("#28A660"));
		g2d.drawImage(img, 
					 (int) Math.round((this.width*0.25)*0), //top Left x
					 (int) Math.round((this.height*0.7)*0),//top left y
					 (int) Math.round((this.width*0.25)*0.85), //bottom right x
					 (int) Math.round((this.height*0.7)*0.5), //bottom right y
					  null);
	}
	
}















