package ToolVersion;

public class ToolChange {

	private String Rationale;
	private String Version;

	public ToolChange (String aRationale,String aVersion) {
		this.Rationale = aRationale;
		this.Version = aVersion;
	}
	public String getRationale() {
		return Rationale;
	}

	public String getVersion() {
		return Version;
	}
}

