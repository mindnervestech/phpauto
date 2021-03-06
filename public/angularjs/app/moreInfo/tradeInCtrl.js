angular.module('newApp')
.controller('TradeInCtrl', ['$scope','$http','$location','$filter','$interval','apiserviceMoreInfo', function ($scope,$http,$location,$filter,$interval,apiserviceMoreInfo) {
	$scope.leadList = [];
	
	apiserviceMoreInfo.getSelectedLeadType().then(function(response){
	
		console.log(response);
		angular.forEach(response, function(value, key) {
			if(value.id > 3){
				$scope.leadList.push(value); 
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
 		                                 { name: 'requestDate', displayName: 'Request Date',enableFiltering: false, width:'9%',cellEditableCondition: false,
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
 		                                { name: 'edit', displayName: '', width:'4%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
    		                                 cellTemplate:' <a ng-click="grid.appScope.getTradeData(row)" href="/showPdf/{{row.entity.id}}" target="_blank" style="margin-top:7px;margin-left:6px;" >View</a>',
    		                                 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		                                       if (row.entity.isRead === false) {
    		                                         return 'red';
    		                                     }
   		                                	} ,
		                                 
		                                 },
		                                 { name: 'salesRep', displayName: 'Sales Rep',enableFiltering: false, width:'9%',cellEditableCondition: false,
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
		          $scope.gridOptions.data = $filter('filter')($scope.tradeInList,{'vin':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'make':grid.columns[2].filters[0].term,'stock':grid.columns[3].filters[0].term,'name':grid.columns[4].filters[0].term},undefined);
		        });
	   		
		};
 		 
 		$scope.getTradeData = function(row) {
    		/*console.log(row.entity.id);
    		 $http.get('/getTradeInDataById/'+row.entity.id)
    	 		.success(function(data) {
    	 			console.log(data);
    	 			$scope.tradeInData = data;
    	 		});
    		$('#popupBtn').click();*/
 			
    	}	 
 		
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
 				
 				$scope.gridOptions.columnDefs.push({ name: 'isRead', displayName: 'Claim',enableFiltering: false, width:'6%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
                	 cellTemplate:'<div class="icheck-list"><input type="checkbox" ng-model="row.entity.isRead" ng-change="grid.appScope.setAsRead(row.entity.isRead,row.entity.id)" data-checkbox="icheckbox_flat-blue" title="Claim this lead" style="margin-left:18%;"></div>', 
                  	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                          if (row.entity.isRead === false) {
                            return 'red';
                        }
                  	} ,
                   });
 				
 				console.log($scope.gridOptions.data);
 		  }
    	
 		 apiserviceMoreInfo.getAllTradeIn().then(function(data){ 
	  
				console.log(data);
				$scope.editgirdData(data);
			$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'status');
			$scope.gridOptions.data = $scope.gridOptions.data.reverse();
			$scope.tradeInList = data;
			console.log(data);
			if(data.length > 0){
				$scope.userRole = data[0].userRole;
				if($scope.userRole == "Sales Person"){
					$scope.premiumFlagForSale = data[0].premiumFlagForSale
				}
			}
		});
  
	  $scope.getAllTradeInData = function() {
		  apiserviceMoreInfo.getAllTradeIn().then(function(data){
		  
			//$scope.gridOptions.data = data;
				$scope.editgirdData(data);
			$scope.tradeInList = data;
			console.log(data);
			console.log(data[0]);
			if(data.length > 0){
				$scope.userRole = data[0].userRole;
				if($scope.userRole == "Sales Person"){
					$scope.premiumFlagForSale = data[0].premiumFlagForSale
				}
			}
		});
	  }
	  
	  var promo =  $interval(function(){
		  apiserviceMoreInfo.getAllTradeIn().then(function(data){
			  
				//$scope.gridOptions.data = data;
					$scope.editgirdData(data);
				$scope.tradeInList = data;
			});
		},60000);
	  
	  $scope.setAsRead = function(flag,id) {
		  apiserviceMoreInfo.tradeInMarkRead(flag,id).then(function(data){
		  
				//$scope.gridOptions.data = data;
				apiserviceMoreInfo.getAllTradeIn().then(function(data){
					//$scope.gridOptions.data = data;
					$scope.editgirdData(data);
					$scope.tradeInList = data;
					if(data.length > 0){
						$scope.userRole = data[0].userRole;
						if($scope.userRole == "Sales Person"){
							$scope.premiumFlagForSale = data[0].premiumFlagForSale
						}
					}
			});
				$scope.$emit('getCountEvent', '123');
		});
		  
		  $scope.getAllTradeInData();
	  }
		 
	  $scope.testDrive = function() {
			$location.path('/scheduleTest');
		} 
	  $scope.requestMore = function() {
			$location.path('/requestMoreInfo');
		}  
	  $scope.goPremium = function() {
			$location.path('/premiumpage');
		}
	  
	  $scope.goContactUs = function() {
			$location.path('/contactUsInfo');
		}
	  $scope.otherLeads = function(leads) {
			$location.path('/otherLeads/'+leads.id);
		}
	  
}]);