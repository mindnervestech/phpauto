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
    'ngAutocomplete',
    'angucomplete',
    'angucomplete-alt',
    'textAngular',
    'nvd3',
    'ui.grid.cellNav'
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
        
       /* .when('/hoursOfOperations', {
            templateUrl: '/dealer/myprofile/hoursOfOperation.html',
            controller: 'myprofileCtrl'
        })
        */
        .when('/createUser', {
            templateUrl: '/dealer/myprofile/createUser.html',
            controller: 'createUserCtrl'
        })
        .when('/createLocation', {
            templateUrl: '/dealer/myprofile/createLocation.html',
            controller: 'createLocationCtrl'
        })
        .when('/deactiveUsers', {
            templateUrl: '/dealer/myprofile/deactiveUser.html',
            controller: 'DeactivateUserCtrl'
        })  
        .when('/deactiveLocations', {
            templateUrl: '/dealer/myprofile/deactivateLocations.html',
            controller: 'deactiveLocationCtrl'
        })
        .when('/sliderImages', {
            templateUrl: '/dealer/homePage/sliderImages.html',
            controller: 'HomePageCtrl'
        })
        .when('/blog', {
            templateUrl: '/dealer/homePage/blog.html',
            controller: 'HomePageCtrl'
        })
        .when('/contactUs', {
            templateUrl: '/dealer/homePage/contactUs.html',
            controller: 'HomePageCtrl'
        })
        
        .when('/warranty', {
            templateUrl: '/dealer/homePage/warranty.html',
            controller: 'HomePageCtrl'
        })
        
        .when('/vehicleProfile', {
            templateUrl: '/dealer/homePage/vehicleProfile.html',
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
        
        .when('/socialMedia', {
            templateUrl: '/dealer/config/socialMedia.html',
            controller: 'ConfigPageCtrl'
        })
        
        .when('/newsLetter', {
            templateUrl: '/dealer/config/newsLetterTab.html',
            controller: 'ConfigPageCtrl'
        })
        
        
        
        .when('/premiumLeads', {
            templateUrl: '/dealer/config/leads.html',
            controller: 'ConfigPageCtrl'
        })
        
        
        .when('/documentation', {
            templateUrl: '/dealer/config/documentation.html',
            controller: 'ConfigPageCtrl'
        })
        
        .when('/autoPortal', {
            templateUrl: '/dealer/config/autoPortal.html',
            controller: 'ConfigPageCtrl'
        })
        
        
        .when('/domainDetails', {
            templateUrl: '/dealer/config/domainPage.html',
            controller: 'ConfigPageCtrl'
        })
        
        
        .when('/plansAndBill', {
            templateUrl: '/dealer/config/planAndBills.html',
            controller: 'ConfigPageCtrl'
        })
        
               .when('/siteTestiMonials', {
            templateUrl: '/dealer/homePage/siteTestimonials.html',
            controller: 'HomePageCtrl'
        })
        
        .when('/siteAboutUs', {
            templateUrl: '/dealer/homePage/siteAboutUs.html',
            controller: 'HomePageCtrl'
        })
      
      .when('/comparision', {
          templateUrl: '/dealer/homePage/compare.html',
          controller: 'HomePageCtrl'
      })
      
        .when('/goToPageContent', {
            templateUrl: '/dealer/homePage/pageContent.html',
            controller: 'HomePageCtrl'
        })
        
         .when('/hoursOfOperations', {
            templateUrl: '/dealer/homePage/hoursOfOperation.html',
            controller: 'myprofileCtrl'
        })
        
        .when('/goToInventoryNew/:type', {
            templateUrl: '/dealer/homePage/siteInventoryNew.html',
            controller: 'HomePageCtrl'
        })
        
        .when('/goToInventoryUsed/:type', {
            templateUrl: '/dealer/homePage/siteInventoryUsed.html',
            controller: 'HomePageCtrl'
        })
        
        .when('/goToInventoryCoverImg/:type', {
            templateUrl: '/dealer/homePage/siteInventoryCoverImg.html',
            controller: 'HomePageCtrl'
        })
        
         .when('/goToInventoryCoverImgNew/:type', {
            templateUrl: '/dealer/homePage/siteInventoryCoverImgNew.html',
            controller: 'HomePageCtrl'
        })
        
         .when('/goToInventoryCoverImgComingSoon/:type', {
            templateUrl: '/dealer/homePage/siteInventoryCoverImgComingSoon.html',
            controller: 'HomePageCtrl'
        })
        
        
        .when('/goToInventoryComingsoon/:type', {
            templateUrl: '/dealer/homePage/siteInventoryComingSoon.html',
            controller: 'HomePageCtrl'
        })
        
       
        .when('/requestMoreInfo', {
            templateUrl: '/dealer/moreInfo/requestMoreInfo.html',
            controller: 'RequestMoreInfoCtrl'
        })
        
        .when('/contactUsInfo', {
            templateUrl: '/dealer/moreInfo/contactUsInfo.html',
            controller: 'contactUsInfoCtrl'
        })
        
        
        
        
        .when('/premiumpage', {
            templateUrl: '/dealer/moreInfo/premiumPage.html',
            controller: 'premiumCtrl'
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
        .when('/sessionsAnalytics/:id/:vin/:status', {
            templateUrl: '/dealer/analytics/sessionsData.html',
            controller: 'SessionsCtrl'
        })
        
         .when('/goToVehicalInfo/:id/:vin/:status', {
            templateUrl: '/dealer/analytics/VehicleDateWise.html',
            controller: 'VehicleDateWiseCtrl'
        })
        
        .when('/allVehicleSessions', {
            templateUrl: '/dealer/analytics/allVehicleSessionsData.html',
            controller: 'allVehicleSessionsDataCtrl'
        })
        
        .when('/allPlatformsInfos', {
            templateUrl: '/dealer/analytics/allPlatformsInfos.html',
            controller: 'allPlatformsInfoCtrl'
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
        .when('/cropImage/:id/:vid', {
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
        
        .when('/cropInventoryImage/:id/:findById/:vType', {
            templateUrl: '/dealer/addPhotos/cropInventoryImage.html',
            controller: 'InventoryCropCtrl'
        })
         .when('/cropCoverImage/:id/:findById', {
            templateUrl: '/dealer/addPhotos/cropCoverPhoto.html',
            controller: 'CoverCropCtrl'
        })
        
        .when('/cropBlogImage/:id/:findById', {
            templateUrl: '/dealer/addPhotos/cropBlogPhoto.html',
            controller: 'BlogCropCtrl'
        })
        
        .when('/cropCompareImage/:id/:findById', {
            templateUrl: '/dealer/addPhotos/cropCompare.html',
            controller: 'CompareCropCtrl'
        })
        
        
        .when('/cropWarImage/:id/:findById', {
            templateUrl: '/dealer/addPhotos/cropWarrantyPhoto.html',
            controller: 'WarrantyCropCtrl'
        })
        
        
        .when('/editVehicleImage/:id/:findById', {
            templateUrl: '/dealer/addPhotos/vehicleProfileCrop.html',
            controller: 'VehicleCropCtrl'
        })
        
        
        .when('/editContactImage/:id/:findById', {
            templateUrl: '/dealer/addPhotos/cropContactPhoto.html',
            controller: 'ContactCropCtrl'
        })
        
        
        .when('/managePhotos/:num', {
            templateUrl: '/dealer/addPhotos/managePhotos.html',
            controller: 'ManagePhotoCtrl'
        })
        .when('/editVehicle/:id/:temp', {
            templateUrl: '/dealer/addVehicle/editVehicle.html',
            controller: 'EditVehicleCtrl'
        })
         .when('/viewVehicles', {
            templateUrl: '/dealer/viewVehicle/viewVehicles.html',
            controller: 'ViewVehiclesCtrl'
        })
        
        .when('/viewRegistration', {
            templateUrl: '/dealer/viewRegistration/viewRegistration.html',
            controller: 'ViewRegistrationCtrl'
        })
        
        .when('/viewClient', {
            templateUrl: '/dealer/viewClient/viewClient.html',
            controller: 'ViewClientCtrl'
        })
        
        
         .when('/dashboardLocation/:LocationId/:managerId/:gmIsManager', {
            templateUrl: '/dealer/dashboard/dashboardLocation.html',
            controller: 'dashboardLocationCtrl'
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
        	if(config.url == '/getInfoCount' || config.url =='/getVisitorOnline') {
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

String.prototype.splice = function(idx, rem, s) {
    return (this.slice(0, idx) + s + this.slice(idx + Math.abs(rem)));
};

MakeApp.directive('currencyInput', function() {
    return {
        restrict: 'A',
        scope: {
            field: '='
        },
        replace: true,
        template: '<span><input type="text" class="form-control form-white" style="border-color:red;" ng-model="field"></input></span>',
        link: function(scope, element, attrs) {

            $(element).bind('keyup', function(e) {
                var input = element.find('input');
                var inputVal = input.val();

                //clearing left side zeros
                while (scope.field.charAt(0) == '0') {
                    scope.field = scope.field.substr(1);
                }

                scope.field = scope.field.replace(/[^\d.\',']/g, '');

                var point = scope.field.indexOf(".");
                if (point >= 0) {
                    scope.field = scope.field.slice(0, point + 3);
                }

                var decimalSplit = scope.field.split(".");
                var intPart = decimalSplit[0];
                var decPart = decimalSplit[1];

                intPart = intPart.replace(/[^\d]/g, '');
                if (intPart.length > 3) {
                    var intDiv = Math.floor(intPart.length / 3);
                    while (intDiv > 0) {
                        var lastComma = intPart.indexOf(",");
                        if (lastComma < 0) {
                            lastComma = intPart.length;
                        }

                        if (lastComma - 3 > 0) {
                            intPart = intPart.splice(lastComma - 3, 0, ",");
                        }
                        intDiv--;
                    }
                }

                if (decPart === undefined) {
                    decPart = "";
                }
                else {
                    decPart = "." + decPart;
                }
                var res = intPart + decPart;

                scope.$apply(function() {scope.field = res});

            });

        }
    };
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
