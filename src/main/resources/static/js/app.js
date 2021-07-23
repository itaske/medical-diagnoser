
var app = angular.module('medicsystem', ['ngRoute', 'ngResource']);

app.config(function ($routeProvider){
    $routeProvider
    .when('/rate/:parameters', {
           templateUrl: '/template/rate.html',
           controller: 'rateController'
    })
    .when('/list-all', {
    templateUrl:'/template/list-all.html',
    controller:'diagnosisController'
    })
    .otherwise({
    redirectTo: '/home',
    controller: 'homeController',
    templateUrl: '/template/home.html'});
});


//Home Controller; Display Symptoms select and diagnose
app.controller('homeController', function ($scope, $http, $location, $route) {
            $scope.symptoms = [];
	    $scope.selectedSymptoms = [];
	      $http({
                 method: 'GET',
                 cache: true,
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
	    };

	    $scope.diagnoseSymptoms = function(){
	    console.log("Diagnosing.....")
	    $scope.selectedSymptoms.forEach(s=>{delete s['$$hashKey']})

	      var parameters = JSON.stringify({gender:$scope.gender, yearOfBirth:$scope.yearOfBirth, symptoms:$scope.selectedSymptoms});
	      console.log(parameters);
            $location.path("/rate/"+parameters);
            $route.reload();
	    }

	    });


// Rate Diagnosis Controller

	    app.controller('rateController', function($scope, $http, $location, $routeParams, $route) {

          $scope.parameters = $routeParams.parameters;

          console.log($scope.parameters);
            $http({
                           method: 'POST',
                           url: 'http://localhost:8080/medic/diagnosis',
                           data: $scope.parameters
                           }).then(function(response){
                           $scope.listOfDiagnosis = response.data;
                           $scope.selection = [];
                           $scope.listOfDiagnosis.forEach(l=>{
                           $scope.selection.push(l);
                           })
                           }, function(errResponse) {
                              $scope.errorMessage = "Error while Getting Diagnosis"
                              + errResponse.data.
                              errorMessage;
                              });



          // Toggle selection for a given fruit by name
          $scope.toggleSelection = function toggleSelection(diagnosis) {
            var idx = $scope.selection.indexOf(diagnosis);

            // Is currently selected
            if (idx > -1) {
              $scope.selection.splice(idx, 1);
            }

            // Is newly selected
            else {
              $scope.selection.push(diagnosis);
            }
          };

          $scope.submit = function(){
            console.log($scope.selection);
             $http({
                                       method: 'POST',
                                       url: 'http://localhost:8080/medic/validate-diagnosis',
                                       data: $scope.selection
                                       }).then(function(response){
                                       $scope.listOfDiagnosis = response.data;
                                         $location.path("/list-all");
                                                   $route.reload();
                                       }, function(errResponse) {
                                          $scope.errorMessage = "Error while Getting Diagnosis"
                                          + errResponse.data.
                                          errorMessage;
                                          });


          }

        });

        app.controller('diagnosisController', function ($scope, $http, $location, $route) {
          $http({
                           method: 'GET',
                           url: 'http://localhost:8080/medic/diagnosis'
                           }).then(function(response){
                           $scope.listOfDiagnosis = response.data;
                           }, function(errResponse) {
                              $scope.errorMessage = "Error while Getting Symptoms"
                              + errResponse.data.
                              errorMessage;
                              });

                              $scope.search = function(){
                              console.log("Entered");
                              console.log($scope.searchTerm);
                                        $scope.listOfDiagnosis = $scope.listOfDiagnosis.filter((val) => val.name.toLowerCase().includes($scope.searchTerm));
                                      }
        }
        );

