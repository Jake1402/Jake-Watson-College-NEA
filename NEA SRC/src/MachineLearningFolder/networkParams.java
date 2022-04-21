package MachineLearningFolder;

//class to create static variables for networks
//so we can build networks without having to pass
//objects over and over again or pass through threads :)
//not important enough to require getters & setters

public class networkParams {
	
	//all static public variables 
	public static int inputs;
	public static int layers;
	public static int density;
	public static int outputs;
	public static double alpha;
	public static double epsilon;
	public static Object func;
	
	public networkParams(int layers, int density, double alpha, double epsilon, Object func){
		networkParams.layers = layers;
		networkParams.density = density;
		networkParams.alpha = alpha;
		networkParams.epsilon = epsilon;
		networkParams.func = func;
	}
}
