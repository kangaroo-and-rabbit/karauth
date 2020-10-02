package io.scenarium.oauth.model;

public enum State {
	// User has remove his account
	REMOVED,
	// User has been blocked his account
	BLOCKED,
	// generic user
	USER,
	// Administrator
	ADMIN
}
