'use strict';

/**
 * @ngdoc overview
 * @name newappApp
 * @description
 * # newappApp
 *
 * Main module of the application.
 */
var MakeApp = angular
  .module('newApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ui.bootstrap',
    'gridster',
    'ui.grid',
    'ui.grid.edit',
    'ui.grid.pagination',
    'angularFileUpload',
    'textAngular',
    'nvd3'
  ])
  .config(function ($routeProvider) {
      $routeProvider
        .when('/', {
            templateUrl: '/dealer/dashboard/dashboard.html',
            controller: 'dashboardCtrl'
        })
        .when('/addVehicle', {
            templateUrl: '/dealer/addVehicle/addVehicle.html',
            controller: 'addVehicleCtrl'
        })
        .when('/homePage', {
            templateUrl: '/dealer/homePage/homePage.html',
            controller: 'HomePageCtrl'
        })
       .when('/myprofile', {
            templateUrl: '/dealer/myprofile/myprofile.html',
            controller: 'myprofileCtrl'
        })
        .when('/createUser', {
            templateUrl: '/dealer/myprofile/createUser.html',
            controller: 'createUserCtrl'
        })
        .when('/createLocation', {
            templateUrl: '/dealer/myprofile/createLocation.html',
            controller: 'createLocationCtrl'
        })
        
         
        .when('/sliderImages', {
            templateUrl: '/dealer/homePage/sliderImages.html',
            controller: 'HomePageCtrl'
        })
        .when('/featuredImages', {
            templateUrl: '/dealer/homePage/featuredImages.html',
            controller: 'HomePageCtrl'
        })
        .when('/crm', {
            templateUrl: '/dealer/CRM/crm.html',
            controller: 'crmCtrl'
        })
        .when('/siteSlogan', {
            templateUrl: '/dealer/homePage/siteSlogan.html',
            controller: 'HomePageCtrl'
        })
        .when('/siteDescription', {
            templateUrl: '/dealer/homePage/siteDescription.html',
            controller: 'HomePageCtrl'
        })
        .when('/siteLogo', {
            templateUrl: '/dealer/homePage/siteLogo.html',
            controller: 'HomePageCtrl'
        })
         .when('/configuration', {
            templateUrl: '/dealer/config/configuration.html',
            controller: 'ConfigPageCtrl'
        })
        .when('/requestMoreInfo', {
            templateUrl: '/dealer/moreInfo/requestMoreInfo.html',
            controller: 'RequestMoreInfoCtrl'
        })
         .when('/scheduleTest', {
            templateUrl: '/dealer/moreInfo/scheduleTest.html',
            controller: 'ScheduleTestCtrl'
        })
        .when('/visitorsAnalytics', {
            templateUrl: '/dealer/analytics/visitors.html',
            controller: 'VisitorsCtrl'
        })
        .when('/actionsAnalytics', {
            templateUrl: '/dealer/analytics/actions.html',
            controller: 'ActionsCtrl'
        })
        .when('/basicsAnalytics', {
            templateUrl: '/dealer/analytics/basics.html',
            controller: 'BasicsCtrl'
        })
        .when('/searchesAnalytics', {
            templateUrl: '/dealer/analytics/searches.html',
            controller: 'SearchesCtrl'
        })
        .when('/refferersAnalytics', {
            templateUrl: '/dealer/analytics/refferers.html',
            controller: 'RefferersCtrl'
        })
        .when('/heatMapInfoAnalytics', {
            templateUrl: '/dealer/analytics/heatMapInfo.html',
            controller: 'heatMapInfoCtrl'
        })
        .when('/sessionsAnalytics/:vin', {
            templateUrl: '/dealer/analytics/sessionsData.html',
            controller: 'SessionsCtrl'
        })
        
        .when('/allVehicleSessions', {
            templateUrl: '/dealer/analytics/allVehicleSessionsData.html',
            controller: 'allVehicleSessionsDataCtrl'
        })
        .when('/contentAnalytics', {
            templateUrl: '/dealer/analytics/content.html',
            controller: 'ContentCtrl'
        })
         .when('/tradeIn', {
            templateUrl: '/dealer/moreInfo/tradeIn.html',
            controller: 'TradeInCtrl'
        })
        .when('/blogs', {
            templateUrl: '/dealer/blogs/showBlogs.html',
            controller: 'BlogsCtrl'
        })
        .when('/createBlog', {
            templateUrl: '/dealer/blogs/createBlog.html',
            controller: 'CreateBlogCtrl'
        })
        .when('/editBlog/:id', {
            templateUrl: '/dealer/blogs/editBlog.html',
            controller: 'EditBlogCtrl'
        })
        .when('/addPhoto/:num', {
            templateUrl: '/dealer/addPhotos/uploadPhoto.html',
            controller: 'PhotoUploadCtrl'
        })
        .when('/cropImage/:id', {
            templateUrl: '/dealer/addPhotos/cropImage.html',
            controller: 'ImageCropCtrl'
        })
        .when('/cropSliderImage/:id', {
            templateUrl: '/dealer/addPhotos/cropSliderImage.html',
            controller: 'SliderCropCtrl'
        })
        .when('/cropFeaturedImage/:id', {
            templateUrl: '/dealer/addPhotos/cropFeaturedImage.html',
            controller: 'FeaturedCropCtrl'
        })
        .when('/managePhotos/:num', {
            templateUrl: '/dealer/addPhotos/managePhotos.html',
            controller: 'ManagePhotoCtrl'
        })
        .when('/editVehicle/:id', {
            templateUrl: '/dealer/addVehicle/editVehicle.html',
            controller: 'EditVehicleCtrl'
        })
         .when('/viewVehicles', {
            templateUrl: '/dealer/viewVehicle/viewVehicles.html',
            controller: 'ViewVehiclesCtrl'
        })
        
        .otherwise({
            redirectTo: '/'
        });
  })
   .directive('onFinishRender', function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function () {
                    scope.$emit('ngRepeatFinished');
                });
            }
        }
    }
 })
 .directive('ngSec',function(){
    return {
    	link: function(scope, element, attrs, ngModelCtrl) {
    		var sec = permissions;
    		var obj = sec[attrs.ngSec];
    		if(typeof  obj === 'undefined') {
    			$(element).remove();
    		}
    		if(obj === 'false') {
    			$(element).remove();
    		}
    		if(obj === 'R') {
    			$(element).block(); //todo http://malsup.com/jquery/block
    			//$(element).children().off('click');
    			
    		}
    	}
    }
})
.factory('MyHttpInterceptor', function ($q) {
    return {
        request: function (config) {
        	if(config.url == '/getInfoCount') {
        		$('#loading').hide();
        	} else {
        		$('#loading').show();
        	}
                    
                    return config || $q.when(config);           
        },
   
        requestError: function (rejection) {
                    $('#loading').hide();
            return $q.reject(rejection);
        },
        
        // On response success
        response: function (response) {
                    $('#loading').hide();
            return response || $q.when(response);
        },
        
        // On response failture
        responseError: function (rejection) {
                    $('#loading').hide();
            return $q.reject(rejection);
        }
      };
})
.config(function ($httpProvider) {
     $httpProvider.interceptors.push('MyHttpInterceptor');  
});

// Route State Load Spinner(used on page or content load)
MakeApp.directive('ngSpinnerLoader', ['$rootScope',
    function($rootScope) {
        return {
            link: function(scope, element, attrs) {
                // by defult hide the spinner bar
                element.addClass('hide'); // hide spinner bar by default
                // display the spinner bar whenever the route changes(the content part started loading)
                $rootScope.$on('$routeChangeStart', function() {
                    //element.removeClass('hide'); // show spinner bar
                });
                // hide the spinner bar on rounte change success(after the content loaded)
                $rootScope.$on('$routeChangeSuccess', function() {
                    setTimeout(function(){
                        element.addClass('hide'); // hide spinner bar
                    },500);
                    $("html, body").animate({
                        scrollTop: 0
                    }, 500);   
                });
            }
        };
    }

]);
