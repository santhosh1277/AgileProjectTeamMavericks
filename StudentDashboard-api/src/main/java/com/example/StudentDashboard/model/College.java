package com.example.StudentDashboard.model;
import java.util.List;

public class College {
	 	private int id;
	    private String name;
	    private List<String> courses;  
	    private String location;
	    private int worldRank;
	    
	   public College(int Id,String name,List<String> courses,String location,int worldRank)
	   {
		   this.id = Id;
		   this.name = name;
	       this.courses = courses;
	       this.location = location;
	       this.worldRank = worldRank;
	   }
}

