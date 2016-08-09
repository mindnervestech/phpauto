angular.module('app.apiserviceConfigPage', [])
.service('apiserviceConfigPage', function($http,$q){

	var accessUrl = '';

	this.getImageConfig=function(){
		var defer = $q.defer();
		
		$http.get('/getImageConfig').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAllSalesUsers=function(){
		var defer = $q.defer();
		
		$http.get('/getAllSalesUsers').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getSocialMediadetail=function(){
		var defer = $q.defer();
		
		$http.get('/getSocialMediadetail').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getCustomerPdfData=function(){
		var defer = $q.defer();
		
		$http.get('/getCustomerPdfData').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getEmailDetails=function(){
		var defer = $q.defer();
		
		$http.get('/getEmailDetails').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getInternalPdfData=function(){
		var defer = $q.defer();
		
		$http.get('/getInternalPdfData').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAllSites=function(){
		var defer = $q.defer();
		
		$http.get('/getAllSites').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getsystemInfo=function(){
		var defer = $q.defer();
		
		$http.get('/getsystemInfo').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getLeadTypeData=function(){
		var defer = $q.defer();
		
		$http.get('/getLeadTypeData').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.allFormName=function(){
		var defer = $q.defer();
		
		$http.get('/allFormName').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getCheckButton=function(entityId, intValue){
		var defer = $q.defer();
		
		$http.get('/getCheckButton/'+entityId+'/'+intValue).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAllLeadData=function(){
		var defer = $q.defer();
		
		$http.get('/getAllLeadData').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.addnewrUser=function(leadcreate){
		var defer = $q.defer();
		
		$http.post('/addnewrUser',leadcreate).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getdeletelead=function(entityId){
		var defer = $q.defer();
		
		$http.get('/getdeletelead/'+entityId).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Remove User",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.Updatecheckbox=function(editleadtype){
		var defer = $q.defer();
		
		$http.post('/Updatecheckbox',editleadtype).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Update successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.addnewForm=function(addform){
		var defer = $q.defer();
		
		$http.post('/addnewForm',addform).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.addnewWebSiteForm=function(website){
		var defer = $q.defer();
		
		$http.post('/addnewWebSiteForm',website).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.updatenewWebSiteForm=function(website){
		var defer = $q.defer();
		
		$http.post('/updatenewWebSiteForm',website).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getFormWebSiteData=function(){
		var defer = $q.defer();
		
		$http.get('/getFormWebSiteData').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getEditFormWebsite=function(website){
		var defer = $q.defer();
		
		$http.post('/getEditFormWebsite',website).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Update successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.showEditData=function(id){
		var defer = $q.defer();
		
		$http.get('/showEditData/'+id).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getLeadTypeData=function(){
		var defer = $q.defer();
		
		$http.get('/getLeadTypeData').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.UpdateLeadType=function(editleadtype){
		var defer = $q.defer();
		
		$http.post('/UpdateLeadType',editleadtype).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Update successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.saveEmailDetails=function(auto){
		var defer = $q.defer();
		
		$http.post('/saveEmailDetails',auto).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Email Info saved successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.saveDomain=function(sitenameData){
		var defer = $q.defer();
		
		$http.post('/saveDomain',sitenameData).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Domain saved successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.saveWebsite=function(webanlyatics){
		var defer = $q.defer();
		
		$http.post('/saveWebsite',webanlyatics).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Tracking Code saved successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getwebsiteAnalyticsData=function(){
		var defer = $q.defer();
		
		$http.get('/getwebsiteAnalyticsData').success(function(data) {
			
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.savemailchimpPage=function(schedular){
		var defer = $q.defer();
		
		$http.post('/savemailchimpPage',schedular).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "mailchimpPage saved successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getmailchimpData=function(){
		var defer = $q.defer();
		
		$http.get('/getmailchimpData').success(function(data) {
			
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.saveAutoPortal=function(autoPort){
		var defer = $q.defer();
		
		$http.post('/saveAutoPortal',autoPort).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Autoportal details saved successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.saveEmailLinks=function(email){
		var defer = $q.defer();
		
		$http.post('/saveEmailLinks',email).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Social Links saved successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.acountDetails=function(autoPort1){
		var defer = $q.defer();
		
		$http.post('/acountDetails',autoPort1).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Username and Passward saved successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getCustomerPdfDataById=function(id){
		var defer = $q.defer();
		
		$http.get('/getCustomerPdfDataById/'+id).success(function(data) {
			
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.deletePdfById=function(customerPdfId){
		var defer = $q.defer();
		
		$http.get('/deletePdfById/'+customerPdfId).success(function(data) {
			$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "PDF deleted successfully",
				});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getInternalPdfDataById=function(id){
		var defer = $q.defer();
		$http.get('/getInternalPdfDataById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteInternalPdf=function(internalPdfId){
		var defer = $q.defer();
		$http.get('/deleteInternalPdf/'+internalPdfId).success(function(data) {
			$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "PDF deleted successfully",
				});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.setPermiumFlag=function(id){
		var defer = $q.defer();
		$http.get('/setPermiumFlag/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveSliderConfig=function(width, height){
		var defer = $q.defer();
		$http.get('/saveSliderConfig/'+width+'/'+height).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Slider config saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveFeaturedConfig=function(width, height){
		var defer = $q.defer();
		$http.get('/saveFeaturedConfig/'+width+'/'+height).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Featured config saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveVehicleConfig=function(width, height){
		var defer = $q.defer();
		$http.get('/saveVehicleConfig/'+width+'/'+height).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Vehicle config saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.setCoverImage=function(width, height){
		var defer = $q.defer();
		$http.get('/setCoverImage/'+width+'/'+height).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Vehicle config saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.savePremiumConfig=function(priceVehical, premiumFlag){
		var defer = $q.defer();
		$http.get('/savePremiumConfig/'+priceVehical+'/'+premiumFlag).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Premium saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveNewsletterDate=function(newsletterDay, newsletterTime, newsletterId, newsletterTimeZone){
		var defer = $q.defer();
		$http.get('/saveNewsletterDate/'+newsletterDay+'/'+newsletterTime+'/'+newsletterId+'/'+newsletterTimeZone).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Newsletter date saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
})