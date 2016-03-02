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
  .controller('dashboardLocationCtrl', ['$scope','$routeParams', 'dashboardService', 'pluginsService', '$http','$compile','$interval','$filter','$location','$timeout','$route','$window', function ($scope, $routeParams,dashboardService, pluginsService,$http,$compile,$interval,$filter,$location,$timeout,$route,$window) {
	$scope.showSelectLocationDash = $routeParams.LocationId;
	$scope.locationValue = $routeParams.LocationId;
	$scope.userKey = $routeParams.managerId;
	$scope.gmIsManager = $routeParams.gmIsManager;
	$scope.userRole = "Manager";
	$scope.userType = "Manager";
	
	$http.get('/getfindGmIsManager')
	.success(function(data) {
		if(data != null){
			$scope.locationValue = data;
		}
		
	});
	
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
	
	$http.get('/getAllVehicles')
		.success(function(data) {
			$scope.vinSearchList = data;
		});
		//$scope.stockRp = {};
	$scope.openLocationDasboard = function(item){
		$scope.showSelectLocationDash = item.id;
		if($routeParams.gmIsManager == 1){
			$http.get('/changePermission/'+$routeParams.LocationId+"/"+$routeParams.managerId+"/"+"0")
			.success(function(data) {
				console.log($location.absUrl());
				 window.location.reload();
			});
		}else{
			$http.get('/changePermission/'+item.id+"/"+$routeParams.managerId+"/"+item.gmIsManager)
			.success(function(data) {
				console.log($location.absUrl());
				 window.location.reload();
			});
		}
		
		$location.path('/dashboardLocation/'+item.id+"/"+item.managerId+"/"+item.gmIsManager);
	  }
	
	$scope.backTodashBord = function(){
		
		if($routeParams.gmIsManager == 1){
			$http.get('/changePermission/'+$routeParams.LocationId+"/"+$routeParams.managerId+"/"+"0")
			.success(function(data) {
				 $window.location.href = '/authenticate';
			});
		}else{
			$window.location.href = '/authenticate';
		}
		
	}
	
	$scope.selectedVinValue = function(selectObj){
		$scope.item = selectObj.originalObject;
		$scope.editLeads.parentChildLead[$scope.editLeads.parentChildLead.length-1].model = $scope.item.model;
		$scope.editLeads.parentChildLead[$scope.editLeads.parentChildLead.length-1].make = $scope.item.make;
		$scope.editLeads.parentChildLead[$scope.editLeads.parentChildLead.length-1].stockNumber = $scope.item.stock;
		
		
	}
	$scope.selectedVin = function (selectObj) {
		if(selectObj.originalObject != undefined){
			$scope.item = selectObj.originalObject;
			console.log($scope.item);
			console.log();
			if($scope.editLeads.parentChildLead.length == 0){
				
			}
			
			$scope.editLeads.parentChildLead[$scope.editLeads.parentChildLead.length-1].model = $scope.item.model;
			$scope.editLeads.parentChildLead[$scope.editLeads.parentChildLead.length-1].make = $scope.item.make;
			$scope.editLeads.parentChildLead[$scope.editLeads.parentChildLead.length-1].stockNumber = $scope.item.stock;
			
		}
	};
	/*$http.get('/getUserType')
	  .success(function(data) {
		  
		  $scope.userLocationData
	 	$scope.userType = data;
	 	if($scope.userType == "Manager") {
	 		$scope.getGMData();
	 		$scope.getAllSalesPersonRecord($scope.userKey);
	 	}
	 	if($scope.userType == "Sales Person") {
	 		$scope.getToDoNotification();
	 		$scope.getAssignedLeads();
	 		$scope.getAllSalesPersonRecord($scope.userKey);
	 	}
	 	
	 	if($scope.userType == "General Manager"){
	 		$scope.topLocations('Week');
	 	}
	});*/
	
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
			$http.get('/updateUserComment/'+$scope.userId+"/"+$scope.userComment)
			.success(function(data) {
			});
		}
		$scope.userId = null;
		$scope.userComment = null;
	};
	
	$http.get('/getUserPermission').success(function(data){
		$scope.userPer = data;
	 });	
	$http.get('/getDataFromCrm').success(function(data){
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
		$http.get('/updateVehiclePrice/'+vin+"/"+price)
		.success(function(data) {
		});
	};
	$scope.setName = function(name,vin){
		$scope.nameLbl = 'true';
		$scope.nameTxt = 'false';
		$http.get('/updateVehicleName/'+vin+"/"+name)
		.success(function(data) {
		});
	};
	
		$http.get('/getUserRole').success(function(data) {
			$scope.userLocationData('Week','location');
			$scope.topLocations('Week');
			$scope.showVehicalBarChart();
			/*$scope.userRole = data.role;
			
			$scope.getSalesDataValue($scope.locationValue);
			if($scope.userRole != "General Manager"){
				$scope.locationValue = data.location.id;
				
				
			}
			
			if($scope.userRole == null){
				  $location.path('/myprofile');
			}*/
		});
		
		
	
	
	$scope.topLocations = function(timeSet){
		$http.get('/getAllLocation/'+timeSet)
		.success(function(data) {
			
		$scope.locationDataListShow = data;	
		angular.forEach($scope.locationDataListShow, function(value, key) {
			if(value.successRate !=null){
				value.successRate = value.successRate.toFixed(2);
			}else{
				value.successRate = 0;
			}
			
			if(value.PlanPer !=null){
				value.PlanPer = value.PlanPer.toFixed(2);
			}else{
				value.PlanPer = 0;
			}
			
			if(value.avgSaleLocation !=null){
				value.avgSaleLocation = value.avgSaleLocation.toFixed(2);
			}else{
				value.avgSaleLocation = 0;
			}
		});
	});
	}
	
	
	
	$scope.findMystatisData = function(startD,endD,locOrPer){
		$http.get('/getUserLocationByDateInfoOther/'+startD+'/'+endD+'/'+locOrPer+"/"+$routeParams.LocationId+"/"+$scope.userKey)
		//$http.get('/getUserLocationByDateInfo/'+startD+"/"+endD)
		.success(function(data) {
			$scope.parLocationData = data;
			$scope.leadsTime.leads = data.leads;
			$scope.leadsTime.goalSetTime = data.goalTime;
			$scope.showLeads = data.leads;
			$scope.stackchart = data.sendData;
			$scope.callChart($scope.stackchart);
		});
	 }
	$scope.dataLocOrPerWise = "person";
	$scope.showLeads = null;
	$scope.userLocationData = function(timeSet,locOrPer){
			$http.get('/getUserLocationInfoOther/'+timeSet+"/"+locOrPer+"/"+$routeParams.LocationId+"/"+$scope.userKey)
			.success(function(data) {
				$scope.parLocationData = data;
				$scope.leadsTime.leads = data.leads;
				$scope.leadsTime.goalSetTime = data.goalTime;
				$scope.showLeads = data.leads;
				$scope.stackchart = data.sendData;
				$scope.callChart($scope.stackchart);
			});
	}
	
	$scope.locationOrPersonData = function(wiseData){
		
		$scope.dataLocOrPerWise = wiseData;
		$scope.userLocationData('Week',$scope.dataLocOrPerWise);
	}
	
	setInterval(function(){
		
		  var startD = $('#cnfstartDateValue').val();
		   var endD = $('#cnfendDateValue').val();
		   if(startD != "" && startD != null && startD != undefined && endD != "" && endD != null && endD != undefined){
			   $scope.findMystatisData(startD,endD,$scope.dataLocOrPerWise);
			   $scope.dataLocOrPerWise = $scope.dataLocOrPerWise;
		   }else{
			   $scope.userLocationData('Week','person');
			   $scope.dataLocOrPerWise = "person";
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
		$http.post("/saveLeads",$scope.leadsTime).success(function(data){
			 $('#Locationwise-model').modal("toggle");
			 $scope.userLocationData('Week','person');
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
		            title: {
		                text: ''
		            }
		        },
		        tooltip: {
		            pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>${point.y}</b><br/>',
		            shared: true
		        },
		        plotOptions: {
		            column: {
		                stacking: ''
		            }
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
		//$routeParams.LocationId = locationId.id;
		$scope.getPerformanceOfUser(0);
		$scope.getSalesDataValue($routeParams.LocationId);
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
        
    	  $http.get('/getVisitorList/'+30)
  		.success(function(data) {
  			$scope.gridOptions.data = data[0].dates[0].items;
  			$scope.visitiorList = data[0].dates[0].items;
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
                widgetWeather();
            }
            handleTodoList();
  		});
    	  
    	  
      });
      
      
      
      /*------------------------financial-charts----------------------------------*/
      $scope.showvehical = 0;
      $scope.showBarvehical = 1;
      $scope.showVehicalBarChart = function(){
    	  $scope.showvehical = 0;
    	  $scope.showBarvehical = 1;
    	  $scope.locationDataList = [];
    	 
    	  
    	   $http.get('/getSoldVehicleDetailsOther/'+$routeParams.LocationId+"/"+$routeParams.managerId)
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
     });
      }
      
    
      $scope.arrayname = [];
      
      $scope.showVehicalFinancialChartByBodyStyle = function(){
    	  $scope.showBarvehical = 0;
    	  $scope.showvehical = 1;
    	  
    	  	$http.get('/getFinancialVehicleDetailsByBodyStyleOther/'+$routeParams.LocationId+"/"+$routeParams.managerId).success(function(data) {
    	  		 createChart(data);
  			});
    	  
    	  
      }
      
      $scope.showVehicalFinancialChart = function(){
    	  $scope.showBarvehical = 0;
    	  $scope.showvehical = 1;
    	  	$http.get('/getFinancialVehicleDetailsOther/'+$routeParams.LocationId+"/"+$routeParams.managerId).success(function(data) {
    	  		 createChart(data);
  			});
    	  
      }
      
      var seriesOptions = [];
      var seriesCounter = 0;
      var stockChart; 
      var stockChart1; 
      function createChart(initdata) {
    	  stockChart1 = 1;
    	  
    	  stockChart = $('#financial-chart').highcharts('StockChart', {
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
                  }]
              },
              /*plotOptions: {
                  series: {
                      compare: 'percent'
                  }
              },*/
              tooltip: {
                  pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.change})<br/>',
                  valueDecimals: 2
              },

              series: initdata
          });
      }; 
     
      
      /*-------------------------------------------------------------*/
      
      /*----------------Bar-Charts-------------------*/
      $scope.showVehicalBarAvgSale = function(){
    	  
    	  $scope.showBarvehical = 1;
    	  $scope.showvehical = 0;
    	  
    	  $http.get('/getSoldVehicleDetailsAvgSaleOther/'+$routeParams.LocationId+"/"+$routeParams.managerId)
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
    });
      }
      
      
      
      $http.get('/getSoldVehicleDetails')
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
  });
      
      /*----------------------*/

      
      $scope.activeTab = true;

      
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
     			 		    		                                 cellTemplate:' <a ng-click="grid.appScope.getTradeData(row)" href="/showPdf/{{row.entity.id}}" target="_blank" style="margin-top:7px;margin-left:6px;" >View</a>',
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
																					{ name: 'name', displayName: 'Name', width:'9%',cellEditableCondition: false,
																					     	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.name}}</a> ',
																					    	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																					    		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
																					                   return 'red';
																					               }
																					         	} ,
																					      },
																					      { name: 'phone', displayName: 'Phone', width:'7%',cellEditableCondition: false,
																					     	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.phone}}</a> ',
																					     	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																					     		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
																					                return 'red';
																					            }
																					      	} ,
																					      },
																					      { name: 'email', displayName: 'Email', width:'12%',cellEditableCondition: false,
																					     	cellTemplate:'<a  href="mailto:{{row.entity.email}}">{{row.entity.email}}</a> ',
																					     	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																					     		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
																					                return 'red';
																					            }
																					      	} ,
																					      },
																					   { name: 'requestDate', displayName: 'Date Added', width:'8%',cellEditableCondition: false,
																					     	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.requestDate}}</a> ',
																					     	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																					     		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
																					                 return 'red';
																					             }
																					       	} ,
																					       },
     	     		     			 		 		                                 { name: 'vin', displayName: 'Vin', width:'8%',cellEditableCondition: false,
     	     		     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.vin}}</a> ',
     	     		     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     		     			 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     	     		     			 		   		                                         return 'red';
     	     		     			 		   		                                     }
     	     		     			 		  		                                	} ,
     	     		     			 		 		                                 },
     	     		     			 		 		                                 { name: 'model', displayName: 'Model', width:'8%',cellEditableCondition: false,
     	     		     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.model}}</a> ',
     	     		     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     		     			 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     	     		     			 		   		                                         return 'red';
     	     		     			 		   		                                     }
     	     		     			 		  		                                	} ,
     	     		     			 		 		                                 },
     	     		     			 		 		                                 { name: 'make', displayName: 'Make', width:'9%',cellEditableCondition: false,
     	     		     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.make}}</a> ',
     	     		     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     		     			 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     	     		     			 		   		                                         return 'red';
     	     		     			 		   		                                     }
     	     		     			 		  		                                	} ,
     	     		     			 		 		                                 },
     	     		     			 		 		                            { name: 'typeOfLead', displayName: 'type', width:'9%',cellEditableCondition: false,
      	     		     			 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.typeOfLead}}</a> ',
      	     		     			 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
      	     		     			 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
      	     		     			 		   		                                         return 'red';
      	     		     			 		   		                                     }
      	     		     			 		  		                                	} ,
      	     		     			 		 		                                 },
     	     		     			 		 		                                 
     	     		     			 		 		                                			 		 		                                 
     	     		     			 		 		                                { name: 'edit', displayName: '', width:'4%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
     	     		     			 		    		                                 cellTemplate:' <a ng-click="grid.appScope.getTradeData(row)" ng-show="row.entity.typeOfLead == \'Trade-In Appraisal\'" href="/showPdf/{{row.entity.id}}" target="_blank" style="margin-top:7px;margin-left:6px;" >View</a>',
     	     		     			 		    		                                 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     	     		     			 		    		                                 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
     	     		     			 		    		                                         return 'red';
     	     		     			 		    		                                     }
     	     		     			 		   		                                	} ,
     	     		     			 				                                 
     	     		     			 				                                 },
     	     		     			 				                                 
     	     		     			 				                               { name: 'btnSold', displayName: '',enableFiltering: false, width:'35%',cellEditableCondition: false,
     	     		     	      			 		                                	cellTemplate:'<button type="button" ng-click="grid.appScope.soldScheduleStatus(row.entity)" class="btn btn-sm btn-primary" ng-show="grid.appScope.userType != \'\'"style="margin-left:3%;">SOLD</button><button type="button" ng-show="grid.appScope.userType != \'\'"ng-click="grid.appScope.cancelScheduleStatus(row.entity)" class="btn btn-sm btn-primary" style="margin-left:0px;">CANCEL</button><button type="button" ng-click="grid.appScope.addNoteToRequestUser(row.entity,\'tradeIn\')" ng-show="grid.appScope.userType != \'\'" class="btn btn-sm btn-primary" style="margin-left:0px;">HISTORY</button><button type="button" ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,2)" ng-show="grid.appScope.userType != \'\' && row.entity.confirmDate != null" class="btn btn-sm btn-primary" style="margin-left:0px;">RESCHEDULE</button><button type="button" ng-click="grid.appScope.scheduleTestDriveForUser(row.entity,2)" ng-show="grid.appScope.userType != \'\' && row.entity.confirmDate == null" class="btn btn-sm btn-primary" style="margin-left:0px;">SCHEDULE</button><button ng-show="grid.appScope.userType == \'Manager\'" type="button" ng-click="grid.appScope.assignCanceledLead(row.entity)" class="btn btn-sm btn-primary" style="margin-left:0%;">ASSIGN</button>',
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
     			 		 		
     			 		 		
    		  $http.get('/getAllRequestInfoSeen')
    				.success(function(data) {
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
    	  		
    	  		 $http.get('/getAllLostAndCompLeads')
 				.success(function(data) {
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
   	  	$http.get('/getAllVehical')
		.success(function(data) {
			$scope.allVehicalss = data;
		});
   	  		/*$scope.getAllVehical = function(){
	   	  		$http.get('/getAllVehical')
	 				.success(function(data) {
	 					$scope.allVehicalss = data;
	 				});
   	  		}*/
   	  	
   	  	
   	  	
   	  	$scope.saveCompleted = function(){
	   	  	 $http.get('/saveCompletedLeads/'+$scope.testCompleted.duration+'/'+$scope.testCompleted.comment+'/'+$scope.testCompleted.id+'/'+$scope.testCompleted.typeOfLead)
				.success(function(data) {
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Completed successfully",
					});
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
   	  		$scope.editLeads = {};
   	  	$scope.stockWiseData = [];
   	  		$scope.editVinData = function(entity){
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
				console.log($('#vinSearch_value').val());
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
   	  		
   	  		$scope.editleads = function(){
   	  		
   	  		$scope.editLeads.stockWiseData = $scope.stockWiseData;
   	  			console.log($scope.editLeads);
	   	  		 $http.post('/editLeads',$scope.editLeads).success(function(data) {
	   	  			 
	   	  			 	$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Lead Update successfully",
						});
	   	  			$("#editLeads").modal('hide');
	   	  			$scope.getAllSalesPersonRecord($scope.salesPerson);
				 });
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
    		  
    		  $http.get('/getUsers').success(function(data){
    				$scope.allUser = data;
    			 });

    	   		$http.get('/getgroupInfo').success(function(data){
    				$scope.allGroup = data;
    			 });
    	   		
    	   	 $scope.saveGroup = function(createGroup){
    			   $http.get('/saveGroup/'+createGroup)
    				.success(function(data) {
    					$.pnotify({
    					    title: "Success",
    					    type:'success',
    					    text: "group saved successfully",
    					});
    					$http.get('/getgroupInfo').success(function(data){
    						$scope.allGroup = data;
    					 });
    				});
    			   
    		   }
    		   
    		   $scope.deleteGroup = function(groupId){
    			   $http.get('/deleteGroup/'+groupId)
    				.success(function(data) {
    					$.pnotify({
    					    title: "Success",
    					    type:'success',
    					    text: "group deleted successfully",
    					});
    					$http.get('/getgroupInfo').success(function(data){
    						$scope.allGroup = data;
    					 });
    				});
    		   }
    		   
    		  
    		  $scope.saveContact = function() {
    			   $http.post('/saveContactsData',$scope.contactsDetails)
    				 .success(function(data) {
	    					 if(data == "") {
		    					 $('#createcontactsModal').modal('hide');
		    					 $.pnotify({
		    						    title: "Success",
		    						    type:'success',
		    						    text: "contact saved successfully",
		    						});
	    					 } else {
	    						 $scope.contactMsg = data;
	    					 }
    					});
    		   }
    		  $scope.flags = {};
    		 $scope.checkIndex = function(item,values){
    			 console.log(values);
    			 angular.forEach($scope.currentData, function(value, key) {
    				 if(value.id == item.id){
    					 if(values == false || values == undefined){
    						 value.flag = 1;
    					 }else{
    						 value.flag = 0;
    					 }
    					 
    				 }
    			 });
    			/*$scope.flags["flag"+index] = 1;
    			console.log($scope["flag"+index]);*/
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
        			$('#comparisonEndDate').val(endDate); 
     			}
     			
     			
     			$scope.arrId = [];
     			angular.forEach($scope.comparisonperson, function(value, key) {
     				$scope.arrId.push(value.id);
     			});
     			console.log($scope.arrId);
     			$scope.comparisonperson = [];
     			
     			angular.forEach($scope.arrId, function(value, key) {
     				console.log(value);
     				$http.get('/getComperSalePersonData/'+value+"/"+startDate+"/"+endDate).success(function(response) {
					 	$scope.comparisonperson.push(response);
				 });
     			});
     			
    		 }
    		 
    		 $scope.comparisonperson = [];
    		 
    		 $scope.checkSalePersonIndex = function(item,values){
    			 
    			 $scope.ComperFlag = 0;
    			 
        			var startDate = $('#comparisonStartDate').val();
        			var endDate = $('#comparisonEndDate').val();
        			console.log(startDate);
        			console.log(endDate);
        			if(startDate == "" || endDate == "" || startDate == undefined || endDate == undefined){
        				var today = new Date()
            			//var priorDate = new Date().setDate(today.getDate()-30)
            			endDate = $filter('date')(today,"yyyy-MM-dd");
        				var arr = [];
            			arr = $filter('date')(today,"yyyy-MM-dd").split('-');
        				startDate = arr[0]+"-"+arr[1]+"-"+"01";
            			$('#comparisonStartDate').val(startDate);
            			$('#comparisonEndDate').val(endDate); 
        			}
    			 
    			 console.log($scope.userPerformanceList);
    					 if(values == false || values == undefined){
    						 item.flag = 1;
    						 
    						 $http.get('/getComperSalePersonData/'+item.id+"/"+startDate+"/"+endDate).success(function(response) {
    							 	$scope.comparisonperson.push(response);
    						 });
    						
    					 }else{
    						 angular.forEach($scope.comparisonperson, function(value, key) {
    							 if(value.id == item.id){
    								 $scope.comparisonperson.splice(key,1);
    							 }
    						 });
    						 
    						 item.flag = 0;
    					 }
    					 console.log($scope.comparisonperson);
    		 }
    		 
    		 $scope.bestEmpComp = function(){
    			 
    			 $scope.ComperFlag = 1;
    			 
    			 $scope.comparisonperson = [];
    			 var today = new Date()
     			var endDate = $filter('date')(today,"yyyy-MM-dd");
 				var arr = [];
     			arr = $filter('date')(today,"yyyy-MM-dd").split('-');
 				var startDate = arr[0]+"-"+arr[1]+"-"+"01";
    			 
 				$http.get('/getComperSalePersonData/'+$scope.salesPerson+"/"+startDate+"/"+endDate).success(function(response) {
				 	$scope.comparisonperson.push(response);
 				});
 				
 				$http.get('/getDateRangSalePerson/'+startDate+"/"+endDate).success(function(response) {
 					console.log(response);
 					if(response != $scope.salesPerson){
 						$http.get('/getComperSalePersonData/'+response+"/"+startDate+"/"+endDate).success(function(response) {
 						 	$scope.comparisonperson.push(response);
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
    	
    		  $http.get('/getMonthlyVisitorsStats').success(function(response) {
    			  var visitorsData = {
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
    			        var chartOptions = {
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
    			        var ctx = document.getElementById("visitors-chart").getContext("2d");
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
    			        var ctx2 = document.getElementById("actions-chart").getContext("2d");
    			        var myNewChart2 = new Chart(ctx2).Line(actionsData, chartOptions);
    			        
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
    			        
    		  });
    		  
    		  setInterval(function(){
    			  $scope.onlineVisitorFind();
    			}, 10000)
    		  
    		$scope.onlineVisitorFind = function(){
    			  
    			  $http.get('/getVisitorOnline').success(function(response) {
    				  $scope.onlineVisitorsCount = response;
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
    			  $scope.showVehicalBarChart();
    			  $scope.getPerformanceOfUser();
    			 if($routeParams.LocationId == null){
    				 $scope.getSalesDataValue(0);
    			 }
    			 
    			$scope.cal_whe_flag = true;
   			   	$(".wheth-report").hide();
   			  // 	$scope.checkManagerLogin();
   			   
    			  $scope.showToDoList = false;
				  $scope.showCalendar = true;
				  
		    		  $http.get('/getScheduleDates')
						.success(function(data) {
						$scope.scheduleDates = data;
						 var datesArray = [];
						 for(var i=0;i<$scope.scheduleDates.length;i++) {
							 var dateStr = $scope.scheduleDates[i].confirmDate;
							 var date = new Date();
							 var arr = [];
							    arr = dateStr.split('-');
					        	date.setYear(arr[0]);
					        	var month = arr[1];
					        	date.setMonth(month-1);
					        	date.setDate(arr[2]);
					        	datesArray.push(date);
						 }
			    		  $(".multidatepicker").multiDatesPicker({
			    			  addDates:datesArray,
			        			  onSelect : function(dateText, inst){
			        				  $scope.showToDoList = true;
			        				  $scope.showCalendar = false;
			        				  $scope.selectedDate = dateText;
//			        				  var date = new Date();
									 var arr1 = [];										
			        				    arr1 = dateText.split('/');
			        				    if(arr1.length > 2){
			        				    	$scope.editdate = arr1[2]+"-"+arr1[0]+"-"+arr1[1];
			        				    }else{
			        				    	$scope.editdate =arr1[0];
			        				    }
			        				  $scope.getScheduleBySelectedDate($scope.editdate);
			        			  }
			    		  });
					});
    		  
		    		  $http.get('/getUsersToAssign')
						.success(function(data) {
						$scope.usersList = data;
					});  
		    		  $scope.getToDoList();
		    		  $scope.getMonthChart();
		    		  $('#topPerf').css("text-decoration","underline");
		    		  $('#weekPerf').css("text-decoration","underline");
		    		  $scope.topPerformers = true;
		    		  $scope.weekPerformance = true;
		    		  $scope.getPerformanceOfUser();
    		  };  
    		  
    		  $scope.getToDoList = function() {
    			  $http.get('/getToDoList')
					.success(function(data) {
					$scope.toDoList = data;
				});  
    		  }
    		  
    		  $scope.showCalendarData = function() {
    			  $scope.showToDoList = false;
				  $scope.showCalendar = true;
				  $scope.init();
    		  }
    		  
    		  $scope.deleteForeverLead = function(entity) {
    			  $scope.leadId = entity.id;
    			  $scope.leadType = entity.leadType;
    			  $('#btnDeleteForever').click();
    		  }
    		  
    		  $scope.comparisonSalePerson = function(){
    			  
    			  if($scope.comparisonperson.length >= 2){
    				  if($scope.comparisonperson[0].totalSalePrice > $scope.comparisonperson[1].totalSalePrice){
    					  $scope.totalSalePricePer = (($scope.comparisonperson[0].totalSalePrice - $scope.comparisonperson[1].totalSalePrice) * 100 / $scope.comparisonperson[0].totalSalePrice).toFixed(2);
    				  }else{
    					  $scope.totalSalePricePer = (($scope.comparisonperson[1].totalSalePrice - $scope.comparisonperson[0].totalSalePrice) * 100 / $scope.comparisonperson[1].totalSalePrice).toFixed(2);
    				  }
    				  
    				  if($scope.comparisonperson[0].totalsaleCar > $scope.comparisonperson[1].totalsaleCar){
    					  $scope.totalsaleCarPer = (($scope.comparisonperson[0].totalsaleCar - $scope.comparisonperson[1].totalsaleCar) * 100 / $scope.comparisonperson[0].totalsaleCar).toFixed(2);
    				  }else{
    					  $scope.totalsaleCarPer = (($scope.comparisonperson[1].totalsaleCar - $scope.comparisonperson[0].totalsaleCar) * 100 / $scope.comparisonperson[1].totalsaleCar).toFixed(2);
    				  }
    				  
    				  if($scope.comparisonperson[0].allGeneratedLeadCount > $scope.comparisonperson[1].allGeneratedLeadCount){
    					  $scope.allGeneratedLeadCountPer = (($scope.comparisonperson[0].allGeneratedLeadCount - $scope.comparisonperson[1].allGeneratedLeadCount) * 100 / $scope.comparisonperson[0].allGeneratedLeadCount).toFixed(2);
    				  }else{
    					  $scope.allGeneratedLeadCountPer = (($scope.comparisonperson[1].allGeneratedLeadCount - $scope.comparisonperson[0].allGeneratedLeadCount) * 100 / $scope.comparisonperson[1].allGeneratedLeadCount).toFixed(2);
    				  }
    				  
    				  if($scope.comparisonperson[0].lostLeadCount > $scope.comparisonperson[1].lostLeadCount){
    					  $scope.lostLeadCountPer = (($scope.comparisonperson[0].lostLeadCount - $scope.comparisonperson[1].lostLeadCount) * 100 / $scope.comparisonperson[0].lostLeadCount).toFixed(2);
    				  }else{
    					  $scope.lostLeadCountPer = (($scope.comparisonperson[1].lostLeadCount - $scope.comparisonperson[0].lostLeadCount) * 100 / $scope.comparisonperson[1].lostLeadCount).toFixed(2);
    				  }
    				  
    				  
    				  if($scope.comparisonperson[0].successRate > $scope.comparisonperson[1].successRate){
    					  $scope.successRatePer = (($scope.comparisonperson[0].successRate - $scope.comparisonperson[1].successRate) * 100 / $scope.comparisonperson[0].successRate).toFixed(2);
    				  }else{
    					  $scope.successRatePer = (($scope.comparisonperson[1].successRate - $scope.comparisonperson[0].successRate) * 100 / $scope.comparisonperson[1].successRate).toFixed(2);
    				  }
    				  
    				  if($scope.comparisonperson[0].likeCount > $scope.comparisonperson[1].likeCount){
    					  $scope.likeCountPer = (($scope.comparisonperson[0].likeCount - $scope.comparisonperson[1].likeCount) * 100 / $scope.comparisonperson[0].likeCount).toFixed(2);
    				  }else{
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
    					  $scope.callMadePer = (($scope.comparisonperson[0].callMade - $scope.comparisonperson[1].callMade) * 100 / $scope.comparisonperson[0].callMade).toFixed(2);
    				  }else{
    					  $scope.callMadePer = (($scope.comparisonperson[1].callMade - $scope.comparisonperson[0].callMade) * 100 / $scope.comparisonperson[1].callMade).toFixed(2);
    				  }
    				  
    				  if($scope.comparisonperson[0].mailSent > $scope.comparisonperson[1].mailSent){
    					  $scope.mailSentPer = (($scope.comparisonperson[0].mailSent - $scope.comparisonperson[1].mailSent) * 100 / $scope.comparisonperson[0].mailSent).toFixed(2);
    				  }else{
    					  $scope.mailSentPer = (($scope.comparisonperson[1].mailSent - $scope.comparisonperson[0].mailSent) * 100 / $scope.comparisonperson[1].mailSent).toFixed(2);
    				  }
    				  
    				  if($scope.comparisonperson[0].testDriveSched > $scope.comparisonperson[1].testDriveSched){
    					  $scope.testDriveSchedPer = (($scope.comparisonperson[0].testDriveSched - $scope.comparisonperson[1].testDriveSched) * 100 / $scope.comparisonperson[0].testDriveSched).toFixed(2);
    				  }else{
    					  $scope.testDriveSchedPer = (($scope.comparisonperson[1].testDriveSched - $scope.comparisonperson[0].testDriveSched) * 100 / $scope.comparisonperson[1].testDriveSched).toFixed(2);
    				  }
    				  
    			  }
    			
    			  $('#btncomparisonSale').click();
    		  }
    		  
    		  $scope.restoreLead = function(entity){
    			  
    			  console.log(entity);
    			  $http.get('/restoreLead/'+entity.id+'/'+entity.leadType)
					.success(function(data) {
						$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Lead Restore successfully",
						});
						$scope.getAllCanceledLeads();
				});
    		  }
    		  
    		  $scope.deleteMyLead = function() {
    			  console.log($scope.leadId);
    			  console.log($scope.leadType);
    			  $http.get('/deleteCanceledLead/'+$scope.leadId+'/'+$scope.leadType)
					.success(function(data) {
						$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Lead deleted successfully",
						});
						$scope.getAllCanceledLeads();
				});
    		  }
    		  
    		  $scope.getScheduleBySelectedDate = function(date) {
    			  $http.get('/getScheduleBySelectedDate/'+date)
					.success(function(data) {
						console.log("changesss");
						console.log(data);
					$scope.scheduleList = data;
				});  
    			  
    			  $http.get('/getToDoBySelectedDate/'+date)
					.success(function(data) {
					$scope.toDoDateList = data;
				}); 
    			  
    		  }
    		  
    			  $http.get('/getAllScheduleTestAssigned')
    					.success(function(data) {
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
    			  
    			  $http.get('/getAllTradeInSeen')
    				.success(function(data) {
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
      	  		
      	  	
    			  
	    		/*$http.get('/getUserType')
	  			  .success(function(data) {
	  			 	$scope.userType = data;
	  			 	if($scope.userType == "Manager") {
	  			 		$scope.getGMData();
	  			 		//$scope.getAnalystData();
	  			 	}
	  			 	if($scope.userType == "Sales Person") {
	  			 		$scope.getToDoNotification();
	  			 		$scope.getAssignedLeads();
	  			 	}
	  			});*/
	    		
	    		var promo =  $interval(function() {
	    			$scope.getToDoNotification();
  			 		$scope.getAssignedLeads();
	    		},120000);
	    		
	    		$scope.visitors = [];
	    		$scope.newUsers = [];
	    		$scope.bounceRate = [];
	    		$http.get('/getVisitorStats').success(function(response) {
	    			console.log(response);
	    			$scope.visitors[0] = {'title':'Visit Today','value':response[0].dates[0].items[0].value};
		    		$scope.visitors[1] = {'title':'Visit Yesterday','value':response[0].dates[1].items[0].value};
		    		$scope.newUsers[0] = Math.round($scope.visitors[0].value==0?0:(response[1].dates[0].items[0].value/$scope.visitors[0].value)*100);
		    		$scope.newUsers[1] = Math.round($scope.visitors[1].value==0?0:(response[1].dates[1].items[0].value/$scope.visitors[1].value)*100);
		    		$scope.bounceRate[0] = response[2].dates[0].items[0].value;
		    		$scope.bounceRate[1] = response[2].dates[1].items[0].value;
	    		});
	    		
	    		$scope.showSessionAnalytics = function(id,vin,status){
	    			console.log(id);
	    			console.log(vin);
	    			console.log(status);
	    			$location.path('/sessionsAnalytics/'+id+"/"+vin+"/"+status);
	    		};
	    		
	    		$scope.currentSelectedType = 0;
	    		$scope.currentSelectedDuration = 0;
	    		$scope.weekData = {};
	    		$scope.currentData = [];
	    		$scope.showWeekVisited = function() {
	    			$scope.currentSelectedDuration = 0;
	    			$scope.getVisitedData('week','countHigh','0','0');
	    		};
	    		
	    		$scope.showMonthVisited = function() {
	    			$scope.currentSelectedDuration = 1;
	    			$scope.getVisitedData('month','countHigh','0','0');
	    		};
	    		
	    		$scope.notchange = 0;
	    		$scope.getVisitedData = function(type,filterBy,search,searchBy) {
	    			$http.get('/getVisitedDataOther/'+type+'/'+filterBy+'/'+search+'/'+searchBy+"/"+$routeParams.LocationId+"/"+$routeParams.managerId+"/"+$routeParams.gmIsManager).success(function(response) {
	    				console.log(response);
	    				$scope.weekData = response;
	    				
	    				if(response.topVisited.length == 0){
	    					$scope.currentSelectedType = 2;
	    					$scope.notchange = 1;
	    				}else{
	    					$scope.notchange = 0;
	    				}
	    				
	    				if($routeParams.gmIsManager == 1){
	    					if($scope.currentSelectedType==0) 
	    					$scope.currentData = response.topVisited;
	    				else if($scope.currentSelectedType==1)
	    					$scope.currentData = response.worstVisited;
	    				else if($scope.currentSelectedType==2)
	    					$scope.currentData = response.allVehical;
	    				}else{
	    					$scope.currentData = response.allVehical;
	    				}
	    				
	    					
	    			});
	    		};
	    		
	    		/*$scope.getFromCrm = function(name){
	    			if(name !=null || name!=''){
	    				$http.get('/getDataFromCrm/'+name).success(function(data){
		    				$scope.crmData = data;
		    			 });
	    			}
	    		};*/
	    		/*$scope.dataFromCrm = function(item){
	    			$scope.item = JSON.parse(item);
	    			$scope.lead.custName = $scope.item.firstName +" "+ $scope.item.lastName;
	    			$scope.lead.custNumber = $scope.item.phone;
	    			$scope.lead.custEmail = $scope.item.email;
	    			$scope.lead.custZipCode = $scope.item.zip;
	    			console.log($scope.item);
	    		};*/
	    		
	    		$scope.selectedObj = function (selectObj) {
	    			if(selectObj.originalObject != undefined){
	    				$scope.item = selectObj.originalObject;
		    		    $scope.lead.custName = $scope.item.fullName;
		    		    $scope.lead.custNumber = $scope.item.phone;
		    			$scope.lead.custEmail = $scope.item.email;
		    			$scope.lead.custZipCode = $scope.item.zip;
	    			}
	    		};
	    			$http.get('/getHeardAboutUs').success(function(response) {
	    				$scope.heardAboutUs = response;
	    				console.log($scope.heardAboutUs);
	    			});
	    			$scope.othertxt=null;
	    		$scope.openCreateNewLeadPopup = function() {
	    			$scope.stockWiseData = [];
	    			$scope.stockWiseData = [{}];
	    			$scope.getMakes();
	    			$("#createLeadPopup").modal();
	    		};
	    		
	    		$scope.openCreateNewLeads = function(item) {
	    			console.log(item);
	    			$scope.stockWiseData = [];
	    			$http.get('/getStockDetails/'+item).success(function(response) {
	    				
	    				console.log(response);
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
	    			//$scope.getStockDetails(item);
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
	    			console.log($scope.stockWiseData);
	    		}
	    		
	    		$scope.initialiase();
	    		$scope.isInValid = false;
	    		$scope.isStockError = false;
	    		$scope.focusOut = function(){
	    		console.log($('#ex1_value').val());
	    			//$scope.lead.custName = $('#ex1_value').val();
	    		};
	    		$scope.createLead = function() {
	    			
	    			
	    			if($scope.lead.custName == ''){
	    				$scope.lead.custName = $('#ex1_value').val();
	    			}
	    			console.log($scope.lead.custName);
	    			//!(($scope.lead.make!='' && $scope.lead.model!='') ||
	    			//($scope.lead.makeSelect!='' && $scope.lead.modelSelect!='')) ||
	    			
	    			if($scope.lead.custName==''||$scope.lead.custZipCode==''||$scope.lead.custEmail==''||$scope.lead.custNumber=='' ||  
	    					 $scope.lead.leadType =='' || $scope.lead.contactedFrom==''||$scope.lead.enthicity==''||$scope.lead.enthicity==null) {
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
		    			} else {
		    				$("#createLeadPopup").modal('hide');
		    				$("#tradeInApp").modal();
		    			}
	    			}
	    			
	    			//$scope.makeLead();
	    			/*console.log($('#ex1_value').val());
	    			$scope.lead.custName = $('#ex1_value').val();
	    			console.log($scope.lead);
	    			if($scope.lead.custName==''||$scope.lead.custZipCode==''||$scope.lead.custEmail==''||$scope.lead.custNumber=='' || !(($scope.lead.make!='' && $scope.lead.model!='') || 
	    					($scope.lead.makeSelect!='' && $scope.lead.modelSelect!='')) || $scope.lead.leadType =='' || $scope.lead.contactedFrom==''||$scope.lead.enthicity==''||$scope.lead.enthicity==null) {
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
		    			} else {
		    				$("#createLeadPopup").modal('hide');
		    				$("#tradeInApp").modal();
		    			}
	    			}*/
	    			
	    			
	    			console.log($scope.lead);
	    			/*$http.post('/createLead',$scope.lead).success(function(response) {
	    				$("#createLeadPopup").modal('hide');
	    				if($scope.lead.leadType=='2') 
	    					$scope.getScheduleTestData();
	    				else if($scope.lead.leadType=='1')
	    					$scope.getRequestMoreData();
	    				else
	    					$scope.getTradeInData();
	    			});*/
	    			//$route.reload();
	    		};
	    		
	    		$scope.makeLeadEdit = function(){
	    				$scope.lead.leadType = '3';
	    			console.log($scope.lead);
	    			$http.post('/createLead',$scope.lead).success(function(response) {
	    					$("#tradeInAppEdit").modal('hide');
	    				
	    			});
	    		}
	    		
	    		$scope.makeLead = function() {
	    			$scope.othertxt = $('#othertxt').val();
	    			console.log($scope.othertxt);
	    			if($scope.lead.hearedFrom == "Other"){
	    				console.log($scope.othertxt);
	    				if($scope.othertxt == null || $scope.othertxt == undefined){
	    					$scope.lead.hearedFrom = "Other";
	    				}else{
	    					$scope.lead.hearedFrom = $scope.othertxt;
	    					$http.get('/addHeard/'+$scope.lead.hearedFrom).success(function(response) {
	    						$http.get('/getHeardAboutUs').success(function(response) {
	    		    				$scope.heardAboutUs = response;
	    		    				console.log($scope.heardAboutUs);
	    		    			});
	    					});
	    				}
	    				
	    			}
	    			$("#createLeadPopup").modal('hide');
	    			$scope.lead.stockWiseData = $scope.stockWiseData;
	    			console.log($scope.lead);
	    			$http.post('/createLead',$scope.lead).success(function(response) {
	    				$scope.getVisitedData('week','countHigh','0','0');
	    				$scope.userLocationData('Week','person');
	    				$scope.getAllSalesPersonRecord($scope.salesPerson);
	    				if($scope.lead.leadType=='2')  {
	    					$scope.getScheduleTestData();
	    					$("#createLeadPopup").modal('hide');
	    				}
	    				else if($scope.lead.leadType=='1') {
	    					$scope.getRequestMoreData();
	    					$("#createLeadPopup").modal('hide');
	    				}
	    				else {
	    					$scope.getTradeInData();
	    					$("#tradeInApp").modal('hide');
	    				}
	    				$scope.initialiase();
	    			});
	    			$scope.getAllSalesPersonRecord($scope.salesPerson);
	    		};
	    		
	    		$scope.changeMakeSelect = function(modelSelect) {
	    			$scope.lead.modelSelect = modelSelect;
	    		};
	    		
	    		$scope.getModelsByMake = function(makeSelect) {
	    			$scope.lead.makeSelect = makeSelect;
	    			$scope.lead.modelSelect = '';
	    			$http.get('/getModels/'+makeSelect).success(function(response) {
	    				$scope.models = response;
	    			});
	    		};
	    		$scope.stockWiseData = [];
	    		$scope.stockWiseData.push({});
	    		$scope.getStockDetails = function(stockRp) {
	    			console.log(stockRp.stockNumber);
	    		
	    			$scope.isStockError = false;
	    			$http.get('/getStockDetails/'+stockRp.stockNumber).success(function(response) {
	    				
	    				
	    				console.log(response);
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
	    			$http.get('/getMakes').success(function(response) {
	    				console.log(response);
	    				$scope.makes = response.makes;
	    	    		
	    	    		
	    			});
	    		};
	    		
	    		$scope.showTopVisited = function() {
	    			$scope.getVisitedData('week','countHigh','0','0');
	    			$scope.currentSelectedType = 0;
	    			$scope.currentData = $scope.weekData.topVisited;
	    		};
	    		
	    		$scope.filterFunction = function(filterBy) {
	    			console.log(filterBy);
	    			$scope.getVisitedData('week',filterBy,'0','0');
	    		};
	    		$scope.search = "";
	    		$scope.searchBy = "";
	    		$scope.showTextBox = function(search){
	    			console.log($scope.search);
	    			console.log($scope.searchBy)
	    			$scope.search = search;
	    			
	    		}
	    		
	    		$scope.findMake = function(value,searchBy){
	    			console.log(value.length);
	    			if(value.length > 2){
	    				$scope.searchBy = searchBy;
	    				$scope.getVisitedData('week','countHigh',value,$scope.searchBy);
	    			}
	    		}
	    		$scope.findModel = function(value,searchBy){
	    			console.log(value.length);
	    			if(value.length > 1){
	    				$scope.searchBy = searchBy;
		    			$scope.getVisitedData('week','countHigh',value,$scope.searchBy);
	    			}
	    		}
	    		
	    		$scope.showWorstVisited = function() {
	    			$scope.currentSelectedType = 1;
	    			$scope.currentData = $scope.weekData.worstVisited;
	    		};
	    		
	    		$scope.showAllvehicles = function(){
	    			$scope.getVisitedData('week','countHigh','0','0');
	    			$scope.currentSelectedType = 2;
	    			$scope.currentData = $scope.weekData.allVehical;
	    		}
	    		
	    		$scope.showWeekVisited();
	    		$scope.doComplete = function() {
	    			$(".live-tile").liveTile();
	    		};
	    		
	    		$scope.getAnalystData = function() {
	    			$http.get('/getAnalystData').success(function(response) {
		    			console.log(response);
	    			});
	    		};
	    		
	    		$scope.getSalesDataValue = function(locationValue) {
	    			
	    			$http.get('/getSalesUserOnly/'+locationValue)
		    		.success(function(data){
		    			console.log(data);
		    			$scope.salesPersonPerf = data;
		    			 $scope.gridOptionsValue.data = $scope.salesPersonPerf;
		    			angular.forEach($scope.salesPersonPerf, function(value, key) {
		    				value.isSelected = false;
		    			});
		    		});
	    		}
	    		
	    		
	    		$scope.getGMData = function() {
		    		$http.get('/getSalesUser')
		    		.success(function(data){
		    			console.log(data);
		    			$scope.salesPersonList =data;
		    			$scope.getAllSalesPersonRecord($scope.salesPersonList[0].id);
		    		});
	    		}
	    		
	    		$scope.getAssignedLeads = function() {
	    			$http.get('/getAssignedLeads')
		    		.success(function(data){
		    			$scope.leadCount = data.count;
		    			if($scope.leadCount != '0') {
		    				var notifContent;
		    				$scope.leadNotification = data.data;
		    				console.log($scope.leadNotification);
		    				if($scope.leadCount==1) {
		    					notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><h4 class='alert-title f-14' id='cnt'>1 New Lead Assigned</h4><p class='row' style='margin-left:0;'><span style='color: #319DB5;font-weight: bold;'>INFO: </span><span>"+$scope.leadNotification.make+" "+$scope.leadNotification.model+" "+$scope.leadNotification.name+"</span></p><p class='row' style='margin-left:0;'><span style='color: #319DB5;font-weight: bold;'>TYPE: </span><span>"+$scope.leadNotification.leadType+"</span></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>See the Leads&nbsp;<i class='glyphicon glyphicon-download'></i></a></p></div></div>";
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
			    	                	$('html, body').animate({scrollTop:480}, 'slow');
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
	    			$http.get('/getNewToDoCount')
		    		.success(function(data){
		    			$scope.toDoCount = data.count;
		    			if($scope.toDoCount != '0') {
		    				var notifContent;
		    				$scope.notification = data.data;
		    				console.log($scope.notification);
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
		    					"<a class='f-12' style='float:right;'>Go to todos&nbsp;<i class='glyphicon glyphicon-download'></i></a></span></p></div></div>";
		    				} else {
		    					notifContent = '<div class="alert alert-dark media fade in bd-0" id="message-alert"><div class="media-left"></div><div class="media-body width-100p"><h4 class="alert-title f-14" id="cnt">'+$scope.toDoCount+' New Todos Assigned</h4><p class="pull-left" style="margin-left:65%;"><a class="f-12">Go to todos&nbsp;<i class="glyphicon glyphicon-download"></i></a></p></div></div>';
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
	    			$http.get('/setLeadSeen')
		    		.success(function(data){
		    		});
	    		};
	    		
	    		$scope.setTodoSeen = function() {
	    			$http.get('/setTodoSeen')
		    		.success(function(data){
		    			
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
    		$scope.schedulTestDir();
    	}
    	
    	$scope.requestMore = function() {
    		$scope.schedTest = false;
    		$scope.reqMore = true;	
        	$scope.testdrv = false;
        	$scope.allLeadd = false;
        	$scope.trdin = false;
        	$scope.showLeadsV = false;
        	$scope.cancelleads = false;
    	}		  
    	$scope.testDrive = function() {
    		$scope.schedTest = false;
    		$scope.reqMore = false;	
        	$scope.testdrv = true;
        	$scope.trdin = false;
        	$scope.allLeadd = false;
        	$scope.showLeadsV = false;
        	$scope.cancelleads = false;
    	}	
    	$scope.tradeIn = function() {
    		$scope.schedTest = false;
    		$scope.reqMore = false;	
        	$scope.testdrv = false;
        	$scope.trdin = true;
        	$scope.allLeadd = false;
        	$scope.showLeadsV = false;
        	$scope.cancelleads = false;
    	}
    	
    	$scope.getAllLeadIn = function(){
    		$scope.schedTest = false;
    		$scope.reqMore = false;	
        	$scope.testdrv = false;
        	$scope.trdin = false;
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
        	$scope.showAllTypeLeads = false;
        	$scope.reqMore = false;	
        	$scope.testdrv = false;
        	$scope.trdin = false;
        	$scope.showLeadsV = false;
        	$scope.cancelleads = true;
        	$scope.getAllCanceledLeads();
        }
        $scope.showLeadsArchive = function(){
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
        	/*$scope.getAllListLeadDate = [];
        	angular.forEach($scope.gridOptions2.data,function(value,key){
        		$scope.getAllListLeadDate.push(value);
        	});
        	angular.forEach($scope.gridOptions5.data,function(value,key){
        		$scope.getAllListLeadDate.push(value);
        	});
        	angular.forEach($scope.gridOptions3.data,function(value,key){
        		$scope.getAllListLeadDate.push(value);
        	});
        	console.log($scope.gridOptions7.data);
        	$scope.gridOptions7.data = $scope.getAllListLeadDate;*/
        }
        
        $scope.schedulTestDir = function(){
        	$http.get('/getTestDirConfir')
			.success(function(data) {
				console.log(data);
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
				   console.log(day);
				   var img= "";
			   angular.forEach($scope.whDataArr,function(value,key){
				  if(angular.equals(day, value.date)){
					 
					  value1.wether = value.text+"  "+value.low;
				  }
			   });
			 });  
        }
        
        $scope.getCompletedData = function(){
        	$http.get('/getAllCompletedLeads')
			.success(function(data) {
				console.log(data);
				$scope.gridOptions10.data = data;
				
				$scope.completedL = data;
			});
        }
        
        $scope.getAllLostAndComLeads = function(){
        	$http.get('/getAllLostAndCompLeads')
			.success(function(data) {
				console.log(data);
				$scope.gridOptions6.data = data;
				
			});
        }
        
        $scope.getAllCanceledLeads = function() {
        	$http.get('/getAllCanceledLeads')
			.success(function(data) {
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
        	console.log(entity);
        	$scope.cancelId = entity.id;
        	$scope.leadType = entity.typeOfLead;
        	$scope.changedUser = "";
        	$('#btnAssignUser').click();
        }
        
        $scope.changeAssignedUser = function() {
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
        	
        }
        
    	$scope.getScheduleTestData = function() {
	    		$http.get('/getAllScheduleTestAssigned')
				.success(function(data) {
					console.log(data);
					$scope.gridOptions2.data = data;
					$scope.AllScheduleTestAssignedList = data;
			});
    	};
    	
    	$scope.getRequestMoreData = function() {
    		$http.get('/getAllRequestInfoSeen')
			.success(function(data) {
			$scope.gridOptions.data = data;
			$scope.AllRequestInfoSeenList = data;
		});
	};
    	
		$scope.getTradeInData = function() {
			 $http.get('/getAllTradeInSeen')
				.success(function(data) {
					console.log(data);
			 		$scope.gridOptions3.data = data;
			 		$scope.AllTradeInSeenList = data;
			 });
		};
	
	
		
	
		$scope.getAllSalesPersonRecord = function(id){
		       console.log(id);
		       console.log($scope.userType);
		       $scope.getAllListLeadDate = [];
		       var countUnReadLead = 0;
		       $scope.salesPerson = id;
		       	if($scope.salesPerson == undefined){
		       		$scope.salesPerson = 0;
		       		id = 0;
		       	}
		       	
		       	
		       
		       	$http.get('/getAllSalesPersonScheduleTestAssigned/'+id)
				.success(function(data) {
				$scope.gridOptions2.data = data;
				$scope.AllScheduleTestAssignedList = data;
					if($scope.userType == "Sales Person"){
						angular.forEach($scope.gridOptions2.data,function(value,key){
			        		$scope.getAllListLeadDate.push(value);
			        		if(value.noteFlag == 0 && value.confirmDate == null){
			        			countUnReadLead++;
			        		}
			        	});
						$scope.lengthOfAllLead = countUnReadLead;
					}
					
				
			    });
 
    		$http.get('/getAllSalesPersonRequestInfoSeen/'+id)
			.success(function(data) {
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
			}
		    });

  
			 $http.get('/getAllSalesPersonTradeInSeen/'+id)
				.success(function(data) {
					console.log(data);
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
					}
				});
			 
			 
			 $scope.getAllCanceledLeads();
			 
			 $http.get('/getAllSalesPersonLostAndComp/'+id)
				.success(function(data) {
			 		$scope.gridOptions6.data = data;
			 		$scope.AllTradeInSeenList = data;
			 });
			 
			 $http.get('/getTestDirConfirById/'+id)
				.success(function(data) {
					console.log(data);
					$scope.gridOptions9.data = data;
					angular.forEach($scope.gridOptions9.data,function(value,key){
						 value.check = false;
					 });
					$scope.setWether($scope.gridOptions9.data);
					$scope.allTestDirConfir = data;
				
				});
			 
			 $http.get('/getAllCompletedLeadsbyId/'+id)
				.success(function(data) {
					console.log(data);
					$scope.gridOptions10.data = data;
					$scope.completedL = data;
				});
			 
			 
			 if($scope.userType == "Manager"){
				 angular.forEach($scope.gridOptions5.data,function(value,key){
		        		$scope.getAllListLeadDate.push(value);
		        		if(value.noteFlag == 0 && value.confirmDate == null){
		        			countUnReadLead++;
		        		}
		        	});
				 angular.forEach($scope.gridOptions2.data,function(value,key){
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
			 }
	    	
	        	$scope.gridOptions7.data = $scope.getAllListLeadDate;
	        	
			
	}
		
		
		
    	$scope.soldScheduleStatus = function(entity) {
    		console.log(entity);
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

    		$http.get('/setScheduleConfirmClose/'+entity.id+'/'+entity.typeOfLead)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Cancel successfully",
				});
				entity.bestDay = "";
				entity.bestTime = "";
				$scope.getAllSalesPersonRecord($scope.salesPerson);
			});
    		
    	}
    	
    	$scope.saveScheduleClose = function() {
    		console.log($scope.scheduleStatusCancel);
	    		$http.get('/setScheduleStatusClose/'+$scope.scheduleStatusCancel.id+'/'+$scope.scheduleStatusCancel.typeOfLead+'/'+$scope.reasonToCancel)
				.success(function(data) {
					$scope.getScheduleTestData();
					$('#scheduleCancelBtn').click();
					$.pnotify({
    				    title: "Success",
    				    type:'success',
    				    text: "Status changed successfully",
    				});
					$scope.getAllSalesPersonRecord($scope.salesPerson);
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
    		$scope.soldContact.make = entity.make;
    		$scope.soldContact.year = entity.year;
    		$scope.soldContact.mileage = entity.mileage;
    		$scope.soldContact.price = entity.price;
    		$('#btnCompleteRequest').click();
    	};
    	
    	
    	
    	/*$scope.saveScheduleStatus = function() {
    		
    		$http.post('/setVehicleAndScheduleStatus',$scope.soldContact)
			.success(function(data) {
				$('#scheduleStatusModal').modal('hide');
				$scope.getAllSalesPersonRecord($scope.salesPerson);
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Vehicle status changed successfully",
				});
				for(var i=0;i<$scope.scheduleList.length;i++) {
 					if($scope.scheduleStatusVal.id == $scope.scheduleList[i].id) {
 						$scope.scheduleList.splice(i,1);
 					}
 				}
		});
	}*/
    	/*$scope.saveRequestStatus = function() {
    		
    		console.log($scope.soldContact);
    		$http.post('/setRequestStatusComplete',$scope.soldContact)
			.success(function(data) {
				$('#requestCompleteStatusModal').modal('hide');
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Status changed successfully",
				});
				$scope.getAllSalesPersonRecord($scope.salesPerson);
			});
    	};		*/
    	
    	$scope.cancelRequestStatus = function(entity) {
    		$scope.requestStatusCancel = entity;
    		$scope.reasonToCancel = "";
    		$('#btnCancelRequest').click();
    	};
    	
    	$scope.saveRequestStatusCancel = function() {
    		$http.get('/setRequestStatusCancel/'+$scope.requestStatusCancel.id+'/'+$scope.reasonToCancel)
			.success(function(data) {
				$('#requestCancelBtn').click();
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Status changed successfully",
				});
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
    		$scope.soldContact.make = entity.make;
    		$scope.soldContact.year = entity.year;
    		$scope.soldContact.mileage = entity.mileage;
    		$scope.soldContact.price = entity.price;
    		$('#btnCompleteRequest').click();
    	}
    	
    	$scope.saveRequestStatus = function() {
    		
    		//console.log($scope.soldContact);
    		$('#soldBtn').attr("disabled", true);
    		$http.post('/setRequestStatusComplete',$scope.soldContact)
			.success(function(data) {
				console.log(data);
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
    	
    	/*$scope.saveCompleteTradeInStatus = function() {
    		$http.post('/setTradeInStatusComplete',$scope.soldContact)
			.success(function(data) {
			
				$('#tradeInCompleteStatusModal').modal('hide');
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Status changed successfully",
				});
				$scope.getAllSalesPersonRecord($scope.salesPerson);
			});
    	}*/
    	
    	
    	$scope.cancelLead = function(leads,index){
    		if(leads.typeOfLead == "Schedule Test Drive"){
    			leads.option = 0;
    		}else if(leads.typeOfLead == "Request More Info"){
    			leads.option = 1;
    		}else if(leads.typeOfLead == "Trade-In Appraisale"){
    			leads.option = 2;
    		}
    		var change = "0";
    		$http.get('/setScheduleStatusClose/'+leads.id+'/'+leads.typeOfLead+'/'+change)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Status changed successfully",
				});
				
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
    		$http.get('/setScheduleStatusClose/'+leads.id+'/'+leads.option+'/'+change)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Status changed successfully",
				});
				
			});
    		$scope.soldContact.parentChildLead.splice(index, 1);
    		
    	}
    	
    	$scope.cancelLeadSched = function(leads,index){
    		
    		console.log(leads);
    		
    		if(leads.typeOfLead == "Schedule Test Drive"){
    			leads.option = 0;
    		}else if(leads.typeOfLead == "Request More Info"){
    			leads.option = 1;
    		}else if(leads.typeOfLead == "Trade-In Appraisale"){
    			leads.option = 2;
    		}
    		var change = "0";
    		$http.get('/setScheduleStatusClose/'+leads.id+'/'+leads.option+'/'+change)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Status changed successfully",
				});
				
			});
    		$scope.testDriveData.parentChildLead.splice(index, 1);
    	}
    	
    	$scope.cancelTradeInStatus = function(entity) {
    		$scope.tradeInStatusCancel = entity;
    		$scope.reasonToCancel = "";
    		$('#btnCancelTradeIn').click();
    	}
    	
    	$scope.saveCancelTradeInStatus = function() {
    		$http.get('/setTradeInStatusCancel/'+$scope.tradeInStatusCancel.id+'/'+$scope.reasonToCancel)
			.success(function(data) {
				$('#tradeInCancelBtn').click();
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Status changed successfully",
				});
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
    		  console.log("...........");
			   var sDate = entity.confirmDate;
			   console.log(entity.vin);
			   console.log(sDate);
			   $http.get("/getScheduleTime/"+entity.vin+'/'+sDate).success(function(data){
				   console.log("success");
				   console.log(data);
				   $scope.cnTimeList = data;
				   $scope.timeList = data;
			   });
    	  }
    	  
    	  $scope.saveConfirmData = function() {
    		  $scope.scheduleTestData.confirmDate = $("#cnfDate").val();
    		  $scope.scheduleTestData.confirmTime = $("#timePick").val();
    		  console.log($scope.scheduleTestData);
    		  $http.post('/saveConfirmData',$scope.scheduleTestData)
    	 		.success(function(data) {
    	 			
    	 			if(data == "success"){
    	 				console.log('success');
        	 			$.pnotify({
        				    title: "Success",
        				    type:'success',
        				    text: "Saved successfully",
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
    		  $http.post('/saveToDoData',$scope.todoData)
    	 		.success(function(data) {
    	 			console.log('success');
    	 			$.pnotify({
    				    title: "Success",
    				    type:'success',
    				    text: "Saved successfully",
    				});
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
    		  $http.get('/saveCompleteTodoStatus/'+$scope.toDoId)
				.success(function(data) {
					$scope.getToDoList();
					$.pnotify({
    				    title: "Success",
    				    type:'success',
    				    text: "Status changed successfully",
    				});
					
					for(var i=0;i<$scope.toDoDateList.length;i++) {
						if($scope.toDoId == $scope.toDoDateList[i].id) {
							$scope.toDoDateList.splice(i,1);
						}
					}
			 });
    		  
    		  
    	  }
    	  
    	  $scope.saveCancelTodoStatus = function() {
    		  $http.get('/saveCancelTodoStatus/'+$scope.toDoId)
				.success(function(data) {
					$scope.getToDoList();
					$.pnotify({
    				    title: "Success",
    				    type:'success',
    				    text: "Status changed successfully",
    				});
					
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
    		   $http.get('/getWeekChartData/'+userId)
    	 		.success(function(data) {
    	 			console.log(data);
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
    		   $http.get('/getMonthChartData/'+userId)
	   	 		.success(function(data) {
	   	 			console.log(data);
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
    		   $http.get('/getThreeMonthChartData/'+userId)
	   	 		.success(function(data) {
	   	 			console.log(data);
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
    		   $http.get('/getSixMonthChartData/'+userId)
	   	 		.success(function(data) {
	   	 			console.log(data);
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
    		   $http.get('/getYearChartData/'+userId)
	   	 		.success(function(data) {
	   	 			console.log(data);
	   	 			$scope.data = data.map(function(series) {
	                     series.values = series.values.map(function(d) { return {x: d[0], y: d[1] } });
 	                     return series;
 	                 });;
	   	 		});
    	   }
    	   
    	   $scope.getRangeData = function() {
    		   console.log($('#startDate').val());
    		   console.log($('#endDate').val());
    		   console.log($scope.graphUserId);
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
    		   $http.get('/getRangeChartData/'+userId+'/'+startDate+'/'+endDate)
	   	 		.success(function(data) {
	   	 			console.log(data);
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
    		   $scope.topPerformers = false;
     		   $scope.worstPerformers = true;
     		   $scope.getPerformanceOfUser();
    	   }
    	   
    	   $scope.showWeekPerformers = function() {
    		   $('#weekPerf').css("text-decoration","underline");
    		   $('#monthPerf').css("text-decoration","none");
    		   $('#yearPerf').css("text-decoration","none");
    		   $scope.weekPerformance = true;
     		   $scope.monthPerformance = false;
     		   $scope.yearPerformance = false;
     		   $scope.getPerformanceOfUser();
    	   }
    	   
    	   $scope.showMonthPerformers = function() {
    		   $('#weekPerf').css("text-decoration","none");
    		   $('#monthPerf').css("text-decoration","underline");
    		   $('#yearPerf').css("text-decoration","none");
    		   $scope.weekPerformance = false;
     		   $scope.monthPerformance = true;
     		   $scope.yearPerformance = false;
     		   $scope.getPerformanceOfUser();
    	   }
    	   
		   $scope.showYearPerformers = function() {
			   $('#weekPerf').css("text-decoration","none");
    		   $('#monthPerf').css("text-decoration","none");
    		   $('#yearPerf').css("text-decoration","underline");
    		   $scope.weekPerformance = false;
     		   $scope.monthPerformance = false;
     		   $scope.yearPerformance = true;
     		   $scope.getPerformanceOfUser();
		   }
		  
		   $scope.showNextButton = 0;
		   $scope.userPerformanceList = {};
		   $scope.countNextValue = 0;
		   $scope.getPerformanceOfUser = function() {
			   
			   if(angular.isUndefined($scope.salesPersonUser) || $scope.salesPersonUser == "") {
				   $scope.salesPersonUser = 0;
			   }
						
						$http.get('/getPerformanceOfUser/'+$scope.topPerformers+'/'+$scope.worstPerformers+'/'+$scope.weekPerformance+'/'+$scope.monthPerformance+'/'+$scope.yearPerformance+'/'+$scope.salesPersonUser+'/'+$routeParams.LocationId)
				 		.success(function(data) {
				 			$scope.userPerformanceList = data;
				 			
				 			console.log($scope.userPerformanceList);
				 		});
			   
		   }
		   
		   
		   $scope.addNoteToRequestUser = function(entity,type) {
			   $scope.userNoteId = entity.id;
			   console.log(entity);
			   $scope.action = "";
			   console.log($scope.action);
			   if(entity.typeOfLead == "Schedule Test" || entity.typeOfLead == "Schedule Test Drive") {
				   $scope.typeOfNote = 'scheduleTest';
			   } else if(entity.typeOfLead == "Request More Info") {
				   $scope.typeOfNote = 'requestMore';
			   } else if(entity.typeOfLead == "Trade In" || entity.typeOfLead == "Trade-In Appraisal") {
				   $scope.typeOfNote = 'tradeIn';
			   } 
			   
			   $scope.userNoteList = entity.note;
			   $scope.userNote = "";
			   $http.get('/getAllAction').success(function(data) {
				   $scope.allAction = data;
		   		});
			   $('#btnUserNote').click();
		   }
		   $scope.showOtherText = 0;
		   $scope.selectOther = function(action){
			   console.log(action);
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
				   $http.get('/saveAction/'+actionValue).success(function(data) {
					   $.pnotify({
	   				    title: "Success",
	   				    type:'success',
	   				    text: "Action saved successfully",
	   				});
					   $http.get('/getAllAction').success(function(data) {
						   $scope.allAction = data;
				   		});
					   
					  
					   $scope.showOtherText = 0;
				   });
			   }
		   }
		   
		   $scope.saveUserNote = function() {
			   $http.get('/saveNoteOfUser/'+$scope.userNoteId+'/'+$scope.typeOfNote+'/'+$scope.userNote+'/'+$scope.action)
		 		.success(function(data) {
		 			$.pnotify({
    				    title: "Success",
    				    type:'success',
    				    text: "Note saved successfully",
    				});
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
			   console.log(entity);
			   
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
			   console.log("..........");
			   console.log();
			   console.log(testDriveData.bestDay);
		   }
		   
		   $scope.saveTestDrive = function() {
			   
			   $scope.testDriveData.bestDay = $('#testDriveDate').val();
			   $scope.testDriveData.bestTime = $('#bestTime').val();
			   
			   	angular.forEach($scope.testDriveData.parentChildLead,function(value,key){
			   			value.bestDay = $('#testDriveDate'+key).val();
			   			value.bestTime =  $('#bestTime'+key).val();
			   	});  
			   
			   $scope.testDriveData.prefferedContact = $("input:radio[name=preffered]:checked").val();
			   console.log($scope.testDriveData);
			   $http.post('/saveTestDrive',$scope.testDriveData)
				.success(function(data) {
					$('#clsPop').click();
					if(data == "success"){
						$scope.getAllSalesPersonRecord($scope.salesPerson);
						$('#driveClose').click();
						$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "saved successfully",
						});
						$scope.testDrive();
						$("#test-drive-tabSched").click();
						//$scope.testDrive();
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
				   document.getElementById("btn-whe-cal-toggle").innerHTML = "<i class='fa fa-calendar'></i>";
				   $(".cal-report").hide();
				   $(".wheth-report").show();
			   } else{
				   $scope.cal_whe_flag = true;
				   document.getElementById("btn-whe-cal-toggle").innerHTML = "<i class='glyphicon glyphicon-certificate'></i>";
				   $(".wheth-report").hide();
				   $(".cal-report").show();
			   }
		   };
		   
		   $http.get("/getscheduletest").success(function(data){
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
			   $scope.data1.confirmTime = $filter('date')($scope.data1.confirmTime,"HH:mm a");
			   $('#dataID').val($scope.data1.id);
			   $('#dataGoogleID').val($scope.data1.google_id);
			   $('#colored-header').modal();
		   };
		   $scope.deleteServiceType = function(serviceData){
			   console.log(serviceData);
			   $scope.appointData = serviceData;
			   $('#futureAppointmentsModal').click();
			   
		   };
		   $scope.deleteFutureAppointment = function(){
			   console.log($scope.appointData.id);
			   $http.get("/deleteAppointById/"+$scope.appointData.id).success(function(data){
				   console.log("success");
				   $http.get("/getscheduletest").success(function(data){
					   $scope.scheduleListData = data;
				   });
			   });
		   };

		   $timeout(function(){
			   $('#cnfReSchDate').on('changeDate', function(e) {
				   document.getElementById("nature-data").innerHTML = "";
				   var day = moment(e.date).format('D MMM YYYY');
				   //alert(day);
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
								img = "<i class='glyphicon glyphicon-certificate'></i>";  
						  }
						  document.getElementById("nature-data").innerHTML = img+"&nbsp;&nbsp;&nbsp;"+value.text+"&nbsp;&nbsp;&nbsp;"+value.low+"&deg;";
					  }
				   });
			   });
			   
			   $('#testDriveDate').on('changeDate', function(e) {
				   var sDate = $('#testDriveDate').val();
				   console.log($scope.testDriveData.vin);
				   console.log(sDate);
				   console.log("...........");
				   $http.get("/getScheduleTime/"+$scope.testDriveData.vin+'/'+sDate).success(function(data){
					   console.log("success");
					   console.log(data);
					   $scope.timeList = data;
				   });
				   
				   document.getElementById("testDriveNature").innerHTML = "";
				   var day = moment(e.date).format('D MMM YYYY');
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
								img = "<i class='glyphicon glyphicon-certificate'></i>";  
						  }
						  document.getElementById("testDriveNature").innerHTML = img+"&nbsp;&nbsp;&nbsp;"+value.text+"&nbsp;&nbsp;&nbsp;"+value.low+"&deg;";
					  }
				   });
			   });
			   
			   $('#cnfDate').on('changeDate', function(e) {
				   
				   console.log("...........");
				   var sDate = $('#cnfDate').val();
				   console.log($scope.scheduleTestData.vin);
				   console.log(sDate);
				   $http.get("/getScheduleTime/"+$scope.scheduleTestData.vin+'/'+sDate).success(function(data){
					   console.log("success");
					   console.log(data);
					   $scope.cnTimeList = data;
					   $scope.timeList = data;
				   });
				   
				   document.getElementById("gridCnfDateNature").innerHTML = "";
				   var day = moment(e.date).format('D MMM YYYY');
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
								img = "<i class='glyphicon glyphicon-certificate'></i>";  
						  }
						  document.getElementById("gridCnfDateNature").innerHTML = img+"&nbsp;&nbsp;&nbsp;"+value.text+"&nbsp;&nbsp;&nbsp;"+value.low+"&deg;";
					  }
				   });
			   });
			   
			   $('#cnfmeetingdate').on('changeDate', function(e) {
				   document.getElementById("meetingnature").innerHTML = "";
				   var day = moment(e.date).format('D MMM YYYY');
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
								img = "<i class='glyphicon glyphicon-certificate'></i>";  
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
								img = "<i class='glyphicon glyphicon-certificate'></i>";  
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
			 //  $scope.checkManagerLogin();
			   $('#meeting-model').modal();
		   };
		   
		  
		   $scope.getSalesPersonData = function(){
			   
   			$http.get('/getSalesUserOnly/'+$routeParams.LocationId)
	    		.success(function(data){
	    			console.log(data);
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
			   console.log(locationId);
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
			   $http.get("/getLocationPlan/"+locationId).success(function(data){
				   
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
				   
				   
				   /*	   
				   
				   console.log(data);
				   $scope.showGrid = 1;
				   $scope.gridOptionsValue.data = data;
				   console.log($scope.gridOptionsValue);
				   console.log($scope.locationdata);
				   angular.forEach($scope.locationdata, function(obj, index){
					   angular.forEach($scope.schPlan.locationList, function(obj1, index1){
						   console.log(obj.id);
						   if(obj.id == obj1){
							   obj.isSelecxzted = true;
						   }else{
							   obj.isSelected = false;
						   }
					   });
					   
				   });*/
			   });
			   
		   }
		   $scope.copyValue = function(monthValue){
			   $scope.leadsTime.totalEarning = monthValue;
		   }
		   
		   $scope.copyValueSale = function(monthValue){
			   $scope.saleleadsTime.totalBrought = monthValue;
		   }
		   
		   
		   $scope.locationTotal = 0;
		   $scope.saveLocationPlan = function(month, locationId){
			   var value = 0;
			   $scope.locationTotal = 0;
			   $scope.leadsTime.locationList  = $scope.locationList;
			   value = $scope.leadsTime.totalEarning;
			   console.log($scope.totalLocationPlanData);
			  
			   $scope.leadsTime.month = month;
			   $http.post("/saveLocationPlan",$scope.leadsTime).success(function(data){
				   console.log(data);
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
				   console.log("sccesss");
				 //  $scope.getLocationPlan();
				  /* angular.forEach($scope.totalLocationPlanData, function(obj, index){
					   console.log(obj.totalEarning);
					   if($scope.leadsTime.month == obj.month){
						   $scope.locationTotal = parseInt($scope.locationTotal) + parseInt(value);
					   }else{
						   $scope.locationTotal = parseInt($scope.locationTotal) + parseInt(obj.totalEarning);
					   }
					   console.log($scope.locationTotal);
				   });*/
				  
				   if($scope.userType == "General Manager"){
					   $scope.getLocationData(locationId);
				   }else{
					   $scope.getLocationPlan();
				   }
				   
				  
			   });
		   }
		   
		   $scope.saveLocationTotal = function(total, locationId){
			   console.log(locationId);
			   if(locationId == null){
				   locationId = 0;
			   }
			   
			   $http.get("/saveLocationTotal/"+total+"/"+locationId).success(function(data){
				   console.log(data);
				   $('#plan-model').modal("toggle");
				   $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Plan Scheduled successfully",
					});
			   });
		   }
		   
		   $scope.saveSalesTotal = function(total){
			   $http.get("/saveSalesTotal/"+total).success(function(data){
				   console.log(data);
				   $('#plan-model').modal("toggle");
				   $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Plan Scheduled successfully",
					});
			   });
		   }
		   
		   $scope.saleleadsTime = {};
		   $scope.saveSalePersonPlan = function(month){
			   $scope.salePerpleTotal = 0;
			  var value= 0;
			  
			  value = $scope.saleleadsTime.totalBrought;
			   $scope.saleleadsTime.salesList = $scope.salesList;
			   $scope.saleleadsTime.month = month;
			   console.log($scope.saleleadsTime);
			   console.log($scope.totalLocationPlanData);
			   $http.post("/saveSalePlan",$scope.saleleadsTime).success(function(data){
				   console.log(data);
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
				   console.log("sccesss");
				   /*angular.forEach($scope.totalLocationPlanData, function(obj, index){
					   $scope.salePerpleTotal = parseInt($scope.salePerpleTotal) + parseInt(obj.totalBrought);
				   });*/
				   
				   $scope.findVehicalPlan($scope.salePerId);
				   /*angular.forEach($scope.totalLocationPlanData, function(obj, index){
					   if($scope.saleleadsTime.month == obj.month){
						   $scope.salePerpleTotal = parseInt($scope.salePerpleTotal) + parseInt(value);
					   }else{
						   $scope.salePerpleTotal = parseInt($scope.salePerpleTotal) + parseInt(obj.totalBrought);
					   }
					   console.log(obj.totalBrought);
					   console.log($scope.salePerpleTotal);
				   });*/
				  
			   });
		   }
		   
		   $scope.getSalePersonData = function(salesId){
			   
			   $scope.salesIdPlan = "salePerson";
			   console.log(salesId);
			   $http.get("/getsalesPlan/"+salesId).success(function(data){
				   console.log(data);
				   $scope.showGrid = 1;
				   $scope.gridOptionsValue.data = data;
			   });
			   
		   }
		   
		   $scope.gridOptionsValue = {
	 		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		 		    paginationPageSize: 150,
	 		 		   // enableFiltering: true,
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
		   
	 		 		//&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Remove Contact" ng-click="grid.appScope.deleteContactsDetail(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i>
		   
	 		 		 $scope.salePerId = 0;
		   $scope.editPlanDetail = function(row) {
			 $scope.schedule = $scope.schPlan.scheduleBy;
			 $scope.saleperson = $scope.schPlan.salePerson;
			  console.log(row.entity);
			  $scope.schPlan = row.entity;
			  $scope.findVehicalPlan(row.entity.id);
			  $scope.schPlan.scheduleBy = $scope.schedule;
			  $scope.schPlan.salePerson = $scope.saleperson;
			  $scope.planIs = "update";
			/*   angular.forEach($scope.salesPersonPerf, function(obj, index){
				   angular.forEach($scope.schPlan.salesList, function(obj1, index1){
					   console.log(obj1);
					   if(obj.id == obj1){
						   obj.isSelected = true;
						  
					   }
				   });
				   
			   });*/
			   $scope.nextbutton = 1;
		   } 
		   $scope.saleMonthTotal = {};
		   $scope.salePerpleTotal = 0;
		   $scope.findVehicalPlan = function(saleId){
			   $scope.salePerpleTotal = 0;
			   $scope.saleMonthTotal = {};
			   $scope.salePerId = saleId;
			   $http.get("/getSaleMonthlyPlan/"+saleId).success(function(data){
				   console.log(data);
				   console.log("<><><>,,>,.,,");
				   $scope.totalLocationPlanData = data;
				   
				   angular.forEach(data, function(obj, index){
					   
					   $scope.salePerpleTotal = parseInt($scope.salePerpleTotal) + parseInt(obj.totalBrought);
					    if(obj.month == "january"){
					    	$scope.saleMonthTotal.januaryTotalEarning = obj.totalBrought;
					    }
					    if(obj.month == "february"){
					    	$scope.saleMonthTotal.februaryTotalEarning = obj.totalBrought;
					    }
					    if(obj.month == "march"){
					    	$scope.saleMonthTotal.marchTotalEarning = obj.totalBrought;
					    }
					    if(obj.month == "april"){
					    	$scope.saleMonthTotal.aprilTotalEarning = obj.totalBrought;
					    }
					    if(obj.month == "may"){
					    	$scope.saleMonthTotal.mayTotalEarning = obj.totalBrought;
					    }
					    if(obj.month == "june"){
					    	$scope.saleMonthTotal.juneTotalEarning = obj.totalBrought;
					    }
					    if(obj.month == "july"){
					    	$scope.saleMonthTotal.julyTotalEarning = obj.totalBrought;
					    }
					    if(obj.month == "august"){
					    	$scope.saleMonthTotal.augustTotalEarning = obj.totalBrought;
					    }
					    if(obj.month == "september"){
					    	$scope.saleMonthTotal.septemberTotalEarning = obj.totalBrought;
					    }
					    if(obj.month == "october"){
					    	$scope.saleMonthTotal.octoberTotalEarning = obj.totalBrought;
					    }
					    if(obj.month == "november"){
					    	$scope.saleMonthTotal.novemberTotalEarning = obj.totalBrought;
					    }
					    if(obj.month == "december"){
					    	$scope.saleMonthTotal.decemberTotalEarning = obj.totalBrought;
					    }
					    
				   });
			   });
			   
		   }
		   
		   $scope.checkDatewsie = function(){
			   var startD = $('#cnfstartDateValue').val();
			   var endD = $('#cnfendDateValue').val();
			   $scope.findMystatisData(startD,endD,$scope.dataLocOrPerWise);
		   }
		   
		   
		   $scope.openPlanning = function(){
			   $scope.schPlan = {};
			   $scope.nextbutton = 0;
			   console.log(".............");
			  // $scope.checkManagerLogin();
			   if($scope.userType != "General Manager"){
				   $scope.getLocationPlan();
			   }
			   $('#plan-model').modal();
		   };
		   
		   $scope.MonthTotal = {};
		   $scope.totalLocationPlanData = null;
		   $scope.getLocationPlan = function(){
			   $scope.locationTotal = 0;
			   $http.get("/getlocationsMonthlyPlan").success(function(data){
				   console.log(data);
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
			   $http.get("/getSalePerson/"+"january").success(function(data){
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
			   $http.get("/getSalePerson/"+"july").success(function(data){
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
				   $http.get("/getSalePerson/"+"february").success(function(data){
					  angular.forEach($scope.salesPersonPerf, function(obj, index){
						  angular.forEach(data, function(obj1, index1){
							  if(obj.id == obj1.user.id){
								  $scope.salesList.push(obj.id);
								  obj.isSelected = true;
							  }
						  });
					   });
				   });
				   
				   
			   console.log($scope.salesList);
			   console.log($scope.saleleadsTime);
			   
			   
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
			   $http.get("/getSalePerson/"+"august").success(function(data){
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
			   $http.get("/getSalePerson/"+"march").success(function(data){
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
			   $http.get("/getSalePerson/"+"september").success(function(data){
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
			   $http.get("/getSalePerson/"+"april").success(function(data){
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
			   $http.get("/getSalePerson/"+"october").success(function(data){
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
			   $http.get("/getSalePerson/"+"may").success(function(data){
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
			   $http.get("/getSalePerson/"+"november").success(function(data){
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
			   $http.get("/getSalePerson/"+"june").success(function(data){
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
			   
			   $http.get("/getSalePerson/"+"december").success(function(data){
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
			   console.log($scope.showOtherIngo);
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
		   
		   $http.get("/getlocations").success(function(data){
			   $scope.locationdata = data;
			   
			   angular.forEach($scope.locationdata, function(obj, index){
				   obj.isSelected = false;
			   });
		   });
		   $scope.locationList = [];
		   $scope.locationClicked = function(e, locationPer,value){
			/*   console.log(locationPer);
			   console.log(value);
			   var startD = $('#cnfstartdate').val();
			   var endD = $('#cnfenddate').val();
			   $http.get("/isValidCheckbok/"+locationPer.id+'/'+startD+'/'+endD).success(function(data){
				   console.log(data);
				   if(data == 1){
					   angular.forEach($scope.locationdata, function(obj, index){
						  if(obj.id == locationPer.id){
							  obj.isSelected = false;  
						  }
					   });
					   $.pnotify({
							  title: "Error",
							    type:'success',
						    text: "Plan already exists",
						});
				   }else{*/
					   if(value == false){
							$scope.locationList.push(locationPer.id);
						}else{
							$scope.deleteItem(locationPer);
						}
				  // }
				   
			   //});
			   
				
				
				console.log($scope.locationList);
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
					console.log(salesPer);
					console.log(value);
					if(value == false){
						$scope.salesList.push(salesPer.id);
					}else{
						$scope.deleteSalesItem(salesPer);
					}
					console.log($scope.salesList);
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
			  
			console.log($scope.schPlan);
			console.log($scope.planIs);
			if($scope.planIs == "save"){
				$http.post("/savePlan",$scope.schPlan).success(function(data){
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
				$http.post("/updatePlan",$scope.schPlan).success(function(data){
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
			
		   /*$scope.checkManagerLogin = function(){
			   if(angular.equals($scope.userType,"Manager")){
				   $http.get("/getloginuserinfo").success(function(data){
					  //alert(JSON.stringify(data));
					  $scope.schmeeting.location = data.location.id;
					  $http.get("/getuser/"+$scope.schmeeting.location).success(function(data){
						   $scope.user = data;
					   });
				   });
			   }
			
		   }*/
		   
		   $scope.checkDateValid = function(){
			   console.log("//...,.,.,");
			   console.log($scope.schPlan.scheduleBy);
			   console.log($scope.schPlan.location);
			   var startD = $('#cnfstartdate').val();
			  // $scope.schPlan.endDate = $('#cnfenddate').val();
			   if($scope.schPlan.scheduleBy != "salePerson"){
				   $scope.schPlan.scheduleBy = "location"; 
			   }
			   if($scope.schPlan.location == undefined){
				   $scope.schPlan.location = 0;
			   }
			   
			   if($scope.schPlan.scheduleBy == "location"){
				   
				   $http.get("/isValidDatecheck/"+$scope.schPlan.location+'/'+startD+'/'+$scope.schPlan.scheduleBy).success(function(data){
					console.log(data);
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
				   console.log($scope.schPlan.salePerson);
				   
				   $http.get("/isValidDatecheck/"+$scope.schPlan.salePerson+'/'+startD+'/'+$scope.schPlan.scheduleBy).success(function(data){
						console.log(data);
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
				   
				   
				   $http.get("/isValidDatecheck/"+$scope.schPlan.location+'/'+endD+'/'+$scope.schPlan.scheduleBy).success(function(data){
					console.log(data);
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
				   $http.get("/isValidDatecheck/"+$scope.schPlan.salePerson+'/'+endD+'/'+$scope.schPlan.scheduleBy).success(function(data){
						console.log(data);
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
			   console.log(checkAll);
			   
			   var startD = $('#cnfstartdate').val();
			   var endD = $('#cnfenddate').val();
			   $scope.locationList = [];
			   if(checkAll == false){
				   angular.forEach($scope.locationdata, function(obj, index){
					   $http.get("/isValidCheckbok/"+obj.id+'/'+startD+'/'+endD).success(function(data){
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
			   console.log($scope.locationList);
		   }
		   $scope.salesList = [];
		   $scope.checkSale = function(checkAll){
			   console.log(checkAll);
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
			   console.log($scope.salesList);
		   }
		   
		   $scope.showuser = function(location){
			   $http.get("/getmanager/"+location).success(function(data){
				   $scope.user = data;
			   });
		   };
		   
		   $scope.submitnewmeeting = function(){
			   $scope.schmeeting.bestDay = $('#cnfmeetingdate').val();
			   $scope.schmeeting.bestTime = $('#cnfmeetingtime').val();
			   $http.post("/savemeeting",$scope.schmeeting).success(function(data){
				   $('#meeting-model').modal("toggle");
				   $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Meeting Scheduled",
					});
			   }); 
		   };
  }]);