angular.module('newApp')
.controller('TradeInCtrl', ['$scope','$http','$location','$filter', function ($scope,$http,$location,$filter) {
	
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
 		                                 { name: 'vin', displayName: 'Vin', width:'17%',cellEditableCondition: false,enableFiltering: false,
 		                                 },
 		                                 { name: 'model', displayName: 'Model',enableFiltering: false, width:'10%',cellEditableCondition: false,
 		                                 },
 		                                 { name: 'make', displayName: 'Make',enableFiltering: false, width:'14%',cellEditableCondition: false,
 		                                 },
 		                                 { name: 'stock', displayName: 'Stock',enableFiltering: false, width:'8%',cellEditableCondition: false,
		                                 },
 		                                 { name: 'name', displayName: 'Name',enableFiltering: false, width:'13%',cellEditableCondition: false,
 		                                 },
 		                                 { name: 'phone', displayName: 'Phone',enableFiltering: false, width:'11%',cellEditableCondition: false,
 		                                 },
 		                                 { name: 'email', displayName: 'Email',enableFiltering: false, width:'12%',cellEditableCondition: false,
 		                                 },
 		                                 { name: 'requestDate', displayName: 'Request Date',enableFiltering: false, width:'14%',cellEditableCondition: false,
 		                                 },
     		                                 ];
  
  
	  $http.get('/getAllTradeIn')
			.success(function(data) {
			$scope.gridOptions.data = data;
		});
  
		  $http.get('/tradeInMarkRead')
			.success(function(data) {
				$scope.$emit('getCountEvent', '123');
		});
  
}]);