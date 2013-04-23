<!DOCTYPE html> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html lang="en">

<jsp:include page="fragments/headTag.jsp"/>

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="container">
	<div class="row">
    	
      		
      		<div class="widget stacked">
					
				<div class="widget-header">
					<i class="shortcut-icon icon-signal"></i>
					<h3>Status</h3>
				</div> <!-- /widget-header -->
				
				<div class="widget-content">
					
					<div class="stats">
						
						<div class="stat">
							<span class="stat-value">12,386</span>									
							Site Visits
						</div> <!-- /stat -->
						
						<div class="stat">
							<span class="stat-value">9,249</span>									
							Unique Visits
						</div> <!-- /stat -->
						
						<div class="stat">
							<span class="stat-value">70%</span>									
							New Visits
						</div> <!-- /stat -->
						
					</div> <!-- /stats -->
					
					
					<div id="chart-stats" class="stats">
						
						<div class="stat stat-chart">							
														
						</div> <!-- /substat -->
						
						<div class="stat stat-time">									
							<span class="stat-value">00:28:13</span>
							Average Time on Site
						</div> <!-- /substat -->
						
					</div> <!-- /substats -->
					
				</div> <!-- /widget-content -->
					
			</div> <!-- /widget -->	
    	
    	
    	
	</div>
</div>

<jsp:include page="fragments/footer.jsp"/>
</body>

</html>
