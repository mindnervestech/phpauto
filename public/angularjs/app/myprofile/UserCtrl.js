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
	$scope.trial;
	$scope.num;
	$scope.duration="month";
	$scope.contactVal;
	$scope.imagesList =[
	                    	{name:'img1',srcName:'/assets/images/p1.png'},
	                    	{name:'img2',srcName:'/assets/images/p2.png'},
	                    	{name:'img3',srcName:'/assets/images/p3.png'},
	                    	{name:'img4',srcName:'/assets/images/p4.png'},
	                    	{name:'img5',srcName:'/assets/images/p5.png'},
	                    	{name:'img6',srcName:'/assets/images/p6.png'},
	                    	{name:'img7',srcName:'/assets/images/p7.png'}
	                       ];
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
			angular.forEach($scope.permissionList, function(obj, index){
				 if ((obj.name == "My Profile") || (obj.name == "Inventory") || (obj.name == "Dashboard")) {
					 $scope.permission.push(obj.name);
					 obj.isSelected = true;
			    };
			  });
		}else{
			console.log("in else");
			$scope.permission = [];
			angular.forEach($scope.permissionList, function(obj, index){
				
				if ((obj.name == "My Profile") || (obj.name == "Inventory") || (obj.name == "Dashboard")) {
					$scope.permission.splice(index, 1);
					obj.isSelected = false;
				}
			  });
		}
	}
	
	$scope.init = function() {
		$http.get('/getAllUsers')
		.success(function(data) {
			console.log(data);
		$scope.gridOptions.data = data;
		
	});
	}
	
	$scope.contract = function(type){
		$scope.contactVal= type;
		if(type=="Employee"){
			$("#number").attr("disabled", true);
			$("#duration").attr("disabled", true);
			$("#number1").attr("disabled", true);
			$("#duration1").attr("disabled", true);
		}else{
			$("#number").attr("disabled", false);
			$("#duration").attr("disabled", false);
			$("#number1").attr("disabled", false);
			$("#duration1").attr("disabled", false);
		}
	}
	
	$scope.createNewUser=function(){
		$scope.permission = [];
		angular.forEach($scope.permissionList, function(obj, index){
			 obj.isSelected = false;
		});
		$scope.img="/assets/images/profile-pic.jpg ";
		
	}
	
	$scope.imageBorder = function(flag) {
		angular.forEach($scope.imagesList, function(obj, index){
			if(obj.name==flag){
				$("#"+flag).removeClass('noClass').addClass('ImageBorder');
				$("#u"+flag).removeClass('noClass').addClass('ImageBorder');
				$scope.img=obj.srcName;
				logofile = undefined;
			}else{
				$("#"+obj.name).removeClass('ImageBorder').addClass('noClass');
				$("#u"+obj.name).removeClass('ImageBorder').addClass('noClass');
			}
		});
		
		
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
		if(row.entity.contractDur=="Employee"){
			$scope.contactVal= row.entity.contractDur;
				$("#number").attr("disabled", true);
				$("#duration").attr("disabled", true);
				$('#employee1').click();
		}else{
			var durations = row.entity.contractDur;
			var val = durations.split(' ');
			$scope.num1 = parseInt(val[0]);
			$scope.duration1=val[1];
			$('#txt1').click();
		}
		console.log($scope.num1);
		console.log($scope.duration1);
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
		
		if($scope.userData.imageName==null){
			$scope.img = $scope.userData.imageUrl;
		}else{
			$scope.img = "http://glider-autos.com/glivrImg/images"+$scope.userData.imageUrl;
		}
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
		if($scope.contactVal=="Employee"){
			$scope.user.contractDur = $scope.contactVal;
		}else{
			if($scope.num==null){
				$scope.num=0;
			}
			$scope.user.contractDur = $scope.num+" "+$scope.duration;
		}
		$scope.user.imageUrl = $scope.img;
		console.log(logofile);
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
		
		if($scope.contactVal=="Employee"){
			$scope.userData.contractDur = $scope.contactVal;
		}else{
			if($scope.num1==null){
				$scope.num1=0;
			}
			$scope.userData.contractDur = $scope.num1+" "+$scope.duration1;
		}
		
		$scope.userData.imageUrl = $scope.img;
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