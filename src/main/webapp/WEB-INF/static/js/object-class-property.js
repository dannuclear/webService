$(function() {
	$('#property').autocomplete({
		appendTo: $('#property-update-form'),
		source: function(request, response) {
			$.ajax({
				url: '/webService/api/v1/properties',
				data: { search: request.term },
				success: function(data) {
					response($.map(data._embedded.propertyList, function(item) {
						return {
							label: item.name,
							value: item.id
						}
					}))
				}
			})
		},
		select: function(event, ui) {
			this.value = ui.item.label;
			$('#property-id').val(ui.item.value);
			return false;
		}
	}
	);

	// $('#object-class-property-document-table').on('click', 'td.editor-delete', function(e) {
	// 	e.preventDefault();
	// 	$('#modalDialog').find('.modal-body').load($(this).find('a').attr('href'), function() {
	// 		alert('ok');
	// 	});
	// });
})

