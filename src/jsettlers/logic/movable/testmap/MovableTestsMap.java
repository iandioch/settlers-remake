package jsettlers.logic.movable.testmap;

import java.util.LinkedList;

import jsettlers.common.Color;
import jsettlers.common.CommonConstants;
import jsettlers.common.landscape.ELandscapeType;
import jsettlers.common.landscape.EResourceType;
import jsettlers.common.map.IGraphicsBackgroundListener;
import jsettlers.common.map.IGraphicsGrid;
import jsettlers.common.mapobject.EMapObjectType;
import jsettlers.common.mapobject.IMapObject;
import jsettlers.common.material.EMaterialType;
import jsettlers.common.material.ESearchType;
import jsettlers.common.movable.EDirection;
import jsettlers.common.movable.IMovable;
import jsettlers.common.position.ShortPoint2D;
import jsettlers.logic.algorithms.path.IPathCalculateable;
import jsettlers.logic.algorithms.path.Path;
import jsettlers.logic.algorithms.path.astar.normal.HexAStar;
import jsettlers.logic.algorithms.path.astar.normal.IAStarPathMap;
import jsettlers.logic.map.newGrid.partition.manager.manageables.IManageableBearer;
import jsettlers.logic.map.newGrid.partition.manager.manageables.IManageableBricklayer;
import jsettlers.logic.map.newGrid.partition.manager.manageables.IManageableDigger;
import jsettlers.logic.map.newGrid.partition.manager.manageables.IManageableWorker;
import jsettlers.logic.map.newGrid.partition.manager.manageables.interfaces.IMaterialRequester;
import jsettlers.logic.newmovable.NewMovable;
import jsettlers.logic.newmovable.interfaces.INewMovableGrid;
import jsettlers.logic.objects.stack.StackMapObject;
import random.RandomSingleton;

public class MovableTestsMap implements IGraphicsGrid, IAStarPathMap {

	private final short width;
	private final short height;

	private final NewMovable movableMap[][];
	private final EMaterialType materialTypeMap[][];
	private final byte materialAmmountMap[][];
	private final HexAStar aStar;

	public MovableTestsMap(int width, int height) {
		this.width = (short) width;
		this.height = (short) height;

		this.movableMap = new NewMovable[width][height];
		this.materialTypeMap = new EMaterialType[width][height];
		this.materialAmmountMap = new byte[width][height];

		aStar = new HexAStar(this, this.width, this.height);
	}

	@Override
	public short getHeight() {
		return height;
	}

	@Override
	public int nextDrawableX(int x, int y) {
		return x + 1;
	}

	@Override
	public short getWidth() {
		return width;
	}

	@Override
	public IMovable getMovableAt(int x, int y) {
		return movableMap[x][y];
	}

	@Override
	public IMapObject getMapObjectsAt(int x, int y) {
		if (materialTypeMap[x][y] != null && materialAmmountMap[x][y] > 0) {
			return new StackMapObject(materialTypeMap[x][y], materialAmmountMap[x][y]);
		} else {
			return null;
		}
	}

	@Override
	public byte getHeightAt(int x, int y) {
		return 0;
	}

	@Override
	public ELandscapeType getLandscapeTypeAt(int x, int y) {
		return ELandscapeType.GRASS;
	}

	@Override
	public int getDebugColorAt(int x, int y) {
		return -1;
	}

	@Override
	public boolean isBorder(int x, int y) {
		return false;
	}

	@Override
	public byte getPlayerAt(int x, int y) {
		return 0;
	}

	@Override
	public byte getVisibleStatus(int x, int y) {
		return CommonConstants.FOG_OF_WAR_VISIBLE;
	}

	@Override
	public boolean isFogOfWarVisible(int x, int y) {
		return true;
	}

	@Override
	public void setBackgroundListener(IGraphicsBackgroundListener backgroundListener) {
	}

