/**
 * 
 */
function getBirds() {
	   $.ajax({
		   url: "InfoServlet",
			type: 'GET',
			dataType: 'text',
			error: function (jqXHR, exception) {
	            var msg = '';
	            if (jqXHR.status === 0) {
	                msg = 'Not connect.\n Verify Network.';
	            } else if (jqXHR.status == 404) {
	                msg = 'Requested page not found. [404]';
	            } else if (jqXHR.status == 500) {
	                msg = 'Internal Server Error [500].';
	            } else if (exception === 'parsererror') {
	                msg = 'Requested JSON parse failed.';
	            } else if (exception === 'timeout') {
	                msg = 'Time out error.';
	            } else if (exception === 'abort') {
	                msg = 'Ajax request aborted.';
	            } else {
	                msg = 'Uncaught Error.\n' + jqXHR.responseText;
	            }
	            alert(msg);
	        },
	      	success: function(data) {
	      		var myWindow = window.open("Bird List And Source", "Bird List And Source","" , "");
	      		myWindow.document.write("<!doctype html>");
	      		myWindow.document.write("<html>");
	      		myWindow.document.write("<head>");
	      		myWindow.document.write("<title>Bird List And Source</title>");
	      		myWindow.document.write("<p>Source: <a href=\"http://www.vision.caltech.edu/visipedia/CUB-200-2011.html\">Caltech-UCSD Birds-200-2011</a></p>");
	      		myWindow.document.write("</head>");
	      		myWindow.document.write("<body>");
	      		var list = data.split("\n");
	      		for(i = 0; i < list.length; i++) {
	      			myWindow.document.write(list[i] + "<br />");
	      		}
	      		myWindow.document.write("</body>");
	      		myWindow.document.write("</html>");
	      		myWindow.document.close();

	      }
	   });
	}