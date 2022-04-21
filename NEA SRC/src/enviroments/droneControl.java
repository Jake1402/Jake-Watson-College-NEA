package enviroments;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import MachineLearningFolder.generateNetwork;
import MachineLearningFolder.networkParams;
import MachineLearningFolder.matrices.vectors.vector;
import agentPortal.volatileAIclass;

public class droneControl implements Runnable{

	//frame resolution 800x600
	private int WIDTH = 800;
	private int HEIGHT = 600;
	
	//Simulation paramerters
	private byte envID = 0x02;	//sim ID
	private double mass = 1.0;	//Drone mass
	private double maxThrust = 20.0;	//drone thrust
	private int dW = (int) (WIDTH*0.06), dH = (int) (HEIGHT*0.035), dcW = (int) (WIDTH*0.02), dcH = (int) (HEIGHT*0.03);		//drone width and height
	private int objW = (int)(WIDTH*0.037), objH = (int)(HEIGHT*0.04);	//obj width and height
	private double dampening = 1.0;		//friction
	private Ellipse2D drone = new Ellipse2D.Float();			//drone object
	private Ellipse2D droneCockpit = new Ellipse2D.Float();		//drone cockpit object
	private Ellipse2D objective = new Ellipse2D.Float();		//target object for drone to reach
	private Rectangle objBounds = new Rectangle();	//objective bounbds box
	private vector objPos = new vector(2);	//vector for objective postions
	
	//Physics engine parameters
	physicsEngine pe = new physicsEngine(this.mass, this.maxThrust, this.dampening);	//setting up physics engine object
	vector vel = new vector(2);
	vector prevVel = vel;
	vector inv = new vector(2);
	vector acc = new vector(2);
	vector dis = new vector(2);
	vector preDis = new vector(2);
	double[] initVal = new double[]{0.0001d,0.0001d};
	double prevVelMagnitude = 0;
	double velMagnitude = 0;
	
	//Simulation rendering variables
	JFrame frame;
	Canvas canvas;
	BufferStrategy bufferStrategy;
	
	long FPS = 24;  //desired fps
	long desiredDeltaLoop = 1000000000/FPS;
	boolean running = true;
	boolean fullExit = true;
	
	//AI variables
	private generateNetwork agent;
	private generateNetwork bestAgent;
	private int MAXMEMORY = (int) (this.FPS*30);		//fps*duration = max amount of "memory" we donate to it (max number of time steps)
	private int memoryINC = 0;
	private vector[] observation = new vector[this.MAXMEMORY];		//array to record observations
	private vector[] action		 = new vector[this.MAXMEMORY];		//array to record actions
	private vector actionRec     = new vector(this.MAXMEMORY);		//vector to record which action we took
	private vector rewardVector  = new vector(this.MAXMEMORY);		//vector to record rewards at time points
	double epsilon = networkParams.epsilon;
	double initEpsilon = this.epsilon;
	double minCount = 0;
	int badCount = 0;
	boolean completed = false;
	
	//for reward logic & and distance calculations
	double distance = 0;
	double prevDistance = 0;
	
	//statistics to be displayed
	int iteration = 1;
	double rewardCounter = 0;
	double bestReward = 0;
	int objectiveHit = 0;
	int objectiveHitBest = 0;
	
	//Batch training the model due to enviroment complexity.
	int batchSize = 32;
	int[] memoryIncArray = new int[batchSize];
	vector[][] observationRecorder = new vector[this.MAXMEMORY][batchSize];
	vector[][] actionsRecorder	   = new vector[this.MAXMEMORY][batchSize];
	vector[]   actionTaken		   = new vector[this.batchSize];
	vector[]   rewardsRecorded	   = new vector[this.batchSize];
	
