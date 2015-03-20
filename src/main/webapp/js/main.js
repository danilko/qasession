var NANESPACE_QA_SESSION = {
	showSessionList : function() {

		$("#divSessionDetail").hide();
		$("#divSessionList").hide();
		$
				.ajax({
					type : 'GET',
					url : 'rest/user',
					contentType : 'application/json',
					dataType : 'json',
					success : function(data) {
						$
								.ajax({
									type : 'GET',
									url : 'rest/attendee/' + data.uid,
									contentType : 'application/json',
									dataType : 'json',
									success : function(data) {
										$("#tbodySession").empty();
										$
												.each(
														data,
														function(i, attendee) {

															$("#tbodySession")
																	.append(
																			"<tr id = \"tbtrSession_"
																					+ attendee.session.sessionId
																					+ "\"><td>"
																					+ attendee.session.sessionId
																							.substring(
																									0,
																									10)
																					+ "</td><td>"
																					+ attendee.session.sessionTopic
																							.substring(
																									0,
																									30)
																					+ "</td><td>"
																					+ attendee.session.sessionStatus
																					+ "</td><td>"
																					+ attendee.sessionRole
																					+ "</td><td>"
																					+ attendee.session.updateDate
																					+ "</td><td><a href=\"#\" onClick=\"NANESPACE_QA_SESSION.showSessionDetail('"
																					+ attendee.session.sessionId
																					+ "')\"><span class=\"glyphicon glyphicon-th-list\"></span></a>  <a href=\"#\" onClick=\"NANESPACE_QA_SESSION.deleteSessionConfirm('"
																					+ attendee.session.sessionId
																					+ "')\"><span class=\"glyphicon glyphicon-trash\"></span></a></td></tr>");
														});
										$("#divSessionList").show();

									}, // success
									error : function(jqXHR, textStatus,
											errorThrown) {
										NANESPACE_QA_SESSION.redirectUser;
									}
								});
					}, // success
					error : function(jqXHR, textStatus, errorThrown) {
						NANESPACE_QA_SESSION.redirectUser;
					}
				});
	},
	showSessionDetail : function(sessionId) {

		$("#divSessionDetail").hide();
		$("#divSessionList").hide();

		$
				.ajax({
					type : 'GET',
					url : 'rest/session/' + sessionId,
					contentType : 'application/json',
					dataType : 'json',
					success : function(data) {
						$("#existSessionSessionTopic").val(data.sessionTopic);
						$("#existSessionSessionDescription").val(
								data.sessionDescription);
						$("#existSessionSessionMaxQuestion").val(
								data.sessionMaxQuestion);
						$("#existSessionSessionStatus option:selected").text(
								data.sessionStatus);

						$("#buttonExistSessionSaveExistSession").attr(
								"onClick",
								"NANESPACE_QA_SESSION.saveExistSession('"
										+ data.sessionId + "');")

						$("#buttonSessionCreateQuestion").attr(
								"onClick",
								"NANESPACE_QA_SESSION.createQuestion('"
										+ data.sessionId + "');")
						$("#sessionQuestionContent").val("");				
										
						if (data.questions.length > 0) {
							$("#divSessionDetailQuestions").empty();
							$
									.each(
											data.questions,
											function(i, question) {
												$("#divSessionDetailQuestions")
														.append(
																"<div id=\"divQuestion_"
																		+ question.questionId
																		+ "\"><span class=\"label label-primary\">Question</span> <span class=\"label label-info\">" + question.questionStatus + "</span>"
																		//+ "<span class=\"label label-info\">Created By " + question.createdBy.userTranslate.firstName + " " + question.createdBy.userTranslate.lastName + "</span>"
																		+ "<p>"
																		+ question.questionContent
																		+ "</p><p class=\"text-right\"><a href=\"#\" onClick=\"NANESPACE_QA_SESSION.editQuestion('"
																		+ question.session.sessionId
																		+ "', '"
																		+ question.questionId
																		+ "');\"><span class=\"glyphicon glyphicon-th-list\"> <a href=\"#\" onClick=\"NANESPACE_QA_SESSION.deleteQuestion('"
																		+ question.session.sessionId
																		+ "', '"
																		+ question.questionId
																		+ "');\"><span class=\"glyphicon glyphicon-trash\"></span></a></p></div>");
												if (question.answer != null) {
													$(
															"#divSessionDetailQuestions")
															.append(
																	"<blockquote><span class=\"label label-success\">Answer</span> <span class=\"label label-info\">Created By " + question.answer.createdBy.userTranslate.firstName + " " + question.answer.createdBy.userTranslate.lastName + "</span><p> "
																			+ question.answer.answerContent
																			+ " </p></blockquote>");
												} // if
											});
						} // if
						else {
							$("#divSessionDetailQuestions")
							.append("<div id=\"divQuestion_empty_question\">There is no question yet, please use [Create Question] to create one now</div>");

						} // else

						$('#divSessionDetail').show();
					}, // success
					error : function(jqXHR, textStatus, errorThrown) {
						NANESPACE_QA_SESSION.redirectUser;
					} // error
				});
	},
	showUserID : function() {
		$.ajax({
			type : 'GET',
			url : 'rest/user',
			contentType : 'application/json',
			dataType : 'json',
			success : function(data) {

				$("#span_user_name").html(
						" " + data.firstName + " " + data.lastName);
				// NANESPACE_QA_SESSION.storeCookie('QA_SESSION_UI_COOKIE' ,
				// data);
			}, // success
			error : function(jqXHR, textStatus, errorThrown) {
				NANESPACE_QA_SESSION.redirectUser;
			}
		});
	}, // showUserID:function
	storeCookie : function(cookieName, object) {
		var cookie = [ name, '=', JSON.stringfy(object), '; domain=.',
				window.location.host.toString, '; path=/controller;' ].join('');
		document.cookie = cookie;
	},
	readCookie : function(cookieName) {
		var object = document.cookie.match(new RegExp(cookieName + '=([^;]+)'));
		object && (object = JSON.parse(object[1]));
		return object;
	},
	deleteCookie : function(cookieName) {
		document.cookie = [ cookieName,
				'=; expires=Thu, 01-Jan-1970 00:00:01 GMT; domain=.',
				window.location.host.toString, '; path=/controller;' ].join('');
	},
	createSession : function() {

		var sessionObject = new Object();

		sessionObject.sessionTopic = $("#sessionTopic").val();
		sessionObject.sessionDescription = $("#sessionDescription").val();
		sessionObject.sessionMaxQuestion = $("#sessionMaxQuestion").val();
		sessionObject.sessionStatus = $("#sessionStatus option:selected")
				.text();

		$
				.ajax({
					type : 'POST',
					url : 'rest/session',
					contentType : 'application/json',
					dataType : 'json',
					data : JSON.stringify(sessionObject),
					success : function(data) {
						$("#tbodySession")
								.append(
										"<tr id = \"tbtrSession_"
												+ data.sessionId
												+ "\"><td>"
												+ data.sessionId.substring(0,
														10)
												+ "</td><td>"
												+ data.sessionTopic.substring(
														0, 30)
												+ "</td><td>"
												+ data.sessionStatus
												+ "</td><td>HOST</td><td>"
												+ data.updateDate
												+ "</td><td><a href=\"#\" onClick=\"NANESPACE_QA_SESSION.showSessionDetail('"
												+ data.sessionId
												+ "')\"><span class=\"glyphicon glyphicon-th-list\"></span></a> <a href=\"#\" onClick=\"NANESPACE_QA_SESSION.deleteSessionConfirm('"
												+ data.sessionId
												+ "')\"><span class=\"glyphicon glyphicon-trash\"></span></a></td></tr>");
						$('#modalNewSession').modal('hide');

						NANESPACE_QA_SESSION.addSessionAlert("SUCCESS",
								"The session is created now.");
					}, // success
					error : function(jqXHR, textStatus, errorThrown) {

						NANESPACE_QA_SESSION
								.addSessionAlert("FAILURE",
										"There is something wrong with backend, please try again later.");
					}
				});
	}, // showSession:function
	addSessionAlert : function(alertType, alertMessage) {
		var alertTypeCSS = "alert-danger";

		if (alertType == "SUCCESS") {
			alertTypeCSS = "alert-success";
		} // if

		$("#divSessionAlertList")
				.html(
						"<div id=\"divSessionAlert\" class=\"alert alert-dismissible "
								+ alertTypeCSS
								+ "\" role=\"alert\"><button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button><strong>"
								+ alertType + ": </strong>" + alertMessage
								+ "</div>");

	}, // addSessionAlert : function(alertType, alertMessage)
	saveExistSession : function(sessionId) {
		var sessionObject = new Object();
		sessionObject.sessionTopic = $("#existSessionSessionTopic").val();
		sessionObject.sessionDescription = $("#existSessionSessionDescription")
				.val();
		sessionObject.sessionMaxQuestion = $("#existSessionSessionMaxQuestion")
				.val();
		sessionObject.sessionStatus = $(
				"#existSessionSessionStatus option:selected").text();

		$
				.ajax({
					type : 'PUT',
					url : 'rest/session/' + sessionId,
					contentType : 'application/json',
					dataType : 'json',
					data : JSON.stringify(sessionObject),
					success : function(data) {

						NANESPACE_QA_SESSION.addSessionAlert("SUCCESS",
								"The session is saved now.");
					}, // success
					error : function(jqXHR, textStatus, errorThrown) {

						NANESPACE_QA_SESSION
								.addSessionAlert("FAILURE",
										"There is something wrong with backend, please try again later.");
					}
				});
	}, // saveExistSession
	deleteSessionConfirm : function(sessionId) {
		$('#modalConfirmDialogLabel').html("Delete Session");
		$('#modalConfirmDialogBody')
				.html(
						"Are you sure you want to delete the session entry? All session info, questions and answers will be lost.");
		$('#modalConfirmDialogButtonConfirm').attr("onClick",
				"NANESPACE_QA_SESSION.deleteSession('" + sessionId + "');");
		$('#modalConfirmDialog').modal('show');
	},
	deleteSession : function(sessionId) {

		$
				.ajax({
					type : 'DELETE',
					url : 'rest/session/' + sessionId,
					contentType : 'application/json',
					dataType : 'json',
					success : function(data) {
						$("#tbtrSession_" + sessionId).remove();

						NANESPACE_QA_SESSION.addSessionAlert("SUCCESS",
								"The session is removed now.");

						$('#modalConfirmDialog').modal('hide');
					}, // success
					error : function(jqXHR, textStatus, errorThrown) {

						NANESPACE_QA_SESSION
								.addSessionAlert("FAILURE",
										"There is something wrong with backend, please try again later.");
					}
				});
	}, // deleteSession : function()
	createQuestion : function(sessionId) {
		var questionObject = new Object();
		questionObject.questionStatus = "OPEN";
		questionObject.questionContent = $("#sessionQuestionContent").val();

		$
				.ajax({
					type : 'POST',
					url : 'rest/session/' + sessionId + "/question",
					contentType : 'application/json',
					data : JSON.stringify(questionObject),
					dataType : 'json',
					success : function(data) {

						NANESPACE_QA_SESSION.showSessionDetail(data.session.sessionId);
						NANESPACE_QA_SESSION.addSessionAlert("SUCCESS",
								"The question is added now.");

					}, // success
					error : function(jqXHR, textStatus, errorThrown) {
						alertMessage = "";
						if (jqXHR.status == 403)
						{
							alertMessage = "This operation is not allowed"
						}  // if
						else
						{
							alertMessage = "There is something wrong with backend, please try again later."
						}  // else
						NANESPACE_QA_SESSION
						.addSessionAlert("FAILURE", alertMessage);
					}
				});
	}, // deleteSession : function()
	deleteQuestion : function(sessionId, questionId) {

		$
				.ajax({
					type : 'DELETE',
					url : 'rest/session/' + sessionId + "/question/"
							+ questionId,
					contentType : 'application/json',
					dataType : 'json',
					success : function(data) {
						$("#divQuestion_" + questionId).remove();

						NANESPACE_QA_SESSION.addSessionAlert("SUCCESS",
								"The question is removed now.");

					}, // success
					error : function(jqXHR, textStatus, errorThrown) {
						alertMessage = "";
						if (jqXHR.status == 403)
						{
							alertMessage = "This operation is not allowed"
						}  // if
						else
						{
							alertMessage = "There is something wrong with backend, please try again later."
						}  // else
						NANESPACE_QA_SESSION
						.addSessionAlert("FAILURE", alertMessage);
					}
				});
	}, // deleteSession : function()
	displayAllSession : function(sessionId) {
	}, // displayAllSession
	logotUser : function() {
		$
				.ajax({
					type : 'POST',
					url : 'rest/user/logout',
					contentType : 'application/json',
					dataType : 'json',
					success : function(data) {
						NANESPACE_QA_SESSION.redirectUser;
					}, // success
					error : function(jqXHR, textStatus, errorThrown) {
						alertMessage = "";
						if (jqXHR.status == 403)
						{
							alertMessage = "This operation is not allowed"
						}  // if
						else
						{
							alertMessage = "There is something wrong with backend, please try again later."
						}  // else
						NANESPACE_QA_SESSION
						.addSessionAlert("FAILURE", alertMessage);
					}
				});
	}, // showUserID:function
	redirectUser : function() {
		window.location.replace(window.location.href);
	} // redirectUser : function
}

NANESPACE_QA_SESSION.showUserID();
NANESPACE_QA_SESSION.showSessionList();
