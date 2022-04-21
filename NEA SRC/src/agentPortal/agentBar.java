
package agentPortal;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import MachineLearningFolder.activationFunctions.*;
import MachineLearningFolder.networkParams;
import agentPortal.saveAndLoad;

//
//	Epsilon greedy is a value
//  that dictates whether or not
//  the AI is "curious". By this i mean
//  it will pick a random action
//
//                 {maxQ(a) with probability 1-e}
//  Action  at t = {                            }
//                 {rand(a) with probability e  }
//

@SuppressWarnings("serial")
public class agentBar extends JPanel{
	
	//~~~~~~~~~~~~~~~~~~~~global variables~~~~~~~~~~~~~~~~~~~~\\

	GraphicsDevice graphicsDevice;
	private int width, height;
	
	private static int minHidden = 1;
	private static int maxHidden = 11;
	private int currentHidden = 6;
	
	private static int minDepth = 1;
public static int maxDepth = 11;
	private int currentDepth = 6;
	
	private static int minAlpha = 0;
	private static int maxAlpha = 4999;
	private int currentAlpha = 2500;
	
	private static int minEpsilon = 1;
	private static int maxEpsilon = 999;
	private int currentEpsilon = 333;
	
	//Volatile parameters, allows communications between threads. Also static
	networkParams netPar = new networkParams(currentHidden, currentDepth, currentAlpha*0.0001d, currentEpsilon*0.001d, new hyperTan());
	
	//Creating UI options
	private JSlider hiddenLayers = new JSlider(JSlider.HORIZONTAL, minHidden, maxHidden, currentHidden);
	private JSlider hiddenDepth  = new JSlider(JSlider.HORIZONTAL, minDepth, maxDepth, currentDepth);
	private JSlider learningSlider = new JSlider(JSlider.HORIZONTAL, minAlpha, maxAlpha, currentAlpha);
	private JSlider epsilonSlider = new JSlider(JSlider.HORIZONTAL, minEpsilon, maxEpsilon, currentEpsilon);
	
	private JLabel layersLabel = new JLabel();
	private JLabel depthLabel = new JLabel();
	private JLabel alphaLabel = new JLabel();
	private JLabel epsilonLabel = new JLabel();
	
	private JButton load = new JButton("load");
	private JButton save = new JButton("save");
	
	private String[] functionsTxt = {"tanh", "ReLU", "LeLU", "Linear"};
	private functions[] functionsObj = {new hyperTan(), new ReLU(), new leakyReLU(), new linear()};
	@SuppressWarnings({"unchecked","rawtypes"})
	private JComboBox activationFunctions = new JComboBox(functionsTxt);
	
	private saveAndLoad SAL = new saveAndLoad();
	
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