	//constructor is overridden, initialises all environment variables
	public droneControl() throws Exception {
		frame = new JFrame("Drone Control Environment");
		
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
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		canvas.createBufferStrategy(3);
		bufferStrategy = canvas.getBufferStrategy();
		
		canvas.requestFocus();
		
		//setting all drone values to default
		vel.setVector(initVal);
		inv.setVector(initVal);
		acc.setVector(initVal);
		dis.setVector(new double[] {(WIDTH/2)-(dW/2), HEIGHT/2});
		
		//loading network
		this.agent = new generateNetwork(8, networkParams.layers, networkParams.density, 5, networkParams.alpha, 0.5d, networkParams.func);
		this.bestAgent = agent;
		
		//initialising or arrays to prevent null pointer exceptions
		for(int setupLoop = 0; setupLoop<this.MAXMEMORY; setupLoop++) {
			this.observation[setupLoop] = new vector(8);
			this.action[setupLoop] = new vector(5);
		}
		
		this.resetParameters();
		//this.newObjective();
		
	}
	
	//constructor is overridden, initialises all environment variables. Used when loading AI
	public droneControl(boolean mode, generateNetwork AI) throws Exception {
		frame = new JFrame("Drone Control Environment");
		
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
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		canvas.createBufferStrategy(3);
		bufferStrategy = canvas.getBufferStrategy();
		
		canvas.requestFocus();
		
		//setting all drone values to default
		vel.setVector(initVal);
		inv.setVector(initVal);
		acc.setVector(initVal);
		dis.setVector(new double[] {(WIDTH/2)-(dW/2), HEIGHT/2});
		
		//loading network
		this.agent = AI;
		this.bestAgent = AI;
		
		//initialising or arrays to prevent null pointer exceptions
		for(int setupLoop = 0; setupLoop<this.MAXMEMORY; setupLoop++) {
			this.observation[setupLoop] = new vector(8);
			this.action[setupLoop] = new vector(5);
		}
		
		this.resetParameters();
		//this.newObjective();
		
	}
	
