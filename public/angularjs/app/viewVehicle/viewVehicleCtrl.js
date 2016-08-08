angular.module('newApp')
.controller('ViewVehiclesCtrl', ['$scope','$http','$location','$filter','apiserviceViewVehicle', function ($scope,$http,$location,$filter,apiserviceViewVehicle) {
	$scope.tempDate = new Date().getTime();
	$scope.type = "All";
	$scope.vType;
	$scope.doPublic = 0;
     $scope.gridOptions = {
    		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    		    paginationPageSize: 150,
    		    enableFiltering: true,
    		    enableCellEditOnFocus: true,
    		    useExternalFiltering: true,
    		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
    		 };
    		 $scope.gridOptions.enableHorizontalScrollbar = 0;
    		 $scope.gridOptions.enableVerticalScrollbar = 2;
    		 $scope.gridOptions.columnDefs = [
    		                                 { name: 'title', displayName: 'Title',enableColumnMenu: false, width:'15%',cellEditableCondition: true,
    		                                	 cellTemplate: '<div> <a ng-mouseenter="grid.appScope.mouse(row)" ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
    		                                 },
    		                                 { name: 'last4Vin', displayName: 'VIN',enableColumnMenu: false, width:'5%',cellEditableCondition: true,
    		                                	 cellTemplate: '<div> <label  style="line-height: 200%;" data-content="{{row.entity.last4Vin}}" >{{row.entity.last4Vin}}</label> </div>',
    		                                 },
    		                                 { name: 'stock', displayName: 'Stock',enableColumnMenu: false, width:'6%',
    		                                 },
    		                                 { name: 'bodyStyle', displayName: 'Body Style',enableColumnMenu: false,enableFiltering: false, width:'9%',cellEditableCondition: false,
    		                                	 cellTemplate:'<select style="width:100%;" ng-model="row.entity.bodyStyle" ng-change="grid.appScope.updateVehicleBody(row)"><option value="">Select</option><option value="Sedan">Sedan</option><option value="Coupe">Coupe</option><option value="SUV">SUV</option><option value="Van">Van</option><option value="Minivan">Minivan</option></select>',
    		                                 },
    		                                 { name: 'mileage', displayName: 'Mileage',enableColumnMenu: false,enableFiltering: false, width:'8%',cellEditableCondition: true,
    		                                 },
    		                                 { name: 'city_mileage', displayName: 'City MPG',enableFiltering: false, width:'8%',enableColumnMenu: false,cellEditableCondition: true,
    		                                 },
    		                                 { name: 'highway_mileage', displayName: 'HWY MPG',enableFiltering: false, enableColumnMenu: false,width:'8%',cellEditableCondition: true,
    		                                 },
    		                                 { name: 'price', displayName: 'Price',enableFiltering: false,enableColumnMenu: false, width:'8%',
    		                                 },
    		                                 { name: 'vehicleCnt', displayName: 'Photos',enableFiltering: false,type:'number',enableColumnMenu: false, width:'4%',cellEditableCondition: false,
    		                                	 cellTemplate: '<div> <a ng-click="grid.appScope.getImages(row)" style="line-height: 200%;" title="" data-content="{{row.entity.vehicleCnt}}">{{row.entity.vehicleCnt}}</a></div>',
    		                                 },
    		                                 { name: 'testDrive', displayName:'Next Test Drive' ,enableFiltering: false,enableColumnMenu: false, width:'10%',cellEditableCondition: false,
    		                                 },
    		                                 { name: 'pageViewCount', displayName: 'Views',enableFiltering: false,type:'number',enableColumnMenu: false, width:'7%',cellEditableCondition: false,
    		                                	 cellTemplate:'<span style="margin-left:10px;">{{row.entity.pageViewCount}}</span><i ng-if="row.entity.sold" title="Vehicle History" style="margin-left:10px;"class="glyphicon glyphicon-eye-open" ng-click="grid.appScope.historyVehicle(row)"></i>',
    		                                 },
    		                                 { name: 'Hide', displayName: 'Hide', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
        		                                 cellTemplate:'<input type="checkbox" name="vehicle" ng-click="grid.appScope.hideVehicle(row)" autocomplete="off">', 
    		                                 
    		                                 },
    		                                 { name: 'edit', displayName: '', width:'12%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
    		                                	 cellTemplate:'<i class="glyphicon glyphicon-picture" ng-click="grid.appScope.editPhoto(row)" ng-if="row.entity.userRole == \'Photographer\'" style="margin-top:7px;margin-left:8px;" title="Edit"></i> &nbsp; <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editVehicle(row)" ng-if="row.entity.userRole != \'Photographer\'" style="margin-top:7px;margin-left:8px;" title="Edit"></i> &nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-ok-circle" ng-click="grid.appScope.updateVehicleStatus(row)" ng-if="row.entity.userRole != \'Photographer\'"  title="Add to Current Inventory"></i> &nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteVehicle(row)" ng-if="row.entity.userRole != \'Photographer\'"></i>&nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-stats" ng-if="row.entity.userRole != \'Photographer\'" ng-click="grid.appScope.showSessionData(row)" title="sessions"></i>&nbsp;', 
    		                                 
    		                                 },
        		                                
        		                                 ];  
     
    		 
    		 
    		 $scope.editPhoto = function(row){
    			 console.log(row);
    			 $location.path('/editVehicle/'+row.entity.id+"/"+true+"/"+row.entity.vin);
    			 
    		 }
    		 
    		 $scope.gridOptions.onRegisterApi = function(gridApi){
    			 $scope.gridApi = gridApi;
    			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
    			 $scope.rowData = rowEntity;
    			 $scope.$apply();
    				 var str = $scope.rowData.price.split(" ");
    				 $scope.rowData.price = str[1];
    				 apiserviceViewVehicle.updateVehicle($scope.rowData).then(function(data){
    			 
    				 	$scope.rowData.price = "$ "+$scope.rowData.price;
    				});
    			 });
    			 
    			 
    			 $scope.gridApi.core.on.filterChanged( $scope, function() {
    		          var grid = this.grid;
    		          $scope.gridOptions.data = $filter('filter')($scope.vehiClesList,{'make':grid.columns[0].filters[0].term,'last4Vin':grid.columns[1].filters[0].term,'stock':grid.columns[2].filters[0].term},undefined);
    		        });
    			 
    			 };
    			 
    			 
    			 $scope.gridOptions1 = {
    		    		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    		    		    paginationPageSize: 150,
    		    		    enableFiltering: true,
    		    		    enableCellEditOnFocus: true,
    		    		    useExternalFiltering: true,
    		    		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
    		    		 };
    		    		 $scope.gridOptions1.enableHorizontalScrollbar = 0;
    		    		 $scope.gridOptions1.enableVerticalScrollbar = 2;
    		    		 $scope.gridOptions1.columnDefs = [
    		    		                                 { name: 'title', displayName: 'Title', width:'15%',enableColumnMenu: false,cellEditableCondition: true,
    		    		                                	 cellTemplate: '<div> <a ng-mouseenter="grid.appScope.mouse(row)" ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
    		    		                                 },
    		    		                                 { name: 'last4Vin', displayName: 'vin',enableColumnMenu: false, width:'5%',cellEditableCondition: true,
    		    		                                	 cellTemplate: '<div> <label  style="line-height: 200%;" data-content="{{row.entity.last4Vin}}" >{{row.entity.last4Vin}}</label> </div>',
    		    		                                 },
    		    		                                 { name: 'stock', displayName: 'Stock',enableColumnMenu: false, width:'6%',
    		    		                                 },
    		    		                                 { name: 'bodyStyle', displayName: 'Body Style',enableColumnMenu: false,enableFiltering: false, width:'9%',cellEditableCondition: false,
    		    		                                	 cellTemplate:'<select style="width:100%;" ng-model="row.entity.bodyStyle" ng-change="grid.appScope.updateVehicleBody(row)"><option value="">Select</option><option value="Sedan">Sedan</option><option value="Coupe">Coupe</option><option value="SUV">SUV</option><option value="Van">Van</option><option value="Minivan">Minivan</option></select>',
    		    		                                 },
    		    		                                 { name: 'mileage', displayName: 'Mileage',enableFiltering: false,enableColumnMenu: false, width:'8%',cellEditableCondition: true,
    		    		                                 },
    		    		                                 { name: 'city_mileage', displayName: 'City MPG',enableFiltering: false,enableColumnMenu: false, width:'8%',cellEditableCondition: true,
    		    		                                 },
    		    		                                 { name: 'highway_mileage', displayName: 'HWY MPG',enableFiltering: false,enableColumnMenu: false, width:'8%',cellEditableCondition: true,
    		    		                                 },
    		    		                                 { name: 'price', displayName: 'Price',enableFiltering: false,enableColumnMenu: false, width:'8%',
    		    		                                 },
    		    		                                 { name: 'vehicleCnt', displayName: 'Photos',enableFiltering: false, width:'7%',enableColumnMenu: false,cellEditableCondition: false,
    		    		                                	 cellTemplate: '<div> <a ng-click="grid.appScope.getImages(row)" style="line-height: 200%;" title="" data-content="{{row.entity.vehicleCnt}}">{{row.entity.vehicleCnt}}</a></div>',
    		    		                                 },
    		    		                                 { name: 'testDrive', displayName:'Next Test Drive' ,enableFiltering: false,enableColumnMenu: false, width:'10%',cellEditableCondition: false,
    		    		                                 },
    		    		                                 { name: 'pageViewCount', displayName: 'Views',enableFiltering: false, width:'9%',enableColumnMenu: false,cellEditableCondition: false,
    		    		                                	 cellTemplate:'<span style="margin-left:10px;">{{row.entity.pageViewCount}}</span><i ng-if="row.entity.sold" title="Vehicle History" style="margin-left:10px;"class="glyphicon glyphicon-eye-open" ng-click="grid.appScope.historyVehicle(row)"></i>',
    		    		                                 },
    		    		                                 { name: 'edit', displayName: '', width:'12%',enableFiltering: false, cellEditableCondition: false,enableColumnMenu: false, enableSorting: false, enableColumnMenu: false,
    		        		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editVehicle(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i> &nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-ok-circle" ng-click="grid.appScope.updateVehicleStatusPublic(row)"  title="publishe"></i> &nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteVehicle(row)"></i>', 
    		    		                                 
    		    		                                 },
    		        		                                
    		        		                                 ];  
    		     
    		    		 $scope.gridOptions1.onRegisterApi = function(gridApi){
    		    			 $scope.gridApi = gridApi;
    		    			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
    		    			 $scope.rowData = rowEntity;
    		    			 $scope.$apply();
    		    				 var str = $scope.rowData.price.split(" ");
    		    				 $scope.rowData.price = str[1];
    		    				 apiserviceViewVehicle.updateVehicle($scope.rowData).then(function(data){
    		    			 
    		    				 	$scope.rowData.price = "$ "+$scope.rowData.price;
    		    				
    		    				});
    		    			 });
    		    			 
    		    			 $scope.gridApi.core.on.filterChanged( $scope, function() {
    		    		          var grid = this.grid;
    		    		          $scope.gridOptions1.data = $filter('filter')($scope.vehiClesList,{'make':grid.columns[0].filters[0].term,'last4Vin':grid.columns[1].filters[0].term,'stock':grid.columns[2].filters[0].term},undefined);
    		    		        });
    		    			 
    		    			 };	 
    			 
    		    			 $scope.gridOptions2 = {
    		    		    		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    		    		    		    paginationPageSize: 150,
    		    		    		    enableFiltering: true,
    		    		    		    enableCellEditOnFocus: true,
    		    		    		    useExternalFiltering: true,
    		    		    		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
    		    		    		 };
    		    		    		 $scope.gridOptions2.enableHorizontalScrollbar = 0;
    		    		    		 $scope.gridOptions2.enableVerticalScrollbar = 2;
    		    		    		 $scope.gridOptions2.columnDefs = [
    		    		    		                                 { name: 'title', displayName: 'Title',enableColumnMenu: false, width:'15%',cellEditableCondition: true,
    		    		    		                                	 cellTemplate: '<div> <a ng-mouseenter="grid.appScope.mouse(row)" ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
    		    		    		                                 },
    		    		    		                                 { name: 'last4Vin', displayName: 'vin',enableColumnMenu: false, width:'5%',cellEditableCondition: true,
    		    		    		                                	 cellTemplate: '<div> <label  style="line-height: 200%;" data-content="{{row.entity.last4Vin}}" >{{row.entity.last4Vin}}</label> </div>',
    		    		    		                                 },
    		    		    		                                 { name: 'stock',enableColumnMenu: false, displayName: 'Stock', width:'6%',
    		    		    		                                 },
    		    		    		                                 { name: 'bodyStyle', displayName: 'Body Style',enableFiltering: false,enableColumnMenu: false, width:'9%',cellEditableCondition: false,
    		    		    		                                	 cellTemplate:'<select style="width:100%;" ng-model="row.entity.bodyStyle" ng-change="grid.appScope.updateVehicleBody(row)"><option value="">Select</option><option value="Sedan">Sedan</option><option value="Coupe">Coupe</option><option value="SUV">SUV</option><option value="Van">Van</option><option value="Minivan">Minivan</option></select>',
    		    		    		                                 },
    		    		    		                                 { name: 'mileage', displayName: 'Mileage',enableFiltering: false,enableColumnMenu: false, width:'8%',cellEditableCondition: true,
    		    		    		                                 },
    		    		    		                                 { name: 'city_mileage', displayName: 'City MPG',enableFiltering: false,enableColumnMenu: false, width:'8%',cellEditableCondition: true,
    		    		    		                                 },
    		    		    		                                 { name: 'highway_mileage', displayName: 'HWY MPG',enableFiltering: false, width:'8%',enableColumnMenu: false,cellEditableCondition: true,
    		    		    		                                 },
    		    		    		                                 { name: 'price', displayName: 'Price',enableFiltering: false,enableColumnMenu: false, width:'8%',
    		    		    		                                 },
    		    		    		                                 { name: 'vehicleCnt', displayName: 'Photos',enableFiltering: false,enableColumnMenu: false, width:'7%',cellEditableCondition: false,
    		    		    		                                	 cellTemplate: '<div> <a ng-click="grid.appScope.getImages(row)" style="line-height: 200%;" title="" data-content="{{row.entity.vehicleCnt}}">{{row.entity.vehicleCnt}}</a></div>',
    		    		    		                                 },
    		    		    		                                 { name: 'testDrive', displayName:'Next Test Drive' ,enableFiltering: false,enableColumnMenu: false, width:'10%',cellEditableCondition: false,
    		    		    		                                 },
    		    		    		                                 { name: 'pageViewCount', displayName: 'History Log',enableFiltering: false,enableColumnMenu: false, width:'9%',cellEditableCondition: false,
    		    		    		                                	 cellTemplate:'<span style="margin-left:10px;">{{row.entity.pageViewCount}}</span><i ng-if="row.entity.sold" title="Vehicle History" style="margin-left:10px;"class="glyphicon glyphicon-eye-open" ng-click="grid.appScope.historyVehicle(row)"></i>',
    		    		    		                                 },
    		    		    		                                 { name: 'edit', displayName: '', width:'12%',enableFiltering: false,enableColumnMenu: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
    		    		        		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editVehicle(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i> &nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-ok-circle" ng-click="grid.appScope.updateVehicleStatus(row)"  title="Add to Current Inventory"></i> &nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteVehicle(row)"></i>&nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-stats" ng-click="grid.appScope.showSessionData(row)" title="sessions"></i>&nbsp;', 
    		    		    		                                 
    		    		    		                                 },
    		    		        		                                
    		    		        		                                 ];  
    		    		     
    		    		    		 $scope.gridOptions2.onRegisterApi = function(gridApi){
    		    		    			 $scope.gridApi = gridApi;
    		    		    			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
    		    		    			 $scope.rowData = rowEntity;
    		    		    			 $scope.$apply();
    		    		    				 var str = $scope.rowData.price.split(" ");
    		    		    				 $scope.rowData.price = str[1];
    		    		    				 apiserviceViewVehicle.updateVehicle($scope.rowData).then(function(data){
    		    		    			 
    		    		    				 	$scope.rowData.price = "$ "+$scope.rowData.price;
    		    		    				});
    		    		    			 });
    		    		    			 
    		    		    			 $scope.gridApi.core.on.filterChanged( $scope, function() {
    		    		    		          var grid = this.grid;
    		    		    		          $scope.gridOptions2.data = $filter('filter')($scope.vehiClesList,{'make':grid.columns[0].filters[0].term,'last4Vin':grid.columns[1].filters[0].term,'stock':grid.columns[2].filters[0].term},undefined);
    		    		    		        });
    		    		    			 
    		    		    			 };

    			 
    			 $scope.updateVehicleBody = function(row){
    				 $scope.rowData = row.entity;
    				 if($scope.rowData.price !=null && $scope.rowData.price != undefined){
    					 var str = $scope.rowData.price.split(" ");
        				 $scope.rowData.price = str[1];
    				 }
    				 apiserviceViewVehicle.updateVehicle($scope.rowData).then(function(data){
    				 
        					$scope.rowData.price = "$ "+$scope.rowData.price;
        				});
    			 };
    			 
    			 
    			 $scope.historyVehicle = function(row){
    				 apiserviceViewVehicle.getVehicleHistory(row.entity.vin).then(function(data){
    				 
    						$scope.vehicleHistory = data;
    						$('#vehicleHistory').click();
    					});
    			 };
    			 
    			 $scope.hideVehicle = function(row){
    				 apiserviceViewVehicle.getGoTodraft(row.entity.id).then(function(data){
    				 
 							$scope.hideText = "Vehicle has been hidden from the website and moved to Drafts list";
 							$scope.hideTitle = "Vehicle moved to drafts";
 							$scope.newlyArrivedTab();
 							$('#hideVehicle').click();
 							$.pnotify({
 							    title: "Success",
 							    type:'success',
 							    text: "Vehicle Added In Draft",
 							});
 						
 					});
    				 
    			 }
    			 
    			 $scope.mouse = function(row) {
    					$('#thumb_image').attr("src", "/getImage/"+row.entity.imgId+"/thumbnail?date="+$scope.tempDate );
    					$('#thumb_image').show();
    					$('#imagePopup').modal();
    				};
    				$scope.mouseout = function(row) {
    					$('#imgClose').click();
    				};
    				
    				$scope.vehicleData = function(sts){
    					if($scope.vType == 'new'){
    						 $scope.doPublic = 0;
    						 apiserviceViewVehicle.getAllVehiclesByType(sts).then(function(data){
    						 
		    			 			for(var i=0;i<data.length;i++) {
		    			 				data[i].price = "$ "+data[i].price;
		    			 			}
		    			 			$scope.vType = "new";
		    			 			$scope.vehiClesList = data;
		    			 			$scope.gridOptions.data = data;
		    			 			$scope.gridOptions.columnDefs[9].displayName='Next Test Drive';
		    			 			$scope.gridOptions.columnDefs[10].displayName='Views';
		    			 		});
    					}
    					if($scope.vType == 'sold'){
    						 $scope.doPublic = 2;
    						 apiserviceViewVehicle.getAllSoldVehiclesByType(sts).then(function(data){
   						 
		    			 			for(var i=0;i<data.length;i++) {
		    			 				data[i].price = "$ "+data[i].price;
		    			 			}
		    			 			$scope.vType = "sold";
		    			 			$scope.vehiClesList = data;
		    			 			$scope.gridOptions2.data = data;
		    			 			$scope.gridOptions.columnDefs[9].displayName='Next Test Drive';
		    			 			$scope.gridOptions.columnDefs[10].displayName='Views';
		    			 		});
   					}
    				};
    				$scope.getImages = function(row) {
    					$location.path('/editVehicle/'+row.entity.id+"/"+true+"/"+row.entity.vin);
    				};
    				
    				
    		    			 $scope.newlyArrivedTab = function() {
    		    				 $scope.flagForUser = true;
    		    				 $scope.gridOptions.data = [];
    		    				 $scope.doPublic = 0;
    		    				 console.log($scope.userType);
    		    				 apiserviceViewVehicle.findLocation().then(function(data){
    		    				 
		    							console.log(data);
		    							$scope.userLocationId = data;
    		    				 if($scope.userType == "Photographer"){
    		    					 apiserviceViewVehicle.getAllVehiclesImage($scope.userLocationId).then(function(data){
    		    				
    		    			 			console.log(data);
    		    			 			/*for(var i=0;i<data.length;i++) {
    		    			 				data[i].price = "$ "+data[i].price;
    		    			 			}*/
    		    			 			for(var i=0;i<data.length;i++) {
    		    			 				
    		    			 				data[i].userRole=$scope.userRole;
    		    			 				
    		    							}
    		    			 			
    		    			 			$scope.vType = "new";
    		    			 			$scope.vehiClesList = data;
    		    			 			$scope.gridOptions.data = data;
    		    			 			console.log($scope.gridOptions.data);
    		    			 			$scope.gridOptions.columnDefs[9].displayName='Next Test Drive';
    		    			 			$scope.gridOptions.columnDefs[10].displayName='Views';
    		    			 		});
    		    				 }
    		    				 else{
    		    					 apiserviceViewVehicle.getAllVehicles($scope.userLocationId).then(function(data){
    		    					 
     		    			 			for(var i=0;i<data.length;i++) {
     		    			 				data[i].price = "$ "+data[i].price;
     		    			 			}
     		    			 			for(var i=0;i<data.length;i++) {
     		    			 				data[i].userRole=$scope.userRole;
     		    			 			}
     		    			 			$scope.vType = "new";
     		    			 			$scope.vehiClesList = data;
     		    			 			$scope.gridOptions.data = data;
     		    			 			console.log($scope.gridOptions.data);
     		    			 			$scope.gridOptions.columnDefs[9].displayName='Next Test Drive';
     		    			 			$scope.gridOptions.columnDefs[10].displayName='Views';
     		    			 		});
    		    					 
    		    				 }
    		    				 });
    		    			 }	    			 
    		    			 
    		    			 $scope.soldTab = function() {
    		    				 $scope.ch = false;
    		    				 $scope.doPublic = 2;
    		    				 $scope.flagForUser = true;
    		    				 apiserviceViewVehicle.getAllSoldVehicles().then(function(data){
    		    				 
    		    			 			for(var i=0;i<data.length;i++) {
    		    			 				data[i].price = "$ "+data[i].price;
    		    			 			}
		    			 				for(var i=0;i<data.length;i++) {
		    			 					data[i].userRole=$scope.userRole;
		    			 				}
		    			 				$scope.vType = "sold";
    		    			 			$scope.type = "All";
    		    			 			$scope.vehiClesList = data;
    		    			 			$scope.gridOptions2.data = data;
    		    			 			$scope.gridOptions.columnDefs[8].displayName='Sold Date';
    		    			 			$scope.gridOptions.columnDefs[9].displayName='History';
    		    			 		});
    		    			 }
    		    			 $scope.draftTab = function() {
    		    				 $scope.flagForUser = true;
    		    				 $scope.doPublic = 1;
    		    				 apiserviceViewVehicle.findLocation().then(function(data){
    		    				 
		    							console.log(data);
		    							$scope.userLocationId = data;
    		    				 if($scope.userType == "Photographer"){
    		    					 console.log($scope.userLocationId);
    		    					 apiserviceViewVehicle.getAllDraftVehiclesdata($scope.userLocationId).then(function(data){
    		    					 
    		    			 			for(var i=0;i<data.length;i++) {
    		    			 				data[i].price = "$ "+data[i].price;
    		    			 			}
    		    			 			for(var i=0;i<data.length;i++) {
    		    			 				
    		    			 				data[i].userRole=$scope.userRole;
    		    			 				
    		    							}
    		    			 			$scope.doPublic = 1;
    		    			 			$scope.vType = "sold";
    		    			 			$scope.type = "All";
    		    			 			$scope.vehiClesList = data;
    		    			 			$scope.gridOptions1.data = data;
    		    			 			$scope.gridOptions.columnDefs[8].displayName='Next Test Drive';
    		    			 			$scope.gridOptions.columnDefs[9].displayName='Views';
    		    			 			
    		    			 			
    		    			 		});
    		    				 }
    		    				 else{
    		    					 apiserviceViewVehicle.getAllDraftVehicles($scope.userLocationId).then(function(data){
    		    					 
     		    			 			for(var i=0;i<data.length;i++) {
     		    			 				data[i].price = "$ "+data[i].price;
     		    			 			}
     		    			 			for(var i=0;i<data.length;i++) {
     		    			 				data[i].userRole=$scope.userRole;
     		    			 			}
     		    			 			$scope.doPublic = 1;
     		    			 			$scope.vType = "sold";
     		    			 			$scope.type = "All";
     		    			 			$scope.vehiClesList = data;
     		    			 			$scope.gridOptions1.data = data;
     		    			 			$scope.gridOptions.columnDefs[8].displayName='Next Test Drive';
     		    			 			$scope.gridOptions.columnDefs[9].displayName='Views';
     		    			 		});
    		    					 
    		    				 }
    		    			 }); 
    		    			 }
    		    			 
    	$scope.editVehicle = function(row) {
    		$location.path('/editVehicleNew/'+row.entity.id+"/"+false+"/"+row.entity.vin);
    	}	 
    	
   $scope.vehiClesList = [];
  
   $scope.viewVehiclesInit = function() {
	   $scope.newlyArrivedTab();
   }
   
   $scope.deleteVehicle = function(row){
	   $('#deleteModal').click();
	   $scope.rowDataVal = row;
   }
   
   $scope.showSessionData = function(row){
	   $location.path('/sessionsAnalytics/'+row.entity.id+"/"+row.entity.vin+"/"+row.entity.status);
   }
   
   $scope.deleteVehicleRow = function() {
	   
	   apiserviceViewVehicle.deleteVehicleById($scope.rowDataVal.entity.id).then(function(data){
	   
			if($scope.rowDataVal.entity.status == 'Newly Arrived') {
				 $scope.viewVehiclesInit();
			} 
			if($scope.rowDataVal.entity.status == 'Sold') {
				$scope.soldTab();
			}
		});
   }
   
   $scope.soldContact = {};
   
   $scope.updateVehicleStatusPublic = function(row){
	   apiserviceViewVehicle.addPublicCar(row.entity.id).then(function(data){
	   
		   	$scope.hideText = "Vehicle has been published";
		   	$scope.hideTitle = "Vehicle has been published";
			$('#hideVehicle').click();
			   
			   $scope.draftTab();
	   });
   }
   $scope.updateVehicleStatus = function(row){
	   $scope.statusVal = "";
	   if(row.entity.status == 'Newly Arrived') {
		   $('#btnStatusSchedule').click();
		   $scope.soldContact.statusVal = "Sold";
	   }
	   if(row.entity.status == 'Sold') {
		   
		    $('#AddbtnInventory').modal();
		  
		  }
	   $scope.addtoinventory = function() {
		   
		   apiserviceViewVehicle.addSameNewCar(row.entity.id).then(function(data){
		   
			   if(data=='success'){
				   $scope.soldContact.statusVal = "Newly Arrived";
				   $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Vehicle has been added to Inventory",
					});
			   }else{
				   $.pnotify({
					    title: "Error",
					    type:'success',
					    text: "Vehicle already in Inventory",
					});
			   }
		   });
		   
		   
	   }
	   
	   $scope.soldContact.make = row.entity.make;
	   $scope.soldContact.mileage = row.entity.mileage;
	   $scope.soldContact.model = row.entity.model;
	   $scope.soldContact.year = row.entity.year;
	   $scope.soldContact.vin = row.entity.vin;
	   $scope.soldContact.id = row.entity.id;
	   var str = row.entity.price.split(" ");
	   $scope.soldContact.price = str[1];
   }
   
	$scope.saveVehicalStatus = function() {
		apiserviceViewVehicle.setVehicleStatus($scope.soldContact).then(function(data){
		
			$('#vehicalStatusModal').modal('hide');
			if($scope.soldContact.statusVal == 'Newly Arrived') {
				 $scope.viewVehiclesInit();
				 $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Vehicle status marked Newly Arrived",
					});
				 $scope.soldTab();
			} 
			if($scope.soldContact.statusVal == 'Sold') {
				$scope.soldTab();
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Vehicle status marked sold",
				});
				
				$scope.newlyArrivedTab();
			}
			
	});
}
   
   $scope.editingData = [];
   
   for (var i = 0; i <  $scope.vehiClesList.length; i++) {
     $scope.editingData[$scope.vehiClesList[i].id] = false;
     $scope.viewField = false;
   }
   

   $scope.modify = function(tableData){
	   $scope.viewField = true;
       $scope.editingData[tableData.id] = true;
   };

   $scope.update = function(tableData){
       $scope.editingData[tableData.id] = false;
       $scope.viewField = false;
       apiserviceViewVehicle.updateVehicle(tableData).then(function(data){
      
		});
   };
   
   $scope.cancle = function(tableData){
	   $scope.editingData[tableData.id] = false;
	   $scope.viewField = false;
   }
   
   $scope.exportDataAsCSV = function() {
	   apiserviceViewVehicle.exportDataAsCSV().then(function(data){
	   
		});
   }
   
   $scope.exportCarfaxCSV = function() {
	   apiserviceViewVehicle.exportCarfaxCSV().then(function(data){
	   
		});
   }
   
   $scope.exportCarGurusCSV = function() {
	   apiserviceViewVehicle.exportCarGurusCSV().then(function(data){
	   
		});
   }
   
  
   
   
   
   /*------------------------------------Add coll------------------------*/
   
   
   $scope.gridOptions3 = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 $scope.gridOptions3.enableHorizontalScrollbar = 0;
	 $scope.gridOptions3.enableVerticalScrollbar = 2;
	 
	
	$scope.flagForChart1 = true;
	$scope.flagForUser = true;
	$scope.allCollectionData = function(){
		$scope.flagForUser = false;
		 $scope.doPublic = 3;
		 apiserviceViewVehicle.getAllCollectionData().then(function(data){
		
			console.log(data);
			$scope.leadtypeObjList = data;
			$scope.gridOptions3.data = data;
			
			console.log("gghgfhghghg");
			console.log($scope.gridOptions3.data);
		});
		
		
		
		$scope.gridOptions3.columnDefs = [
		                                 { name: 'id', displayName: 'Id', width:'15%',cellEditableCondition: false
		                                 },
		                                 { name: 'title', displayName: 'Title', width:'60%',cellEditableCondition: false
		                                 },
		                                 
		                                 { name: 'edit', displayName: ' ', width:'25%',
  		                                 cellTemplate:'<i class="fa fa-trash" ng-if="row.entity.title != \'New\' && row.entity.title != \'Used\' && row.entity.title != \'Coming Soon\'" ng-click="grid.appScope.removeCollection(row)"  title="Delete"></i> &nbsp;&nbsp;&nbsp&nbsp;<i class="glyphicon glyphicon-pencil" ng-if="row.entity.title != \'New\' && row.entity.title != \'Used\' && row.entity.title != \'Coming Soon\'" ng-click="grid.appScope.editCollection(row)"  title="Edit"></i> ', 
  		                                 
		                                 },
		                                    ];
	}
	
	$scope.addCollection = function(){
		console.log("Checkkkk");
		$scope.collections = {};
		
		$('#collectionpopups').modal('show');
	}
	
	
	$scope.collections = {};
	$scope.saveCollectionData = function(){
		console.log("in collection");
		$scope.collections.title = $scope.collections.title;
		if($scope.collections.id == undefined){
			$scope.collections.id = 0;
		}
		console.log($scope.collections);
		apiserviceViewVehicle.addNewCollection($scope.collections).then(function(data){
		
				$scope.form = data;
			 console.log("::::::success")
				
			 $("#collectionpopups").modal('hide');
			 $('#editcollectionpopup').modal('hide');
			 $scope.allCollectionData();
			});
	}
	
	
	$scope.editCollection = function(row){
		console.log(row.entity);
		$scope.collections = row.entity;
		$('#editcollectionpopup').modal('show');
	}
	
	
	$scope.removeCollection = function(row){
		console.log(row.entity);
		$scope.collections = row.entity.id;
		console.log($scope.collections);
		$('#deletepopup').modal('show');
	}
	
	
	$scope.deleteCollection = function(){
		 console.log("in deletend functio");
		 console.log($scope.collections);
		 apiserviceViewVehicle.deleteCollectionData($scope.collections).then(function(data){
		
				 console.log("out deletend functio");
				 $scope.allCollectionData();
				 
				 

			});
	 }
	
	
}]);
