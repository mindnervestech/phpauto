angular.module('app.apiserviceMoreInfo', [])
.service('apiserviceMoreInfo', function($http,$q){

	var accessUrl = '';

	this.getSelectedLeadType=function(){
		var defer = $q.defer();
		
		$http.get('/getSelectedLeadType').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAllContactInfo=function(){
		var defer = $q.defer();
		
		$http.get('/getAllContactInfo').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.requestInfoMarkRead=function(flag, id){
		var defer = $q.defer();
		
		$http.get('/requestInfoMarkRead/'+flag+'/'+id).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};

	this.getAllOtherLeadInfo=function(leadId){
		var defer = $q.defer();
		
		$http.get('/getAllOtherLeadInfo/'+leadId).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.changeAssignedUser=function(id, userKey, leadType){
		var defer = $q.defer();
		
		$http.get('/changeAssignedUser/'+id+'/'+userKey+'/'+leadType).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Claimed Successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getSalesUserValue=function(){
		var defer = $q.defer();
		
		$http.get('/getSalesUserValue').success(function(data) {
			
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deletePremiumLead=function(id, leadType){
		var defer = $q.defer();
		
		$http.get('/deletePremiumLead/'+id+'/'+leadType).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Delete successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAllPremiumIn=function(){
		var defer = $q.defer();
		$http.get('/getAllPremiumIn').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.releaseLeads=function(id, leadType){
		var defer = $q.defer();
		
		$http.get('/releaseLeads/'+id+'/'+leadType).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Premium lead has been successfully released for everyone to claim ",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAllRequestInfo=function(){
		var defer = $q.defer();
		$http.get('/getAllRequestInfo').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllScheduleTest=function(){
		var defer = $q.defer();
		$http.get('/getAllScheduleTest').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.scheduleTestMarkRead=function(flag, id){
		var defer = $q.defer();
		
		$http.get('/scheduleTestMarkRead/'+flag+'/'+id).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};

	this.saveConfirmData=function(scheduleTestData){
		var defer = $q.defer();
		
		$http.post('/saveConfirmData/'+scheduleTestData).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAllTradeIn=function(){
		var defer = $q.defer();
		$http.get('/getAllTradeIn').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.tradeInMarkRead=function(flag, id){
		var defer = $q.defer();
		
		$http.get('/tradeInMarkRead/'+flag+'/'+id).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
})
