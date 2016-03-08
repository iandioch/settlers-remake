package jsettlers.ai.neurons;

import jsettlers.ai.highlevel.AiStatistics;
import jsettlers.common.buildings.EBuildingType;
import jsettlers.common.material.EMaterialType;

import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * @author codingberlin
 */
public class BuildingIndividual {

	List<NoChangeNeuron> inputNeurons = new Vector<NoChangeNeuron>();
	List<WeightedInputsNeuron> calculatingNeurons = new Vector<WeightedInputsNeuron>();

	public BuildingIndividual() {
		for (int i=0; i < 7; i++) {
			inputNeurons.add(new NoChangeNeuron());
		}
		for (int i=0; i < inputNeurons.size()+1; i++) {
			WeightedInputsNeuron calculatingNeuron = new WeightedInputsNeuron();
			calculatingNeurons.add(calculatingNeuron);
			if (i < inputNeurons.size()) {
				for (Neuron inputNeuron : inputNeurons) {
					calculatingNeuron.addInputNeuron(inputNeuron);
				}
			} else {
				for (int ii = 0; ii < inputNeurons.size(); ii++) {
					calculatingNeuron.addInputNeuron(calculatingNeurons.get(ii));
				}
			}
		}
	}

	public EBuildingType determineNextBuildingToBuild(AiStatistics aiStatistics, byte playerId) {
		inputNeurons.get(0).setInput(aiStatistics.getNumberOfTotalBuildingsForPlayer(playerId) - aiStatistics
				.getNumberOfNotFinishedBuildingsForPlayer(playerId));
		inputNeurons.get(1).setInput(getRatioOrOne(aiStatistics.getTotalNumberOfBuildingTypeForPlayer(EBuildingType.FORESTER, playerId),
				aiStatistics.getTotalNumberOfBuildingTypeForPlayer(EBuildingType.LUMBERJACK, playerId)));
		inputNeurons.get(2).setInput(getRatioOrOne(aiStatistics.getTotalNumberOfBuildingTypeForPlayer(EBuildingType.STONECUTTER, playerId),
				aiStatistics.getTotalNumberOfBuildingTypeForPlayer(EBuildingType.LUMBERJACK, playerId)));
		inputNeurons.get(3).setInput(getRatioOrOne(aiStatistics.getTotalNumberOfBuildingTypeForPlayer(EBuildingType.SAWMILL, playerId),
				aiStatistics.getTotalNumberOfBuildingTypeForPlayer(EBuildingType.LUMBERJACK, playerId)));
		inputNeurons.get(4).setInput(aiStatistics.getNumberOfMaterialTypeForPlayer(EMaterialType.AXE, playerId) - aiStatistics
				.getNumberOfNotFinishedBuildingTypesForPlayer(EBuildingType.LUMBERJACK, playerId));
		inputNeurons.get(5).setInput(aiStatistics.getNumberOfMaterialTypeForPlayer(EMaterialType.PICK, playerId) - aiStatistics
				.getNumberOfNotFinishedBuildingTypesForPlayer(EBuildingType.STONECUTTER, playerId));
		inputNeurons.get(6).setInput(aiStatistics.getNumberOfMaterialTypeForPlayer(EMaterialType.SAW, playerId) - aiStatistics
				.getNumberOfNotFinishedBuildingTypesForPlayer(EBuildingType.SAWMILL, playerId));

		for (WeightedInputsNeuron neuron : calculatingNeurons) {
			neuron.calculateOutput();
		}
		float output = calculatingNeurons.get(calculatingNeurons.size()-1).getOutput();

		if (output < 0) {
			return null;
		}

		int buildingIndex = Math.round(Math.min(output, 3));
		return EBuildingType.values()[buildingIndex];
	}

	private float getRatioOrOne(float dividend, float divisor) {
		if (divisor == 0) {
			return 1;
		}
		return dividend / divisor;
	}

	public void generateRandomWeights() {
		Random random = new Random();
		for (WeightedInputsNeuron neuron : calculatingNeurons) {
			List<Integer> oldWeights = neuron.getWeights();
			for (int i = 0; i < oldWeights.size(); i++) {
				oldWeights.set(i, random.nextInt(9)-4);
			}
			neuron.setWeights(oldWeights);
		}
	}

	@Override public String toString() {
		String result = "";
		for (WeightedInputsNeuron neuron : calculatingNeurons) {
			for (Integer weight : neuron.getWeights()) {
				result += weight + "|";
			}
		}
		return result.substring(0, result.length()-1);
	}

}
