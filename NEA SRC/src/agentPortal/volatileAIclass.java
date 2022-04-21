package agentPortal;

import MachineLearningFolder.generateNetwork;

//
//A volatile static class so variables
//can be shared between threads and remain 
//static among all of them.
//
public class volatileAIclass {
	public static volatile byte env;
	public static volatile generateNetwork AI;
}
