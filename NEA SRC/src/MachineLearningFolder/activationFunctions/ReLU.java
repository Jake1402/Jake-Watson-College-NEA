package MachineLearningFolder.activationFunctions;

import MachineLearningFolder.matrices.vectors.*;

public class ReLU implements functions{
	
	//ReLU activated taking double as input
	public double activated(double x) {
		return Math.max(0, x);
	}

	//derived ReLU activated taking double as input
	public double derived(double x) {
		// TODO Auto-generated method stub
		return (double) ((x < 0) ? 0 : 1);
	}
	
	//returns the activated ReLu function
	public vector activated(vector inputArray) {
		vector tempArray = new vector(inputArray.getSize());
		for(int id = 0; id < inputArray.getSize(); id++) {
				tempArray.setElement(id, Math.max(0, inputArray.getElement(id)));
		}
		return tempArray;
	}
	
	//returns derivative of ReLu function
	public vector derived(vector inputArray){
		vector tempArray = new vector(inputArray.getSize());
		for(int id = 0; id < inputArray.getSize(); id++) {
			tempArray.setElement(id, (double) ((inputArray.getElement(id) < 0) ? 0.0d : 1.0d));
		}
		return tempArray;
	}
}