'use strict';

 /* Controllers */
var refreshTime = 10000;

function StatusCtrl($scope, $route, $timeout, userStatusService, downloadsService, alertService) {
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
	
	$scope.refresh = function() {
	    userStatusService.getUserStatusData().then(function(data){$scope.current = data});
	    downloadsService.getQueue().then(function(data) {$scope.queue = data});
	}
	
	$scope.onTimeout = function(){
		$scope.refresh();
		if ($route.current.templateUrl == '/ui/home.html') {
			refreshTimer = $timeout($scope.onTimeout, refreshTime);
		} else {
			$timeout.cancel(refreshTimer);
		}
	}
    var refreshTimer = $timeout($scope.onTimeout, refreshTime);
    
    $scope.deleteFromQueue = function(download) {
		downloadsService.deleteFromQueue(download.queueId)
		  .then(function(data){
			  alertService.showSuccess("The download was deleted!");
			  $scope.refresh();
		  },
		  function(errorMessage){
			  alertService.showError("There was an error when deleting download");
		  })
    }
    
}

function NavController($scope, $location){
   $scope.navClass = function (page) {
        var currentRoute = $location.path().substring(1) || 'home';
        return page === currentRoute ? 'active' : '';
    };   
}

function ProfileCtrl($scope, $dialog, alertService, userConfigService, userDataResource) {
    $scope.username = userDataResource.getUserName();

    $scope.validateRequired = function(v){
        if(!v) return "Required Field";
    }
    $scope.qualities = [
        {text : "Standard", id : "STANDARD"},
        {text : "HD", id : "HD"},
        {text : "Full HD", id : "FULLHD"}
    ]

    

    $scope.configsMap = {};
    for(var i=0;i<$scope.configs.length;i++){
        $scope.configsMap[$scope.configs[i].key] = $scope.configs[i].value;
    }
   
    $scope.profileParams = 
    {
    
        "Account" : [
            {id : "password", value : "", title : "Password", type : "password"},
        ],
        "Home Server" : [
            {id : "TransferUrl", value : "", title : "Url*", type : "url", required : true},
            {id : "TransferUsername", value : "", title : "Username*", type : "text", required : true},
            {id : "TransferPassword", value : "", title : "Password*", type : "password", required : true},
            {id : "TransferRemoteDir", value : "", title : "Remote Dir*", type : "text", required : true}
        ],
        "Post Actions" : [
            {id : "SshUrl", value : "", title : "Ssh Url", type : "url"},
            {id : "SshUsername", value : "", title : "Ssh Username", type : "text"},
            {id : "SshPassword", value : "", title : "Ssh Password", type : "password"},
            {id : "SshCmd", value : "", title : "Ssh Command", type : "text"}
        ],
        "Third Party" : [
            {id : "ThirdParty", value : "", title : "Third Party Service", type : "select2", sources : [
                {text : "IMDB", id : "imdb"}, 
                {text : "Trakt", id:  "trakt"} 
            ]},
            {id : "ImdbList", value : "", title : "Imdb List Id", type : "text"},
            {id : "ImdbAuthor", value : "", title : "Imdb Author Id", type : "text"},
            {id : "ImdbContentQuality", value : "", title : "Imdb Quality", type : "select2", sources : $scope.qualities},

            {id : "TraktUsername", value : "", title : "Trakt Username", type : "text"},
            {id : "TraktPassword", value : "", title : "Trakt Password", type : "password"},
            {id : "TraktAuthKey", value : "", title : "Trakt Auth Key", type : "text"},
            {id : "TraktContentQuality", value : "", title : "Trakt Quality", type : "select2", sources : $scope.qualities}

        ]
        
    };
     
    $scope.closeHandler = function(option){
        if(option.id == "password"){
            userConfigService.savePassword(option.value).then(function(data){
                if(data.status == "SUCCESS")
                    alertService.showSuccess("Password saved successfully");
                else
                    alertService.showError("Error saving Password");
            })
        }
        else{
            userConfigService.saveConfig(option.id, option.value).then(function(data){
                if(data.status == "SUCCESS")
                    alertService.showSuccess(option.title + " saved successfully");
                else
                    alertService.showError("Error saving " + option.title);
            })
        }
        $scope.$apply();
    }
    $scope.isVisible = function(option){
        if(option.id.indexOf("Imdb")== -1 && option.id.indexOf("Trakt")== -1)
            return true;
        var thirdPartyService = $scope.searchParam("ThirdParty").value;
        if(thirdPartyService  == "") 
            return false;
        else
        return option.id.toLowerCase().indexOf(thirdPartyService) != -1;
    }

    for(var config in $scope.configsMap){
    angular.forEach($scope.profileParams,function(value, key){
        angular.forEach($scope.profileParams[key],function(value,key2){
            var param = $scope.profileParams[key][key2].id;
            if(param == config){
                $scope.profileParams[key][key2].value = $scope.configsMap[config];
            }
        });
    });

    }
    
    $scope.searchParam = function(id){
        var returnVal;
        angular.forEach($scope.profileParams,function(value, key){
            angular.forEach($scope.profileParams[key],function(value,key2){
                if($scope.profileParams[key][key2].id == id)
                    returnVal =  $scope.profileParams[key][key2];
            });
        });
        return returnVal;
    };
   
   
    
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
	$scope.admin = userDataResource.isAdmin();
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
    
    
function ServerSettingsCtrl($scope,$dialog, adminRssService, alertService){
    
   $scope.deleteRssFeed = function(feed){
   
	var title = '';
	var msg = 'Are you sure you want to delete this feed?';
	var btns = [{result:'cancel', label: 'Cancel', cssClass : 'btn-danger'}, {result:'ok', label: 'OK', cssClass: 'btn-primary'}];

	$dialog.messageBox(title, msg, btns)
	    .open()
	    .then(function(result){
	    if(result == 'ok'){
			adminRssService.deleteRssFeed(feed)
			.then(function(){
			    alertService.showSuccess("Rss deleted successfully");
			    $scope.refreshRssFeeds();
			});

	    }
	});
    };

    $scope.opts = {
		backdrop: true,
		keyboard: true,
		backdropClick: true,
		templateUrl: '/ui/rss-dialog.html',
		dialogClass : "rss-dialog modal",
		resolve: {
		    dialogType: function(){return angular.copy($scope.dialogType);},
		    rssFeed : function(){return angular.copy($scope.rssFeed);}
		},
		controller: 'RssDialogCtrl'
    };

  

	$scope.refreshRssFeeds = function(){
		adminRssService.getRssList().
		then(function(data){
		    $scope.rssFeeds = data.rssFeeds;
		}),function(errorMsg){
		    alertService.showError("There was an error trying to fetch feeds");
		}
   }

   $scope.addNewRssFeed = function(){
	$scope.dialogType = 'add';
	var d = $dialog.dialog($scope.opts);
	d.open().then(function(result){
	if(result == 'ok')
	    $scope.refreshRssFeeds();
	});
	
   };
   
   $scope.editRssFeed = function(feed){
	$scope.rssFeed = feed;
	$scope.dialogType = 'edit';
	var d = $dialog.dialog($scope.opts);
	d.open().then(function(result){
	//if(result == 'ok')
	    $scope.refreshRssFeeds();
	})
   }
}


function RssDialogCtrl ($scope, dialog, adminRssService, alertService, dialogType, rssFeed){
    
    if(dialogType == 'add'){
		$scope.msg = 'Add a new rss feed';
		$scope.rssFeed = {"name" : "", "url" : ""};
    } else if(dialogType == 'edit'){
		$scope.msg = 'Edit rss feed';
		$scope.rssFeed = rssFeed;
    }

    $scope.closeOk = function(){
	    adminRssService.saveRssFeed({"name" : $scope.rssFeed.name, 
	    "id" : $scope.rssFeed.id,
	    "url" :$scope.rssFeed.url})
		.then(function(){
				alertService.showSuccess("Rss Feed saved successfully!");
				dialog.close('ok');
	    } ,function(errorMessage){
			$scope.error = errorMessage;
			alertService.showError("There was an when saving the Rss Feed");
			dialog.close('error');
	    });
    };
    $scope.closeCancel = function(){
	dialog.close();
    };
    
    $scope.checkUrl= function(){
	$scope.urlOk = checkURL($scope.rssFeed.url);
	return  $scope.urlOk ? 'success' : 'error';
    };
    
    $scope.checkName = function(){
	$scope.nameOk = $scope.rssFeed.name != "";
	return $scope.nameOk ? 'success' : 'error';
    }
    
    $scope.validate = function(){
	return $scope.urlOk && $scope.nameOk;
    }
}
   
   
function checkURL(value) {
  return /^(ftp|https?):\/\/+(www\.)?([a-z0-9\-\.]{3,}\.[a-z]{3}|localhost)\/.*/.test(value);

}


function UsersCtrl($scope, adminUsersService, alertService, $dialog){
 $scope.deleteUser = function(user){
   
	var title = '';
	var msg = 'Are you sure you want to delete this user?';
	var btns = [{result:'cancel', label: 'Cancel', cssClass : 'btn-danger'}, {result:'ok', label: 'OK', cssClass: 'btn-primary'}];

	$dialog.messageBox(title, msg, btns)
	    .open()
	    .then(function(result){
	    if(result == 'ok'){
			adminUsersService.deleteUser(user)
			.then(function(){
			    alertService.showSuccess("User deleted successfully");
			    $scope.refreshUsers();
			});

	    }
	});
    };

    $scope.opts = {
		backdrop: true,
		keyboard: true,
		backdropClick: true,
		templateUrl: '/ui/user-dialog.html',
		dialogClass : "users-dialog modal",
		resolve: {
		    dialogType: function(){return angular.copy($scope.dialogType);},
		    user : function(){return angular.copy($scope.user);}
		},
		controller: 'UserDialogCtrl'
    };

  

	$scope.refreshUsers = function(){
		adminUsersService.getUsersList().
		then(function(data){
		    $scope.users = data.users;
		}),function(errorMsg){
		    alertService.showError("There was an error trying to fetch feeds");
		}
   }

   $scope.addNewUser = function(){
	$scope.dialogType = 'add';
	var d = $dialog.dialog($scope.opts);
	d.open().then(function(result){
	if(result == 'ok')
	    $scope.refreshUsers();
	});
	
   };
   
   $scope.editUser = function(user){
	$scope.user = user;
	$scope.dialogType = 'edit';
	var d = $dialog.dialog($scope.opts);
	d.open().then(function(result){
	//if(result == 'ok')
	    $scope.refreshUsers();
	})
   }   
}


function UserDialogCtrl ($scope, dialog, adminUsersService, alertService, dialogType, user){
    
    if(dialogType == 'add'){
		$scope.msg = 'Add a new user';
		$scope.user = {"username" : "", "password" : "", "admin" : false};
    } else if(dialogType == 'edit'){
		$scope.msg = 'Edit user';
		$scope.user= user;
    }

    $scope.closeOk = function(){
	    adminUsersService.saveUser({"username" : $scope.user.username, 
	    "id" : $scope.user.id,
	    "password" :$scope.user.password,
	    "admin" :$scope.user.admin})
		.then(function(data){
				if(data.status == "SUCCESS")
				    alertService.showSuccess("User saved successfully!");
				else
				    alertService.showError(data.message);
				dialog.close('ok');
	    } ,function(errorMessage){
			$scope.error = errorMessage;
			alertService.showError("There was an when saving the User");
			dialog.close('error');
	    });
    };
    $scope.closeCancel = function(){
	dialog.close();
    };
    
    $scope.checkPassword= function(){
	$scope.passwordOk = $scope.user.password != "";
	return  $scope.passwordOk ? 'success' : 'error';
    };
    
    $scope.checkName = function(){
	$scope.nameOk = $scope.user.username != "";
	return $scope.nameOk ? 'success' : 'error';
    }
    
    $scope.validate = function(){
	return $scope.passwordOk && $scope.nameOk;
    }
}

