var NANESPACE_QA_SESSOPM = {
	showUserID : function() {
		$.ajax({
			type : 'GET',
			url : 'rest/user',
			contentType  : 'application/json',
			dataType: 'json',
			success : function(data) {
				
				$("#span_userid").html(
						" " + data.first_name + " " + data.last_name);
				
				storeCookie('QA_SESSION_UI_COOKIE' , data);
			}, // success
			error: function(jqXHR, textStatus, errorThrown) {
				NANESPACE_QA_SESSOPM.redirectUser;
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
				NANESPACE_QA_SESSOPM.redirectUser;
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
				NANESPACE_QA_SESSOPM.redirectUser;
			}
		});
	}, // showSession:function
	createSession : function() {
		
		var sessionObject = new Object();;
		
		sessionObject.sessionTopic = "test topic";
		sessionObject.sessionDescription = "test description";
		sessionObject.sessionMaxQuestion = "5";
		sessionObject.sessionStatus = "OPEN";
		
		$.ajax({
			type : 'POST',
			url : 'rest/session',
			contentType  : 'application/json',
			dataType: 'json',
			data: JSON.stringify(sessionObject),
			success : function(data) {
				$("#testP").html(data.SessionId + " " + data.sessionTopic + " " + data.sessionDescription + " " + data.sessionMaxQuestion);
			}, // success
			error: function(jqXHR, textStatus, errorThrown) {
				NANESPACE_QA_SESSOPM.redirectUser;
			}
		});
	}, // showSession:function
	logotUser : function() {
		$.ajax({
			type : 'POST',
			url : 'rest/user/logout',
			contentType  : 'application/json',
			dataType: 'json',
			success : function(data) {
				NANESPACE_QA_SESSOPM.redirectUser;
			}, // success
			error: function(jqXHR, textStatus, errorThrown) {
				NANESPACE_QA_SESSOPM.redirectUser;
			}
		});
	}, // showUserID:function
	redirectUser : function()
	{
		window.location.replace(window.location.href);
	}
}

NANESPACE_QA_SESSOPM.showUserID();