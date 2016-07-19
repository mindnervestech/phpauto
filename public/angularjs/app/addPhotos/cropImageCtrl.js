
	
angular.module('newApp')
.controller('ImageCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {

	$scope.coords = {};
	if(userRole == "Photographer"){
	$scope.imgId = "http://www.glider-autos.com/getImage/"+$routeParams.id+"/full?d=" + Math.random();
	}
	else{
		$scope.imgId = "/getImage/"+$routeParams.id+"/full?d=" + Math.random();
	}
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		if(userRole == "Photographer"){
			 $http.get('/findLocation')
				.success(function(data) {
					console.log(data);
					$scope.userLocationId = data;
			$http.get('http://www.glider-autos.com/getImageById/'+$routeParams.id+"/"+$scope.userLocationId)
			.success(function(data) {
				imageW = data.col;
				imageH = data.row;
				
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				$scope.image = data;
				$('#target').css({
					width: Math.round(727) + 'px',
					height: Math.round(727*(imageH/imageW)) + 'px'
				});
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: data.width/data.height
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
				});
		}else{
			$http.get('/findLocation')
			.success(function(data) {
				console.log(data);
				$scope.userLocationId = data;
			
			$http.get('/getImageById/'+$routeParams.id+"/"+$scope.userLocationId)
			.success(function(data) {
				imageW = data.col;
				imageH = data.row;
				
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				$scope.image = data;
				$('#target').css({
					width: Math.round(727) + 'px',
					height: Math.round(727*(imageH/imageW)) + 'px'
				});
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: data.width/data.height
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
			});
		}
		
		 
		 
	}
		 function showCoords(c)
		    {
			    var rx = 200 / c.w;
				var ry = 200*(imageH/imageW) / c.h;
				
				$('#preview-container').css({
					width: Math.round(200) + 'px',
					height: Math.round(200*(imageH/imageW)) + 'px'
				});
				
				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
		    };
		 
		$scope.saveImage = function() {
			$scope.coords.imageId = $routeParams.id;
			if(userRole == "Photographer"){
				
				$http.post('http://www.glider-autos.com/editImage',$scope.coords)
				.success(function(data) {
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Saved successfully",
					});
					$location.path('/editVehicle/'+$routeParams.vid+'/'+true+"/"+$routeParams.vin);
				});
				
			}else{
				$http.post('/editImage',$scope.coords)
				.success(function(data) {
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Saved successfully",
					});
					$location.path('/editVehicle/'+$routeParams.vid+'/'+true+"/"+$routeParams.vin);
				});
			}
			
		}    
}]);	




angular.module('newApp')
.controller('SliderCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {

	$scope.coords = {};
	$scope.imgId = "/getSliderImage/"+$routeParams.id+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		 $http.get('/getSliderImageDataById/'+$routeParams.id)
			.success(function(data) {
				imageW = data.width;
				imageH = data.height;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				$('#target').css({
					height: Math.round((727)/(data.col/data.row)) + 'px'
				});
				
				$scope.image = data;
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: data.width/data.height
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
		 
	}
		 function showCoords(c)
		    {
			    var rx = 200 / c.w;
				var ry = 200*(imageH/imageW) / c.h;
				
				$('#preview-container').css({
					width: Math.round(200) + 'px',
					height: Math.round(200*(imageH/imageW)) + 'px'
				});
				
				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
				 
		    };
		 
		   
		    
		$scope.saveImage = function(image) {
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editSliderImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "All changed has been saved",
				});
				$location.path('/homePage');
				$scope.$apply();
			});
		}    
		 
		
}]);	


angular.module('newApp')
.controller('CoverCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	$scope.coords = {};
	$scope.imgId = "/aboutUsCoverImageById/"+$routeParams.findById+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		
		
		 $http.get('/getCoverDataById/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				imageW = data.col;
				imageH = data.row;
			//	$scope.thumbPath=data.thumbPath;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				 $('#target').css({
					 width: Math.round(727) + 'px',
					 height: Math.round(727*(imageH/imageW)) + 'px'
					 });
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: data.width/data.height
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
		 
	}
		 function showCoords(c)
		    {
			 
			    console.log("??????????");
			    console.log(c);
			 	var rx = 200 / c.w;
				var ry = 200*(imageH/imageW) / c.h;
				
				$('#preview-container').css({
					width: Math.round(200) + 'px',
					height: Math.round(200*(imageH/imageW)) + 'px'
				});
				
				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
				 
		    };
		    
		$scope.saveImage = function(image) {
			console.log(image);
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editCovrImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Croped Image has been saved",
				});
				
				$location.path('/siteAboutUs');
				//$scope.$apply();
			});
		}    
		 
		
}]);	





