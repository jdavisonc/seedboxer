<!DOCTYPE html> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html ng-app="seedboxerui">

    <head>
	
	<spring:url value="/webjars/bootstrap/2.3.1/css/bootstrap.min.css" var="bootstrapCss"/>
	<link href="${bootstrapCss}" rel="stylesheet"/>
	<style type="text/css">
      /* Override some defaults */
      html, body {
        background-color: #eee;
      }
      body {
        padding-top: 40px; 
      }
      .container {
        width: 300px;
      }

      /* The white background content wrapper */
      .container > .content {
        background-color: #fff;
        padding: 20px;
        margin: 0 -20px; 
        -webkit-border-radius: 10px 10px 10px 10px;
           -moz-border-radius: 10px 10px 10px 10px;
                border-radius: 10px 10px 10px 10px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.15);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.15);
                box-shadow: 0 1px 2px rgba(0,0,0,.15);
      }

	  .login-form {
		margin-left: 65px;
	  }
	
	  legend {
		margin-right: -50px;
		font-weight: bold;
	  	color: #404040;
	  }

    </style>
    
    
    </head>

<body>
<div class="container">
    <div class="content">
      <div class="row">
        <div class="login-form">
          <h2>Login</h2>
	  <span style="color:red"><%= request.getAttribute("errorMessage") %></span>
	  <spring:url value="j_spring_security_check" var="springSecurityCheck"/>
          <form method="post" action="${springSecurityCheck}">
            <fieldset>
              <div class="clearfix">
                <input type="text" placeholder="Username" name="j_username">
              </div>
              <div class="clearfix">
                <input type="password" placeholder="Password" name="j_password">
              </div>
              <button class="btn primary" type="submit">Sign in</button>
            </fieldset>
          </form>
        </div>
      </div>
    </div>
  </div> <!-- /container -->

</body>

</html>