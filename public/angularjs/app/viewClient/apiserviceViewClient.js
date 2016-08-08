angular.module('app.apiserviceViewClient', [])
.service('apiserviceViewClient', function($http,$q){

	var accessUrl = '';

	this.getRemoveUser=function(id){
		var defer = $q.defer();
		
		$http.get('/getRemoveUser/'+id).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Remove User",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getSendDemoLink=function(id){
		var defer = $q.defer();
		
		$http.get('/getSendDemoLink/'+id).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Demo Link send",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.updateClientUser=function(register){
		var defer = $q.defer();
		
		$http.post('/updateClientUser',register).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Update successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getCarsDetails=function(){
		var defer = $q.defer();
		$http.get('/getCarsDetails').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getBoat=function(){
		var defer = $q.defer();
		$http.get('/getBoat').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getMotorcycles=function(){
		var defer = $q.defer();
		$http.get('/getMotorcycles').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getDesignerFurniture=function(){
		var defer = $q.defer();
		$http.get('/getDesignerFurniture').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getRealState=function(){
		var defer = $q.defer();
		$http.get('/getRealState').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAirplanes=function(){
		var defer = $q.defer();
		$http.get('/getAirplanes').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getServiceProvider=function(){
		var defer = $q.defer();
		$http.get('/getServiceProvider').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getLuxuryProducts=function(){
		var defer = $q.defer();
		$http.get('/getLuxuryProducts').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
})