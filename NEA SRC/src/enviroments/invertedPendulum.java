package enviroments;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import MachineLearningFolder.generateNetwork;
import MachineLearningFolder.networkParams;
import MachineLearningFolder.matrices.vectors.*;
import agentPortal.volatileAIclass;

/**
 * @author jakey
 * 
 * Simulation class for the cart pole/inverted pendulum 
 * enviroment, if your interested in how the simulation 
 * works refer to the following wiki page and scientific 
 * paper below:
 * 
 * https://coneural.org/florian/papers/05_cart_pole.pdf
 * https://en.wikipedia.org/wiki/Inverted_pendulum
 *
 */

@SuppressWarnings("unused")
public class invertedPendulum implements Runnable{

	//screen dimensions
	private int WIDTH = 800;	
	private int HEIGHT = 600;
	
	//system properties, pendulum properties, cart properties, physics engine etc...
	private final byte envID = 0x01;	//enviroment ID
	private final double massPendulum = 0.1d;	//mass of pendulum
	private final double massCart = 1.0d;	//mass of cart
	private final double gravity = -9.8d/4.0d;	//gravity in the simulation
	private final double pendulumLength = 0.5d;	//length of pendulum
	private double totalMass = this.massCart+this.massPendulum;	//combined mass
	private double ForceCart, theta=Math.PI+((Math.random()/5)-0.1), thetaacc=0, pendulumVel=0, dampening=0.99, cartVel=0;	//envirmoent kinematics
	double poleMassLength = this.massPendulum*this.pendulumLength;	//pendulum static torque
	pendulumMaths pm;	//nested class physics engine
	private int cartW = (int)(WIDTH*0.08), cartH = (int)(HEIGHT*0.04);	//cart dimaensions
	private Rectangle cart = new Rectangle((int)((WIDTH/2)-(cartW/2)), (int)((HEIGHT/2)-cartH)-6, cartW, cartH);	//cart object
	int xPos = (int)((WIDTH/2)-(cartW/2));	//cart starting X position
	
	//statistics to be displayed
	int iteration = 1;
	int rewardCounter = 0;
	int bestReward = 0;
	float timeLeft = 0.0f;
	
	//rendering objects the window, the canvas, and screen update object
	JFrame frame;
	Canvas canvas;
	BufferStrategy bufferStrategy;
	
	//simulation loop properties
	long FPS = 24;  //desired fps
	long desiredDeltaLoop = 1000000000/FPS;
	boolean running = true;
	boolean fullExit = true;
	
	//AI objects
	private generateNetwork agent;
	private generateNetwork bestAgent;
	private int MAXMEMORY = (int) (this.FPS*15);		//fps*duration = max amount of "memory" we donate to it (max number of time steps)
	private int memoryINC = 0;
	private vector[] observation = new vector[this.MAXMEMORY];		//array to record observations
	private vector[] action		 = new vector[this.MAXMEMORY];		//array to record actions
	private vector actionRec   = new vector(this.MAXMEMORY);		//vector to record which action we took
	private vector rewardVector  = new vector(this.MAXMEMORY);		//vector to record rewards at time points
	double badCount = 0;											//counter to see if AI is stuck at local minimum
	boolean completed = false;
	
	//constructor to initialise everything
	public invertedPendulum(boolean mode) {
		
		//initialising window
		frame = new JFrame("Inverted Pendulum Environment");
		
		//creating a panel to attach our canvas to for rendering
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setLayout(null);
		
		//initialising canvas
		canvas = new Canvas();
		canvas.setBounds(0, 0, WIDTH, (int)(HEIGHT));
		canvas.setIgnoreRepaint(true);
		
		panel.add(canvas);
		
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		canvas.createBufferStrategy(3);
		bufferStrategy = canvas.getBufferStrategy();
		
		canvas.requestFocus();
		
		//initialising maths/physics engine
		pm = new pendulumMaths(massPendulum, massCart, gravity, pendulumLength);
		
		//initialising neural network
		this.agent = new generateNetwork(4, networkParams.layers, networkParams.density, 2, networkParams.alpha, 0.85, networkParams.func);
		this.bestAgent = agent;
		
		//initialising or arrays to prevent null pointer exceptions
		for(int setupLoop = 0; setupLoop<this.MAXMEMORY; setupLoop++) {
			this.observation[setupLoop] = new vector(4);
			this.action[setupLoop] = new vector(2);
		}
		
	}
	
