var NANESPACE_QA_SESSION = {
	showUserID : function() {
		$.ajax({
			type : 'GET',
			url : 'rest/user',
			contentType  : 'application/json',
			dataType: 'json',
			success : function(data) {
				
				$("#span_user_name").html(
						" " + data.first_name + " " + data.last_name);
				//NANESPACE_QA_SESSION.storeCookie('QA_SESSION_UI_COOKIE' , data);
				
				$.ajax({
					type : 'GET',
					url : 'rest/attendee/' + data.uid,
					contentType  : 'application/json',
					dataType: 'json',
					success : function(data) {

						$("#tbodySession").empty();
						$.each(data, function(i, attendee) {
							$("#tbodySession").append("<tr id = \"tbtrSession_" + attendee.session.sessionId +"\"><td>" + attendee.session.sessionId.substring(0,10) + "</td><td>"+attendee.session.sessionTopic.substring(0,30)+"</td><td>" + attendee.session.sessionStatus + "</td><td>" + attendee.sessionRole + "</td><td>" + attendee.session.updateDate + "</td><td><a href=\"#\" onClick=\"NANESPACE_QA_SESSION.openSession('" + attendee.session.sessionId + "')\"><span class=\"glyphicon glyphicon-th-list\"></span></a>  <a href=\"#\" onClick=\"NANESPACE_QA_SESSION.deleteSession('" + attendee.session.sessionId+ "')\"><span class=\"glyphicon glyphicon-trash\"></span></a></td></tr>");
							});
					}, // success
					error: function(jqXHR, textStatus, errorThrown) {
						NANESPACE_QA_SESSION.redirectUser;
					}
				});
			}, // success
			error: function(jqXHR, textStatus, errorThrown) {
				NANESPACE_QA_SESSION.redirectUser;
			}
		});
	}, // showUserID:function
	storeCookie:function(cookieName, object)
	{
		var cookie = [name, '=', JSON.stringfy(object), '; domain=.', window.location.host.toString, '; path=/controller;'].join('');
		document.cookie = cookie;
	},
	readCookie: function(cookieName)
	{
		var object = document.cookie.match(new RegExp(cookieName + '=([^;]+)'));
		object && (object = JSON.parse(object[1]));
		return object;
	},
	deleteCookie: function(cookieName)
	{
		document.cookie = [cookieName, '=; expires=Thu, 01-Jan-1970 00:00:01 GMT; domain=.', window.location.host.toString, '; path=/controller;'].join('');
	},
	showSessionList : function() {
		$.ajax({
			type : 'GET',
			url : 'rest/attendee/' + readCookie('QA_SESSION_UI_COOKIE').email,
			contentType  : 'application/json',
			dataType: 'json',
			success : function(data) {
				
			}, // success
			error: function(jqXHR, textStatus, errorThrown) {
				NANESPACE_QA_SESSION.redirectUser;
			}
		});
	}, // showSessionList:function
	showSession : function() {
		$.ajax({
			type : 'GET',
			url : 'rest/user',
			contentType  : 'application/json',
			dataType: 'json',
			success : function(data) {
				$("#span_userid").html(
						" " + data.first_name + " " + data.last_name);
			}, // success
			error: function(jqXHR, textStatus, errorThrown) {
				NANESPACE_QA_SESSION.redirectUser;
			}
		});
	}, // showSession:function
	createSession : function() {
		
		var sessionObject = new Object();;
		
		sessionObject.sessionTopic = $("#sessionTopic").val();
		sessionObject.sessionDescription = $("#sessionDescription").val();
		sessionObject.sessionMaxQuestion = $("#sessionMaxQuestion").val();
		sessionObject.sessionStatus = $( "#sessionStatus option:selected" ).text();
		
		$.ajax({
			type : 'POST',
			url : 'rest/session',
			contentType  : 'application/json',
			dataType: 'json',
			data: JSON.stringify(sessionObject),
			success : function(data) {
				$("#tbodySession").append("<tr id = \"tbtrSession_" + data.sessionId +"\"><td>" + data.sessionId.substring(0,10) + "</td><td>" + data.sessionTopic.substring(0,30) + "</td><td>" + data.sessionStatus + "</td><td>HOST</td><td>" + data.updateDate + "</td><td><a href=\"#\" onClick=\"NANESPACE_QA_SESSION.openSession('" + data.sessionId+ "')\"><span class=\"glyphicon glyphicon-th-list\"></span></a> <a href=\"#\" onClick=\"NANESPACE_QA_SESSION.deleteSession('" + data.sessionId+ "')\"><span class=\"glyphicon glyphicon-trash\"></span></a></td></tr>");
				$('#myModal').modal('hide');
			}, // success
			error: function(jqXHR, textStatus, errorThrown) {'rest/attendee/' + $("#span_userid").html()
				NANESPACE_QA_SESSION.redirectUser;
			}
		});
	}, // showSession:function
	deleteSession : function(sessionId) {
		
		$.ajax({
			type : 'DELETE',
			url : 'rest/session/' + sessionId,
			contentType  : 'application/json',
			dataType: 'json',
			success : function(data) {
				$("#tbtrSession_" + sessionId).remove();
			}, // success
			error: function(jqXHR, textStatus, errorThrown) {
				NANESPACE_QA_SESSION.redirectUser;
			}
		});
	}, // deleteSession : function()
	openSession : function(sessionId) {

	}, // editSession : function()
	displayAllSession : function(sessionId) {
	}, // displayAllSession
	logotUser : function() {
		$.ajax({
			type : 'POST',
			url : 'rest/user/logout',
			contentType  : 'application/json',
			dataType: 'json',
			success : function(data) {
				NANESPACE_QA_SESSION.redirectUser;
			}, // success
			error: function(jqXHR, textStatus, errorThrown) {
				NANESPACE_QA_SESSION.redirectUser;
			}
		});
	}, // showUserID:function
	redirectUser : function()
	{
		window.location.replace(window.location.href);
	}
}

NANESPACE_QA_SESSION.showUserID();
NANESPACE_QA_SESSION.displayAllSession();
