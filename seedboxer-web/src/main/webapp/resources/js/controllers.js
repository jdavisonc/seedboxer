'use strict';

 /* Controllers */

function StatusCtrl($scope, userStatusService, queueService) {

	$scope.current = {};
	
	function refreshStatus(){
	    userStatusService.getUserStatusData()
	    .then(function(data){
		$scope.current = data;
	    },
	    function(errorMessage){
		$scope.error=errorMessage;
	    });
	};

	refreshStatus();

	$scope.queue = {};
	
	function refreshQueue(){
	  queueService.getQueue()
	  .then(function(data){
	      $scope.queue = data;
	  },
	  function(errorMessage){
	      $scope.error = errorMessage;
	  })
	};
	
	refreshQueue();
	/*
	$scope.queue = [
		{ order: 1, title: 'Person.of.Interest.S02E21.720p.HDTV.X264-DIMENSION'},
		{ order: 2, title: 'Game.of.Thrones.S03E05.720p.HDTV.x264-IMMERSE'},
		{ order: 3, title: 'The.Big.Bang.Theory.S06E22.720p.HDTV.X264-DIMENSION'},
	];
	*/

}

function NavController($scope, $location){
   $scope.navClass = function (page) {
        var currentRoute = $location.path().substring(1) || 'home';
        return page === currentRoute ? 'active' : '';
    };   
}

function ProfileCtrl($scope, userConfigService, $dialog) {
   
   $scope.configs = [];
    var t = '<div class="modal-header">'+
          '<h1>This is the title</h1>'+
          '</div>'+
          '<div class="modal-body">'+
          '<p>Enter a value to pass to <code>close</code> as the result: <input ng-model="result" /></p>'+
          '</div>'+
          '<div class="modal-footer">'+
          '<button ng-click="close(result)" class="btn btn-primary" >Close</button>'+
          '</div>';
   function refreshConfig(){
       userConfigService.getConfigList()
       .then(function(data){
	   $scope.configs = data.configs;
       },
       function(errorMessage){
	   $scope.error = errorMessage;
       })
   };
   
   refreshConfig();
   
    $scope.opts = {
    backdrop: true,
    keyboard: true,
    backdropClick: true,
    templateUrl: '/ui/add-config-dialog.html',
    //template: t,
    controller: 'AddConfigDialogController'
  };

    

  $scope.openDialog = function(){
    var d = $dialog.dialog($scope.opts);
    d.open().then(function(result){
      if(result)
      {
        alert('dialog closed with result: ' + result);
      }
    });
  };

}

// the dialog is injected in the specified controller
function AddConfigDialogController($scope, dialog){
    $scope.close = function(result){
	dialog.close(result);
    };
}
function ContentCtrl($scope) {
	$scope.contents = [
	    {
		"name": "Go.On.S01E14.720p.HDTV.X264-DIMENSION",
		"order": 0,
		"queueId": null,
		"downloaded": false
	    },
	    {
		"name": "UK Top 40 Singles Chart Week 09 2013 OverDrive-RG",
		"order": 0,
		"queueId": null,
		"downloaded": false
	    },
	    {
		"name": "Prince_of_Persia_The_Forgotten_Sands_XBOX360-SPARE",
		"order": 0,
		"queueId": null,
		"downloaded": false
	    },
	    {
		"name": "Game.of.Thrones.S03E07.720p.HDTV.x264-EVOLVE",
		"order": 0,
		"queueId": null,
		"downloaded": false
	    }];
}

