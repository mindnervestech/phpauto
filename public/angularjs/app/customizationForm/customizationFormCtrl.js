'use strict';

/**
 * @ngdoc function
 * @name newappApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the newappApp
 */

angular.module('newApp')
  .controller('customizationFormCtrl', ['$scope', 'dashboardService', 'pluginsService', '$http','$compile','$interval','$filter','$location','$timeout','$route','$q','$upload','$builder','$routeParams',function ($scope, dashboardService, pluginsService,$http,$compile,$interval,$filter,$location,$timeout,$route,$q,$upload,$builder,$routeParams) {

	  	$scope.showSaveButton = $routeParams.pageType;
	  	
	  
		   $scope.initFunction = function(){
			   $http.get('/getCustomizationform/'+'Create Lead').success(function(response) {
					
					$scope.setjson = response;
					$scope.setjson.jsonData = angular.fromJson(response.jsonData);
					$scope.initComponent = angular.fromJson(response.jsonData);
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
		       
		       
		  $scope.saveCreateLeadForm = function(){
			 console.log($scope.form);
			 console.log($builder.forms['default']);
			 $scope.form = $builder.forms['default'];
			  $http.post('/getLeadCrateForm', $scope.form)
				 .success(function(data) {
					 $.pnotify({
	    				    title: "Success",
	    				    type:'success',
	    				    text: "Form Created successfully",
	    				});
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
			  $http.post('/getLeadCrateFormTitle', $scope.setjson)
				 .success(function(data) {
					 $.pnotify({
	    				    title: "Success",
	    				    type:'success',
	    				    text: "Title Edit successfully",
	    				});
					 $scope.initFunction();
						$('#edititle').modal('hide');
					});
			  
		  }
		  
		
		 
		   
  }]);
