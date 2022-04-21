package MachineLearningFolder.matrices.matrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import MachineLearningFolder.matrices.vectors.*;

public class matrix extends matrixOperations implements matrixMaths{

	private Random random = new Random((int) new Date().getTime());
	
	private double[][] generatedMatrix;
	private vector properties = new vector(2);
	
	matrix Matrix = this;
	
	//getters and setters
	public vector getProperties() {
		return properties;
	}
	public int getWidth() {
		return this.generatedMatrix[0].length;
	}
	public int getHeight() {
		return this.generatedMatrix.length;
	}
	public double getElement(int row, int column) {
		return this.generatedMatrix[row][column];
	}
	
	
	public void setMatrix(double[][] array) {
		properties.setElement(0, array[0].length);
		properties.setElement(1, array.length);
		this.generatedMatrix = array;
		this.setHighArray(this.generatedMatrix);
	}
	public void setMatrix(matrix Matrix) {
		this.generatedMatrix = Matrix.MatrixToArray();
		this.setHighArray(this.generatedMatrix);
	}	
	public void setElement(int row, int column, double value) {
		this.generatedMatrix[row][column] = value;
		this.setHighArray(this.generatedMatrix);
	}
	
	
	//constructor
	public matrix(int width, int height) {
		//creating a 2d array and doing operations on that
		this.generatedMatrix = new double[height][width];
		properties.setElement(0, width);
		properties.setElement(1, height);
		this.setHighArray(this.generatedMatrix);
	}
	
	//returning the array
	public double[][] MatrixToArray() {
		return this.generatedMatrix;
	}
	
	//fill matrix with gaussian random numbers
	public void gaussianRandom(int gaussian) {
		for(int row = 0; row < properties.getElement(1); row++) {
			for(int column = 0; column < properties.getElement(0); column++) {
				this.generatedMatrix[row][column] = 0.0d;
				for(int randomN = 0; randomN < gaussian; randomN++) {
					this.generatedMatrix[row][column] = (double)((random.nextDouble()*-2)+1);
				}
				this.generatedMatrix[row][column] = this.generatedMatrix[row][column]/gaussian;
			}
		}
		//setting it to the array of the matrix operations class 
		this.setHighArray(this.generatedMatrix);
	}
	
	//fill matrix with ranodm numbers
	public void populateMatrix(int upperBound, int lowerBound) {
		for(int row = 0; row < properties.getElement(1); row++) {
			for(int column = 0; column < properties.getElement(0); column++) {
				this.generatedMatrix[row][column] = (double)((random.nextDouble()*-(lowerBound-upperBound))+lowerBound);
			}
		}
		//setting it to the array of the matrix operations class 
		this.setHighArray(this.generatedMatrix);
	}
	
	//fill matrix with zeros
	public void fillZeros() {
		for(int row = 0; row < properties.getElement(1); row++) {
			for(int column = 0; column < properties.getElement(0); column++) {
				this.generatedMatrix[row][column] = 0.0d;
			}
		}
		this.setHighArray(this.generatedMatrix);
	}
	
	//save matrix to file
	public void save(String savePath) throws IOException {
		BufferedWriter br;

		//setting up file
		br = new BufferedWriter(new FileWriter(savePath));
		StringBuilder sb = new StringBuilder();
		
		//writing each index in array to the file
		for (int rows = 0; rows < this.MATRIX_ARRAY.length; rows++) {
			for(int cols = 0; cols < this.MATRIX_ARRAY[0].length; cols++) {
				sb.append(this.MATRIX_ARRAY[rows][cols]+", ");
			}
			sb.append("\n");
		}
		
		sb.append("!");

		br.write(sb.toString());
		br.close();

	}
	
	//load matrix from file
	public void load(String loadPath) throws Exception {
		
		BufferedReader reader = new BufferedReader(new FileReader(loadPath));
		int rows = 1, cols = 0;
		String line;
		line = reader.readLine();
		cols = line.split(",").length-1;
		
		while (reader.readLine() != null) {rows++;}
		System.out.println(cols + " - " + rows);
		reader.close();
		
		double[][] doubleArr = new double[rows][cols];
		
		int index = 0;
		
		File csv = new File(loadPath);
		Scanner csvFile = new Scanner(csv);
		
		//reading each line of file
		while(csvFile.hasNext()) {
			System.out.println("Top");
			System.out.println(index);
			String[] stringSplit = csvFile.nextLine().split(", ");
			
			for(int csvLineIndex = 0; csvLineIndex < stringSplit.length; csvLineIndex++) {

				doubleArr[index][csvLineIndex] = Double.parseDouble(stringSplit[csvLineIndex]);
				System.out.println(Double.parseDouble(stringSplit[csvLineIndex]));
			}
			index++;
			
			if(csvFile.hasNext("!")) {break;}
			
		}
		
		matrix Matrix = new matrix(cols, rows);
		
	}
	
}