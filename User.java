package unl.soc;

import java.util.ArrayList;
import java.util.List;

public class User {
		
	private String username;
	private String password;
	protected List<String> visibility = new ArrayList<>();
	
	
	public User(String username, String password, List<String> visibility){
		this.username = username;
		this.password = password;
		this.visibility = visibility;
	}
	
	public User(String username, String password) {
		this(username, password, new ArrayList<String>());
	}

	
	
	public String getUsername() {
		return this.username;
	}
	public String getPassword() {
		return this.password;
	}
	public List<String> getVisibility() {
		return this.visibility;
	}
}
