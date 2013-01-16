<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="span2">
	<div class="well sidebar-nav">
		<ul class="nav nav-list">
			<li class="nav-header">Get recommended Shreds</li>

				
			<div class="dropdown">
				<a class="dropdown-toggle" data-toggle="dropdown" href="#">With tags you like</a>
				<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
				<li>
					<form action="<c:url value='/shred/recommendations/byTags'/>" method="GET"class="form-search">
					
					<input type="text" name = "tagList" placeholder="Tag1, Tag2, Tag3.." class="input-medium search-query"/> <input type="submit" class="btn btn-info btn-mini" />			
					</form>
					</li>
				</ul>
			</div>			
			
			<li><a href="<c:url value='/shred/recommendations?action=highestRating'/>">With highest rating</a></li>
			<li><a
				href="<c:url value='/shred/recommendations?action=fanShreds&shredderId='/>${shredder.id}">Made
					by your fans</a></li>
			<li><a
				href="<c:url value='/shred/recommendations?action=shredsFromShreddersYouMightKnow&shredderId='/>${shredder.id}">From
					shredders you might know</a></li>
			<li><a href="<c:url value='/shred/recommendations?action=all'/>">All shreds</a></li>
			<li class="nav-header">Ongoing battles</li>
			<c:forEach var="battle" items="${battles}">
				<li><a href="<c:url value='/battle/'/>${battle.id}">${battle.battler.shredder.username}
						vs ${battle.battlee.shredder.username}</a></li>
			</c:forEach>

			<li class="nav-header">Fanees</li>
			<c:forEach var="fan" items="${fans}">
				<li><a href="<c:url value='/shredder/'/>${fan.id}">${fan.username}</a></li>
			</c:forEach>

			<li class="nav-header">The tagpool</li>
			<c:forEach var="tag" items="${tags}">
				<li><a href="<c:url value='/shred/recommendations/byTags'/>?tagList=${tag.label}">${tag.label}</a></li>
			</c:forEach>
		</ul>
	</div>
	<!--/.well -->
</div>