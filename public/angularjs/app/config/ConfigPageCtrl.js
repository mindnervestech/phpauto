angular.module('newApp')
.controller('ConfigPageCtrl', ['$scope','$http','$location','$filter','$upload','$routeParams', function ($scope,$http,$location,$filter,$upload,$routeParams) {
	$scope.premium = {};
	
	

	
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 $scope.gridOptions.enableVerticalScrollbar = 2;
	 
	$scope.formInit = function(){
		$scope.systemInfo();
	}
	
	$scope.init = function() {
		
		$http.get('/getImageConfig')
		.success(function(data) {
			$scope.cover=data.coverData;
			$scope.vehSize=data.vehicleImageConfig;
			$scope.slider = data.slider;
			$scope.featured = data.featured;
			$scope.newsletterDay = data.NewsletterDate;
			$scope.newsletterId = data.NewsletterId;
			$scope.newsletterTime = data.newsletterTime;
			$scope.newsletterTimeZone = data.NewsletterTimeZone;
			$scope.domain = data.domain;
			$scope.showLoginPasswordText($scope.domain.hostingProvider);
		
			$scope.premium.priceVehical = parseInt(data.premiumLeads.premium_amount);
			
			if(data.premiumLeads.premium_flag == 1){
				$scope.premium.premiumFlag = true;
			}else{
				$scope.premium.premiumFlag = false;
			}
		});
		
		
		$http.get('/getAllSalesUsers').success(function(data){
			$scope.salesPersonList =data;
		
			
		});
		
			/*$scope.showToUser = function(){
		$scope.showTable = 1;
		console.log(locationId);
		$http.get('/getAllSalesUsers').success(function(data){
			$scope.salesPersonList =data;
		
			$scope.user=data;
			if($scope.salesPersonList.length > 0){
				$scope.getAllSalesPersonRecord($scope.salesPersonList[0].id);
			}
		});
	}*/
	
		
		
			$http.get('/getSocialMediadetail')
			.success(function(data) {
				/*$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Slider config saved successfully",
				});*/
				$scope.media=data;
				//$scope.customerPdfList=data;
				
			});	
		
		$http.get('/getCustomerPdfData')
			.success(function(data) {
				/*$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Slider config saved successfully",
				});*/
				$scope.customerPdfList=data;
				
			});
			
		
		$http.get('/getEmailDetails')
			.success(function(data) {
				
				$scope.email=data;
				
			});	
		
			$http.get('/getInternalPdfData')
			.success(function(data) {
				/*$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Slider config saved successfully",
				});*/
				$scope.internalPdfList=data;
			});	
			
			$http.get('/getAllSites')
	 		.success(function(data) {
	 			$scope.siteList = data;
	 			
	 		});
		
	}
	
	
	
	$scope.flagForChart1 = true;
	$scope.systemInfo = function(){
		console.log("sdfghjkp0000");
		$http.get('/getsystemInfo').success(function(data){
			console.log("systemInfo");
			console.log(data);
			$scope.leadtypeObjList = data;
 			$scope.gridOptions.data = data;
		});
		
		
		
		$scope.gridOptions.columnDefs = [
		                                 { name: 'name', displayName: 'Name', width:'70%',
		                                	 cellTemplate:'<div ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.ShowCreateNewForm1(row)">{{row.entity.name}}</label></div>',
		                                 },
		                                 
		                                 { name: 'edit', displayName: ' ', width:'30%',
    		                                 cellTemplate:'<i class="glyphicon glyphicon-pencil" ng-click="grid.appScope.ShowCreateNewForm(row)" title="Edit"></i> ', 
    		                                 /*ng-if="(row.entity.leadName != "Request More Info" || row.entity.leadName != "Schedule Test" || row.entity.leadName != "Trade In")"*/
		                                 },
		                                    ];
	}
	$scope.flagForChart1 = true;
$scope.leadTypeAll = function(){
		
		console.log("sdfghjkp0000");
		$http.get('/getLeadTypeData').success(function(data){
			console.log("lead type data");
			console.log(data);
			$scope.leadtypeObjList = data;
 			$scope.gridOptions.data = data;
 			angular.forEach($scope.gridOptions.data, function(obj, index){
				if(obj.checkValue == true){
					obj.checkValue = true;
				}else if(obj.checkValue == false){
					obj.checkValue = false;
				}
			});
 			console.log("gghgfhghghg");
 			console.log($scope.gridOptions.data);
		});
		
		
		
		$scope.gridOptions.columnDefs = [
		                                 { name: 'id', displayName: 'Id', width:'15%',cellEditableCondition: false
		                                 },
		                                 { name: 'leadName', displayName: 'Lead Type', width:'50%',cellEditableCondition: false
		                                 },
		                                 {name:'org', displayName:'Show on Website', width:'15%',
		                                	 cellTemplate:'<div class="link-domain" ><input type="checkbox" ng-model="checkValue" ng-disabled="row.entity.leadName == \'Contact Us\' || row.entity.leadName == \'Request More Info\' || row.entity.leadName == \'Request For Appointment\'"  ng-checked="row.entity.checkValue" ng-click="grid.appScope.selectCheck(row)">  </div>',
		                                 },
		                                 { name: 'edit', displayName: ' ', width:'20%',
    		                                 cellTemplate:'<i class="fa fa-trash" ng-if="row.entity.leadName != \'Request More Info\' && row.entity.leadName != \'Request For Appointment\' && row.entity.leadName != \'Contact Us\'" ng-click="grid.appScope.removeUser(row)"  title="Delete"></i> &nbsp;&nbsp;&nbsp<i class="glyphicon glyphicon-pencil" ng-if="row.entity.leadName != \'Request More Info\' && row.entity.leadName != \'Request For Appointment\' && row.entity.leadName != \'Contact Us\'" ng-click="grid.appScope.EditUser(row)"  title="Edit"></i> ', 
    		                                 /*ng-if="(row.entity.leadName != "Request More Info" || row.entity.leadName != "Schedule Test" || row.entity.leadName != "Trade In")"*/
		                                 },
		                                    ];
	}
	
	$scope.allFormName = function(){
		$http.get('/allFormName')
		.success(function(data){
			$scope.gridOptions.data=data;
			console.log(data);
		})
	}	
	
	
	
	$scope.selectCheckbox = function(row){
		console.log(row);
		$scope.entityId=row.entity.id;
		console.log($scope.entityId);
		console.log(row);
		console.log(row.entity.checkValue);
		var intValue = 0;
		if(row.entity.checkValue == undefined){
			intValue = 1;
		}
		if(row.entity.checkValue == true){
			intValue = 0;
		}else if(row.entity.checkValue == false){
			intValue = 1;
		}
		
		$http.get('/getCheckButton/'+$scope.entityId+"/"+intValue)
		.success(function(data){
			//$scope.gridOptions.data=data;
			//console.log(data);
		})
		
	}
	
	$scope.ShowCreateNewForm = function(row){
		console.log(row);
		if(row.entity.name == "Create Lead"){
			$location.path('/CreateLeadForm/'+"Preview"+"/"+'Create Lead');
		}else
		if(row.entity.name == "Add Product"){

			$location.path('/InventoryForm/'+"Edit"+"/"+'Inventory');
			
		}
		else if(row.entity.name == "Add to CRM"){

			$location.path('/CRMForm/'+"Edit"+"/"+'CRM');
			
		}else if(row.entity.name == "Request More Info"){
			$location.path('/RequestMoreInfoForm/'+"Edit"+"/"+row.entity.name);
		}
		else if(row.entity.name == "Contact Us"){
			$location.path('/ContactUsForm/'+"Edit"+"/"+row.entity.name);
		}
		else if(row.entity.name == "Request Appointment"){
			$location.path('/RequestAppointmentForm/'+"Edit"+"/"+row.entity.name);
		}
		else{
			$location.path('/otherForm/'+"Edit"+"/"+row.entity.name);
		}
		
	}

	$scope.ShowCreateNewForm1 = function(row){
		//$location.path('/leadCreateForm/'+"Preview");
		console.log($scope.entityname);

		if(row.entity.name == "Create Lead"){
			$location.path('/'+'CreateLeadForm/'+"Preview"+"/"+'Create Lead');
			}else if(row.entity.name == "Add Product"){

			$location.path('/'+'InventoryForm/'+"Preview"+"/"+'Inventory');
			
		}
		else if(row.entity.name == "Add to CRM"){

			$location.path('/'+'CRMForm/'+"Preview"+"/"+'CRM');
			
		}else if(row.entity.name == "Request More Info"){
			$location.path('/RequestMoreInfoForm/'+"Preview"+"/"+row.entity.name);
		}
		else if(row.entity.name == "Contact Us"){
			$location.path('/ContactUsForm/'+"Preview"+"/"+row.entity.name);
		}
		else if(row.entity.name == "Request Appointment"){
			$location.path('/RequestAppointmentForm/'+"Preview"+"/"+row.entity.name);
		}
		else{
			$location.path('/otherForm/'+"Preview"+"/"+row.entity.name);
		}
	}
	
	
	$scope.allLeaddata = function(){
		$http.get('/getAllLeadData')
		.success(function(data){
			$scope.gridOptions.data=data;
			console.log(data);
		})
	}

		$scope.openAddNew = function(){
			console.log("Checkkkk");
			$scope.leadcreate={"leadName":""};
			//$('#createLeadPopup').click();
			$('#completedPopup').modal('show');
		}
		
		$scope.saveCompleted = function(){
		console.log("::::::insideRegester");
		console.log($scope.leadcreate);
		$http.post("/addnewrUser",$scope.leadcreate).success(function(data){
				$scope.form = data;
			 console.log("::::::success")
				
			 $("#completedPopup").modal('hide');
			 $scope.leadTypeAll();
			});
		}
		
		 $scope.removeUser = function(row){
			 console.log("show popu");
			 $scope.entityId=row.entity.id;
			 $('#leadtypebutton').click();
			// $('#leadtypebutton').modal('show');
			// $scope.deletelead = row.entity;
			 
		 }
		
		 $scope.deletelead = function(){
			 console.log("in deletend functio");
			 $http.get('/getdeletelead/'+$scope.entityId)
				.success(function(data) {
					 console.log("out deletend functio");
					 $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Remove User",
						});
					 
					 $scope.leadTypeAll();
					 
					 

				});
		 }
		 
		 $scope.selectCheck = function(row){
				
			 $('#editPopupcheck').click();
			 console.log(row.entity)
			
			 $scope.editleadtype.id = row.entity.id;
			 $scope.editleadtype.profile = row.entity.profile;
			 console.log($scope.editleadtype.profile);
			 $scope.selectCheckbox(row);
			 
		 }
			
		 $scope.editleadtype={};
		 $scope.Updatecheckbox = function(){
			 console.log($scope.editleadtype);
			 console.log("out of funtion");
			 
			 $http.post("/Updatecheckbox",$scope.editleadtype)
			 .success(function(data){
				 console.log(data);
				 console.log("in of funtion");
				 $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Update successfully",
					});
         		$("#editPopupscheck").modal('hide');
         		
    		});
			
		 }
		 
		 $scope.addNewForm = function(){
				console.log("Checkkkk");
				$scope.addform={"name":""};
				
				$('#completedPopup').modal('show');
			}
		 
		
		 
			$scope.savedNewForm = function(){
			$http.post("/addnewForm",$scope.addform).success(function(data){
				$scope.form = data;
				$("#completedPopup").modal('hide');
				 $scope.allFormName();
				});
			}
			
			$scope.flagForChart1 = true;
			$scope.website = {};
			$scope.savedNewFormWebsite = function(){
				console.log($scope.website);
				$http.post("/addnewWebSiteForm",$scope.website).success(function(data){
					$scope.form = data;
					console.log(data);
					
					 $("#completedPopup").modal('hide');
					 $scope.showEditData();
					 $scope.webSiteinfo();
					 
					});
				
				
				}
			$scope.updateNewFormWebsite = function(){
				console.log($scope.website);
				console.log($scope.rowDataVal);
				$scope.website.id = $scope.rowDataVal.id;
				$http.post("/updatenewWebSiteForm",$scope.website).success(function(data){
					$scope.form = data;
					console.log(data);
					$('#outcome').click();
					// $("#outcome").modal('hide');
					$scope.webSiteinfo();
					});
				 	
				
				}
			$scope.webSiteinfo = function(){
				$http.get('/getFormWebSiteData').success(function(data){
					
					console.log(data);
					
		 			$scope.gridOptions.data = data;
		 			console.log($scope.gridOptions.data);
		 			console.log("grid data")
				});
				$scope.gridOptions.columnDefs = [
				                                 { name: 'id', displayName: 'Id', width:'10%',cellEditableCondition: false
				                                 },
				                                 { name: 'title', displayName: 'Title', width:'20%',cellEditableCondition: false
				                                 },
				                                 {name:'form_type', displayName:'Form Type', width:'15%'},
				                                 { name: 'lead_name', displayName: 'Lead Name ', width:'15%' },
				                                 { name: 'outcome', displayName: 'Outcome ', width:'15%' },
				                                 {name:'or', displayName:'', width:'15%',
				                                	 /*cellTemplate:'<div><div class="link-domain"ng-click="grid.appScope.outcome(row)">Outcome &nbsp;&nbsp;&nbsp </div><i class="glyphicon glyphicon-pencil" ng-click="grid.appScope.updateAllFormWebsite(row)"  title="Edit"></i></div>',*/
				                                	 cellTemplate:'<i class="link-domain"ng-click="grid.appScope.outcome(row)">Outcome</i> &nbsp;&nbsp;&nbsp<i class="glyphicon glyphicon-pencil" ng-click="grid.appScope.updateAllFormWebsite(row)"  title="Edit"></i> ',
				                                 }, 
				                                 {name:'10', displayName:'LeadLink', width:'15%',
				                                	 /*cellTemplate:'<div><div class="link-domain"ng-click="grid.appScope.outcome(row)">Outcome &nbsp;&nbsp;&nbsp </div><i class="glyphicon glyphicon-pencil" ng-click="grid.appScope.updateAllFormWebsite(row)"  title="Edit"></i></div>',*/
				                                	 cellTemplate:'<div class="link-domain" ><i class="glyphicon glyphicon-edit" ng-click="grid.appScope.allLeadRender(row)"  title="Edit"></i></div>',
				                                 }, 
				                                 
				                                 ];
			}
			
			
			 $scope.addNewFormWebSite = function(){
					console.log("add form website");
					$scope.website={"title":""};
					$('#completedPopup').modal('show');
					$scope.leadTypeAllData();
					
				}
			 $scope.outcome = function(row){
					console.log(row.entity);
					$scope.website.outcome = row.entity.outcome;
					$('#outcome').click();
					 

					$scope.rowDataVal = row.entity;
				};
				
				 $scope.allLeadRender = function(row){
						console.log(row);
						if(row.entity.lead_name != ""){
						 if(row.entity.lead_name == "Request More Info"){
							$location.path('/RequestMoreInfoForm/'+"Edit"+"/"+row.entity.lead_name);
						}
						else if(row.entity.lead_name == "Contact Us"){
							$location.path('/ContactUsForm/'+"Edit"+"/"+row.entity.lead_name);
						}
						else if(row.entity.lead_name == "Request Appointment"){
							$location.path('/RequestAppointmentForm/'+"Edit"+"/"+row.entity.lead_name);
						}
						else{
							$location.path('/otherForm/'+"Edit"+"/"+row.entity.lead_name);
						}
						}
						
					}
				 
			 
				$scope.editData = {};
				 $scope.updateAllFormWebsite = function(row){
					 $('#editPopupwebsite').click();
					 console.log(row.entity)
					 $scope.editData = row.entity;
					 $scope.leadTypeAllData();
					 $scope.showEditData();
				 }
				 
				/* $scope.viewRegiInit = function(){
					 $scope.pendingUser();
				 }*/
				 
				 $scope.editFormWebsite = function(website){
					 console.log($scope.editData);
					 $scope.website.id = $scope.editData.id;
					 $http.post("/getEditFormWebsite",$scope.website).success(function(data){
		         		$.pnotify({
							    title: "Success",
							    type:'success',
							    text: "Update successfully",
							});
		         		$("#editPopupswebsite").modal('hide');
		         		$scope.webSiteinfo();
		    		});
					 
				 }
				
				 $scope.showEditData = function(){
					 $scope.website.id = $scope.editData.id;
					 console.log($scope.website.id);
					 console.log("ddddddddd");
						$http.get('/showEditData/'+$scope.website.id)
						.success(function(data){
							//$scope.gridOptions.data=data;
							console.log(data);
							$scope.website = data;
						})
					}	
				 
			 $scope.leadTypeAllData = function(){
					
					console.log("sdfghjkp0000");
					$http.get('/getLeadTypeData').success(function(data){
						console.log("lead type data");
						console.log(data);
						$scope.leadtypeObjList = data;
			 		});
				}
			 
			 $scope.showOtherFild = 0;
			 $scope.selectOption = function(type){
					//var type = $('#form_type').val();
					console.log(type);
					
					if(type == "Contact Form"){
						$scope.showOtherFild = 1;
					}
					if(type == "Call to Action" || type == "Survey"){
						$scope.showOtherFild = 0;
					}
				}
				
			 
				
		 $scope.EditUser = function(row){
			
			 $('#editPopup').click();
			 console.log(row.entity)
			 $scope.leadName = row.entity.leadName;
			 $scope.editleadtype.id = row.entity.id;
			 
		 }
			
		 $scope.editleadtype={};
		 $scope.UpdateLeadType = function(leadName){
			 console.log($scope.editleadtype);
			 console.log("out of funtion");
			 $scope.editleadtype.leadName = leadName;
			 $http.post("/UpdateLeadType",$scope.editleadtype)
			 .success(function(data){
				 console.log("in of funtion");
				 $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Update successfully",
					});
         		$("#editPopups").modal('hide');
         		//$scope.allLeaddata();
         		//$scope.selectCheckbox();
         		$scope.leadTypeAll();
    		});
		 }
		 
		 
	$scope.documentationSetting = function() {
		$location.path('/documentation');
		
	}
	
	
	$scope.leadTypeInfo = function() {
		//console.log("ddd22");
		$location.path('/leadtype');
		
	}
	$scope.form = function() {
		//console.log("ddd22");
		$location.path('/form');
		
	}
	$scope.webSite = function() {
		console.log("wesite");
		$location.path('/webSite');
		
	}
	
	$scope.autoPortal = function() {
		$location.path('/autoPortal');
		
	}
	
	$scope.domainDetails= function() {
		$location.path('/domainDetails');
		
	}
	
	
	$scope.plansAndBill= function() {
		$location.path('/plansAndBill');
		
	}
	
	$scope.newsLetterSetting = function() {
		$location.path('/newsLetter');
		
	}
	
	$scope.socialMedia = function() {
		$location.path('/socialMedia');
		
	}
	
	$scope.graphicInfo = function() {
		$location.path('/configuration');
		
	}
	$scope.premiumLeadsInfo= function() {
		$location.path('/premiumLeads');
		
	}
	
	$scope.saveEmailDetails= function(auto) {
		$http.post('/saveEmailDetails',auto)
		.success(function(data) {

            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Email Info saved successfully",
			});
            
		});
		
		
		
	}
	$scope.selectHost = 0;
	$scope.showLoginPasswordText = function(hostname){
		if(hostname != "Other"){
			$scope.selectHost = 1;
		}else{
			$scope.selectHost = 2;
		}
	}
	
	$scope.saveDomain= function(sitenameData) {
				
		$http.post('/saveDomain',sitenameData)
		.success(function(data) {

            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Domain saved successfully",
			});
            
		});
		
		
		
	}
	
	
	
	
	
	
	
	
	$scope.autoPort={};
	$scope.saveAutoPortal= function(auto,siteName) {
		auto.sitename=siteName;
		$scope.autoPort=auto;
		//$scope.autoPort.siteName=siteName;
		$http.post('/saveAutoPortal',$scope.autoPort)
		.success(function(data) {

            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Autoportal details saved successfully",
			});
            
		});
		
	}
	
	$scope.saveEmailLinks= function(email) {
		console.log(email);
		//$scope.autoPort.siteName=siteName;
		$http.post('/saveEmailLinks',email)
		.success(function(data) {

            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Social Links saved successfully",
			});
            
		});
	}
	
	
	
	
	$scope.autoPort1={};
	$scope.acountDetails= function(auto,siteName) {
		auto.sitename=siteName;
		$scope.autoPort1=auto;
		//$scope.autoPort.siteName=siteName;
		$http.post('/acountDetails',$scope.autoPort1)
		.success(function(data) {

            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Username and Passward saved successfully",
			});
            
		});
		
		
		
	}
	
	
	
	
	 var logofile;
		$scope.onFileSelect = function ($files) {
			logofile = $files;
			$upload.upload({
		 	         url : '/saveInternalPdf',
		 	         method: 'POST',
		 	         file:logofile,
		 	      }).success(function(data) {
		 	  			$.pnotify({
		 	  			    title: "Success",
		 	  			    type:'success',
		 	  			    text: "pdf saved successfully",
		 	  			});
			
		 	  			$http.get('/getInternalPdfData')
		 	  			.success(function(data) {
		 	  				
		 	  				$scope.internalPdfList=data;
		 	  			});		
		 	  			
			
		});	
		}
		
		var logofile1;
		$scope.onCustomerFileSelect = function ($files) {
			logofile1 = $files;
			$upload.upload({
		 	         url : '/saveCustomerPdf',
		 	         method: 'POST',
		 	         file:logofile1,
		 	      }).success(function(data) {
		 	  			$.pnotify({
		 	  			    title: "Success",
		 	  			    type:'success',
		 	  			    text: "pdf saved successfully",
		 	  			});
			
		 	  			$http.get('/getCustomerPdfData')
		 	  			.success(function(data) {
		 	  				/*$.pnotify({
		 	  				    title: "Success",
		 	  				    type:'success',
		 	  				    text: "Slider config saved successfully",
		 	  				});*/
		 	  				
		 	  			});		
		 	  			
			
		});	
		}
		
		$scope.deletePdf = function(id) {
			
			$http.get('/getCustomerPdfDataById/'+id)
  			.success(function(data) {
		        $scope.customerPdfName=data;
  			});
			
			$('#btndeleteCustomerPdf').click();
			$scope.customerPdfId = id;
			
		}
		
		
		$scope.deletePdfCustomer = function() {
			console.log("$scope.customerPdfId"+$scope.customerPdfId);
			
			$http.get('/deletePdfById/'+$scope.customerPdfId)
	  			.success(function(data) {
	  				$.pnotify({
	  				    title: "Success",
	  				    type:'success',
	  				    text: "PDF deleted successfully",
	  				});
			
	  			});
		}
		
		

		$scope.deleteInternalPdf = function(id) {
			
			$http.get('/getInternalPdfDataById/'+id)
  			.success(function(data) {
		        $scope.internalPdfName=data;
  			});
			
			$('#btndeleteInternalPdf').click();
			$scope.internalPdfId = id;
		}
		
		
		
          $scope.deletePdfInternal = function() {
			
        	  console.log("$scope.internalPdfId"+$scope.internalPdfId);
			$http.get('/deleteInternalPdf/'+$scope.internalPdfId)
	  			.success(function(data) {
	  				$.pnotify({
	  				    title: "Success",
	  				    type:'success',
	  				    text: "PDF deleted successfully",
	  				});
			
	  			});
		}
		
		$scope.permiumAss = function(saleP){
		$http.get('/setPermiumFlag/'+saleP.id).success(function(data){
			console.log("Yesssss");
		});
	}
	
	$scope.saveSlider = function() {
		$http.get('/saveSliderConfig/'+$scope.slider.width+'/'+$scope.slider.height)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Slider config saved successfully",
			});
		});
	}
	
	$scope.saveFeatured = function() {
		$http.get('/saveFeaturedConfig/'+$scope.featured.width+'/'+$scope.featured.height)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Featured config saved successfully",
			});
		});
	}
	
	$scope.saveVehicleSize = function() {
		$http.get('/saveVehicleConfig/'+$scope.vehSize.width+'/'+$scope.vehSize.height)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Vehicle config saved successfully",
			});
		});
	}
	
	
	
	
	$scope.saveCoverImageSize = function() {
		$http.get('/setCoverImage/'+$scope.cover.width+'/'+$scope.cover.height)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Vehicle config saved successfully",
			});
		});
	}
	
	$scope.savePremium = function() {
		if($scope.premium.premiumFlag == undefined || $scope.premium.premiumFlag == null){
			$scope.premium.premiumFlag = "0";
		}
		$http.get('/savePremiumConfig/'+$scope.premium.priceVehical+'/'+$scope.premium.premiumFlag)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Premium saved successfully",
			});
		});
	}
	
	$scope.saveDayOfMonth = function() {
		$scope.newsletterTime = $('#newsTime').val();
		$http.get('/saveNewsletterDate/'+$scope.newsletterDay+'/'+$scope.newsletterTime+'/'+$scope.newsletterId+'/'+$scope.newsletterTimeZone)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Newsletter date saved successfully",
			});
		});
	}
	
}]);	