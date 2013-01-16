<%@page import="com.mikey.shredhub.api.domain.newsitem.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javascript">

function dopopover(id, description, content ) {
	$('#shred-'+id).popover(data={
		trigger:'hover',
		placement: 'left',
		content: content,
		title: description,
		animation:true
	});
}

function createShredContent(tags, rating) {
	tags = tags.replace(/,\s*$/, "");
	return "<p>Tags: " + tags + "</p>" + "<p> Rating: " + rating + "</p>";
}

function createBattleContent(round, status, shredder, shreddee) {
	var str = "<p>"+shredder + " vs. " + shreddee + "</p>";
	str += "<p>Round " + round + "</p>";
	str += "<p>Status: " + status +"</p>";
	return str;	
}
function createShredderContent(adress, description, guitars) {
	guitars = guitars.replace(/,\s*$/, "");
	var str = "<p>Lives in: " + adress +"</p>";
	str += "<p>"+description +"</p>";
	str += "<p>Shreds on: "+guitars+"</p>";
	return str;	
}
</script>

<div class="span3">
	<div class="well sidebar-nav">
		<ul class="nav nav-list">
			<li class="nav-header">Shred news</li>
			
			<c:forEach var="newsitem" items ="${news}" varStatus="status">
			<%  
			ShredNewsItem item = ((ShredNewsItem) pageContext.getAttribute("newsitem"));
			if (item instanceof NewShredFromFanee ){		 
			%>
			
			<!-- Create the list of tags for the shred -->
			<c:set var="tagsList" value=""></c:set>
			<c:forEach var="tag" items="${newsitem.shred.tags}">
				<c:set var="tagsList" value="${tag.label}, ${tagsList}" /> 
			</c:forEach>		
						
			<li>
			<a rel="popover" 
				id="shred-${status.count}" 
				onmouseover="dopopover(${status.count},
				'${newsitem.shred.description}', 
				createShredContent('${tagsList}', ${newsitem.shred.rating.rating}));"
				href="<c:url value='/shredder/'/>${newsitem.shred.owner.id}">
				${newsitem.shred.owner.username} just added a new shred at ${newsitem.timeCreated}</a>
			</li>
	
			<%
			} else if (item instanceof BattleShredNewsItem ) {
			%>
			<li>
			<a
			id="shred-${status.count}" 
			onmouseover="dopopover(${status.count},
			'Battle','${newsitem.battleShred.owner.username} shredded in round ${newsitem.battleShred.round}');"
			href="<c:url value='/battle/'/>${newsitem.battleId}">${newsitem.battleShred.owner.username}
			 shredded in a battle at ${newsitem.timeCreated}</a></li>
			
			<%
			} else if (item instanceof NewBattleCreatedNewsItem ) {
			%>
			<li>
			<a
			id="shred-${status.count}"
			onmouseover="dopopover(${status.count},
				'Battle', 
				createBattleContent(${newsitem.battle.round},'${newsitem.battle.status}', '${newsitem.battle.battler.shredder.username}', '${newsitem.battle.battlee.shredder.username}'));" 
			 href="<c:url value='/battle/'/>${newsitem.battle.id}">${newsitem.battle.battler.shredder.username}
			 just entered a Battle against ${newsitem.battle.battlee.shredder.username} at ${newsitem.timeCreated}</a></li>
			
			<%
			} else if (item instanceof NewPotentialFaneeNewsItem ) {
			%>
			<!-- Create the list of guitars. Don't bother doing equiptment, it's such a hazzle! -->
			<c:set var="guitars" value=""></c:set>
			<c:forEach var="g" items="${newsitem.shredder.guitars}">
				<c:set var="guitars" value="${g}, ${guitars}" /> 
			</c:forEach>
			<li> 
			<a
			id="shred-${status.count}"
			onmouseover="dopopover(${status.count},
				'${newsitem.shredder.username}', 
				createShredderContent('${newsitem.shredder.country}','${newsitem.shredder.description}','${guitars}' ));"		
			 href="<c:url value='/shredder/'/>${newsitem.shredder.id}">${newsitem.shredder.username} just joined ShredHub.
			You are shred-alike. Check out his shreds.</a></li>
			<% } %>
			</c:forEach>
		</ul>
	</div>
	<!--/.well -->
</div>