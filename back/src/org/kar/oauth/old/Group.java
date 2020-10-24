package org.kar.oauth.old;

public class Group {
	// Unique ID of the group
	public long id;
	// Unique ID of the parent group
	public long parentGroupId = -1;
	// Name of the group (Must be unique)
	public String groupName;
}
