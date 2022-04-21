package MachineLearningFolder;

import MachineLearningFolder.matrices.matrix.*;
import MachineLearningFolder.matrices.vectors.*;
import MachineLearningFolder.activationFunctions.*;
//THIS IS WORKING DONT TOUCH
//ONLY TOUCH BELLMAN STUFF

@SuppressWarnings("unused")
public class generateNetwork {
	
	//global variables
	private int inputs;
	private int layers;
	private int density;
	private int outputs;
	private double alpha;
	private double initAlpha;
	int cnt = 0;
	private Object func;
	
	private matrix[] weightTensor;
	private vector[] outputTensor;
	private vector[] errorTensor;
	private matrix[] deltaTensor;
	private momentum[] p;
	
	private double gamma;
	private int bellmanEquationSize;
	private int memoryLength;
	private vector   rewardVector;
	private vector   actionRec;
	private vector[] observation;
	private vector[] action;
	private vector[] newActionSet;
	
	//getters and setters
	public int getInputs() {
		return this.inputs;
	}
	
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	//two constructors with an override
	public generateNetwork(int inputs, int layers, int density, int outputs, double learningRate, double gamma, Object activation) {
		//all network parameters
		this.inputs = inputs;
		this.layers = layers;
		this.density = density;
		this.outputs = outputs;
		this.alpha = learningRate;
		this.initAlpha = alpha;
		this.gamma = gamma;
		this.func = activation;
		//initialising tensors for training
		this.weightTensor = new matrix[layers+1];
		this.p = new momentum[layers+1];
		this.deltaTensor = new matrix[layers+1];
		this.errorTensor = new vector[layers+2];
		this.outputTensor = new vector[layers+2];
		
		//initiating weight tensors
		for(int layer = 0; layer<this.weightTensor.length; layer++) {
			if(layer == 0) {
				this.weightTensor[layer] = new matrix(this.inputs, this.density);
				this.weightTensor[layer].gaussianRandom(10);
				this.p[layer] = new momentum(0.9d, this.weightTensor[layer]);
				continue;
			}else if(layer==this.weightTensor.length-1) {
				this.weightTensor[layer] = new matrix(this.density, this.outputs);
				this.weightTensor[layer].gaussianRandom(10);
				this.p[layer] = new momentum(0.9d, this.weightTensor[layer]);
				continue;
			}
			this.weightTensor[layer] = new matrix(this.density, this.density);
			this.weightTensor[layer].gaussianRandom(10);
			this.p[layer] = new momentum(0.9d, this.weightTensor[layer]);
		}
	}
	
	//overriding constructor
	public generateNetwork(int inputs, int layers, int density, int outputs, double learningRate, double gamma, Object activation, matrix[] weightTensor) {
		//all network parameters
		this.inputs = inputs;
		this.layers = layers;
		this.density = density;
		this.outputs = outputs;
		this.alpha = learningRate;
		this.initAlpha = alpha;
		this.gamma = gamma;
		this.func = activation;
		
		//initialising tensors for training
		this.weightTensor = weightTensor;
		this.p = new momentum[layers+1];
		this.deltaTensor = new matrix[layers+1];
		this.errorTensor = new vector[layers+2];
		this.outputTensor = new vector[layers+2];
		
		//initiating weight tensors
		for(int layer = 0; layer<this.weightTensor.length; layer++) {
			if(layer == 0) {
				this.weightTensor[layer] = new matrix(this.inputs, this.density);
				this.weightTensor[layer].gaussianRandom(75);
				this.p[layer] = new momentum(0.9d, this.weightTensor[layer]);
				continue;
			}else if(layer==this.weightTensor.length-1) {
				this.weightTensor[layer] = new matrix(this.density, this.outputs);
				this.weightTensor[layer].gaussianRandom(75);
				this.p[layer] = new momentum(0.9d, this.weightTensor[layer]);
				continue;
			}
			this.weightTensor[layer] = new matrix(this.density, this.density);
			this.weightTensor[layer].gaussianRandom(75);
			this.p[layer] = new momentum(0.9d, this.weightTensor[layer]);
		}
	}
	
	//get outputs from network by feeding it information
	public vector feedForward(vector input) throws Exception{
		vector output = input;
		for(int layer = 0; layer < this.weightTensor.length; layer++) {
				output = ((functions)this.func).activated(this.weightTensor[layer].matrixDot(this.weightTensor[layer], output));
		}
		return output;
	}
	
