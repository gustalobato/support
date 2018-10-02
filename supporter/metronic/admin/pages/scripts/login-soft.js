var Login = function () {

	var handleShowHide = function () {

	        jQuery('#forget-password').click(function () {
	            jQuery('.login-form').hide();
	            jQuery('.forget-form').show();
	        });

	        jQuery('#back-btn').click(function () {
	            jQuery('.login-form').show();
	            jQuery('.forget-form').hide();
	        });

	}
    
    return {
        //main function to initiate the module
        init: function () {
        	
        	handleShowHide();
            
	       	$.backstretch([
		        "metronic/admin/pages/media/bg/1.jpg",
    		    "metronic/admin/pages/media/bg/2.jpg",
    		    "metronic/admin/pages/media/bg/3.jpg",
    		    "metronic/admin/pages/media/bg/4.jpg"
		        ], {
		          fade: 1000,
		          duration: 10000
		    });
        }

    };

}();