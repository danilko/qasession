var NANESPACE_QA_SESSION = {
	showSessionList : function() {

		$("#divSessionDetail").hide();
		$("#divSessionList").hide();
		$.ajax({
			type : 'GET',
			url : 'rest/user',
			contentType : 'application/json',
			dataType : 'json',
			success : function(data) {
				$.ajax({
					type : 'GET',
					url : 'rest/qasession/',
					contentType : 'application/json',
					dataType : 'json',
					success : function(data) {
						$("#tbodySession").empty();
						$.each(data, function(i, qasession) {
							NANESPACE_QA_SESSION
									.addNewSessionRecordToTable(qasession);
						});
						$("#divSessionList").show();

					}, // success
					error : function(jqXHR, textStatus, errorThrown) {
						NANESPACE_QA_SESSION.redirectUser;
					}
				});
			}, // success
			error : function(jqXHR, textStatus, errorThrown) {
				NANESPACE_QA_SESSION.redirectUser;
			}
		});
	},
	addNewSessionRecordToTable : function(qasession) {
		$("#tbodySession")
				.append(
						"<tr id = \"tbtrSession_"
								+ qasession.qasessionId
								+ "\"><td>"
								+ qasession.qasessionId.substring(0, 10)
								+ "</td><td>"
								+ qasession.qasessionTopic.substring(0, 30)
								+ "</td><td>"
								+ qasession.qasessionStatus
								+ "</td><td>"
								+ qasession.updateTimestamp
								+ " UTC</td><td><a href=\"#\" onClick=\"NANESPACE_QA_SESSION.showSessionDetail('"
								+ qasession.qasessionId
								+ "')\"><span class=\"glyphicon glyphicon-th-list\"></span></a>  <a href=\"#\" onClick=\"NANESPACE_QA_SESSION.deleteSessionConfirm('"
								+ qasession.qasessionId
								+ "')\"><span class=\"glyphicon glyphicon-trash\"></span></a></td></tr>");
	},
	findUserTranslate : function(userId) {
		var lUserTranslate = null;

		$.ajax({
			type : 'GET',
			url : 'rest/user/_all',
			contentType : 'application/json',
			dataType : 'json',
			async : false,
			success : function(data) {
				$.each(data, function(i, userTranslate) {
					if (userTranslate.userId == userId) {
						lUserTranslate = userTranslate;
					}
				});
			}
		});

		return lUserTranslate;
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
							$.each(data.questions, function(i, question) {
								NANESPACE_QA_SESSION.showQuestion(question);
							});
						} // if
						else {
							$("#divSessionDetailQuestions")
									.append(
											"<div id=\"divQuestion_empty_question\">There is no question yet, please use [Create Question] to create one now</div>");

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

				$("#span_user_name").html(" " + data.name);
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
					success : function(qasession) {
						NANESPACE_QA_SESSION
								.addNewSessionRecordToTable(qasession);

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
		sessionObject.qasessionDescription = $(
				"#existSessionSessionDescription").val();
		sessionObject.qasessionMaxQuestion = $(
				"#existSessionSessionMaxQuestion").val();
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
	showQuestion : function(question) {
		if (question.answers.length > 0) {
			questionAnswerStatus = "Answered";
		} else {
			questionAnswerStatus = "No Answer";
		}

		if (!$("#divQuestion_" + question.questionId).length) {
			$("#divSessionDetailQuestions").append(
					"<div id=\"divQuestion_" + question.questionId
							+ "\"></div>");
		} else {
			$("#divQuestion_" + question.questionId).empty();
		}

		questionSection = "<span class=\"label label-primary\">Question</span> <span class=\"label label-info\">"
				+ question.questionStatus
				+ "</span> <span class=\"label label-info\">"
				+ questionAnswerStatus
				+ "</span><br/><br/><div id='divShowQuestion_"
				+ question.questionId
				+ "'><blockquote id='blockquoteShowQuestion_"
				+ question.questionId
				+ "' onClick=\"NANESPACE_QA_SESSION.editQuestion('"
				+ question.questionId
				+ "');\">"
				+ question.questionContent
				+ "<footer>Last Update On "
				+ question.updateTimestamp
				+ " UCT By "
				+ NANESPACE_QA_SESSION.findUserTranslate(question.updatedBy).name
				+ "</footer></div><div style=\"display: none;\" id='divEditQuestion_"
				+ question.questionId
				+ "'><textarea id='textareaEditQuestion_"
				+ question.questionId
				+ "' class=\"form-control\" rows=\"5\">"
				+ question.questionContent
				+ "</textarea></div><div class=\"text-right\">";

		if (question.answers.length == 0) {
			questionSection = questionSection
					+ "<a href=\"#\" id=\"ahrefCreateAnswer_"
					+ question.questionId
					+ "\" onClick=\"NANESPACE_QA_SESSION.createAnswer('"
					+ question.questionId
					+ "');\"><span class=\"glyphicon glyphicon-console\"></a>";
		}

		questionSection = questionSection
				+ " <a href=\"#\" id=\"ahrefEditQuestion_"
				+ question.questionId
				+ "\"  onClick=\"NANESPACE_QA_SESSION.editQuestion('"
				+ question.questionId
				+ "');\"><span class=\"glyphicon glyphicon-pencil\"></a>";

		questionSection = questionSection
				+ " <a href=\"#\" style=\"display : none;\" id=\"ahrefSaveQuestion_"
				+ question.questionId
				+ "\"  onClick=\"NANESPACE_QA_SESSION.saveQuestion('"
				+ question.qasessionId + "', '" + question.questionId
				+ "');\"><span class=\"glyphicon glyphicon-floppy-saved\"></a>";

		questionSection = questionSection
				+ " <a href=\"#\" style=\"display : none;\" id=\"ahrefCancelEditQuestion_"
				+ question.questionId
				+ "\"  onClick=\"NANESPACE_QA_SESSION.cancelEditQuestion('"
				+ question.questionId
				+ "');\"><span class=\"glyphicon glyphicon-floppy-remove\"></a>";

		questionSection = questionSection
				+ "<a href=\"#\" id=\"ahrefDeleteQuestion_"
				+ question.questionId
				+ "\" onClick=\"NANESPACE_QA_SESSION.deleteQuestion('"
				+ question.qasessionId
				+ "', '"
				+ question.questionId
				+ "');\"><span class=\"glyphicon glyphicon-trash\"></span></a></div>";

		if (question.answers.length > 0) {
			$
					.each(
							question.answers,
							function(i, answer) {

								questionSection = questionSection
										+ "<div id = \"divAnswer_"
										+ answer.answerId
										+ "\"><span class=\"label label-primary\">Answer</span><br/><br/><div id='divShowAnswer_"
										+ answer.answerId
										+ "'><blockquote id='blockquoteShowAnswer_"
										+ answer.answerId
										+ "' onClick=\"NANESPACE_QA_SESSION.editAnswer('"
										+ answer.answerId
										+ "');\">"
										+ answer.answerContent
										+ "<footer>Last Update On "
										+ answer.updateTimestamp
										+ " UCT By "
										+ NANESPACE_QA_SESSION
												.findUserTranslate(answer.updatedBy).name
										+ "</footer></blockquote></div><div style=\"display: none;\" id='divEditAnswer_"
										+ answer.answerId
										+ "'><textarea id='textareaEditAnswer_"
										+ answer.answerId
										+ "' class=\"form-control\" rows=\"5\">"
										+ answer.answerContent
										+ "</textarea></div><div class=\"text-right\">";

								questionSection = questionSection
										+ " <a href=\"#\" id=\"ahrefEditAnswer_"
										+ answer.answerId
										+ "\"  onClick=\"NANESPACE_QA_SESSION.editAnswer('"
										+ answer.answerId
										+ "');\"><span class=\"glyphicon glyphicon-pencil\"></a>";

								questionSection = questionSection
										+ " <a href=\"#\" style=\"display : none;\" id=\"ahrefSaveAnswer_"
										+ answer.answerId
										+ "\"  onClick=\"NANESPACE_QA_SESSION.saveAnswer('"
										+ question.qasessionId
										+ "', '"
										+ question.questionId
										+ "', '"
										+ answer.answerId
										+ "');\"><span class=\"glyphicon glyphicon-floppy-saved\"></a>";

								questionSection = questionSection
										+ " <a href=\"#\" style=\"display : none;\" id=\"ahrefCancelEditAnswer_"
										+ answer.answerId
										+ "\"  onClick=\"NANESPACE_QA_SESSION.cancelEditAnswer('"
										+ answer.answerId
										+ "');\"><span class=\"glyphicon glyphicon-floppy-remove\"></a>";

								questionSection = questionSection
										+ "<a href=\"#\" id=\"ahrefDeleteAnswer_"
										+ answer.answerId
										+ "\" onClick=\"NANESPACE_QA_SESSION.deleteAnswer('"
										+ question.qasessionId
										+ "', '"
										+ question.questionId
										+ "', '"
										+ answer.answerId
										+ "');\"><span class=\"glyphicon glyphicon-trash\"></span></a></div></div>";
							});
		} // if
		else {
			questionSection = questionSection
					+ "<div style=\"display: none;\" id='divNewAnswer_"
					+ question.questionId
					+ "'><div><textarea id='textareaNewAnswer_"
					+ question.questionId
					+ "' class=\"form-control\" rows=\"5\" placeholder=\"Enter answer content\"></textarea></div><div class=\"text-right\">";

			questionSection = questionSection
					+ " <a href=\"#\" id=\"ahrefSaveNewAnswer_"
					+ question.qasessionId
					+ "\"  onClick=\"NANESPACE_QA_SESSION.saveNewAnswer('"
					+ question.qasessionId
					+ "', '"
					+ question.questionId
					+ "');\"><span class=\"glyphicon glyphicon-floppy-saved\"></a>";

			questionSection = questionSection
					+ " <a href=\"#\" id=\"ahrefCancelNewAnswer_"
					+ question.qasessionId
					+ "\"  onClick=\"NANESPACE_QA_SESSION.cancelNewAnswer('"
					+ question.qasessionId
					+ "');\"><span class=\"glyphicon glyphicon-floppy-remove\"></a></div>";
		}

		questionSection = questionSection + "</div>";

		$("#divQuestion_" + question.questionId).append(questionSection);
	},
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

						NANESPACE_QA_SESSION
								.showSessionDetail(data.qasessionId);
						NANESPACE_QA_SESSION.addSessionAlert("SUCCESS",
								"The question is added now.");

					}, // success
					error : function(jqXHR, textStatus, errorThrown) {
						alertMessage = "";
						if (jqXHR.status == 403) {
							alertMessage = "This operation is not allowed"
						} // if
						else {
							alertMessage = "There is something wrong with backend, please try again later."
						} // else
						NANESPACE_QA_SESSION.addSessionAlert("FAILURE",
								alertMessage);
					}
				});
	}, // createQuestion : function()
	saveQuestion : function(qasessionId, questionId) {

		var questionObject = new Object();
		questionObject.questionStatus = "OPEN";
		questionObject.questionContent = $(
				"#textareaEditQuestion_" + questionId).val();

		$
				.ajax({
					type : 'PUT',
					url : 'rest/qasession/' + qasessionId + "/question/"
							+ questionId,
					contentType : 'application/json',
					data : JSON.stringify(questionObject),
					dataType : 'json',
					success : function(question) {

						NANESPACE_QA_SESSION.showQuestion(question);
						NANESPACE_QA_SESSION.addSessionAlert("SUCCESS",
								"The question is updated now.");
					}, // success
					error : function(jqXHR, textStatus, errorThrown) {
						alertMessage = "";
						if (jqXHR.status == 403) {
							alertMessage = "This operation is not allowed"
						} // if
						else {
							alertMessage = "There is something wrong with backend, please try again later."
						} // else
						NANESPACE_QA_SESSION.addSessionAlert("FAILURE",
								alertMessage);
					}
				});
	},
	editQuestion : function(questionId) {
		$("#divShowQuestion_" + questionId).hide();
		$("#divEditQuestion_" + questionId).show();

		$("#ahrefEditQuestion_" + questionId).hide();
		$("#ahrefCancelEditQuestion_" + questionId).show();
		$("#ahrefSaveQuestion_" + questionId).show();

	},
	cancelEditQuestion : function(questionId) {

		$("#divEditQuestion_" + questionId).hide();
		$("#divShowQuestion_" + questionId).show();

		$("#ahrefEditQuestion_" + questionId).show();
		$("#ahrefCancelEditQuestion_" + questionId).hide();
		$("#ahrefSaveQuestion_" + questionId).hide();
	},
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
						if (jqXHR.status == 403) {
							alertMessage = "This operation is not allowed"
						} // if
						else {
							alertMessage = "There is something wrong with backend, please try again later."
						} // else
						NANESPACE_QA_SESSION.addSessionAlert("FAILURE",
								alertMessage);
					}
				});
	}, // deleteSession : function()
	createAnswer : function(questionId) {

		$("#divNewAnswer_" + questionId).show();
		$("#ahrefCreateAnswer_" + questionId).hide();
	}, // createQuestion : function()
	saveNewAnswer : function(qasessionId, questionId) {
		var answerObject = new Object();
		answerObject.answerContent = $("#textareaNewAnswer_" + questionId)
				.val();

		$
				.ajax({
					type : 'POST',
					url : 'rest/qasession/' + qasessionId + "/question/"
							+ questionId + "/answer",
					contentType : 'application/json',
					data : JSON.stringify(answerObject),
					dataType : 'json',
					async : false,
					error : function(jqXHR, textStatus, errorThrown) {
						alertMessage = "";
						if (jqXHR.status == 403) {
							alertMessage = "This operation is not allowed"
						} // if
						else {
							alertMessage = "There is something wrong with backend, please try again later."
						} // else
						NANESPACE_QA_SESSION.addSessionAlert("FAILURE",
								alertMessage);
					}
				});

		$
				.ajax({
					type : 'GET',
					url : 'rest/qasession/' + qasessionId + "/question/"
							+ questionId,
					contentType : 'application/json',
					dataType : 'json',
					success : function(question) {
						NANESPACE_QA_SESSION.showQuestion(question);
						NANESPACE_QA_SESSION.addSessionAlert("SUCCESS",
								"The answer is updated now.");
					},
					error : function(jqXHR, textStatus, errorThrown) {
						alertMessage = "";
						if (jqXHR.status == 403) {
							alertMessage = "This operation is not allowed"
						} // if
						else {
							alertMessage = "There is something wrong with backend, please try again later."
						} // else
						NANESPACE_QA_SESSION.addSessionAlert("FAILURE",
								alertMessage);
					}
				});
	}, // createQuestion : function()
	saveAnswer : function(qasessionId, questionId, answerId) {
		var answerObject = new Object();
		answerObject.answerContent = $("#textareaEditAnswer_" + answerId).val();

		$
				.ajax({
					type : 'PUT',
					url : 'rest/qasession/' + qasessionId + "/question/"
							+ questionId + "/answer/" + answerId,
					contentType : 'application/json',
					data : JSON.stringify(answerObject),
					dataType : 'json',
					async : false,
					error : function(jqXHR, textStatus, errorThrown) {
						alertMessage = "";
						if (jqXHR.status == 403) {
							alertMessage = "This operation is not allowed"
						} // if
						else {
							alertMessage = "There is something wrong with backend, please try again later."
						} // else
						NANESPACE_QA_SESSION.addSessionAlert("FAILURE",
								alertMessage);
					}
				});
		
		$
		.ajax({
			type : 'GET',
			url : 'rest/qasession/' + qasessionId + "/question/"
					+ questionId,
			contentType : 'application/json',
			dataType : 'json',
			success : function(question) {
				NANESPACE_QA_SESSION.showQuestion(question);
				NANESPACE_QA_SESSION.addSessionAlert("SUCCESS",
						"The answer is saved now.");
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertMessage = "";
				if (jqXHR.status == 403) {
					alertMessage = "This operation is not allowed"
				} // if
				else {
					alertMessage = "There is something wrong with backend, please try again later."
				} // else
				NANESPACE_QA_SESSION.addSessionAlert("FAILURE",
						alertMessage);
			}
		});
	},
	cancelNewAnswer : function(questionId) {
		$("#divNewAnswer_" + questionId).hide();
		$("#textareaNewAnswer_" + questionId).val("");
		$("#ahrefCreateAnswer_" + questionId).show();
	},
	editAnswer : function(answerId) {
		$("#divShowAnswer_" + answerId).hide();
		$("#divEditAnswer_" + answerId).show();

		$("#ahrefEditAnswer_" + answerId).hide();
		$("#ahrefCancelEditAnswer_" + answerId).show();
		$("#ahrefSaveAnswer_" + answerId).show();

	},
	cancelEditAnswer : function(answerId) {

		$("#divEditAnswer_" + answerId).hide();
		$("#divShowAnswer_" + answerId).show();

		$("#ahrefEditAnswer_" + answerId).show();
		$("#ahrefCancelEditAnswer_" + answerId).hide();
		$("#ahrefSaveAnswer_" + answerId).hide();
	},
	deleteAnswer : function(qasessionId, questionId, answerId) {

		$
				.ajax({
					type : 'DELETE',
					url : 'rest/qasession/' + qasessionId + "/question/"
							+ questionId + '/answer/' + answerId,
					contentType : 'application/json',
					dataType : 'json',
					success : function(data) {
						$("#divAnswer_" + questionId).remove();

						NANESPACE_QA_SESSION.addSessionAlert("SUCCESS",
								"The question is removed now.");

					}, // success
					error : function(jqXHR, textStatus, errorThrown) {
						alertMessage = "";
						if (jqXHR.status == 403) {
							alertMessage = "This operation is not allowed"
						} // if
						else {
							alertMessage = "There is something wrong with backend, please try again later."
						} // else
						NANESPACE_QA_SESSION.addSessionAlert("FAILURE",
								alertMessage);
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
						if (jqXHR.status == 403) {
							alertMessage = "This operation is not allowed"
						} // if
						else {
							alertMessage = "There is something wrong with backend, please try again later."
						} // else
						NANESPACE_QA_SESSION.addSessionAlert("FAILURE",
								alertMessage);
					}
				});
	}, // showUserID:function
	redirectUser : function() {
		window.location.replace(window.location.href);
	} // redirectUser : function
}

NANESPACE_QA_SESSION.showUserID();
NANESPACE_QA_SESSION.showSessionList();
