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
	
	this.sendComingSoonPOpUp = function(){
		var defer = $q.defer();
		$http.get('/sendComingSoonPOpUp').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.changeVehicleNotif = function(id){
		var defer = $q.defer();
		$http.get('/changeVehicleNotif/'+id).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getPlanMonday = function(){
		var defer = $q.defer();
		$http.get('/getPlanMonday').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getAcceptAndDecline = function(id,reason,decline){
		var defer = $q.defer();
		$http.get('/getAcceptAndDecline/'+id+'/'+reason+"/"+decline).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getAddPrice = function(id,price,arrivalD){
		var defer = $q.defer();
		$http.get('/getAddPrice/'+id+"/"+price+"/"+arrivalD).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Price Add successfully",
			});
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.sendComingSoonEmail = function(vin){
		var defer = $q.defer();
		$http.get('/sendComingSoonEmail/'+vin).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.setArrivelDate = function(id,aDate){
		var defer = $q.defer();
		$http.get('/setArrivelDate/'+id+"/"+aDate).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getScheduleDates = function(){
		var defer = $q.defer();
		$http.get('/getScheduleDates').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getToDoList = function(){
		var defer = $q.defer();
		$http.get('/getToDoList').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.restoreLead = function(id,leadType){
		var defer = $q.defer();
		$http.get('/restoreLead/'+id+"/"+leadType).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Lead Restore successfully",
			});
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.deleteCanceledLead = function(leadId,leadType){
		var defer = $q.defer();
		$http.get('/deleteCanceledLead/'+leadId+"/"+leadType).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Lead deleted successfully",
			});
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getScheduleBySelectedDate = function(data){
		var defer = $q.defer();
		$http.get('/getScheduleBySelectedDate/'+data).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getToDoBySelectedDate = function(data){
		var defer = $q.defer();
		$http.get('/getToDoBySelectedDate/'+data).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
	this.getAllScheduleTestAssigned = function(){
		var defer = $q.defer();
		$http.get('/getAllScheduleTestAssigned').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllTradeInSeen = function(){
		var defer = $q.defer();
		$http.get('/getAllTradeInSeen').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getReminderPopup = function(){
		var defer = $q.defer();
		$http.get('/getReminderPopup').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllMakeList = function(){
		var defer = $q.defer();
		$http.get('/getAllMakeList').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getVisitorStats = function(){
		var defer = $q.defer();
		$http.get('/getVisitorStats').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
	this.getVisitedData = function(id,type,filterBy,search,searchBy,vehicles,startD,endD){
		var defer = $q.defer();
		$http.get('/getVisitedData/'+id+"/"+type+'/'+filterBy+'/'+search+'/'+searchBy+'/'+vehicles+'/'+startD+'/'+endD).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getHeardAboutUs = function(){
		var defer = $q.defer();
		$http.get('/getHeardAboutUs').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
	this.getStockDetails = function(item){
		var defer = $q.defer();
		$http.get('/getStockDetails/'+item).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.createLead = function(lead){
		var defer = $q.defer();
		$http.post('/createLead',lead).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.addHeard = function(hearedFrom){
		var defer = $q.defer();
		$http.get('/addHeard/'+hearedFrom).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getHeardAboutUs = function(){
		var defer = $q.defer();
		$http.get('/getHeardAboutUs').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.createLeads = function(lead, file){
		var defer = $q.defer();
		if(file != undefined){
			$upload.upload({
	            url : '/createLead',
	            method: 'POST',
	            file:files,
	            data:lead
	         }).success(function(data) {
	   			$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Lead created successfully",
				});
	   		 });
		}else{
			$http.post('/createLead',lead).success(function(data) {
				defer.resolve(data);
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Lead created successfully",
				});
			});
		}
		
		return defer.promise;
	};
	
	
	this.getModels = function(makeSelect){
		var defer = $q.defer();
		$http.get('/getModels/'+makeSelect).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getMakes = function(){
		var defer = $q.defer();
		$http.get('/getMakes').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllModelList = function(value){
		var defer = $q.defer();
		$http.get('/getAllModelList/'+value).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAnalystData = function(){
		var defer = $q.defer();
		$http.get('/getAnalystData').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getSalesUserOnly = function(locationValue){
		var defer = $q.defer();
		$http.get('/getSalesUserOnly/'+locationValue).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getSalesUserList = function(locationId){
		var defer = $q.defer();
		$http.get('/getSalesUserList/'+locationId).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getSalesUser = function(){
		var defer = $q.defer();
		$http.get('/getSalesUser').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAssignedLeads = function(){
		var defer = $q.defer();
		$http.get('/getAssignedLeads').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getNewToDoCount = function(){
		var defer = $q.defer();
		$http.get('/getNewToDoCount').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	this.setLeadSeen = function(){
		var defer = $q.defer();
		$http.get('/setLeadSeen').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.setTodoSeen = function(){
		var defer = $q.defer();
		$http.get('/setTodoSeen').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};

	
	this.getTestDirConfir = function(){
		var defer = $q.defer();
		$http.get('/getTestDirConfir').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllCompletedLeads = function(){
		var defer = $q.defer();
		$http.get('/getAllCompletedLeads').success(function(data) {
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
	
	this.getAllCanceledLeads = function(){
		var defer = $q.defer();
		$http.get('/getAllCanceledLeads').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.changeAssignedUser = function(cancelId, changedUser, leadType){
		var defer = $q.defer();
		$http.get('/changeAssignedUser/'+cancelId+'/'+changedUser+'/'+leadType).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllSalesPersonScheduleTestAssigned = function(id){
		var defer = $q.defer();
		$http.get('/getAllSalesPersonScheduleTestAssigned/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllSalesPersonRequestInfoSeen = function(id){
		var defer = $q.defer();
		$http.get('/getAllSalesPersonRequestInfoSeen/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllSalesPersonOtherLead = function(id){
		var defer = $q.defer();
		$http.get('/getAllSalesPersonOtherLead/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllSalesPersonContactUsSeen = function(id){
		var defer = $q.defer();
		$http.get('/getAllSalesPersonContactUsSeen/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllSalesPersonTradeInSeen = function(id){
		var defer = $q.defer();
		$http.get('/getAllSalesPersonTradeInSeen/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
	
	
	
		
	
	
	
	
	
})
