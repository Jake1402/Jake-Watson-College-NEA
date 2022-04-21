package MachineLearningFolder.matrices.vectors;

import MachineLearningFolder.matrices.matrix.*;

import java.util.Objects;

public interface vectorMaths {

	//this function doesnt do anything
	public default int argmax(vector Vector1) {
		return 1;
	}
	
	//add two vectors
	public default vector vectorAddition(vector Vector1, vector Vector2) {
		
		if(Vector1.getSize() >= Vector2.getSize()) {
			vector tempVector = new vector(Vector1.getSize());
			for(int i = 0; i < Vector1.getSize(); i++) {
				if(i < Vector2.getSize()) {
					tempVector.setElement(i, Vector1.getElement(i)+Vector2.getElement(i));
				}else {
					tempVector.setElement(i, Vector1.getElement(i));
				}
			}
			return tempVector;
		}else {
			vector tempVector = new vector(Vector2.getSize());
			for(int i = 0; i < Vector2.getSize(); i++) {
				if(i < Vector1.getSize()) {
					tempVector.setElement(i, Vector1.getElement(i)+Vector2.getElement(i));
				}else {
					tempVector.setElement(i, Vector2.getElement(i));
				}
			}
			return tempVector;
		}
	}

	//multiply two vectors together by their elements
	public default vector elementWiseMultiplication(vector A, vector B) throws Exception{
		if(A.getSize()!=B.getSize()) throw new java.lang.Exception("Vectors size mismatch");
		vector result = new vector(A.getSize());
		for(int pos = 0; pos < result.getSize(); pos++){ result.setElement(pos, A.getElement(pos)*B.getElement(pos)); }
		return result;
	}

	public default vector vectorSubtraction(vector Vector1, vector Vector2) {
		vector tempVector = new vector(Vector1.getSize());
		for(int i = 0; i < Vector1.getSize(); i++) {
			tempVector.setElement(i, Vector1.getElement(i) - Vector2.getElement(i));
		}
		
		return tempVector;
			
	}
	
	//vector dot product to return a single values
	public default double vectorDot(vector Vector1, vector Vector2) throws Exception{
		
		//check if vectors are compatible
		if(Vector1.getSize() != Vector2.getSize()) {
			throw new java.lang.Exception("Vector size mismatch");
		}else {
			double dotSum = 0.0f;
			for(int i = 0; i < Vector1.getSize(); i++) {
				dotSum += (double)(Vector1.getElement(i)*Vector2.getElement(i));
			}
			return dotSum;
		}
	}
	
	//return a matrix when doing vector dot
	public default matrix vectorDotToMatrix(vector Vector1, vector Vector2) throws Exception{
		
		if(Objects.isNull(Vector1) || Objects.isNull(Vector2)) {
			throw new java.lang.Exception("Null vector was given!");
		}
		
		matrix Matrix = new matrix(Vector2.getSize(), Vector1.getSize());
		
		for(int rows = 0; rows < Vector1.getSize(); rows++) {
			for(int cols = 0; cols < Vector2.getSize(); cols++) {
				Matrix.setElement(rows, cols, Vector1.getElement(rows)*Vector2.getElement(cols));
			}
		}
		
		return Matrix;
		
	}
	
	//scales a vector
	public default vector scale(vector Vector, double scalar) {
		vector tempVector = new vector(Vector.getSize());
		for(int rows = 0; rows < Vector.getSize(); rows++) {tempVector.setElement(rows, Vector.getElement(rows)*scalar);}
		return tempVector;
	}
	
}