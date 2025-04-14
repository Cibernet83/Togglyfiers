package com.sarahk.togglyfiers.api.event;

import com.sarahk.togglyfiers.api.behavior.ToggleBehavior;
import net.neoforged.bus.api.Event;

import java.util.Arrays;
import java.util.List;

public class RegisterToggleBehaviorsEvent extends Event {

	private final List<ToggleBehavior> behaviors;

	public RegisterToggleBehaviorsEvent(List<ToggleBehavior> behaviors) {
		this.behaviors = behaviors;
	}

	public void registerBehavior(ToggleBehavior... behaviors) {
		this.behaviors.addAll(Arrays.stream(behaviors).toList());
	}
}