	//training method, 2 versions exist so an overide can occur
	public void train(vector[] Tinputs, vector[] Toutputs, int epochs, int memoryInc) throws Exception{
		//training epochs
		for(int epoch = 0; epoch < epochs; epoch++) {
			//iterate through all training data
			for(int setIndex = 0; setIndex < memoryInc; setIndex++) {
				//feeding untrained network data to train on (output tensor index:0 is initial input)
				this.outputTensor[0] = Tinputs[setIndex];
				for(int layer = 1; layer < this.weightTensor.length+1; layer++) {
					this.outputTensor[layer] = ((functions)this.func).activated(this.weightTensor[layer-1].matrixDot(this.weightTensor[layer-1], this.outputTensor[layer-1]));
				}
				//calculate error of each layer
				for(int errorIndex = this.errorTensor.length-1; errorIndex>=0; errorIndex--) {
					if(errorIndex == this.outputTensor.length-1) {
						this.errorTensor[errorIndex] = this.outputTensor[errorIndex].vectorSubtraction(Toutputs[setIndex], this.outputTensor[errorIndex]);
						this.errorTensor[errorIndex] = this.errorTensor[errorIndex].elementWiseMultiplication(this.errorTensor[errorIndex], ((functions)(this.func)).derived(this.outputTensor[errorIndex]));
					}else {
						this.errorTensor[errorIndex] = this.weightTensor[errorIndex].matrixDot(this.weightTensor[errorIndex].transpose(), this.errorTensor[errorIndex+1]);
						this.errorTensor[errorIndex] = this.errorTensor[errorIndex].elementWiseMultiplication(this.errorTensor[errorIndex], ((functions)(this.func)).derived(this.outputTensor[errorIndex]));
					}
				}
				//calculate delta weights
				for(int deltaIndex = this.deltaTensor.length-1; deltaIndex >= 0; deltaIndex--) {
					this.deltaTensor[deltaIndex] = this.outputTensor[deltaIndex].vectorDotToMatrix(this.errorTensor[deltaIndex+1], this.outputTensor[deltaIndex]);
				}
				//modify weights
				for(int weightPropigationIndex = 0; weightPropigationIndex < this.weightTensor.length; weightPropigationIndex++) {
					this.weightTensor[weightPropigationIndex] = this.weightTensor[weightPropigationIndex].matrixAddition(this.weightTensor[weightPropigationIndex], this.deltaTensor[weightPropigationIndex].scale(this.p[weightPropigationIndex].applyMomentum(this.deltaTensor[weightPropigationIndex]), this.alpha));
				}
			}
		}
	}
	
public void train(vector[] Tinputs, vector[] Toutputs, int epochs) throws Exception{
	//training epochs
		for(int epoch = 0; epoch < epochs; epoch++) {
			//iterate through all training data
			for(int setIndex = 0; setIndex < Tinputs[0].getSize(); setIndex++) {

				//feeding untrained network data to train on (output tensor index:0 is initial input)
				this.outputTensor[0] = Tinputs[setIndex];
				for(int layer = 1; layer < this.weightTensor.length+1; layer++) {
					this.outputTensor[layer] = ((functions)this.func).activated(this.weightTensor[layer-1].matrixDot(this.weightTensor[layer-1], this.outputTensor[layer-1]));
				}
				//calculate error of each layer
				for(int errorIndex = this.errorTensor.length-1; errorIndex>=0; errorIndex--) {
					if(errorIndex == this.outputTensor.length-1) {
						this.errorTensor[errorIndex] = this.outputTensor[errorIndex].vectorSubtraction(Toutputs[setIndex], this.outputTensor[errorIndex]);
						this.errorTensor[errorIndex] = this.errorTensor[errorIndex].elementWiseMultiplication(this.errorTensor[errorIndex], ((functions)(this.func)).derived(this.outputTensor[errorIndex]));
					}else {
						this.errorTensor[errorIndex] = this.weightTensor[errorIndex].matrixDot(this.weightTensor[errorIndex].transpose(), this.errorTensor[errorIndex+1]);
						this.errorTensor[errorIndex] = this.errorTensor[errorIndex].elementWiseMultiplication(this.errorTensor[errorIndex], ((functions)(this.func)).derived(this.outputTensor[errorIndex]));
					}
				}
				//calculate delta weights
				for(int deltaIndex = this.deltaTensor.length-1; deltaIndex >= 0; deltaIndex--) {
					this.deltaTensor[deltaIndex] = this.outputTensor[deltaIndex].vectorDotToMatrix(this.errorTensor[deltaIndex+1], this.outputTensor[deltaIndex]);
				}
				//modify weights
				for(int weightPropigationIndex = 0; weightPropigationIndex < this.weightTensor.length; weightPropigationIndex++) {
					this.weightTensor[weightPropigationIndex] = this.weightTensor[weightPropigationIndex].matrixAddition(this.weightTensor[weightPropigationIndex], this.deltaTensor[weightPropigationIndex].scale(this.deltaTensor[weightPropigationIndex], this.alpha));
				}
			}
		}
	}
	
//setup the Q function
	public void setupBellman(vector rewardVector, vector[] observation, vector[] action, vector actionRec, int memoryLength) {
		this.memoryLength = memoryLength;
		this.rewardVector = rewardVector;
		this.observation = observation;
		this.action = action;
		this.newActionSet = new vector[this.memoryLength];
		//iterate through and initialise a new array of vectors
		for(int i = 0; i < this.memoryLength; i++) {
			this.newActionSet[i] = new vector(this.action[0].getSize());
		}
		this.actionRec = actionRec;
	}
	
	//calculate our q function 
	public vector[] calculateBellman() throws Exception {
		
		//iterate through training data/experience
		for(int timeStep = 0; timeStep<this.memoryLength; timeStep++) {
	
			vector SARSAvec = new vector(2);
			SARSAvec = (this.action[timeStep+1].scale(this.action[timeStep+1], this.gamma));
			SARSAvec.setElement((int)this.actionRec.getElement(timeStep), (double)(SARSAvec.getElement((int)this.actionRec.getElement(timeStep)) + this.rewardVector.getElement(timeStep)));
			this.newActionSet[timeStep].replaceVector(SARSAvec);
			SARSAvec.print(false);
		}
		Thread.sleep(25);	//prevents a buggy looking mess
		return this.newActionSet;
	}
	
	//get the weight tensor, important for saving 
	public matrix[] getWeightTensor() {
		return this.weightTensor;
	}
	//set weight tensor, important for loading
	public void setWeightTensor(matrix[] weightTensor) {
		this.weightTensor = weightTensor;
	}
	
}
