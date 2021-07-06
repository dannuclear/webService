function DeleteFunction(message) {
	if (confirm(message))
		return true;
	else {
		return false;
	}
}

$.extend(true, $.fn.dataTable.defaults, {
	ajax: {
		type: 'POST',
		contentType: 'application/json',
		data: function(d) {
			return JSON.stringify(d);
		}
	},
	searching: true,
	ordering: true,
	processing: true,
	serverSide: true,
	dom: '<"row"<"toolbar col-sm-12 col-md-6"><"col-sm-12 col-md-6"fb>>'
		+ '<"row"<"col-sm-12"tr>>'
		+ '<"row"<"col-sm-12 col-md-5"i><"col-sm-12 col-md-2"l><"col-sm-12 col-md-5"p>>'
});
