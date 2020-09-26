
public enum State {
	empty("0"),
	dust("1"),
	robot("2");

	private String name = "";

	State(String name){
		this.name = name;
	}
	
	public String tosString() {
		return name; 
	}
}


