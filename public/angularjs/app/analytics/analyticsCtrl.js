angular.module('newApp')
.controller('VisitorsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	
	
	 $scope.gridOptions.enableHorizontalScrollbar = 0;
		 $scope.gridOptions.enableVerticalScrollbar = 0;
		 $scope.gridOptions.columnDefs = [
		                                 { name: 'geolocation', displayName: 'Location', width:'15%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'ip_address', displayName: 'IpAddress', width:'15%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'referrer_domain', displayName: 'Referrer_Domain', width:'15%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'total_visits', displayName: 'Total_Visits', width:'12%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
	                                 },
		                                 { name: 'web_browser', displayName: 'Web_Browser', width:'15%',cellEditableCondition: false,
	                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
		                                 },
		                                 { name: 'time_pretty', displayName: 'Time_Pretty', width:'15%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	  		                                       if (row.entity.isRead === false) {
	  		                                         return 'red';
	  		                                     }
	 		                                	} ,
			                                 },
			                                { name: 'screen_resolution', displayName: 'Screen_Resolution', width:'15%',cellEditableCondition: false,
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
		          $scope.gridOptions.data = $filter('filter')($scope.requsetMoreList,{'geolocation':grid.columns[0].filters[0].term,'ip_address':grid.columns[1].filters[0].term,'referrer_domain':grid.columns[2].filters[0].term,'total_visits':grid.columns[3].filters[0].term,'web_browser':grid.columns[4].filters[0].term},undefined);
		        });
	   		
  		};
  		
  		
		 $http.get('/getVisitorList')
			.success(function(data) {
				console.log(data[0].dates[0].items);
			$scope.gridOptions.data = data[0].dates[0].items;
			$scope.visitiorList = data[0].dates[0].items;
			$('#sliderBtn').click();
		});
	
	
	$scope.goToActions = function() {
		$location.path('/actionsAnalytics');
	}
	
	$scope.goToBasics = function() {
		$location.path('/basicsAnalytics');
	}
	
	$scope.goToSearches = function() {
		$location.path('/searchesAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
}]);


angular.module('newApp')
.controller('ActionsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	
	
	 $scope.gridOptions.enableHorizontalScrollbar = 0;
		 $scope.gridOptions.enableVerticalScrollbar = 0;
		 $scope.gridOptions.columnDefs = [
		                                 { name: 'geolocation', displayName: 'Location', width:'15%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'ip_address', displayName: 'IpAddress', width:'15%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'referrer_domain', displayName: 'Referrer_Domain', width:'15%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'total_visits', displayName: 'Total_Visits', width:'12%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
	                                 },
		                                 { name: 'web_browser', displayName: 'Web_Browser', width:'15%',cellEditableCondition: false,
	                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
 		                                       if (row.entity.isRead === false) {
 		                                         return 'red';
 		                                     }
		                                	} ,
		                                 },
		                                 { name: 'time_pretty', displayName: 'Time_Pretty', width:'15%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	  		                                       if (row.entity.isRead === false) {
	  		                                         return 'red';
	  		                                     }
	 		                                	} ,
			                                 },
			                                { name: 'screen_resolution', displayName: 'Screen_Resolution', width:'15%',cellEditableCondition: false,
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
		          $scope.gridOptions.data = $filter('filter')($scope.requsetMoreList,{'geolocation':grid.columns[0].filters[0].term,'ip_address':grid.columns[1].filters[0].term,'referrer_domain':grid.columns[2].filters[0].term,'total_visits':grid.columns[3].filters[0].term,'web_browser':grid.columns[4].filters[0].term},undefined);
		        });
	   		
 		};
 		
 		
		 $http.get('/getActionList')
			.success(function(data) {
				console.log(data);
			$scope.gridOptions.data = data[0].dates[0].items;
			$scope.visitiorList = data[0].dates[0].items;
			$('#sliderBtn').click();
		});	
	
	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	$scope.goToBasics = function() {
		$location.path('/basicsAnalytics');
	}
	
	$scope.goToSearches = function() {
		$location.path('/searchesAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
}]);

angular.module('newApp')
.controller('BasicsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	$scope.goToActions = function() {
		$location.path('/actionsAnalytics');
	}
	
	$scope.goToSearches = function() {
		$location.path('/searchesAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
}]);


angular.module('newApp')
.controller('SearchesCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	$scope.goToActions = function() {
		$location.path('/actionsAnalytics');
	}
	
	$scope.goToBasics = function() {
		$location.path('/basicsAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
}]);


angular.module('newApp')
.controller('RefferersCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	$scope.goToActions = function() {
		$location.path('/actionsAnalytics');
	}
	
	$scope.goToBasics = function() {
		$location.path('/basicsAnalytics');
	}
	
	$scope.goToSearches = function() {
		$location.path('/searchesAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
}]);


angular.module('newApp')
.controller('ContentCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	$scope.goToActions = function() {
		$location.path('/actionsAnalytics');
	}
	
	$scope.goToBasics = function() {
		$location.path('/basicsAnalytics');
	}
	
	$scope.goToSearches = function() {
		$location.path('/searchesAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	
	
}]);