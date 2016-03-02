angular.module('newApp')
.controller('createLocationCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
	$scope.img = "/assets/images/profile-pic.jpg";
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
	 		                                 { name: 'locationName', displayName: 'Location', width:'18%',cellEditableCondition: false,enableFiltering: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'locationemail', displayName: 'Location Email',enableFiltering: false, width:'18%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'locationphone', displayName: 'Location Phone',enableFiltering: false, width:'18%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'managerFullName', displayName: 'Manager Name',enableFiltering: false, width:'18%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                { name: 'email', displayName: 'Manager Email',enableFiltering: false, width:'18%',cellEditableCondition: false,
	 		                                 	
	 		                               },
	 		                                 { name: 'edit', displayName: '', width:'15%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
        		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editUser(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i> <i class="fa fa-trash" ng-click="grid.appScope.deactiveLocation(row)" style="margin-top:7px;margin-left:14px;" title="Deactive Location"></i>', 
    		                                 
    		                                 },
	     		                                 ];
	
	 		 
	 		/*&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteUser(row)"></i>*/
	$scope.gotoProfile = function() {
		$location.path('/myprofile');
	}
	
	$scope.init = function() {
			$http.get('/getLocationForGM')
			.success(function(data) {
				console.log(data);
				$scope.gridOptions.data = data;
			});
			
			$http.get('/getLocationValueForGM')
			.success(function(data) {
				console.log(data);
				$scope.gmIsManager = data;
			});
		};
	$scope.goToDeactivateLoaction = function() {
			$location.path('/deactiveLocations');
	};
	$scope.deactiveLocation = function(row){
		console.log(row.entity);
		$('#deactiveLocation').click();
		$scope.rowDataVal = row;
	};
	
	$scope.createNewLocation=function(){
		$scope.img="/assets/images/profile-pic.jpg ";
		$scope.imgLocation ="/assets/images/profile-pic.jpg ";
		$scope.user = [];
	}
	
	
	
	$scope.editUser = function(row) {
		$('#editUserModal').click();
		$scope.user = row.entity;
		console.log($scope.user);
		$scope.imgLocation = "http://glider-autos.com/glivrImg/images/"+$scope.user.imageUrl;
		$scope.img = "http://glider-autos.com/glivrImg/images"+$scope.user.mImageUrl;
	}
	
	$scope.deactiveLocationById = function() {
		console.log($scope.rowDataVal.entity.id);
		$http.get('/deactiveLocationById/'+$scope.rowDataVal.entity.id)
		.success(function(data) {
			$('#deleteClose').click();
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Location deactive succesfully!",
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
								//console.log(e.target.result);
							});
							
						}
					}(fileReader, i);
				}
			}
		}	
		var logofile1;
		$scope.onLogoFileLocationSelect = function ($files) {
			logofile1 = $files;
			for (var i = 0; i < $files.length; i++) {
				var $file = $files[i];
				if (window.FileReader && $file.type.indexOf('image') > -1) {
					var fileReader = new FileReader();
					fileReader.readAsDataURL($files[i]);
					var loadFile = function (fileReader, index) {
						fileReader.onload = function (e) {
							$timeout(function () {
								$scope.imgLocation = e.target.result;
								//console.log(e.target.result);
							});
							
						}
					}(fileReader, i);
				}
			}
		}	
	   
	$scope.managerObj = {};
	$scope.locationObj = {};
	$scope.saveImage = function() {
		$scope.user.userType = "Manager"
		if($scope.user.mi == undefined){
			$scope.user.mi = 0;
		}
		
		$scope.managerObj.userType = $scope.user.userType;
		$scope.managerObj.firstName = $scope.user.firstName;
		$scope.managerObj.lastName = $scope.user.lastName;
		$scope.managerObj.email = $scope.user.email;
		$scope.managerObj.phone = $scope.user.phone;
		$scope.managerObj.mi = $scope.user.mi;
		
		//locationObj 
		$scope.locationObj.locationName = $scope.user.locationName;
		$scope.locationObj.locationaddress = $scope.user.locationaddress;
		$scope.locationObj.locationemail = $scope.user.locationemail;
		$scope.locationObj.locationphone = $scope.user.locationphone;
		$scope.locationObj.mi = $scope.user.mi;
		console.log($scope.user);
		console.log($scope.managerObj);
		console.log($scope.locationObj);
			
		if(angular.isUndefined(logofile1)) {
			
				$http.post('/uploadLocationImageFile',$scope.locationObj)
				.success(function(data) {
					$scope.managerObj.locationId = data;
		    		$scope.saveManager();
		            $('#btnClose').click();
		            $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "User saved successfully",
					});
				});
		} else {
			   $upload.upload({
		            url : '/uploadLocationImageFile',
		            method: 'post',
		            file:logofile1,
		            data:$scope.locationObj
		        }).success(function(data, status, headers, config) {
		    		$scope.managerObj.locationId = data;
		    		$scope.saveManager();
		            $("#file").val('');
		            $('#btnClose').click();
		            $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "User saved successfully",
					});
		        });
		}
		$scope.init();
	   };
	
	$scope.saveManager = function(){
		if(angular.isUndefined(logofile)) {
			
			$http.post('/uploadManagerImageFile',$scope.managerObj)
			.success(function(data) {
				
	            $('#btnClose').click();
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
			});
	} else {
		   $upload.upload({
	            url : '/uploadManagerImageFile',
	            method: 'post',
	            file:logofile,
	            data:$scope.managerObj
	        }).success(function(data, status, headers, config) {
	            $("#file").val('');
	            $('#btnClose').click();
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
	        });
	}

        $scope.init();
	};
	
	
	$scope.updateImage = function() {
		$scope.user.userType = "Manager"
			
			if($scope.managerObj.mi == false){
				$scope.managerObj.id = $scope.user.managerId;
				$scope.managerObj.userType = $scope.user.userType;
				$scope.managerObj.firstName = $scope.user.firstName;
				$scope.managerObj.lastName = $scope.user.lastName;
				$scope.managerObj.email = $scope.user.email;
				$scope.managerObj.phone = $scope.user.phone;
			}
		
		$scope.managerObj.mi = $scope.user.mi;
		$scope.managerObj.locationId = $scope.user.id;
		
		
		$scope.locationObj.id = $scope.user.id;
		$scope.locationObj.locationName = $scope.user.locationName;
		$scope.locationObj.locationaddress = $scope.user.locationaddress;
		$scope.locationObj.locationemail = $scope.user.locationemail;
		$scope.locationObj.locationphone = $scope.user.locationphone;
		console.log($scope.user);
		console.log($scope.managerObj);
		console.log($scope.locationObj);
			
		if(angular.isUndefined(logofile1)) {
			
				$http.post('/updateUploadLocationImageFile',$scope.locationObj)
				.success(function(data) {
					$scope.managerObj.locationId = data;
					$scope.updateManager();
		            $('#btnClose').click();
		            $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Location saved successfully",
					});
		            $scope.init();
				});
		} else {
			   $upload.upload({
		            url : '/updateUploadLocationImageFile',
		            method: 'post',
		            file:logofile1,
		            data:$scope.locationObj
		        }).success(function(data, status, headers, config) {
		            console.log('success');
		            console.log(data);
		           
		    		$scope.managerObj.locationId = data;
		    		$scope.updateManager();
		            
		            $("#file").val('');
		            $('#btnClose').click();
		            $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Location saved successfully",
					});
		            $scope.init();
		        });
		}
	   }
	
	$scope.updateManager = function(){
		if(angular.isUndefined(logofile)) {
			
			$http.post('/UpdateuploadManagerImageFile',$scope.managerObj)
			.success(function(data) {
				
	            $('#btnClose1').click();
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Manager saved successfully",
				});
	            $scope.init();
			});
	} else {
		   $upload.upload({
	            url : '/UpdateuploadManagerImageFile',
	            method: 'post',
	            file:logofile,
	            data:$scope.managerObj
	        }).success(function(data, status, headers, config) {
	            console.log('success');
	            $scope.user.firstName=" ";
	            $scope.user.lastName=" ";
	            $scope.user.email=" ";
	            $scope.user.phone=" ";
	            $scope.user.userType=" ";
	            $scope.user.img=" ";
	            $("#file").val('');
	            $('#btnClose1').click();
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Manager saved successfully",
				});
	            $scope.init();
	        });
	}
		$('#btnClose1').click();
	};
	