	//constructor to initialise everything overridden
	public invertedPendulum(boolean mode, generateNetwork ai) {
		
		//initialising window
		frame = new JFrame("Inverted Pendulum Environment");
		
		//creating a panel to attach our canvas to for rendering
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setLayout(null);
		
		//initialising canvas
		canvas = new Canvas();
		canvas.setBounds(0, 0, WIDTH, (int)(HEIGHT));
		canvas.setIgnoreRepaint(true);
		
		panel.add(canvas);
		
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		canvas.createBufferStrategy(3);
		bufferStrategy = canvas.getBufferStrategy();
		
		canvas.requestFocus();
		
		//initialising maths/physics engine
		pm = new pendulumMaths(massPendulum, massCart, gravity, pendulumLength);
		
		//loading network
		this.agent = ai;
		this.bestAgent = agent;
		
		//initialising or arrays to prevent null pointer exceptions
		for(int setupLoop = 0; setupLoop<this.MAXMEMORY; setupLoop++) {
			this.observation[setupLoop] = new vector(4);
			this.action[setupLoop] = new vector(2);
		}
		this.resetParameters();
		
	}
	
	//overridden from runnable class
	@Override
	public void run() {
		
		while(this.fullExit) {
			long initTime;
			long endTime;
			long currentTime = System.nanoTime();
			long lastUpdateTime;
			long deltaTime;
			
			
			while(running) {
				
				try {

					//recording observation and actions
					this.observation[this.memoryINC].setVector(new double[]{((this.theta-Math.PI)/((25*Math.PI)/180)), (this.xPos/400.0)-1, this.pendulumVel, this.cartVel});
					this.action[this.memoryINC] = this.agent.feedForward(this.observation[this.memoryINC]);

					//perform action by using argmax conditional
					this.action[this.memoryINC].print(false);
					if(this.action[this.memoryINC].argmax() == 0) {
						this.actionRec.setElement(this.memoryINC, 0.0);
						this.ForceCart = 2.5d;
					}else if(this.action[this.memoryINC].argmax() == 1) {
						this.actionRec.setElement(this.memoryINC, 1.0);
						this.ForceCart = -2.5d;
					}
					
					initTime = System.nanoTime();
					//render screen
					render();
					lastUpdateTime = currentTime;
					currentTime = System.nanoTime();
					update((int)((currentTime - lastUpdateTime)/(1000000)));
					//calculate delta time
					endTime = System.nanoTime();
					deltaTime = endTime - initTime;
					
					
					if(deltaTime > desiredDeltaLoop) {}else {
						try {
							//pasue for perfect amount of time to maintain frame rate
							Thread.sleep((desiredDeltaLoop - deltaTime)/1000000);
						} catch (InterruptedException e) {}
					}

					this.timeLeft -= (float)((desiredDeltaLoop - deltaTime)/1000000000.0);
					
					//Calculate rewards and death
					if(Math.abs(this.theta-Math.PI) > (14*Math.PI)/180 || this.xPos <= 0 || this.xPos >= 800) {
						
						//checking if AI is stuck in local minimum
						if(this.rewardCounter >= this.bestReward) {
							this.bestAgent = agent;
							volatileAIclass.AI = this.bestAgent; 	//save best AI to volatile class to be saved
							volatileAIclass.env = this.envID;		//save ENV ID to volatile class to be saved
							this.bestReward = this.rewardCounter;
						}else {
							//begin training if AI has poor performance
							this.rewardVector.setElement(this.memoryINC, -1.5d);
							this.agent.setupBellman(this.rewardVector, this.observation, this.action, this.actionRec, this.memoryINC);
							vector[] trainingOut = this.agent.calculateBellman();
							this.agent.train(this.observation, trainingOut, 1, this.memoryINC-1);
							this.badCount++;
							if(this.badCount == 4) {
								this.agent = this.bestAgent;
								volatileAIclass.AI = this.bestAgent;
								volatileAIclass.env = this.envID;
								this.badCount = 0;
							}
						}
						//reset parameters and leave nested loop
						this.resetParameters();
						break;
						
						
						
					}
					else {
						if(Math.abs(this.theta-Math.PI) < (12*Math.PI)/180) {
							//plus one reward for surviving
							this.rewardVector.setElement(this.memoryINC, 1d);
							this.rewardCounter += 1;
						}else {
							//died so minus reward
							this.rewardVector.setElement(this.memoryINC, -1.0d);
							this.rewardCounter -= 1;
						}
					}
					
					//if survived full episode congrats
					this.memoryINC++;
					if(this.memoryINC >= this.MAXMEMORY) {
						
						this.bestAgent = agent;
						this.bestReward = this.rewardCounter;
						volatileAIclass.AI = this.bestAgent; 	//save best AI to volatile class to be saved
						volatileAIclass.env = this.envID;		//save ENV ID to volatile class to be saved
						
						//output solution found
						JOptionPane.showMessageDialog(null, "Solution found!");
						Thread.sleep(500);
						this.resetParameters();
						
						break;
					}
					
				}catch(Exception e1) {
					volatileAIclass.AI = this.bestAgent;
					volatileAIclass.env = this.envID;
					this.fullExit = false;
					this.running = false;
					e1.printStackTrace();
				}
			}	
		}
	}

