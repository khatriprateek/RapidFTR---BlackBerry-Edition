package com.rapidftr.controllers.internal;

import com.rapidftr.controllers.ChildController;
import com.rapidftr.controllers.HomeScreenController;
import com.rapidftr.controllers.LoginController;
import com.rapidftr.controllers.ResetDeviceController;
import com.rapidftr.controllers.SyncController;
import com.rapidftr.model.Child;
import com.rapidftr.process.Process;

public class Dispatcher {
	private final HomeScreenController homeScreenController;
	private final LoginController loginController;
	private final ChildController childController;
	private final SyncController syncController;
	private ResetDeviceController resetDeviceController;

	public Dispatcher(HomeScreenController homeScreenController,
			LoginController loginController, ChildController childController,
			SyncController syncController, ResetDeviceController restController) {
		this.homeScreenController = homeScreenController;
		this.homeScreenController.setDispatcher(this);
		this.loginController = loginController;
		this.loginController.setDispatcher(this);
		this.childController = childController;
		this.childController.setDispatcher(this);
		this.syncController = syncController;
		this.syncController.setDispatcher(this);
		this.resetDeviceController = restController;
	}

	public void homeScreen() {
		homeScreenController.show();
	}

	public void viewChildren() {
		childController.viewChildren();
	}

	public void synchronizeForms() {
		syncController.synchronizeForms();
	}

	public void newChild() {
		childController.newChild();
	}

	public void syncChildRecords() {
		syncController.syncAllChildRecords();
	}

	public void searchChild() {
		childController.showChildSearchScreen();
	}

	public void syncChild(Child child) {
		syncController.syncChildRecord(child);
	}

	public void resetDevice() {
		resetDeviceController.resetDevice();
		loginController.clearLoginScreen();
	}

	public void login(Process process) {
		loginController.showLoginScreen(process);

	}

}
