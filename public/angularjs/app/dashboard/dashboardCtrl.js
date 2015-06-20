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
			if($scope.vinData.specification.siteIds != null) {
				for(var i=0;i<$scope.vinData.specification.siteIds.length;i++) {
					for(var j=0;j<$scope.siteList.length;j++) {
						if($scope.vinData.specification.siteIds[i] == $scope.siteList[j].id) {
							$scope.siteList[j].flag = true;
						}
					}
				}
			}
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
	$scope.gridsterOpts = {
		    columns: 6, // the width of the grid, in columns
		    pushing: true, // whether to push other items out of the way on move or resize
		    floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
		    swapping: true, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
		     width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
		     colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
		    rowHeight: 'match', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
		    margins: [10, 10], // the pixel distance between each widget
		    outerMargin: true, // whether margins apply to outer edges of the grid
		    isMobile: false, // stacks the grid items if true
		    mobileBreakPoint: 600, // if the screen is not wider that this, remove the grid layout and stack the items
		    mobileModeEnabled: true, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
		    minColumns: 6, // the minimum columns the grid must have
		    minRows: 1, // the minimum height of the grid, in rows
		    maxRows: 100,
		    defaultSizeX: 1, // the default width of a gridster item, if not specifed
		    defaultSizeY: 1, // the default height of a gridster item, if not specified
		    resizable: {
			       enabled: false,
			       handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
			       start: function(event, $element, widget) {}, // optional callback fired when resize is started,
			       resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
			       stop: function(event, $element, widget) {} // optional callback fired when item is finished resizing
			    },
		    /* minSizeX: 1, // minimum column width of an item
		    maxSizeX: null, // maximum column width of an item
		    minSizeY: 1, // minumum row height of an item
		    maxSizeY: null, // maximum row height of an item
		   */
			    draggable: {
				       enabled: true, // whether dragging items is supported
				       handle: '.my-class', // optional selector for resize handle
				       start: function(event, $element, widget) {}, // optional callback fired when drag is started,
				       drag: function(event, $element, widget) {}, // optional callback fired when item is moved,
				       stop: function(event, $element, widget) {
				    	   //console.log($element[0].getAttribute("data-row")+" "+$element[0].getAttribute("data-col")+" "+$element[0].getAttribute("id"));
				    	   $http.get('/savePosition/'+$element[0].getAttribute("data-row")+'/'+$element[0].getAttribute("data-col")+'/'+$element[0].getAttribute("id"))
					   		.success(function(data) {
					   			//console.log('success');
					   		});
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	$scope.imageList = [];
	$scope.init = function() {
		$http.get('/getImagesByVin/'+$routeParams.num)
		.success(function(data) {
			console.log(data);
			$scope.imageList = data;
		});
	}
   
	$scope.$on('ngRepeatFinished', function(ngRepeatFinishedEvent) {
		for(var i=0;i<$scope.imageList.length;i++) {
			if($scope.imageList[i].defaultImage == true) {
				$('#imgId'+i).css("border","3px solid");
				$('#imgId'+i).css("color","red");
			}
		}
	});
	
	$scope.setAsDefault = function(image,index) {
		
		for(var i=0;i<$scope.imageList.length;i++) {
			if($scope.imageList[i].defaultImage == true) {
				$http.get('/removeDefault/'+$scope.imageList[i].id)
				.success(function(data) {
					
				});
				$('#imgId'+i).removeAttr("style","");
				$scope.imageList[i].defaultImage = false;
			}
		}
		
		$http.get('/setDefaultImage/'+image.id)
		.success(function(data) {
			console.log('success');
			image.defaultImage = true;
			$('#imgId'+index).css("border","3px solid");
			$('#imgId'+index).css("color","red");
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

angular.module('newApp')
.controller('ViewVehiclesCtrl', ['$scope','$http','$location', function ($scope,$http,$location) {
  
	 $scope.$on('$viewContentLoaded', function () {

         function fnFormatDetails(oTable, nTr) {
             var aData = oTable.fnGetData(nTr);
             var sOut = '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">';
             sOut += '<tr><td>Rendering engine:</td><td>' + aData[1] + ' ' + aData[4] + '</td></tr>';
             sOut += '<tr><td>Link to source:</td><td>Could provide a link here</td></tr>';
             sOut += '<tr><td>Extra info:</td><td>And any further details here (images etc)</td></tr>';
             sOut += '</table>';
             return sOut;
         }

         /*  Insert a 'details' column to the table  */
         var nCloneTh = document.createElement('th');
         var nCloneTd = document.createElement('td');
         nCloneTd.innerHTML = '<i class="fa fa-plus-square-o"></i>';
         nCloneTd.className = "center";

         $('#table2 thead tr').each(function () {
             this.insertBefore(nCloneTh, this.childNodes[0]);
         });

         $('#table2 tbody tr').each(function () {
             this.insertBefore(nCloneTd.cloneNode(true), this.childNodes[0]);
         });

         /*  Initialse DataTables, with no sorting on the 'details' column  */
         var oTable = $('#table2').dataTable({
             destroy: true,
             "aoColumnDefs": [{
                 "bSortable": false,
                 "aTargets": [0]
             }],
             "aaSorting": [
                 [1, 'asc']
             ]
         });

         /*  Add event listener for opening and closing details  */
         $(document).on('click', '#table2 tbody td i', function () {
             var nTr = $(this).parents('tr')[0];
             if (oTable.fnIsOpen(nTr)) {
                 /* This row is already open - close it */
                 $(this).removeClass().addClass('fa fa-plus-square-o');
                 oTable.fnClose(nTr);
             } else {
                 /* Open this row */
                 $(this).removeClass().addClass('fa fa-minus-square-o');
                 oTable.fnOpen(nTr, fnFormatDetails(oTable, nTr), 'details');
             }
         });
     });

     $scope.$on('$destroy', function () {
         $('table').each(function () {
             if ($.fn.dataTable.isDataTable($(this))) {
                 $(this).dataTable({
                     "bDestroy": true
                 }).fnDestroy();
             }
         });
     });

   $scope.vehiClesList = [];
   $scope.viewVehiclesInit = function() {
 	  $http.get('/getAllVehicles')
 		.success(function(data) {
 			$scope.vehiClesList = data;
 			console.log(data);
 		});
   }
   
   $scope.deleteVehicle = function(id){
	   $http.get('/deleteVehicleById/'+id)
		.success(function(data) {
			 $scope.viewVehiclesInit();
		});
   }
   
   
   $scope.updateVehicleStatus = function(id){
	   $http.get('/updateVehicleStatus/'+id)
		.success(function(data) {
			 $scope.viewVehiclesInit();
		});
   }
   
   $scope.editingData = [];
   
   for (var i = 0; i <  $scope.vehiClesList.length; i++) {
     $scope.editingData[$scope.vehiClesList[i].id] = false;
     $scope.viewField = false;
   }
   

   $scope.modify = function(tableData){
	   $scope.viewField = true;
       $scope.editingData[tableData.id] = true;
   };

   $scope.update = function(tableData){
     console.log(tableData);
       $scope.editingData[tableData.id] = false;
       $scope.viewField = false;
       $http.post('/updateVehicle',tableData)
		.success(function(data) {
			console.log('success');
		});
   };
   
   $scope.cancle = function(tableData){
	   $scope.editingData[tableData.id] = false;
	   $scope.viewField = false;
   }
   
}]);



