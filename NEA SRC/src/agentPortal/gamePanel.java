package agentPortal;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import enviroments.*;

//
//  enviroments to include
//		-drone control		-> simulation
//		-cart pole clone	-> simulation
//

@SuppressWarnings("serial")
public class gamePanel extends JPanel {
	
	
	//~~~~~~~~~~~~~~~~~~~~global variables~~~~~~~~~~~~~~~~~~~~\\
	
	
	public GraphicsDevice graphicsDevice;
	private int width, height;
	
	
	//~~~~~~~~~~~~~~~~~~~~setters and getters~~~~~~~~~~~~~~~~~~~~\\
	
	
	private void setScreenWidth(int screenWidth) {
		this.width = screenWidth;
	}
	public int getScreenWidth() {
		return this.width;
	}
	
	
	private void setScreenHeight(int screenHeight) {
		this.height = screenHeight;
	}
	public int getScreenHeight() {
		return this.height;
	}
	
	
	
	public gamePanel(GraphicsDevice graphicsDevice){
		
		//getting screen dimensions
		this.graphicsDevice = graphicsDevice;
		this.setScreenWidth(graphicsDevice.getDisplayMode().getWidth());
		this.setScreenHeight(graphicsDevice.getDisplayMode().getHeight());
		 
		this.setLayout(null);
		this.setBounds((int)(this.getScreenWidth()*0.65*0.20), 0, (int)(this.getScreenWidth()*(1-(0.65*0.20))) , (int)(this.getScreenHeight()*0.70));
		this.setBackground(Color.decode("#48c780"));
		
		//~~~~~~~~~~~~~~~~~~~~game & sim panels~~~~~~~~~~~~~~~~~~~~\\
		
		flappyBirdPanel bird = new flappyBirdPanel(this.graphicsDevice);
		dinoJumpPanel   dino = new dinoJumpPanel(this.graphicsDevice);
		droneControlPanel dcp = new droneControlPanel(this.graphicsDevice);
		invertedPendulumPanel ipp = new invertedPendulumPanel(this.graphicsDevice);

		//~~~~~~~~~~~~~~~~~~~~component bounds~~~~~~~~~~~~~~~~~~~~\\
		
		bird.setBounds((int)(this.getWidth()*0.05), (int)(this.getHeight()*0.1), (int)(this.getWidth()*0.2), (int)(this.getHeight()*0.3));
		dino.setBounds((int)(this.getWidth()*0.29), (int)(this.getHeight()*0.1), (int)(this.getWidth()*0.2), (int)(this.getHeight()*0.3));
		dcp.setBounds((int)(this.getWidth()*0.05), (int)(this.getHeight()*0.5), (int)(this.getWidth()*0.2), (int)(this.getHeight()*0.3));
		ipp.setBounds((int)(this.getWidth()*0.29), (int)(this.getHeight()*0.5), (int)(this.getWidth()*0.2), (int)(this.getHeight()*0.3));
		
		//~~~~~~~~~~~~~~~~~~~~adding components~~~~~~~~~~~~~~~~~~~~\\
		
		this.add(bird);
		this.add(dino);
		this.add(dcp);
		this.add(ipp);
		
	}

	
	//~~~~~~~~~~~~~~~~~~~~Environment panel classes~~~~~~~~~~~~~~~~~~~~\\
	
	//nested class
	private class flappyBirdPanel extends JPanel{
		
		//globals
		private int width, height;
		
		//getters and setters
		private void setScreenWidth(int screenWidth) {
			this.width = screenWidth;
		}
		public int getScreenWidth() {
			return this.width;
		}
		
		
		private void setScreenHeight(int screenHeight) {
			this.height = screenHeight;
		}
		public int getScreenHeight() {
			return this.height;
		}
		
		private JButton start = new JButton("start");
		private JLabel Title = new JLabel("Flappy Bird");
		private JLabel description = new JLabel();
		
		
		public flappyBirdPanel(GraphicsDevice graphicsDevice){
			
			this.setScreenWidth(graphicsDevice.getDisplayMode().getWidth());
			this.setScreenHeight(graphicsDevice.getDisplayMode().getHeight());
			this.setLayout(null);
			this.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.decode("#75857c")));
			this.setBackground(Color.decode("#28A660"));

