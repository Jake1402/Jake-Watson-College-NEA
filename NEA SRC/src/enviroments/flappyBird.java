package enviroments;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import MachineLearningFolder.matrices.vectors.vector;

//
//	Use ( u+at )dt
//

public class flappyBird implements Runnable{

	private int WIDTH = 800;
	private int HEIGHT = 900;
	
	JFrame frame;
	Canvas canvas;
	BufferStrategy bufferStrategy;
	
	long FPS = 24;  //desired fps
	long desiredDeltaLoop = 1000000000/FPS;
	
	boolean running = true;
	
	public void windowClosed(WindowEvent e){
		this.running = false;
	    System.exit(0);
	}
	
	public flappyBird() {
		
		frame = new JFrame("Flappy Bird Enviroment");
		
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setLayout(null);
		
		canvas = new Canvas();
		canvas.setBounds(0, 0, WIDTH, HEIGHT);
		canvas.setIgnoreRepaint(true);
		
		panel.add(canvas);
		
		//frame.setDefaultCloseOperation(JFrame.EX);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		
		canvas.requestFocus();
	}
	
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
	
	//encapsulation
	class pipes{
		
		int WIDTH, HEIGHT;	//windows dimensions
		
		int xPos;
		vector gate = new vector(2);			//gate top xy
		Rectangle[] pipe = new Rectangle[] {new Rectangle(), new Rectangle()};	//{top pipe, bottom pipe}
		
		public pipes(int WIDTH, int HEIGHT){
			try {
				gate.setVector(new double[] {this.WIDTH*1.5d, ((new Random().nextDouble())/2.5)*this.WIDTH});
				
			} catch (Exception e) {}	//don't care stay mad
		}
		
	}

}
