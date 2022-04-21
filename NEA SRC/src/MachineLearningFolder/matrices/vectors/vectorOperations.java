package MachineLearningFolder.matrices.vectors;

import MachineLearningFolder.matrices.matrix.*;

public abstract class vectorOperations {
	
	//high level vector
	protected double[] VECTOR_ARRAY;
	
	//returns the max value in a vector
	public int argmax() {
		int position = 0;
		double highValue = 0;
		for(int i = 0; i<VECTOR_ARRAY.length; i++) {
			
			if(i==0) {position=i;highValue=VECTOR_ARRAY[i]; continue;}
			
			if(VECTOR_ARRAY[i] > highValue) {
				highValue = VECTOR_ARRAY[i];
				position=i;
			}
		}
		
		return position;
	}
	
	//turns matrix to vector (doesnt do anything)
	public vector flattenMatrix(matrix inputMatrix) {
		int values;
		return new vector(2);
	}
	
	//turns vector into array
	public double[] vectorToArray(vector Vector) {
		
		double[] temp = new double[Vector.getSize()];
		for(int i = 0; i < Vector.getSize(); i++) {
			temp[i] = (double)(Vector.getElement(i));
		}
		
		return temp;
	}
	
	//turns vector in to matrix
	public matrix toMatrix() {
		matrix conv = new matrix(0,this.VECTOR_ARRAY.length);
		conv.setMatrix(new double[][]{this.VECTOR_ARRAY});
		return conv;
	}
	
	//prints vector either horizontally or vertically
	public void print(boolean verticalPrint) {
		if(verticalPrint) {
			for(int index = 0; index < this.VECTOR_ARRAY.length; index++) {
				System.out.println(this.VECTOR_ARRAY[index]+",");
			}
		}else {
			for(int index = 0; index < this.VECTOR_ARRAY.length; index++) {
				System.out.print(this.VECTOR_ARRAY[index]+", ");
			}
			System.out.println();
		}
	}
	
}
