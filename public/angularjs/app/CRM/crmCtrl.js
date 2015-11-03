angular.module('newApp')
.controller('crmCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {
	if(!$scope.$$phase) {
		$scope.$apply();
	}
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
   		                                 { name: 'email', displayName: 'Email',enableFiltering: false, width:'10%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'phone', displayName: 'Phone',enableFiltering: false, width:'10%',
   		                                 },
   		                                 { name: 'assignedTo', displayName: 'Assigned To',enableFiltering: false, width:'12%',cellEditableCondition: false,
   		                                 },
	   		                              { name: 'edit', displayName: 'Edit', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
	    		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editContactsDetail(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i>', 
			                                 
			                                 },
			                                 { name: 'newsletter', displayName: 'Newsletter',enableFiltering: false, width:'9%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
	 		                                	 cellTemplate:'<div class="icheck-list"><input type="checkbox" ng-model="row.entity.newsletter" ng-change="grid.appScope.setAsRead(row.entity.newsletter,row.entity.contactId)" data-checkbox="icheckbox_flat-blue" style="margin-left:42%;"></div>', 
	 		                                 },
       		                                
       		                                 ];  
    
   		$scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'contactId':grid.columns[0].filters[0].term,'firstName':grid.columns[2].filters[0].term,'lastName':grid.columns[3].filters[0].term,'companyName':grid.columns[4].filters[0].term},undefined);
		        });
	   		
   		};
   		 
   		$http.get('/getAllContactsData').success(function(data){
				$scope.gridOptions.data = data;
				$scope.contactsList = data;
			 });
   		 
   		 
   		 $scope.getContactsData = function() {
   			 $http.get('/getAllContactsData').success(function(data){
   				$scope.gridOptions.data = data;
   				$scope.contactsList = data;
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
			   $scope.progress = parseInt(100.0 * evt.loaded / evt.total)+"%";
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
		   $('#contactsModal').modal();
	   }
   		 
	   $scope.updateContacts = function() {
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
		   console.log(newsletter);
		   console.log(id);
		   $http.get('/addNewsLetter/'+newsletter+'/'+id)
			.success(function(data) {
				
		});
	   }
	   $scope.contactMsg = "";
	   $scope.createContact = function() {
		   $scope.contactsDetails = {};
		   $scope.contactMsg = "";
		   $('#createcontactsModal').modal();
	   }
	   
	   $scope.saveContact = function() {
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
	   
}]);