package MachineLearningFolder.matrices.matrix;

import MachineLearningFolder.matrices.vectors.*;


public interface matrixMaths {

	//adding to matrices together
	public default matrix matrixAddition(matrix matrixAlpha, matrix matrixBeta){
		
		matrix Matrix = new matrix(matrixAlpha.getWidth(), matrixBeta.getHeight());
		
		//iterate through each index of both matrixs and sum them
		for(int rows = 0; rows < Matrix.getHeight(); rows++) {
			for(int cols = 0; cols < Matrix.getWidth(); cols++) {
				Matrix.setElement(rows, cols, matrixAlpha.getElement(rows, cols) + matrixBeta.getElement(rows, cols));
			}
		}
		
		return Matrix;
	}
	
	//element wise division on a matrix
	public default matrix elementWiseDivision(matrix matrixAlpha, matrix matrixBeta) {
		matrix Matrix = new matrix(matrixAlpha.getWidth(), matrixBeta.getHeight());
		
		//iterate through each index of both and divide them
		for(int rows = 0; rows < Matrix.getHeight(); rows++) {
			for(int cols = 0; cols < Matrix.getWidth(); cols++) {
				Matrix.setElement(rows, cols, matrixAlpha.getElement(rows, cols) / matrixBeta.getElement(rows, cols));
			}
		}
		
		return Matrix;
	}
	
	//do dot product on two matrix
	public default matrix matrixDot(matrix matrixAlpha, matrix matrixBeta) throws Exception{
		//check if the matrices are compatible
		if(matrixAlpha.getHeight() != matrixBeta.getWidth()){
			throw new java.lang.Exception("Matrix size are incompatible");
		}
		matrix result = new matrix(matrixBeta.getWidth(), matrixAlpha.getHeight());
		double sum = 0.0d;
		//perform dot product on them
		for(int r = 0; r < result.getHeight(); r++){
			for(int c = 0; c < result.getWidth(); c++){
				for(int mult = 0; mult < matrixAlpha.getHeight(); mult++){

					sum+=matrixAlpha.getElement(r, mult) * matrixBeta.getElement(mult, c);

				}
				result.setElement(r, c, sum);
				sum=0;
			}
		}

		return result;
	}
	
	//matrix dot between matrix and vector
	public default vector matrixDot(matrix Matrix, vector Vector) throws Exception{
		
		//check if compatible
		if(Vector.getSize() != Matrix.getWidth()) {
			throw new java.lang.Exception("Matrix and Vector size mismatch");
		}
		
		vector result = new vector(Matrix.getHeight());
		double sum = 0;
		
		//iterate thorough mmultiply and sum.
		for(int rows = 0; rows < Matrix.getHeight(); rows++) {
			for(int columns = 0; columns < Matrix.getWidth(); columns++) {
				
				sum += Matrix.getElement(rows, columns) * Vector.getElement(columns);
				
			}
			result.setElement(rows, sum);
			sum=0;
		}
		
		return result;
	}

	//raise a matrix to the power of another
	public default matrix matrixPow(matrix matrixAlpha, int power) {
		
		matrix powMat = new matrix(matrixAlpha.getWidth(), matrixAlpha.getHeight());
		for(int row = 0; row < matrixAlpha.getHeight(); row++) {
			for(int col = 0; col < matrixAlpha.getWidth(); col++) {
				powMat.setElement(row, col, Math.pow(matrixAlpha.getElement(row, col), power));
			}
		}
		return powMat;
		
	}
	
	//calculate the root of a matrix
	public default matrix matrixRoot(matrix matrixAlpha) {
		
		matrix powMat = new matrix(matrixAlpha.getWidth(), matrixAlpha.getHeight());
		for(int row = 0; row < matrixAlpha.getHeight(); row++) {
			for(int col = 0; col < matrixAlpha.getWidth(); col++) {
				powMat.setElement(row, col, Math.sqrt((matrixAlpha.getElement(row, col))));
			}
		}
		return powMat;
		
	}
	
	//scale a matrix
	public default matrix scale(matrix matrixAlpha, double scalar){
		
		matrix temp = new matrix(matrixAlpha.getWidth(), matrixAlpha.getHeight());
		
		for(int rows = 0; rows < matrixAlpha.getHeight(); rows++) {
			for(int cols = 0; cols < matrixAlpha.getWidth(); cols++) {
				temp.setElement(rows, cols, matrixAlpha.getElement(rows, cols)*scalar);
			}
		}
		
		return temp;
	}
	
	//add a scalar to each element in a matrix
	public default matrix scalarAddition(matrix matrixAlpha, double scalar) {
		
		matrix temp = new matrix(matrixAlpha.getWidth(), matrixAlpha.getHeight());
		
		for(int rows = 0; rows < matrixAlpha.getHeight(); rows++) {
			for(int cols = 0; cols < matrixAlpha.getWidth(); cols++) {
				temp.setElement(rows, cols, matrixAlpha.getElement(rows, cols) + scalar);
			}
		}
		
		return temp;
	}
	
}