angular.module('newApp')
.controller('VehicleCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	$scope.coords = {};
	$scope.imgId = "/vehicleProfileImageByIdForCrop/"+$routeParams.id+"/"+$routeParams.makeValue+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.minImgheight;
	$scope.minImgwidth;
	$scope.init = function() {
		$scope.makeValue=$routeParams.makeValue;	
		
		 $http.get('/getVehicleProfileDataById/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				imageW = data.col;
				imageH = data.row;
				$scope.minImgheight = data.height;
				$scope.minImgwidth = data.width;
			//	$scope.thumbPath=data.thumbPath;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				 $('#target').css({
					 width: Math.round(727) + 'px',
					 height: Math.round(727*(imageH/imageW)) + 'px'
					 });
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: 4/1
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
		 
	}
		 function showCoords(c)
		    {
			 
			    console.log("??????????");
			    console.log(c);
			    if(c.x < 0 || c.y < 0){
			    	$scope.showErrorMsg = 1;
			    }
			 	var rx = 200 / c.w;
				var ry = 200*(imageH/imageW) / c.h;
				
				$('#preview-container').css({
					width: Math.round(200) + 'px',
					height: Math.round(200*(imageH/imageW)) + 'px'
				});
				
				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
				 
		    };
		    
		$scope.saveImage = function(image) {
			console.log(image);
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editVehicleProfileImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Croped Image has been saved",
				});
				console.log("/vehicleProfile/"+$scope.makeValue);
				//$location.path('/vehicleProfile/'+$scope.makeValue);
				//$scope.$apply();
			});
		}    
		 
		
}]);	






angular.module('newApp')
.controller('WarrantyCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	$scope.coords = {};
	console.log("jjjjhhh");
	console.log($routeParams.findById);
	$scope.imgId = "/warrantyImageById/"+$routeParams.findById+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.minImgheight;
	$scope.minImgwidth;
	$scope.init = function() {
		
		
		 $http.get('/getWarDataById/'+$routeParams.id)
			.success(function(data) {
				
				console.log(data);
				imageW = data.col;
				imageH = data.row;
				$scope.minImgheight = data.height;
				$scope.minImgwidth = data.width;
			//	$scope.thumbPath=data.thumbPath;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				 $('#target').css({
					 width: Math.round(727) + 'px',
					 height: Math.round(727*(imageH/imageW)) + 'px'
					 });
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: 4/1
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
		 
	}
		 function showCoords(c)
		    {
			 
			    console.log(c);
			    if(c.x < 0 || c.y < 0){
			    	$scope.showErrorMsg = 1;
			    }
			    
			 	var rx = 200 / c.w;
				var ry = 200*(imageH/imageW) / c.h;
				
				$('#preview-container').css({
					width: Math.round(200) + 'px',
					height: Math.round(200*(imageH/imageW)) + 'px'
				});
				
				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
				 
		    };
		    
		$scope.saveImage = function(image) {
			console.log(image);
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editWarImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Croped Image has been saved",
				});
				
				$location.path('/warranty');
				//$scope.$apply();
			});
		}    
		 
		
}]);	






angular.module('newApp')
.controller('BlogCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	$scope.coords = {};
	$scope.imgId = "/blogImageById/"+$routeParams.findById+"/full?d="+ Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.minImgheight;
	$scope.minImgwidth;
	$scope.showErrorMsg = 0;
	$scope.init = function() {
		
		
		 $http.get('/getBlogDataById/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				imageW = data.col;
				imageH = data.row;
				$scope.minImgheight = data.height;
				$scope.minImgwidth = data.width;
			//	$scope.thumbPath=data.thumbPath;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				 $('#target').css({
					 width: Math.round(727) + 'px',
					 height: Math.round(727*(imageH/imageW)) + 'px'
					 });
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: 4/1
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
		 
	}
		 function showCoords(c)
		    {
			 
			    console.log("??????????");
			    console.log(c);
			    if(c.x < 0 || c.y < 0){
			    	$scope.showErrorMsg = 1;
			    }
			 	var rx = 200 / c.w;
				var ry = 200*(imageH/imageW) / c.h;
				
				$('#preview-container').css({
					width: Math.round(200) + 'px',
					height: Math.round(200*(imageH/imageW)) + 'px'
				});
				
				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
				 
		    };
		    
		$scope.saveImage = function(image) {
			console.log(image);
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editBlogImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Croped Image has been saved",
				});
				
				$location.path('/blog');
				//$scope.$apply();
			});
		}    
		 
		
}]);	


