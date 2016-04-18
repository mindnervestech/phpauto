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
    		                                 cellTemplate:'<i class="glyphicon glyphicon-ok-circle" ng-click="grid.appScope.activeUsers(row)"  title="Active"></i> &nbsp;', 
		                                 
		                                 },
		                                    ];
	
		 $scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		          $scope.gridOptions.data = $filter('filter')($scope.visitiorList,{'geolocation':grid.columns[0].filters[0].term,'ip_address':grid.columns[1].filters[0].term,'referrer_domain':grid.columns[2].filters[0].term,'total_visits':grid.columns[3].filters[0].term,'web_browser':grid.columns[4].filters[0].term},undefined);
		        });
	   		
  		};
  		
  		
		 
		 $scope.pendingUser = function(){
			 $http.get('/getRegistrList')
				.success(function(data) {
					console.log(data);
					console.log("///////////kkkkkkkkkkkkkkk");
				$scope.gridOptions.data = data;
				//$('#sliderBtn').click();
			});
		 }
		 
		 $scope.activeUsers = function(){
			 console.log("jjjjjjj");
		 }
		
		 $scope.viewRegiInit = function(){
			 $scope.pendingUser();
		 }
		 
	/*
		 $scope.lastDays = function(value){
			 $http.get('/getVisitorList/'+value)
				.success(function(data) {
					console.log(data[0].dates[0].items);
				$scope.gridOptions.data = data[0].dates[0].items;
				$scope.visitiorList = data[0].dates[0].items;
				$('#sliderBtn').click();
			});
		 }*/
	
	
	
}]);
