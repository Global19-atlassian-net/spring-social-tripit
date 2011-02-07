<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page session="false" %>
<html>
<head>
	<title>Spring Social Showcase: Sign In</title>

	<script type="text/javascript" src="<c:url value="/resources/jquery/1.4/jquery.js" />"></script>	
	
  <script type="text/javascript" src="http://platform.linkedin.com/in.js">
    api_key: 8U1nfplbpkkBTIKDcuonVY_gZ_9Pl0UOhbXr6qNIL1ByPz4ZietgM92sR-mMzpTM
    onLoad: onLinkedInLoad
    authorize: true
  </script>
    <script src="http://platform.twitter.com/anywhere.js?id=YR571S2JiVBOFyJS5MEg&v=1" type="text/javascript"></script>
</head>
<body>
<h1>Spring Social Showcase: Sign In</h1>

<form id="signin" action="<c:url value="/signin/authenticate" />" method="post">
	<div class="formInfo">
  		<c:if test="${signinError}">
  		<div class="error">
  			Your sign in information was incorrect.
  			Please try again or <a href="<c:url value="/signup" />">sign up</a>.
  		</div>
 	 	</c:if>
	</div>
	<fieldset>
		<label for="login">Username</label>
		<input id="login" name="j_username" type="text" size="25" <c:if test="${not empty signinErrorMessage}">value="${SPRING_SECURITY_LAST_USERNAME}"</c:if> />
		<label for="password">Password</label>
		<input id="password" name="j_password" type="password" size="25" />	
<button type="submit">Sign In</button>
	</fieldset>
	
	<p>Some test user/password pairs you may use are:</p>
	<ul>
		<li>habuma/habuma</li>
		<li>kdonald/kdonald</li>
		<li>rclarkson/rclarkson</li>
	</ul>
</form>


<!-- TWITTER @ANYWHERE SIGNIN -->
	<script type="text/javascript">
		twttr.anywhere.config({ callbackURL: "http://localhost:8080/spring-social-showcase/signin" });
		twttr.anywhere(function (T) {
			twttr.anywhere.signOut();
			T("#loginButton").connectButton({
				authComplete: function(user, bridgeCode) {
					// bridge code must be traded for access token
					$('#tw_signin #bridgeCode').val(bridgeCode);
					$('#tw_signin').submit();
				}
			});
		});
	</script>
	
	
	<form id="tw_signin" action="<c:url value="/signin/twitter"/>" method="POST">
		<div id="loginButton"></div>
		<input type="hidden" id="bridgeCode" name="bridgeCode" value="" />
	</form>
<!-- FACEBOOK SIGNIN -->
	<script src='http://connect.facebook.net/en_US/all.js'></script>
	<script>
		function signInWithFacebook() {
			// Good OAuth 2 access token. Ready for use with Graph API
			$('#fb_signin #accessToken').val(FB.getSession().access_token);
			$('#fb_signin').submit();
		}
	</script>	
	<script type='text/javascript'>	
		FB.init({appId: '0b754d95f9c9899b0d6c4454b6f2dde7', status: true, cookie: true, xfbml: true});
		FB.Event.subscribe('auth.sessionChange', function(response) { if (response.session) {} else {} });
	</script>

	
	<form id="fb_signin" action="<c:url value="/signin/facebook"/>" method="POST">
		<div id="fb-root"></div>
		<p><fb:login-button onlogin="signInWithFacebook();" v="2" length="long">Login with Facebook</fb:login-button></p>
		<input type="hidden" id="accessToken" name="accessToken" value="" />
	</form>
<%--
<hr/>
	<script>
		function onLinkedInLoad() {
		    IN.Event.on(IN, "auth", onLinkedInAuth);
		}
		
		function onLinkedInAuth() {
			// note that this token is temporary (~30 mins) and you'll get a new one each time.
			// need a way to trade it in on a long-lasting token
			$('#liToken').replaceWith("Access Token: " + IN.ENV.auth.oauth_token);
		}
	</script>
	<script type="in/login" ></script>
	<div id="liToken"></div>
 --%>	
</body>
</html>
