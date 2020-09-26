
public enum State {
	empty("0"),
	dust("1"),
	jewelry("2"),
	dustjewelry("3"),
	robot("4");

	private String name = "";

	State(String name){
		this.name = name;
	}
	
	public String tosString() {
		return name; 
	}
}


