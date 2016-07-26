
angular.module('newApp')
.controller('myprofileCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','apiserviceProfile', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,apiserviceProfile) {
	$scope.myprofile = {};
	$scope.userKey = userKey;
	var placeSearch, autocomplete;
	$scope.imgGM="/assets/images/profile-pic.jpg ";
	
	
	apiserviceProfile.getUserRole().then(function(data){
		$scope.userRole = data.role;
	});
	
	apiserviceProfile.getDealerProfile().then(function(data){
		$scope.myprofile = data.dealer;
		$scope.user = data.user;
		$scope.myprofile.dealer_id = data.user.location.id;
		$scope.imgGM = "http://glider-autos.com/glivrImg/images"+$scope.user.imageUrl;
	});

	apiserviceProfile.getMyProfile().then(function(data){
		$scope.myprofile = data;
		$scope.initAutocomplete();
	});
	
	
	$scope.saveMyprofile = function() {
		var geocoder = new google.maps.Geocoder(); 
		var address = $scope.myprofile.address;
		geocoder.geocode( { 'address': address}, function(results, status) { 
			if (status == google.maps.GeocoderStatus.OK) 
			{ 
				var latitude = results[0].geometry.location.lat(); 
				var longitude = results[0].geometry.location.lng();
				$scope.myprofile.latlong = latitude+","+longitude;
				$scope.getLatLong();
			}else{
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Address not found on google map",
				});
				$scope.getLatLong();
			} 
		});
		
   }
	$scope.init =function(){
		
		apiserviceProfile.getSaleHourData().then(function(data){
			$scope.operation=data[0];
		});
		
		apiserviceProfile.getSaleHourDataForService().then(function(data){
			$scope.operation1=data[0];
		});
		
		apiserviceProfile.getSaleHourDataForParts().then(function(data){
			$scope.operation2=data[0];
		});
		
		
	}
	
	
	var componentForm = {
			  street_number: 'short_name',
			  route: 'long_name',
			  locality: 'long_name',
			  administrative_area_level_1: 'short_name',
			  country: 'long_name',
			  postal_code: 'short_name'
			};
	$scope.initAutocomplete = function() {
		  autocomplete = new google.maps.places.Autocomplete((document.getElementById('autocomplete')),
		      {types: ['geocode']});
		  autocomplete.addListener('place_changed', fillInAddress);
		}
	
	function fillInAddress() {
		  var place = autocomplete.getPlace();
		  $scope.myprofile.address = place.formatted_address;
		  for (var component in componentForm) {
		    //document.getElementById(component).value = '';
		    //document.getElementById(component).disabled = false;
		  }
		  for (var i = 0; i < place.address_components.length; i++) {
		    var addressType = place.address_components[i].types[0];
		    if (componentForm[addressType]) {
		      var val = place.address_components[i][componentForm[addressType]];
		      //document.getElementById(addressType).value = val;
		    }
		  }
		}
	
	$scope.geolocate = function() {
		  if (navigator.geolocation) {
		    navigator.geolocation.getCurrentPosition(function(position) {
		      var geolocation = {
		        lat: position.coords.latitude,
		        lng: position.coords.longitude
		      };
		      var circle = new google.maps.Circle({
		        center: geolocation,
		        radius: position.coords.accuracy
		      });
		      autocomplete.setBounds(circle.getBounds());
		    });
		  }
		};
	
	
	
	
	
	
	
	$scope.managerP = {};
	$scope.getLatLong = function() {
	
			apiserviceProfile.myprofile(logofile1, $scope.myprofile).then(function(data){
			});
	}
	
	$scope.goToLoaction = function() {
		$location.path('/createLocation');
	}
	$scope.goToDeactivateLoaction = function() {
			$location.path('/deactiveLocations');
	};
	$scope.goToUsers = function() {
		$location.path('/createUser');
	}
	$scope.goToDeactive = function() {
		$location.path('/deactiveUsers');
	};
	$scope.createGeneralManager =function(){
		$scope.imgGM="/assets/images/profile-pic.jpg ";
		
	}
	
	
	
     	$scope.goToBlog = function() {
		   $location.path('/blog');
	   }
	   
	   $scope.goToContactUs = function() {
		   $location.path('/contactUs');
	   }
       
	   $scope.goToWarranty = function() {
		   $location.path('/warranty');
	   }
	   
	   
	   
	   $scope.goToVehicleProfile = function() {
		   $location.path('/vehicleProfile');
	   }
	   
	   $scope.goToSlider = function() {
		   $location.path('/sliderImages');
	   }
	   $scope.goToFeatured = function() {
		   $location.path('/featuredImages');
	   }
	   $scope.goToSlogan = function() {
		   $location.path('/siteSlogan');
	   }
	   $scope.goToDesc = function() {
		   $location.path('/siteDescription');
	   }
	   $scope.goToLogo = function() {
		   $location.path('/siteLogo');
	   }
	   $scope.goToTestiMonials = function() {
		   $location.path('/siteTestiMonials');
	   }
	   $scope.goToAboutUs = function(){
		   $location.path('/siteAboutUs');
	   }
	   
	   $scope.goToPageContent = function(){
		   console.log("::::::::");
		   $location.path('/goToPageContent');
	   }
	   
	   
	   $scope.goToInventory = function(){
		   $location.path('/goToInventoryNew/'+"New");
	   }
	   
	   $scope.gotoHoursOfOperation = function() {
			
			$location.path('/hoursOfOperations');
		}; 	
	   
	   
	   $scope.goToInventoryUsed = function(){
		   $location.path('/goToInventoryUsed/'+"Used");
	   }
	   $scope.goToInventorycomingSoon = function(){
		   $location.path('/goToInventoryComingsoon/'+"comingSoon");
	   }
	   
      
	   $scope.goToCompare = function(){
		   $location.path('/comparision');
	   }
	
	/*$scope.gotoHoursOfOperation = function() {
		
		$location.path('/hoursOfOperations');
	};*/ 	
	
	
	$scope.operation2={};
	$scope.saveSalesHoursForParts = function() {
		$scope.operation2.typeOfOperation='parts';
		$scope.operation2.sunOpenTime = $('#sunOpen2').val();
		$scope.operation2.sunCloseTime= $('#sunClose2').val();
		$scope.operation2.monOpenTime = $('#monOpen2').val();
		$scope.operation2.monCloseTime= $('#monClose2').val();   
		$scope.operation2.tueOpenTime = $('#tueOpen2').val();
		$scope.operation2.tueCloseTime= $('#tueClose2').val();   
		$scope.operation2.wedOpenTime = $('#wedOpen2').val();
		$scope.operation2.wedCloseTime= $('#wedClose2').val();
		$scope.operation2.thuOpenTime = $('#thuOpen2').val();
		$scope.operation2.thuCloseTime= $('#thuClose2').val();
		$scope.operation2.friOpenTime = $('#friOpen2').val();
		$scope.operation2.friCloseTime= $('#friClose2').val();
		$scope.operation2.satOpenTime = $('#satOpen2').val();
		$scope.operation2.satCloseTime= $('#satClose2').val();
		   
		
		apiserviceProfile.saveSalesHoursForParts($scope.operation2).then(function(data){
		});
		   		 
	}; 	
	
	$scope.operation={};
	$scope.saveSalesHours = function() {
		$scope.operation.typeOfOperation='sales';
		$scope.operation.sunOpenTime = $('#sunOpen').val();
		$scope.operation.sunCloseTime= $('#sunClose').val();
		$scope.operation.monOpenTime = $('#monOpen').val();
		$scope.operation.monCloseTime= $('#monClose').val();   
		$scope.operation.tueOpenTime = $('#tueOpen').val();
		$scope.operation.tueCloseTime= $('#tueClose').val();   
		$scope.operation.wedOpenTime = $('#wedOpen').val();
		$scope.operation.wedCloseTime= $('#wedClose').val();
		$scope.operation.thuOpenTime = $('#thuOpen').val();
		$scope.operation.thuCloseTime= $('#thuClose').val();
		$scope.operation.friOpenTime = $('#friOpen').val();
		$scope.operation.friCloseTime= $('#friClose').val();
		$scope.operation.satOpenTime = $('#satOpen').val();
		$scope.operation.satCloseTime= $('#satClose').val();
		   
		apiserviceProfile.saveHours($scope.operation).then(function(data){
		});
		   
		 
	}; 	
	
	
	$scope.checkForService = function() {
		$scope.checkForServiceValue=0;
		$scope.operation1.typeOfOperation='service';
		if($scope.operation1.serviceCheck == false || $scope.operation1.serviceCheck == undefined ){
			$scope.checkForServiceValue=1;
		}
		else{
			$scope.checkForServiceValue=0;
		}
		
		apiserviceProfile.checkForService($scope.checkForServiceValue).then(function(data){
		});
		
				
	}
	
	$scope.checkForServiceForPart = function() {
		$scope.checkForServiceValue=0;
		$scope.operation1.typeOfOperation='parts';
		if($scope.operation2.serviceCheck == false || $scope.operation2.serviceCheck == undefined ){
			$scope.checkForServiceValue=1;
		}
		else{
			$scope.checkForServiceValue=0;
		}
		
		apiserviceProfile.checkForServiceForPart($scope.checkForServiceValue).then(function(data){
		});
		/*$http.get('/checkForServiceForPart/'+$scope.checkForServiceValue)
		.success(function(data) {
			
		});*/
		
	}
	
	$scope.operation1={};
	$scope.saveSalesHoursForService = function() {
		$scope.operation1.typeOfOperation='service';
		$scope.operation1.sunOpenTime = $('#sunOpen1').val();
		$scope.operation1.sunCloseTime= $('#sunClose1').val();
		$scope.operation1.monOpenTime = $('#monOpen1').val();
		$scope.operation1.monCloseTime= $('#monClose1').val();   
		$scope.operation1.tueOpenTime = $('#tueOpen1').val();
		$scope.operation1.tueCloseTime= $('#tueClose1').val();   
		$scope.operation1.wedOpenTime = $('#wedOpen1').val();
		$scope.operation1.wedCloseTime= $('#wedClose1').val();
		$scope.operation1.thuOpenTime = $('#thuOpen1').val();
		$scope.operation1.thuCloseTime= $('#thuClose1').val();
		$scope.operation1.friOpenTime = $('#friOpen1').val();
		$scope.operation1.friCloseTime= $('#friClose1').val();
		$scope.operation1.satOpenTime = $('#satOpen1').val();
		$scope.operation1.satCloseTime= $('#satClose1').val();
		   
		
		apiserviceProfile.saveSalesHoursForService($scope.operation1).then(function(data){
		});
		/*
		   $http.post('/saveSalesHoursForService',$scope.operation1)
			.success(function(data) {
				
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "saved successfully",
				});
			});*/
		 
	}; 	
	
	
	

	 var logofile;
		$scope.onLogoFileSelect = function ($files) {
			logofile = $files;
			for (var i = 0; i < $files.length; i++) {
				var $file = $files[i];
				if (window.FileReader && $file.type.indexOf('image') > -1) {
					var fileReader = new FileReader();
					fileReader.readAsDataURL($files[i]);
					var loadFile = function (fileReader, index) {
						fileReader.onload = function (e) {
							$timeout(function () {
								$scope.img = e.target.result;
							});
							
						}
					}(fileReader, i);
				}
			}
		}	
	
	
	$scope.saveImage = function() {
		
		$scope.user.id = $scope.userKey;
		$scope.user.userType = "General Manager";
		if(angular.isUndefined(logofile)) {
			
			apiserviceProfile.updateImageFile(logofile, $scope.user).then(function(data){
				$('#GM').click();
	            $('#btnClose').click();
			});
				
		} else {
			
			apiserviceProfile.updateImageFile(logofile, $scope.user).then(function(data){
				$("#file").val('');
				$('#GM').click();
	            $('#btnClose').click();
			});
			
		}
	   }
	
	
	
