angular.module('app.apiserviceHomePage', [])
.service('apiserviceHomePage', function($http,$q){

	var accessUrl = '';

	this.saveSliderPosition=function(sliderList){
		var defer = $q.defer();
		
		$http.post('/saveSliderPosition',sliderList).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Position saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveFeaturedPosition=function(featuredList){
		var defer = $q.defer();
		
		$http.post('/saveFeaturedPosition',featuredList).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Position saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveCoveredPosition=function(coverList){
		var defer = $q.defer();
		
		$http.post('/saveCoveredPosition',coverList).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Position saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveComparePosition=function(compareList){
		var defer = $q.defer();
		
		$http.post('/saveComparePosition',compareList).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Position saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
})