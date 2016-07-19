angular.module('app.apiserviceBlog', [])
.service('apiserviceBlog', function($http,$q,$upload){

	var mavenUrl = 'http://www.glider-autos.com:9889/';
	
	
	this.saveBlogData = function(logofile,blogData){
		var defer = $q.defer();
	    console.log("effdfc");
			$upload.upload({
	            url : '/saveBlog',
	            method: 'post',
	            file:logofile,
	            data:blogData
	        }).success(function(data, status, headers, config) {
	        	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Blog saved successfuly!",
				});
	        	defer.resolve(data);
	        });
		return defer.promise;
		
	};
	
	
	this.updateBlogData = function(logofile,blogData){
		var defer = $q.defer();
		
		if(logofile == null) {
			console.log("effdfc");
			$http.post('/updateBlog',blogData).success(function(data) {
				console.log(blogData);
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Blog updated successfuly!",
				});
				defer.resolve(data);
			});
		} else {
			$upload.upload({
	            url : '/updateBlog',
	            method: 'post',
	            file:logofile,
	            data:blogData
	        }).success(function(data, status, headers, config) {
	        	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Blog updated successfuly!",
				});
	        	defer.resolve(data);
	        });
		}
		
		return defer.promise;
	};
	

	
	this.getAllBlogData = function(){
		var defer = $q.defer();
		$http.get('/getAllBlogs').success(function(data) {
			defer.resolve(data);
		}); 
		
		return defer.promise;
	};
	
	
	this.deleteBlogRowData=function(id){
		console.log(id);
		var defer = $q.defer();
		$http.get('/deleteBlog/'+id).success(function(data) {
		$.pnotify({
		    title: "Success",
		    type:'success',
		    text: "Blog deleted successfuly!",
		    
		});
		defer.resolve(data);
	}).error(function(error){
			defer.reject(error);
		});
		
		return defer.promise;
	};
	
	
	
	this.getBlogById = function(id){
		var defer = $q.defer();
		
		$http.get('/getBlogById/'+id).success(function(data) {
			
			defer.resolve(data);
		
		});
		return defer.promise;
	};
	
	
	
})
