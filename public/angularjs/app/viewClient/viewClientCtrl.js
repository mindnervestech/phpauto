angular.module('newApp')
.controller('ViewClientCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
	
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
		                                 { name: 'businessName', displayName: 'Business Name', width:'15%',cellEditableCondition: false,
		                                	 cellTemplate:'<a href="http://maps.google.com/?q={{row.entity.businessName}}" target="_blank">{{row.entity.businessName}}</a>',
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.sendDemoFlag == 0) {
			                                         return 'red';
			                                     }
			                                	} ,
		                                 },
		                                 { name: 'email', displayName: 'Website', width:'20%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.sendDemoFlag == 0) {
			                                         return 'red';
			                                     }
			                                	} ,
		                                 },
		                                 { name: 'clientsince', displayName: 'Client Since', width:'14%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.sendDemoFlag == 0) {
			                                         return 'red';
			                                     }
			                                	} ,
		                                 },
		                                 { name: 'activity', displayName: 'Last Time Logged in', width:'15%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.sendDemoFlag == 0) {
			                                         return 'red';
			                                     }
			                                	} ,
		                                 },
		                                 { name: 'options', displayName: 'Active Users', width:'14%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.sendDemoFlag == 0) {
			                                         return 'red';
			                                     }
			                                	} ,
		                                 },
		                                 /*{ name: 'businessAddress', displayName: 'Actions', width:'10%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.sendDemoFlag == 0) {
			                                         return 'red';
			                                     }
			                                	} ,
		                                 },*/
		                                 { name: 'edit', displayName: 'Actions', width:'10%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
    		                                 cellTemplate:'<i class="glyphicon glyphicon-ok-circle" ng-click="grid.appScope.sendDemoUrl(row)"  title="Send Demo Url"></i> &nbsp;&nbsp;&nbsp<i class="fa fa-trash" ng-click="grid.appScope.removeUser(row)"  title="Remove"></i> &nbsp;&nbsp;&nbsp<i class="glyphicon glyphicon-pencil" ng-click="grid.appScope.EditUser(row)"  title="Edit"></i> ', 
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
		         $scope.gridOptions.data = $filter('filter')($scope.pendingList,{'name':grid.columns[0].filters[0].term,'email':grid.columns[1].filters[0].term,'businessName':grid.columns[2].filters[0].term,'businessAddress':grid.columns[3].filters[0].term,'options':grid.columns[4].filters[0].term,'activity':grid.columns[5].filters[0].term,'clientsince':grid.columns[6].filters[0].term},undefined);
		        });
	   		
  		};
  		
  		
		 
		 $scope.pendingUser = function(){
			 $scope.doShow = 0;
			 $http.get('/getRegistrList')
				.success(function(data) {
				$scope.gridOptions.data = data;
				$scope.pendingList = data;
			});
		 }
		 
		
		/* $scope.Status = function(row){
			 $http.get('/getStatus/'+row.entity.id)
				.success(function(data) {
					 $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Status",
						});
				$scope.gridOptions.data = data;
				$scope.pendingUser();
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
					 if(row.entity.options =="Cars"){
						 $scope.goTocars();
					 }
					 if(row.entity.options =="Motorcycles"){
						 $scope.goToMotorcycles();
					 }
					 if(row.entity.options =="Boat"){
						 $scope.goToBoat();
					 }
					 if(row.entity.options =="DesignerFurniture"){
						 $scope.goToDesignerFurniture();
					 }
					 if(row.entity.options =="RealEstate"){
						 $scope.goToRealState();
					 }
					 if(row.entity.options =="Airplanes"){
						 $scope.goToAirplanes();
					 }
					 if(row.entity.options =="ServiceProvider"){
						 $scope.goToServiceProvider();
					 }
					 if(row.entity.options =="LuxuryProducts"){
						 $scope.goToLuxuryProducts();
					 }
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
					 if(row.entity.options =="Cars"){
						 $scope.goTocars();
					 }
					 if(row.entity.options =="Motorcycles"){
						 $scope.goToMotorcycles();
					 }
					 if(row.entity.options =="Boat"){
						 $scope.goToBoat();
					 }
					 if(row.entity.options =="DesignerFurniture"){
						 $scope.goToDesignerFurniture();
					 }
					 if(row.entity.options =="RealEstate"){
						 $scope.goToRealState();
					 }
					 if(row.entity.options =="Airplanes"){
						 $scope.goToAirplanes();
					 }
					 if(row.entity.options =="ServiceProvider"){
						 $scope.goToServiceProvider();
					 }
					 if(row.entity.options =="LuxuryProducts"){
						 $scope.goToLuxuryProducts();
					 }
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
		 
	
		 $scope.goTocars = function() {
			 
			 $scope.doPublic = 0;
			 $http.get('/getCarsDetails')
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
			 $http.get('/getBoat')
		 		.success(function(data) {
		 			$scope.registrationObjList = data;
		 			$scope.gridOptions.data = data;
		 			
		 		});
			 }
			 $scope.goToMotorcycles = function() {
				 
				 $scope.doPublic = 0;
				 $http.get('/getMotorcycles')
			 		.success(function(data) {
			 			$scope.registrationObjList = data;
			 			$scope.gridOptions.data = data;
			 			
			 		});
			 }
			 
			$scope.goToDesignerFurniture = function() {
				 
				 $scope.doPublic = 0;
				 $http.get('/getDesignerFurniture')
			 		.success(function(data) {
			 			$scope.registrationObjList = data;
			 			$scope.gridOptions.data = data;
			 			
			 		});
			 }
			
			$scope.goToRealState = function() {
				 
				 $scope.doPublic = 0;
				 $http.get('/getRealState')
			 		.success(function(data) {
			 			$scope.registrationObjList = data;
			 			$scope.gridOptions.data = data;
			 			
			 		});
			 }
				
			$scope.goToAirplanes = function() {
				 
				 $scope.doPublic = 0;
				 $http.get('/getAirplanes')
			 		.success(function(data) {
			 			$scope.registrationObjList = data;
			 			$scope.gridOptions.data = data;
			 			
			 		});
			 }
			
			$scope.goToServiceProvider = function() {
				 
				 $scope.doPublic = 0;
				 $http.get('/getServiceProvider')
			 		.success(function(data) {
			 			$scope.registrationObjList = data;
			 			$scope.gridOptions.data = data;
			 			
			 		});
			 }
			
			$scope.goToLuxuryProducts = function() {
				 
				 $scope.doPublic = 0;
				 $http.get('/getLuxuryProducts')
			 		.success(function(data) {
			 			$scope.registrationObjList = data;
			 			$scope.gridOptions.data = data;
			 			
			 		});
			 }
}]);
