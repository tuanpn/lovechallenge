package vn.sunnet.game.components;

import java.util.Map;

import vn.sunnet.game.entities.Component;
import vn.sunnet.game.entities.Entity;

public class FollowCameraComponent extends Component {

	@Override
	public void onRemove() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setup() {
		Entity e = getOwner();
		e.getLevel().setLookAt(e);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void configure(Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

}
