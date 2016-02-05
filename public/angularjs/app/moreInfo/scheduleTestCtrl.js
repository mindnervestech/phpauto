angular.module('newApp')
.controller('ScheduleTestCtrl', ['$scope','$http','$location','$filter','$interval', function ($scope,$http,$location,$filter,$interval) {
	
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
 		                                 { name: 'vin', displayName: 'Vin', width:'7%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
 		                                 },
 		                                 { name: 'model', displayName: 'Model', width:'7%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
 		                                 },
 		                                 { name: 'make', displayName: 'Make', width:'7%',cellEditableCondition: false,
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
 		                                 { name: 'name', displayName: 'Name', width:'7%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	  		                                       if (row.entity.isRead === false) {
	  		                                         return 'red';
	  		                                     }
	 		                                	} ,
 		                                 },
 		                                 { name: 'phone', displayName: 'Phone',enableFiltering: false, width:'7%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
 		                                 },
 		                                 { name: 'email', displayName: 'Email',enableFiltering: false, width:'7%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
 		                                 },
 		                                 { name: 'bestDay', displayName: 'Best Day',enableFiltering: false, width:'9%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
		                                 },
		                                 { name: 'bestTime', displayName: 'Best Time',enableFiltering: false, width:'8%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	  		                                       if (row.entity.isRead === false) {
	  		                                         return 'red';
	  		                                     }
	 		                                	} ,
 		                                 },
 		                                 { name: 'requestDate', displayName: 'Request Date',enableFiltering: false, width:'10%',cellEditableCondition: false,
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
 		                                { name: 'isRead', displayName: 'Claim',enableFiltering: false, width:'6%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
 		                                	 cellTemplate:'<div class="icheck-list"><input type="checkbox" ng-model="row.entity.isRead" ng-change="grid.appScope.setAsRead(row.entity.isRead,row.entity.id)" data-checkbox="icheckbox_flat-blue" title="Claim this lead" style="float:left;margin-left:15%;"></div>', 
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
		          $scope.gridOptions.data = $filter('filter')($scope.scheduleList,{'vin':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'make':grid.columns[2].filters[0].term,'stock':grid.columns[3].filters[0].term,'name':grid.columns[4].filters[0].term},undefined);
		        });
	   		
 		};
 		 
 		$('.datepicker').datepicker({
 		});
 		$("#cnfDate").datepicker().datepicker("setDate", new Date());
 		$('#timepicker1').timepicker();
 		$scope.userRole = null;
	  $http.get('/getAllScheduleTest')
			.success(function(data) {
			$scope.gridOptions.data = data;
			$scope.scheduleList = data;
			console.log(data);
			if(data.length > 0){
				$scope.userRole = data[0].userRole;
			}
			$('#sliderBtn').click();
		});
	  
	  $scope.getAllScheduleTestData = function() {
		  $http.get('/getAllScheduleTest')
			.success(function(data) {
			$scope.gridOptions.data = data;
			$scope.scheduleList = data;
			
			$('#sliderBtn').click();
		});
	  }
  
  		var promo =  $interval(function(){
  			 $http.get('/getAllScheduleTest')
  			.success(function(data) {
	  			$scope.gridOptions.data = data;
	  			$scope.scheduleList = data;
	  			$('#sliderBtn').click();
  			});
  		},60000);
  
  $scope.setAsRead = function(flag,id) {
	  
	  $http.get('/scheduleTestMarkRead/'+flag+'/'+id)
		.success(function(data) {
			//$scope.gridOptions.data = data;
			$http.get('/getAllScheduleTest')
			.success(function(data) {
			$scope.gridOptions.data = data;
			$scope.scheduleList = data;
			if(data.length > 0){
				$scope.userRole = data[0].userRole;
			}
			$('#sliderBtn').click();
		});
			$scope.$emit('getCountEvent', '123');
	});
	  $scope.getAllScheduleTestData();
  }
 
  $scope.scheduleTestData = {};
  
  $scope.confirmDateTime = function(entity) {
	  $scope.scheduleTestData.id = entity.id;
	  $scope.scheduleTestData.email = entity.email;
	  $scope.scheduleTestData.confirmDate = entity.confirmDate;
	  $scope.scheduleTestData.confirmTime = entity.confirmTime;
  }
  
  $scope.saveConfirmData = function() {
	  $scope.scheduleTestData.confirmDate = $("#cnfDate").val();
	  $scope.scheduleTestData.confirmTime = $("#timepicker1").val();
	  $http.post('/saveConfirmData',$scope.scheduleTestData)
 		.success(function(data) {
 			console.log('success');
 			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
 			$('#modalClose').click();
 		});
  }
  
  $scope.requestMore = function() {
		$location.path('/requestMoreInfo');
	}  

	$scope.tradeIn = function() {
		$location.path('/tradeIn');
	}
	
	$scope.goPremium = function() {
		$location.path('/premiumpage');
	}
  
}]);