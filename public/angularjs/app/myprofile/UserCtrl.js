angular.module('newApp')
.controller('createUserCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','apiserviceUser', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,apiserviceUser) {
	$scope.img = "/assets/images/profile-pic.jpg";
	$scope.user = {};
	$scope.permission = [];
	$scope.permissionList =[
	{name:'Dashboard',isSelected:false},
	{name:'Website Editing',isSelected:false},
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
	
	 		 
	 		$scope.gridOptions1 = {
	 		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		 		    paginationPageSize: 150,
	 		 		    enableFiltering: false,
	 		 		    useExternalFiltering: false,
	 		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 		 };
	 		 		 $scope.gridOptions1.enableHorizontalScrollbar = 0;
	 		 		 $scope.gridOptions1.enableVerticalScrollbar = 2;
	 		 		 $scope.gridOptions1.columnDefs = [
														{ name: 'edit', displayName: '', width:'5%',cellEditableCondition: false,
														 	cellTemplate:'<input type="checkbox" ng-change="grid.appScope.selectLead(row)" ng-model="row.entity.check"> ',
															 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
														               return 'red';
														           }
														     	} ,
														  },
														{ name: 'name', displayName: 'Name', width:'10%',cellEditableCondition: false,
													     	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.name}}</a> ',
													    	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
													    		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
													                   return 'red';
													               }
													         	} ,
													      },
													      { name: 'phone', displayName: 'Phone', width:'10%',cellEditableCondition: false,
													     	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.phone}}</a> ',
													     	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
													     		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
													                return 'red';
													            }
													      	} ,
													      },
													      { name: 'email', displayName: 'Email', width:'15%',cellEditableCondition: false,
													     	cellTemplate:'<a  href="mailto:{{row.entity.email}}">{{row.entity.email}}</a> ',
													     	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
													     		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
													                return 'red';
													            }
													      	} ,
													      },
													   { name: 'requestDate', displayName: 'Date Added', width:'10%',cellEditableCondition: false,
													     	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.requestDate}}</a> ',
													     	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
													     		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
													                 return 'red';
													             }
													       	} ,
													       },
		 		 		                                 { name: 'vin', displayName: 'Vin', width:'15%',cellEditableCondition: false,
		 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.vin}}</a> ',
		 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
		 		   		                                         return 'red';
		 		   		                                     }
		 		  		                                	} ,
		 		 		                                 },
		 		 		                                 { name: 'model', displayName: 'Model', width:'10%',cellEditableCondition: false,
		 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.model}}</a> ',
		 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
		 		   		                                         return 'red';
		 		   		                                     }
		 		  		                                	} ,
		 		 		                                 },
		 		 		                                 { name: 'make', displayName: 'Make', width:'10%',cellEditableCondition: false,
		 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.make}}</a> ',
		 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
		 		   		                                         return 'red';
		 		   		                                     }
		 		  		                                	} ,
		 		 		                                 },
		 		 		                            { name: 'leadType', displayName: 'type', width:'15%',cellEditableCondition: false,
		 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.leadType}}</a> ',
		 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
		 		   		                                         return 'red';
		 		   		                                     }
		 		  		                                	} ,
		 		 		                                 },
		 		 		                                 
		 		     		                          ];	 		 
	 		 
	$scope.gotoProfile = function() {
		$location.path('/myprofile');
	};
	$scope.goToDeactive = function() {
		$location.path('/deactiveUsers');
	};
	$scope.gotoHoursOfOperation = function() {
			$location.path('/hoursOfOperations');
		};
	
	$scope.userId = null;
	$scope.checked = [];
	$scope.usersList = null;
	$scope.assignUser = null;
	$scope.userLeads = {};
	
	apiserviceUser.getAllUsersToAssign().then(function(data){
		$scope.usersList = data;
		angular.forEach($scope.usersList, function(obj, index){
			 if ((obj.userType == "Manager")) {
				 $scope.assignUser = obj.id;
		    };
		  });	
	});
	/*$http.get('/getAllUsersToAssign')
	.success(function(data) {
		$scope.usersList = data;
		angular.forEach($scope.usersList, function(obj, index){
			 if ((obj.userType == "Manager")) {
				 $scope.assignUser = obj.id;
		    };
		  });
	});*/
	
	apiserviceUser.getInternalPdfData().then(function(data){
		$scope.internalPdfList=data;
	});
	
	/*$http.get('/getInternalPdfData')
		.success(function(data) {
			
			$scope.internalPdfList=data;
		});	*/	
	
	$scope.selectLead = function(row){
		if(row.entity.check == true){
			$scope.checked.push(row.entity);
		}else{
			$scope.deleteSelectedLead(row.entity);
		}
	};
	
	$scope.deleteSelectedLead = function(item){
		angular.forEach($scope.checked, function(obj, index){
			 if ((item.id == obj.id) && (item.leadType == obj.leadType)) {
				 $scope.checked.splice(index, 1);
		       	return;
		    };
		  });
	}
	
	$scope.selectAll = function(){
		$scope.checked = [];
		angular.forEach($scope.gridOptions1.data, function(obj, index){
			 obj.check = true;
			 $scope.checked.push(obj);
		  });
		console.log($scope.checked);
	};
	$scope.assignTo = function(){
		//console.log($scope.assignUser);
		//console.log($scope.checked);
		$scope.userLeads.id = $scope.assignUser;
		$scope.userLeads.leads = $scope.checked;
		//console.log($scope.userLeads);
		if($scope.userLeads.leads.length > 0){
			
			apiserviceUser.assignToUser($scope.userLeads).then(function(data){
				apiserviceUser.getAllLeadsByUser($scope.userId).then(function(data){
					$scope.gridOptions1.data = data;
				});
		            $scope.clouseUserLead();
			});
			
		}else{
			$.pnotify({
			    title: "Error",
			    type:'success',
			    text: "Please select leads",
			});
		}
	};
	$scope.deactivateAccount = function(){
		console.log($scope.gridOptions1.data.length);
		if($scope.gridOptions1.data.length <= 0){
			apiserviceUser.deactivateAccount($scope.userId).then(function(data){
				$scope.init();
    			$('#clsUserLead').click();
			});
		}else{
			$.pnotify({
			    title: "Error",
			    type:'success',
			    text: "Please assign all leads",
			});
		}
	};
	
	
	$scope.clouseUserLead = function(){
		$scope.checked = [];
		$scope.usersList = null;
		$scope.assignUser = null;
		$scope.userLeads = {};
		
		apiserviceUser.getAllUsersToAssign().then(function(data){
			$scope.usersList = data;
			angular.forEach($scope.usersList, function(obj, index){
				 if ((obj.userType == "Manager")) {
					 $scope.assignUser = obj.id;
			    };
			  });
		});
	};
	$scope.showOtherFild = 0;
	
	$scope.selectOption = function(){
		var type = $('#userType').val();
		console.log(type);
		
		if(type == "Sales Person"){
			$scope.showOtherFild = 1;
		}
		
		if(type == "Photographer"){
		
			$scope.showOtherFild = 2;
		}
		
		
		
		if(type == "Sales Person"){
			//console.log("in if");
			angular.forEach($scope.permissionList, function(obj, index){
				 if ((obj.name == "My Profile") || (obj.name == "Inventory") || (obj.name == "Dashboard")) {
					 $scope.permission.push(obj.name);
					 obj.isSelected = true;
			    };
			  });
		}else{
			//console.log("in else");
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
		apiserviceUser.getAllUsers().then(function(data){
			$scope.gridOptions.data = data;
		});
	}
	
	$scope.contract = function(type){
		$scope.contactVal= type;
		if(type=="Employee" || type=="One Time Order"){
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
		//console.log(validText);
		if(validText == "Yes"){
			$scope.showText = 1;
		}else{
			$scope.showText = 0;
		}
		
	}
	$scope.getTrial = function(validText){
		//console.log(validText);
		if(validText == "Yes"){
			$scope.showText = 1;
		}else{
			$scope.showText = 0;
		}
		
	}
	
	//console.log($scope.permissionList);
	
	$scope.rolesClicked = function(e, rolePer,value){
		console.log(rolePer);
		console.log(value);
		if(value == false){
			$scope.permission.push(rolePer.name);
		}else{
			$scope.deleteItem(rolePer);
		}
		//console.log($scope.permission);
	}
	$scope.deleteItem = function(rolePer){
		angular.forEach($scope.permission, function(obj, index){
			 if ((rolePer.name == obj)) {
				 $scope.permission.splice(index, 1);
		       	return;
		    };
		  });
	}
	
	
	$scope.pdfDoc = [];
	$scope.selectPdf = function(e,item,value){
		console.log(item);
		console.log(value);
		if(value == false || value == undefined){
			$scope.pdfDoc.push(item.internalPdfId);
		}else{
			$scope.deletepdfItem(item);
		}
		console.log($scope.pdfDoc);
	}
	$scope.deletepdfItem = function(item){
		angular.forEach($scope.pdfDoc, function(obj, index){
			 if ((item.internalPdfId == obj)) {
				 $scope.pdfDoc.splice(index, 1);
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
		//console.log($scope.num1);
		//console.log($scope.duration1);
		$scope.userData = row.entity;
			
		
		if($scope.userData.userType == "Sales Person"){
			$scope.showOtherFild = 1;
		}
		
		if($scope.userData.userType == "Photographer"){
		
			$scope.showOtherFild = 2;
		}
		
		
		$scope.userData.trialPeriod = parseInt($scope.userData.trialPeriod);
		//console.log($scope.userData);
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
		//console.log(row.entity);
		//console.log(row.entity.id);
		$scope.userId = row.entity.id;
		apiserviceUser.getAllLeadsByUser($scope.userId).then(function(data){
			$scope.gridOptions1.data = data;
		});
		/*$http.get('/getAllLeadsByUser/'+$scope.userId)
		.success(function(data) {
			//console.log(data);
		$scope.gridOptions1.data = data;
		
	});*/
		$('#deleteModal').click();
		   $scope.rowDataVal = row;
	};
	
	/*$scope.deleteUserById = function() {
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
	};*/
	
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
	   
		$scope.hOperation = {};
	$scope.saveImage = function() {
		apiserviceUser.findLocation().then(function(data){
			console.log(data);
			$scope.user.locationId = data;
		
		$scope.user.permissions = $scope.permission;
		$scope.user.pdfIds = $scope.pdfDoc;
		
		if($scope.user.premiumFlag == undefined){
			$scope.user.premiumFlag = false;
		}
		
		if($scope.user.userType == "Photographer"){
			$scope.user.hOperation.sunOpenTime = $('#sunOpen').val();
			$scope.user.hOperation.monOpenTime = $('#monOpen').val();
			$scope.user.hOperation.tueOpenTime = $('#tueOpen').val();
			$scope.user.hOperation.wedOpenTime = $('#wedOpen').val();
			$scope.user.hOperation.thuOpenTime = $('#thuOpen').val();
			$scope.user.hOperation.friOpenTime = $('#friOpen').val();
			$scope.user.hOperation.satOpenTime = $('#satOpen').val();
			
			$scope.user.hOperation.sunCloseTime = $('#sunClose').val();
			$scope.user.hOperation.monCloseTime = $('#monClose').val();
			$scope.user.hOperation.tueCloseTime = $('#tueClose').val();
			$scope.user.hOperation.wedCloseTime = $('#wedClose').val();
			$scope.user.hOperation.thuCloseTime = $('#thuClose').val();
			$scope.user.hOperation.friCloseTime = $('#friClose').val();
			$scope.user.hOperation.satCloseTime = $('#satClose').val();


			if($scope.user.hOperation.sunOpen == undefined){
				$scope.user.hOperation.sunOpen = false;
			}
			if($scope.user.hOperation.monOpen == undefined){
				$scope.user.hOperation.monOpen = false;
			}
			if($scope.user.hOperation.tueOpen == undefined){
				$scope.user.hOperation.tueOpen = false;
			}
			if($scope.user.hOperation.wedOpen == undefined){
				$scope.user.hOperation.wedOpen = false;
			}
			if($scope.user.hOperation.thuOpen == undefined){
				$scope.user.hOperation.thuOpen = false;
			}
			if($scope.user.hOperation.friOpen == undefined){
				$scope.user.hOperation.friOpen = false;
			}
			if($scope.user.hOperation.satOpen == undefined){
				$scope.user.hOperation.satOpen = false;
			}
			
				$scope.user.portalName = "MavenFurniture";
			
			if($scope.user.hOperation.sunClose == undefined){
				$scope.user.hOperation.sunClose = false;
			}
			if($scope.user.hOperation.monClose == undefined){
				$scope.user.hOperation.monClose = false;
			}
			if($scope.user.hOperation.tueClose == undefined){
				$scope.user.hOperation.tueClose = false;
			}
			if($scope.user.hOperation.wedClose == undefined){
				$scope.user.hOperation.wedClose = false;
			}
			if($scope.user.hOperation.thuClose == undefined){
				$scope.user.hOperation.thuClose = false;
			}
			if($scope.user.hOperation.friClose == undefined){
				$scope.user.hOperation.friClose = false;
			}
			if($scope.user.hOperation.satClose == undefined){
				$scope.user.hOperation.satClose = false;
			}
			
			
			$scope.user.contractDurStartDate = $("#cnfstartDateValue").val();
			$scope.user.contractDurEndDate = $("#cnfendDateValue").val();
		}
		
		if($scope.contactVal=="Employee"){
			$scope.user.contractDur = $scope.contactVal;
		}else if($scope.contactVal=="One Time Order"){
			$scope.user.contractDur = $scope.contactVal;
		}else{
			if($scope.num==null){
				$scope.num=0;
			}
			$scope.user.contractDur = $scope.num+" "+$scope.duration;
		}
		$scope.user.imageUrl = $scope.img;
		if($scope.user.userType == "Photographer"){
			if(angular.isUndefined(logofile)) {
				//if($scope.emailMsg == "") {
					apiserviceUser.uploadImageFile($scope.user, $scope.user.userType).then(function(data){
						$scope.user.firstName=" ";
			            $scope.user.lastName=" ";
			            $scope.user.email=" ";
			            $scope.user.phone=" ";
			            $scope.user.userType=" ";
			            $scope.user.img=" ";
			            $('#btnClose').click();
					});
					
					
				//}
			} else {
				//if($scope.emailMsg == "") {
					apiserviceUser.uploadImageFileLoad($scope.user, $scope.user.userType, logofile).then(function(data){
						 console.log('success');
				            $scope.user.firstName=" ";
				            $scope.user.lastName=" ";
				            $scope.user.email=" ";
				            $scope.user.phone=" ";
				            $scope.user.userType=" ";
				            $scope.user.img=" ";
				            $("#file").val('');
				            $('#btnClose').click();
					});
				//}
			}
		}else{
			if(angular.isUndefined(logofile)) {
				//if($scope.emailMsg == "") {
					apiserviceUser.uploadImageFile($scope.user, $scope.user.userType).then(function(data){
						$scope.user.firstName=" ";
			            $scope.user.lastName=" ";
			            $scope.user.email=" ";
			            $scope.user.phone=" ";
			            $scope.user.userType=" ";
			            $scope.user.img=" ";
			            $('#btnClose').click();
			            
			            $scope.init();
					});
				//}else{
				//	console.log("9999");
				//}
			} else {
				//if($scope.emailMsg == "") {
					
					apiserviceUser.uploadImageFileLoad($scope.user, $scope.user.userType, logofile).then(function(data){
						 console.log('success');
				            $scope.user.firstName=" ";
				            $scope.user.lastName=" ";
				            $scope.user.email=" ";
				            $scope.user.phone=" ";
				            $scope.user.userType=" ";
				            $scope.user.img=" ";
				            $("#file").val('');
				            $('#btnClose').click();
				            $scope.init();
					});
				  
				//}
			}
		}
		
		});
		
	   }
	
	$scope.openClick = function(){
		$('#sunOpen').timepicker();
		$('#sunClose').timepicker();
		$('#monOpen').timepicker();
		$('#monClose').timepicker();
		$('#tueOpen').timepicker();
		$('#tueClose').timepicker();
		$('#wedOpen').timepicker();
		$('#wedClose').timepicker();
		$('#thuOpen').timepicker();
		$('#thuClose').timepicker();
		$('#friOpen').timepicker();
		$('#friClose').timepicker();
		$('#satOpen').timepicker();
		$('#satClose').timepicker();
		
	}
	
	$scope.updateImage = function() {
		$scope.userData.permissions = $scope.permission;
		$scope.userData.pdfIds = $scope.pdfDoc;
		delete $scope.userData.successRate;
		
		if($scope.userData.userType == "Photographer"){
			$scope.userData.hOperation.sunOpenTime = $('#sunOpen').val();
			$scope.userData.hOperation.monOpenTime = $('#monOpen').val();
			$scope.userData.hOperation.tueOpenTime = $('#tueOpen').val();
			$scope.userData.hOperation.wedOpenTime = $('#wedOpen').val();
			$scope.userData.hOperation.thuOpenTime = $('#thuOpen').val();
			$scope.userData.hOperation.friOpenTime = $('#friOpen').val();
			$scope.userData.hOperation.satOpenTime = $('#satOpen').val();
			
			if($scope.userData.hOperation.sunOpen == undefined){
				$scope.userData.hOperation.sunOpen = false;
			}
			if($scope.userData.hOperation.monOpen == undefined){
				$scope.userData.hOperation.monOpen = false;
			}
			if($scope.userData.hOperation.tueOpen == undefined){
				$scope.userData.hOperation.tueOpen = false;
			}
			if($scope.userData.hOperation.wedOpen == undefined){
				$scope.userData.hOperation.wedOpen = false;
			}
			if($scope.userData.hOperation.thuOpen == undefined){
				$scope.userData.hOperation.thuOpen = false;
			}
			if($scope.userData.hOperation.friOpen == undefined){
				$scope.userData.hOperation.friOpen = false;
			}
			if($scope.userData.hOperation.satOpen == undefined){
				$scope.userData.hOperation.satOpen = false;
			}
		}
		
		if($scope.contactVal=="Employee"){
			$scope.userData.contractDur = $scope.contactVal;
		}else if($scope.contactVal=="One Time Order"){
			$scope.userData.contractDur = $scope.contactVal;
		}else{
			if($scope.num1==null){
				$scope.num1=0;
			}
			$scope.userData.contractDur = $scope.num1+" "+$scope.duration1;
		}
		
		//console.log($scope.userData);
		$scope.userData.locationId = 0;
		if(angular.isUndefined(logofile)) {
			$scope.userData.imageUrl = $scope.img;
			//console.log("no logofile");
			if($scope.emailMsg == "") {
				apiserviceUser.updateImageFile($scope.userData).then(function(data){
					$('#btnEditClose').click();
		            $scope.init();
				});
					/*$http.post('/updateImageFile',$scope.userData)
					.success(function(data) {
						
					});*/
				}
			} else {
				if($scope.emailMsg == "") {
					
					apiserviceUser.updateImageFileLoad($scope.userData, logofile).then(function(data){
						$('#btnEditClose').click();
			            $scope.init();
					});
					
					/*$upload.upload({
			            url : '/updateImageFile',
			            method: 'post',
			            file:logofile,
			            data:$scope.userData
			        }).success(function(data, status, headers, config) {
			            //console.log('success');
			            $('#btnEditClose').click();
			           
			            $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "User saved successfully",
						});
			            $scope.init();
			        });*/
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
		
		apiserviceUser.checkEmailOfUser($scope.email).then(function(data){
			$scope.emailMsg = data;
		});
		/*$http.get('/checkEmailOfUser/'+$scope.email)
		.success(function(data) {
			$scope.emailMsg = data;
		});*/
	}
	
}]);	

angular.module('newApp')
.controller('DeactivateUserCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
	//console.log("in DeactivateUserCtrl");
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
       		                                 cellTemplate:'<button type="button" ng-click="grid.appScope.activateAccount(row.entity)" style="margin-left:5px;" class="btn btn-sm btn-primary">Active</button>', 
   		                                 
   		                                 },
	     		                                 ];
	 		$scope.init = function() {
	 			$http.get('/getAllDeactivateUsers')
	 			.success(function(data) {
	 				//console.log(data);
	 			$scope.gridOptions.data = data;
	 			
	 			});
	 		};
	 		$scope.activateAccount = function(item){
	 			//console.log(item);
	 			$scope.userId = item.id;
	 			$('#activateModal').click();
	 		};
	 		$scope.activeUserAccount = function(){
	 			console.log($scope.userId);
	 			$http.get('/activeAccount/'+$scope.userId)
	 			.success(function(data) {
	 				//console.log("success");
	 				$scope.init();
	 			});
	 		};
	 		$scope.gotoProfile = function() {
	 			$location.path('/myprofile');
	 		};
	 		$scope.goToLoaction = function() {
	 			$location.path('/createLocation');
	 		};
	 		
	 		$scope.goToUsers = function() {
	 			$location.path('/createUser');
	 		};
	 		$scope.goToDeactive = function() {
	 			$location.path('/deactiveUsers');
	 		};
}]);