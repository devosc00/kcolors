var myApp = angular.module('myApp', []);

myApp.factory('Tuples', function() {
    
    //var colorsIrow = ['green', 'grey', 'dodgerblue', 'yellow', 'pink'];
    var colorsIIrow = ['teal', 'olive', 'wheat', 'salmon', 'dark'];

    var Tuples = {colorsIrow: ['green', 'grey', 'dodgerblue', 'yellow', 'pink'], 
                  colorsIIrow: ['teal', 'olive', 'wheat', 'salmon', 'dark']
                };
    return Tuples;
})

myApp.factory('socketService', function() {
    var service = {};

    service.connect = function() {
        if(service.ws) { return; }

        var ws = new WebSocket("ws://localhost:9000/indexWS");

        ws.onopen = function() {
            service.callback("Succeeded to open a connection");
        };

        ws.onmessage = function(message) {
            service.callback(message.data);
        };

        service.ws = ws;
    }

    service.send = function(message) {
        service.ws.send(message);
    }

    service.subscribe = function(callback) {
        service.callback = callback;
    }

    return service;
});


function AvengersCtrl($scope, Tuples, socketService) {
    $scope.colorsRow = Tuples.colorsIrow;
    $scope.colorsSrow = Tuples.colorsIIrow;
    var data;

    $scope.select = function(id) {
         data = id.target.attributes.data.value;
        socketService.connect();

      console.log("click " + data);

    }

    $scope.messages = [];

    socketService.subscribe(function(data) {
        $scope.messages.push(data);
        $scope.$apply();
    });

    $scope.connect = function() {
        socketService.connect();
    }

    $scope.send = function(data) {
        socketService.send (JSON.stringify(data));
        $scope.text = "some text from send";
    }

}

