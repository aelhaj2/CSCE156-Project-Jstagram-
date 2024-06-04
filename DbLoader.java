package unl.soc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbLoader {
	private final static String hostname = "cse-linux-01.unl.edu";
	private final static String username = "mmandernach2";
	private final static String password = "156project2";
	private final static String url = "jdbc:mysql://" + hostname + "/" + username;
	private static Connection conn = null;
	private static String query = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;
	private final static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd	HH:mm:ss");
	
	/* 
	 * 		Function to load all the user data from the database.
	 */
	
	public static Map<String, User> getUserData() {
		Map<String, User> tableData = new HashMap<>();
		try {
			conn = DriverManager.getConnection(url, username, password);
			query = "select * from Users";
			ps = conn.prepareCall(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String username = rs.getString("username");
				String password = rs.getString("password");
				User user = new User(username, password);
				tableData.put(username, user);
			}
			rs.close();
			ps.close();
			
			query = "select * from Visibility";
			ps = conn.prepareCall(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				String username = rs.getString("username");
				String visibleUser = rs.getString("visibleUser");
				tableData.get(username).visibility.add(visibleUser);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return tableData;
	}
	
	/* 
	 * 		Function to load all the posts from the database.
	 */
	
	public static Map<Integer, Post> getPostData(Map<String, User> userData){
		Map<Integer, Post> postMap = new HashMap<>();
		try {
			conn = DriverManager.getConnection(url, username, password);
			query = "select * from Post";
			ps = conn.prepareCall(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String postText = rs.getString("postText");
				String postUsername  = rs.getString("username");
				LocalDateTime postTime = LocalDateTime.parse(rs.getString("postTime"), 
						timeFormat);
				int postId = rs.getInt("postId");
				int likes = rs.getInt("likes");
				Post newPost = new Post(postText, userData.get(postUsername), postTime, postId, likes);
				postMap.put(postId, newPost);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		
		return postMap;
	}
	
	/* 
	 * 		Function to add a new post the database.
	 */
	
	public static void addPost(Post newPost) {
		try {
			conn = DriverManager.getConnection(url, username, password);
			query = """					
					insert into Post(username, postText, postTime)
							  values(?,?,?);
					""";
			ps = conn.prepareCall(query);
			ps.setString(1, newPost.getPostUser().getUsername());
			ps.setString(2, newPost.getPostText());
			ps.setString(3, newPost.getPostTime().format(timeFormat).toString());
			ps.executeUpdate();			
			ps.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/* 
	 * 		Function to add a new visible user to the current user's list to the database.
	 */
	public static void addVisibleUser(User currentUser, String newUsername) {
		try {
			conn = DriverManager.getConnection(url, username, password);
			query = """					
					insert into Visibility(username, visibleUser)
							  values(?,?);
					""";
			ps = conn.prepareCall(query);
			ps.setString(1, currentUser.getUsername());
			ps.setString(2, newUsername);
			ps.executeUpdate();			
			ps.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* 
	 * 		Function to remove a visible user from the current user's list in the database.
	 */
	
	public static void removeVisibleUser(User currentUser, String removeUsername) {
		try {
			conn = DriverManager.getConnection(url, username, password);
			query = """					
					delete from Visibility where username = ? and visibleUser = ?
					""";
			ps = conn.prepareCall(query);
			ps.setString(1, currentUser.getUsername());
			ps.setString(2, removeUsername);
			ps.executeUpdate();			
			ps.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* 
	 * 		Function to add a like to the selected post in the database.
	 */
	
	public static void likePost(Map<Integer, Post> postMap, int postToLike) {
		try {
			conn = DriverManager.getConnection(url, username, password);
			query = """					
					update Post set likes = (likes + 1) where postId = ?
					""";
			ps = conn.prepareCall(query);
			ps.setInt(1, postMap.get(postToLike).getPostId());
			ps.executeUpdate();			
			ps.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* 
	 * 		Function to remove a like from the selected post in the database.
	 */
	
	public static void unlikePost(Map<Integer, Post> postMap, int postToLike) {
		try {
			conn = DriverManager.getConnection(url, username, password);
			query = """					
					update Post set likes = (likes - 1) where postId = ?
					""";
			ps = conn.prepareCall(query);
			ps.setInt(1, postMap.get(postToLike).getPostId());
			ps.executeUpdate();			
			ps.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/* 
	 * 		Function to create a new user in the database.
	 */
	
	public static void registerUser(User newUser) {
		try {
			conn = DriverManager.getConnection(url, username, password);
			query = """					
					insert into Users(username, password)
							  values(?,?);
					""";
			ps = conn.prepareCall(query);
			ps.setString(1, newUser.getUsername());
			ps.setString(2, newUser.getPassword());
			ps.executeUpdate();			
			ps.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* 
	 * 		Function to remove the current user in the database.
	 */
	
	public static void deleteUser(User currentUser) {
		try {
			conn = DriverManager.getConnection(url, username, password);
			query = """
					delete from Visibility where username = ?;		
					""";
			ps = conn.prepareCall(query);
			ps.setString(1, currentUser.getUsername());
			ps.executeUpdate();	
			query = """
					delete from Post where username = ?;		
					""";
			ps = conn.prepareCall(query);
			ps.setString(1, currentUser.getUsername());
			ps.executeUpdate();	
			query = """
					delete from Users where username = ?;		
					""";
			ps = conn.prepareCall(query);
			ps.setString(1, currentUser.getUsername());
			ps.executeUpdate();	
			ps.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}