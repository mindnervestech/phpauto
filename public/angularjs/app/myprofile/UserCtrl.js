angular.module('newApp')
.controller('createUserCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
	$scope.img = "/assets/images/profile-pic.jpg";
	$scope.user = {};
	$scope.permission = [];
	$scope.permissionList =[
	{name:'Dashboard',isSelected:false},
	{name:'Home Page Editing',isSelected:false},
	{name:'Add Vehicle',isSelected:false},
	{name:'Inventory',isSelected:false},
	{name:'Customer Requests',isSelected:false},
	{name:'Blogs',isSelected:false},
	{name:'My Profile',isSelected:false},
	{name:'Website Analytics',isSelected:false},
	{name:'CRM',isSelected:false},
	{name:'Financial Statistics',isSelected:false},
	{name:'Account Settings',isSelected:false}];
	$scope.userData = {};
	$scope.dash = null;
	$scope.profile = null;
	$scope.inventory = null;
	$scope.trial;
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
	
	$scope.selectOption = function(){
		var type = $('#userType').val();
		console.log(type);
		if(type == "Sales Person"){
			console.log("in if");
			$scope.dash = "Dashboard";
			$scope.profile = "Inventory";
			$scope.inventory = "My Profile";
		}else{
			console.log("in else");
			$scope.dash = null;
			$scope.profile = null;
			$scope.inventory = null;
		}
	}
	
	$scope.init = function() {
		$http.get('/getAllUsers')
		.success(function(data) {
			console.log(data);
		$scope.gridOptions.data = data;
		
	});
	}
	
	$scope.createNewUser=function(){
		$scope.permission = [];
		angular.forEach($scope.permissionList, function(obj, index){
			 obj.isSelected = false;
		});
		$scope.img="/assets/images/profile-pic.jpg ";
		
	}
	$scope.showText = 0;
	$scope.getProvided = function(validText){
		console.log(validText);
		if(validText == "Yes"){
			$scope.showText = 1;
		}else{
			$scope.showText = 0;
		}
		
	}
	$scope.getTrial = function(validText){
		console.log(validText);
		if(validText == "Yes"){
			$scope.showText = 1;
		}else{
			$scope.showText = 0;
		}
		
	}
	
	console.log($scope.permissionList);
	
	$scope.rolesClicked = function(e, rolePer,value){
		console.log(rolePer);
		console.log(value);
		if(value == false){
			$scope.permission.push(rolePer.name);
		}else{
			$scope.deleteItem(rolePer);
		}
		console.log($scope.permission);
	}
	$scope.deleteItem = function(rolePer){
		angular.forEach($scope.permission, function(obj, index){
			 if ((rolePer.name == obj)) {
				 $scope.permission.splice(index, 1);
		    
		       	return;
		    };
		  });
	}
	
	$scope.editUser = function(row) {
		angular.forEach($scope.permissionList, function(obj, index){
			 obj.isSelected = false;
		});
		$('#editUserModal').click();
		$scope.userData = row.entity;
		$scope.userData.trialPeriod = parseInt($scope.userData.trialPeriod);
		console.log($scope.userData);
		$scope.permission = [];
		
		angular.forEach($scope.permissionList, function(obj, index){
			angular.forEach($scope.userData.permissions, function(obj1, index1){
				if(obj.name == obj1){
					 obj.isSelected = true;
					 $scope.permission.push(obj.name);
				 }
			});
		});
		
		
		$scope.img = "http://glider-autos.com/glivrImg/images"+$scope.userData.imageUrl;
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
	
	 var logofile;
		$scope.onLogoFileSelect = function ($files) {
			logofile = $files;
			for (var i = 0; i < $files.length; i++) {
				var $file = $files[i];
				if (window.FileReader && $file.type.indexOf('image') > -1) {
					var fileReader = new FileReader();
					fileReader.readAsDataURL($files[i]);
					var loadFile = function (fileReader, index) {
						fileReader.onload = function (e) {
							$timeout(function () {
								$scope.img = e.target.result;
								console.log(e.target.result);
							});
							
						}
					}(fileReader, i);
				}
			}
		}	
	   
	
	$scope.saveImage = function() {
		
		$scope.user.permissions = $scope.permission;
		console.log($scope.user);
		if(angular.isUndefined(logofile)) {
			if($scope.emailMsg == "") {
				$http.post('/uploadImageFile',$scope.user)
				.success(function(data) {
					$scope.user.firstName=" ";
		            $scope.user.lastName=" ";
		            $scope.user.email=" ";
		            $scope.user.phone=" ";
		            $scope.user.userType=" ";
		            $scope.user.img=" ";
		            $('#btnClose').click();
		            $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "User saved successfully",
					});
		            $scope.init();
				});
			}
		} else {
			if($scope.emailMsg == "") {
			   $upload.upload({
		            url : '/uploadImageFile',
		            method: 'post',
		            file:logofile,
		            data:$scope.user
		        }).success(function(data, status, headers, config) {
		            console.log('success');
		            $scope.user.firstName=" ";
		            $scope.user.lastName=" ";
		            $scope.user.email=" ";
		            $scope.user.phone=" ";
		            $scope.user.userType=" ";
		            $scope.user.img=" ";
		            $("#file").val('');
		            $('#btnClose').click();
		            $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "User saved successfully",
					});
		            $scope.init();
		        });
			}
		}
	   }
	
	$scope.updateImage = function() {
		$scope.userData.permissions = $scope.permission;
		delete $scope.userData.successRate;
		console.log($scope.userData);
		$scope.userData.locationId = 0;
		if(angular.isUndefined(logofile)) {
			if($scope.emailMsg == "") {
					$http.post('/updateImageFile',$scope.userData)
					.success(function(data) {
						$('#btnEditClose').click();
				           
			            $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "User saved successfully",
						});
			            $scope.init();
					});
				}
			} else {
				if($scope.emailMsg == "") {
					$upload.upload({
			            url : '/updateImageFile',
			            method: 'post',
			            file:logofile,
			            data:$scope.userData
			        }).success(function(data, status, headers, config) {
			            console.log('success');
			            $('#btnEditClose').click();
			           
			            $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "User saved successfully",
						});
			            $scope.init();
			        });
				}
			}
	   }
	$scope.emailMsg = "";
	$scope.checkEmail = function(type) {
		console.log($('#userEmail').val());
		if(type == 'create') {
			$scope.email = $('#userEmail').val()
		}
		if(type == 'edit') {
			$scope.email = $('#userEditEmail').val()
		}
		$http.get('/checkEmailOfUser/'+$scope.email)
		.success(function(data) {
			$scope.emailMsg = data;
		});
	}
	
}]);	