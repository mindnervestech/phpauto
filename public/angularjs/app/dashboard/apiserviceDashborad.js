angular.module('app.apiserviceDashborad', [])
.service('apiserviceDashborad', function($http,$q,$upload){

	var accessUrl = '';

	this.getLocationDays=function(){
		var defer = $q.defer();
		
		$http.get('/getLocationDays').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getDealerProfile = function(){
		var defer = $q.defer();
		$http.get('/getDealerProfile').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
	this.getAllVehicles = function(){
		var defer = $q.defer();
		$http.get('/getAllVehicles').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getUserType = function(){
		var defer = $q.defer();
		$http.get('/getUserType').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.updateUserComment = function(userId, userComment){
		var defer = $q.defer();
		$http.get('/updateUserComment/'+userId+"/"+userComment).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getUserPermission = function(){
		var defer = $q.defer();
		$http.get('/getUserPermission').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getDataFromCrm = function(){
		var defer = $q.defer();
		$http.get('/getDataFromCrm').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.updateVehiclePrice = function(vin, price){
		var defer = $q.defer();
		$http.get('/updateVehiclePrice/'+vin+"/"+price).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	
	this.updateVehicleName = function(vin, name){
		var defer = $q.defer();
		$http.get('/updateVehicleName/'+vin+"/"+name).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getUserRole = function(){
		var defer = $q.defer();
		$http.get('/getUserRole').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getfindGmIsManager = function(){
		var defer = $q.defer();
		$http.get('/getfindGmIsManager').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getUserForMeeting = function(bestDt,bestTm,bestEndTm){
		var defer = $q.defer();
		$http.get('/getUserForMeeting/'+bestDt+"/"+bestTm+"/"+bestEndTm).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getUserAppointment = function(bestDt,bestTm,bestEndTm){
		var defer = $q.defer();
		$http.get('/getUserForMeeting/'+$scope.bestDt+"/"+$scope.bestTm+"/"+$scope.bestEndTm).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getAllLocation = function(timeSet){
		var defer = $q.defer();
		$http.get('/getAllLocation/'+timeSet).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.gmLocationManager = function(locationId){
		var defer = $q.defer();
		$http.get('/gmLocationManager/'+locationId).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getUserLocationByDateInfo = function(id,startD,endD,locOrPer){
		var defer = $q.defer();
		$http.get('/getUserLocationByDateInfo/'+id+"/"+startD+'/'+endD+'/'+locOrPer).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getPlanTarget = function(locOrPer){
		var defer = $q.defer();
		$http.get('/getPlanTarget/'+locOrPer).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.saveLeads = function(leadsTime){
		var defer = $q.defer();
		$http.post("/saveLeads",leadsTime).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getVisitorList = function(startDateV, endDateV){
		var defer = $q.defer();
		$http.get('/getVisitorList/'+startDateV+"/"+endDateV).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getSoldVehicleDetails = function(volumeStatStartDateId, volumeStatEndDateId){
		var defer = $q.defer();
		$http.get('/getSoldVehicleDetails/'+volumeStatStartDateId+"/"+volumeStatEndDateId).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getFinancialVehicleDetailsByBodyStyle = function(volumeStatStartDateId, volumeStatEndDateId){
		var defer = $q.defer();
		$http.get('/getFinancialVehicleDetailsByBodyStyle/'+volumeStatStartDateId+"/"+volumeStatEndDateId).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getFinancialVehicleDetails = function(volumeStatStartDateId, volumeStatEndDateId){
		var defer = $q.defer();
		$http.get('/getFinancialVehicleDetails/'+volumeStatStartDateId+"/"+volumeStatEndDateId).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getSoldVehicleDetailsAvgSale = function(volumeStatStartDateId, volumeStatEndDateId){
		var defer = $q.defer();
		$http.get('/getSoldVehicleDetailsAvgSale/'+volumeStatStartDateId+"/"+volumeStatEndDateId).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getAllRequestInfoSeen = function(){
		var defer = $q.defer();
		$http.get('/getAllRequestInfoSeen').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getAllContactUsSeen = function(){
		var defer = $q.defer();
		$http.get('/getAllContactUsSeen').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getAllLostAndCompLeads = function(){
		var defer = $q.defer();
		$http.get('/getAllLostAndCompLeads').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.saveCompletedLeads = function(duration, comment, id, typeOfLead){
		var defer = $q.defer();
		$http.get('/saveCompletedLeads/'+duration+'/'+comment+'/'+id+'/'+typeOfLead).success(function(data) {
			$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Completed successfully",
					});
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getSessionIdData = function(sessionId){
		var defer = $q.defer();
		$http.get('/getSessionIdData/'+sessionId).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	
	this.getSessionData = function(clickySessionId, startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getSessionData/'+clickySessionId+"/"+startDateFilter+"/"+endDateFilter).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getreferrerTypeData = function(typeOfReferrer, locationFlag, startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getreferrerTypeData/'+typeOfReferrer+"/"+locationFlag+"/"+startDateFilter+"/"+endDateFilter).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.geLendingPageData = function(landingpage){
		var defer = $q.defer();
		$http.post('/geLendingPageData',landingpage).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	
	this.getSession = function(clickySessionId, startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getSession/'+clickySessionId+"/"+startDateFilter+"/"+endDateFilter).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getCustomizationform = function(formType){
		var defer = $q.defer();
		$http.get('/getCustomizationform/'+formType).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.editLeads = function(editLeads,file){
		var defer = $q.defer();
		
		if(file != undefined){
			$upload.upload({
	            url : '/editLeads',
	            method: 'POST',
	            file:files,
	            data:$scope.editLeads
	         }).success(function(data) {
	        	 defer.resolve(data);
	   			console.log('success');
	   			
	   		 });
		}else{
			$http.post('/editLeads',$scope.editLeads).success(function(data) {
	  			 	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Lead's information has been successfully saved.",
				});
	  			 	defer.resolve(data);
			});	 	
		}
		
		return defer.promise;
	};
	
	
	this.getUsers = function(){
		var defer = $q.defer();
		$http.get('/getUsers').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getgroupInfo = function(){
		var defer = $q.defer();
		$http.get('/getgroupInfo').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.saveGroup = function(createGroup){
		var defer = $q.defer();
		$http.get('/saveGroup/'+createGroup).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "group saved successfully",
			});
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.deleteGroup = function(groupId){
		var defer = $q.defer();
		$http.get('/deleteGroup/'+groupId).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "group deleted successfully",
			});
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.saveContactsData = function(contactsDetails){
		var defer = $q.defer();
		$http.post('/saveContactsData',$scope.contactsDetails).success(function(data) {
			 $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "contact saved successfully",
				});
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	
	this.getComperSalePersonData = function(value, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getComperSalePersonData/'+value+"/"+startDate+"/"+endDate).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getDateRangSalePerson = function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getDateRangSalePerson/'+startDate+"/"+endDate).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getMonthlyVisitorsStats = function(startDate, endDate){
		var defer = $q.defer();
		$http.get('/getMonthlyVisitorsStats/'+startDate+"/"+endDate).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getVisitorOnline = function(){
		var defer = $q.defer();
		$http.get('/getVisitorOnline').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getUsersToAssign = function(){
		var defer = $q.defer();
		$http.get('/getUsersToAssign').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getClickyVisitorList = function(){
		var defer = $q.defer();
		$http.get('/getClickyVisitorList').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getPlanByMonthAndUser = function(userKey,monthNam){
		var defer = $q.defer();
		$http.get('/getPlanByMonthAndUser/'+userKey+'/'+monthNam).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	
	this.getPlanByMonthAndUserForLocation = function(userKey,monthNam){
		var defer = $q.defer();
		$http.get('/getPlanByMonthAndUserForLocation/'+userKey+'/'+monthNam).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getHeatMapListDale = function(startD,endD){
		var defer = $q.defer();
		$http.get('/getHeatMapListDale/'+startD+'/'+endD).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	
	
	
})
