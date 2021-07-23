
var app = angular.module('medicsystem', ['ngRoute', 'ngResource']);

app.config(function ($routeProvider){
    $routeProvider
    .when('/list-all', {
           templateUrl: '/template/list-all.html',
           controller: 'listController'
    }).otherwise({
    redirectTo: '/home',
    controller: 'homeController',
    templateUrl: '/template/home.html'});
});

app.controller('homeController', function ($scope, $http) {
            $scope.symptoms = [];
	    $scope.selectedSymptoms = [];
	      $http({
                 method: 'GET',
                 url: 'http://localhost:8080/medic/symptoms'
                 }).then(function(response){
                 $scope.symptoms = response.data;
                 }, function(errResponse) {
                    $scope.errorMessage = "Error while Getting Symptoms"
                    + errResponse.data.
                    errorMessage;
                    });

        $scope.currentSymptom = {};
	    $scope.addSymptom = function(symptoms){
	        symptoms.forEach(s=>$scope.selectedSymptoms.push(s));
	        $scope.symptoms = $scope.symptoms.filter(e=> {
	        return !symptoms.includes(e);
	        });

	    }

	    $scope.removeSymptom = function(symptoms){
	         symptoms.forEach(s=>$scope.symptoms.push(s));
	         $scope.selectedSymptoms = $scope.selectedSymptoms.filter(s=>{
	            return !symptoms.includes(s);
	         })
	    }

	    $scope.display = false;
	    $scope.display_value = "none";
	    $scope.displaySymptoms = function(){
	        $scope.display = !$scope.display;
	      if ($scope.display){
	         $scope.display_value = "block";
	      }else{
	      $scope.display_value = "none";
	      }
	    }


	    });

