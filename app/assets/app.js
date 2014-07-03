var myApp = angular.module('myApp', []);

myApp.factory('Tuples', function() {
    
    //var colorsIrow = ['green', 'grey', 'dodgerblue', 'yellow', 'pink'];
    var colorsIIrow = ['teal', 'olive', 'wheat', 'salmon', 'dark'];

    var Tuples = {colorsIrow: ['green', 'grey', 'dodgerblue', 'yellow', 'pink'], 
                  colorsIIrow: ['teal', 'olive', 'wheat', 'salmon', 'dark']
                };



    return Tuples;
})



function AvengersCtrl($scope, Tuples) {
    $scope.colorsRow = Tuples.colorsIrow;
    $scope.colorsSrow = Tuples.colorsIIrow;
  
    $scope.select = function(id) {
      console.log("click " + id.target.attributes.data.value);
      
    }
}