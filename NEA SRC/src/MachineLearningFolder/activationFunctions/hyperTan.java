package MachineLearningFolder.activationFunctions;

import MachineLearningFolder.matrices.vectors.*;


public class hyperTan implements functions{
	//gave TanH own method to save space on 
	public double activated(double x) {
		double numerator =   (double) (Math.exp(x) - Math.exp(-x));
		double denominator = (double) (Math.exp(x) + Math.exp(-x));
		return (double)(numerator/denominator);
	}
	
	public double derived(double x) {
		return (double) (1 - Math.pow(this.activated(x), 2));
	}
	
	//returns the activated TanH function
	public vector activated(vector inputArray) {
		vector VectorTemp = new vector(inputArray.getSize());
		for(int id = 0; id < inputArray.getSize(); id++) {
				VectorTemp.setElement(id, this.activated(inputArray.getElement(id)));
		}
		return VectorTemp;
	}
	
	//returns the derivative tanh function
	public vector derived(vector inputArray) {
		vector VectorTemp = new vector(inputArray.getSize());
		for(int id = 0; id < inputArray.getSize(); id++) {
				VectorTemp.setElement(id, (double) (1 - Math.pow(this.activated(inputArray.getElement(id)), 2)));
		}
		return VectorTemp;
	}
}