			//~~~~~~~~~~~~~~~~~~~~component settings~~~~~~~~~~~~~~~~~~~~\\
			
			Title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			start.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			
			//toop tip text
			description.setText("<html>"
							 + "Flappy bird is a very simple game that<br/>"
							  + "consists of a bird and multiple pipes<br/>"
							  + "  that the bird must pass through in<br/>"
							  + "     order to get points. <br/><br/>"
							  + "AI Requirements:<br/>"
							  + "Inputs - 4			Outputs - 2</html>");
			
			//~~~~~~~~~~~~~~~~~~~~component bounds~~~~~~~~~~~~~~~~~~~~\\
			
			Title.setBounds((int)(this.getScreenWidth()*0.65*0.20*(0.5-0.085)), (int)(this.getScreenHeight()*0.0), 
					(int)(this.getScreenWidth()*(1-(0.65*0.20))*0.17), (int)(this.getScreenHeight()*(1-(0.65*0.20))*0.05));
			
			description.setBounds((int)(this.getScreenWidth()*0.65*0.20*0.2), (int)(this.getScreenHeight()*0.03), 
					(int)(this.getScreenWidth()*(1-(0.65*0.20))*0.17), (int)(this.getScreenHeight()*(1-(0.65*0.20))*0.15));
			
			start.setBounds((int)(this.getScreenWidth()*0.65*0.20*0.1), (int)(this.getScreenHeight()*0.15), 
							(int)(this.getScreenWidth()*(1-(0.65*0.20))*0.17), (int)(this.getScreenHeight()*(1-(0.65*0.20))*0.05));
			
			//~~~~~~~~~~~~~~~~~~~~component event listener~~~~~~~~~~~~~~~~~~~~\\
			
