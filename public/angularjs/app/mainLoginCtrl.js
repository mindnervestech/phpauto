angular.module('newApp',[]); 
angular.module('newApp').controller('mainLoginCtrl',
	    ['$scope','$location','$http','$interval',
	        function ($scope,$location,$http,$interval) {
	    	
	    		$scope.agree = false;
	    		$('#subId').attr("disabled", true);
	    		$('#subId').hide();
	    		
	    		$scope.userEmail=null;
	    		$scope.userPass=null;
	    		
	            $scope.test = function () {
	            	console.log("In login success");
	            };
	            console.log("In login");
	            
	            $scope.disableSave = function(){
	            	console.log($scope.agree);
	            	if($scope.agree !=true){
	            		$('#subId').attr("disabled", false);
	            		$('#subId').show();
	            	}else{
	            		$('#subId').attr("disabled", true);
	            		$('#subId').hide();
	            	}
	            }
	            $scope.acceptAgreement = function (email) {
	            	console.log($scope.name);
	            	console.log($scope.date);
	            	console.log($scope.phone);
	            	console.log(email);
	            	console.log("In login success");
	            	$scope.userData = {};
	            	$scope.userData.email = email;
	            	$scope.userData.userName = $scope.name;
	            	$scope.userData.userDate = $scope.date;
	            	$scope.userData.userPhone = $scope.phone;
	            	$http.post('/acceptAgreement',$scope.userData)
	    	   		.success(function(data) {
	    	   		
	            });
	            }
	           /* $scope.onSubmit = function(){
	            	
	            	console.log("submit");
	            	$('#submitData').attr("display", "block");
	            	
	            }*/
	            
	            $scope.test = function(){
	            	//$scope.initAutocomplete();
	            };
	            
	            
	            
	            
	           var placeSearch, autocomplete;
	            
	           	/*	$scope.initAutocomplete = function() {
	            	 
	            	 
	            	 autocomplete = new google.maps.places.Autocomplete((document.getElementById('autocomplete')),
	            	     {types: ['geocode']});
	            	 autocomplete.addListener('place_changed', fillInAddress);
	            	}

	            	function fillInAddress() {
	            	 var place = autocomplete.getPlace();
	            	 console.log(place);
	            	 $scope.register.businessName = place.name;
	            	 $scope.register.businessAddress = place.formatted_address;
	            	
	            	}*/
	            	
	            
	            
	            $scope.openRegister = function(){
	            	console.log("Checkkkk");
	            	$('#registerModel').modal();
	            }
	            
	            $scope.registerUser = function(register){
	            	console.log("::::::insideRegester");
	            	console.log(register);
	            	$http.post("/registerUser",register).success(function(data){
	            		//$('#registerPopClose').click();
	            		 $('.page_overlay, .pops, .popsReg ').fadeOut(500);
	            		 console.log("::::::success")
	            		/*$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "registration successfully",
						});*/
	       		});
	            }
	            
	            $scope.getbusinessname = function(businessName){

	            	$scope.register.businessAddress = '';
	            	var arr = [];
	     			arr = businessName.split(',');
	     			for(var i=0; i<arr.length; i++){
	     				if(i != 0){
	     					$scope.register.businessAddress = $scope.register.businessAddress + arr[i] +",";
	     				}else{
	     					$scope.register.businessAddress = '';
	     					$scope.register.businessName = $scope.register.businessAddress + arr[i];
	     				}
	            		
	            	}
	            	
	            	if(businessName != undefined){
	            		$http.get('/getbusinessData/'+businessName)
				 		.success(function(data) {
				 			console.log(data);
				 			
				 			
				 			$scope.locationNameList = data.predictions;
				 			
				 		});
	            	}else{
	            		$scope.locationNameList = null;
	            	}	            
	            	
	            	
				 }
	            
	           
	            
	            $scope.businessAddress = function(check){
	            	console.log(check);
	            	$scope.register.businessAddress = check;
	            }
	            
	        }]);
angular.module('newApp').controller('mainPdfCtrl',
	    ['$scope','$location','$http','$interval',
	        function ($scope,$location,$http,$interval) {
	    	
	    		/*$scope.agree = false;
	    			    		
	    		$scope.userEmail=null;
	    		$scope.userPass=null;
	    		
	            $scope.test = function () {
	            	console.log("In login success");
	            };
	            console.log("In login");
	            
	            $scope.disableSave = function(){
	            	console.log($scope.agree);
	            	if($scope.agree !=true){
	            		$('#subId').attr("disabled", false);
	            		$('#subId').show();
	            	}else{
	            		$('#subId').attr("disabled", true);
	            		$('#subId').hide();
	            	}
	            }
	            $scope.acceptAgreement = function (email) {
	            	console.log($scope.userEmail);
	            	console.log($scope.userPass);
	            	console.log(email);
	            	console.log("In login success");
	            	$scope.userData = {};
	            	$scope.userData.email = email;
	            	$http.post('/acceptAgreement',$scope.userData)
	    	   		.success(function(data) {
	    	   		
	            });
	            }*/
	        }]);
