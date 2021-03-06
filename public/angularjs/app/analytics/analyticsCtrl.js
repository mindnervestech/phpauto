angular.module('newApp')
.controller('VisitorsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','$rootScope','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,$rootScope,apiserviceAnalytics) {
	
	$scope.engTimeTitle=$routeParams.engTimeTitle;
	$scope.trafficSourceTitle = $routeParams.trafficSourceTitle;
	$scope.engActionTitle = $routeParams.title;
	$scope.visitorInfos = $routeParams.visitorInfo;
	$scope.ipAddressInfo = $routeParams.ipAddressInfo;
	$scope.infoType = $routeParams.typeOfInfo;
	$scope.typeOfReferrer = $routeParams.type;
	$scope.idForDomain = $routeParams.idForDomain;
	$scope.idForRefferal = $routeParams.idForRefferal;
	$scope.locationFlag = $routeParams.flagForLocation;
	$scope.startDate1=$routeParams.startDate1;
	$scope.endDate1=$routeParams.endDate1;
	$scope.startDate2=$routeParams.startDate2;
	$scope.endDate2=$routeParams.endDate2;
	$scope.idOfLand=$routeParams.idOfLanding;
	$scope.typeOfLand=$routeParams.flagForLandingUrl;
	$scope.startDateForLand=$routeParams.startDateForLand;
	$scope.endDateForLand=$routeParams.endDateForLand;
	$scope.refDomain=$routeParams.refDomain;
	
	//$rootScope.startDateFilter = moment().subtract('days', 7).format("YYYY-MM-DD");;
	//$rootScope.endDateFilter = moment().add('days', -1).format("YYYY-MM-DD");
	   
	    setTimeout(function(){

	        $('.reportrange').daterangepicker(
	                {
	                    ranges: {
	                        'Today': [moment(), moment()],
	                        'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
	                        'Last 7 Days': [moment().subtract('days', 6), moment()],
	                        'Last 14 Days':[moment().subtract('days', 13), moment()],
	                        'Last 28 Days': [moment().subtract('days', 27), moment()],
	                        'Last 60 Days': [moment().subtract('days', 60), moment()],
	                        'Last 90 Days': [moment().subtract('days', 90), moment()],
	                        'This Month': [moment().startOf('month'), moment().endOf('month')],
	                        'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
	                    },
	                    startDate: moment().subtract('days', 6),
	                    endDate: moment()
	                },
	                function(start, end) {
	                    var startDate =  moment(start).format("MMDDYYYY");
	                    var endDate =  moment(end).format("MMDDYYYY");
	                    $rootScope.startDateFilter = moment(start).format("YYYY-MM-DD");
	                    $rootScope.endDateFilter = moment(end).format("YYYY-MM-DD");
	                    if($scope.typeOfInfo != undefined){
	                    	
	                    $scope.setShowVisitorsInfoType($scope.typeOfInfo); 
	                    
	                    }
	                    $scope.$emit('reportDateChange', { startDate: startDate, endDate: endDate });
	                    $('.reportrange span').html(start.format('MMM D, YYYY') + ' - ' + end.format('MMM D, YYYY'));
	                    $scope.$apply();
	                }
	            );
	                $rootScope.startDateFilter= moment().subtract('days', 6).format('YYYY-MM-DD');
	                $rootScope.endDateFilter = moment().format("YYYY-MM-DD");

	            $('.reportrange span').html(moment().subtract('days', 6).format('MMM D, YYYY') + ' - ' + moment().format('MMM D, YYYY'));
	            $scope.$emit('reportDateChange', { startDate: moment().subtract('days', 6).format('MMDDYYYY'), endDate:  moment().format('MMDDYYYY') });

	    }, 2000);
	
	
	
	
	
	
	
	
	
	
	
	 function initialized() {
	    	 
	    	
	      var myLatlng = new google.maps.LatLng($scope.latitude,$scope.longitude);
	      var myOptions = {
	        zoom: 8,
	        center: myLatlng,
	        mapTypeId: google.maps.MapTypeId.ROADMAP
	      }
	      var map = new google.maps.Map(document.getElementById("map-canvas"), myOptions);
	      var marker = new google.maps.Marker({
    	      position: myLatlng,
    	      map: map,
    	      visible: true
    	  });
	     
	    }

	 $scope.selectedData = function(){
		 $scope.gridOptions3.data = [];
		 apiserviceAnalytics.getVisitorList($scope.startDateFilter, $scope.endDateFilter).then(function(data){
		 
				angular.forEach(data, function(value, key) {
					var array = value.timePretty.split(',');
					var timeNew= value.timePretty.split(' ');
					var newTimePretty;
					value.timeSet = array[1];
					value.newTimePretty=timeNew[1]+" "+timeNew[2]+" "+timeNew[3]+" "+timeNew[4];
					
					value.timeTotal=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.timeTotal)), 'HH:mm:ss');
					 var splitTime   = value.timeTotal.split(":");
					 if(splitTime[0] == 00){
						 value.timeTotal = splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 else{
						 value.timeTotal = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 
					
				});
				
				angular.forEach(data,function(value, key){
					
					if($scope.engActionTitle != undefined){
						var newactions = $scope.engActionTitle.split(" ");
						if(newactions[1] == "actions" ||newactions[1] == "action"){
							if(value.actions == newactions[0]){
								$scope.gridOptions3.data.push(value);
							}
							else{
								
							}
						}
					}
					
					if($scope.engTimeTitle != undefined){
						
						var newtime = $scope.engTimeTitle.split('');
						if($scope.engTimeTitle.length == 3){
							
							timedata = value.timeTotal.split("m");
							timedata = parseInt(timedata);
							newtime = parseInt(newtime[1]);
								if(timedata < newtime){
									$scope.gridOptions3.data.push(value);
								}
						}
						if($scope.engTimeTitle.length >= 4){
							
							var newtime = $scope.engTimeTitle.split(" ");
							var timedata = value.timeTotal.split(" ");
							
							timedata1 = timedata[0].split("m");
							timedata1 = parseInt(timedata1);
							
							if(newtime[0].length == 2){
								var	newtimedata = "0"+newtime[0];
								newdata = newtimedata.split("m");
								newdata = parseInt(newdata);
									if(newdata == timedata1){
										$scope.gridOptions3.data.push(value);
									}
							}
							else{
								newtime1 = newtime[0].split("m");
								newtime2 = newtime[2].split("m");
								newtime2 = parseInt(newtime2);
								newtime1 = parseInt(newtime1);
									if(newtime1 <= timedata1 && newtime2 >= timedata1){
										$scope.gridOptions3.data.push(value);
									}
							}
						}
					
					}
					
					if($scope.trafficSourceTitle != undefined){
						if($scope.trafficSourceTitle == "Direct"){
							$scope.gridOptions3.data.push(value);
						}
						else if($scope.trafficSourceTitle == "Links"){
							$scope.gridOptions3.data.push(value);
						}
						else if($scope.trafficSourceTitle == "Searches"){
							$scope.gridOptions3.data.push(value);
						}
					}
					
					});
					
			$scope.gridOptions3.data = $filter('orderBy')($scope.gridOptions3.data,'dateClick');
			$scope.gridOptions3.data = $scope.gridOptions3.data.reverse();
			$scope.visitiorList = data;
		});
		
		
		$scope.gridOptions3.columnDefs = [
		                                 {name: 'newTimePretty', displayName: 'Date & Time', width:'12%'},
		                                 {name: 'geolocation', displayName: 'Location', width:'10%',
		                                	 cellTemplate:'<div ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.referrerTypeDataForLocation(row.entity.geolocation)">{{row.entity.geolocation}}</label></div>',
		                                 },
		                                 {name:'organization', displayName:'Internet Provider', width:'15%',
		                                	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showVisitorInfo(row.entity.id)">{{row.entity.organization}}</label></div>',
		                                 },
		                                  {name:'actions', displayName:'Actions', width:'8%',
		                                	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showVisitorInfo(row.entity.id)">{{row.entity.actions}} actions </label></div>',
		                                	 
		                                 },
		                                 {name:'timeTotal', displayName:'Time Spent', width:'10%'},
		                                 {name:'abc', displayName:'Searches & Refferals', width:'40%',
		                                	 cellTemplate:'<div ng-if="row.entity.referrerUrl != null"><span ng-click="grid.appScope.showUrlInfo(row.entity.id)" ><img src="//con.tent.network/media/icon_search.gif"></span><a href="{{row.entity.referrerUrl}}"> <img src="//con.tent.network/media/arrow.gif"></a><a class="link-domain" ng-click="grid.appScope.showUrlInfoForDomain(row.entity.id)">{{row.entity.referrerDomain}}</a>&nbsp;&nbsp;<a  class="link-domain"  ng-click="grid.appScope.showUrlInfoForRefferal(row.entity.id)">{{row.entity.referrerUrl}}</a></div>',
		                                	 
		                                },
		                                 {name:'Sear', displayName:'Page', width:'10%',
		                                	 cellTemplate:'<a  target="_blank"  href="{{row.entity.landingPage}}"><img class="mb-2" style="margin-left: 8px;width: 21px;" title="View heatmap for this page" src="https://con.tent.network/media/icon_spy.gif"></a>',
		                                 }
		                                 
		                             ];  
		 
		
		 
	 }
	 
	    function loadScript() {
	      var script = document.createElement("script");
	      script.type = "text/javascript";
	      script.src = "http://maps.google.com/maps/api/js?sensor=false&callback=initialize";
	      document.body.appendChild(script);
	    }

	
	
	
	 var date = new Date();
	 $scope.typeOfInfo = 'Visitor log';
	  var startdate= new Date(date.getFullYear(), date.getMonth(), 1);
	  if($rootScope.startDateFilter != undefined && $rootScope.endDateFilter !=undefined )
		{
		$scope.startDate=$rootScope.startDateFilter;
		$scope.endDate=$rootScope.endDateFilter;
		}
	else{
		 $rootScope.startDateFilter= moment().subtract('days', 7).format('YYYY-MM-DD ');
       $rootScope.endDateFilter = moment().format("YYYY-MM-DD");
	}
		$scope.initFunction = function(){
			$scope.DateWiseFind();
			$scope.latitude=undefined;
			$scope.longitude=undefined;
			 google.maps.event.addDomListener(window, 'load', initialized);
			 if($scope.visitorInfos != undefined){
				 apiserviceAnalytics.getVisitorData($scope.visitorInfos).then(function(data){
			
			$scope.latitude=data.latitude; 
			$scope.longitude=data.longitude;
			initialized();
			$scope.visitorInfo=data;
			$scope.visitorInfo.timeTotal=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt($scope.visitorInfo.timeTotal)), 'HH:mm:ss');
			 var splitTime   = $scope.visitorInfo.timeTotal.split(":");
			 if(splitTime[0] == 00){
				 $scope.visitorInfo.timeTotal = splitTime[1]+"m "+splitTime[2]+"s";
			 }
			 else{
				 $scope.visitorInfo.timeTotal = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
			 }
			 
			 
			 $scope.sessionId=data.sessionId;
			 if($scope.sessionId != null && $scope.sessionId != undefined){
				 
				 
				 apiserviceAnalytics.getSessionData($scope.sessionId, $scope.startDateFilter, $scope.endDateFilter).then(function(data){
				 
						
						$scope.gridOptions1.data=data;
							$scope.visitiorList = data;
						});
						
						
						$scope.gridOptions1.columnDefs = [
						                                 {name: 'newTimePretty', displayName: '', width:'15%',
						                                	 cellTemplate:'<div ><label >{{row.entity.newDate}}</label> </br>   <label ">{{row.entity.newTime}}</label> </div>',	 
						                                 },
						                                 {name: 'actionUrl', displayName: '', width:'10%',
						                                	 cellTemplate:'<div ><a   target="_blank"   href="{{row.entity.geolocation}}" >{{row.entity.geolocation}}</a> </br>   <label>{{row.entity.actionTitle}}</label> </div>',
						                                 },
						                                 {name:'organization', displayName:'Internet Provider', width:'15%',
						                                	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showVisitorInfo(row.entity.id)">{{row.entity.organization}}</label></div>',
						                                 },
						                                 {name:'actions', displayName:'Actions', width:'10%',
						                                	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showVisitorInfo(row.entity.id)">{{row.entity.actions}} actions </label></div>',
						                                	 
						                                 },
						                                 {name:'timeTotal', displayName:'Time Spent', width:'10%'},
						                                 {name:'referrerUrl', displayName:'Searches & Refferals', width:'40%',
						                                	 cellTemplate:'<div ng-if="row.entity.referrerUrl != null"><span ng-click="grid.appScope.showUrlInfo(row.entity.id)" ><img src="//con.tent.network/media/icon_search.gif"></span><a href="{{row.entity.referrerUrl}}"> <img src="//con.tent.network/media/arrow.gif"></a><a class="link-domain" ng-click="grid.appScope.showUrlInfoForDomain(row.entity.id)">{{row.entity.referrerDomain}}</a>&nbsp;&nbsp;<a  class="link-domain"  ng-click="grid.appScope.showUrlInfoForRefferal(row.entity.id)">{{row.entity.referrerUrl}}</a></div>',
						                                	 },
						                                 /*{name:'Sear', displayName:'Page', width:'40%',
						                                	 cellTemplate:'<a   target="_blank"  href="{{row.entity.landingPage}}"><img class="mb-2" style="margin-left: 8px;width: 21px;" title="View heatmap for this page" src="https://con.tent.network/media/icon_spy.gif"></a>',
						                                 }*/
						                                 
						                             ];  
						
				 
				 
			 }
			 
			
		});
			 }
			 
			 else if($scope.ipAddressInfo != undefined){
			
				apiserviceAnalytics.getIPAddress($scope.ipAddressInfo).then(function(data){
				
					$scope.latitude=data.latitude; 
					$scope.longitude=data.longitude;
					initialized();
					$scope.visitorInfo=data;
					$scope.visitorInfo.timeTotal=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt($scope.visitorInfo.timeTotal)), 'HH:mm:ss');
					 var splitTime   = $scope.visitorInfo.timeTotal.split(":");
					 if(splitTime[0] == 00){
						 $scope.visitorInfo.timeTotal = splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 else{
						 $scope.visitorInfo.timeTotal = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
					 }
				});
			}
			
			
			
			else if($scope.infoType != undefined && $scope.infoType != null  ){
				
				$scope.typeOfInfo = $scope.infoType;
				$scope.DateWiseFind();
				
			}
			 
			
			else if( $scope.trafficSourceTitle != undefined ){
	        	$scope.typeOfInfo="";	
	        	apiserviceAnalytics.getTrafficSourceData($scope.trafficSourceTitle, $rootScope.startDateFilter, $rootScope.endDateFilter).then(function(data){
	        	
					$scope.gridOptions2.data = data;
					$scope.browserObjList = data;
					
					angular.forEach($scope.gridOptions2.data, function(value, key) {
						if(value.title=="totalT"){
							value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
							 var splitTime   = value.these_visitors.split(":");
							 if(splitTime[0] == 00){
								 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 else{
								 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 
							}	
						if(value.title=="averageT"){
							value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
							 var splitTime1   = value.these_visitors.split(":");
							 if(splitTime1[0] == 00){
								 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 else{
								 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 
							}	 
							 
						if(value.title=="totalT"){
							value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
							 var splitTime   = value.all_visitors.split(":");
							 if(splitTime[0] == 00){
								 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 else{
								 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 
							 
							}	
						if(value.title=="averageT"){
							value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
							 var splitTime1   = value.all_visitors.split(":");
							 if(splitTime1[0] == 00){
								 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 else{
								 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 
							}	 
					});
					 $scope.gridOptions2.columnDefs = [
					                                   {name: 'tt', displayName: 'Summary of filtered visitors', width:'40%',
					                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>', 
					                                   },
											             {name:'these_visitors', displayName:'These Visitors', width:'20%' },
											             {name:'all_visitors', displayName:'All Visitors', width:'20%' },
											             {name:'these', displayName:'Difference', width:'20%',
											            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',
											             },
											          ]
				
				});	
	        	$scope.selectedData();
				}
		
			
			
			
			
			else if( $scope.engTimeTitle != undefined ){
		        	$scope.typeOfInfo="";	
		        	apiserviceAnalytics.getEngTimeData($scope.engTimeTitle, $scope.startDate, $scope.endDate).then(function(data){
		        	
						$scope.gridOptions2.data = data;
						$scope.browserObjList = data;
						angular.forEach($scope.gridOptions2.data, function(value, key) {
							if(value.title=="totalT"){
								value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
								 var splitTime   = value.these_visitors.split(":");
								 if(splitTime[0] == 00){
									 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
								 }
								 else{
									 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
								 }
								 
								}	
							if(value.title=="averageT"){
								value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
								 var splitTime1   = value.these_visitors.split(":");
								 if(splitTime1[0] == 00){
									 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
								 }
								 else{
									 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
								 }
								 
								}	 
								 
							if(value.title=="totalT"){
								value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
								 var splitTime   = value.all_visitors.split(":");
								 if(splitTime[0] == 00){
									 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
								 }
								 else{
									 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
								 }
								 
								 
								}	
							if(value.title=="averageT"){
								value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
								 var splitTime1   = value.all_visitors.split(":");
								 if(splitTime1[0] == 00){
									 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
								 }
								 else{
									 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
								 }
								 
								}	 
						});
						 $scope.gridOptions2.columnDefs = [
						                                   {name: 'tit', displayName: 'Summary of filtered visitors', width:'40%',
						                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',   
						                                   },
												             {name:'these_visitors', displayName:'These Visitors', width:'20%' },
												             {name:'all_visitors', displayName:'All Visitors', width:'20%' },
												             {name:'these_vis', displayName:'Difference', width:'20%',
												            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',	 
												             },
												          ]
					
					});	
		        	$scope.selectedData();
					}
					
			
			
			
			  else if( $scope.engActionTitle != undefined ){
        	$scope.typeOfInfo="";	
        	apiserviceAnalytics.getEngActionData($scope.engActionTitle, $rootScope.startDateFilter, $rootScope.endDateFilter).then(function(data){
        	
				$scope.gridOptions2.data = data;
				$scope.browserObjList = data;
				
				angular.forEach($scope.gridOptions2.data, function(value, key) {
					if(value.title=="totalT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime   = value.these_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						}	
					if(value.title=="averageT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.these_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
						 
					if(value.title=="totalT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime   = value.all_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 
						}	
					if(value.title=="averageT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.all_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
				});
				 $scope.gridOptions2.columnDefs = [
				                                   {name: 'titl', displayName: 'Summary of filtered visitors', width:'40%',
				                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',
				                                   },
										             {name:'these_visitors', displayName:'These Visitors', width:'20%' },
										             {name:'all_visitors', displayName:'All Visitors', width:'20%' },
										             {name:'differenc', displayName:'Difference', width:'20%',
										            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',
										             },
										          ]
			
			});	
        	 $scope.selectedData();
			}
			
			
			
			if($scope.typeOfReferrer != undefined && $scope.typeOfReferrer != null){
				apiserviceAnalytics.getreferrerTypeData($scope.typeOfReferrer,$scope.locationFlag, $rootScope.startDateFilter, $rootScope.endDateFilter).then(function(data){
				
				$scope.chartFlag1=false;
				$scope.gridOptions1.data = data;
				angular.forEach($scope.gridOptions1.data, function(value, key) {
					if( value.city != null && value.city != undefined  ){
						$scope.city=value.city;
					}
					if(value.title=="totalT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime   = value.these_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						}	
					if(value.title=="averageT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.these_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
						 
					if(value.title=="totalT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime   = value.all_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 
						}	
					if(value.title=="averageT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.all_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}
					
					
				});
				$scope.visitiorList = data;
				
			});
			 
			 
			 $scope.gridOptions1.columnDefs = [
												{name: 'tt', displayName: 'Summary of filtered visitors', width:'40%',
													   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>', 
												},
												  {name:'these_visitors', displayName:'These Visitors', width:'20%',
													cellTemplate:'<div><span>{{row.entity.these_visitors}}</span></div>',
												  },
												  {name:'all_visitors', displayName:'All Visitors', width:'20%',
													  cellTemplate:'<div><span>{{row.entity.all_visitors}}</span></div>',
												  },
												  {name:'these', displayName:'Difference', width:'20%',
												 	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',
												  },
									            
									         ]
			 
			 
			 if($scope.locationFlag == 'location'){
				 $scope.typeOfInfo == 'Visitor log';
					$scope.DateWiseFind();
			 }
			}
			if($scope.idForDomain != undefined && $scope.idForDomain != null){
				$scope.flagForLanding="ForDomain";
				apiserviceAnalytics.getVisitorDataForLanding($scope.idForDomain,$scope.flagForLanding, $rootScope.startDateFilter, $rootScope.endDateFilter).then(function(data){
				
				$scope.chartFlag1=false;
				$scope.gridOptions1.data = data;
				angular.forEach($scope.gridOptions1.data, function(value, key) {
					if( value.city != null && value.city != undefined  ){
						$scope.city=value.city;
					}
					if(value.title=="totalT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime   = value.these_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						}	
					if(value.title=="averageT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.these_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
						 
					if(value.title=="totalT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime   = value.all_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 
						}	
					if(value.title=="averageT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.all_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}
				});
				$scope.visitiorList = data;
				
			});
			 $scope.gridOptions1.columnDefs = [
												{name: 'tt', displayName: 'Summary of filtered visitors', width:'40%',
													   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>', 
												},
												  {name:'these_visitors', displayName:'These Visitors', width:'20%' },
												  {name:'all_visitors', displayName:'All Visitors', width:'20%' },
												  {name:'these', displayName:'Difference', width:'20%',
												 	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',
												  },
									            
									         ]
			 
			 
			 
			 
			 apiserviceAnalytics.getVisitorList($scope.startDateFilter, $scope.endDateFilter).then(function(data){
				 
				 console.log(data);
					//$scope.gridOptions2.data = data;
					angular.forEach(data, function(value, key) {
						if(value.referrerDomain != null && value.referrerDomain != "" ){
							$scope.gridOptions2.data.push(value);
						}
						
					});
					$scope.visitiorList = data;
					
				});
				
				$scope.gridOptions2.columnDefs = [
				                                 {name: 'newTimePretty', displayName: 'Date & Time', width:'12%'},
				                                 {name: 'geolocation', displayName: 'Location', width:'10%',
				                                	 cellTemplate:'<div ><label  style="color:#319DB5;cursor:pointer;"  >{{row.entity.geolocation}}</label></div>',
				                                 },
				                                 {name:'organization', displayName:'Internet Provider', width:'15%',
				                                	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  >{{row.entity.organization}}</label></div>',
				                                 },
				                                 {name:'actions', displayName:'Actions', width:'8%',
				                                	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  >{{row.entity.actions}} actions </label></div>',
				                                	 
				                                 },
				                                 {name:'timeTotal', displayName:'Time Spent', width:'10%'},
				                                 {name:'abc', displayName:'Searches & Refferals', width:'40%',
				                                	 cellTemplate:'<div ng-if="row.entity.referrerUrl != null"><span  ><img src="//con.tent.network/media/icon_search.gif"></span><a href="{{row.entity.referrerUrl}}"> <img src="//con.tent.network/media/arrow.gif"></a><a class="link-domain">{{row.entity.referrerDomain}}</a>&nbsp;&nbsp;<a  class="link-domain"  >{{row.entity.referrerUrl}}</a></div>',
				                                	 
				                                },
				                                 {name:'Sear', displayName:'Page', width:'10%',
				                                	 cellTemplate:'<a  target="_blank"  href="{{row.entity.landingPage}}"><img class="mb-2" style="margin-left: 8px;width: 21px;" title="View heatmap for this page" src="https://con.tent.network/media/icon_spy.gif"></a>',
				                                 }
				                                 
				                             ];  
				 
				
			
			
			}
			
			if($scope.idForRefferal != undefined && $scope.idForRefferal != null){
				$scope.flagForLanding="ForRefferalUrl";
				apiserviceAnalytics.getVisitorDataForLanding($scope.idForRefferal,$scope.flagForLanding, $rootScope.startDateFilter, $rootScope.endDateFilter).then(function(data){
				
				$scope.chartFlag1=false;
				$scope.gridOptions1.data = data;
				console.log(data);
				angular.forEach($scope.gridOptions1.data, function(value, key) {
					if( value.city != null && value.city != undefined  ){
						$scope.city=value.city;
						console.log($scope.city);
					}
					if(value.title=="totalT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime   = value.these_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						}	
					if(value.title=="averageT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.these_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
						 
					if(value.title=="totalT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime   = value.all_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 
						}	
					if(value.title=="averageT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.all_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}
					
					
				});
				$scope.visitiorList = data;
				
			});
			 
			 
			 $scope.gridOptions1.columnDefs = [
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
		
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	
	$scope.gridOptions1 = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	
	$scope.gridOptions2 = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	
	$scope.gridOptions3 = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	     $scope.showVisitorsInfo = function(typeOfInfo){
	    	
	    	 $location.path('/visitorsAnalytics/'+typeOfInfo);
	    	   
			} 
	
		 $scope.setShowVisitorsInfoType = function(typeOfInfo){
			  
			 $scope.flagForChart1 = true;
			 $scope.flagForChart = false;
				$scope.typeOfInfo = typeOfInfo;
				$scope.DateWiseFind();
			} 
		 
		 $scope.DateWiseFind = function(){
			 var startDate = $("#cnfstartDateValue").val();
			var endDate = $("#cnfendDateValue").val();	 
			if($scope.flagForIpAdressData == 1 && $scope.flagForIpAdressData != undefined ){
				startDate=$scope.startDate1;
				endDate=$scope.endDate1;
			}
			if($scope.locationFlag == 'location'){
				startDate=$scope.startDate2;
				endDate=$scope.endDate2;
			}
			
			if(endDate == '' || startDate == ''){
				 var startDate = $scope.startDate;
					var endDate = $scope.endDate;
			}
			if($scope.typeOfInfo == 'Visitor log'){
				$scope.tabClickFlag=1;
				apiserviceAnalytics.getVisitorList($scope.startDateFilter, $scope.endDateFilter).then(function(data){
				 
					$scope.gridOptions.data = data;
					$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'dateClick');
					$scope.gridOptions.data = $scope.gridOptions.data.reverse();
					angular.forEach($scope.gridOptions.data, function(value, key) {
						var array = value.timePretty.split(',');
						var timeNew= value.timePretty.split(' ');
						var newTimePretty;
						value.timeSet = array[1];
						value.newTimePretty=timeNew[1]+" "+timeNew[2]+" "+timeNew[3]+" "+timeNew[4];
						value.timeTotal=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.timeTotal)), 'HH:mm:ss');
						 var splitTime   = value.timeTotal.split(":");
						 if(splitTime[0] == 00){
							 value.timeTotal = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.timeTotal = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						
					});
					$scope.visitiorList = data;
				});
				
				
				$scope.gridOptions.columnDefs = [
				                                 {name: 'newTimePretty', displayName: 'Date & Time', width:'12%'},
				                                 {name: 'geolocation', displayName: 'Location', width:'10%',
				                                	 cellTemplate:'<div ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.referrerTypeDataForLocation(row.entity.geolocation)">{{row.entity.geolocation}}</label></div>',
				                                 },
				                                 {name:'organization', displayName:'Internet Provider', width:'15%',
				                                	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showVisitorInfo(row.entity.id)">{{row.entity.organization}}</label></div>',
				                                 },
				                                 {name:'actions', displayName:'Actions', width:'8%',
				                                	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showVisitorInfo(row.entity.id)">{{row.entity.actions}} actions </label></div>',
				                                	 
				                                 },
				                                 {name:'timeTotal', displayName:'Time Spent', width:'10%'},
				                                 {name:'abc', displayName:'Searches & Refferals', width:'40%',
				                                	 cellTemplate:'<div ng-if="row.entity.referrerUrl != null"><span ng-click="grid.appScope.showUrlInfo(row.entity.id)" ><img src="//con.tent.network/media/icon_search.gif"></span><a href="{{row.entity.referrerUrl}}"> <img src="//con.tent.network/media/arrow.gif"></a><a class="link-domain" ng-click="grid.appScope.showUrlInfoForDomain(row.entity.id)">{{row.entity.referrerDomain}}</a>&nbsp;&nbsp;<a  class="link-domain"  ng-click="grid.appScope.showUrlInfoForRefferal(row.entity.id)">{{row.entity.referrerUrl}}</a></div>',
				                                	 
				                                },
				                                 {name:'Sear', displayName:'Page', width:'10%',
				                                	 cellTemplate:'<a  target="_blank"  href="{{row.entity.landingPage}}"><img class="mb-2" style="margin-left: 8px;width: 21px;" title="View heatmap for this page" src="https://con.tent.network/media/icon_spy.gif"></a>',
				                                 }
				                                 
				                             ];  
				 
				
			}else if($scope.typeOfInfo == 'Action log'){
				$scope.tabClickFlag=2;
				apiserviceAnalytics.getActionListData($scope.startDateFilter,$scope.endDateFilter).then(function(data){
				 
					$scope.gridOptions.data = data;
					$scope.visitiorList = data;
					
					$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'curr_Date');
					$scope.gridOptions.data = $scope.gridOptions.data.reverse();
				});
				 
				 
				$scope.gridOptions.columnDefs = [
									             {name: 'timePretty', displayName: 'Time'},
									             {name:'a', displayName:'User',
									            	 cellTemplate:'<div><span><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showVisitorInfo(row.entity.vid)">{{row.entity.organization}}</label></span><br><a  ng-click="grid.appScope.referrerTypeDataForLocation(row.entity.geoLocation)" >{{row.entity.geoLocation}}</a>,&nbsp;&nbsp<a  ng-click="grid.appScope.referrerTypeDataForOs(row.entity.operatingSystem)" > {{row.entity.operatingSystem}}</a> <a  ng-click="grid.appScope.referrerTypeDataForBrowser(row.entity.webBrowser)" >{{row.entity.webBrowser}} </a> </div>',
									             },
									             {name:'landingPage', displayName:'Action',
									            	 cellTemplate:'<div><span><a href="{{row.entity.actionUrl}}" target="_blank">{{row.entity.actionUrl}}</a></span></div>',
									             },
									             {name: 'referrerTy', displayName: 'Referrer',
									            	 cellTemplate:'<div  ng-if="row.entity.referrerDomain != null"><span><a ng-click="grid.appScope.referrerTypeDataForDomain(row.entity.referrerDomain)"  >{{row.entity.referrerDomain}}</a>  <a   href="{{row.entity.referrerUrl}}" target="_blank" ><img src="//con.tent.network/media/arrow.gif" > </a> </span></div>',	 
									             }
									         ]
			}else if($scope.typeOfInfo == 'Engagement action'){
				$scope.tabClickFlag=3;
				apiserviceAnalytics.getEngagementAct($scope.startDateFilter,$scope.endDateFilter).then(function(data){
					$scope.gridOptions.data = data;
					$scope.visitiorList = data;
					
					
					angular.forEach($scope.gridOptions.data, function(value, key) {
						var number = value.title.split('action');
						
						if(number[0].length <= 2)
							{
								value.sortingValue = parseInt(number[0]);
							}
						else{
								var number1 = number[0].split('-');
								if(parseInt(number1) == 1){
									value.sortingValue = parseInt("10");
								}else{
									value.sortingValue = parseInt(number1);
								}
							}
					});
					
					
					$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'sortingValue');
				});
				 
				 $scope.gridOptions.columnDefs = [
										             {name: 'title', displayName: 'Actions', width:'60%',
										            	 cellTemplate:'<div ><span  ng-if="row.entity.value == 0 ">{{row.entity.title}} </span> <a ng-if="row.entity.value != 0"  ng-click="grid.appScope.getEngActionInfo(row.entity.title)">{{row.entity.title}}</a></div>', 
										             },
								             {name:'value', displayName:'Visitors', width:'10%',
								            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.newValuePrecentage.toFixed(2)}}%)</span></div>',
								             },
								             {name:'statsUrl', displayName:'', width:'20%',
								            	 cellTemplate:'<div><span><img width="{{row.entity.value}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
								             },
								             {name:'Per', displayName:'', width:'10%',		
								            	 cellTemplate:'<div  style="margin-left:47px;"><span title="{{row.entity.secondCount}}" ng-click="grid.appScope.showEngagementActionChart(row.entity.title)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
								            
								             }
										             
										            	 ]
				 
			
			}
			
			else if($scope.typeOfInfo == 'Engagement time'){
				$scope.tabClickFlag=4;
				apiserviceAnalytics.getEngagementTime($scope.startDateFilter,$scope.endDateFilter).then(function(data){
				
				$scope.gridOptions.data = data;
				$scope.visitiorList = data;
				angular.forEach($scope.gridOptions.data, function(value, key) {
					if(value.title.indexOf("&amp;lt;1m") > -1){
						value.title = "<1m";
					}
					if(value.title.indexOf("&amp;lt;10m") > -1){
						console.log(value.title);
						value.title = "<10m";
					}
					if(value.title.indexOf("&amp;gt;") > -1){
						var array = value.title.split('gt;');
						value.title = ">"+array[1];
					}
					
				});
				angular.forEach($scope.gridOptions.data, function(value, key) {
					var number = value.title.split('m');
					if(number[0].length <= 2)
						{
							value.sortingValue = parseInt(number[0]);
						}
					else{
							var number1 = number[0].split('-');
							if(parseInt(number1) == 1){
								value.sortingValue = parseInt("10");
							}else{
								value.sortingValue = parseInt(number1);
							}
						}
					
					if(value.title == '<1m'){
						value.sortingValue = parseInt("0");
					}
					else if(value.title == '<10m'){
						value.sortingValue = parseInt("10");
					}
					else if(value.title == '>60m'){
						value.sortingValue = parseInt("70");
					}
				});
				
				
				$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'sortingValue');
				
				
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'title', displayName: 'Time', width:'50%',
									            	 cellTemplate:'<div ><span  ng-if="row.entity.value == 0 ">{{row.entity.title}} </span> <a ng-if="row.entity.value != 0"  ng-click="grid.appScope.getEngActionTime(row.entity.title)">{{row.entity.title}}</a></div>',	
									             },
									             {name:'value', displayName:'Visitors', width:'10%'},
									             {name:'newValuePrecentage', displayName:'valuePercent', width:'10%',
									            	 cellTemplate:'<div  ><span > {{row.entity.newValuePrecentage.toFixed(2)}}% </span></div>',
									            	 },
									             {name:'stats_url', displayName:'', width:'20%',
									            	 cellTemplate:'<div><span><img width="{{row.entity.value}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									             },
									             {name:'url', displayName:'', width:'10%',		
									            	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showEngagementTimeChart(row.entity.title)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
									            
									             }
									             
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Traffic soures'){
				$scope.tabClickFlag=6;
				apiserviceAnalytics.getTrafficScoures($scope.startDateFilter,$scope.endDateFilter).then(function(data){
				
				$scope.gridOptions.data = data;
				$scope.visitiorList = data;
				$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
				$scope.gridOptions.data = $scope.gridOptions.data.reverse();
				angular.forEach($scope.gridOptions.data, function(value, key) {
					
					value.avgtime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.avgtime)), 'HH:mm:ss');
					value.totTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totTime)), 'HH:mm:ss');
					 var splitTime   = value.totTime.split(":");
					 var splitTime1   = value.avgtime.split(":");
					 if(splitTime[0] == 00){
						 value.totTime = splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 else{
						 value.totTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 
					 if(splitTime1[0] == 00){
						 value.avgtime = splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 else{
						 value.avgtime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 
					
				});
				
				
				
				
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'title', displayName: 'Source', width:'10%', 
									            	 cellTemplate:'<div > <a ng-click="grid.appScope.getTrafficInfo(row.entity.title)">{{row.entity.title}}</a> </div>',
									             },
									             {name:'value', displayName:'Visitors', width:'10%',
									            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.percentage.toFixed(2)}}%)</span></div>',
									             },
									             {name: 'avgaction', displayName: 'Average Actions', width:'10%',
									            	 cellTemplate:'<div><span>{{row.entity.avgaction.toFixed(2)}}</span></div>',
									             },
									             {name: 'avgtime', displayName: 'Average Time', width:'15%'},
									             {name: 'totTime', displayName: 'Total Time', width:'15%'},
									             {name: 'bounceRate', displayName: 'Bounce Rate', width:'10%'},
									             {name:'stats_url', displayName:'', width:'20%',
									            	 cellTemplate:'<div><span><img width="{{row.entity.value}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									             },
									             {name:'Percent', displayName:'', width:'10%',		
									            	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showTrafficScouresChart(row.entity.title)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
									            
									             }
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Active visitors'){
				$scope.tabClickFlag=5;
				apiserviceAnalytics.getActiveVisitors($scope.startDateFilter,$scope.endDateFilter).then(function(data){
				
				$scope.gridOptions.data = data;
				$scope.clickyList = data;
				//$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
				//$scope.gridOptions.data = $scope.gridOptions.data.reverse();
			});
			 
				$scope.gridOptions.columnDefs = [
                                               {name: 'geoLocation', displayName: 'Location', width:'20%'},
									             {name: 'title', displayName: 'Ip Address', width:'30%',
                                            	   cellTemplate:'<div><span><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showIpAddressInfo(row.entity.title)">{{row.entity.title}}</label> </span></br><span>{{row.entity.organization}} </span></div>',
									            	 },
									             {name:'value', displayName:'Visits', width:'20%',
									            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
									             },
									             {name:'stats_url', displayName:'', width:'30%',
									            	 cellTemplate:'<div><span><img width="{{row.entity.value}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									             },
									            
									         ]
				
			}
			
		 }
		 $scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		          $scope.gridOptions.data = $filter('filter')($scope.visitiorList,{'newTimePretty':grid.columns[0].filters[0].term,'geolocation':grid.columns[1].filters[0].term,'organization':grid.columns[2].filters[0].term,'actions':grid.columns[3].filters[0].term,'timeTotal':grid.columns[4].filters[0].term,'referrerUrl':grid.columns[5].filters[0].term},undefined);
		        });
	   		
  		};
		 
  		
		 $scope.flagForVisitor = false;
		 $scope.visitorInfo={};
		 $scope.showVisitorInfo = function(id) {
			 var startDate = $("#cnfstartDateValue").val();
				var endDate = $("#cnfendDateValue").val();
					$location.path('/visitorInfo/'+id+"/"+$scope.startDateFilter+"/"+$scope.endDateFilter);
		 }
		 $scope.showIpAddressInfo = function(id) {
			 var startDate = $("#cnfstartDateValue").val();
				var endDate = $("#cnfendDateValue").val();
					$location.path('/ipAddressInfo/'+id+"/"+$scope.startDateFilter+"/"+$scope.endDateFilter);
		 }
		 
		 
		 
		 $scope.getEngActionInfo = function(title) {
		  $location.path('/getEngActionInfo/'+title);
		 }
		 
		 $scope.getEngActionTime = function(title) {
		  $location.path('/getEngActionTime/'+title);
		 }
		 
		 $scope.getTrafficInfo= function(title) {
		  $location.path('/getTrafficInfo/'+title);
		 }
		 
		 
		 $scope.chartFlag1=true;
		 $scope.chartFlag=true;
		 $scope.referrerTypeData = function(type) {
			 
			 var startDate = $("#cnfstartDateValue").val();
				var endDate = $("#cnfendDateValue").val();
			 $scope.flagForLocation='other';
			 $location.path('/visitorInfoForMap/'+type+"/"+$scope.flagForLocation+"/"+$scope.startDate1+"/"+$scope.endDate1);
			 
		 }
		 
		 $scope.referrerTypeDataForLocation = function(type) {
			 var startDate = $("#cnfstartDateValue").val();
				var endDate = $("#cnfendDateValue").val();
			 $scope.flagForLocation='location';
			 $location.path('/visitorInfoForMap/'+type+"/"+$scope.flagForLocation+"/"+$scope.startDateFilter+"/"+$scope.startDateFilter);
			 
		 }
		 $scope.showUrlInfoForDomain = function(id) {
			 apiserviceAnalytics.getReferrerDomain(id).then(function(data){
			  console.log(data);
			  if(data.referrerDomain != null && data.referrerDomain !=""){
				  $scope.referrerDomain=data.referrerDomain;
			  }
			  else{
				  $scope.referrerDomain="abc";
			  }
			 
			 console.log($scope.referrerDomain);
			 
			 var startDate = $("#cnfstartDateValue").val();
				var endDate = $("#cnfendDateValue").val();
			 $scope.flagForLocation='location';
			 $location.path('/visitorInfoForDomain/'+id+"/"+$scope.flagForLocation+"/"+$scope.startDateFilter+"/"+$scope.startDateFilter+"/"+$scope.referrerDomain);
			 });
		 }
		 $scope.showUrlInfoForRefferal = function(id) {
			 var startDate = $("#cnfstartDateValue").val();
				var endDate = $("#cnfendDateValue").val();
			 $scope.flagForLocation='location';
			 $location.path('/visitorInfoForRefferal/'+id+"/"+$scope.flagForLocation+"/"+$scope.startDateFilter+"/"+$scope.startDateFilter);
			 
		 }
		 
		 $scope.referrerTypeDataForIpAdress = function(type) {
			
			 var startDate = $("#cnfstartDateValue").val();
			 var endDate = $("#cnfendDateValue").val();
			 $scope.flagForLocation='IP';
			// $location.path('/visitorInfoForMap/'+type+"/"+$scope.flagForLocation+"/"+startDate+"/"+endDate);
			 $scope.latitude=undefined;
			 $scope.longitude=undefined;
			 google.maps.event.addDomListener(window, 'load', initialized);
				
			 apiserviceAnalytics.getreferrerTypeData(type, $scope.flagForLocation, $scope.startDate1,$scope.endDate1).then(function(data){
				
					$scope.latitude=$scope.visitorInfo.latitude; 
					$scope.longitude=$scope.visitorInfo.longitude;
					initialized();
				$scope.typeOfInfo == 'Visitor log';
				$scope.DateWiseFind();
				
			});
			 
			 
		 }
		 
		 $scope.referrerTypeDataForLang = function(type) {
			 var startDate = $("#cnfstartDateValue").val();
				var endDate = $("#cnfendDateValue").val();
			 $scope.flagForLocation='language';
			 $location.path('/visitorInfoForMap/'+type+"/"+$scope.flagForLocation+"/"+$scope.startDateFilter+"/"+$scope.endDateFilter);
			 
		 }
		 
		 $scope.referrerTypeDataForOrg = function(type) {
			 var startDate = $("#cnfstartDateValue").val();
				var endDate = $("#cnfendDateValue").val();
			 $scope.flagForLocation='org';
			 $location.path('/visitorInfoForMap/'+type+"/"+$scope.flagForLocation+"/"+$scope.startDateFilter+"/"+$scope.endDateFilter);
			 
		 }
		 
		 
		 $scope.referrerTypeDataForHost = function(type) {
			 var startDate = $("#cnfstartDateValue").val();
				var endDate = $("#cnfendDateValue").val();
			 $scope.flagForLocation='host';
			 $location.path('/visitorInfoForMap/'+type+"/"+$scope.flagForLocation+"/"+$scope.startDateFilter+"/"+$scope.endDateFilter);
			 
		 }
		 
		 
		 $scope.referrerTypeDataForBrowser = function(type) {
			 var startDate = $("#cnfstartDateValue").val();
				var endDate = $("#cnfendDateValue").val();
			 $scope.flagForLocation='browser';
			 $location.path('/visitorInfoForMap/'+type+"/"+$scope.flagForLocation+"/"+$scope.startDateFilter+"/"+$scope.endDateFilter);
			 
		 }
		
		 
		 $scope.referrerTypeDataForOs = function(type) {
			 var startDate = $("#cnfstartDateValue").val();
				var endDate = $("#cnfendDateValue").val();
			 $scope.flagForLocation='os';
			 $location.path('/visitorInfoForMap/'+type+"/"+$scope.flagForLocation+"/"+$scope.startDateFilter+"/"+$scope.endDateFilter);
			 
		 }
		
		 
		 $scope.referrerTypeDataForScreen = function(type) {
			 var startDate = $("#cnfstartDateValue").val();
				var endDate = $("#cnfendDateValue").val();
			 $scope.flagForLocation='screen';
			 $location.path('/visitorInfoForMap/'+type+"/"+$scope.flagForLocation+"/"+$scope.startDateFilter+"/"+$scope.endDateFilter);
			 
		 }
		 $scope.referrerTypeDataForDomain = function(type) {
			 $scope.flagForLocation='Domain';
			 $location.path('/visitorInfoForMap/'+type+"/"+$scope.flagForLocation+"/"+$scope.startDateFilter+"/"+$scope.endDateFilter);
			 
		 }
		 
		 
		 
		 
		 
		 $scope.showUrlInfo = function(id) {
			 var startDate = $("#cnfstartDateValue").val();
			var endDate = $("#cnfendDateValue").val();	 
				$scope.flagForLanding="ForSearch";
				apiserviceAnalytics.getVisitorDataForLanding(id, $scope.startDateFilter, $scope.endDateFilter,$scope.flagForLanding).then(function(data){
				$scope.gridOptions.data = data;
				$scope.visitiorList = data;
				
			});
			 
			 
			 $scope.gridOptions.columnDefs = [
                                              {name: 'title', displayName: 'Summary of filtered visitors', width:'40%'},
									             {name: 'value', displayName: 'These visitors', width:'30%'
									             },
									            
									         ]
				
			 
		 }
		 
		
		 
		
		 
		  
		
		 $scope.urlobj={};
		 $scope.flagForChart1 = true;
		 $scope.flagForChart = true;
		 $scope.showEngagementActionChart = function(title) {
			 var startDate =$rootScope.startDateFilter;
				var endDate =$rootScope.endDateFilter;	 
				
				$scope.urlobj.startDate=startDate;
				$scope.urlobj.endDate=endDate;
				$scope.urlobj.url=title;
				$scope.flagForChart=0;
				apiserviceAnalytics.getEngActionChart($scope.urlobj).then(function(data){
					$scope.flagForChart=1;
					$scope.flagForChart1 = false;
					createChart(data);
					$scope.value = [];
					$scope.dates = [];
				
			});
				
			 
			}
		
		 
		 
		 $scope.urlobj={};
		 $scope.flagForChart1 = true;
		 $scope.flagForChart = true;
		 $scope.showEngagementTimeChart = function(title) {
			 if(title == '<10m'){
				 title='&amp;lt;10m';
			 }
			 var startDate =$rootScope.startDateFilter;
				var endDate =$rootScope.endDateFilter;	 
				
				$scope.urlobj.startDate=startDate;
				$scope.urlobj.endDate=endDate;
				$scope.urlobj.url=title;
				$scope.flagForChart=0;
				apiserviceAnalytics.getEngTimeChart($scope.urlobj).then(function(data){
					$scope.flagForChart=1;
					$scope.flagForChart1 = false;
					createChart(data);
					$scope.value = [];
					$scope.dates = [];
				
			});
				
			 
			}
		
		 
		 
		 
		 $scope.urlobj={};
		 $scope.flagForChart1 = true;
		 $scope.flagForChart = true;
		 $scope.showTrafficScouresChart = function(title) {
			 var startDate =$rootScope.startDateFilter;
				var endDate =$rootScope.endDateFilter;	 
				
				$scope.urlobj.startDate=startDate;
				$scope.urlobj.endDate=endDate;
				$scope.urlobj.url=title;
				$scope.flagForChart=0;
				apiserviceAnalytics.getTrafficChart($scope.urlobj).then(function(data){
					$scope.flagForChart=1;
					$scope.flagForChart1 = false;
					createChart(data);
					$scope.value = [];
					$scope.dates = [];
				
			});
				
			 
			}
		 
		 var seriesOptions = [];
	      var seriesCounter = 0;
	      var stockChart; 
	      var stockChart1; 
	      
	      function createChart(initdata) {
	    	  stockChart1 = 1;
	    	  stockChart = $('#functional-chart').highcharts({
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
	      
	      
		 
	
	$scope.goToActions = function() {
		$location.path('/actionsAnalytics');
	}
	
	$scope.goToVideoAnalytics = function() {
		$location.path('/goToVideoAnalytics');
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
	
	$scope.goCampaignss = function(){
		$location.path('/CampaignsAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
	$scope.goToSessionsData = function() {
		$location.path('/sessionsAnalytics');
	}
	
	$scope.goToheatMapInfo = function() {
		$location.path('/heatMapInfoAnalytics');
	}
	
	$scope.goToPlatformsInfo = function(){
		$location.path('/allPlatformsInfos');
	}
	
	$scope.goToContentInfo = function(){
		$location.path('/goToContentInfo');
	}
	
	
}]);

angular.module('newApp')
.controller('goToContentInfoCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','$rootScope','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,$rootScope,apiserviceAnalytics) {
	
	$scope.urlForMedia=$routeParams.idForMedia;
	$scope.idForGrid=$routeParams.id;
	$scope.idForEvent=$routeParams.idForEvent;
	$scope.idForPages=$routeParams.idForPages;
	$scope.idForExit=$routeParams.idForExit;
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	
	$scope.gridOptions1 = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	
	$scope.gridOptions2 = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	
	
	  setTimeout(function(){

	        $('.reportrange').daterangepicker(
	                {
	                    ranges: {
	                        'Today': [moment(), moment()],
	                        'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
	                        'Last 7 Days': [moment().subtract('days', 6), moment()],
	                        'Last 14 Days':[moment().subtract('days', 13), moment()],
	                        'Last 28 Days': [moment().subtract('days', 27), moment()],
	                        'Last 60 Days': [moment().subtract('days', 60), moment()],
	                        'Last 90 Days': [moment().subtract('days', 90), moment()],
	                        'This Month': [moment().startOf('month'), moment().endOf('month')],
	                        'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
	                    },
	                    startDate: moment().subtract('days', 6),
	                    endDate: moment()
	                },
	                function(start, end) {
	                    var startDate =  moment(start).format("MMDDYYYY");
	                    var endDate =  moment(end).format("MMDDYYYY");
	                    $rootScope.startDateFilter = moment(start).format("YYYY-MM-DD");
	                    $rootScope.endDateFilter = moment(end).format("YYYY-MM-DD");
	                    if($scope.typeOfInfo != undefined){
	                    	
	                    $scope.setShowPagesInfoType($scope.typeOfInfo); 
	                    
	                    }
	                    $scope.$emit('reportDateChange', { startDate: startDate, endDate: endDate });
	                    $('.reportrange span').html(start.format('MMM D, YYYY') + ' - ' + end.format('MMM D, YYYY'));
	                    $scope.$apply();
	                }
	            );
	                $rootScope.startDateFilter= moment().subtract('days', 6).format('YYYY-MM-DD');
	                $rootScope.endDateFilter = moment().format("YYYY-MM-DD");

	            $('.reportrange span').html(moment().subtract('days', 6).format('MMM D, YYYY') + ' - ' + moment().format('MMM D, YYYY'));
	            $scope.$emit('reportDateChange', { startDate: moment().subtract('days', 6).format('MMDDYYYY'), endDate:  moment().format('MMDDYYYY') });

	    }, 2000);
	
	
	
	 var date = new Date();
	 $scope.typeOfInfo = 'Pages';
	  var startdate= new Date(date.getFullYear(), date.getMonth(), 1);
	  

	  if($rootScope.startDateFilter != undefined && $rootScope.endDateFilter !=undefined )
		{
		$scope.startDate=$rootScope.startDateFilter;
		$scope.endDate=$rootScope.endDateFilter;
		}
	else{
		 $rootScope.startDateFilter= moment().subtract('days', 6).format('YYYY-MM-DD ');
         $rootScope.endDateFilter = moment().format("YYYY-MM-DD");
	}
		
	  $scope.initFunction = function(){
		 console.log("init");
		  $scope.startDate=$rootScope.startDateFilter;
			$scope.endDate=$rootScope.endDateFilter;
			
			$scope.DateWiseFind();
			if($scope.idForGrid != undefined){
				apiserviceAnalytics.getEntranceData($scope.idForGrid, $scope.startDate, $scope.endDate).then(function(data){
				
				$scope.gridOptions1.data = data;
				$scope.browserObjList = data;
				
				angular.forEach($scope.gridOptions1.data, function(value, key) {
					if(value.title=="totalT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime   = value.these_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						}	
					if(value.title=="averageT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.these_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
						 
					if(value.title=="totalT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime   = value.all_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 
						}	
					if(value.title=="averageT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.all_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
				});
				 $scope.gridOptions1.columnDefs = [
				                                   {name: 'ti', displayName: 'Summary of filtered visitors', width:'40%',
				                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>', 
				                                   },
										             {name:'these_visitors', displayName:'These Visitors', width:'20%' },
										             {name:'all_visitors', displayName:'All Visitors', width:'20%' },
										             {name:'these_', displayName:'Difference', width:'20%',
										            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>', 
										             },
										          ]
			
			});	
				
				$scope.contentVisitorData();	
		}
		
			if($scope.idForPages != undefined){
				apiserviceAnalytics.getPagesData($scope.idForPages, $scope.startDate, $scope.endDate).then(function(data){
				
				$scope.gridOptions1.data = data;
				$scope.pagesObjList = data;
				
				angular.forEach($scope.gridOptions1.data, function(value, key) {
					if(value.title=="totalT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime   = value.these_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						}	
					if(value.title=="averageT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.these_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
						 
					if(value.title=="totalT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime   = value.all_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 
						}	
					if(value.title=="averageT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.all_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
				});
				 $scope.gridOptions1.columnDefs = [
				                                   {name: 'titl', displayName: 'Summary of filtered visitors', width:'40%',
				                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',
				                                   },
										             {name:'these_visitors', displayName:'These Visitors', width:'20%' },
										             {name:'all_visitors', displayName:'All Visitors', width:'20%' },
										             {name:'these_vis', displayName:'Difference', width:'20%',
										            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',
										             },
										          ]
			
			});	
				$scope.contentVisitorData();	
				
		}
			
			if($scope.idForEvent != undefined){
				apiserviceAnalytics.getEventData($scope.idForEvent, $scope.startDate, $scope.endDate).then(function(data){
				
				$scope.gridOptions1.data = data;
				$scope.browserObjList = data;
				
				angular.forEach($scope.gridOptions1.data, function(value, key) {
					if(value.title=="totalT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime   = value.these_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						}	
					if(value.title=="averageT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.these_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
						 
					if(value.title=="totalT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime   = value.all_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 
						}	
					if(value.title=="averageT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.all_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
				});
				 $scope.gridOptions1.columnDefs = [
				                                   {name: 't', displayName: 'Summary of filtered visitors', width:'40%',
				                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',
				                                   },
										             {name:'these_visitors', displayName:'These Visitors', width:'20%' },
										             {name:'all_visitors', displayName:'All Visitors', width:'20%' },
										             {name:'vis', displayName:'Difference', width:'20%',
										            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',	 
										             },
										          ]
			
			});	
				$scope.contentVisitorData();
			}
			
			
			if($scope.idForExit != undefined){
				apiserviceAnalytics.getExitData($scope.idForExit, $scope.startDate, $scope.endDate).then(function(data){
				
				$scope.gridOptions1.data = data;
				$scope.browserObjList = data;
				
				angular.forEach($scope.gridOptions1.data, function(value, key) {
					if(value.title=="totalT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime   = value.these_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						}	
					if(value.title=="averageT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.these_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
						 
					if(value.title=="totalT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime   = value.all_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 
						}	
					if(value.title=="averageT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.all_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
				});
				 $scope.gridOptions1.columnDefs = [
				                                   {name: 'tt', displayName: 'Summary of filtered visitors', width:'40%',
				                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',
				                                   },
										             {name:'these_visitors', displayName:'These Visitors', width:'20%' },
										             {name:'all_visitors', displayName:'All Visitors', width:'20%' },
										             {name:'these', displayName:'Difference', width:'20%',
										            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',
										             },
										          ]
			
			});	
				$scope.contentVisitorData();
			}
			
			 $scope.gridOptions.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
				 
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			          $scope.gridOptions.data = $filter('filter')($scope.visitiorList,{'geolocation':grid.columns[0].filters[0].term,'ip_address':grid.columns[1].filters[0].term,'referrer_domain':grid.columns[2].filters[0].term,'total_visits':grid.columns[3].filters[0].term,'web_browser':grid.columns[4].filters[0].term},undefined);
			        });
		   		
	  		};
	  		
	
	  }
	
		 $scope.contentVisitorData = function(){
			 apiserviceAnalytics.getVisitorList($scope.startDateFilter, $scope.endDateFilter).then(function(data){
			
				$scope.gridOptions2.data = data;
				$scope.gridOptions2.data = $filter('orderBy')($scope.gridOptions2.data,'dateClick');
				$scope.gridOptions2.data = $scope.gridOptions2.data.reverse();
				angular.forEach($scope.gridOptions2.data, function(value, key) {
					var array = value.timePretty.split(',');
					var timeNew= value.timePretty.split(' ');
					var newTimePretty;
					value.timeSet = array[1];
					value.newTimePretty=timeNew[1]+" "+timeNew[2]+" "+timeNew[3]+" "+timeNew[4];
					//value.timeTotal = $filter('date')(value.timeTotal, 'hh:mm:ss');
					value.timeTotal=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.timeTotal)), 'HH:mm:ss');
					 var splitTime   = value.timeTotal.split(":");
					 if(splitTime[0] == 00){
						 value.timeTotal = splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 else{
						 value.timeTotal = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 
					
				});
				$scope.visitiorList = data;
			});
			
			
			$scope.gridOptions2.columnDefs = [
			                              {name: 'newTimePretty', displayName: 'Date & Time', width:'12%'},
			                              {name: 'geolocation', displayName: 'Location', width:'10%',
			                             	 cellTemplate:'<div ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.referrerTypeDataForLocation(row.entity.geolocation)">{{row.entity.geolocation}}</label></div>',
			                              },
			                              {name:'organization', displayName:'Internet Provider', width:'15%',
			                             	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showVisitorInfo(row.entity.id)">{{row.entity.organization}}</label></div>',
			                              },
			                              {name:'actions', displayName:'Actions', width:'8%',
			                             	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showVisitorInfo(row.entity.id)">{{row.entity.actions}} actions </label></div>',
			                             	 
			                              },
			                              {name:'timeTotal', displayName:'Time Spent', width:'10%'},
			                              {name:'abc', displayName:'Searches & Refferals', width:'40%',
			                             	 cellTemplate:'<div ng-if="row.entity.referrerUrl != null"><span ng-click="grid.appScope.showUrlInfo(row.entity.id)" ><img src="//con.tent.network/media/icon_search.gif"></span><a href="{{row.entity.referrerUrl}}"> <img src="//con.tent.network/media/arrow.gif"></a><a class="link-domain" ng-click="grid.appScope.showUrlInfoForDomain(row.entity.id)">{{row.entity.referrerDomain}}</a>&nbsp;&nbsp;<a  class="link-domain"  ng-click="grid.appScope.showUrlInfoForRefferal(row.entity.id)">{{row.entity.referrerUrl}}</a></div>',
			                             	 
			                             },
			                              {name:'Sear', displayName:'Page', width:'10%',
			                             	 cellTemplate:'<a  target="_blank"  href="{{row.entity.landingPage}}"><img class="mb-2" style="margin-left: 8px;width: 21px;" title="View heatmap for this page" src="https://con.tent.network/media/icon_spy.gif"></a>',
			                              }
			                              
			                          ];
			 
			 }
		
  		
	
		 $scope.setShowPagesInfoType = function(typeOfInfo){
				$scope.typeOfInfo = typeOfInfo;
				 $scope.flagForChart1 = true;
				 $scope.flagForChart = false;
				$scope.DateWiseFind();
			} 
		 
		 $scope.DateWiseFind = function(){
			 var startDate =$rootScope.startDateFilter;
			var endDate =$rootScope.endDateFilter;
			if(endDate == '' || startDate == ''){
				 var startDate = $scope.startDate;
					var endDate = $scope.endDate;
			}
			if($scope.typeOfInfo == 'Pages'){
				$scope.gridOptions.data = [];
				apiserviceAnalytics.getPagesListDale(startDate, endDate).then(function(data){
				 
					$scope.gridOptions.data = data;
					$scope.visitiorList = data;
					$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
					$scope.gridOptions.data = $scope.gridOptions.data.reverse();
					angular.forEach($scope.gridOptions.data, function(value, key) {
						
						value.averageTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.averageTime)), 'HH:mm:ss');
						value.totalTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totalTime)), 'HH:mm:ss');
						 var splitTime   = value.totalTime.split(":");
						 var splitTime1   = value.averageTime.split(":");
						 if(splitTime[0] == 00){
							 value.totalTime = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.totalTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 if(splitTime1[0] == 00){
							 value.averageTime = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.averageTime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						
					});
					
				});
				
				 $scope.gridOptions.columnDefs = [
										             {name: 'showUrl', displayName: 'Page', width:'30%',
										            	 cellTemplate:'<div><a ng-click="grid.appScope.showPagesData(row.entity.id)" >{{row.entity.showUrl}}</a><br><span>{{row.entity.title}}</span></div>'
										             },
										             {name: 'url', displayName: ' ', width:'10%',
										            	 cellTemplate:'<div><a href="{{row.entity.url}}" target="_blank"><img class="mb-2" style="margin-left: 8px;width: 21px;" title="View heatmap for this page" src="http://con.tent.network/media/icon_heatmap.png" ></a> <a href="{{row.entity.showUrl}}" target="_blank"><img class="mb-2" style="margin-left: 8px;width: 21px;" src="http://con.tent.network/media/arrow.gif" ></a> </div>'
										             },
										             {name:'value', displayName:'Views', width:'10%',
										            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
										             },
										            
										             {name: 'averageActions', displayName: 'Visitors', width:'10%'},
										             {name: 'averageTime', displayName: 'Average Time', width:'10%'},
										             {name: 'totalTime', displayName: 'Total Time', width:'10%'},
										             {name: 'bounceRate', displayName: 'Exit', width:'10%'},
										             
										             /*
										             {name:' ', displayName:'', width:'20%',
										            	 cellTemplate:'<div><span><img width="{{row.entity.value_percent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
										             },*/
										             {name:'a', displayName:'Filter Result', width:'10%',		
										            	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showPagesChart(row.entity.showUrl)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
										            
 										             }
										             
										         ]
				
			}else if($scope.typeOfInfo == 'Entrance'){
				$scope.gridOptions.data = [];
				apiserviceAnalytics.getEntranceList(startDate, endDate).then(function(data){
				 
						$scope.gridOptions.data = data;
					$scope.visitiorList = data;
					$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
					$scope.gridOptions.data = $scope.gridOptions.data.reverse();
					angular.forEach($scope.gridOptions.data, function(value, key) {
						
						value.averageTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.averageTime)), 'HH:mm:ss');
						value.totalTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totalTime)), 'HH:mm:ss');
						 var splitTime   = value.totalTime.split(":");
						 var splitTime1   = value.averageTime.split(":");
						 if(splitTime[0] == 00){
							 value.totalTime = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.totalTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 if(splitTime1[0] == 00){
							 value.averageTime = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.averageTime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						
					});
				});
				 
				 
				 $scope.gridOptions.columnDefs = [
										             {name: 'url', displayName: 'Page', width:'30%',
										            	 cellTemplate:'<div><span><a ng-click="grid.appScope.showEntranceActionData(row.entity.id)" >{{row.entity.url}}</a><br><span>{{row.entity.title}}</span></div>',
										             },
										             {name:'value', displayName:'Views', width:'10%'},
										             {name:'valuePercent', displayName:'value_Percent', width:'10%'},
										            /* {name:'statsUrl', displayName:'', width:'20%',
										            	 cellTemplate:'<div><span><img width="{{row.entity.value_percent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
										             },*/
										             {name: 'averageActions', displayName: 'Average Actions', width:'8%'},
										             {name: 'averageTime', displayName: 'Average Time', width:'8%'},
										             {name: 'totalTime', displayName: 'Total Time', width:'8%'},
										             {name: 'bounceRate', displayName: 'Exit', width:'8%'},
										             {name:'percent', displayName:'', width:'8%',		
										            	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showEntranceChart(row.entity.mainUrl)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
										            
										             }
										             
										         ]
			}else if($scope.typeOfInfo == 'Exit'){
				$scope.gridOptions.data = [];
				apiserviceAnalytics.getExit(startDate, endDate).then(function(data){
					$scope.gridOptions.data = data;
					$scope.visitiorList = data;
					$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
					$scope.gridOptions.data = $scope.gridOptions.data.reverse();
					angular.forEach($scope.gridOptions.data, function(value, key) {
						
						value.averageTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.averageTime)), 'HH:mm:ss');
						value.totalTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totalTime)), 'HH:mm:ss');
						 var splitTime   = value.totalTime.split(":");
						 var splitTime1   = value.averageTime.split(":");
						 if(splitTime[0] == 00){
							 value.totalTime = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.totalTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 if(splitTime1[0] == 00){
							 value.averageTime = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.averageTime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						
					});
					
				});
				 
				 $scope.gridOptions.columnDefs = [
										             {name:'t', displayName:'Pages', width:'60%',
										            	 cellTemplate:'<div><span><a ng-click="grid.appScope.showExitData(row.entity.id)" >{{row.entity.editedUrl}}</a><br>{{row.entity.title}}</span></div>',
										             },
										             {name:'v', displayName:'Visits', width:'10%',
										            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
										             },
										             {name:'stats_url', displayName:'', width:'20%',
										            	 cellTemplate:'<div><span><img width="{{row.entity.value}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
										             },
										             {name:'percentage', displayName:'', width:'10%',		
										            	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showExitChart(row.entity.url)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
										            
										             }
										            
										         ]
				 
			}else if($scope.typeOfInfo == 'Downloads'){
				$scope.gridOptions.data = [];
				apiserviceAnalytics.getDownloads(startDate, endDate).then(function(data){
				
				$scope.gridOptions.data = data/*[0].dates[0].items*/;
				$scope.visitiorList = data/*[0].dates[0].items*/;
				$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
				$scope.gridOptions.data = $scope.gridOptions.data.reverse();
				angular.forEach($scope.gridOptions.data, function(value, key) {
					
					value.averageTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.averageTime)), 'HH:mm:ss');
					value.totalTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totalTime)), 'HH:mm:ss');
					 var splitTime   = value.totalTime.split(":");
					 var splitTime1   = value.averageTime.split(":");
					 if(splitTime[0] == 00){
						 value.totalTime = splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 else{
						 value.totalTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 
					 if(splitTime1[0] == 00){
						 value.averageTime = splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 else{
						 value.averageTime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 
					
				});
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name:'title', displayName:'File', width:'60%',
									            	 cellTemplate:'<div><span>{{row.entity.title}}<br>{{row.entity.url}}</span></div>',
									             },
									             {name:'v', displayName:'Downloads', width:'15%',
									            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
									             },
									             {name:'statsUrl', displayName:'', width:'15%',
									            	 cellTemplate:'<div><span><img width="{{row.entity.value}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									             },
									             {name:'point', displayName:'', width:'10%',		
									            	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showDownloadsChart(row.entity.url)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
									            
									             }
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Event'){
				$scope.gridOptions.data = [];
				apiserviceAnalytics.getEvent(startDate, endDate).then(function(data){
				
				$scope.gridOptions.data = data/*[0].dates[0].items*/;
				$scope.visitiorList = data/*[0].dates[0].items*/;
				$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
				$scope.gridOptions.data = $scope.gridOptions.data.reverse();
				angular.forEach($scope.gridOptions.data, function(value, key) {
					
					value.averageTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.averageTime)), 'HH:mm:ss');
					value.totalTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totalTime)), 'HH:mm:ss');
					 var splitTime   = value.totalTime.split(":");
					 var splitTime1   = value.averageTime.split(":");
					 if(splitTime[0] == 00){
						 value.totalTime = splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 else{
						 value.totalTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 
					 if(splitTime1[0] == 00){
						 value.averageTime = splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 else{
						 value.averageTime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 
					
				});
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'url', displayName: 'Event', width:'50%',
									            	 cellTemplate:'<div><span><a ng-click="grid.appScope.showEventData(row.entity.id)">{{row.entity.url}}</a> <br>{{row.entity.title}}</span></div>',
									             },
									             {name: 'value', displayName: 'Hits', width:'20%',
									            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;&nbsp;({{row.entity.valuePercent}}%)</span></div>',
									             },
									             {name:'statsUrl', displayName:'', width:'20%',
									            	 cellTemplate:'<div><span><img width="{{row.entity.value}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									             },
									             {name:'po', displayName:'', width:'10%',		
									            	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showEventChart(row.entity.url)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
									            
									             }
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Media'){
				$scope.gridOptions.data = [];
				apiserviceAnalytics.getMediaDetails(startDate, endDate).then(function(data){
			    $scope.gridOptions.data = data;
				$scope.visitiorList = data;
				$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
				$scope.gridOptions.data = $scope.gridOptions.data.reverse();
				angular.forEach($scope.gridOptions.data, function(value, key) {
					
					value.averageTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.averageTime)), 'HH:mm:ss');
					value.totalTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totalTime)), 'HH:mm:ss');
					 var splitTime   = value.totalTime.split(":");
					 var splitTime1   = value.averageTime.split(":");
					 if(splitTime[0] == 00){
						 value.totalTime = splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 else{
						 value.totalTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 
					 if(splitTime1[0] == 00){
						 value.averageTime = splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 else{
						 value.averageTime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 
					
				});
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'url', displayName: 'Media', width:'50%',
									            	 cellTemplate: '<div><span ng-click="grid.appScope.goToMediaPages(row.entity.id)">{{row.entity.url}}</span></div>',
									             },
									             {name:'value', displayName:'Views', width:'10%'},
									             {name:'valuePercent', displayName:'value_Percent', width:'10%'},
									             {name:'statsUrl', displayName:'', width:'20%',
									            	 cellTemplate:'<div><span><img width="{{row.entity.value}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									             },
									             {name:'poi', displayName:'', width:'10%',		
									            	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showMediaChart(row.entity.url)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
									            
									             }
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Domains'){
				$scope.gridOptions.data = [];
				apiserviceAnalytics.getDomains(startDate, endDate).then(function(data){
				$scope.gridOptions.data = data;
				$scope.visitiorList = data;
				$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
				$scope.gridOptions.data = $scope.gridOptions.data.reverse();
				angular.forEach($scope.gridOptions.data, function(value, key) {
					
					value.averageTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.averageTime)), 'HH:mm:ss');
					value.totalTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totalTime)), 'HH:mm:ss');
					 var splitTime   = value.totalTime.split(":");
					 var splitTime1   = value.averageTime.split(":");
					 if(splitTime[0] == 00){
						 value.totalTime = splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 else{
						 value.totalTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 
					 if(splitTime1[0] == 00){
						 value.averageTime = splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 else{
						 value.averageTime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 
					
				});
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name:'title', displayName:'Domain', width:'50%',
									            	 cellTemplate:'<div><span>{{row.entity.title}}</span></div>',
									             },
									             {name:'v', displayName:'Hits', width:'20%',
									            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.valuePercent}}%)</span></div>',
									             },
									             {name:'statsUrl', displayName:'', width:'20%',
									            	 cellTemplate:'<div><span><img width="{{row.entity.value}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									             },
									             {name:'p', displayName:'', width:'10%',		
									            	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showDomainsChart(row.entity.title)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
									            
									             }
									            
									         ]
				
			}
			
		 }
		 
		 $scope.mediaFunction = function(){
				 var startDate = $rootScope.startDateFilter;
				 var endDate = $rootScope.endDateFilter;	 
				
				 apiserviceAnalytics.getMediaData($scope.urlForMedia, startDate, endDate).then(function(data){ 
				 
						$scope.gridOptions.data = data;
						$scope.mediaObjList = data;
						
						angular.forEach($scope.gridOptions.data, function(value, key) {
							if(value.title=="totalTime"){
								value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
								 var splitTime   = value.these_visitors.split(":");
								 if(splitTime[0] == 00){
									 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
								 }
								 else{
									 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
								 }
								 
								 
								}	
							if(value.title=="averageTime"){
								value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
								 var splitTime1   = value.these_visitors.split(":");
								 if(splitTime1[0] == 00){
									 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
								 }
								 else{
									 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
								 }
								 
								}	 
								 
							if(value.title=="totalTime"){
								value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
								 var splitTime   = value.all_visitors.split(":");
								 if(splitTime[0] == 00){
									 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
								 }
								 else{
									 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
								 }
								 
								 
								}	
							if(value.title=="averageTime"){
								value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
								 var splitTime1   = value.all_visitors.split(":");
								 if(splitTime1[0] == 00){
									 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
								 }
								 else{
									 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
								 }
								 
								}	 
								
						});
						
						 $scope.gridOptions.columnDefs = [
						                                   {name: 'ur', displayName: 'Summary of filtered visitors', width:'55%',
						                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',			
						                                   },
												             {name:'these_visitors', displayName:'These Visitors', width:'15%' },
												             {name:'all_visitors', displayName:'All Visitors', width:'15%' },
												             {name:'the_vis', displayName:'Difference', width:'15%',
												            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',
												             },
													        
													        
												            	 ]
				 });
				 }
		 
		 
		 $scope.showEntranceActionData = function(id) {
			 $location.path('/entranceGrid/'+id);
			 
			 
		 }
		 
		 $scope.showPagesData = function(id) {
			 $location.path('/pagesGrid/'+id);
			 
			 
		 }
		 
		 $scope.showEventData= function(id) {
			 $location.path('/getEventData/'+id);
			 
			 
		 }
		 
		 $scope.showExitData = function(id) {
			 $location.path('/getExitData/'+id);
			 
			 
		 }
		 
		 $scope.goToContentInfo = function(){
				
			}
		 
		 
		 $scope.urlobj={};
		 $scope.flagForChart1 = true;
		 $scope.flagForChart = true;
		 $scope.showPagesChart = function(url) {
			 var startDate =$rootScope.startDateFilter;
				var endDate =$rootScope.endDateFilter;	 
				
				
				$scope.urlobj.startDate=startDate;
				$scope.urlobj.endDate=endDate;
				$scope.urlobj.url=url;
				$scope.flagForChart=0;
				apiserviceAnalytics.getPagesChart($scope.urlobj).then(function(data){
				
					$scope.flagForChart=1;
					$scope.flagForChart1 = false;
					createChart(data);
					
					$scope.value = [];
					$scope.dates = [];
			});
			}
		 
		 $scope.urlobj={};
		 $scope.flagForChart1 = true;
		 $scope.flagForChart = true;
		 $scope.showEntranceChart = function(url) {
			 var startDate =$rootScope.startDateFilter;
				var endDate =$rootScope.endDateFilter;
				
				
				$scope.urlobj.startDate=startDate;
				$scope.urlobj.endDate=endDate;
				$scope.urlobj.url=url;
				$scope.flagForChart=0;
				apiserviceAnalytics.getEntranceChart($scope.urlobj).then(function(data){
				
					$scope.flagForChart=1;
					$scope.flagForChart1 = false;
					createChart(data);
					
					$scope.value = [];
					$scope.dates = [];
			});
			}
		 
		 $scope.urlobj={};
		 $scope.flagForChart1 = true;
		 $scope.flagForChart = true;
		 $scope.showExitChart = function(url) {
			 var startDate =$rootScope.startDateFilter;
				var endDate =$rootScope.endDateFilter;
				
				
				$scope.urlobj.startDate=startDate;
				$scope.urlobj.endDate=endDate;
				$scope.urlobj.url=url;
				$scope.flagForChart=0;
				apiserviceAnalytics.getExitChart($scope.urlobj).then(function(data){
				
					$scope.flagForChart=1;
					$scope.flagForChart1 = false;
					createChart(data);
					
					$scope.value = [];
					$scope.dates = [];
			});
			}
		 
		 
		 $scope.urlobj={};
		 $scope.flagForChart1 = true;
		 $scope.flagForChart = true;
		 $scope.showDownloadsChart = function(url) {
			 var startDate =$rootScope.startDateFilter;
				var endDate =$rootScope.endDateFilter;	 
				
				
				$scope.urlobj.startDate=startDate;
				$scope.urlobj.endDate=endDate;
				$scope.urlobj.url=url;
				$scope.flagForChart=0;
				apiserviceAnalytics.getDownloadsChart($scope.urlobj).then(function(data){
				
					$scope.flagForChart=1;
					$scope.flagForChart1 = false;
					createChart(data);
					
					$scope.value = [];
					$scope.dates = [];
			});
			}
		 
		 
		 $scope.urlobj={};
		 $scope.flagForChart1 = true;
		 $scope.flagForChart = true;
		 $scope.showEventChart = function(url) {
			 var startDate =$rootScope.startDateFilter;
				var endDate =$rootScope.endDateFilter;	 
				
				
				$scope.urlobj.startDate=startDate;
				$scope.urlobj.endDate=endDate;
				$scope.urlobj.url=url;
				$scope.flagForChart=0;
				apiserviceAnalytics.getEventChart($scope.urlobj).then(function(data){
				
					$scope.flagForChart=1;
					$scope.flagForChart1 = false;
					createChart(data);
					
					$scope.value = [];
					$scope.dates = [];
			});
			}
		 
		 $scope.urlobj={};
		 $scope.flagForChart1 = true;
		 $scope.flagForChart = true;
		 $scope.showMediaChart = function(title) {
			 var startDate =$rootScope.startDateFilter;
				var endDate =$rootScope.endDateFilter;	 
				
				
				$scope.urlobj.startDate=startDate;
				$scope.urlobj.endDate=endDate;
				$scope.urlobj.title=title;
				$scope.flagForChart=0;
				apiserviceAnalytics.getMediaChart($scope.urlobj).then(function(data){
				
					$scope.flagForChart=1;
					$scope.flagForChart1 = false;
					createChart(data);
					
					$scope.value = [];
					$scope.dates = [];
			});
			}
		 
		 $scope.urlobj={};
		 $scope.flagForChart1 = true;
		 $scope.flagForChart = true;
		 $scope.showDomainsChart = function(title) {
			 var startDate =$rootScope.startDateFilter;
				var endDate =$rootScope.endDateFilter;
				$scope.urlobj.startDate=startDate;
				$scope.urlobj.endDate=endDate;
				$scope.urlobj.title=title;
				$scope.flagForChart=0;
				apiserviceAnalytics.getshowDomainsChart($scope.urlobj).then(function(data){
				
					$scope.flagForChart=1;
					$scope.flagForChart1 = false;
					createChart(data);
					
					$scope.value = [];
					$scope.dates = [];
			});
			}
		 
		 
		 var seriesOptions = [];
		 var seriesCounter = 0;
		 var stockChart; 
		 var stockChart1; 
		 
		 function createChart(initdata) {
			  stockChart1 = 1;
			  console.log("$scope.flagForChart"+$scope.flagForChart);
			  stockChart = $('#functional-chart').highcharts({
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
		 
		 
		
		 $scope.goToVisitors = function() {
				$location.path('/visitorsAnalytics');
			}
	
		 $scope.goToVideoAnalytics = function() {
				$location.path('/goToVideoAnalytics');
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
	
	$scope.goCampaignss = function(){
		$location.path('/CampaignsAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
	$scope.goToSessionsData = function() {
		$location.path('/sessionsAnalytics');
	}
	
	$scope.goToheatMapInfo = function() {
		$location.path('/heatMapInfoAnalytics');
	}
	
	$scope.goToPlatformsInfo = function(){
		$location.path('/allPlatformsInfos');
	}
	
	$scope.goToContentInfo = function(){
		$location.path('/goToContentInfo');
	}
	
	$scope.goToMediaPages = function(id){
		$location.path('/contentInfo/'+id);
	}
	
}]);


angular.module('newApp')
.controller('VideoAnalyticsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','$rootScope','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,$rootScope,apiserviceAnalytics) {
	
	setTimeout(function(){

	        $('.reportrange').daterangepicker(
	                {
	                    ranges: {
	                        'Today': [moment(), moment()],
	                        'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
	                        'Last 7 Days': [moment().subtract('days', 7), moment()],
	                        'Last 14 Days':[moment().subtract('days', 14), moment()],
	                        'Last 28 Days': [moment().subtract('days', 28), moment()],
	                        'Last 60 Days': [moment().subtract('days', 60), moment()],
	                        'Last 90 Days': [moment().subtract('days', 90), moment()],
	                        'This Month': [moment().startOf('month'), moment().endOf('month')],
	                        'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
	                    },
	                    startDate: moment().subtract('days', 7),
	                    endDate: moment()
	                },
	                function(start, end) {
	                    var startDate =  moment(start).format("MMDDYYYY");
	                    var endDate =  moment(end).format("MMDDYYYY");
	                    $rootScope.startDateFilter = moment(start).format("YYYY-MM-DD");
	                    $rootScope.endDateFilter = moment(end).format("YYYY-MM-DD ");
	                    if($scope.typeOfInfo != undefined){
	                    	
	                    console.log($scope.typeOfInfo);	
	                    $scope.setShowVisitorsInfoType($scope.typeOfInfo); 
	                    
	                    }
	                    console.log($rootScope.startDateFilter);
	            		console.log($rootScope.endDateFilter);
	                    $scope.$emit('reportDateChange', { startDate: startDate, endDate: endDate });
	                    $('.reportrange span').html(start.format('MMM D, YYYY') + ' - ' + end.format('MMM D, YYYY'));
	                    $scope.$apply();
	                }
	            );
	                console.log(moment().subtract('days', 7).format('YYYY-MM-DD '));
	                $rootScope.startDateFilter= moment().subtract('days', 7).format('YYYY-MM-DD ');
	                $rootScope.endDateFilter = moment().format("YYYY-MM-DD");

	            $('.reportrange span').html(moment().subtract('days', 7).format('MMM D, YYYY') + ' - ' + moment().format('MMM D, YYYY'));
	            $scope.$emit('reportDateChange', { startDate: moment().subtract('days', 7).format('MMDDYYYY'), endDate:  moment().format('MMDDYYYY') });

	    }, 2000);
	  if($rootScope.startDateFilter != undefined && $rootScope.endDateFilter !=undefined )
		{
		  console.log("in if");
		$scope.startDate=$rootScope.startDateFilter;
		$scope.endDate=$rootScope.endDateFilter;
		}
	else{
		console.log("in else");
		 $rootScope.startDateFilter= moment().subtract('days', 7).format('YYYY-MM-DD ');
       $rootScope.endDateFilter = moment().format("YYYY-MM-DD");
	}
	
	
	 var date = new Date();
	// $scope.typeOfInfo = 'Pages';
	 $scope.typeOfInfo = 'video&video_meta=1';
	  var startdate= new Date(date.getFullYear(), date.getMonth(), 1);
		$scope.startDate=$rootScope.startDateFilter;
		$scope.endDate=$rootScope.endDateFilter;
	
		$scope.initFunction = function(){
			$scope.DateWiseFind();
			console.log($scope.typeOfInfo);
		}
		
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	
	
	
		 $scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		          $scope.gridOptions.data = $filter('filter')($scope.visitiorList,{'geolocation':grid.columns[0].filters[0].term,'ip_address':grid.columns[1].filters[0].term,'referrer_domain':grid.columns[2].filters[0].term,'total_visits':grid.columns[3].filters[0].term,'web_browser':grid.columns[4].filters[0].term},undefined);
		        });
	   		
  		};
  		
  		
  		$scope.setShowVisitorsInfoType = function(typeOfInfo){
			$scope.typeOfInfo = typeOfInfo;
			$scope.DateWiseFind();
		} 
		
		 $scope.DateWiseFind = function(){
			 var startDate = $rootScope.startDateFilter;
			var endDate = $rootScope.endDateFilter;	 
			console.log(endDate);
			
			if(endDate == '' || startDate == ''){
				 var startDate = $scope.startDate;
					var endDate = $scope.endDate;
			}
			
			if($scope.typeOfInfo == 'video&video_meta=1'){
				console.log(">>>>>>>");
				apiserviceAnalytics.getVideoAction(startDate, endDate).then(function(data){
				 
					$scope.gridOptions.data = data[0].dates[0].items;
					
					angular.forEach($scope.gridOptions.data, function(obj, index){
						console.log($scope.gridOptions.data[index].video_meta);
						//$scope.gridOptions.data[index].video_meta.average_time_before_pause_seek = Math.floor($scope.gridOptions.data[index].video_meta.average_time_before_pause_seek/ 1000);
						//$scope.gridOptions.data[index].video_meta.total_time_spent_watching = Math.floor($scope.gridOptions.data[index].video_meta.total_time_spent_watching/ 1000);
						
						console.log($scope.gridOptions.data[index].video_meta.total_time_spent_watching);
						
						var seconds = Math.floor($scope.gridOptions.data[index].video_meta.total_time_spent_watching / 1000);
				        var h = 3600;
				        var m = 60;
				        var hours = Math.floor(seconds/h);
				        var minutes = Math.floor( (seconds % h)/m );
				        var scnds = Math.floor( (seconds % m) );
				        var timeString = '';
				        if(scnds < 10) scnds = "0"+scnds;
				        if(hours < 10) hours = "0"+hours;
				        if(minutes < 10) minutes = "0"+minutes;
				        if(hours == "00" ){
				        	timeString =minutes +" m  "+scnds+" s ";
				        }else{
				        	timeString = hours +" h  "+ minutes +" m  "+scnds+" s ";
				        }
				        $scope.gridOptions.data[index].video_meta.total_time_spent_watching=timeString ;
				        
				        
				        var seconds = Math.floor($scope.gridOptions.data[index].video_meta.average_time_before_pause_seek / 1000);
				        var h = 3600;
				        var m = 60;
				        var hours = Math.floor(seconds/h);
				        var minutes = Math.floor( (seconds % h)/m );
				        var scnds = Math.floor( (seconds % m) );
				        var timeString = '';
				        if(scnds < 10) scnds = "0"+scnds;
				        if(hours < 10) hours = "0"+hours;
				        if(minutes < 10) minutes = "0"+minutes;
				        if(hours == "00" ){
				        	timeString =minutes +" m  "+scnds+" s ";
				        }else{
				        	timeString = hours +" h"+ minutes +" m"+scnds+"  s ";
				        }
				        
				        $scope.gridOptions.data[index].video_meta.average_time_before_pause_seek=timeString ;
				        
				        
				        var seconds = Math.floor($scope.gridOptions.data[index].video_meta.average_time_spent_watching / 1000);
				        var h = 3600;
				        var m = 60;
				        var hours = Math.floor(seconds/h);
				        var minutes = Math.floor( (seconds % h)/m );
				        var scnds = Math.floor( (seconds % m) );
				        var timeString = '';
				        if(scnds < 10) scnds = "0"+scnds;
				        if(hours < 10) hours = "0"+hours;
				        if(minutes < 10) minutes = "0"+minutes;
				        if(hours == "00" ){
				        	timeString =minutes +" m  "+scnds+" s ";
				        }else{
				        	timeString = hours +" h "+ minutes +" m "+scnds+" s ";
				        }
				        $scope.gridOptions.data[index].video_meta.average_time_spent_watching=timeString ;
				        
				        
				        if( $scope.gridOptions.data[index].video_meta.watched_entire_video_without_stopping != "0"){
				        var seconds = Math.floor($scope.gridOptions.data[index].video_meta.watched_entire_video_without_stopping / 1000);
				        var h = 3600;
				        var m = 60;
				        var hours = Math.floor(seconds/h);
				        var minutes = Math.floor( (seconds % h)/m );
				        var scnds = Math.floor( (seconds % m) );
				        var timeString = '';
				        if(scnds < 10) scnds = "0"+scnds;
				        if(hours < 10) hours = "0"+hours;
				        if(minutes < 10) minutes = "0"+minutes;
				        if(hours == "00" ){
				        	timeString =minutes +" m  "+scnds+" s ";
				        }else{
				        	timeString = hours +" h  "+ minutes +" m  "+scnds+" s ";
				        }
				        $scope.gridOptions.data[index].video_meta.watched_entire_video_without_stopping=timeString ;
				        }
				        else{
				        	var timeString = '0 Seconds ';
				        	console.log("::::::");
				        	$scope.gridOptions.data[index].video_meta.watched_entire_video_without_stopping=timeString;
				        }
				        
				        
						
						
					});
					
					console.log($scope.gridOptions.data);
					$scope.visitiorList = data[0].dates[0].items;
					
					
				});
				 
				 $scope.gridOptions.columnDefs = [
										             {name: 'title', displayName: 'Video', width:'60%',
										            	 cellTemplate:'<div> <span style="color: #337ab7;margin-top:10px;"" >{{row.entity.url}} </span> </br> <span style="color: black; margin-top:10px;" >{{row.entity.video_meta.average_time_before_pause_seek}} &nbsp;&nbsp;  </span> <label> Average time before pause seek </label></br> <span style="color: black" >{{row.entity.video_meta.average_time_spent_watching}}  &nbsp;&nbsp; </span><label>Average time spent watching</label></br> <span style="color: black">{{row.entity.video_meta.total_time_spent_watching}}  &nbsp;&nbsp;</span> <label> Total time spent watching</label></br> <span style="color: black">{{row.entity.video_meta.watched_entire_video_without_stopping}} &nbsp;&nbsp;  </span><label>Watched entire video without stopping </label> </div>',	 
										             },
										             {name:'value', displayName:'Views', width:'15%',
										            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
										             },
										             {name:'stats_url', displayName:'', width:'25%',
										            	 cellTemplate:'<div><span> {{row.entity.value}}% &nbsp;&nbsp; <img width="{{row.entity.value}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
										            	 						             },
										            
										         ]
				 
			}
			
			
			
		 }
		 
		
		 $scope.goToVisitors = function() {
				$location.path('/visitorsAnalytics');
			}
	
		 $scope.goToVideoAnalytics = function() {
				$location.path('/goToVideoAnalytics');
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
	
	$scope.goCampaignss = function(){
		$location.path('/CampaignsAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
	$scope.goToSessionsData = function() {
		$location.path('/sessionsAnalytics');
	}
	
	$scope.goToheatMapInfo = function() {
		$location.path('/heatMapInfoAnalytics');
	}
	
	$scope.goToPlatformsInfo = function(){
		$location.path('/allPlatformsInfos');
	}
	
	$scope.goToContentInfo = function(){
		$location.path('/goToContentInfo');
	}
	
	
}]);




angular.module('newApp')
.controller('allPlatformsInfoCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','$rootScope','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,$rootScope,apiserviceAnalytics) {
	
	$scope.titleForBrowser=$routeParams.idForBrowser;
	$scope.titleForOS=$routeParams.idForOS;
	$scope.titleForScreen=$routeParams.idForScreen;
	$scope.titleForHardware=$routeParams.idForHardware;
	console.log(">>>>>"+$scope.idForBrowser);
	console.log(">>>>>"+$scope.idForScreen);
	console.log(">>>>>operating id"+$scope.idForOS);
	console.log($rootScope.startDateFilter);
	console.log($rootScope.endDateFilter);
	  setTimeout(function(){

	        $('.reportrange').daterangepicker(
	                {
	                    ranges: {
	                        'Today': [moment(), moment()],
	                        'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
	                        'Last 7 Days': [moment().subtract('days', 6), moment()],
	                        'Last 14 Days':[moment().subtract('days', 13), moment()],
	                        'Last 28 Days': [moment().subtract('days', 27), moment()],
	                        'Last 60 Days': [moment().subtract('days', 60), moment()],
	                        'Last 90 Days': [moment().subtract('days', 90), moment()],
	                        'This Month': [moment().startOf('month'), moment().endOf('month')],
	                        'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
	                    },
	                    startDate: moment().subtract('days', 6),
	                    endDate: moment()
	                },
	                function(start, end) {
	                    var startDate =  moment(start).format("MMDDYYYY");
	                    var endDate =  moment(end).format("MMDDYYYY");
	                    $rootScope.startDateFilter = moment(start).format("YYYY-MM-DD");
	                    $rootScope.endDateFilter = moment(end).format("YYYY-MM-DD ");
	                    if($scope.typeOfInfo != undefined){
	                    	
	                    console.log($scope.typeOfInfo);	
	                    $scope.changeTab($scope.typeOfInfo); 
	                    
	                    }
	                    console.log($rootScope.startDateFilter);
	            		console.log($rootScope.endDateFilter);
	                    $scope.$emit('reportDateChange', { startDate: startDate, endDate: endDate });
	                    $('.reportrange span').html(start.format('MMM D, YYYY') + ' - ' + end.format('MMM D, YYYY'));
	                    $scope.$apply();
	                }
	            );
	                console.log(moment().subtract('days', 6).format('YYYY-MM-DD '));
	                $rootScope.startDateFilter= moment().subtract('days', 6).format('YYYY-MM-DD ');
	                $rootScope.endDateFilter = moment().format("YYYY-MM-DD");

	            $('.reportrange span').html(moment().subtract('days', 6).format('MMM D, YYYY') + ' - ' + moment().format('MMM D, YYYY'));
	            $scope.$emit('reportDateChange', { startDate: moment().subtract('days', 6).format('MMDDYYYY'), endDate:  moment().format('MMDDYYYY') });

	    }, 2000);
	  if($rootScope.startDateFilter != undefined && $rootScope.endDateFilter !=undefined )
		{
		  console.log("in if");
		$scope.startDate=$rootScope.startDateFilter;
		$scope.endDate=$rootScope.endDateFilter;
		}
	else{
		console.log("in else");
		 $rootScope.startDateFilter= moment().subtract('days', 6).format('YYYY-MM-DD ');
       $rootScope.endDateFilter = moment().format("YYYY-MM-DD");
	}
	
	
	
	
	
	
	var date = new Date();
	// $scope.typeOfInfo = 'Pages';
	 $scope.typeOfInfo = 'browser';
	  var startdate= new Date(date.getFullYear(), date.getMonth(), 1);
		$scope.startDate=$rootScope.startDateFilter;
		$scope.endDate=$rootScope.endDateFilter;
	
		$scope.gridOptions1 = {
		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
		 		    paginationPageSize: 150,
		 		    enableFiltering: true,
		 		    useExternalFiltering: true,
		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
		 		 };
		
		
		 $scope.gridOptions1.enableHorizontalScrollbar = 0;
			 $scope.gridOptions1.enableVerticalScrollbar = 2;
		
		
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
		                                 { name: 'key', displayName: 'Title', width:'60%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'value', displayName: 'Count', width:'40%',cellEditableCondition: false,
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
		          $scope.gridOptions.data = $filter('filter')($scope.ActionList,{'action_title':grid.columns[0].filters[0].term,'action_type':grid.columns[1].filters[0].term,'ip_address':grid.columns[2].filters[0].term,'referrer_domain':grid.columns[3].filters[0].term,'referrer_search':grid.columns[4].filters[0].term},undefined);
		        });
	   		
		};
	
	
		
	
	$scope.valueSet = {};
	$scope.typSet = [];
	
	 
	
	$scope.changeTab = function(typeOfInfo){
		$scope.typeOfInfo = typeOfInfo;
		$scope.flagForChart1 = true;
		$scope.getDateWiseFind();
	} 
 
 $scope.getDateWiseFind = function(){
	 var startDate = $rootScope.startDateFilter;
	 var endDate = $rootScope.endDateFilter;	 
	 console.log(startDate);
	 console.log(endDate);
	
	if(endDate == '' || startDate == ''){
		 var startDate = $scope.startDate;
			var endDate = $scope.endDate;
	}
	if($scope.typeOfInfo == 'browser'){
		apiserviceAnalytics.getbrowser(startDate, endDate).then(function(data){
		
				console.log(data);
			$scope.gridOptions.data = data;
			$scope.visitiorList = data;
			$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
			$scope.gridOptions.data = $scope.gridOptions.data.reverse();
			angular.forEach($scope.gridOptions.data, function(value, key) {
				
				value.averageTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.averageTime)), 'HH:mm:ss');
				value.totalTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totalTime)), 'HH:mm:ss');
				 var splitTime   = value.totalTime.split(":");
				 var splitTime1   = value.averageTime.split(":");
				 if(splitTime[0] == 00){
					 value.totalTime = splitTime[1]+"m "+splitTime[2]+"s";
				 }
				 else{
					 value.totalTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
				 }
				 
				 if(splitTime1[0] == 00){
					 value.averageTime = splitTime1[1]+"m "+splitTime1[2]+"s";
				 }
				 else{
					 value.averageTime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
				 }
				 
				
			});
		});
		 
		 $scope.gridOptions.columnDefs = [
								              {name: 'title', displayName: 'browser', width:'40%',
								            	  cellTemplate: '<div><span style="color:#319DB5;cursor:pointer;" ng-click="grid.appScope.goToBrowserpage(row.entity.title)">{{row.entity.title}}</span></div>',
								              },
								             {name:'value', displayName:'Visitors', width:'10%',
								            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.valuePercent}}%)</span></div>',
								             },
								            /* {name:'stats_url', displayName:'', width:'10%',
								            	 cellTemplate:'<div><span><img width="{{row.entity.valuePercent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
								             },*/
								             {name: 'averageActions', displayName: 'Average Actions', width:'10%'},
								             {name: 'averageTime', displayName: 'Average Time', width:'10%'},
								             {name: 'totalTime', displayName: 'Total Time', width:'10%'},
								             {name: 'bounceRate', displayName: 'Exit', width:'10%'},
								             {name:'url', displayName:'', width:'10%',		
								            	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showBrowserChart(row.entity.title)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
								            
								             }
								            	 ]
	}
	else if($scope.typeOfInfo == 'os'){
		apiserviceAnalytics.getOperatingSystem(startDate, endDate).then(function(data){
		
				console.log(data);
			$scope.gridOptions.data = data;
			$scope.visitiorList = data;
			$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
			$scope.gridOptions.data = $scope.gridOptions.data.reverse();
			angular.forEach($scope.gridOptions.data, function(value, key) {
							
							value.averageTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.averageTime)), 'HH:mm:ss');
							value.totalTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totalTime)), 'HH:mm:ss');
							 var splitTime   = value.totalTime.split(":");
							 var splitTime1   = value.averageTime.split(":");
							 if(splitTime[0] == 00){
								 value.totalTime = splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 else{
								 value.totalTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 
							 if(splitTime1[0] == 00){
								 value.averageTime = splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 else{
								 value.averageTime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 
							
				});
		});
		 
		 $scope.gridOptions.columnDefs = [
								             {name: 'titl', displayName: 'Operating System', width:'40%',
								            	 cellTemplate: '<div><span style="color:#319DB5;cursor:pointer;" ng-click="grid.appScope.goToOperatingSystempage(row.entity.title)">{{row.entity.title}}</span></div>',
								             },
								             {name:'value', displayName:'Visitors', width:'10%',
									        	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.valuePercent}}%)</span></div>',
									         },
									        /* {name:'stats_url', displayName:'', width:'10%',
									        	 cellTemplate:'<div><span><img width="{{row.entity.valuePercent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									         },*/
									         {name: 'averageActions', displayName: 'Average Actions', width:'10%'},
									         {name: 'averageTime', displayName: 'Average Time', width:'10%'},
									         {name: 'totalTime', displayName: 'Total Time', width:'10%'},
									         {name: 'bounceRate', displayName: 'Exit', width:'10%'},
									         {name:'ur', displayName:'', width:'10%',		
									        	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showOperatingSystemChart(row.entity.title)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
									        
									         }
								            	 ]
	}
	else if($scope.typeOfInfo == 'resolution'){
		apiserviceAnalytics.getScreenResolution(startDate, endDate).then(function(data){
		
				console.log(data);
			$scope.gridOptions.data = data;
			$scope.visitiorList = data;
			$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
			$scope.gridOptions.data = $scope.gridOptions.data.reverse();
			angular.forEach($scope.gridOptions.data, function(value, key) {
							
							value.averageTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.averageTime)), 'HH:mm:ss');
							value.totalTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totalTime)), 'HH:mm:ss');
							 var splitTime   = value.totalTime.split(":");
							 var splitTime1   = value.averageTime.split(":");
							 if(splitTime[0] == 00){
								 value.totalTime = splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 else{
								 value.totalTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 
							 if(splitTime1[0] == 00){
								 value.averageTime = splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 else{
								 value.averageTime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 
							
				});
		});
		 
		 $scope.gridOptions.columnDefs = [
		                                  {name: 'tit', displayName: 'Screen Resolution', width:'40%',
		                                	  cellTemplate: '<div><span style="color:#319DB5;cursor:pointer;" ng-click="grid.appScope.goToResolutionpage(row.entity.title)">{{row.entity.title}}</span></div>',
		                                  },
								             {name:'value', displayName:'Visitors', width:'10%',
									        	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.valuePercent}}%)</span></div>',
									         },
									        /* {name:'stats_url', displayName:'', width:'10%',
									        	 cellTemplate:'<div><span><img width="{{row.entity.valuePercent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									         },*/
									         {name: 'averageActions', displayName: 'Average Actions', width:'10%'},
									         {name: 'averageTime', displayName: 'Average Time', width:'10%'},
									         {name: 'totalTime', displayName: 'Total Time', width:'10%'},
									         {name: 'bounceRate', displayName: 'Exit', width:'10%'},
									         {name:'urll', displayName:'', width:'10%',		
									        	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showScreenResolutionChart(row.entity.title)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
									        
									         }
								            ]
	}
	
	else if($scope.typeOfInfo == 'hardware'){
		apiserviceAnalytics.getHardware(startDate, endDate).then(function(data){
		
				console.log(data);
			$scope.gridOptions.data = data;
			$scope.visitiorList = data;
			$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
			$scope.gridOptions.data = $scope.gridOptions.data.reverse();
			angular.forEach($scope.gridOptions.data, function(value, key) {
							
							value.averageTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.averageTime)), 'HH:mm:ss');
							value.totalTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totalTime)), 'HH:mm:ss');
							 var splitTime   = value.totalTime.split(":");
							 var splitTime1   = value.averageTime.split(":");
							 if(splitTime[0] == 00){
								 value.totalTime = splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 else{
								 value.totalTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 
							 if(splitTime1[0] == 00){
								 value.averageTime = splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 else{
								 value.averageTime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 
							
			});
		});
		 
		 $scope.gridOptions.columnDefs = [
		                                   {name: 'ti', displayName: 'Hardware', width:'40%',
		                                	   cellTemplate: '<div><span style="color:#319DB5;cursor:pointer;" ng-click="grid.appScope.goToHardwarepage(row.entity.title)">{{row.entity.title}}</span></div>',
		                                   },
								             {name:'value', displayName:'Visitors', width:'10%',
									        	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.valuePercent}}%)</span></div>',
									         },
									        /* {name:'stats_url', displayName:'', width:'10%',
									        	 cellTemplate:'<div><span><img width="{{row.entity.valuePercent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									         },*/
									         {name: 'averageActions', displayName: 'Average Actions', width:'10%'},
									         {name: 'averageTime', displayName: 'Average Time', width:'10%'},
									         {name: 'totalTime', displayName: 'Total Time', width:'10%'},
									         {name: 'bounceRate', displayName: 'Exit', width:'10%'},
									         {name:'stsurl', displayName:'', width:'10%',		
									        	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showHardwareChart(row.entity.title)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
									        
									         }
								            	 ]
	}
 }
			 $scope.platformVisitorData = function(){
				 apiserviceAnalytics.getVisitorList($scope.startDateFilter, $scope.endDateFilter).then(function(data){
			 
				$scope.gridOptions1.data = data;
				console.log($scope.gridOptions1.data);
				$scope.gridOptions1.data = $filter('orderBy')($scope.gridOptions1.data,'dateClick');
				$scope.gridOptions1.data = $scope.gridOptions1.data.reverse();
				//console.log($scope.gridOptions.data);
				//cellFilter: 'date:"yyyy-MM-dd"',enableSorting: true,
				angular.forEach($scope.gridOptions1.data, function(value, key) {
					var array = value.timePretty.split(',');
					var timeNew= value.timePretty.split(' ');
					var newTimePretty;
					value.timeSet = array[1];
					value.newTimePretty=timeNew[1]+" "+timeNew[2]+" "+timeNew[3]+" "+timeNew[4];
					//value.timeTotal = $filter('date')(value.timeTotal, 'hh:mm:ss');
					value.timeTotal=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.timeTotal)), 'HH:mm:ss');
					 var splitTime   = value.timeTotal.split(":");
					 if(splitTime[0] == 00){
						 value.timeTotal = splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 else{
						 value.timeTotal = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 
					
				});
				 console.log($scope.gridOptions1.data);
				$scope.visitiorList = data;
			});
			
			
			$scope.gridOptions1.columnDefs = [
			                              {name: 'newTimePretty', displayName: 'Date & Time', width:'12%'},
			                              {name: 'geolocation', displayName: 'Location', width:'10%',
			                             	 cellTemplate:'<div ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.referrerTypeDataForLocation(row.entity.geolocation)">{{row.entity.geolocation}}</label></div>',
			                              },
			                              {name:'organization', displayName:'Internet Provider', width:'15%',
			                             	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showVisitorInfo(row.entity.id)">{{row.entity.organization}}</label></div>',
			                              },
			                              {name:'actions', displayName:'Actions', width:'8%',
			                             	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showVisitorInfo(row.entity.id)">{{row.entity.actions}} actions </label></div>',
			                             	 
			                              },
			                              {name:'timeTotal', displayName:'Time Spent', width:'10%'},
			                              {name:'abc', displayName:'Searches & Refferals', width:'40%',
			                             	 cellTemplate:'<div ng-if="row.entity.referrerUrl != null"><span ng-click="grid.appScope.showUrlInfo(row.entity.id)" ><img src="//con.tent.network/media/icon_search.gif"></span><a href="{{row.entity.referrerUrl}}"> <img src="//con.tent.network/media/arrow.gif"></a><a class="link-domain" ng-click="grid.appScope.showUrlInfoForDomain(row.entity.id)">{{row.entity.referrerDomain}}</a>&nbsp;&nbsp;<a  class="link-domain"  ng-click="grid.appScope.showUrlInfoForRefferal(row.entity.id)">{{row.entity.referrerUrl}}</a></div>',
			                             	 
			                             },
			                              {name:'Sear', displayName:'Page', width:'10%',
			                             	 cellTemplate:'<a  target="_blank"  href="{{row.entity.landingPage}}"><img class="mb-2" style="margin-left: 8px;width: 21px;" title="View heatmap for this page" src="https://con.tent.network/media/icon_spy.gif"></a>',
			                              }
			                              
			                          ];
			 
			 }
	$scope.initFunction = function(){
		console.log("in init function");
		 var startDate = $rootScope.startDateFilter;
		 var endDate = $rootScope.endDateFilter;	 
		 console.log(startDate);
		 console.log(endDate);
		 if($scope.titleForBrowser != undefined){
			 apiserviceAnalytics.getbrowserdata($scope.titleForBrowser,startDate, endDate).then(function(data){ 
		 
			 console.log(data);
				$scope.gridOptions.data = data;
				$scope.browserObjList = data;
				
				angular.forEach($scope.gridOptions.data, function(value, key) {
					if(value.title=="totalT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime   = value.these_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 
						}	
					if(value.title=="averageT"){
						value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.these_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
						 
					if(value.title=="totalT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime   = value.all_visitors.split(":");
						 if(splitTime[0] == 00){
							 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 
						}	
					if(value.title=="averageT"){
						value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
						 var splitTime1   = value.all_visitors.split(":");
						 if(splitTime1[0] == 00){
							 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						}	 
						
				});
				
				 $scope.gridOptions.columnDefs = [
				                                   {name: 'title', displayName: 'Summary of filtered visitors', width:'55%',
				                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',			
				                                   },
										             {name:'these_visitors', displayName:'These Visitors', width:'15%' },
										             {name:'all_visitors', displayName:'All Visitors', width:'15%' },
										             {name:'these_vis', displayName:'Difference', width:'15%',
										            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>', 
										             },
											        
											        
										            	 ]
		 });
		 $scope.platformVisitorData();
		 }
		 else if($scope.titleForOS != undefined){
			 apiserviceAnalytics.getOperatingSystemdata($scope.titleForOS,startDate, endDate).then(function(data){
			 
				 console.log(data);
					$scope.gridOptions.data = data;
					$scope.operatingObjList = data;
					
					angular.forEach($scope.gridOptions.data, function(value, key) {
						if(value.title=="totalT"){
							value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
							 var splitTime   = value.these_visitors.split(":");
							 if(splitTime[0] == 00){
								 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 else{
								 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 
							 
							}	
						if(value.title=="averageT"){
							value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
							 var splitTime1   = value.these_visitors.split(":");
							 if(splitTime1[0] == 00){
								 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 else{
								 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 
							}	 
							 
						if(value.title=="totalT"){
							value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
							 var splitTime   = value.all_visitors.split(":");
							 if(splitTime[0] == 00){
								 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 else{
								 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 
							 
							}	
						if(value.title=="averageT"){
							value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
							 var splitTime1   = value.all_visitors.split(":");
							 if(splitTime1[0] == 00){
								 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 else{
								 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 
							}	 
							
							
						
							
						
					});
					
					
					
					
					 $scope.gridOptions.columnDefs = [
					                                   {name: 'title', displayName: 'Summary of filtered visitors', width:'55%',
					                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',
					                                   },
											             {name:'these_visitors', displayName:'These Visitors', width:'15%' },
											             {name:'all_visitors', displayName:'All Visitors', width:'15%' },
											             {name:'these', displayName:'Difference', width:'15%',
											            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',	 
											             },
												        
												        
											            	 ]
			 });
			 $scope.platformVisitorData();
			 }
		 
		 else if($scope.titleForScreen != undefined){
			 apiserviceAnalytics.getResolutiondata($scope.titleForScreen,startDate, endDate).then(function(data){
			 
				 console.log(data);
					$scope.gridOptions.data = data;
					$scope.screenObjList = data;
					
					angular.forEach($scope.gridOptions.data, function(value, key) {
						if(value.title=="totalT"){
							value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
							 var splitTime   = value.these_visitors.split(":");
							 if(splitTime[0] == 00){
								 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 else{
								 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 
							 
							}	
						if(value.title=="averageT"){
							value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
							 var splitTime1   = value.these_visitors.split(":");
							 if(splitTime1[0] == 00){
								 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 else{
								 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 
							}	 
							 
						if(value.title=="totalT"){
							value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
							 var splitTime   = value.all_visitors.split(":");
							 if(splitTime[0] == 00){
								 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 else{
								 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 
							 
							}	
						if(value.title=="averageT"){
							value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
							 var splitTime1   = value.all_visitors.split(":");
							 if(splitTime1[0] == 00){
								 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 else{
								 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 
							}	 
							
							
						
							
						
					});
					
					
					
					
					 $scope.gridOptions.columnDefs = [
					                                   {name: 'title', displayName: 'Summary of filtered visitors', width:'55%',
					                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',   
					                                   },
											             {name:'these_visitors', displayName:'These Visitors', width:'15%' },
											             {name:'all_visitors', displayName:'All Visitors', width:'15%' },
											             {name:'these_v', displayName:'Difference', width:'15%',
											            	 
											            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',},
												        
												        
											            	 ]
			 });
			 $scope.platformVisitorData();
			 }
		  
		 else if($scope.titleForHardware != undefined){
			 apiserviceAnalytics.getHardwaredata($scope.titleForHardware,startDate, endDate).then(function(data){
			 
				 console.log(data);
					$scope.gridOptions.data = data;
					$scope.hardwareObjList = data;
					
					angular.forEach($scope.gridOptions.data, function(value, key) {
						if(value.title=="totalT"){
							value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
							 var splitTime   = value.these_visitors.split(":");
							 if(splitTime[0] == 00){
								 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 else{
								 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 
							 
							}	
						if(value.title=="averageT"){
							value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
							 var splitTime1   = value.these_visitors.split(":");
							 if(splitTime1[0] == 00){
								 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 else{
								 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 
							}	 
							 
						if(value.title=="totalT"){
							value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
							 var splitTime   = value.all_visitors.split(":");
							 if(splitTime[0] == 00){
								 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 else{
								 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 
							 
							}	
						if(value.title=="averageT"){
							value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
							 var splitTime1   = value.all_visitors.split(":");
							 if(splitTime1[0] == 00){
								 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 else{
								 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 
							}	 
							
							
						
							
						
					});
					
					
					
					
					 $scope.gridOptions.columnDefs = [
					                                   {name: 'title', displayName: 'Summary of filtered visitors', width:'55%',
					                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',   
					                                   },
											             {name:'these_visitors', displayName:'These Visitors', width:'15%' },
											             {name:'all_visitors', displayName:'All Visitors', width:'15%' },
											             {name:'diff', displayName:'Difference', width:'15%',
											            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',	 
											             },
												        
												        
											            	 ]
			 });
			 $scope.platformVisitorData();
			 }
		 
		 
	}

	
 
 
 

 $scope.urlobj={};
 $scope.flagForChart1 = true;
 $scope.flagForChart = true;
 $scope.showBrowserChart = function(title) {
	 console.log(">>>>>>>>");
	 var startDate =$rootScope.startDateFilter;
		var endDate =$rootScope.endDateFilter;
		
		
		$scope.urlobj.startDate=startDate;
		$scope.urlobj.endDate=endDate;
		$scope.urlobj.title=title;
		$scope.flagForChart=0;
		apiserviceAnalytics.getBrowserChart($scope.urlobj).then(function(data){
		
			console.log(data);
			$scope.flagForChart=1;
			$scope.flagForChart1 = false;
			createChart(data);
			
			$scope.value = [];
			$scope.dates = [];
	});
	}
 
 
 $scope.urlobj={};
 $scope.flagForChart1 = true;
 $scope.flagForChart = true;
 $scope.showOperatingSystemChart = function(title) {
	 console.log(">>>>>>>>");
	 var startDate =$rootScope.startDateFilter;
		var endDate =$rootScope.endDateFilter;
		
		
		$scope.urlobj.startDate=startDate;
		$scope.urlobj.endDate=endDate;
		$scope.urlobj.title=title;
		$scope.flagForChart=0;
		apiserviceAnalytics.getshowOperatingSystemChart($scope.urlobj).then(function(data){
		
			console.log(data);
			$scope.flagForChart=1;
			$scope.flagForChart1 = false;
			createChart(data);
			
			$scope.value = [];
			$scope.dates = [];
	});
	}
 
 $scope.urlobj={};
 $scope.flagForChart1 = true;
 $scope.flagForChart = true;
 $scope.showScreenResolutionChart = function(title) {
	 console.log(">>>>>>>>");
	 var startDate =$rootScope.startDateFilter;
		var endDate =$rootScope.endDateFilter;
		
		
		$scope.urlobj.startDate=startDate;
		$scope.urlobj.endDate=endDate;
		$scope.urlobj.title=title;
		$scope.flagForChart=0;
		apiserviceAnalytics.getScreenResolutionChart($scope.urlobj).then(function(data){
		
			console.log(data);
			$scope.flagForChart=1;
			$scope.flagForChart1 = false;
			createChart(data);
			
			$scope.value = [];
			$scope.dates = [];
	});
	}
 
 $scope.urlobj={};
 $scope.flagForChart1 = true;
 $scope.flagForChart = true;
 $scope.showHardwareChart = function(title) {
	 console.log(">>>>>>>>");
	 var startDate =$rootScope.startDateFilter;
		var endDate =$rootScope.endDateFilter;
		
		
		$scope.urlobj.startDate=startDate;
		$scope.urlobj.endDate=endDate;
		$scope.urlobj.title=title;
		$scope.flagForChart=0;
		apiserviceAnalytics.getHardwareChart($scope.urlobj).then(function(data){
		
			console.log(data);
			$scope.flagForChart=1;
			$scope.flagForChart1 = false;
			createChart(data);
			
			$scope.value = [];
			$scope.dates = [];
	});
	}

 
 
 var seriesOptions = [];
 var seriesCounter = 0;
 var stockChart; 
 var stockChart1; 
 
 function createChart(initdata) {
	  stockChart1 = 1;
	  console.log("$scope.flagForChart"+$scope.flagForChart);
	  stockChart = $('#functional-chart').highcharts({
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
 
 $scope.goToContentInfo = function(){
		$location.path('/goToContentInfo');
	}
 
 $scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	 $scope.goToVideoAnalytics = function() {
			$location.path('/goToVideoAnalytics');
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
	
	$scope.goCampaignss = function(){
		$location.path('/CampaignsAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
	$scope.goToSessionsData = function() {
		$location.path('/sessionsAnalytics');
	}
	
	$scope.goToheatMapInfo = function() {
		$location.path('/heatMapInfoAnalytics');
	}
	
	$scope.goToVehicleInfo = function(){
		$location.path('/allVehicleSessions');
	}
	
	$scope.goToBrowserpage = function(id){
		console.log("id  is"+id);
		$location.path('/platformInfo/'+id);
	}
	
	$scope.goToOperatingSystempage = function(id){
		console.log("id  is"+id);
		$location.path('/operatingSystemInfo/'+id);
	}
	$scope.goToResolutionpage = function(id){
		console.log("id  is screen"+id);
		$location.path('/ResolutionInfo/'+id);
	}
	$scope.goToHardwarepage = function(id){
		console.log("id  is hardware"+id);
		$location.path('/HardwareInfo/'+id);
	}
	
	/*$scope.changeTab = function(type){
		$scope.gridOptions.data = [];
		$scope.typSet = [];
		if(type == "os"){
			$.each($scope.operatingSystem, function(key, value) {
	            $scope.valueSet = {};
	            $scope.valueSet.key = key;
	            $scope.valueSet.value = value;
	            $scope.typSet.push($scope.valueSet);
	            
			});
			$scope.labelSet = "OS";
			$scope.gridOptions.data = $scope.typSet;
		}
		if(type == "browser"){
			$.each($scope.webBrosmap, function(key, value) {
	            $scope.valueSet = {};
	            $scope.valueSet.key = key;
	            $scope.valueSet.value = value;
	            $scope.typSet.push($scope.valueSet);
	            
			});
			$scope.labelSet = "Browser";
			$scope.gridOptions.data = $scope.typSet;
		}
		if(type == "resolution"){
			$.each($scope.screenResoluations, function(key, value) {
	            $scope.valueSet = {};
	            $scope.valueSet.key = key;
	            $scope.valueSet.value = value;
	            $scope.typSet.push($scope.valueSet);
	            
			});
			$scope.labelSet = "Resolution";
			$scope.gridOptions.data = $scope.typSet;
		}
		if(type == "hardware"){
			$.each($scope.screenResoluations, function(key, value) {
	            $scope.valueSet = {};
	            $scope.valueSet.key = key;
	            $scope.valueSet.value = value;
	            $scope.typSet.push($scope.valueSet);
	            
			});
			$scope.gridOptions.data = $scope.typSet;
			$scope.labelSet = "Hardware";
			
		}
		
		console.log($scope.typSet);
		
	}*/
	
}]);


angular.module('newApp')
.controller('CampaignsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','$rootScope','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,$rootScope,apiserviceAnalytics) {
	
	

	console.log($rootScope.startDateFilter);
	console.log($rootScope.endDateFilter);
	  setTimeout(function(){

	        $('.reportrange').daterangepicker(
	                {
	                    ranges: {
	                        'Today': [moment(), moment()],
	                        'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
	                        'Last 7 Days': [moment().subtract('days', 7), moment()],
	                        'Last 14 Days':[moment().subtract('days', 13), moment()],
	                        'Last 28 Days': [moment().subtract('days', 28), moment()],
	                        'Last 60 Days': [moment().subtract('days', 60), moment()],
	                        'Last 90 Days': [moment().subtract('days', 90), moment()],
	                        'This Month': [moment().startOf('month'), moment().endOf('month')],
	                        'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
	                    },
	                    startDate: moment().subtract('days', 7),
	                    endDate: moment()
	                },
	                function(start, end) {
	                    var startDate =  moment(start).format("MMDDYYYY");
	                    var endDate =  moment(end).format("MMDDYYYY");
	                    $rootScope.startDateFilter = moment(start).format("YYYY-MM-DD");
	                    $rootScope.endDateFilter = moment(end).format("YYYY-MM-DD ");
	                    if($scope.typeOfInfo != undefined){
	                    	
	                    console.log($scope.typeOfInfo);	
	                    $scope.changeTab($scope.typeOfInfo); 
	                    
	                    }
	                    console.log($rootScope.startDateFilter);
	            		console.log($rootScope.endDateFilter);
	                    $scope.$emit('reportDateChange', { startDate: startDate, endDate: endDate });
	                    $('.reportrange span').html(start.format('MMM D, YYYY') + ' - ' + end.format('MMM D, YYYY'));
	                    $scope.$apply();
	                }
	            );
	                console.log(moment().subtract('days', 7).format('YYYY-MM-DD '));
	                $rootScope.startDateFilter= moment().subtract('days', 7).format('YYYY-MM-DD ');
	                $rootScope.endDateFilter = moment().format("YYYY-MM-DD");

	            $('.reportrange span').html(moment().subtract('days', 7).format('MMM D, YYYY') + ' - ' + moment().format('MMM D, YYYY'));
	            $scope.$emit('reportDateChange', { startDate: moment().subtract('days', 7).format('MMDDYYYY'), endDate:  moment().format('MMDDYYYY') });

	    }, 2000);
	  if($rootScope.startDateFilter != undefined && $rootScope.endDateFilter !=undefined )
		{
		  console.log("in if");
		$scope.startDate=$rootScope.startDateFilter;
		$scope.endDate=$rootScope.endDateFilter;
		}
	else{
		console.log("in else");
		 $rootScope.startDateFilter= moment().subtract('days', 7).format('YYYY-MM-DD ');
       $rootScope.endDateFilter = moment().format("YYYY-MM-DD");
	}
	

	
	
	
	
	
	$scope.showSet = 0;
	$scope.savecampaign = function(campaign){
		$scope.campaign = campaign;
		console.log($scope.campaign);
		apiserviceAnalytics.saveCampaigns($scope.campaign).then(function(data){
		
		});
		
		$scope.showSet = 0;
	}
	
	apiserviceAnalytics.getCampaign().then(function(data){
		$scope.listCamp = data;
		});
	
	$scope.showSetup = function(){
		$scope.showSet = 1;
	}
	
	$scope.goToSearches = function() {
		$location.path('/searchesAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	
}]);


angular.module('newApp')
.controller('ActionsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,apiserviceAnalytics) {

	
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
		                                 { name: 'action_title', displayName: 'Action_Title', width:'18%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'action_type', displayName: 'Action_Type', width:'18%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'ip_address', displayName: 'Ip_address', width:'15%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'referrer_domain', displayName: 'Referrer_Domain', width:'18%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
	                                 },
		                                 { name: 'referrer_search', displayName: 'Referrer_Search', width:'18%',cellEditableCondition: false,
	                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
 		                                       if (row.entity.isRead === false) {
 		                                         return 'red';
 		                                     }
		                                	} ,
		                                 },
		                                 { name: 'time_pretty', displayName: 'Time_Pretty', width:'21%',cellEditableCondition: false,
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
		          $scope.gridOptions.data = $filter('filter')($scope.ActionList,{'action_title':grid.columns[0].filters[0].term,'action_type':grid.columns[1].filters[0].term,'ip_address':grid.columns[2].filters[0].term,'referrer_domain':grid.columns[3].filters[0].term,'referrer_search':grid.columns[4].filters[0].term},undefined);
		        });
	   		
 		};
 		
 		apiserviceAnalytics.getActionList(30).then(function(data){
		 
				console.log(data[0].dates[0].items);
			$scope.gridOptions.data = data[0].dates[0].items;
			$scope.ActionList = data[0].dates[0].items;
			$('#sliderBtn').click();
		});	
	
		 
		 $scope.lastDays = function(value){
			 apiserviceAnalytics.getActionList(value).then(function(data){
			 	
					console.log(data[0].dates[0].items);
				$scope.gridOptions.data = data[0].dates[0].items;
				$scope.visitiorList = data[0].dates[0].items;
				$('#sliderBtn').click();
			});
		 }
	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	 $scope.goToVideoAnalytics = function() {
			$location.path('/goToVideoAnalytics');
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
	$scope.goCampaignss = function(){
		$location.path('/CampaignsAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	$scope.goToSessionsData = function() {
		$location.path('/sessionsAnalytics');
	}
	
	$scope.goToheatMapInfo = function() {
		$location.path('/heatMapInfoAnalytics');
	}
	
	$scope.goToVehicleInfo = function(){
		$location.path('/allVehicleSessions');
	}
	
}]);

angular.module('newApp')
.controller('BasicsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,apiserviceAnalytics) {

	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	 $scope.goToVideoAnalytics = function() {
			$location.path('/goToVideoAnalytics');
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
	
	$scope.goCampaignss = function(){
		$location.path('/CampaignsAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
	$scope.goToSessionsData = function() {
		$location.path('/sessionsAnalytics');
	}
	
	$scope.goToheatMapInfo = function() {
		$location.path('/heatMapInfoAnalytics');
	}
	
	$scope.goToVehicleInfo = function(){
		$location.path('/allVehicleSessions');
	}
	
}]);


angular.module('newApp')
.controller('SearchesCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','$rootScope','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,$rootScope,apiserviceAnalytics) {
    
	$scope.idForSearch=$routeParams.idForSearch;
	$scope.idForEngine=$routeParams.idForEngine;
	$scope.idForRecent=$routeParams.idForRecent;
	$scope.idForNewest=$routeParams.idForNewest;
	$scope.idForRanking=$routeParams.idForRanking;
	console.log($scope.idForEngine);
	console.log($rootScope.endDateFilter);
	  setTimeout(function(){

	        $('.reportrange').daterangepicker(
	                {
	                    ranges: {
	                        'Today': [moment(), moment()],
	                        'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
	                        'Last 7 Days': [moment().subtract('days', 6), moment()],
	                        'Last 14 Days':[moment().subtract('days', 13), moment()],
	                        'Last 28 Days': [moment().subtract('days', 27), moment()],
	                        'Last 60 Days': [moment().subtract('days', 60), moment()],
	                        'Last 90 Days': [moment().subtract('days', 90), moment()],
	                        'This Month': [moment().startOf('month'), moment().endOf('month')],
	                        'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
	                    },
	                    startDate: moment().subtract('days', 6),
	                    endDate: moment()
	                },
	                function(start, end) {
	                    var startDate =  moment(start).format("MMDDYYYY");
	                    var endDate =  moment(end).format("MMDDYYYY");
	                    $rootScope.startDateFilter = moment(start).format("YYYY-MM-DD");
	                    $rootScope.endDateFilter = moment(end).format("YYYY-MM-DD ");
	                    if($scope.typeOfInfo != undefined){
	                    	
	                    console.log($scope.typeOfInfo);	
	                    $scope.setShowSearchesType($scope.typeOfInfo); 
	                    
	                    }
	                    console.log($rootScope.startDateFilter);
	            		console.log($rootScope.endDateFilter);
	                    $scope.$emit('reportDateChange', { startDate: startDate, endDate: endDate });
	                    $('.reportrange span').html(start.format('MMM D, YYYY') + ' - ' + end.format('MMM D, YYYY'));
	                    $scope.$apply();
	                }
	            );
	                console.log(moment().subtract('days', 6).format('YYYY-MM-DD '));
	                $rootScope.startDateFilter= moment().subtract('days', 6).format('YYYY-MM-DD ');
	                $rootScope.endDateFilter = moment().format("YYYY-MM-DD");

	            $('.reportrange span').html(moment().subtract('days', 6).format('MMM D, YYYY') + ' - ' + moment().format('MMM D, YYYY'));
	            $scope.$emit('reportDateChange', { startDate: moment().subtract('days', 6).format('MMDDYYYY'), endDate:  moment().format('MMDDYYYY') });

	    }, 2000);
	  if($rootScope.startDateFilter != undefined && $rootScope.endDateFilter !=undefined )
		{
		  console.log("in if");
		$scope.startDate=$rootScope.startDateFilter;
		$scope.endDate=$rootScope.endDateFilter;
		}
	else{
		console.log("in else");
		 $rootScope.startDateFilter= moment().subtract('days', 6).format('YYYY-MM-DD ');
       $rootScope.endDateFilter = moment().format("YYYY-MM-DD");
	}
	

	
	
	 var date = new Date();
	 $scope.typeOfInfo = 'Searches';
	  var startdate= new Date(date.getFullYear(), date.getMonth(), 1);
		$scope.startDate=$rootScope.startDateFilter;
		$scope.endDate=$rootScope.endDateFilter;
	
		$scope.initFunction = function(){
			$scope.DateWiseFind();
		}
		
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	
	$scope.gridOptions2 = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	
		 $scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		          $scope.gridOptions.data = $filter('filter')($scope.visitiorList,{'geolocation':grid.columns[0].filters[0].term,'ip_address':grid.columns[1].filters[0].term,'referrer_domain':grid.columns[2].filters[0].term,'total_visits':grid.columns[3].filters[0].term,'web_browser':grid.columns[4].filters[0].term},undefined);
		        });
	   		
  		};
  		
  		
	
		 $scope.setShowSearchesType = function(typeOfInfo){
				$scope.typeOfInfo = typeOfInfo;
				$scope.DateWiseFind();
				$scope.flagForChart1 = true;
				 $scope.flagForChart=false;
			} 
		 
		 $scope.DateWiseFind = function(){
			 var startDate = $rootScope.startDateFilter;
			var endDate = $rootScope.endDateFilter;	 
			console.log(endDate);
			
			if(endDate == '' || startDate == ''){
				 var startDate = $scope.startDate;
					var endDate = $scope.endDate;
			}
			if($scope.typeOfInfo == 'Searches'){
				apiserviceAnalytics.getSearchesListDale(startDate, endDate).then(function(data){
				 
						$scope.gridOptions.data = data;
						console.log(data);
						$scope.visitiorList = data;
						$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
						$scope.gridOptions.data = $scope.gridOptions.data.reverse();
						angular.forEach($scope.gridOptions.data, function(value, key) {
							
							value.averageTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.averageTime)), 'HH:mm:ss');
							value.totalTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totalTime)), 'HH:mm:ss');
							 var splitTime   = value.totalTime.split(":");
							 var splitTime1   = value.averageTime.split(":");
							 if(splitTime[0] == 00){
								 value.totalTime = splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 else{
								 value.totalTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
							 }
							 
							 if(splitTime1[0] == 00){
								 value.averageTime = splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 else{
								 value.averageTime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
							 }
							 
							
						});
						
						
						
						
					});
					 
						$scope.gridOptions.columnDefs = [
											             {name: 'titl', displayName: 'Source', width:'30%',
											            	 cellTemplate:'<div > <a ng-click="grid.appScope.getSearchInfo(row.entity.id)">{{row.entity.title}}</a> </div>',
											             },
											             {name:'value', displayName:'Visitors', width:'10%',
											            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.valuePercent}}%)</span></div>',
											             },
											             {name: 'averageActions', displayName: 'Average Actions', width:'10%'},
											             {name: 'averageTime', displayName: 'Average Time', width:'15%'},
											             {name: 'totalTime', displayName: 'Total Time', width:'15%'},
											             {name: 'bounceRate', displayName: 'Bounce Rate', width:'10%'},
											             /*{name:'stats_url', displayName:'', width:'20%',
											            	 cellTemplate:'<div><span><img width="{{row.entity.valuePercent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
											             },*/
											             {name:'Percent', displayName:'', width:'10%',		
											            	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.getSearchesPagesChart(row.entity.title)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
											            
											             }
											            
											         ]
					
			}else if($scope.typeOfInfo == 'Keywords'){
				
				apiserviceAnalytics.getKeywordsList(startDate, endDate).then(function(data){
				 
						$scope.gridOptions.data = data;
					console.log($scope.gridOptions.data);
					$scope.visitiorList = data;
					$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
					$scope.gridOptions.data = $scope.gridOptions.data.reverse();
				});
				 
				 
				 $scope.gridOptions.columnDefs = [
										             {name: 'title', displayName: 'Search', width:'60%'},
										             {name:'v', displayName:'Visitors', width:'20%',
										            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.valuePercent}}%)</span></div>',
										             },
										             {name:'stats_url', displayName:'Rank', width:'20%',
										            	 cellTemplate:'<div><span><img width="{{row.entity.value}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
										             },
										            
										         ]
				
			}else if($scope.typeOfInfo == 'Engines'){
				
				$scope.gridOptions.data=[];
				apiserviceAnalytics.getEngines(startDate, endDate).then(function(data){
				 
						console.log(data);
					$scope.gridOptions.data = data;
					$scope.visitiorList = data;
					$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'value');
					$scope.gridOptions.data = $scope.gridOptions.data.reverse();
					angular.forEach($scope.gridOptions.data, function(value, key) {
						
						value.averageTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.averageTime)), 'HH:mm:ss');
						value.totalTime=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.totalTime)), 'HH:mm:ss');
						 var splitTime   = value.totalTime.split(":");
						 var splitTime1   = value.averageTime.split(":");
						 if(splitTime[0] == 00){
							 value.totalTime = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.totalTime = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 
						 if(splitTime1[0] == 00){
							 value.averageTime = splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 else{
							 value.averageTime = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
						 }
						 
						
					});
					
					
					
				});
				 
				 $scope.gridOptions.columnDefs = [
										             {name: 'title', displayName: 'Engine', width:'20%',
										            	 cellTemplate:'<div ><a ng-click="grid.appScope.getEngineInfo(row.entity.title)">{{row.entity.title}}</a> </div>',	
										             },
										             {name:'v', displayName:'Visitors', width:'10%',
										            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.valuePercent}}%)</span></div>',
										             },
										             {name: 'averageActions', displayName: 'Average Actions', width:'10%',
										            	 cellTemplate:'<div><span> {{row.entity.averageActions}}</span></div>', 
										             },
										             {name: 'averageTime', displayName: 'Average Time', width:'10%'},
										             {name: 'totalTime', displayName: 'Total Time', width:'15%'},
										             {name: 'bounceRate', displayName: 'Bounce Rate', width:'10%'},
										             {name:'statsUrl', displayName:'', width:'15%',
										            	 cellTemplate:'<div><span><img width="{{row.entity.value}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
										             },
										             {name:'url', displayName:'', width:'10%',		
										            	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showEnginesChart(row.entity.title)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
										            
										             }
										            
										         ]
				 
				
				 
			}else if($scope.typeOfInfo == 'Recent'){
				$scope.gridOptions.data=[];
				apiserviceAnalytics.getRecent(startDate, endDate).then(function(data){
				
				$scope.gridOptions.data = data;
				$scope.visitiorList = data;
				
			});
			 
				 $scope.gridOptions.columnDefs = [
										             {name: 'timePretty', displayName: 'Time', width:'20%'},
										             {name:'title', displayName:'Item', width:'80%',
										            	 cellTemplate:'<div > <a ng-click="grid.appScope.getRecentInfo(row.entity.id)">{{row.entity.title}}</a> </div>',
										             }
										            
										         ]
				
			}else if($scope.typeOfInfo == 'Newest Unique'){
				$scope.gridOptions.data=[];
				apiserviceAnalytics.getNewestUni(startDate, endDate).then(function(data){
				
				$scope.gridOptions.data = data;
				console.log($scope.gridOptions.data);
				$scope.visitiorList = data;
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'timePretty', displayName: 'Time', width:'20%'},
									             {name:'title', displayName:'Item', width:'80%',
									            	 cellTemplate:'<div > <a ng-click="grid.appScope.getNewestInfo(row.entity.id)">{{row.entity.title}}</a> </div>',	 
									             }
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Local Searches'){
				apiserviceAnalytics.getSearchesLocal(startDate, endDate).then(function(data){
				
				$scope.gridOptions.data = data;
				console.log($scope.gridOptions.data);
				$scope.visitiorList = data;
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'timePretty', displayName: 'Time', width:'20%'},
									             {name:'title', displayName:'Item', width:'80%'}
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Rankings'){
				$scope.gridOptions.data=[];
				apiserviceAnalytics.getRankings(startDate, endDate).then(function(data){
				
					console.log(data);
				$scope.gridOptions.data = data/*[0].dates[0].items*/;
				console.log($scope.gridOptions.data);
				$scope.visitiorList = data/*[0].dates[0].items*/;
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'title', displayName: 'Search', width:'60%',
									            	 cellTemplate:'<div > <a ng-click="grid.appScope.getRankingInfo(row.entity.id)">{{row.entity.title}}</a> </div>',	 
									             },
									             {name:'v', displayName:'Rank', width:'10%',
									            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
									             },
									             {name:'stats_url', displayName:'', width:'20%',
									            	 cellTemplate:'<div><span><img width="{{row.entity.value}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									             },
									             {name:'chart', displayName:'', width:'10%',		
									            	 cellTemplate:'<div  style="margin-left:47px;"><span ng-click="grid.appScope.showRankingsChart(row.entity.title)"> {{row.entity.averagePercent.toFixed(2)}}% </span></div>',
									            
									             }
									         ]
				
			}else if($scope.typeOfInfo == 'Rankings_SheerSEO'){
				
			}
			
		 }
		 
		 $scope.showUrlInfoForSearch= function(url) {
			 var startDate = $("#cnfstartDateValue").val();
			var endDate = $("#cnfendDateValue").val();	
			console.log(startDate);
				console.log(endDate);
				$scope.flagForLanding="ForSearch";
				apiserviceAnalytics.getDataForSearch(url, startDate, endDate).then(function(data){
			
				
				console.log("::::::::");
				console.log(data);
				
				$scope.gridOptions.data = data;
				console.log($scope.gridOptions.data);
				$scope.visitiorList = data;
				
				angular.forEach($scope.gridOptions.data, function(value, key) {
					if( value.title== "Average time / visit" && value.value != null  ){
						value.value=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.value)), 'HH:mm:ss');
					 var splitTime1   = value.value.split(":");
					 if(splitTime1[0] == 00){
						 value.value = splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 else{
						 value.value = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
					 }
					 
					}
					if(value.title== "Total time" && value.value != null){
						value.value=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.value)), 'HH:mm:ss');
						var splitTime   = value.value.split(":");
						if(splitTime[0] == 00){
							value.value = splitTime[1]+"m "+splitTime[2]+"s";
						 }
						 else{
							 value.value = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
						 }
					}
					
					
				});
				
				
				
			});
			 
			 
			 $scope.gridOptions.columnDefs = [
                                              {name: 'title', displayName: 'Summary of filtered visitors', width:'40%'},
									             {name: 'value', displayName: 'These visitors', width:'30%'
									             },
									            
									         ]
									        
				
			 
		 }	 
		 
		 $scope.getSearchInfo = function(id){
				 console.log(id);
			  $location.path('/getSearchInfo/'+id);
			 
		 }
		 
		 $scope.getEngineInfo = function(id){
			 console.log(id);
		  $location.path('/getEngineInfo/'+id);
		 
	 }
		 $scope.getNewestInfo = function(id){
			 console.log(id);
		  $location.path('/getNewestInfo/'+id);
		 
	 }
		 $scope.getRecentInfo = function(id){
			 console.log(id);
		  $location.path('/getRecentInfo/'+id);
		 
	 }
		 
		 $scope.getRankingInfo = function(id){
			 console.log(id);
		  $location.path('/getRankingInfo/'+id);
		 
	 }	 
		 
		 $scope.searchVisitorData = function(){
			 apiserviceAnalytics.getVisitorList($scope.startDateFilter, $scope.endDateFilter).then(function(data){
			 
				$scope.gridOptions2.data = data;
				console.log($scope.gridOptions2.data);
				$scope.gridOptions2.data = $filter('orderBy')($scope.gridOptions2.data,'dateClick');
				$scope.gridOptions2.data = $scope.gridOptions2.data.reverse();
				//console.log($scope.gridOptions.data);
				//cellFilter: 'date:"yyyy-MM-dd"',enableSorting: true,
				angular.forEach($scope.gridOptions2.data, function(value, key) {
					var array = value.timePretty.split(',');
					var timeNew= value.timePretty.split(' ');
					var newTimePretty;
					value.timeSet = array[1];
					value.newTimePretty=timeNew[1]+" "+timeNew[2]+" "+timeNew[3]+" "+timeNew[4];
					//value.timeTotal = $filter('date')(value.timeTotal, 'hh:mm:ss');
					value.timeTotal=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.timeTotal)), 'HH:mm:ss');
					 var splitTime   = value.timeTotal.split(":");
					 if(splitTime[0] == 00){
						 value.timeTotal = splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 else{
						 value.timeTotal = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
					 }
					 
					
				});
				 console.log($scope.gridOptions2.data);
				$scope.visitiorList = data;
			});
			
			
			$scope.gridOptions2.columnDefs = [
			                              {name: 'newTimePretty', displayName: 'Date & Time', width:'12%'},
			                              {name: 'geolocation', displayName: 'Location', width:'10%',
			                             	 cellTemplate:'<div ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.referrerTypeDataForLocation(row.entity.geolocation)">{{row.entity.geolocation}}</label></div>',
			                              },
			                              {name:'organization', displayName:'Internet Provider', width:'15%',
			                             	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showVisitorInfo(row.entity.id)">{{row.entity.organization}}</label></div>',
			                              },
			                              {name:'actions', displayName:'Actions', width:'8%',
			                             	 cellTemplate:'<div class="link-domain" ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.showVisitorInfo(row.entity.id)">{{row.entity.actions}} actions </label></div>',
			                             	 
			                              },
			                              {name:'timeTotal', displayName:'Time Spent', width:'10%'},
			                              {name:'abc', displayName:'Searches & Refferals', width:'40%',
			                             	 cellTemplate:'<div ng-if="row.entity.referrerUrl != null"><span ng-click="grid.appScope.showUrlInfo(row.entity.id)" ><img src="//con.tent.network/media/icon_search.gif"></span><a href="{{row.entity.referrerUrl}}"> <img src="//con.tent.network/media/arrow.gif"></a><a class="link-domain" ng-click="grid.appScope.showUrlInfoForDomain(row.entity.id)">{{row.entity.referrerDomain}}</a>&nbsp;&nbsp;<a  class="link-domain"  ng-click="grid.appScope.showUrlInfoForRefferal(row.entity.id)">{{row.entity.referrerUrl}}</a></div>',
			                             	 
			                             },
			                              {name:'Sear', displayName:'Page', width:'10%',
			                             	 cellTemplate:'<a  target="_blank"  href="{{row.entity.landingPage}}"><img class="mb-2" style="margin-left: 8px;width: 21px;" title="View heatmap for this page" src="https://con.tent.network/media/icon_spy.gif"></a>',
			                              }
			                              
			                          ];
			 
			 }
		 
		 
		 $scope.uniqueDataForAllTab = function(){
			 
			console.log($scope.idForSearch) ;
			 if($scope.idForSearch != undefined){
				 apiserviceAnalytics.getsearchInfo($scope.idForSearch, $scope.startDate, $scope.endDate).then(function(data){
					
							$scope.gridOptions.data=data;
							console.log($scope.gridOptions.data);
							angular.forEach($scope.gridOptions.data, function(value, key) {
								if(value.title=="totalT"){
									value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
									 var splitTime   = value.these_visitors.split(":");
									 if(splitTime[0] == 00){
										 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 else{
										 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 
									 
									}	
								if(value.title=="averageT"){
									value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
									 var splitTime1   = value.these_visitors.split(":");
									 if(splitTime1[0] == 00){
										 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 else{
										 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 
									}	 
									 
								if(value.title=="totalT"){
									value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
									 var splitTime   = value.all_visitors.split(":");
									 if(splitTime[0] == 00){
										 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 else{
										 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 
									 
									}	
								if(value.title=="averageT"){
									value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
									 var splitTime1   = value.all_visitors.split(":");
									 if(splitTime1[0] == 00){
										 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 else{
										 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 
									}	 
									
								
							});
							
							
					 
					 
					 
				});
					$scope.searchVisitorData();
					
					 $scope.gridOptions.columnDefs = [
					                                   {name: 'title', displayName: 'Summary of filtered visitors', width:'55%',
					                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',			
					                                   },
											             {name:'these_visitors', displayName:'These Visitors', width:'15%' },
											             {name:'all_visitors', displayName:'All Visitors', width:'15%' },
											             {name:'these_vis', displayName:'Difference', width:'15%',
											            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',
											             },
												        
												        
											            	 ]
			 
					 
		 }
			 
			 
			 
			 if($scope.idForEngine != undefined){
				 apiserviceAnalytics.getEngineInfo($scope.idForEngine, $scope.startDate, $scope.endDate).then(function(data){
					
							$scope.gridOptions.data=data;
							console.log($scope.gridOptions.data);
							angular.forEach($scope.gridOptions.data, function(value, key) {
								if(value.title=="totalT"){
									value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
									 var splitTime   = value.these_visitors.split(":");
									 if(splitTime[0] == 00){
										 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 else{
										 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 
									 
									}	
								if(value.title=="averageT"){
									value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
									 var splitTime1   = value.these_visitors.split(":");
									 if(splitTime1[0] == 00){
										 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 else{
										 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 
									}	 
									 
								if(value.title=="totalT"){
									value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
									 var splitTime   = value.all_visitors.split(":");
									 if(splitTime[0] == 00){
										 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 else{
										 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 
									 
									}	
								if(value.title=="averageT"){
									value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
									 var splitTime1   = value.all_visitors.split(":");
									 if(splitTime1[0] == 00){
										 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 else{
										 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 
									}
									
								
							});
							
							
					 
					 
					 
				});
					
					 $scope.searchVisitorData();
					 $scope.gridOptions.columnDefs = [
					                                   {name: 'tit', displayName: 'Summary of filtered visitors', width:'55%',
					                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',			
					                                   },
											             {name:'these_visitors', displayName:'These Visitors', width:'15%' },
											             {name:'all_visitors', displayName:'All Visitors', width:'15%' },
											             {name:'thevis', displayName:'Difference', width:'15%',
											            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',
											             },
												        
												        
											            	 ]
					
		 }
			 

			 if($scope.idForRecent != undefined){
				 apiserviceAnalytics.getRecentInfo($scope.idForRecent, $scope.startDate, $scope.endDate).then(function(data){
					
							$scope.gridOptions.data=data;
							console.log($scope.gridOptions.data);
							angular.forEach($scope.gridOptions.data, function(value, key) {
								if(value.title=="totalT"){
									value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
									 var splitTime   = value.these_visitors.split(":");
									 if(splitTime[0] == 00){
										 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 else{
										 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 
									 
									}	
								if(value.title=="averageT"){
									value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
									 var splitTime1   = value.these_visitors.split(":");
									 if(splitTime1[0] == 00){
										 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 else{
										 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 
									}	 
									 
								if(value.title=="totalT"){
									value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
									 var splitTime   = value.all_visitors.split(":");
									 if(splitTime[0] == 00){
										 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 else{
										 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 
									 
									}	
								if(value.title=="averageT"){
									value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
									 var splitTime1   = value.all_visitors.split(":");
									 if(splitTime1[0] == 00){
										 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 else{
										 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 
									}	 
									
								
							});
							
							
					 
					 
					 
				});
					
					 $scope.searchVisitorData();
					 $scope.gridOptions.columnDefs = [
					                                   {name: 'tea', displayName: 'Summary of filtered visitors', width:'55%',
					                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',			
					                                   },
											             {name:'these_visitors', displayName:'These Visitors', width:'15%' },
											             {name:'all_visitors', displayName:'All Visitors', width:'15%' },
											             {name:'vis', displayName:'Difference', width:'15%',
											            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>',
											             },
												        
												        
											            	 ]
					
		 }
			 
			 
			 if($scope.idForNewest != undefined){
				 apiserviceAnalytics.getNewestInfo($scope.idForNewest, $scope.startDate, $scope.endDate).then(function(data){
					
							$scope.gridOptions.data=data;
							console.log($scope.gridOptions.data);
							angular.forEach($scope.gridOptions.data, function(value, key) {
								if(value.title=="totalT"){
									value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
									 var splitTime   = value.these_visitors.split(":");
									 if(splitTime[0] == 00){
										 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 else{
										 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 
									 
									}	
								if(value.title=="averageT"){
									value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
									 var splitTime1   = value.these_visitors.split(":");
									 if(splitTime1[0] == 00){
										 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 else{
										 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 
									}	 
									 
								if(value.title=="totalT"){
									value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
									 var splitTime   = value.all_visitors.split(":");
									 if(splitTime[0] == 00){
										 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 else{
										 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 
									 
									}	
								if(value.title=="averageT"){
									value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
									 var splitTime1   = value.all_visitors.split(":");
									 if(splitTime1[0] == 00){
										 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 else{
										 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 
									}	 
									
								
							});
							
							
					 
					 
					 
				});
					 $scope.searchVisitorData();
					
					 $scope.gridOptions.columnDefs = [
					                                   {name: 'title', displayName: 'Summary of filtered visitors', width:'55%',
					                                	   cellTemplate: '<div> <img  src={{row.entity.images}} >&nbsp;{{row.entity.title}}</div>',			
					                                   },
											             {name:'these_visitors', displayName:'These Visitors', width:'15%' },
											             {name:'all_visitors', displayName:'All Visitors', width:'15%' },
											             {name:'these_vis', displayName:'Difference', width:'15%',
											            	 cellTemplate:'<div><span>{{row.entity.difference.toFixed(2)}}%</span></div>', 
											             },
												        
												        
											            	 ]
					
		 }
		 

			 
			 if($scope.idForRanking != undefined){
				 apiserviceAnalytics.getRankingInfo($scope.idForRanking, $scope.startDate, $scope.endDate).then(function(data){
					
							$scope.gridOptions.data=data;
							console.log($scope.gridOptions.data);
							angular.forEach($scope.gridOptions.data, function(value, key) {
								if(value.title=="totalTime"){
									value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
									 var splitTime   = value.these_visitors.split(":");
									 if(splitTime[0] == 00){
										 value.these_visitors = splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 else{
										 value.these_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 
									 
									}	
								if(value.title=="averageTime"){
									value.these_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.these_visitors)), 'HH:mm:ss');
									 var splitTime1   = value.these_visitors.split(":");
									 if(splitTime1[0] == 00){
										 value.these_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 else{
										 value.these_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 
									}	 
									 
								if(value.title=="totalTime"){
									value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
									 var splitTime   = value.all_visitors.split(":");
									 if(splitTime[0] == 00){
										 value.all_visitors = splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 else{
										 value.all_visitors = splitTime[0]+"h "+splitTime[1]+"m "+splitTime[2]+"s";
									 }
									 
									 
									}	
								if(value.title=="averageTime"){
									value.all_visitors=$filter('date')(new Date(0, 0, 0).setSeconds(parseInt(value.all_visitors)), 'HH:mm:ss');
									 var splitTime1   = value.all_visitors.split(":");
									 if(splitTime1[0] == 00){
										 value.all_visitors = splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 else{
										 value.all_visitors = splitTime1[0]+"h "+splitTime1[1]+"m "+splitTime1[2]+"s";
									 }
									 
									}	 
									
								
							});
							
							
					 
					 
					 
				});
					 $scope.searchVisitorData();
					
					 $scope.gridOptions.columnDefs = [
					                                   {name: 'title', displayName: 'Summary of filtered visitors', width:'55%',
					                                	   cellTemplate: '<div>{{row.entity.title}}</div>',			
					                                   },
											             {name:'these_visitors', displayName:'These Visitors', width:'15%' },
											             {name:'all_visitors', displayName:'All Visitors', width:'15%' },
											             {name:'these_vis', displayName:'Difference', width:'15%' },
												        
												        
											            	 ]
					
		 }
		
			 
			 
			 
		 }
		 
		 $scope.flagForChart1 = true;
		 $scope.flagForChart = true;
		 $scope.showSearchesPagesChart = function(title) {
			 console.log(">>>>>>>>");
			 var startDate =$rootScope.startDateFilter;
				var endDate =$rootScope.endDateFilter;
				
				
				$scope.urlobj.startDate=startDate;
				$scope.urlobj.endDate=endDate;
				$scope.urlobj.title=title;
				$scope.flagForChart=0;
				apiserviceAnalytics.getSearchesPagesChart($scope.urlobj).then(function(data){
				
					console.log(data);
					$scope.flagForChart=1;
					$scope.flagForChart1 = false;
					createChart(data);
					
					$scope.value = [];
					$scope.dates = [];
					
				
			});
				
			 
			}
		 
		 
		 
		
		 $scope.urlobj={};
		 $scope.flagForChart1 = true;
		 $scope.flagForChart = true;
		 $scope.showEnginesChart = function(title) {
			 console.log(">>>>>>>>");
			 var startDate =$rootScope.startDateFilter;
				var endDate =$rootScope.endDateFilter;
				
				
				$scope.urlobj.startDate=startDate;
				$scope.urlobj.endDate=endDate;
				$scope.urlobj.title=title;
				$scope.flagForChart=0;
				apiserviceAnalytics.getEnginesChart($scope.urlobj).then(function(data){
				
					console.log(data);
					$scope.flagForChart=1;
					$scope.flagForChart1 = false;
					createChart(data);
					
					$scope.value = [];
					$scope.dates = [];
					
				
			});
				
			 
			}
		 $scope.urlobj={};
		 $scope.flagForChart1 = true;
		 $scope.flagForChart = true;
		 $scope.showRankingsChart = function(title) {
			 console.log(">>>>>>>>");
			 var startDate =$rootScope.startDateFilter;
				var endDate =$rootScope.endDateFilter;
				
				
				$scope.urlobj.startDate=startDate;
				$scope.urlobj.endDate=endDate;
				$scope.urlobj.title=title;
				$scope.flagForChart=0;
				apiserviceAnalytics.getRankingsChart($scope.urlobj).then(function(data){
				
					console.log(data);
					$scope.flagForChart=1;
					$scope.flagForChart1 = false;
					createChart(data);
					
					$scope.value = [];
					$scope.dates = [];
					
				
			});
				
			 
			}
		 
		 
		
		 
		 
		 var seriesOptions = [];
	      var seriesCounter = 0;
	      var stockChart; 
	      var stockChart1; 
	      
	      function createChart(initdata) {
	    	  stockChart1 = 1;
	    	  console.log("$scope.flagForChart"+$scope.flagForChart);
	    	  stockChart = $('#functional-chart').highcharts({
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
	      
		 
		 
		 
		 
		 
	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	 $scope.goToVideoAnalytics = function() {
			$location.path('/goToVideoAnalytics');
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
	$scope.goToSessionsData = function() {
		$location.path('/sessionsAnalytics');
	}
	
	$scope.goToheatMapInfo = function() {
		$location.path('/heatMapInfoAnalytics');
	}
	
	$scope.goToVehicleInfo = function(){
		$location.path('/allVehicleSessions');
	}
	
}]);


angular.module('newApp')
.controller('RefferersCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,apiserviceAnalytics) {

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
		                                 { name: 'geolocation', displayName: 'Location', width:'15%',cellEditableCondition: false,
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
		                                 { name: 'referrer_search', displayName: 'Referrer_Search', width:'18%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'referrer_type', displayName: 'Referrer_Type', width:'12%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
	                                 },
		                                 { name: 'referrer_url', displayName: 'Referrer_Url', width:'22%',cellEditableCondition: false,
	                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
 		                                       if (row.entity.isRead === false) {
 		                                         return 'red';
 		                                     }
		                                	} ,
		                                 },
		                                 { name: 'time_pretty', displayName: 'Time_Pretty', width:'22%',cellEditableCondition: false,
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
		          $scope.gridOptions.data = $filter('filter')($scope.visitiorList,{'geolocation':grid.columns[0].filters[0].term,'referrer_domain':grid.columns[1].filters[0].term,'referrer_search':grid.columns[2].filters[0].term,'referrer_type':grid.columns[3].filters[0].term,'referrer_url':grid.columns[4].filters[0].term},undefined);
		        });
	   		
 		};
 		
 		apiserviceAnalytics.getVisitorList(30).then(function(data){
		 
				console.log(data[0].dates[0].items);
			$scope.gridOptions.data = data[0].dates[0].items;
			$scope.visitiorList = data[0].dates[0].items;
			$('#sliderBtn').click();
		});
		 
		 $scope.lastDays = function(value){
			 apiserviceAnalytics.getVisitorList(value).then(function(data){
			 
					console.log(data[0].dates[0].items);
				$scope.gridOptions.data = data[0].dates[0].items;
				$scope.visitiorList = data[0].dates[0].items;
				$('#sliderBtn').click();
			});
		 }
	
	
	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	 $scope.goToVideoAnalytics = function() {
			$location.path('/goToVideoAnalytics');
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
	
	$scope.goCampaignss = function(){
		$location.path('/CampaignsAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
	$scope.goToSessionsData = function() {
		$location.path('/sessionsAnalytics');
	}
	
	$scope.goToheatMapInfo = function() {
		$location.path('/heatMapInfoAnalytics');
	}
	
	$scope.goToVehicleInfo = function(){
		$location.path('/allVehicleSessions');
	}
	
}]);


angular.module('newApp')
.controller('ContentCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,apiserviceAnalytics) {

	apiserviceAnalytics.getContentList().then(function(data){
	
		console.log(data);
		//$scope.gridOptions.data = data[0].dates[0].items;
		//$scope.SearchList = data[0].dates[0].items;
		$('#sliderBtn').click();
	});
	
	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	 $scope.goToVideoAnalytics = function() {
			$location.path('/goToVideoAnalytics');
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
	
	$scope.goCampaignss = function(){
		$location.path('/CampaignsAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	$scope.goToSessionsData = function() {
		$location.path('/sessionsAnalytics');
	}
	
	$scope.goToheatMapInfo = function() {
		$location.path('/heatMapInfoAnalytics');
	}
	
	$scope.goToVehicleInfo = function(){
		$location.path('/allVehicleSessions');
	}
	
}]);


angular.module('newApp')
.controller('VehicleDateWiseCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,apiserviceAnalytics) {
console.log("....................");

apiserviceAnalytics.getVehiclePriceLogs($routeParams.id, $routeParams.vin, $routeParams.status).then(function(data){

	console.log("succeess");
	console.log(data);
	$scope.listingLog = data;;
});

$('#viewsChartS').css("text-decoration","underline");
	/*$http.get('/getFinancialVehicleDetailsByBodyStyle').success(function(data) {
		console.log("succee");
		console.log(data);
		 createChart(data);
	});*/
	
	$scope.showCustomerRequest = function(){
		$('#customerR').css("text-decoration","underline");
		$('#leadsV').css("text-decoration","none");
		 $('#viewsChartS').css("text-decoration","none");
		$('#follwersChartS').css("text-decoration","none");
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		
		if(startDate == ""){
			startDate = "0";
		}
		
		if(endDate == ""){
			endDate = "0";
		}
		apiserviceAnalytics.getCustomerRequest($routeParams.id, $routeParams.vin, $routeParams.status, startDate, endDate).then(function(data){
		
			console.log("succeess");
			apiserviceAnalytics.getCustomerRequestFlag($routeParams.id, $routeParams.vin, $routeParams.status, startDate, endDate).then(function(data){
			
				console.log("succeess");
				console.log(data1);
				
				data.push(data1);
				 createChart(data);
			
			});
			
		});
		
	}
	
	$scope.dataValue = [];
$scope.showLeads = function(){
	$('#customerR').css("text-decoration","none");
	$('#follwersChartS').css("text-decoration","none");
	 $('#viewsChartS').css("text-decoration","none");
	$('#leadsV').css("text-decoration","underline");
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		
		if(startDate == ""){
			startDate = "0";
		}
		
		if(endDate == ""){
			endDate = "0";
		}
		apiserviceAnalytics.getCustomerRequestLeads($routeParams.id, $routeParams.vin, $routeParams.status, startDate, endDate).then(function(data){
		
			console.log("succeess");
			//$scope.dataValue = data;
			console.log(data);
			apiserviceAnalytics.getCustomerRequestFlag($routeParams.id, $routeParams.vin, $routeParams.status, startDate, endDate).then(function(data){
			
				console.log("succeess");
				console.log(data1);
				data.push(data1);
		       createChart(data);
			
			});
		});
		
	}

 $scope.showViewsChart = function(){
	 $('#customerR').css("text-decoration","none");
	 $('#leadsV').css("text-decoration","none");
	 $('#follwersChartS').css("text-decoration","none");
	 $('#viewsChartS').css("text-decoration","underline");
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			if(startDate == ""){
				startDate = "0";
			}
			
			if(endDate == ""){
				endDate = "0";
			}
			$scope.obj =  [];
			apiserviceAnalytics.getviniewsChartLeads($routeParams.id, $routeParams.vin, $routeParams.status, startDate, endDate).then(function(data){
			
				console.log("succeess");
				console.log(data);
				apiserviceAnalytics.getCustomerRequestFlag($routeParams.id, $routeParams.vin, $routeParams.status, startDate, endDate).then(function(data){
				
					console.log("succeess");
					console.log(data1);
					//$scope.obj.push(data1);
					data.push(data1);
					apiserviceAnalytics.getCustomerLounchDateFlag($routeParams.id, $routeParams.vin, $routeParams.status, startDate, endDate).then(function(data){
					
						console.log(data2);
						//$scope.obj.push(data2);
						console.log($scope.obj);
						data.push(data2);
						console.log(data);
						createChart(data);
					});
					
					 
				
				});
			});
 }
 
  $scope.showFollwerChart = function(){
	  $('#customerR').css("text-decoration","none");
	  $('#leadsV').css("text-decoration","none");
	  $('#viewsChartS').css("text-decoration","none");
	  $('#follwersChartS').css("text-decoration","underline");
	  
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			if(startDate == ""){
				startDate = "0";
			}
			
			if(endDate == ""){
				endDate = "0";
			}
			apiserviceAnalytics.getFollowerLeads($routeParams.id, $routeParams.vin, $routeParams.status, startDate, endDate).then(function(data){
			
				console.log("succeess");
				console.log(data);
				apiserviceAnalytics.getCustomerRequestFlag($routeParams.id, $routeParams.vin, $routeParams.status, startDate, endDate).then(function(data){
				
					console.log("succeess");
					console.log(data1);
					
					data.push(data1);
					 createChart(data);
				
				});
			});
			
  }
	
	 var seriesOptions = [];
     var seriesCounter = 0;
     var stockChart; 
     var stockChart1; 
     function createChart(initdata) {
   	  stockChart1 = 1;
   	  console.log(initdata);
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
                 //pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.change})<br/>',
                 valueDecimals: 2
             },

             series: initdata
         });
     }; 

	$scope.goSessionInfo = function(){
		$location.path('/sessionsAnalytics/'+$routeParams.vin);
	}
	
	
}]);


angular.module('newApp')
.controller('SessionsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,apiserviceAnalytics) {
console.log("....................");
console.log($routeParams.vin);
$scope.lineChartweek = 0;
$scope.lineChartmonth = 0;
$scope.lineChartday = 0;


$scope.lineChartMap = function(lasttime){
	console.log(lasttime);
	apiserviceAnalytics.getSessionDaysVisitorsStats($routeParams.vin,lasttime).then(function(data){
	
			
		var randomScalingFactor = [10,20,30,40,50,60,70,80];
		var lineChartData = {
		    labels: response.months,
		    datasets: [
		      {
		          label: "My Second dataset",
		          fillColor: "rgba(49, 157, 181,0.2)",
		          strokeColor: "#319DB5",
		          pointColor: "#319DB5",
		          pointStrokeColor: "#fff",
		          pointHighlightFill: "#fff",
		          pointHighlightStroke: "#319DB5",
		          data: response.allVisitor
		      }
		    ]
		}
		if($scope.lineChartmonth == 1){
			var ctx = document.getElementById("line-chart").getContext("2d");
			ctx.canvas.width = 1430;
			ctx.canvas.height = 380;
		}
		if($scope.lineChartweek == 1){
			var ctx = document.getElementById("line-chartweek").getContext("2d");
			ctx.canvas.width = 1430;
			ctx.canvas.height = 380;
		}
		if($scope.lineChartday == 1){
			var ctx = document.getElementById("line-chartday").getContext("2d");
			ctx.canvas.width = 1430;
			ctx.canvas.height = 380;
		}
		
		window.myLine = new Chart(ctx).Line(lineChartData,{
		    responsive: true,
		    //scaleOverride: true,
		    //scaleSteps: 4,
		    //scaleStepWidth:2,
		    tooltipCornerRadius: 0,
		    tooltipTemplate: "<%= value %>",
		    multiTooltipTemplate: "<%= value %>",
		});

	});
}

$scope.weekFunction = function(){
	$scope.lineChartMap("week");
	$scope.lineChartweek = 1;
	$scope.lineChartmonth = 0;
	$scope.lineChartday = 0;
}

$scope.monthFunction = function(){
	$scope.lineChartMap("month");
	$scope.lineChartweek = 0;
	$scope.lineChartmonth = 1;
	$scope.lineChartday = 0;
}

$scope.dayFunction = function(){
	$scope.lineChartMap("day");
	$scope.lineChartday = 1;
	$scope.lineChartweek = 0;
	$scope.lineChartmonth = 0;
}

var lineChartData1 = {
	    labels: [1,2,3,4,5,6,7],
	    datasets: [
	      {
	          label: "My Second dataset",
	          fillColor: "rgba(49, 157, 181,0.2)",
	          strokeColor: "#319DB5",
	          pointColor: "#319DB5",
	          pointStrokeColor: "#fff",
	          pointHighlightFill: "#fff",
	          pointHighlightStroke: "#319DB5",
	          data: [0,0,0,1,0,2,0]
	      }
	    ]
	}
	var ctx1 = document.getElementById("line-chart1").getContext("2d");
	window.myLine = new Chart(ctx1).Line(lineChartData1, {
	    responsive: true,
	    scaleOverride: true,
	    tooltipCornerRadius: 0,
	    scaleSteps: 4,
	    scaleStepWidth:2,
	    showScale: false,
	    pointDot: false,
	    tooltipTemplate: "",
	    multiTooltipTemplate: "",
	});
	
	var ctx2 = document.getElementById("line-chart2").getContext("2d");
	window.myLine = new Chart(ctx2).Line(lineChartData1, {
	    responsive: true,
	    scaleOverride: true,
	    tooltipCornerRadius: 0,
	    scaleSteps: 4,
	    scaleStepWidth:2,
	    showScale: false,
	    pointDot: false,
	    tooltipTemplate: "",
	    multiTooltipTemplate: "",
	});
	
	var ctx2 = document.getElementById("line-chart3").getContext("2d");
	window.myLine = new Chart(ctx2).Line(lineChartData1, {
	    responsive: true,
	    scaleOverride: true,
	    tooltipCornerRadius: 0,
	    scaleSteps: 4,
	    scaleStepWidth:2,
	    showScale: false,
	    pointDot: false,
	    tooltipTemplate: "",
	    multiTooltipTemplate: "",
	});
	
	var ctx2 = document.getElementById("line-chart4").getContext("2d");
	window.myLine = new Chart(ctx2).Line(lineChartData1, {
	    responsive: true,
	    scaleOverride: true,
	    tooltipCornerRadius: 0,
	    scaleSteps: 4,
	    scaleStepWidth:2,
	    showScale: false,
	    pointDot: false,
	    tooltipTemplate: "",
	    multiTooltipTemplate: "",
	});
	
	var ctx2 = document.getElementById("line-chart5").getContext("2d");
	window.myLine = new Chart(ctx2).Line(lineChartData1, {
	    responsive: true,
	    scaleOverride: true,
	    tooltipCornerRadius: 0,
	    scaleSteps: 4,
	    scaleStepWidth:2,
	    showScale: false,
	    pointDot: false,
	    tooltipTemplate: "",
	    multiTooltipTemplate: "",
	});
	

	apiserviceAnalytics.getStatusList($routeParams.vin).then(function(data){

	$scope.lineChartMap("day");
	$scope.lineChartday = 1;
	console.log("data");
	console.log(data);
	$scope.followers = data.followers;
	$scope.pageview = data.pageview;
	$scope.avgSessDur = data.avgSessDur;
	$scope.users = data.users;
	$scope.enginesound = data.enginesound;
	$scope.virtualtour = data.virtualtour;
	
	
	var pieData = [
	               { value: data.newUser, color: "rgba(27, 184, 152,0.9)", highlight: "rgba(27, 184, 152,1)", label: "New Visitor" },
	               { value: data.users, color: "rgba(201, 98, 95,0.9)", highlight: "rgba(201, 98, 95,1)", label: "Returning Visitor" }
	           ];
	var ctx = document.getElementById("pie-chart visitorInfo").getContext("2d");
	window.myPie = new Chart(ctx).Pie(pieData, {
	    tooltipCornerRadius: 0
	});
	
	var pieData = [
	               { value: data.requestmoreinfo, color: "rgba(27, 184, 152,0.9)", highlight: "rgba(27, 184, 152,1)", label: "Submited" },
	               { value: data.requestmoreinfoshow, color: "rgba(201, 98, 95,0.9)", highlight: "rgba(201, 98, 95,1)", label: "Clicked" }
	           ];
	var ctx = document.getElementById("pie-chart requestmoreinfo").getContext("2d");
	window.myPie = new Chart(ctx).Pie(pieData, {
	    tooltipCornerRadius: 0
	});
	
		var pieData = [
		               { value: data.scheduletest, color: "rgba(27, 184, 152,0.9)", highlight: "rgba(27, 184, 152,1)", label: "Submited" },
		               { value: data.scheduletestshow, color: "rgba(201, 98, 95,0.9)", highlight: "rgba(201, 98, 95,1)", label: "Clicked" }
		           ];
		var ctx = document.getElementById("pie-chart scheduletest").getContext("2d");
		window.myPie = new Chart(ctx).Pie(pieData, {
		    tooltipCornerRadius: 0
		});
	
	var pieData = [
	               { value: data.tradeinapp, color: "rgba(27, 184, 152,0.9)", highlight: "rgba(27, 184, 152,1)", label: "Submited" },
	               { value: data.tradeinappshow, color: "rgba(201, 98, 95,0.9)", highlight: "rgba(201, 98, 95,1)", label: "Clicked" }
	           ];
	var ctx = document.getElementById("pie-chart trade").getContext("2d");
	window.myPie = new Chart(ctx).Pie(pieData, {
	    tooltipCornerRadius: 0
	});
	
	var pieData = [
	               { value: data.emailtofriend, color: "rgba(27, 184, 152,0.9)", highlight: "rgba(27, 184, 152,1)", label: "Submited" },
	               { value: data.emailtofriendshow, color: "rgba(201, 98, 95,0.9)", highlight: "rgba(201, 98, 95,1)", label: "Clicked" }
	           ];
	var ctx = document.getElementById("pie-chart emailtofriend").getContext("2d");
	window.myPie = new Chart(ctx).Pie(pieData, {
	    tooltipCornerRadius: 0
	});
});


$scope.langshow = 0;
$scope.webBroshow = 0;
apiserviceAnalytics.getDemographics($routeParams.vin).then(function(data){

	$scope.selectedTab = 1;
	$scope.langshow = 1;
	console.log(data);
	$scope.langmap = data.language;
	$scope.webBrosmap = data.webBrowser;
	$scope.operatingSystem = data.operatingSystem;
	$scope.location = data.location;
	$scope.screenResoluations = data.screenResoluation;
	console.log($scope.screenResoluation);
});


$scope.showBrowerCount = function(){
	$scope.langshow = 0;
	$scope.webBroshow = 1;
	$scope.operatingshow = 0;
	$scope.locationshow = 0;
	$scope.screenResoluation = 0;
}

$scope.showlanguageCount = function(){
	$scope.langshow = 1;
	$scope.webBroshow = 0;
	$scope.operatingshow = 0;
	$scope.locationshow = 0;
	$scope.screenResoluation = 0;
}

$scope.showLocationCount = function(){
	$scope.locationshow = 1;
	$scope.langshow = 0;
	$scope.webBroshow = 0;
	$scope.operatingshow = 0;
	$scope.screenResoluation = 0;
}

$scope.showOperatingCount = function(){
	$scope.operatingshow = 1;
	$scope.langshow = 0;
	$scope.webBroshow = 0;
	$scope.locationshow = 0;
	$scope.screenResoluation = 0;
}

$scope.showscreenResoluationCount = function(){
	$scope.operatingshow = 0;
	$scope.langshow = 0;
	$scope.webBroshow = 0;
	$scope.locationshow = 0;
	$scope.screenResoluation = 1;
}

$scope.analyType = "";

	$scope.openUserPopup = function(analyType){
		$scope.analylineChartmonth = 1;
		$scope.analylineChartweek = 0;
		$scope.analylineChartday = 0;
		$('#userModal').modal();
		$scope.analyType = analyType;
		$scope.analylineChartMap("month",$scope.analyType);
		
	}
	
	$scope.openEmailpopup = function(mailType){
		console.log(mailType);
		$scope.maillineChartmonth = 1;
		$scope.maillineChartday = 0;
		$scope.maillineChartweek = 0;
		$scope.mailType = mailType;
		$('#emailModal').modal();
		$scope.maillineChartMap("month",$scope.mailType);
		
	}
	
	$scope.analylineChartMap = function(lastTime,analyType){
		apiserviceAnalytics.getSessionDaysUserStats($routeParams.vin, lastTime, analyType).then(function(data){
		
			
			var randomScalingFactor = [10,20,30,40,50,60,70,80];
			var analyLineChartData = {
			    labels: response.months,
			    datasets: [
			      {
			          label: "My Second dataset",
			          fillColor: "rgba(49, 157, 181,0.2)",
			          strokeColor: "#319DB5",
			          pointColor: "#319DB5",
			          pointStrokeColor: "#fff",
			          pointHighlightFill: "#fff",
			          pointHighlightStroke: "#319DB5",
			          data: response.allVisitor
			      }
			    ]
			}
			if($scope.analylineChartmonth == 1){
				var ctxx = document.getElementById("analyline-chart").getContext("2d");
				ctxx.canvas.width = 1430;
				ctxx.canvas.height = 380;
			}
			if($scope.analylineChartweek == 1){
				var ctxx = document.getElementById("analyline-chartweek").getContext("2d");
				ctxx.canvas.width = 1430;
				ctxx.canvas.height = 380;
			}
			if($scope.analylineChartday == 1){
				var ctxx = document.getElementById("analyline-chartday").getContext("2d");
				ctxx.canvas.width = 1430;
				ctxx.canvas.height = 380;
			}
			
			window.myLine = new Chart(ctxx).Line(analyLineChartData,{
			    responsive: true,
			   // scaleOverride: true,
			   // scaleSteps: 4,
			    //scaleStepWidth:2,
			    tooltipCornerRadius: 0,
			    tooltipTemplate: "<%= value %>",
			    multiTooltipTemplate: "<%= value %>",
			});

		});
		
	}

	
$scope.maillineChartMap = function(lastTime,analyType){
	apiserviceAnalytics.getMailDaysUserStats($routeParams.vin, lastTime, analyType).then(function(data){
		
			var randomScalingFactor = [10,20,30,40,50,60,70,80];
			var mailLineChartData = {
			    labels: response.months,
			    datasets: [
			      {
			          label: "My Second dataset",
			          fillColor: "rgba(49, 157, 181,0.2)",
			          strokeColor: "#319DB5",
			          pointColor: "#319DB5",
			          pointStrokeColor: "#fff",
			          pointHighlightFill: "#fff",
			          pointHighlightStroke: "#319DB5",
			          data: response.allVisitor
			      }
			    ]
			}
			if($scope.maillineChartmonth == 1){
				var ctxMail = document.getElementById("mailline-chart").getContext("2d");
				ctxMail.canvas.width = 1430;
				ctxMail.canvas.height = 380;
			}
			if($scope.maillineChartweek == 1){
				var ctxMail = document.getElementById("mailline-chartweek").getContext("2d");
				ctxMail.canvas.width = 1430;
				ctxMail.canvas.height = 380;
			}
			if($scope.maillineChartday == 1){
				var ctxMail = document.getElementById("mailline-chartday").getContext("2d");
				ctxMail.canvas.width = 1430;
				ctxMail.canvas.height = 380;
			}
			
			window.myLine = new Chart(ctxMail).Line(mailLineChartData,{
			    responsive: true,
			   // scaleOverride: true,
			   // scaleSteps: 4,
			   // scaleStepWidth:2,
			    tooltipCornerRadius: 0,
			    tooltipTemplate: "<%= value %>",
			    multiTooltipTemplate: "<%= value %>",
			});

		});
		
	}

	
$scope.maildayFunction = function(){
	console.log($scope.mailType)
	$scope.maillineChartMap("day",$scope.mailType);
	$scope.maillineChartday = 1;
	$scope.maillineChartweek = 0;
	$scope.maillineChartmonth = 0;
}

$scope.mailweekFunction = function(){
	console.log($scope.mailType)
	$scope.maillineChartMap("week",$scope.mailType);
	$scope.maillineChartday = 0;
	$scope.maillineChartweek = 1;
	$scope.maillineChartmonth = 0;
}

$scope.mailmonthFunction = function(){
	console.log($scope.mailType)
	$scope.maillineChartMap("month",$scope.mailType);
	$scope.maillineChartday = 0;
	$scope.maillineChartweek = 0;
	$scope.maillineChartmonth = 1;
}	


	$scope.analydayFunction = function(){
		console.log($scope.analyType)
		$scope.analylineChartMap("day",$scope.analyType);
		$scope.analylineChartday = 1;
		$scope.analylineChartweek = 0;
		$scope.analylineChartmonth = 0;
	}
	
	$scope.analyweekFunction = function(){
		console.log($scope.analyType)
		$scope.analylineChartMap("week",$scope.analyType);
		$scope.analylineChartday = 0;
		$scope.analylineChartweek = 1;
		$scope.analylineChartmonth = 0;
	}
	
	$scope.analymonthFunction = function(){
		console.log($scope.analyType)
		$scope.analylineChartMap("month",$scope.analyType);
		$scope.analylineChartday = 0;
		$scope.analylineChartweek = 0;
		$scope.analylineChartmonth = 1;
	}

	/*$scope.goToVisitors = function(analyType) {
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
	
	$scope.goToSessionsData = function() {
		$location.path('/sessionsAnalytics');
	}
	
	$scope.goToheatMapInfo = function() {
		$location.path('/heatMapInfoAnalytics');
	}
	
	$scope.goToVehicleInfo = function(){
		$location.path('/allVehicleSessions');
	}*/
	
	$scope.goToVehicalInfo = function(){
		$location.path('/goToVehicalInfo/'+$routeParams.id+"/"+$routeParams.vin+"/"+$routeParams.status);
	}
	
	
}]);



angular.module('newApp')
.controller('heatMapInfoCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,apiserviceAnalytics) {
	$scope.showHeatMap = 0;
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
		                                 { name: 'title', displayName: 'Title', width:'30%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'showUrl', displayName: 'ShowUrl', width:'40%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'value_percent', displayName: 'Value Percent', width:'25%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'edit', displayName: '', width:'9%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
    		                                 cellTemplate:'<a ng-click="grid.appScope.showheatmap(row)"><img class="mb-2" style="margin-left: 8px;width: 21px;" title="View heatmap for this page" src="https://cdn.staticstuff.net/media/icon_heatmap.png"></a>', 
		                                 
		                                 },
		                                 ];
	
		 $scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		          $scope.gridOptions.data = $filter('filter')($scope.heatMapList,{'title':grid.columns[0].filters[0].term,'showUrl':grid.columns[1].filters[0].term,'value_percent':grid.columns[2].filters[0].term},undefined);
		        });
	   		
 		};
 		
 		apiserviceAnalytics.getHeatMapList(30).then(function(data){
		 
				//console.log(data[0].dates[0].items);
				
				$scope.gridOptions.data = data[0].dates[0].items;
				$scope.heatMapList = data[0].dates[0].items;
				angular.forEach($scope.gridOptions.data, function(value, key) {
					$scope.array = value.url.split('#');
					$scope.gridOptions.data[key].showUrl = $scope.array[0];
					$scope.heatMapList[key].showUrl = $scope.array[0];
				});
				
				console.log($scope.gridOptions.data);
				console.log($scope.heatMapList);
			$('#sliderBtn').click();
		});
		 
		 
		 $scope.showheatmap = function(row){
			 $scope.showHeatMap = 1;
			 console.log(row.entity.url);
			 var data = row.entity.url;
			 $('#heatMapModal').modal();
			 $(".container-iframe-sit").attr("src",data);
			 
		 }
		 
		 
			$scope.goToVisitors = function() {
				$location.path('/visitorsAnalytics');
			}
			
			 $scope.goToVideoAnalytics = function() {
					$location.path('/goToVideoAnalytics');
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
			
			$scope.goCampaignss = function(){
				$location.path('/CampaignsAnalytics');
			}
			
			$scope.goToContent = function() {
				$location.path('/contentAnalytics');
			}
			
			$scope.goToSessionsData = function() {
				$location.path('/sessionsAnalytics');
			}
			
			$scope.goToActions = function() {
				$location.path('/actionsAnalytics');
			}
	
			$scope.goToVehicleInfo = function(){
				$location.path('/allVehicleSessions');
			}
	
}]);


angular.module('newApp')
.controller('allVehicleSessionsDataCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','apiserviceAnalytics', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,apiserviceAnalytics) {
	
	$scope.lineChartweek = 0;
	$scope.lineChartmonth = 0;
	$scope.lineChartday = 0;

	$scope.lineChartMap = function(lasttime){
		console.log(lasttime);
		apiserviceAnalytics.getAllVehicleSession(lasttime).then(function(response){
		
			var randomScalingFactor = [10,20,30,40,50,60,70,80];
			var lineChartData = {
			    labels: response.months,
			    datasets: [
			      {
			          label: "My Second dataset",
			          fillColor: "rgba(49, 157, 181,0.2)",
			          strokeColor: "#319DB5",
			          pointColor: "#319DB5",
			          pointStrokeColor: "#fff",
			          pointHighlightFill: "#fff",
			          pointHighlightStroke: "#319DB5",
			          data: response.allVisitor
			      }
			    ]
			}
			if($scope.lineChartmonth == 1){
				var ctx = document.getElementById("line-chart").getContext("2d");
				ctx.canvas.width = 1430;
				ctx.canvas.height = 380;
			}
			if($scope.lineChartweek == 1){
				var ctx = document.getElementById("line-chartweek").getContext("2d");
				ctx.canvas.width = 1430;
				ctx.canvas.height = 380;
			}
			if($scope.lineChartday == 1){
				var ctx = document.getElementById("line-chartday").getContext("2d");
				ctx.canvas.width = 1430;
				ctx.canvas.height = 380;
				
			}
			
			window.myLine = new Chart(ctx).Line(lineChartData,{
			    responsive: true,
			   // scaleOverride: true,
			   // scaleSteps: 4,
			   // scaleStepWidth:2,
			    tooltipCornerRadius: 0,
			    tooltipTemplate: "<%= value %>",
			    multiTooltipTemplate: "<%= value %>",
			});

		});
	}

	$scope.weekFunction = function(){
		$scope.lineChartMap("week");
		$scope.lineChartweek = 1;
		$scope.lineChartmonth = 0;
		$scope.lineChartday = 0;
	}

	$scope.monthFunction = function(){
		$scope.lineChartMap("month");
		$scope.lineChartweek = 0;
		$scope.lineChartmonth = 1;
		$scope.lineChartday = 0;
	}

	$scope.dayFunction = function(){
		$scope.lineChartMap("day");
		$scope.lineChartday = 1;
		$scope.lineChartweek = 0;
		$scope.lineChartmonth = 0;
	}

	var lineChartData1 = {
		    labels: [1,2,3,4,5,6,7],
		    datasets: [
		      {
		          label: "My Second dataset",
		          fillColor: "rgba(49, 157, 181,0.2)",
		          strokeColor: "#319DB5",
		          pointColor: "#319DB5",
		          pointStrokeColor: "#fff",
		          pointHighlightFill: "#fff",
		          pointHighlightStroke: "#319DB5",
		          data: [0,0,0,1,0,2,0]
		      }
		    ]
		}
		var ctx1 = document.getElementById("line-chart1").getContext("2d");
		window.myLine = new Chart(ctx1).Line(lineChartData1, {
		    responsive: true,
		    scaleOverride: true,
		    tooltipCornerRadius: 0,
		    scaleSteps: 4,
		    scaleStepWidth:2,
		    showScale: false,
		    pointDot: false,
		    tooltipTemplate: "",
		    multiTooltipTemplate: "",
		});
		
		var ctx2 = document.getElementById("line-chart2").getContext("2d");
		window.myLine = new Chart(ctx2).Line(lineChartData1, {
		    responsive: true,
		    scaleOverride: true,
		    tooltipCornerRadius: 0,
		    scaleSteps: 4,
		    scaleStepWidth:2,
		    showScale: false,
		    pointDot: false,
		    tooltipTemplate: "",
		    multiTooltipTemplate: "",
		});
		
		var ctx2 = document.getElementById("line-chart3").getContext("2d");
		window.myLine = new Chart(ctx2).Line(lineChartData1, {
		    responsive: true,
		    scaleOverride: true,
		    tooltipCornerRadius: 0,
		    scaleSteps: 4,
		    scaleStepWidth:2,
		    showScale: false,
		    pointDot: false,
		    tooltipTemplate: "",
		    multiTooltipTemplate: "",
		});
		
		var ctx2 = document.getElementById("line-chart4").getContext("2d");
		window.myLine = new Chart(ctx2).Line(lineChartData1, {
		    responsive: true,
		    scaleOverride: true,
		    tooltipCornerRadius: 0,
		    scaleSteps: 4,
		    scaleStepWidth:2,
		    showScale: false,
		    pointDot: false,
		    tooltipTemplate: "",
		    multiTooltipTemplate: "",
		});
		
		var ctx2 = document.getElementById("line-chart5").getContext("2d");
		window.myLine = new Chart(ctx2).Line(lineChartData1, {
		    responsive: true,
		    scaleOverride: true,
		    tooltipCornerRadius: 0,
		    scaleSteps: 4,
		    scaleStepWidth:2,
		    showScale: false,
		    pointDot: false,
		    tooltipTemplate: "",
		    multiTooltipTemplate: "",
		});
		

		apiserviceAnalytics.getAllvehicleStatusList().then(function(data){
	
		$scope.lineChartMap("day");
		$scope.lineChartday = 1;
		console.log("data");
		console.log(data);
		$scope.followers = data.followers;
		$scope.pageview = data.pageview;
		$scope.avgSessDur = data.avgSessDur;
		$scope.users = data.users;
		$scope.enginesound = data.enginesound;
		$scope.virtualtour = data.virtualtour;
		
		var pieData = [
		               { value: data.newUser, color: "rgba(27, 184, 152,0.9)", highlight: "rgba(27, 184, 152,1)", label: "New Visitor" },
		               { value: data.users, color: "rgba(201, 98, 95,0.9)", highlight: "rgba(201, 98, 95,1)", label: "Returning Visitor" }
		           ];
		var ctx = document.getElementById("pie-chart visitorInfo").getContext("2d");
		window.myPie = new Chart(ctx).Pie(pieData, {
		    tooltipCornerRadius: 0
		});
		
		var pieData = [
		               { value: data.requestmoreinfo, color: "rgba(27, 184, 152,0.9)", highlight: "rgba(27, 184, 152,1)", label: "Submited" },
		               { value: data.requestmoreinfoshow, color: "rgba(201, 98, 95,0.9)", highlight: "rgba(201, 98, 95,1)", label: "Clicked" }
		           ];
		var ctx = document.getElementById("pie-chart requestmoreinfo").getContext("2d");
		window.myPie = new Chart(ctx).Pie(pieData, {
		    tooltipCornerRadius: 0
		});
		
			var pieData = [
			               { value: data.scheduletest, color: "rgba(27, 184, 152,0.9)", highlight: "rgba(27, 184, 152,1)", label: "Submited" },
			               { value: data.scheduletestshow, color: "rgba(201, 98, 95,0.9)", highlight: "rgba(201, 98, 95,1)", label: "Clicked" }
			           ];
			var ctx = document.getElementById("pie-chart scheduletest").getContext("2d");
			window.myPie = new Chart(ctx).Pie(pieData, {
			    tooltipCornerRadius: 0
			});
		
		var pieData = [
		               { value: data.tradeinapp, color: "rgba(27, 184, 152,0.9)", highlight: "rgba(27, 184, 152,1)", label: "Submited" },
		               { value: data.tradeinappshow, color: "rgba(201, 98, 95,0.9)", highlight: "rgba(201, 98, 95,1)", label: "Clicked" }
		           ];
		var ctx = document.getElementById("pie-chart trade").getContext("2d");
		window.myPie = new Chart(ctx).Pie(pieData, {
		    tooltipCornerRadius: 0
		});
		
		var pieData = [
		               { value: data.emailtofriend, color: "rgba(27, 184, 152,0.9)", highlight: "rgba(27, 184, 152,1)", label: "Submited" },
		               { value: data.emailtofriendshow, color: "rgba(201, 98, 95,0.9)", highlight: "rgba(201, 98, 95,1)", label: "Clicked" }
		           ];
		var ctx = document.getElementById("pie-chart emailtofriend").getContext("2d");
		window.myPie = new Chart(ctx).Pie(pieData, {
		    tooltipCornerRadius: 0
		});
	});


	$scope.langshow = 0;
	$scope.webBroshow = 0;
	apiserviceAnalytics.getAllVehicleDemographics().then(function(data){
	
		$scope.selectedTab = 1;
		$scope.langshow = 1;
		console.log(data);
		$scope.langmap = data.language;
		$scope.webBrosmap = data.webBrowser;
		$scope.operatingSystem = data.operatingSystem;
		$scope.location = data.location;
		$scope.screenResoluations = data.screenResoluation;
		console.log($scope.screenResoluation);
	});


	$scope.showBrowerCount = function(){
		$scope.langshow = 0;
		$scope.webBroshow = 1;
		$scope.operatingshow = 0;
		$scope.locationshow = 0;
		$scope.screenResoluation = 0;
	}

	$scope.showlanguageCount = function(){
		$scope.langshow = 1;
		$scope.webBroshow = 0;
		$scope.operatingshow = 0;
		$scope.locationshow = 0;
		$scope.screenResoluation = 0;
	}

	$scope.showLocationCount = function(){
		$scope.locationshow = 1;
		$scope.langshow = 0;
		$scope.webBroshow = 0;
		$scope.operatingshow = 0;
		$scope.screenResoluation = 0;
	}

	$scope.showOperatingCount = function(){
		$scope.operatingshow = 1;
		$scope.langshow = 0;
		$scope.webBroshow = 0;
		$scope.locationshow = 0;
		$scope.screenResoluation = 0;
	}

	$scope.showscreenResoluationCount = function(){
		$scope.operatingshow = 0;
		$scope.langshow = 0;
		$scope.webBroshow = 0;
		$scope.locationshow = 0;
		$scope.screenResoluation = 1;
	}
	
	
	$scope.maildayFunction = function(){
		console.log($scope.mailType)
		$scope.maillineChartMap("day",$scope.mailType);
		$scope.maillineChartday = 1;
		$scope.maillineChartweek = 0;
		$scope.maillineChartmonth = 0;
	}

	$scope.mailweekFunction = function(){
		console.log($scope.mailType)
		$scope.maillineChartMap("week",$scope.mailType);
		$scope.maillineChartday = 0;
		$scope.maillineChartweek = 1;
		$scope.maillineChartmonth = 0;
	}

	$scope.mailmonthFunction = function(){
		console.log($scope.mailType)
		$scope.maillineChartMap("month",$scope.mailType);
		$scope.maillineChartday = 0;
		$scope.maillineChartweek = 0;
		$scope.maillineChartmonth = 1;
	}	


	$scope.analydayFunction = function(){
			console.log($scope.analyType)
			$scope.analylineChartMap("day",$scope.analyType);
			$scope.analylineChartday = 1;
			$scope.analylineChartweek = 0;
			$scope.analylineChartmonth = 0;
	}
		
	$scope.analyweekFunction = function(){
			console.log($scope.analyType)
			$scope.analylineChartMap("week",$scope.analyType);
			$scope.analylineChartday = 0;
			$scope.analylineChartweek = 1;
			$scope.analylineChartmonth = 0;
	}
		
	$scope.analymonthFunction = function(){
			console.log($scope.analyType)
			$scope.analylineChartMap("month",$scope.analyType);
			$scope.analylineChartday = 0;
			$scope.analylineChartweek = 0;
			$scope.analylineChartmonth = 1;
	}

	
$scope.analylineChartMap = function(lastTime,analyType){
	apiserviceAnalytics.getSessionDaysAllStats(lastTime, analyType).then(function(data){
		
			var randomScalingFactor = [10,20,30,40,50,60,70,80];
			var analyLineChartData = {
			    labels: response.months,
			    datasets: [
			      {
			          label: "My Second dataset",
			          fillColor: "rgba(49, 157, 181,0.2)",
			          strokeColor: "#319DB5",
			          pointColor: "#319DB5",
			          pointStrokeColor: "#fff",
			          pointHighlightFill: "#fff",
			          pointHighlightStroke: "#319DB5",
			          data: response.allVisitor
			      }
			    ]
			}
			if($scope.analylineChartmonth == 1){
				var ctxx = document.getElementById("analyline-chart").getContext("2d");
				ctxx.canvas.width = 1430;
				ctxx.canvas.height = 380;
			}
			if($scope.analylineChartweek == 1){
				var ctxx = document.getElementById("analyline-chartweek").getContext("2d");
				ctxx.canvas.width = 1430;
				ctxx.canvas.height = 380;
			}
			if($scope.analylineChartday == 1){
				var ctxx = document.getElementById("analyline-chartday").getContext("2d");
				ctxx.canvas.width = 1430;
				ctxx.canvas.height = 380;
			}
			
			window.myLine = new Chart(ctxx).Line(analyLineChartData,{
			    responsive: true,
			   // scaleOverride: true,
			    //scaleSteps: 4,
			    //scaleStepWidth:2,
			    tooltipCornerRadius: 0,
			    tooltipTemplate: "<%= value %>",
			    multiTooltipTemplate: "<%= value %>",
			});

		});
		
	}
	
$scope.analyType = "";

$scope.openUserPopup = function(analyType){
	console.log(analyType);
	$scope.analylineChartmonth = 1;
	$scope.analylineChartweek = 0;
	$scope.analylineChartday = 0;
	$('#userModal').modal();
	$scope.analyType = analyType;
	$scope.analylineChartMap("month",$scope.analyType);
	
}

$scope.openEmailpopup = function(mailType){
	console.log(mailType);
	$scope.maillineChartmonth = 1;
	$scope.maillineChartday = 0;
	$scope.maillineChartweek = 0;
	$scope.mailType = mailType;
	$('#emailModal').modal();
	$scope.maillineChartMap("month",$scope.mailType);
	
}

$scope.maillineChartMap = function(lastTime,analyType){
	console.log("hii printtttt");
	apiserviceAnalytics.getAllMailDaysUserStats(lastTime, analyType).then(function(data){
	
		var randomScalingFactor = [10,20,30,40,50,60,70,80];
		var mailLineChartData = {
		    labels: response.months,
		    datasets: [
		      {
		          label: "My Second dataset",
		          fillColor: "rgba(49, 157, 181,0.2)",
		          strokeColor: "#319DB5",
		          pointColor: "#319DB5",
		          pointStrokeColor: "#fff",
		          pointHighlightFill: "#fff",
		          pointHighlightStroke: "#319DB5",
		          data: response.allVisitor
		      }
		    ]
		}
		if($scope.maillineChartmonth == 1){
			var ctxMail = document.getElementById("mailline-chart").getContext("2d");
			ctxMail.canvas.width = 1430;
			ctxMail.canvas.height = 380;
		}
		if($scope.maillineChartweek == 1){
			var ctxMail = document.getElementById("mailline-chartweek").getContext("2d");
			ctxMail.canvas.width = 1430;
			ctxMail.canvas.height = 380;
		}
		if($scope.maillineChartday == 1){
			var ctxMail = document.getElementById("mailline-chartday").getContext("2d");
			ctxMail.canvas.width = 1430;
			ctxMail.canvas.height = 380;
		}
		
		window.myLine = new Chart(ctxMail).Line(mailLineChartData,{
		    responsive: true,
		   // scaleOverride: true,
		   // scaleSteps: 4,
		   // scaleStepWidth:2,
		    tooltipCornerRadius: 0,
		    tooltipTemplate: "<%= value %>",
		    multiTooltipTemplate: "<%= value %>",
		});

	});
	
}

	
	
	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	 $scope.goToVideoAnalytics = function() {
			$location.path('/goToVideoAnalytics');
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
	
	$scope.goCampaignss = function(){
		$location.path('/CampaignsAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
	$scope.goToSessionsData = function() {
		$location.path('/sessionsAnalytics');
	}
	
	$scope.goToheatMapInfo = function() {
		$location.path('/heatMapInfoAnalytics');
	}
	
	$scope.goToActions = function() {
		$location.path('/actionsAnalytics');
	}
	
	
}]);