			start.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//Thread executeGame = new Thread(new flappyBird());
					//executeGame.start();								//uncomment
					JOptionPane.showMessageDialog(null, "<html> This enviroment is not present<br/> please use Pendulum or Drone </html>", "Enviroment - 404", 1);
				}
			});
			
			//~~~~~~~~~~~~~~~~~~~~add components~~~~~~~~~~~~~~~~~~~~\\
			
			this.add(Title);
			this.add(description);
			this.add(start);
		}
	}
	
	//nested class
	private class dinoJumpPanel extends JPanel{
		
		//globals
		private int width, height;
		
		//getters and setters
		private void setScreenWidth(int screenWidth) {
			this.width = screenWidth;
		}
		public int getScreenWidth() {
			return this.width;
		}
		
		
		private void setScreenHeight(int screenHeight) {
			this.height = screenHeight;
		}
		public int getScreenHeight() {
			return this.height;
		}
		
		private JButton start = new JButton("start");
		private JLabel Title = new JLabel("Dino Jump");
		private JLabel description = new JLabel();
		
		public dinoJumpPanel(GraphicsDevice graphicsDevice){
			
			this.setScreenWidth(graphicsDevice.getDisplayMode().getWidth());
			this.setScreenHeight(graphicsDevice.getDisplayMode().getHeight());
			this.setLayout(null);
			this.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.decode("#75857c")));
			this.setBackground(Color.decode("#28A660"));

			//~~~~~~~~~~~~~~~~~~~~component settings~~~~~~~~~~~~~~~~~~~~\\
			
			Title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			start.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			
			description.setText("<html>"
							 + "Dino Jump is a very simple game that<br/>"
							  + "consists of a dinosaur and obstacle<br/>"
							  + "  that the dinosaur must avoid in<br/>"
							  + "     order to get survive. <br/><br/>"
							  + "AI Requirements:<br/>"
							  + "Inputs - 6			Outputs - 2</html>");
			
			//~~~~~~~~~~~~~~~~~~~~component bounds~~~~~~~~~~~~~~~~~~~~\\
			
			Title.setBounds((int)(this.getScreenWidth()*0.65*0.20*(0.5-0.085)), (int)(this.getScreenHeight()*0.0), 
					(int)(this.getScreenWidth()*(1-(0.65*0.20))*0.17), (int)(this.getScreenHeight()*(1-(0.65*0.20))*0.05));
			
			description.setBounds((int)(this.getScreenWidth()*0.65*0.20*0.2), (int)(this.getScreenHeight()*0.03), 
					(int)(this.getScreenWidth()*(1-(0.65*0.20))*0.17), (int)(this.getScreenHeight()*(1-(0.65*0.20))*0.15));
			
			start.setBounds((int)(this.getScreenWidth()*0.65*0.20*0.1), (int)(this.getScreenHeight()*0.15), 
							(int)(this.getScreenWidth()*(1-(0.65*0.20))*0.17), (int)(this.getScreenHeight()*(1-(0.65*0.20))*0.05));
			
			//~~~~~~~~~~~~~~~~~~~~component event listener~~~~~~~~~~~~~~~~~~~~\\
			
			start.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//Thread executeGame = new Thread(new dinoJump());
					//executeGame.start();
					JOptionPane.showMessageDialog(null, "<html> This enviroment is not present<br/> please use Pendulum or Drone </html>", "Enviroment - 404", 1);
				}
			});
			
			//~~~~~~~~~~~~~~~~~~~~add components~~~~~~~~~~~~~~~~~~~~\\
			
			this.add(Title);
			this.add(description);
			this.add(start);
		}
	}
	
	//nested class
	private class droneControlPanel extends JPanel{
		
		//globals
		private int width, height;
		
		//getters and setters
		private void setScreenWidth(int screenWidth) {
			this.width = screenWidth;
		}
		public int getScreenWidth() {
			return this.width;
		}
		
		
		private void setScreenHeight(int screenHeight) {
			this.height = screenHeight;
		}
		public int getScreenHeight() {
			return this.height;
		}
		
		private JButton start = new JButton("start");
		private JLabel Title = new JLabel("Drone Control");
		private JLabel description = new JLabel();
		
		public droneControlPanel(GraphicsDevice graphicsDevice){
			
			this.setScreenWidth(graphicsDevice.getDisplayMode().getWidth());
			this.setScreenHeight(graphicsDevice.getDisplayMode().getHeight());
			this.setLayout(null);
			this.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.decode("#75857c")));
			this.setBackground(Color.decode("#28A660"));

			//~~~~~~~~~~~~~~~~~~~~component settings~~~~~~~~~~~~~~~~~~~~\\
			
			Title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			start.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			
			description.setText("<html>"
							  + "Drone Control is a simple enviroment where<br/>"
							  + "your AI will have to fly a drone to certain<br/>"
							  + "objectives in a 2D enviroment in order to<br/>"
							  + "recieve rewards. <br/><br/>"
							  + "AI Requirements:<br/>"
							  + "Inputs - 8			Outputs - 5</html>");
			
			this.setToolTipText("<html>"
					  + "Best solving parameters:<br/>"
					  + "   -> 2 Layer<br/>"
					  + "   -> 5 Density<br/>"
					  + "   -> Hyper tangent function<br/>"
					  + "   -> Learning rate 0.1 to 0.01<br/>"
					  + "   -> Epsilon between 100% or 0%<br/>"
					  + "<br/>"
					  + "May take upto 2000 iterations to get results<br/>"
					  + "if not observing any training reset simulation<br/>"
					  + "This simulation is complex and will take alot of time<br/>"
					  + "please do be patient or run many at once for best<br/>"
					  + "results, it does have threading leave over night!</html>");
			
			//~~~~~~~~~~~~~~~~~~~~component bounds~~~~~~~~~~~~~~~~~~~~\\
			
			Title.setBounds((int)(this.getScreenWidth()*0.65*0.20*(0.5-0.085)), (int)(this.getScreenHeight()*0.0), 
					(int)(this.getScreenWidth()*(1-(0.65*0.20))*0.17), (int)(this.getScreenHeight()*(1-(0.65*0.20))*0.05));
			
			description.setBounds((int)(this.getScreenWidth()*0.65*0.20*0.2), (int)(this.getScreenHeight()*0.03), 
					(int)(this.getScreenWidth()*(1-(0.65*0.20))*0.17), (int)(this.getScreenHeight()*(1-(0.65*0.20))*0.15));
			
			start.setBounds((int)(this.getScreenWidth()*0.65*0.20*0.1), (int)(this.getScreenHeight()*0.15), 
							(int)(this.getScreenWidth()*(1-(0.65*0.20))*0.17), (int)(this.getScreenHeight()*(1-(0.65*0.20))*0.05));
			
			//~~~~~~~~~~~~~~~~~~~~component event listener~~~~~~~~~~~~~~~~~~~~\\
			
			start.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
					Thread executeGame = new Thread(new droneControl());
					executeGame.start();
					}catch(Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			
			//~~~~~~~~~~~~~~~~~~~~add components~~~~~~~~~~~~~~~~~~~~\\
			
			this.add(Title);
			this.add(description);
			this.add(start);
		}
	}
	
	//nested class
	private class invertedPendulumPanel extends JPanel{
		
		//globals
		private int width, height;
		
		//getters & setters
		private void setScreenWidth(int screenWidth) {
			this.width = screenWidth;
		}
		public int getScreenWidth() {
			return this.width;
		}
		
		
		private void setScreenHeight(int screenHeight) {
			this.height = screenHeight;
		}
		public int getScreenHeight() {
			return this.height;
		}
		
		private JButton start = new JButton("start");
		private JLabel Title = new JLabel("Inverted Pendulum");
		private JLabel description = new JLabel();
		
		public invertedPendulumPanel(GraphicsDevice graphicsDevice){
			
			this.setScreenWidth(graphicsDevice.getDisplayMode().getWidth());
			this.setScreenHeight(graphicsDevice.getDisplayMode().getHeight());
			this.setLayout(null);
			this.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.decode("#75857c")));
			this.setBackground(Color.decode("#28A660"));

			//~~~~~~~~~~~~~~~~~~~~component settings~~~~~~~~~~~~~~~~~~~~\\
			
			Title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			start.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			
			description.setText("<html>"
							  + "Inverted Pendulum is a simple enviroment where<br/>"
							  + "your AI will have to balance a pendulum<br/>"
							  + "in order to recieve rewards. The pendulum<br/>"
							  + "is fixed to cart that can move in the X-axis<br/><br/>"
							  + "AI Requirements:<br/>"
							  + "Inputs - 4			Outputs - 2</html>");
			
			this.setToolTipText("<html>"
							  + "Best solving parameters:<br/>"
							  + "   -> 1 Layer<br/>"
							  + "   -> 4 Density<br/>"
							  + "   -> Hyper tangent function<br/>"
							  + "   -> Learning rate 0.08 to 0.02<br/>"
							  + "   -> Simulation doesn't require a epsilon<br/>"
							  + "<br/>"
							  + "May take upto 200 iterations to get results<br/>"
							  + "if not observing any training reset simulation</html>");
			
			//~~~~~~~~~~~~~~~~~~~~component bounds~~~~~~~~~~~~~~~~~~~~\\
			
			Title.setBounds((int)(this.getScreenWidth()*0.65*0.20*(0.5-0.085)), (int)(this.getScreenHeight()*0.0), 
					(int)(this.getScreenWidth()*(1-(0.65*0.20))*0.17), (int)(this.getScreenHeight()*(1-(0.65*0.20))*0.05));
			
			description.setBounds((int)(this.getScreenWidth()*0.65*0.20*0.2), (int)(this.getScreenHeight()*0.03), 
					(int)(this.getScreenWidth()*(1-(0.65*0.20))*0.17), (int)(this.getScreenHeight()*(1-(0.65*0.20))*0.15));
			
			start.setBounds((int)(this.getScreenWidth()*0.65*0.20*0.1), (int)(this.getScreenHeight()*0.15), 
							(int)(this.getScreenWidth()*(1-(0.65*0.20))*0.17), (int)(this.getScreenHeight()*(1-(0.65*0.20))*0.05));
			
			//~~~~~~~~~~~~~~~~~~~~component event listener~~~~~~~~~~~~~~~~~~~~\\
			
			//anonymous function
			start.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Thread executeGame = new Thread(new invertedPendulum(true));
					executeGame.start();
				}
			});
			
			//~~~~~~~~~~~~~~~~~~~~add components~~~~~~~~~~~~~~~~~~~~\\
			
			this.add(Title);
			this.add(description);
			this.add(start);
		}
	}
	
}
