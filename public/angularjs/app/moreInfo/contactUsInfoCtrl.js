angular.module('newApp')
.controller('contactUsInfoCtrl', ['$scope','$http','$location','$filter','$interval', function ($scope,$http,$location,$filter,$interval) {
	
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
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                 { name: 'phone', displayName: 'Phone',enableFiltering: false, width:'12%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                 { name: 'email', displayName: 'Email',enableFiltering: false, width:'17%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                 { name: 'requestDate', displayName: 'ContactUs Date',enableFiltering: false, width:'15%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                
  		                               { name: 'status', displayName: 'Status',enableFiltering: false, width:'12%',cellEditableCondition: false,
  		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
  		                                 },
  		                               { name: 'message', displayName: 'Message',enableFiltering: false, width:'14%',cellEditableCondition: false,
  		                                	cellTemplate:'<div><a ng-click="grid.appScope.showMessage(row.entity)"  style="color: black; text-overflow:ellipsis;" title="{{row.entity.message}}" >{{row.entity.message}}</a> </div>',
  		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		                                       if (row.entity.isRead === false) {
    		                                         return 'red';
    		                                     }
   		                                	} ,
   		                                 },
 		                                 { name: 'isRead', displayName: 'Claim',enableFiltering: false, width:'10%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
 		                                	 cellTemplate:'<div class="icheck-list"><input type="checkbox" ng-model="row.entity.isRead" ng-change="grid.appScope.setAsRead(row.entity.isRead,row.entity.id)" data-checkbox="icheckbox_flat-blue" title="Claim this lead" style="margin-left:18%;"></div>', 
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
     		                                 ];
  
 		$scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		          $scope.gridOptions.data = $filter('filter')($scope.requsetMoreList,{'name':grid.columns[4].filters[0].term},undefined);
		        });
	   		
  		};
 		 
	  $http.get('/getAllContactInfo')
			.success(function(data) {
				console.log(data);
				
			$scope.gridOptions.data = data;
			$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'status');
			$scope.gridOptions.data = $scope.gridOptions.data.reverse();
			
			$scope.requsetMoreList = data;
			if(data.length > 0){
				$scope.userRole = data[0].userRole;
				if($scope.userRole == "Sales Person"){
					$scope.premiumFlagForSale = data[0].premiumFlagForSale
				}
			}
		});
	  
	  
	  
	  $scope.showMessage = function(entity) {
	  
		  console.log(entity);
		 $scope.contactMessage=entity.message;
		  $('#btncontactMessageModal').click();
	  }
	  
	  
	  $scope.getAllRequestInfo = function() {
		  $http.get('/getAllContactInfo')
			.success(function(data) {
			$scope.gridOptions.data = data;
			$scope.requsetMoreList = data;
		});
	  
	  }
	  
	  var promo =  $interval(function(){
			  $http.get('/getAllContactInfo')
				.success(function(data) {
				$scope.gridOptions.data = data;
				$scope.requsetMoreList = data;
			});
	  },60000);
	  
	  $scope.premiumFlagForSale = 0;
  $scope.setAsRead = function(flag,id) {
	  
	  $http.get('/requestInfoMarkRead/'+flag+'/'+id)
		.success(function(data) {
			//$scope.gridOptions.data = data;
			$http.get('/getAllContactInfo')
			.success(function(data) {
				console.log(data);
			$scope.gridOptions.data = data;
			$scope.requsetMoreList = data;
			if(data.length > 0){
				$scope.userRole = data[0].userRole;
				if($scope.userRole == "Sales Person"){
					$scope.premiumFlagForSale = data[0].premiumFlagForSale
				}
			}
		});
			$scope.$emit('getCountEvent', '123');
	});
	  
	  $scope.getAllRequestInfo();
  }
  
	$scope.testDrive = function() {
		$location.path('/scheduleTest');
	}  
  
	$scope.tradeIn = function() {
		$location.path('/tradeIn');
	}
	
	$scope.goPremium = function() {
		$location.path('/premiumpage');
	}
	
	
	
  
}]);