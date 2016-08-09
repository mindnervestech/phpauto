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
	
	this.saveBlogPosition=function(blogList){
		var defer = $q.defer();
		$http.post('/saveBlogPosition',blogList).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Position saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveVehiclePosition=function(vehicleProfileList){
		var defer = $q.defer();
		$http.post('/saveVehiclePosition',vehicleProfileList).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Position saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveWarrantyPosition=function(warrantyList){
		var defer = $q.defer();
		$http.post('/saveWarrantyPosition',warrantyList).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Position saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveContactPosition=function(contactList){
		var defer = $q.defer();
		$http.post('/saveContactPosition',contactList).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Position saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllMakeList=function(){
		var defer = $q.defer();
		$http.get('/getAllMakeList').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getMakeWiseData=function(makeValue){
		var defer = $q.defer();
		$http.get('/getMakeWiseData/'+makeValue).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getSliderAndFeaturedImages=function(){
		var defer = $q.defer();
		$http.get('/getSliderAndFeaturedImages').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteSliderImage=function(id){
		var defer = $q.defer();
		$http.get('/deleteSliderImage/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteFeaturedImage=function(id){
		var defer = $q.defer();
		$http.get('/deleteFeaturedImage/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteInventoryImage=function(id){
		var defer = $q.defer();
		$http.get('/deleteInventoryImage/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteCvrImage=function(id){
		var defer = $q.defer();
		$http.get('/deleteCvrImage/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteBlogImage=function(id){
		var defer = $q.defer();
		$http.get('/deleteBlogImage/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteVehicleImage=function(id, makeValue){
		var defer = $q.defer();
		$http.get('/deleteVehicleImage/'+id+'/'+makeValue).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteCompareImage=function(id){
		var defer = $q.defer();
		$http.get('/deleteCompareImage/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteWarImage=function(id){
		var defer = $q.defer();
		$http.get('/deleteWarImage/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteContactImage=function(id){
		var defer = $q.defer();
		$http.get('/deleteContactImage/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveSiteHeading=function(siteHeading){
		var defer = $q.defer();
		$http.get('/saveSiteHeading/'+siteHeading).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveSitetestiMonial=function(testiMonial){
		var defer = $q.defer();
		$http.post('/saveSitetestiMonial',testiMonial).success(function(data) {
			$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Testimonails have been successfully saved",
				});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveSiteAboutUsHeader=function(header){
		var defer = $q.defer();
		$http.post('/saveSiteAboutUsHeader',header).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "AboutUs have been successfully saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveBlogHeader=function(header){
		var defer = $q.defer();
		$http.post('/saveBlogHeader',header).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Blog have been successfully saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveCompareHeader=function(header1){
		var defer = $q.defer();
		$http.post('/saveCompareHeader',header1).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Compare have been successfully saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveMakeFlag=function(makeFlag1){
		var defer = $q.defer();
		$http.get('/saveMakeFlag/'+makeFlag1).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveSocialFlag=function(socialFlag1){
		var defer = $q.defer();
		$http.get('/saveSocialFlag/'+socialFlag1).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveFinanceFlag=function(financeFlag1){
		var defer = $q.defer();
		$http.get('/saveFinanceFlag/'+financeFlag1).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveprofileHeader=function(ProfileHeader){
		var defer = $q.defer();
		$http.post('/saveprofileHeader',ProfileHeader).success(function(data) {
			 $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "profileHeader have been successfully saved",
				});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveHeader=function(header){
		var defer = $q.defer();
		$http.post('/saveHeader',header).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Blog have been successfully saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveContactHeader=function(header){
		var defer = $q.defer();
		$http.post('/saveContactHeader',header).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Contact have been successfully saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveSiteAboutUs=function(aboutUs){
		var defer = $q.defer();
		$http.post('/saveSiteAboutUs',aboutUs).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "AboutUs have been successfully saved",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveSiteDescription=function(siteDescription){
		var defer = $q.defer();
		$http.post('/saveSiteDescription',siteDescription).success(function(data) {
			
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getLogoData=function(){
		var defer = $q.defer();
		$http.get('/getLogoData').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveSiteTabText=function(tabText){
		var defer = $q.defer();
		$http.get('/saveSiteTabText/'+tabText).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Tab text saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveSiteInventory=function(siteInventory){
		var defer = $q.defer();
		$http.post('/saveSiteInventory',siteInventory).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Inventory saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
})