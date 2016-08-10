angular.module('app.apiserviceAnalytics', [])
.service('apiserviceAnalytics', function($http,$q){

	var accessUrl = '';

	this.getVisitorList=function(startDateFilter, endDateFilter){
		var defer = $q.defer();
		
		$http.get('/getVisitorList/'+startDateFilter+'/'+endDateFilter).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getVisitorData=function(visitorInfos){
		var defer = $q.defer();
		$http.get('/getVisitorData/'+visitorInfos).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getSessionData=function(sessionId, startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getSessionData/'+sessionId+'/'+startDateFilter+'/'+endDateFilter).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getIPAddress=function(ipAddressInfo){
		var defer = $q.defer();
		$http.get('/getIPAddress/'+ipAddressInfo).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getTrafficSourceData=function(trafficSourceTitle, startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getTrafficSourceData/'+trafficSourceTitle+"/"+startDateFilter+"/"+endDateFilter).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEngTimeData=function(engTimeTitle, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getEngTimeData/'+engTimeTitle+"/"+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEngActionData=function(engActionTitle, startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getEngActionData/'+engActionTitle+"/"+startDateFilter+"/"+endDateFilter).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getreferrerTypeData=function(engActionTitle, locationFlag, startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getreferrerTypeData/'+engActionTitle+"/"+locationFlag+"/"+startDateFilter+"/"+endDateFilter).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getVisitorDataForLanding=function(idForDomain, flagForLanding, startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getVisitorDataForLanding/'+idForDomain+"/"+flagForLanding+"/"+startDateFilter+"/"+endDateFilter).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getVisitorDataForLanding=function(idForRefferal, flagForLanding, startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getVisitorDataForLanding/'+idForRefferal+"/"+flagForLanding+"/"+startDateFilter+"/"+endDateFilter).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
	this.getActionListData=function(startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getActionListData/'+startDateFilter+"/"+endDateFilter).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEngagementAct=function(startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getEngagementAct/'+startDateFilter+"/"+endDateFilter).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEngagementTime=function(startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getEngagementTime/'+startDateFilter+"/"+endDateFilter).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getTrafficScoures=function(startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getTrafficScoures/'+startDateFilter+"/"+endDateFilter).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getActiveVisitors=function(startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getActiveVisitors/'+startDateFilter+"/"+endDateFilter).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEngActionChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getEngActionChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEngTimeChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getEngTimeChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getTrafficChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getTrafficChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEntranceData=function(idForGrid, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getEntranceData/'+idForGrid+"/"+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getPagesData=function(idForPages, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getPagesData/'+idForPages+"/"+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEventData=function(idForEvent, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getEventData/'+idForEvent+"/"+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getExitData=function(idForExit, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getExitData/'+idForExit+"/"+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getPagesListDale=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getPagesListDale/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEntranceList=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getEntranceList/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getExit=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getExit/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getDownloads=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getDownloads/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEvent=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getEvent/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getMediaDetails=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getMediaDetails/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getDomains=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getDomains/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getMediaData=function(urlForMedia, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getMediaData/'+urlForMedia+'/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getPagesChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getPagesChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEntranceChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getEntranceChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getExitChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getExitChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getDownloadsChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getDownloadsChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEventChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getEventChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getMediaChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getMediaChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getshowDomainsChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getshowDomainsChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getVideoAction=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getVideoAction/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getbrowser=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getbrowser/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getOperatingSystem=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getOperatingSystem/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getScreenResolution=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getScreenResolution/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getHardware=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getHardware/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getbrowserdata=function(titleForBrowser, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getbrowserdata/'+titleForBrowser+'/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getOperatingSystemdata=function(titleForOS, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getOperatingSystemdata/'+titleForOS+'/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getResolutiondata=function(titleForScreen, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getResolutiondata/'+titleForScreen+'/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getHardwaredata=function(titleForHardware, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getHardwaredata/'+titleForHardware+'/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getBrowserChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getBrowserChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getshowOperatingSystemChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getshowOperatingSystemChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getScreenResolutionChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getScreenResolutionChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getHardwareChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getHardwareChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveCampaigns=function(campaign){
		var defer = $q.defer();
		$http.post('/saveCampaigns',campaign).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveCampaigns=function(campaign){
		var defer = $q.defer();
		$http.post('/saveCampaigns',campaign).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getCampaign=function(){
		var defer = $q.defer();
		$http.get('/getCampaign').success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getActionList=function(time){
		var defer = $q.defer();
		$http.get('/getActionList/'+time).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getSearchesListDale=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getSearchesListDale/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getKeywordsList=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getKeywordsList/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEngines=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getEngines/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getRecent=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getRecent/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getNewestUni=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getNewestUni/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getSearchesLocal=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getSearchesLocal/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getRankings=function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getRankings/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getDataForSearch=function(url, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getDataForSearch/'+url+'/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getsearchInfo=function(idForSearch, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getsearchInfo/'+idForSearch+'/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEngineInfo=function(idForEngine, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getEngineInfo/'+idForEngine+'/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getRecentInfo=function(idForRecent, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getRecentInfo/'+idForRecent+'/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getNewestInfo=function(idForNewest, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getNewestInfo/'+idForNewest+'/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getRankingInfo=function(idForRanking, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getRankingInfo/'+idForRanking+'/'+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getSearchesPagesChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getSearchesPagesChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEnginesChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getEnginesChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getRankingsChart=function(urlobj){
		var defer = $q.defer();
		$http.post('/getRankingsChart',urlobj).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getContentList=function(){
		var defer = $q.defer();
		$http.get('/getContentList').success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getVehiclePriceLogs=function(id, vin, status){
		var defer = $q.defer();
		$http.get('/getVehiclePriceLogs/'+id+'/'+vin+'/'+status).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getCustomerRequest=function(id, vin, status, startDate,endDate){
		var defer = $q.defer();
		$http.get('/getCustomerRequest/'+id+'/'+vin+'/'+status+'/'+startDate+'/'+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getCustomerRequestFlag=function(id, vin, status, startDate,endDate){
		var defer = $q.defer();
		$http.get('/getCustomerRequestFlag/'+id+'/'+vin+'/'+status+'/'+startDate+'/'+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getCustomerRequestLeads=function(id, vin, status, startDate,endDate){
		var defer = $q.defer();
		$http.get('/getCustomerRequestLeads/'+id+'/'+vin+'/'+status+'/'+startDate+'/'+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getviniewsChartLeads=function(id, vin, status, startDate,endDate){
		var defer = $q.defer();
		$http.get('/getviniewsChartLeads/'+id+'/'+vin+'/'+status+'/'+startDate+'/'+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getCustomerLounchDateFlag=function(id, vin, status, startDate,endDate){
		var defer = $q.defer();
		$http.get('/getCustomerLounchDateFlag/'+id+'/'+vin+'/'+status+'/'+startDate+'/'+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getFollowerLeads=function(id, vin, status, startDate,endDate){
		var defer = $q.defer();
		$http.get('/getFollowerLeads/'+id+'/'+vin+'/'+status+'/'+startDate+'/'+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getSessionDaysVisitorsStats=function(vin, lasttime){
		var defer = $q.defer();
		$http.get('/getSessionDaysVisitorsStats/'+vin+'/'+lasttime).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getStatusList=function(vin){
		var defer = $q.defer();
		$http.get('/getStatusList/'+vin).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getDemographics=function(vin){
		var defer = $q.defer();
		$http.get('/getDemographics/'+vin).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getSessionDaysUserStats=function(vin, lasttime, analyType){
		var defer = $q.defer();
		$http.get('/getSessionDaysUserStats/'+vin+'/'+lasttime+'/'+analyType).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getMailDaysUserStats=function(vin, lasttime, analyType){
		var defer = $q.defer();
		$http.get('/getMailDaysUserStats/'+vin+'/'+lasttime+'/'+analyType).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getHeatMapList=function(time){
		var defer = $q.defer();
		$http.get('/getHeatMapList/'+time).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllVehicleSession=function(time){
		var defer = $q.defer();
		$http.get('/getAllVehicleSession/'+time).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllvehicleStatusList=function(){
		var defer = $q.defer();
		$http.get('/getAllvehicleStatusList').success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllVehicleDemographics=function(){
		var defer = $q.defer();
		$http.get('/getAllVehicleDemographics').success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getSessionDaysAllStats=function(lasttime, analyType){
		var defer = $q.defer();
		$http.get('/getSessionDaysAllStats/'+lasttime+'/'+analyType).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllMailDaysUserStats=function(lasttime, analyType){
		var defer = $q.defer();
		$http.get('/getAllMailDaysUserStats/'+lasttime+'/'+analyType).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
})