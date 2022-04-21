package MachineLearningFolder.activationFunctions;

import MachineLearningFolder.matrices.vectors.vector;

//all are overridden
public class linear implements functions{

	//linear activation function taking double as input
	@Override
	public double activated(double x) {
		return x;
	}
	//derivative of linear activation function taking double as input
	@Override
	public double derived(double x) {
		return 1;
	}
	//linear activation function taking vector as input
	@Override
	public vector activated(vector inputArray) {
		return inputArray;
	}
	//derivative of linear activation function taking vector  as input
	@Override
	public vector derived(vector inputArray) {
		vector tempArray = new vector(inputArray.getSize());
		for(int id = 0; id < inputArray.getSize(); id++) {
				tempArray.setElement(id, (double) (1.0d));
		}
		return tempArray;
	}

}
