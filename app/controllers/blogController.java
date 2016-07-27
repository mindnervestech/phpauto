package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.AuthUser;
import models.Blog;
import models.Location;

import org.apache.commons.io.FileUtils;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import viewmodel.BlogVM;
import views.html.home;
public class blogController extends Controller {
	
	final static String rootDir = Play.application().configuration()
			.getString("image.storage.path");
	final static String pdfRootDir = Play.application().configuration()
			.getString("pdfRootDir");

	final static String imageUrlPath = Play.application().configuration()
			.getString("image.url.path");

	final static String userRegistration = Play.application().configuration()
			.getString("userRegistration");

	final static String vehicleUrlPath = Play.application().configuration()
			.getString("vehicle.url.path");

	final static String mashapeKey = Play.application().configuration()
			.getString("mashapeKey");

	final static String emailUsername = Play.application().configuration()
			.getString("mail.username");

	final static String emailPassword = Play.application().configuration()
			.getString("mail.password");

	
	
	
	public static Result getBlogById(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	BlogVM vm = new BlogVM();
	    	Blog blog = Blog.findById(id);
	    	vm.id = blog.id;
			vm.title = blog.title;
			vm.postedBy = blog.postedBy;
			vm.description = blog.description;
			vm.imageUrl = blog.imageUrl;
			vm.videoUrl = blog.videoUrl;
	    	return ok(Json.toJson(vm));
    	}
    }
    
	
	
	 public static Result updateBlog() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser userObj = (AuthUser) getLocalUser();
		    	Form<BlogVM> form = DynamicForm.form(BlogVM.class).bindFromRequest();
		    	BlogVM vm = form.get();
		    	Blog blogObj = Blog.findById(vm.id);
		    	if(blogObj != null) {
		    		blogObj.setTitle(vm.title);
		    		blogObj.setDescription(vm.description);
		    		blogObj.setVideoUrl(vm.videoUrl);
		    		blogObj.setPostedBy(vm.postedBy);
		    		blogObj.update();
		    		
		    		MultipartFormData body = request().body().asMultipartFormData();
			    	if(body != null) {
				    	FilePart picture = body.getFile("file0");
				    	  if (picture != null) {
				    		File oldfdir = new File(rootDir+blogObj.getImageUrl());  
				    		oldfdir.delete();
				    	    String fileName = picture.getFilename();
				    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"blogImages"+File.separator+blogObj.getId());
				    	    if(!fdir.exists()) {
				    	    	fdir.mkdir();
				    	    }
				    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"blogImages"+File.separator+blogObj.getId()+File.separator+fileName;
				    	    File file = picture.getFile();
				    	    try {
				    	    		FileUtils.moveFile(file, new File(filePath));
				    	    		blogObj.setImageUrl(File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"blogImages"+File.separator+blogObj.getId()+File.separator+fileName);
				    	    		blogObj.setImageName(fileName);
				    	    		blogObj.update();
				    	  } catch (FileNotFoundException e) {
				  			e.printStackTrace();
					  		} catch (IOException e) {
					  			e.printStackTrace();
					  		} 
				    	  }
			    	}
		    	}
		    	return ok(blogObj.imageUrl);
	    	}	
	    }



	 public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
	    	//AuthUser user = getLocalUser();
			return user;
		}

	    
	 
	 
	 public static Result deleteBlogImage(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AuthUser user = (AuthUser) getLocalUser();
		    	Blog image = Blog.findById(id);
		    	File file = new File(rootDir+File.separator+"BlogImages"+File.separator+image.coverImageName);
		    	file.delete();
		    	File thumbfile = new File(rootDir+File.separator+"BlogImages"+File.separator+"thumbnail_"+image.coverImageName);
		    	thumbfile.delete();
		    	//image.delete();
		    	image.setThumbPath(null);
		    	image.setCoverImageName(null);
		    	image.setPath(null);
		    	image.setRow(null);
		    	image.setCol(null);
		    	image.update();
		    	return ok();
	    	}
	    }
	    
	  
	    public static Result getAllBlogs() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AuthUser userObj = (AuthUser) getLocalUser();
		    	List<Blog> blogList = Blog.findByLocation(Long.valueOf(session("USER_LOCATION")));
		    	//List<Blog> blogList = Blog.findByUser(userObj);
		    	List<BlogVM> vmList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	int num = 0;
		    	for(Blog blog: blogList) {
		    		num++;
		    		BlogVM vm = new BlogVM();
		    		vm.id = blog.id;
		    		vm.title = blog.title;
		    		vm.postedBy = blog.postedBy;
		    		vm.postedDate = df.format(blog.postedDate);
		    		vm.number = num;
		    		vmList.add(vm);
		    	}
		    	
		    	return ok(Json.toJson(vmList));
	    	}	
	    }
	    
	    
	    public static Result deleteBlog(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	Blog blog = Blog.findById(id);
		    	blog.delete();
		    	return ok();
	    	}
	    }
	    
	    
	    public static Result saveBlog() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AuthUser userObj = (AuthUser) getLocalUser();
		    	Form<BlogVM> form = DynamicForm.form(BlogVM.class).bindFromRequest();
		    	BlogVM vm = form.get();
		    	Blog blogObj = Blog.findByUserAndTitle(userObj, vm.title);
		    	Blog blog = new Blog();
		    	if(blogObj == null) {
			    	blog.title = vm.title;
			    	blog.description = vm.description;
			    	blog.videoUrl = vm.videoUrl;
			    	blog.postedBy = vm.postedBy;
			    	blog.postedDate = new Date();
			    	blog.user = userObj;
					blog.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			    	blog.save();
			    	
			    	MultipartFormData body = request().body().asMultipartFormData();
			    	
			    	FilePart picture = body.getFile("file0");
			    	  if (picture != null) {
			    	    String fileName = picture.getFilename();
			    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"blogImages"+File.separator+blog.getId());
			    	    if(!fdir.exists()) {
			    	    	fdir.mkdir();
			    	    }
			    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"blogImages"+File.separator+blog.getId()+File.separator+fileName;
			    	    File file = picture.getFile();
			    	    try {
			    	    		FileUtils.moveFile(file, new File(filePath));
			    	    		blog.setImageUrl(File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"blogImages"+File.separator+blog.getId()+File.separator+fileName);
			    	    		blog.setImageName(fileName);
			    	    		blog.update();
			    	  } catch (FileNotFoundException e) {
			  			e.printStackTrace();
				  		} catch (IOException e) {
				  			e.printStackTrace();
				  		} 
			    	  }
		    	} else {
		    		return ok(blogObj.imageUrl);
		    	}
		    	
		    	return ok(blog.imageUrl);
	    	}	
	    }
	    
	  
	
	
	
	
	
	

}