angular.module('newApp')
.controller('CompareCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	$scope.coords = {};
	$scope.imgId = "/compareImageById/"+$routeParams.findById+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		
		
		 $http.get('/getCompareDataById/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				imageW = data.col;
				imageH = data.row;
			//	$scope.thumbPath=data.thumbPath;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				 $('#target').css({
					 width: Math.round(727) + 'px',
					 height: Math.round(727*(imageH/imageW)) + 'px'
					 });
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: data.width/data.height
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
		 
	}
		 function showCoords(c)
		    {
			 
			    console.log("??????????");
			    console.log(c);
			 	var rx = 200 / c.w;
				var ry = 200*(imageH/imageW) / c.h;
				
				$('#preview-container').css({
					width: Math.round(200) + 'px',
					height: Math.round(200*(imageH/imageW)) + 'px'
				});
				
				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
				 
		    };
		    
		$scope.saveImage = function(image) {
			console.log(image);
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editCompareImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Croped Image has been saved",
				});
				
				$location.path('/comparision');
				//$scope.$apply();
			});
		}    
		 
		
}]);	








angular.module('newApp')
.controller('ContactCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	$scope.coords = {};
	$scope.imgId = "/contactImageById/"+$routeParams.findById+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.minImgheight;
	$scope.minImgwidth;
	$scope.init = function() {
		
		
		 $http.get('/getContactDataById/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				imageW = data.col;
				imageH = data.row;
				$scope.minImgheight = data.height;
				$scope.minImgwidth = data.width;
			//	$scope.thumbPath=data.thumbPath;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				 $('#target').css({
					 width: Math.round(727) + 'px',
					 height: Math.round(727*(imageH/imageW)) + 'px'
					 });
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: 4/1
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
		 
	}
		 function showCoords(c)
		    {
			 
			    console.log("??????????");
			    console.log(c);
			    if(c.x < 0 || c.y < 0){
			    	$scope.showErrorMsg = 1;
			    }
			 	var rx = 200 / c.w;
				var ry = 200*(imageH/imageW) / c.h;
				
				$('#preview-container').css({
					width: Math.round(200) + 'px',
					height: Math.round(200*(imageH/imageW)) + 'px'
				});
				
				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
				 
		    };
		    
		$scope.saveImage = function(image) {
			console.log(image);
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editContactImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Croped Image has been saved",
				});
				
				$location.path('/contactUs');
				//$scope.$apply();
			});
		}    
		 
		
}]);	





angular.module('newApp')
.controller('FeaturedCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {

	$scope.coords = {};
	$scope.imgId = "/getFeaturedImage/"+$routeParams.id+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		 $http.get('/getFeaturedImageDataById/'+$routeParams.id)
			.success(function(data) {
				imageW = data.width;
				imageH = data.height;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				$('#target').css({
					height: Math.round((727)/(data.col/data.row)) + 'px'
				});
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: data.width/data.height
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
		 
	}
		 function showCoords(c)
		    {
			 	var rx = 200 / c.w;
				var ry = 200*(imageH/imageW) / c.h;
				
				$('#preview-container').css({
					width: Math.round(200) + 'px',
					height: Math.round(200*(imageH/imageW)) + 'px'
				});
				
				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
				 
		    };
		 
		$scope.saveImage = function(image) {
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editFeaturedImage',$scope.coords)
			.success(function(data) {
				$location.path('/homePage');
				$scope.$apply();
			});
		}    
		 
		
}]);	

angular.module('newApp')
.controller('InventoryCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {

	$scope.coords = {};
	$scope.imgId = "/getInventoryImage/"+$routeParams.findById+"/"+$routeParams.vType+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.minImgheight;
	$scope.minImgwidth;
	$scope.init = function() {
		 $http.get('/getInventoryImageDataById/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				imageW = data.col;
				imageH = data.row;
				
				$scope.minImgheight = data.height;
				$scope.minImgwidth = data.width;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				$('#target').css({
					width: Math.round(727) + 'px',
					height: Math.round(727*(imageH/imageW)) + 'px'
				});
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect: [ 0, 0, data.width, data.height],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: 4/1
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
		 
	}
		 function showCoords(c)
		    {
			 if(c.x < 0 || c.y < 0){
			    	$scope.showErrorMsg = 1;
			    }
			 	var rx = 200 / c.w;
				var ry = 200*(imageH/imageW) / c.h;
				
				$('#preview-container').css({
					width: Math.round(200) + 'px',
					height: Math.round(200*(imageH/imageW)) + 'px'
				});
				
				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
				 
		    };
		 
		$scope.saveImage = function() {
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			//$scope.coords.imgName = image.imgName;
			//$scope.coords.description = image.description;
			//$scope.coords.link = image.link;
			
			$http.post('/editInventoryImage',$scope.coords)
			.success(function(data) {
				$location.path('/goToInventoryNew/New');
				$scope.$apply();
			});
		}    
		 
		
}]);

