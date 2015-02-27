var NANESPACE_QA_SESSOPM = {
		showUserID: function() {
			$
			.ajax({
				type : 'GET',
				url : 'rest/user',
				dataType : 'json',
				success : function(data) {
					$("#li_userinfo").html(data.first_name + " " + data.last_name); 
				}  // success
				});
		} // showUserID:function

}

NANESPACE_QA_SESSOPM.showUserID();