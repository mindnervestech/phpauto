angular.module('newApp')
.controller('InventoryImageCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {


	
	console.log($routeParams.pId);
	
	$scope.coords = {};
	if(userRole == "Photographer"){
		$scope.imgId = "http://www.glider-autos.com:9889/getImageInv/"+$routeParams.id+"/full?d=" + Math.random();
		}
		else{
			$scope.imgId = "/getImageInv/"+$routeParams.id+"/full?d=" + Math.random();
		}
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		if(userRole == "Photographer"){
			 $http.get('/findLocation')
				.success(function(data) {
					console.log(data);
					$scope.userLocationId = data;
			$http.get('http://www.glider-autos.com:9889/getInventoryImageById/'+$routeParams.id)
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
			
			$http.get('/getInventoryImageById/'+$routeParams.id)
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
		 
		$scope.saveImage = function() {
			console.log($routeParams.id);
			$scope.coords.imageId = $routeParams.id;
			console.log($scope.coords);
			
	if(userRole == "Photographer"){
				
				$http.post('http://www.glider-autos.com:9889/editInventoryImage',$scope.coords)
				.success(function(data) {
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Saved successfully",
					});
					$location.path('/editInventory/'+$routeParams.id+'/'+true+"/"+$routeParams.productId);
				});
				
			}else{
				$http.post('/editInventoryImage',$scope.coords)
				.success(function(data) {
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Saved successfully",
					});
					$location.path('/editInventory/'+$routeParams.id+'/'+true+"/"+$routeParams.productId);
				});
			}
		}        
}]);

