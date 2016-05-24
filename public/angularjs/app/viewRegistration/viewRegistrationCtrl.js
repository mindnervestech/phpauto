angular.module('newApp')
.controller('ViewRegistrationCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
	
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
		                                 { name: 'name', displayName: 'Name', width:'15%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.sendDemoFlag == 0) {
			                                         return 'red';
			                                     }
			                                	} ,
		                                 },
		                                 { name: 'email', displayName: 'Email', width:'20%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.sendDemoFlag == 0) {
			                                         return 'red';
			                                     }
			                                	} ,
		                                 },
		                                 { name: 'businessName', displayName: 'Business Name', width:'14%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.sendDemoFlag == 0) {
			                                         return 'red';
			                                     }
			                                	} ,
		                                 },
		                                 { name: 'businessAdd', displayName: 'Business Address', width:'20%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.sendDemoFlag == 0) {
			                                         return 'red';
			                                     }
			                                	} ,
		                                 },
		                                 { name: 'options', displayName: 'Options', width:'14%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.sendDemoFlag == 0) {
			                                         return 'red';
			                                     }
			                                	} ,
		                                 },
		                                 { name: 'activity', displayName: 'Activity', width:'10%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.sendDemoFlag == 0) {
			                                         return 'red';
			                                     }
			                                	} ,
		                                 },
		                                 { name: 'edit', displayName: '', width:'10%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
    		                                 cellTemplate:'<i class="glyphicon glyphicon-ok-circle" ng-click="grid.appScope.sendDemoUrl(row)"  title="Send Demo Url"></i> &nbsp;&nbsp;&nbsp<i class="fa fa-trash" ng-click="grid.appScope.removeUser(row)"  title="Remove"></i> &nbsp;&nbsp;&nbsp<i class="glyphicon glyphicon-pencil" ng-click="grid.appScope.EditUser(row)"  title="Edit"></i>', 
    		                                 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.sendDemoFlag == 0) {
			                                         return 'red';
			                                     }
			                                	} ,
		                                 },
		                                    ];
	
		 $scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		          $scope.gridOptions.data = $filter('filter')($scope.pendingList,{'name':grid.columns[0].filters[0].term,'email':grid.columns[1].filters[0].term,'businessName':grid.columns[2].filters[0].term,'businessAdd':grid.columns[3].filters[0].term,'options':grid.columns[4].filters[0].term,'activity':grid.columns[5].filters[0].term},undefined);
		        });
	   		
  		};
  		
  		/*$scope.gridOptions1 = {
  		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
  		 		    paginationPageSize: 150,
  		 		    enableFiltering: true,
  		 		    useExternalFiltering: true,
  		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
  		 		 };
  		
  		
  		 $scope.gridOptions1.enableHorizontalScrollbar = 0;
  			 $scope.gridOptions1.enableVerticalScrollbar = 2;
  			 $scope.gridOptions1.columnDefs = [
  			                                 { name: 'name', displayName: 'Name', width:'15%',cellEditableCondition: false,
  			                                	
  			                                 },
  			                                 { name: 'email', displayName: 'Email', width:'20%',cellEditableCondition: false,
  			                                	
  			                                 },
  			                                 { name: 'businessName', displayName: 'Business Name', width:'15%',cellEditableCondition: false,
  			                                	
  			                                 },
  			                                 { name: 'businessAdd', displayName: 'Business Addree', width:'25%',cellEditableCondition: false,
  			                                	
  			                                 },
  			                                 { name: 'options', displayName: 'Options', width:'15%',cellEditableCondition: false,
  		                                	 
  			                                 },
  			                                 { name: 'edit', displayName: '', width:'10%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
  	    		                                 cellTemplate:'<i class="glyphicon glyphicon-ok-circle" ng-click="grid.appScope.deactiveUsers(row)"  title="suspend"></i> &nbsp;', 
  			                                 
  			                                 },
  			                                    ];
  		
  			 $scope.gridOptions1.onRegisterApi = function(gridApi){
  				 $scope.gridApi = gridApi;
  				 
  		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
  			          var grid = this.grid;
  			          $scope.gridOptions1.data = $filter('filter')($scope.activeList,{'name':grid.columns[0].filters[0].term,'email':grid.columns[1].filters[0].term,'businessName':grid.columns[2].filters[0].term,'businessAdd':grid.columns[3].filters[0].term,'options':grid.columns[4].filters[0].term},undefined);
  			        });
  		   		
  	  		};*/
  		
		 
		 $scope.pendingUser = function(){
			 $scope.doShow = 0;
			 $http.get('/getRegistrList')
				.success(function(data) {
				$scope.gridOptions.data = data;
				$scope.pendingList = data;
			});
		 }
		 
		 /*$scope.activeUsers = function(row){
			 console.log(row.entity);
			 $http.get('/getSetActiveUser/'+row.entity.id)
				.success(function(data) {
					 $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "user Active",
						});
				$scope.gridOptions.data = data;
				$scope.pendingUser();
			});
		 }*/
		 
		 /*$scope.deactiveUsers = function(row){
			 console.log(row.entity);
			 $http.get('/getSetdeActiveUser/'+row.entity.id)
				.success(function(data) {
					 $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "user Active",
						});
				$scope.gridOptions.data = data;
				$scope.pendingUser();
			});
		 }*/
		 
		 
		/* $scope.viewActive = function(){
			 $scope.doShow = 1;
			 $http.get('/getActiveRegistrList')
				.success(function(data) {
					console.log(data);
				$scope.gridOptions1.data = data;
				$scope.activeList = data;
			});
		 }*/
		
		 $scope.removeUser = function(row){
			 $http.get('/getRemoveUser/'+row.entity.id)
				.success(function(data) {
					 $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Remove User",
						});
				$scope.gridOptions.data = data;
				$scope.pendingUser();
			});
		 }
		 
		 $scope.sendDemoUrl = function(row){
			 $http.get('/getSendDemoLink/'+row.entity.id)
				.success(function(data) {
					 $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Demo Link send",
						});
				$scope.gridOptions.data = data;
				$scope.pendingUser();
			});
		 }
		 
		 $scope.register = {};
		 $scope.EditUser = function(row){
			 $('#editPopup').click();
			 console.log(row.entity)
			 $scope.register = row.entity;
			 $scope.register.oneLocation = row.entity.location;
			 $scope.register.businessAddress = row.entity.businessAdd;
		 }
		 
		 $scope.viewRegiInit = function(){
			 $scope.pendingUser();
		 }
		 
		 $scope.UpdateRegisterUser = function(){
			 console.log($scope.register);
			 $http.post("/updateRegisterUser",$scope.register).success(function(data){
         		$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Update successfully",
					});
         		$("#editPopups").modal('hide');
    		});
		 }
		 
	/*
		 $scope.lastDays = function(value){
			 $http.get('/getVisitorList/'+value)
				.success(function(data) {
					console.log(data[0].dates[0].items);
				$scope.gridOptions.data = data[0].dates[0].items;
				$scope.visitiorList = data[0].dates[0].items;
				$('#editPopup').click();
			});
		 }*/
		 $scope.goTocars = function() {
			 
			 $scope.doPublic = 0;
			 $http.get('/getAllCarsDetails')
		 		.success(function(data) {
		 			
		 			//for(var i=0;i<data.length;i++) {
		 			//	data[i].price = "$ "+data[i].price;
		 			//}
		 			
		 			//$scope.doPublic = 1;
		 			//$scope.vType = "cars";
		 			//$scope.type = "All";
		 			$scope.registrationObjList = data;
		 			$scope.gridOptions.data = data;
		 			//$scope.gridOptionscolumnDefs[8].displayName='Next Test Drive';
		 			//$scope.gridOptions.columnDefs[9].displayName='Views';
		 			
		 			
		 		});
		 }
		 

		 $scope.goToBoat = function() {
			 
			 $scope.doPublic = 0;
			 $http.get('/getAllBoat')
		 		.success(function(data) {
		 			$scope.registrationObjList = data;
		 			$scope.gridOptions.data = data;
		 			
		 		});
			 }
			 $scope.goToMotorcycles = function() {
				 
				 $scope.doPublic = 0;
				 $http.get('/getAllMotorcycles')
			 		.success(function(data) {
			 			$scope.registrationObjList = data;
			 			$scope.gridOptions.data = data;
			 			
			 		});
			 }
			 
			$scope.goToDesignerFurniture = function() {
				 
				 $scope.doPublic = 0;
				 $http.get('/getAllDesignerFurniture')
			 		.success(function(data) {
			 			$scope.registrationObjList = data;
			 			$scope.gridOptions.data = data;
			 			
			 		});
			 }
			
			$scope.goToRealState = function() {
				 
				 $scope.doPublic = 0;
				 $http.get('/getAllRealState')
			 		.success(function(data) {
			 			$scope.registrationObjList = data;
			 			$scope.gridOptions.data = data;
			 			
			 		});
			 }
				
			$scope.goToAirplanes = function() {
				 
				 $scope.doPublic = 0;
				 $http.get('/getAllAirplanes')
			 		.success(function(data) {
			 			$scope.registrationObjList = data;
			 			$scope.gridOptions.data = data;
			 			
			 		});
			 }
			
			$scope.goToServiceProvider = function() {
				 
				 $scope.doPublic = 0;
				 $http.get('/getAllServiceProvider')
			 		.success(function(data) {
			 			$scope.registrationObjList = data;
			 			$scope.gridOptions.data = data;
			 			
			 		});
			 }
			
			$scope.goToLuxuryProducts = function() {
				 
				 $scope.doPublic = 0;
				 $http.get('/getAllLuxuryProducts')
			 		.success(function(data) {
			 			$scope.registrationObjList = data;
			 			$scope.gridOptions.data = data;
			 			
			 		});
			 }
}]);