/*	$scope.updateImage = function() {
		delete $scope.userData.successRate;
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
	   }*/
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

angular.module('newApp')
.controller('deactiveLocationCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
	console.log("welcome");
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
			 		                                 { name: 'locationName', displayName: 'Location', width:'18%',cellEditableCondition: false,enableFiltering: false,
			 		                                	
			 		                                 },
			 		                                 { name: 'locationemail', displayName: 'Location Email',enableFiltering: false, width:'18%',cellEditableCondition: false,
			 		                                	
			 		                                 },
			 		                                 { name: 'locationphone', displayName: 'Location Phone',enableFiltering: false, width:'18%',cellEditableCondition: false,
			 		                                	
			 		                                 },
			 		                                 { name: 'managerFullName', displayName: 'Manager Name',enableFiltering: false, width:'18%',cellEditableCondition: false,
			 		                                	
			 		                                 },
			 		                                { name: 'email', displayName: 'Manager Email',enableFiltering: false, width:'18%',cellEditableCondition: false,
			 		                                 	
			 		                                },
			 		                                 { name: 'edit', displayName: '', width:'15%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
		       		                                 	cellTemplate:'<i class="fa fa-trash" ng-click="grid.appScope.activeLocation(row)" style="margin-top:7px;margin-left:14px;" title="Active Location"></i>', 
			 		                                 },
	     		                               ];
	 		 
	 		$scope.gotoProfile = function() {
	 			$location.path('/myprofile');
	 		};
	 		
	 		$scope.goToLoaction = function() {
	 			$location.path('/createLocation');
	 		};
	 		
	 		$scope.goToDeactivateLoaction = function() {
	 			$location.path('/deactiveLocations');
	 		};
	 		$scope.init = function() {
	 			$http.get('/getDeactiveLocationForGM')
	 			.success(function(data) {
	 				console.log(data);
	 				$scope.gridOptions.data = data;
	 			});
	 		};
	 		
	 		$scope.activeLocation = function(row){
	 			console.log(row.entity);
	 			$('#deactiveLocation').click();
	 			$scope.rowDataVal = row;
	 		};
	 		
	 		$scope.activeLocationById = function() {
	 			console.log($scope.rowDataVal.entity.id);
	 			$http.get('/activeLocationById/'+$scope.rowDataVal.entity.id)
	 			.success(function(data) {
	 				$('#deleteClose').click();
	 				$.pnotify({
	 				    title: "Success",
	 				    type:'success',
	 				    text: "Location active succesfully!",
	 				});
	 				$scope.init();
	 			});
	 		};

}]);