'use strict';

/**
 * @ngdoc function
 * @name newappApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the newappApp
 */
angular.module('newApp').directive('myPostRepeatDirective', function() {
  return function(scope, element, attrs) {
    if (scope.$last) {
      scope.$eval('doComplete()');
    }
  };
});
angular.module('newApp')
  .controller('dashboardCtrl', ['$scope', 'dashboardService', 'pluginsService', '$http','$compile','$interval','$filter','$location','$timeout','$route','$q','$upload','$rootScope','apiserviceDashborad',function ($scope, dashboardService, pluginsService,$http,$compile,$interval,$filter,$location,$timeout,$route,$q,$upload,$rootScope,apiserviceDashborad) {
	  var ele = document.getElementById('loadingmanual');	
   	$(ele).hide();
   	
   	$scope.showFormly = '1';
	   $scope.showFormly1 = '0';
	   
	   apiserviceDashborad.getLocationDays().then(function(data){
		   $scope.locationDays = data;
	   });
	   

	   
	   apiserviceDashborad.getDealerProfile().then(function(data){
		   $scope.userProfile = data.dealer;
	   });
	
	
	
	 $rootScope.$on("CallComingSoonMethod", function(){
         $scope.priceAlertMsg();
      });
	 
	 $rootScope.$on("CallReminderMethod", function(){
		 $scope.reminderPopup();
      });
	 
	 
	
	$scope.txt = false;
	$scope.userKey = userKey;
	$scope.vehicles = "All";
	$scope.userRole;
	$scope.locationValue = null;
	$scope.priceLbl = 'true';
	$scope.priceTxt = 'false';
	$scope.nameLbl = 'true';
	$scope.nameTxt = 'false';
	$scope.vin = null;
	$scope.index = null;
	$scope.successRate= 'false';
	$scope.currentLeads = 'true';
	$scope.amountSold = 'true';
	$scope.soldCar= 'true';
	$scope.orderItem = null;
	$scope.index=null;
	$scope.userId = null;
	$scope.userComment = null;
	$scope.countFlag = 'true';
	$scope.priceFlag = 'true';
	$scope.priceFlagL = 'true';
	$scope.followerFlag = 'true';
	$scope.avgSaleFlagL = 'true';
	$scope.soldCarFlagL = 'true';
	$scope.successRateFlagL = 'true';
	$scope.percentOfMoneyFlagL = 'true';
	$scope.leadFlag = 'true';
	$scope.listingFilter = null;
	$scope.len = null;
	$scope.showGmLocation = 0;
	if(locationId != 0){
		$scope.showGmLocation = 1;
		$scope.showSelectLocationDash = locationId;
		
	}
	
	apiserviceDashborad.getAllVehicles().then(function(data){
		$scope.vinSearchList = data;
	   });
	
		
			$scope.stockWiseData = [];
	$scope.selectedVin = function (selectObj) {
		if(typeof selectObj.originalObject != 'undefined'){
			$scope.item = selectObj.originalObject;
			if($scope.len !=null){
				$scope.stockWiseData[$scope.len].model = $scope.item.model;
				$scope.stockWiseData[$scope.len].make = $scope.item.make;
				$scope.stockWiseData[$scope.len].stockNumber = $scope.item.stock;
				$scope.stockWiseData[$scope.len].year = $scope.item.year;
				$scope.stockWiseData[$scope.len].bodyStyle = $scope.item.bodyStyle;
				$scope.stockWiseData[$scope.len].mileage = $scope.item.mileage;
				$scope.stockWiseData[$scope.len].transmission = $scope.item.transmission;
				$scope.stockWiseData[$scope.len].drivetrain = $scope.item.drivetrain;
				$scope.stockWiseData[$scope.len].engine = $scope.item.engine;
				$scope.stockWiseData[$scope.len].vin = $scope.item.vin;
				$scope.stockWiseData[$scope.len].imgId = $scope.item.imgId;
				$scope.stockWiseData[$scope.len].searchStr = $scope.item.vin;
			}
			$('#vinSearch_value').val($scope.item.vin);
		}
	};
	
	apiserviceDashborad.getUserType().then(function(data){
		$scope.userType = data;
	 	if($scope.userType == "Manager") {
	 		$scope.getGMData();
	 		$scope.getToDoNotification();
	 		$scope.getAllSalesPersonRecord($scope.userKey);
	 		$scope.topLocations('Week');
	 	}
	 	if($scope.userType == "Sales Person") {
	 		$scope.getToDoNotification();
	 		$scope.getAssignedLeads();
	 		$scope.getAllSalesPersonRecord($scope.userKey);
	 	}
	 	
	 	if($scope.userType == "General Manager"){
	 		$scope.topLocations('Week');
	 	}
	});
	
	$scope.topListingCount = function(name,flag){
		if(flag=='true'){
			$scope.listingFilter = "-"+name;
			$scope.countFlag = 'false';
		}else{
			$scope.listingFilter = name;
			$scope.countFlag = 'true';
		}		
	};
	$scope.topListingPrice = function(name,flag){
		if(flag=='true'){
			$scope.listingFilter = "-"+name;
			$scope.priceFlag = 'false';
		}else{
			$scope.listingFilter = name;
			$scope.priceFlag = 'true';
		}		
	};
	$scope.topListingFollower = function(name,flag){
		if(flag=='true'){
			$scope.listingFilter = "-"+name;
			$scope.followerFlag = 'false';
		}else{
			$scope.listingFilter = name;
			$scope.followerFlag = 'true';
		}		
	};
	$scope.topListingLead = function(name,flag){
		if(flag=='true'){
			$scope.listingFilter = "-"+name;
			$scope.leadFlag = 'false';
		}else{
			$scope.listingFilter = name;
			$scope.leadFlag = 'true';
		}		
	};

	$scope.imageEnter = function(index){
		$scope.index=index;
	};
	$scope.imageLeave  = function(index){
		$scope.index=null;
	};
	
	$scope.orderItem = '-salesAmount';
	$scope.successRateFilter = function(successRate){
		if(successRate == 'true'){
			$scope.orderItem = '-successRate';
			$scope.successRate = 'false';
		}else if(successRate == 'false'){
			$scope.orderItem = 'successRate';
			$scope.successRate = 'true';
		}
	};
	$scope.currentLeadsFilter = function(currentLeads){
		if(currentLeads == 'true'){
			$scope.orderItem = '-currentLeads';
			$scope.currentLeads = 'false';
		}else if(currentLeads == 'false'){
			$scope.orderItem = 'currentLeads';
			$scope.currentLeads = 'true';
		}
	};
	$scope.amountSoldFilter = function(amountSold){
		if(amountSold == 'true'){
			$scope.orderItem = '-salesAmount';
			$scope.amountSold = 'false';
		}else if(amountSold == 'false'){
			$scope.orderItem = 'salesAmount';
			$scope.amountSold = 'true';
		}
	};
	$scope.soldCarFilter = function(soldCar){
		if(soldCar == 'true'){
			$scope.orderItem = '-saleCar';
			$scope.soldCar = 'false';
		}else if(soldCar == 'false'){
			$scope.orderItem = 'saleCar';
			$scope.soldCar = 'true';
		}
	};
	
	
	$scope.topListingPriceLocation = function(flag){
		if(flag=='true'){
			$scope.listingFilterLocation = '-totalMoneyBrougthLocation';
			$scope.priceFlagL = 'false';
		}else{
			$scope.listingFilterLocation = 'totalMoneyBrougthLocation';
			$scope.priceFlagL = 'true';
		}		
	};	
	
	$scope.topListingSoldCarLocation = function(flag){
		if(flag=='true'){
			$scope.listingFilterLocation = '-carSoldLocation';
			$scope.soldCarFlagL = 'false';
		}else{
			$scope.listingFilterLocation = 'carSoldLocation';
			$scope.soldCarFlagL = 'true';
		}		
	};	
	
	$scope.topListingAvgSaleLocation = function(flag){
		if(flag=='true'){
			$scope.listingFilterLocation = '-avgSaleLocation';
			$scope.avgSaleFlagL = 'false';
		}else{
			$scope.listingFilterLocation = 'avgSaleLocation';
			$scope.avgSaleFlagL = 'true';
		}		
	};	
	
	$scope.topListingPercentOfMoneyLocation = function(flag){
		if(flag=='true'){
			$scope.listingFilterLocation = '-percentOfMoney';
			$scope.percentOfMoneyFlagL = 'false';
		}else{
			$scope.listingFilterLocation = 'percentOfMoney';
			$scope.percentOfMoneyFlagL = 'true';
		}		
	};	
	
	$scope.topListingSuccessRateLocation = function(flag){
		if(flag=='true'){
			$scope.listingFilterLocation = '-successRate';
			$scope.successRateFlagL = 'false';
		}else{
			$scope.listingFilterLocation = 'successRate';
			$scope.successRateFlagL = 'true';
		}		
	};	
	
	
	
	$scope.openComment = function(item){
		$scope.userId = item.id;
		$('#commentModel').modal();
	};
	
	$scope.saveComment = function(){
		if($scope.userComment !=null && $scope.userComment !=""){
			apiserviceDashborad.updateUserComment($scope.userId, $scope.userComment).then(function(data){
			});
		}
		$scope.userId = null;
		$scope.userComment = null;
	};
	
	
	apiserviceDashborad.getUserPermission().then(function(data){
		$scope.userPer = data;
	});
	
	apiserviceDashborad.getDataFromCrm().then(function(data){
		$scope.searchList = data;
	});
	
	
	$scope.editPrice = function(vin,index,id){
		$scope.priceLbl = 'false';
		$scope.priceTxt = 'true';
		$scope.vin = vin;
		$scope.index = index;
		$('#editPrice').focus();
	};
	$scope.editName = function(vin,index,id){
		$scope.nameLbl = 'false';
		$scope.nameTxt = 'true';
		$scope.vin = vin;
		$scope.index = index;
		$('#editName').focus();
	};
	$scope.setLable = function(price,vin){
		$scope.priceLbl = 'true';
		$scope.priceTxt = 'false';
		
		apiserviceDashborad.updateVehiclePrice(vin, price).then(function(data){
		});
	};
	$scope.setName = function(name,vin){
		$scope.nameLbl = 'true';
		$scope.nameTxt = 'false';
		apiserviceDashborad.updateVehicleName(vin, name).then(function(data){
		});
	};
	$scope.showBackGmButton = 0;
		
		apiserviceDashborad.getUserRole().then(function(data){
			$scope.userRole = data.role;
			 var startD = $('#cnfstartDateValue').val();
			   var endD = $('#cnfendDateValue').val();
			   
			   apiserviceDashborad.getfindGmIsManager().then(function(data){
				   $scope.showBackGmButton = data;
				});
			
			$scope.getSalesDataValue($scope.locationValue);
			
			if($scope.userRole == "Manager"){
				//$scope.userLocationData('Week','location');
				   $scope.findMystatisData(startD,endD,'location');
			}else if($scope.userRole != "General Manager"){
				$scope.locationValue = data.location.id;
				$scope.findMystatisData(startD,endD,'person');
				//$scope.userLocationData('Week','person');
			}
			
			
			
			if(locationId != 0){
				if($scope.userRole == "Manager"){
					  $scope.findMystatisData(startD,endD,'location');
				}else{
					$scope.findMystatisData(startD,endD,'person');
				}
			}else{
				 $scope.showSelectLocationDash = $scope.locationValue;
			}
			$scope.showVehicalBarChart();
			if($scope.userRole == null){
				  $location.path('/myprofile');
			}
		});
	
	/*	$http.get('/getUserRole').success(function(data) {
			
	
		});*/
		
		$('#cnfmeetingtime').timepicker().on('hide.timepicker', function (e) {
			$scope.checked = [];
			$scope.bestDt = $('#cnfmeetingdate').val();
			$scope.bestTm = $('#cnfmeetingtime').val();
			$scope.bestEndTm = $('#cnfmeetingtimeEnd').val();
			if(($scope.bestDt != null && $scope.bestDt != "")  && ($scope.bestTm !=null && $scope.bestTm !="") && ($scope.bestEndTm !=null && $scope.bestEndTm !="")){
				$http.get('/getUserAppointment/'+$scope.bestDt+"/"+$scope.bestTm+"/"+$scope.bestEndTm)
				.success(function(data) {
					if(data.length > 0){
						if(data[0].meetingStatus != null){
							$scope.appoTitle = data[0].name +"(Meeting)";
						}else{
							$scope.appoTitle = data[0].name +"(Test Drive)";
						}
						$scope.personName = "";
						$scope.dateTime = data[0].confirmDate;
						$scope.dateTime1 = data[0].confirmTime;
						$scope.dateEndTime = data[0].confirmEndTime;
						
						$('#userAppointment').click();
						angular.forEach(data, function(obj, index){
							var name = obj.fullName+", ";
							$scope.personName = $scope.personName + name;
						});
					}
				});
				
				   apiserviceDashborad.getUserForMeeting($scope.bestDt, $scope.bestTm, $scope.bestEndTm).then(function(data){
					   $scope.gridOptions11.data = data;
						angular.forEach($scope.gridOptions11.data, function(obj, index){
							if(obj.userStatus == 'N/A'){
								obj.disabled = false;
							}else{
								obj.disabled = true;
							}
						});
				   });
			}else{
				$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Please Select Date and Time",
					});
			}
		});
		
		$('#cnfmeetingtimeEnd').timepicker().on('hide.timepicker', function (e) {
			$scope.checked = [];
			$scope.bestDt = $('#cnfmeetingdate').val();
			$scope.bestTm = $('#cnfmeetingtime').val();
			$scope.bestEndTm = $('#cnfmeetingtimeEnd').val();
			if(($scope.bestDt != null && $scope.bestDt != "")  && ($scope.bestTm !=null && $scope.bestTm !="") && ($scope.bestEndTm !=null && $scope.bestEndTm !="")){
				
				apiserviceDashborad.getUserAppointment($scope.bestDt, $scope.bestTm, $scope.bestEndTm).then(function(data){
					if(data.length > 0){
						if(data[0].meetingStatus != null){
							$scope.appoTitle = data[0].name +"(Meeting)";
						}else{
							$scope.appoTitle = data[0].name +"(Test Drive)";
						}
						$scope.personName = "";
						$scope.dateTime = data[0].confDate;
						$scope.dateTime1 = data[0].confirmTime;
						$scope.dateEndTime = data[0].confirmEndTime;
						$('#userAppointment').click();
						angular.forEach(data, function(obj, index){
							if(obj.fullName != null){
								var name = obj.fullName+", ";
								$scope.personName = $scope.personName + name;
							}
						});
					}
				});
				
				apiserviceDashborad.getUserForMeeting($scope.bestDt, $scope.bestTm, $scope.bestEndTm).then(function(data){
					$scope.gridOptions11.data = data;
					angular.forEach($scope.gridOptions11.data, function(obj, index){
						if(obj.userStatus == 'N/A'){
							obj.disabled = false;
						}else{
							obj.disabled = true;
						}
					});
				});
				
			}else{
				$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Please Select Date and Time",
					});
			}
		});
		$scope.checked = [];
		$scope.userRow;
		$scope.selectUserPop = function(row){
			if(row.entity.isSelect == true){
				angular.forEach($scope.gridOptions11.data, function(obj, index){
					if(obj.$$hashKey == row.entity.$$hashKey){
						obj.isSelect = false;
					}
				});
				$scope.userRow = row;
				$scope.message = row.entity.fullName + " has a meeting during this time. Do you still want to send invitation ?";
				$('#meetingUsr').click();
			}else{
				$scope.deleteSelectedUser(row.entity);
			}
		};
		
		$scope.addUserMeeting = function(){
				angular.forEach($scope.gridOptions11.data, function(obj, index){
					if(obj.$$hashKey == $scope.userRow.entity.$$hashKey){
						obj.isSelect = true;
					}
				});
			$scope.checked.push($scope.userRow.entity);
		};
		
		$scope.selectUser = function(row){
			if(row.entity.isSelect == true){
				$scope.checked.push(row.entity);
			}else{
				$scope.deleteSelectedUser(row.entity);
			}
		};
		
		$scope.deleteSelectedUser = function(item){
			angular.forEach($scope.checked, function(obj, index){
				 if ((item.id == obj.id) && (item.leadType == obj.leadType)) {
					 $scope.checked.splice(index, 1);
			       	return;
			    };
			  });
		};
		
	$scope.gridOptions11 = {
	 		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		 		    paginationPageSize: 150,
	 		 		   // enableFiltering: true,
	 		 		    useExternalFiltering: true,
	 		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 		 };
	 		 		 $scope.gridOptions11.enableHorizontalScrollbar = 0;
	 		 		 $scope.gridOptions11.enableVerticalScrollbar = 2;
	 		 		 $scope.gridOptions11.columnDefs = [
															{ name: 'isSelect', displayName: 'Select', width:'10%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
																cellTemplate:'<input type="checkbox" ng-change="grid.appScope.selectUser(row)" ng-model="row.entity.isSelect" ng-show="row.entity.disabled" > <input type="checkbox" ng-change="grid.appScope.selectUserPop(row)" ng-model="row.entity.isSelect" ng-if="row.entity.disabled == false">',
															},
															 { name: 'fullName', displayName: 'Full Name', width:'30%',cellEditableCondition: false,
																cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																   if (row.entity.isRead === false) {
																	 return 'red';
																 }
																} ,
															 },
														   { name: 'role', displayName: 'Role', width:'20%',cellEditableCondition: false,
																	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																	   if (row.entity.isRead === false) {
																		 return 'red';
																	 }
																	} ,
															},
	 		 		                               
														   { name: 'userStatus', displayName: 'Availability', width:'20%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
																cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																	   if (row.entity.isRead === false) {
																		 return 'red';
																	 }
																	} 
															},
															{ name: 'time', displayName: 'Time', width:'20%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
																cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																	   if (row.entity.isRead === false) {
																		 return 'red';
																	 }
																	} 
															},
	 		 		                               
														];
	
	$scope.topLocations = function(timeSet){
		apiserviceDashborad.getAllLocation(timeSet).then(function(data){
			if(locationId == 0){
				$scope.showSelectLocationDash = $scope.locationValue;
			}
		$scope.locationDataListShow = data;	
		angular.forEach($scope.locationDataListShow, function(value, key) {
			if(value.successRate !=null){
				value.successRate = value.successRate.toFixed(1);
			}else{
				value.successRate = 0;
			}
			
			if(value.PlanPer !=null){
				value.PlanPer = value.PlanPer.toFixed(0);
			}else{
				value.PlanPer = 0;
			}
			
			if(value.avgSaleLocation !=null){
				value.avgSaleLocation = value.avgSaleLocation.toFixed(1);
			}else{
				value.avgSaleLocation = 0;
			}
		});
		});
		
		
	}
	
	
	
	$scope.findMystatisData = function(startD,endD,locOrPer){
		
		if(locationId != 0){
			
			apiserviceDashborad.gmLocationManager(locationId).then(function(data){
				apiserviceDashborad.getUserLocationByDateInfo(data.id,startD,endD,locOrPer).then(function(data){
					$scope.flagForBestSale=data.flagForBestSaleIcon;
					apiserviceDashborad.getPlanTarget(locOrPer).then(function(data1){
						
						data.sendData[0].plan = data1.data[0].price;
						$scope.stackchart = data.sendData;
						if($scope.stackchart[0].data[0] == 0){
							$scope.stackchart[0].plan = 0;
						}
						if(data1.data[0].price != null){
							$scope.stackchart[0].price = data1.data[0].price;
						}else{
							$scope.stackchart[0].price = 0;
						}
						$scope.callChart($scope.stackchart);
						if(data1.data[0].price == null){
							var chart = $('#container').highcharts();
					        chart.yAxis[0].removePlotLine('plotline-1');
						}
						
				});
					$scope.countTestDrives=data.countTestDrives;
					$scope.parLocationData = data;
					$scope.leadsTime.leads = data.leads;
					$scope.leadsTime.goalSetTime = data.goalTime;
					$scope.showLeads = data.leads;	
				});
			});
			
		}else{
			apiserviceDashborad.getUserLocationByDateInfo($scope.userKey,startD,endD,locOrPer).then(function(data){
				$scope.flagForBestSale=data.flagForBestSaleIcon;
				apiserviceDashborad.getPlanTarget(locOrPer).then(function(data1){
					data.sendData[0].plan = data1.data[0].price;
					$scope.stackchart = data.sendData;
					if($scope.stackchart[0].data[0] == 0){
						$scope.stackchart[0].plan = 0;
					}
					if(data1.data[0].price != null){
						$scope.stackchart[0].price = data1.data[0].price;
					}else{
						$scope.stackchart[0].price = 0;
					}
					$scope.callChart($scope.stackchart);
					if(data1.data[0].price == null){
						var chart = $('#container').highcharts();
				        chart.yAxis[0].removePlotLine('plotline-1');
					}
					
				});
				
				   $scope.countTestDrives=data.countTestDrives;
					$scope.parLocationData = data;
					$scope.leadsTime.leads = data.leads;
					$scope.leadsTime.goalSetTime = data.goalTime;
					$scope.showLeads = data.leads;
			});
			
		}
		
	 }
	$scope.dataLocOrPerWise = "location";
	$scope.showLeads = null;
	$scope.locationOrPersonData = function(wiseData){
		 var startD = $('#cnfstartDateValue').val();
		   var endD = $('#cnfendDateValue').val();
		$scope.dataLocOrPerWise = wiseData;
		$scope.findMystatisData(startD,endD,$scope.dataLocOrPerWise);
	}
	
	setInterval(function(){
		
		  var startD = $('#cnfstartDateValue').val();
		   var endD = $('#cnfendDateValue').val();
		   
		  
		   
		   if(startD != "" && startD != null && startD != undefined && endD != "" && endD != null && endD != undefined){
			   if($scope.userRole == "Manager"){
				   $scope.findMystatisData(startD,endD,$scope.dataLocOrPerWise);
					//$scope.userLocationData('Week','location');
					$scope.dataLocOrPerWise = "location";
			   }else{
				   $scope.findMystatisData(startD,endD,'person');
				   //$scope.userLocationData('Week','person');
				   $scope.dataLocOrPerWise = "person";
			   }
		   }else{
			   if($scope.userRole == "Manager"){
				   $scope.findMystatisData(startD,endD,$scope.dataLocOrPerWise);
					//$scope.userLocationData('Week','location');
					$scope.dataLocOrPerWise = "location";
			   }else{
				   $scope.findMystatisData(startD,endD,'person');
				   //$scope.userLocationData('Week','person');
				   $scope.dataLocOrPerWise = "person";
			   }
			   
			  
		   }
		
		}, 120000)
	
	$scope.openLeadspopUp = function(){
		/*   $scope.schPlan = {};
		   $scope.nextbutton = 0;*/
		 //  $scope.checkManagerLogin();
		   $('#Locationwise-model').modal();
	   };
	   $scope.leadsTime = {};
	$scope.saveLeads = function(){
		 var startD = $('#cnfstartDateValue').val();
		   var endD = $('#cnfendDateValue').val();
		   apiserviceDashborad.saveLeads($scope.leadsTime).then(function(data){
			   $('#Locationwise-model').modal("toggle");
				 if($scope.userRole == "Manager"){
						//$scope.userLocationData('Week','location');
					 $scope.findMystatisData(startD,endD,'location');
				   }else{
					   $scope.findMystatisData(startD,endD,'person');
					   //$scope.userLocationData('Week','person');
				   }
		   });
		
	}
	   $scope.callChart = function(stackchart){
			$('#container').highcharts({
		        chart: {
		            type: 'column'
		        },
		        title: {
		            text: ''
		        },
		        
		        xAxis: {
		            categories: ""
		        },
		        yAxis: {
		        		plotLines:[{
							value:stackchart[0].plan,
							color: '#ff0000',
							width:2,
							zIndex:4,
							id: 'plotline-1',
							label:{text:"Plan : $"+stackchart[0].price}
						}],
						tooltip: {
								pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>${series.data[0]}</b><br/>',
								shared: true
							}
		        },
		        plotOptions: {
		            column: {
		                stacking: ''
		            }
		        },
				credits: {
							enabled: false
						},
		        series: stackchart
		    });
	   }
	

	   
	$scope.tasksValue = [];
	angular.forEach(taskslist, function(value, key) {
		angular.forEach(value.items, function(value, key) {
			$scope.tasksValue.push(value);
		});
	});
	
	$scope.showLocationWise = function(locationId){
		$scope.locationValue = locationId.id;
		$scope.getPerformanceOfUser(0);
		$scope.getSalesDataValue($scope.locationValue);
	}
	
	angular.forEach($scope.tasksValue, function(value, key) {
		value.updated.value = $filter('date')(value.updated.value,"dd/MM/yyyy")
	});
	$scope.eventValue = events;
	  $scope.stringArray = [];
	  $scope.visitiorListMap = [];
	  var lengthValue = 0;
	  var valueCount = 0;
	  $scope.gridOptions = {};
      $scope.$on('$viewContentLoaded', function () {
    	  
    	  var startDate =  $("#vstartDate").val();
		   var endDate = $("#vendDate").val();
    	  
		   apiserviceDashborad.getVisitorList($scope.startDateV,$scope.endDateV).then(function(data){
			   
  			$scope.gridOptions.data = data;
  			$scope.visitiorList = data;
  			angular.forEach($scope.visitiorList, function(value, key) {
  				$scope.stringArray[value.geolocation] = {
  	    	            "flag" : 0,
  	    				"name" : value.geolocation
  	    	        };
  			});
  			
  			
  			var ab = 0;
  			angular.forEach($scope.visitiorList, function(value, key) {
  				
  					if($scope.stringArray[value.geolocation].name == value.geolocation && $scope.stringArray[value.geolocation].flag == 0){
  						$scope.visitiorListMap[ab] = value;
  						$scope.stringArray[value.geolocation].flag = 1;
  						$scope.stringArray[value.geolocation].value = 1;
  						ab = ab +1;
  					}else{
  						$scope.stringArray[value.geolocation].value = $scope.stringArray[value.geolocation].value + 1;
  					}
  			});
  			
  			
  			
  			angular.forEach($scope.visitiorListMap, function(value, key) {
  				if($scope.stringArray[value.geolocation].name == value.geolocation){
  					value.valueCount = $scope.stringArray[value.geolocation].value;
  				}
  			});
  			
  			dashboardService.init($scope.visitiorListMap);
            pluginsService.init();
            dashboardService.setHeights()
            if ($('.widget-weather').length) {
				if($scope.userProfile == null){
					widgetWeather("New York");
				}else{
					widgetWeather($scope.userProfile.address);
				}
                
            }
            handleTodoList();
  		});
    	  
    	  
      });
      
      $scope.msgShow = 0;
      
      /*------------------------financial-charts----------------------------------*/
      $scope.showvehical = 0;
      $scope.showBarvehical = 1;
      
      $scope.showVehicalBarChart = function(volumeStatStartDateId,volumeStatEndDateId){
    	  $scope.showvehical = 0;
    	  $scope.showBarvehical = 1;
    	  if(volumeStatStartDateId == undefined || volumeStatEndDateId == undefined ){
    		  volumeStatStartDateId = $('#volumeStatStartDateId').val();
    		  volumeStatEndDateId = $('#volumeStatEndDateId').val();
    	  }
    	  if(volumeStatStartDateId != '' && volumeStatEndDateId != ''){
    		  
    		  apiserviceDashborad.getSoldVehicleDetails(volumeStatStartDateId,volumeStatEndDateId).then(function(data){
    		 
    	   		$scope.locationDataList = data;	
    	       if(data.length == 0){
    	    	   $scope.msgShow = 1;
    	       }else{
    	    	   $scope.msgShow = 0;
    	       }
    	   		
    	         var items = Array($scope.locationDataList);
    	         var randomData = $scope.locationDataList;
    	        
    	         $('#bar-chartVehicle').highcharts('StockChart', {
    	             chart: {
    	                 alignTicks: false,
    	                 height: 230,
    	                 borderColor: '#C9625F',
    	                 backgroundColor: 'transparent',
    	                 spacingTop: 0,
    	                 spacingBottom: 5,
    	                 spacingLeft: 0,
    	                 spacingRight: 0
    	             },
    	             rangeSelector: {
    	                 inputEnabled: false,
    	                 selected: 2
    	             },
    	             credits: {
    	                 enabled: false
    	             },
    	             exporting: {
    	                 enabled: false
    	             },
    	             scrollbar: {
    	                 enabled: false
    	             },
    	             navigator: {
    	                 enabled: false
    	             },
    	             colors: ['rgba(128, 133, 233,0.8)'],
    	             xAxis: {
    	                 lineColor: '#e1e1e1',
    	                 tickColor: '#EFEFEF'
    	             },
    	             yAxis: {
    	                 gridLineColor: '#e1e1e1'
    	             },
    	             series: [
    	                 {
    	                     type: 'column',
    	                     name: 'Sales Volume',
    	                     data: randomData,
    	                     dataGrouping: {
    	                         units: [
    	                             [
    	                                 'week', // unit name
    	                                 [1] // allowed multiples
    	                             ], [
    	                                 'month',
    	                                 [1, 2, 3, 4, 5, 6]
    	                             ]
    	                         ]
    	                     }
    	                 }
    	             ]
    	         });
    	     });
    	  }
    	   
      }
      
    
      $scope.arrayname = [];
      
      $scope.showVehicalFinancialChartByBodyStyle = function(){
    	  
    	  var volumeStatStartDateId = $('#volumeStatStartDateId').val();
		  var volumeStatEndDateId = $('#volumeStatEndDateId').val();
		  
    	  $scope.showBarvehical = 0;
    	  $scope.showvehical = 1;
    	  apiserviceDashborad.getFinancialVehicleDetailsByBodyStyle(volumeStatStartDateId,volumeStatEndDateId).then(function(data){
    		  
    	  		$scope.msgShow = 0;
    	  		/*angular.forEach(data, function(value, key) {
    	  			if(value.data.length != 0){
    	  				$scope.msgShow = 0;
    	  			}
    	  		});*/
    	  		console.log(data);
    	  		 createChart(data);
  			});
    	  
    	  
      }
      
      $scope.showVehicalFinancialChart = function(){
    	  $scope.showBarvehical = 0;
    	  $scope.showvehical = 1;
    	  
    	  var volumeStatStartDateId = $('#volumeStatStartDateId').val();
		  var volumeStatEndDateId = $('#volumeStatEndDateId').val();
		  apiserviceDashborad.getFinancialVehicleDetails(volumeStatStartDateId,volumeStatEndDateId).then(function(data){
			  
		  		$scope.msgShow = 1;
    	  		angular.forEach(data.data, function(value, key) {
    	  			if(value.data.length != 0){
    	  				$scope.msgShow = 0;
    	  			}
    	  		});
    	  		
    	  		/*if(data.length == 0){
    	     	   $scope.msgShow = 1;
    	        }else{
    	     	   $scope.msgShow = 0;
    	        }*/
    	  		 createChart(data);
  			});
    	  
      }
      
      var seriesOptions = [];
      var seriesCounter = 0;
      var stockChart; 
      var stockChart1; 
      function createChart1(initdata) {
    	  stockChart1 = 1;
    	  
    	  stockChart = $('#financial-chart').highcharts({
    		  title: {
  	            text: '',
  	            x: -20 //center
  	        },
              chart: {
                  height: 300,
                  borderColor: '#C9625F',
                  backgroundColor: 'transparent'
              },
              rangeSelector: {
                  selected: 1,
                  inputEnabled: $('#container').width() > 480
              },
              colors: ['#18A689', '#f7a35c', '#8085e9', '#f15c80', '#91e8e1'],
              credits: {
                  enabled: false
              },
              exporting: {
                  enabled: false
              },
              scrollbar: {
                  enabled: false
              },
              navigator: {
                  enabled: false
              },
              xAxis: {
                  lineColor: '#e1e1e1',
                  tickColor: '#EFEFEF',
                  
              },
              yAxis: {
                  gridLineColor: '#e1e1e1',
                  labels: {
                      formatter: function () {
                          return (this.value > 0 ? ' + ' : '') + this.value;
                      }
                  },
                  plotLines: [{
                      value: 0,
                      width: 2,
                      color: 'silver'
                  }],
                  tooltip: {
                      pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b><br/>',
                      valueDecimals: 0
                  }
              },              
              plotOptions: {
	            	  series: {
	                      compare: 'percent'
	                  },
            	    line: {
            	        marker: {
            	            enabled: true,
            	            radius: 4
            	        }
            	    }
            	},
                
            	legend: {
    	            layout: 'vertical',
    	            align: 'right',
    	            verticalAlign: 'middle',
    	            borderWidth: 0
    	        },
              series: initdata
          });
      };
     
      
      function createChart(initdata) {
    	  stockChart1 = 1;
    	  
    	  stockChart = $('#financial-chart').highcharts({
    		  title: {
    	            text: '',
    	            x: -20 //center
    	        },
    	        xAxis: {
    	            categories: initdata.dates
    	        },
    	        yAxis: {
    	            title: {
    	                text: ''
    	            },
    	            plotLines: [{
    	                value: 0,
    	                width: 1,
    	                color: '#808080'
    	            }],
    	            min : 0
    	        },
    	        tooltip: {
    	            valueSuffix: ''
    	        },
    	        legend: {
    	            layout: 'vertical',
    	            align: 'right',
    	            verticalAlign: 'middle',
    	            borderWidth: 0
    	        },
    	        series: initdata.data
          });
      };
      
      /*-------------------------------------------------------------*/
      
      /*----------------Bar-Charts-------------------*/
      $scope.showVehicalBarAvgSale = function(){
    	  
    	  var volumeStatStartDateId = $('#volumeStatStartDateId').val();
		  var volumeStatEndDateId = $('#volumeStatEndDateId').val();
    	  
    	  $scope.showBarvehical = 1;
    	  $scope.showvehical = 0;
    	  
    	  apiserviceDashborad.getSoldVehicleDetailsAvgSale(volumeStatStartDateId,volumeStatEndDateId).then(function(data){
    		  
  			$scope.locationDataList = data;	
  			if(data.length == 0){
	     	   $scope.msgShow = 1;
	        }else{
	     	   $scope.msgShow = 0;
	        }
  		
        var items = Array($scope.locationDataList);
        var randomData = $scope.locationDataList;
       
        $('#bar-chartVehicle').highcharts('StockChart', {
            chart: {
                alignTicks: false,
                height: 230,
                borderColor: '#C9625F',
                backgroundColor: 'transparent',
                spacingTop: 0,
                spacingBottom: 5,
                spacingLeft: 0,
                spacingRight: 0
            },
            rangeSelector: {
                inputEnabled: false,
                selected: 2
            },
            credits: {
                enabled: false
            },
            exporting: {
                enabled: false
            },
            scrollbar: {
                enabled: false
            },
            navigator: {
                enabled: false
            },
            colors: ['rgba(128, 133, 233,0.8)'],
            xAxis: {
                lineColor: '#e1e1e1',
                tickColor: '#EFEFEF'
            },
            yAxis: {
                gridLineColor: '#e1e1e1'
            },
            series: [
                {
                    type: 'column',
                    name: 'Sales Volume',
                    data: randomData,
                    dataGrouping: {
                        units: [
                            [
                                'week', // unit name
                                [1] // allowed multiples
                            ], [
                                'month',
                                [1, 2, 3, 4, 5, 6]
                            ]
                        ]
                    }
                }
            ]
        });
    });
      }
      
      
      
      /*$http.get('/getSoldVehicleDetails')
		.success(function(data) {
		$scope.locationDataList = data;	
    
		
      var items = Array($scope.locationDataList);
      var randomData = $scope.locationDataList;
     
      $('#bar-chartVehicle').highcharts('StockChart', {
          chart: {
              alignTicks: false,
              height: 230,
              borderColor: '#C9625F',
              backgroundColor: 'transparent',
              spacingTop: 0,
              spacingBottom: 5,
              spacingLeft: 0,
              spacingRight: 0
          },
          rangeSelector: {
              inputEnabled: false,
              selected: 2
          },
          credits: {
              enabled: false
          },
          exporting: {
              enabled: false
          },
          scrollbar: {
              enabled: false
          },
          navigator: {
              enabled: false
          },
          colors: ['rgba(128, 133, 233,0.8)'],
          xAxis: {
              lineColor: '#e1e1e1',
              tickColor: '#EFEFEF'
          },
          yAxis: {
              gridLineColor: '#e1e1e1'
          },
          series: [
              {
                  type: 'column',
                  name: 'Sales Volume',
                  data: randomData,
                  dataGrouping: {
                      units: [
                          [
                              'week', // unit name
                              [1] // allowed multiples
                          ], [
                              'month',
                              [1, 2, 3, 4, 5, 6]
                          ]
                      ]
                  }
              }
          ]
      });
  });*/
      
      /*----------------------*/

      
      $scope.activeTab = true;

      $scope.gridOptions13 = {
  	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
  	 		    paginationPageSize: 150,
  	 		    enableFiltering: true,
  	 		    useExternalFiltering: true,
  	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
  	 		 };
  	 		 $scope.gridOptions13.enableHorizontalScrollbar = 2;
  	 		 $scope.gridOptions13.enableVerticalScrollbar = 2;
  	 		 $scope.gridOptions13.columnDefs = [
  	 		                                 { name: 'make', displayName: 'Make', width:'14%',cellEditableCondition: false,
  	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.make}}</a> ',
  	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  	 		                                       if (row.entity.noteFlag != 1) {
  	 		                                         return 'red';
  	 		                                       }
  	 		                                	} ,
  	 		                                 },
  	 		                                 { name: 'model', displayName: 'model', width:'8%',cellEditableCondition: false,
  	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.model}}</a> ',
  	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  	  		                                       if (row.entity.noteFlag != 1) {
  	  		                                         return 'red';
  	  		                                     }
  	 		                                	} ,
  	 		                                 },
  	 		                                 { name: 'name', displayName: 'Name', width:'10%',cellEditableCondition: false,
  	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.name}}</a> ',
  	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  	  		                                       if (row.entity.noteFlag != 1) {
  	  		                                         return 'red';
  	  		                                     }
  	 		                                	} ,
  	 		                                 },
  	 		                                 { name: 'phone', displayName: 'Phone', width:'7%',cellEditableCondition: false,
  	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.phone}}</a> ',
  	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  	  		                                       if (row.entity.noteFlag != 1) {
  	  		                                         return 'red';
  	  		                                     }
  	 		                                	} ,
  	 		                                 },
  	 		                                 { name: 'email', displayName: 'Email', width:'9%',cellEditableCondition: false,
  	 		                                	cellTemplate:'<a  href="mailto:{{row.entity.email}}">{{row.entity.email}}</a> ',
  	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  	  		                                       if (row.entity.noteFlag != 1) {
  	  		                                         return 'red';
  	  		                                     }
  	 		                                	} ,
  	 		                                 },
  	 		                                { name: 'requestDate', displayName: 'Date Added', width:'5%',cellEditableCondition: false,
  	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.requestDate}}</a> ',
 	 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
 	 		   		                                       if (row.entity.noteFlag != 1) {
 	 		   		                                         return 'red';
 	 		   		                                     }
 	 		  		                                	} ,
 	 		 		                                 },
  	 		                                
  	     		                                 ];
 			
  	 		$scope.gridOptions13.onRegisterApi = function(gridApi){
  				 $scope.gridApi = gridApi;
  				 
  		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
  			          var grid = this.grid;
  			          $scope.gridOptions13.data = $filter('filter')($scope.AllOtherLeadSeenList,{'make':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'name':grid.columns[2].filters[0].term,'phone':grid.columns[3].filters[0].term,'email':grid.columns[4].filters[0].term,'requestDate':grid.columns[5].filters[0].term},undefined);
  			        });
  		   		
  	  		};
      
      
      $scope.gridOptions5 = {
    	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    	 		    paginationPageSize: 150,
    	 		    enableFiltering: true,
    	 		    useExternalFiltering: true,
    	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
    	 		 };
    	 		 $scope.gridOptions5.enableHorizontalScrollbar = 0;
    	 		 $scope.gridOptions5.enableVerticalScrollbar = 2;
    	 		 $scope.gridOptions5.columnDefs = [
    	 		                                 { name: 'vin', displayName: 'Vin', width:'10%',cellEditableCondition: false,
    	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.vin}}</a> ',
    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    	 		                                       if (row.entity.noteFlag != 1) {
    	 		                                         return 'red';
    	 		                                       }
    	 		                                	} ,
    	 		                                 },
    	 		                                 { name: 'model', displayName: 'Model', width:'7%',cellEditableCondition: false,
    	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.model}}</a> ',
    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    	  		                                       if (row.entity.noteFlag != 1) {
    	  		                                         return 'red';
    	  		                                     }
    	 		                                	} ,
    	 		                                 },
    	 		                                 { name: 'make', displayName: 'Make', width:'8%',cellEditableCondition: false,
    	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.make}}</a> ',
    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    	  		                                       if (row.entity.noteFlag != 1) {
    	  		                                         return 'red';
    	  		                                     }
    	 		                                	} ,
    	 		                                 },
    	 		                                 { name: 'name', displayName: 'Name', width:'10%',cellEditableCondition: false,
    	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.name}}</a> ',
    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    	  		                                       if (row.entity.noteFlag != 1) {
    	  		                                         return 'red';
    	  		                                     }
    	 		                                	} ,
    	 		                                 },
    	 		                                 { name: 'phone', displayName: 'Phone', width:'7%',cellEditableCondition: false,
    	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.phone}}</a> ',
    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    	  		                                       if (row.entity.noteFlag != 1) {
    	  		                                         return 'red';
    	  		                                     }
    	 		                                	} ,
    	 		                                 },
    	 		                                 { name: 'email', displayName: 'Email', width:'9%',cellEditableCondition: false,
    	 		                                	cellTemplate:'<a  href="mailto:{{row.entity.email}}">{{row.entity.email}}</a> ',
    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    	  		                                       if (row.entity.noteFlag != 1) {
    	  		                                         return 'red';
    	  		                                     }
    	 		                                	} ,
    	 		                                 },
    	 		                                { name: 'requestDate', displayName: 'Date Added', width:'5%',cellEditableCondition: false,
    	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.requestDate}}</a> ',
	 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	 		   		                                       if (row.entity.noteFlag != 1) {
	 		   		                                         return 'red';
	 		   		                                     }
	 		  		                                	} ,
	 		 		                                 },
    	 		                                { name: 'btnSold', displayName: '',enableFiltering: false, width:'45%',cellEditableCondition: false,
			 		                                	cellTemplate:'<button type="button" ng-click="grid.appScope.completeRequestStatus(row.entity)" class="btn btn-sm btn-primary "  ng-show="grid.appScope.userType != \'\'" style="margin-left:3%;">SOLD</button><button type="button" ng-click="grid.appScope.cancelRequestStatus(row.entity)" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">CANCEL</button><button type="button" ng-click="grid.appScope.addNoteToRequestUser(row.entity,\'requestMore\')" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">HISTORY</button><button type="button" ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,1)" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">SCHEDULE</button><button type="button" ng-click="grid.appScope.createContact(row.entity)" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">ADD TO CLIENTELE</button><button ng-show="grid.appScope.userType == \'Manager\'" type="button" ng-click="grid.appScope.assignCanceledLead(row.entity)" class="btn btn-sm btn-primary" style="margin-left:0%;">ASSIGN</button>',
			 		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			   		                                       if (row.entity.noteFlag != 1) {
			   		                                         return 'red';
			   		                                     }
			  		                                	} ,
			 		                                 },
    	     		                                 ];
    	 		 
    	 		$scope.gridOptions8 = {
    	    	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    	    	 		    paginationPageSize: 150,
    	    	 		    enableFiltering: true,
    	    	 		    useExternalFiltering: true,
    	    	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
    	    	 		 };
    	    	 		 $scope.gridOptions8.enableHorizontalScrollbar = 0;
    	    	 		 $scope.gridOptions8.enableVerticalScrollbar = 2;
    	    	 		 $scope.gridOptions8.columnDefs = [
    	    	 		                                 { name: 'vin', displayName: 'Vin', width:'10%',cellEditableCondition: false,
    	    	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.vin}}</a> ',
    	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    	    	 		                                       if (row.entity.noteFlag != 1) {
    	    	 		                                         return 'red';
    	    	 		                                       }
    	    	 		                                	} ,
    	    	 		                                 },
    	    	 		                                 { name: 'model', displayName: 'Model', width:'7%',cellEditableCondition: false,
    	    	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.model}}</a> ',
    	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    	    	  		                                       if (row.entity.noteFlag != 1) {
    	    	  		                                         return 'red';
    	    	  		                                     }
    	    	 		                                	} ,
    	    	 		                                 },
    	    	 		                                 { name: 'make', displayName: 'Make', width:'8%',cellEditableCondition: false,
    	    	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.make}}</a> ',
    	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    	    	  		                                       if (row.entity.noteFlag != 1) {
    	    	  		                                         return 'red';
    	    	  		                                     }
    	    	 		                                	} ,
    	    	 		                                 },
    	    	 		                                 { name: 'name', displayName: 'Name', width:'10%',cellEditableCondition: false,
    	    	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.name}}</a> ',
    	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    	    	  		                                       if (row.entity.noteFlag != 1) {
    	    	  		                                         return 'red';
    	    	  		                                     }
    	    	 		                                	} ,
    	    	 		                                 },
    	    	 		                                 { name: 'phone', displayName: 'Phone', width:'7%',cellEditableCondition: false,
    	    	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.phone}}</a> ',
    	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    	    	  		                                       if (row.entity.noteFlag != 1) {
    	    	  		                                         return 'red';
    	    	  		                                     }
    	    	 		                                	} ,
    	    	 		                                 },
    	    	 		                                 { name: 'email', displayName: 'Email', width:'9%',cellEditableCondition: false,
    	    	 		                                	cellTemplate:'<a  href="mailto:{{row.entity.email}}">{{row.entity.email}}</a> ',
    	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    	    	  		                                       if (row.entity.noteFlag != 1) {
    	    	  		                                         return 'red';
    	    	  		                                     }
    	    	 		                                	} ,
    	    	 		                                 },
    	    	 		                                { name: 'requestDate', displayName: 'Date Added', width:'5%',cellEditableCondition: false,
    	    	 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.requestDate}}</a> ',
    		 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		 		   		                                       if (row.entity.noteFlag != 1) {
    		 		   		                                         return 'red';
    		 		   		                                     }
    		 		  		                                	} ,
    		 		 		                                 },
    	    	 		                                { name: 'btnSold', displayName: '',enableFiltering: false, width:'45%',cellEditableCondition: false,
    				 		                                	cellTemplate:'<button type="button" ng-click="grid.appScope.completeRequestStatus(row.entity)" class="btn btn-sm btn-primary " ng-disabled="row.entity.vin == null" ng-show="grid.appScope.userType != \'\'" style="margin-left:3%;">SOLD</button><button type="button" ng-click="grid.appScope.cancelRequestStatus(row.entity)" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">CANCEL</button><button type="button" ng-click="grid.appScope.addNoteToRequestUser(row.entity,\'requestMore\')" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">HISTORY</button><button type="button" ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,1)" ng-show="grid.appScope.userType != \'\'" ng-disabled="row.entity.vin == null" class="btn btn-sm btn-primary" style="margin-left:0px;">SCHEDULE</button><button type="button" ng-click="grid.appScope.createContact(row.entity)" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">ADD TO CLIENTELE</button><button ng-show="grid.appScope.userType == \'Manager\'" type="button" ng-click="grid.appScope.assignCanceledLead(row.entity)" class="btn btn-sm btn-primary" style="margin-left:0%;">ASSIGN</button>',
    				 		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    				   		                                       if (row.entity.noteFlag != 1) {
    				   		                                         return 'red';
    				   		                                     }
    				  		                                	} ,
    				 		                                 },
    	    	     		                                 ];
    	 		
    			 $scope.gridOptions2 = {
     			 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
     			 		    paginationPageSize: 150,
     			 		    enableFiltering: true,
     			 		    useExternalFiltering: true,
     			 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
     			 		 };
     			 		 $scope.gridOptions2.enableHorizontalScrollbar = 0;
     			 		 $scope.gridOptions2.enableVerticalScrollbar = 2;
     			 		 $scope.gridOptions2.columnDefs = [
     			 		                                 { name: 'vin', displayName: 'Vin', width:'5%',cellEditableCondition: false,
     			 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.vin}}</a> ',
     			 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			   		                                       if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     			   		                                         return 'red';
     			   		                                     }
     			  		                                	} ,
     			 		                                 },
     			 		                                 { name: 'model', displayName: 'Model', width:'6%',cellEditableCondition: false,
     			 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.model}}</a> ',
     			 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			   		                                       if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     			   		                                         return 'red';
     			   		                                     }
     			  		                                	} ,
     			 		                                 },
     			 		                                 { name: 'make', displayName: 'Make', width:'6%',cellEditableCondition: false,
     			 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.make}}</a> ',
     			 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			   		                                       if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     			   		                                         return 'red';
     			   		                                     }
     			  		                                	} ,
     			 		                                 },
     			 		                                 
     			 		                                 { name: 'name', displayName: 'Name', width:'6%',cellEditableCondition: false,
     			 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.name}}</a> ',
     					                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     				  		                                       if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     				  		                                         return 'red';
     				  		                                     }
     				 		                                	} ,
     			 		                                 },
     			 		                                 { name: 'phone', displayName: 'Phone', width:'6%',cellEditableCondition: false,
     			 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.phone}}</a> ',
     			 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			   		                                       if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     			   		                                         return 'red';
     			   		                                     }
     			  		                                	} ,
     			 		                                 },
     			 		                                 { name: 'email', displayName: 'Email', width:'7%',cellEditableCondition: false,
     			 		                                	cellTemplate:'<a  href="mailto:{{row.entity.email}}">{{row.entity.email}}</a> ',
     			 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			   		                                       if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     			   		                                         return 'red';
     			   		                                     }
     			  		                                	} ,
     			 		                                 },
     			 		                                 /*{ name: 'confirmDate', displayName: 'Confirm Day', width:'8%',cellEditableCondition: false,
     			 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.confirmDate}}</a> ',
     			 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			   		                                       if (row.entity.confirmDate === null && row.entity.note.length <= 1) {
     			   		                                         return 'red';
     			   		                                     }
     			  		                                	} ,
     					                                 },
     					                                 { name: 'confirmTime', displayName: 'Confirm Time', width:'8%',cellEditableCondition: false,
     					                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.confirmTime}}</a> ',
     					                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     				  		                                       if (row.entity.confirmDate === null && row.entity.note.length <= 1) {
     				  		                                         return 'red';
     				  		                                     }
     				 		                                	} ,
     			 		                                 },*/
     			 		                               { name: 'bestDay', displayName: 'Date Added', width:'8%',cellEditableCondition: false,
     			 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.bestDay}}</a> ',
      			 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
      			   		                                       if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
      			   		                                         return 'red';
      			   		                                     }
      			  		                                	} ,
      					                                 },
      					                                 { name: 'bestTime', displayName: 'Requested Time', width:'8%',cellEditableCondition: false,
      					                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.bestTime}}</a> ',
      					                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
      				  		                                       if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
      				  		                                         return 'red';
      				  		                                     }
      				 		                                	} ,
      			 		                                 },
     			 		                                { name: 'isRead', displayName: 'Confirm',enableFiltering: false, width:'10%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
     			 		                                	 cellTemplate:'<div class="icheck-list"ng-show="grid.appScope.userType != \'\'" ></div><button type="button" ng-click="grid.appScope.confirmDateTime(row.entity)"ng-show="grid.appScope.userType != \'\'"ng-show="row.entity.isRead" data-toggle="modal" data-target="#modal-basic" class="btn btn-sm btn-primary" style="margin-top:2%;" ng-click="confres()">Confirm/Reschedule</button>', 
     			 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			  		                                       if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     			  		                                         return 'red';
     			  		                                     }
     			 		                                	} ,
     			 		                                 },
     			 		                               { name: 'btnSold', displayName: '',enableFiltering: false, width:'30%',cellEditableCondition: false,
      			 		                                	cellTemplate:'<button type="button" ng-click="grid.appScope.soldScheduleStatus(row.entity)" ng-show="grid.appScope.userType != \'\'"class="btn btn-sm btn-primary" style="margin-left:3%;">SOLD</button><button type="button" ng-click="grid.appScope.cancelScheduleStatus(row.entity)" ng-show="grid.appScope.userType != \'\'"class="btn btn-sm btn-primary" style="margin-left:0px;">CANCEL</button><button type="button" ng-click="grid.appScope.addNoteToRequestUser(row.entity,\'scheduleTest\')" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">HISTORY</button><button type="button" ng-click="grid.appScope.createContact(row.entity)" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">ADD TO CLIENTELE</button><button type="button" ng-click="grid.appScope.assignCanceledLead(row.entity)" class="btn btn-sm btn-primary" style="margin-left:0%;">ASSIGN</button>',
      			 		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
       			   		                                       if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
       			   		                                         return 'red';
       			   		                                     }
       			  		                                	} ,
       			 		                                 },
     			     		                                 ];
    	 		 
     			 		 
     			 		$scope.gridOptions3 = {
     			 		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
     			 		 		    paginationPageSize: 150,
     			 		 		    enableFiltering: true,
     			 		 		    useExternalFiltering: true,
     			 		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
     			 		 		 };
     			 		 		 $scope.gridOptions3.enableHorizontalScrollbar = 0;
     			 		 		 $scope.gridOptions3.enableVerticalScrollbar = 2;
     			 		 		 $scope.gridOptions3.columnDefs = [
     			 		 		                                 { name: 'vin', displayName: 'Vin', width:'8%',cellEditableCondition: false,
     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.vin}}</a> ',
     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			 		 		                                	 if (row.entity.noteFlag != 1) {
     			 		   		                                         return 'red';
     			 		   		                                     }
     			 		  		                                	} ,
     			 		 		                                 },
     			 		 		                                 { name: 'model', displayName: 'Model', width:'8%',cellEditableCondition: false,
     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.model}}</a> ',
     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			 		 		                                	 if (row.entity.noteFlag != 1) {
     			 		   		                                         return 'red';
     			 		   		                                     }
     			 		  		                                	} ,
     			 		 		                                 },
     			 		 		                                 { name: 'make', displayName: 'Make', width:'9%',cellEditableCondition: false,
     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.make}}</a> ',
     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			 		 		                                	 if (row.entity.noteFlag != 1) {
     			 		   		                                         return 'red';
     			 		   		                                     }
     			 		  		                                	} ,
     			 		 		                                 },
     			 		 		                                 { name: 'name', displayName: 'Name', width:'8%',cellEditableCondition: false,
     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.name}}</a> ',
     			 				                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			 				                                		 if (row.entity.noteFlag != 1) {
     			 			  		                                         return 'red';
     			 			  		                                     }
     			 			 		                                	} ,
     			 		 		                                 },
     			 		 		                                 { name: 'phone', displayName: 'Phone', width:'7%',cellEditableCondition: false,
     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.phone}}</a> ',
     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			 		 		                                	 if (row.entity.noteFlag != 1) {
     			 		   		                                         return 'red';
     			 		   		                                     }
     			 		  		                                	} ,
     			 		 		                                 },
     			 		 		                                 { name: 'email', displayName: 'Email', width:'10%',cellEditableCondition: false,
     			 		 		                                	cellTemplate:'<a  href="mailto:{{row.entity.email}}">{{row.entity.email}}</a> ',
     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			 		 		                                	 if (row.entity.noteFlag != 1) {
     			 		   		                                         return 'red';
     			 		   		                                     }
     			 		  		                                	} ,
     			 		 		                                 },
     			 		 		                              { name: 'requestDate', displayName: 'Date Added', width:'8%',cellEditableCondition: false,
     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.requestDate}}</a> ',
     			 		 		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			 		 		                                		 if (row.entity.noteFlag != 1) {
      			 		   		                                         return 'red';
      			 		   		                                     }
      			 		  		                                	} ,
      			 		 		                                 },
     			 		 		                                			 		 		                                 
     			 		 		                                { name: 'edit', displayName: '', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
     			 		    		                                 cellTemplate:' <a ng-click="grid.appScope.getTradeData(row)" href="/showPdf/{{row.entity.id}}" data-title="A new page" target="_blank" style="margin-top:7px;margin-left:6px;" >View</a>',
     			 		    		                                 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     			 		    		                                	 if (row.entity.noteFlag != 1) {
     			 		    		                                         return 'red';
     			 		    		                                     }
     			 		   		                                	} ,
     			 				                                 
     			 				                                 },
     			 				                                 
     			 				                               { name: 'btnSold', displayName: '',enableFiltering: false, width:'40%',cellEditableCondition: false,
     	      			 		                                	cellTemplate:'<button type="button" ng-click="grid.appScope.completeTradeInStatus(row.entity)" class="btn btn-sm btn-primary" ng-show="grid.appScope.userType != \'\'"style="margin-left:3%;">SOLD</button><button type="button" ng-show="grid.appScope.userType != \'\'"ng-click="grid.appScope.cancelTradeInStatus(row.entity)" class="btn btn-sm btn-primary" style="margin-left:0px;">CANCEL</button><button type="button" ng-click="grid.appScope.addNoteToRequestUser(row.entity,\'tradeIn\')" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">HISTORY</button><button type="button" ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,2)" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">SCHEDULE</button><button type="button" ng-click="grid.appScope.createContact(row.entity)" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">ADD TO CLIENTELE</button><button ng-show="grid.appScope.userType == \'Manager\'" type="button" ng-click="grid.appScope.assignCanceledLead(row.entity)" class="btn btn-sm btn-primary" style="margin-left:0%;">ASSIGN</button>',
     	      			 		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	      			 		                                	 if (row.entity.noteFlag != 1) {
     	       			   		                                         return 'red';
     	       			   		                                     }
     	       			  		                                	} ,
     	       			 		                                 },
     			 		     		                                 ];
     			 		  
     			 		 		$scope.gridOptions4 = {
     	     			 		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
     	     			 		 		    paginationPageSize: 150,
     	     			 		 		    enableFiltering: true,
     	     			 		 		    useExternalFiltering: true,
     	     			 		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
     	     			 		 		 };
     	     			 		 		 $scope.gridOptions4.enableHorizontalScrollbar = 0;
     	     			 		 		 $scope.gridOptions4.enableVerticalScrollbar = 2;
     	     			 		 		 $scope.gridOptions4.columnDefs = [
     	     			 		 		                                 { name: 'vin', displayName: 'Vin', width:'10%',cellEditableCondition: false,
     	     			 		 		                                	
     	     			 		 		                                 },
     	     			 		 		                                 { name: 'model', displayName: 'Model', width:'8%',cellEditableCondition: false,
     	     			 		 		                                	
     	     			 		 		                                 },
     	     			 		 		                                 { name: 'make', displayName: 'Make', width:'8%',cellEditableCondition: false,
     	     			 		 		                                	
     	     			 		 		                                 },
     	     			 		 		                                 { name: 'name', displayName: 'Name', width:'9%',cellEditableCondition: false,
     	     			 				                                	 
     	     			 		 		                                 },
     	     			 		 		                                 { name: 'phone', displayName: 'Phone', width:'7%',cellEditableCondition: false,
     	     			 		 		                                	
     	     			 		 		                                 },
     	     			 		 		                                 { name: 'email', displayName: 'Email', width:'8%',cellEditableCondition: false,
     	     			 		 		                                	cellTemplate:'<a  href="mailto:{{row.entity.email}}">{{row.entity.email}}</a> ',
     	     			 		 		                                	
     	     			 		 		                                 },
     	     			 		 		                                 
     	     			 		 		                                { name: 'salesRep', displayName: 'Sales Rep', width:'8%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
     	     			 		    		                                 
     	     			 				                                 
     	     			 				                                 },
     	     			 				                              { name: 'leadType', displayName: 'Lead', width:'9%', cellEditableCondition: false, 
     	     			 				                                 
     	     			 				                                 },
     	     			 				                              { name: 'status', displayName: 'Reason', width:'6%', cellEditableCondition: false, 
     	     			 		    		                                 
     	     			 				                                 },
     	     			 				                              { name: 'statusDate', displayName: 'Date added', width:'6%', cellEditableCondition: false,
     	     			 		    		                                 
  	     			 				                                 },
     	     			 				                              
     	     			 				                               { name: 'btnSold', displayName: '',enableFiltering: false, width:'24%',cellEditableCondition: false,
     	     	      			 		                                	cellTemplate:'<button type="button" ng-click="grid.appScope.assignCanceledLead(row.entity)" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:3%;">ASSIGN</button><button type="button" ng-click="grid.appScope.deleteForeverLead(row.entity)" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:0%;">DELETE</button><button type="button" ng-click="grid.appScope.restoreLead(row.entity)" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:0%;">RESTORE</button><button type="button" ng-click="grid.appScope.addNoteToRequestUser(row.entity,\'cansal\')" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:0%;">HISTORY</button>',
     	     	      			 		                                	 
     	     	       			 		                                 },
     	     			 		     		                                 ];
     	     			 		  
     			 		 		 
     	     			 		 	 $scope.gridOptions6 = {
     	     			 	    	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
     	     			 	    	 		    paginationPageSize: 150,
     	     			 	    	 		    enableFiltering: true,
     	     			 	    	 		    useExternalFiltering: true,
     	     			 	    	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
     	     			 	    	 		 };
     	     			 	    	 		 $scope.gridOptions6.enableHorizontalScrollbar = 0;
     	     			 	    	 		 $scope.gridOptions6.enableVerticalScrollbar = 2;
     	     			 	    	 		 $scope.gridOptions6.columnDefs = [
     	     			 	    	 		                                 { name: 'vin', displayName: 'Vin', width:'12%',cellEditableCondition: false,
     	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     			 	    	 		                                       if (row.entity.isRead === false) {
     	     			 	    	 		                                         return 'red';
     	     			 	    	 		                                       }
     	     			 	    	 		                                	} ,
     	     			 	    	 		                                 },
     	     			 	    	 		                                 { name: 'model', displayName: 'Model', width:'10%',cellEditableCondition: false,
     	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     			 	    	  		                                       if (row.entity.isRead === false) {
     	     			 	    	  		                                         return 'red';
     	     			 	    	  		                                     }
     	     			 	    	 		                                	} ,
     	     			 	    	 		                                 },
     	     			 	    	 		                                 { name: 'make', displayName: 'Make', width:'10%',cellEditableCondition: false,
     	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     			 	    	  		                                       if (row.entity.isRead === false) {
     	     			 	    	  		                                         return 'red';
     	     			 	    	  		                                     }
     	     			 	    	 		                                	} ,
     	     			 	    	 		                                 },
     	     			 	    	 		                                 { name: 'name', displayName: 'Name', width:'12%',cellEditableCondition: false,
     	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     			 	    	  		                                       if (row.entity.isRead === false) {
     	     			 	    	  		                                         return 'red';
     	     			 	    	  		                                     }
     	     			 	    	 		                                	} ,
     	     			 	    	 		                                 },
     	     			 	    	 		                                 { name: 'phone', displayName: 'Phone', width:'12%',cellEditableCondition: false,
     	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     			 	    	  		                                       if (row.entity.isRead === false) {
     	     			 	    	  		                                         return 'red';
     	     			 	    	  		                                     }
     	     			 	    	 		                                	} ,
     	     			 	    	 		                                 },
     	     			 	    	 		                                 { name: 'email', displayName: 'Email', width:'15%',cellEditableCondition: false,
     	     			 	    	 		                                	cellTemplate:'<a  href="mailto:{{row.entity.email}}">{{row.entity.email}}</a> ',
     	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     			 	    	  		                                       if (row.entity.isRead === false) {
     	     			 	    	  		                                         return 'red';
     	     			 	    	  		                                     }
     	     			 	    	 		                                	} ,
     	     			 	    	 		                                 },
     	     			 	    	 		                             { name: 'status', displayName: 'Status', width:'10%',cellEditableCondition: false,
      	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
      	     			 	    	  		                                       if (row.entity.isRead === false) {
      	     			 	    	  		                                         return 'red';
      	     			 	    	  		                                     }
      	     			 	    	 		                                	} ,
      	     			 	    	 		                                 },
      	     			 	    	 		                            { name: 'statusDate', displayName: 'Date added', width:'10%',cellEditableCondition: false,
       	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
       	     			 	    	  		                                       if (row.entity.isRead === false) {
       	     			 	    	  		                                         return 'red';
       	     			 	    	  		                                     }
       	     			 	    	 		                                	} ,
       	     			 	    	 		                                 },
      	     			 	    	 		                            
     	     			 	    	 		                             
     	     			 	    	 		                                { name: 'btnSold', displayName: '',enableFiltering: false, width:'12%',cellEditableCondition: false,
     	     			 				 		                                	cellTemplate:'<button type="button" ng-click="grid.appScope.addNoteToRequestUser(row.entity,\'cansal\')" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:0%;">HISTORY</button><button ng-show="grid.appScope.userType == \'Manager\'" type="button" ng-click="grid.appScope.assignCanceledLead(row.entity)" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:0%;">ASSIGN</button>',
     	     			 				 		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     			 				   		                                       if (row.entity.isRead === false) {
     	     			 				   		                                         return 'red';
     	     			 				   		                                     }
     	     			 				  		                                	} ,
     	     			 				 		                                 },
     	     			 	    	     		                                 ]; 
     	     			 	    	 		 
     	     			 	    	 		 
     	     			 	    	 		$scope.gridOptions7 = {
     	     		     			 		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
     	     		     			 		 		    paginationPageSize: 150,
     	     		     			 		 		    enableFiltering: true,
     	     		     			 		 		    useExternalFiltering: true,
     	     		     			 		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
     	     		     			 		 		 };
     	     		     			 		 		 $scope.gridOptions7.enableHorizontalScrollbar = 0;
     	     		     			 		 		 $scope.gridOptions7.enableVerticalScrollbar = 2;
     	     		     			 		 		 $scope.gridOptions7.columnDefs = [
																					{ name: 'name', displayName: 'Name', width:'8%',cellEditableCondition: false,
																					     	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.name}}</a> ',
																					    	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																					    		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
																					                   return 'red';
																					               }
																					         	} ,
																					      },
																					      { name: 'phone', displayName: 'Phone', width:'6%',cellEditableCondition: false,
																					     	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.phone}}</a> ',
																					     	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																					     		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
																					                return 'red';
																					            }
																					      	} ,
																					      },
																					      { name: 'email', displayName: 'Email', width:'10%',cellEditableCondition: false,
																					     	cellTemplate:'<a  href="mailto:{{row.entity.email}}">{{row.entity.email}}</a> ',
																					     	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																					     		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
																					                return 'red';
																					            }
																					      	} ,
																					      },
																					   { name: 'requestDate', displayName: 'Date Added', width:'7%',cellEditableCondition: false,
																					     	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.requestDate}}</a> ',
																					     	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																					     		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
																					                 return 'red';
																					             }
																					       	} ,
																					       },
     	     		     			 		 		                                 { name: 'vin', displayName: 'Vin', width:'9%',cellEditableCondition: false,
     	     		     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.vin}}</a> ',
     	     		     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     		     			 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     	     		     			 		   		                                         return 'red';
     	     		     			 		   		                                     }
     	     		     			 		  		                                	} ,
     	     		     			 		 		                                 },
     	     		     			 		 		                                 { name: 'model', displayName: 'Model', width:'6%',cellEditableCondition: false,
     	     		     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.model}}</a> ',
     	     		     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     		     			 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     	     		     			 		   		                                         return 'red';
     	     		     			 		   		                                     }
     	     		     			 		  		                                	} ,
     	     		     			 		 		                                 },
     	     		     			 		 		                                 { name: 'make', displayName: 'Make', width:'7%',cellEditableCondition: false,
     	     		     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.make}}</a> ',
     	     		     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     		     			 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     	     		     			 		   		                                         return 'red';
     	     		     			 		   		                                     }
     	     		     			 		  		                                	} ,
     	     		     			 		 		                                 },
     	     		     			 		 		                            { name: 'typeOfLead', displayName: 'type', width:'8%',cellEditableCondition: false,
      	     		     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.typeOfLead}}</a> ',
      	     		     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
      	     		     			 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
      	     		     			 		   		                                         return 'red';
      	     		     			 		   		                                     }
      	     		     			 		  		                                	} ,
      	     		     			 		 		                                 },
     	     		     			 		 		                                 
     	     		     			 		 		                                			 		 		                                 
     	     		     			 		 		                                { name: 'edit', displayName: '', width:'4%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
     	     		     			 		    		                                 cellTemplate:' <a ng-click="grid.appScope.getTradeData(row)" ng-show="row.entity.typeOfLead == \'Trade-In Appraisal\'" href="/showPdf/{{row.entity.id}}" data-title="A new page" target="_blank" style="margin-top:7px;margin-left:6px;" >View</a>',
     	     		     			 		    		                                 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     		     			 		    		                                 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     	     		     			 		    		                                         return 'red';
     	     		     			 		    		                                     }
     	     		     			 		   		                                	} ,
     	     		     			 				                                 
     	     		     			 				                                 },
     	     		     			 				                                 
     	     		     			 				                               { name: 'btnSold', displayName: '',enableFiltering: false, width:'40%',cellEditableCondition: false,        /* <button type="button" ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,2)" ng-show="grid.appScope.userType != \'\' && row.entity.confirmDate != null" class="btn btn-sm btn-primary" style="margin-left:0px;">RESCHEDULE</button><button type="button" ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,2)" ng-show="grid.appScope.userType != \'\' && row.entity.confirmDate == null" class="btn btn-sm btn-primary" style="margin-left:0px;">SCHEDULE</button>*/                                                                     
     	     		     	      			 		                                	cellTemplate:'<button type="button" ng-click="grid.appScope.soldScheduleStatus(row.entity)" class="btn btn-sm btn-primary" ng-show="grid.appScope.userType != \'\'"style="margin-left:3%;">SOLD</button> <button type="button" ng-show="grid.appScope.userType != \'\'"ng-click="grid.appScope.cancelScheduleStatus(row.entity)" class="btn btn-sm btn-primary" style="margin-left:0px;">CANCEL</button> <button type="button" ng-click="grid.appScope.addNoteToRequestUser(row.entity,\'tradeIn\')" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">HISTORY</button><button ng-show="grid.appScope.userType == \'Manager\'" type="button" ng-click="grid.appScope.assignCanceledLead(row.entity)" class="btn btn-sm btn-primary" style="margin-left:0%;">ASSIGN</button> <select ng-model="action" ng-change="grid.appScope.actionOnPdf(row.entity,action)" class="btn btn-sm btn-primary" style="text-transform :uppercase;height :25px"><option value="" >Actions</option><option ng-if="row.entity.confirmDate == null" value="Schedule">Schedule</option><option ng-if="row.entity.confirmDate != null" value="Rechedule" value="Reschedule">Reschedule</option> <option value="SendPdf">Send Pdf</option> <option value="clientele">Add to Clientele </option>  </select> ',   
     	     		     	      			 		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     		     	      			 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     	     		     	       			   		                                         return 'red';
     	     		     	       			   		                                     }
     	     		     	       			  		                                	} ,
     	     		     	       			 		                                 },
     	     		     			 		     		                                 ]; 
     	     			 	    	 		 
     	     		     			 		 	$scope.gridOptions9 = {
        	     		     			 		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
        	     		     			 		 		    paginationPageSize: 150,
        	     		     			 		 		    enableFiltering: true,
        	     		     			 		 		    useExternalFiltering: true,
        	     		     			 		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
        	     		     			 		 		 };
        	     		     			 		 		 $scope.gridOptions9.enableHorizontalScrollbar = 0;
        	     		     			 		 		 $scope.gridOptions9.enableVerticalScrollbar = 2;
        	     		     			 		 		 $scope.gridOptions9.columnDefs = [
        	     		     			 		 		                                   
        	     		     			 		 		                               { name: 'name', displayName: 'Name', width:'9%',cellEditableCondition: false,
       	     		     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,2)" style="color: #5b5b5b;">{{row.entity.name}}</a> ',
       	     		     			 				                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
       	     		     			 			  		                                       if (row.entity.isRead === false) {
       	     		     			 			  		                                         return 'red';
       	     		     			 			  		                                     }
       	     		     			 			 		                                	} ,
       	     		     			 		 		                                 },
       	     		     			 		 		                                 { name: 'phone', displayName: 'Phone', width:'7%',cellEditableCondition: false,
       	     		     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,2)" style="color: #5b5b5b;">{{row.entity.phone}}</a> ',
       	     		     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
       	     		     			 		   		                                       if (row.entity.isRead === false) {
       	     		     			 		   		                                         return 'red';
       	     		     			 		   		                                     }
       	     		     			 		  		                                	} ,
       	     		     			 		 		                                 },
       	     		     			 		 		                                 { name: 'email', displayName: 'Email', width:'12%',cellEditableCondition: false,
       	     		     			 		 		                                	cellTemplate:'<a  href="mailto:{{row.entity.email}}">{{row.entity.email}}</a> ',
       	     		     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
       	     		     			 		   		                                       if (row.entity.isRead === false) {
       	     		     			 		   		                                         return 'red';
       	     		     			 		   		                                     }
       	     		     			 		  		                                	} ,
       	     		     			 		 		                                 },
        	     		     			 		 		                                 { name: 'vin', displayName: 'Vin', width:'8%',cellEditableCondition: false,
        	     		     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,2)" style="color: #5b5b5b;">{{row.entity.vin}}</a> ',
        	     		     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     		     			 		   		                                       if (row.entity.isRead === false) {
        	     		     			 		   		                                         return 'red';
        	     		     			 		   		                                     }
        	     		     			 		  		                                	} ,
        	     		     			 		 		                                 },
        	     		     			 		 		                                 { name: 'model', displayName: 'Model', width:'8%',cellEditableCondition: false,
        	     		     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,2)" style="color: #5b5b5b;">{{row.entity.model}}</a> ',
        	     		     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     		     			 		   		                                       if (row.entity.isRead === false) {
        	     		     			 		   		                                         return 'red';
        	     		     			 		   		                                     }
        	     		     			 		  		                                	} ,
        	     		     			 		 		                                 },
        	     		     			 		 		                                 { name: 'make', displayName: 'Make', width:'9%',cellEditableCondition: false,
        	     		     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,2)" style="color: #5b5b5b;">{{row.entity.make}}</a> ',
        	     		     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     		     			 		   		                                       if (row.entity.isRead === false) {
        	     		     			 		   		                                         return 'red';
        	     		     			 		   		                                     }
        	     		     			 		  		                                	} ,
        	     		     			 		 		                                 },
        	     		     			 		 		                             { name: 'confirmDate', displayName: 'Date', width:'8%',cellEditableCondition: false,
        	     		     	     			 		                                	cellTemplate:'<a ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,2)" style="color: #5b5b5b;">{{row.entity.confirmDate}}</a> ',
        	     		     	     			 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     		     	     			 		                                	 if (row.entity.isRead === false) {
        	     		     			 		   		                                         return 'red';
        	     		     			 		   		                                     }
        	     		     	     			  		                                	} ,
        	     		     	     					                                 },
        	     		     	     					                                 { name: 'confirmTime', displayName: 'Time', width:'6%',cellEditableCondition: false,
        	     		     	     					                                	cellTemplate:'<a ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,2)" style="color: #5b5b5b;">{{row.entity.confirmTime}}</a> ',
        	     		     	     					                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     		     	     					                                		 if (row.entity.isRead === false) {
            	     		     			 		   		                                         return 'red';
            	     		     			 		   		                                     }
        	     		     	     				 		                                	} ,
        	     		     	     			 		                                 },
        	     		     	     			 		                             { name: 'wethar', displayName: 'Weather', width:'8%',cellEditableCondition: false,
         	     		     	     					                                	cellTemplate:'<a style="color: #5b5b5b;">{{row.entity.wether}}&deg;</a> ',
         	     		     	     					                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
         	     		     	     					                                	 if (row.entity.isRead === false) {
        	     		     			 		   		                                         return 'red';
        	     		     			 		   		                                     }
         	     		     	     				 		                                	} ,
         	     		     	     			 		                                 },
         	     		     	     			 		                            { name: 'checkBoxComp', displayName: 'Completed',enableFiltering: false, width:'5%',cellEditableCondition: false,
     	     		     	       			 		                                	 	cellTemplate:'<input type="checkbox" ng-click="grid.appScope.testDriveCompleted(row.entity, row.entity.check)" ng-model="row.entity.check"  name="check" >',
     	     		     	       			 		                                	 		cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     		     	       			 		                                	 			if (row.entity.isRead === false) {
     	     		     	       			 		                                	 				return 'red';
     	     		     	       			 		                                	 			}
     	     		     	       			 		                                	 		} ,
     	     		     	       			 		                                 	},
        	     		     			 				                               { name: 'btnSold', displayName: '',enableFiltering: false, width:'25%',cellEditableCondition: false,
        	     		     			 				                                	cellTemplate:'<button type="button" ng-click="grid.appScope.confirmDateTime(row.entity)" ng-show="grid.appScope.userType != \'\'"ng-show="row.entity.isRead" data-toggle="modal" data-target="#modal-basic" class="btn btn-sm btn-primary"  ng-click="confres()">Reschedule</button><button type="button" ng-click="grid.appScope.cancelScheduleComfir(row.entity)" ng-show="grid.appScope.userType != \'\'"class="btn btn-sm btn-primary" style="margin-left:0px;">CANCEL</button>',
        	     		     	      			 		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     		     	       			   		                                       if (row.entity.isRead === false) {
        	     		     	       			   		                                         return 'red';
        	     		     	       			   		                                     }
        	     		     	       			  		                                	} ,
        	     		     	       			 		                                 },
        	     		     	       			 		                                 
        	     		     	       			 		                                 
        	     		     			 		     		                               ]; 
        	     		     			 		 		$scope.gridOptions10 = {
        	     	     	     			 	    	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
        	     	     	     			 	    	 		    paginationPageSize: 150,
        	     	     	     			 	    	 		    enableFiltering: true,
        	     	     	     			 	    	 		    useExternalFiltering: true,
        	     	     	     			 	    	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
        	     	     	     			 	    	 		 };
        	     	     	     			 	    	 		 $scope.gridOptions10.enableHorizontalScrollbar = 0;
        	     	     	     			 	    	 		 $scope.gridOptions10.enableVerticalScrollbar = 2;
        	     	     	     			 	    	 		 $scope.gridOptions10.columnDefs = [
        	     	     	     			 	    	 		                                 { name: 'vin', displayName: 'Vin', width:'15%',cellEditableCondition: false,
        	     	     	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     	     	     			 	    	 		                                       if (row.entity.isRead === false) {
        	     	     	     			 	    	 		                                         return 'red';
        	     	     	     			 	    	 		                                       }
        	     	     	     			 	    	 		                                	} ,
        	     	     	     			 	    	 		                                 },
        	     	     	     			 	    	 		                                 { name: 'model', displayName: 'Model', width:'10%',cellEditableCondition: false,
        	     	     	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     	     	     			 	    	  		                                       if (row.entity.isRead === false) {
        	     	     	     			 	    	  		                                         return 'red';
        	     	     	     			 	    	  		                                     }
        	     	     	     			 	    	 		                                	} ,
        	     	     	     			 	    	 		                                 },
        	     	     	     			 	    	 		                                 { name: 'make', displayName: 'Make', width:'10%',cellEditableCondition: false,
        	     	     	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     	     	     			 	    	  		                                       if (row.entity.isRead === false) {
        	     	     	     			 	    	  		                                         return 'red';
        	     	     	     			 	    	  		                                     }
        	     	     	     			 	    	 		                                	} ,
        	     	     	     			 	    	 		                                 },
        	     	     	     			 	    	 		                                 { name: 'name', displayName: 'Name', width:'12%',cellEditableCondition: false,
        	     	     	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     	     	     			 	    	  		                                       if (row.entity.isRead === false) {
        	     	     	     			 	    	  		                                         return 'red';
        	     	     	     			 	    	  		                                     }
        	     	     	     			 	    	 		                                	} ,
        	     	     	     			 	    	 		                                 },
        	     	     	     			 	    	 		                                 { name: 'phone', displayName: 'Phone', width:'10%',cellEditableCondition: false,
        	     	     	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     	     	     			 	    	  		                                       if (row.entity.isRead === false) {
        	     	     	     			 	    	  		                                         return 'red';
        	     	     	     			 	    	  		                                     }
        	     	     	     			 	    	 		                                	} ,
        	     	     	     			 	    	 		                                 },
        	     	     	     			 	    	 		                                 { name: 'email', displayName: 'Email', width:'13%',cellEditableCondition: false,
        	     	     	     			 	    	 		                                	cellTemplate:'<a  href="mailto:{{row.entity.email}}">{{row.entity.email}}</a> ',
        	     	     	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     	     	     			 	    	  		                                       if (row.entity.isRead === false) {
        	     	     	     			 	    	  		                                         return 'red';
        	     	     	     			 	    	  		                                     }
        	     	     	     			 	    	 		                                	} ,
        	     	     	     			 	    	 		                                 },
        	     	     	     			 	    	 		                             /*{ name: 'status', displayName: 'Status', width:'10%',cellEditableCondition: false,
        	     	      	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     	      	     			 	    	  		                                       if (row.entity.isRead === false) {
        	     	      	     			 	    	  		                                         return 'red';
        	     	      	     			 	    	  		                                     }
        	     	      	     			 	    	 		                                	} ,
        	     	      	     			 	    	 		                                 },
        	     	      	     			 	    	 		                            { name: 'statusDate', displayName: 'Date added', width:'10%',cellEditableCondition: false,
        	     	       	     			 	    	 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     	       	     			 	    	  		                                       if (row.entity.isRead === false) {
        	     	       	     			 	    	  		                                         return 'red';
        	     	       	     			 	    	  		                                     }
        	     	       	     			 	    	 		                                	} ,
        	     	       	     			 	    	 		                                 },
        	     	       	     			 	    	 		                                 */
        	     	       	     			 	    	 		                           { name: 'testDriveCompletedComment', displayName: 'Comments', width:'10%',cellEditableCondition: false,
         	     	       	     			 	    	 		                               	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
         	     	       	     			 	    	  		                                       if (row.entity.isRead === false) {
         	     	       	     			 	    	  		                                    	   return 'red';
         	     	       	     			 	    	  		                                     }
         	     	       	     			 	    	 		                               } ,
         	     	       	     			 	    	 		                          },  
		         	     	       	     			 	    	 		                  { name: 'testDriveCompletedDuration', displayName: 'Duration', width:'7%',cellEditableCondition: false,
		       	     	       	     			 	    	 		                               	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		       	     	       	     			 	    	  		                                       if (row.entity.isRead === false) {
		       	     	       	     			 	    	  		                                    	   return 'red';
		       	     	       	     			 	    	  		                                     }
		       	     	       	     			 	    	 		                               } ,
		       	     	       	     			 	    	 		                          },
        	     	      	     			 	    	 		                            
        	     	     	     			 	    	 		                             
        	     	     	     			 	    	 		                                /*{ name: 'btnSold', displayName: '',enableFiltering: false, width:'12%',cellEditableCondition: false,
        	     	     	     			 				 		                                	cellTemplate:'<button type="button" ng-click="grid.appScope.addNoteToRequestUser(row.entity,\'cansal\')" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:0%;">HISTORY</button><button ng-show="grid.appScope.userType == \'Manager\'" type="button" ng-click="grid.appScope.assignCanceledLead(row.entity)" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:0%;">ASSIGN</button>',
        	     	     	     			 				 		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        	     	     	     			 				   		                                       if (row.entity.isRead === false) {
        	     	     	     			 				   		                                         return 'red';
        	     	     	     			 				   		                                     }
        	     	     	     			 				  		                                	} ,
        	     	     	     			 				 		                                 },*/
        	     	     	     			 	    	     		                                 ]; 		 
     	     		     			 		 		 		 
     	     			 		 		 
     			 		 		 
     			 		 		$('.datepicker').datepicker({
     			 		 		});
     			 		 		//$("#cnfDate").datepicker().datepicker("setDate", new Date());
     			 		 		//$("#cnfDate").datepicker("setDate", new Date());
     			 		 		$('#cnfDate').val(new Date());
     			 		 		$('#timepicker1').timepicker(); 
     			 		 		
     			 		 		
     			 		 		apiserviceDashborad.getAllRequestInfoSeen().then(function(data){
     			 		 			$scope.gridOptions5.data = data;
     			    				$scope.AllRequestInfoSeenList = data;
     			 		 		});
    		 
     			 		 		apiserviceDashborad.getAllContactUsSeen().then(function(data){
     			 		 			$scope.gridOptions5.data = data;
     			    				$scope.AllRequestInfoSeenList = data;
     			 		 		});
    		  
    		    		  
	    		  $scope.gridOptions7.onRegisterApi = function(gridApi){
	 				 $scope.gridApi = gridApi;
	 				 
	 		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
	 			          var grid = this.grid;
	 			          $scope.gridOptions7.data = $filter('filter')($scope.getAllListLeadDate,{'name':grid.columns[0].filters[0].term,'phone':grid.columns[1].filters[0].term,'email':grid.columns[2].filters[0].term,'requestDate':grid.columns[3].filters[0].term,'vin':grid.columns[4].filters[0].term,'model':grid.columns[5].filters[0].term,'make':grid.columns[6].filters[0].term,'typeOfLead':grid.columns[7].filters[0].term},undefined);
	 			        });
	 		   		
	 	  		};
	 	  		$scope.gridOptions9.onRegisterApi = function(gridApi){
					 $scope.gridApi = gridApi;
					 
			   		$scope.gridApi.core.on.filterChanged( $scope, function() {
				          var grid = this.grid;
				          $scope.gridOptions9.data = $filter('filter')($scope.allTestDirConfir,{'name':grid.columns[0].filters[0].term,'phone':grid.columns[1].filters[0].term,'email':grid.columns[2].filters[0].term,'requestDate':grid.columns[3].filters[0].term,'vin':grid.columns[4].filters[0].term,'model':grid.columns[5].filters[0].term,'make':grid.columns[6].filters[0].term,'confirmDate':grid.columns[7].filters[0].term,'confirmTime':grid.columns[8].filters[0].term,'wethar':grid.columns[9].filters[0].term},undefined);
				        });
			   		
		  		};
    		  
    		  $scope.gridOptions5.onRegisterApi = function(gridApi){
    				 $scope.gridApi = gridApi;
    				 
    		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
    			          var grid = this.grid;
    			          $scope.gridOptions5.data = $filter('filter')($scope.AllRequestInfoSeenList,{'vin':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'make':grid.columns[2].filters[0].term,'name':grid.columns[3].filters[0].term,'phone':grid.columns[4].filters[0].term,'email':grid.columns[5].filters[0].term,'requestDate':grid.columns[6].filters[0].term},undefined);
    			        });
    		   		
    	  		};
    	  		
    	  		
    	  		apiserviceDashborad.getAllLostAndCompLeads().then(function(data){
    	  			$scope.gridOptions6.data = data;
     				$scope.AllLostAndCompl = data;
	 		 		});
    	  		
    	  		
     		  
     		  $scope.gridOptions6.onRegisterApi = function(gridApi){
   				 $scope.gridApi = gridApi;
   				 
   		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
   			          var grid = this.grid;
   			          $scope.gridOptions6.data = $filter('filter')($scope.AllLostAndCompl,{'vin':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'make':grid.columns[2].filters[0].term,'name':grid.columns[3].filters[0].term,'phone':grid.columns[4].filters[0].term,'email':grid.columns[5].filters[0].term,'status':grid.columns[6].filters[0].term,'statusDate':grid.columns[7].filters[0].term},undefined);
   			        });
   		   		
   	  		};
   	  		
   	  		$scope.allVehical = null;
   	  	apiserviceDashborad.getAllVehicles().then(function(data){
			$scope.allVehicalss = data;
		});
   	  		/*$scope.getAllVehical = function(){
	   	  		$http.get('/getAllVehical')
	 				.success(function(data) {
	 					$scope.allVehicalss = data;
	 				});
   	  		}*/
   	  	
   	  	
   	 
   	  	$scope.saveCompleted = function(){
   	  		
	   	  	apiserviceDashborad.saveCompletedLeads($scope.testCompleted.duration,$scope.testCompleted.comment,$scope.testCompleted.id,$scope.testCompleted.typeOfLead).then(function(data){
					
					$("#completedPopup").modal('hide');
					$scope.getAllSalesPersonRecord($scope.salesPerson);
					
			});
   	  	}
   	  		
   	  	$scope.openForm = function(){
			$("#tradeInAppEdit").modal();
		}
   	 $scope.testCompleted = {};
   	  	$scope.testDriveCompleted = function(entity, check){
   	  		$scope.check = true;
   	  		if(check == false || check == undefined){
   	  			$scope.testCompleted.id = entity.id;
   	  			$scope.testCompleted.typeOfLead = entity.typeOfLead;
   	  			$("#completedPopup").modal();
   	  		}
   	  	}
   	  	
   	  	$scope.showPdf = function(id){
   	  		$scope.pdfFile = "/getPdfPath/"+id;
   	  		$('#openPdffile').click();
   	  	}
   	 
   	$scope.gridForReferrer = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 }; 				          
      $scope.gridForSessionNew = {
		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
		 		    paginationPageSize: 150,
		 		    enableFiltering: true,
		 		    useExternalFiltering: true,
		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
		 		 }; 
      $scope.gridForSession = {
		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
		 		    paginationPageSize: 150,
		 		    enableFiltering: true,
		 		    useExternalFiltering: true,
		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
		 		 }; 
      $scope.gridForSecondModal = {
		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
		 		    paginationPageSize: 150,
		 		    enableFiltering: true,
		 		    useExternalFiltering: true,
		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
		 		 }; 
      
     /* $scope.changeActiveTabImage = function (id){
    	  $http.get('/getSessionIdData/'+$scope.sessionId)
  		.success(function(data) {
  			$scope.sessionData = data;
  			console.log(data);
  			if($scope.sessionData.length == 0){
  				console.log("length is");
  				console.log($scope.sessionData.length);
  			}
  			else{
  				$scope.changeActiveImage($scope.id);
  			}
  		});
      }*/

     /* $('#editLeads').modal({
    	  backdrop: 'static',
    	  keyboard: false
    	})*/
    /*  $(document).ready(function(){
    	  $('#editLeads').bind('click', function(event){
    	      if (event.target == $('#editLeads').get(0))
    	           
    	  });
    	  
      }*/
      $('#editLeads').click(function() {
    	  $('.popups-close').trigger('click');
    	  });
      
      $scope.flagForSecondmodal=0;
      $scope.referrerTypeDataForIpAdress = function(type) {
			 console.log(type);
			 $scope.ipData={};
			 var today = new Date()
				$scope.startDateFilter = $filter('date')(today,"yyyy-MM-dd");
				$scope.endDateFilter = $filter('date')(today,"yyyy-MM-dd");
			 $scope.flagForLocation='IP';
			 
			 if(type != "0"){
				 $("#editLeads").modal('hide');
				 $('#deeperInfoModal').click();
				 $scope.flagForReferrer = 0;
				 $scope.flagForData = 0;
			 }
			 
			 $scope.flagForSecondmodal=1;
			
			 apiserviceDashborad.getSessionIdData($scope.sessionId).then(function(data){
				 
					if(data == ""){
								$scope.sessiondata = '0';
					}else{
						
						$scope.flagForSecondMap=1;
						$scope.sessiondata = data;
						$scope.sessiondata.timeTotal=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(data.timeTotal)), 'HH:mm:ss');
						 var splitTime   = data.timeTotal.split(":");
						 if(splitTime[0] == '00'){
							 $scope.sessiondata.timeTotal = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 $scope.sessiondata.timeTotal = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
							
					}
					$scope.latitude=data.latitude; 
					$scope.longitude=data.longitude;
					$scope.clickySessionId=data.sessionId;
					console.log($scope.clickySessionId);
					
					if($scope.clickySessionId != null && $scope.clickySessionId != undefined){
						var today = new Date()
						$scope.startDateFilter = $filter('date')(today,"yyyy-MM-dd");
						$scope.endDateFilter = $filter('date')(today,"yyyy-MM-dd");
						console.log($scope.startDateFilter);
		        		console.log($scope.endDateFilter);
		        		
		        		 apiserviceDashborad.getSessionData($scope.clickySessionId, $scope.startDateFilter, $scope.endDateFilter).then(function(data){
		        			 
								console.log(data);
								$scope.gridForSessionNew.data=data;
								angular.forEach($scope.gridForSessionNew.data, function(value, key) {
									 if(value.timeTotal!=null){
											value.timeTotal=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.timeTotal)), 'HH:mm:ss');
											 var splitTime   = value.timeTotal.split(":");
											 if(splitTime[0] == '00'){
												 value.timeTotal = splitTime[1]+"m "+splitTime[2]+"s";
											 }
											 else{
												 value.timeTotal = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
											 }
											 
											}
									});
									//$scope.visitiorList = data;
									$scope.openGrid(); 
									 google.maps.event.addDomListener(window, "load", initialized);
									initialized();
									 	 
								});
						  
					 }
					else{
						google.maps.event.addDomListener(window, "load", initialized);
						initialized();
					}
					
				});
		 }
      
      
      $scope.openGrid = function(){
    	  $scope.gridForSessionNew.columnDefs = [];
			$scope.gridForSessionNew.columnDefs = [
													{name: 'abc', displayName: 'Date & Time', width:'20%',
														 cellTemplate:'<div><label >{{row.entity.newDate}}</label> &nbsp&nbsp  <label ">{{row.entity.newTime}}</label> </div>',	 
													},
													{name: 'a', displayName: 'Location', width:'20%',
														 cellTemplate:'<div><label >{{row.entity.geolocation}}</label></div>',	 
													},
													{name: 'organization', displayName: 'Orgainzation', width:'20%'},
													{name: 'abcs', displayName: 'Action', width:'20%',
														 cellTemplate:'<div><label >{{row.entity.actions}} actions</label></div>',	 
													},
													{name: 'ad', displayName: 'Total Time', width:'20%',
														 cellTemplate:'<div><label >{{row.entity.timeTotal}}</label></div>',	 
													},
					                                 
					                          ];

      }
      
      $scope.flagForReferrer=0;
      $scope.getLoacationDetails = function(type){
    	  $scope.typeOfReferrer=type;
    	  $scope.locationFlag='location';
    	  $scope.flagForReferrer=1;
    	  $scope.flagForData = 0;
    	  $scope.referrerTypeDataForIpAdress("0");
    	  $scope.getReferrerDetails();
      }
      $scope.flagForData=0;
      $scope.getOrgDetails = function(type){
    	  $scope.typeOfReferrer=type;
    	  $scope.locationFlag='org';
    	  $scope.flagForReferrer=1;
    	  $scope.flagForData = 1;
    	  $scope.referrerTypeDataForIpAdress("0");
    	  $scope.getReferrerDetails();
      }
      
      $scope.getBrowserDetails = function(type){
    	  $scope.typeOfReferrer=type;
    	  $scope.locationFlag='browser';
    	  $scope.flagForReferrer=1;
    	  $scope.flagForData = 0;
    	  $scope.referrerTypeDataForIpAdress("0");
    	  $scope.getReferrerDetails();
      }
      $scope.getOsDetails = function(type){
    	  $scope.typeOfReferrer=type;
    	  $scope.locationFlag='os';
    	  $scope.flagForReferrer=1;
    	  $scope.flagForData = 0;
    	  $scope.referrerTypeDataForIpAdress("0");
    	  $scope.getReferrerDetails();
      }
      $scope.getScreenDetails = function(type){
    	  $scope.typeOfReferrer=type;
    	  $scope.locationFlag='screen';
    	  $scope.flagForReferrer=1;
    	  $scope.flagForData = 0;
    	  $scope.referrerTypeDataForIpAdress("0");
    	  $scope.getReferrerDetails();
      }
      $scope.getReferrerType = function(type){
    	  $scope.typeOfReferrer=type;
    	  $scope.locationFlag='source';
    	  $scope.flagForReferrer=1;
    	  $scope.flagForData = 0;
    	  $scope.referrerTypeDataForIpAdress("0");
    	  $scope.getReferrerDetails();
      }
      $scope.referrerTypeDataForHost = function(type){
    	  $scope.typeOfReferrer=type;
    	  $scope.locationFlag='host';
    	  $scope.flagForReferrer=1;
    	  $scope.flagForData = 0;
    	  $scope.referrerTypeDataForIpAdress("0");
    	  $scope.getReferrerDetails();
      }
      $scope.referrerTypeDataForLang = function(type){
    	  $scope.typeOfReferrer=type;
    	  $scope.locationFlag='language';
    	  $scope.flagForReferrer=1;
    	  $scope.flagForData = 0;
    	  $scope.referrerTypeDataForIpAdress("0");
    	  $scope.getReferrerDetails();
      }
      $scope.getLandingPage = function(type){
    	  $scope.typeOfReferrer=type;
    	  $scope.locationFlag='landing';
    	  $scope.flagForReferrer=1;
    	  $scope.flagForData = 0;
    	  $scope.referrerTypeDataForIpAdress("0");
    	  $scope.getLendingDetails();
    	  console.log("in referrer details funct");
    	}
		 
      $scope.getReferrerDetails = function(){
		if($scope.typeOfReferrer != undefined && $scope.typeOfReferrer != null){
			console.log("in domain functon");
			 $('#editLeads').modal('hide');  
			 $scope.flagForSecondmodal=1;
			 $('#deeperInfoModal').click();
			
			$scope.date = $filter('date')($scope.date,"yyyy-MM-dd");
			console.log($scope.date);
			console.log("in date function");
			var todayDate = new Date(),
			 weekDate = new Date();
			weekDate.setTime(todayDate.getTime()-(7*24*3600000));
			var today = new Date();
			$scope.endDateFilter = $filter('date')($scope.date,"yyyy-MM-dd");
			$scope.startDateFilter = $filter('date')($scope.date,"yyyy-MM-dd");
			console.log($scope.typeOfReferrer);
			console.log($scope.startDateFilter);
			console.log($scope.endDateFilter);
			console.log("flag is");
			console.log($scope.flagForData);
			
			apiserviceDashborad.getreferrerTypeData($scope.typeOfReferrer, $scope.locationFlag, $scope.startDateFilter,$scope.endDateFilter).then(function(data){
			
			console.log($scope.data);
			$scope.gridForReferrer.data = data;
			console.log($scope.gridForReferrer.data);
			$scope.openGrid();
			angular.forEach($scope.gridForReferrer.data, function(value, key) {
				if( value.city != null && value.city != undefined  ){
					$scope.city=value.city;
					console.log($scope.city);
				}
				if(value.title=="totalT"){
					value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
					 var splitTime   = value.these_visitors.split(":");
					 if(splitTime[0] == '00'){
						 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 else{
						 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 
					}	
				if(value.title=="averageT"){
					value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
					 var splitTime1   = value.these_visitors.split(":");
					 if(splitTime1[0] == '00'){
						 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 else{
						 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 
					}	 
					 
				if(value.title=="totalT"){
					value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
					 var splitTime   = value.all_visitors.split(":");
					 if(splitTime[0] == '00'){
						 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 else{
						 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 
					 
					}	
				if(value.title=="averageT"){
					value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
					 var splitTime1   = value.all_visitors.split(":");
					 if(splitTime1[0] == '00'){
						 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 else{
						 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 
					}
				
				
			});
			$scope.visitiorList = data;
			
		});
		 
		 
		 $scope.gridForReferrer.columnDefs = [
											{name: 'tt', displayName: 'Summary of filtered visitors', width:'40%',
												   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>', 
											},
											  {name:'these_visitors', displayName:'These Visitors', width:'20%' },
											  {name:'all_visitors', displayName:'All Visitors', width:'20%' },
											  {name:'these', displayName:'Difference', width:'20%',
											 	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',
											  },
								            
								         ]
		 
		 
		}
      
      } 
      $scope.openGrid = function(){
    	  console.log($scope.flagForData);
    	  console.log("flag");
    	  $scope.gridForSessionNew.columnDefs = [];
    	  if($scope.flagForData == 1){
    		  
			$scope.gridForSessionNew.columnDefs = [
					                                 {name: 'abc', displayName: 'Date & Time', width:'20%',
					                                	 cellTemplate:'<div><label >{{row.entity.newDate}}</label> &nbsp&nbsp  <label ">{{row.entity.newTime}}</label> </div>',	 
					                                 },
					                                 {name: 'a', displayName: 'Location', width:'20%',
					                                	 cellTemplate:'<div><label >{{row.entity.geolocation}}</label></div>',	 
					                                 },
					                                 {name: 'ipAddress', displayName: 'Ip Address', width:'20%'},
					                                 {name: 'abcs', displayName: 'Action', width:'20%',
					                                	 cellTemplate:'<div><label >{{row.entity.actions}} actions</label></div>',	 
					                                 },
					                                 {name: 'ad', displayName: 'Total Time', width:'20%',
					                                	 cellTemplate:'<div><label >{{row.entity.timeTotal}}</label></div>',	 
					                                 },
					                                
					                          ];

 
      }else if($scope.flagForData != 1){
    	  $scope.gridForSessionNew.columnDefs = [
				                                 {name: 'abc', displayName: 'Date & Time', width:'20%',
				                                	 cellTemplate:'<div><label >{{row.entity.newDate}}</label> &nbsp&nbsp  <label ">{{row.entity.newTime}}</label> </div>',	 
				                                 },
				                                 {name: 'a', displayName: 'Location', width:'20%',
				                                	 cellTemplate:'<div><label >{{row.entity.geolocation}}</label></div>',	 
				                                 },
				                                 {name: 'organization', displayName: 'Orgainzation', width:'20%'},
				                                 {name: 'abcs', displayName: 'Action', width:'20%',
				                                	 cellTemplate:'<div><label >{{row.entity.actions}} actions</label></div>',	 
				                                 },
				                                 {name: 'ad', displayName: 'Total Time', width:'20%',
				                                	 cellTemplate:'<div><label >{{row.entity.timeTotal}}</label></div>',	 
				                                 },
				                                
				                          ];
      }
      }
     $scope.landingpage = {};
      
      $scope.getLendingDetails = function(){
  		if($scope.typeOfReferrer != undefined && $scope.typeOfReferrer != null){
  			console.log("in domain functon");
  			 $('#editLeads').modal('hide');  
  			 $scope.flagForSecondmodal=1;
  			 $('#deeperInfoModal').click();
  			var todayDate = new Date(),
  			 weekDate = new Date();
  			weekDate.setTime(todayDate.getTime()-(7*24*3600000));
  			var today = new Date();
  			$scope.endDateFilter = $filter('date')(today,"yyyy-MM-dd");
  			$scope.startDateFilter = $filter('date')(weekDate,"yyyy-MM-dd");
  			console.log($scope.typeOfReferrer);
  			console.log($rootScope.startDateFilter);
  			console.log($rootScope.endDateFilter);
  			
  		  $scope.landingpage.startDateFilter=$scope.startDateFilter;
  	      $scope.landingpage.endDateFilter=$scope.endDateFilter;
  	      $scope.landingpage.locationFlag=$scope.locationFlag;
  	      $scope.landingpage.typeOfReferrer=$scope.typeOfReferrer;
  	      
  	    apiserviceDashborad.geLendingPageData($scope.landingpage).then(function(data){
  	    
  			console.log($scope.data);
  			$scope.gridForReferrer.data = data;
  			console.log($scope.gridForReferrer.data);
  			angular.forEach($scope.gridForReferrer.data, function(value, key) {
  				if( value.city != null && value.city != undefined  ){
  					$scope.city=value.city;
  					console.log($scope.city);
  				}
  				if(value.title=="totalT"){
  					value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
  					 var splitTime   = value.these_visitors.split(":");
  					 if(splitTime[0] == '00'){
  						 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
  					 }
  					 else{
  						 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
  					 }
  					 
  					}	
  				if(value.title=="averageT"){
  					value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
  					 var splitTime1   = value.these_visitors.split(":");
  					 if(splitTime1[0] == '00'){
  						 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
  					 }
  					 else{
  						 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
  					 }
  					 
  					}	 
  					 
  				if(value.title=="totalT"){
  					value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
  					 var splitTime   = value.all_visitors.split(":");
  					 if(splitTime[0] == '00'){
  						 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
  					 }
  					 else{
  						 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
  					 }
  					 
  					 
  					}	
  				if(value.title=="averageT"){
  					value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
  					 var splitTime1   = value.all_visitors.split(":");
  					 if(splitTime1[0] == '00'){
  						 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
  					 }
  					 else{
  						 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
  					 }
  					 
  					}
  				
  				
  			});
  			$scope.visitiorList = data;
  			
  		});
  		 
  		 
  		 $scope.gridForReferrer.columnDefs = [
  											{name: 'tt', displayName: 'Summary of filtered visitors', width:'40%',
  												   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>', 
  											},
  											  {name:'these_visitors', displayName:'These Visitors', width:'20%' },
  											  {name:'all_visitors', displayName:'All Visitors', width:'20%' },
  											  {name:'these', displayName:'Difference', width:'20%',
  											 	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',
  											  },
  								            
  								         ]
  		 
  		}
        
        }
      
      $scope.goToVisitorDetails = function(entity){
      $('#secondModal').modal('hide');  
      $('#editLeads').modal('show');
      
      }
      
      $scope.ch = function(){
    	  $scope.infoColorFlag=0;
    	  document.getElementById("activeTabImage").src = "../../../assets/global/images/leadsImages/session_data active.png";
      	  document.getElementById("infoImage").src = "../../../assets/global/images/leadsImages/information-button inactive.png";
      	initialized();
      }
      
   	$scope.infoColorFlag=1;
   	$scope.visitorInfo={};
   	 $scope.changeActiveTabImage = function(id){
	  console.log("inside");
	  if(id != undefined){
		  $scope.sessionId = id;
	  }
	  //document.getElementById("activeTabImage").src = "../../../assets/global/images/leadsImages/session_data active.png";
  	  //document.getElementById("infoImage").src = "../../../assets/global/images/leadsImages/information-button inactive.png";
  	 console.log($scope.sessionId) ;
  	 
  	apiserviceDashborad.getSessionIdData($scope.sessionId).then(function(data){
  	 
			if(data == ""){
						$scope.sessiondata = '0';
			}else{
				$scope.sessiondata = data;
				$scope.sessiondata.timeTotal=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(data.timeTotal)), 'HH:mm:ss');
				 var splitTime   = data.timeTotal.split(":");
				 if(splitTime[0] == '00'){
					 $scope.sessiondata.timeTotal = splitTime[1]+"m "+splitTime[2]+"s";
				 }
				 else{
					 $scope.sessiondata.timeTotal = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
				 }
					
			}
			
			  console.log(data);
			$scope.latitude=data.latitude; 
			$scope.longitude=data.longitude;
			$scope.date = data.dateClick;
			
			$scope.clickySessionId=data.sessionId;
			console.log($scope.clickySessionId);
		   console.log($scope.latitude);
		   console.log($scope.longitude);
			$scope.visitorInfo=data;
			if($scope.clickySessionId != null && $scope.clickySessionId != undefined){
				var today = new Date()
				$scope.startDateFilter = $filter('date')(today,"yyyy-MM-dd");
				$scope.endDateFilter = $filter('date')(today,"yyyy-MM-dd");
				console.log($scope.startDateFilter);
        		console.log($scope.endDateFilter);
        		apiserviceDashborad.getSession($scope.clickySessionId,$scope.startDateFilter,$scope.endDateFilter).then(function(data){
						$scope.gridForSession.data=data;
							$scope.visitiorList = data;
							
							 $scope.gridForSession.columnDefs = [
								                                 {name: 'newTimePretty', displayName: 'Date & Time', width:'40%',
								                                	 cellTemplate:'<div ><label >{{row.entity.newDate}}</label> &nbsp&nbsp  <label ">{{row.entity.newTime}}</label> </div>',	 
								                                 },
								                                 {name: 'actionUrl', displayName: 'Viewed Pages', width:'40%',
								                                	 cellTemplate:'<div ><a   target="_blank"   href="{{row.entity.actionUrl}}" >{{row.entity.newActionUrl}}</a> </br>   <label>{{row.entity.actionTitle}}</label> </div>',
								                                 },
								                                 {name: 'heatmapUrl', displayName: 'Heatmap', width:'20%',
								                                	 cellTemplate:'<div ><a href="{{row.entity.heatmapUrl}}" target="_blank"><img  ng-if="row.entity.heatmapUrl != null" class="mb-2" style="margin-left: 8px;width: 21px;" title="View heatmap for this page" src="http://con.tent.network/media/icon_heatmap.png" ></a></div>',	 
								                                 },
								                          ];
							 
							 google.maps.event.addDomListener(window, "load", initialized);
							//	initialized();
						});
						
			
				  
			 }
			else{
				//google.maps.event.addDomListener(window, "load", initialized);
			initialized();
			}
			
			
		}).error(function(data, status) {
			$scope.changeInfoImage("1");
		});
				
		
  	  
	 }
   	 
   	 
   	 
    	function initialized() {
    		$timeout(function(){
   		 console.log($scope.latitude);
   		 console.log($scope.longitude);
   		 $scope.long=parseInt($scope.longitude);
   		$scope.lat=parseInt($scope.latitude);
	      var myLatlng = new google.maps.LatLng($scope.lat,$scope.long);
	      var myOptions = {
	        zoom: 8,
	        center: myLatlng,
	        mapTypeId: google.maps.MapTypeId.ROADMAP
	      }
	      if($scope.flagForSecondMap == 1){
	      var map = new google.maps.Map(document.getElementById("canvas2"), myOptions);
	      }else{
	    	  //var map = new google.maps.Map(document.getElementById("canvas1"), myOptions);
	      }
	      var marker = new google.maps.Marker({
   	      position: myLatlng,
   	      map: map,
   	      visible: true
   	  });
   		
    		}, 3000);
	    }
   	
    $scope.changeInfoImage = function(id){
    	$scope.infoColorFlag=1;
    	 document.getElementById("infoImage").src = "../../../assets/global/images/leadsImages/information-button active.png";
   	  document.getElementById("activeTabImage").src = "../../../assets/global/images/leadsImages/session_data inactive.png";
  	 }
   	 
    
   	        $scope.financeData={};
   	  		$scope.editLeads = {};
   	  	$scope.stockWiseData = [];
   	  		$scope.editVinData = function(entity){
   	  			console.log(entity);
   	  		 $scope.changeActiveTabImage();
   	  		$scope.customData = entity.customMapData;
   	  			$scope.showFormly1 = '1';
   	  	    $scope.showFormly = '1';
   	  			console.log($scope.customData);
   	  		
   	  		 apiserviceDashborad.getCustomizationform('Create Lead').then(function(response){
   	  		 
				 $scope.editInput = response;
				 $scope.josnData = angular.fromJson(response.jsonData);
				 $scope.userFields = $scope.addFormField(angular.fromJson(response.jsonData));
				 console.log($scope.userFields);
				 $scope.user = {};
				 
				 console.log($scope.josnData);
		   	  	 $.each($scope.customData, function(attr, value) {
			   	  	angular.forEach($scope.josnData, function(value1, key1) {
				   	  			if(value1.key == attr){
				   	  				if(value1.component == "timerange"){
				   	  					console.log(value);
				   	  				$scope.customData.timeSet = value;
				   	  				}
				   	  			}
			   	  	});
		   	  	 });
   	  		});
   	  		
   	  		  $scope.financeData.downPayment=1000;
   	  		  $scope.financeData.annualInterestRate=7;
   	  		  $scope.financeData.numberOfYears=5;
   	  		  $scope.financeData.price=entity.price;
   	  		  $scope.financeData.frequencyOfPayments=26;
   	  		$scope.payments="00";
   	  		$scope.payment="0.000";
   	  		  
   	  		 // $scope.financeData.frequencyOfPayments=
   	  			$scope.stockWiseData = [];
   	  			$scope.editLeads = {};
   	  			//$scope.getAllVehical();
   	  			$('#btneditleads').click();
   	  			//$scope.editLeads = entity;
   	  			if(entity.typeOfLead == "Trade In" || entity.typeOfLead == "Trade-In Appraisal") {
				   $scope.pdffile = entity.pdfPath;
				   $scope.lead = entity.leadsValue;
			   }   	  		  
   	  			
   	  		$scope.stockWiseData.push({
				model:entity.model,
				make:entity.make,
				stockNumber:entity.stock,
				year:entity.year,
				bodyStyle:entity.bodyStyle,
				mileage:entity.mileage,
				transmission:entity.transmission,
				drivetrain:entity.drivetrain,
				engine:entity.engine,
				vin:entity.vin,
				imgId:entity.imgId,
				searchStr:entity.vin,
			});
   	  			$scope.editLeads.vin = entity.vin;
				$('#vinSearch_value').val(entity.vin);
				$('#vinSearch').val(entity.vin);
   	  			$scope.editLeads.stockNumber = entity.stock;
		   	  	$scope.editLeads.model = entity.model;
		   	  	$scope.editLeads.make = entity.make;
		   	  	$scope.editLeads.year = entity.year;
		   	  	$scope.editLeads.custZipCode = entity.custZipCode;
				$scope.editLeads.enthicity = entity.enthicity;
				$scope.editLeads.parentChildLead = entity.parentChildLead;
		   	  	
		   	  	$scope.editLeads.id = entity.id;
		   	  	$scope.editLeads.custName = entity.name;
		   	  	$scope.editLeads.custEmail = entity.email;
		   	  	$scope.editLeads.custNumber = entity.phone;
		   	  	$scope.editLeads.leadType = entity.typeOfLead;
   	  		}
   	  		
   	  		$scope.changesVin = function(vinNo,stockNo){
   	  		
   	  			angular.forEach($scope.allVehicalss, function(value, key) {
   	  				if(vinNo != ""){
	   	  				if(value.vin == vinNo){
	   	  					$scope.editLeads.model = value.model;
	   	  					$scope.editLeads.make = value.make;
	   	  					$scope.editLeads.year = value.year;
	   	  					//$scope.editLeads.custZipCode = value.custZipCode;
	   	  					//$scope.editLeads.enthicity = value.enthicity;
	   	  					$scope.editLeads.stockNumber = value.stock;
	   	  				}
   	  				}else if(stockNo != ""){
   	  					if(value.stock == stockNo){
   	  					$scope.editLeads.model = value.model;
	  					$scope.editLeads.make = value.make;
	  					$scope.editLeads.year = value.year;
	  				//	$scope.editLeads.custZipCode = value.custZipCode;
	  				//	$scope.editLeads.enthicity = value.enthicity;
	  					$scope.editLeads.vin = value.vin;
   	  					}
   	  				}
   	  				
   	  			});
   	  		}
   	 $scope.calculateFinancialData = function(financeData){
   		 
   	  	var cost         =financeData.price;
		var down_payment =financeData.downPayment;
		var interest     =financeData.annualInterestRate;
		var loan_years   =financeData.numberOfYears;
		var frequency_rate    =financeData.frequencyOfPayments;
		
		var interest_rate = (interest) / 100;
		 var rate          = interest_rate / frequency_rate;
		 $scope.payments      = loan_years * frequency_rate;
		var difference    = cost - down_payment;
		$scope.payment = Math.floor((difference*rate)/(1-Math.pow((1+rate),(-1* $scope.payments)))*100)/100;
   	 }
   	  	
   	  		$scope.editleads = function(){
   	  		$scope.customList = [];
   	  		$scope.customData.time_range = $("#bestTimes").val()
			if($scope.customData.time_range == undefined){
				delete $scope.customData.time_range
			}
			console.log($('#exCustoms_value').val());
			$scope.customData.custName = $('#exCustoms_value').val();
			if($scope.customData.custName == undefined){
				delete $scope.customData.custName;
			}
			$scope.customData.autocompleteText = $("#autocomplete").val()
			if($scope.customData.autocompleteText == undefined){
				delete $scope.customData.autocompleteText;
			}
   	  		console.log($scope.customData);
   	  	 $.each($scope.customData, function(attr, value) {
		      $scope.customList.push({
	   	  			key:attr,
	   	  			value:value,
	   	  			
				});
		   });
   	  	 
		
   	  		$scope.editLeads.customData = $scope.customList;
   	  		$scope.editLeads.stockWiseData = $scope.stockWiseData;
   	  		console.log($scope.editLeads);
   	  	var files = [];
   	  	if($rootScope.fileCustom != undefined){
			console.log($rootScope.fileCustom);
			files = $rootScope.fileCustom;
			
			// delete $scope.lead.customData;
			// delete $scope.lead.options;
			// delete $scope.lead.stockWiseData;
			 apiserviceDashborad.editLeads($scope.editLeads,files).then(function(data){
			 });
			 
			}else{ 
				apiserviceDashborad.editLeads($scope.editLeads,$rootScope.fileCustom).then(function(data){
					$("#editLeads").modal('hide');
	   	  			$scope.getAllSalesPersonRecord($scope.salesPerson);
				 });
			 }
   	  		}
   	  		$scope.joinDatePick = function(index){
   	  			
   	  			$('#bestTime'+index).timepicker(); 
   	  		}
   	  		
    		  
    		  $scope.contactsDetails = {};
    		  $scope.contactMsg = "";
    		  $scope.createContact = function(entity) {
    			  $scope.contactsDetails = {};
    			  $scope.contactsDetails.workEmail = "Work";
    			   $scope.contactsDetails.workEmail1 = "Work";
    			   $scope.contactsDetails.workPhone = "Work";
    			   $scope.contactsDetails.workPhone1 = "Work";
    			  $scope.contactMsg = "";
    			  $scope.contactsDetails.firstName = entity.name;
    			  $scope.contactsDetails.email = entity.email;
    			  $scope.contactsDetails.phone = entity.phone;
    			  $('#createcontactsModal').modal();
    		  }
    		  
    		  apiserviceDashborad.getUsers().then(function(data){
    			  $scope.allUser = data;
    		  });
    		 
    		  apiserviceDashborad.getgroupInfo().then(function(data){
    			  $scope.allGroup = data;
    		  });
    	   		    	   		
    		  
    	   	 $scope.saveGroup = function(createGroup){
    	   		 
    	   	  apiserviceDashborad.saveGroup(createGroup).then(function(data){
    	   		apiserviceDashborad.getgroupInfo().then(function(data){
    	   			$scope.allGroup = data;
    	   		});
    		  });
    			   
    		   }
    		   
    		   $scope.deleteGroup = function(groupId){
    			   apiserviceDashborad.deleteGroup(groupId).then(function(data){
    				   apiserviceDashborad.getgroupInfo().then(function(data){
    	    	   			$scope.allGroup = data;
    	    	   		});
    			   });
    			   
    			   /*$http.get('/deleteGroup/'+groupId)
    				.success(function(data) {
    					$.pnotify({
    					    title: "Success",
    					    type:'success',
    					    text: "group deleted successfully",
    					});
    					$http.get('/getgroupInfo').success(function(data){
    						$scope.allGroup = data;
    					 });
    				});*/
    		   }
    		   
    		  
    		  $scope.saveContact = function() {
    			  apiserviceDashborad.saveContactsData($scope.contactsDetails).then(function(data){
    				  if(data == "") {
	    					 $('#createcontactsModal').modal('hide');
 					 } else {
 						 $scope.contactMsg = data;
 					 }
    			  });
    		   }
    		  $scope.flags = {};
    		 $scope.checkIndex = function(item,values){
    			 angular.forEach($scope.currentData, function(value, key) {
    				 if(value.id == item.id){
    					 if(values == false || values == undefined){
    						 value.flag = 1;
    					 }else{
    						 value.flag = 0;
    					 }
    					 
    				 }
    			 });
    		 }
    		 
    		 $scope.comparisonTwoData = function(){
    			 var startDate = $('#comparisonStartDate').val();
     			var endDate = $('#comparisonEndDate').val();
     			
     			if(startDate == "" || endDate == "" || startDate == undefined || endDate == undefined){
     				var today = new Date()
     				endDate = $filter('date')(today,"yyyy-MM-dd");
    				var arr = [];
        			arr = $filter('date')(today,"yyyy-MM-dd").split('-');
    				startDate = arr[0]+"-"+arr[1]+"-"+"01";
        			$('#comparisonStartDate').val(startDate);
        			$('#comparisonEndDate').val(startDate);
        			//$('#comparisonEndDate').val(endDate); 
     			}
     			
     			
     			$scope.arrId = [];
     			angular.forEach($scope.comparisonperson, function(value, key) {
     				$scope.arrId.push(value.id);
     			});
     			$scope.comparisonperson = [];
     			
     			angular.forEach($scope.arrId, function(value, key) {
     				  apiserviceDashborad.getComperSalePersonData(value,startDate,endDate).then(function(data){
     					 $scope.comparisonperson.push(data);
     				  });
     			});
     			
    		 }
    		 
    		 $scope.comparisonperson = [];
    		 $scope.flagvalue = 0;
    		 $scope.checkSalePersonIndex = function(item,values){
    			 
    			 $scope.ComperFlag = 0;
    			 
        			var startDate = $('#comparisonStartDate').val();
        			var endDate = $('#comparisonEndDate').val();
        		
        			if(startDate == "" || endDate == "" || startDate == undefined || endDate == undefined){
        				var today = new Date()
            			//var priorDate = new Date().setDate(today.getDate()-30)
            			endDate = $filter('date')(today,"yyyy-MM-dd");
        				var arr = [];
            			arr = $filter('date')(today,"yyyy-MM-dd").split('-');
        				startDate = arr[0]+"-"+arr[1]+"-"+"01";
            			$('#comparisonStartDate').val(startDate);
            			//$('#comparisonEndDate').val(startDate);
            			$('#comparisonEndDate').val(endDate); 
        			}
    			 
    					 if(values == false || values == undefined){
    						 $scope.flagvalue++;
    						 item.flag = 1;
    						 
    						 apiserviceDashborad.getComperSalePersonData(item.id,startDate,endDate).then(function(data){
    							 $scope.comparisonperson.push(data);
    						 });
    						 
    					 }else{
					 $scope.flagvalue--;
					  item.flag = 0;
    						 angular.forEach($scope.comparisonperson, function(value, key) {
    							 if(value.id == item.id){
    								 $scope.comparisonperson.splice(key,1);
    							 }
    						 });
    						 
    						
    					 }
    					 
    					
    		 }
    		 
    		 $scope.bestEmpComp = function(){
    			 
    			 $scope.ComperFlag = 1;
    			 
    			 $scope.comparisonperson = [];
    			/* var today = new Date()
 				var arr = [];
     			arr = $filter('date')(today,"yyyy-MM-dd").split('-');
 				var startDate = arr[0]+"-"+arr[1]+"-"+"01";
 				var endDate = arr[0]+"-"+arr[1]+"-"+"01";*/
    			 var startD = $('#cnfstartDateValue').val();
  			   var endD = $('#cnfendDateValue').val();
  			   
  			 var arr = [];
  			 var arr1 = [];
  			   arr = startD.split('-');
  			 arr1 = endD.split('-');
  			 var startDate = arr[2]+"-"+arr[1]+"-"+arr[0];
  			 var endDate = arr1[2]+"-"+arr1[1]+"-"+arr1[0];
    			 
  			 apiserviceDashborad.getComperSalePersonData($scope.salesPerson,startDate,endDate).then(function(data){
				 $scope.comparisonperson.push(data);
			 });
 				
  			   apiserviceDashborad.getDateRangSalePerson(startDate,endDate).then(function(response){
 					if(response != $scope.salesPerson){
 						apiserviceDashborad.getComperSalePersonData(response,startDate,endDate).then(function(data){
 						 	$scope.comparisonperson.push(data);
 						 	$scope.flagvalue = 2;
 						 	$scope.comparisonPassSalePerson($scope.comparisonperson);
 		 				});
 					}else{
 						$('#btncomparisonBest').click();
 					}
 				});
    			
    		 }
    		 
    		 $scope.comparisonPassSalePerson = function(comparisonperson){
    			 $scope.comparisonSalePerson();
    		 }
    	
    		 $scope.visitorsStats = function(startDate, endDate){
    			 
    			
    			 if($("#vstartDate").val() == "" || $("#vendDate").val() == ""){
    				 if(startDate == undefined || endDate == undefined){
        				 startDate =  $("#vstartDate").val();
            			 endDate = $("#vendDate").val();
            			 
        			 }
    			 }else{
    				 startDate =  $("#vstartDate").val();
        			 endDate = $("#vendDate").val();
    			 }
    			 
    			 $scope.heatMapShow(startDate,endDate);
    			 var visitorsData = {};
    			 var chartOptions ={};
    			 /*var ctx = document.getElementById("visitors-chart").getContext("2d");
			        var myNewChart = new Chart(ctx).Line(visitorsData, chartOptions);
    			 */
    			 
    			 apiserviceDashborad.getMonthlyVisitorsStats(startDate,endDate).then(function(response){
    				  
  			        $scope.onlineVisitorsCount = response.onlineVisitors;
			        $scope.totalVisitorsCount = response.totalVisitors;
			        $scope.actionsCount = response.actions;
			        $scope.averageActionsCount = response.averageActions;
			        $scope.totalTimeCount = response.totalTime;
			        $scope.averageTimeCount = response.averageTime;
			        $scope.bounceRateCount = response.bounceRate;
			        $scope.goalsCount = response.goals;
			        $scope.revenueCount = response.revenue;
			        $scope.pagesList = response.pagesList;
			        $scope.referersList = response.referersList;
			        $scope.searchesList = response.searchesList;
			        
			        
        			  visitorsData = {
        			            labels: response.months,
        			            datasets: [
        			                {
        			                    label: "New Visitors",
        			                    fillColor: "rgba(49, 157, 181,0.5)",
        			                    strokeColor: "rgba(49, 157, 181,0.7)",
        			                    pointColor: "rgba(49, 157, 181,1)",
        			                    pointStrokeColor: "#fff",
        			                    pointHighlightFill: "#fff",
        			                    pointHighlightStroke: "rgba(49, 157, 181,1)",
        			                    data: response.onlineVisitor
        			                },
        			                {
        			                    label: "All visitors",
        			                    fillColor: "rgba(200,200,200,0.5)",
        			                    strokeColor: "rgba(200,200,200,1)",
        			                    pointColor: "rgba(200,200,200,1)",
        			                    pointStrokeColor: "#fff",
        			                    pointHighlightFill: "#fff",
        			                    pointHighlightStroke: "rgba(200,200,200,1)",
        			                    data: response.allVisitor
        			                }
        			            ]
        			        };
        			        chartOptions = {
        			            scaleGridLineColor: "rgba(0,0,0,.05)",
        			            scaleGridLineWidth: 1,
        			            bezierCurve: true,
        			            pointDot: true,
        			            pointHitDetectionRadius: 20,
        			            tooltipCornerRadius: 0,
        			            scaleShowLabels: false,
        			            tooltipTemplate: "dffdff",
        			            multiTooltipTemplate: "<%= datasetLabel %> - <%= value %>",
        			            responsive: true,
        			            showScale: false,
        			        };
        			        var ctx = null;
        			        //var myNewChart = null;
        			        //$('#visitors-chart').remove();
        			       // ctx = document.getElementById("visitors-chart").getContext("2d");
        			        //	myNewChart = new Chart(ctx).Line(visitorsData, chartOptions);

        			        	var pieChartContent = document.getElementById('pieChartContent');
        			        	$('#pieChartContent').append('<canvas id="visitors-chart" style="margin-bottom: 29px;"><canvas>');

        			        	ctx = document.getElementById("visitors-chart").getContext("2d");
        			        	var myNewChart = new Chart(ctx).Line(visitorsData, chartOptions);	
        			        	
        			        	
        			        	
        			        	
        			        
        			        var actionsData = {
            			            labels: response.months,
            			            datasets: [
            			                {
            			                    label: "Actions",
            			                    fillColor: "rgba(49, 157, 181,0.5)",
            			                    strokeColor: "rgba(49, 157, 181,0.7)",
            			                    pointColor: "rgba(49, 157, 181,1)",
            			                    pointStrokeColor: "#fff",
            			                    pointHighlightFill: "#fff",
            			                    pointHighlightStroke: "rgba(49, 157, 181,1)",
            			                    data: response.actionsList
            			                },
            			                {
            			                    label: "Average actions",
            			                    fillColor: "rgba(200,200,200,0.5)",
            			                    strokeColor: "rgba(200,200,200,1)",
            			                    pointColor: "rgba(200,200,200,1)",
            			                    pointStrokeColor: "#fff",
            			                    pointHighlightFill: "#fff",
            			                    pointHighlightStroke: "rgba(200,200,200,1)",
            			                    data: response.averageActionsList
            			                }
            			            ]
            			        };
        			        if(document.getElementById("actions-chart") !=null){
        			        	var ctx2 = document.getElementById("actions-chart").getContext("2d");
            			        var myNewChart2 = new Chart(ctx2).Line(actionsData, chartOptions);
        			        }
        			        
        			        
        		  });
    			  
    			  $scope.stringArray = [];
    			  $scope.visitiorListMap = [];
    			  
    			  apiserviceDashborad.getVisitorList(startDate,endDate).then(function(data){
    		  			
    		  			$scope.gridOptions.data = data;
    		  			$scope.visitiorList = data;
    		  			angular.forEach($scope.visitiorList, function(value, key) {
    		  				$scope.stringArray[value.geolocation] = {
    		  	    	            "flag" : 0,
    		  	    				"name" : value.geolocation
    		  	    	        };
    		  			});
    		  			
    		  			
    		  			var ab = 0;
    		  			angular.forEach($scope.visitiorList, function(value, key) {
    		  				
    		  					if($scope.stringArray[value.geolocation].name == value.geolocation && $scope.stringArray[value.geolocation].flag == 0){
    		  						$scope.visitiorListMap[ab] = value;
    		  						$scope.stringArray[value.geolocation].flag = 1;
    		  						$scope.stringArray[value.geolocation].value = 1;
    		  						ab = ab +1;
    		  					}else{
    		  						$scope.stringArray[value.geolocation].value = $scope.stringArray[value.geolocation].value + 1;
    		  					}
    		  			});
    		  			
    		  			
    		  			
    		  			angular.forEach($scope.visitiorListMap, function(value, key) {
    		  				if($scope.stringArray[value.geolocation].name == value.geolocation){
    		  					value.valueCount = $scope.stringArray[value.geolocation].value;
    		  				}
    		  			});
    		  			
    		  			dashboardService.init($scope.visitiorListMap);
    		            pluginsService.init();
    		            dashboardService.setHeights()
    		            if ($('.widget-weather').length) {
    						if($scope.userProfile == null){
    							widgetWeather("New York");
    						}else{
    							widgetWeather($scope.userProfile.address);
    						}
    		                
    		            }
    		            handleTodoList();
    		  		});
        		  
    		 }
    		 
    		
    		  setInterval(function(){
    			  $scope.onlineVisitorFind();
    			}, 10000)
    		  
    		$scope.onlineVisitorFind = function(){
    			  apiserviceDashborad.getVisitorOnline().then(function(data){
    				  $scope.onlineVisitorsCount = data;
    			  });
    		  }	
    			
    		  $scope.getAllVisitorList = function(){
    			  $location.path('/visitorsAnalytics/');
    		  }
    		  $scope.getAllActionList = function(){
    			  $location.path('/actionsAnalytics/');
    		  }
    		  $scope.serchesPage = function(){
    			  $location.path('/searchesAnalytics/');
    		  }
    		  $scope.refferPage = function(){
    			  $location.path('/refferersAnalytics/');
    		  }
    		  
    		  $scope.todoData = {};
    		  
    		  $scope.topPerformers = false;
    		  $scope.worstPerformers = false;
    		  $scope.weekPerformance = false;
    		  $scope.monthPerformance = false;
    		  $scope.yearPerformance = false;
    		  $scope.showLeads = false;
    		  
    		
    		  $scope.init = function() {
    			  
    			  //$scope.likeMsg();
    			  //$scope.invitationMsg();
    			  //$scope.decline();
    			 // $scope.acceptMsg();
    			  //$scope.deleteMeeting();
    			  $scope.PlanOnMonday();
    			  //$scope.updateMeeting();
    			  //$scope.planMsg();
    			  $scope.priceAlertMsg();
    			  
    			 $scope.check={};
    			  var date = new Date();
    			  
    			 
    			  
    			  var startdate= new Date(date.getFullYear(), date.getMonth(), 1);
  				$scope.check.startDate=$filter('date')(startdate, 'dd-MM-yyyy');
  				$scope.check.endDate=$filter('date')(date, 'dd-MM-yyyy');
  			  
  				$scope.startDateV = $filter('date')(startdate, 'yyyy-MM-dd');
  				$scope.endDateV = $filter('date')(date, 'yyyy-MM-dd');
  				$scope.visitorsStats($scope.startDateV, $scope.endDateV);
  				
  				$scope.volumeStatStartDate = $filter('date')(startdate, 'yyyy-MM-dd');
  				$scope.volumeStatEndDate = $filter('date')(date, 'yyyy-MM-dd');	
  				$scope.showVehicalBarChart($scope.volumeStatStartDate, $scope.volumeStatEndDate);
  				
  				$scope.startDateForListing = $filter('date')(startdate, 'dd-MM-yyyy');
  				$scope.endDateForListing = 	$filter('date')(date, 'dd-MM-yyyy');
  				
  				$scope.startDateForSalesPeople=$filter('date')(startdate, 'dd-MM-yyyy');
  				$scope.endDateForSalesPeople=$filter('date')(date, 'dd-MM-yyyy');
  			
  				 $scope.getPerformanceOfUser();
  				
    			 if($scope.locationValue == null){
    				 $scope.getSalesDataValue(0);
    			 }
    			
    			 
    			$scope.cal_whe_flag = true;
   			   	$(".wheth-report").hide();
   			   	$scope.checkManagerLogin();
   			   	
   			   	$scope.schedulmultidatepicker();
		   			 apiserviceDashborad.getUsersToAssign().then(function(data){
						$scope.usersList = data;
					});  
		    		  $scope.getToDoList();
		    		  $scope.getMonthChart();
		    		  $('#topPerf').css("text-decoration","underline");
		    		  $('#weekPerf').css("text-decoration","underline");
		    		  $scope.topPerformers = true;
		    		  $scope.weekPerformance = true;
	    			  $scope.showVehicalBarChart();

		    		  $scope.vehicleData("All",$scope.startDateForListing,$scope.endDateForListing);
		    		  $scope.heatMapShow($scope.startDateV,$scope.endDateV);
		    		  
		    		  $scope.getClickyVisitorListData();
    		  };  
    		  
    		  $scope.getClickyVisitorListData = function(){
    			  apiserviceDashborad.getClickyVisitorList().then(function(data){
    			  });
    		  }
    		  
    		  
    		  var d = new Date();
    		  var month = new Array();
    		  month[0] = "January";
    		  month[1] = "February";
    		  month[2] = "March";
    		  month[3] = "April";
    		  month[4] = "May";
    		  month[5] = "June";
    		  month[6] = "July";
    		  month[7] = "August";
    		  month[8] = "September";
    		  month[9] = "October";
    		  month[10] = "November";
    		  month[11] = "December";
    		  var monthNam = month[d.getMonth()];
    		  
    		  apiserviceDashborad.getPlanByMonthAndUser($scope.userKey,monthNam).then(function(data){
    			  if(data == 1){
						$scope.flagForPlan = 1;
					}
			  });
    		  
    		  apiserviceDashborad.getPlanByMonthAndUserForLocation($scope.userKey,monthNam).then(function(data){
    			  if(data == 1){
						$scope.flagForPlanForLocation = 1;
					}
			  });
    		  
    		
    		  $scope.heatMapShow = function(startD,endD){
    			  $scope.showHeatMap = 0;
    				$scope.gridOptions12 = {
    				 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    				 		    paginationPageSize: 150,
    				 		    enableFiltering: true,
    				 		    useExternalFiltering: true,
    				 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
    				 		 };
    				
    				
    				 $scope.gridOptions12.enableHorizontalScrollbar = 0;
    					 $scope.gridOptions12.enableVerticalScrollbar = 2;
    					 
    					 $scope.gridOptions12.columnDefs = [
    					                                 { name: 'title', displayName: 'Title', width:'30%',cellEditableCondition: false,
    					                                	cellTemplate:'<div><span ng-if="row.entity.title != \'Some page\'">{{row.entity.title}}</span></div>',
    					                                 },
    					                                 { name: 'showUrl', displayName: 'ShowUrl', width:'47%',cellEditableCondition: false,
    					                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    					                                       if (row.entity.isRead === false) {
    					                                         return 'red';
    					                                     }
    					                                	} ,
    					                                 },
    					                                 { name: 'value_percent', displayName: 'Value Percent', width:'10%',cellEditableCondition: false,
    					                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    					                                       if (row.entity.isRead === false) {
    					                                         return 'red';
    					                                     }
    					                                	} ,
    					                                 },
    					                                 { name: 'edit', displayName: '', width:'9%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
    			    		                                 cellTemplate:'<a ng-click="grid.appScope.showheatmap(row)"><img  ng-if="row.entity.title != \'Some page\'"  class="mb-2" style="margin-left: 8px;width: 21px;" title="View heatmap for this page" src="https://cdn.staticstuff.net/media/icon_heatmap.png"></a>', 
    					                                 
    					                                 },
    					                                 ];
    				
    					 $scope.gridOptions.onRegisterApi = function(gridApi){
    						 $scope.gridApi = gridApi;
    						 
    				   		$scope.gridApi.core.on.filterChanged( $scope, function() {
    					          var grid = this.grid;
    					          $scope.gridOptions12.data = $filter('filter')($scope.heatMapList,{'title':grid.columns[0].filters[0].term,'showUrl':grid.columns[1].filters[0].term,'value_percent':grid.columns[2].filters[0].term},undefined);
    					        });
    				   		
    			 		};
    			 		
    			 		
    			 		apiserviceDashborad.getHeatMapListDale(startD,endD).then(function(data){
    			 			
    							$scope.gridOptions12.data = data;
    							$scope.heatMapList = data;
    							/*$scope.gridOptions12.data = data[0].dates[0].items;
    							$scope.heatMapList = data[0].dates[0].items;
    							angular.forEach($scope.gridOptions12.data, function(value, key) {
    								$scope.array = value.url.split('#');
    								$scope.gridOptions12.data[key].showUrl = $scope.array[0];
    								$scope.heatMapList[key].showUrl = $scope.array[0];
    							});*/
    							
    						$('#sliderBtn').click();
    					});
    					 
    					 
    					
    		  }

    		  $scope.showheatmap = function(row){
					 $scope.showHeatMap = 1;
					 var data = row.entity.url;
					 $('#heatMapModal').modal();
					 $(".container-iframe-sit").attr("src",data);
					 
				 }

		/*	$scope.likeMsg = function() {

								$http
										.get('/getcommentLike')
										.success(
												function(data) {
													angular.forEach(data, function(value, key) {
														var notifContent = '<div class="alert alert-dark media fade in bd-0" id="message-alert"><div class="media-left"></div>'
															+ '<div class="media-body width-100p col-md-12" style="padding: 0px;"><div class="col-md-3" style="padding: 0px;"><img style="width: 120px;" src="'+value.imageUrl+'"></div><div class="col-md-9"><div class="col-md-12" style="text-align: center;"><h2 style="color: goldenrod;margin-top: 0px;">Congratulations!</h2></div><span class="col-md-12" style="margin-left: 22px;text-align: center;"><h3 style="margin-top: 0px;"><span>'+value.firstName+'  '+ value.lastName+'</span><br><span> just Liked your work!</span><br><span>'+value.userComment+'</span></h3></span><p class="pull-left" style="margin-left:85%;"><a class="f-12">Close&nbsp;</a></p></div></div>'
															+ '</div>';
													var position = 'topRight';
													if ($('body').hasClass(
															'rtl'))
														position = 'topLeft';
													var n = noty({
														text : notifContent,
														type : 'success',
														layout : position,
														theme : 'made',
														animation : {
															open : 'animated bounceIn',
															close : 'animated bounceOut'
														},

														callback : {
															onShow : function() {
																$(
																		'#noty_topRight_layout_container, .noty_container_type_success')
																		.css(
																				
																				'width',
																				410)
																		.css('margin-left', -82)
																		.css(
																				
																				'bottom',
																				10);
															},
															onCloseClick : function() {
																$('html, body')
																		.animate(
																				{
																					scrollTop : 480
																				},
																				'slow');
															}
														}
													});
													});
									
												});

							}*/
			
			
			 
			
			
		/*	$scope.planMsg = function(){
				
				$http.get('/getPlanMsg')
	    		.success(function(data){
	    				var notifContent;
	    				angular.forEach(data, function(value, key) {
	    					var month=value.month;
	    					if(month !=null){
	    						month = month.toLowerCase();
	    						month=month.substring(0,1).toUpperCase()+month.substring(1);
	    					}
	    					
	    				notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span> "+month+"'s plan has been assigned</span></p></div></div>";
	    				
	    				var position = 'topRight';
		    	        if ($('body').hasClass('rtl'))
		    	        	position = 'topLeft';
		    	        var n = noty({
							text : notifContent,
							type : 'success',
							layout : position,
							theme : 'made',
							buttons: [
							          {
							              addClass: 'general-button customLeft', text: 'See', onClick: function($noty)
							              {
							            	  
							            	  $scope.planForsalePersonForMonth(value.month);
							                 $noty.close();
							              }
							          }
									 ],
							animation : {
								open : 'animated bounceIn',
								close : 'animated bounceOut'
							},

							callback : {
								onShow : function() {
									$(
											'#noty_topRight_layout_container, .noty_container_type_success')
											.css(
													
													'width',
													477)
											.css('margin-left', -135)
											.css(
													
													'bottom',
													10);
								},
								onCloseClick : function() {
									$('html, body')
											.animate(
													{
														scrollTop : 480
													},
													'slow');
								}
							}
						});
		    	        
		    	        var element = $('#cnt');
						$compile(element)($scope);
	    				});
	    		});
			}*/
			
		/*	$scope.updateMeeting = function(){

				$http.get('/getUpdateMeeting')
	    		.success(function(data){
	    				var notifContent;
	    				angular.forEach(data, function(value, key) {
	    				var t = $filter('date')(value.confirmTime,"hh:mm a");
	    				notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span>"+value.name+" information has been changed</span><br><span>"+value.confirmDate+"   "+value.confirmTime+" - "+value.confirmEndTime+"</span><br><span>"+value.reason+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
	    				
	    				var position = 'topRight';
		    	        if ($('body').hasClass('rtl')) position = 'topLeft';
		    	        var n = noty({
		    	            text: notifContent,
		    	            type: 'success',
		    	            layout: position,
		    	            theme: 'made',
		    	            animation: {
		    	                open: 'animated bounceIn',
		    	                close: 'animated bounceOut'
		    	            },
		    	            
		    	            callback: {
		    	                onShow: function () {
		    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('margin-left', -30).css('bottom', 10);
		    	                },
		    	                onCloseClick: function () {
		    	                	$('html, body').animate({scrollTop:480}, 'slow');
		    	                }
		    	            }
		    	        });
		    	        
		    	        var element = $('#cnt');
						$compile(element)($scope);
	    				});
	    		});
			}*/
			
			
		/*	$scope.acceptMsg = function(){

				
				$http.get('/getaccepted')
	    		.success(function(data){
	    			
	    				var notifContent;
	    				angular.forEach(data, function(value, key) {
	    				notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span>"+value.assignedTo.firstName+"&nbsp;&nbsp;"+value.assignedTo.lastName+" accepted your invitation to "+value.name+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
	    				
	    				var position = 'topRight';
		    	        if ($('body').hasClass('rtl')) position = 'topLeft';
		    	        var n = noty({
		    	            text: notifContent,
		    	            type: 'success',
		    	            layout: position,
		    	            theme: 'made',
		    	            animation: {
		    	                open: 'animated bounceIn',
		    	                close: 'animated bounceOut'
		    	            },
		    	            
		    	            callback: {
		    	                onShow: function () {
		    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('margin-left', -30).css('bottom', 10);
		    	                },
		    	                onCloseClick: function () {
		    	                	$('html, body').animate({scrollTop:480}, 'slow');
		    	                }
		    	            }
		    	        });
		    	        
		    	        var element = $('#cnt');
						$compile(element)($scope);
	    				});
	    		});
			}*/
			/*$scope.decline = function(){
				
				
				
				$http.get('/getdecline')
	    		.success(function(data){

	    				var notifContent;
	    				angular.forEach(data, function(value, key) {
	    				notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span> Your invitation to "+value.assignedTo.firstName+"&nbsp;&nbsp;"+value.assignedTo.lastName+" has been declined</span><br><span> Reason: "+value.declineReason+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
	    				
	    				var position = 'topRight';
		    	        if ($('body').hasClass('rtl')) position = 'topLeft';
		    	        var n = noty({
		    	            text: notifContent,
		    	            type: 'success',
		    	            layout: position,
		    	            theme: 'made',
		    	            animation: {
		    	                open: 'animated bounceIn',
		    	                close: 'animated bounceOut'
		    	            },
		    	            
		    	            callback: {
		    	                onShow: function () {
		    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('bottom', 10);
		    	                },
		    	                onCloseClick: function () {
		    	                	$('html, body').animate({scrollTop:480}, 'slow');
		    	                }
		    	            }
		    	        });
		    	        
		    	        var element = $('#cnt');
						$compile(element)($scope);
	    				});
	    		});
				
				
			}*/
			 $scope.callForLocalCheck = function(){
				   $scope.aValue;
				   $scope.aValue = localStorage.getItem('flagForNoty');
			   }
			 
			 $scope.priceAlertMsg = function(){
				 
				 apiserviceDashborad.sendComingSoonPOpUp().then(function(data){
						angular.forEach(data, function(value, key) {
							if(value.notifFlag != 1||$rootScope.comingSoonFlag ==1){
							var notifContent = '<div class="alert alert-dark media fade in bd-0" id="message-alert"><div class="media-left"></div>'
								+ '<div class="media-body width-100p col-md-12" style="padding: 0px;border-bottom: solid;margin-bottom: 7px;"><div class="col-md-3" style="padding: 0px;"><img style="width: 165px;margin-top: 55px;" src="'+value.imageUrl+'"></div><div class="col-md-9"><div class="col-md-12" style="text-align: center;"><h3 style="margin-top: 0px;color: cornsilk;font-size: 16px;"><b>Vehicle Arriaval</b></h3></div><span class="col-md-12" style="margin-left: 22px;font-size: 16px;"><h3><span style="font-size: 16px;"><b>Year    : </b></span><span style="font-size: 16px;">'+value.year+'</span><br><span  style="font-size: 16px;"><span><b>Make   : </b></span>'+value.make+'</b></span><br><span  style="font-size: 16px;"><span><b>Model  : </b></span>'+value.model+'</b></span><br><span  style="font-size: 16px;"><span><b>Price     : </b></span>'+value.price+'</b></span>'
								+'<br><span  style="font-size: 16px;"><span><b>Vin    :  </b></span>'+value.vin+'</b></span>'
								+'<br><span  style="font-size: 16px;"><span><b>Scheduled Arrival Date : </b></span>'+value.comingSoonDate+'</b></span>'
								+'<br><span  style="font-size: 16px;"><span><b>Subscribers : </b></span>'+value.subscribers+'</b></span></h3></span><p class="pull-left" style="margin-left:85%;"></p></div></div>'
								+ '</div>';
						var position = 'topRight';
						if ($('body').hasClass(
								'rtl'))
							position = 'topLeft';
						var n = noty({
							text : notifContent,
							type : 'success',
							layout : position,
							theme : 'made',
							buttons: [
							          {
									        addClass: 'general-button btnText', text: 'Change Arrival Date', onClick: function($noty)
									              {
									            	  $scope.changeArrivalDate(value);
		
									                 $noty.close();
									              }
									          },
							          {
							              addClass: 'general-button btnText', text: 'Make it live  & Notify All', onClick: function($noty)
							              {
							            	  $scope.makeIt(value);
							                 $noty.close();
							              }
							          },
							          {
							              addClass: 'general-button btnText', text: 'Add or Change Price ', onClick: function($noty)
							              {
							            	 // $scope.acceptDate(value);
							            	  $scope.addPrice(value);
							                 $noty.close();
							              }
							          },
							          {
							              addClass: 'general-button btnText', text: 'Close', onClick: function($noty)
							              {
							            	 // $scope.acceptDate(value);
							            	  $scope.setNotifFlag(value);
							                 $noty.close();
							              }
							          },
							          
									 ],
							animation : {
								open : 'animated bounceIn',
								close : 'animated bounceOut'
							},

							callback : {
								onShow : function() {
									$(
											'#noty_topRight_layout_container, .noty_container_type_success')
											.css(
													
													'width',
													555)
											.css('margin-left', -207)
											.css(
													
													'bottom',
													10);
								},
								onCloseClick : function() {
									$('html, body')
											.animate(
													{
														scrollTop : 480
													},
													'slow');
								}
							}
						});
						}
						});
		    		});
			 }		
			 
			 $scope.setNotifFlag= function(value){
				 console.log(value);
				 apiserviceDashborad.changeVehicleNotif(value.id).then(function(data){
					 
				 });
			 }
			 
			$scope.PlanOnMonday = function(){
				 apiserviceDashborad.getPlanMonday().then(function(data){
	    			if(data == 1){
	    				$scope.callForLocalCheck();
	    				if($scope.aValue == false){
	    					var notifContent;
	    					notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span>Sales Plan for this month has been added</span><br></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'></p></div></div>";
	    					var position = 'topRight';
					if ($('body').hasClass(
							'rtl'))
						position = 'topLeft';
					var n = noty({
						text : notifContent,
						type : 'success',
						layout : position,
						theme : 'made',
						buttons: [
									{
									    addClass: 'general-button btnText', text: 'Close', onClick: function($noty)
									    {
									       $noty.close();
									    }
									},  
						          {
								        addClass: 'general-button btnText', text: 'See sales Plan', onClick: function($noty)
								              {
								        	if($scope.flagForPlanForLocation != 1 && $scope.userRole == "Manager"){
								        		$scope.planForLocationManager();
								        	}
								        	if($scope.flagForPlan != 1 && $scope.userRole == "Sales Person"){
								        		$scope.planForsalePerson();
								        	}
								        	localStorage.setItem('flagForNoty', 'true'); 
								                 $noty.close();
								              }
								          }
								            
						          
								 ],
						animation : {
							open : 'animated bounceIn',
							close : 'animated bounceOut'
						},

						callback : {
							onShow : function() {
								$(
										'#noty_topRight_layout_container, .noty_container_type_success')
										.css(
												
												'width',
												477)
										.css('margin-left', -130)
										.css(
												
												'bottom',
												10);
							},
							onCloseClick : function() {
								$('html, body')
										.animate(
												{
													scrollTop : 480
												},
												'slow');
							}
						}
					});
	    			}
	    			}
	    			else{
	    				
	    				localStorage.setItem('flagForNoty', 'false'); 
	    			}
	    			
	    				
	    		});
			}
			
			
		/*	$scope.deleteMeeting = function(){
				
				$http.get('/getdeleteMeeting')
	    		.success(function(data){

	    				var notifContent;
	    				angular.forEach(data, function(value, key) {
	    					if(value.declineUser == 'Host'){
	    						notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span>"+value.name+" has been cancelled</span><br></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
	    	    				
	    	    				var position = 'topRight';
	    		    	        if ($('body').hasClass('rtl')) position = 'topLeft';
	    		    	        var n = noty({
	    		    	            text: notifContent,
	    		    	            type: 'success',
	    		    	            layout: position,
	    		    	            theme: 'made',
	    		    	            animation: {
	    		    	                open: 'animated bounceIn',
	    		    	                close: 'animated bounceOut'
	    		    	            },
	    		    	            
	    		    	            callback: {
	    		    	                onShow: function () {
	    		    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('bottom', 10);
	    		    	                },
	    		    	                onCloseClick: function () {
	    		    	                	$('html, body').animate({scrollTop:480}, 'slow');
	    		    	                }
	    		    	            }
	    		    	        });
	    		    	        
	    		    	        var element = $('#cnt');
	    						$compile(element)($scope);
	    					}else if(value.declineUser == 'this person'){
	    						notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span> "+value.firstName+"&nbsp;&nbsp;"+value.lastName+" can't go to the "+value.name+"</span><br><span>"+value.confirmDate+"&nbsp;&nbsp"+value.confirmTime+"</span><br><span>"+value.reason+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
	    	    				
	    	    				var position = 'topRight';
	    		    	        if ($('body').hasClass('rtl')) position = 'topLeft';
	    		    	        var n = noty({
	    		    	            text: notifContent,
	    		    	            type: 'success',
	    		    	            layout: position,
	    		    	            theme: 'made',
	    		    	            animation: {
	    		    	                open: 'animated bounceIn',
	    		    	                close: 'animated bounceOut'
	    		    	            },
	    		    	            
	    		    	            callback: {
	    		    	                onShow: function () {
	    		    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('bottom', 10);
	    		    	                },
	    		    	                onCloseClick: function () {
	    		    	                	$('html, body').animate({scrollTop:480}, 'slow');
	    		    	                }
	    		    	            }
	    		    	        });
	    		    	        
	    		    	        var element = $('#cnt');
	    						$compile(element)($scope);
	    					}
	    				
	    				});
	    		});
				
				
			}*/
			
			
		/*	$scope.invitationMsg = function() {

				$http
						.get('/getinvitationMsg')
						.success(
								function(data) {
									angular.forEach(data, function(value, key) {
										var notifContent = '<div class="alert alert-dark media fade in bd-0" id="message-alert"><div class="media-left"></div>'
											+ '<div class="media-body width-100p col-md-12" style="padding: 0px;"><div class="col-md-3" style="padding: 0px;"><img style="width: 120px;" src="'+value.imageUrl+'"></div><div class="col-md-9"><div class="col-md-12" style="text-align: center;"><h3 style="margin-top: 0px;">New meeting invitation received</h3></div><span class="col-md-12" style="margin-left: 22px;text-align: center;border-bottom: solid;"><h3><span>'+value.name+'</span><br><span style="color: cornflowerblue;"><b>'+value.confirmDate+'&nbsp;&nbsp;&nbsp;&nbsp;'+value.confirmTime+' </b></span></h3></span><hr><p class="pull-left" style="margin-left:85%;"></p></div></div>'
											+ '</div>';
									var position = 'topRight';
									if ($('body').hasClass(
											'rtl'))
										position = 'topLeft';
									var n = noty({
										text : notifContent,
										type : 'success',
										layout : position,
										theme : 'made',
										buttons: [
										          {
												        addClass: 'general-button btnText', text: 'Decline', onClick: function($noty)
												              {
												            	  $scope.declineDate(value);
												                 $noty.close();
												              }
												          },
										          {
										              addClass: 'general-button btnText', text: 'Accept', onClick: function($noty)
										              {
										            	  $scope.acceptDate(value);
										                 $noty.close();
										              }
										          }
												 ],
										animation : {
											open : 'animated bounceIn',
											close : 'animated bounceOut'
										},

										callback : {
											onShow : function() {
												$(
														'#noty_topRight_layout_container, .noty_container_type_success')
														.css(
																
																'width',
																477)
														.css('margin-left', -135)
														.css(
																
																'bottom',
																10);
											},
											onCloseClick : function() {
												$('html, body')
														.animate(
																{
																	scrollTop : 480
																},
																'slow');
											}
										}
									});
									});
					
								});

			}*/
			
			
			$('#button-0').css("background-color","black");
			$('#button-1').css("background-color","black");
			
			/*$scope.acceptDate = function(value){
				var reason = null;
				
				 $http.get('/getAcceptAndDecline/'+value.id+"/"+reason+"/"+"accept")
					.success(function(data) {
						$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Meeting invitation has been accepted",
						});
						
						$scope.schedulmultidatepicker();
						$http.get("/getscheduletest").success(function(data){
							   $scope.scheduleListData = data;
						   });
						
					});
				
			}*/
			
			$scope.declineDate = function(value){
				$('#decline-model').modal();
				$scope.valueId = value;
			}
			
			$scope.declineMeeting = function(reason){
				
				apiserviceDashborad.getAcceptAndDecline($scope.valueId.id, reason, "decline").then(function(data){
					
					 $('#decline-model').modal("toggle");
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Meeting invitation has been declined",
					});
					
				});
			}
			
			$scope.makeIt = function(value){
				console.log(value);
				$scope.vinForPopup=value.vin;
				if(value.price == 0){
					$scope.priceDetail = value;
					$('#addPriceOther').modal();
				}
				else{
					$scope.addMakeIt($scope.vinForPopup);
				}
			}

			$scope.addByPriceMakeIt = function(price){
				 apiserviceDashborad.getAddPrice($scope.priceDetail.id,price,$scope.arrivalD).then(function(data){
						 $('#addPriceOther').modal("toggle");
						
						$scope.addMakeIt($scope.vinForPopup);
						
					});
				 
			 }
			
			$scope.addMakeIt = function(vin){
				console.log("inside coming soon");
				console.log(vin);
				
				apiserviceDashborad.sendComingSoonEmail(vin).then(function(data){
					 // $scope.indexInitFunction();
					$scope.notifCount();
					 $('#addPrice').modal("toggle");
				});
			}
			
			$scope.changeArrivalDate = function(value){
          	  $('#changeDate').modal();
          	  console.log(value);
          	  $scope.arrDate = value.comingSoonDate;
          	  $scope.priceDetail = value;
      	  	}
			
			$scope.addChangeArrival = function(){
				var aDate = $('#arrivalDate').val();
				console.log(aDate);
				console.log("$scope.buttFlagNew"+$scope.buttFlagNew);
				
				apiserviceDashborad.setArrivelDate($scope.priceDetail.id,aDate).then(function(data){
					
					 $('#changeDate').hide();
					// $scope.indexInitFunction();
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Arrival date Change successfully",
					});
					console.log($scope.buttFlagNew);
					if($scope.buttFlagNew != 1){
				//	$scope.notifCount();
					}
					
				});
			}

			$scope.buttFlag = 0;
			 $scope.addPrice = function(value){
				 
				 $scope.vinForPriceChange=value.vin;
				 $scope.buttFlag = 0;
				 $scope.arrDate = value.comingSoonDate;
				 $scope.checkDate = value.comingSoonDate;
				 $scope.price = value.price;
				 if($scope.price == 0){
					 $scope.buttFlag = 0;
				 }else{
					 $scope.buttFlag = 1;
				 }
				$('#addPrice').modal();
      		  	$scope.priceDetail = value;
      	  	}
			 $scope.addMakeItInPrice = function(){
				 $scope.arrDate = $('#arrDate').val();
				 console.log($scope.arrDate);
				 if($scope.arrDate != $scope.checkDate){
					 $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "coming soon date not match with current date",
						});
				 }else{
					 $scope.addMakeIt($scope.vinForPriceChange);
				 }
			 }
			 
			 
			 $scope.buttFlagNew=0;
			 $scope.addPriceInVeh = function(price){
				 
				 var aDate = $('#changeArrivalDate').val();
				 console.log(aDate);
				 $scope.arrivalD=aDate;
				 apiserviceDashborad.getAddPrice($scope.priceDetail.id, price, aDate).then(function(data){
						 //$scope.indexInitFunction();
						//$scope.notifictionCount=$scope.notifictionCount-1;
						 $('#addPrice').modal("toggle");
						
						$scope.buttFlagNew = 1;
						$scope.notifCount();
						
					});
				 $('#arrivalDate').val($('#changeArrivalDate').val());
				 $scope.addChangeArrival();
			 }
			
    		  
    		  $scope.schedulmultidatepicker = function(){
    			  $scope.showToDoList = false;
				  $scope.showCalendar = true;
				  
				  apiserviceDashborad.getScheduleDates().then(function(data){
						$scope.scheduleDates = data;
						 var datesArray = [];
						 for(var i=0;i<$scope.scheduleDates.length;i++) {
							 var dateStr = $scope.scheduleDates[i].confirmDate;
							 var date = new Date();
							 var arr = [];
							
							 if(dateStr != null){
							    arr = dateStr.split('-');
					        	date.setYear(arr[0]);
					        	var month = arr[1];
					        	date.setMonth(month-1);
					        	date.setDate(arr[2]);
					        	datesArray.push(date);
							 }	
						 }
						 
						// set up some code to be executed later, in 5 seconds (5000 milliseconds):
						  setTimeout(function () {
							  $(".multidatepicker").multiDatesPicker({
			    			  		addDates:datesArray,
			        			  onSelect: function(dateText, inst){
			        				  $scope.showToDoList = true;
			        				  $scope.showCalendar = false;
			        				  $scope.selectedDate = dateText;
			        				  $scope.editdate = dateText;
			        				  $scope.getScheduleBySelectedDate($scope.editdate);
			        			  }
			    		  });
						}, 5000);
						  
					});
    		  }
    		  
    		  $scope.getToDoList = function() {
    			  apiserviceDashborad.getToDoList().then(function(data){
    				  $scope.toDoList = data;
    			  });
    			  
    		  }
    		  
    		  $scope.showCalendarData = function() {
    			  $scope.showToDoList = false;
				  $scope.showCalendar = true;
				  $scope.schedulmultidatepicker();
				  //$scope.init();
    		  }
    		  
    		  $scope.deleteForeverLead = function(entity) {
    			  $scope.leadId = entity.id;
    			  $scope.leadType = entity.leadType;
    			  $('#btnDeleteForever').click();
    		  }
    		  
    		  $scope.comparisonSalePerson = function(){
    			  var i = 0;
    			 
    			  $scope.leadPer = [];
    			  $scope.leadName={};
    			  
    			  $scope.offLeadPer = [];
    			  $scope.offLeadName={};
    			  if($scope.flagvalue == 2){
    				  
    				  
    				  
    				  angular.forEach($scope.comparisonperson[0].byType, function(value, key) {
    					  $scope.offLeadName={};
    					  
    					  angular.forEach($scope.comparisonperson[1].byType, function(value1, key1) {
        						
    						  if($scope.comparisonperson[0].byType[key].name == $scope.comparisonperson[1].byType[key1].name){
    							  
    							  if($scope.comparisonperson[0].byType[key].value > $scope.comparisonperson[1].byType[key1].value ){
        							  $scope.comparisonperson[0].byType[key].flag=1;
        	    				  }else if ($scope.comparisonperson[0].byType[key].value == $scope.comparisonperson[1].byType[key1].value) {
        	    					  $scope.comparisonperson[1].byType[key1].flag=2;
        	    					  $scope.comparisonperson[0].byType[key].flag=2;
								}
    							  else{
        	    					  $scope.comparisonperson[1].byType[key1].flag=0;
        	    				  }
    							  
    						  }
        					  
        	     			});
    	     			});
    				  
    				  
    				  
    				  
    				
    				  angular.forEach($scope.comparisonperson[0].priceRang, function(value, key) {
    					  $scope.offLeadName={};
    					  
    					  angular.forEach($scope.comparisonperson[1].priceRang, function(value1, key1) {
        						
    						  if($scope.comparisonperson[0].priceRang[key].name == $scope.comparisonperson[1].priceRang[key1].name){
    							  
    							  if($scope.comparisonperson[0].priceRang[key].value > $scope.comparisonperson[1].priceRang[key1].value ){
        							  $scope.comparisonperson[0].priceRang[key].flag=1;
        	    				  }else if ($scope.comparisonperson[0].priceRang[key].value == $scope.comparisonperson[1].priceRang[key1].value) {
        	    					  $scope.comparisonperson[0].priceRang[key].flag=2;
        	    					  $scope.comparisonperson[1].priceRang[key1].flag=2;
        	    					  
								}
    							  else{
        	    					  $scope.comparisonperson[1].priceRang[key1].flag=0;
        	    				  }
    						  }
        	     			});
    	     			});
    				  angular.forEach($scope.comparisonperson[0].planComplete, function(value, key) {
    					  $scope.offLeadName={};
    					  
    					  angular.forEach($scope.comparisonperson[1].planComplete, function(value1, key1) {
    						  
    						  if($scope.comparisonperson[0].planComplete[key].name == $scope.comparisonperson[1].planComplete[key1].name){
        						  
        						  if($scope.comparisonperson[0].planComplete[key].value > $scope.comparisonperson[1].planComplete[key1].value){
        							  $scope.comparisonperson[0].planComplete[key].flag=1;
        	    				  }else if($scope.comparisonperson[0].planComplete[key].value == $scope.comparisonperson[1].planComplete[key1].value){
        	    					  $scope.comparisonperson[1].planComplete[key1].flag=2;
        	    					  $scope.comparisonperson[0].planComplete[key].flag=2;
        	    				  }
        						  else{
        	    					  $scope.comparisonperson[1].planComplete[key].flag=0;
        	    				  }
    						  }
        	     			});
    	     			});
    				  
    				  angular.forEach($scope.comparisonperson[0].offlineLead, function(value, key) {
    					  $scope.offLeadName={};
    					  
    					  angular.forEach($scope.comparisonperson[1].offlineLead, function(value1, key1) {
    						  
    						  if($scope.comparisonperson[0].offlineLead[key].name == $scope.comparisonperson[1].offlineLead[key1].name){
        						  if($scope.comparisonperson[0].offlineLead[key].value > $scope.comparisonperson[1].offlineLead[key1].value){
        							  $scope.comparisonperson[0].offlineLead[key].flag=1;
        	    					  $scope.offLineLeadPer = (($scope.comparisonperson[0].offlineLead[key].value - $scope.comparisonperson[1].offlineLead[key1].value) * 100 / $scope.comparisonperson[0].offlineLead[key].value).toFixed(2);
        	    				  }else if($scope.comparisonperson[0].offlineLead[key].value == $scope.comparisonperson[1].offlineLead[key1].value){
        	    					  
        	    					  $scope.offLineLeadPer = (($scope.comparisonperson[0].offlineLead[key].value - $scope.comparisonperson[1].offlineLead[key1].value) * 100 / $scope.comparisonperson[0].offlineLead[key].value).toFixed(2);
        	    					  $scope.comparisonperson[1].offlineLead[key1].flag=2;
        	    					  $scope.comparisonperson[0].offlineLead[key].flag=2;
        	    				  }
        						  
        						  else{
        	    					  $scope.comparisonperson[1].offlineLead[key1].flag=0;
        	    					  $scope.offLineLeadPer = (($scope.comparisonperson[1].offlineLead[key1].value - $scope.comparisonperson[0].offlineLead[key].value) * 100 / $scope.comparisonperson[1].offlineLead[key1].value).toFixed(2);
        	    				  }
        						  
        						  $scope.offLeadName.name =$scope.comparisonperson[1].offlineLead[key1].name;
        						  $scope.offLeadName.value = $scope.offLineLeadPer;
        						  $scope.offLeadPer.push($scope.offLeadName);
    						  }
        					  
        					  
        	     			});
    					  
    					  
    	     			});
    				  
    				  
    				  angular.forEach($scope.comparisonperson[0].onLineLead, function(value, key) {
    					  $scope.leadName={};
    					  $scope.onlineLeadFlag;
    					  angular.forEach($scope.comparisonperson[1].onLineLead, function(value1, key1) {
    						  
    						  if($scope.comparisonperson[0].onLineLead[key].name == $scope.comparisonperson[1].onLineLead[key1].name){
        						  if($scope.comparisonperson[0].onLineLead[key].value > $scope.comparisonperson[1].onLineLead[key1].value){
        							  $scope.comparisonperson[0].onLineLead[key].flag=1;
        	    					  $scope.onLineLeadPer = (($scope.comparisonperson[0].onLineLead[key].value - $scope.comparisonperson[1].onLineLead[key1].value) * 100 / $scope.comparisonperson[0].onLineLead[key].value).toFixed(2);
        	    				  }else if($scope.comparisonperson[0].onLineLead[key].name == $scope.comparisonperson[1].onLineLead[key1].name){
        	    					  $scope.comparisonperson[1].onLineLead[key1].flag=2;
        	    					  $scope.comparisonperson[0].onLineLead[key].flag=2;
        	    					  $scope.onLineLeadPer = (($scope.comparisonperson[0].onLineLead[key].value - $scope.comparisonperson[1].onLineLead[key1].value) * 100 / $scope.comparisonperson[0].onLineLead[key].value).toFixed(2);  
        	    				  }
        						  else{
        	    					  $scope.comparisonperson[1].onLineLead[key1].flag=0;
        	    					  $scope.onLineLeadPer = (($scope.comparisonperson[1].onLineLead[key1].value - $scope.comparisonperson[0].onLineLead[key].value) * 100 / $scope.comparisonperson[1].onLineLead[key1].value).toFixed(2);
        	    				  }
        						  $scope.leadName.name =$scope.comparisonperson[1].onLineLead[key1].name;
        						  $scope.leadName.value = $scope.onLineLeadPer;
        						  $scope.leadPer.push($scope.leadName);
    						  }
        	     			});
    	     			});
    				  
    				  if($scope.comparisonperson[0].avgLeadLifeCycle > $scope.comparisonperson[1].avgLeadLifeCycle){
    					  $scope.totalAvgLeadLifeCyclePer = (($scope.comparisonperson[0].avgLeadLifeCycle - $scope.comparisonperson[1].avgLeadLifeCycle) * 100 / $scope.comparisonperson[0].avgLeadLifeCycle).toFixed(2);
    					  
    				  }else if($scope.comparisonperson[0].avgLeadLifeCycle == $scope.comparisonperson[1].avgLeadLifeCycle){
    					  
    					  $scope.totalAvgLeadLifeCyclePer = (($scope.comparisonperson[0].avgLeadLifeCycle - $scope.comparisonperson[1].avgLeadLifeCycle) * 100 / $scope.comparisonperson[0].avgLeadLifeCycle).toFixed(2);
    				  }
    				  
    				  else{
    					  $scope.totalAvgLeadLifeCyclePer = (($scope.comparisonperson[1].avgLeadLifeCycle - $scope.comparisonperson[0].avgLeadLifeCycle) * 100 / $scope.comparisonperson[1].avgLeadLifeCycle).toFixed(2);
    					  
    				  }

    				  if($scope.comparisonperson[0].followUpTime > $scope.comparisonperson[1].followUpTime){
    					  $scope.totalFollowUpTimePer = (($scope.comparisonperson[0].followUpTime - $scope.comparisonperson[1].followUpTime) * 100 / $scope.comparisonperson[0].followUpTime).toFixed(2);
    					  
    				  }else if($scope.comparisonperson[0].followUpTime == $scope.comparisonperson[1].followUpTime){
    					  
    					  $scope.totalFollowUpTimePer = (($scope.comparisonperson[0].followUpTime - $scope.comparisonperson[1].followUpTime) * 100 / $scope.comparisonperson[0].followUpTime).toFixed(2);
    				  }
    				  else{
    					  $scope.totalFollowUpTimePer = (($scope.comparisonperson[1].followUpTime - $scope.comparisonperson[0].followUpTime) * 100 / $scope.comparisonperson[1].followUpTime).toFixed(2);
    				  }
    				  
    				  
    				  if($scope.comparisonperson[0].salary > $scope.comparisonperson[1].salary){
    					  $scope.totalsalaryPer = (($scope.comparisonperson[0].salary - $scope.comparisonperson[1].salary) * 100 / $scope.comparisonperson[0].salary).toFixed(2);
    				  }
    				  else if($scope.comparisonperson[0].salary == $scope.comparisonperson[1].salary){
    					  
    					  $scope.totalsalaryPer = (($scope.comparisonperson[0].salary - $scope.comparisonperson[1].salary) * 100 / $scope.comparisonperson[0].salary).toFixed(2);
    				  }
    				  else{
    					  $scope.totalsalaryPer = (($scope.comparisonperson[1].salary - $scope.comparisonperson[0].salary) * 100 / $scope.comparisonperson[1].salary).toFixed(2);
    				  }
    				  
    				  
    				  if($scope.comparisonperson[0].leadCost > $scope.comparisonperson[1].leadCost){
    					  $scope.leadCostPer = (($scope.comparisonperson[0].leadCost - $scope.comparisonperson[1].leadCost) * 100 / $scope.comparisonperson[0].leadCost).toFixed(2);
    				  }
    				  else if($scope.comparisonperson[0].leadCost == $scope.comparisonperson[1].leadCost){
    					  
    					  $scope.leadCostPer = (($scope.comparisonperson[0].leadCost - $scope.comparisonperson[1].leadCost) * 100 / $scope.comparisonperson[0].leadCost).toFixed(2);
    				  }
    				  else{
    					  $scope.leadCostPer = (($scope.comparisonperson[1].leadCost - $scope.comparisonperson[0].leadCost) * 100 / $scope.comparisonperson[1].leadCost).toFixed(2);
    				  }
    				  
    				  
    				  
    				  if($scope.comparisonperson[0].totalSalePrice > $scope.comparisonperson[1].totalSalePrice){
    					  $scope.totalSalePricePer = (($scope.comparisonperson[0].totalSalePrice - $scope.comparisonperson[1].totalSalePrice) * 100 / $scope.comparisonperson[0].totalSalePrice).toFixed(2);
    					   
    				  }else if($scope.comparisonperson[0].totalSalePrice == $scope.comparisonperson[1].totalSalePrice){
    					  $scope.totalSalePricePer = (($scope.comparisonperson[0].totalSalePrice - $scope.comparisonperson[1].totalSalePrice) * 100 / $scope.comparisonperson[0].totalSalePrice).toFixed(2);
    				  }
    				  else{
    					  $scope.totalSalePricePer = (($scope.comparisonperson[1].totalSalePrice - $scope.comparisonperson[0].totalSalePrice) * 100 / $scope.comparisonperson[1].totalSalePrice).toFixed(2);
    					
    				  }
    				  
    				  if($scope.comparisonperson[0].totalsaleCar > $scope.comparisonperson[1].totalsaleCar){
    					  $scope.totalsaleCarPer = (($scope.comparisonperson[0].totalsaleCar - $scope.comparisonperson[1].totalsaleCar) * 100 / $scope.comparisonperson[0].totalsaleCar).toFixed(2);
    					 
    				  }
    				  else if($scope.comparisonperson[0].totalsaleCar == $scope.comparisonperson[1].totalsaleCar){
    					  
    					  $scope.totalsaleCarPer = (($scope.comparisonperson[0].totalsaleCar - $scope.comparisonperson[1].totalsaleCar) * 100 / $scope.comparisonperson[0].totalsaleCar).toFixed(2);
    				  }
    				  else{
    					  $scope.totalsaleCarPer = (($scope.comparisonperson[1].totalsaleCar - $scope.comparisonperson[0].totalsaleCar) * 100 / $scope.comparisonperson[1].totalsaleCar).toFixed(2);  					  
    				  }
    				  
    				  if($scope.comparisonperson[0].allGeneratedLeadCount > $scope.comparisonperson[1].allGeneratedLeadCount){
    					  $scope.allGeneratedLeadCountPer = (($scope.comparisonperson[0].allGeneratedLeadCount - $scope.comparisonperson[1].allGeneratedLeadCount) * 100 / $scope.comparisonperson[0].allGeneratedLeadCount).toFixed(2);
    					 
    				  }else if($scope.comparisonperson[0].allGeneratedLeadCount == $scope.comparisonperson[1].allGeneratedLeadCount){
    					  $scope.allGeneratedLeadCountPer = (($scope.comparisonperson[0].allGeneratedLeadCount - $scope.comparisonperson[1].allGeneratedLeadCount) * 100 / $scope.comparisonperson[0].allGeneratedLeadCount).toFixed(2);
    				  }
    				  else{
    					  $scope.allGeneratedLeadCountPer = (($scope.comparisonperson[1].allGeneratedLeadCount - $scope.comparisonperson[0].allGeneratedLeadCount) * 100 / $scope.comparisonperson[1].allGeneratedLeadCount).toFixed(2);
    					 
    				  }
    				  
    				  if($scope.comparisonperson[0].lostLeadCount > $scope.comparisonperson[1].lostLeadCount){
    					  $scope.lostLeadCountPer = (($scope.comparisonperson[0].lostLeadCount - $scope.comparisonperson[1].lostLeadCount) * 100 / $scope.comparisonperson[0].lostLeadCount).toFixed(2);
    				
    				  }else{
    					  $scope.lostLeadCountPer = (($scope.comparisonperson[1].lostLeadCount - $scope.comparisonperson[0].lostLeadCount) * 100 / $scope.comparisonperson[1].lostLeadCount).toFixed(2);
    					
    				  }
    				  
    				  
    				  if($scope.comparisonperson[0].successRate > $scope.comparisonperson[1].successRate){
    					  $scope.successRatePer = (($scope.comparisonperson[0].successRate - $scope.comparisonperson[1].successRate) * 100 / $scope.comparisonperson[0].successRate).toFixed(2);
    					 
    				  }else if($scope.comparisonperson[0].successRate == $scope.comparisonperson[1].successRate){
    					  $scope.successRatePer = (($scope.comparisonperson[0].successRate - $scope.comparisonperson[1].successRate) * 100 / $scope.comparisonperson[0].successRate).toFixed(2);
    				  }
    				  else{
    					  $scope.successRatePer = (($scope.comparisonperson[1].successRate - $scope.comparisonperson[0].successRate) * 100 / $scope.comparisonperson[1].successRate).toFixed(2);
    					 
    				  }
    				  
    				  if($scope.comparisonperson[0].likeCount > $scope.comparisonperson[1].likeCount){
    					  $scope.likeCountPer = (($scope.comparisonperson[0].likeCount - $scope.comparisonperson[1].likeCount) * 100 / $scope.comparisonperson[0].likeCount).toFixed(2);
    					 
    				  }else if($scope.comparisonperson[0].likeCount == $scope.comparisonperson[1].likeCount){
    					  $scope.likeCountPer = (($scope.comparisonperson[0].likeCount - $scope.comparisonperson[1].likeCount) * 100 / $scope.comparisonperson[0].likeCount).toFixed(2);
    				  }
    				  else{
    					  $scope.likeCountPer = (($scope.comparisonperson[1].likeCount - $scope.comparisonperson[0].likeCount) * 100 / $scope.comparisonperson[1].likeCount).toFixed(2);
    					 
    				  }
    				  
    				  if($scope.comparisonperson[0].returningClints > $scope.comparisonperson[1].returningClints){
    					  $scope.returningClintsPer = (($scope.comparisonperson[0].returningClints - $scope.comparisonperson[1].returningClints) * 100 / $scope.comparisonperson[0].returningClints).toFixed(2);
    					 
    				  }else{
    					  $scope.returningClintsPer = (($scope.comparisonperson[1].returningClints - $scope.comparisonperson[0].returningClints) * 100 / $scope.comparisonperson[1].returningClints).toFixed(2);
    					  
    				  }
    				  if(isNaN($scope.returningClintsPer)){
    					  $scope.returningClintsPer = "";
    				  }
    				  
    				  if($scope.comparisonperson[0].callMade > $scope.comparisonperson[1].callMade){
    					  $scope.callMadePer = (($scope.comparisonperson[0].callMade - $scope.comparisonperson[1].callMade) * 100 / $scope.comparisonperson[0].callMade).toFixed(0);
    					  
    				  }else if($scope.comparisonperson[0].callMade == $scope.comparisonperson[1].callMade){
    					  
    					  $scope.callMadePer = (($scope.comparisonperson[0].callMade - $scope.comparisonperson[1].callMade) * 100 / $scope.comparisonperson[0].callMade).toFixed(0);
    				  }
    				  
    				  else{
    					  $scope.callMadePer = (($scope.comparisonperson[1].callMade - $scope.comparisonperson[0].callMade) * 100 / $scope.comparisonperson[1].callMade).toFixed(0);
    					  
    				  }
    				  
    				  if($scope.comparisonperson[0].mailSent > $scope.comparisonperson[1].mailSent){
    					  $scope.mailSentPer = (($scope.comparisonperson[0].mailSent - $scope.comparisonperson[1].mailSent) * 100 / $scope.comparisonperson[0].mailSent).toFixed(0);
    					 
    				  }
    				  else if($scope.comparisonperson[0].mailSent == $scope.comparisonperson[1].mailSent) {
    					  $scope.mailSentPer = (($scope.comparisonperson[0].mailSent - $scope.comparisonperson[1].mailSent) * 100 / $scope.comparisonperson[0].mailSent).toFixed(0);
    					  
    				  }else{
    					  $scope.mailSentPer = (($scope.comparisonperson[1].mailSent - $scope.comparisonperson[0].mailSent) * 100 / $scope.comparisonperson[1].mailSent).toFixed(0);
    					 
    				  }
    				  
    				  if($scope.comparisonperson[0].testDriveSched > $scope.comparisonperson[1].testDriveSched){
    					  $scope.testDriveSchedPer = (($scope.comparisonperson[0].testDriveSched - $scope.comparisonperson[1].testDriveSched) * 100 / $scope.comparisonperson[0].testDriveSched).toFixed(0);
    					  
    				  }else if($scope.comparisonperson[0].testDriveSched == $scope.comparisonperson[1].testDriveSched){
    					  
    					  $scope.testDriveSchedPer = (($scope.comparisonperson[0].testDriveSched - $scope.comparisonperson[1].testDriveSched) * 100 / $scope.comparisonperson[0].testDriveSched).toFixed(0);
    				  }
    				  else{
    					  $scope.testDriveSchedPer = (($scope.comparisonperson[1].testDriveSched - $scope.comparisonperson[0].testDriveSched) * 100 / $scope.comparisonperson[1].testDriveSched).toFixed(0);
    				  }
    				  $('#btncomparisonSale').click();
    			  }
    		  }
    		  
    		  $scope.restoreLead = function(entity){
    			  apiserviceDashborad.restoreLead(entity.id, entity.leadType).then(function(data){
    				  $scope.getAllCanceledLeads();
    			  });
    		  }
    		  
    		  $scope.deleteMyLead = function() {
    			  apiserviceDashborad.deleteCanceledLead($scope.leadId, entity.leadType).then(function(data){
    				  $scope.getAllCanceledLeads();
    			  });
    		  }
    		  
    		  $scope.getScheduleBySelectedDate = function(date) {
    			  
    			  apiserviceDashborad.getScheduleBySelectedDate(date).then(function(data){
    				  $scope.scheduleList = data;
    			  });
    			  
    			  apiserviceDashborad.getToDoBySelectedDate(date).then(function(data){
    				  $scope.toDoDateList = data;
    			  });
    			  
    		  }
    		  
    		  
	    		  apiserviceDashborad.getAllScheduleTestAssigned().then(function(data){
	    			  $scope.gridOptions2.data = data;
  					  $scope.AllScheduleTestAssignedList = data;
				  });			 
    			  
    	
    			  $scope.gridOptions2.onRegisterApi = function(gridApi){
     				 $scope.gridApi = gridApi;
     				 
     		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
     			          var grid = this.grid;
     			          $scope.gridOptions2.data = $filter('filter')($scope.AllScheduleTestAssignedList,{'vin':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'make':grid.columns[2].filters[0].term,'name':grid.columns[3].filters[0].term,'phone':grid.columns[4].filters[0].term,'email':grid.columns[5].filters[0].term,'confirmDate':grid.columns[6].filters[0].term,'confirmTime':grid.columns[7].filters[0].term,'bestDay':grid.columns[8].filters[0].term,'bestTime':grid.columns[9].filters[0].term},undefined);
     			        });
     		   		
     	  		};
     	  		
     	  	 apiserviceDashborad.getAllTradeInSeen().then(function(data){
     	  		$scope.gridOptions3.data = data;
		 		$scope.AllTradeInSeenList = data;
			  });
    			  
    			  $scope.gridOptions3.onRegisterApi = function(gridApi){
      				 $scope.gridApi = gridApi;
      				 
      		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
      			          var grid = this.grid;
      			          $scope.gridOptions3.data = $filter('filter')($scope.AllTradeInSeenList,{'vin':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'make':grid.columns[2].filters[0].term,'name':grid.columns[3].filters[0].term,'phone':grid.columns[4].filters[0].term,'email':grid.columns[5].filters[0].term,'requestDate':grid.columns[6].filters[0].term},undefined);
      			        });
      		   		
      	  		};
      	  		
      	  	 $scope.gridOptions10.onRegisterApi = function(gridApi){
  				 $scope.gridApi = gridApi;
  				 
  		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
  			          var grid = this.grid;
  			          $scope.gridOptions10.data = $filter('filter')($scope.completedL,{'vin':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'make':grid.columns[2].filters[0].term,'name':grid.columns[3].filters[0].term,'phone':grid.columns[4].filters[0].term,'email':grid.columns[5].filters[0].term,'status':grid.columns[6].filters[0].term},undefined);
  			        });
  		   		
  	  		};
      	  		
      	  	
  	  	 apiserviceDashborad.getUserType().then(function(data){
  	  		
	  			 	$scope.userType = data;
	  			 	if($scope.userType == "Manager") {
	  			 		$scope.getGMData();
	  			 		//$scope.getAnalystData();
	  			 	}
	  			 	if($scope.userType == "Sales Person") {
	  			 		//$scope.getToDoNotification();
//	  			 		$scope.getAssignedLeads();  // $http.get('/getUserType') is calling two times
	  			 	}
	  			});
	    		
	    		var promo =  $interval(function() {
	    			$scope.getToDoNotification();
  			 		$scope.getAssignedLeads();
  			 	
	    		},120000);
	    		var promos =  $interval(function() {
  			 		$scope.reminderPopup();
	    		},900000);
	    		
	    		$scope.reminderPopup = function(){
	    			
	    			apiserviceDashborad.getReminderPopup().then(function(data){
		  				var notifContent;
	    				angular.forEach(data, function(value, key) {
	    					if(value.action == "Test drive reminder"){
	    						notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span>"+value.action+"</span><br><span>\n "+value.notes+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
	    	    				
	    	    				var position = 'topRight';
	    		    	        if ($('body').hasClass('rtl')) position = 'topLeft';
	    		    	        var n = noty({
	    		    	            text: notifContent,
	    		    	            type: 'success',
	    		    	            layout: position,
	    		    	            theme: 'made',
	    		    	            buttons: [
	    										{
	    										    addClass: 'general-button btnText', text: 'Cancel Test Drive', onClick: function($noty)
	    										    {
	    										    	$scope.cancelScheduleStatus(value);
	    										       $noty.close();
	    										    }
	    										},  
	    							          {
	    									        addClass: 'general-button btnText', text: 'Edit Test Drive', onClick: function($noty)
	    									              {
	    									        	$scope.editVinData(value);
	    									                 $noty.close();
	    									              }
	    									          }
	    									            
	    							          
	    									 ],
	    		    	            animation: {
	    		    	                open: 'animated bounceIn',
	    		    	                close: 'animated bounceOut'
	    		    	            },
	    		    	            
	    		    	            callback: {
	    		    	                onShow: function () {
	    		    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('bottom', 10);
	    		    	                },
	    		    	                onCloseClick: function () {
	    		    	                	$('html, body').animate({scrollTop:480}, 'slow');
	    		    	                }
	    		    	            }
	    		    	        });
	    		    	        
	    		    	        var element = $('#cnt');
	    						$compile(element)($scope);
	    					}else{
	    						notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span>"+value.action+"</span><br><span>\n "+value.notes+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
	    	    				
	    	    				var position = 'topRight';
	    		    	        if ($('body').hasClass('rtl')) position = 'topLeft';
	    		    	        var n = noty({
	    		    	            text: notifContent,
	    		    	            type: 'success',
	    		    	            layout: position,
	    		    	            theme: 'made',
	    		    	            animation: {
	    		    	                open: 'animated bounceIn',
	    		    	                close: 'animated bounceOut'
	    		    	            },
	    		    	            
	    		    	            callback: {
	    		    	                onShow: function () {
	    		    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('bottom', 10);
	    		    	                },
	    		    	                onCloseClick: function () {
	    		    	                	$('html, body').animate({scrollTop:480}, 'slow');
	    		    	                }
	    		    	            }
	    		    	        });
	    		    	        
	    		    	        var element = $('#cnt');
	    						$compile(element)($scope);
	    					} 					
	    				});
		  				  
		  				  
		  			  });
	    		}
	    		
	    		apiserviceDashborad.getAllMakeList().then(function(response){
	    			console.log(response);
	    			$scope.makelist = response;
    			});
	    		
	    		
	    		$scope.visitors = [];
	    		$scope.newUsers = [];
	    		$scope.bounceRate = [];
	    		
	    		apiserviceDashborad.getVisitorStats().then(function(response){
	    			$scope.visitors[0] = {'title':'Visit Today','value':response[0].dates[0].items[0].value};
		    		$scope.visitors[1] = {'title':'Visit Yesterday','value':response[0].dates[1].items[0].value};
		    		$scope.newUsers[0] = Math.round($scope.visitors[0].value==0?0:(response[1].dates[0].items[0].value/$scope.visitors[0].value)*100);
		    		$scope.newUsers[1] = Math.round($scope.visitors[1].value==0?0:(response[1].dates[1].items[0].value/$scope.visitors[1].value)*100);
		    		$scope.bounceRate[0] = response[2].dates[0].items[0].value;
		    		$scope.bounceRate[1] = response[2].dates[1].items[0].value;
	    		});
	    		
	    		$scope.showSessionAnalytics = function(id,vin,status){
	    			$location.path('/sessionsAnalytics/'+id+"/"+vin+"/"+status);
	    		};
	    		
	    		$scope.currentSelectedType = 0;
	    		$scope.currentSelectedDuration = 0;
	    		$scope.weekData = {};
	    		$scope.currentData = [];
	    		$scope.showMonthVisited = function() {
	    			$scope.currentSelectedMonthDuration = 1;
	    			$scope.currentSelectedAllTimeDuration = 0;
	    			$scope.currentSelectedWeekDuration = 0;
	    			$scope.getVisitedData('month','countHigh','0','0','All');
	    		};
	    		
	    		$scope.showAllTimeVisited = function() {
	    			var startD = $('#cnfstartDateValueForListing').val();
		 			   var endD = $('#cnfendDateValueForListing').val();
	    			$scope.currentSelectedAllTimeDuration = 1;
	    			$scope.currentSelectedMonthDuration = 0;
	    			$scope.currentSelectedWeekDuration = 0;
	    			$scope.getVisitedData('allTime','countHigh','0','0','All',startD,endD);
	    		};
	    		
	    		
	    		$scope.vehicleData=function(vehicles,startDate,endDate){
	    			$scope.all=vehicles; 
	    			$scope.getVisitedData('datewise','countHigh','0','0',vehicles,startDate,endDate); 			
	    		};
	    		
	    		$scope.topVisitedDataDatewise = function(){
	    			var startD = $('#cnfstartDateValueForListing').val();
		 			var endD = $('#cnfendDateValueForListing').val();
		    		$scope.getVisitedData('datewise','countHigh','0','0','All',startD,endD);
	    		}
	    		
	    		$scope.notchange = 0;
	    		$scope.getVisitedData = function(type,filterBy,search,searchBy,vehicles,startD,endD) {
	    			if(locationId != 0){
	    				apiserviceDashborad.gmLocationManager(locationId).then(function(data){
	    					apiserviceDashborad.getVisitedData(data.id,type,filterBy,search,searchBy,vehicles,startD,endD).then(function(response){
		    				$scope.weekData = response;
		    				
		    				if(response.topVisited.length == 0){
		    					$scope.currentSelectedType = 2;
		    					$scope.notchange = 1;
		    				}else{
		    					$scope.notchange = 0;
		    				}
		    				
		    				if($scope.currentSelectedType==0) 
		    					$scope.currentData = response.topVisited;
		    				else if($scope.currentSelectedType==1)
		    					$scope.currentData = response.worstVisited;
		    				else if($scope.currentSelectedType==2)
		    					$scope.currentData = response.allVehical;
		    				$(ele).hide();
		    			});
		    					
		    				});
	    			}else{
	    				apiserviceDashborad.getVisitedData($scope.userKey,type,filterBy,search,searchBy,vehicles,startD,endD).then(function(response){
	    				$scope.weekData = response;
	    				
	    				if(response.topVisited.length == 0){
	    					$scope.currentSelectedType = 2;
	    					$scope.notchange = 1;
	    				}else{
	    					$scope.notchange = 0;
	    				}
	    				
	    				if($scope.currentSelectedType==0) 
	    					$scope.currentData = response.topVisited;
	    				else if($scope.currentSelectedType==1)
	    					$scope.currentData = response.worstVisited;
	    				else if($scope.currentSelectedType==2)
	    					$scope.currentData = response.allVehical;
	    				$(ele).hide();
	    				});
	    					
	    			}
	    		};
	    		
	    		$scope.selectedObj = function (selectObj) {
	    			if(selectObj.originalObject != undefined){
	    				$scope.item = selectObj.originalObject;
		    		    $scope.lead.custName = $scope.item.fullName;
		    		    $scope.lead.custNumber = $scope.item.phone;
		    			$scope.lead.custEmail = $scope.item.email;
		    			$scope.lead.custZipCode = $scope.item.zip;
	    			}
	    		};
	    		
	    		apiserviceDashborad.getHeardAboutUs().then(function(response){
	    				$scope.heardAboutUs = response;
	    			});
	    			$scope.othertxt=null;
	    			
	    		$scope.openCreateNewLeadPopup = function() {
	    			 $scope.showFormly = '0';
	    			   $scope.showFormly1 = '0';
	    			$scope.stockWiseData = [];
	    			$scope.stockWiseData = [{}];
	    			apiserviceDashborad.getCustomizationform('Create Lead').then(function(response){
	    				console.log(response);
	    				console.log(angular.fromJson(response.jsonData));
	    				
	    				 $scope.editInput = response;
	    				 $scope.userFields = $scope.addFormField(angular.fromJson(response.jsonData));
	    				 console.log($scope.userFields);
	    				 $scope.user = {};
	    			
	    			$scope.getMakes();
	    			$("#createLeadPopup").modal();
	    		});
	    		}
	    		
	    		$scope.openCreateNewLeads = function(item) {
	    			$scope.stockWiseData = [];
	    			apiserviceDashborad.getStockDetails(item).then(function(response){
	    				if(response.isData) {
	    					 $scope.stockWiseData.push({
	    							model:response.model,
	    							make:response.make,
	    							stockNumber:response.stock,
	    							year:response.year,
	    							bodyStyle:response.bodyStyle,
	    							mileage:response.mileage,
	    							transmission:response.transmission,
	    							drivetrain:response.drivetrain,
	    							engine:response.engine,
	    							vin:response.vin,
	    							imgId:response.imgId,
	    						});
	    				} 
	    			});
	    			$scope.getMakes();
	    			$("#createLeadPopup").modal();
	    		};
	    		
	    		$scope.initialiase = function() {
	    			$scope.lead = {
	    					make:'',
	    					model:'',
	    					makeSelect:'',
	    					modelSelect:'',
	    					custName:'',
	    					custEmail:'',
	    					custNumber:'',
	    					leadType:'',
	    					contactedFrom:'',
	    					prefferedContact:'email',
	    					bestDay:'',
	    					bestTime:'',
	    					comments:'',
	    					options:[],
	    					year:'',
	    					exteriorColour:'',
	    					kilometres:'',
	    					engine:'',
	    					doors:'',
	    					transmission:'',
	    					drivetrain:'',
	    					bodyRating:'',
	    					tireRating:'',
	    					engineRating:'',
	    					transmissionRating:'',
	    					glassRating:'',
	    					interiorRating:'',
	    					exhaustRating:'',
	    					rentalReturn:'',
	    					odometerAccurate:'',
	    					serviceRecords:'',
	    					lienholder:'',
	    					titleholder:'',
	    					equipment:'',
	    					vehiclenew:'',
	    					accidents:'',
	    					damage:'',
	    					paint:'',
	    					salvage:''
	    			};
	    		};
	    		$scope.removeLead = function(index){
	    			$scope.stockWiseData.splice(index, 1);
	    		}
	    		$scope.customList = [];
	    		$scope.initialiase();
	    		$scope.isInValid = false;
	    		$scope.isStockError = false;
	    		$scope.focusIn = function(itm){
					$scope.len = itm;
	    		};
	    		$scope.customData = {};
	    		$scope.createLead = function() {
	    			$scope.customList =[];
	    			$scope.customData.time_range = $("#bestTimes").val();
	    			console.log($scope.customData.setTime);
	    			if($scope.customData.time_range == undefined){
	    				delete $scope.customData.time_range;
	    			}
	    			console.log($scope.customData);
	    			
	    			console.log($('#exCustoms_value').val());
	    			$scope.customData.custName = $('#exCustoms_value').val();
	    			if($scope.customData.custName == undefined){
	    				delete $scope.customData.custName;
	    			}
	    			$scope.customData.autocompleteText = $("#autocomplete").val()
	    			if($scope.customData.autocompleteText == undefined){
	    				delete $scope.customData.autocompleteText;
	    			}
	    			$scope.josnData = 0;
	    			
	    			
	    			apiserviceDashborad.getCustomizationform('Create Lead').then(function(response){
	    				$scope.josnData = angular.fromJson(response.jsonData);
	    				console.log($scope.josnData);
	    					$.each($scope.customData, function(attr, value) {
	    						angular.forEach($scope.josnData, function(value1, key) {
	    							console.log(attr);
	    							console.log(value1.key);
	    							if(value1.key == attr){
		    							$scope.customList.push({
				    		   	  			key:attr,
				    		   	  			value:value,
				    		   	  			savecrm:value1.savecrm,
				    		   	  			displayGrid:value1.displayGrid,
				    		   	  			
				    					});
		    						} 
		    					});
			    			   });
	    					
	    					
	    					
	    					console.log($("#bestTimes").val());
	    	    			console.log($scope.customData);
	    	    			console.log($scope.customList);
	    	    			 
	    	    			
	    	    			$scope.lead.customData = $scope.customList;
	    	    			console.log($scope.lead);
	    	    			console.log($("#autocomplete").val());
	    	    			
	    	    			if($scope.lead.custName == ''){
	    	    				$scope.lead.custName = $('#ex1_value').val();
	    	    			}
	    	    			if($scope.lead.custName==''||$scope.lead.custZipCode==''||$scope.lead.custEmail==''||$scope.lead.custNumber=='' ||  
	    	    					 $scope.lead.leadType =='') {
	    	    				$scope.isInValid = true;
	    	    			} else {
	    	    				$scope.isInValid = false;
	    	    			
	    		    			if($scope.lead.leadType=='1') {
	    		    				
	    	    					$scope.makeLead();
	    		    			} else if($scope.lead.leadType=='2') {
	    		    				$scope.lead.bestDay = $("#leadBestDay").val();
	    			    			$scope.lead.bestTime = $("#leadBestTime").val();
	    			    			if($("input[name=leadPreffered]:checked").val())
	    			    				$scope.lead.prefferedContact = $("input[name=leadPreffered]:checked").val();
	    			    			
	    			    			if(!$scope.lead.bestDay || $scope.lead.bestDay == '' ||!$scope.lead.bestTime || $scope.lead.bestTime=='' || !$scope.lead.prefferedContact ||$scope.lead.prefferedContact=='') {
	    			    				$scope.isInValid = true;
	    			    			} else {
	    			    				$scope.makeLead();
	    			    			}
	    		    			} else if($scope.lead.leadType=='3') {
	    		    				$("#createLeadPopup").modal('hide');
	    		    				$("#tradeInApp").modal();
	    		    			}else{
	    		    				$scope.makeLead();
	    		    			}
	    	    			}
	    	    			if($scope.lead.leadType != '3'){
	    	    				//window.location.reload();
	    	    			}
	      	  		});
	    			
	    			
	    			
	    		};
	    		
	    		$scope.makeLeadEdit = function(){
	    				$scope.lead.leadType = '3';
	    			apiserviceDashborad.createLead($scope.lead).then(function(response){
	    					$("#tradeInAppEdit").modal('hide');
	    			});
	    		}
	    		
	    		$scope.makeLead = function() {
	    			
	    			$scope.othertxt = $('#othertxt').val();
	    			if($scope.lead.hearedFrom == "Other"){
	    				if($scope.othertxt == null || $scope.othertxt == undefined){
	    					$scope.lead.hearedFrom = "Other";
	    				}else{
	    					$scope.lead.hearedFrom = $scope.othertxt;
	    					apiserviceDashborad.addHeard($scope.lead.hearedFrom).then(function(response){
	    						apiserviceDashborad.getHeardAboutUs().then(function(response){	
	    		    				$scope.heardAboutUs = response;
	    		    			});
	    					});
	    				}
	    				
	    			}
	    			
	    			 var startD = $('#cnfstartDateValue').val();
	    			   var endD = $('#cnfendDateValue').val();
	    			
	    			$("#createLeadPopup").modal('hide');
	    			$scope.lead.stockWiseData = $scope.stockWiseData;
	    			
	    			
	    			var files = [];
	    			console.log("^^^%%%*&&&&&&");
	    			//console.log($scope.lead);
	    			if($rootScope.fileCustom != undefined){
	    				console.log($rootScope.fileCustom);
	    				files = $rootScope.fileCustom;
	    				
	    				
	    				console.log(files.length);
	    				console.log(files);
	    				console.log("bothfile");
	    				
	    				apiserviceDashborad.createLeads($scope.lead, files).then(function(response){
	    					
	    				});
	    				}else{
	    					console.log($scope.lead);
	    					apiserviceDashborad.createLeads($scope.lead, $rootScope.fileCustom).then(function(response){
	    						//$scope.getVisitedData('week','countHigh','0','0','All');
	    	    				$scope.topVisitedDataDatewise();
	    	    				//$scope.userLocationData('Week','person');
	    	    				 $scope.findMystatisData(startD,endD,'person');
	    	    				$scope.getAllSalesPersonRecord($scope.salesPerson);
	    	    				/*if($scope.lead.leadType=='2')  {
	    	    					$scope.getScheduleTestData();
	    	    					$("#createLeadPopup").modal('hide');
	    	    				}
	    	    				else*/ if($scope.lead.leadType=='1') {
	    	    					$scope.getRequestMoreData();
	    	    					$("#createLeadPopup").modal('hide');
	    	    				}else {
	    	    					$scope.getTradeInData();
	    	    					$("#tradeInApp").modal('hide');
	    	    					window.location.reload();
	    	    				}
	    	    				$scope.initialiase();
	    	    			});
	    				}
	    			$scope.getAllSalesPersonRecord($scope.salesPerson);
	    		};
	    		
	    		$scope.changeMakeSelect = function(modelSelect) {
	    			$scope.lead.modelSelect = modelSelect;
	    		};
	    		
	    		$scope.getModelsByMake = function(makeSelect) {
	    			$scope.lead.makeSelect = makeSelect;
	    			$scope.lead.modelSelect = '';
	    			apiserviceDashborad.getModels(makeSelect).then(function(response){
	    				$scope.models = response;
	    			});
	    		};
	    		$scope.stockWiseData = [];
	    		$scope.stockWiseData.push({});
	    		$scope.getStockDetails = function(stockRp) {
	    			$scope.isStockError = false;
	    			apiserviceDashborad.getStockDetails(stockRp.stockNumber).then(function(response){
	    				if(response.isData) {
	    					$scope.isStockError = false;
	    					stockRp.make = response.make;
	    					stockRp.model = response.model;
	    					stockRp.bodyStyle = response.bodyStyle;
	    					stockRp.engine = response.engine;
	    					stockRp.mileage = response.mileage;
	    					stockRp.transmission = response.transmission;
	    					stockRp.drivetrain = response.drivetrain;
	    					stockRp.vehicleImage = response.vehicleImage;
	    					stockRp.imgId = response.imgId;
	    					stockRp.year = response.year;
	    					stockRp.vin = response.vin;
	    				} else {
	    					$scope.isStockError = true;
	    				}
	    			});
	    		};
	    		
	    		$scope.pushRecord = function(){
	    			$scope.stockWiseData.push({});
	    		}
	    		
	    		$scope.makes = [];
	    		$scope.models = [];
	    		$scope.getMakes = function() {
	    			apiserviceDashborad.getMakes().then(function(response){
	    				$scope.makes = response.makes;
	    			});
	    		};
	    		
	    		$scope.showTopVisited = function() {
	    			$scope.topVisitedDataDatewise();
	    			$scope.currentSelectedType = 0;
	    			$scope.currentData = $scope.weekData.topVisited;
	    		};
	    		
	    		$scope.filterFunction = function(filterBy) {
	    			var startD = $('#cnfstartDateValueForListing').val();
		 			var endD = $('#cnfendDateValueForListing').val();
	    			$scope.getVisitedData('week',filterBy,'0','0','All',startD,endD);
	    		};
	    		$scope.search = "";
	    		$scope.searchBy = "";
	    		$scope.showTextBox = function(search){
					if(search=='Make'){
						$scope.currentSelectedDuration = 0;
					}if(search=='Model'){
						$scope.currentSelectedDuration = 1;
					}
	    			$scope.search = search;
	    			
	    		}
	    		$scope.molelFlag = 0;
	    		$scope.findMake = function(value,searchBy){
	    			$(ele).show();
	    			var startD = $('#cnfstartDateValueForListing').val();
		 			   var endD = $('#cnfendDateValueForListing').val();
		 			  $scope.molelFlag = 1;
		 			   
	    			if(value.length > 2){
	    				$scope.searchBy = searchBy;
	    				$scope.getVisitedData('week','countHigh',value,$scope.searchBy,'All',startD,endD);
	    			}
					if(value.length == 0){
	    				$scope.getVisitedData('week','countHigh','0','0','All',startD,endD);
	    			}
	    			apiserviceDashborad.getAllModelList(value).then(function(response){
		    			$scope.modellist = response;
	    			});
	    		}
	    		$scope.findModel = function(value,searchBy,maketype){
	    			var startD = $('#cnfstartDateValueForListing').val();
		 			   var endD = $('#cnfendDateValueForListing').val();
	    			if(value.length > 1){
	    				value = value+"_&_"+maketype;
	    				console.log(value);
	    				$scope.searchBy = searchBy;
		    			$scope.getVisitedData('week','countHigh',value,$scope.searchBy,'All',startD,endD);
	    			}
					if(value.length == 0){
	    				$scope.getVisitedData('week','countHigh','0','0','All',startD,endD);
	    			}
	    		}
	    		
	    		$scope.showWorstVisited = function() {
	    			$scope.currentSelectedType = 1;
	    			$scope.currentData = $scope.weekData.worstVisited;
	    		};
	    		
	    		$scope.showAllvehicles = function(){
	    			//$scope.getVisitedData('week','countHigh','0','0','All');
	    			$scope.topVisitedDataDatewise();
	    			
	    			$scope.currentSelectedType = 2;
	    			$scope.currentData = $scope.weekData.allVehical;
	    		}
	    		
	    		//$scope.showWeekVisited();
	    		$scope.doComplete = function() {
	    			$(".live-tile").liveTile();
	    		};
	    		
	    		$scope.getAnalystData = function() {
	    			apiserviceDashborad.getAnalystData().then(function(response){
	    			});
	    		};
	    		
	    		$scope.getSalesDataValue = function(locationValue) {
	    			if(locationValue == null){
	 				   $scope.locationValue = 0;
	 			    }
	    			
	    			$scope.locationValue = locationValue;
	    			apiserviceDashborad.getSalesUserOnly(locationValue).then(function(data){
		    			$scope.salesPersonPerf = data;
		    			 $scope.gridOptionsValue.data = $scope.salesPersonPerf;
		    			angular.forEach($scope.salesPersonPerf, function(value, key) {
		    				value.isSelected = false;
		    			});
		    		});
	    		}

	    		$scope.getGMData1 = function() {
	    			apiserviceDashborad.getSalesUserList(locationId).then(function(data){
		    			$scope.salesPersonList =data;
		    			$scope.user=data;
		    			if($scope.salesPersonList.length > 0){
		    				$scope.getAllSalesPersonRecord($scope.salesPersonList[0].id);
		    			}
		    		});
	    		}
	    		
	    		$scope.getGMData = function() {
	    			apiserviceDashborad.getSalesUser().then(function(data){
		    			$scope.salesPersonList =data;
		    			$scope.getAllSalesPersonRecord($scope.salesPersonList[0].id);
		    		});
	    		}
	    		
	    		$scope.getAssignedLeads = function() {
	    			apiserviceDashborad.getAssignedLeads().then(function(data){
		    			$scope.leadCount = data.count;
		    			if($scope.leadCount != '0') {
		    				var notifContent;
		    				$scope.leadNotification = data.data;
		    				if($scope.leadCount==1 ) {
		    					if($scope.leadNotification.premiumFlag == 0 && $scope.leadNotification.premiumFlag != null){
		    						if($scope.userType != "Manager"){
		    							notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><h4 class='alert-title f-14' id='cnt'>Premium lead has been assigned to you.</h4><p class='row' style='margin-left:0;'><span style='color: #319DB5;font-weight: bold;'>INFO: </span><span>"+$scope.leadNotification.name+" "+$scope.leadNotification.make+" "+$scope.leadNotification.model+" "+$scope.leadNotification.trim+"</span></p><p class='row' style='margin-left:0;'><span style='color: #319DB5;font-weight: bold;'>Price: </span><span>"+$scope.leadNotification.price+"</span></p><p class='row' style='margin-left:0;'><span style='color: #319DB5;font-weight: bold;'>TYPE: </span><span>"+$scope.leadNotification.leadType+"</span></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>See the Leads&nbsp;<i class='glyphicon glyphicon-download'></i></a></p></div></div>";		    							
		    						}
		    					}else{
		    						notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><h4 class='alert-title f-14' id='cnt'>1 New Lead Assigned</h4><p class='row' style='margin-left:0;'><span style='color: #319DB5;font-weight: bold;'>INFO: </span><span>"+$scope.leadNotification.make+" "+$scope.leadNotification.model+" "+$scope.leadNotification.name+"</span></p><p class='row' style='margin-left:0;'><span style='color: #319DB5;font-weight: bold;'>TYPE: </span><span>"+$scope.leadNotification.leadType+"</span></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>See the Leads&nbsp;<i class='glyphicon glyphicon-download'></i></a></p></div></div>";		    						
		    					}
		    				} else {
		    					notifContent = '<div class="alert alert-dark media fade in bd-0" id="message-alert"><div class="media-left"></div><div class="media-body width-100p"><h4 class="alert-title f-14" id="cnt">'+$scope.leadCount+' New Leads Assigned</h4><p class="pull-left" style="margin-left:65%;"><a class="f-12">See the Leads&nbsp;<i class="glyphicon glyphicon-download"></i></a></p></div></div>';
		    				}
		    				var position = 'topRight';
			    	        if ($('body').hasClass('rtl')) position = 'topLeft';
			    	        var n = noty({
			    	            text: notifContent,
			    	            type: 'success',
			    	            layout: position,
			    	            theme: 'made',
			    	            animation: {
			    	                open: 'animated bounceIn',
			    	                close: 'animated bounceOut'
			    	            },
			    	            
			    	            callback: {
			    	                onShow: function () {
			    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('bottom', 10);
			    	                },
			    	                onCloseClick: function () {
			    	                	$('html, body').animate({scrollTop:1660}, 'slow'); // changed from 480 to 1660 for show myleads grid
			    	                	
			    	                	if($scope.leadNotification.premiumFlag == 0 && $scope.leadNotification.premiumFlag != null){
			    	                		if($scope.leadNotification.leadType == 'Schedule Test'){
			    	                			$scope.testDrive();
					    	                	$('#test-drive-tabSched').click();
				    	                		for(var i=0;i<$scope.gridOptions2.data.length;i++){
				    	                			if($scope.gridOptions2.data[i].id == $scope.leadNotification.id){
				    	                				$scope.editVinData($scope.gridOptions2.data[i]);
				    	                				break;
				    	                			}
				    	                		}
			    	                		}else if($scope.leadNotification.leadType == 'Request More Info'){
			    	                			$scope.requestMore()
			    	                			$('#tab').click();
			    	                			for(var i=0;i<$scope.gridOptions5.data.length;i++){
				    	                			if($scope.gridOptions5.data[i].id == $scope.leadNotification.id){
				    	                				$scope.editVinData($scope.gridOptions5.data[i]);
				    	                				break;
				    	                			}
				    	                		}
			    	                		}else{
			    	                			$scope.tradeIn();
			    	                			$('#profile-tab').click();
			    	                			for(var i=0;i<$scope.gridOptions3.data.length;i++){
				    	                			if($scope.gridOptions3.data[i].id == $scope.leadNotification.id){
				    	                				$scope.editVinData($scope.gridOptions3.data[i]);
				    	                				break;
				    	                			}
				    	                		}
			    	                		}
			    	                	}
			    	                }
			    	            }
			    	        });
			    	        
			    	        var element = $('#cnt');
							$compile(element)($scope);
		    			}
		    			
		    			$scope.setLeadSeen();
		    		});
	    			
	    		};
	    		
	    		$scope.getToDoNotification = function() {
	    			apiserviceDashborad.getNewToDoCount().then(function(data){
		    			$scope.toDoCount = data.count;
		    			if($scope.toDoCount != '0'&& $scope.flagForPopUp !=1) {
		    				var notifContent;
		    				$scope.notification = data.data;
		    				if($scope.toDoCount==1) {
		    					notifContent = "<div class='alert alert-dark media fade in bd-0 "+($scope.notification.priority=='Low'?"":$scope.notification.priority == 'Medium'?"pri-low": $scope.notification.priority =='High' ?"pri-medium":"pri-high")+"' id='message-alert'>"+
		    					"<div class='media-left'></div>"+
		    					"<div class='media-body width-100p'>"+
		    					"<h4 class='alert-title f-14' id='cnt'>1 New Todos Assigned</h4>"+
		    					"<p class='row' style='margin-left:0;'>"+
		    					"<span style='color:#319DB5;font-weight:bold;'>DESCRIPTION: </span>"+
		    					"<span style='color:white;'>"+$scope.notification.task+"</span></p>"+
		    					"<p class='row' style='margin-left:0;'>" +
		    					"<span style='color:#319DB5;font-weight:bold;'>DUE DATE: </span>" +
		    					"<span style='color:#319DB5;'>"+$scope.notification.dueDate+"</span></p>" +
		    					"<p class='row' style='margin-left:0;'>" +
		    					"<span class='col-md-6' style='padding:0;'>" +
		    					"<span style='color:#319DB5;font-weight:bold;'>PRIORITY: </span>" +
		    					"<span class='"+(($scope.notification.priority=='Low'?'':$scope.notification.priority == 'Medium'?'text-low': $scope.notification.priority =='High' ?'text-medium':'text-high'))+"'>"+$scope.notification.priority+"</span></span>" +
		    					"<span class='col-md-4 col-md-offset-1' style='padding:0;'>" +
		    					"</span></p></div></div>";  /*<a class='f-12' style='float:right;'>Go to todos&nbsp;<i class='glyphicon glyphicon-download'></i></a>*/
		    				} else {
		    					notifContent = '<div class="alert alert-dark media fade in bd-0" id="message-alert"><div class="media-left"></div><div class="media-body width-100p"><h4 class="alert-title f-14" id="cnt">'+$scope.toDoCount+' New Todos Assigned</h4><p class="pull-left" style="margin-left:65%;"></p></div></div>'; /*<a class="f-12">Go to todos&nbsp;<i class="glyphicon glyphicon-download"></i></a>*/
		    				}
		    				var position = 'topRight';
			    	        if ($('body').hasClass('rtl')) position = 'topLeft';
			    	        var n = noty({
			    	            text: notifContent,
			    	            type: 'success',
			    	            layout: position,
			    	            theme: 'made',
			    	            animation: {
			    	                open: 'animated bounceIn',
			    	                close: 'animated bounceOut'
			    	            },
			    	            
			    	            callback: {
			    	                onShow: function () {
			    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('bottom', 10);
			    	                },
			    	                onCloseClick: function () {
			    		    			$('html, body').animate({scrollTop:2700}, 'slow');
			    	                }
			    	            }
			    	        });
			    	        
			    	        var element = $('#cnt');
							$compile(element)($scope);
		    			}
		    			
		    			$scope.setTodoSeen();
		    		});
	    		}
	    		
	    		$scope.setLeadSeen = function() {
	    			apiserviceDashborad.setLeadSeen().then(function(data){
		    		});
	    		};
	    		
	    		
	    		
	    		$scope.setTodoSeen = function() {
	    			apiserviceDashborad.setTodoSeen().then(function(data){
		    		});
	    		}
	    		
	    		$scope.showSalesStats = true;
	    		$scope.showPagesVisited = false;
	    		$scope.salesStats = function() {
	    			$scope.showSalesStats = true;
	    			$scope.showPagesVisited = false;
	    		}
	    		
	    		$scope.pagesVisited = function() {
	    			$scope.showSalesStats = false;
	    			$scope.showPagesVisited = true;
	    		}
	    		
    	$scope.reqMore = false;	
    	$scope.testdrv = false;
    	$scope.trdin = false;
    	$scope.allLeadd = true;
    	$scope.showLeadsV = false;
    	$scope.cancelleads = false;
    	$scope.showAllTypeLeads = true;
    	$scope.contact = false;
    	
    	
    	$scope.showLeadsDrive = function(){
    		$scope.showAllTypeLeads = true;
    		$scope.getAllLeadIn();
    		//$scope.requestMore();
    	}
    	$scope.schedultestDrive = function(){
    		$scope.showAllTypeLeads = false;
    		$scope.schedTest = true;
    		$scope.schedTestGrid = true;
    		$scope.reqMore = false;	
        	$scope.testdrv = false;
        	$scope.trdin = false;
        	$scope.allLeadd = false;
        	$scope.showLeadsV = false;
        	$scope.cancelleads = false;
        	$scope.contact = false;
//    		$scope.schedulTestDir();
    	}
    	
    	$scope.requestMore = function() {
    		$scope.otherLeads = false;
    		$scope.schedTest = false;
    		$scope.reqMore = true;	
        	$scope.testdrv = false;
        	$scope.allLeadd = false;
        	$scope.trdin = false;
        	$scope.showLeadsV = false;
        	$scope.cancelleads = false;
        	$scope.contact = false;
    	}		  
    	$scope.testDrive = function() {
    		$scope.schedTest = false;
    		$scope.otherLeads = false;
    		$scope.reqMore = false;		
        	$scope.testdrv = true;
        	$scope.trdin = false;
        	$scope.allLeadd = false;
        	$scope.showLeadsV = false;
        	$scope.cancelleads = false;
        	$scope.contact = false;
    	}	
    	$scope.tradeIn = function() {
    		$scope.schedTest = false;
    		$scope.otherLeads = false;
    		$scope.reqMore = false;	
        	$scope.testdrv = false;
        	$scope.trdin = true;
        	$scope.allLeadd = false;
        	$scope.showLeadsV = false;
        	$scope.cancelleads = false;
        	$scope.contact = false;
    	}
    	
    	$scope.contactUs = function(){
    		$scope.otherLeads = false;
    		$scope.schedTest = false;
    		$scope.reqMore = false;	
        	$scope.testdrv = false;
        	$scope.trdin = false;
        	$scope.allLeadd = false;
        	$scope.showLeadsV = false;
        	$scope.cancelleads = false;
        	$scope.contact = true;
    	}
    	
    	$scope.getAllLeadIn = function(){
    		$scope.otherLeads = false;
    		$scope.schedTest = false;
    		$scope.reqMore = false;	
        	$scope.testdrv = false;
        	$scope.trdin = false;
        	$scope.contact = false;
        	$scope.showLeadsV = false;
        	$scope.cancelleads = false;
        	$scope.allLeadd = true;
        	$scope.getAllLeadsValue();
    	}
    	
    	$scope.getScheduless = function(){
    		$scope.completedLeadsTest = false;
    		$scope.schedTestGrid = true;
    		$scope.schedTest = true;
    	}
    	
    	$scope.getCompletedLeads = function(){
    		$scope.completedLeadsTest = true;
    		$scope.schedTestGrid = false;
    		$scope.otherLeads = false;
    		$scope.schedTest = true;
    		$scope.reqMore = false;	
        	$scope.testdrv = false;
        	$scope.trdin = false;
        	$scope.showLeadsV = false;
        	$scope.cancelleads = false;
        	$scope.allLeadd = false;
        	$scope.getCompletedData();
   	  	}
    	
        $scope.canceledLeads = function() {
        	$scope.schedTest = false;
        	$scope.otherLeads = false;
        	$scope.showAllTypeLeads = false;
        	$scope.reqMore = false;	
        	$scope.testdrv = false;
        	$scope.trdin = false;
        	$scope.showLeadsV = false;
        	$scope.cancelleads = true;
//        	$scope.getAllCanceledLeads();
        }
        $scope.showLeadsArchive = function(){
        	$scope.otherLeads = false;
        	$scope.schedTest = false;
        	$scope.showAllTypeLeads = false;
        	$scope.reqMore = false;	
        	$scope.testdrv = false;
        	$scope.trdin = false;
        	$scope.cancelleads = false;
        	$scope.showLeadsV = true;
        	$scope.getAllLostAndComLeads();
        }
        
        $scope.getAllListLeadDate = [];
        $scope.getAllLeadsValue = function(){
        	$scope.getAllSalesPersonRecord($scope.salesPerson);
        }
        
        $scope.schedulTestDir = function(){
        	apiserviceDashborad.getTestDirConfir().then(function(data){
				$scope.gridOptions9.data = data;
				 angular.forEach($scope.gridOptions9.data,function(value,key){
					 value.check = false;
				 });
				$scope.allTestDirConfir = data;
				$scope.setWether($scope.gridOptions9.data);
			});
        }

        $scope.setWether = function(gridValue){
			  
			   angular.forEach(gridValue,function(value1,key1){
				   var day = moment(value1.confirmDate).format('D MMM YYYY');
				   var img= "";
			   angular.forEach($scope.whDataArr,function(value,key){
				  if(angular.equals(day, value.date)){
					  value1.wether = value.text+"  "+value.low;
				  }
			   });
			 });  
        }
        
        $scope.getCompletedData = function(){
        	apiserviceDashborad.getAllCompletedLeads().then(function(data){
				$scope.gridOptions10.data = data;
				$scope.completedL = data;
			});
        }
        
        $scope.getAllLostAndComLeads = function(){
        	apiserviceDashborad.getAllLostAndCompLeads().then(function(data){
				$scope.gridOptions6.data = data;
			});
        }
        
        $scope.getAllCanceledLeads = function() {
        	apiserviceDashborad.getAllCanceledLeads().then(function(data){
				$scope.gridOptions4.data = data;
				$scope.canceledLead = data;
			});
        }
        
        $scope.gridOptions4.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			          $scope.gridOptions4.data = $filter('filter')($scope.canceledLead,{'vin':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'make':grid.columns[2].filters[0].term,'name':grid.columns[3].filters[0].term,'phone':grid.columns[4].filters[0].term,'email':grid.columns[5].filters[0].term,'leadType':grid.columns[7].filters[0].term,'status':grid.columns[8].filters[0].term,'statusDate':grid.columns[9].filters[0].term},undefined);
			        });
	  		};
	  		
        $scope.assignCanceledLead = function(entity) {
        	$scope.cancelId = entity.id;
        	$scope.leadType = entity.typeOfLead;
        	$scope.changedUser = "";
        	$scope.getSalesDataValue($scope.locationValue);
        	$('#btnAssignUser').click();
        }
        
        $scope.changeAssignedUser = function() {
        	apiserviceDashborad.changeAssignedUser($scope.cancelId,$scope.changedUser,$scope.leadType).then(function(data){
				$('#closeChangeUser').click();
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User assigned successfully",
				});
				//$scope.getAllCanceledLeads();
				$route.reload();
			});
        	
        }
        
    	$scope.getScheduleTestData = function() {
    		apiserviceDashborad.getAllScheduleTestAssigned().then(function(data){
					$scope.gridOptions2.data = data;
					$scope.AllScheduleTestAssignedList = data;
			});
    	};
    	
    	$scope.getRequestMoreData = function() {
    	apiserviceDashborad.getAllRequestInfoSeen().then(function(data){
			$scope.gridOptions.data = data;
			$scope.AllRequestInfoSeenList = data;
		});
	};
    	
		$scope.getTradeInData = function() {
			apiserviceDashborad.getAllTradeInSeen().then(function(data){
			 		$scope.gridOptions3.data = data;
			 		$scope.AllTradeInSeenList = data;
			 });
		};
	
	
		$scope.changeSalesPerson = function(){
			var id = $('#salesPersonUserId').val();
			$scope.salesPerson = id;
			$scope.getAllSalesPersonRecord(id);
//			$scope.getAllLeadIn();
//			$('#home-tab').click();
		};
	
		$scope.getScheduleData = function(id){
			var deferred = $q.defer();
			apiserviceDashborad.getAllSalesPersonScheduleTestAssigned(id).then(function(data){
			$scope.gridOptions2.data = data;
			$scope.AllScheduleTestAssignedList = data;
			var countUnReadLead = 0;
				if($scope.userType == "Sales Person"){
					angular.forEach($scope.gridOptions2.data,function(value,key){
		        		$scope.getAllListLeadDate.push(value);
		        		if(value.noteFlag == 0 && value.confirmDate == null){
		        			countUnReadLead++;
		        		}
		        	});
					$scope.lengthOfAllLead = countUnReadLead;
					deferred.resolve("success");
				}else{
					deferred.resolve("error");
				}
		    });
			return deferred.promise;
		};
		
		$scope.getRequestData = function(id){
			var deferred = $q.defer();
			apiserviceDashborad.getAllSalesPersonRequestInfoSeen(id).then(function(data){
				var countUnReadLead = 0;
			$scope.gridOptions5.data = data;
			$scope.AllRequestInfoSeenList = data;
			if($scope.userType == "Sales Person"){
				angular.forEach($scope.gridOptions5.data,function(value,key){
	        		$scope.getAllListLeadDate.push(value);
	        		if(value.noteFlag == 0 && value.confirmDate == null){
	        			countUnReadLead++;
	        		}
	        	});
					$scope.lengthOfAllLead = countUnReadLead;
					deferred.resolve("success");
			}else{
				deferred.resolve("error");
			}
		   });
			return deferred.promise;
		};
		
		$scope.getOtherLeadInfo = function(id){
			var deferred = $q.defer();
			apiserviceDashborad.getAllSalesPersonOtherLead(id).then(function(data){
				$scope.otherLead = data;
				//$scope.gridOptions13.data = data;
				//$scope.AllOtherLeadSeenList = data;
				if($scope.userType == "Sales Person"){
					angular.forEach($scope.otherLead,function(value,key){
		        		$scope.getAllListLeadDate.push(value);
		        		if(value.noteFlag == 0 && value.confirmDate == null){
		        			countUnReadLead++;
		        		}
		        	});
						$scope.lengthOfAllLead = countUnReadLead;
						deferred.resolve("success");
				}else{
					deferred.resolve("error");
				}
			
		   });
			return deferred.promise;
		};
		
		$scope.getContactUsData = function(id){
			var deferred = $q.defer();
			apiserviceDashborad.getAllSalesPersonContactUsSeen(id).then(function(data){
				var countUnReadLead = 0;
			$scope.gridOptions8.data = data;
			$scope.AllContactUsInfoSeenList = data;
			if($scope.userType == "Sales Person"){
				angular.forEach($scope.gridOptions8.data,function(value,key){
	        		$scope.getAllListLeadDate.push(value);
	        		if(value.noteFlag == 0 && value.confirmDate == null){
	        			countUnReadLead++;
	        		}
	        	});
					$scope.lengthOfAllLead = countUnReadLead;
					deferred.resolve("success");
			}else{
				deferred.resolve("error");
			}
		   });
			return deferred.promise;
		};
		
		
		$scope.getTradeInData = function(id){
			
			var deferred = $q.defer();
			apiserviceDashborad.getAllSalesPersonTradeInSeen(id).then(function(data){
					var countUnReadLead = 0;
			 		$scope.gridOptions3.data = data;
			 		$scope.AllTradeInSeenList = data;
			 		if($scope.userType == "Sales Person"){
						angular.forEach($scope.gridOptions3.data,function(value,key){
			        		$scope.getAllListLeadDate.push(value);
			        		if(value.noteFlag == 0 && value.confirmDate == null){
			        			countUnReadLead++;
			        		}
			        	});
						$scope.lengthOfAllLead = countUnReadLead;
						deferred.resolve("success");
				}else{
					deferred.resolve("error");
				}
			   });
				return deferred.promise;
		};
		
		$scope.addData = function(){
			var deferred = $q.defer();
			var countUnReadLead = 0;
			$scope.getAllListLeadDate = [];
			if($scope.userType == "Manager" || $scope.userType == "Sales Person"){
  				 angular.forEach($scope.gridOptions2.data,function(value,key){
  		        		$scope.getAllListLeadDate.push(value);
  		        		if(value.noteFlag == 0 && value.confirmDate == null){
  		        			countUnReadLead++;
  		        		}
  		        	});
  				 angular.forEach($scope.gridOptions8.data,function(value,key){
		        		$scope.getAllListLeadDate.push(value);
		        		if(value.noteFlag == 0 && value.confirmDate == null){
		        			countUnReadLead++;
		        		}
		        	}); 
  				 angular.forEach($scope.gridOptions5.data,function(value,key){
  		        		$scope.getAllListLeadDate.push(value);
  		        		if(value.noteFlag == 0 && value.confirmDate == null){
  		        			countUnReadLead++;
  		        		}
  		        	});
  				 angular.forEach($scope.gridOptions3.data,function(value,key){
  		        		$scope.getAllListLeadDate.push(value);
  		        		if(value.noteFlag == 0 && value.confirmDate == null){
  		        			countUnReadLead++;
  		        		}
  		        	});
  				 
  				 $scope.lengthOfAllLead = countUnReadLead;
  				 deferred.resolve("success");
  			 }else{
  				deferred.resolve("error");
  			 }
			
			
			return deferred.promise;
		};
		
		$scope.getAllSalesPersonRecord = function(id){
		       $scope.getAllListLeadDate = [];
		       $scope.salesPerson = id;
		       	if($scope.salesPerson == undefined){
		       		$scope.salesPerson = 0;
		       		id = 0;
		       	}
		       	//debugger;
		       	$scope.getScheduleData(id).then(
		       			function(success){
		       				$scope.getRequestData(id).then(
		    		       			function(success){
		    		       				$scope.getTradeInData(id).then(
		    		    		       			function(success){
		    		    		       				$scope.getOtherLeadInfo(id).then(
		    		    		    		       			function(success){
		    		    		       				$scope.getContactUsData(id).then(
		    		    		    		       			function(success){
		    		    		       				$scope.addData().then(
		    		    		    		       			function(success){
		    		    		    		       				$scope.gridOptions7.data = $scope.getAllListLeadDate;
		    		    				           	        	
		    		    				           	        	//$scope.getAllCanceledLeads();
		    		    				           	        	//added by vinayak 23-Apr-2016
		    		    		    		       				apiserviceDashborad.getAllCanceledLeads(id).then(function(data){
		    		    				           					$scope.gridOptions4.data = data;
		    		    				           					$scope.canceledLead = data;
		    		    				           				});
		    		    				           	        	
		    		    		    		       				apiserviceDashborad.getAllSalesPersonLostAndComp(id).then(function(data){
		    		    						       			 		$scope.gridOptions6.data = data;
		    		    						       			 		$scope.AllTradeInSeenList = data;
		    		    						       			 });
		    		    						       			 
		    		    		    		       				apiserviceDashborad.getTestDirConfirById(id).then(function(data){
		    		    						       					$scope.gridOptions9.data = data;
		    		    						       					angular.forEach($scope.gridOptions9.data,function(value,key){
		    		    						       						 value.check = false;
		    		    						       					 });
		    		    						       					$scope.setWether($scope.gridOptions9.data);
		    		    						       					$scope.allTestDirConfir = data;
		    		    						       				
		    		    						       				});
		    		    						       			 
		    		    		    		       				
		    		    		    		       				apiserviceDashborad.getAllCompletedLeadsbyId(id).then(function(data){
		    		    						       					$scope.gridOptions10.data = data;
		    		    						       					$scope.completedL = data;
		    		    						       				});
		    		    		    		       			},function(error){
		    		    		    		       				
		    		    		    		       			}
		    		    		    		       	);
		    		    		    		       			},function(error){
		    		    		    		       				
		    		    		    		       			}
		    		    		    		       	);	
		    		    		       			},function(error){
		    		    		       				
		    		    		       			}
		    		    		       	);
		    		       				
		    		       			},function(error){
		    		       				
		    		       			}
		    		       	);
		    		       		},function(error){
		    		       				
		    		       			}
		    		       	);
		       				
		       			},function(error){
		       				
		       			}
		       	);
		       	
	}
		
		$scope.ducumentNames = [];
		$scope.sendPdfEmail= function (pdf){
			console.log(pdf);
			$scope.pdf.pdfIds = $scope.pdfDoc;
			$scope.pdf.vin=$scope.vehiclePdfVin;
			console.log("$scope.vehiclePdfVin"+$scope.vehiclePdfVin);
			console.log(":::::::::"+$scope.pdfDoc.length);
			$scope.pdf.email=$scope.SendPdfemail;
			$scope.flagForMsg=0;
			
			if($scope.pdfDoc.length != 0 || $scope.pdf.vin != null){
			apiserviceDashborad.sendPdfEmail($scope.pdf).then(function(data){
				
				console.log("$scope.customePdfmodel"+$scope.customePdfmodel);
				
				/* model value undefined */
				if($scope.customePdfmodel == undefined ){
					$scope.customePdfId =$scope.pdfIdForUndefinedModel;
					$scope.customePdfmodel = true;
				}
				console.log("$scope.customePdfId"+$scope.customePdfId);
				console.log(" after $scope.customePdfmodel"+$scope.customePdfmodel);
				if($scope.customePdfmodel == true && $scope.customePdfId != null && $scope.customePdfId != undefined ){
					console.log("iiiiinnnnn");
					apiserviceDashborad.deletePdfById($scope.customePdfId).then(function(data){
				
		  			});
				}
				
				$("#sendPdfmodal").modal('hide');
				$route.reload();
				
			});
			
			}
			else{
				
				$scope.flagForMsg=1;
				
			}
			
			
			
		}
		
		$scope.pdf={};
		$scope.actionOnPdf= function (entity,option){
			console.log("inside pdf");
			console.log(entity);
			apiserviceDashborad.getCustomerPdfForVehicle(entity.vin).then(function(data){
					$scope.vehiclePdfList=data;
					console.log(data);
				
				});
			
			
			
			//$scope.pdf.typeOfLead=entity.typeOfLead;
			if(entity.typeOfLead == "Request More Info"){
				$scope.pdf.typeOfLead="requestMore";
			}
			else if(entity.typeOfLead == "Schedule Test Drive"){
				$scope.pdf.typeOfLead="scheduleTest";
			}
          else if(entity.typeOfLead == "Trade-In Appraisal"){
        	  $scope.pdf.typeOfLead="tradeIn";
			}
			
			
			$scope.pdf.id=entity.id;
				if(option == 'Schedule' || option == 'Rechedule' ){
					$scope.scheduleTestDriveForUser(entity,2);
				}
				else if(option == 'SendPdf'){
					$scope.SendPdfemail=entity.email;
					console.log($scope.SendPdfemail);
					$('#sendPdfRequest').click();
					
					
					apiserviceDashborad.getCustomerPdfData().then(function(data){
       					$scope.customerPdfList=data;
       					console.log(data);
       				
       				});
					
					
				}
				
				else if(option == 'clientele'){
					$scope.createContact(entity)
				}
				
			
		}
		$scope.pdfDoc = [];
		$scope.selectPdf = function(e,item,value){
			console.log(item);
			console.log(value);
			if(value == false || value == undefined){
				$scope.pdfDoc.push(item.customerPdfId);
			}else{
				$scope.deletepdfItem(item);
			}
			console.log($scope.pdfDoc);
		}
		
		
		$scope.selectVehiclePdf = function(vin,value){
			console.log(vin);
			console.log(value);
			if(value == false || value == undefined){
				$scope.vehiclePdfVin=vin;
			}
			
		}
		
		
		
		$scope.deletepdfItem = function(item){
			console.log(item);
			angular.forEach($scope.pdfDoc, function(obj, index){
				 if ((item.customerPdfId == obj)) {
					 $scope.pdfDoc.splice(index, 1);
			       	return;
			    };
			  });
		}
		

		var logofile1;
		$scope.flagForUpload=0;
		$scope.onCustomerFileSelect = function ($files) {
			logofile1 = $files;
			 
		    var ele = document.getElementById('loadingmanual');	
		  	$(ele).show();
			console.log("??????????");
			console.log(logofile1);
			apiserviceDashborad.saveCustomerPdf(logofile1).then(function(data){
			
				 var ele = document.getElementById('loadingmanual');	
		 		   	$(ele).hide();
		 	  			
		 	  				$scope.flagForUpload=1;
		 	  				apiserviceDashborad.getCustomerPdfData().then(function(data){
		       				  console.log("ddddddddd");
		       				  console.log(data);
		       					$scope.lengths=data.length-1;
			 	  				$scope.uploadData=data;
			 	  				
    						angular.forEach($scope.uploadData,function(obj, index){
    							console.log(index);
    							console.log($scope.lengths);
    						 if(index == $scope.lengths) {
    							 console.log(obj);
    							 $scope.pdfIdForUndefinedModel=obj.customerPdfId;
        					$scope.pdfDoc.push(obj.customerPdfId);
    					    }
    						console.log("Customer pdfData");
    					   $scope.customerPdfList=data;
	       					console.log(data);
    					  });
		       				});
				
		});	
		}
		
		
		
		$scope.deletePdf = function(item,model) {
			console.log(item);
			console.log(model);
			
			if(model == undefined || model == false  ){
				$scope.customePdfmodel=false;
			}
			else{
				$scope.customePdfmodel=true;
			}
			
			//$scope.customePdfmodel=model;
			$scope.customePdfId=item.customerPdfId;
			console.log($scope.customePdfId);
			console.log($scope.customePdfmodel);
			
		}
		
		/*$scope.deletePdfForDoc = function(model) {
			
			if(model == undefined){
				model=true;
			}
			console.log("}}}}}");
			console.log($scope.lengths);
			angular.forEach($scope.uploadData,function(obj, index){
				console.log(index);
				console.log($scope.lengths);
			 if(index == $scope.lengths) {
				 console.log(obj);
			$scope.customPdfIdNew = obj.customerPdfId;
		    }
			 
			});
			//$scope.customePdfmodel=model;
			//$scope.customePdfId=item.customerPdfId;
			console.log($scope.customPdfIdNew);
			//console.log($scope.customePdfmodel);
			
		}
		*/
		
		
    	$scope.soldScheduleStatus = function(entity) {
    		$scope.scheduleStatusVal = entity;
    		$scope.soldContact = {};
    		$scope.soldContact.infoId = entity.id;
    		$scope.soldContact.name = entity.name;
    		$scope.soldContact.email = entity.email;
    		$scope.soldContact.phone = entity.phone;
    		$scope.soldContact.custZipCode = entity.custZipCode;
    		$scope.soldContact.enthicity = entity.enthicity;
    		$scope.soldContact.typeOfLead = entity.typeOfLead;
    		$scope.soldContact.parentChildLead = entity.parentChildLead;
    		if(entity.howContactedUs != null && angular.isUndefined(entity.howContactedUs)) {
    			$scope.soldContact.howContactedUs = entity.howContactedUs;
    		} else {
    			$scope.soldContact.howContactedUs = "Online";
    		}
    		if(entity.howFoundUs != null && angular.isUndefined(entity.howFoundUs)) {
    			$scope.soldContact.howFoundUs = entity.howFoundUs;
    		} else {
    			$scope.soldContact.howFoundUs = "";
    		}
    		$scope.vehicletype=entity.typeofVehicle;
    		$scope.soldContact.make = entity.make;
    		$scope.soldContact.year = entity.year;
    		$scope.soldContact.mileage = entity.mileage;
    		$scope.soldContact.price = entity.price;
    		$('#btnCompleteRequest').click();
    	}
    	
    	
    	
    	$scope.cancelScheduleStatus = function(entity) {
    		$scope.scheduleStatusCancel = entity;
    		$scope.reasonToCancel = "";
    		$('#btnCancelSchedule').click();
    	}
    	
    	$scope.cancelScheduleComfir = function(entity){
              $scope.entityVar=entity;
    		$('#btnCancelTestDrive').click();
    		
				entity.bestDay = "";
				entity.bestTime = "";
				$scope.getAllSalesPersonRecord($scope.salesPerson);
			
    		
    	}
    	
    	
    	$scope.cancelScheduleTestDriveComfir = function(){
    		
    		apiserviceDashborad.setScheduleConfirmClose($scope.entityVar.id, $scope.entityVar.typeOfLead).then(function(data){
			
    	    });
    		$route.reload();
    	}
    	
    	$scope.cancelSure = function(){
    		$('#scheduleCancelModal').modal("toggle");
    		if($scope.scheduleStatusCancel.confirmDate == null){
    			$scope.saveScheduleClose();
    		}else{
    			$('#cancelBtn').click();
    		}
    	}
    	
    	$scope.CancelTradeInStatus = function(){
    		$('#tradeInCancelModal').modal("toggle");
    		$('#cancelBtnTradeIn').click();
    	}
    	
    	$scope.saveScheduleClose = function() {
    		apiserviceDashborad.setScheduleStatusClose($scope.scheduleStatusCancel.id, $scope.scheduleStatusCancel.typeOfLead,$scope.reasonToCancel).then(function(data){
					$scope.getScheduleTestData();
					$('#scheduleCancelBtn').click();
					
					//$scope.getAllSalesPersonRecord($scope.salesPerson);
					$route.reload();
					/*for(var i=0;i<$scope.scheduleList.length;i++) {
	 					if($scope.scheduleStatusCancel.id == $scope.scheduleList[i].id) {
	 						$scope.scheduleList.splice(i,1);
	 					}
	 				}*/
			});
    	}
    	
    	$scope.soldContact = {};
    	
    	$scope.completeRequestStatus = function(entity) {
    		$scope.requestStatusComplete = entity;
    		$scope.soldContact = {};
    		$scope.soldContact.infoId = entity.id;
    		$scope.soldContact.name = entity.name;
    		$scope.soldContact.email = entity.email;
    		$scope.soldContact.phone = entity.phone;
    		$scope.soldContact.custZipCode = entity.custZipCode;
    		$scope.soldContact.typeOfLead = entity.typeOfLead;
    		$scope.soldContact.enthicity = entity.enthicity;
    		$scope.soldContact.parentChildLead = entity.parentChildLead;
    		if(entity.howContactedUs != null && angular.isUndefined(entity.howContactedUs)) {
    			$scope.soldContact.howContactedUs = entity.howContactedUs;
    		} else {
    			$scope.soldContact.howContactedUs = "Online";
    		}
    		if(entity.howFoundUs != null && angular.isUndefined(entity.howFoundUs)) {
    			$scope.soldContact.howFoundUs = entity.howFoundUs;
    		} else {
    			$scope.soldContact.howFoundUs = "";
    		}
    		$scope.vehicletype=entity.typeofVehicle;
    		$scope.soldContact.make = entity.make;
    		$scope.soldContact.year = entity.year;
    		$scope.soldContact.mileage = entity.mileage;
    		$scope.soldContact.price = entity.price;
    		$('#btnCompleteRequest').click();
    	};
    	
    	$scope.cancelRequestStatus = function(entity) {
    		$scope.requestStatusCancel = entity;
    		$scope.reasonToCancel = "";
    		$('#btnCancelRequest').click();
    	};
    	
    	$scope.saveRequestStatusCancel = function() {
    		apiserviceDashborad.setRequestStatusCancel($scope.requestStatusCancel.id, $scope.reasonToCancel).then(function(data){
				$('#requestCancelBtn').click();
				$scope.getAllSalesPersonRecord($scope.salesPerson);
			});
    	};
    	
    	$scope.completeTradeInStatus = function(entity) {
    		$scope.tradeInStatusComplete = entity;
    		$scope.soldContact = {};
    		$scope.soldContact.infoId = entity.id;
    		$scope.soldContact.name = entity.name;
    		$scope.soldContact.email = entity.email;
    		$scope.soldContact.phone = entity.phone;
    		$scope.soldContact.custZipCode = entity.custZipCode;
    		$scope.soldContact.enthicity = entity.enthicity;
    		$scope.soldContact.typeOfLead = entity.typeOfLead;
    		$scope.soldContact.parentChildLead = entity.parentChildLead;
    		if(entity.howContactedUs != null && angular.isUndefined(entity.howContactedUs)) {
    			$scope.soldContact.howContactedUs = entity.howContactedUs;
    		} else {
    			$scope.soldContact.howContactedUs = "Online";
    		}
    		if(entity.howFoundUs != null && angular.isUndefined(entity.howFoundUs)) {
    			$scope.soldContact.howFoundUs = entity.howFoundUs;
    		} else {
    			$scope.soldContact.howFoundUs = "";
    		}
    		$scope.vehicletype=entity.typeofVehicle;
    		$scope.soldContact.make = entity.make;
    		$scope.soldContact.year = entity.year;
    		$scope.soldContact.mileage = entity.mileage;
    		$scope.soldContact.price = entity.price;
    		$('#btnCompleteRequest').click();
    	}
    	
    	$scope.saveRequestStatus = function() {
    		
    		$('#soldBtn').attr("disabled", true);
    		apiserviceDashborad.setRequestStatusComplete($scope.soldContact).then(function(data){
				$route.reload();
				if(data=='contact error'){
					$.pnotify({
					    title: "Error",
					    type:'success',
					    text: "Contact email already exist",
					});
				}
				$('#requestCompleteStatusModal').modal('hide');
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Status changed successfully",
				});
				$('#soldBtn').attr("disabled", false);
				$scope.getAllLeadIn();
				$scope.showVehicalBarChart();
			});
    	};		
    	
    	$scope.cancelLead = function(leads,index){
    		if(leads.typeOfLead == "Schedule Test Drive"){
    			leads.option = 0;
    		}else if(leads.typeOfLead == "Request More Info"){
    			leads.option = 1;
    		}else if(leads.typeOfLead == "Trade-In Appraisale"){
    			leads.option = 2;
    		}
    		var change = "0";
    		apiserviceDashborad.setScheduleStatusClose(leads.id,leads.typeOfLead,change).then(function(data){
								
			});
    		
    		$scope.editLeads.parentChildLead.splice(index, 1);
    		
    	}
    	
    	$scope.cancelLeadSold = function(leads,index){
    		
    		
    		if(leads.typeOfLead == "Schedule Test Drive"){
    			leads.option = 0;
    		}else if(leads.typeOfLead == "Request More Info"){
    			leads.option = 1;
    		}else if(leads.typeOfLead == "Trade-In Appraisale"){
    			leads.option = 2;
    		}
    		var change = "0";
    		
    		apiserviceDashborad.setScheduleStatusClose(leads.id,leads.option,change).then(function(data){
				
			});
    		$scope.soldContact.parentChildLead.splice(index, 1);
    		
    	}
    	
    	$scope.cancelLeadSched = function(leads,index){
    		if(leads.typeOfLead == "Schedule Test Drive"){
    			leads.option = 0;
    		}else if(leads.typeOfLead == "Request More Info"){
    			leads.option = 1;
    		}else if(leads.typeOfLead == "Trade-In Appraisale"){
    			leads.option = 2;
    		}
    		var change = "0";
    		apiserviceDashborad.setScheduleStatusClose(leads.id,leads.option,change).then(function(data){
				
			});
    		$scope.testDriveData.parentChildLead.splice(index, 1);
    	}
    	
    	$scope.cancelTradeInStatus = function(entity) {
    		$scope.tradeInStatusCancel = entity;
    		$scope.reasonToCancel = "";
    		$('#btnCancelTradeIn').click();
    	}
    	
    	$scope.saveCancelTradeInStatus = function() {
    		apiserviceDashborad.setTradeInStatusCancel($scope.tradeInStatusCancel.id,$scope.reasonToCancel).then(function(data){
				$('#tradeInCancelBtn').click();
				$scope.getTradeInData();
			});
    	}
    	
    	$scope.scheduleTestData = {};
    	  
    	  $scope.confirmDateTime = function(entity) {
    		  $scope.cnTimeList = [];
    		  $scope.timeList = [];
    		  $scope.scheduleTestData.id = entity.id;
    		  $scope.scheduleTestData.email = entity.email;
    		  $scope.scheduleTestData.confirmDate = entity.confirmDate;
    		  $scope.scheduleTestData.confirmTime = entity.confirmTime;
    		  $scope.scheduleTestData.option = entity.option;
    		  $scope.scheduleTestData.vin = entity.vin;
			   var sDate = entity.confirmDate;
			   apiserviceDashborad.getScheduleTime(entity.vin,sDate).then(function(data){
				   $scope.cnTimeList = data;
				   $scope.timeList = [];
				   $.each(data, function(i, el){
				       if($.inArray(el, $scope.timeList) === -1) $scope.timeList.push(el);
				   })
			   });
    	  }
    	  
    	  $scope.saveConfirmData = function() {
    		  $scope.scheduleTestData.confirmDate = $("#cnfDate").val();
    		  $scope.scheduleTestData.confirmTime = $("#timePick").val();
    		  $scope.scheduleTestData.cnfDateNature=$scope.cnfDateNature;
    		  apiserviceDashborad.saveConfirmData($scope.scheduleTestData).then(function(data){
    	 			$scope.flagForPopUp=1;
    	 			if(data.mesg == "success"){
        	 			$.pnotify({
        				    title: "Success",
        				    type:'success',
        				    text: "Information is saved and email has been sent to"+" "+data.name,
        				});
        	 			$('#modalClose').click();
        	 			$scope.getAllSalesPersonRecord($scope.salesPerson);
        	 			$scope.init();
        	 			$route.reload();
    	 			}else{
    	 				$.pnotify({
        				    title: "Error",
        				    type:'success',
        				    text: "Test Drive Time Allready Exist",
        				});
    	 			}
    	 			
    	 		});
    	  }
    	
    	  $scope.saveToDoData = function() {
    		  $scope.todoData.dueDate = $("#cnftodoDate").val();
    		  
    		  apiserviceDashborad.saveToDoData($scope.todoData).then(function(data){
    	 			
    	 			$('#modaltodoClose').click();
    	 			$scope.getToDoList();
    	 			$scope.init();
    	 			$scope.todoData = {};
    	 		});
    	  }
    	  
    	  $scope.toDoStatusComplete = function(id) {
    		  $scope.toDoId = id;
    		  $('#btnCompleteToDo').click();
    	  }
    	  
    	  $scope.toDoStatusClose = function(id) {
    		  $scope.toDoId = id;
    		  $('#btnCancelToDo').click();
    	  }
    	  
    	  $scope.saveCompleteTodoStatus = function() {
    		  
    		  apiserviceDashborad.saveCompleteTodoStatus($scope.toDoId).then(function(data){
					$scope.getToDoList();
					
					for(var i=0;i<$scope.toDoDateList.length;i++) {
						if($scope.toDoId == $scope.toDoDateList[i].id) {
							$scope.toDoDateList.splice(i,1);
						}
					}
			 });
    		  
    		  
    	  }
    	  
    	  $scope.saveCancelTodoStatus = function() {
    		  apiserviceDashborad.saveCancelTodoStatus($scope.toDoId).then(function(data){
					$scope.getToDoList();
					
					for(var i=0;i<$scope.toDoDateList.length;i++) {
						if($scope.toDoId == $scope.toDoDateList[i].id) {
							$scope.toDoDateList.splice(i,1);
						}
					}
			 });
    	  }

    	  
    	  $scope.options = {
    	            chart: {
    	                type: 'linePlusBarChart',
    	                height: 500,
    	                margin: {
    	                    top: 30,
    	                    right: 75,
    	                    bottom: 50,
    	                    left: 75
    	                },
    	                bars: {
    	                    forceY: [0]
    	                },
    	                bars2: {
    	                    forceY: [0]
    	                },
    	                color: ['#319db5', 'darkred'],
    	                x: function(d,i) { return i },
    	                xAxis: {
    	                    axisLabel: 'X Axis',
    	                    tickFormat: function(d) {
    	                        var dx = $scope.data[0].values[d] && $scope.data[0].values[d].x || 0;
    	                        if (dx > 0) {
    	                            return d3.time.format('%x')(new Date(dx))
    	                        }
    	                        return null;
    	                    }
    	                },
    	                y1Axis: {
    	                    axisLabel: 'Price($)',
    	                    tickFormat: function(d){
    	                        return d3.format(',f')(d);
    	                    }
    	                }
    	            }
    	        };

    	        
    	        
    	   $scope.getWeekChart = function() {
    		   var userId;
    		   if(angular.isUndefined($scope.graphUserId) || $scope.graphUserId == "") {
    			   userId = 0;
    		   } else {
    			   userId = $scope.graphUserId;
    		   }
    		   apiserviceDashborad.getWeekChartData(userId).then(function(data){
    	 			$scope.data = data.map(function(series) {
    	 	    	                     series.values = series.values.map(function(d) { return {x: d[0], y: d[1] } });
    	 	    	                     return series;
    	 	    	                 });
    	 		});
    	   }     
    	   
    	   $scope.getMonthChart = function() {
    		   var userId;
    		   if(angular.isUndefined($scope.graphUserId) || $scope.graphUserId == "") {
    			   userId = 0;
    		   } else {
    			   userId = $scope.graphUserId;
    		   }
    		   apiserviceDashborad.getMonthChartData(userId).then(function(data){
	   	 			$scope.data = data.map(function(series) {
	                     series.values = series.values.map(function(d) { return {x: d[0], y: d[1] } });
 	                     return series;
 	                 });;
	   	 		});
    	   }
    	   
    	   $scope.getThreeMonthChart = function() {
    		   var userId;
    		   if(angular.isUndefined($scope.graphUserId) || $scope.graphUserId == "") {
    			   userId = 0;
    		   } else {
    			   userId = $scope.graphUserId;
    		   }
    		   
    		   apiserviceDashborad.getThreeMonthChartData(userId).then(function(data){
	   	 			$scope.data = data.map(function(series) {
	                     series.values = series.values.map(function(d) { return {x: d[0], y: d[1] } });
 	                     return series;
 	                 });;
	   	 		});
    	   }
    	   
    	   $scope.getSixMonthChart = function() {
    		   var userId;
    		   if(angular.isUndefined($scope.graphUserId) || $scope.graphUserId == "") {
    			   userId = 0;
    		   } else {
    			   userId = $scope.graphUserId;
    		   }
    		   
    		   apiserviceDashborad.getSixMonthChartData(userId).then(function(data){
	   	 			$scope.data = data.map(function(series) {
	                     series.values = series.values.map(function(d) { return {x: d[0], y: d[1] } });
 	                     return series;
 	                 });;
	   	 		});
    	   }
    	   
    	   $scope.getYearChart = function() {
    		   var userId;
    		   if(angular.isUndefined($scope.graphUserId) || $scope.graphUserId == "") {
    			   userId = 0;
    		   } else {
    			   userId = $scope.graphUserId;
    		   }
    		   
    		   apiserviceDashborad.getYearChartData(userId).then(function(data){
	   	 			$scope.data = data.map(function(series) {
	                     series.values = series.values.map(function(d) { return {x: d[0], y: d[1] } });
 	                     return series;
 	                 });;
	   	 		});
    	   }
    	   
    	   $scope.getRangeData = function() {
    		   var userId;
    		   var startDate = $('#startDate').val();
    		   var endDate = $('#endDate').val();
    		   if(startDate!="" && endDate!="") {
    		   var startDateArr = startDate.split('/');
    		   var endDateArr = endDate.split('/');
    		   	startDate = startDateArr[2]+"-"+startDateArr[0]+"-"+startDateArr[1];
    		   	endDate = endDateArr[2]+"-"+endDateArr[0]+"-"+endDateArr[1];
    		   if(angular.isUndefined($scope.graphUserId) || $scope.graphUserId == "") {
    			   userId = 0;
    		   } else {
    			   userId = $scope.graphUserId;
    		   }
    		   
    		   apiserviceDashborad.getRangeChartData(userId, startDate, endDate).then(function(data){
	   	 			$scope.data = data.map(function(series) {
	                     series.values = series.values.map(function(d) { return {x: d[0], y: d[1] } });
 	                     return series;
 	                 });
	   	 		});
    		   }
    	   }
    	   
    	   $scope.showTopPerformers = function() {
    		   $('#topPerf').css("text-decoration","underline");
    		   $('#worstPerf').css("text-decoration","none");
    		   $scope.topPerformers = true;
     		   $scope.worstPerformers = false;
     		   $scope.getPerformanceOfUser();
    	   }
    	   
    	   $scope.showWorstPerformers = function() {
    		   $('#worstPerf').css("text-decoration","underline");
    		   $('#topPerf').css("text-decoration","none");
    		   $('#allPerf').css("text-decoration","none");
    		   $scope.topPerformers = false;
     		   $scope.worstPerformers = true;
     		   $scope.getPerformanceOfUser();
    	   }
    	   
    	   $scope.showWeekPerformers = function() {
    		   $('#weekPerf').css("text-decoration","underline");
    		   $('#monthPerf').css("text-decoration","none");
    		   $('#yearPerf').css("text-decoration","none");
    		   $('#allPerf').css("text-decoration","none");
    		   $scope.weekPerformance = true;
     		   $scope.monthPerformance = false;
     		   $scope.yearPerformance = false;
     		  $scope.allTimePerformance = false;
     		   $scope.getPerformanceOfUser();
    	   }
    	   
    	   $scope.showMonthPerformers = function() {
    		   $('#weekPerf').css("text-decoration","none");
    		   $('#allPerf').css("text-decoration","none");
    		   $('#monthPerf').css("text-decoration","underline");
    		   $('#yearPerf').css("text-decoration","none");
    		   $scope.weekPerformance = false;
     		   $scope.monthPerformance = true;
     		   $scope.yearPerformance = false;
     		  $scope.allTimePerformance = false;
     		   $scope.getPerformanceOfUser();
    	   }
    	   
		   $scope.showYearPerformers = function() {
			   $('#weekPerf').css("text-decoration","none");
    		   $('#monthPerf').css("text-decoration","none");
    		   $('#allPerf').css("text-decoration","none");
    		   $('#yearPerf').css("text-decoration","underline");
    		   $scope.weekPerformance = false;
     		   $scope.monthPerformance = false;
     		   $scope.yearPerformance = true;
     		  $scope.allTimePerformance = false;
     		   $scope.getPerformanceOfUser();
		   }
		   
		   $scope.showAllTimePerformers = function(){
			   $('#weekPerf').css("text-decoration","none");
    		   $('#monthPerf').css("text-decoration","none");
    		   $('#yearPerf').css("text-decoration","none");
    		   $('#allPerf').css("text-decoration","underline");
    		   
    		   $scope.weekPerformance = false;
     		   $scope.monthPerformance = false;
     		   $scope.yearPerformance = false;
     		   $scope.allTimePerformance = true;
     		   $scope.getPerformanceOfUser();
		   }
		  
		   $scope.showNextButton = 0;
		   $scope.userPerformanceList = {};
		   $scope.countNextValue = 0;
		   $scope.getPerformanceOfUser = function() {
			    
			   var startD = $('#startDateValueForSale').val();
			   var endD = $('#endDateValueForSales').val();
			   if(startD == undefined && endD == undefined){
				   
				   startD=$scope.startDateForSalesPeople;
				   endD=$scope.endDateForSalesPeople;
			   }
			   
			   if(angular.isUndefined($scope.salesPersonUser) || $scope.salesPersonUser == "") {
				   $scope.salesPersonUser = 0;
			   }
			   
			    apiserviceDashborad.getUserRole().then(function(data){
						if($scope.userRole != "General Manager"){
							$scope.locationValue = data.location.id;
						}else{
							if(locationId != 0){
								$scope.locationValue = locationId;
							}else{
								$scope.locationValue = 0;
							}
						}
						
						 apiserviceDashborad.getPerformanceOfUser($scope.topPerformers, $scope.worstPerformers, $scope.weekPerformance, $scope.monthPerformance, $scope.yearPerformance, $scope.allTimePerformance, $scope.salesPersonUser, $scope.locationValue, startD, endD).then(function(data){
				 			$scope.userPerformanceList = data;
				 		});
					});
		   }
		   
		   
		   $scope.addNoteToRequestUser = function(entity,type) {
			   $scope.userNoteId = entity.id;
			   $scope.action = "";
			   if(entity.typeOfLead == "Schedule Test" || entity.typeOfLead == "Schedule Test Drive") {
				   $scope.typeOfNote = 'scheduleTest';
			   } else if(entity.typeOfLead == "Request More Info") {
				   $scope.typeOfNote = 'requestMore';
			   } else if(entity.typeOfLead == "Trade In" || entity.typeOfLead == "Trade-In Appraisal") {
				   $scope.typeOfNote = 'tradeIn';
			   } 
			   
			   $scope.userNoteList = entity.note;
			   $scope.userNote = "";
			   apiserviceDashborad.getAllAction().then(function(data){
				   $scope.allAction = data;
		   		});
			   $('#btnUserNote').click();
		   }
		   $scope.showOtherText = 0;
		   $scope.selectOther = function(action){
			   if(action == "Other"){
				   $scope.showOtherText = 1;
			   }else{
				   $scope.showOtherText = 0;
			   }
		   }
		   $scope.errMsg = 0;
		   $scope.saveAction = function(actionValue){
			   if(actionValue == null || actionValue == '' || actionValue == undefined){
				   $scope.errMsg = 1;
			   }else{
				   $scope.errMsg = 0;
				   apiserviceDashborad.saveAction(actionValue).then(function(data){
					   apiserviceDashborad.getAllAction().then(function(data){
						   $scope.allAction = data;
				   		});
					   
					  
					   $scope.showOtherText = 0;
				   });
			   }
		   }
		   
		   $scope.saveUserNote = function() {
			   apiserviceDashborad.saveNoteOfUser($scope.userNoteId, $scope.typeOfNote, $scope.userNote, $scope.action).then(function(data){
		 			
		 			 $scope.getAllSalesPersonRecord($scope.salesPerson);
					$('#noteClose').click();
					if($scope.typeOfNote == 'scheduleTest') {
						$scope.getScheduleTestData();
					}
					if($scope.typeOfNote == 'requestMore') {
						$scope.getRequestMoreData();
					}
					if($scope.typeOfNote == 'tradeIn') {
						$scope.getTradeInData();
					}
					//$scope.getAllSalesPersonRecord($('#salesPersonUserId').val());
		 		});
		   }
		   $scope.testDriveData = {};
		   $scope.scheduleTestDriveForUser = function(entity,option) {
			   $scope.stockWiseData = [];
			   $scope.cnTimeList = [];
	    	   	   $scope.timeList = [];
			   $('#btnTestDrive').click();
			   $scope.testDriveData.id = entity.id;
			   $scope.testDriveData.name = entity.name;
			   $scope.testDriveData.email = entity.email;
			   $scope.testDriveData.phone = entity.phone;
			   $scope.testDriveData.vin = entity.vin;
			   $scope.testDriveData.parentChildLead = entity.parentChildLead;
			   $scope.testDriveData.bestDay = entity.bestDay;
			   $scope.testDriveData.bestTime = entity.bestTime;
			   $scope.testDriveData.confirmDate = entity.confirmDate;
			   $scope.testDriveData.confirmTime = entity.confirmTime;
			   $scope.testDriveData.option = option;
			   $scope.testDriveData.typeOfLead = entity.typeOfLead;
			   //$('#bestTime').val("");
			   $scope.testDriveData.prefferedContact = "";
			   
			   $scope.stockWiseData.push({
					model:entity.model,
					make:entity.make,
					stockNumber:entity.stock,
					year:entity.year,
					bodyStyle:entity.bodyStyle,
					mileage:entity.mileage,
					transmission:entity.transmission,
					drivetrain:entity.drivetrain,
					engine:entity.engine,
					vin:entity.vin,
					imgId:entity.imgId,
				});
			   
		   }
		   
		   $scope.getScheduleTime = function(){
		   }
		   
		   $scope.saveTestDrive = function() {
			   
			   $scope.testDriveData.bestDay = $('#testDriveDate').val();
			   $scope.testDriveData.bestTime = $('#bestTime').val();
			   var aaa = $('#testDriveNature').val();
			   $scope.testDriveData.weatherValue=$scope.wetherValue;
			   	angular.forEach($scope.testDriveData.parentChildLead,function(value,key){
			   			value.bestDay = $('#testDriveDate'+key).val();
			   			value.bestTime =  $('#bestTime'+key).val();
			   	});  
			   
			   $scope.testDriveData.prefferedContact = $("input:radio[name=preffered]:checked").val();
			   
			   apiserviceDashborad.saveTestDrive($scope.testDriveData).then(function(data){
					$('#clsPop').click();
					if(data == "success"){
						  $scope.schedulmultidatepicker();
						   $http.get("/getscheduletest").success(function(data){
							   $scope.scheduleListData = data;
						   });
						$scope.getAllSalesPersonRecord($scope.salesPerson);
						$('#driveClose').click();
						$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Test Drive confirmation email has been sent to"+" "+$scope.testDriveData.name+" ",
						});
						$scope.testDrive();
						$("#test-drive-tabSched").click();
					}else{
						$.pnotify({
						    title: "Error",
						    type:'success',
						    text: "Test Drive Time Allready Exist",
						});
					}
				});
			   
			 
		   }
		   
		   $scope.changeType = function(){
			   if($scope.cal_whe_flag){
				   $scope.cal_whe_flag = false;
				   document.getElementById("btn-whe-cal-toggle").innerHTML = "<i title='Show Calender' class='fa fa-calendar'></i>";
				   $(".cal-report").hide();
				   $(".wheth-report").show();
			   } else{
				   $scope.cal_whe_flag = true;
				   document.getElementById("btn-whe-cal-toggle").innerHTML = "<i title='Show Weather' class='glyphicon glyphicon-cloud'></i>";
				   $(".wheth-report").hide();
				   $(".cal-report").show();
			   }
		   };
		   
		   apiserviceDashborad.getscheduletest().then(function(data){
			   $scope.scheduleListData = data;
		   });
		   
		   $scope.whDataArr = [];
		   $.simpleWeather({
			    location: 'New York',
			    woeid: '',
			    unit: 'f',
			    success: function (weather) {
			    	$scope.whDataArr = weather.forecast;
			    	//alert(JSON.stringify(weather));
			    }});
		   
		   $scope.editServiceType = function(serviceData){
			   document.getElementById("nature-data").innerHTML = "";
			   $scope.data1 = serviceData;
			   $scope.data1.confirmDate = $filter('date')($scope.data1.confirmDate,"MM-dd-yyyy");
			   $scope.data1.confirmTime = $filter('date')($scope.data1.confirmTime,"hh:mm a");
			   $scope.data1.confirmEndTime = $filter('date')($scope.data1.confirmEndTime,"hh:mm a");
			   
			   apiserviceDashborad.getUserForMeeting($scope.data1.confirmDate,$scope.data1.confirmTime,$scope.data1.confirmEndTime).then(function(data){
					$scope.gridOptions11.data = data;
					angular.forEach($scope.gridOptions11.data, function(obj, index){
						if(obj.userStatus == 'N/A'){
							obj.disabled = false;
						}else{
							obj.disabled = true;
						}
					});
					angular.forEach($scope.gridOptions11.data, function(obj, index){
						angular.forEach($scope.data1.userdata, function(obj1, index1){
						if(obj.id == obj1.id ){
							obj.disabled = false;
							obj.isSelect = true;
							
						}
					});
					});
				});
			  
			   $('#dataID').val($scope.data1.id);
			   $('#dataGoogleID').val($scope.data1.google_id);
			   if($scope.data1.meetingStatus == 'meeting'){
				   $('#colored-header').modal();
			   }else{
				   var str =  $scope.data1.confirmDate.split("-");
  				 	var cDate = str[2]+"-"+str[0]+"-"+str[1];
  				 	$scope.data1.confirmDate = cDate;
				   $scope.scheduleTestDriveForUser($scope.data1,2);
			   }
			   
		   };
		   $scope.deleteServiceType = function(serviceData){
			   $scope.appointData = serviceData;
			   if($scope.appointData.setFlagSameUser != null){
				   $('#futureAppointmentsModalDelete').click();
		   		}else{
		   		 $('#futureAppointmentsModal').click();
		   		}
		   };
		   $scope.deleteFutureAppointment = function(){
			   if($scope.appointData.meetingStatus != "meeting"){
				   var resone = "changes";
				   apiserviceDashborad.deleteAppointById($scope.appointData.id,$scope.appointData.typeOfLead,resone).then(function(data){
					   $scope.schedulmultidatepicker();
					   apiserviceDashborad.getscheduletest().then(function(data){
						   $scope.scheduleListData = data;
					   });
				   }); 
			   }else{
			   		 $('#deleteMeeting-model').modal();
			   }
		   };
		   
		   $scope.deleteFutureAppointmentReason = function(reason){
			   apiserviceDashborad.deleteAppointById($scope.appointData.id,$scope.appointData.typeOfLead,reason).then(function(data){
				   $scope.schedulmultidatepicker();
				   $('#deleteMeeting-model').modal('hide');
				   apiserviceDashborad.getscheduletest().then(function(data){
					   $scope.scheduleListData = data;
				   });
			   });
		   }

		   $timeout(function(){
			   $('#cnfReSchDate').on('changeDate', function(e) {
				   document.getElementById("nature-data").innerHTML = "";
				   var day = moment(e.date).format('DD MMM YYYY');
				   var img= "";
				   angular.forEach($scope.whDataArr,function(value,key){
					  if(angular.equals(day, value.date)){
						  if(angular.equals(value.text,"Sunny")){
							  img = "<i class='wi wi-day-sunny'></i>"; 
						  }
						  if(angular.equals(value.text,"Partly Cloudy")){
							  img = "<i class='wi wi-night-partly-cloudy'></i>"; 
						  }
						  if(angular.equals(value.text,"Cloudy")){
							  img = "<i class='fa fa-cloud'></i>"; 
						  }
						  if(angular.equals(value.text,"Rain")){
							img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"Light Rain")){
							  img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"PM Rain")){
							  img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"Thundershowers")){
							  img = "<i class='wi wi-showers'></i>";  
						  }
						  if(angular.equals(value.text,"Fog")){
								img = "<i class='wi wi-fog'></i>";  
						  }
						  if(angular.equals(value.text,"Fair")){
								img = "<i class='glyphicon glyphicon-cloud' title='Show Weather'></i>";  
						  }
						  document.getElementById("nature-data").innerHTML = img+"&nbsp;&nbsp;&nbsp;"+value.text+"&nbsp;&nbsp;&nbsp;"+value.low+"&deg;";
					  }
				   });
			   });
			   
			   $('#testDriveDate').on('changeDate', function(e) {
				   var sDate = $('#testDriveDate').val();
				   apiserviceDashborad.getScheduleTime($scope.testDriveData.vin, sDate).then(function(data){
					   $scope.timeList = [];
					   $.each(data, function(i, el){
					       if($.inArray(el, $scope.timeList) === -1) $scope.timeList.push(el);
					   })
				   });
				   
				   document.getElementById("testDriveNature").innerHTML = "";
				   
				   var day = moment(e.date).format('DD MMM YYYY');
				   var img= "";
				   angular.forEach($scope.whDataArr,function(value,key){
					  if(angular.equals(day, value.date)){
						  if(angular.equals(value.text,"Sunny")){
							  img = "<i class='wi wi-day-sunny'></i>"; 
						  }
						  if(angular.equals(value.text,"Partly Cloudy")){
							  img = "<i class='wi wi-night-partly-cloudy'></i>"; 
						  }
						  if(angular.equals(value.text,"Cloudy")){
							  img = "<i class='fa fa-cloud'></i>"; 
						  }
						  if(angular.equals(value.text,"Rain")){
							img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"PM Rain")){
							  img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"Thundershowers")){
							  img = "<i class='wi wi-showers'></i>";  
						  }
						  if(angular.equals(value.text,"Light Rain")){
							  img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"Fog")){
								img = "<i class='wi wi-fog'></i>";  
						  }
						  if(angular.equals(value.text,"Fair")){
								img = "<i class='glyphicon glyphicon-cloud' title='Show Weather'></i>";  
						  }
						  $scope.wetherValue = value.text+"&"+value.low+"&deg;";
						  document.getElementById("testDriveNature").innerHTML = img+"&nbsp;&nbsp;&nbsp;"+value.text+"&nbsp;&nbsp;&nbsp;"+value.low+"&deg;";
					  }
				   });
			   });
			   
			   $('#cnfDate').on('changeDate', function(e) {
				   var sDate = $('#cnfDate').val();
				   
				   apiserviceDashborad.getScheduleTime($scope.scheduleTestData.vin, sDate).then(function(data){
					   $scope.cnTimeList = data;
					   $scope.timeList = [];
					   $.each(data, function(i, el){
					       if($.inArray(el, $scope.timeList) === -1) $scope.timeList.push(el);
					   })
				   });
				   
				   document.getElementById("gridCnfDateNature").innerHTML = "";
				   var day = moment(e.date).format('DD MMM YYYY');
				   var img= "";
				   angular.forEach($scope.whDataArr,function(value,key){
					  if(angular.equals(day, value.date)){
						  if(angular.equals(value.text,"Sunny")){
							  img = "<i class='wi wi-day-sunny'></i>"; 
						  }
						  if(angular.equals(value.text,"Partly Cloudy")){
							  img = "<i class='wi wi-night-partly-cloudy'></i>"; 
						  }
						  if(angular.equals(value.text,"Cloudy")){
							  img = "<i class='fa fa-cloud'></i>"; 
						  }
						  if(angular.equals(value.text,"Rain")){
							img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"Light Rain")){
							  img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"PM Rain")){
							  img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"Thundershowers")){
							  img = "<i class='wi wi-showers'></i>";  
						  }
						  if(angular.equals(value.text,"Fog")){
								img = "<i class='wi wi-fog'></i>";  
						  }
						  if(angular.equals(value.text,"Fair")){
								img = "<i class='glyphicon glyphicon-cloud' title='Show Weather'></i>";  
						  }
						  $scope.cnfDateNature=value.text+"&"+value.low+"&deg;";
						  document.getElementById("gridCnfDateNature").innerHTML = img+"&nbsp;&nbsp;&nbsp;"+value.text+"&nbsp;&nbsp;&nbsp;"+value.low+"&deg;";
					  }
				   });
			   });
			   
			   $('#cnfmeetingdate').on('changeDate', function(e) {
				   document.getElementById("meetingnature").innerHTML = "";
				   var day = moment(e.date).format('DD MMM YYYY');
				   var img= "";
				   angular.forEach($scope.whDataArr,function(value,key){
					  if(angular.equals(day, value.date)){
						  if(angular.equals(value.text,"Sunny")){
							  img = "<i class='wi wi-day-sunny'></i>"; 
						  }
						  if(angular.equals(value.text,"Partly Cloudy")){
							  img = "<i class='wi wi-night-partly-cloudy'></i>"; 
						  }
						  if(angular.equals(value.text,"Cloudy")){
							  img = "<i class='fa fa-cloud'></i>"; 
						  }
						  if(angular.equals(value.text,"Rain")){
							img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"Light Rain")){
							  img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"PM Rain")){
							  img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"Thundershowers")){
							  img = "<i class='wi wi-showers'></i>";  
						  }
						  if(angular.equals(value.text,"Fog")){
								img = "<i class='wi wi-fog'></i>";  
						  }
						  if(angular.equals(value.text,"Fair")){
								img = "<i class='glyphicon glyphicon-cloud' title='Show Weather'></i>";  
						  }
						  document.getElementById("meetingnature").innerHTML = img+"&nbsp;&nbsp;&nbsp;"+value.text+"&nbsp;&nbsp;&nbsp;"+value.low+"&deg;";
					  }
				   });
			   });
			   
			   
			   $('#leadBestDay').on('changeDate', function(e) {
				   
				   document.getElementById("meetingnature1").innerHTML = "";
				   var day = moment(e.date).format('DD MMM YYYY');
				   var img= "";
				   angular.forEach($scope.whDataArr,function(value,key){
					  if(angular.equals(day, value.date)){
						  if(angular.equals(value.text,"Cloudy")){
							  img = "<i class='fa fa-cloud'></i>"; 
						  }
						  if(angular.equals(value.text,"Rain")){
							img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"Light Rain")){
							  img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"PM Rain")){
							  img = "<i class='wi wi-rain'></i>";  
						  }
						  if(angular.equals(value.text,"Thundershowers")){
							  img = "<i class='wi wi-showers'></i>";  
						  }
						  if(angular.equals(value.text,"Fog")){
								img = "<i class='wi wi-fog'></i>";  
						  }
						  if(angular.equals(value.text,"Fair")){
								img = "<i class='glyphicon glyphicon-cloud' title='Show Weather'></i>";  
						  }
						  document.getElementById("meetingnature1").innerHTML = img+"&nbsp;&nbsp;&nbsp;"+value.text+"&nbsp;&nbsp;&nbsp;"+value.low+"&deg;";
					  }
				   });
			   });
			   
		   }, 4000);
		   
		   $scope.resettestDriveNature = function(){
			   document.getElementById("testDriveNature").innerHTML = "";
		   };
		   
		   $scope.confres = function(){
			   document.getElementById("gridCnfDateNature").innerHTML = "";
		   };
		   
		   $scope.schmeeting = {};
		   $scope.locationdata = {};
		   $scope.manager = {};
		   $scope.user = {};
		   
		   $scope.openNeweeting = function(){
			   $scope.schmeeting = {};
			   $scope.manager = {};
			   $scope.user = {};
			   $scope.checkManagerLogin();
			  
			   $('#cnfmeetingdate').val('');
				$('#cnfmeetingtime').val('');
				$('#cnfmeetingtimeEnd').val('');
			   $scope.gridOptions11.data = [];
			   $scope.getGMData1();
			   $('#meeting-model').modal();
		   };
		   
		  
		   $scope.getSalesPersonData = function(){
			   if(locationId != 0){
				   $scope.locationValue = locationId;
			   }
			   apiserviceDashborad.getSalesUserOnly($scope.locationValue).then(function(data){
	    			$scope.salesPersonPerf = data;
	    			 $scope.gridOptionsValue.data = $scope.salesPersonPerf;
	    			angular.forEach($scope.salesPersonPerf, function(value, key) {
	    				value.isSelected = false;
	    			});
	    		});
			   $scope.showGrid = 0;
		   }
		   
		   $scope.showGrid = 0;
		   
		   $scope.getLocationData = function(locationId){
			   $scope.locationTotal = 0;
			    $scope.locationList = [];
			   angular.forEach($scope.locationdata, function(value, key){
				   if(value.id == locationId){
					   value.isSelected = true;
					   $scope.locationList.push(locationId);
				   }else{
					   value.isSelected = false;
				   }
			   });
			   
			   $scope.schPlan.scheduleBy = 'location';
			   apiserviceDashborad.getLocationPlan(locationId).then(function(data){
				   $scope.MonthTotal = {};
				   	$scope.totalLocationPlanData = data;
				   
				   
				   angular.forEach(data, function(obj, index){
					   
					   $scope.locationTotal = parseInt($scope.locationTotal) + parseInt(obj.totalEarning);
					    if(obj.month == "january"){
					    	$scope.MonthTotal.januaryTotalEarning = obj.totalEarning;
					    }
					    if(obj.month == "february"){
					    	$scope.MonthTotal.februaryTotalEarning = obj.totalEarning;
					    }
					    if(obj.month == "march"){
					    	$scope.MonthTotal.marchTotalEarning = obj.totalEarning;
					    }
					    if(obj.month == "april"){
					    	$scope.MonthTotal.aprilTotalEarning = obj.totalEarning;
					    }
					    if(obj.month == "may"){
					    	$scope.MonthTotal.mayTotalEarning = obj.totalEarning;
					    }
					    if(obj.month == "june"){
					    	$scope.MonthTotal.juneTotalEarning = obj.totalEarning;
					    }
					    if(obj.month == "july"){
					    	$scope.MonthTotal.julyTotalEarning = obj.totalEarning;
					    }
					    if(obj.month == "august"){
					    	$scope.MonthTotal.augustTotalEarning = obj.totalEarning;
					    }
					    if(obj.month == "september"){
					    	$scope.MonthTotal.septemberTotalEarning = obj.totalEarning;
					    }
					    if(obj.month == "october"){
					    	$scope.MonthTotal.octoberTotalEarning = obj.totalEarning;
					    }
					    if(obj.month == "november"){
					    	$scope.MonthTotal.novemberTotalEarning = obj.totalEarning;
					    }
					    if(obj.month == "december"){
					    	$scope.MonthTotal.decemberTotalEarning = obj.totalEarning;
					    }
					    
				   });
			   });
		   }
		   $scope.copyValue = function(monthValue){
			   $scope.leadsTime.totalEarning = monthValue;
		   }
		   
		   $scope.copyValueSale = function(monthValue){
			   $scope.saleleadsTime.totalBrought = monthValue;
		   }
		   
		   
		   
		  
		   
		   
		   $scope.planForsalePerson = function(){
			   $('#salepersonPlanModel').modal();
			   apiserviceDashborad.getPlanByMonthAndUser($scope.userKey, $scope.parLocationData.monthCurr).then(function(data){
					$scope.saleMonthTotalPer=data;
					$scope.monthFlagForSale=0;
				});
			   
		   }
		  
		   /*$scope.planForsalePersonForMonth = function(month){
			   $('#salepersonPlanModelForMonth').modal();
			   $http.get('/getPlanByMonthAndUser/'+$scope.userKey+'/'+month)
				.success(function(data) {
					if(data != null){
						$scope.monthFlagForSale=1;
					}
					$scope.saleMonthTotalPerForMonth=data;
				});
		   }*/
		   
		   $scope. planForLocationManager = function(){
			   $('#locationPlanModel').modal();
			   apiserviceDashborad.getPlanByMonthAndUserForLocation($scope.userKey, $scope.parLocationData.monthCurr).then(function(data){
					$scope.locationTotalPer=data;
				});
		   }
		   
		   $scope.locationTotal = 0;
		   $scope.saveLocationPlan = function(month, locationIds){
			   var value = 0;
			   $scope.locationTotal = 0;
			   $scope.leadsTime.locationList  = $scope.locationList;
			   value = $scope.leadsTime.totalEarning;
			   
			   if(locationId != 0){
				   apiserviceDashborad.gmLocationManager(locationId).then(function(data){
						$scope.leadsTime.userkey = data.id;
					});
				}else{
					$scope.leadsTime.userkey = $scope.userKey;
				}
			   
			   $scope.leadsTime.month = month;
			   apiserviceDashborad.saveLocationPlan($scope.leadsTime).then(function(data){
				   $scope.janOpen = 0;
				   $scope.julyOpen = 0;
				   $scope.februaryOpen = 0;
				   $scope.decemberOpen = 0;
				   $scope.juneOpen = 0;
				   $scope.mayOpen = 0;
				   $scope.novemberOpen = 0;
				   $scope.octoberOpen = 0;
				   $scope.aprilOpen = 0;
				   $scope.septemberOpen = 0;
				   $scope.marchOpen = 0;
				   $scope.augustOpen = 0;
				   $scope.leadsTime.totalEarning = "";
				   $scope.leadsTime.minEarning = "";
				   $scope.leadsTime.vehiclesSell = "";
				   $scope.leadsTime.avgCheck = "";
				   
				   if($scope.userRole == "Manager"){
					   var startD = $('#cnfstartDateValue').val();
					   var endD = $('#cnfendDateValue').val();
					   $scope.findMystatisData(startD,endD,'location');
				   }
				   
				   if($scope.userType == "General Manager"){
					   if(locationId != 0){
						   $scope.getLocationPlan();
					   }else{
						   $scope.getLocationData(locationIds);
					   }
					   
				   }else{
					   $scope.getLocationPlan();
				   }
			   });
		   }
		   
		   $scope.saveLocationTotal = function(total, locationIds){
			   if(locationIds == null){
				   locationIds = 0;
			   }
			   if(locationId != 0){
				   locationIds = locationId;
			   }
			   
			   apiserviceDashborad.saveLocationTotal(total, locationIds).then(function(data){
				   $('#plan-model').modal("toggle");
			   });
		   }
		   
		   $scope.saveSalesTotal = function(total){
			   $scope.userkey=$scope.userKeyforSalestotal;
			   if(locationId != 0){
				   
				   apiserviceDashborad.gmLocationManager(locationId).then(function(data){
					   apiserviceDashborad.saveSalesTotal(total,data.id).then(function(data){
							   $('#plan-model').modal("toggle");
						   });
					});
				   
				   
			   }else{
				   apiserviceDashborad.saveSalesTotal(total,$scope.userkey).then(function(data){
					   $('#plan-model').modal("toggle");
				   });
			   }
			   
		   }
		   
		   $scope.saleleadsTime = {};
		   $scope.saveSalePersonPlan = function(month){
			   $scope.salePerpleTotal = 0;
			  var value= 0;
			  $scope.salesList.push($scope.salePerId);
			  value = $scope.saleleadsTime.totalBrought;
			   $scope.saleleadsTime.salesList = $scope.salesList;
			   $scope.saleleadsTime.month = month;
			   apiserviceDashborad.saveSalePlan($scope.saleleadsTime).then(function(data){
				   $scope.janOpen = 0;
				   $scope.julyOpen = 0;
				   $scope.februaryOpen = 0;
				   $scope.decemberOpen = 0;
				   $scope.juneOpen = 0;
				   $scope.mayOpen = 0;
				   $scope.novemberOpen = 0;
				   $scope.octoberOpen = 0;
				   $scope.aprilOpen = 0;
				   $scope.septemberOpen = 0;
				   $scope.marchOpen = 0;
				   $scope.augustOpen = 0;
				   $scope.saleleadsTime.totalEarning = "";
				   $scope.saleleadsTime.minEarning = "";
				   $scope.saleleadsTime.vehiclesSell = "";
				   $scope.saleleadsTime.avgCheck = "";
				   $scope.findVehicalPlan($scope.salePerId);
			   });
		   }
		   
		   $scope.getSalePersonData = function(salesId){
			   $scope.salesIdPlan = "salePerson";
			   $scope.getSalesDataValue($scope.locationValueForPlan) ;
			   $scope.schPlan.scheduleBy = "salePerson"
		   }
		   
		   $scope.getLocation = function(){
			   $scope.schPlan.scheduleBy = "location";
		   }
		   
		   $scope.gridOptionsValue = {
	 		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		 		    paginationPageSize: 150,
	 		 		    useExternalFiltering: true,
	 		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 		 };
	 		 		 $scope.gridOptionsValue.enableHorizontalScrollbar = 0;
	 		 		 $scope.gridOptionsValue.enableVerticalScrollbar = 2;
	 		 		 $scope.gridOptionsValue.columnDefs = [
	 		 		                                 { name: 'fullName', displayName: 'Full Name', width:'40%',cellEditableCondition: false,
	 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	 		   		                                       if (row.entity.isRead === false) {
	 		   		                                         return 'red';
	 		   		                                     }
	 		  		                                	} ,
	 		 		                                 },
	 		 		                               { name: 'quota', displayName: 'Quota', width:'20%',cellEditableCondition: false,
		 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		 		   		                                       if (row.entity.isRead === false) {
		 		   		                                         return 'red';
		 		   		                                     }
		 		  		                                	} ,
		 		 		                                 },
	 		 		                               
	 		 		                               { name: 'edit', displayName: 'Edit', width:'14%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
	 		   		   	                                cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editPlanDetail(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i>', 
	 		   		                                
	 		   		                                },
	 		 		                               
	 		     		                                 ];
		   
		   
	 		 		 $scope.salePerId = 0;
		   $scope.editPlanDetail = function(row) {
			 $scope.schedule = $scope.schPlan.scheduleBy;
			 $scope.saleperson = $scope.schPlan.salePerson;
			  $scope.schPlan = row.entity;
			  $scope.findVehicalPlan(row.entity.id);
			  $scope.schPlan.scheduleBy = $scope.schedule;
			  $scope.schPlan.salePerson = $scope.saleperson;
			  $scope.planIs = "update";
			
			   $scope.nextbutton = 1;
		   } 
		   $scope.saleMonthTotal = {};
		   $scope.salePerpleTotal = 0;
		   $scope.findVehicalPlan = function(saleId){
			   $scope.salePerpleTotal = 0;
			   $scope.saleMonthTotal = {};
			   $scope.salePerId = saleId;
			   $scope.userKeyforSalestotal=saleId;
			   apiserviceDashborad.getSaleMonthlyPlan(saleId).then(function(data){
				   $scope.totalLocationPlanData = data;
				   var d = new Date();
				   var n = d.getMonth()+1;
				   angular.forEach(data, function(obj, index){
					   $scope.salePerpleTotal = parseInt($scope.salePerpleTotal) + parseInt(obj.totalBrought);
					    if(obj.month == "january"){
					    	$scope.monthValue=1;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.januaryFlag=true;
					    	}
					    	
					    	$scope.saleMonthTotal.januaryTotalEarning = obj.totalBrought;
					    }
					    else{
					    	$scope.monthValue=1;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.januaryFlag=true;
					    	}
					    	
					    }
					    if(obj.month == "february"){
					    	$scope.monthValue=2;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.februaryFlag=true;
					    	}
					    	$scope.saleMonthTotal.februaryTotalEarning = obj.totalBrought;
					    }
					    else{
					    	$scope.monthValue=2;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.februaryFlag=true;
					    	}
					    }
					    if(obj.month == "march"){
					    	$scope.monthValue=3;
					    	if($scope.monthValue < n){
					    		
					    		$scope.saleMonthTotal.marchFlag=true;
					    	}
						    	
					    	$scope.saleMonthTotal.marchTotalEarning = obj.totalBrought;
					    }
					    else{
					    	$scope.monthValue=3;
					    	if($scope.monthValue < n){
					    		
					    		$scope.saleMonthTotal.marchFlag=true;
					    	}
					    }
					    if(obj.month == "april"){
					    	$scope.monthValue=4;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.aprilFlag=true;
					    	}
						    	
					    	$scope.saleMonthTotal.aprilTotalEarning = obj.totalBrought;
					    }
					    else{
					    	$scope.monthValue=4;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.aprilFlag=true;
					    	}
					    	
					    }
					    if(obj.month == "may"){
					    	$scope.monthValue=5;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.mayFlag=true;
					    	}
						    	
					    	$scope.saleMonthTotal.mayTotalEarning = obj.totalBrought;
					    }
						else{
							$scope.monthValue=5;
					    	if($scope.monthValue < n){
								$scope.saleMonthTotal.mayFlag=true;
					    	}
					    		
						}
					    if(obj.month == "june"){
					    	$scope.monthValue=6;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.juneFlag=1;
					    	}
						    	
					    	$scope.saleMonthTotal.juneTotalEarning = obj.totalBrought;
					    }
						else{
							$scope.monthValue=6;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.juneFlag=true;
					    	}
						}
					    if(obj.month == "july"){
					    	$scope.monthValue=7;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.julyFlag=true;
					    	}
						    	
					    	$scope.saleMonthTotal.julyTotalEarning = obj.totalBrought;
					    }
						else{
							$scope.monthValue=7;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.julyFlag=true;
					    	}
						}
					    if(obj.month == "august"){
					    	$scope.monthValue=8;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.augustFlag=1;
					    	}
						    	
					    	$scope.saleMonthTotal.augustTotalEarning = obj.totalBrought;
					    }
						else{
							$scope.monthValue=8;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.augustFlag=true;
					    	}
						}
					    if(obj.month == "september"){
					    	$scope.monthValue=9;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.septemberFlag=true;
					    	}
						    	
					    	$scope.saleMonthTotal.septemberTotalEarning = obj.totalBrought;
					    }
						else{
							$scope.monthValue=9;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.septemberFlag=true;
					    	}
						}
					    if(obj.month == "october"){
					    	$scope.monthValue=10;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.octoberFlag=true;
					    	}
						    	
					    	$scope.saleMonthTotal.octoberTotalEarning = obj.totalBrought;
					    }
						else{
							$scope.monthValue=10;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.octoberFlag=true;
					    	}
							
						}
					    if(obj.month == "november"){
					    	$scope.monthValue=11;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.novemberFlag=true;
					    	}
						    	
					    	$scope.saleMonthTotal.novemberTotalEarning = obj.totalBrought;
					    }
						else{
							$scope.monthValue=11;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.novemberFlag=true;
					    	}
						}
					    if(obj.month == "december"){
					    	$scope.monthValue=12;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.decemberFlag=true;
					    	}
					    	$scope.saleMonthTotal.decemberTotalEarning = obj.totalBrought;
					    }
						else{
							$scope.monthValue=12;
					    	if($scope.monthValue < n){
					    		$scope.saleMonthTotal.decemberFlag=true;
					    	}
						}
					    
				   });
			   });
			   
		   }
		   
		   $scope.checkDatewsie = function(){
			   var startD = $('#cnfstartDateValue').val();
			   var endD = $('#cnfendDateValue').val();
			   $scope.findMystatisData(startD,endD,$scope.dataLocOrPerWise);
			   
			   
			   $scope.startDateForListing = $filter('date')(startD, 'dd-MM-yyyy');
 				$scope.endDateForListing = 	$filter('date')(endD, 'dd-MM-yyyy');
 				$scope.vehicleData("All",$scope.startDateForListing,$scope.endDateForListing);
			   
 				
 				
 				$scope.volumeStatStartDate=$filter('date')(startD, 'dd-MM-yyyy');
 				$scope.volumeStatEndDate = $filter('date')(endD, 'dd-MM-yyyy');
 				 var arr = [];
 	  			 var arr1 = [];
 	  			   arr = startD.split('-');
 	  			 arr1 = endD.split('-');
 	  			$scope.volumeStatStartDate = arr[2]+"-"+arr[1]+"-"+arr[0];
 	  			$scope.volumeStatEndDate= arr1[2]+"-"+arr1[1]+"-"+arr1[0];
  				$scope.showVehicalBarChart($scope.volumeStatStartDate, $scope.volumeStatEndDate);
  				$scope.startDateForSalesPeople=$filter('date')(startD, 'dd-MM-yyyy');
  				$scope.endDateForSalesPeople=$filter('date')(endD, 'dd-MM-yyyy');
  				 $('#startDateValueForSale').val(startD);
  				$('#endDateValueForSales').val(endD);
  				$scope.getPerformanceOfUser();
  				$scope.startDateV = $filter('date')(startD, 'yyyy-MM-dd');
  				$scope.endDateV = $filter('date')(endD, 'yyyy-MM-dd');
  				
  				 var arr = [];
 	  			 var arr1 = [];
 	  			   arr = startD.split('-');
 	  			 arr1 = endD.split('-');
 	  			$scope.startDateV = arr[2]+"-"+arr[1]+"-"+arr[0];
 	  			$scope.endDateV= arr1[2]+"-"+arr1[1]+"-"+arr1[0];
  				
 	  			 $("#vstartDate").val($scope.startDateV);
   			    $("#vendDate").val($scope.endDateV);
  				$scope.visitorsStats($scope.startDateV, $scope.endDateV);
		   }
		   
		   
		   $scope.openPlanning = function(){
			   $scope.schPlan = {};
			   $scope.nextbutton = 0;
			   $scope.checkManagerLogin();
			   if($scope.userType != "General Manager"){
				   $scope.getLocationPlan();
			   }else{
				   if(locationId != 0){
					   $scope.getLocationPlan();
				   }
			   }
			   $('#plan-model').modal();
		   };
		   $scope.openPlanningForSale = function(id){
			   $scope.schPlan = {};
			   $scope.nextbutton = 0;
			   $scope.entity;
			   $scope.checkManagerLogin();
			   if($scope.userType != "General Manager"){
				   $scope.getLocationPlan();
			   }else{
				   if(locationId != 0){
					   $scope.getLocationPlan();
				   }
			   }
			   $('#plan-model').modal();
			   $('#pln').click();
			   if(locationId != 0){
				   $scope.locationValue = locationId;
			   }
			   apiserviceDashborad.getSalesUserOnly($scope.locationValue).then(function(data){
	    			$scope.salesPersonPerf = data;
	    			 $scope.gridOptionsValue.data = $scope.salesPersonPerf;
	    			angular.forEach($scope.salesPersonPerf, function(value, key) {
						if(id==value.id){
							$scope.nextbutton = 1;
							$scope.schPlan.scheduleBy = 'salePerson';
							$scope.entity = value;
							$scope.schedule = $scope.schPlan.scheduleBy;
							$scope.saleperson = $scope.schPlan.salePerson;
							$scope.schPlan = $scope.entity;
							$scope.findVehicalPlan($scope.entity.id);
							$scope.schPlan.scheduleBy = $scope.schedule;
							$scope.schPlan.salePerson = $scope.saleperson;
							$scope.planIs = "update";
							
						}
	    				value.isSelected = false;
	    			});
	    		});
			   
			   
		   };
		   
		   $scope.MonthTotal = {};
		   $scope.totalLocationPlanData = null;
		   $scope.getLocationPlan = function(){
			   $scope.locationTotal = 0;
			   
			   $scope.value = 0;
			   var d = new Date();
			   var n = d.getMonth()+1;
			   if(locationId != 0){
				   apiserviceDashborad.gmLocationManager(locationId).then(function(data){
				   apiserviceDashborad.getlocationsMonthlyPlan(datas.id).then(function(data){
							   $scope.totalLocationPlanData = data;
							   var d = new Date();
							   var n = d.getMonth()+1;
							   angular.forEach(data, function(obj, index){
								   $scope.locationTotal = parseInt($scope.locationTotal) + parseInt(obj.totalEarning);
								    if(obj.month == "january"){
								    	$scope.monthValue=1;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.januaryFlag=true;
								    	}
								    	$scope.MonthTotal.januaryTotalEarning = obj.totalEarning;
								    }
								    else{
								    	$scope.monthValue=1;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.januaryFlag=true;
								    	}
								    	
								    }
								    if(obj.month == "february"){
								    	
								    	$scope.monthValue=2;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.februaryFlag=true;
								    	}
								    	
								    	$scope.MonthTotal.februaryTotalEarning = obj.totalEarning;
								    }
								    else{
								    	$scope.monthValue=2;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.februaryFlag=true;
								    	}
								    	
								    }
								    if(obj.month == "march"){
								    	$scope.monthValue=3;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.marchFlag=true;
								    	}
								    	
								    	$scope.MonthTotal.marchTotalEarning = obj.totalEarning;
								    }
								    else{
								    	$scope.monthValue=3;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.marchFlag=true;
								    	}
								    	
								    }
								    if(obj.month == "april"){
								    	
								    	$scope.monthValue=4;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.aprilFlag=true;
								    	}
								    	$scope.MonthTotal.aprilTotalEarning = obj.totalEarning;
								    }
								    else{
								    	$scope.monthValue=4;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.aprilFlag=true;
								    	}
								    }
								    if(obj.month == "may"){
								    	$scope.monthValue=5;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.mayFlag=true;
								    	}
								    	
								    	$scope.MonthTotal.mayTotalEarning = obj.totalEarning;
								    }
								    else{
								    	$scope.monthValue=5;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.mayFlag=true;
								    	}
								    }
								    if(obj.month == "june"){
								    	
								    	$scope.monthValue=6;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.juneFlag=true;
								    	}
								    	$scope.MonthTotal.juneTotalEarning = obj.totalEarning;
								    }
								    else{
								    	$scope.monthValue=6;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.juneFlag=true;
								    	}
								    }
								    if(obj.month == "july"){
								    	$scope.monthValue=7;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.julyFlag=true;
								    	}
								    	$scope.MonthTotal.julyTotalEarning = obj.totalEarning;
								    }else{
								    	$scope.monthValue=7;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.julyFlag=true;
								    	}
								    }
								    if(obj.month == "august"){
								    	
								    	$scope.monthValue=8;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.augustFlag=true;
								    	}
								    	$scope.MonthTotal.augustTotalEarning = obj.totalEarning;
								    }
								    else{
								    	$scope.monthValue=8;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.augustFlag=true;
								    	}
								    }
								    if(obj.month == "september"){
								    	
								    	$scope.monthValue=9;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.septemberFlag=true;
								    	}
								    	
								    	$scope.MonthTotal.septemberTotalEarning = obj.totalEarning;
								    }
								    else{
								    	$scope.monthValue=9;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.septemberFlag=true;
								    	}
								    }
								    if(obj.month == "october"){
								    	$scope.monthValue=10;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.octoberFlag=true;
								    	}
								    	$scope.MonthTotal.octoberTotalEarning = obj.totalEarning;
								    }
								    else{
								    	$scope.monthValue=10;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.octoberFlag=true;
								    	}
								    }
								    if(obj.month == "november"){
								    	$scope.monthValue=11;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.novemberFlag=true;
								    	}
								    	$scope.MonthTotal.novemberTotalEarning = obj.totalEarning;
								    }
								    else{
								    	$scope.monthValue=11;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.novemberFlag=true;
								    	}
								    }
								    if(obj.month == "december"){
								    	$scope.monthValue=12;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.decemberFlag=true;
								    	}
								    	
								    	$scope.MonthTotal.decemberTotalEarning = obj.totalEarning;
								    }
								    else{
								    	
								    	$scope.monthValue=12;
								    	if($scope.monthValue < n){
								    		$scope.saleMonthTotal.decemberFlag=true;
								    	}
								    	
								    }
								    
							   });
						   });
					});
				  
			   }else{
				   apiserviceDashborad.getlocationsMonthlyPlan($scope.userKey).then(function(data){
					   $scope.totalLocationPlanData = data;
					   angular.forEach(data, function(obj, index){
						   $scope.locationTotal = parseInt($scope.locationTotal) + parseInt(obj.totalEarning);
						    if(obj.month == "january"){
						    	$scope.monthValue=1;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.januaryFlag=true;
						    	}
						    	$scope.MonthTotal.januaryTotalEarning = obj.totalEarning;
						    }
						    else{
						    	$scope.monthValue=1;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.januaryFlag=true;
						    	}
						    }
						    if(obj.month == "february"){
						    	$scope.monthValue=2;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.februaryFlag=true;
						    	}
						    	$scope.MonthTotal.februaryTotalEarning = obj.totalEarning;
						    }else{
						    	$scope.monthValue=2;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.februaryFlag=true;
						    	}
						    }
						    if(obj.month == "march"){
						    	$scope.monthValue=3;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.marchFlag=true;
						    	}
						    	$scope.MonthTotal.marchTotalEarning = obj.totalEarning;
						    }
						    else{
						    	$scope.monthValue=3;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.marchFlag=true;
						    	}
						    }
						    if(obj.month == "april"){
						    	$scope.monthValue=4;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.aprilFlag=true;
						    	}
						    	$scope.MonthTotal.aprilTotalEarning = obj.totalEarning;
						    }else{
						    	$scope.monthValue=4;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.aprilFlag=true;
						    	}
						    }
						    if(obj.month == "may"){
						    	$scope.monthValue=5;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.mayFlag=true;
						    	}
						    	$scope.MonthTotal.mayTotalEarning = obj.totalEarning;
						    }
						    else{
						    	$scope.monthValue=5;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.mayFlag=true;
						    	}
						    }
						    if(obj.month == "june"){
						    	$scope.monthValue=6;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.juneFlag=true;
						    	}
						    	$scope.MonthTotal.juneTotalEarning = obj.totalEarning;
						    }
						    else{
						    	$scope.monthValue=6;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.juneFlag=true;
						    	}
						    }
						    if(obj.month == "july"){
						    	$scope.monthValue=7;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.julyFlag=true;
						    	}
						    	$scope.MonthTotal.julyTotalEarning = obj.totalEarning;
						    }else{
						    	$scope.monthValue=7;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.julyFlag=true;
						    	}
						    }
						    if(obj.month == "august"){
						    	$scope.monthValue=8;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.augustFlag=true;
						    	}
						    	
						    	$scope.MonthTotal.augustTotalEarning = obj.totalEarning;
						    }
						    else{
						    	$scope.monthValue=8;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.augustFlag=true;
						    	}
						    }
						    if(obj.month == "september"){
						    	
						    	$scope.monthValue=9;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.septemberFlag=true;
						    	}
						    	$scope.MonthTotal.septemberTotalEarning = obj.totalEarning;
						    }
						    else{
						    	$scope.monthValue=9;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.septemberFlag=true;
						    	}
						    }
						    if(obj.month == "october"){
						    	
						    	$scope.monthValue=10;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.octoberFlag=true;
						    	}
						    	$scope.MonthTotal.octoberTotalEarning = obj.totalEarning;
						    }
						    else{
						    	$scope.monthValue=10;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.octoberFlag=true;
						    	}
						    }
						    if(obj.month == "november"){
						    	$scope.monthValue=11;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.novemberFlag=true;
						    	}
						    	
						    	$scope.MonthTotal.novemberTotalEarning = obj.totalEarning;
						    }
						    else{
						    	$scope.monthValue=11;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.novemberFlag=true;
						    	}
						    }
						    if(obj.month == "december"){
						    	
						    	$scope.monthValue=12;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.decemberFlag=true;
						    	}
						    	$scope.MonthTotal.decemberTotalEarning = obj.totalEarning;
						    }
						    else{
						    	$scope.monthValue=12;
						    	if($scope.monthValue < n){
						    		$scope.saleMonthTotal.decemberFlag=true;
						    	}
						    }
						    
					   });
				   });
			   }
			  
		   }
		   
		   $scope.janOpen = 0;
		   $scope.julyOpen = 0;
		   $scope.februaryOpen = 0;
		   $scope.augustOpen = 0;
		   $scope.marchOpen = 0;
		   $scope.septemberOpen = 0;
		   $scope.aprilOpen = 0;
		   $scope.octoberOpen = 0;
		   $scope.septemberOpen = 0;
		   $scope.novemberOpen = 0;
		   $scope.juneOpen = 0;
		   $scope.decemberOpen = 0;
		   
		   $scope.janrOpen = function(){
			   $scope.showOtherIngo = 0;
			   $scope.leadsTime = {};
			   $scope.saleleadsTime = {};
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "january"){
				    	$scope.leadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "january"){
				    	$scope.saleleadsTime = obj;
				    }
			   });
			   angular.forEach($scope.salesPersonPerf, function(obj, index){
				   obj.isSelected = false;
			   });
			  
			   
			   $scope.salesList = [];
			   apiserviceDashborad.getSalePerson("january").then(function(data){
				  angular.forEach($scope.salesPersonPerf, function(obj, index){
					  angular.forEach(data, function(obj1, index1){
						  if(obj.id == obj1.user.id){
							  $scope.salesList.push(obj.id);
							  obj.isSelected = true;
						  }
					  });
				   });
			   });
			   if($scope.janOpen == 0){
				   $scope.janOpen = 1;   
			   }else{
				   $scope.janOpen = 0;
			   }
			   
			   $scope.julyOpen = 0;
			   $scope.februaryOpen = 0;
			   $scope.decemberOpen = 0;
			   $scope.juneOpen = 0;
			   $scope.mayOpen = 0;
			   $scope.novemberOpen = 0;
			   $scope.octoberOpen = 0;
			   $scope.aprilOpen = 0;
			   $scope.septemberOpen = 0;
			   $scope.marchOpen = 0;
			   $scope.augustOpen = 0;
		   }
		   
		   $scope.janClose = function(){
			   $scope.janOpen = 0;
		   }
		   
		   $scope.julysOpen = function(){
			   $scope.showOtherIngo = 0;
			   $scope.leadsTime = {};
			   $scope.saleleadsTime = {};
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "july"){
				    	$scope.leadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "july"){
				    	$scope.saleleadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.salesPersonPerf, function(obj, index){
				   obj.isSelected = false;
			   });
			   
			   $scope.salesList = [];
			   apiserviceDashborad.getSalePerson("july").then(function(data){
				  angular.forEach($scope.salesPersonPerf, function(obj, index){
					  angular.forEach(data, function(obj1, index1){
						  if(obj.id == obj1.user.id){
							  $scope.salesList.push(obj.id);
							  obj.isSelected = true;
						  }
					  });
				   });
			   });
			   
			   if($scope.julyOpen == 0){
				   $scope.julyOpen = 1;   
			   }else{
				   $scope.julyOpen = 0;
			   }
			   
			   $scope.janOpen = 0;
			   $scope.februaryOpen = 0;
			   $scope.decemberOpen = 0;
			   $scope.juneOpen = 0;
			   $scope.mayOpen = 0;
			   $scope.novemberOpen = 0;
			   $scope.octoberOpen = 0;
			   $scope.aprilOpen = 0;
			   $scope.septemberOpen = 0;
			   $scope.marchOpen = 0;
			   $scope.augustOpen = 0;
		   }
		   
		   
		   $scope.julyClose = function(){
			   $scope.julyOpen = 0;
		   }
		   $scope.februarysOpen = function(){
			   $scope.showOtherIngo = 0;
			   $scope.leadsTime = {};
			   $scope.saleleadsTime = {};
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "february"){
				    	$scope.leadsTime = obj;
				    }
			   });
			   
			   
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "february"){
				    	$scope.saleleadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.salesPersonPerf, function(obj, index){
				   obj.isSelected = false;
			   });
			   
			   
			   $scope.salesList = [];
			   apiserviceDashborad.getSalePerson("february").then(function(data){
					  angular.forEach($scope.salesPersonPerf, function(obj, index){
						  angular.forEach(data, function(obj1, index1){
							  if(obj.id == obj1.user.id){
								  $scope.salesList.push(obj.id);
								  obj.isSelected = true;
							  }
						  });
					   });
				   });
				   
			   if($scope.februaryOpen == 0){
				   $scope.februaryOpen = 1;   
			   }else{
				   $scope.februaryOpen = 0;
			   }
			   
			   $scope.janOpen = 0;
			   $scope.julyOpen = 0;
			   $scope.decemberOpen = 0;
			   $scope.juneOpen = 0;
			   $scope.mayOpen = 0;
			   $scope.novemberOpen = 0;
			   $scope.octoberOpen = 0;
			   $scope.aprilOpen = 0;
			   $scope.septemberOpen = 0;
			   $scope.marchOpen = 0;
			   $scope.augustOpen = 0;
		   }
		   
		   $scope.februaryClose = function(){
			   $scope.februaryOpen = 0;
		   }
		   
		   $scope.augustsOpen = function(){
			   $scope.showOtherIngo = 0;
			   $scope.leadsTime = {};
			   $scope.saleleadsTime = {};
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "august"){
				    	$scope.leadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "august"){
				    	$scope.saleleadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.salesPersonPerf, function(obj, index){
				   obj.isSelected = false;
			   });
			   
			   
			   $scope.salesList = [];
			   apiserviceDashborad.getSalePerson("august").then(function(data){
				  angular.forEach($scope.salesPersonPerf, function(obj, index){
					  angular.forEach(data, function(obj1, index1){
						  if(obj.id == obj1.user.id){
							  $scope.salesList.push(obj.id);
							  obj.isSelected = true;
						  }
					  });
				   });
			   });
			   
			   if($scope.augustOpen == 0){
				   $scope.augustOpen = 1;   
			   }else{
				   $scope.augustOpen = 0;
			   }
			   
			   $scope.janOpen = 0;
			   $scope.julyOpen = 0;
			   $scope.februaryOpen = 0;
			   $scope.decemberOpen = 0;
			   $scope.juneOpen = 0;
			   $scope.mayOpen = 0;
			   $scope.novemberOpen = 0;
			   $scope.octoberOpen = 0;
			   $scope.aprilOpen = 0;
			   $scope.septemberOpen = 0;
			   $scope.marchOpen = 0;
		   }
		   
		   $scope.augustClose = function(){
			   $scope.augustOpen = 0;
		   }
		   
		   
		   $scope.marchClose = function(){
			   $scope.marchOpen = 0;
		   }
		   
		   
		   $scope.marchsOpen = function(){
			   $scope.showOtherIngo = 0;
			   $scope.leadsTime = {};
			   $scope.saleleadsTime = {};
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "march"){
				    	$scope.leadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "march"){
				    	$scope.saleleadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.salesPersonPerf, function(obj, index){
				   obj.isSelected = false;
			   });
			   
			   
			   $scope.salesList = [];
			   apiserviceDashborad.getSalePerson("march").then(function(data){
				  angular.forEach($scope.salesPersonPerf, function(obj, index){
					  angular.forEach(data, function(obj1, index1){
						  if(obj.id == obj1.user.id){
							  $scope.salesList.push(obj.id);
							  obj.isSelected = true;
						  }
					  });
				   });
			   });
			   
			   if($scope.marchOpen == 0){
				   $scope.marchOpen = 1;   
			   }else{
				   $scope.marchOpen = 0;
			   }
			   
			   $scope.janOpen = 0;
			   $scope.julyOpen = 0;
			   $scope.februaryOpen = 0;
			   $scope.decemberOpen = 0;
			   $scope.juneOpen = 0;
			   $scope.mayOpen = 0;
			   $scope.novemberOpen = 0;
			   $scope.octoberOpen = 0;
			   $scope.aprilOpen = 0;
			   $scope.septemberOpen = 0;
			   $scope.augustOpen = 0;
		   }
		   
		   $scope.septemberClose = function(){
			   $scope.septemberOpen = 0; 
		   }
		   
		   $scope.septembersOpen = function(){
			   $scope.showOtherIngo = 0;
			   $scope.leadsTime = {};
			   $scope.saleleadsTime = {};
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "september"){
				    	$scope.leadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "september"){
				    	$scope.saleleadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.salesPersonPerf, function(obj, index){
				   obj.isSelected = false;
			   });
			   
			   $scope.salesList = [];
			   apiserviceDashborad.getSalePerson("september").then(function(data){
				  angular.forEach($scope.salesPersonPerf, function(obj, index){
					  angular.forEach(data, function(obj1, index1){
						  if(obj.id == obj1.user.id){
							  $scope.salesList.push(obj.id);
							  obj.isSelected = true;
						  }
					  });
				   });
			   });
			   
			   if($scope.septemberOpen == 0){
				   $scope.septemberOpen = 1;   
			   }else{
				   $scope.septemberOpen = 0;
			   }
			   
			   $scope.janOpen = 0;
			   $scope.julyOpen = 0;
			   $scope.februaryOpen = 0;
			   $scope.decemberOpen = 0;
			   $scope.juneOpen = 0;
			   $scope.mayOpen = 0;
			   $scope.novemberOpen = 0;
			   $scope.octoberOpen = 0;
			   $scope.aprilOpen = 0;
			   $scope.marchOpen = 0;
			   $scope.augustOpen = 0;
		   }
		   
		   $scope.aprilClose = function(){
			   $scope.aprilOpen = 0;
		   }
		   
		   $scope.aprilsOpen = function(){
			   $scope.showOtherIngo = 0;
			   $scope.leadsTime = {};
			   $scope.saleleadsTime = {};
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "april"){
				    	$scope.leadsTime = obj;
				    }
			   });
			   
			   
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "april"){
				    	$scope.saleleadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.salesPersonPerf, function(obj, index){
				   obj.isSelected = false;
			   });
			   
			   $scope.salesList = [];
			   apiserviceDashborad.getSalePerson("april").then(function(data){
				  angular.forEach($scope.salesPersonPerf, function(obj, index){
					  angular.forEach(data, function(obj1, index1){
						  if(obj.id == obj1.user.id){
							  $scope.salesList.push(obj.id);
							  obj.isSelected = true;
						  }
					  });
				   });
			   });
			   
			   if($scope.aprilOpen == 0){
				   $scope.aprilOpen = 1;   
			   }else{
				   $scope.aprilOpen = 0;
			   }
			   
			   $scope.janOpen = 0;
			   $scope.julyOpen = 0;
			   $scope.februaryOpen = 0;
			   $scope.decemberOpen = 0;
			   $scope.juneOpen = 0;
			   $scope.mayOpen = 0;
			   $scope.novemberOpen = 0;
			   $scope.octoberOpen = 0;
			   $scope.septemberOpen = 0;
			   $scope.marchOpen = 0;
			   $scope.augustOpen = 0;
		   }
		   
		   
		   $scope.octoberClose = function(){
			   $scope.octoberOpen = 0;
		   }
		   
		   
		   $scope.octobersOpen = function(){
			   $scope.showOtherIngo = 0;
			   $scope.leadsTime = {};
			   $scope.saleleadsTime = {};
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "october"){
				    	$scope.leadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "october"){
				    	$scope.saleleadsTime = obj;
				    }
			   });
			   
			   
			   angular.forEach($scope.salesPersonPerf, function(obj, index){
				   obj.isSelected = false;
			   });
			   
			   $scope.salesList = [];
			   apiserviceDashborad.getSalePerson("october").then(function(data){
				  angular.forEach($scope.salesPersonPerf, function(obj, index){
					  angular.forEach(data, function(obj1, index1){
						  if(obj.id == obj1.user.id){
							  $scope.salesList.push(obj.id);
							  obj.isSelected = true;
						  }
					  });
				   });
			   });
			   
			   if($scope.octoberOpen == 0){
				   $scope.octoberOpen = 1;   
			   }else{
				   $scope.octoberOpen = 0;
			   }
			   
			   $scope.janOpen = 0;
			   $scope.julyOpen = 0;
			   $scope.februaryOpen = 0;
			   $scope.decemberOpen = 0;
			   $scope.juneOpen = 0;
			   $scope.mayOpen = 0;
			   $scope.novemberOpen = 0;
			   $scope.aprilOpen = 0;
			   $scope.septemberOpen = 0;
			   $scope.marchOpen = 0;
			   $scope.augustOpen = 0;
		   }
		   
		   $scope.mayClose = function(){
			   $scope.mayOpen = 0;
		   }
		   
		   $scope.maysOpen = function(){
			   $scope.showOtherIngo = 0;
			   $scope.leadsTime = {};
			   $scope.saleleadsTime = {};
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "may"){
				    	$scope.leadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "may"){
				    	$scope.saleleadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.salesPersonPerf, function(obj, index){
				   obj.isSelected = false;
			   });
			   
			   $scope.salesList = [];
			   apiserviceDashborad.getSalePerson("may").then(function(data){
				  angular.forEach($scope.salesPersonPerf, function(obj, index){
					  angular.forEach(data, function(obj1, index1){
						  if(obj.id == obj1.user.id){
							  $scope.salesList.push(obj.id);
							  obj.isSelected = true;
						  }
					  });
				   });
			   });
			   
			   if($scope.mayOpen == 0){
				   $scope.mayOpen = 1;   
			   }else{
				   $scope.mayOpen = 0;
			   }
			   
			   $scope.septemberOpen = 0;
			   $scope.janOpen = 0;
			   $scope.julyOpen = 0;
			   $scope.februaryOpen = 0;
			   $scope.decemberOpen = 0;
			   $scope.juneOpen = 0;
			   $scope.novemberOpen = 0;
			   $scope.octoberOpen = 0;
			   $scope.aprilOpen = 0;
			   $scope.marchOpen = 0;
			   $scope.augustOpen = 0;
		   }
		   
		   $scope.novemberClose = function(){
			   $scope.novemberOpen = 0;
		   }
		   
		   $scope.novembersOpen = function(){
			   $scope.showOtherIngo = 0;
			   $scope.leadsTime = {};
			   $scope.saleleadsTime = {};
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "november"){
				    	$scope.leadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "november"){
				    	$scope.saleleadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.salesPersonPerf, function(obj, index){
				   obj.isSelected = false;
			   });
			   
			   $scope.salesList = [];
			   apiserviceDashborad.getSalePerson("november").then(function(data){
				  angular.forEach($scope.salesPersonPerf, function(obj, index){
					  angular.forEach(data, function(obj1, index1){
						  if(obj.id == obj1.user.id){
							  $scope.salesList.push(obj.id);
							  obj.isSelected = true;
						  }
					  });
				   });
			   });
			   
			   if($scope.novemberOpen == 0){
				   $scope.novemberOpen = 1;   
			   }else{
				   $scope.novemberOpen = 0;
			   }
			   
			   $scope.janOpen = 0;
			   $scope.julyOpen = 0;
			   $scope.februaryOpen = 0;
			   $scope.decemberOpen = 0;
			   $scope.juneOpen = 0;
			   $scope.mayOpen = 0;
			   $scope.octoberOpen = 0;
			   $scope.aprilOpen = 0;
			   $scope.septemberOpen = 0;
			   $scope.marchOpen = 0;
			   $scope.augustOpen = 0;
		   }
		   
		   $scope.juneClose = function(){
			   $scope.juneOpen = 0;
		   }
		   
		   
		   $scope.junesOpen = function(){
			   $scope.showOtherIngo = 0;
			   $scope.leadsTime = {};
			   $scope.saleleadsTime = {};
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "june"){
				    	$scope.leadsTime = obj;
				    }
			   });
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "june"){
				    	$scope.saleleadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.salesPersonPerf, function(obj, index){
				   obj.isSelected = false;
			   });
			   
			   $scope.salesList = [];
			   apiserviceDashborad.getSalePerson("june").then(function(data){
				  angular.forEach($scope.salesPersonPerf, function(obj, index){
					  angular.forEach(data, function(obj1, index1){
						  if(obj.id == obj1.user.id){
							  $scope.salesList.push(obj.id);
							  obj.isSelected = true;
						  }
					  });
				   });
			   });
			   
			   if($scope.juneOpen == 0){
				   $scope.juneOpen = 1;   
			   }else{
				   $scope.juneOpen = 0;
			   }
			   
			   $scope.janOpen = 0;
			   $scope.julyOpen = 0;
			   $scope.februaryOpen = 0;
			   $scope.decemberOpen = 0;
			   $scope.mayOpen = 0;
			   $scope.novemberOpen = 0;
			   $scope.octoberOpen = 0;
			   $scope.aprilOpen = 0;
			   $scope.septemberOpen = 0;
			   $scope.marchOpen = 0;
			   $scope.augustOpen = 0;
		   }
		   
		   
		   $scope.decemberClose = function(){
			   $scope.decemberOpen = 0;
		   }
		   
		   
		   $scope.decembersOpen = function(){
			   $scope.showOtherIngo = 0;
			   $scope.leadsTime = {};
			   $scope.saleleadsTime = {};
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "december"){
				    	$scope.leadsTime = obj;
				    }
			   });
			   
			   angular.forEach($scope.totalLocationPlanData, function(obj, index){
				    if(obj.month == "december"){
				    	$scope.saleleadsTime = obj;
				    }
			   });
			   
			   $scope.salesList = [];
			   angular.forEach($scope.salesPersonPerf, function(obj, index){
				   obj.isSelected = false;
			   });
			   
			   apiserviceDashborad.getSalePerson("december").then(function(data){
				  angular.forEach($scope.salesPersonPerf, function(obj, index){
					  angular.forEach(data, function(obj1, index1){
						  if(obj.id == obj1.user.id){
							  $scope.salesList.push(obj.id);
							  obj.isSelected = true;
						  }
					  });
				   });
			   });
			   
			   if($scope.decemberOpen == 0){
				   $scope.decemberOpen = 1;   
			   }else{
				   $scope.decemberOpen = 0;
			   }
			   
			   $scope.janOpen = 0;
			   $scope.julyOpen = 0;
			   $scope.februaryOpen = 0;
			   $scope.juneOpen = 0;
			   $scope.mayOpen = 0;
			   $scope.novemberOpen = 0;
			   $scope.octoberOpen = 0;
			   $scope.aprilOpen = 0;
			   $scope.septemberOpen = 0;
			   $scope.marchOpen = 0;
			   $scope.augustOpen = 0;
		   }
		   
		   $scope.showOtherIngo = 0;
		   $scope.showOtherIngos = function(){
			   if($scope.showOtherIngo == 0){
				   $scope.showOtherIngo = 1;
			   }else{
				   $scope.showOtherIngo = 0;
			   }
		   }
		   
		   $scope.nextbutton = 0;
		   $scope.goNext = function(){
			   $scope.planIs = "save";
			   $scope.nextbutton = 1;
		   }
		   
		   apiserviceDashborad.getlocations().then(function(data){
			   $scope.locationdata = data;
			   angular.forEach($scope.locationdata, function(obj, index){
				   obj.isSelected = false;
			   });
		   });
		   $scope.locationList = [];
		   $scope.locationClicked = function(e, locationPer,value){
					   if(value == false){
							$scope.locationList.push(locationPer.id);
						}else{
							$scope.deleteItem(locationPer);
						}
			}
			$scope.deleteItem = function(locationPer){
				angular.forEach($scope.locationList, function(obj, index){
					 if ((locationPer.id == obj)) {
						 $scope.locationList.splice(index, 1);
				       	return;
				    };
				  });
			}
		   
			   $scope.salesClicked = function(e, salesPer,value){
					if(value == false){
						$scope.salesList.push(salesPer.id);
					}else{
						$scope.deleteSalesItem(salesPer);
					}
				}
				$scope.deleteSalesItem = function(salesPer){
					angular.forEach($scope.salesList, function(obj, index){
						 if ((salesPer.id == obj)) {
							 $scope.salesList.splice(index, 1);
					       	return;
					    };
					  });
				}
			
		$scope.schPlan = {};
		$scope.submitnewPlan = function(){
			$scope.schPlan.locationList = $scope.locationList;
			$scope.schPlan.salesList = $scope.salesList;
			   $scope.schPlan.startDate = $('#cnfstartdate').val();
			   $scope.schPlan.endDate = $('#cnfenddate').val();
			   if($scope.schPlan.scheduleBy != "salePerson"){
				   $scope.schPlan.scheduleBy = "location"; 
			   }
			  
			if($scope.planIs == "save"){
				
				apiserviceDashborad.savePlan($scope.schPlan).then(function(data){
					if(data == 1){
						$.pnotify({
							  title: "Error",
							    type:'success',
						    text: "Plan already exists",
						});
					}else{
						$('#plan-model').modal("toggle");
						   $scope.nextbutton = 0;
						   $.pnotify({
							    title: "Success",
							    type:'success',
							    text: "Plan Scheduled",
							});
					}
					   
				   }); 
			}else if($scope.planIs == "update"){
				apiserviceDashborad.updatePlan($scope.schPlan).then(function(data){
					if(data == 1){
						$.pnotify({
							  title: "Error",
							    type:'success',
						    text: "Plan already exists",
						});
					}else{
						$('#plan-model').modal("toggle");
						   $scope.nextbutton = 0;
						   $.pnotify({
							    title: "Success",
							    type:'success',
							    text: "Plan Scheduled",
							});
					}
				   }); 
			}
			
		}	
			
		   $scope.checkManagerLogin = function(){
			   if(angular.equals($scope.userType,"Manager") || angular.equals($scope.userType,"Sales Person")){
				   apiserviceDashborad.getloginuserinfo().then(function(data){
					  //alert(JSON.stringify(data));
					  $scope.schmeeting.location = data.location.id;
					  $scope.locationValueForPlan=data.location.id;
					  apiserviceDashborad.getuser($scope.schmeeting.location).then(function(data){
						   $scope.user = data;
					   });
				   });
			   }
			
		   }
		   $scope.inviteStaff = function(txt){
			   if(txt==false){
				   $('#inUser').attr("disabled", true);
				   $scope.schmeeting.allStaff = true;
			   }else if(txt==true){
				   $('#inUser').attr("disabled", false);
				   $scope.schmeeting.allStaff = false;
			   }
		   };
		   $scope.checkDateValid = function(){
			   var startD = $('#cnfstartdate').val();
			   if($scope.schPlan.scheduleBy != "salePerson"){
				   $scope.schPlan.scheduleBy = "location"; 
			   }
			   if($scope.schPlan.location == undefined){
				   $scope.schPlan.location = 0;
			   }
			   
			   if($scope.schPlan.scheduleBy == "location"){
				   apiserviceDashborad.isValidDatecheck($scope.schPlan.location, startD, $scope.schPlan.scheduleBy).then(function(data){
					   if(data == 1){
						   if($scope.planIs != "update"){
							   $('#cnfstartdate').val("");
						   } 
						   $.pnotify({
							    title: "Success",
							    type:'success',
							    text: "Plan already exists",
							});
					   }
				   });
				   
				   
			   }
			   if($scope.schPlan.scheduleBy == "salePerson"){
				   apiserviceDashborad.isValidDatecheck($scope.schPlan.salePerson, startD, $scope.schPlan.scheduleBy).then(function(data){
						   if(data == 1){
							   if($scope.planIs != "update"){
								   $('#cnfstartdate').val("");
							   } 
							   $.pnotify({
								    title: "Success",
								    type:'success',
								    text: "Plan already exists",
								});
						   }
					   });
			   }
		   }
		   
		   $scope.checkDateValid1 = function(){
			   var endD = $('#cnfenddate').val();
			   if($scope.schPlan.scheduleBy != "salePerson"){
				   $scope.schPlan.scheduleBy = "location"; 
			   }
			   if($scope.schPlan.scheduleBy == "location"){
				   apiserviceDashborad.isValidDatecheck($scope.schPlan.location, endD, $scope.schPlan.scheduleBy).then(function(data){
					   if(data == 1){
						   if($scope.planIs != "update"){
							   $('#cnfstartdate').val("");
						   } 
						   $.pnotify({
							    title: "Success",
							    type:'success',
							    text: "Plan already exists",
							});
					   }
				   });
			   }
			   if($scope.schPlan.scheduleBy == "salePerson"){
				   apiserviceDashborad.isValidDatecheck($scope.schPlan.salePerson, endD, $scope.schPlan.scheduleBy).then(function(data){
						   if(data == 1){
							   if($scope.planIs != "update"){
								   $('#cnfstartdate').val("");
							   } 
							   $.pnotify({
								    title: "Success",
								    type:'success',
								    text: "Plan already exists",
								});
						   }
					   });
			   }
		   }
		   
		   $scope.allloction = false;
		   $scope.allloctionSale = false;
		   $scope.checkLocation = function(checkAll){
			   
			   var startD = $('#cnfstartdate').val();
			   var endD = $('#cnfenddate').val();
			   $scope.locationList = [];
			   if(checkAll == false){
				   angular.forEach($scope.locationdata, function(obj, index){
					   apiserviceDashborad.isValidCheckbok(obj.id, startD, endD).then(function(data){
						   if(data == 0){
							   obj.isSelected = true;
							   $scope.locationList.push(obj.id);
						   }
					   });
					   
				   });
			   }else{
				   angular.forEach($scope.locationdata, function(obj, index){
					   obj.isSelected = false;
				   });
			   }
		   }
		   $scope.salesList = [];
		   $scope.checkSale = function(checkAll){
			   $scope.salesList = [];
			   if(checkAll == false){
				   angular.forEach($scope.salesPersonPerf, function(obj, index){
					   obj.isSelected = true;
					   $scope.salesList.push(obj.id);
				   });
			   }else{
				   angular.forEach($scope.salesPersonPerf, function(obj, index){
					   obj.isSelected = false;
				   });
			   }
		   }
		   
		   $scope.showuser = function(location){
			   apiserviceDashborad.getmanager(location).then(function(data){
				   $scope.user = data;
			   });
		   };
		   
		   $scope.submitnewmeeting = function(){
			   if($scope.checked.length > 0){
				   $scope.schmeeting.usersList = $scope.checked;
				   $scope.schmeeting.bestDay = $('#cnfmeetingdate').val();
				   $scope.schmeeting.bestTime = $('#cnfmeetingtime').val();
				   $scope.schmeeting.bestEndTime = $('#cnfmeetingtimeEnd').val();
				   apiserviceDashborad.savemeeting($scope.schmeeting).then(function(data){
					   $('#meeting-model').modal("toggle");
					   apiserviceDashborad.getscheduletest().then(function(data){
						   $scope.scheduleListData = data;
					   });
					   $scope.schedulmultidatepicker();
					   
				   });
			   }else{
				   $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Please Select user",
					});
			   }
		   };
		   
		   $scope.updateScheduleTest = function(){
			   $scope.data1.confDate = $('#cnfReSchDate').val();
			   $scope.data1.confTime = $('#timeSchPick').val();
			   $scope.data1.confirmEndTime = $('#timeSchPickEnd').val();
			   
			  $scope.data1.usersList = $scope.checked;
			  $scope.data1.isRead=0;
			  apiserviceDashborad.updateScheduleTest($scope.data1).then(function(data){
				   $('#colored-header').modal("toggle");
				   
				   $scope.data1.confirmDate = $('#cnfReSchDate').val();
				   $scope.data1.confirmTime = $('#timeSchPick').val();
				   $scope.data1.confirmEndTime = $('#timeSchPickEnd').val();
				   
				   apiserviceDashborad.getscheduletest().then(function(data){
					   $scope.scheduleListData = data;
				   });
				   
				  
				   $scope.schedulmultidatepicker();
			   }); 
			   
		   }
		   $scope.leadList = [];
		   apiserviceDashborad.getSelectedLeadType().then(function(response){
				
				angular.forEach(response, function(value, key) {
					//if(value.id > 3){
						$scope.leadList.push(value); 
					//}
				});
			
			});
		   
		   
 $scope.AllOtherLeadSeenList = [];
		   
		   $scope.otherLeadId = function(leads){
			   
			   $scope.AllOtherLeadSeenList = [];
			   console.log("change");
			   console.log($scope.otherLead);
			   $scope.otherLeads = true;
			   $scope.schedTest = false;
	    		$scope.reqMore = false;	
	        	$scope.testdrv = false;
	        	$scope.allLeadd = false;
	        	$scope.trdin = false;
	        	$scope.showLeadsV = false;
	        	$scope.cancelleads = false;
	        	$scope.contact = false;
	        	
	        	angular.forEach($scope.otherLead,function(value,key){
	        		console.log(value.isContactusType);
	        		console.log(leads);
	        		if(parseInt(value.isContactusType)== leads){
	    				$scope.AllOtherLeadSeenList.push(value);
	        		}
	        	});
	        	
	        	
	        	
	        	$scope.gridMapObect = [];
				var findFlag = 0;
				angular.forEach($scope.AllOtherLeadSeenList,function(value,key){
					if(findFlag == 0){
						angular.forEach(value.customData,function(value1,key1){
							$scope.gridMapObect.push({values: value1.value , key: value1.key});
							findFlag = 1;
						});
					}
				});
				angular.forEach($scope.AllOtherLeadSeenList,function(value,key){
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
				
				$scope.gridOptions13.data = $scope.AllOtherLeadSeenList;
				
				angular.forEach($scope.gridMapObect,function(value,key){
					var name = value.key;
					name = name.replace(" ","");
					console.log(name);
					$scope.gridOptions13.columnDefs.push({ name: name, displayName: name, width:'10%',cellEditableCondition: false,
		              	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		              		if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
		                        return 'red';
		                    }
		              	} ,
		               });
				});
				
				$scope.gridOptions13.columnDefs.push({ name: 'btnSold', displayName: '',enableFiltering: false, width:'40%',cellEditableCondition: false,
                 	cellTemplate:'<button type="button" ng-click="grid.appScope.completeRequestStatus(row.entity)" class="btn btn-sm btn-primary "  ng-show="grid.appScope.userType != \'\'" style="margin-left:3%;">SOLD</button><button type="button" ng-click="grid.appScope.cancelRequestStatus(row.entity)" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">CANCEL</button><button type="button" ng-click="grid.appScope.addNoteToRequestUser(row.entity,\'requestMore\')" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">HISTORY</button><button type="button" ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,1)" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">SCHEDULE</button><button type="button" ng-click="grid.appScope.createContact(row.entity)" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">ADD TO CLIENTELE</button><button ng-show="grid.appScope.userType == \'Manager\'" type="button" ng-click="grid.appScope.assignCanceledLead(row.entity)" class="btn btn-sm btn-primary" style="margin-left:0%;">ASSIGN</button>',
                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                         if (row.entity.noteFlag != 1) {
                           return 'red';
                       }
                 	} ,
                 });
	        	
	        	 
	        	console.log($scope.gridOptions13.data);
	    	
		   }
		   
  }]);


angular.module('newApp')
.controller('PhotoUploadCtrl', ['$scope','$routeParams','$location', function ($scope,$routeParams,$location) {
   var myDropzone = new Dropzone("#dropzoneFrm",{
	   parallelUploads: 30,
	   headers: { "vinNum": $routeParams.num },
	   acceptedFiles:"image/*",
	   addRemoveLinks:true,
	   autoProcessQueue:false,
	   init:function () {
		   this.on("queuecomplete", function (file) {
		          
		          $location.path('/managePhotos/'+$routeParams.num);
		          $scope.$apply();
		      });
		   this.on("complete", function() {
			   this.removeAllFiles();
		   });
	   }
   });
   $scope.uploadFiles = function() {
	   Dropzone.autoDiscover = false;
	   myDropzone.processQueue();
	   
   }
   
}]);

angular.module('newApp')
.controller('ManagePhotoCtrl', ['$scope','$routeParams','$location','$http','$timeout', function ($scope,$routeParams,$location,$http,$timeout) {
	$scope.gridsterOpts = {
		    columns: 6, // the width of the grid, in columns
		    pushing: true, // whether to push other items out of the way on move or resize
		    floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
		    swapping: true, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
		     width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
		     colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
		    rowHeight: 'match', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
		    margins: [10, 10], // the pixel distance between each widget
		    outerMargin: true, // whether margins apply to outer edges of the grid
		    isMobile: false, // stacks the grid items if true
		    mobileBreakPoint: 600, // if the screen is not wider that this, remove the grid layout and stack the items
		    mobileModeEnabled: true, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
		    minColumns: 6, // the minimum columns the grid must have
		    minRows: 1, // the minimum height of the grid, in rows
		    maxRows: 100,
		    defaultSizeX: 1, // the default width of a gridster item, if not specifed
		    defaultSizeY: 1, // the default height of a gridster item, if not specified
		    resizable: {
			       enabled: false,
			       handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
			       start: function(event, $element, widget) {}, // optional callback fired when resize is started,
			       resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
			       stop: function(event, $element, widget) {} // optional callback fired when item is finished resizing
			    },
		    /* minSizeX: 1, // minimum column width of an item
		    maxSizeX: null, // maximum column width of an item
		    minSizeY: 1, // minumum row height of an item
		    maxSizeY: null, // maximum row height of an item
		   */
			    draggable: {
				       enabled: true, // whether dragging items is supported
				       handle: '.my-class', // optional selector for resize handle
				       start: function(event, $element, widget) {}, // optional callback fired when drag is started,
				       drag: function(event, $element, widget) {}, // optional callback fired when item is moved,
				       stop: function(event, $element, widget) {
				    	   if($(event.target).html() == 'Set Default' || $(event.target).html() == 'Edit' || $(event.target)[0].className == 'glyphicon glyphicon-zoom-in' || $(event.target)[0].className == 'btn fa fa-times') {
				    		   return;
				    	   };
				    	   for(var i=0;i<$scope.imageList.length;i++) {
				    		   delete $scope.imageList[i].description;
				    		   delete $scope.imageList[i].width;
				    		   delete $scope.imageList[i].height;
				    		   delete $scope.imageList[i].link;
				    	   } 
				    	   $http.post('/savePosition',$scope.imageList)
					   		.success(function(data) {
					   			$.pnotify({
								    title: "Success",
								    type:'success',
								    text: "Position saved successfully",
								});
					   		});
				    	   
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	$scope.imageList = [];
	$scope.init = function() {
		 $timeout(function(){
			apiserviceDashborad.getImagesByVin($routeParams.num).then(function(data){
				$scope.imageList = data;
			});
		 }, 3000);
	}
   
	$scope.$on('ngRepeatFinished', function(ngRepeatFinishedEvent) {
		for(var i=0;i<$scope.imageList.length;i++) {
			if($scope.imageList[i].defaultImage == true) {
				$('#imgId'+i).css("border","3px solid");
				$('#imgId'+i).css("color","red");
			}
		}
	});
	
	$scope.setAsDefault = function(image,index) {
		
		for(var i=0;i<$scope.imageList.length;i++) {
			if($scope.imageList[i].defaultImage == true) {
				apiserviceDashborad.removeDefault($scope.imageList[i].id, image.id).then(function(data){
				});
				$('#imgId'+i).removeAttr("style","");
				$scope.imageList[i].defaultImage = false;
				image.defaultImage = true;
				$('#imgId'+index).css("border","3px solid");
				$('#imgId'+index).css("color","red");
				break;
			}
		}
		
		if(i == $scope.imageList.length) {
			
			apiserviceDashborad.setDefaultImage(image.id).then(function(data){
			});
			
			image.defaultImage = true;
			$('#imgId'+index).css("border","3px solid");
			$('#imgId'+index).css("color","red");
		}
		
		
	}
	
	$scope.deleteImage = function(img) {
		
		apiserviceDashborad.deleteImage(img.id).then(function(data){
			$scope.imageList.splice($scope.imageList.indexOf(img),1);
		});
		
	}
	
	$scope.showFullImage = function(image) {
		$scope.imageId = image.id;
		$scope.imageName = image.imgName;
	}
	
	$scope.editImage = function(image) {
		$location.path('/cropImage/'+image.id);
	}
	
}]);

