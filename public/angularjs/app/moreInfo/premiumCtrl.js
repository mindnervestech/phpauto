angular.module('newApp')
.controller('premiumCtrl', ['$scope','$http','$location','$filter','$interval', function ($scope,$http,$location,$filter,$interval) {
	
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
 		                                 { name: 'vin', displayName: 'Vin', width:'11%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
 		                                 },
 		                                 { name: 'model', displayName: 'Model', width:'8%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
 		                                 },
 		                                 { name: 'make', displayName: 'Make', width:'8%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
 		                                 },
 		                                 { name: 'stock', displayName: 'Stock', width:'6%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
		                                 },
 		                                 { name: 'name', displayName: 'Name', width:'9%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	  		                                       if (row.entity.isRead === false) {
	  		                                         return 'red';
	  		                                     }
	 		                                	} ,
 		                                 },
 		                                 { name: 'phone', displayName: 'Phone',enableFiltering: false, width:'8%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
 		                                 },
 		                                 { name: 'email', displayName: 'Email',enableFiltering: false, width:'9%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
 		                                 },
 		                                 { name: 'requestDate', displayName: 'Request Date',enableFiltering: false, width:'11%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
 		                                 },
 		                                { name: 'edit', displayName: '', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
    		                                 cellTemplate:' <a ng-click="grid.appScope.getTradeData(row)" href="/showPdf/{{row.entity.id}}" target="_blank" style="margin-top:7px;margin-left:6px;" >View</a>',
    		                                 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		                                       if (row.entity.isRead === false) {
    		                                         return 'red';
    		                                     }
   		                                	} ,
		                                 
		                                 },
		                                 { name: 'salesRep', displayName: 'Sales Rep',enableFiltering: false, width:'11%',cellEditableCondition: false,
	   		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	    		                                       if (row.entity.isRead === false) {
	    		                                         return 'red';
	    		                                     }
	   		                                	} ,
	   		                                 },
	   		                               { name: 'status', displayName: 'Status',enableFiltering: false, width:'9%',cellEditableCondition: false,
	   		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	    		                                       if (row.entity.isRead === false) {
	    		                                         return 'red';
	    		                                     }
	   		                                	} ,
	   		                                 },
		                                 { name: 'btn', displayName: 'Assign',enableFiltering: false, width:'6%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
 		                                	 cellTemplate:'<button type="button" ng-click="grid.appScope.assignCanceledLead(row.entity)" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:3%;">ASSIGN</button>', 
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                /*{ name: 'btnSold', displayName: '',enableFiltering: false, width:'24%',cellEditableCondition: false,
		 		                                	cellTemplate:'<button type="button" ng-click="grid.appScope.assignCanceledLead(row.entity)" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:3%;">ASSIGN</button><button type="button" ng-click="grid.appScope.deleteForeverLead(row.entity)" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:0%;">DELETE</button><button type="button" ng-click="grid.appScope.restoreLead(row.entity)" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:0%;">RESTORE</button><button type="button" ng-click="grid.appScope.addNoteToRequestUser(row.entity,\'cansal\')" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:0%;">HISTORY</button>',
		 		                                	 
		 		                                 },*/
 		                                 
     		                                 ];
  
  
 		$scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		          $scope.gridOptions.data = $filter('filter')($scope.tradeInList,{'vin':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'make':grid.columns[2].filters[0].term,'stock':grid.columns[3].filters[0].term,'name':grid.columns[4].filters[0].term},undefined);
		        });
	   		
		};
 		 
		$http.get('/getSalesUserValue')
		.success(function(data){
			console.log(data);
			$scope.salesPersonPerf = data;
			
		});
		
		
		 $scope.changeAssignedUser = function() {
			 console.log($scope.leadlId);
			 console.log($scope.leadType);
			 console.log($scope.changedUser);
	        	$http.get('/changeAssignedUser/'+$scope.leadlId+'/'+$scope.changedUser+'/'+$scope.leadType)
				.success(function(data) {
					$('#assignUserModal').modal('hide');
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "User assigned successfully",
					});
					 $scope.getAllPremiumData();
				});
	        	
	        }
		
 		/*$scope.getTradeData = function(row) {
    		console.log(row.entity.id);
    		 $http.get('/getTradeInDataById/'+row.entity.id)
    	 		.success(function(data) {
    	 			console.log(data);
    	 			$scope.tradeInData = data;
    	 		});
    		$('#popupBtn').click();
 			
    	}	 */
    	
 		 
	  $http.get('/getAllPremiumIn')
			.success(function(data) {
			$scope.gridOptions.data = data;
			$scope.tradeInList = data;
			$scope.userRole = data[0].userRole;
		});
  
	  $scope.getAllPremiumData = function() {
		  $http.get('/getAllPremiumIn')
			.success(function(data) {
			$scope.gridOptions.data = data;
			//$scope.tradeInList = data;
		});
	  }
	  
	  var promo =  $interval(function(){
			  $http.get('/getAllPremiumIn')
				.success(function(data) {
				$scope.gridOptions.data = data;
				$scope.tradeInList = data;
			});
		},60000);
	  
	 /* $scope.setAsRead = function(flag,id) {
		  
		  $http.get('/tradeInMarkRead/'+flag+'/'+id)
			.success(function(data) {
				$scope.gridOptions.data = data;
				$scope.$emit('getCountEvent', '123');
		});
		  
		 // $scope.getAllPremiumData();
	  }*/
	  
	  
	  $scope.assignCanceledLead = function(entity) {
		  console.log(entity);
      	$scope.leadlId = entity.id;
      	$scope.leadType = entity.leadType;
      	$scope.changedUser = "";
      	$('#btnAssignUser').click();
      }
      
      /*$scope.changeAssignedUser = function() {
      	$http.get('/changeAssignedUser/'+$scope.cancelId+'/'+$scope.changedUser+'/'+$scope.leadType)
			.success(function(data) {
				$('#closeChangeUser').click();
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User assigned successfully",
				});
				$scope.getAllCanceledLeads();
			});
      	
      }*/
	  
	  
		 
	  $scope.testDrive = function() {
			$location.path('/scheduleTest');
		} 
	  $scope.requestMore = function() {
			$location.path('/requestMoreInfo');
		}  
	  $scope.tradeIn = function() {
			$location.path('/tradeIn');
		}
	  
}]);