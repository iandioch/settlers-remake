package jsettlers.ai.neurons;

/**
 * @author codingberlin
 */
public class NoChangeNeuron implements Neuron {

	private float input;

	@Override public float getOutput() {
		return input;
	}

	public void setInput(float input) {
		this.input = input;
	}
}
