
angular.module('newApp')
.controller('HomePageCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','apiserviceHomePage', function ($scope,$http,$location,$filter,$routeParams,$upload,apiserviceHomePage) {
	if(!$scope.$$phase) {
		$scope.$apply();
	}
	
	
	$scope.makeForUpload='/uploadVehiclePhotos?make='+$routeParams.makeValue;
	if( $routeParams.makeValue != null || $routeParams.makeValue !=undefined ){
		$scope.makeValue=$routeParams.makeValue;
	}
	else{
		$scope.makeValue='all';
	}
	
	$scope.tempDate = new Date().getTime();
	console.log($routeParams.type);
	$scope.vTypes = $routeParams.type;
	
	$scope.sliderList = [];
	$scope.featuredList = [];
	$scope.coverList = [];
	$scope.blogList = [];
	$scope.makeListForImage = [];
	$scope.compareList = [];
	$scope.warrantyList = [];
	$scope.contactList = [];
	$scope.vehicleProfileList = [];
	$scope.gridsterOpts1 = {
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
				    	   if($(event.target).html() == 'Edit' || $(event.target)[0].className == 'glyphicon glyphicon-zoom-in' || $(event.target)[0].className == 'btn fa fa-times') {
				    		   return;
				    	   };
				    	   for(var i=0;i<$scope.sliderList.length;i++) {
				    		   delete $scope.sliderList[i].description;
				    		   delete $scope.sliderList[i].width;
				    		   delete $scope.sliderList[i].height;
				    		   delete $scope.sliderList[i].link;
				    		   delete $scope.sliderList[i].vin;
				    		   delete $scope.sliderList[i].defaultImage;
				    	   }
				    	   apiserviceHomePage.saveSliderPosition($scope.sliderList).then(function(data){
				    	   
					   			
					   		});
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	
	$scope.gridsterOpts2 = {
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
				    	   
				    	   if($(event.target).html() == 'Edit' || $(event.target)[0].className == 'glyphicon glyphicon-zoom-in' || $(event.target)[0].className == 'btn fa fa-times') {
				    		   return;
				    	   };
				    	   
				    	   for(var i=0;i<$scope.featuredList.length;i++) {
				    		   delete $scope.featuredList[i].description;
				    		   delete $scope.featuredList[i].width;
				    		   delete $scope.featuredList[i].height;
				    		   delete $scope.featuredList[i].link;
				    		   delete $scope.featuredList[i].vin;
				    		   delete $scope.featuredList[i].defaultImage;
				    	   }
				    	   apiserviceHomePage.saveFeaturedPosition($scope.featuredList).then(function(data){
				    	   });
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	
	
	

	$scope.gridsterOpts5 = {
		    columns: 6, // the width of the grid, in columns
		    pushing: true, // whether to push other items out of the way on move or resize
		    floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
		    swapping: true, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
		     width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
		     colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
		    rowHeight: '160px', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
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
				    	   
				    	   if($(event.target).html() == 'Edit' || $(event.target)[0].className == 'glyphicon glyphicon-zoom-in' || $(event.target)[0].className == 'btn fa fa-times') {
				    		   return;
				    	   };
				    	   
				    	   for(var i=0;i<$scope.coverList.length;i++) {
				    		   delete $scope.coverList[i].description;
				    		   delete $scope.coverList[i].width;
				    		   delete $scope.coverList[i].height;
				    		   delete $scope.coverList[i].link;
				    		   delete $scope.coverList[i].vin;
				    		   delete $scope.coverList[i].defaultImage;
				    	   }
				    	   apiserviceHomePage.saveCoveredPosition($scope.coverList).then(function(data){
				    	   
					   		});
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	
	
	$scope.gridsterOpts9 = {
		    columns: 6, // the width of the grid, in columns
		    pushing: true, // whether to push other items out of the way on move or resize
		    floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
		    swapping: true, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
		     width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
		     colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
		    rowHeight: '160px', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
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
				    	   
				    	   if($(event.target).html() == 'Edit' || $(event.target)[0].className == 'glyphicon glyphicon-zoom-in' || $(event.target)[0].className == 'btn fa fa-times') {
				    		   return;
				    	   };
				    	   
				    	   for(var i=0;i<$scope.compareList.length;i++) {
				    		   delete $scope.compareList[i].description;
				    		   delete $scope.compareList[i].width;
				    		   delete $scope.compareList[i].height;
				    		   delete $scope.compareList[i].link;
				    		   delete $scope.compareList[i].vin;
				    		   delete $scope.compareList[i].defaultImage;
				    	   }
				    	   apiserviceHomePage.saveComparePosition($scope.compareList).then(function(data){
				    	   });
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	
	
	

	$scope.gridsterOpts6 = {
		    columns: 6, // the width of the grid, in columns
		    pushing: true, // whether to push other items out of the way on move or resize
		    floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
		    swapping: true, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
		     width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
		     colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
		    rowHeight: '160px', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
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
				    	   
				    	   if($(event.target).html() == 'Edit' || $(event.target)[0].className == 'glyphicon glyphicon-zoom-in' || $(event.target)[0].className == 'btn fa fa-times') {
				    		   return;
				    	   };
				    	   
				    	   for(var i=0;i<$scope.blogList.length;i++) {
				    		   delete $scope.blogList[i].description;
				    		   delete $scope.blogList[i].width;
				    		   delete $scope.blogList[i].height;
				    		   delete $scope.blogList[i].link;
				    		   delete $scope.blogList[i].vin;
				    		   delete $scope.blogList[i].defaultImage;
				    	   }
				    	   apiserviceHomePage.saveBlogPosition($scope.blogList).then(function(data){
				    	   });
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	

	$scope.gridsterOpts10 = {
		    columns: 6, // the width of the grid, in columns
		    pushing: true, // whether to push other items out of the way on move or resize
		    floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
		    swapping: true, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
		     width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
		     colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
		    rowHeight: '160px', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
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
				    	   
				    	   if($(event.target).html() == 'Edit' || $(event.target)[0].className == 'glyphicon glyphicon-zoom-in' || $(event.target)[0].className == 'btn fa fa-times') {
				    		   return;
				    	   };
				    	   
				    	   for(var i=0;i<$scope.vehicleProfileList.length;i++) {
				    		   delete $scope.vehicleProfileList[i].description;
				    		   delete $scope.vehicleProfileList[i].width;
				    		   delete $scope.vehicleProfileList[i].height;
				    		   delete $scope.vehicleProfileList[i].link;
				    		   delete $scope.vehicleProfileList[i].vin;
				    		   delete $scope.vehicleProfileList[i].defaultImage;
				    	   }
				    	   apiserviceHomePage.saveVehiclePosition($scope.vehicleProfileList).then(function(data){
				    	   });
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	
	
	
	
	
	
	
	$scope.gridsterOpts8 = {
		    columns: 6, // the width of the grid, in columns
		    pushing: true, // whether to push other items out of the way on move or resize
		    floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
		    swapping: true, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
		     width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
		     colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
		    rowHeight: '160px', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
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
				    	   
				    	   if($(event.target).html() == 'Edit' || $(event.target)[0].className == 'glyphicon glyphicon-zoom-in' || $(event.target)[0].className == 'btn fa fa-times') {
				    		   return;
				    	   };
				    	   
				    	   for(var i=0;i<$scope.warrantyList.length;i++) {
				    		   delete $scope.warrantyList[i].description;
				    		   delete $scope.warrantyList[i].width;
				    		   delete $scope.warrantyList[i].height;
				    		   delete $scope.warrantyList[i].link;
				    		   delete $scope.warrantyList[i].vin;
				    		   delete $scope.warrantyList[i].defaultImage;
				    	   }
				    	   apiserviceHomePage.saveWarrantyPosition($scope.warrantyList).then(function(data){
				    	   });
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	
	
	
	
	
	$scope.gridsterOpts7 = {
		    columns: 6, // the width of the grid, in columns
		    pushing: true, // whether to push other items out of the way on move or resize
		    floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
		    swapping: true, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
		     width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
		     colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
		    rowHeight: '160px', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
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
				    	   
				    	   if($(event.target).html() == 'Edit' || $(event.target)[0].className == 'glyphicon glyphicon-zoom-in' || $(event.target)[0].className == 'btn fa fa-times') {
				    		   return;
				    	   };
				    	   
				    	   for(var i=0;i<$scope.contactList.length;i++) {
				    		   delete $scope.contactList[i].description;
				    		   delete $scope.contactList[i].width;
				    		   delete $scope.contactList[i].height;
				    		   delete $scope.contactList[i].link;
				    		   delete $scope.contactList[i].vin;
				    		   delete $scope.contactList[i].defaultImage;
				    	   }
				    	   apiserviceHomePage.saveContactPosition($scope.contactList).then(function(data){
				    	   });
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	
	
	$scope.header={};
	$scope.header1={};
	$scope.header2={};
	$scope.header3={};
	$scope.header4={};
	$scope.profileHeader={};
	$scope.siteDescription = {};
	$scope.siteHeading = "";
	$scope.inventoryImg = [];
	$scope.init = function() {
		apiserviceHomePage.getAllMakeList().then(function(data){
		 
				console.log(">>>>>>>>>");
				console.log(data);
				$scope.makeList=data;
				$scope.makeWiseData();
				
				
			});
		
			 $scope.makeWiseData = function() {
			 apiserviceHomePage.getMakeWiseData($scope.makeValue).then(function(data){
		 
				console.log(data);
				console.log(data.vehProfData);
				$scope.vehProfDataByMake=data.vehProfData;
				console.log($scope.makeList);
				angular.forEach( $scope.makeList, function(value, key) {
					var keepGoing = true;
					// $scope.makeList[key].flagForMake=1;
					angular.forEach( $scope.vehProfDataByMake, function(value1, key1) {
						if(keepGoing) {
						if(value1.makeValue == value.make && value1.path != null ){
							
							
							$scope.makeList[key].flagForMake=0;
							keepGoing = false;
						}
						else{
							$scope.makeList[key].flagForMake=1;
						}
						}
					});
					
				});
				
				
				
				
				$scope.profileHeader.mainTitle=data.vehicleProfileData[0].mainTitle;
				$scope.profileHeader.textData=data.vehicleProfileData[0].subtitle;
				if(data.vehicleProfileData[0].socialFlag == 1){
					$scope.profileHeader.socialFlag=true;
				}
				if(data.vehicleProfileData[0].makeFlag == 1){
					$scope.profileHeader.makeFlag=true;
				}
				if(data.vehicleProfileData[0].financeFlag == 1){
					$scope.profileHeader.financeFlag=true;
				}
				$scope.thumbPat=data.vehicleProfileData[0].thumbPath;
				   if(data.vehicleProfileData[0].financeFlag == 1){
					   $scope.financeFlag=true;
				   }
				   else{
					   $scope.financeFlag=false;
				   }
				   
				   if(data.vehicleProfileData[0].socialFlag == 1){
					   $scope.socialFlag=true;
				   }
				   else{
					   $scope.socialFlag=false;
				   }
				   
				   if(data.vehicleProfileData[0].makeFlag == 1){
					   $scope.makeFlag=true;
				   }
				   else{
					   $scope.makeFlag=false;
				   }
				   
				if($scope.thumbPat != null){
					$scope.vehicleProfileList = data.vehicleProfileData;
				}
	
			});
		 }
		
		$scope.tempDate = new Date().getTime();
		apiserviceHomePage.getSliderAndFeaturedImages().then(function(data){
		 
				$scope.sliderList = data.sliderList;
				$scope.featuredList = data.featuredList;
				$scope.configList = data.configList;
				$scope.coverConfig = data.coverImg;
					angular.forEach(data.inventoryImg, function(value, key) {
						if(value.vType == $scope.vTypes){
							$scope.inventoryImg.push(value);
							$scope.siteInventory = value;
							if($scope.siteInventory.applyAll == "1"){
								$scope.siteInventory.applyAll = true;
							}else{
								$scope.siteInventory.applyAll = false;
							}
						}
						
					});
					console.log($scope.inventoryImg);
				
					$scope.header.mainTitle=data.siteAboutUs[0].headerTitle;
				$scope.header.textData=data.siteAboutUs[0].subtitle;
				$scope.thumbPath=data.siteAboutUs[0].thumbPath;
				if($scope.thumbPath != null){
				$scope.coverList = data.siteAboutUs;
				}
				$scope.configListForCvr=data.coverImageConf;
				console.log(data.coverImageConf[2].width);
				console.log(data.coverImageConf[2].height);
				$scope.configWidth=data.coverImageConf[2].width;
				$scope.configHeight=data.coverImageConf[2].height;
				$scope.header1.mainTitle=data.blogData[0].mainTitle;
				$scope.header1.textData=data.blogData[0].subtitle;
				$scope.thumbPath1=data.blogData[0].thumbPath;
				if($scope.thumbPath1 != null){
				$scope.blogList = data.blogData;
				}
				
				$scope.header2.mainTitle=data.contactData[0].mainTitle;
				$scope.header2.textData=data.contactData[0].subtitle;
				$scope.thumbPath2=data.contactData[0].thumbPath;
				if($scope.thumbPath2 != null){
				$scope.contactList = data.contactData;
				}
				
				$scope.header3.mainTitle=data.warData[0].mainTitle;
				$scope.header3.textData=data.warData[0].subtitle;
				$scope.thumbPath3=data.warData[0].thumbPath;
				if($scope.thumbPath3 != null){
				$scope.warrantyList = data.warData;
				}
				
	
					if(data.warData[0].hideMenu == "1"){
						$scope.header3.hideMenu = true;
					}else{
						$scope.header3.hideMenu = false;
					}
				//$scope.warrantyList = data.warData;
				//}
				
				
				$scope.header4.mainTitle=data.compareData[0].mainTitle;
				$scope.header4.textData=data.compareData[0].subtitle;
				$scope.thumbPath4=data.compareData[0].thumbPath;
				if($scope.thumbPath4 != null){
				$scope.compareList = data.compareData;
				}
				
				$scope.siteHeading = data.contentVM[0].heading;
				$scope.testiMonial = data.testiMonialVM[0];
				$scope.aboutUs = data.siteAboutUs[0];
				$scope.siteDescription.descHeading = data.contentVM[0].descHeading;
				$scope.siteDescription.description = data.contentVM[0].description;
			});
		 
		 
	}
	
	var myDropzone2;
	
	$scope.uploadSliderImages = function() {
		myDropzone2 = new Dropzone("#dropzoneFrm2",{
			   parallelUploads: 1,
			   acceptedFiles:"image/*",
			   addRemoveLinks:true,
			   autoProcessQueue:false,
			   maxFiles:1,
			   
			   accept: function(file, done) {
				   file.rejectDimensions = function() { done("Invalid dimension."); };
				   file.acceptDimensions = done;
				  
				  },
			   init:function () {
				   this.on("queuecomplete", function (file) {
				          
					   $scope.init();
				          $scope.$apply();
				      });
				   this.on("thumbnail", function(file) {
					      // Do the dimension checks you want to do
					      if (file.width < $scope.configList[0].width || file.height < $scope.configList[0].height) {
					    	  $('#sliderBtnMsg').click();
					    	  file.rejectDimensions()
					      }
					      else {
					        file.acceptDimensions();
					      }
					    });
				   this.on("complete", function() {
					   
					   this.removeAllFiles();
				   });
			   }
				  
		   });
	}
	   $scope.uploadFiles = function() {
		   if($scope.sliderList.length>=3) {
			   $('#btnMsg').click();
		   } else {
			   Dropzone.autoDiscover = false;
			   myDropzone2.processQueue();
		   }
		   
	   }
	   
	  
	   
	   
	   
	   var myDropzone5;
	   $scope.coverImageUpload = function() {
	   myDropzone5 = new Dropzone("#dropzoneFrm5",{
		   parallelUploads: 30,
		     headers: { "vinNum": 1 },
		     acceptedFiles:"image/*",
		     addRemoveLinks:true,
		     autoProcessQueue:false,
		     init:function () {
				   this.on("queuecomplete", function (file) {
				          
					   $scope.init();
				          $scope.$apply();
				      });
				   this.on("thumbnail", function(file) {
					      // Do the dimension checks you want to do
					   $scope.imgSmall = 0;
					      if (file.width < $scope.coverConfig[0].cropWidth || file.height < $scope.coverConfig[0].cropHeight) {
					    	  $scope.imgSmall = 1;
					    	  //file.rejectDimensions()
					      }
					      else {
					        file.acceptDimensions();
					      }
					    });
				   this.on("complete", function() {
					   
					   this.removeAllFiles();
				   });
			   }
		     });

	   
	   	   
	 }


	   var myDropzone6;
	   $scope.blogImageUpload = function() {
	   myDropzone6 = new Dropzone("#dropzoneFrm6",{
		   parallelUploads: 30,
		     headers: { "vinNum": 1 },
		     acceptedFiles:"image/*",
		     addRemoveLinks:true,
		     autoProcessQueue:false,
		     init:function () {
				   this.on("queuecomplete", function (file) {
				          
					   $scope.init();
				          $scope.$apply();
				      });
				   this.on("thumbnail", function(file) {
					      // Do the dimension checks you want to do
					   $scope.imgSmall = 0;
					      if (file.width < $scope.coverConfig[0].cropWidth || file.height < $scope.coverConfig[0].cropHeight) {
					    	  $scope.imgSmall = 1;
					    	  //file.rejectDimensions()
					      }
					      else {
					        file.acceptDimensions();
					      }
					    });
				   this.on("complete", function() {
					   
					   this.removeAllFiles();
				   });
			   }
		     });
	   	   
	 }
      
	   var myDropzone9;
	   $scope.compareImageUpload = function() {
	   myDropzone9 = new Dropzone("#dropzoneFrm9",{
		   parallelUploads: 30,
		     headers: { "vinNum": 1 },
		     acceptedFiles:"image/*",
		     addRemoveLinks:true,
		     autoProcessQueue:false,
		     init:function () {
				   this.on("queuecomplete", function (file) {
				          
					   $scope.init();
				          $scope.$apply();
				      });
				   this.on("thumbnail", function(file) {
					      // Do the dimension checks you want to do
					   $scope.imgSmall = 0;
					      if (file.width < $scope.coverConfig[0].cropWidth || file.height < $scope.coverConfig[0].cropHeight) {
					    	  $scope.imgSmall = 1;
					    	  //file.rejectDimensions()
					      }
					      else {
					        file.acceptDimensions();
					      }
					    });
				   this.on("complete", function() {
					   
					   this.removeAllFiles();
				   });
			   }
		     });
	   	   
	 }
        
	   
	   
	   
	   var myDropzone8;
	   $scope.warrantyImageUpload = function() {
	   myDropzone8 = new Dropzone("#dropzoneFrm8",{
		   parallelUploads: 30,
		    /* headers: { "vinNum": 1 },
		     acceptedFiles:"image/*",
		     addRemoveLinks:true,
		     autoProcessQueue:false,*/
		   parallelUploads: 1,
		   acceptedFiles:"image/*",
		   addRemoveLinks:true,
		   autoProcessQueue:false,
		   maxFiles:1,
		   
		     init:function () {
				   this.on("queuecomplete", function (file) {
				          
					   $scope.init();
				          $scope.$apply();
				      });
				   this.on("thumbnail", function(file) {
					      // Do the dimension checks you want to do
					   $scope.imgSmall = 0;
					      if (file.width < $scope.coverConfig[0].cropWidth || file.height < $scope.coverConfig[0].cropHeight) {
					    	  $scope.imgSmall = 1;
					    	  //file.rejectDimensions()
					      }
					      else {
					        file.acceptDimensions();
					      }
					    });
				   this.on("complete", function() {
					   
					   this.removeAllFiles();
				   });
			   }
		     });
	   	   
	 }
	   
	   
	   var myDropzone10;
	   $scope.vehicleImageUpload = function() {
	   myDropzone10 = new Dropzone("#dropzoneFrm10",{
		   parallelUploads: 30,
		     headers: { "vinNum": 1 },
		     acceptedFiles:"image/*",
		     addRemoveLinks:true,
		     autoProcessQueue:false,
		     init:function () {
				   this.on("queuecomplete", function (file) {
				          
					   $scope.init();
				          $scope.$apply();
				      });
				   this.on("thumbnail", function(file) {
					      // Do the dimension checks you want to do
					   $scope.imgSmall = 0;
					      if (file.width < $scope.coverConfig[0].cropWidth || file.height < $scope.coverConfig[0].cropHeight) {
					    	  $scope.imgSmall = 1;
					    	  //file.rejectDimensions()
					      }
					      else {
					        file.acceptDimensions();
					      }
					    });
				   this.on("complete", function() {
					   
					   this.removeAllFiles();
				   });
			   }
		     });
	   	   
	 }
	   
	   
	   
	   
	   

	   var myDropzone7;
	   $scope.contactImageUpload = function() {
	   myDropzone7 = new Dropzone("#dropzoneFrm7",{
		   parallelUploads: 30,
		     headers: { "vinNum": 1 },
		     acceptedFiles:"image/*",
		     addRemoveLinks:true,
		     autoProcessQueue:false,
		     init:function () {
				   this.on("queuecomplete", function (file) {
				          
					   $scope.init();
				          $scope.$apply();
				      });
				   this.on("thumbnail", function(file) {
					      // Do the dimension checks you want to do
					   $scope.imgSmall = 0;
					      if (file.width < $scope.coverConfig[0].cropWidth || file.height < $scope.coverConfig[0].cropHeight) {
					    	  $scope.imgSmall = 1;
					    	  //file.rejectDimensions()
					      }
					      else {
					        file.acceptDimensions();
					      }
					    });
				   this.on("complete", function() {
					   
					   this.removeAllFiles();
				   });
			   }
		     });
	   	   
	 }

 
	   
	   
	   
	   
	   
	   var myDropzone3;
	   $scope.uploadFeaturedImages = function() {
		   
		   myDropzone3 = new Dropzone("#dropzoneFrm3",{
			   parallelUploads: 1,
			   acceptedFiles:"image/*",
			   addRemoveLinks:true,
			   autoProcessQueue:false,
			   maxFiles:1,
			   accept: function(file, done) {
				   file.rejectDimensions = function() { done("Invalid dimension."); };
				   file.acceptDimensions = done;
				  
				  },
			   init:function () {
				   this.on("queuecomplete", function (file) {
				          
					   $scope.init();
				          $scope.$apply();
				      });
				   
				   this.on("thumbnail", function(file) {
					      // Do the dimension checks you want to do
					      if (file.width < $scope.configList[1].width || file.height < $scope.configList[1].height) {
					    	  $('#featuredBtnMsg').click();
					    	  file.rejectDimensions()
					      }
					      else {
					        file.acceptDimensions();
					      }
					    });
				   
				   this.on("complete", function() {
					   this.removeAllFiles();
				   });
			   }
		   });
	   }  
	   
	   $scope.coverImageFilesUpload = function() {
		   console.log(">>>>>"+$scope.coverList.length);
		  if($scope.coverList.length>=1) {
		   $('#btnFeaturedMsg').click();
		   } else {
			   console.log("inside upload");
			   Dropzone.autoDiscover = false;
			   myDropzone5.processQueue();
		   }
		   
	   }
	   
	   $scope.blogImageFilesUpload = function() {
		   console.log(">>>>>"+$scope.blogList.length);
		  if($scope.blogList.length>=1) {
		   $('#btnFeaturedMsg').click();
		   } else {
			   console.log("inside upload");
			   Dropzone.autoDiscover = false;
			   myDropzone6.processQueue();
		   }
		   
	   }
	   
	   $scope.compareImageFilesUpload = function() {
		   console.log(">>>>>"+$scope.compareList.length);
		  if($scope.compareList.length>=1) {
		   $('#btnFeaturedMsg').click();
		   } else {
			   console.log("inside upload");
			   Dropzone.autoDiscover = false;
			   myDropzone9.processQueue();
		   }
		   
	   }
	   
	   
	   $scope.warrantyFilesUpload = function() {
		  if($scope.warrantyList.length>=1) {
		   $('#btnFeaturedMsg').click();
		   } else {
			   console.log("inside upload");
			   Dropzone.autoDiscover = false;
			   myDropzone8.processQueue();
		   }
		   
	   }
	   
	   
	   $scope.vehicleFilesUpload = function() {
		   console.log(">>>>>"+$scope.vehicleProfileList.length);
		  if($scope.vehicleProfileList.length>=1) {
		   $('#btnFeaturedMsg').click();
		   } else {
			   console.log("inside upload");
			   Dropzone.autoDiscover = false;
			   myDropzone10.processQueue();
		   }
		   
	   }
	   
	   
	   
	   $scope.contactImageFilesUpload = function() {
		   console.log(">>>>>"+$scope.contactList.length);
		   if($scope.contactList.length>=1) {
		   $('#btnFeaturedMsg').click();
		   } else {
			   console.log("inside upload");
			   Dropzone.autoDiscover = false;
			   myDropzone7.processQueue();
		   }
		   
	   }
	   
	   
	   
	   
	   $scope.uploadFeaturedFiles = function() {
		   if($scope.featuredList.length>=3) {
			   $('#btnFeaturedMsg').click();
		   } else {
			   Dropzone.autoDiscover = false;
			   myDropzone3.processQueue();
		   }
		   
	   }
	   
	   
	   $scope.uploadInventoryCoverFiles = function() {
		   if($scope.inventoryImg.length>=1) {
			   $('#btnFeaturedMsg').click();
		   } else {
			   console.log("3333333333333333333333");
			   Dropzone.autoDiscover = false;
			   myDropzone4.processQueue();
		   }
	   }
	   
	   
	   $scope.deleteSliderImage = function(image) {
		   apiserviceHomePage.deleteSliderImage(image.id).then(function(data){
		   
				$scope.sliderList.splice($scope.sliderList.indexOf(image),1);
			});
	   }
	   
	   $scope.deleteFeaturedImage = function(image) {
		   apiserviceHomePage.deleteFeaturedImage(image.id).then(function(data){
		  
				$scope.featuredList.splice($scope.featuredList.indexOf(image),1);
			});
	   }
	   
	   $scope.deleteInventoryImage = function(image) {
		   console.log($scope.vTypes);
		   apiserviceHomePage.deleteInventoryImage(image.id).then(function(data){
		   
				$scope.inventoryImg.splice($scope.inventoryImg.indexOf(image),1);
			});
	   }
	   
	    $scope.deleteCvrImage = function(image) {
	    	apiserviceHomePage.deleteCvrImage(image.id).then(function(data){
		   
				$scope.coverList.splice($scope.coverList.indexOf(image),1);
			});
	   }

	    $scope.deleteBlogImage = function(image) {
	    	apiserviceHomePage.deleteBlogImage(image.id).then(function(data){
			   
					$scope.blogList.splice($scope.blogList.indexOf(image),1);
				});
		   }
	    
	    
	    $scope.deleteVehicleImage = function(image) {
	    	apiserviceHomePage.deleteVehicleImage(image.id, $scope.makeValue).then(function(data){
			   
					$scope.vehicleProfileList.splice($scope.vehicleProfileList.indexOf(image),1);
				});
		   }
	    
	    
	    $scope.deleteCompareImage = function(image) {
	    	apiserviceHomePage.deleteCompareImage(image.id).then(function(data){
			   
					$scope.compareList.splice($scope.compareList.indexOf(image),1);
				});
		   }
	    
	    $scope.deleteWarImage = function(image) {
	    	apiserviceHomePage.deleteWarImage(image.id).then(function(data){
			   
					$scope.warrantyList.splice($scope.warrantyList.indexOf(image),1);
				});
		   }
	    
	    
	    $scope.deleteContactImage = function(image) {
	    	apiserviceHomePage.deleteContactImage(image.id).then(function(data){
			   
					$scope.contactList.splice($scope.contactList.indexOf(image),1);
				});
		   }
	    
	   
	   $scope.showFullSliderImage = function(image) {
		   $scope.sliderImgId = image.id;
		   $scope.sliderImgName = image.imgName;
	   }
	   
	   $scope.showFullFeaturedImage = function(image) {
		   $scope.featuredImgId = image.id;
		   $scope.featuredImgName = image.imgName;
	   }
	   
	   $scope.editSliderImage = function(image) {
		   $location.path('/cropSliderImage/'+image.id);
	   }
	   
	   $scope.editFeaturedImage = function(image) {
		   $location.path('/cropFeaturedImage/'+image.id);
	   }

	   $scope.editInventoryImage = function(image) {
		   console.log($scope.vTypes);
		   $location.path('/cropInventoryImage/'+image.id+"/"+image.findById+"/"+image.vType);
	   }
	 
	   $scope.editCvrImage = function(image) {
		   console.log("imageimageimage");
		   console.log(image);
		   $location.path('/cropCoverImage/'+image.id+"/"+image.findById);
	   }
	   
	   $scope.editBlogImage = function(image) {
		   console.log("imageimageimage");
		   console.log(image);
		   $location.path('/cropBlogImage/'+image.id+"/"+image.findById);
	   }
	   
	   $scope.editCompareImage = function(image) {
		   console.log("imageimageimage");
		   console.log(image);
		   $location.path('/cropCompareImage/'+image.id+"/"+image.findById);
	   }
	   
	   $scope.editWarImage = function(image) {
		   console.log("imageimageimage");
		   console.log(image);
		   $location.path('/cropWarImage/'+image.id+"/"+image.findById);
	   }
	   
	   $scope.editVehicleImage = function(image) {
		   console.log("imageimageimage");
		   console.log(image);
		   $location.path('/editVehicleImage/'+image.id+"/"+image.findById+"/"+image.makeValue);
	   }
	   
	   
	   
	   $scope.editContactImage = function(image) {
		   console.log("imageimageimage");
		   console.log(image);
		   $location.path('/editContactImage/'+image.id+"/"+image.findById);
	   }
	   
	   $scope.saveSiteHeading = function(siteHeading) {
		   apiserviceHomePage.saveSiteHeading(siteHeading).then(function(data){
		   
			});
	   }
	   
	   $scope.saveSiteTestimonials = function(){
		   apiserviceHomePage.saveSitetestiMonial($scope.testiMonial).then(function(data){
			   console.log('success');
	   		});
		}
	   var aboutUsfile;
	   $scope.onAboutUsImgSelect = function ($files) {
			aboutUsfile = $files;
			
		}	
		
	   
	   $scope.saveAboutUsHeader = function(header) {
		   console.log(header);
		   apiserviceHomePage.saveSiteAboutUsHeader(header).then(function(data){
		   }); 
		}
	   
	   $scope.saveBlogHeader = function(header) {
		   console.log(header);
		   apiserviceHomePage.saveBlogHeader(header).then(function(data){
		   }); 
		}
	   
	   $scope.saveCompareHeader = function(header1) {
		   console.log(header1);
		   console.log(header1.applyAll);
		   if(header1.applyAll == undefined || header1.applyAll == true){
			   header1.headerFlag =1;
		   }
		   else{
			   header1.headerFlag =0;
		   }
		   apiserviceHomePage.saveCompareHeader(header1).then(function(data){
		   }); 
		}
	   
	   
	   $scope.saveMakeFlag = function() {
		   console.log($scope.makeFlag);
		   
		   if($scope.makeFlag == false){
			   $scope.makeFlag1 =1;
		   }
		   else{
			   $scope.makeFlag1=0;
		   }
		   
		   apiserviceHomePage.saveMakeFlag($scope.makeFlag1).then(function(data){
		   });
	   }
	   
	   $scope.saveSocialFlag = function() {
		   console.log($scope.socialFlag);
		   
		   if($scope.socialFlag ==false){
			   $scope.socialFlag1=1;
		   }
		   else{
			   $scope.socialFlag1=0;
		   }
		   apiserviceHomePage.saveSocialFlag($scope.socialFlag1).then(function(data){
		   });
	   }
	   
	   
	   $scope.saveFinanceFlag = function() {
		   console.log($scope.financeFlag);
		   
		   if($scope.financeFlag == false){
			   $scope.financeFlag1=1;
		   }
		   else{
			   $scope.financeFlag1=0;
		   }
		   
		   apiserviceHomePage.saveFinanceFlag($scope.financeFlag1).then(function(data){
		   });
	   }
	    
	   
	   
	   
	   
	   
	   
	   $scope.saveProfileHeader = function(ProfileHeader) {
		   console.log(ProfileHeader);
		   console.log($scope.financeFlag);
		   console.log($scope.socialFlag);
		   console.log($scope.makeFlag);
		   if($scope.makeFlag == false){
			   ProfileHeader.makeFlag =1;
		   }
		   else{
			   ProfileHeader.makeFlag=0;
		   }
		   if($scope.socialFlag == false){
			   ProfileHeader.socialFlag =1;
		   }
		   else{
			   ProfileHeader.socialFlag=0;
		   }
		   if($scope.financeFlag == false){
			   ProfileHeader.financeFlag =1;
		   }
		   else{
			   ProfileHeader.financeFlag=0;
		   }
		   
			   
				  
				   
				
	     ProfileHeader.makeValue=$scope.makeValue;
	     apiserviceHomePage.saveprofileHeader(ProfileHeader).then(function(data){
		   }); 
		}
	   
	   
	   $scope.uploadImageForMake = function(make) {
		   
		   $scope.makeValue=make;
		   console.log("LLLLLLLLL"+$scope.makeValue);
		   $location.path('/goToMakeImageUpload/'+$scope.makeValue);
		   
	   }
	   
	   
	   
	   $scope.saveHeader = function(header) {
		   console.log(header);
		   if(header.hideMenu == undefined){
			   header.hideMenu = false;
		   }
		   apiserviceHomePage.saveHeader(header).then(function(data){
		   }); 
		}

	   
	   $scope.saveContactHeader = function(header) {
		   console.log(header);
		   apiserviceHomePage.saveContactHeader(header).then(function(data){
		   }); 
		   
	   }
	   
	   
		
	$scope.saveSiteAboutUs = function() {
		
	
	
		if(angular.isUndefined(aboutUsfile)) {
			apiserviceHomePage.saveSiteAboutUs($scope.aboutUs).then(function(data){
			});
			
		} else {
			$scope.aboutUs.id = 0;
		
		   $upload.upload({
	            url : '/saveSiteAboutUs',
	            method: 'post',
	            file:aboutUsfile,
	            data:$scope.aboutUs
	        }).success(function(data, status, headers, config) {
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "AboutUs have been successfully saved",
				});
	            
	        });
		}
		
	}
		
	   
	   /*$scope.saveSiteAboutUs = function(){
		   $http.post('/saveSiteAboutUs',$scope.aboutUs)
	   		.success(function(data) {
	   			console.log('success');
				$.pnotify({
 				    title: "Success",
 				    type:'success',
 				    text: "AboutUs have been successfully saved",
 				});
	   		});
	   }*/
	   
	   $scope.saveSiteDescription = function() {
		   apiserviceHomePage.saveSiteDescription($scope.siteDescription).then(function(data){
		   
	   		});
	   }
	  
	   $scope.getLogoData = function() {
		   apiserviceHomePage.getLogoData().then(function(data){
		   
				$scope.logoName = data.logoName;
				$scope.feviconName = data.feviconName;
				$scope.tabText = data.tabText;
			});
	   }
	   
	   var logofile;
		$scope.onLogoFileSelect = function($files) {
			logofile = $files;
		}
	   
		 var feviconfile;
			$scope.onFeviconFileSelect = function($files) {
				feviconfile = $files;
			}
		
	   $scope.saveLogoImage = function() {
		   $upload.upload({
	            url : '/uploadLogoFile',
	            method: 'post',
	            file:logofile,
	        }).success(function(data, status, headers, config) {
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Logo image saved successfully",
				});
	            $scope.getLogoData();
	        });
	   }
	   
	   $scope.saveFeviconImage = function() {
		   $upload.upload({
	            url : '/uploadFeviconFile',
	            method: 'post',
	            file:feviconfile,
	        }).success(function(data, status, headers, config) {
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Fevicon image saved successfully",
				});
	            $scope.getLogoData();
	        });
	   }
	   $scope.tabText;
	   $scope.saveTabText = function(tabText) {
		   apiserviceHomePage.saveSiteTabText(tabText).then(function(data){
			   $scope.getLogoData();
			});
	   }
	   
	   $scope.saveInventory = function(type){
		   console.log(type);
		   console.log($scope.siteInventory);
		   if($scope.siteInventory.applyAll == undefined){
			   $scope.siteInventory.applyAll = false;
		   }
		   $scope.siteInventory.vType = type;
		   apiserviceHomePage.saveSiteInventory($scope.siteInventory).then(function(data){
		   });
		   
	   }
	   var myDropzone4;
	   $scope.uploadCoverImages = function() {
			myDropzone4 = new Dropzone("#dropzoneFrm4",{
				parallelUploads: 30,
				  /* headers: { "vinNum": 1 },
				   acceptedFiles:"image/*",
				   addRemoveLinks:true,
				   autoProcessQueue:false,*/
				   
				   parallelUploads: 1,
				   acceptedFiles:"image/*",
				   addRemoveLinks:true,
				   autoProcessQueue:false,
				   maxFiles:1,
				  
				   init:function () {
					   this.on("queuecomplete", function (file) {
					          
						   $scope.init();
					          $scope.$apply();
					      });
					   this.on("thumbnail", function(file) {
						      // Do the dimension checks you want to do
						   $scope.imgSmall = 0;
						      if (file.width < $scope.coverConfig[0].cropWidth || file.height < $scope.coverConfig[0].cropHeight) {
						    	  $scope.imgSmall = 1;
						    	  //file.rejectDimensions()
						      }
						      else {
						        file.acceptDimensions();
						      }
						    });
					   this.on("complete", function() {
						   
						   this.removeAllFiles();
					   });
				   }
			   });
					  
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
	   
	    $scope.goToInventoryCoverImg = function(type){
		   if(type == "Used"){
			   $location.path('/goToInventoryCoverImg/'+type);
		   }
			if(type == "New"){
				 $location.path('/goToInventoryCoverImgNew/'+type);
			}
			if(type == "comingSoon"){
				$location.path('/goToInventoryCoverImgComingSoon/'+type);  
			}
		   
	   }
	    
	    $scope.goToCompare = function(){
			   $location.path('/comparision');
		   }
	    
	   $scope.goToInventoryUsed = function(){
		   $location.path('/goToInventoryUsed/'+"Used");
	   }
	   $scope.goToInventorycomingSoon = function(){
		   $location.path('/goToInventoryComingsoon/'+"comingSoon");
	   }
	   
	   
}]);
