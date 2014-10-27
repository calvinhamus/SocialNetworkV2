package course.cloud.computing.data;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import course.cloud.computing.classes.HTTPCode;
import course.cloud.computing.classes.HTTPCodes;
import course.cloud.computing.classes.ProcessingTime;
import course.cloud.computing.classes.Tweet;
import course.cloud.computing.classes.User;
import course.cloud.computing.classes.Users;


public class SocialNetworkDataBase 
{
	private static AtomicBoolean init = new AtomicBoolean(false);
	private static Connection conn = null;
	 // JDBC driver name and database URL
	static final String driver = "com.mysql.jdbc.Driver";  
	static final String url = "jdbc:mysql://CS597-Calvin-LoadBalancer:3306/";
	static final String dbName = "chatter";
	   //  Database credentials
	static final String userName = "remoteUser";
	static final String password = "password";

	private static ProcessingTime processTimeList = new ProcessingTime();

	private static HTTPCodes httpCodes = new HTTPCodes();
	 
	private void readDBTable(Set<String> set, DatabaseMetaData dbmeta,
			String searchCriteria, String schema) throws SQLException 
	{
		ResultSet rs = dbmeta.getTables(null, schema, null,
				new String[] { searchCriteria });
		while (rs.next()) {
			set.add(rs.getString("TABLE_NAME").toLowerCase());
		}
	}
	private static void initialize()
	{
		try { 
				Class.forName(driver).newInstance(); 
				conn = DriverManager.getConnection(url+dbName,userName,password); 
				//  
			} catch (Exception e) 
			{ 
				e.printStackTrace(); 
			}
	}
	public static void closeConn() throws SQLException 
	{
		conn.close();
	}

