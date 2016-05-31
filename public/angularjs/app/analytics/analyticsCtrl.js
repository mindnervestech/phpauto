angular.module('newApp')
.controller('VisitorsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	 var date = new Date();
	 $scope.typeOfInfo = 'Visitor log';
	  var startdate= new Date(date.getFullYear(), date.getMonth(), 1);
		$scope.startDate=$filter('date')(startdate, 'yyyy-MM-dd');
		$scope.endDate=$filter('date')(date, 'yyyy-MM-dd');
	
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
	
	
	 /*$scope.gridOptions.enableHorizontalScrollbar = 0;
		 $scope.gridOptions.enableVerticalScrollbar = 2;*/
		/* $scope.gridOptions.columnDefs = [
		                                 { name: 'geolocation', displayName: 'Location', width:'15%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'organization', displayName: 'Organization', width:'15%',cellEditableCondition: false,
			                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                                       if (row.entity.isRead === false) {
			                                         return 'red';
			                                     }
			                                	} ,
			                                 },
		                                 { name: 'ipAddress', displayName: 'IpAddress', width:'15%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'referrerDomain', displayName: 'Referrer_Domain', width:'15%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
		                                 },
		                                 { name: 'totalVisits', displayName: 'Total_Visits', width:'12%',cellEditableCondition: false,
		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                                       if (row.entity.isRead === false) {
		                                         return 'red';
		                                     }
		                                	} ,
	                                 },
		                                 { name: 'webBrowser', displayName: 'Web_Browser', width:'15%',cellEditableCondition: false,
	                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
		                                 },
		                                 { name: 'timePretty', displayName: 'Time_Pretty', width:'15%',cellEditableCondition: false,
		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	  		                                       if (row.entity.isRead === false) {
	  		                                         return 'red';
	  		                                     }
	 		                                	} ,
			                                 },
			                                { name: 'screenResolution', displayName: 'Screen_Resolution', width:'15%',cellEditableCondition: false,
			                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		  		                                       if (row.entity.isRead === false) {
		  		                                         return 'red';
		  		                                     }
		 		                                	} ,
				                              },
		                                 
			                                 
	                                 
		                                
 		                                 ];*/
	
		 $scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		          $scope.gridOptions.data = $filter('filter')($scope.visitiorList,{'geolocation':grid.columns[0].filters[0].term,'ip_address':grid.columns[1].filters[0].term,'referrer_domain':grid.columns[2].filters[0].term,'total_visits':grid.columns[3].filters[0].term,'web_browser':grid.columns[4].filters[0].term},undefined);
		        });
	   		
  		};
  		
  		
		/* $http.get('/getVisitorList/'+$scope.startDate+"/"+$scope.endDate)
			.success(function(data) {
				console.log(data);
				//console.log(data[0].dates[0].items);
			$scope.gridOptions.data = data;// data[0].dates[0].items;
			$scope.visitiorList = data;//data[0].dates[0].items;
			$('#sliderBtn').click();
		});*/
	
		 $scope.setShowVisitorsInfoType = function(typeOfInfo){
				$scope.typeOfInfo = typeOfInfo;
				$scope.DateWiseFind();
			} 
		 
		 $scope.DateWiseFind = function(){
			 var startDate = $("#cnfstartDateValue").val();
			var endDate = $("#cnfendDateValue").val();	 
			console.log(endDate);
			
			if(endDate == '' || startDate == ''){
				 var startDate = $scope.startDate;
					var endDate = $scope.endDate;
			}
			if($scope.typeOfInfo == 'Visitor log'){
				
				 $http.get('/getVisitorList/'+startDate+"/"+endDate)
					.success(function(data) {
					$scope.gridOptions.data = data;
					//console.log($scope.gridOptions.data);
					angular.forEach($scope.gridOptions.data, function(value, key) {
						var array = value.timePretty.split(',');
						value.timeSet = array[1];
						value.timeTotal = $filter('date')(value.timeTotal, 'hh:mm:ss');
						
					});
					 console.log($scope.gridOptions.data);
					$scope.visitiorList = data;
				});
				
				
				$scope.gridOptions.columnDefs = [
				                                 {name: 'timeSet', displayName: 'Time', width:'8%'},
				                                 {name: 'geolocation', displayName: 'Location', width:'10%'},
				                                 {name:'organization', displayName:'Internet Provider', width:'15%'},
				                                 {name:'actions', displayName:'Actions', width:'8%'},
				                                 {name:'timeTotal', displayName:'Time Spent', width:'10%'},
				                                 {name:'landingPage', displayName:'landing_page', width:'45%'},
				                                 {name:'Sear', displayName:'Search', width:'10%',
				                                	 cellTemplate:'<a target="_blank"><img class="mb-2" style="margin-left: 8px;width: 21px;" title="View heatmap for this page" src="https://con.tent.network/media/icon_spy.gif"></a>',
				                                 }
				                                 
				                             ];  
				
			}else if($scope.typeOfInfo == 'Action log'){
				
				
				 $http.get('/getVisitorList/'+startDate+"/"+endDate)
					.success(function(data) {
					$scope.gridOptions.data = data;
					console.log($scope.gridOptions.data);
					$scope.visitiorList = data;
				});
				 
				 
				$scope.gridOptions.columnDefs = [
									             {name: 'timePretty', displayName: 'Time'},
									             {name:'a', displayName:'User',
									            	 cellTemplate:'<div><span>{{row.entity.organization}}</span><br><span>{{row.entity.geolocation}},&nbsp;&nbsp {{row.entity.operatingSystem}},&nbsp;&nbsp {{row.entity.webBrowser}}</span></div>',
									             },
									             {name:'landingPage', displayName:'Action',
									            	 cellTemplate:'<div><span><a href="{{row.entity.landingPage}}">{{row.entity.landingPage}}</a></span></div>',
									             },
									             {name: 'referrerType', displayName: 'Referrer'}
									         ]
			}else if($scope.typeOfInfo == 'Engagement_action'){
				 $http.get('/getEngagementAction/'+startDate+"/"+endDate)
					.success(function(data) {
						console.log(data);
					$scope.gridOptions.data = data;
					console.log($scope.gridOptions.data);
					$scope.visitiorList = data;
					
					
				});
				 
				 $scope.gridOptions.columnDefs = [
										             {name: 'title', displayName: 'Actions', width:'60%'},
										             {name:'value', displayName:'Visitors', width:'20%',
										            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
										             },
										             {name:'stats_url', displayName:'', width:'20%',
										            	 cellTemplate:'<div class="col-sm-12"> <div class="col-sm-2"><span><img width="{{row.entity.value_percent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div> <div class="col-sm-6" style="margin-left:47px;"><span > {{row.entity.averagePercent}}% </span></div> </div>',
										            
										             }
										            	 ]
				 
			
			}
			
			else if($scope.typeOfInfo == 'Engagement_time'){
				$http.get('/getEngagementTime/'+startDate+"/"+endDate)
				.success(function(data) {
				$scope.gridOptions.data = data;
				console.log($scope.gridOptions.data);
				$scope.visitiorList = data;
				/*if ( inputString.indexOf(findme) > -1 ) {
				    alert( "found it" );
				}*/
				angular.forEach($scope.gridOptions.data, function(value, key) {
					if(value.title.indexOf("&amp;lt;") > -1){
						value.title = "<1m";
					}
					if(value.title.indexOf("&amp;gt;") > -1){
						var array = value.title.split('gt;');
						value.title = ">"+array[1];
					}
					
				});
				
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'title', displayName: 'Actions', width:'50%'},
									             {name:'value', displayName:'Visitors', width:'10%'},
									             {name:'value_percent', displayName:'value_Percent', width:'10%'},
									             {name:'stats_url', displayName:'', width:'30%',
									            	 cellTemplate:'<div class="col-sm-12"> <div class="col-sm-2"><span><img width="{{row.entity.value_percent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div> <div class="col-sm-6" style="margin-left:47px;"><span > {{row.entity.averagePercent}}% </span></div> </div>',
									             },
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Traffic_scoures'){
				$http.get('/getTrafficScoures/'+startDate+"/"+endDate)
				.success(function(data) {
				$scope.gridOptions.data = data[0].dates[0].items;
				console.log($scope.gridOptions.data);
				$scope.visitiorList = data[0].dates[0].items;
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'title', displayName: 'Source', width:'70%'},
									             {name:'value', displayName:'Visitors', width:'30%',
									            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
									             },
									            
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Active_visitors'){
				$http.get('/getActiveVisitors/'+startDate+"/"+endDate)
				.success(function(data) {
				$scope.gridOptions.data = data[0].dates[0].items;
				console.log($scope.gridOptions.data);
				$scope.visitiorList = data[0].dates[0].items;
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'title', displayName: 'Ip Address', width:'50%'},
									             {name:'value', displayName:'Visits', width:'20%',
									            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
									             },
									             {name:'stats_url', displayName:'', width:'20%',
									            	 cellTemplate:'<div><span><img width="{{row.entity.value_percent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									             },
									            
									         ]
				
			}
			
		 }
		 
		
	
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
.controller('goToContentInfoCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	 var date = new Date();
	 $scope.typeOfInfo = 'Pages';
	  var startdate= new Date(date.getFullYear(), date.getMonth(), 1);
		$scope.startDate=$filter('date')(startdate, 'yyyy-MM-dd');
		$scope.endDate=$filter('date')(date, 'yyyy-MM-dd');
	
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
	
	
	
		 $scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		          $scope.gridOptions.data = $filter('filter')($scope.visitiorList,{'geolocation':grid.columns[0].filters[0].term,'ip_address':grid.columns[1].filters[0].term,'referrer_domain':grid.columns[2].filters[0].term,'total_visits':grid.columns[3].filters[0].term,'web_browser':grid.columns[4].filters[0].term},undefined);
		        });
	   		
  		};
  		
  		
	
		 $scope.setShowPagesInfoType = function(typeOfInfo){
				$scope.typeOfInfo = typeOfInfo;
				$scope.DateWiseFind();
			} 
		 
		 $scope.DateWiseFind = function(){
			 var startDate = $("#cnfstartDateValue").val();
			var endDate = $("#cnfendDateValue").val();	 
			console.log(endDate);
			
			if(endDate == '' || startDate == ''){
				 var startDate = $scope.startDate;
					var endDate = $scope.endDate;
			}
			if($scope.typeOfInfo == 'Pages'){
				
				 $http.get('/getPagesListDale/'+startDate+"/"+endDate)
					.success(function(data) {
					$scope.gridOptions.data = data;
					//console.log($scope.gridOptions.data);
					
					 console.log($scope.gridOptions.data);
					$scope.visitiorList = data;
				});
				
				 $scope.gridOptions.columnDefs = [
										             {name: 'showUrl', displayName: 'Page', width:'60%',
										            	 cellTemplate:'<div><span>{{row.entity.showUrl}}</span><br><span>{{row.entity.title}}</span></div>'
										             },
										             {name: 'url', displayName: '', width:'10%',
										            	 cellTemplate:'<a href="{{row.entity.url}}" target="_blank"><img class="mb-2" style="margin-left: 8px;width: 21px;" title="View heatmap for this page" src="https://cdn.staticstuff.net/media/icon_heatmap.png"></a>'
										             },
										             {name:'value', displayName:'Views', width:'25%',
										            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
										             },
										            
										         ]
				
			}else if($scope.typeOfInfo == 'Entrance'){
				
				
				 $http.get('/getEntranceList/'+startDate+"/"+endDate)
					.success(function(data) {
						$scope.gridOptions.data = data[0].dates[0].items;
					console.log($scope.gridOptions.data);
					$scope.visitiorList = data;
				});
				 
				 
				 $scope.gridOptions.columnDefs = [
										             {name: 'showUrl', displayName: 'Page', width:'60%',
										            	 cellTemplate:'<div><span>{{row.entity.url}}</span><br><span>{{row.entity.title}}</span></div>'
										             },
										             {name:'value', displayName:'Views', width:'20%'},
										             {name:'value_percent', displayName:'value_Percent', width:'20%'},
										            
										         ]
			}else if($scope.typeOfInfo == 'Exit'){
				 $http.get('/getExit/'+startDate+"/"+endDate)
					.success(function(data) {
					$scope.gridOptions.data = data[0].dates[0].items;
					$scope.visitiorList = data[0].dates[0].items;
					
				});
				 
				 $scope.gridOptions.columnDefs = [
										             {name:'t', displayName:'Pages', width:'60%',
										            	 cellTemplate:'<div><span>{{row.entity.url}}<br>{{row.entity.title}}</span></div>',
										             },
										             {name:'v', displayName:'Visits', width:'20%',
										            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
										             },
										             {name:'stats_url', displayName:'', width:'20%',
										            	 cellTemplate:'<div><span><img width="{{row.entity.value_percent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
										             },
										            
										         ]
				 
			}else if($scope.typeOfInfo == 'Downloads'){
				$http.get('/getDownloads/'+startDate+"/"+endDate)
				.success(function(data) {
				$scope.gridOptions.data = data[0].dates[0].items;
				console.log($scope.gridOptions.data);
				console.log("fghjhhhhhhhhhhhhhhhhh");
				$scope.visitiorList = data[0].dates[0].items;
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name:'title', displayName:'File', width:'60%',
									            	 cellTemplate:'<div><span>{{row.entity.title}}<br>{{row.entity.url}}</span></div>',
									             },
									             {name:'v', displayName:'Downloads', width:'20%',
									            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
									             },
									             {name:'stats_url', displayName:'', width:'20%',
									            	 cellTemplate:'<div><span><img width="{{row.entity.value_percent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									             },
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Event'){
				$http.get('/getEvent/'+startDate+"/"+endDate)
				.success(function(data) {
				$scope.gridOptions.data = data[0].dates[0].items;
				console.log($scope.gridOptions.data);
				$scope.visitiorList = data[0].dates[0].items;
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'url', displayName: 'Event', width:'50%',
									            	 cellTemplate:'<div><span>{{row.entity.url}}<br>{{row.entity.title}}</span></div>',
									             },
									             {name: 'value', displayName: 'Hits', width:'30%',
									            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
									             },
									             {name:'stats_url', displayName:'', width:'30%',
									            	 cellTemplate:'<div><span><img width="{{row.entity.value_percent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									             },
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Media'){
				$http.get('/getActiveVisitors/'+startDate+"/"+endDate)
				.success(function(data) {
				$scope.gridOptions.data = data[0].dates[0].items;
				console.log($scope.gridOptions.data);
				$scope.visitiorList = data[0].dates[0].items;
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'title', displayName: 'Actions', width:'50%'},
									             {name:'value', displayName:'Visitors', width:'10%'},
									             {name:'value_percent', displayName:'value_Percent', width:'10%'},
									             {name:'stats_url', displayName:'', width:'30%',
									            	 cellTemplate:'<div><span><img width="{{row.entity.value_percent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
									             },
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Domains'){
				$http.get('/getDomains/'+startDate+"/"+endDate)
				.success(function(data) {
					console.log(data);
				$scope.gridOptions.data = data[0].dates[0].items;
				console.log($scope.gridOptions.data);
				$scope.visitiorList = data[0].dates[0].items;
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name:'title', displayName:'Domain', width:'60%',
									            	 cellTemplate:'<div><span>{{row.entity.title}}</span></div>',
									             },
									             {name:'v', displayName:'Hits', width:'20%',
									            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
									             },
									             {name:'stats_url', displayName:'', width:'20%',
									            	 cellTemplate:'<div><span><img width="{{row.entity.value_percent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
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
.controller('VideoAnalyticsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	 var date = new Date();
	// $scope.typeOfInfo = 'Pages';
	 $scope.typeOfInfo = 'video&video_meta=1';
	  var startdate= new Date(date.getFullYear(), date.getMonth(), 1);
		$scope.startDate=$filter('date')(startdate, 'yyyy-MM-dd');
		$scope.endDate=$filter('date')(date, 'yyyy-MM-dd');
	
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
			 var startDate = $("#cnfstartDateValue").val();
			var endDate = $("#cnfendDateValue").val();	 
			console.log(endDate);
			
			if(endDate == '' || startDate == ''){
				 var startDate = $scope.startDate;
					var endDate = $scope.endDate;
			}
			
			if($scope.typeOfInfo == 'video&video_meta=1'){
				console.log(">>>>>>>");
				 $http.get('/getVideoAction/'+startDate+"/"+endDate)
					.success(function(data) {
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
										            	 cellTemplate:'<div><span> {{row.entity.value_percent}}% &nbsp;&nbsp; <img width="{{row.entity.value_percent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
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
.controller('allPlatformsInfoCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
	
	
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
	
	
		$http.get('/getAllVehicleDemographicsInData').success(function(data) {
		console.log(data);
		$scope.langmap = data.language;
		$scope.webBrosmap = data.webBrowser;
		$scope.operatingSystem = data.operatingSystem;
		$scope.location = data.location;
		$scope.screenResoluations = data.screenResoluation;
		$scope.changeTab('browser');
	});
	
	$scope.valueSet = {};
	$scope.typSet = [];
	
	 
	$scope.changeTab = function(type){
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
			/*$.each($scope.screenResoluations, function(key, value) {
	            $scope.valueSet = {};
	            $scope.valueSet.key = key;
	            $scope.valueSet.value = value;
	            $scope.typSet.push($scope.valueSet);
	            
			});
			$scope.gridOptions.data = $scope.typSet;*/
			$scope.labelSet = "Hardware";
			
		}
		
		console.log($scope.typSet);
		
	}
	
}]);


angular.module('newApp')
.controller('CampaignsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
	
	$scope.showSet = 0;
	$scope.savecampaign = function(campaign){
		$scope.campaign = campaign;
		console.log($scope.campaign);
		$http.post("/saveCampaigns",$scope.campaign).success(function(data){
			 console.log("yessssssss");
		});
		
		$scope.showSet = 0;
	}
	
	 $http.get('/getCampaign')
		.success(function(data) {
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
.controller('ActionsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	
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
 		
 		
		 $http.get('/getActionList/'+30)
			.success(function(data) {
				console.log(data[0].dates[0].items);
			$scope.gridOptions.data = data[0].dates[0].items;
			$scope.ActionList = data[0].dates[0].items;
			$('#sliderBtn').click();
		});	
	
		 
		 $scope.lastDays = function(value){
			 $http.get('/getActionList/'+value)
				.success(function(data) {
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
.controller('BasicsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

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
.controller('SearchesCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	 var date = new Date();
	 $scope.typeOfInfo = 'Searches';
	  var startdate= new Date(date.getFullYear(), date.getMonth(), 1);
		$scope.startDate=$filter('date')(startdate, 'yyyy-MM-dd');
		$scope.endDate=$filter('date')(date, 'yyyy-MM-dd');
	
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
			} 
		 
		 $scope.DateWiseFind = function(){
			 var startDate = $("#cnfstartDateValue").val();
			var endDate = $("#cnfendDateValue").val();	 
			console.log(endDate);
			
			if(endDate == '' || startDate == ''){
				 var startDate = $scope.startDate;
					var endDate = $scope.endDate;
			}
			if($scope.typeOfInfo == 'Searches'){
				
				 $http.get('/getSearchesListDale/'+startDate+"/"+endDate)
					.success(function(data) {
					$scope.gridOptions.data = data[0].dates[0].items;
					//console.log($scope.gridOptions.data);
					
					 console.log($scope.gridOptions.data);
					$scope.visitiorList = data[0].dates[0].items;
				});
				
				 $scope.gridOptions.columnDefs = [
										             {name: 'title', displayName: 'Search', width:'60%'},
										             {name:'v', displayName:'Visitors', width:'20%',
										            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
										             },
										            
										         ]
				
			}else if($scope.typeOfInfo == 'Keywords'){
				
				
				 $http.get('/getKeywordsList/'+startDate+"/"+endDate)
					.success(function(data) {
						$scope.gridOptions.data = data[0].dates[0].items;
					console.log($scope.gridOptions.data);
					$scope.visitiorList = data;
				});
				 
				 
				 $scope.gridOptions.columnDefs = [
										             {name: 'title', displayName: 'Search', width:'60%'},
										             {name:'v', displayName:'Visitors', width:'20%',
										            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
										             },
										             {name:'stats_url', displayName:'Rank', width:'20%',
										            	 cellTemplate:'<div><span><img width="{{row.entity.value_percent}}" height="20" src="//con.tent.network/media/graph_bar_standard.gif"</span></div>',
										             },
										            
										         ]
				
			}else if($scope.typeOfInfo == 'Engines'){
				 $http.get('/getEngines/'+startDate+"/"+endDate)
					.success(function(data) {
					$scope.gridOptions.data = data[0].dates[0].items;
					$scope.visitiorList = data[0].dates[0].items;
					
				});
				 
				 $scope.gridOptions.columnDefs = [
										             {name: 'title', displayName: 'Engine', width:'60%'},
										             {name:'v', displayName:'Visitors', width:'20%',
										            	 cellTemplate:'<div><span>{{row.entity.value}}&nbsp;&nbsp;&nbsp;({{row.entity.value_percent}}%)</span></div>',
										             },
										            
										         ]
				 
			}else if($scope.typeOfInfo == 'Recent'){
				$http.get('/getRecent/'+startDate+"/"+endDate)
				.success(function(data) {
				$scope.gridOptions.data = data[0].dates[0].items;
				$scope.visitiorList = data[0].dates[0].items;
				
			});
			 
				 $scope.gridOptions.columnDefs = [
										             {name: 'time_pretty', displayName: 'Time', width:'20%'},
										             {name:'item', displayName:'Item', width:'80%'}
										            
										         ]
				
			}else if($scope.typeOfInfo == 'Newest Unique'){
				$http.get('/getNewestUni/'+startDate+"/"+endDate)
				.success(function(data) {
				$scope.gridOptions.data = data[0].dates[0].items;
				console.log($scope.gridOptions.data);
				$scope.visitiorList = data[0].dates[0].items;
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'time_pretty', displayName: 'Time', width:'20%'},
									             {name:'item', displayName:'Item', width:'80%'}
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Local Searches'){
				$http.get('/getSearchesLocal/'+startDate+"/"+endDate)
				.success(function(data) {
				$scope.gridOptions.data = data[0].dates[0].items;
				console.log($scope.gridOptions.data);
				$scope.visitiorList = data[0].dates[0].items;
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'time_pretty', displayName: 'Time', width:'20%'},
									             {name:'item', displayName:'Item', width:'80%'}
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Rankings'){
				$http.get('/getRankings/'+startDate+"/"+endDate)
				.success(function(data) {
					console.log(data);
				$scope.gridOptions.data = data[0].dates[0].items;
				console.log($scope.gridOptions.data);
				$scope.visitiorList = data[0].dates[0].items;
				
			});
			 
				$scope.gridOptions.columnDefs = [
									             {name: 'time_pretty', displayName: 'Search', width:'20%'},
									             {name:'item', displayName:'Rank', width:'80%'}
									            
									         ]
				
			}else if($scope.typeOfInfo == 'Rankings_SheerSEO'){
				
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
.controller('RefferersCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

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
 		
 		
		 $http.get('/getVisitorList/'+30)
			.success(function(data) {
				console.log(data[0].dates[0].items);
			$scope.gridOptions.data = data[0].dates[0].items;
			$scope.visitiorList = data[0].dates[0].items;
			$('#sliderBtn').click();
		});
		 
		 $scope.lastDays = function(value){
			 $http.get('/getVisitorList/'+value)
				.success(function(data) {
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
.controller('ContentCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	
	$http.get('/getContentList')
	.success(function(data) {
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
.controller('VehicleDateWiseCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
console.log("....................");

$http.get('/getVehiclePriceLogs/'+$routeParams.id+"/"+$routeParams.vin+"/"+$routeParams.status).success(function(data) {
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
		
		$http.get('/getCustomerRequest/'+$routeParams.id+"/"+$routeParams.vin+"/"+$routeParams.status+"/"+startDate+"/"+endDate).success(function(data) {
			console.log("succeess");
			
			$http.get('/getCustomerRequestFlag/'+$routeParams.id+"/"+$routeParams.vin+"/"+$routeParams.status+"/"+startDate+"/"+endDate).success(function(data1) {
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
		
		$http.get('/getCustomerRequestLeads/'+$routeParams.id+"/"+$routeParams.vin+"/"+$routeParams.status+"/"+startDate+"/"+endDate).success(function(data) {
			console.log("succeess");
			//$scope.dataValue = data;
			console.log(data);
			$http.get('/getCustomerRequestFlag/'+$routeParams.id+"/"+$routeParams.vin+"/"+$routeParams.status+"/"+startDate+"/"+endDate).success(function(data1) {
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
			$http.get('/getviniewsChartLeads/'+$routeParams.id+"/"+$routeParams.vin+"/"+$routeParams.status+"/"+startDate+"/"+endDate).success(function(data) {
				console.log("succeess");
				console.log(data);
				$http.get('/getCustomerRequestFlag/'+$routeParams.id+"/"+$routeParams.vin+"/"+$routeParams.status+"/"+startDate+"/"+endDate).success(function(data1) {
					console.log("succeess");
					console.log(data1);
					//$scope.obj.push(data1);
					data.push(data1);
					$http.get('/getCustomerLounchDateFlag/'+$routeParams.id+"/"+$routeParams.vin+"/"+$routeParams.status+"/"+startDate+"/"+endDate).success(function(data2) {
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
			
			$http.get('/getFollowerLeads/'+$routeParams.id+"/"+$routeParams.vin+"/"+$routeParams.status+"/"+startDate+"/"+endDate).success(function(data) {
				console.log("succeess");
				console.log(data);
				$http.get('/getCustomerRequestFlag/'+$routeParams.id+"/"+$routeParams.vin+"/"+$routeParams.status+"/"+startDate+"/"+endDate).success(function(data1) {
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
.controller('SessionsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
console.log("....................");
console.log($routeParams.vin);
$scope.lineChartweek = 0;
$scope.lineChartmonth = 0;
$scope.lineChartday = 0;


$scope.lineChartMap = function(lasttime){
	console.log(lasttime);
	$http.get('/getSessionDaysVisitorsStats/'+$routeParams.vin+'/'+lasttime).success(function(response){
			
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
	


$http.get('/getStatusList/'+$routeParams.vin).success(function(data) {
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
$http.get('/getDemographics/'+$routeParams.vin).success(function(data) {
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
		
		$http.get('/getSessionDaysUserStats/'+$routeParams.vin+'/'+lastTime+'/'+analyType).success(function(response){
			
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
		$http.get('/getMailDaysUserStats/'+$routeParams.vin+'/'+lastTime+'/'+analyType).success(function(response){
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
.controller('heatMapInfoCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
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
 		
 		
		 $http.get('/getHeatMapList/'+30)
			.success(function(data) {
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
.controller('allVehicleSessionsDataCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
	
	$scope.lineChartweek = 0;
	$scope.lineChartmonth = 0;
	$scope.lineChartday = 0;

	$scope.lineChartMap = function(lasttime){
		console.log(lasttime);
		$http.get('/getAllVehicleSession/'+lasttime).success(function(response){
				
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
		


	$http.get('/getAllvehicleStatusList').success(function(data) {
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
	$http.get('/getAllVehicleDemographics').success(function(data) {
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
		
		$http.get('/getSessionDaysAllStats/'+lastTime+'/'+analyType).success(function(response){
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
	$http.get('/getAllMailDaysUserStats/'+lastTime+'/'+analyType).success(function(response){
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