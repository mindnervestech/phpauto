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
	            
	            $scope.openRegister = function(){
	            	console.log("Checkkkk");
	            	$('#registerModel').modal();
	            }
	            
	            $scope.registerUser = function(register){
	            	console.log("::::::insideRegester");
	            	console.log(register);
	            	$http.post("/registerUser",register).success(function(data){
		            	 $('#registerPopClose').click();
		            	 
	            		 console.log("::::::success")
	            		/*$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "registration successfully",
						});*/
	       		});
	            }
	            
	            $scope.getbusinessname = function(businessName){
	            	console.log(businessName);
	            	 $http.get('/getbusinessData/'+businessName)
				 		.success(function(data) {
				 			console.log(data);
				 			$scope.locationNameList = data.predictions;
				 		});
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
