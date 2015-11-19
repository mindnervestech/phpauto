angular.module('newApp')
.controller('VisitorsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	
	$scope.goToActions = function() {
		$location.path('/actionsAnalytics');
	}
	
	$scope.goToBasics = function() {
		$location.path('/basicsAnalytics');
	}
	
	$scope.goToSearches = function() {
		$location.path('/searchesAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
}]);


angular.module('newApp')
.controller('ActionsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	$scope.goToBasics = function() {
		$location.path('/basicsAnalytics');
	}
	
	$scope.goToSearches = function() {
		$location.path('/searchesAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
}]);

angular.module('newApp')
.controller('BasicsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	$scope.goToActions = function() {
		$location.path('/actionsAnalytics');
	}
	
	$scope.goToSearches = function() {
		$location.path('/searchesAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
}]);


angular.module('newApp')
.controller('SearchesCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	$scope.goToActions = function() {
		$location.path('/actionsAnalytics');
	}
	
	$scope.goToBasics = function() {
		$location.path('/basicsAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
}]);


angular.module('newApp')
.controller('RefferersCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	$scope.goToActions = function() {
		$location.path('/actionsAnalytics');
	}
	
	$scope.goToBasics = function() {
		$location.path('/basicsAnalytics');
	}
	
	$scope.goToSearches = function() {
		$location.path('/searchesAnalytics');
	}
	
	$scope.goToContent = function() {
		$location.path('/contentAnalytics');
	}
	
}]);


angular.module('newApp')
.controller('ContentCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {

	$scope.goToVisitors = function() {
		$location.path('/visitorsAnalytics');
	}
	
	$scope.goToActions = function() {
		$location.path('/actionsAnalytics');
	}
	
	$scope.goToBasics = function() {
		$location.path('/basicsAnalytics');
	}
	
	$scope.goToSearches = function() {
		$location.path('/searchesAnalytics');
	}
	
	$scope.goToRefferers = function() {
		$location.path('/refferersAnalytics');
	}
	
	
}]);