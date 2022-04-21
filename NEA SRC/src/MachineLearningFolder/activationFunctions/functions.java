package MachineLearningFolder.activationFunctions;

import MachineLearningFolder.matrices.vectors.*;


//
//While this interface may appear useless
//it allows us to choose activation functions
//with just 1 line of code, instead of many
//or requiring us to rewrite the program
//

//
//how to use?
//Object obj;
//CONUSTRUCTOR(Object obj)
//	this.obj = obj;
//((functions)this.obj).whatEver()
//In the constructor just pass what ever activation function
//you want in (make it static) e.g. LELU, sigmoid etc...
//

//all of this will be overriden 
//just a nice way to cast 
public interface functions {

	public double activated(double x);

	public double derived(double x);
	
	public vector activated(vector inputArray);
	
	public vector derived(vector inputArray);
	
}
