<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div class="navbar navbar-inverse navbar-fixed-top" ng-controller="HeaderCtrl">
	
	<div class="navbar-inner">
		
		<div class="container">
			
			<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
				<i class="icon-cog"></i>
			</a>
			
			<a class="brand" href="./index.html">
				<img src="/img/favicon.png" width="25" heigth="25" /> SeedBoxer
			</a>		
			
			<div class="nav-collapse collapse">
				<ul class="nav pull-right">
			
					<li class="dropdown">
						
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">
							<i class="icon-user icon-white"></i> 
							{{username}}
							<b class="caret"></b>
						</a>
						
						<ul class="dropdown-menu">
							<li><a href="#my-profile">My Profile</a></li>
							<li><a href="#server-settings">Server Settings</a></li>
							<li class="divider"></li>
							<li><a href="j_spring_security_logout">Logout</a></li>
						</ul>
						
					</li>
				</ul>
				
			</div><!--/.nav-collapse -->	
	
		</div> <!-- /container -->
		
	</div> <!-- /navbar-inner -->
	
	<div class="container" style="margin-top:-10px">
	    <div ng-controller="AlertCtrl" class="fade" ng-class="fadeIn()">
			<alert type="alert.type" close="closeAlert($index)">{{alert.msg}}</alert>
		</div>
	</div>
	
</div>