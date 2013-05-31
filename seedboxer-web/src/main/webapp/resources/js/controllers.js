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
		dialogClass : "add-config-dialog modal",
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
    } else if(dialogType == 'edit'){
		$scope.msg = 'Edit configuration';
		$scope.key = config.key;
		$scope.value = config.value;
    }

    $scope.closeOk = function(){
	    userConfigService.saveConfig($scope.key, $scope.value)
		    	.then(function(){
					alertService.showSuccess("Configuration saved successfully!");
					dialog.close('ok');
		    } ,function(errorMessage){
				$scope.error = errorMessage;
				alertService.showError("There was an when saving the configuration");
				dialog.close('error');
		    });
	    };
	    $scope.closeCancel = function(){
	    	dialog.close();
	    };
	    $scope.select2Options = {
		minimumResultsForSearch : -1,
		width : 'resolve'
    }
    
}



function DownloadsCtrl($scope, downloadsService, alertService) {

	$scope.putToDownload = function(item) {
		downloadsService.putToDownload(item.name)
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

function ContentsCtrl($scope, userContentService, userDataResource, alertService){
    
    $scope.opts = {
		backdrop: true,
		keyboard: true,
		backdropClick: true,
		templateUrl: '/ui/add-content-dialog.html',
		//dialogClass : "add-content-dialog",
		/*
		resolve: {
		    dialogType: function(){return angular.copy($scope.dialogType);},
		    config : function(){return angular.copy($scope.config);},
		    configTypes : function(){return angular.copy($scope.types);}
		},*/
		controller: 'AddContentDialogCtrl'
    };    
    
    $scope.refreshContents = function(){
	    userContentService.getContentList().
	    then(function(data){
	    	$scope.contents = data;
	    }),function(errorMsg){
	    	alertService.showError("There was an error trying to fetch configurations");
	    }
    };
    
    $scope.deleteContent = function(content){
	    userContentService.deleteContent(content).
	    then(function(data){
	    	alertService.showSuccess("Content deleted successfully!");
	    	$scope.refreshContents();
	    });
    };
    
    $scope.addNewContent = function(){
		var media = $scope.selectedContent;
		var mediaInfo = {
			"name" : media.title,
			"quality" : "HD"
		};
		if(media.url.indexOf("/movie/") != -1){
		    mediaInfo.type = "MOVIE";
		    mediaInfo.year = media.year;
		}
		if(media.url.indexOf("/show/") != -1) {
		    mediaInfo.type = "TV_SHOW";
		}
		$scope.$apply(function(){
			userContentService.saveContent(mediaInfo)
		    	.then($scope.refreshContents);
		});
    }
    
    $scope.addContent = function(){
		$(".select2-container").show();
		$("#select2").select2("open");
    };
    
    $scope.select2Options = {
	minimumInputLength: 5,
	width : 'resolve',
	placeholder: "Search for a movie",

	ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
	    url: "/webservices/search",
	    data: function (term) {
		return {
		    term: term.replace(/\s/g,"_"), // search term
		    apikey: userDataResource.getApiKey() // please do not use so this example keeps working
		};
	    },
	    results: function (data, page) { // parse the results into the format expected by Select2.
		// since we are using custom formatting functions we do not need to alter remote JSON data
		return {results: data.searchResults};
	    }
	},
	
	initSelection: function(element, callback) {
	  callback(element);
	},
	/*initSelection: function(element, callback) {
	    // the input tag has a value attribute preloaded that points to a preselected movie's id
	    // this function resolves that id attribute to an object that select2 can render
	    // using its formatResult renderer - that way the movie name is shown preselected
	    var id=$(element).val();
	    if (id!=="") {
		$.ajax("http://api.rottentomatoes.com/api/public/v1.0/movies/"+id+".json", {
		    data: {
			apikey: "ju6z9mjyajq2djue3gbvv26t"
		    },
		    dataType: "jsonp"
		}).done(function(data) {callback(data);});
	    }
	},*/
	formatResult: mediaFormatResult, // omitted for brevity, see the source of this page
	formatSelection: mediaFormatSelection,  // omitted for brevity, see the source of this page
	dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
	id : function(object){return object.title},
	escapeMarkup: function (m) {return m;} // we do not want to escape markup since we are displaying html in results
	}   
	
	$("#select2").select2($scope.select2Options);
	$(".select2-container").hide();
	$("#select2").on("change",$scope.addNewContent);
	$("#select2").on("close",function(){
	    $scope.selectedContent = $("#select2").select2("data");
	    $(".select2-container").hide();
	});
}




function mediaFormatResult(media) {
        var markup = "<table class='movie-result'><tr>";
        if (media.images.poster !== undefined) {
	    var minipic = media.images.poster;
	    minipic = minipic.replace(/(\/[\d]+)/,"$1-138");
            markup += "<td class='movie-image'><img src='" + minipic + "'/></td>";
        }
        markup += "<td class='movie-info'><div class='movie-title'>" + media.title + "</div>";
       if (media.overview !== undefined) {
            markup += "<div class='movie-synopsis'>" + media.overview + "</div>";
        }
        markup += "</td></tr></table>"
        return markup;
    }

    function mediaFormatSelection(movie) {
        return movie.title;
    }