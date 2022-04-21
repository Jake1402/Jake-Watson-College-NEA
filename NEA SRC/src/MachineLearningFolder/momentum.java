package MachineLearningFolder;

import MachineLearningFolder.matrices.matrix.*;
import MachineLearningFolder.matrices.vectors.*;

//
//	An optimiser for the drone control algorithm
//	to try and find a solution for problems faster
//	hence the use of the momentum optimiser
//	more information on this can be found here
//
//	https://towardsdatascience.com/a-look-at-gradient-descent-and-rmsprop-optimizers-f77d483ef08b
//

public class momentum {

	//momentum weights
	matrix vdw;
	//beta, deciding how much momentum
	double beta;
	
	//momentum constructor
	public momentum(double beta, matrix dw) {
		this.beta = beta;
		this.vdw = new matrix(dw.getWidth(), dw.getHeight());
		this.vdw.fillZeros();
	}
	
	//calculating and applying momentum
	public matrix applyMomentum(matrix dw) {
		matrix tempMat = this.vdw.scale(this.vdw, this.beta);
		tempMat = tempMat.matrixAddition(tempMat, dw.scale(dw, (1 - this.beta)));
		this.vdw = tempMat;
		return this.vdw;
	}
}
