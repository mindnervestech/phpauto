angular.module('newApp')
.controller('BlogsCtrl', ['$scope','$http','$location','$filter', function ($scope,$http,$location,$filter) {
	
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 		 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 		 $scope.gridOptions.enableVerticalScrollbar = 2;
	 		 $scope.gridOptions.columnDefs = [
	 		                                 { name: 'number', displayName: 'Sr No.',enableFiltering: false, width:'8%',cellEditableCondition: false,
	 		                                 },
	 		                                 { name: 'title', displayName: 'Title',enableFiltering: false, width:'40%',cellEditableCondition: false,
	 		                                 },
	 		                                 { name: 'postedDate', displayName: 'Published Date',enableFiltering: false, width:'15%',cellEditableCondition: false,
	 		                                 },
	 		                                 { name: 'postedBy', displayName: 'Posted By',enableFiltering: false, width:'22%',cellEditableCondition: false,
	 		                                 },
	 		                                { name: 'edit', displayName: 'Action', width:'15%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
	    		                                 cellTemplate:' <a ng-click="grid.appScope.editBlog(row)" style="margin-top:7px;margin-left:10%;" >Edit</a> <a ng-click="grid.appScope.deleteBlog(row)" style="margin-top:7px;margin-left:10%;" >Delete</a>', 
			                                 
			                                 },
	     		                                 ];
	  
	 	$scope.init = function() {
	 		
		 	$http.get('/getAllBlogs')
			.success(function(data) {
				$scope.gridOptions.data = data;
			}); 
		 	
	 	}	 
	 	
	 	
	 	$scope.editBlog = function(row) {
	 		$location.path('/editBlog/'+row.entity.id);
	 	}	
	 	
	 	$scope.deleteBlog = function(row) {
	 		$('#deleteModal').click();
	 		$scope.deleteId = row.entity.id;
	 	}
	 	
	 	$scope.deleteBlogRow = function() {
	 		$http.get('/deleteBlog/'+$scope.deleteId)
			.success(function(data) {
			$scope.init();
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Blog deleted successfuly!",
			});
		}); 
	 	}
	 	
}]);

angular.module('newApp')
.controller('CreateBlogCtrl', ['$scope','$http','$location','$filter','$upload', function ($scope,$http,$location,$filter,$upload) {

	$scope.blogData = {};
	$scope.descMsg = "";
	$scope.blogData.description = "";
	$scope.blogImageUrl = "";
	var logofile = null;
	$scope.onLogoFileSelect = function($files) {
		logofile = $files;
	}
	
	
	$scope.saveBlog = function() {
		if(angular.isUndefined($scope.blogData.description)) {
			$scope.descMsg = "*Please add description";
		} else {
			$scope.descMsg = "";
			
			$upload.upload({
	            url : '/saveBlog',
	            method: 'post',
	            file:logofile,
	            data:$scope.blogData
	        }).success(function(data, status, headers, config) {
	        	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Blog saved successfuly!",
				});
	        	$scope.blogImageUrl = data;
	        });
			
		}
	}
	
	
}]);

angular.module('newApp')
.controller('EditBlogCtrl', ['$scope','$http','$location','$routeParams','$upload', function ($scope,$http,$location,$routeParams,$upload) {

	$scope.blogData = {};
	$scope.descMsg = "";
	$http.get('/getBlogById/'+$routeParams.id)
	.success(function(data) {
		console.log(data);
		$scope.blogData = data;
	}); 
	
	var logofile = null;
	$scope.onLogoFileSelect = function($files) {
		logofile = $files;
	}
	
	$scope.updateBlog = function() {
		if(angular.isUndefined($scope.blogData.description)) {
			$scope.descMsg = "*Please add description";
		} else {
			$scope.descMsg = "";
			if(logofile == null) {
				$http.post('/updateBlog',$scope.blogData)
				.success(function(data) {
					
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Blog updated successfuly!",
					});
				});
			} else {
				$upload.upload({
		            url : '/updateBlog',
		            method: 'post',
		            file:logofile,
		            data:$scope.blogData
		        }).success(function(data, status, headers, config) {
		        	$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Blog updated successfuly!",
					});
		        	$scope.blogData.imageUrl = data;
		        });
			}
		}
	}
	
	
}]);
