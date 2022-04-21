package agentPortal;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.GraphicsDevice;

import agentPortal.agentBar;
import agentPortal.gamePanel;

//
//	main user interface frame
//	contains the enviroments 
//	and options panel
//

@SuppressWarnings({ "serial", "unused" })
public class agentPortal extends JFrame implements Runnable{
	
	GraphicsDevice graphicsDevice;
	private int width, height;
	
	//getter setters
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
	
	//constructor 
	public agentPortal(GraphicsDevice graphicsDevice){
		
		//get display dimensions
		this.graphicsDevice = graphicsDevice;
		this.setWidth(graphicsDevice.getDisplayMode().getWidth());
		this.setHeight(graphicsDevice.getDisplayMode().getHeight());
		
	}
	
	@Override
	public void run() {
		//add AI options and enviroments to frame
		this.add(new agentBar(this.graphicsDevice));
		this.add(new gamePanel(this.graphicsDevice));
		
		//set color and other panel options
		this.getContentPane().setBackground(Color.decode("#48c780"));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Agent Portal");
		this.setLayout(null);
		this.setSize((int)(this.getWidth()*0.61), (int)(this.getHeight()*0.70));
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
}
