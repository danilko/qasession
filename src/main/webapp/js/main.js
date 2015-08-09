/**
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) Kai-Ting (Danil) Ko
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */

var NANESPACE_QA_SESSION = {
	showSessionList : function() {

		$("#divSessionDetail").hide();
		$("#divSessionList").hide();

		NANESPACE_QA_SESSION.getCurrentUserInfo();

		$.ajax({
			type : 'GET',
			url : 'rest/qasession/',
			contentType : 'application/json',
			dataType : 'json',
			success : function(data) {
				$("#tbodySession").empty();
				$.each(data, function(i, qasession) {
					NANESPACE_QA_SESSION.addNewSessionRecordToTable(qasession);
				});
				$("#divSessionList").show();

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
	createCacheProvider : function() {
		// values will be stored here
		this._cacheProvider = {};
	},
	setCacheObject : function(cacheKey, cacheObject) {
		this._cacheProvider[cacheKey] = cacheObject;
		return cacheObject;
	},
	getCacheObject : function(cacheKey) {
		return this._cacheProvider[cacheKey];
	},
	removeCacheObject : function(cacheKey) {
		delete this._cacheProvider[cacheKey];
	},
	getCurrentUserInfo : function() {
		var lCurrentUserInfo = NANESPACE_QA_SESSION.getCacheObject("userInfo");

		if (lCurrentUserInfo == null) {
			$.ajax({
				type : 'GET',
				url : 'rest/user',
				contentType : 'application/json',
				dataType : 'json',
				async : false,
				success : function(data) {
					lCurrentUserInfo = data;
					NANESPACE_QA_SESSION.setCacheObject("userInfo",
							lCurrentUserInfo);
				}, // success
				error : function(jqXHR, textStatus, errorThrown) {
					NANESPACE_QA_SESSION.redirectUser;
				} // error
			});
		} // if

		return lCurrentUserInfo;
	},
	getUserTranslate : function(userId) {
		var lUserTranslates = NANESPACE_QA_SESSION
				.getCacheObject("userTranslates");

		if (lUserTranslates == null) {
			$.ajax({
				type : 'GET',
				url : 'rest/user/_all',
				contentType : 'application/json',
				dataType : 'json',
				async : false,
				success : function(data) {
					lUserTranslates = data;
					NANESPACE_QA_SESSION.setCacheObject("userTranslates",
							lUserTranslates);
				} // success
			});
		} // if
		var lUserTranslate = null;
		$.each(lUserTranslates, function(i, userTranslate) {
			// Find the target userTranslate, break the loop
			if (userTranslate.userId == userId) {
				lUserTranslate = userTranslate;
			}
		});

		return lUserTranslate;
	},
	getAttendeeByAttendeeId : function(userId) {
		var attendees = NANESPACE_QA_SESSION.getCacheObject("session").attendees;
		var lAttendee = null;

		$.each(attendees, function(i, attendee) {
			if (attendee.userId == userId) {
				lAttendee = attendee;
			} // if
		});

		return lAttendee;
	},
	showSessionDetail : function(qasessionId) {

		$
				.ajax({
					type : 'GET',
					url : 'rest/qasession/' + qasessionId,
					contentType : 'application/json',
					dataType : 'json',
					success : function(data) {

						NANESPACE_QA_SESSION
								.removeCacheObject("userTranslates");
						NANESPACE_QA_SESSION.removeCacheObject("session");
						NANESPACE_QA_SESSION.setCacheObject("session", data);

						$("#existSessionSessionTopic").val(data.qasessionTopic);
						$("#existSessionSessionDescription").val(
								data.qasessionDescription);
						$("#existSessionSessionMaxQuestion").val(
								data.qasessionMaxQuestion);
						$("#existSessionSessionStatus option:selected").text(
								data.qasessionStatus);

						var currentUserAttendee = NANESPACE_QA_SESSION
								.getAttendeeByAttendeeId(NANESPACE_QA_SESSION
										.getCurrentUserInfo().userId);
						// If not HOST specific function
						if (currentUserAttendee != null
								&& currentUserAttendee.qasessionRole == "HOST") {
							$("#buttonExistSessionSaveExistSession").attr(
									"onClick",
									"NANESPACE_QA_SESSION.saveExistSession('"
											+ data.qasessionId + "');");

							$("#existSessionSessionTopic").disabled = false;
							;
							$("#existSessionSessionDescription").disabled = false;
							$("#existSessionSessionMaxQuestion").disabled = false;
							$("#existSessionSessionStatus").disabled = false;

							$("#buttonExistSessionSaveExistSession").show();
						} // if 
						else {
							$("#existSessionSessionTopic").disabled = true;
							;
							$("#existSessionSessionDescription").disabled = true;
							$("#existSessionSessionMaxQuestion").disabled = true;
							$("#existSessionSessionStatus").disabled = true;

							$("#buttonExistSessionSaveExistSession").hide();
						} // else

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

						$("#divSessionDetail").hide();
						$("#divSessionList").hide();

						$('#divSessionDetail').show();
					}, // success
					error : function(jqXHR, textStatus, errorThrown) {
						NANESPACE_QA_SESSION.redirectUser;
					} // error
				});
	},
	showUserID : function() {
		$("#span_user_name").html(
				NANESPACE_QA_SESSION.getCurrentUserInfo().name);
	}, // showUserID:function
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
		$('#modalConfirmDialogButtonConfirm').show();
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
		var currentUserAttendee = NANESPACE_QA_SESSION
				.getAttendeeByAttendeeId(NANESPACE_QA_SESSION
						.getCurrentUserInfo().userId);

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
				+ NANESPACE_QA_SESSION.getUserTranslate(question.updatedBy).name
				+ "</footer></div>";

		// HOST/Question AUthor specific function
		if ((currentUserAttendee != null && currentUserAttendee.qasessionRole == "HOST")
				|| (question.updatedBy == NANESPACE_QA_SESSION
						.getCurrentUserInfo().userId)) {
			// Display Question
			questionSection = questionSection
					+ "<div style=\"display: none;\" id='divEditQuestion_"
					+ question.questionId
					+ "'><textarea id='textareaEditQuestion_"
					+ question.questionId
					+ "' class=\"form-control\" rows=\"5\">"
					+ question.questionContent
					+ "</textarea></div><div class=\"text-right\">";

			if ((question.answers.length == 0)
					&& (currentUserAttendee != null && currentUserAttendee.qasessionRole == "HOST")) {
				questionSection = questionSection
						+ "<a href=\"#\" id=\"ahrefCreateAnswer_"
						+ question.questionId
						+ "\" onClick=\"NANESPACE_QA_SESSION.createAnswer('"
						+ question.questionId
						+ "');\"><span class=\"glyphicon glyphicon-console\"></a>";
			} // if

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
					+ question.qasessionId
					+ "', '"
					+ question.questionId
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
					+ "');\"><span class=\"glyphicon glyphicon-trash\"></span></a>";
		} // if
		questionSection = questionSection + "</div>";
		if (question.answers.length > 0) {
			$
					.each(
							question.answers,
							function(i, answer) {

								// Display Answer
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
												.getUserTranslate(answer.updatedBy).name
										+ "</footer></blockquote></div>";

								// HOST specific function
								if (currentUserAttendee != null
										&& currentUserAttendee.qasessionRole == "HOST") {
									questionSection = questionSection
											+ "<div style=\"display: none;\" id='divEditAnswer_"
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
											+ "');\"><span class=\"glyphicon glyphicon-trash\"></span></a></div>";
								} // if

								questionSection = questionSection + "</div>";
							});
		} // if
		else if (currentUserAttendee != null
				&& currentUserAttendee.qasessionRole == "HOST") {

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
	logoutUser : function() {
		$('#modalConfirmDialogLabel').html("Logout");
		$('#modalConfirmDialogBody')
				.html(
						"Please logout from your current single sign out provider (Facebook, Google, Twitter) or delete your browser session cookie to logout");
		$('#modalConfirmDialogButtonConfirm').hide();
		$('#modalConfirmDialog').modal('show');

	}, // logoutUser : function
	redirectUser : function() {
		window.location.replace(window.location.href);
	} // redirectUser : function
}

NANESPACE_QA_SESSION.createCacheProvider();

NANESPACE_QA_SESSION.showUserID();
NANESPACE_QA_SESSION.showSessionList();