/*--------------------------------Manager profile---------------------------*/
	
	$scope.managerProfile = null;
	$scope.initManager = function() {
		
		apiserviceProfile.getMangerAndLocation().then(function(data){
			$scope.managerProfile = data;
			$scope.imgLocation = "http://glider-autos.com/glivrImg/images/"+$scope.managerProfile.imageUrl;
			$scope.img = "http://glider-autos.com/glivrImg/images"+$scope.managerProfile.mImageUrl;
		});
		
	}
	
	
	var logofile;
	$scope.onLogoFileSelect = function ($files) {
		logofile = $files;
		for (var i = 0; i < $files.length; i++) {
			var $file = $files[i];
			if (window.FileReader && $file.type.indexOf('image') > -1) {
				var fileReader = new FileReader();
				fileReader.readAsDataURL($files[i]);
				var loadFile = function (fileReader, index) {
					fileReader.onload = function (e) {
						$timeout(function () {
							$scope.imgGM = e.target.result;
						});
						
					}
				}(fileReader, i);
			}
		}
	}	
	var logofile1;
	$scope.onLogoFileLocationSelect = function ($files) {
		logofile1 = $files;
		for (var i = 0; i < $files.length; i++) {
			var $file = $files[i];
			if (window.FileReader && $file.type.indexOf('image') > -1) {
				var fileReader = new FileReader();
				fileReader.readAsDataURL($files[i]);
				var loadFile = function (fileReader, index) {
					fileReader.onload = function (e) {
						$timeout(function () {
							$scope.imgLocation = e.target.result;
						});
						
					}
				}(fileReader, i);
			}
		}
	}	
   
$scope.managerObj = {};
$scope.locationObj = {};
	
	
	$scope.updateManagerProfile = function() {
		$scope.managerProfile.userType = "Manager"
		$scope.managerObj.id = $scope.managerProfile.managerId;
		$scope.managerObj.userType = $scope.managerProfile.userType;
		$scope.managerObj.firstName = $scope.managerProfile.firstName;
		$scope.managerObj.lastName = $scope.managerProfile.lastName;
		$scope.managerObj.email = $scope.managerProfile.email;
		$scope.managerObj.phone = $scope.managerProfile.phone;
		$scope.managerObj.mi = "false";
			
		
		apiserviceProfile.UpdateuploadManagerImageFile(logofile, $scope.managerObj).then(function(data){
			
		});
	
	   }
	
	$scope.updateManager = function(){
	
	}
	
}]);	

