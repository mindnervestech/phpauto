angular.module('newApp')
.controller('BlogsCtrl', ['$scope','$http','$location','$filter','apiserviceBlog', function ($scope,$http,$location,$filter,apiserviceBlog) {
	
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
	 		
		 	
		 	apiserviceBlog.getAllBlogData().then(function(data){
				console.log(data);
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
	 		
	 		apiserviceBlog.deleteBlogRowData($scope.deleteId).then(function(data){
				console.log(data);
				$scope.init();
			});	
	 		
	 	}
	 	
}]);

angular.module('newApp')
.controller('CreateBlogCtrl', ['$scope','$http','$location','$filter','$upload','apiserviceBlog', function ($scope,$http,$location,$filter,$upload,apiserviceBlog) {

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
			
			apiserviceBlog.saveBlogData(logofile,$scope.blogData).then(function(data){
				console.log("dddd");
				console.log(data);
				$scope.blogImageUrl = data;
			});
			
		}
	}
	
	
}]);

angular.module('newApp')
.controller('EditBlogCtrl', ['$scope','$http','$location','$routeParams','$upload','apiserviceBlog', function ($scope,$http,$location,$routeParams,$upload,apiserviceBlog) {

	$scope.blogData = {};
	$scope.descMsg = "";
	
	apiserviceBlog.getBlogById($routeParams.id).then(function(data){
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
			apiserviceBlog.updateBlogData(logofile,$scope.blogData).then(function(data){
				console.log("dddd");
				console.log(data);
				$scope.blogData.imageUrl = data;
			});
			
		}
		
		
	}
	
	
}]);
