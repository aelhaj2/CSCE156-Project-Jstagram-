package unl.soc;

import java.time.LocalDateTime;

public class Post {
	private String postText;
	private User postUser;
	private LocalDateTime postTime;
	private int postId;
	protected int likes;
	
	public Post(String postText, User postAcct, LocalDateTime postTime, int postId, int likes) {
		this.postText = postText;
		this.postUser = postAcct;
		this.postTime = postTime;
		this.postId = postId;
		this.likes = likes;
	}
		
	public String getPostText() {
		return this.postText;
	}
	public User getPostUser() {
		return this.postUser;
	}
	public LocalDateTime getPostTime() {
		return this.postTime;
	}
	public int getPostId() {
		return this.postId;
	}
	public int getLikes() {
		return this.likes;
	}

	@Override
	public String toString() {
		return "Post [postText=" + postText + ", postUser=" + postUser + ", postTime=" + postTime + "]";
	}
}
