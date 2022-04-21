package MachineLearningFolder.matrices.vectors;

import java.util.Date;
import java.util.Random;

//"inherits" from two other clases
public class vector extends vectorOperations implements vectorMaths{
	
	//globals
	private double[] Vector;
	//uses data for random seed
	private Random random = new Random((int) new Date().getTime());
	
	//getters and setters
	public int getSize() {
		return Vector.length;
	}
	
	public void setElement(int element, double value) {
		this.Vector[element] = value;
		this.VECTOR_ARRAY = this.Vector;
	}
	public double getElement(int element) {
		return this.Vector[element];
	}
	
	public double[] getVectorList() {
		return this.Vector;
	}
	 
	public void setVector(double[] valuesForVector) throws Exception {
		if(valuesForVector.length > this.Vector.length) {
			throw new java.lang.Error("Inputted vector is larger than Vector.");
		}

		this.Vector = valuesForVector;
		this.VECTOR_ARRAY = valuesForVector;
	}

	
	//~~~~~~~~~~===========Constructor=============~~~~~~~~~~\\

	
	
	public vector(int elements) {
		this.Vector = new double[elements];
		for(int it = 0; it < elements; it++) {
			this.Vector[it] = 0.0d;
		}
		this.VECTOR_ARRAY = this.Vector;
	}
	
	//~~~~~~~~~~============Main methids============~~~~~~~~~~\\
	
	//gaussian random (normalised random numbers)
	public void gaussianRandom() {
		for(int element = 0; element < this.Vector.length; element++) {
			for(int randomN = 0; randomN < 25; randomN++) {
				this.Vector[element] += (double)((random.nextDouble()*-2)+1);
			}
			this.Vector[element] = this.Vector[element]/25;
		}
		this.VECTOR_ARRAY = this.Vector;
	}
	
	//fills vector with random numbers
	public void populateVector(int upperBound, int lowerBound) {
		for(int element = 0; element < this.Vector.length; element++) {
			this.Vector[element] = (double)((random.nextDouble()*-(lowerBound-upperBound))+lowerBound);
		}
		this.VECTOR_ARRAY = this.Vector;
	}
	
	//replace vector with another
	public void replaceVector(vector updatedVector) {
		this.Vector = this.vectorToArray(updatedVector);
		this.VECTOR_ARRAY = this.Vector;
	}
	
}
