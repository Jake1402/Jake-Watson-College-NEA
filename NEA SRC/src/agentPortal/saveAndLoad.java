package agentPortal;

import MachineLearningFolder.generateNetwork;
import MachineLearningFolder.networkParams;
import MachineLearningFolder.matrices.matrix.matrix;
import agentPortal.volatileAIclass;
import enviroments.droneControl;
import enviroments.invertedPendulum;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

//
//	Following code is horrible
//	I would like to apologise to 
//	the marker and examiner, first 
//	trying to create a decent file format
//

@SuppressWarnings({"serial","unused"})
public class saveAndLoad extends JFrame{
	
	//file explorer to make it easier for user
	private JFileChooser fileChoose = new JFileChooser();
	
	//varaibles to be saved
	private generateNetwork AI;
	private byte enviroment;
	private String url;
	
	//tensor that consists of weights
	private matrix[] weightTensor;
	
	//save the AI to file
	public void save() {
		
		try {

			this.AI = volatileAIclass.AI;
			this.enviroment = volatileAIclass.env;
		
			//pick directory
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
			    File selectedFile = fileChooser.getSelectedFile();
			    this.url = selectedFile.getAbsolutePath();
			} 
			this.weightTensor = this.AI.getWeightTensor();
			
			//setup file writer
			BufferedWriter br;

			//create file ending in .csv
			br = new BufferedWriter(new FileWriter(this.url+".csv"));
			StringBuilder sb = new StringBuilder();
			
			//loop through weight tensor and save each matrix
			for(int layer = 0; layer < this.weightTensor.length; layer++) {

				//save the dimensions of each matrix above the matrix
				sb.append("DIM,"+this.weightTensor[layer].getWidth() + "," + this.weightTensor[layer].getHeight()+"\n");
				for (int rows = 0; rows < this.weightTensor[layer].getHeight(); rows++) {
					for(int cols = 0; cols < this.weightTensor[layer].getWidth(); cols++) {
						sb.append(this.weightTensor[layer].getElement(rows, cols)+", ");
					}
					sb.append("\n");
				}
				sb.append("-");
				sb.append("\n");
			}
			sb.append("!");
			
			//finished
			br.write(sb.toString());
			br.close();
			
		}catch(Exception e1) {
			JOptionPane.showMessageDialog(null, "null error, file may not exist");
		}
	}
	
	public void load() {
		try {
			
			//pick file to load
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
			    File selectedFile = fileChooser.getSelectedFile();
			    this.url = selectedFile.getAbsolutePath();
			} 
			
			//create a list to save file content to
			List<String> fileAsList = new ArrayList<String>();
			
			BufferedReader reader = new BufferedReader(new FileReader(this.url));
			int layer = 0, counter = 0;;
			String line = reader.readLine();
			//saving file to list
			while(line != null) {
				counter++;
				//System.out.println(line);
				fileAsList.add(line);
				if(line.equals("-")) {layer++;}
				line = reader.readLine();
			}
			reader.close();
			
			this.weightTensor = new matrix[layer];
			
			boolean init = true;
			layer = 0;
			int row = 0;
			int rows = 0;
			
			//iterating through each line of list and deciding wether or not its will be saved to a matrix  or not
			for(String str : fileAsList) {
				// split each line of file a comma
				String[] arr = str.split(",");
				
				//checking split strings for certain items
				if(str.equals("-")) {layer++;}
				if(arr[0].contains("DIM")) {
					this.weightTensor[layer] = new matrix(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
					rows = Integer.parseInt(arr[2]);
				}
				
				//Checking of its a dimension, or end of file
				if(!str.contains("DIM") && !str.equals("-") && !str.equals("!")) {
					for(int i = 0; i < arr.length-1; i++) {
						System.out.println(arr[i] + " - " + row + " - " + rows);
						this.weightTensor[layer].setElement(row, i, Double.parseDouble(arr[i]));
					}
					row++;
					if(row >= rows) {
						row=0;
					}
				}

			}
		
			//loading the file contents into an AI
			this.AI = new generateNetwork(this.weightTensor[0].getWidth()+1, this.weightTensor.length-1, this.weightTensor[0].getHeight()+1, this.weightTensor[this.weightTensor.length-1].getHeight()+1, networkParams.alpha,0.85, networkParams.func);
			this.AI.setWeightTensor(this.weightTensor);
			
			//System.out.println(this.weightTensor[0].getWidth() + " - " + this.weightTensor.length + " - " + this.weightTensor[0].getHeight() + " - " + this.weightTensor[this.weightTensor.length-1].getHeight() );
			
			//decideing which enviroment it is based on number of inputs
			switch(this.weightTensor[0].getWidth()){
			case 4:
				Thread executeGame = new Thread(new invertedPendulum(true, this.AI));
				executeGame.start();
				break;
			case 8:
				Thread executeGame1 = new Thread(new droneControl(true, this.AI));
				executeGame1.start();
				break;
			}
			

			
		}catch(Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "File may be corrupt or non existant");
		}
	}
	
}
