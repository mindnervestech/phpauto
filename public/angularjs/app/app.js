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
    'builder',
    'builder.components',
    'starter.apiservice',
    'app.apiserviceUser',
    'app.apiserviceAddEditInventory',
    'app.apiserviceBlog',
    'app.apiserviceProfile',
    'app.apiserviceLocation',
    'app.apiserviceRegistration',
    'app.apiserviceAddEditVehicle',
    'app.apiserviceDashborad',
    'app.apiserviceCrm',
    'app.apiserviceMoreInfo',
    'app.apiserviceViewInventory',
    'app.apiserviceViewClient',
    'app.apiserviceInventory',
    'app.apiserviceCustomizationForm',
    'app.apiserviceViewVehicle',
    'app.apiserviceHomePage',
    'app.apiserviceConfigPage',
    'app.apiserviceAnalytics',
    'app.apiserviceAddPhotos',
   // 'textAngular',
    'nvd3',
    'formly',
    'ionic',
    'formlyIonic',
    'ngDialog',
    'ui.grid.cellNav'
  ])
  .config(function ($routeProvider) {
      $routeProvider
        .when('/', {
            templateUrl: '/dealer/dashboard/dashboard.html',
            controller: 'dashboardCtrl'
        })
        .when('/addInventory', {
            templateUrl: '/dealer/addInventory/addInventory.html',
            controller: 'addInventoryCtrl'
        })

        .when('/CreateLeadForm/:pageType/:formType', {
            templateUrl: '/dealer/customizationForm/createLeadForm.html',
            controller: 'customizationFormCtrl'
        })
        .when('/RequestMoreInfoForm/:pageType/:formType', {
            templateUrl: '/dealer/customizationForm/allLead.html',
            controller: 'customizationFormCtrl'
        })
        
        .when('/TradeIn/:pageType/:formType', {
            templateUrl: '/dealer/customizationForm/allLead.html',
            controller: 'customizationFormCtrl'
        })
        
        .when('/otherForm/:pageType/:formType', {
            templateUrl: '/dealer/customizationForm/other.html',
            controller: 'customizationFormCtrl'
        })
        
        
        .when('/RequestAppointmentForm/:pageType/:formType', {
            templateUrl: '/dealer/customizationForm/allLead.html',
            controller: 'customizationFormCtrl'
        })
        
        .when('/InventoryForm/:pageType/:formType', {
            templateUrl: '/dealer/customizationForm/createInventoryForm.html',
            controller: 'customizationFormCtrl'
        })
        .when('/CRMForm/:pageType/:formType', {
            templateUrl: '/dealer/customizationForm/createCrmForm.html',
            controller: 'customizationFormCtrl'
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
        
         .when('/webAnalytics', {
            templateUrl: '/dealer/config/websiteAnalytics.html',
            controller: 'ConfigPageCtrl'
        })
         .when('/mailchimpPage', {
            templateUrl: '/dealer/config/mailchimp.html',
            controller: 'ConfigPageCtrl'
        })
        
        .when('/leadtype', {
            templateUrl: '/dealer/config/leadtype.html',
            controller: 'ConfigPageCtrl'
        })
        
        .when('/form', {
            templateUrl: '/dealer/config/form.html',
            controller: 'ConfigPageCtrl'
        })
         .when('/webSite', {
            templateUrl: '/dealer/config/webSite.html',
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
        .when('/myCalendar', {
           templateUrl: 'dealer/myCalendar/myCalendar.html',
           controller: 'myCalendarCtrl'
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
        
        
        .when('/goToMakeImageUpload/:makeValue', {
            templateUrl: '/dealer/homePage/coverImageForMake.html',
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
        .when('/otherLeads/:leadId', {
            templateUrl: '/dealer/moreInfo/otherLeads.html',
            controller: 'otherLeadCtrl'
        })
        
        
        
        .when('/premiumpage', {
            templateUrl: '/dealer/moreInfo/premiumPage.html',
            controller: 'premiumCtrl'
        })
        
         .when('/scheduleTest', {
            templateUrl: '/dealer/moreInfo/scheduleTest.html',
            controller: 'ScheduleTestCtrl'
        })
        
        
        .when('/goToVideoAnalytics', {
            templateUrl: '/dealer/analytics/videoAnalytics.html',
            controller: 'VideoAnalyticsCtrl'
        })
        
      .when('/visitorInfo/:visitorInfo/:startDate1/:endDate1', {
          templateUrl: '/dealer/analytics/visitorInfo.html',
          controller: 'VisitorsCtrl'
      })
       .when('/ipAddressInfo/:ipAddressInfo/:startDate1/:endDate1', {
          templateUrl: '/dealer/analytics/visitorInfo.html',
          controller: 'VisitorsCtrl'
      })
      
      .when('/visitorInfoForMap/:type/:flagForLocation/:startDate2/:endDate2', {
          templateUrl: '/dealer/analytics/referrerTypeData.html',
          controller: 'VisitorsCtrl'
      })
      .when('/visitorInfoForDomain/:idForDomain/:flagForLocation/:startDate2/:endDate2/:refDomain', {
          templateUrl: '/dealer/analytics/referrerTypeData.html',
          controller: 'VisitorsCtrl'
      })
      .when('/visitorInfoForRefferal/:idForRefferal/:flagForLocation/:startDate2/:endDate2', {
          templateUrl: '/dealer/analytics/referrerTypeData.html',
          controller: 'VisitorsCtrl'
      })
      .when('/landingPageInfo/:idOfLanding/:flagForLandingUrl/:startDateForLand/:endDateForLand', {
          templateUrl: '/dealer/analytics/landingPage.html',
          controller: 'VisitorsCtrl'
      })
      
      .when('/getEngActionInfo/:title', {
          templateUrl: '/dealer/analytics/engActionAvgData.html',
          controller: 'VisitorsCtrl'
      })
      
      
      .when('/getEngActionTime/:engTimeTitle', {
          templateUrl: '/dealer/analytics/engActionAvgData.html',
          controller: 'VisitorsCtrl'
      })
      
      .when('/getTrafficInfo/:trafficSourceTitle', {
          templateUrl: '/dealer/analytics/engActionAvgData.html',
          controller: 'VisitorsCtrl'
      })
      
      
      .when('/getSearchInfo/:idForSearch', {
          templateUrl: '/dealer/analytics/searchTabInfo.html',
          controller: 'SearchesCtrl'
      }) 
      
      .when('/getEngineInfo/:idForEngine', {
          templateUrl: '/dealer/analytics/searchTabInfo.html',
          controller: 'SearchesCtrl'
      }) 
      
      .when('/getRecentInfo/:idForRecent', {
          templateUrl: '/dealer/analytics/searchTabInfo.html',
          controller: 'SearchesCtrl'
      }) 
      
      .when('/getNewestInfo/:idForNewest', {
          templateUrl: '/dealer/analytics/searchTabInfo.html',
          controller: 'SearchesCtrl'
      }) 
      
      .when('/getRankingInfo/:idForRanking', {
          templateUrl: '/dealer/analytics/searchTabInfo.html',
          controller: 'SearchesCtrl'
      }) 
      
      
        .when('/visitorsAnalytics', {
            templateUrl: '/dealer/analytics/visitors.html',
            controller: 'VisitorsCtrl'
        })
        
        .when('/visitorsAnalytics/:typeOfInfo', {
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
        
        .when('/CampaignsAnalytics', {
            templateUrl: '/dealer/analytics/campaigns.html',
            controller: 'CampaignsCtrl'
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
        
         .when('/platformInfo/:idForBrowser', {
            templateUrl: '/dealer/analytics/platformInfo.html',
            controller: 'allPlatformsInfoCtrl'
        })
        
        
         .when('/ResolutionInfo/:idForScreen', {
            templateUrl: '/dealer/analytics/platformInfo.html',
            controller: 'allPlatformsInfoCtrl'
        })
        
          .when('/HardwareInfo/:idForHardware', {
            templateUrl: '/dealer/analytics/platformInfo.html',
            controller: 'allPlatformsInfoCtrl'
        })
        
         .when('/operatingSystemInfo/:idForOS', {
            templateUrl: '/dealer/analytics/platformInfo.html',
            controller: 'allPlatformsInfoCtrl'
        })
        
        .when('/contentInfo/:idForMedia', {
            templateUrl: '/dealer/analytics/contentInfo.html',
            controller: 'goToContentInfoCtrl' 
        })
        
        .when('/goToContentInfo', {
            templateUrl: '/dealer/analytics/goToContentInfo.html',
            controller: 'goToContentInfoCtrl' 
        })
         .when('/entranceGrid/:id', {
            templateUrl: '/dealer/analytics/entrancePageGrid.html',
            controller: 'goToContentInfoCtrl'
        })
         .when('/pagesGrid/:idForPages', {
            templateUrl: '/dealer/analytics/entrancePageGrid.html',
            controller: 'goToContentInfoCtrl'
        })
        
        .when('/getEventData/:idForEvent', {
            templateUrl: '/dealer/analytics/entrancePageGrid.html',
            controller: 'goToContentInfoCtrl'
        })
         .when('/getExitData/:idForExit', {
            templateUrl: '/dealer/analytics/entrancePageGrid.html',
            controller: 'goToContentInfoCtrl'
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
        .when('/cropImage/:id/:vid/:vin', {
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
        
       .when('/cropInventoryImages/:id/:pId/:productId', {
            templateUrl: '/dealer/addInventory/cropInventoryImage.html',
            controller: 'InventoryImageCropCtrl'
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
        
        
        .when('/editVehicleImage/:id/:findById/:makeValue', {
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
	
	.when('/editInventory/:id/:temp/:productId', {
            templateUrl: '/dealer/addInventory/editInventory.html',
            controller: 'EditInventoryCtrl'
        })
	
        .when('/editVehicle/:id/:temp/:vin', {
            templateUrl: '/dealer/addVehicle/editVehicle.html',
            controller: 'EditVehicleCtrl'
        })
       /* .when('/editVehicle/:id/:temp', {
            templateUrl: '/dealer/addVehicle/editVehicle.html',
            controller: 'EditVehicleCtrl'
        })*/
        .when('/editVehicleNew/:id/:temp/:vinNew', {
            templateUrl: '/dealer/addVehicle/editVehicle.html',
            controller: 'EditVehicleCtrl'
        })
        
         .when('/viewVehicles', {
            templateUrl: '/dealer/viewVehicle/viewVehicles.html',
            controller: 'ViewVehiclesCtrl'
        })
        
	 .when('/viewInventory', {
            templateUrl: '/dealer/viewInventory/viewInventory.html',
            controller: 'viewInventoryCtrl'
        })
        
        .when('/inventoryPhotographer', {
            templateUrl: '/dealer/inventory/inventoryPhotographer.html',
            controller: 'inventoryPhotographer'
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
