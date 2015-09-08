angular.module('newApp')
.controller('createUserCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	
	$scope.user = {};
	$scope.userData = {};
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
	 		                                 { name: 'fullName', displayName: 'Name', width:'25%',cellEditableCondition: false,enableFiltering: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'email', displayName: 'Email',enableFiltering: false, width:'20%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'phone', displayName: 'Phone',enableFiltering: false, width:'20%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'userType', displayName: 'Role',enableFiltering: false, width:'20%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'edit', displayName: '', width:'15%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
        		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editUser(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i> &nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteUser(row)"></i>', 
    		                                 
    		                                 },
	     		                                 ];
	
	$scope.gotoProfile = function() {
		$location.path('/myprofile');
	}
	
	$scope.init = function() {
		$http.get('/getAllUsers')
		.success(function(data) {
		$scope.gridOptions.data = data;
	});
	}
	
	
	$scope.saveUser = function() {
			
			$http.post('/saveUser',$scope.user)
			 .success(function(data) {
					$('#btnClose').click();
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "User created succesfully!",
					});
					$scope.init();
				});
			
	};
	
	$scope.updateUser = function() {
		
		$http.post('/updateUser',$scope.userData)
		 .success(function(data) {
				$('#btnEditClose').click();
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User updated succesfully!",
				});
				$scope.init();
			});
		
};
	
	$scope.editUser = function(row) {
		$('#editUserModal').click();
		$scope.userData = row.entity;
	}
	
	$scope.deleteUser = function(row) {
		$('#deleteModal').click();
		   $scope.rowDataVal = row;
	};
	
	$scope.deleteUserById = function() {
		$http.get('/deleteUserById/'+$scope.rowDataVal.entity.id)
		.success(function(data) {
			$('#deleteClose').click();
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "User deleted succesfully!",
			});
			$scope.init();
		});
	};
	
}]);	