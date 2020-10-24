package org.kar.oauth.old;

public class Package {
	// Unique ID of the package/module
	public long id;
	// name of the package
	public String name;
	// version of the package
	public String version;
	// current coverage of the package
	public double coverage;
	// have module deployed
	public long refBinary;
	// have test deployed
	public long refTest;
	// have source deployed
	public long refSource;
	// have java-doc deployed
	public long refJavaDoc;
	
}
