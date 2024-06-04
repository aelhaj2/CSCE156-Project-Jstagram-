package unl.soc;

/*
 * 	Written by: Mason Mandernach and Abdel Elhaj
 * 			on: 2023-12-06 	
 * 		   for: CSCE 156 Project 2
 * 
 * 	Function: A simple social media platform where
 * 			 the user's data is stored in a database.
 * 	         The user can then see the posts visisble
 *           to them, and edit the list of people 
 *           that can see their posts. In addition, 
 *           they can like and unlike posts, add an 
 *           account, delete their current account, 
 *           and make new posts.
 */

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;

public class JstgramMain {

	public static void main(String[] args) {
		Map<String, User> userData = DbLoader.getUserData();
		Map<Integer, Post> postMap = DbLoader.getPostData(userData);
		Scanner userIn = new Scanner(System.in);
		boolean quit = false;

		while (quit == false) {
			Views.mainWindow(userData.size());
			char logOrReg = userIn.next().charAt(0);
			switch (logOrReg) {
			case 'R':
			case 'r':
				boolean uniqueUsername = false;
				userIn.nextLine();
				System.out.println("Enter username: ");
				String registerUser = userIn.nextLine();
				while (uniqueUsername == false) {
					if (userData.containsKey(registerUser) == false) {
						uniqueUsername = true;
						System.out.println("Enter password: ");
						String registerPass = userIn.nextLine();
						User newUser = new User(registerUser, registerPass);
						userData.put(registerUser, newUser);
						DbLoader.registerUser(newUser);
					} else {
						System.out.println("Try again, enter username: ");
						registerUser = userIn.nextLine();
					}
				}
				break;
			case 'L':
			case 'l':
				userIn.nextLine();
				System.out.println("Enter username: ");
				String loginUser = userIn.nextLine();
				System.out.println("Enter password: ");
				String loginPass = userIn.nextLine();
				while (userData.containsKey(loginUser) == false
						|| !(loginPass.equals(userData.get(loginUser).getPassword()))) {
					System.out.println("INVALID LOGIN");
					System.out.println("Enter username:");
					loginUser = userIn.next();
					System.out.println("Enter password:");
					loginPass = userIn.next();
				}
				User currentUser = userData.get(loginUser);
				boolean loggedOut = false;
				while (loggedOut == false) {
					Views.accountWindow(currentUser.getUsername());
					char acctSelect = userIn.next().charAt(0);
					boolean back = false;
					switch (acctSelect) {
					case 'P':
					case 'p':
						while (back == false) {
							Views.postWindow(currentUser, postMap);
							char postSelect = userIn.next().charAt(0);
							switch (postSelect) {
							case '+':
								userIn.nextLine();
								System.out.println("Enter text to post (max of 34 characters): ");
								String newPostText = userIn.nextLine();
								Post newPost = new Post(newPostText, currentUser, LocalDateTime.now(),
										postMap.size() + 1, 0);
								postMap.put(postMap.size() + 1, newPost);
								DbLoader.addPost(newPost);
								break;
							case 'L':
							case 'l':
								userIn.nextLine();
								System.out.println("Enter the Id of the post you'd like to like: ");
								int postToLike = userIn.nextInt();
								postMap.get(postToLike).likes++;
								DbLoader.likePost(postMap, postToLike);
								break;
							case 'U':
							case 'u':
								userIn.nextLine();
								System.out.println("Enter the Id of the post you'd like to unlike: ");
								int postToUnlike = userIn.nextInt();
								if (postMap.get(postToUnlike).likes == 0) {
									System.out.println("This post already has 0 likes.");
									break;
								} else {
									postMap.get(postToUnlike).likes--;
									DbLoader.likePost(postMap, postToUnlike);
									break;
								}			
							case 'B':
							case 'b':
								back = true;
								break;
							default:
								System.out.println("Invalid selection, try again!");
							}
						}
						break;
					case 'V':
					case 'v':
						while (back == false) {
							Views.visibilityWindow(currentUser);
							char visSelect = userIn.next().charAt(0);
							switch (visSelect) {
							case '+':
								userIn.nextLine();
								System.out.println("Enter the username you would like to see your posts: ");
								String newVisUsername = userIn.nextLine();
								currentUser.visibility.add(newVisUsername);
								DbLoader.addVisibleUser(currentUser, newVisUsername);
								break;
							case '-':
								userIn.nextLine();
								System.out.println("Enter the username you would like to not see your posts: ");
								String removeVisUsername = userIn.nextLine();
								currentUser.visibility.remove(removeVisUsername);
								DbLoader.addVisibleUser(currentUser, removeVisUsername);
								break;
							case 'B':
							case 'b':
								back = true;
								break;
							default:
								System.out.println("Invalid selection, try again!");
							}
						}
						break;
					case 'D':
					case 'd':
						userData.remove(currentUser.getUsername());
						DbLoader.deleteUser(currentUser);
						loggedOut = true;
						break;
					case 'Q':
					case 'q':
						System.out.println("Thank you for using Jstgram!");
						loggedOut = true;
						quit = true;
						break;
					default:
						System.out.println("Invalid selection, try again!");
					}
				}
				break;
			default:
				System.out.println("Invalid selection, try again!");
			}
		}
		userIn.close();
	}
}