	private final INewMovableGrid movableGrid = new INewMovableGrid() {
		@Override
		public void leavePosition(ShortPoint2D position, NewMovable movable) {
			if (movableMap[position.getX()][position.getY()] == movable) {
				movableMap[position.getX()][position.getY()] = null;
			}
		}

		@Override
		public boolean hasNoMovableAt(short x, short y) {
			return isInBounds(x, y) && movableMap[x][y] == null;
		}

		@Override
		public boolean isInBounds(short x, short y) {
			return 0 <= x && x < width && 0 <= y && y < height;
		}

		@Override
		public void enterPosition(ShortPoint2D position, NewMovable movable) {
			movableMap[position.getX()][position.getY()] = movable;
		}

		@Override
		public Path calculatePathTo(IPathCalculateable pathRequester, ShortPoint2D targetPos) {
			return aStar.findPath(pathRequester, targetPos);
		}

		@Override
		public void addJobless(IManageableBearer bearer) {
			if (!materials.isEmpty()) {
				ShortPoint2D source = materials.pop();
				final ShortPoint2D targetPos = new ShortPoint2D(RandomSingleton.getInt(0, width - 1), RandomSingleton.getInt(0, height - 1));
				bearer.executeJob(source, new IMaterialRequester() {
					@Override
					public void requestFailed() {
					}

					@Override
					public boolean isRequestActive() {
						return true;
					}

					@Override
					public ShortPoint2D getPos() {
						return targetPos;
					}
				}, materialTypeMap[source.getX()][source.getY()]);
			}
		}

		private LinkedList<ShortPoint2D> materials = new LinkedList<ShortPoint2D>();

		@Override
		public boolean takeMaterial(ShortPoint2D pos, EMaterialType materialType) {
			if (materialTypeMap[pos.getX()][pos.getY()] == materialType && materialAmmountMap[pos.getX()][pos.getY()] > 0) {
				materialAmmountMap[pos.getX()][pos.getY()]--;
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean dropMaterial(ShortPoint2D pos, EMaterialType materialType, boolean offer) {
			materialTypeMap[pos.getX()][pos.getY()] = materialType;
			materialAmmountMap[pos.getX()][pos.getY()]++;

			materials.add(pos);

			return true;
		}

		@Override
		public NewMovable getMovableAt(short x, short y) {
			return movableMap[x][y];
		}

		@Override
		public byte getPlayerAt(short x, short y) {
			return 0;
		}

		@Override
		public boolean isBlocked(short x, short y) {
			return false;
		}

		@Override
		public void addJobless(IManageableWorker worker) {
		}

		@Override
		public void addJobless(IManageableDigger digger) {
		}

		@Override
		public float getResourceAmountAround(short x, short y, EResourceType type) {
			return 0;
		}

		@Override
		public EDirection getDirectionOfSearched(ShortPoint2D position, ESearchType searchType) {
			return null;
		}

		@Override
		public boolean executeSearchType(ShortPoint2D pos, ESearchType searchType) {
			return false;
		}

		@Override
		public EMaterialType popToolProductionRequest(ShortPoint2D pos) {
			return null;
		}

		@Override
		public void placePigAt(ShortPoint2D pos, boolean place) {
		}

		@Override
		public boolean hasPigAt(ShortPoint2D position) {
			return false;
		}

		@Override
		public boolean isPigAdult(ShortPoint2D position) {
			return false;
		}

		@Override
		public void placeSmoke(ShortPoint2D position, boolean smokeOn) {
		}

		@Override
		public boolean canPushMaterial(ShortPoint2D position) {
			return false;
		}

		@Override
		public boolean canPop(ShortPoint2D position, EMaterialType material) {
			return false;
		}

		@Override
		public byte getHeightAt(ShortPoint2D position) {
			return 0;
		}

		@Override
		public boolean isMarked(ShortPoint2D position) {
			return false;
		}

		@Override
		public void setMarked(ShortPoint2D position, boolean marked) {
		}

		@Override
		public Path searchDijkstra(IPathCalculateable pathCalculateable, short centerX, short centerY, short radius, ESearchType searchType) {
			return null;
		}

		@Override
		public Path searchInArea(IPathCalculateable pathCalculateable, short centerX, short centerY, short radius, ESearchType searchType) {
			return null;
		}

		@Override
		public void addJobless(IManageableBricklayer bricklayer) {
		}

		@Override
		public void addSelfDeletingMapObject(ShortPoint2D position, EMapObjectType mapObjectType, int duration, byte player) {
		}

		@Override
		public void changeHeightTowards(short x, short y, byte targetHeight) {
		}

		@Override
		public boolean isValidPosition(IPathCalculateable pathRequester, ShortPoint2D position) {
			short x = position.getX(), y = position.getY();
			return isInBounds(x, y) && !isBlocked(x, y) && (!pathRequester.needsPlayersGround() || pathRequester.getPlayer() == getPlayerAt(x, y));
		}

		@Override
		public boolean isProtected(short x, short y) {
			return false;
		}

		@Override
		public boolean isBlockedOrProtected(short x, short y) {
			return isBlocked(x, y) || isProtected(x, y);
		}

		@Override
		public boolean fitsSearchType(IPathCalculateable pathCalculateable, ShortPoint2D pos, ESearchType searchType) {
			return false;
		}

		@Override
		public void changePlayerAt(ShortPoint2D pos, byte player) {
		}

		@Override
		public void removeJobless(IManageableBearer bearer) {
		}

		@Override
		public void removeJobless(IManageableWorker worker) {
		}

		@Override
		public void removeJobless(IManageableDigger digger) {
		}

		@Override
		public void removeJobless(IManageableBricklayer bricklayer) {
		}

		@Override
		public ELandscapeType getLandscapeTypeAt(short x, short y) {
			return ELandscapeType.GRASS;
		}

	};

	public INewMovableGrid getMovableGrid() {
		return movableGrid;
	}

	// ==================== IAStarPathMap ==============================================================

	@Override
	public boolean isBlocked(IPathCalculateable requester, short x, short y) {
		return false;
	}

	@Override
	public float getCost(short sx, short sy, short tx, short ty) {
		return 1;
	}

	@Override
	public void markAsOpen(short x, short y) {
	}

	@Override
	public void markAsClosed(short x, short y) {
	}

	@Override
	public void setDebugColor(short x, short y, Color color) {
	}
}
