package unl.soc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Views {
	public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_Green = "\u001B[32m";
    public static final String ANSI_Yellow = "\u001B[33m";
    public static final String ANSI_Blue = "\u001B[34m";
    public static final String ANSI_Cyan = "\u001B[36m";
    
    
    public static void mainWindow(int dbSize) {
		System.out.printf(ANSI_Cyan + """
				 ======================================== 
				|         Welcome to Jstgram 2.0         |
				|                                        |
				|              *************             |
				|                    *                   |
				|                    *                   |
				|                    *                   |
				|                    *                   |
				|              *     *                   |
				|              *******                   |
				|                                        |
				|  (R) Register                          |
				|  (L) Login                             |
				|                                        |
				| Current number of users in database: %-2d|
				 ========================================
				""" + ANSI_RESET, dbSize);
	}
    
    public static void visibilityWindow(User currentUser){
		
		System.out.printf(ANSI_Cyan+"""
				 ===============================================
				|  My posts are visible to the following users  |
				|                                               |                                             
				""");
		for (int i = 0; i < currentUser.visibility.size(); i++) {
			System.out.printf("|  "+ANSI_Green+"%-45s"+ANSI_Cyan+"|\n", currentUser.visibility.get(i));
		}
		System.out.printf("""
				|                                               |
				|  (+) Add a User                               |
				|  (-) Delete a User                            |
				|  (B) Back                                     |
				|                                               |
				|  Current user : %-30s|
				""", currentUser.getUsername());
		System.out.println(" =============================================== "+ANSI_RESET);
	}
    
    public static void accountWindow(String currentUsername) {
    	System.out.printf(ANSI_Cyan+"""
    			 ========================================
    			|                                        |
    			|  (P) Posts                             |
    			|  (V) Post Visibility                   |
    			|  (D) Delete Account                    |
    			|  (Q) Quit                              |
    			|                                        |
    			|  Current user: %-24s|
    			 ========================================
    			"""+ANSI_RESET, currentUsername);
    }
    
    public static void postWindow(User currentUser, Map<Integer, Post> postMap) {
    	System.out.printf(ANSI_Cyan+"""
    			 ========================================
    			|  My posts and visible posts            |
    			|                                        |
    			""");
    	for(int i = 1; i < postMap.size()+1; i++) {
    		if(postMap.get(i).getPostUser().visibility.contains(currentUser.getUsername()) || 
    				currentUser.getUsername().equals(postMap.get(i).getPostUser().getUsername())){
    			System.out.printf("|  "+ANSI_Green+"%2d: %-34s"+ANSI_Cyan+"|\n", 
    					postMap.get(i).getPostId(), postMap.get(i).getPostText());
    			System.out.printf("|  "+ANSI_Green+"%30d Likes"+ANSI_Cyan+"  |\n", postMap.get(i).getLikes());
    			System.out.printf("|"+ANSI_Yellow+"%11s, %s"+ANSI_Cyan+"  |\n", 
    					postMap.get(i).getPostUser().getUsername(), 
    					postMap.get(i).getPostTime().format(DateTimeFormatter.ofPattern("KK:mm:ss a, MMM dd, yyyy")).toString());
    		}
    	}
    	System.out.printf("""
    			|                                        |
    			|  (L) Like post                         |
    			|  (U) Unlike post                       |
    			|  (+) Publish a new post                |
    			|  (B) Back                              |
    			|                                        |
    			|  Current user: %-24s|
    			 ========================================
    			"""+ANSI_RESET, currentUser.getUsername());    	
    }
}