	//renderer
	private void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		//clear screen
		g.clearRect(0, 0, WIDTH, HEIGHT);
		//redraw screen
		render(g);
		g.dispose();
		//show screen
		bufferStrategy.show();	
	}
	
	//euler integrator to calcaulte new positions on kinematic model
	private void update(int deltaTime) {
		
		double temp = (this.ForceCart + this.poleMassLength*Math.pow(this.pendulumVel, 2)*Math.sin(this.theta))/this.totalMass;        
		
		double thetaacc = ((this.gravity * Math.sin(this.theta)) - (Math.cos(this.theta) * temp))/
				(this.pendulumLength* ( (4.0/3.0) - ((this.massPendulum * Math.pow(Math.cos(this.theta), 2)) / this.totalMass)));
		
		double accCart = temp - (this.poleMassLength*this.thetaacc*Math.cos(this.theta))/this.totalMass;
		
		this.xPos += (cartVel/((desiredDeltaLoop - deltaTime)/1000000));
		this.cartVel += accCart;
		this.cartVel*=this.dampening;
		
		this.pendulumVel += thetaacc;
		this.theta += this.pendulumVel/((desiredDeltaLoop - deltaTime)/1000000);
		this.pendulumVel*=this.dampening;
		
		int renderPos = (int) (this.xPos + (cartVel/((desiredDeltaLoop - deltaTime)/1000000))*150);
		
		cart = new Rectangle((int)(renderPos), (int)((HEIGHT/2)-cartH)-6, cartW, cartH);
	}
	
	//render everything, including on screen stats
	protected void render(Graphics2D g) {
		g.setStroke(new BasicStroke(6));
		g.drawLine(0, HEIGHT/2, WIDTH, HEIGHT/2);
		
		g.setFont(new Font("SANS_SERIF", Font.BOLD, 20));
		g.drawString(String.format("Iteration > %d", this.iteration), (int)(this.WIDTH*0.03), (int)(this.HEIGHT*0.92));
		g.drawString(String.format("Rewards sum > %d", this.rewardCounter), (int)(this.WIDTH*0.03), (int)(this.HEIGHT*0.97));
		g.drawString(String.format("Output > [%.4f <-> %.4f]", this.action[this.memoryINC].getElement(0), this.action[this.memoryINC].getElement(1)), (int)(this.WIDTH*0.03), (int)(this.HEIGHT*0.05));
		g.drawString(String.format("Highest reward sum > %d", this.bestReward), (int)(this.WIDTH*0.03), (int)(this.HEIGHT*0.1));
		
		try {
			this.drawPendulum(g);
		} catch (Exception e) {}
		
	}
	
	//attempt to draw pendulum, if anything goes wrong throw Exception
	private void drawPendulum(Graphics2D g) throws Exception {
		g.setColor(Color.decode("#bd7d2a"));
		g.draw(cart);
		g.fill(cart);
		
		g.setColor(Color.decode("#000000"));
		g.drawOval((int)cart.getCenterX()-3, (int)cart.getMinY()-6, 6, 6);
		
		vector joint = new vector(2);
		joint.setVector(new double[] {(double)(cart.getCenterX()-3), (double)(cart.getMinY()-6)});
		vector posPen = pm.calculatePosition(this.theta, joint);

		g.setStroke(new BasicStroke(10.0f));
		g.setColor(Color.decode("#d95c14"));
		g.drawLine((int)cart.getCenterX(), (int)cart.getMinY()-6, (int)posPen.getElement(0), (int) posPen.getElement(1));
	}
	
	//reset parameters to default
	public void resetParameters() {
		this.memoryINC=0;
		this.xPos = (int)((WIDTH/2)-(cartW/2));
		//slight randomness so not every session is same
		this.theta=Math.PI+((Math.random()/5)-(1.0/10.0));
		this.thetaacc=0;
		this.pendulumVel=0;
		this.cartVel=0;
		this.iteration++;
		this.rewardCounter = 0;
		this.timeLeft = 15;
		
		try {Thread.sleep(50);} catch (InterruptedException e) {}	//50ms sleep so screen doesn't freak out
	}
	
	//nested class for the kinematics/physics of the pendulum
	protected static class pendulumMaths{
		
		private static double massPendulum;
		private static double massCart;
		private static double gravity;
		private static double pendulumLength;
		
		pendulumMaths(double massPendulum, double massCart, double gravity, double pendulumLength){
			pendulumMaths.massPendulum = massPendulum;
			pendulumMaths.massCart = massCart;
			pendulumMaths.gravity = gravity;
			pendulumMaths.pendulumLength = pendulumLength;
		}
		
		//calcuale posisiton
		public vector calculatePosition(double theta, vector joint) {
			vector position = new vector(2);
			
			position.setElement(0, (double)(joint.getElement(0) + 300*pendulumMaths.pendulumLength*Math.sin(theta)));
			position.setElement(1, (double)(joint.getElement(1) + 300*pendulumMaths.pendulumLength*Math.cos(theta)));
			
			return position;
		}
		
		//claculate torque
		public float calculateTorque(double theta) {
			return (float) (pendulumMaths.massPendulum*pendulumMaths.pendulumLength*pendulumMaths.gravity*Math.sin(theta));
		}
	}
	
}