'use strict';

 /* Controllers */

function StatusCtrl($scope, userStatusService, alertService) {

      
	$scope.play = function(){
		userStatusService.start()
		  .then(function(data){
			  alertService.showSuccess("The downloads are starting rigth now!");
		  },
		  function(errorMessage){
			  alertService.showError("There was an error when starting");
		  })
	};
      
	$scope.stop = function(){
		userStatusService.stop()
		  .then(function(data){
			  alertService.showSuccess("The downloads were stopped :(");
		  },
		  function(errorMessage){
			  alertService.showError("There was an error when stopping");
		  })
	};
}

function NavController($scope, $location){
   $scope.navClass = function (page) {
        var currentRoute = $location.path().substring(1) || 'home';
        return page === currentRoute ? 'active' : '';
    };   
}

function ProfileCtrl($scope, $dialog, alertService, userConfigService, userDataResource) {
   $scope.username = userDataResource.getUserName();
   $scope.deleteConfig = function(item){
   
	var title = '';
	var msg = 'Are you sure you want to delete this configuration?';
	var btns = [{result:'cancel', label: 'Cancel'}, {result:'ok', label: 'OK', cssClass: 'btn-primary'}];

	$dialog.messageBox(title, msg, btns)
	    .open()
	    .then(function(result){
	    if(result == 'ok'){
		userConfigService.deleteConfig(item.key)
		.then(function(){
		    alertService.showSuccess("Configuration deleted successfully");
		    $scope.refreshConfig();
		});

	    }
	});
    };

    $scope.opts = {
    backdrop: true,
    keyboard: true,
    backdropClick: true,
    templateUrl: '/ui/add-config-dialog.html',
    resolve: {
	dialogType: function(){return angular.copy($scope.dialogType);},
	config : function(){return angular.copy($scope.config);},
	configTypes : function(){return angular.copy($scope.types);}
   },
   controller: 'ConfigDialogCtrl'
  };

    

  $scope.refreshConfig = function(){
	userConfigService.getConfigList().
	then(function(data){
	    $scope.configs = data.configs;
	}),function(errorMsg){
	    alertService.showError("There was an error trying to fetch configurations");
	}
   }

   $scope.addNewConfig = function(){
	$scope.dialogType = 'add';
	var d = $dialog.dialog($scope.opts);
	d.open().then(function(result){
	if(result == 'ok')
	    $scope.refreshConfig();
	});
   };
   
   $scope.editConfig = function(conf){
	$scope.config = conf;
	$scope.dialogType = 'edit';
	var d = $dialog.dialog($scope.opts);
	d.open().then(function(result){
	//if(result == 'ok')
	    $scope.refreshConfig();
	})
   }
}
function ConfigDialogCtrl ($scope, dialog, userConfigService, alertService, dialogType, config, configTypes){
    $scope.configTypes = configTypes.configs;
    if(dialogType == 'add'){
	$scope.msg = 'Add a new configuration';
	$scope.key = null;
	$scope.value = null;
    }
    else if(dialogType == 'edit'){
	$scope.msg = 'Edit configuration';
	$scope.key = config.key;
	$scope.value = config.value;
    }

    $scope.closeOk = function(){
	userConfigService.saveConfig($scope.key, $scope.value)
	    .then(function(){
		alertService.showSuccess("Configuration saved successfully!");
		dialog.close('ok');
	    }
	    ,function(errorMessage){
		$scope.error = errorMessage;
		alertService.showError("There was an when saving the configuration");
		dialog.close('error');
	    });
    };
    $scope.closeCancel = function(){
	dialog.close();
    };
}

function ContentsCtrl($scope, contentsService, alertService) {
	
	$scope.contents = {};
	
	function refreshContents(){
		contentsService.getContents()
	  .then(function(data){
	      $scope.contents = data;
	  },
	  function(errorMessage){
		  alertService.showError("There was an when getting contents");
	  })
	};
	
	refreshContents();
	
	$scope.putToDownload = function(item) {
		contentsService.putToDownload(item.name)
		  .then(function(data){
			  alertService.showSuccess("The content was in the queue!");
		  },
		  function(errorMessage){
			  alertService.showError("There was an when sending contents to queue");
		  })
	};
}

function AlertCtrl($scope, alertService) {

    $scope.showAlert = false;
    $scope.alertService = alertService;
    $scope.alert = alertService.alert;

    $scope.$watch('alertService.counter',function(showAlert, oldVal, scope){
	if(scope.showAlert = scope.alertService.getAlert() != null){
		scope.alert = scope.alertService.getAlert();
	}
    },true);

    $scope.fadeIn = function(){
    	return $scope.showAlert ? 'in' : '';
    }

    $scope.closeAlert = function() {
	$scope.showAlert = false;
    };

}


function HeaderCtrl($scope, userDataResource) {
    
    /*
    We implemented the userData call to the server to be done 
    before anything else. We achieve this result by calling the server
    and after receiving the response, only then we define the routes and 
    reload them.
    This works nice for all the views, but the header is not part of any view
    so it gets rendered before the result is back from the server and the route
    reloading doesn't affect it.
    That's why we have to use a watch here.
    */
    $scope.$watch(function(){
	return userDataResource.getUserName()
    },
    function(newVal, oldVal, scope){
	scope.username = newVal;
    })

}