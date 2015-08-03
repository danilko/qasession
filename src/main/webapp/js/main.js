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
									url : 'rest/qasession/',
									contentType : 'application/json',
									dataType : 'json',
									success : function(data) {
										$("#tbodySession").empty();
										$
												.each(
														data,
														function(i, qasession) {

															$("#tbodySession")
																	.append(
																			"<tr id = \"tbtrSession_"
																					+ qasession.qasessionId
																					+ "\"><td>"
																					+ qasession.qasessionId
																							.substring(
																									0,
																									10)
																					+ "</td><td>"
																					+ qasession.qasessionTopic
																							.substring(
																									0,
																									30)
																					+ "</td><td>"
																					+ qasession.qasessionStatus
																					+ "</td><td>"
																					+ qasession.updateDate
																					+ "</td><td><a href=\"#\" onClick=\"NANESPACE_QA_SESSION.showSessionDetail('"
																					+ qasession.qasessionId
																					+ "')\"><span class=\"glyphicon glyphicon-th-list\"></span></a>  <a href=\"#\" onClick=\"NANESPACE_QA_SESSION.deleteSessionConfirm('"
																					+ qasession.qasessionId
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
	showSessionDetail : function(qasessionId) {

		$("#divSessionDetail").hide();
		$("#divSessionList").hide();

		$
				.ajax({
					type : 'GET',
					url : 'rest/qasession/' + qasessionId,
					contentType : 'application/json',
					dataType : 'json',
					success : function(data) {
						$("#existSessionSessionTopic").val(data.qasessionTopic);
						$("#existSessionSessionDescription").val(
								data.qasessionDescription);
						$("#existSessionSessionMaxQuestion").val(
								data.qasessionMaxQuestion);
						$("#existSessionSessionStatus option:selected").text(
								data.qasessionStatus);

						$("#buttonExistSessionSaveExistSession").attr(
								"onClick",
								"NANESPACE_QA_SESSION.saveExistSession('"
										+ data.qasessionId + "');")

						$("#buttonSessionCreateQuestion").attr(
								"onClick",
								"NANESPACE_QA_SESSION.createQuestion('"
										+ data.qasessionId + "');")
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
																		+ "</p><p class=\"text-right\"> <a href=\"#\" onClick=\"NANESPACE_QA_SESSION.createAnswer('"
																		+ question.qasessionId
																		+ "', '"
																		+ question.questionId
																		+ "');\"><span class=\"glyphicon glyphicon-console\"></a>  <a href=\"#\" onClick=\"NANESPACE_QA_SESSION.editQuestion('"
																		+ question.qasessionId
																		+ "', '"
																		+ question.questionId
																		+ "');\"><span class=\"glyphicon glyphicon-th-list\">  <a href=\"#\" onClick=\"NANESPACE_QA_SESSION.deleteQuestion('"
																		+ question.qasessionId
																		+ "', '"
																		+ question.questionId
																		+ "');\"><span class=\"glyphicon glyphicon-trash\"></span></a></p></div>");
												if (question.answer != null) {
													$(
															"#divSessionDetailQuestions")
															.append(
																	"<blockquote><span class=\"label label-success\">Answer</span> <span class=\"label label-info\">Created By " + question.answer.createdBy + "</span><p> "
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
						" " + data.name);
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

		var qasessionObject = new Object();

		qasessionObject.qasessionTopic = $("#sessionTopic").val();
		qasessionObject.qasessionDescription = $("#sessionDescription").val();
		qasessionObject.qasessionMaxQuestion = $("#sessionMaxQuestion").val();
		qasessionObject.qasessionStatus = $("#sessionStatus option:selected")
				.text();

		$
				.ajax({
					type : 'POST',
					url : 'rest/qasession',
					contentType : 'application/json',
					dataType : 'json',
					data : JSON.stringify(qasessionObject),
					success : function(data) {
						$("#tbodySession")
								.append(
										"<tr id = \"tbtrSession_"
												+ data.qasessionId
												+ "\"><td>"
												+ data.qasessionId.substring(0,
														10)
												+ "</td><td>"
												+ data.qasessionTopic.substring(
														0, 30)
												+ "</td><td>"
												+ data.qasessionStatus
												+ "</td><td>HOST</td><td>"
												+ data.update_timestamp
												+ "</td><td><a href=\"#\" onClick=\"NANESPACE_QA_SESSION.showSessionDetail('"
												+ data.qasessionId
												+ "')\"><span class=\"glyphicon glyphicon-th-list\"></span></a> <a href=\"#\" onClick=\"NANESPACE_QA_SESSION.deleteSessionConfirm('"
												+ data.qasessionId
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
	saveExistSession : function(qasessionId) {
		var sessionObject = new Object();
		sessionObject.qasessionTopic = $("#existSessionSessionTopic").val();
		sessionObject.qasessionDescription = $("#existSessionSessionDescription")
				.val();
		sessionObject.qasessionMaxQuestion = $("#existSessionSessionMaxQuestion")
				.val();
		sessionObject.qasessionStatus = $(
				"#existSessionSessionStatus option:selected").text();

		$
				.ajax({
					type : 'PUT',
					url : 'rest/qasession/' + qasessionId,
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
	deleteSessionConfirm : function(qasessionId) {
		$('#modalConfirmDialogLabel').html("Delete Session");
		$('#modalConfirmDialogBody')
				.html(
						"Are you sure you want to delete the session entry? All session info, questions and answers will be lost.");
		$('#modalConfirmDialogButtonConfirm').attr("onClick",
				"NANESPACE_QA_SESSION.deleteSession('" + qasessionId + "');");
		$('#modalConfirmDialog').modal('show');
	},
	deleteSession : function(qasessionId) {

		$
				.ajax({
					type : 'DELETE',
					url : 'rest/qasession/' + qasessionId,
					contentType : 'application/json',
					dataType : 'json',
					success : function(data) {
						$("#tbtrSession_" + qasessionId).remove();

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
	createQuestion : function(qasessionId) {
		var questionObject = new Object();
		questionObject.questionStatus = "OPEN";
		questionObject.questionContent = $("#sessionQuestionContent").val();

		$
				.ajax({
					type : 'POST',
					url : 'rest/qasession/' + qasessionId + "/question",
					contentType : 'application/json',
					data : JSON.stringify(questionObject),
					dataType : 'json',
					success : function(data) {

						NANESPACE_QA_SESSION.showSessionDetail(data.qasessionId);
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
	}, // createQuestion : function()
	createAnswer : function(qasessionId, questionId) {
		var answerObject = new Object();
		answerObject.answerContent = $("#sessionQuestionAnswerContent").val();

		$
				.ajax({
					type : 'POST',
					url : 'rest/qasession/' + qasessionId + "/question/" + questionId,
					contentType : 'application/json',
					data : JSON.stringify(answerObject),
					dataType : 'json',
					success : function(data) {

						NANESPACE_QA_SESSION.showSessionDetail(data.qasessionId);
						NANESPACE_QA_SESSION.addSessionAlert("SUCCESS",
								"The answer is added now.");

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
	}, // createQuestion : function()
	deleteQuestion : function(qasessionId, questionId) {

		$
				.ajax({
					type : 'DELETE',
					url : 'rest/qasession/' + qasessionId + "/question/"
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
	displayAllSession : function(qasessionId) {
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
