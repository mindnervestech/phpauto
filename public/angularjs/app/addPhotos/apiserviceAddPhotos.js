angular.module('app.apiserviceAddPhotos', [])
.service('apiserviceAddPhotos', function($http,$q){

	var accessUrl = '';

	this.getImageById=function(id, userLocationId){
		var defer = $q.defer();
		$http.get('http://www.glider-autos.com/getImageById/'+id+'/'+userLocationId).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.findLocation=function(){
		var defer = $q.defer();
		$http.get('/findLocation').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getImageById=function(id, userLocationId){
		var defer = $q.defer();
		$http.get('/getImageById/'+id+'/'+userLocationId).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.editImage=function(coords){
		var defer = $q.defer();
		$http.post('http://www.glider-autos.com/editImage',coords).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.editImage=function(coords){
		var defer = $q.defer();
		$http.post('/editImage',coords).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getSliderImageDataById=function(id){
		var defer = $q.defer();
		$http.get('/getSliderImageDataById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.editSliderImage=function(coords){
		var defer = $q.defer();
		$http.post('/editSliderImage',coords).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "All changed has been saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getCoverDataById=function(id){
		var defer = $q.defer();
		$http.get('/getCoverDataById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.editCovrImage=function(coords){
		var defer = $q.defer();
		$http.post('/editCovrImage',coords).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Croped Image has been saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getVehicleProfileDataById=function(id){
		var defer = $q.defer();
		$http.get('/getVehicleProfileDataById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.editVehicleProfileImage=function(coords){
		var defer = $q.defer();
		$http.post('/editVehicleProfileImage',coords).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Croped Image has been saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getWarDataById=function(id){
		var defer = $q.defer();
		$http.get('/getWarDataById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.editWarImage=function(coords){
		var defer = $q.defer();
		$http.post('/editWarImage',coords).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Croped Image has been saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getBlogDataById=function(id){
		var defer = $q.defer();
		$http.get('/getBlogDataById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.editBlogImage=function(coords){
		var defer = $q.defer();
		$http.post('/editBlogImage',coords).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Croped Image has been saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getCompareDataById=function(id){
		var defer = $q.defer();
		$http.get('/getCompareDataById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.editCompareImage=function(coords){
		var defer = $q.defer();
		$http.post('/editCompareImage',coords).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Croped Image has been saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getContactDataById=function(id){
		var defer = $q.defer();
		$http.get('/getContactDataById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.editContactImage=function(coords){
		var defer = $q.defer();
		$http.post('/editContactImage',coords).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Croped Image has been saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getFeaturedImageDataById=function(id){
		var defer = $q.defer();
		$http.get('/getFeaturedImageDataById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.editFeaturedImage=function(coords){
		var defer = $q.defer();
		$http.post('/editFeaturedImage',coords).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Croped Image has been saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getInventoryImageDataById=function(id){
		var defer = $q.defer();
		$http.get('/getInventoryImageDataById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.editInventoryImages=function(coords){
		var defer = $q.defer();
		$http.post('/editInventoryImages',coords).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Croped Image has been saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.savePosition=function(imageList){
		var defer = $q.defer();
		$http.post('/savePosition',imageList).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Position saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteImage = function(id){
		var defer = $q.defer();
		$http.get('/deleteImage/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.setDefaultImage = function(id){
		var defer = $q.defer();
		$http.get('/setDefaultImage/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.removeDefault = function(imageListId, id){
		var defer = $q.defer();
		$http.get('/removeDefault/'+imageListId+"/"+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getImagesByVin = function(num){
		var defer = $q.defer();
		$http.get('/getImagesByVin/'+num).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
})