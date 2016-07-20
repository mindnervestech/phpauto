angular.module('newApp')
.controller('otherLeadCtrl', ['$scope','$http','$location','$filter','$interval','$routeParams', function ($scope,$http,$location,$filter,$interval,$routeParams) {
	
	console.log("&&");
	console.log($routeParams.leadId);
	$scope.leadId = $routeParams.leadId;
	
	$scope.leadList = [];
	$http.get('/getSelectedLeadType').success(function(response) {
		console.log(response);
		
		angular.forEach(response, function(value, key) {
			if(value.id > 3){
				$scope.leadList.push(value); 
			}
			if(value.id == $scope.leadId){
				$scope.leadName = value.leadName;
			}
		});
		console.log($scope.leadList);
	
	});
	
  $scope.gridOptions = {
 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
 		    paginationPageSize: 150,
 		    enableFiltering: true,
 		    useExternalFiltering: true,
 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
 		 };
 		 $scope.gridOptions.enableHorizontalScrollbar = 2;
 		 $scope.gridOptions.enableVerticalScrollbar = 2;
 		 $scope.gridOptions.columnDefs = [
										{ name: 'title', displayName: 'Title', width:'12%',cellEditableCondition: false,
										 	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
										        if (row.entity.isRead === false) {
										          return 'red';
										        }
										 	} ,
										  },
										  { name: 'designer', displayName: 'Designer', width:'8%',cellEditableCondition: false,
										 	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
										         if (row.entity.isRead === false) {
										           return 'red';
										       }
										 	} ,
										  },
										  { name: 'year', displayName: 'Year', width:'7%',cellEditableCondition: false,
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
 		                                 { name: 'email', displayName: 'Email',enableFiltering: false, width:'10%',cellEditableCondition: false,
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
 		                                { name: 'price', displayName: 'Price',enableFiltering: false, width:'5%',cellEditableCondition: false,
 		                                	cellTemplate:'<div>{{row.entity.price | number:0}}</div>',
	   		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	    		                                       if (row.entity.isRead === false) {
	    		                                         return 'red';
	    		                                     }
	   		                                	} ,
	   		                             },
 		                                { name: 'salesRep', displayName: 'Sales Rep',enableFiltering: false, width:'10%',cellEditableCondition: false,
  		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
  		                                 },
  		                               { name: 'status', displayName: 'Status',enableFiltering: false, width:'8%',cellEditableCondition: false,
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
		          $scope.gridOptions.data = $filter('filter')($scope.requsetMoreList,{'vin':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'make':grid.columns[2].filters[0].term,'stock':grid.columns[3].filters[0].term,'name':grid.columns[4].filters[0].term},undefined);
		        });
	   		
  		};
 		 
	  $http.get('/getAllOtherLeadInfo/'+$scope.leadId)
			.success(function(data) {
				console.log(data);
				
			//$scope.gridOptions.data = data;
				$scope.editgirdData(data);
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
	  
	  $scope.editgirdData = function(data){
			  $scope.gridOptions.data = data;
			  $scope.gridMapObect = [];
				var findFlag = 0;
				angular.forEach($scope.gridOptions.data,function(value,key){
					if(findFlag == 0){
						angular.forEach(value.customData,function(value1,key1){
							$scope.gridMapObect.push({values: value1.value , key: value1.key});
							findFlag = 1;
						});
					}
				});
				angular.forEach($scope.gridOptions.data,function(value,key){
					angular.forEach($scope.gridMapObect,function(value1,key1){
						var name = value1.key;
						name = name.replace(" ","");
						value[name] = null;
						angular.forEach(value.customData,function(value2,key2){
							if(value1.key == value2.key){
								value[name] = value2.value;
							}
						});
					});
				});	
				
				
				angular.forEach($scope.gridMapObect,function(value,key){
					var name = value.key;
					name = name.replace(" ","");
					console.log(name);
					$scope.gridOptions.columnDefs.push({ name: name, displayName: name, width:'10%',cellEditableCondition: false,
		              	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		              		if (row.entity.isRead === false) {
	                            return 'red';
	                        }
		              	} ,
		               });
				});
				
				$scope.gridOptions.columnDefs.push({ name: 'isRead', displayName: 'Claim',enableFiltering: false, width:'7%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
                	 cellTemplate:'<div class="icheck-list"><input type="checkbox" ng-model="row.entity.isRead" ng-change="grid.appScope.setAsRead(row.entity.isRead,row.entity.id)" data-checkbox="icheckbox_flat-blue" title="Claim this lead" style="margin-left:18%;"></div>', 
                  	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                          if (row.entity.isRead === false) {
                            return 'red';
                        }
                  	} ,
                   });
				
				console.log($scope.gridOptions.data);
		  }
	  
	  $scope.getAllRequestInfo = function() {
		  $http.get('/getAllOtherLeadInfo/'+$scope.leadId)
			.success(function(data) {
			//$scope.gridOptions.data = data;
				$scope.editgirdData(data);
			$scope.requsetMoreList = data;
		});
	  
	  }
	  
	  var promo =  $interval(function(){
			  $http.get('/getAllOtherLeadInfo/'+$scope.leadId)
				.success(function(data) {
				//$scope.gridOptions.data = data;
					$scope.editgirdData(data);
				$scope.requsetMoreList = data;
			});
	  },60000);
	  
	  $scope.premiumFlagForSale = 0;
  $scope.setAsRead = function(flag,id) {
	  
	  $http.get('/requestInfoMarkRead/'+flag+'/'+id)
		.success(function(data) {
			//$scope.gridOptions.data = data;
			$http.get('/getAllOtherLeadInfo/'+$scope.leadId)
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
	
	$scope.goContactUs = function() {
		$location.path('/contactUsInfo');
	}
	$scope.otherLeads = function(leads) {
		console.log(leads.id);
		$location.path('/otherLeads/'+leads.id);
	}
	$scope.requestMore = function() {
		$location.path('/requestMoreInfo');
	} 
  
}]);