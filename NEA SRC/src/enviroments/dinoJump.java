package enviroments;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class dinoJump implements Runnable{

	//frame resolutions
	private int WIDTH = 800;
	private int HEIGHT = 900;
	
	//frame objects
	JFrame frame;
	Canvas canvas;
	BufferStrategy bufferStrategy;
	
	//what FPS we want
	long FPS = 20;  //desired fps
	long desiredDeltaLoop = 1000000000/FPS;
	
	boolean running = true;
	
	public void windowClosed(WindowEvent e){
		this.running = false;
	    System.exit(0);
	}
	
	//main little display
	public dinoJump() {
		
		frame = new JFrame("Dino Jump Environment");
		
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setLayout(null);
		
		canvas = new Canvas();
		canvas.setBounds(0, 0, WIDTH, HEIGHT);
		canvas.setIgnoreRepaint(true);
		
		panel.add(canvas);
		
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		
		canvas.requestFocus();
	}
	
	//main enviroment loop
	@Override
	public void run() {
		long initTime;
		long endTime;
		long currentTime = System.nanoTime();
		long lastUpdateTime;
		long deltaTime;
		
		while(running) {
			initTime = System.nanoTime();
			
			render();
			
			lastUpdateTime = currentTime;
			currentTime = System.nanoTime();
			update((int)((currentTime - lastUpdateTime)/(1000000)));
			
			endTime = System.nanoTime();
			deltaTime = endTime - initTime;
			
			if(deltaTime > desiredDeltaLoop) {
				
			}else {
				try {
					Thread.sleep((desiredDeltaLoop - deltaTime)/1000000);
				} catch (InterruptedException e) {}
			}
		}
		
	}

	//enviroment renderer
	private void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);
		render(g);
		g.dispose();
		bufferStrategy.show();	
	}
	
	private double x = 0;
	private void update(int deltaTime) {

		x+= deltaTime*0.2;
		while(x>500) {
			x-=500;
		}
	}
	
	protected void render(Graphics2D g) {
		g.fillRect((int)x, 0, 200, 200);
	}

}
