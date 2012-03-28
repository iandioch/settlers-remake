package jsettlers.logic.newmovable;

import java.io.Serializable;

import jsettlers.common.material.EMaterialType;
import jsettlers.common.movable.EAction;
import jsettlers.common.movable.EDirection;
import jsettlers.common.movable.EMovableType;
import jsettlers.common.position.ShortPoint2D;

public abstract class NewMovableStrategy implements Serializable {
	private static final long serialVersionUID = 3135655342562634378L;

	private final NewMovable movable;

	protected NewMovableStrategy(NewMovable movable) {
		this.movable = movable;
		// TODO Auto-generated constructor stub
	}

	public static NewMovableStrategy getStrategy(NewMovable movable, EMovableType movableType) {
		switch (movableType) {
		case TEST_MOVABLE:
			return new TestMovableStrategy(movable);

		default:
			return null;
		}
	}

	protected abstract void action();

	protected final void convertTo(EMovableType movableType) {
		movable.convertTo(movableType);
	}

	protected final void setMaterial(EMaterialType materialType) {
		movable.setMaterial(materialType);
	}

	protected final void playAction(EAction movableAction, float duration) { // TODO @Andreas : rename EAction to EMovableAction
		movable.playAction(movableAction, duration);
	}

	protected final void lookInDirection(EDirection direction) {
		movable.lookInDirection(direction);
	}

	protected final boolean goToPos(ShortPoint2D targetPos) {
		return movable.goToPos(targetPos);
	}

	public ShortPoint2D getPosition() {
		return movable.getPos();
	}
}