	/****************************************************************User Section******************************************/
	public static int insertUser(String userName,String email, String password) throws SQLException {
		
		PreparedStatement statement = null;
		if(getUserByName(userName) == 0)
		{
			initialize();
			if (statement == null) {
				statement = conn
						.prepareStatement("insert into users(userName,email,password) values (?, ?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
			}
			statement.setString(1, userName);
			statement.setString(2, email);
			statement.setString(3, password);
			statement.execute();
			ResultSet set = statement.getGeneratedKeys();
			if (set.next()){ 
				return set.getInt(1);
			}
			    
		}
		return 0;
		
	}
	public static int getUserById(int Id) throws SQLException
	{
		initialize();
		PreparedStatement lookup = null;
		if (lookup == null) {
			lookup = conn
					.prepareStatement("select id from users where id = ?");
		}
		lookup.setInt(1, Id);
		ResultSet set = lookup.executeQuery();
		int result = 0;
		if (set.next()) {
			result = set.getInt(1);
		}
		return result;
	}
	public static int getUserByName(String name) throws SQLException
	{
		initialize();
		PreparedStatement lookup = null;
		if (lookup == null) {
			lookup = conn
					.prepareStatement("select id from users where username = ?");
		}
		lookup.setString(1, name);
		ResultSet set = lookup.executeQuery();
		int result = 0;
		if (set.next()) {
			result = set.getInt(1);
		}
		 closeConn();
		return result;
	}
	public static long getUsers() throws SQLException {
		initialize();
		PreparedStatement lookup = null;
		if (lookup == null) {
			lookup = conn
					.prepareStatement("select count(*) from users ");
		}
		//lookup.setString(1, URL);
		ResultSet set = lookup.executeQuery();
		long count = 0;
		if (set.next()) {
			count = set.getLong(1);
		}
		 
		return count;
	}
	/**************************************************************** End User Section******************************************/
	/****************************************************  Followers and Following Section******************************************/
	public static Users getPendingFollowing(int userId) throws SQLException
	{
		initialize();
		Users users = new Users();
		Users followers = getFollowers(userId);
		Users following = getFollowing(userId);
		for (User user  : followers.getUserList()) 
		{
			if(!following.getUserList().contains(user))
			{
				users.addUser(user);
			}
		}
		 
		return users;
	}
	public static Users getPendingFollow(int userId) throws SQLException
	{
		initialize();
		Users users = new Users();
		Users followers = getFollowers(userId);
		Users following = getFollowing(userId);
		for (User user  : following.getUserList()) 
		{
			if(!followers.getUserList().contains(user))
			{
				users.addUser(user);
			}
		}
		 
		return users;
	}
	public static int followUser(int userId, int followingId) throws SQLException 
	{
		initialize();
		if(!checkFollowingStatus(userId,followingId))
		{
			PreparedStatement statement = null;
			if (statement == null) {
				statement = conn
						.prepareStatement("INSERT INTO userFollowing(userId,followeringId) VALUES(?, ?)",PreparedStatement.RETURN_GENERATED_KEYS);
			}
			statement.setInt(1, userId);
			statement.setInt(2, followingId);
			statement.execute();
			ResultSet set = statement.getGeneratedKeys();
			if (set.next()){ 
				 
				return followingId;
			}			
		}
			 
			return 0;
		
		//return user;
	}
	private static boolean checkFollowingStatus(int userId, int followingId) throws SQLException {
		initialize();
		PreparedStatement lookup = null;
		if (lookup == null) {
			lookup = conn
					.prepareStatement("SELECT followeringId FROM userfollowing WHERE userId = ?");
		}
		lookup.setInt(1, userId);
		ResultSet set = lookup.executeQuery();
		if (set.next()) {
			//int follingId = set.getInt("followeringId");
			 
			return true;
		}
		 
		return false;
	}

	public static int unfollowUser(int userId, int followingId) throws SQLException
	{
		initialize();
		PreparedStatement lookup = null;
		if (lookup == null) {
			lookup = conn
					.prepareStatement("DELETE FROM userfollowing WHERE userId = ? AND followeringId = ?");
		}
		lookup.setInt(1, userId);
		lookup.setInt(2, followingId);
		if(lookup.executeUpdate()>0)
		{
			 
			return followingId;
		}
			
		 		
		return 0;
	}
	public static Users getFollowing(int userId) throws SQLException
	{
		initialize();
		Users users = new Users();
		PreparedStatement lookup = null;
		if (lookup == null) {
			lookup = conn
					.prepareStatement("SELECT followeringId FROM userfollowing WHERE userId = ?");
		}
		lookup.setInt(1, userId);
		ResultSet set = lookup.executeQuery();
		if (set.next()) {
			User tempUser = new User();
			tempUser.setId(set.getInt(1));
			users.addUser(tempUser);
		}
		 
		return users;
	}
	public static Users getFollowers(int userId) throws SQLException
	{
		initialize();
		Users users = new Users();
		PreparedStatement lookup = null;
		if (lookup == null) {
			lookup = conn
					.prepareStatement("SELECT followerId FROM userfollowers WHERE userId = ?");
		}
		lookup.setInt(1, userId);
		ResultSet set = lookup.executeQuery();
		if (set.next()) {
			User tempUser = new User();
			tempUser.setId(set.getInt(1));
			users.addUser(tempUser);
		}
		 
		return users;
	}
	/**********Tweet funcitons ************/
	public static int createNewTweet(int userId, String msg) throws SQLException
	{
		initialize();
		PreparedStatement statement = null;
		if (statement == null) {
			statement = conn
					.prepareStatement("INSERT INTO tweets(userId,text) VALUES(?, ?)",PreparedStatement.RETURN_GENERATED_KEYS);
		}
		statement.setInt(1, userId);
		statement.setString(2, msg);
		statement.execute();
		ResultSet set = statement.getGeneratedKeys();
		if (set.next()){
			 
			return set.getInt(1);
		}
		 
		return 0;
	}
	public static Tweet getTweetById(int tweetId) throws SQLException
	{
		initialize();
		Tweet tweet = new Tweet();
		tweet.setId(0);
		PreparedStatement lookup = null;
		if (lookup == null) {
			lookup = conn
					.prepareStatement("SELECT * FROM tweets WHERE id = ?");
		}
		lookup.setInt(1, tweetId);
		ResultSet set = lookup.executeQuery();
		if (set.next()) {
			
			tweet.setMessage(set.getString("text"));
			tweet.setId(set.getInt("id"));
			
				//return tweet;
		}
		 
		return tweet;
	}
	public static int removeTweet(int userId, int tweetId) throws SQLException
	{
		initialize();
		PreparedStatement lookup = null;
		if (lookup == null) {
			lookup = conn
					.prepareStatement("DELETE FROM tweets WHERE userId = ? AND id = ?");
		}
		lookup.setInt(1, userId);
		lookup.setInt(2, tweetId);
		if(lookup.executeUpdate() >0){
			 
			return 1;
		}
		//lookup.executeQuery();		
			 
		return 0;
	}
	public static void addToProcessingTime(Long time)
	{
		processTimeList.addTime(time);
		//Collections.sort(processTimeList);
	}
	public static ProcessingTime getProcessingTime()
	{
		return processTimeList;
	}
	public static void addCode(String error)
	{
		
		HTTPCode code = new HTTPCode(error);
		httpCodes.addCode(code);
	}
	public static ArrayList<HTTPCode> getErrors(String type)
	{
		return httpCodes.getErrors(type);	
	}
	public static ArrayList<HTTPCode> getResolution(String resolution)
	{
		return httpCodes.getSuccess(resolution);
	}

	

}
