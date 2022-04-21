package MachineLearningFolder.activationFunctions;

import MachineLearningFolder.matrices.vectors.*;

public class leakyReLU implements functions{

	//leakyReLu function taking a double
	public double activated(double x) {
		return (double) ((x <= 0) ? x*0.01 : x);
	}

	//derivative of leakyReLu function taking a double
	public double derived(double x) {
		return (double) ((x <= 0) ? 0.01 : 1);
	}
	
	//leakyReLu function taking vector as input
	public vector activated(vector inputArray) {
		vector tempArray = new vector(inputArray.getSize());
		for(int id = 0; id < inputArray.getSize(); id++) {
				tempArray.setElement(id, (double) ((inputArray.getElement(id) < 0) ? inputArray.getElement(id)*0.01 : inputArray.getElement(id)));
		}
		return tempArray;
	}
	
	//derivative of leakyReLu function taking vector as input
	public vector derived(vector inputArray) {
		vector tempArray = new vector(inputArray.getSize());
		for(int id = 0; id < inputArray.getSize(); id++) {
				tempArray.setElement(id, (double) ((inputArray.getElement(id) < 0) ? 0.01 : 1));
		}
		return tempArray;
	}
	
}
