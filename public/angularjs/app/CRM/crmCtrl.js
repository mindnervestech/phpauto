angular.module('newApp')
.controller('crmCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','apiserviceCrm', function ($scope,$http,$location,$filter,$routeParams,$upload,apiserviceCrm) {
	if(!$scope.$$phase) {
		$scope.$apply();
	}
	
	apiserviceCrm.getCustomizationform('CRM').then(function(response){
		console.log(response);
		 $scope.editInput = response;
		 $scope.userFields = $scope.addFormField(angular.fromJson(response.jsonData));
		 $scope.josnData = angular.fromJson(response.jsonData);
		 console.log($scope.userFields);
		 $scope.user = {};
		});
	
	$scope.allLoc = true;
	$scope.gridOptions = {
   		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
   		    paginationPageSize: 150,
   		    enableFiltering: true,
   		    useExternalFiltering: true,
   		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
   		 };
   		 $scope.gridOptions.enableHorizontalScrollbar = 2;
   		 $scope.gridOptions.enableVerticalScrollbar = 2;
   		
   		 $scope.gridOptions.columnDefs = [
   		                                 { name: 'contactId', displayName: 'Contact Id', width:'10%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'type', displayName: 'Type',enableFiltering: false, width:'10%',
   		                                 },
   		                                 { name: 'firstName', displayName: 'First Name', width:'11%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'lastName', displayName: 'Last Name', width:'11%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'companyName', displayName: 'Company Name', width:'12%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'email', displayName: 'Email', width:'10%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'phone', displayName: 'Phone',enableFiltering: false, width:'10%',
   		                                 },
   		                                 { name: 'assignedToName', displayName: 'Assigned To',enableFiltering: false, width:'12%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'edit', displayName: 'Edit', width:'6%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
   		   	                                cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editContactsDetail(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Remove Contact" ng-click="grid.appScope.deleteContactsDetail(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i>', 
   		                                },
   		                                { name: 'newsletter', displayName: 'Newsletter',enableFiltering: false, width:'8%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
   		                                cellTemplate:'<div class="icheck-list"><input type="checkbox" ng-model="row.entity.newsletter" ng-checked:newsletter ng-change="grid.appScope.setAsRead(row.entity.newsletter,row.entity.contactId)" data-checkbox="icheckbox_flat-blue" style="margin-left:42%;"></div>', 
   		                                },
       		                                
       		                                 ];  
    
   		$scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'contactId':grid.columns[0].filters[0].term,'firstName':grid.columns[2].filters[0].term,'lastName':grid.columns[3].filters[0].term,'companyName':grid.columns[4].filters[0].term,'email':grid.columns[5].filters[0].term},undefined);
		        });
	   		
   		};
   		 
   		$scope.deleteAllContactPop = function(){
   			$('#removeModal').click();
   		};
   		$scope.removeAllContactsData = function(){
   			apiserviceCrm.removeAllContactsData().then(function(data){
   				console.log("success.");
   				$scope.getContactsData();
   			});
   		};
   		$scope.exportCsvPop = function(){
   			$('#exportModal').click();
   		};
   		$scope.exportCsv = function(){
   			
   			apiserviceCrm.exportContactsData().then(function(data){
   				$.fileDownload('/downloadStatusFile',
						{	   	
							   /*httpMethod : "POST",
							   data : {
							   }*/
						}).done(function(e, response)
								{
									$.pnotify({
												title: "Success",
												type:'success',
												text: "File download successfully",
									});
								}).fail(function(e, response)
								{
									// failure
									console.log('fail');
									console.log(response);
									console.log(e);
								});
   			});
   		}
   		
   		apiserviceCrm.getAllContactsData().then(function(data){
   			console.log(data);
				//$scope.gridOptions.data = data;
   			$scope.editgirdData(data);
				$scope.contactsList = data;
				
   		});
   		
   		
   		
  	  $scope.editgirdData = function(data){
		  $scope.gridOptions.data = data;
		  $scope.gridMapObect = [];
			var findFlag = 0;
			angular.forEach($scope.gridOptions.data,function(value,key){
				if(findFlag == 0){
					angular.forEach(value.customData,function(value1,key1){
						console.log(value1.displayGrid);
						if(value1.displayGrid == "true"){
						$scope.gridMapObect.push({values: value1.value , key: value1.key});
					}
						findFlag = 1;
					});
				}
			});
			angular.forEach($scope.gridOptions.data,function(value,key){
				angular.forEach($scope.gridMapObect,function(value1,key1){
					var name = value1.key;
					name = name.replace(" ","");
					value[name] = null;
					angular.forEach(value.customData,function(value2,key2){
						if(value1.key == value2.key){
							value[name] = value2.value;
						}
					});
				});
			});	
			
			console.log($scope.gridMapObect);
			angular.forEach($scope.gridMapObect,function(value,key){
				var name = value.key;
				name = name.replace(" ","");
				console.log($scope.gridMapObect);
				$scope.gridOptions.columnDefs.push({ name: name, displayName: name, width:'10%',cellEditableCondition: false,
	              	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	              		if (row.entity.isRead === false) {
                            return 'red';
                        }
	              	} ,
	               });
			});
			
			/*$scope.gridOptions.columnDefs.push({name: 'isRead', displayName: 'Claim',enableFiltering: false, width:'7%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
          	 cellTemplate:'<div class="icheck-list"><input type="checkbox" ng-model="row.entity.isRead" ng-change="grid.appScope.setAsRead(row.entity.isRead,row.entity.id)" data-checkbox="icheckbox_flat-blue" title="Claim this lead" style="margin-left:18%;"></div>', 
            	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                    if (row.entity.isRead === false) {
                      return 'red';
                  }
            	} ,
             });*/
	  }
	  
   		
   		
   		
  	    apiserviceCrm.getUsers().then(function(data){
			$scope.allUser = data;
		 });
  	    
  	    apiserviceCrm.getlocations().then(function(data){
			$scope.locationData = data;
		 });
  	    
  	    apiserviceCrm.getUserRole().then(function(data){
			$scope.userRole = data.role;
			if($scope.userRole != "General Manager"){
			}
		});
   		$scope.getLocationData = function(locatnId){
   			$scope.allLoc = false;
   			$scope.locId = locatnId;
   			console.log($scope.locId);
   			if(locatnId !=null){
   				apiserviceCrm.getAllContactsByLocation(locatnId).then(function(data){
   					$scope.gridOptions.data = data;
   	   				$scope.contactsList = data;
   				});
   			}
   		};

   		 apiserviceCrm.getgroupInfo().then(function(data){
			$scope.allGroup = data;
		 });
   		 
   		 $scope.getContactsData = function() {
   			 $scope.allLoc = true;
   			$scope.locId = null;
   		  apiserviceCrm.getAllContactsData().then(function(data){
   				$scope.gridOptions.data = data;
   				$scope.contactsList = data;
   				
   				$scope.customData = data.customMapData;
   			 console.log($scope.customData);
   			 $scope.contactsList.collection=data.collection;
   			 if($scope.customData.time_range != undefined){
   				 $("#bestTimes").val($scope.customData.time_range);
   			 }
   			 
   			 if($scope.customData.address_bar != undefined){
   				 $("#autocomplete").val($scope.customData.address_bar);
   			 }
   			 
   			
   			 
   			 $.each($scope.customData, function(attr, value) {
   				 var res = value.split("[");
   					 if(res[1] != undefined){
   						 console.log(JSON.parse(value));
   						 $scope.customData[attr] = JSON.parse(value);
   				   	  			
   					 }
   							
   				 });
   			 
   			 console.log($scope.customData);
   				
   			 });
   		 }
   		 
   		var logofile;
		$scope.onCsvFileSelect = function($files) {
			logofile = $files;
		}
	   $scope.progress;
	   $scope.showProgress = false;
	   $scope.saveContactsFile = function() {
		   apiserviceCrm.uploadContactsFile(logofile).then(function(data){
			   $scope.getContactsData();
		   });
	   }
	   
   		$scope.contactsDetails = {};
	   $scope.editContactsDetail = function(row) {
		   $scope.showFormly = '0';
		   $scope.showFormly1 = '1';
		   $scope.contactsDetails = row.entity;
		   if($scope.contactsDetails.email1 != null){
			   $scope.showEmail1 = 1;
		   }else{
			   $scope.showEmail1 = 0;
		   }
		  		   
		   if($scope.contactsDetails.phone1 != null){
			   $scope.showanotherPhone = 1;
		   }else{
			   $scope.showanotherPhone = 0;
		   }
		   if($scope.contactsDetails.website != null){
			   $scope.showWebsite = 1;
		   }else{
			   $scope.showWebsite = 0;
		   }
		   if($scope.contactsDetails.allAddresses != null){
			   $scope.showAddess = 1;
		   }else{
			   $scope.showAddess = 0;
		   }
		   if($scope.contactsDetails.campaignSource != null){
			   $scope.showCampaign = 1;
		   }else{
			   $scope.showCampaign = 0;
		   }
		   if($scope.contactsDetails.priority != null){
			   $scope.showPriority = 1;
		   }else{
			   $scope.showPriority = 0;
		   }
		   if($scope.contactsDetails.groups != null){
			   $scope.showGroup = 1;
		   }else{
			   $scope.showGroup = 0;
		   }
		   
		   $scope.customData = row.entity.customMapData;
			 console.log($scope.customData);
			 $scope.contactsList.collection=row.entity.collection;
			 if($scope.customData.time_range != undefined){
				 $("#bestTimes").val($scope.customData.time_range);
			 }
			 
			 if($scope.customData.address_bar != undefined){
				 $("#autocomplete").val($scope.customData.address_bar);
			 }
			 
			
			 
			 $.each($scope.customData, function(attr, value) {
				 value = JSON.stringify(value);
	   				console.log(value);
				 console.log(value);
				 
				 var res = value.split("[");
					 if(res[1] != undefined){
						 console.log(JSON.parse(value));
						 $scope.customData[attr] = JSON.parse(value);
				   	  			
					 }
							
				 });
			 
		   $('#contactsModal').modal();
	   }
   		 
	   $scope.updateContacts = function() {
		   console.log("grfe");
		   $scope.customList =[];
		   console.log($("#autocomplete").val());
		   console.log($scope.specification);
		   $scope.customData.custName = $('#exCustoms_value').val();
			if($scope.customData.custName == undefined){
				delete $scope.customData.custName;
			}
			$scope.customData.address_bar = $("#autocomplete").val();
			if($scope.customData.address_bar == undefined){
				delete $scope.customData.address_bar;
			}
			$scope.customData.time_range = $("#bestTimes").val();
			if($scope.customData.time_range == undefined){
				delete $scope.customData.time_range;
			}
			
			console.log($scope.customData);
			console.log($scope.josnData);
		
		$.each($scope.customData, function(attr, value) {
			angular.forEach($scope.josnData, function(value1, key) {
				if(value1.key == attr){
					if(angular.isObject(value) == true){
						console.log(value);
						console.log(angular.toJson(value));
						$scope.customList.push({
			   	  			key:attr,
			   	  			value:angular.toJson(value),
			   	  			savecrm:value1.savecrm,
			   	  			displayGrid:value1.displayGrid,
			   	  			
						});
					}else{
						$scope.customList.push({
			   	  			key:attr,
			   	  			value:value,
			   	  			savecrm:value1.savecrm,
			   	  			displayGrid:value1.displayGrid,
			   	  			
						});
					}
					
				} 
			});
		   });
		console.log($scope.customList);
		$scope.contactsDetails.customData = $scope.customList;
		 apiserviceCrm.updateContactsData($scope.contactsDetails).then(function(data){
			 $('#contactsModal').modal('hide');
		 });
		   
	   }
	   
	   $scope.setAsRead = function(newsletter,id) {
		   
		   apiserviceCrm.addNewsLetter(newsletter,id).then(function(data){
		   });
		   
	   }
	   $scope.showFormly = '0';
	   $scope.showFormly1 = '0';
	   $scope.contactMsg = "";
	   $scope.createContact = function() {
		   $scope.showFormly = '1';
		   $scope.showFormly1 = '0';
		   $scope.contactsDetails = {};
		   $scope.contactsDetails.workEmail = "Work";
		   $scope.contactsDetails.workEmail1 = "Work";
		   $scope.contactsDetails.workPhone = "Work";
		   $scope.contactsDetails.workPhone1 = "Work";
		   $scope.contactMsg = "";
		   $('#createcontactsModal').modal();
	   }
	   $scope.customData = {};
	   $scope.saveContact = function() {
		   $scope.customList =[];
			
			console.log($("#autocomplete").val());
			   console.log($scope.specification);
			   $scope.customData.custName = $('#exCustoms_value').val();
				if($scope.customData.custName == undefined){
					delete $scope.customData.custName;
				}
				$scope.customData.address_bar = $("#autocomplete").val();
				if($scope.customData.address_bar == undefined){
					delete $scope.customData.address_bar;
				}
				$scope.customData.time_range = $("#bestTimes").val();
				if($scope.customData.time_range == undefined){
					delete $scope.customData.time_range;
				}
				
				console.log($scope.customData);
				console.log($scope.userFields);
			
			$.each($scope.customData, function(attr, value) {
				angular.forEach($scope.josnData, function(value1, key) {
					if(value1.key == attr){
						if(angular.isObject(value) == true){
							console.log(value);
							console.log(angular.toJson(value));
							$scope.customList.push({
				   	  			key:attr,
				   	  			value:angular.toJson(value),
				   	  			savecrm:value1.savecrm,
				   	  			displayGrid:value1.displayGrid,
				   	  			
							});
						}else{
							$scope.customList.push({
				   	  			key:attr,
				   	  			value:value,
				   	  			savecrm:value1.savecrm,
				   	  			displayGrid:value1.displayGrid,
				   	  			
							});
						}
						
					} 
				});
			   });
			
			 
			
			console.log($scope.customList);
			$scope.contactsDetails.customData = $scope.customList;
			apiserviceCrm.saveContactsData($scope.contactsDetails).then(function(data){
				 if(data == "") {
					 $scope.contactMsg="";
					 $('#createcontactsModal').modal('hide');
					 $scope.getContactsData();
				 } else {
					 $scope.contactMsg = data;
				 }
				 
				});
	   }
	   
	   $scope.saveGroup = function(createGroup){
		   apiserviceCrm.saveGroup(createGroup).then(function(data){
			   apiserviceCrm.getgroupInfo().then(function(data){
					$scope.allGroup = data;
				 });
			});
	   }
	   
	   $scope.deleteGroup = function(groupId){
		   console.log(groupId);
		   apiserviceCrm.deleteGroup(groupId).then(function(data){
			   apiserviceCrm.getgroupInfo().then(function(data){
					console.log(data);
					$scope.allGroup = data;
				 });
			});
	   }
	   
	   $scope.deleteContactsDetail = function(row){
		   $('#deleteModal').click();
		   $scope.rowDataVal = row;
	    }
		   
		 $scope.deleteContactRow = function() {
			apiserviceCrm.deleteContactsById($scope.rowDataVal.entity.contactId).then(function(data){
			 if(data=='success'){
				 $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Delete successfully",
				 });
	
				 $scope.getContactsData();
			 }else{
				 $.pnotify({
				    title: "Error",
				    type:'error',
				    text: "Contact can't deleted",
				 });
	
			 }

		 	});
		}
		 $scope.callListMailChim = function(){
			 apiserviceCrm.callList().then(function(data){
							$scope.getContactsData();
							
						});
			
		}
	   
}]);