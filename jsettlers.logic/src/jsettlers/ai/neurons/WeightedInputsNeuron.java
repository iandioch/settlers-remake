package jsettlers.ai.neurons;

import java.util.List;
import java.util.Vector;

/**
 * @author codingberlin
 */
public class WeightedInputsNeuron implements Neuron {

	List<Neuron> inputNeurons = new Vector<Neuron>();
	List<Integer> inputNeuronWeights = new Vector<Integer>();
	float output;

	public void addInputNeuron(Neuron neuron) {
		inputNeurons.add(neuron);
		inputNeuronWeights.add(0);
	}

	public List<Integer> getWeights() {
		return inputNeuronWeights;
	}

	public void setWeights(List<Integer> newWeights) {
		inputNeuronWeights = newWeights;
	}

	public void calculateOutput() {
		output = 0;
		for (int i = 0; i<inputNeurons.size(); i++) {
			output += inputNeurons.get(i).getOutput()*inputNeuronWeights.get(i);
		}
		output = output / inputNeurons.size();
	}

	@Override public float getOutput() {
		return output;

	}
}
