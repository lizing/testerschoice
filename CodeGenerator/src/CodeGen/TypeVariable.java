package CodeGen;

public class TypeVariable{
	private String type;
	private String id;
	
	public TypeVariable(String type, String id) {
		this.type = type;
		this.id = id;
	}
	
	public String getType(){
		return type;
	}
	
	public String getId(){
		return id;
	}
}