	@SuppressWarnings("unchecked")
	agentBar(GraphicsDevice graphicsDevice){

		//getting screen dimensions
		this.graphicsDevice = graphicsDevice;
		this.setScreenWidth(graphicsDevice.getDisplayMode().getWidth());
		this.setScreenHeight(graphicsDevice.getDisplayMode().getHeight());
		
		//configuring layout
		this.setLayout(null);
		this.setBounds(0, 0, (int)(this.getScreenWidth()*0.65*0.20) , (int)(this.getScreenHeight()*0.70));
		this.setBackground(Color.decode("#28A660"));
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 4, Color.decode("#75857c")));
		
		//~~~~~~~~~~~~~~~~~~~~adding network image~~~~~~~~~~~~~~~~~~~~\\
		
		//attempting to add images
		try {
			Image myPicture = ImageIO.read(this.getClass().getResource("/images/AI_DIAGRAM.jpg")).getScaledInstance((int)(this.getWidth()*0.75), (int)(this.getWidth()*0.75), 16);
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			picLabel.setBounds((int)(this.getWidth()*0.0), (int)(this.getHeight()*0.01), (int)(this.getWidth()), (int)(this.getWidth()*0.75));
			this.add(picLabel);
		} catch (IOException e) {
		}

		//~~~~~~~~~~~~~~~~~~~~component settings~~~~~~~~~~~~~~~~~~~~\\
		
		hiddenLayers.setName("hidden");
		hiddenLayers.setMajorTickSpacing(5);
		hiddenLayers.setMinorTickSpacing(1);
		hiddenLayers.setPaintLabels(true);
		hiddenLayers.setPaintTicks(true);
		
		hiddenDepth.setName("depth");
		hiddenDepth.setMajorTickSpacing(5);
		hiddenDepth.setMinorTickSpacing(1);
		hiddenDepth.setPaintLabels(true);
		hiddenDepth.setPaintTicks(true);
		
		learningSlider.setName("population");
		learningSlider.setMajorTickSpacing(2499);
		learningSlider.setMinorTickSpacing(250);
		learningSlider.setPaintLabels(true);
		learningSlider.setPaintTicks(true);
		
		epsilonSlider.setName("epsilon");
		epsilonSlider.setMajorTickSpacing(490);
		epsilonSlider.setMinorTickSpacing(100);
		epsilonSlider.setPaintLabels(true);
		epsilonSlider.setPaintTicks(true);

		//~~~~~~~~~~~~~~~~~~~~component colours~~~~~~~~~~~~~~~~~~~~\\
		
		hiddenLayers.setBackground(Color.decode("#28A660"));
		hiddenDepth.setBackground(Color.decode("#28A660"));
		learningSlider.setBackground(Color.decode("#28A660"));
		epsilonSlider.setBackground(Color.decode("#28A660"));
		
		load.setBackground(Color.decode("#508244"));
		save.setBackground(Color.decode("#508244"));
		
		//~~~~~~~~~~~~~~~~~~~~adding event listeners~~~~~~~~~~~~~~~~~~~~\\
		
		Handler Listener = new Handler();
		
		//anonymous function to handle drop box for activation functions
		activationFunctions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				netPar.func = functionsObj[activationFunctions.getSelectedIndex()];
				//System.out.println(netPar.func.toString());
			}
			
		});
		
		hiddenLayers.addChangeListener(Listener);
		hiddenDepth.addChangeListener(Listener);
		learningSlider.addChangeListener(Listener);
		epsilonSlider.addChangeListener(Listener);
		
		//another anonymous functions for loading and saving AIs
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == save) {
					SAL.save();
				}
			}
		});
		
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == load) {
					SAL.load();
				}
			}
		});
		
		//~~~~~~~~~~~~~~~~~~~~adding tool tip text~~~~~~~~~~~~~~~~~~~~\\
		
		load.setToolTipText("<html>"
				+ "Loads a saved Neural Network <br/>"
				+ "with file format of \"*.csv\" <br/>"
				+ "or \"*.ann\" then loads the enviroment <br/>"
				+ "with training mode turned on or off."
				+ "</html>");
		
		save.setToolTipText("<html>"
				+ "Saves a best Neural Network to your PC<br/>"
				+ "with file format of \"*.csv\" or <br/>"
				+ " \"*.ann\" so it can be loaded later <br/>"
				+ "with training mode turned on or off."
				+ "</html>");
		
		//~~~~~~~~~~~~~~~~~~~~setting bounds for scaling~~~~~~~~~~~~~~~~~~~~\\

		hiddenLayers.setBounds((int)(this.getWidth()*0.1), (int)(this.getHeight()*0.29), (int)(this.getWidth()*0.8), (int)(this.getHeight()*0.05));
		layersLabel.setBounds((int)(this.getWidth()*0.38), (int)(this.getHeight()*0.34), (int)(this.getWidth()*0.6), (int)(this.getHeight()*0.05));//0.005
		
		hiddenDepth.setBounds((int)(this.getWidth()*0.1), (int)(this.getHeight()*0.39), (int)(this.getWidth()*0.8), (int)(this.getHeight()*0.05));//0.01
		depthLabel.setBounds((int)(this.getWidth()*0.38), (int)(this.getHeight()*0.44), (int)(this.getWidth()*0.6), (int)(this.getHeight()*0.05));//0.015

		activationFunctions.setBounds((int)(this.getWidth()*0.1), (int)(this.getHeight()*0.49), (int)(getWidth()*0.8), (int)(this.getHeight()*0.035));//0.02 - done
		
		learningSlider.setBounds((int)(this.getWidth()*0.1), (int)(this.getHeight()*0.55), (int)(this.getWidth()*0.8), (int)(this.getHeight()*0.052));//0.025
		alphaLabel.setBounds((int)(this.getWidth()*0.25), (int)(this.getHeight()*0.6), (int)(this.getWidth()*0.6), (int)(this.getHeight()*0.05));//0.03

		epsilonSlider.setBounds((int)(this.getWidth()*0.1), (int)(this.getHeight()*0.65), (int)(this.getWidth()*0.8), (int)(this.getHeight()*0.052));//0.035
		epsilonLabel.setBounds((int)(this.getWidth()*0.28), (int)(this.getHeight()*0.7), (int)(this.getWidth()*0.6), (int)(this.getHeight()*0.05));//0.04

		save.setBounds((int)(this.getWidth()*0.1), (int)(this.getHeight()*0.77), (int)(this.getWidth()*0.35), (int)(this.getHeight()*0.15));
		load.setBounds((int)(this.getWidth()*0.55), (int)(this.getHeight()*0.77), (int)(this.getWidth()*0.35), (int)(this.getHeight()*0.15));
		
		//~~~~~~~~~~~~~~~~~~~~adding components~~~~~~~~~~~~~~~~~~~~\\
		
		this.add(hiddenLayers);
		layersLabel.setText("Layers - "+currentHidden);
		this.add(layersLabel);
		
		this.add(hiddenDepth);
		depthLabel.setText("Density - "+currentDepth);
		this.add(depthLabel);
		
		activationFunctions.setPrototypeDisplayValue("Activation Functions");
		this.add(activationFunctions);

		this.add(learningSlider);
		alphaLabel.setText("Learning rate - "+currentAlpha*0.0001);
		this.add(alphaLabel);
		
		this.add(epsilonSlider);
		epsilonLabel.setText("Epsilon greedy - "+currentEpsilon*0.1+"%");
		this.add(epsilonLabel);

		this.add(load);
		this.add(save);
		
	}
	
	//~~~~~~~~~~~~~~~~~~~~Handlers for a reactive interface~~~~~~~~~~~~~~~~~~~~\\
	
	private class Handler implements ChangeListener{

		@SuppressWarnings("static-access")
		@Override
		public void stateChanged(ChangeEvent e) {
			
			//if whatever clicked do this and update network parameters
			if(e.getSource() == hiddenLayers) {
				currentHidden = hiddenLayers.getValue();
				layersLabel.setText("Layers - "+currentHidden);
				netPar.layers = currentHidden;
			}
			if(e.getSource() == hiddenDepth) {
				currentDepth = hiddenDepth.getValue();
				depthLabel.setText("Density - "+currentDepth);
				netPar.density = currentDepth;
			}
			if(e.getSource() == learningSlider) {
				currentAlpha = learningSlider.getValue();
				alphaLabel.setText("Learning rate - "+currentAlpha*0.0001);
				netPar.alpha = currentAlpha*0.0001;
			}
			if(e.getSource() == epsilonSlider) {
				currentEpsilon = epsilonSlider.getValue();
				epsilonLabel.setText("Epsilon greedy - "+currentEpsilon*0.1+"%");
				netPar.epsilon = currentEpsilon*.001d;
			}
		}
	}
}
