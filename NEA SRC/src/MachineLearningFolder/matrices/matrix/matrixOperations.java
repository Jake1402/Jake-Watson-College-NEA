package MachineLearningFolder.matrices.matrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import MachineLearningFolder.matrices.vectors.*;

public abstract class matrixOperations {
	
	protected double[][] MATRIX_ARRAY;
	
	//set higher matrix
	protected void setHighArray(double[][] higherArray ) {
		this.MATRIX_ARRAY = higherArray;
	}
	
	//transpose the matrix
	public matrix transpose() {
		
		matrix matrixT = new matrix(MATRIX_ARRAY.length, MATRIX_ARRAY[0].length);
		
		for(int rows = 0; rows < matrixT.getHeight(); rows++) {
			for(int cols = 0; cols < matrixT.getWidth(); cols++) {
				matrixT.setElement(rows, cols, MATRIX_ARRAY[cols][rows]);
			}
		}
		
		return matrixT;
	}
	
	//print out the matrix
	public void print() {
		for(int rows = 0; rows < MATRIX_ARRAY.length; rows++) {
			for(int cols = 0; cols < MATRIX_ARRAY[0].length; cols++) {
				
				System.out.print(MATRIX_ARRAY[rows][cols]+", ");
			
			}
			System.out.println();
		}
	}
	
	//get the dimensions of a matrix
	public void shape() {
		System.out.println("SHPAE : { " + this.MATRIX_ARRAY.length + ", " + this.MATRIX_ARRAY[0].length + " }");
	}
	
	
	//turn a matrix into a vecotr
	public vector flatten() {
		vector flattMat = new vector(this.MATRIX_ARRAY.length*this.MATRIX_ARRAY[0].length);
		int pos = 0;
		for(int i = 0; i < MATRIX_ARRAY.length; i ++) {
			for(int s = 0; s < MATRIX_ARRAY[0].length; s ++) {
				flattMat.setElement((i * MATRIX_ARRAY.length) + s,MATRIX_ARRAY[i][s]);
			}
		}
		return flattMat;
	}
	
}
