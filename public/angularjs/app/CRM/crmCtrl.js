angular.module('newApp')
.controller('crmCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {
	if(!$scope.$$phase) {
		$scope.$apply();
	}
	
	$http.get('/getCustomizationform/'+'CRM').success(function(response) {
		console.log(response);
		 $scope.editInput = response;
		 $scope.userFields = $scope.addFormField(angular.fromJson(response.jsonData));
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
   		 $scope.gridOptions.enableHorizontalScrollbar = 0;
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
   			$http.get('/removeAllContactsData').success(function(data){
   				console.log("success.");
   				$.pnotify({
						title: "Success",
						type:'success',
						text: "File download successfully",
   					});
   				$scope.getContactsData();
   			});
   		};
   		$scope.exportCsvPop = function(){
   			$('#exportModal').click();
   		};
   		$scope.exportCsv = function(){
   			$http.get('/exportContactsData').success(function(data){
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
   		
   		$http.get('/getAllContactsData').success(function(data){
   			console.log(data);
				$scope.gridOptions.data = data;
				$scope.contactsList = data;
				
   		});
   		 
   		$http.get('/getUsers').success(function(data){
			$scope.allUser = data;
		 });
   		$http.get('/getlocations').success(function(data){
			$scope.locationData = data;
		 });
   		$http.get('/getUserRole').success(function(data) {
			$scope.userRole = data.role;
			if($scope.userRole != "General Manager"){
			}
		});
   		$scope.getLocationData = function(locatnId){
   			$scope.allLoc = false;
   			$scope.locId = locatnId;
   			console.log($scope.locId);
   			if(locatnId !=null){
   				$http.get('/getAllContactsByLocation/'+locatnId)
   				.success(function(data) {
   					$scope.gridOptions.data = data;
   	   				$scope.contactsList = data;
   				});
   			}
   		};

   		$http.get('/getgroupInfo').success(function(data){
			$scope.allGroup = data;
		 });
   		 
   		 $scope.getContactsData = function() {
   			 $scope.allLoc = true;
   			$scope.locId = null;
   			 $http.get('/getAllContactsData').success(function(data){
   				$scope.gridOptions.data = data;
   				$scope.contactsList = data;
   				
   				$scope.customData = data.customMapData;
   			 console.log(data);
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
		   $upload.upload({
	            url : '/uploadContactsFile',
	            method: 'post',
	            file:logofile,
		   }).progress(function(evt) {
			   $scope.showProgress = true;
			   $scope.progress = parseInt((100.0 * evt.loaded) / evt.total)+"%";
	        }).success(function(data, status, headers, config) {
	            console.log('success');
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "file uploaded successfully",
				});
	            $scope.getContactsData();
	        });
	   }
	   
   		$scope.contactsDetails = {};
	   $scope.editContactsDetail = function(row) {
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
				 var res = value.split("[");
					 if(res[1] != undefined){
						 console.log(JSON.parse(value));
						 $scope.customData[attr] = JSON.parse(value);
				   	  			
					 }
							
				 });
			 
			 console.log($scope.customData);
		
		   
		   
		   
		   
		   
		   
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
			console.log($scope.userFields);
		
		$.each($scope.customData, function(attr, value) {
			angular.forEach($scope.userFields, function(value1, key) {
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
		   
		   $http.post('/updateContactsData',$scope.contactsDetails)
			 .success(function(data) {
				 $('#contactsModal').modal('hide');
				 $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "contact updated successfully",
					});
				});
	   }
	   
	   $scope.setAsRead = function(newsletter,id) {
		   $http.get('/addNewsLetter/'+newsletter+'/'+id)
			.success(function(data) {
				
		});
	   }
	   $scope.contactMsg = "";
	   $scope.createContact = function() {
		   $scope.contactsDetails = {};
		   $scope.contactsDetails.workEmail = "Work";
		   $scope.contactsDetails.workEmail1 = "Work";
		   $scope.contactsDetails.workPhone = "Work";
		   $scope.contactsDetails.workPhone1 = "Work";
		   $scope.contactMsg = "";
		   $('#createcontactsModal').modal();
	   }
	   
	   $scope.saveContact = function() {
		   console.log(">>>>>");
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
				angular.forEach($scope.userFields, function(value1, key) {
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
		   
		  $http.post('/saveContactsData',$scope.contactsDetails)
			 .success(function(data) {
				 if(data == "") {
					 $('#createcontactsModal').modal('hide');
					 $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "contact saved successfully",
						});
					 $scope.getContactsData();
				 } else {
					 $scope.contactMsg = data;
				 }
				 
				});
	   }
	   
	   $scope.saveGroup = function(createGroup){
		   $http.get('/saveGroup/'+createGroup)
			.success(function(data) {
				console.log("sccess");
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "group saved successfully",
				});
				$http.get('/getgroupInfo').success(function(data){
					$scope.allGroup = data;
				 });
			});
	   }
	   
	   $scope.deleteGroup = function(groupId){
		   console.log(groupId);
		   $http.get('/deleteGroup/'+groupId)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "group deleted successfully",
				});
				$http.get('/getgroupInfo').success(function(data){
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
		 $http.get('/deleteContactsById/'+$scope.rowDataVal.entity.contactId)
		 .success(function(data) {
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
	   
}]);