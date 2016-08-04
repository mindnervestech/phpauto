angular.module('app.apiserviceCrm', [])
.service('apiserviceCrm', function($http,$q,$upload){

	var accessUrl = '';

	this.getCustomizationform = function(formType){
		var defer = $q.defer();
		$http.get('/getCustomizationform/'+formType).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.removeAllContactsData = function(){
		var defer = $q.defer();
		$http.get('/removeAllContactsData').success(function(data) {
			$.pnotify({
				title: "Success",
				type:'success',
				text: "File download successfully",
				});
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.exportContactsData = function(){
		var defer = $q.defer();
		$http.get('/exportContactsData').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getAllContactsData = function(){
		var defer = $q.defer();
		$http.get('/getAllContactsData').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getUsers = function(){
		var defer = $q.defer();
		$http.get('/getUsers').success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.getlocations = function(){
		var defer = $q.defer();
		$http.get('/getlocations').success(function(data) {
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
	
	this.getAllContactsByLocation = function(locatnId){
		var defer = $q.defer();
		$http.get('/getAllContactsByLocation/'+locatnId).success(function(data) {
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
	
	this.uploadContactsFile = function(logofile){
		var defer = $q.defer();
		 $upload.upload({
	            url : '/uploadContactsFile',
	            method: 'post',
	            file:logofile,
		   }).progress(function(evt) {
			  // $scope.showProgress = true;
			   //$scope.progress = parseInt((100.0 * evt.loaded) / evt.total)+"%";
	        }).success(function(data, status, headers, config) {
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "file uploaded successfully",
				});
	            defer.resolve(data);
	            
	        });
		
		return defer.promise;
	};
	
	
	this.updateContactsData = function(contactsDetails){
		var defer = $q.defer();
		$http.post('/updateContactsData',contactsDetails).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "contact updated successfully",
			});
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	
	this.addNewsLetter = function(newsletter,id){
		var defer = $q.defer();
		 $http.get('/addNewsLetter/'+newsletter+'/'+id).success(function(data) {
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.saveContactsData = function(contactsDetails){
		var defer = $q.defer();
		$http.post('/saveContactsData',contactsDetails).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "contact saved successfully",
			});
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
	
	this.deleteContactsById = function(contactId){
		var defer = $q.defer();
		 $http.get('/deleteContactsById/'+contactId).success(function(data) {
			 $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "group deleted successfully",
				});
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	this.callList = function(){
		var defer = $q.defer();
		 $http.get('/callList').success(function(data) {
			 $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Import successfully",
				 });
			defer.resolve(data);
		});
		
		return defer.promise;
	};
	
	
	
})