	//run method overridden from runnable
	@Override
	public void run() {
		
		while(fullExit) {
		
			//all timing variables for perfect FPS
			long initTime;
			long endTime;
			long currentTime = System.nanoTime();
			long lastUpdateTime;
			long deltaTime = 0;
			
			while(running) {
				try {
					
					//normalising inputs between [-1,1] 6 inputs for AI position
					//target position and drone velocity, angle 
					this.observation[this.memoryINC].setVector(new double[]{
							this.returnMappedValues(this.drone.getCenterX(), 0, 400)-1.0,
							this.returnMappedValues(this.drone.getCenterY(), 0, 300)-1.0,
							this.returnMappedValues(this.objective.getCenterX(), 0, 400)-1.0,
							this.returnMappedValues(this.objective.getCenterY(), 0, 300)-1.0,
							this.returnMappedValues(this.vel.getElement(0), 0, 20),
							this.returnMappedValues(this.vel.getElement(1), 0, 20),
							Math.cos(Math.tan((this.objective.getCenterY()-this.drone.getCenterY())/(this.objective.getCenterX()-this.drone.getCenterX()))),
							Math.sin(Math.tan((this.objective.getCenterY()-this.drone.getCenterY())/(this.objective.getCenterX()-this.drone.getCenterX())))		
					});
					this.action[this.memoryINC] = this.agent.feedForward(this.observation[this.memoryINC]);
					//reseting the dampening to remove breaking effect
					//setting up our exploitation-exploration function
					//to assist with early stages of learning
					pe.dampening = 0.95;
					this.epsilon = this.initEpsilon*Math.exp(-0.02*(this.iteration-1));
					if(this.epsilon <= Math.random()) {
						switch(this.action[this.memoryINC].argmax()) {
						case 0://Left
							this.actionRec.setElement(this.memoryINC, 0.0);
							acc = pe.accelerationVector(-1, 0); 			
							break;
						case 1://Up
							this.actionRec.setElement(this.memoryINC, 1.0);
							acc = pe.accelerationVector(0, 1); 
							break;
						case 2://Right
							this.actionRec.setElement(this.memoryINC, 2.0);
							acc = pe.accelerationVector(1, 0); 
							break;
						case 3://Down
							this.actionRec.setElement(this.memoryINC, 3.0);
							acc = pe.accelerationVector(0, -1); 
							break;
						case 4://Break
							this.actionRec.setElement(this.memoryINC, 4.0);
							acc = pe.accelerationVector(0, 0.0);
							acc = pe.accelerationVector(1, 0.0); 
							pe.dampening = 0.90d;
							break;
						}
					}else {
						//Getting the AI response to the enviroment and using a 
						//switch case to add a statement to execute AI commands
						this.actionRec.setElement(this.memoryINC, new Random(System.nanoTime()).nextInt(5));
						switch((int)(this.actionRec.getElement(this.memoryINC))) {
						case 0://Left
							this.actionRec.setElement(this.memoryINC, 0.0);
							acc = pe.accelerationVector(-1, 0); 			
							break;
						case 1://Up
							this.actionRec.setElement(this.memoryINC, 1.0);
							acc = pe.accelerationVector(0, 1); 
							break;
						case 2://Right
							this.actionRec.setElement(this.memoryINC, 2.0);
							acc = pe.accelerationVector(1, 0); 
							break;
						case 3://Down
							this.actionRec.setElement(this.memoryINC, 3.0);
							acc = pe.accelerationVector(0, -1); 
							break;
						case 4://Break
							this.actionRec.setElement(this.memoryINC, 4.0);
							acc = pe.accelerationVector(0, 0.0);
							acc = pe.accelerationVector(1, 0.0); 
							pe.dampening = 0.90d;
							break;
						}
					}
					
					//Main simulation loop
					initTime = System.nanoTime();
					
					//redner during loop to calucate timing
					render();
					
					lastUpdateTime = currentTime;
					currentTime = System.nanoTime();
				
					update((int)((currentTime - lastUpdateTime)/(1000000)));
					
					endTime = System.nanoTime();
					deltaTime = endTime - initTime;
		
					if(deltaTime > desiredDeltaLoop) {}else {
						try {
							Thread.sleep((long) ((desiredDeltaLoop - deltaTime)/1000000.0));
						} catch (InterruptedException e) {}
					}
					
					//determine whether or not AI is "cheating" this will be fixed with new reward system
					if((Math.abs(this.dis.getElement(0) - this.preDis.getElement(0)) <= 1d || Math.abs(this.dis.getElement(1) - this.preDis.getElement(1)) <= 0.8d)) {
						this.minCount++;
						if(this.minCount >= 88) {
							//record AI as best
							if(this.rewardCounter >= this.bestReward || this.objectiveHit >= this.objectiveHitBest) {
								this.bestAgent = agent;
								volatileAIclass.AI = this.bestAgent;	//saves best agents to volatile class
								volatileAIclass.env = this.envID;		//saves env ID to volatile class
								this.bestReward = this.rewardCounter;
								if(this.objectiveHit >= this.objectiveHitBest) {
									this.objectiveHitBest = this.objectiveHit;
								}else {
									this.bestReward = this.rewardCounter;
								}
							}else {							
								this.badCount++;
								if(this.badCount == 10) {
									this.agent = this.bestAgent;
									volatileAIclass.AI = this.bestAgent;
									volatileAIclass.env = this.envID;
									this.badCount = 0;
								}
							}
						}
						try {
							for(int backReward = 0; backReward >= 10; backReward++) {
								this.rewardVector.setElement(this.memoryINC-backReward, -0.5d);
							}
						}catch(Exception e) {
							this.rewardVector.setElement(this.memoryINC, -1.5d);
						}
						this.memoryINC++;
						//train AI
						if(batchSize == 32) {
							this.agent.setupBellman(this.rewardVector, this.observation, this.action, this.actionRec, this.memoryINC);
							vector[] trainingOut = this.agent.calculateBellman();
							this.agent.train(this.observation, trainingOut, 1, this.memoryINC);
						}
					}else {
						this.minCount = 0;
					}
					
					//Check AI bounds
					if(this.drone.getX() > 780 || this.drone.getX() < 10 || this.drone.getY() < 10 || this.drone.getY() > 590 || this.rewardCounter < -300) {

						//record AI as best
						if(this.rewardCounter >= this.bestReward || this.objectiveHit >= this.objectiveHitBest) {
							
							this.bestAgent = agent;
							volatileAIclass.AI = this.bestAgent;
							volatileAIclass.env = this.envID;
							if(this.objectiveHit >= this.objectiveHitBest) {
								this.objectiveHitBest = this.objectiveHit;
							}else {
								this.bestReward = this.rewardCounter;
							}
							
						}else {								
							this.badCount++;
							if(this.badCount == 10 && this.epsilon < 0.15) {
								this.agent = this.bestAgent;
								volatileAIclass.AI = this.bestAgent;
								volatileAIclass.env = this.envID;
								this.badCount = 0;
								this.resetParameters();
								break;
							}
						}
						
						try {
							for(int backReward = 0; backReward >= 8; backReward++) {
								this.rewardVector.setElement(this.memoryINC-backReward, -2.0d);
							}
						}catch(Exception e) {
							this.rewardVector.setElement(this.memoryINC, -1.5d);
						}
						//train AI
						this.memoryINC++;
						this.agent.setupBellman(this.rewardVector, this.observation, this.action, this.actionRec, this.memoryINC);
						vector[] trainingOut = this.agent.calculateBellman();
						this.agent.train(this.observation, trainingOut, 1, this.memoryINC);
						this.resetParameters();
						break;
						
					}else {
						this.rewardVector.setElement(this.memoryINC, 0.0d);
						this.rewardCounter += 1.0d;
						if(this.drone.intersects(this.objBounds)) {
							
							try {
								for(int backReward = 0; backReward >= 13; backReward++) {
									this.rewardVector.setElement(this.memoryINC-backReward, 2.0d);
								}
							}catch(Exception e) {
								this.rewardVector.setElement(this.memoryINC, 1.0d);
							}
							
							this.rewardCounter += 100.0d;
							this.newObjective();
						}	
					}
					
					//Ends epoch if AI hits max time duration
					this.memoryINC++;
					if(this.memoryINC >= this.MAXMEMORY || this.objectiveHit >= 15) {
						if(this.rewardCounter >= this.bestReward || this.objectiveHit >= this.objectiveHitBest) {
							this.bestAgent = agent;
							volatileAIclass.AI = this.bestAgent;	//saves best agents to volatile class
							volatileAIclass.env = this.envID;		//saves env ID to volatile class
							if(this.objectiveHit >= this.objectiveHitBest) {
								this.objectiveHitBest = this.objectiveHit;
							}else {
								this.bestReward = this.rewardCounter;
							}
						}

						//if AI hasn't solved the problem
						if(this.objectiveHit < 15) {
							this.agent.setupBellman(this.rewardVector, this.observation, this.action, this.actionRec, this.memoryINC-1);
							vector[] trainingOut = this.agent.calculateBellman();
							this.agent.train(this.observation, trainingOut, 1, this.memoryINC-1);	
						}
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

	//rendering, clears screen for re-draw
	private void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		//clears screen
		g.clearRect(0, 0, WIDTH, HEIGHT);
		//redraws now
		this.displayStats(g);
		render(g);
		g.dispose();
		//shows it
		bufferStrategy.show();	
	}

	//euler integrator for calculating distances
	private void update(int deltaTime) {

		preDis = dis;
		prevVel = vel;
		prevVelMagnitude = Math.sqrt(Math.pow(this.prevVel.getElement(0), 2) + Math.pow(this.prevVel.getElement(1), 2));
		vel = pe.velocityVector(acc, deltaTime/1000.0d, inv);
		velMagnitude = Math.sqrt(Math.pow(this.vel.getElement(0), 2) + Math.pow(this.vel.getElement(1), 2));
		inv = vel;
		dis = dis.vectorAddition(dis, pe.displacementVector(vel, deltaTime/1000.0d));
		
	}
	
	//render out game objects UFO & Target
	protected void render(Graphics2D g) {
		
		//code below is very important, setFrame(x, y, width, height) -> setColor(new Color(r, g, b)) -> fill(shape)
		//last translate reset canvas position so other objects can be drawn without requiring complicated math functions
		this.drone.setFrame(dis.getElement(0), dis.getElement(1), this.dW, this.dH);
		this.droneCockpit.setFrame(dis.getElement(0)+(this.dW/2.8), dis.getElement(1)-this.dH/2.5, this.dcW, this.dcH);
		g.setColor(new Color(32, 159, 189));
		g.fill(this.droneCockpit);
		g.setColor(new Color(51,51,51));
		g.fill(this.drone);
		g.setColor(new Color(255, 255, 0));
		g.fill(this.objective);
	}
	
	//drawing stats to the screen e.g. current network outputs, score, current generation, etc
	void displayStats(Graphics2D g) {
		g.setFont(new Font("SANS_SERIF", Font.BOLD, 20));
		g.drawString(String.format("Epsilon > %.3f", this.epsilon), (int)(this.WIDTH*0.03), (int)(this.HEIGHT*0.87));
		g.drawString(String.format("Iteration > %d", this.iteration), (int)(this.WIDTH*0.03), (int)(this.HEIGHT*0.92));
		g.drawString(String.format("Rewards sum > %f", this.rewardCounter), (int)(this.WIDTH*0.03), (int)(this.HEIGHT*0.97));
		g.drawString(String.format("Output > [%.3f <-> %.3f <-> %.3f <-> %.3f <-> %.3f]", this.action[this.memoryINC].getElement(0), this.action[this.memoryINC].getElement(1), this.action[this.memoryINC].getElement(2), this.action[this.memoryINC].getElement(3), this.action[this.memoryINC].getElement(4)), (int)(this.WIDTH*0.03), (int)(this.HEIGHT*0.05));
		g.drawString(String.format("Highest reward sum > %f", this.bestReward), (int)(this.WIDTH*0.03), (int)(this.HEIGHT*0.1));
	}
	
	//mapping values between a max and min
	double returnMappedValues(double x, double min, double max) {
		return ((x-min)/(max - min));
	}
	
	//method to generate a new objective
	void newObjective() {
		this.objectiveHit++;
		this.objPos.setElement(0, (((Math.random()*0.6)+0.2)*this.WIDTH));
		this.objPos.setElement(1, (((Math.random()*0.6)+0.2)*this.HEIGHT));
		this.objective.setFrame((int)this.objPos.getElement(0), (int)this.objPos.getElement(1), this.objW, this.objH);
		this.objBounds.setBounds((int)(this.objPos.getElement(0)), (int)(this.objPos.getElement(1)), this.objW, this.objH);
	}
	
	//setting all enviroment values to default
	void resetParameters() throws Exception {
		vel.setVector(initVal);
		inv.setVector(initVal);
		acc.setVector(initVal);
		dis.setVector(new double[] {(WIDTH/2)-(dW/2), HEIGHT/2});
		this.rewardCounter = 0;
		this.objectiveHit = 0;
		this.memoryINC = 0;
		this.minCount = 0;
		this.badCount = 0;
		this.iteration++;
		this.newObjective();
		Thread.sleep(100);
	}
	
	
	
	//Physics engine class, literally just suvat
	//nested class
	protected class physicsEngine{
		
		private double mass;
		private double maxThrust;
		public double dampening;
		
		public physicsEngine(double mass, double maxThrust, double dampening){
			this.mass = mass;
			this.maxThrust = maxThrust;
			this.dampening = dampening;
		}
		
		public vector accelerationVector(double thrustScalarX, double thrustScalarY) {
			vector accPE = new vector(2);
			
			accPE.setElement(0, (double)((thrustScalarX*this.maxThrust)/this.mass));
			accPE.setElement(1, -(double)((thrustScalarY*this.maxThrust)/this.mass));
			
			return accPE;
		}
		
		public vector velocityVector(vector acc, double time, vector initV) {
			vector velPE = initV;
			vector tmp = acc.scale(acc, time);
			velPE = velPE.vectorAddition(velPE, tmp);
			velPE.scale(velPE, this.dampening);
			
			//capping velocity to a maximum so the drone doesn't shoot off screen
			if(velPE.getElement(0) > 12.0d ) {
				velPE.setElement(0, 12.0d);
			}else if(velPE.getElement(0) < -12.0d) {
				velPE.setElement(0, -12.0d);
			}else if(velPE.getElement(1) > 12.0d ) {
				velPE.setElement(1, 12.0d);
			}else if(velPE.getElement(1) < -12.0d) {
				velPE.setElement(1, -12.0d);
			}
			
			return velPE;
		}
		
		public vector displacementVector(vector velocity, double time) {
			vector displacementPE = new vector(2);
			displacementPE = velocity.scale(velocity, time*20.0);		//20pix = 1m
			return displacementPE;
		}
	}
}