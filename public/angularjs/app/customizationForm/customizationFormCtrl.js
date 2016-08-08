'use strict';

/**
 * @ngdoc function
 * @name newappApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the newappApp
 */

angular.module('newApp')
  .controller('customizationFormCtrl', ['$scope', 'dashboardService', 'pluginsService', '$http','$compile','$interval','$filter','$location','$timeout','$route','$q','$upload','$builder','$routeParams','apiserviceCustomizationForm',function ($scope, dashboardService, pluginsService,$http,$compile,$interval,$filter,$location,$timeout,$route,$q,$upload,$builder,$routeParams,apiserviceCustomizationForm) {

	  	$scope.showSaveButton = $routeParams.pageType;
	  	$scope.formType = $routeParams.formType;
	  	console.log($routeParams.formType);
	  	
	  if($routeParams.formType == "Schedule Test Drive"){
		  $scope.appointFlag=1;
		  
	  }
	  
		   $scope.initFunction = function(){
			   apiserviceCustomizationForm.getCustomizationform($routeParams.formType).then(function(response){
			   
					console.log(response);

					if(response == 0){
						$scope.setjson = null;
						$scope.setjson.jsonData = null;
						$scope.initComponent = null;
					}else{
						$scope.setjson = response;
						$scope.setjson.jsonData = angular.fromJson(response.jsonData);
						$scope.initComponent = angular.fromJson(response.jsonData);
					}
					
			});
			   apiserviceCustomizationForm.getAllSites().then(function(data){
				   $scope.siteList = data;
		 		});
		   }
	  
	  
		/*------------------Form Builder ---------------------*/
		   $scope.setjson = {};
		   	$scope.initComponent = [];
			var vm = this;
			$scope.fields = [];
			$scope.component = [];
		  $scope.formName;
		  $scope.subSectonsName = 'default';

		   	$scope.options = {
				formState: {
			    	awesomeIsForced: false,
			  	}
			};
		  $scope.initComponent = [];
		  $scope.FB = [];
		  var FBuilder = {
		    'initComponent': $scope.initComponent,
		    'name':'default'
		  };
		    var count = 0;  
		    $scope.FB.push(FBuilder);
		 
		 console.log($builder, "$builder");
		    $scope.addSubsection = function() {
		    var formName = []
		    $builder.forms['default'+count]= formName;
		    var FBuilder = {
		        'initComponent': $scope.initComponent,
		        'name' : 'default'+count
		    };
		    $scope.subSectonsName = FBuilder.name;
		    $scope.FB.push(FBuilder);
		    count++; 
		    };
		    

		    $scope.form = $builder.forms['default'];
		       $scope.$watch('form', function() {
		         console.log('$scope.form',$scope.form);
		         console.log('$builder.forms',$builder.forms);
		       }, true);
		       
		       $scope.editform = {};
		  $scope.saveCreateLeadForm = function(){
			 console.log($scope.form);
			 angular.forEach($builder.forms['default'], function(value, key) {
				 var key;
               		key = value.label;
               		key = key.replace("  ","_");
               		key = key.replace(" ","_");
               		key = key.toLowerCase();
               		value.key = key;
               		console.log(key);
				 console.log(value.key);
			 });
			 console.log($builder.forms['default']);
			 $scope.editform.formType = $routeParams.formType;
			 $scope.editform.jsonform = $builder.forms['default'];
			 
			 console.log($scope.editform);
			 apiserviceCustomizationForm.getLeadCrateForm($scope.editform).then(function(data){
			  
					});
		  }     
		  $scope.editLeadInfo = function(title){
			   $scope.setjson.showFild = title;
			  $('#edititle').modal('show');

		  }
		  $scope.editAllTitle = function(){
			 
			  delete $scope.setjson.locations;
			  delete $scope.setjson.showFild;
			  delete $scope.setjson.jsonData;
			  $scope.setjson.formType = $routeParams.formType;
			  apiserviceCustomizationForm.getLeadCrateFormTitle($scope.setjson).then(function(data){

				  		$scope.initFunction();
						$('#edititle').modal('hide');
					});
			  
		  }
		  
		
		 
		   
  }]);
