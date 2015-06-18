'use strict';

/**
 * @ngdoc function
 * @name newappApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the newappApp
 */
angular.module('newApp')
  .controller('dashboardCtrl', ['$scope', 'dashboardService', 'pluginsService', '$http', function ($scope, dashboardService, pluginsService,$http) {
      $scope.$on('$viewContentLoaded', function () {
          dashboardService.init();
          pluginsService.init();
          dashboardService.setHeights()
          if ($('.widget-weather').length) {
              widgetWeather();
          }
          handleTodoList();
      });

      $scope.activeTab = true;

  }]);

angular.module('newApp')
.controller('addVehicleCtrl', ['$scope','$http','$location', function ($scope,$http,$location) {
  
   $scope.vehicleInit = function() {
 	  $http.get('/getAllSites')
 		.success(function(data) {
 			$scope.siteList = data;
 		});
   }
   
   $scope.siteIds = [];
   $scope.setSiteId = function(id,flag) {
 	  if(flag == true) {
 		  $scope.siteIds.push(id);
 	  } 
 	  if(flag == false) {
 		  $scope.siteIds.splice($scope.siteIds.indexOf(id),1);
 	  }
 	  
   };
   
   $scope.getVinData = function() {
 	  $http.get('/getVehicleInfo/'+$scope.vinNumber)
		.success(function(data) {
			$scope.vinData = data;
		});
   }
   
   $scope.saveVehicle = function() {
 	  console.log($scope.vinData);
 	  $scope.vinData.specification.siteIds = $scope.siteIds;
 	  
 	  $http.post('/saveVehicle',$scope.vinData.specification)
		.success(function(data) {
			console.log('success');
		});
 	  
   }
   
   $scope.addPhoto = function() {
	   $scope.saveVehicle();
	   $location.path('/addPhoto/'+$scope.vinData.specification.vin);
   }
   
}]);


angular.module('newApp')
.controller('PhotoUploadCtrl', ['$scope','$routeParams','$location', function ($scope,$routeParams,$location) {
	console.log($routeParams.num);
   var myDropzone = new Dropzone("#dropzoneFrm",{
	   parallelUploads: 30,
	   headers: { "vinNum": $routeParams.num },
	   acceptedFiles:"image/*",
	   addRemoveLinks:true,
	   autoProcessQueue:false
   });
   $scope.uploadFiles = function() {
	   Dropzone.autoDiscover = false;
	   myDropzone.processQueue();
	   $location.path('/managePhotos/'+$routeParams.num);
   }
   
}]);

angular.module('newApp')
.controller('ManagePhotoCtrl', ['$scope','$routeParams','$location','$http', function ($scope,$routeParams,$location,$http) {
	$scope.imageList = [];
	$scope.init = function() {
		$http.get('/getImagesByVin/'+$routeParams.num)
		.success(function(data) {
			console.log(data);
			$scope.imageList = data;
		});
	}
   
	$scope.deleteImage = function(img) {
		$http.get('/deleteImage/'+img.id)
		.success(function(data) {
			console.log('success');
			$scope.imageList.splice($scope.imageList.indexOf(img),1);
		});
		
	}
	
}]);




